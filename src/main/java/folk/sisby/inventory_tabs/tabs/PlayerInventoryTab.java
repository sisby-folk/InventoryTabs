package folk.sisby.inventory_tabs.tabs;

import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;

public class PlayerInventoryTab implements Tab {
    public static final Text TITLE = Text.translatable("gui.inventory_tabs.tab.inventory");
    public ItemStack itemStack;

    public PlayerInventoryTab() {
        itemStack = new ItemStack(Blocks.PLAYER_HEAD);
        itemStack.getOrCreateNbt().putString("SkullOwner", MinecraftClient.getInstance().player.getGameProfile().getName());
    }

    @Override
    public boolean open() {
        MinecraftClient client = MinecraftClient.getInstance();
        client.setScreen(new InventoryScreen(client.player));
        return true;
    }

    @Override
    public boolean shouldBeRemoved(World world, boolean current) {
        return false;
    }

    @Override
    public Text getHoverText() {
        return TITLE;
    }

    @Override
    public int getPriority() {
        return 100;
    }

    @Override
    public ItemStack getTabIcon() {
        return itemStack;
    }

    @Override
    public boolean equals(Object other) {
        return other != null && getClass() == other.getClass();
    }
}
