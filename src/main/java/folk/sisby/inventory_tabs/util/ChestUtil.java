package folk.sisby.inventory_tabs.util;

import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.enums.ChestType;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ChestUtil {
    public static boolean isDouble(World world, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos);
        return blockState.contains(Properties.CHEST_TYPE) && blockState.get(ChestBlock.CHEST_TYPE) != ChestType.SINGLE;
    }

    public static BlockPos getOtherChestBlockPos(World world, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos);
        if (blockState.get(ChestBlock.CHEST_TYPE) == ChestType.LEFT) return pos.offset(blockState.get(ChestBlock.FACING).rotateYClockwise());
        return pos.offset(blockState.get(ChestBlock.FACING).rotateYCounterclockwise());
    }
}
