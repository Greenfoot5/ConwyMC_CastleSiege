package me.greenfoot5.castlesiege.events.combat;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Modifies the fall damage taken by players
 */
public class FallDamage implements Listener {

    /**
     * Set the custom fall damage for the player
     * Cancels damage when landed on hay bales
     * @param e The event called when a player takes falling damage
     */
    @EventHandler(ignoreCancelled = true)
    public void onFallDamage(EntityDamageEvent e) {
        if (e.getCause() != EntityDamageEvent.DamageCause.FALL || !(e.getEntity() instanceof Player)
        || InCombat.isPlayerInLobby((e.getEntity().getUniqueId()))) {
            return;
        }

        // Make sure that landing on the edge of a hay bale also works
        Collection<Material> landedOn = getLandedOn(e.getEntity().getLocation());

        if (landedOn.contains(Material.HAY_BLOCK)) {
            e.setCancelled(true);
        } else {
            e.setDamage(e.getDamage() * 1.25);
        }

        for (Material materie : softBlock()) {
            if (landedOn.contains(materie)) {
                e.setDamage(e.getDamage() / 2.5);
            }
        }
    }

    /**
     * Get the square of blocks landed on
     * @param l The location where the player landed
     * @return The blocks touching the player's feet
     */
    private Collection<Material> getLandedOn(Location l) {
        Collection<Material> landedOn = new ArrayList<>();
        double baseX = l.getX();
        double baseZ = l.getZ();
        l.setY(l.getY() - 1);

        // Floor X and floor Z
        l.setX(baseX - 0.3);
        l.setZ(baseZ - 0.3);
        landedOn.add(l.getBlock().getType());

        // Floor X and ceil Z
        l.setX(baseX - 0.3);
        l.setZ(baseZ + 0.3);
        landedOn.add(l.getBlock().getType());

        // Ceil X and floor Z
        l.setX(baseX + 0.3);
        l.setZ(baseZ - 0.3);
        landedOn.add(l.getBlock().getType());

        // Ceil X and ceil Z
        l.setX(baseX + 0.3);
        l.setZ(baseZ + 0.3);
        landedOn.add(l.getBlock().getType());

        return landedOn;
    }

    public ArrayList<Material> softBlock() {
        ArrayList<Material> leaves = new ArrayList<>();
        leaves.add(Material.OAK_LEAVES);
        leaves.add(Material.DARK_OAK_LEAVES);
        leaves.add(Material.SPRUCE_LEAVES);
        leaves.add(Material.JUNGLE_LEAVES);
        leaves.add(Material.ACACIA_LEAVES);
        leaves.add(Material.MANGROVE_LEAVES);
        leaves.add(Material.BIRCH_LEAVES);
        leaves.add(Material.CHERRY_LEAVES);
        leaves.add(Material.AZALEA_LEAVES);
        leaves.add(Material.FLOWERING_AZALEA_LEAVES);
        leaves.add(Material.MOSS_BLOCK);
        leaves.add(Material.KELP);
        leaves.add(Material.SNOW_BLOCK);
        return leaves;
    }
}
