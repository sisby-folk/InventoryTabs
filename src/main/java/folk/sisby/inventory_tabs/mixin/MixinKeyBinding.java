package folk.sisby.inventory_tabs.mixin;

import java.util.Map;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import folk.sisby.inventory_tabs.InventoryTabs;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

@Mixin(KeyBinding.class)
public class MixinKeyBinding {
	@Shadow private int timesPressed;
	@Shadow private InputUtil.Key boundKey;

	@Shadow @Final private static Map<String, KeyBinding> KEYS_BY_ID;
	@Unique private static final Multimap<InputUtil.Key, KeyBinding> KEYS_TO_BINDINGS = ArrayListMultimap.create();

	@Inject(method = "<init>(Ljava/lang/String;Lnet/minecraft/client/util/InputUtil$Type;ILjava/lang/String;)V", at = @At("TAIL"))
	private void saveConflictedBinds(String translationKey, InputUtil.Type type, int code, String category, CallbackInfo ci) {
		KEYS_TO_BINDINGS.put(boundKey, (KeyBinding) (Object) this);
	}

	@Inject(method = "onKeyPressed", at = @At("HEAD"), cancellable = true)
	private static void allowTabConflictedOnKeyPressed(InputUtil.Key key, CallbackInfo ci) {
		if (!key.equals(((MixinKeyBinding) (Object) InventoryTabs.NEXT_TAB).boundKey)) return;
		for (KeyBinding bind : KEYS_TO_BINDINGS.get(key)) {
			((MixinKeyBinding) (Object) bind).timesPressed++;
		}
		ci.cancel();
	}

	@Inject(method = "setKeyPressed", at = @At("HEAD"), cancellable = true)
	private static void allowTabConflictedSetKeyPressed(InputUtil.Key key, boolean pressed$, CallbackInfo ci) {
		if (!key.equals(((MixinKeyBinding) (Object) InventoryTabs.NEXT_TAB).boundKey)) return;
		for (KeyBinding bind : KEYS_TO_BINDINGS.get(key)) {
			bind.setPressed(pressed$);
		}
		ci.cancel();
	}

	@Inject(method = "updateKeysByCode", at = @At("HEAD"))
	private static void updateConflictedBinds(CallbackInfo ci) {
		KEYS_TO_BINDINGS.clear();
		for (KeyBinding bind : KEYS_BY_ID.values()) {
			KEYS_TO_BINDINGS.put(((MixinKeyBinding) (Object) bind).boundKey, bind);
		}
	}
}
