package folk.sisby.inventory_tabs.tabs;

import folk.sisby.inventory_tabs.mixin.HandledScreenAccessor;
import folk.sisby.inventory_tabs.util.DrawUtil;
import folk.sisby.inventory_tabs.util.WidgetPosition;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Rect2i;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public interface Tab {
    Identifier TABS_TEXTURE = new Identifier("textures/gui/container/creative_inventory/tabs.png");
    int TEXTURE_WIDTH = 28;
    int TEXTURE_U = 28;
    int[] TEXTURE_V = new int[]{2, 32, 64, 96};
    int[] TEXTURE_HEIGHT = new int[]{30, 32, 28, 32};
    int[] Y_OFFSET = new int[]{0, 0, 0, -4};
    int[] ITEM_Y_OFFSET = new int[]{0, 0, -3, -3};
    int[] HEIGHT_OFFSET = new int[]{0, 4, 0, 4};

    /**
     * Opens the screen associated with the tab.
     */
    void open(ClientPlayerEntity player, ClientWorld world, ScreenHandler handler, ClientPlayerInteractionManager interactionManager);

    /**
     * @return true if the tab should stop being displayed. Should be synced up with the provider that provides this tab.
     */
    boolean shouldBeRemoved(World world, boolean current);

    /**
     * @return the stack to render as an icon in the default rendering method.
     */
    ItemStack getTabIcon();

    /**
     * @return the text that's displayed when hovering over the tab.
     */
    Text getHoverText();

    /**
     * Called when the screen associated with the tab is closed (for handlers that aren't destroyed when closed on the servers)
     */
    default void close(ClientPlayerEntity player, ClientWorld world, ScreenHandler handler, ClientPlayerInteractionManager interactionManager) {
    }

    /**
     * @return the tab's left-priority when being displayed. The player's inventory is at 100.
     */
    default int getPriority() {
        return 0;
    }

    /**
     * @return whether the tabs open method instantly opens the screen on the client side without slot sync.
     * Used for the survival inventory. Helps preserve cursor stacks.
     */
    default boolean isInstant() {
        return false;
    }

    /**
     * @return whether the tab can only be safely opened through the player inventory screen.
     * Helps prevent lockups, but might flicker.
     */
    default boolean isBuffered() {
        return false;
    }

    default void render(HandledScreen<?> screen, MatrixStack matrices, WidgetPosition pos, int width, int height, double mouseX, double mouseY, boolean current) {
        int type = pos.up ? (!current ? 0 : 1) : (!current ? 2 : 3);
        int y = pos.y + (pos.up ? -height : 0);
        int drawHeight = height + HEIGHT_OFFSET[type];
        int drawY = y + Y_OFFSET[type];
        DrawUtil.drawCrunched(screen, matrices, TABS_TEXTURE, pos.x, drawY, width, drawHeight, TEXTURE_WIDTH, TEXTURE_HEIGHT[type], TEXTURE_U, TEXTURE_V[type]);
        int itemPadding = Math.max(0, (width - 16) / 2);
        int itemX = pos.x + itemPadding;
        int itemY = y + itemPadding + ITEM_Y_OFFSET[type];
        ((HandledScreenAccessor) screen).drawItem(getTabIcon(), itemX, itemY, null);
    }

    default void renderTooltips(HandledScreen<?> screen, MatrixStack matrices, WidgetPosition pos, int width, int height, double mouseX, double mouseY, boolean current) {
        int type = pos.up ? (!current ? 0 : 1) : (!current ? 2 : 3);
        int y = pos.y + (pos.up ? -height : 0);
        int itemPadding = Math.max(0, (width - 16) / 2);
        int itemX = pos.x + itemPadding;
        int itemY = y + itemPadding + ITEM_Y_OFFSET[type];
        if (new Rect2i(itemX, itemY, 16, 16).contains((int) mouseX, (int) mouseY)) {
            screen.renderTooltip(matrices, getHoverText(), (int) mouseX, (int) mouseY);
        }
    }
}
