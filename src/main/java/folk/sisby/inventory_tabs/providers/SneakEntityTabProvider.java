package folk.sisby.inventory_tabs.providers;

import folk.sisby.inventory_tabs.InventoryTabs;
import folk.sisby.inventory_tabs.tabs.EntityTab;
import folk.sisby.inventory_tabs.tabs.Tab;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.HorseBaseEntity;

public class SneakEntityTabProvider extends SimpleEntityTabProvider {
    public SneakEntityTabProvider () {
        super();
        warmMatches.put(InventoryTabs.id("horse_base_entity"), e -> e instanceof HorseBaseEntity);
        preclusions.put(InventoryTabs.id("untamed"), e -> e instanceof HorseBaseEntity h && !h.isTame());
    }

    public Tab createTab(Entity entity) {
        return new EntityTab(entity instanceof HorseBaseEntity ? 45 : 40, entity, true);
    }

    @Override
    public int getPriority() {
        return 30;
    }
}
