package folk.sisby.inventory_tabs;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.text.Text;

public class ControlHintToast implements Toast {
    protected Text title;
    protected Text keyHint;
    protected KeyBinding keyBinding;

    public ControlHintToast(Text title, KeyBinding keybinding)
    {
        this.title = title;
        this.keyBinding = keybinding;
        keyHint = keyBinding.getBoundKeyLocalizedText();
    }


    @Override
    public Visibility draw(DrawContext context, ToastManager manager, long startTime) {
        context.drawNineSlicedTexture(TEXTURE, 0, 0, getWidth(), getHeight(), 4, 160, 32, 0, 0);
        context.drawText(manager.getClient().textRenderer, title, 32, 7, 0xFFFFFF, false);
        context.drawText(manager.getClient().textRenderer, keyHint, 32, 18, 0xFFFFFF, false);

        return Visibility.SHOW;
    }
}
