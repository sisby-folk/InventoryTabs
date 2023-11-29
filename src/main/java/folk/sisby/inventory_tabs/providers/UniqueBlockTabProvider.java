package folk.sisby.inventory_tabs.providers;

import folk.sisby.inventory_tabs.InventoryTabs;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.CartographyTableBlock;
import net.minecraft.block.CraftingTableBlock;
import net.minecraft.block.GrindstoneBlock;
import net.minecraft.block.LoomBlock;
import net.minecraft.block.StonecutterBlock;

public class UniqueBlockTabProvider extends BlockTabProvider {
    public UniqueBlockTabProvider() {
        super();
        matches.put(InventoryTabs.id("crafting_table_block"), b -> b instanceof CraftingTableBlock);
        matches.put(InventoryTabs.id("anvil_block"), b -> b instanceof AnvilBlock);
        matches.put(InventoryTabs.id("cartography_table_block"), b -> b instanceof CartographyTableBlock);
        matches.put(InventoryTabs.id("grindstone_block"), b -> b instanceof GrindstoneBlock);
        matches.put(InventoryTabs.id("loom_block"), b -> b instanceof LoomBlock);
        matches.put(InventoryTabs.id("stonecutter_block"), b -> b instanceof StonecutterBlock);
    }

    @Override
    public int getPriority() {
        return 20;
    }

    @Override
    public boolean isUnique() {
        return true;
    }
}
