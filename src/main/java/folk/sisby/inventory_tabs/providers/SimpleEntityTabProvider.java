package folk.sisby.inventory_tabs.providers;

import folk.sisby.inventory_tabs.InventoryTabs;
import net.minecraft.client.MinecraftClient;

public class SimpleEntityTabProvider extends EntityTabProvider {
    public SimpleEntityTabProvider() {
        preclusions.put(InventoryTabs.id("vehicle"), e -> e == MinecraftClient.getInstance().player.getVehicle());
    }

    @Override
    public int getPriority() {
        return 20;
    }
}
