package folk.sisby.inventory_tabs;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.event.lifecycle.v1.CommonLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InventoryTabs implements ClientModInitializer {
    public static final String ID = "inventory_tabs";
    public static final Logger LOGGER = LoggerFactory.getLogger(ID);
    public static final InventoryTabsConfig CONFIG = InventoryTabsConfig.createToml(FabricLoader.getInstance().getConfigDir(), "", ID, InventoryTabsConfig.class);

    public static KeyBinding NEXT_TAB;
    public static KeyBinding TOGGLE_TABS;

    public static Identifier id(String path) {
        return new Identifier(ID, path);
    }

    @Override
    public void onInitializeClient() {
        CommonLifecycleEvents.TAGS_LOADED.register((manager, success) -> TabProviders.reload(manager));
        ClientTickEvents.END_WORLD_TICK.register(TabManager::tick);
        NEXT_TAB = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.inventory_tabs.key.next_tab",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_TAB,
                "key.categories.inventory"
        ));
        TOGGLE_TABS = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.inventory_tabs.key.toggle_tabs",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_LEFT_BRACKET,
                "key.categories.inventory"
        ));
    }
}
