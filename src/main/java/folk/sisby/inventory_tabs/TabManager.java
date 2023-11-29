package folk.sisby.inventory_tabs;

import folk.sisby.inventory_tabs.duck.InventoryTabsScreen;
import folk.sisby.inventory_tabs.tabs.BlockTab;
import folk.sisby.inventory_tabs.tabs.EntityTab;
import folk.sisby.inventory_tabs.tabs.ItemTab;
import folk.sisby.inventory_tabs.tabs.Tab;
import folk.sisby.inventory_tabs.tabs.VehicleInventoryTab;
import folk.sisby.inventory_tabs.util.CursorStackUtil;
import folk.sisby.inventory_tabs.util.MouseUtil;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.math.Rect2i;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.HandledScreenCloseC2SPacket;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TabManager {
    private static final Identifier BUTTONS_TEXTURE = InventoryTabs.id("textures/gui/buttons.png");
    public static final int TAB_WIDTH = 24;
    public static final int TAB_HEIGHT = 25;
    public static final int BUTTON_WIDTH = 10;
    public static final int BUTTON_HEIGHT = 18;

    private static HandledScreen<?> currentScreen;
    private static final List<Tab> tabs = new ArrayList<>();
    private static int currentPage = 0;
    private static Tab currentTab;
    private static List<Vector2i> tabPositions;
    private static boolean tabOpenedRecently;
    private static boolean skipRestore;

    public static void initScreen(MinecraftClient client, HandledScreen<?> screen) {
        currentScreen = screen;
        tabPositions = ((InventoryTabsScreen) currentScreen).getTabPositions(TAB_WIDTH);
        if (!tabOpenedRecently) onOpenTab(guessOpenedTab(client, screen));
        tabOpenedRecently = false;
        if (!skipRestore) {
            CursorStackUtil.tryPop(MinecraftClient.getInstance().player, MinecraftClient.getInstance().interactionManager, currentScreen.getScreenHandler());
            MouseUtil.tryPop();
        } else {
            skipRestore = false;
        }
    }

    public static Tab guessOpenedTab(MinecraftClient client, HandledScreen<?> screen) {
        World world = client.player.getWorld();
        // "Open Inventory" Guesses
        if (currentScreen instanceof InventoryScreen) return tabs.get(0);
        if (client.player.hasVehicle()) {
            for (Tab tab : tabs) {
                if (tab instanceof VehicleInventoryTab vit) {
                    if (client.player.getVehicle().equals(vit.entity)) {
                        return tab;
                    }
                }
            }
        }
        // Crosshair Guesses
        if (client.crosshairTarget instanceof BlockHitResult result) {
            BlockPos pos = result.getBlockPos();
            BlockEntity blockEntity = world.getBlockEntity(pos);
            for (Tab tab : tabs) {
                if (tab instanceof BlockTab bt) {
                    if (pos.equals(bt.pos) || blockEntity == world.getBlockEntity(bt.pos)) return tab;
                }
            }
        } else if (client.crosshairTarget instanceof EntityHitResult result) {
            Entity entity = result.getEntity();
            for (Tab tab : tabs) {
                if (tab instanceof EntityTab et) {
                    if (entity.equals(et.entity)) {
                        return tab;
                    }
                }
            }
        }
        // Hand Guesses
        for (ItemStack stack : client.player.getItemsHand()) {
            if (stack.isEmpty()) continue;
            for (Tab tab : tabs) {
                if (tab instanceof ItemTab it) {
                    if (stack.equals(it.stack)) {
                        return tab;
                    }
                }
            }
        }
        return null;
    }

    public static void tick(World world) {
        if (tabs.removeIf(t -> t.shouldBeRemoved(world, t == currentTab))) {
            sortTabs();
        }
        TabProviders.REGISTRY.values().forEach(tabProvider -> tabProvider.addAvailableTabs(MinecraftClient.getInstance().player, TabManager::tryAddTab));
        if (currentTab != null && !tabs.contains(currentTab)) currentTab = null;
    }

    public static void tryAddTab(Tab tab) {
        if (!tabs.contains(tab)) {
            tabs.add(tab);
            sortTabs();
        }
    }

    public static void sortTabs() {
        tabs.sort(Comparator.comparingInt(Tab::getPriority).reversed().thenComparing(t -> t.getHoverText().getString()));
    }

    public static void clearTabs() {
        tabs.clear();
    }

    public static boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            if (getPageButton(true).contains((int) mouseX, (int) mouseY)) {
                if (currentPage > 0) {
                    setCurrentPage(currentPage - 1);
                    playClick();
                }
                return true;
            }

            if (getPageButton(false).contains((int) mouseX, (int) mouseY)) {
                if (currentPage < getMaximumPage()) {
                    setCurrentPage(currentPage + 1);
                    playClick();
                }
                return true;
            }

            for (int i = 0; i < tabPositions.size(); i++) {
                Vector2i pos = tabPositions.get(i);
                Tab tab = getTab(currentPage * tabPositions.size() + i);
                if (pos != null && tab != null && tab != currentTab) {
                    if (getTabArea(pos).contains((int) mouseX, (int) mouseY)) {
                        onTabClick(tab);
                        playClick();
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public static boolean isClickOutsideBounds(double mouseX, double mouseY) {
        return !getPageButton(true).contains((int) mouseX, (int) mouseY) && !getPageButton(false).contains((int) mouseX, (int) mouseY) && tabPositions.stream().noneMatch(pos -> getTabArea(pos).contains((int) mouseX, (int) mouseY));
    }

    public static boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (InventoryTabs.NEXT_TAB.matchesKey(keyCode, scanCode)) {
            if (Screen.hasShiftDown()) {
                if (tabs.indexOf(currentTab) == 0) {
                    onTabClick(tabs.get(tabs.size() - 1));
                } else {
                    onTabClick(tabs.get(tabs.indexOf(currentTab) - 1));
                }
            } else {
                if (tabs.indexOf(currentTab) == tabs.size() - 1) {
                    onTabClick(tabs.get(0));
                } else {
                    onTabClick(tabs.get(tabs.indexOf(currentTab) + 1));
                }
            }
            return true;
        }

        return false;
    }

    public static void onTabClick(Tab tab) {
        MouseUtil.push();
        CursorStackUtil.push(MinecraftClient.getInstance().player, MinecraftClient.getInstance().interactionManager, currentScreen.getScreenHandler());
        skipRestore = true;
        tabOpenedRecently = true;
        MinecraftClient.getInstance().getNetworkHandler().sendPacket(new HandledScreenCloseC2SPacket(currentScreen.getScreenHandler().syncId));
        if (tab.open()) {
            onOpenTab(tab);
            if (!skipRestore) {
                CursorStackUtil.tryPop(MinecraftClient.getInstance().player, MinecraftClient.getInstance().interactionManager, currentScreen.getScreenHandler());
                MouseUtil.tryPop();
            }
        } else {
            tabOpenedRecently = false;
        }
        skipRestore = false;
    }

    public static void onOpenTab(Tab tab) {
        if (currentTab != null && currentTab != tab) currentTab.onClose(currentScreen);
        currentTab = tab;
        setCurrentPage(indexOf(tab) / tabPositions.size());
    }

    public static int indexOf(Tab tab) {
        int i = 0;
        for (Tab t : tabs) {
            if (t.equals(tab)) return i;
            i++;
        }
        return -1;
    }

    public static Tab getTab(int index) {
        int i = 0;
        for (Tab t : tabs) {
            if (i == index) return t;
            i++;
        }
        return null;
    }

    public static void setCurrentPage(int page) {
        if (page == 0 || tabs.size() >= tabPositions.size()) currentPage = page;
    }

    public static int getMaximumPage() {
        return tabs.size() / (tabPositions.size() + 1);
    }

    public static void renderBackground(GuiGraphics graphics) {
        tabPositions = ((InventoryTabsScreen) currentScreen).getTabPositions(TAB_WIDTH);
        int i = 0;
        for (Vector2i pos : tabPositions) {
            Tab tab = getTab(currentPage * tabPositions.size() + i);
            if (pos != null && tab != null) tab.renderBackground(graphics, pos.x, pos.y, TAB_WIDTH, TAB_HEIGHT, tab == currentTab);
            i++;
        }
    }

    public static void renderForeground(GuiGraphics graphics, double mouseX, double mouseY) {
        int i = 0;
        for (Vector2i pos : tabPositions) {
            Tab tab = getTab(currentPage * tabPositions.size() + i);
            if (pos != null && tab != null) tab.renderForeground(graphics, pos.x, pos.y, TAB_WIDTH, TAB_HEIGHT, mouseX, mouseY,tab == currentTab);
            i++;
        }

        if (getMaximumPage() > 0) {
            drawButton(graphics, mouseX, mouseY, true);
            drawButton(graphics, mouseX, mouseY, false);
        }
    }

    private static Rect2i getPageButton(boolean left) {
        Vector2i pos = tabPositions.get(left ? 0 : tabPositions.size() - 1);
        return new Rect2i(pos.x + (left ? -BUTTON_WIDTH : TAB_WIDTH), pos.y - BUTTON_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT);
    }

    private static Rect2i getTabArea(Vector2i pos) {
        return new Rect2i(pos.x, pos.y - TAB_HEIGHT, TAB_WIDTH, TAB_HEIGHT);
    }
    
    private static void drawButton(GuiGraphics graphics, double mouseX, double mouseY, boolean left) {
        Rect2i rect = getPageButton(left);
        boolean hovered = rect.contains((int) mouseX, (int) mouseY);
        boolean active = left ? currentPage > 0 : currentPage < getMaximumPage();
        int u = BUTTON_WIDTH * (left ? 0 : 1);
        int v = BUTTON_HEIGHT * (active ? hovered ? 2 : 1 : 0);
        graphics.drawTexture(BUTTONS_TEXTURE, rect.getX(), rect.getY(), u, v, rect.getWidth(), rect.getHeight());
        if (hovered) graphics.drawTooltip(currentScreen.getTextRenderer(), Text.literal((currentPage + 1) + "/" + (getMaximumPage() + 1)), (int) mouseX, (int) mouseY);
    }

    public static void playClick() {
        MinecraftClient.getInstance().getSoundManager()
                .play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK.value(), 1.0F));
    }

}


