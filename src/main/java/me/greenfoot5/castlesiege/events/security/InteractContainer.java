package me.greenfoot5.castlesiege.events.security;

import me.greenfoot5.castlesiege.database.CSActiveData;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryHolder;

import java.util.Arrays;
import java.util.Collection;

/**
 * Prevents players from opening containers and interacting with certain other blocks
 */
public class InteractContainer implements Listener {

    // A collection of all blocks that cannot be interacted with
    private static final Collection<Material> bannedBlocks = Arrays.asList(Material.ANVIL, Material.CARTOGRAPHY_TABLE,
            Material.CHIPPED_ANVIL, Material.CRAFTING_TABLE, Material.DAMAGED_ANVIL,
            Material.DAYLIGHT_DETECTOR, Material.ENCHANTING_TABLE, Material.ENDER_CHEST,
            Material.FLETCHING_TABLE, Material.FLOWER_POT, Material.GRINDSTONE,
            Material.LOOM, Material.SMITHING_TABLE, Material.STONECUTTER);

    /**
     * Cancels event when player interacts inventory holder or other banned block
     * @param e The event called when a player right-clicks a block or entity
     */
    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        // Allow interacting in creative mode
        if (e.getPlayer().getGameMode() == GameMode.CREATIVE || !CSActiveData.hasPlayer(e.getPlayer().getUniqueId())) {
            return;
        }

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            assert e.getClickedBlock() != null;
            if (e.getClickedBlock().getState() instanceof InventoryHolder ||
                    bannedBlocks.contains(e.getClickedBlock().getType()) || e.getClickedBlock().getState() instanceof Sign)  {
                e.setCancelled(true);
            }
        }
    }

    /**
     * Cancels event when player interacts with a sign to change what is on it.
     * @param e The event called when a player attempts to edit a sign.
     */
    @EventHandler
    public void onInteract(SignChangeEvent e) {
        // Allow interacting in creative mode
        if (e.getPlayer().getGameMode() != GameMode.CREATIVE) {
            e.setCancelled(true);
        }
    }
}
