package folk.sisby.inventory_tabs.util;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;

public class CursorStackUtil {
    private static int stashSlot = -1;

    public static void push(ClientPlayerEntity player, ClientPlayerInteractionManager manager, ScreenHandler handler) {
        if (!handler.getCursorStack().isEmpty()) {
            stashSlot = player.getInventory().getEmptySlot();
            if (stashSlot != -1 && manager != null) {
                // Put the cursor stack there
                handler.getSlotIndex(player.getInventory(), stashSlot).ifPresent((screenSlot) -> manager.clickSlot(
                        handler.syncId,
                        screenSlot,
                        0, // Mouse Left Click
                        SlotActionType.PICKUP,
                        player
                ));
            }
        }
    }

    public static void tryPop(ClientPlayerEntity player, ClientPlayerInteractionManager manager, ScreenHandler handler) {
        if (stashSlot != -1) {
            handler.getSlotIndex(player.getInventory(), stashSlot).ifPresent((screenSlot) -> manager.clickSlot(
                    handler.syncId,
                    screenSlot,
                    0, // Mouse Left Click
                    SlotActionType.PICKUP,
                    player
            ));
            stashSlot = -1;
        }
    }
}
