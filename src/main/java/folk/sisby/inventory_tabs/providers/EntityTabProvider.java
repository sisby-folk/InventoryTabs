package folk.sisby.inventory_tabs.providers;

import folk.sisby.inventory_tabs.TabProviders;
import folk.sisby.inventory_tabs.tabs.EntityTab;
import folk.sisby.inventory_tabs.tabs.Tab;
import folk.sisby.inventory_tabs.util.PlayerUtil;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class EntityTabProvider extends RegistryTabProvider<EntityType<?>> {
    public final Map<Identifier, Predicate<Entity>> warmMatches = new HashMap<>();
    public final Set<EntityType<?>> failedMatches = new HashSet<>();
    public final Map<Identifier, Predicate<Entity>> preclusions = new HashMap<>();

    @Override
    public void addAvailableTabs(ClientPlayerEntity player, Consumer<Tab> addTab) {
        World world = player.getWorld();
        for (Entity entity : world.getNonSpectatingEntities(Entity.class, Box.of(player.getPos(), PlayerUtil.REACH * 2, PlayerUtil.REACH * 2, PlayerUtil.REACH * 2))) {
            EntityType<?> type = entity.getType();
            if (!values.contains(type) && !failedMatches.contains(type)) {
                if (TabProviders.warmEntities.contains(type) && warmMatches.values().stream().anyMatch(t -> t.test(entity))) {
                    TabProviders.warmEntities.remove(type);
                    values.add(type);
                } else {
                    failedMatches.add(type);
                }
            }
            if ((values.contains(type) && preclusions.values().stream().noneMatch(p -> p.test(entity)))) {
                if (!PlayerUtil.inRange(player, entity)) continue;
                addTab.accept(createTab(entity));
            }
        }
    }

    public Tab createTab(Entity entity) {
        return new EntityTab(40, entity, false);
    }
}
