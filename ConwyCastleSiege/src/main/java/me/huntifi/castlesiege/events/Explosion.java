package me.huntifi.castlesiege.events;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.HashMap;

public class Explosion implements Listener {

    // Maps used to store the old states of destroyed stone and wood type blocks
    private static final HashMap<Location, BlockState> hybrid = new HashMap<>();
    private static final HashMap<Location, BlockState> stone = new HashMap<>();
    private static final HashMap<Location, BlockState> wood = new HashMap<>();

    /**
     * TODO
     */
    @EventHandler (priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onExplode(EntityExplodeEvent e) {
        for (Block block : e.blockList()) {
            register(block);
        }
    }

    /**
     * Register exploded blocks in their corresponding hashmap
     * @param block The exploded block
     */
    private void register(Block block) {
        switch (block.getType()) {
            // Hybrid block types
            case LEVER:
                hybrid.put(block.getLocation(), block.getState());
                break;

            // Stone block types
            case ANDESITE:
            case ANDESITE_SLAB:
            case ANDESITE_STAIRS:
            case ANDESITE_WALL:

            case BRICKS:
            case BRICK_SLAB:
            case BRICK_STAIRS:
            case BRICK_WALL:

            case CHISELED_QUARTZ_BLOCK:
            case CHISELED_RED_SANDSTONE:
            case CHISELED_SANDSTONE:
            case CHISELED_STONE_BRICKS:

            case COAL_BLOCK:
            case COAL_ORE:

            case COBBLESTONE:
            case COBBLESTONE_SLAB:
            case COBBLESTONE_STAIRS:
            case COBBLESTONE_WALL:

            case CUT_RED_SANDSTONE:
            case CUT_RED_SANDSTONE_SLAB:
            case CUT_SANDSTONE:
            case CUT_SANDSTONE_SLAB:

            case DARK_PRISMARINE:
            case DARK_PRISMARINE_SLAB:
            case DARK_PRISMARINE_STAIRS:

            case DIAMOND_BLOCK:
            case DIAMOND_ORE:

            case DIORITE:
            case DIORITE_SLAB:
            case DIORITE_STAIRS:
            case DIORITE_WALL:

            case EMERALD_BLOCK:
            case EMERALD_ORE:

            case END_STONE:
            case END_STONE_BRICKS:
            case END_STONE_BRICK_SLAB:
            case END_STONE_BRICK_STAIRS:
            case END_STONE_BRICK_WALL:
            case END_PORTAL_FRAME:

            case GOLD_BLOCK:
            case GOLD_ORE:

            case GRANITE:
            case GRANITE_SLAB:
            case GRANITE_STAIRS:
            case GRANITE_WALL:

            case HEAVY_WEIGHTED_PRESSURE_PLATE:

            case IRON_BARS:
            case IRON_BLOCK:
            case IRON_DOOR:
            case IRON_ORE:
            case IRON_TRAPDOOR:

            case LAPIS_BLOCK:
            case LAPIS_ORE:

            case LIGHT_WEIGHTED_PRESSURE_PLATE:

            case MOSSY_COBBLESTONE:
            case MOSSY_COBBLESTONE_SLAB:
            case MOSSY_COBBLESTONE_STAIRS:
            case MOSSY_COBBLESTONE_WALL:

            case MOSSY_STONE_BRICKS:
            case MOSSY_STONE_BRICK_SLAB:
            case MOSSY_STONE_BRICK_STAIRS:
            case MOSSY_STONE_BRICK_WALL:

            case NETHERRACK:
            case NETHER_BRICKS:
            case NETHER_BRICK_FENCE:
            case NETHER_BRICK_SLAB:
            case NETHER_BRICK_STAIRS:
            case NETHER_BRICK_WALL:
            case NETHER_QUARTZ_ORE:

            case POLISHED_ANDESITE:
            case POLISHED_ANDESITE_SLAB:
            case POLISHED_ANDESITE_STAIRS:

            case POLISHED_DIORITE:
            case POLISHED_DIORITE_SLAB:
            case POLISHED_DIORITE_STAIRS:

            case POLISHED_GRANITE:
            case POLISHED_GRANITE_SLAB:
            case POLISHED_GRANITE_STAIRS:

            case PRISMARINE:
            case PRISMARINE_SLAB:
            case PRISMARINE_STAIRS:
            case PRISMARINE_WALL:

            case PRISMARINE_BRICKS:
            case PRISMARINE_BRICK_SLAB:
            case PRISMARINE_BRICK_STAIRS:

            case QUARTZ_BLOCK:
            case QUARTZ_PILLAR:
            case QUARTZ_SLAB:
            case QUARTZ_STAIRS:

            case REDSTONE_BLOCK:
            case REDSTONE_ORE:

            case RED_NETHER_BRICKS:
            case RED_NETHER_BRICK_SLAB:
            case RED_NETHER_BRICK_STAIRS:
            case RED_NETHER_BRICK_WALL:

            case RED_SANDSTONE:
            case RED_SANDSTONE_SLAB:
            case RED_SANDSTONE_STAIRS:
            case RED_SANDSTONE_WALL:

            case SANDSTONE:
            case SANDSTONE_SLAB:
            case SANDSTONE_STAIRS:
            case SANDSTONE_WALL:

            case SMOOTH_QUARTZ:
            case SMOOTH_QUARTZ_SLAB:
            case SMOOTH_QUARTZ_STAIRS:

            case SMOOTH_RED_SANDSTONE:
            case SMOOTH_RED_SANDSTONE_SLAB:
            case SMOOTH_RED_SANDSTONE_STAIRS:

            case SMOOTH_SANDSTONE:
            case SMOOTH_SANDSTONE_SLAB:
            case SMOOTH_SANDSTONE_STAIRS:

            case SMOOTH_STONE:
            case SMOOTH_STONE_SLAB:

            case STONE:
            case STONE_BUTTON:
            case STONE_PRESSURE_PLATE:
            case STONE_SLAB:
            case STONE_STAIRS:

            case STONE_BRICKS:
            case STONE_BRICK_SLAB:
            case STONE_BRICK_STAIRS:
            case STONE_BRICK_WALL:
                stone.put(block.getLocation(), block.getState());
                break;

            // Wood block types
            case ACACIA_BUTTON:
            case ACACIA_DOOR:
            case ACACIA_FENCE:
            case ACACIA_FENCE_GATE:
            case ACACIA_LOG:
            case ACACIA_PLANKS:
            case ACACIA_PRESSURE_PLATE:
            case ACACIA_SIGN:
            case ACACIA_SLAB:
            case ACACIA_STAIRS:
            case ACACIA_TRAPDOOR:
            case ACACIA_WALL_SIGN:
            case ACACIA_WOOD:

            case BIRCH_BUTTON:
            case BIRCH_DOOR:
            case BIRCH_FENCE:
            case BIRCH_FENCE_GATE:
            case BIRCH_LOG:
            case BIRCH_PLANKS:
            case BIRCH_PRESSURE_PLATE:
            case BIRCH_SIGN:
            case BIRCH_SLAB:
            case BIRCH_STAIRS:
            case BIRCH_TRAPDOOR:
            case BIRCH_WALL_SIGN:
            case BIRCH_WOOD:

            case DARK_OAK_BUTTON:
            case DARK_OAK_DOOR:
            case DARK_OAK_FENCE:
            case DARK_OAK_FENCE_GATE:
            case DARK_OAK_LOG:
            case DARK_OAK_PLANKS:
            case DARK_OAK_PRESSURE_PLATE:
            case DARK_OAK_SIGN:
            case DARK_OAK_SLAB:
            case DARK_OAK_STAIRS:
            case DARK_OAK_TRAPDOOR:
            case DARK_OAK_WALL_SIGN:
            case DARK_OAK_WOOD:

            case JUNGLE_BUTTON:
            case JUNGLE_DOOR:
            case JUNGLE_FENCE:
            case JUNGLE_FENCE_GATE:
            case JUNGLE_LOG:
            case JUNGLE_PLANKS:
            case JUNGLE_PRESSURE_PLATE:
            case JUNGLE_SIGN:
            case JUNGLE_SLAB:
            case JUNGLE_STAIRS:
            case JUNGLE_TRAPDOOR:
            case JUNGLE_WALL_SIGN:
            case JUNGLE_WOOD:

            case OAK_BUTTON:
            case OAK_DOOR:
            case OAK_FENCE:
            case OAK_FENCE_GATE:
            case OAK_LOG:
            case OAK_PLANKS:
            case OAK_PRESSURE_PLATE:
            case OAK_SIGN:
            case OAK_SLAB:
            case OAK_STAIRS:
            case OAK_TRAPDOOR:
            case OAK_WALL_SIGN:
            case OAK_WOOD:

            case SPRUCE_BUTTON:
            case SPRUCE_DOOR:
            case SPRUCE_FENCE:
            case SPRUCE_FENCE_GATE:
            case SPRUCE_LOG:
            case SPRUCE_PLANKS:
            case SPRUCE_PRESSURE_PLATE:
            case SPRUCE_SIGN:
            case SPRUCE_SLAB:
            case SPRUCE_STAIRS:
            case SPRUCE_TRAPDOOR:
            case SPRUCE_WALL_SIGN:
            case SPRUCE_WOOD:
                wood.put(block.getLocation(), block.getState());
                break;
        }
    }
}
