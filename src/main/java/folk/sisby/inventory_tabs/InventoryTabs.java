package folk.sisby.inventory_tabs;

import com.mojang.blaze3d.platform.InputUtil;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.event.lifecycle.v1.CommonLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.option.KeyBind;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("deprecation")
public class InventoryTabs implements ClientModInitializer {
    public static final String ID = "inventory_tabs";
    public static final Logger LOGGER = LoggerFactory.getLogger(ID);
    public static final InventoryTabsConfig CONFIG = InventoryTabsConfig.createToml(FabricLoader.getInstance().getConfigDir(), "", ID, InventoryTabsConfig.class);

    public static final KeyBind NEXT_TAB = KeyBindingHelper.registerKeyBinding(new KeyBind(
            "key.inventory_tabs.key.next_tab",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_TAB,
            "key.categories.inventory"
    ));

    public static Identifier id(String path) {
        return new Identifier(ID, path);
    }

    @Override
    public void onInitializeClient() {
        CommonLifecycleEvents.TAGS_LOADED.register((manager, success) -> TabProviders.reload(manager));
        ClientTickEvents.END_WORLD_TICK.register(TabManager::tick);
    }
}
