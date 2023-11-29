package folk.sisby.inventory_tabs.tabs;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.world.World;

public class VehicleInventoryTab extends EntityTab {
    public VehicleInventoryTab(Entity entity) {
        super(90, entity, false);
    }

    @Override
    public boolean open() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null || player.getVehicle() != entity) return false;
        player.networkHandler.sendPacket(new ClientCommandC2SPacket(player, ClientCommandC2SPacket.Mode.OPEN_INVENTORY));
        return true;
    }

    @Override
    public boolean shouldBeRemoved(World world, boolean current) {
        if (current) return false;
        return entity.isRemoved() || MinecraftClient.getInstance().player.getVehicle() != entity;
    }
}
