<!--suppress HtmlDeprecatedTag, XmlDeprecatedElement -->
<center><img alt="screenshot" src="https://cdn.modrinth.com/data/VD1aynYU/images/7feef9e9ba223616e0462725ea7f5d860aea946f.png" /></center>

<center>
Tabs to swap between nearby screens like chests, crafting stations, and even entities.<br/>
A fourth-generation rewrite of <a href="https://www.curseforge.com/minecraft/mc-mods/inventory-tabs">Inventory Tabs</a> by <a href="https://github.com/cakewhip">CakeWhip</a>, as continued by <a href="https://modrinth.com/user/Andrew6rant">Andrew6rant</a>.<br/>
Requires <a href="https://modrinth.com/mod/connector">Connector</a> and <a href="https://modrinth.com/mod/forgified-fabric-api">FFAPI</a> on forge. For creative mode, try <a href="https://modrinth.com/mod/sidekick">Sidekick</a>!<br/>
</center>

---

**Inventory Tabs** is a client-side mod that displays tabs on screens, each representing nearby blocks and entities.

Tap `tab` to cycle forward screens, `shift+tab` to cycle back, or just click around!

### Features

- Swap between crafting stations, storage, and your inventory without leaving their screens.
- Works out of the box with many modded blocks - with configuration in case anything goes wrong.
- Storage tabs can be labelled using signs, item frames, or (w/ server) anvil-renaming for organisation.
- Stacks held on your cursor carry over to the next screen as long as your inventory isn't full.

<center><img width=640 alt="Shulker Box Symphony" src="https://cdn.modrinth.com/data/VD1aynYU/images/3a5405eff19e20620cb757142aa040ca366fcb72.gif" /></center>

