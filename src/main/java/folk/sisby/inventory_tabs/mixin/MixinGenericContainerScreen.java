package folk.sisby.inventory_tabs.mixin;

import folk.sisby.inventory_tabs.InventoryTabs;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(GenericContainerScreen.class)
public abstract class MixinGenericContainerScreen extends HandledScreen<GenericContainerScreenHandler> {
    @Shadow @Final private int rows;

    public MixinGenericContainerScreen(GenericContainerScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

	@ModifyArgs(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/HandledScreen;<init>(Lnet/minecraft/screen/ScreenHandler;Lnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/text/Text;)V"))
	private static void containerHideTitle(Args args) {
		if (((GenericContainerScreenHandler) args.get(0)).getRows() == 6 && InventoryTabs.CONFIG.compactLargeContainers) {
			args.set(2, Text.empty());
		}
	}

    @Inject(method = "<init>", at = @At("TAIL"))
    public void containerTextHeight(GenericContainerScreenHandler handler, PlayerInventory inventory, Text title, CallbackInfo ci) {
        if (rows == 6 && InventoryTabs.CONFIG.compactLargeContainers) {
            this.backgroundHeight -= 30;
        } else {
            this.backgroundHeight -= 2;
            this.playerInventoryTitleY = this.backgroundHeight - 94;
        }
    }

    @Inject(method = "drawBackground", at = @At("TAIL"))
    public void containerHeader(MatrixStack matrices, float delta, int mouseX, int mouseY, CallbackInfo ci) {
        drawTexture(matrices, (this.width - this.backgroundWidth) / 2, (this.height - this.backgroundHeight) / 2, 0, 0, this.backgroundWidth, 7);
    }

    @ModifyArg(method = "drawBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/GenericContainerScreen;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V", ordinal = 0), index = 2)
    public int containerY(int original) {
        return original + 7;
    }

    @ModifyArg(method = "drawBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/GenericContainerScreen;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V", ordinal = 0), index = 4)
    public int containerV(int original) {
        if (rows == 6 && InventoryTabs.CONFIG.compactLargeContainers) return original + 17;
        return original + 8;
    }

    @ModifyArg(method = "drawBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/GenericContainerScreen;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V", ordinal = 0), index = 6)
    public int containerHeight(int original) {
        if (rows == 6 && InventoryTabs.CONFIG.compactLargeContainers) return original - 17;
        return original - 8;
    }

    @ModifyArg(method = "drawBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/GenericContainerScreen;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V", ordinal = 1), index = 2)
    public int inventoryY(int original) {
        if (rows == 6 && InventoryTabs.CONFIG.compactLargeContainers) return original - 10;
        return original - 1;
    }

    @ModifyArg(method = "drawBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/GenericContainerScreen;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V", ordinal = 1), index = 4)
    public int inventoryV(int original) {
        if (rows == 6 && InventoryTabs.CONFIG.compactLargeContainers) return original + 9;
        return original;
    }

    @ModifyArg(method = "drawBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/GenericContainerScreen;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V", ordinal = 1), index = 6)
    public int inventoryHeight(int original) {
        if (rows == 6 && InventoryTabs.CONFIG.compactLargeContainers) return original - 9;
        return original;
    }
}
