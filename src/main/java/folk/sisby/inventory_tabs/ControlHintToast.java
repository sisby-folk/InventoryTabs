package folk.sisby.inventory_tabs;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class ControlHintToast extends SystemToast {
    protected Text keyHint;
    protected KeyBinding keyBinding;

    public ControlHintToast(Text title, KeyBinding keybinding)
    {
        super(Type.PERIODIC_NOTIFICATION, title, null);
        this.keyBinding = keybinding;

        keyHint = Text.translatable("toast.inventory_tabs.disabled.key_hint", keyBinding.getBoundKeyLocalizedText().copy().formatted(Formatting.GOLD)).formatted(Formatting.WHITE);
        setContent(title, keyHint);
    }
}