- Container screens are realigned with other screens, instead of being 1 pixel lower.
- Fits on-screen with [EMI](https://modrinth.com/mod/emi), even on very high GUI scales - Use `Left (Compressed)` effects.
<center><img width=640 alt="EMI VGA Preview" src="https://cdn.modrinth.com/data/VD1aynYU/images/7356435a874c5f5c587b59f4b71461da2e997df1.png" /></center>

#### Anti-Cheat & Fairness

Inventory Tabs offers no guarantees or defence against server moderation or anti-cheat - tabs provide an unfair advantage, and look outwardly suspicious to onlookers in multiplayer.<br/>
Notably, the mod allows some usually impossible actions for the sake of convenience, e.g.
  - The full player inventory can be accessed while riding a horse or chest boat.
  - Sneak-interact inventories can be accessed without dismounting from a vehicle.

---

### Modpack Configuration

Inventory Tabs 4 is designed from the ground up to be friendlier to modpacks.<br/>
It's configured via `config/inventory_tabs.toml`, which includes comments providing extra context - like what each tab provider does. 

By default, helpful information for setting up the mod for your modpack is logged when loading into a world. Toggle `configLogging` to disable this.

If tabs are appearing on a screen they don't fit well with, the screen can be blacklisted:

```toml
[screenOverrides]
	"fwaystones:waystone" = false
```

If tabs are being made for an inappropriate block, you can manually disable their tab provider:

```toml
[blockProviderOverrides]
	"cool_mod:incompatible_block" = ""
	"really_cool_mod:*" = ""
```

Or manually override it to a relevant one:

```toml
[blockProviderOverrides]
	"#cool_mod:crafting_stations" = "inventory_tabs:block_unique"
	"cool_mod:single_chest" = "inventory_tabs:block_simple"
	"cool_mod:*_cabinet" = "inventory_tabs:block_simple_storage"
	"cool_mod:doubleable_chest" = "inventory_tabs:block_chest"
```

Make sure you test your configuration! These options are _not_ intended to be crash-proof.

Overrides also work for entities and items, via `[entityProviderOverrides]` and `[itemProviderOverrides]` respectively.

If too many inappropriate blocks are being matched, you may want to disable the default matching logic for a provider entirely:

```toml
[registryProviderDefaults]
	"inventory_tabs:block_simple" = false
```

The `block_simple` provider uses a blacklist instead of a whitelist, so it generates a lot of false-positive tabs. It's enabled by default to help with finding good/bad tabs.

For a full-pack example, check out the config in [Tinkerer's Quilt Modded](https://github.com/sisby-folk/tinkerers-quilt/blob/1.20_modded/config/inventory_tabs.toml).  

---

### Mod Architecture

Each visible tab on the screen is a [Tab](https://github.com/sisby-folk/inventory-tabs/blob/1.20/src/main/java/folk/sisby/inventory_tabs/tabs/Tab.java), which is responsible for rendering itself, knowing how to be opened, and knowing when it should be removed from the list of tabs (e.g. when no longer in range).

These tabs are held by the [Tab Manager](https://github.com/sisby-folk/inventory-tabs/blob/1.20/src/main/java/folk/sisby/inventory_tabs/TabManager.java), which is effectively the "Tab Screen". It adds onto regular screen methods to do its own rendering, ticking, and mouse click handling. It's also responsible for checking if any new tabs should be added.

Tabs are added via registered [Tab Providers](https://github.com/sisby-folk/inventory-tabs/blob/1.20/src/main/java/folk/sisby/inventory_tabs/TabProviders.java). Basic providers like [Vehicle Inventory](https://github.com/sisby-folk/inventory-tabs/blob/1.20/src/main/java/folk/sisby/inventory_tabs/providers/VehicleInventoryTabProvider.java) check simple conditions and add a special tab. The more advanced [Registry Providers](https://github.com/sisby-folk/inventory-tabs/blob/1.20/src/main/java/folk/sisby/inventory_tabs/providers/RegistryTabProvider.java) are designed to be assigned a specific set of [blocks](https://github.com/sisby-folk/inventory-tabs/blob/1.20/src/main/java/folk/sisby/inventory_tabs/providers/BlockTabProvider.java), [entity types](https://github.com/sisby-folk/inventory-tabs/blob/1.20/src/main/java/folk/sisby/inventory_tabs/providers/EntityTabProvider.java), or [items](https://github.com/sisby-folk/inventory-tabs/blob/1.20/src/main/java/folk/sisby/inventory_tabs/providers/ItemTabProvider.java) that they're responsible for providing tabs for - which is frozen at reload time - and then searching for those around the player every tick. Using these generics, simpler concrete providers are made ([Ender Chests](https://github.com/sisby-folk/inventory-tabs/blob/1.20/src/main/java/folk/sisby/inventory_tabs/providers/EnderChestTabProvider.java), [Unique Block](https://github.com/sisby-folk/inventory-tabs/blob/1.20/src/main/java/folk/sisby/inventory_tabs/providers/UniqueBlockTabProvider.java).)


### Addons

```groovy
repositories {
	maven { url "https://repo.sleeping.town/" }
}
dependencies {
   modImplementation "folk.sisby:inventory-tabs:1.2.0"
}
```

Addons for Inventory Tabs 4 can add new tab types and tab providers, and add advanced matching logic to current providers, provide custom tab layouts for their screens (including inverted tabs), and signal when specific tabs should be marked as open.

For a practical example, check out [Portable Crafting](https://github.com/sisby-folk/portable-crafting)!

---

### Afterword

All mods are built on the work of many others - We're the fourth set of maintainers of this mod!<br/>
This version is a partial rewrite, following the work of CakeWhip, LiamMCW, and Andrew6rant.

This mod is included in [Tinkerer's Quilt](https://modrinth.com/modpack/tinkerers-quilt) - our modpack about rediscovering vanilla.

We're open to better ways to implement our mods. If you see something odd and have an idea, let us know!

---

<center>
<b>Tinkerer's:</b> <a href="https://modrinth.com/modpack/tinkerers-quilt">Quilt</a> - <a href="https://modrinth.com/mod/tinkerers-smithing">Smithing</a> - <a href="https://modrinth.com/mod/origins-minus">Origins</a> - <a href="https://modrinth.com/mod/tinkerers-statures">Statures</a> - <a href="https://modrinth.com/mod/picohud">HUD</a><br/>
<b>Loveletters:</b> <i>Tabs</i> - <a href="https://modrinth.com/mod/antique-atlas-4">Atlas</a> - <a href="https://modrinth.com/mod/portable-crafting">Portable Crafting</a> - <a href="https://modrinth.com/mod/drogstyle">Drogstyle</a><br/>
<b>Others:</b> <a href="https://modrinth.com/mod/switchy">Switchy</a> - <a href="https://modrinth.com/mod/crunchy-crunchy-advancements">Crunchy</a> - <a href="https://modrinth.com/mod/starcaller">Starcaller</a><br/>
</center>
