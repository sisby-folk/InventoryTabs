package folk.sisby.inventory_tabs.providers;

import folk.sisby.inventory_tabs.InventoryTabs;
import folk.sisby.inventory_tabs.tabs.ChestBlockTab;
import folk.sisby.inventory_tabs.tabs.Tab;
import net.minecraft.block.AbstractChestBlock;
import net.minecraft.block.ChestBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ChestBlockTabProvider extends BlockTabProvider {
    public ChestBlockTabProvider() {
        super();
        matches.put(InventoryTabs.id("abstract_chest_block"), b -> b instanceof AbstractChestBlock<?>);
        preclusions.put(InventoryTabs.id("chest_blocked"), ChestBlock::isChestBlocked);
    }

    @Override
    public Tab createTab(World world, BlockPos pos) {
        return new ChestBlockTab(-10, world, pos);
    }

    @Override
    public int getPriority() {
        return 50;
    }
}
