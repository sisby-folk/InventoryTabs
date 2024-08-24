package folk.sisby.inventory_tabs.mixin;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(HandledScreen.class)
public interface HandledScreenAccessor {
	@Accessor("x")
	int getX();

	@Accessor("y")
	int getY();

	@Accessor("backgroundWidth")
	int getBackgroundWidth();

	@Accessor("backgroundHeight")
	int getBackgroundHeight();

	@Invoker("drawItem")
	void drawItem(ItemStack stack, int x, int y, String amountText);
}
