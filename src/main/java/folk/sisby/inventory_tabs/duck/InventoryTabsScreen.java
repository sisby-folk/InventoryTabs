package folk.sisby.inventory_tabs.duck;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.List;

public interface InventoryTabsScreen {
    boolean inventoryTabs$allowTabs();

    default List<Vector2i> getTabPositions(int tabWidth) {
        HandledScreen<?> screen = (HandledScreen<?>) this;
        List<Vector2i> list = new ArrayList<>();
        int count = screen.backgroundWidth / tabWidth;
        int margins = screen.backgroundWidth - tabWidth * count;

        for (int i = 0; i < count; i++) {
            list.add(new Vector2i(screen.x + margins / 2 + i * tabWidth, screen.y));
        }

        return list;
    }
}
