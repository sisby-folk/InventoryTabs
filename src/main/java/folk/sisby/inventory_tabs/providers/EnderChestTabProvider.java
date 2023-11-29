package folk.sisby.inventory_tabs.providers;

import folk.sisby.inventory_tabs.InventoryTabs;
import folk.sisby.inventory_tabs.tabs.BlockTab;
import folk.sisby.inventory_tabs.tabs.Tab;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.EnderChestBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EnderChestTabProvider extends BlockTabProvider {
    public EnderChestTabProvider() {
        super();
        matches.put(InventoryTabs.id("ender_chest_block"), b -> b instanceof EnderChestBlock);
        preclusions.put(InventoryTabs.id("chest_blocked"), ChestBlock::isChestBlocked);
    }

    @Override
    public Tab createTab(World world, BlockPos pos) {
        return new BlockTab(-5, world, pos);
    }

    @Override
    public int getPriority() {
        return 60;
    }
}
