package folk.sisby.inventory_tabs.tabs;

import folk.sisby.inventory_tabs.InventoryTabs;
import folk.sisby.inventory_tabs.mixin.SignBlockEntityAccessor;
import folk.sisby.inventory_tabs.util.BlockUtil;
import net.minecraft.block.Block;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public class BlockTab implements Tab {
    public final int priority;
    public final Block block;
    public final BlockPos pos;
    public final boolean unique;
    public final Map<Identifier, BiPredicate<World, BlockPos>> preclusions;
    public List<BlockPos> multiblockPositions;
    public ItemStack itemStack;
    public Text hoverText;

    public BlockTab(World world, BlockPos pos, Map<Identifier, BiPredicate<World, BlockPos>> preclusions, int priority, boolean unique) {
        this.priority = priority;
        this.unique = unique;
        this.block = world.getBlockState(pos).getBlock();
        this.pos = pos;
        this.preclusions = preclusions;
        this.multiblockPositions = new ArrayList<>(List.of(pos));
        refreshMultiblock(world);
        refreshPreview(world);
    }

    @Override
    public void open(ClientPlayerEntity player, ClientWorld world, ScreenHandler handler, ClientPlayerInteractionManager interactionManager) {
        if (InventoryTabs.CONFIG.rotatePlayer) player.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, Vec3d.ofCenter(pos));
        interactionManager.interactBlock(player, Hand.MAIN_HAND, new BlockHitResult(Vec3d.of(pos).add(0.5D, 0.5D, 0.5D), Direction.EAST, pos, false));
    }

    @Override
    public boolean shouldBeRemoved(World world, boolean current) {
        if (!world.getBlockState(pos).getBlock().equals(block)) return true;
        refreshMultiblock(world);
        refreshPreview(world);
        if (current) return false;
        return preclusions.values().stream().anyMatch(p -> p.test(world, pos));
    }

    @Override
    public ItemStack getTabIcon() {
        return itemStack;
    }

    @Override
    public Text getHoverText() {
        return hoverText;
    }

    protected void refreshPreviewAtPos(World world, BlockPos previewPos) {
        List<ItemFrameEntity> itemFrames = world.getNonSpectatingEntities(ItemFrameEntity.class, new Box(Vec3d.of(previewPos).add(0.5D, 0.5D, 0.5D), Vec3d.of(previewPos).add(0.5D, 0.5D, 0.5D)).expand(0.6, 0.3, 0.6));
        if (!itemFrames.isEmpty()) {
            itemStack = itemFrames.get(0).getHeldItemStack();
            if (itemStack.hasCustomName()) hoverText = itemStack.getName().copy().formatted(Formatting.ITALIC);
        }
        if (world.getBlockEntity(previewPos) instanceof LockableContainerBlockEntity lcbe && lcbe.hasCustomName()) {
            hoverText = lcbe.getCustomName().copy().formatted(Formatting.ITALIC);
        }
        List<SignBlockEntity> signs = BlockUtil.getAttachedBlocks(world, previewPos, (w, p) -> w.getBlockEntity(p) instanceof SignBlockEntity sbe ? sbe : null);
        if (!signs.isEmpty()) {
            String name = Arrays.stream(((SignBlockEntityAccessor) signs.get(0)).getTexts()).map(Text::getString).filter(s -> !s.isBlank()).collect(Collectors.joining(" "));
            if (!name.isBlank()) hoverText = Text.literal(name).formatted(Formatting.ITALIC);
        }
    }

    protected void refreshPreview(World world) {
        itemStack = new ItemStack(block);
        hoverText = getDefaultHoverText(world);
        for (BlockPos multiPos : multiblockPositions) {
            refreshPreviewAtPos(world, multiPos);
        }
    }

    protected Text getDefaultHoverText(World world) {
        return block.getName();
    }

    protected void refreshMultiblock(World world) {
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (unique) {
            return other instanceof ItemTab it && Objects.equals(block.asItem(), it.stack.getItem()) ||
                    other instanceof BlockTab bt && Objects.equals(block, bt.block);
        } else {
            return other instanceof BlockTab bt && Objects.equals(multiblockPositions.get(0), bt.multiblockPositions.get(0));
        }
    }
}
