package folk.sisby.inventory_tabs.util;

import folk.sisby.inventory_tabs.InventoryTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static folk.sisby.inventory_tabs.util.BlockLineOfSightTimerUtil.*;

public class PlayerUtil {
    public static final int REACH = 5;
    public static final double BLOCK_REACH_SQUARE = REACH * REACH;

    public static boolean inRange(PlayerEntity player, BlockPos pos) {
        if (pos.toCenterPos().squaredDistanceTo(player.getEyePos()) > BLOCK_REACH_SQUARE) return false;
        BlockHitResult result = raycast(player, pos);
        return pos.equals(result.getBlockPos());
    }

    public static boolean inRange(PlayerEntity player, Entity entity) {
        if (entity.getPos().squaredDistanceTo(player.getEyePos()) > BLOCK_REACH_SQUARE) return false;
        if (InventoryTabs.CONFIG.ignoreWalls) return true;
        EntityHitResult result = raycast(player, entity);
        return result != null && entity.equals(result.getEntity());
    }

    public static BlockHitResult raycast(PlayerEntity player, BlockPos pos) {
        List<Vec3d> blockOffsets = new ArrayList<>();
        BlockLosTimer blockLosTimer = getBlockLosTimer(pos);
        if (blockLosTimer != null && blockLosTimer.lastViableOffset != null) {
            blockOffsets.add(blockLosTimer.lastViableOffset);
        }
        blockOffsets.addAll(generateRandomVec3dList(9, new Vec3d(0.0D, 0.0D, 0.0D), new Vec3d(1.0D, 1.0D, 1.0D)));
        for (Vec3d offset : blockOffsets) {
            BlockHitResult hitResult = player.getWorld().raycast(new RaycastContext(player.getEyePos(), Vec3d.of(pos).add(offset), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, player));
            if (hitResult.getType() != HitResult.Type.MISS && hitResult.getBlockPos().equals(pos)) {
                refreshBlockLosTimer(pos, offset);
                return hitResult;
            }
        }
        return BlockHitResult.createMissed(player.getPos(), Direction.EAST, player.getBlockPos());
    }

    public static EntityHitResult raycast(PlayerEntity player, Entity entity) {
        return ProjectileUtil.raycast(player, player.getEyePos(), entity.getPos(), player.getBoundingBox().stretch(entity.getRotationVec(1.0F).multiply(REACH)).expand(1.0, 1.0, 1.0), e -> true, BLOCK_REACH_SQUARE);
    }

    public static List<Vec3d> generateRandomVec3dList(int count, Vec3d min, Vec3d max) {
        List<Vec3d> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            list.add(generateRandomVec3d(min, max));
        }
        return list;
    }

    private static Vec3d generateRandomVec3d(Vec3d min, Vec3d max) {
        Random random = new Random();
        double x = min.x + (max.x - min.x) * random.nextDouble();
        double y = min.y + (max.y - min.y) * random.nextDouble();
        double z = min.z + (max.z - min.z) * random.nextDouble();
        return new Vec3d(x, y, z);
    }
}
