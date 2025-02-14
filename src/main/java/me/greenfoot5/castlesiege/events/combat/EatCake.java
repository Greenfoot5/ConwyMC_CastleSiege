package me.greenfoot5.castlesiege.events.combat;

import me.greenfoot5.castlesiege.Main;
import me.greenfoot5.castlesiege.database.UpdateStats;
import me.greenfoot5.castlesiege.kits.kits.Kit;
import me.greenfoot5.castlesiege.kits.kits.coin_kits.Medic;
import me.greenfoot5.castlesiege.maps.TeamController;
import me.greenfoot5.castlesiege.misc.CSNameTag;
import me.greenfoot5.conwymc.data_types.Tuple;
import me.greenfoot5.conwymc.util.Messenger;
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

import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

/**
 * Handles cake buffs
 */
public class EatCake implements Listener {

    public static final int CAKE_DURATION = 160;
    public static final int REGEN_AMPLIFIER = 12;

    public static HashSet<UUID> cooldowns = new HashSet<>();

    /**
     * Take a bite from a cake and gain a short period of regeneration.
     * @param event The event called when right-clicking a cake
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEatCake(PlayerInteractEvent event) {
        if (InCombat.isPlayerInLobby((event.getPlayer().getUniqueId()))) {
            return;
        }

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

            Tuple<Boolean, Player> canEat = canEatCake(eater);

            if (canEat.getFirst()) {
                // Send messages and award heal
                if (canEat.getSecond() != null && !Objects.equals(eater, canEat.getSecond())) {
                    Messenger.sendActionInfo(CSNameTag.mmUsername(canEat.getSecond()) + "'s cake is healing you!", eater);
                    Messenger.sendActionSuccess("Your cake is healing " + CSNameTag.mmUsername(eater), canEat.getSecond());
                    UpdateStats.addHeals(canEat.getSecond().getUniqueId(), 1);

                } else {
                    Messenger.sendHealing("The cake is healing you!", eater);
                }

                // Eat cake
                Bukkit.getScheduler().runTask(Main.plugin, () -> {
                    eater.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, CAKE_DURATION, REGEN_AMPLIFIER));
                    Cake cakeData = (Cake) cake.getBlockData();

                    if (cakeData.getBites() >= cakeData.getMaximumBites()) {
                        cake.breakNaturally();
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
     * @return Whether the player can eat the cake
     */
    private synchronized Tuple<Boolean, Player> canEatCake(Player eater) {
        Player placer = null;
        for (Kit kit : Kit.equippedKits.values()) {
            if (kit instanceof Medic) {
                if (kit.getEquippedPlayer() != null)
                    placer = kit.getEquippedPlayer();
            }
        }

        // The cake was placed by an enemy
        if (placer != null && TeamController.getTeam(eater.getUniqueId())
                != TeamController.getTeam(placer.getUniqueId())) {
            Messenger.sendActionError("This is an enemy cake, destroy it!", eater);
            return new Tuple<>(false, placer);
        }

        // The eater has full health or is on cooldown
        if (eater.getHealth() == Kit.equippedKits.get(eater.getUniqueId()).baseHealth
                || cooldowns.contains(eater.getUniqueId())) {
            return new Tuple<>(false, placer);
        }

        // Apply cooldown
        cooldowns.add(eater.getUniqueId());
        Bukkit.getScheduler().runTaskLaterAsynchronously(Main.plugin, () -> cooldowns.remove(eater.getUniqueId()), CAKE_DURATION - 1);
        return new Tuple<>(true, placer);
    }
}
