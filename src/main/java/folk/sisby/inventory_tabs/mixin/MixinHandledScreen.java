package folk.sisby.inventory_tabs.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import folk.sisby.inventory_tabs.InventoryTabs;
import folk.sisby.inventory_tabs.ScreenSupport;
import folk.sisby.inventory_tabs.TabManager;
import folk.sisby.inventory_tabs.duck.InventoryTabsScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HandledScreen.class)
public abstract class MixinHandledScreen extends Screen implements InventoryTabsScreen {
    @Unique Boolean inventoryTabs$allowTabs = false;

    protected MixinHandledScreen(Text title) {
        super(title);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void checkSupported(ScreenHandler handler, PlayerInventory inventory, Text title, CallbackInfo ci) {
        inventoryTabs$allowTabs = ScreenSupport.allowTabs(this);
    }
    
    @Inject(method = "init", at = @At("RETURN"))
    private void init(CallbackInfo callbackInfo) {
        if (!inventoryTabs$allowTabs) return;
        HandledScreen<?> self = (HandledScreen<?>) (Object) this;
        TabManager.initScreen(client, self);
    }

    @Inject(method = "render", at = @At("TAIL"))
    protected void render(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (!inventoryTabs$allowTabs) return;
        TabManager.render(matrices, mouseX, mouseY);
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    public void mouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> callbackInfo) {
        if (!inventoryTabs$allowTabs) return;
        if (TabManager.mouseClicked(mouseX, mouseY, button)) {
            callbackInfo.setReturnValue(true);
            callbackInfo.cancel();
        }
    }

    @Inject(method = "mouseReleased", at = @At("HEAD"), cancellable = true)
    public void mouseReleased(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> callbackInfo) {
        if (!inventoryTabs$allowTabs) return;
        if (TabManager.mouseReleased(mouseX, mouseY, button)) {
            callbackInfo.setReturnValue(true);
            callbackInfo.cancel();
        }
    }

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    public void keyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> callbackInfo) {
        if (!inventoryTabs$allowTabs) return;
        if (TabManager.keyPressed(keyCode, scanCode, modifiers)) {
            callbackInfo.setReturnValue(true);
            callbackInfo.cancel();
        }
    }

    @Inject(method = "isClickOutsideBounds", at = @At("RETURN"), cancellable = true)
    protected void isClickOutsideBounds(double mouseX, double mouseY, int left, int top, int button, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) {
            cir.setReturnValue(TabManager.isClickOutsideBounds(mouseX, mouseY));
        }
    }

	@ModifyExpressionValue(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;getDisplayName()Lnet/minecraft/text/Text;"))
	private Text removeCompactPlayerInventoryTitle(Text original) {
		HandledScreen<?> self = (HandledScreen<?>) (Object) this;
		if (InventoryTabs.CONFIG.compactLargeContainers && self.getScreenHandler() instanceof GenericContainerScreenHandler gcsh && gcsh.getRows() == 6) {
			return Text.of("");
		}
		return original;
	}

    @Override
    public boolean inventoryTabs$allowTabs() {
        return inventoryTabs$allowTabs;
    }
}
