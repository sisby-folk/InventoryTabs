package folk.sisby.inventory_tabs.mixin;

import folk.sisby.inventory_tabs.InventoryTabs;
import net.minecraft.screen.GenericContainerScreenHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(GenericContainerScreenHandler.class)
public abstract class MixinGenericContainerScreenHandler {
    @Shadow @Final private int rows;

    @ModifyArg(method = "<init>(Lnet/minecraft/screen/ScreenHandlerType;ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/inventory/Inventory;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/slot/Slot;<init>(Lnet/minecraft/inventory/Inventory;III)V", ordinal = 0), index = 3)
    public int raiseContainerSlotY(int original) {
        if (InventoryTabs.CONFIG.consistentChests) {
            if (rows == 6 && InventoryTabs.CONFIG.compactChests) return original - 10;
            return original - 1;
        }
        return original;
    }

    @ModifyArg(method = "<init>(Lnet/minecraft/screen/ScreenHandlerType;ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/inventory/Inventory;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/slot/Slot;<init>(Lnet/minecraft/inventory/Inventory;III)V", ordinal = 1), index = 3)
    public int raiseInventorySlotY(int original) {
        if (InventoryTabs.CONFIG.consistentChests) {
            if (rows == 6 && InventoryTabs.CONFIG.compactChests) return original - 19;
            return original - 1;
        }
        return original;
    }

    @ModifyArg(method = "<init>(Lnet/minecraft/screen/ScreenHandlerType;ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/inventory/Inventory;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/slot/Slot;<init>(Lnet/minecraft/inventory/Inventory;III)V", ordinal = 2), index = 3)
    public int raiseHotbarSlotY(int original) {
        if (InventoryTabs.CONFIG.consistentChests) {
            if (rows == 6 && InventoryTabs.CONFIG.compactChests) return original - 19;
            return original - 1;
        }
        return original;
    }
}
