package folk.sisby.inventory_tabs;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import folk.sisby.inventory_tabs.providers.BlockTabProvider;
import folk.sisby.inventory_tabs.providers.ChestBlockTabProvider;
import folk.sisby.inventory_tabs.providers.EnderChestTabProvider;
import folk.sisby.inventory_tabs.providers.EntityTabProvider;
import folk.sisby.inventory_tabs.providers.ItemTabProvider;
import folk.sisby.inventory_tabs.providers.PlayerInventoryTabProvider;
import folk.sisby.inventory_tabs.providers.RegistryTabProvider;
import folk.sisby.inventory_tabs.providers.ShulkerBoxTabProvider;
import folk.sisby.inventory_tabs.providers.SimpleBlockTabProvider;
import folk.sisby.inventory_tabs.providers.SimpleEntityTabProvider;
import folk.sisby.inventory_tabs.providers.SimpleItemTabProvider;
import folk.sisby.inventory_tabs.providers.SneakEntityTabProvider;
import folk.sisby.inventory_tabs.providers.TabProvider;
import folk.sisby.inventory_tabs.providers.UniqueBlockTabProvider;
import folk.sisby.inventory_tabs.providers.UniqueItemTabProvider;
import folk.sisby.inventory_tabs.providers.VehicleInventoryTabProvider;
import folk.sisby.inventory_tabs.util.RegistryValue;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Holder;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class TabProviders {
    // Tab Provider Registry.
    public static final BiMap<Identifier, TabProvider> REGISTRY = HashBiMap.create();

    // Registry Providers - Can be expanded via #matches
    public static final ShulkerBoxTabProvider BLOCK_SHULKER_BOX = register(InventoryTabs.id("block_shulker_box"), new ShulkerBoxTabProvider());
    public static final EnderChestTabProvider BLOCK_ENDER_CHEST = register(InventoryTabs.id("block_ender_chest"), new EnderChestTabProvider());
    public static final ChestBlockTabProvider BLOCK_CHEST = register(InventoryTabs.id("block_chest"), new ChestBlockTabProvider());
    public static final SimpleBlockTabProvider BLOCK_SIMPLE = register(InventoryTabs.id("block_simple"), new SimpleBlockTabProvider());
    public static final UniqueBlockTabProvider BLOCK_UNIQUE = register(InventoryTabs.id("block_unique"), new UniqueBlockTabProvider());

    public static final SneakEntityTabProvider ENTITY_SNEAK = register(InventoryTabs.id("entity_sneak"), new SneakEntityTabProvider());
    public static final SimpleEntityTabProvider ENTITY_SIMPLE = register(InventoryTabs.id("entity_simple"), new SimpleEntityTabProvider());

    public static final ItemTabProvider ITEM_UNIQUE = register(InventoryTabs.id("item_unique"), new UniqueItemTabProvider());
    public static final ItemTabProvider ITEM_SIMPLE = register(InventoryTabs.id("item_simple"), new SimpleItemTabProvider());

    // Single-Purpose
    public static final PlayerInventoryTabProvider PLAYER_INVENTORY = register(InventoryTabs.id("player_inventory"), new PlayerInventoryTabProvider());
    public static final VehicleInventoryTabProvider VEHICLE_INVENTORY = register(InventoryTabs.id("vehicle_inventory"), new VehicleInventoryTabProvider());

    public static Set<EntityType<?>> warmEntities;

    public static void reload(DynamicRegistryManager manager) {
        InventoryTabs.LOGGER.info("[InventoryTabs] Reloading tab providers.");
        refreshConfigPlaceholders();
        reloadRegistryProviders(manager, RegistryKeys.BLOCK, getProviders(BlockTabProvider.class), InventoryTabs.CONFIG.blockProviderOverrides);
        warmEntities = reloadRegistryProviders(manager, RegistryKeys.ENTITY_TYPE, getProviders(EntityTabProvider.class), InventoryTabs.CONFIG.entityProviderOverrides);
        reloadRegistryProviders(manager, RegistryKeys.ITEM, getProviders(ItemTabProvider.class), InventoryTabs.CONFIG.itemProviderOverrides);
        TabManager.clearTabs();
        InventoryTabs.LOGGER.info("[InventoryTabs] Finished reloading tab providers.");
    }

    public static <T extends RegistryTabProvider<?>> Map<Identifier, T> getProviders(Class<T> clazz) {
        return REGISTRY.values().stream().filter(p -> clazz.isAssignableFrom(p.getClass())).sorted(Comparator.comparingInt(p -> ((T) p).getPriority()).reversed()).collect(Collectors.toMap(p -> REGISTRY.inverse().get(p), p -> (T) p, (a, b) -> b, LinkedHashMap::new));
    }

    private static <T> Set<T> reloadRegistryProviders(DynamicRegistryManager manager, RegistryKey<Registry<T>> registryKey, Map<Identifier, ? extends RegistryTabProvider<T>> providers, Map<String, String> overrideConfig) {
        Set<T> warmValues = new HashSet<>();
        providers.values().forEach(p -> p.values.clear());
        // Construct override map
        Map<RegistryValue<T>, RegistryTabProvider<T>> overrides = new HashMap<>();
        for (Map.Entry<String, String> override : overrideConfig.entrySet()) {
            RegistryValue<T> registryValue = RegistryValue.fromRegistryString(manager, registryKey, override.getKey());
            if (registryValue == null) {
                InventoryTabs.LOGGER.warn("[Inventory Tabs] Unknown override registry value ID {}, skipping...", override.getKey());
                continue;
            }
            if (override.getValue().isEmpty()) {
                overrides.put(registryValue, null);
                continue;
            }
            if (Identifier.tryParse(override.getValue()) == null || providers.get(Identifier.tryParse(override.getValue())) == null) {
                InventoryTabs.LOGGER.warn("[Inventory Tabs] Unknown override tab provider ID {}, skipping...", override.getValue());
                continue;
            }
            overrides.put(registryValue, providers.get(new Identifier(override.getValue())));
        }
        // Add values to providers
        for (Map.Entry<RegistryKey<T>, T> entry : manager.get(registryKey).getEntries()) {
            Holder<T> holder = manager.getHolderProvider().getLookup(registryKey).orElseThrow().getHolderOrThrow(entry.getKey());

            Optional<Map.Entry<RegistryValue<T>, RegistryTabProvider<T>>> override = overrides.entrySet().stream().filter(e -> e.getKey().is(holder)).findFirst();
            if (override.isPresent()) {
                if (override.get().getValue() != null) override.get().getValue().values.add(entry.getValue());
                continue;
            }
            for (Map.Entry<Identifier, ? extends RegistryTabProvider<T>> provider : providers.entrySet()) {
                if (!InventoryTabs.CONFIG.registryProviderDefaults.getOrDefault(provider.getKey().toString(), true))
                    continue;
                if (provider.getValue().consumes(entry.getValue())) break;
            }
            warmValues.add(entry.getValue());
        }
        return warmValues;
    }

    private static void refreshConfigPlaceholders() {
        Map<String, Boolean> tempMap = new HashMap<>(InventoryTabs.CONFIG.registryProviderDefaults);
        InventoryTabs.CONFIG.registryProviderDefaults.clear();
        TabProviders.REGISTRY.keySet().forEach(id -> InventoryTabs.CONFIG.registryProviderDefaults.put(id.toString(), tempMap.getOrDefault(id.toString(), true)));
    }

    public static <T extends TabProvider> T register(Identifier id, T tabProvider) {
        REGISTRY.put(id, tabProvider);
        return tabProvider;
    }
}