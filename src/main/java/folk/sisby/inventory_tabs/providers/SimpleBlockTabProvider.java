package folk.sisby.inventory_tabs.providers;

import folk.sisby.inventory_tabs.InventoryTabs;
import folk.sisby.inventory_tabs.tabs.BlockTab;
import folk.sisby.inventory_tabs.tabs.Tab;
import net.minecraft.block.AbstractBannerBlock;
import net.minecraft.block.AbstractSkullBlock;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BeehiveBlock;
import net.minecraft.block.BellBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.ChiseledBookshelfBlock;
import net.minecraft.block.CommandBlock;
import net.minecraft.block.ComparatorBlock;
import net.minecraft.block.ConduitBlock;
import net.minecraft.block.DaylightDetectorBlock;
import net.minecraft.block.EndGatewayBlock;
import net.minecraft.block.EndPortalBlock;
import net.minecraft.block.JigsawBlock;
import net.minecraft.block.JukeboxBlock;
import net.minecraft.block.SpawnerBlock;
import net.minecraft.block.StructureBlock;
import net.minecraft.block.piston.PistonExtensionBlock;
import net.minecraft.block.sculk.SculkSensorBlock;
import net.minecraft.block.sign.AbstractSignBlock;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class SimpleBlockTabProvider extends BlockTabProvider {
    public final Map<Identifier, Predicate<Block>> blacklist = new HashMap<>();

    public SimpleBlockTabProvider() {
        blacklist.put(InventoryTabs.id("abstract_banner_block"), b -> b instanceof AbstractBannerBlock);
        blacklist.put(InventoryTabs.id("abstract_sign_block"), b -> b instanceof AbstractSignBlock);
        blacklist.put(InventoryTabs.id("abstract_skull_block"), b -> b instanceof AbstractSkullBlock);
        blacklist.put(InventoryTabs.id("beehive_block"), b -> b instanceof BeehiveBlock);
        blacklist.put(InventoryTabs.id("bed_block"), b -> b instanceof BedBlock);
        blacklist.put(InventoryTabs.id("bell_block"), b -> b instanceof BellBlock);
        blacklist.put(InventoryTabs.id("campfire_block"), b -> b instanceof CampfireBlock);
        blacklist.put(InventoryTabs.id("command_block"), b -> b instanceof CommandBlock);
        blacklist.put(InventoryTabs.id("comparator_block"), b -> b instanceof ComparatorBlock);
        blacklist.put(InventoryTabs.id("conduit_block"), b -> b instanceof ConduitBlock);
        blacklist.put(InventoryTabs.id("daylight_detector_block"), b -> b instanceof DaylightDetectorBlock);
        blacklist.put(InventoryTabs.id("end_gateway_block"), b -> b instanceof EndGatewayBlock);
        blacklist.put(InventoryTabs.id("end_portal_block"), b -> b instanceof EndPortalBlock);
        blacklist.put(InventoryTabs.id("jigsaw_block"), b -> b instanceof JigsawBlock);
        blacklist.put(InventoryTabs.id("jukebox_block"), b -> b instanceof JukeboxBlock);
        blacklist.put(InventoryTabs.id("piston_extension_block"), b -> b instanceof PistonExtensionBlock);
        blacklist.put(InventoryTabs.id("sculk_sensor_block"), b -> b instanceof SculkSensorBlock);
        blacklist.put(InventoryTabs.id("spawner_block"), b -> b instanceof SpawnerBlock);
        blacklist.put(InventoryTabs.id("structure_block"), b -> b instanceof StructureBlock);
        blacklist.put(InventoryTabs.id("chiseled_bookshelf_block"), b -> b instanceof ChiseledBookshelfBlock);
        matches.put(InventoryTabs.id("block_entity_provider_blacklist"), b -> b instanceof BlockEntityProvider && blacklist.values().stream().noneMatch(p -> p.test(b)));
    }

    @Override
    public Tab createTab(World world, BlockPos pos) {
        return new BlockTab(30, world, pos);
    }

    @Override
    public int getPriority() {
        return 0;
    }
}
