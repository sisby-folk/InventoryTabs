package folk.sisby.inventory_tabs.mixin;

import folk.sisby.inventory_tabs.InventoryTabs;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GenericContainerScreen.class)
public abstract class MixinGenericContainerScreen extends HandledScreen<GenericContainerScreenHandler> {
    @Shadow @Final private int rows;
    @Shadow @Final private static Identifier TEXTURE;

    public MixinGenericContainerScreen(GenericContainerScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    public void fixInventoryTextHeight(GenericContainerScreenHandler handler, PlayerInventory inventory, Text title, CallbackInfo ci) {
        if (InventoryTabs.CONFIG.consistentChests) {
            if (rows == 6 && InventoryTabs.CONFIG.compactChests) {
                this.backgroundHeight -= 30;
                this.title = Text.empty();
                this.playerInventoryTitle = Text.empty();
            } else {
                this.backgroundHeight -= 2;
                this.playerInventoryTitleY = this.backgroundHeight - 94;
            }
        }
    }

    @Inject(method = "drawBackground", at = @At(value = "TAIL"))
    public void compactChestsHeader(GuiGraphics graphics, float delta, int mouseX, int mouseY, CallbackInfo ci) {
        if (InventoryTabs.CONFIG.consistentChests) {
            graphics.drawTexture(TEXTURE, (this.width - this.backgroundWidth) / 2, (this.height - this.backgroundHeight) / 2, 0, 0, this.backgroundWidth, 7);
        }
    }

    @ModifyArg(method = "drawBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V", ordinal = 0), index = 2)
    public int compactChestsContainerY(int original) {
        if (InventoryTabs.CONFIG.consistentChests) {
            return original + 7;
        }
        return original;
    }

    @ModifyArg(method = "drawBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V", ordinal = 0), index = 4)
    public int compactChestsContainerV(int original) {
        if (InventoryTabs.CONFIG.consistentChests) {
            if (rows == 6 && InventoryTabs.CONFIG.compactChests) return original + 17;
            return original + 8;
        }
        return original;
    }

    @ModifyArg(method = "drawBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V", ordinal = 0), index = 6)
    public int compactChestsContainerHeight(int original) {
        if (InventoryTabs.CONFIG.consistentChests) {
            if (rows == 6 && InventoryTabs.CONFIG.compactChests) return original - 17;
            return original - 8;
        }
        return original;
    }

    @ModifyArg(method = "drawBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V", ordinal = 1), index = 2)
    public int inventoryY(int original) {
        if (InventoryTabs.CONFIG.consistentChests) {
            if (rows == 6 && InventoryTabs.CONFIG.compactChests) return original - 10;
            return original - 1;
        }
        return original;
    }

    @ModifyArg(method = "drawBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V", ordinal = 1), index = 4)
    public int inventoryV(int original) {
        if (InventoryTabs.CONFIG.consistentChests) {
            if (rows == 6 && InventoryTabs.CONFIG.compactChests) return original + 9;
            return original;
        }
        return original;
    }

    @ModifyArg(method = "drawBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V", ordinal = 1), index = 6)
    public int inventoryHeight(int original) {
        if (InventoryTabs.CONFIG.consistentChests) {
            if (rows == 6 && InventoryTabs.CONFIG.compactChests) return original - 9;
            return original;
        }
        return original;
    }
}
