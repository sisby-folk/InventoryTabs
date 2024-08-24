package folk.sisby.inventory_tabs.duck;

import folk.sisby.inventory_tabs.InventoryTabs;
import folk.sisby.inventory_tabs.ScreenSupport;
import folk.sisby.inventory_tabs.mixin.HandledScreenAccessor;
import folk.sisby.inventory_tabs.util.WidgetPosition;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;

public interface InventoryTabsScreen {
    boolean inventoryTabs$allowTabs();

    default List<WidgetPosition> getTabPositions(int tabWidth) {
        HandledScreen<?> screen = (HandledScreen<?>) this;
        List<WidgetPosition> list = new ArrayList<>();
        Identifier screenHandlerId = Registry.SCREEN_HANDLER.getId(ScreenSupport.getScreenHandlerType(screen.getScreenHandler()));
        Pair<Integer, Integer> offsets = ScreenSupport.SCREEN_BOUND_OFFSETS.getOrDefault(screenHandlerId, new Pair<>(0,0));
        boolean invert = ScreenSupport.SCREEN_INVERTS.getOrDefault(screenHandlerId, InventoryTabs.CONFIG.invertTabsByDefault);
        int width = ((HandledScreenAccessor) screen).getBackgroundWidth() + offsets.getLeft() + offsets.getRight();
        int left = Math.max(((HandledScreenAccessor) screen).getX() - offsets.getLeft(), 0);

        int count = width / tabWidth;
        int margins = width - tabWidth * count;

        for (int i = 0; i < count; i++) {
            list.add(new WidgetPosition(left + margins / 2 + i * tabWidth, invert ? ((HandledScreenAccessor) screen).getY() + ((HandledScreenAccessor) screen).getBackgroundHeight() : ((HandledScreenAccessor) screen).getY(), !invert));
        }

        return list;
    }
}
