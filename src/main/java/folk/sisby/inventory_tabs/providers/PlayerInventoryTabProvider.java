package folk.sisby.inventory_tabs.providers;

import folk.sisby.inventory_tabs.tabs.PlayerInventoryTab;
import folk.sisby.inventory_tabs.tabs.Tab;
import net.minecraft.client.network.ClientPlayerEntity;

import java.util.function.Consumer;

public class PlayerInventoryTabProvider implements TabProvider {
    @Override
    public void addAvailableTabs(ClientPlayerEntity player, Consumer<Tab> addTab) {
        addTab.accept(new PlayerInventoryTab());
    }
}
