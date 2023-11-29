package folk.sisby.inventory_tabs.util;

import folk.sisby.inventory_tabs.InventoryTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.RaycastContext;

public class PlayerUtil {
    public static final int REACH = 5;
    public static final double BLOCK_REACH_SQUARE = REACH * REACH;
    public static boolean inRange(PlayerEntity player, BlockPos pos) {
        if (pos.ofCenter().squaredDistanceTo(player.getEyePos()) > BLOCK_REACH_SQUARE) return false;
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
        return player.getWorld().raycast(new RaycastContext(player.getEyePos(), pos.ofCenter(), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, player));
    }

    public static EntityHitResult raycast(PlayerEntity player, Entity entity) {
        return ProjectileUtil.raycast(player, player.getEyePos(), entity.getPos(), player.getBoundingBox().stretch(entity.getRotationVec(1.0F).multiply(REACH)).expand(1.0, 1.0, 1.0), e -> true, BLOCK_REACH_SQUARE);
    }
}
