package folk.sisby.inventory_tabs.util;

import net.minecraft.block.BlockState;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class BlockUtil {
    public static List<BlockPos> getBlocksInRadius(BlockPos center, int radius) {
        List<BlockPos> outList = new ArrayList<>();
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    outList.add(new BlockPos(center.getX() + x, center.getY() + y, center.getZ() + z));
                }
            }
        }
        return outList;
    }

    public static <T> List<T> getAttachedBlocks(World world, BlockPos pos, BiFunction<World, BlockPos, T> mapper) {
        List<T> outList = new ArrayList<>();
        for (Direction direction : Direction.Type.HORIZONTAL) {
            if (!direction.getAxis().isHorizontal()) continue;
            BlockPos attachedPos = pos.offset(direction, 1);
            BlockState attachedState = world.getBlockState(attachedPos);
            if (attachedState.contains(Properties.HORIZONTAL_FACING) && attachedState.get(Properties.HORIZONTAL_FACING) == direction) {
                T mappedValue = mapper.apply(world, attachedPos);
                if (mappedValue != null) outList.add(mappedValue);
            }
        }
        return outList;
    }
}
