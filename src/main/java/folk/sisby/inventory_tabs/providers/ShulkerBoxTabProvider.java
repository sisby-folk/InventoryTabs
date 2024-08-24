package folk.sisby.inventory_tabs.providers;

import folk.sisby.inventory_tabs.InventoryTabs;
import folk.sisby.inventory_tabs.mixin.ShulkerBoxBlockAccessor;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ShulkerBoxTabProvider extends BlockTabProvider {
    public ShulkerBoxTabProvider() {
        super();
        matches.put(InventoryTabs.id("shulker_box_block"), b -> b instanceof ShulkerBoxBlock);
        preclusions.put(InventoryTabs.id("shulker_box_blocked"), (w, p) -> w.getBlockEntity(p) instanceof ShulkerBoxBlockEntity s && !ShulkerBoxBlockAccessor.canOpen(w.getBlockState(p), w, p, s));
    }

    @Override
    public int getTabOrderPriority(World world, BlockPos pos) {
        return -50;
    }

    @Override
    public int getRegistryPriority() {
        return 70;
    }
}
