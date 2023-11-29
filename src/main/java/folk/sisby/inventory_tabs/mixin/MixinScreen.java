package folk.sisby.inventory_tabs.mixin;

import folk.sisby.inventory_tabs.TabManager;
import folk.sisby.inventory_tabs.duck.InventoryTabsScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public class MixinScreen {
    @Inject(method = "renderBackground", at = @At("TAIL"))
    void renderTabBackground(GuiGraphics graphics, CallbackInfo ci) {
        if (!(this instanceof InventoryTabsScreen its) || !its.inventoryTabs$allowTabs()) return;
        TabManager.renderBackground(graphics);
    }
}
