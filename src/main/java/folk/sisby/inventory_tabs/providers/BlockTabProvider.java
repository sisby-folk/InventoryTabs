package folk.sisby.inventory_tabs.providers;

import folk.sisby.inventory_tabs.tabs.BlockTab;
import folk.sisby.inventory_tabs.tabs.Tab;
import folk.sisby.inventory_tabs.util.BlockUtil;
import folk.sisby.inventory_tabs.util.PlayerUtil;
import net.minecraft.block.Block;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

public abstract class BlockTabProvider extends RegistryTabProvider<Block> {
    public final Map<Identifier, BiPredicate<World, BlockPos>> preclusions = new HashMap<>();

    @Override
    public void addAvailableTabs(ClientPlayerEntity player, Consumer<Tab> addTab) {
        World world = player.getWorld();
        Set<Block> blocksAdded = new HashSet<>();
        for (BlockPos pos : BlockUtil.getBlocksInRadius(player.getBlockPos(), PlayerUtil.REACH)) {
            Block block = world.getBlockState(pos).getBlock();
            if (values.contains(block) && preclusions.values().stream().noneMatch(p -> p.test(world, pos))) {
                if (!PlayerUtil.inRange(player, pos)) continue;
                if (isUnique() && !blocksAdded.add(block)) continue;
                addTab.accept(createTab(world, pos));
            }
        }
    }

    public Tab createTab(World world, BlockPos pos) {
        return new BlockTab(0, world, pos);
    }
}
