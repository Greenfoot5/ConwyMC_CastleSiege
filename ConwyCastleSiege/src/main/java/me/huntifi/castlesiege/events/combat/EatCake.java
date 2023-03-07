package me.huntifi.castlesiege.events.combat;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.database.UpdateStats;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.kits.kits.donator_kits.Medic;
import me.huntifi.castlesiege.maps.NameTag;
import me.huntifi.castlesiege.maps.TeamController;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

public class EatCake implements Listener {

    public static final int CAKE_DURATION = 100;
    public static final int REGEN_AMPLIFIER = 12;

    /**
     * Take a bite from a cake and gain a short period of regeneration.
     * @param event The event called when right-clicking a cake
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEatCake(PlayerInteractEvent event) {
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
                    Messenger.sendActionInfo(NameTag.color(placer) + placer.getName() + ChatColor.AQUA + "'s cake is healing you!", eater);
                    Messenger.sendActionInfo("Your cake is healing " + NameTag.color(eater) + eater.getName(), placer);
                    UpdateStats.addHeals(placer.getUniqueId(), 1);

                } else {
                    eater.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                            ChatColor.AQUA + "The cake is healing you!"));
                }

                // Eat cake
                Bukkit.getScheduler().runTask(Main.plugin, () -> {
                    eater.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, CAKE_DURATION, REGEN_AMPLIFIER));
                    Cake cakeData = (Cake) cake.getBlockData();

                    if (cakeData.getBites() == cakeData.getMaximumBites()) {
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
