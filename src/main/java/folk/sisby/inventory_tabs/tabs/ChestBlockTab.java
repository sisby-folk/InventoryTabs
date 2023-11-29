package folk.sisby.inventory_tabs.tabs;

import folk.sisby.inventory_tabs.util.ChestUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.enums.ChestType;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Objects;

import static folk.sisby.inventory_tabs.util.ChestUtil.getOtherChestBlockPos;
import static folk.sisby.inventory_tabs.util.ChestUtil.isDouble;

public class ChestBlockTab extends BlockTab {
    public BlockPos leftPos;

    public ChestBlockTab(int priority, World world, BlockPos pos) {
        super(priority, world, pos);
        shouldBeRemoved(world, false);
    }

    @Override
    public boolean shouldBeRemoved(World world, boolean current) {
        BlockState blockState = world.getBlockState(pos);
        leftPos = blockState.contains(Properties.CHEST_TYPE) && blockState.get(ChestBlock.CHEST_TYPE) == ChestType.RIGHT ? ChestUtil.getOtherChestBlockPos(world, pos) : pos;
        return ChestBlock.isChestBlocked(world, pos) || super.shouldBeRemoved(world, current);
    }

    @Override
    public void refreshPreview(World world) {
        boolean isDouble = isDouble(world, pos);
        itemStack = new ItemStack(block);
        hoverText = Text.translatable(isDouble ? "container.chestDouble" : "container.chest");
        if (pos != leftPos) refreshPreviewAtPos(world, pos);
        if (isDouble) {
            BlockPos otherPos = getOtherChestBlockPos(world, pos);
            refreshPreviewAtPos(world, otherPos);
        }
        if (pos == leftPos) refreshPreviewAtPos(world, pos);
    }

    @Override
    public boolean equals(Object other) {
        return other != null && getClass() == other.getClass() && Objects.equals(leftPos, ((ChestBlockTab) other).leftPos);
    }
}
