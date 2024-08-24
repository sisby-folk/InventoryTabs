package folk.sisby.inventory_tabs.mixin;

import java.util.Map;
import java.util.Objects;

import com.llamalad7.mixinextras.sugar.Local;
import folk.sisby.inventory_tabs.InventoryTabs;
import folk.sisby.inventory_tabs.duck.InventoryTabsScreen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

@Mixin(KeyBinding.class)
public abstract class MixinKeyBinding {
	@Shadow @Final private static Map<InputUtil.Key, KeyBinding> KEY_TO_BINDINGS;
	
	@Shadow private int timesPressed;
	
	@Inject(method = "onKeyPressed", at = @At("HEAD"), cancellable = true)
	private static void onKeyPressed(InputUtil.Key key, CallbackInfo ci) {
		MixinKeyBinding alternative = (MixinKeyBinding) (Object) findAlternative(key, KEY_TO_BINDINGS.get(key), InventoryTabs.NEXT_TAB);
		if(alternative != null) {
			alternative.timesPressed++;
			ci.cancel();
		}
	}
	
	@Inject(method = "setKeyPressed", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/KeyBinding;setPressed(Z)V"), cancellable = true)
	private static void keyPressed(InputUtil.Key key, boolean pressed, CallbackInfo ci, @Local KeyBinding KeyBind) {
		KeyBinding alternative = findAlternative(key, KeyBind, InventoryTabs.NEXT_TAB);
		if(alternative != null) {
			alternative.setPressed(pressed);
			ci.cancel();
		}
	}
	
	@Unique private static KeyBinding findAlternative(InputUtil.Key key, KeyBinding binding, KeyBinding alternativeTo) {
		if(binding == alternativeTo && (!(MinecraftClient.getInstance().currentScreen instanceof InventoryTabsScreen its) || !its.inventoryTabs$allowTabs())) {
			for(Map.Entry<InputUtil.Key, KeyBinding> entry : KEY_TO_BINDINGS.entrySet()) {
				if(Objects.equals(entry.getKey(), key) && entry.getValue() != alternativeTo) {
					return entry.getValue();
				}
			}
		}
		return null;
	}
}
