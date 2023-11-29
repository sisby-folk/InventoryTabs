package folk.sisby.inventory_tabs;

import folk.sisby.kaleido.api.WrappedConfig;
import folk.sisby.kaleido.lib.quiltconfig.api.annotations.Comment;
import folk.sisby.kaleido.lib.quiltconfig.api.values.ValueList;
import folk.sisby.kaleido.lib.quiltconfig.api.values.ValueMap;

import java.util.List;
import java.util.Map;

public class InventoryTabsConfig extends WrappedConfig {
    public enum AllowDeny {
        ALLOW,
        DENY
    }

    @Comment("Whether to allow interacting with entities through blocks.")
    @Comment("More seamless, but may be considered cheating by the server.")
    public final Boolean ignoreWalls = true;

    @Comment("Whether to rotate the player towards the clicked tab")
    @Comment("Less seamless, but trips anti-cheat less often.")
    public final Boolean rotatePlayer = false;

    @Comment("Remove the 1 unit padding in container screens that makes single chest screens slightly taller")
    @Comment("If a modded container has item stacks offset 1 unit below where they should, turn this off.")
    public final Boolean consistentChests = true;

    @Comment("Strip nonessential features from 6-row container screens to fit them onto minimum-ratio viewports.")
    public final Boolean compactChests = false;

    @Comment("Screens where the tab row should be rendered.")
    public final ScreenFilter tabDisplayFilter = new ScreenFilter(AllowDeny.ALLOW, ValueList.create(""
    ), ValueList.create(""
    ));

    public static class ScreenFilter implements Section {
        @Comment("Whether to allow or deny tab display on screens that don't match config or code filters.")
        public final AllowDeny fallback;

        @Comment("Screen Type IDs to deny rendering tabs on.")
        @Comment("Screens allowed through code take precedent over this, e.g. HorseScreen.")
        public final List<String> deny;

        @Comment("Screen Type IDs to allow rendering tabs on.")
        @Comment("Screens denied through code take precedent over this, e.g. CreativeInventoryScreen.")
        public final List<String> allow;

        public ScreenFilter(AllowDeny fallback, List<String> deny, List<String> allow) {
            this.fallback = fallback;
            this.deny = deny;
            this.allow = allow;
        }
    }

    @Comment("")
    @Comment("Allows forcing blocks or block tags to be handled by a specific tab provider. '' to disable.")
    public final Map<String, String> blockProviderOverrides = ValueMap.builder("")
            .put("minecraft:fletching_table", "")
            .build();

    @Comment("")
    @Comment("Allows forcing entities or entity tags to be handled by a specific tab provider. '' to disable.")
    public final Map<String, String> entityProviderOverrides = ValueMap.builder("")
            .put("minecraft:chest_minecart", "inventory_tabs:entity_simple")
            .build();

    @Comment("")
    @Comment("Allows forcing items or item tags to be handled by a specific tab provider. Use NONE to disable.")
    public final Map<String, String> itemProviderOverrides = ValueMap.builder("")
            .put("minecraft:dirt", "")
            .build();

    @Comment("Whether to enable the default registry-matching logic for each tab provider.")
    @Comment("When default logic is disabled, a tab provider will only be used if overridden below!")
    @Comment("Simple Block: Blocks with block entities that can be interacted with to open a screen.")
    @Comment("-Default logic: All BlockEntityProviders that don't match a list of known-bad types are added.")
    @Comment("Chest Block: Must have facing and single/left/right properties, and can be blocked above.")
    @Comment("-Default logic: All AbstractChestBlocks are added.")
    @Comment("Unique Block: Blocks that open the same screen, regardless of where they are.")
    @Comment("-Default logic: Blocks from a list of known-good vanilla types are added.")
    @Comment("Simple Entity: Entity that can be interacted with to open a screen.")
    @Comment("-Default logic: Nothing is added.")
    @Comment("Sneak Entity: Entity that can be interacted with while sneaking to open a screen.")
    @Comment("-Default logic: Nothing is added.")
    public final Map<String, Boolean> registryProviderDefaults = ValueMap.builder(true).build();
}
