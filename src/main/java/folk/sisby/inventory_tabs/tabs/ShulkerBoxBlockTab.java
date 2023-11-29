package folk.sisby.inventory_tabs.tabs;


import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ShulkerBoxBlockTab extends BlockTab {
    public ShulkerBoxBlockTab(World world, BlockPos pos) {
        super(-50, world, pos);
    }

    @Override
    public boolean shouldBeRemoved(World world, boolean current) {
        return world.getBlockEntity(pos) instanceof ShulkerBoxBlockEntity sbe && !ShulkerBoxBlock.canOpen(world.getBlockState(pos), world, pos, sbe) || super.shouldBeRemoved(world, current);
    }
}
