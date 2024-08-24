package folk.sisby.inventory_tabs;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.HorseScreen;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Predicate;

public class ScreenSupport {
    public static Map<Identifier, Predicate<HandledScreen<?>>> DENY = new HashMap<>();
    public static Map<Identifier, Predicate<HandledScreen<?>>> ALLOW = new HashMap<>();
    public static Map<Identifier, Pair<Integer, Integer>> SCREEN_BOUND_OFFSETS = new HashMap<>();
    public static Map<Identifier, Boolean> SCREEN_INVERTS = new HashMap<>();

    public static Boolean allowTabs(RegistryKey<ScreenHandlerType<?>> type) {
        if (InventoryTabs.CONFIG.screenOverrides.entrySet().stream().filter(e -> !e.getValue()).anyMatch(e -> Objects.equals(e.getKey(), type.getValue().toString()))) return false;
        if (InventoryTabs.CONFIG.screenOverrides.entrySet().stream().filter(Map.Entry::getValue).anyMatch(e -> Objects.equals(e.getKey(), type.getValue().toString()))) return true;
        return null;
    }

	public static ScreenHandlerType<?> getScreenHandlerType(ScreenHandler handler) {
		try {
			return handler.getType();
		} catch (UnsupportedOperationException | NoSuchElementException ignored) {
			return null;
		}
	}

    public static boolean allowTabs(Screen screen) {
        if (screen instanceof HandledScreen<?> hs && hs.getScreenHandler() != null) {
            if (DENY.values().stream().anyMatch(p -> p.test(hs))) return false;
            if (ALLOW.values().stream().anyMatch(p -> p.test(hs))) return true;
	        ScreenHandlerType<?> type = getScreenHandlerType(hs.getScreenHandler());
	        if (type != null) {
		        RegistryKey<ScreenHandlerType<?>> key = Registry.SCREEN_HANDLER.getKey(type).orElse(null);
				if (key != null) {
					Boolean override = allowTabs(key);
					if (override != null) return override;
				}
	        }
            return InventoryTabs.CONFIG.allowScreensByDefault;
        }
        return false;
    }

    static {
        DENY.put(InventoryTabs.id("creative_screen"), hs -> hs instanceof CreativeInventoryScreen);
        ALLOW.put(InventoryTabs.id("horse_screen"), hs -> hs instanceof HorseScreen);
        InventoryTabs.CONFIG.leftBoundOffsetOverride.forEach((screenHandlerId, offset) -> SCREEN_BOUND_OFFSETS.put(screenHandlerId.equals("null") ? null : new Identifier(screenHandlerId), new Pair<>(offset, 0)));
        InventoryTabs.CONFIG.rightBoundOffsetOverride.forEach((screenHandlerId, offset) -> SCREEN_BOUND_OFFSETS.merge(screenHandlerId.equals("null") ? null : new Identifier(screenHandlerId), new Pair<>(0, offset), (o, n) -> new Pair<>(o.getLeft(), n.getRight())));
        InventoryTabs.CONFIG.invertedTabsOverride.forEach((screenHandlerId, doInvert) -> SCREEN_INVERTS.put(screenHandlerId.equals("null") ? null : new Identifier(screenHandlerId), doInvert));
    }
}
