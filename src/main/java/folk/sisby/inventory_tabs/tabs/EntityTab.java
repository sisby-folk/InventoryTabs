package folk.sisby.inventory_tabs.tabs;

import folk.sisby.inventory_tabs.util.PlayerUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractionWithEntityC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.Objects;

public class EntityTab implements Tab {
    public final int priority;
    public final Entity entity;
    public final boolean sneakInteract;
    public ItemStack itemStack;

    public EntityTab(int priority, Entity entity, boolean sneakInteract) {
        this.priority = priority;
        this.entity = entity;
        this.sneakInteract = sneakInteract;
        this.itemStack = entity.getPickBlockStack() != null ? entity.getPickBlockStack() : Items.BARRIER.getDefaultStack();
        refreshPreviewStack();
    }

    @Override
    public boolean open() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null || player.getVehicle() == entity) return false;
        player.networkHandler.sendPacket(PlayerInteractionWithEntityC2SPacket.interact(entity, sneakInteract, player.getActiveHand()));
        if (sneakInteract) player.networkHandler.sendPacket(new ClientCommandC2SPacket(player, ClientCommandC2SPacket.Mode.RELEASE_SHIFT_KEY));
        return true;
    }

    @Override
    public boolean shouldBeRemoved(World world, boolean current) {
        if (current) return false;
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        return entity.isRemoved() || player.getVehicle() == entity || !PlayerUtil.inRange(player, entity);
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public Text getHoverText() {
        return entity.hasCustomName() ? entity.getCustomName().copy().formatted(Formatting.ITALIC) : entity.getName();
    }

    @Override
    public ItemStack getTabIcon() {
        return itemStack;
    }

    protected void refreshPreviewStack() {
    }

    @Override
    public boolean equals(Object other) {
        return other != null && getClass() == other.getClass() && Objects.equals(entity.getUuid(), ((EntityTab) other).entity.getUuid());
    }
}
