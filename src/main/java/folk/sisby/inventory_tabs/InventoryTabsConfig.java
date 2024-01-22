package folk.sisby.inventory_tabs;

import folk.sisby.kaleido.api.WrappedConfig;
import folk.sisby.kaleido.lib.quiltconfig.api.annotations.Comment;
import folk.sisby.kaleido.lib.quiltconfig.api.values.ValueMap;

import java.util.Map;

public class InventoryTabsConfig extends WrappedConfig {
    @Comment("How many ticks to wait before moving to the next tab when the keybind is held")
    @Comment("Used to smooth out the visual difference between instant screens (like the player inventory) and ping-dependent screens")
    @Comment("Can be lowered for shenanigans")
    public final Integer holdTabCooldown = 3;

    @Comment("Whether to allow interacting with entities through blocks")
    @Comment("More seamless, but may be considered cheating by the server")
    public final Boolean ignoreWalls = true;

    @Comment("Whether to rotate the player towards the clicked tab")
    @Comment("Less seamless, but trips anti-cheat less often")
    public final Boolean rotatePlayer = false;

    @Comment("Remove the 1 unit padding in container screens them slightly taller and offset from other screens")
    @Comment("If you're seeing stacks in the wrong places (offset up by 1 unit) on modded containers, turn this off")
    public final Boolean consistentContainers = true;

    @Comment("Strip titles from 6-row container screens to fit them onto minimum-ratio viewports")
    public final Boolean compactLargeContainers = false;

    @Comment("Emits helpful information for setting up this config when joining a world")
    @Comment("Logs all registered screen handler IDs for use in screen overrides")
    @Comment("Logs all registry tab provider contents (blocks etc) to help find bad tabs")
    public final Boolean configLogging = false;

    @Comment("Whether to show tabs on screens that aren't specified below")
    public final Boolean allowScreensByDefault = true;

    @Comment("")
    @Comment("-------------------------------")
    @Comment("")
    @Comment("Manually set whether tabs should be shown on a given screen")
    @Comment("")
    public final Map<String, Boolean> screenOverrides = ValueMap.builder(true)
            .put("minecraft:smoker", true)
            .build();

    @Comment("")
    @Comment("-------------------------------")
    @Comment("")
    @Comment("Manually set the left bound offset for a given screen")
    @Comment("Positive values expand the left boundary, allowing more tabs to be drawn")
    @Comment("")
    public final Map<String, Integer> leftBoundOffsetOverride = ValueMap.builder(0).put("null", 0).build();

    @Comment("")
    @Comment("-------------------------------")
    @Comment("")
    @Comment("Manually set the right bound offset for a given screen")
    @Comment("Positive values expand the right boundary, allowing more tabs to be drawn")
    @Comment("")
    public final Map<String, Integer> rightBoundOffsetOverride = ValueMap.builder(0).put("null", 0).build();

    @Comment("")
    @Comment("-------------------------------")
    @Comment("")
    @Comment("Manually set the Tab Provider for a given block or tag")
    @Comment("| -Provider ID-        | -Tab Behaviour-                            | -Default Blocks Added-")
    @Comment("| block_shulker_box    | Checks shulker blocked state               | Adds all ShulkerBoxBlocks")
    @Comment("| block_ender_chest    | Unique, checks chest blocked state         | Adds all EnderChestBlocks")
    @Comment("| block_chest          | L/R Multiblock, checks chest blocked state | Adds all AbstractChestBlocks")
    @Comment("| block_unique         | Only one tab can exist per block type      | Adds all CraftingTableBlocks, AnvilBlocks, CartographyTableBlocks, GrindstoneBlocks, LoomBlocks, and StonecutterBlocks")
    @Comment("| block_simple_storage | Sorted alongside other chest-like tabs     | Adds all BarrelBlocks")
    @Comment("| block_simple         | No special logic                           | Adds all BlockWithEntities, except 25 known-bad vanilla block classes - this adds lots of invalid modded blocks!")
    @Comment("| \"\"                   | No tab at all!                             | Use this to disable bad block tabs in your pack")
    @Comment("")
    public final Map<String, String> blockProviderOverrides = ValueMap.builder("")
            .put("minecraft:crafting_table", "inventory_tabs:block_unique")
            .put("minecraft:fletching_table", "")
            .build();

    @Comment("")
    @Comment("-------------------------------")
    @Comment("")
    @Comment("Manually set the Tab Provider for a given entity or tag")
    @Comment("| -Provider ID- | -Tab Behaviour-                          | -Default Entities Added-")
    @Comment("| entity_sneak  | Uses sneak-interact, checks tamed/riding | Adds all RideableOpenableInventories")
    @Comment("| entity_simple | Checks player isn't riding the entity    | Adds all StorageMinecartEntities")
    @Comment("| \"\"            | No tab at all!                           | Use this to disable bad entity tabs in your pack")
    @Comment("")
    public final Map<String, String> entityProviderOverrides = ValueMap.builder("")
            .put("minecraft:chest_minecart", "inventory_tabs:entity_simple")
            .build();

    @Comment("")
    @Comment("-------------------------------")
    @Comment("")
    @Comment("Manually set the Tab Provider for a given item or tag")
    @Comment("| -Provider ID- | -Tab Behaviour-                      | -Default Items Added-")
    @Comment("| item_unique   | Only one tab can exist per item type | Adds nothing")
    @Comment("| item_simple   | No special logic                     | Adds nothing")
    @Comment("| \"\"            | No tab at all!                       | Use this to disable bad item tabs in your pack")
    @Comment("")
    public final Map<String, String> itemProviderOverrides = ValueMap.builder("")
            .put("minecraft:dirt", "")
            .build();

    @Comment("")
    @Comment("-------------------------------")
    @Comment("")
    @Comment("Enable or disable the default added logic for each tab provider (see above)")
    @Comment("Non-registry Tab Providers like player_inventory and vehicle_inventory can't be disabled here")
    @Comment("")
    public final Map<String, Boolean> registryProviderDefaults = ValueMap.builder(true)
            .put("inventory_tabs:block_simple", true)
            .build();
}
