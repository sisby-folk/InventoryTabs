package folk.sisby.inventory_tabs.providers;

import folk.sisby.inventory_tabs.tabs.Tab;
import net.minecraft.client.network.ClientPlayerEntity;

import java.util.function.Consumer;

public interface TabProvider {
    void addAvailableTabs(ClientPlayerEntity player, Consumer<Tab> addTab);
}
