package me.huntifi.castlesiege.events.combat;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.database.CSActiveData;
import me.huntifi.castlesiege.database.UpdateStats;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.kits.kits.coin_kits.Medic;
import me.huntifi.castlesiege.maps.TeamController;
import me.huntifi.castlesiege.misc.CSNameTag;
import me.huntifi.conwymc.util.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Cake;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;

/**
 * Handles cake buffs
 */
public class EatCake implements Listener {

    public static final int CAKE_DURATION = 160;
    public static final int REGEN_AMPLIFIER = 12;

    /**
     * Take a bite from a cake and gain a short period of regeneration.
     * @param event The event called when right-clicking a cake
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEatCake(PlayerInteractEvent event) {
        if (!CSActiveData.hasPlayer(event.getPlayer().getUniqueId()))
            return;

        // Check if the player attempts to eat a cake
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK || !Objects.requireNonNull(event.getClickedBlock()).getType().equals(Material.CAKE))
            return;

        event.setCancelled(true);
        Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
            // Prevent eating cake in lobby
            Player eater = event.getPlayer();
            if (InCombat.isPlayerInLobby(eater.getUniqueId()))
                return;

            // Get the player who placed the cake
            Block cake = event.getClickedBlock();
            Player placer = Medic.getPlacer(cake);

            if (canEatCake(eater, placer)) {
                // Send messages and award heal
                if (placer != null && !Objects.equals(eater, placer)) {
                    Messenger.sendActionInfo(CSNameTag.mmUsername(placer) + "'s cake is healing you!", eater);
                    Messenger.sendActionSuccess("Your cake is healing " + CSNameTag.mmUsername(eater), placer);
                    UpdateStats.addHeals(placer.getUniqueId(), 1);

                } else {
                    Messenger.sendHealing("The cake is healing you!", eater);
                }

                // Eat cake
                Bukkit.getScheduler().runTask(Main.plugin, () -> {
                    eater.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, CAKE_DURATION, REGEN_AMPLIFIER));
                    Cake cakeData = (Cake) cake.getBlockData();

                    if (cakeData.getBites() >= cakeData.getMaximumBites()) {
                        cake.breakNaturally();
                        Medic.cakes.remove(placer);
                    } else {
                        cakeData.setBites(cakeData.getBites() + 1);
                        cake.setBlockData(cakeData);
                    }
                });
            }
        });
    }

    /**
     * Check if the player can eat from the cake.
     * @param eater The player who attempts to eat the cake
     * @param placer The player who placed the cake
     * @return Whether the player can eat the cake
     */
    private synchronized boolean canEatCake(Player eater, Player placer) {
        // The cake was placed by an enemy
        if (placer != null && TeamController.getTeam(eater.getUniqueId())
                != TeamController.getTeam(placer.getUniqueId())) {
            Messenger.sendActionError("This is an enemy cake, destroy it!", eater);
            return false;
        }

        // The eater has full health or is on cooldown
        if (eater.getHealth() == Kit.equippedKits.get(eater.getUniqueId()).baseHealth
                || Medic.cooldown.contains(eater)) {
            return false;
        }

        // Apply cooldown
        Medic.cooldown.add(eater);
        Bukkit.getScheduler().runTaskLaterAsynchronously(Main.plugin, () -> Medic.cooldown.remove(eater), CAKE_DURATION - 1);
        return true;
    }
}
