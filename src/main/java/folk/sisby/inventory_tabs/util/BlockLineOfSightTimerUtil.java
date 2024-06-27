package folk.sisby.inventory_tabs.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BlockLineOfSightTimerUtil {
    // Timeouts are technically not necessary, but it helps reduce flicker in case
    // the stored offset becomes obstructed while the block itself is still in line
    // of sight; though, the flicker is only a thing if you get very unlucky in the
    // random offset generation. 4 ticks are probably more than enough.
    private static final int BLOCK_LOS_TIMEOUT_TICKS = 4;
    public static List<BlockLosTimer> blockLosTimers = new ArrayList<>();

    public static void tick() {
        blockLosTimers.forEach(BlockLosTimer::tickTimer);
        blockLosTimers.removeIf(timer -> timer.ticksTillTimeout <= 0);
    }

    public static class BlockLosTimer {
        public final BlockPos pos;
        public int ticksTillTimeout = BLOCK_LOS_TIMEOUT_TICKS;
        private boolean isInLosThisTick = true;
        public Vec3d lastViableOffset = null;

        BlockLosTimer(BlockPos pos) {
            this.pos = pos;
            blockLosTimers.add(this);
        }

        private void tickTimer() {
            ticksTillTimeout = isInLosThisTick ? BLOCK_LOS_TIMEOUT_TICKS : ticksTillTimeout - 1;
            isInLosThisTick = false;
        }
    }

    public static void refreshBlockLosTimer(BlockPos pos, Vec3d offset) {
        BlockLosTimer timer = Objects.requireNonNullElseGet(getBlockLosTimer(pos), () -> new BlockLosTimer(pos));
        timer.lastViableOffset = offset;
        timer.isInLosThisTick = true;
    }

    public static BlockLosTimer getBlockLosTimer(BlockPos pos) {
        return blockLosTimers.stream().filter(timer -> timer.pos.equals(pos)).findFirst().orElse(null);
    }
}
