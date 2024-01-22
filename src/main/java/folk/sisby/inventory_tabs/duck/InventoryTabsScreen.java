package folk.sisby.inventory_tabs.duck;

import folk.sisby.inventory_tabs.ScreenSupport;
import folk.sisby.inventory_tabs.util.WidgetPosition;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import java.util.ArrayList;
import java.util.List;

public interface InventoryTabsScreen {
    boolean inventoryTabs$allowTabs();

    default List<WidgetPosition> getTabPositions(int tabWidth) {
        HandledScreen<?> screen = (HandledScreen<?>) this;
        List<WidgetPosition> list = new ArrayList<>();
        Identifier screenHandlerId = Registries.SCREEN_HANDLER.getId(screen.getScreenHandler().type);
        Pair<Integer, Integer> offsets = ScreenSupport.SCREEN_BOUND_OFFSETS.getOrDefault(screenHandlerId, new Pair<>(0,0));
        int width = screen.backgroundWidth + offsets.getLeft() + offsets.getRight();
        int left = Math.max(screen.x - offsets.getLeft(), 0);

        int count = width / tabWidth;
        int margins = width - tabWidth * count;

        for (int i = 0; i < count; i++) {
            list.add(new WidgetPosition(left + margins / 2 + i * tabWidth, screen.y, true));
        }

        return list;
    }
}
