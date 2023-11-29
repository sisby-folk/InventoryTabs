package folk.sisby.inventory_tabs.providers;

import folk.sisby.inventory_tabs.tabs.Tab;
import folk.sisby.inventory_tabs.tabs.VehicleInventoryTab;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.RideableOpenableInventory;

import java.util.function.Consumer;

public class VehicleInventoryTabProvider implements TabProvider {
    @Override
    public void addAvailableTabs(ClientPlayerEntity player, Consumer<Tab> addTab) {
        if (player.hasVehicle() && player.getVehicle() instanceof RideableOpenableInventory) {
            addTab.accept(new VehicleInventoryTab(player.getVehicle()));
        }
    }
}
