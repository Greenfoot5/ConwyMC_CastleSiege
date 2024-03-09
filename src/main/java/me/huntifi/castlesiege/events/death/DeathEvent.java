package me.huntifi.castlesiege.events.death;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.commands.donator.duels.DuelCommand;
import me.huntifi.castlesiege.commands.gameplay.BountyCommand;
import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.castlesiege.database.UpdateStats;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.events.combat.AssistKill;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.events.connection.PlayerConnect;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.NameTag;
import me.huntifi.castlesiege.maps.objects.Flag;
import me.huntifi.castlesiege.maps.objects.Gate;
import me.huntifi.castlesiege.maps.objects.Ram;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

/**
 * Manages what happens when a player dies
 */
public class DeathEvent implements Listener {

    public static final ArrayList<Player> onCooldown = new ArrayList<>();

    private static final HashMap<Player, Player> killerMap = new HashMap<>();

    /**
     * Set the killer of a player
     * @param target The player who dies
     * @param killer The player who killed
     */
    public static void setKiller(Player target, Player killer) {
        killerMap.put(target, killer);
    }

    /**
     * Spawn the player correctly
     * @param event The event called when a player respawns
     */
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();

        if (MapController.isSpectator(player.getUniqueId())) {
            event.setRespawnLocation(MapController.getCurrentMap().flags[0].getSpawnPoint());
            return;
        }

        if (Objects.equals(ActiveData.getData(player.getUniqueId()).getSetting("randomDeath"), "true") ||
        MapController.forcedRandom)
            player.performCommand("random");
        else
            Kit.equippedKits.get(player.getUniqueId()).setItems(player.getUniqueId(), false);

        player.setWalkSpeed(0.2f);
        Bukkit.getScheduler().runTaskLater(Main.plugin, () -> respawnCounter(player), 10);
        if (ActiveData.getData(player.getUniqueId()).getSetting("woolmapTitleMessage").equals("true")) {
            PlayerConnect.sendTitlebarMessages(player);
        }
    }

    /**
     * Auto-respawn the player
     * Apply stat changes
     * @param event The event called when a player dies
     */
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        onCooldown.add(event.getEntity());
        event.getEntity().eject();
        Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
            respawn(event.getEntity());
            stopCapping(event.getEntity());
            stopRamming(event.getEntity());
            if (MapController.isOngoing() && !InCombat.isPlayerInLobby(event.getEntity().getUniqueId())) {
                updateStats(event);
            }
            InCombat.playerDied(event.getEntity().getUniqueId());
        });
    }

    /**
     * Disable death message.
     * Placed in separate function to allow performing after mythic mobs.
     * @param event The event called when a player dies
     */
    @EventHandler (priority = EventPriority.MONITOR)
    public void disableDeathMessage(PlayerDeathEvent event) {
        event.deathMessage(null);
    }

    private void respawn(Player p) {
        Bukkit.getScheduler().runTaskLater(Main.plugin, () -> p.spigot().respawn(), 10);
    }

    /**
     * counts down to 0 then respawns the player
     * @param p the player to display the counter to
     */
    private void respawnCounter(Player p) {
        if (!p.isOnline()) return;

        Title.Times times = Title.Times.times(Duration.ZERO, Duration.ofMillis(1000), Duration.ofMillis(750));
        new BukkitRunnable() {
            @Override
            public void run() {
                Title title = Title.title(Component.empty(), Component.text("You're able to spawn in ", NamedTextColor.DARK_GREEN)
                        .append(Component.text(3, NamedTextColor.DARK_RED)),
                        times);
                p.showTitle(title);
               }
            }.runTaskLater(Main.plugin, 20);
        new BukkitRunnable() {
            @Override
            public void run() {
                Title title = Title.title(Component.empty(), Component.text("You're able to spawn in ", NamedTextColor.DARK_GREEN)
                                .append(Component.text(2, NamedTextColor.DARK_RED)),
                        times);
                p.showTitle(title);

            }
        }.runTaskLater(Main.plugin, 40);
        new BukkitRunnable() {
            @Override
            public void run() {
                Title title = Title.title(Component.empty(), Component.text("You're able to spawn in ", NamedTextColor.DARK_GREEN)
                                .append(Component.text(1, NamedTextColor.DARK_RED)),
                        times);
                p.showTitle(title);
            }
        }.runTaskLater(Main.plugin, 60);
        new BukkitRunnable() {
            @Override
            public void run() {
                Title title = Title.title(Component.empty(), Component.text("You can now spawn!", NamedTextColor.DARK_GREEN),
                        times);
                p.showTitle(title);
                onCooldown.remove(p);
            }
        }.runTaskLater(Main.plugin, 80);
    }

    /**
     * Remove the player from all capping zones
     * @param p The player
     */
    private void stopCapping(Player p) {
        for (Flag flag : MapController.getCurrentMap().flags) {
            flag.playerExit(p);
        }
    }

    /**
     * Remove the player from all rams
     * @param p The player
     */
    private void stopRamming(Player p) {
        for (Gate gate : MapController.getCurrentMap().gates) {
            Ram ram = gate.getRam();
            if (ram != null)
                ram.playerExit(p);
        }
    }

    /**
     * Update the stats of everyone involved in the kill
     * +1 death for the player that died
     * +1 kill for the player that dealt the final damage
     * +1 assist for the player that dealt the most damage
     * @param e The event called when a player dies
     */
    private void updateStats(PlayerDeathEvent e) {
        // Death
        Player target = e.getEntity();

        // Kill
        Player killer = killerMap.getOrDefault(target, target.getKiller());
        killerMap.remove(target);
        if (killer != null && !DuelCommand.isDueling(killer)) {
            UpdateStats.addKill(killer.getUniqueId());
            AssistKill.removeDamager(target.getUniqueId(), killer.getUniqueId());

            // Kill and death messages
            Kit kit = Kit.equippedKits.get(killer.getUniqueId());
                if (target.getLastDamageCause() != null
                        && target.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.PROJECTILE)
                    killDeathMessage(killer, target, kit.getProjectileMessage());
                else
                    killDeathMessage(killer, target, kit.getMeleeMessage());

            // Check for bounty
            BountyCommand.killStreak(killer);
        }

        // Assist
        UUID assist = AssistKill.get(target.getUniqueId());
        if (assist != null && killer != null && killer.getUniqueId() != assist) {
            // There are separate players for the kill and the assist
            UpdateStats.addAssist(assist);
            assistMessage(assist, target);
            BountyCommand.grantRewards(target, killer, Bukkit.getPlayer(assist));
        } else if (killer != null) {
            // There is no player for the assist, or it is the same as the killer
            BountyCommand.grantRewards(target, killer);
        } else if (assist != null) {
            // There is only a player for the assist, the death is not player related
            UpdateStats.addAssist(assist);
            assistMessage(assist, target);
        }

        if (killer != null || assist != null) {
            UpdateStats.addDeaths(target.getUniqueId(), 1);
        }
    }

    /**
     * Send kill and death messages to the players
     * @param killer The player who killed
     * @param target The player who died
     * @param messages The messages sent to the killer and target
     */
    private void killDeathMessage(Player killer, Player target, Tuple<String[], String[]> messages) {
        Messenger.send(Component.text("You" + messages.getFirst()[0]
                                + NameTag.username(target) + messages.getFirst()[1])
                .append(Component.text(" (" + (ActiveData.getData(killer.getUniqueId()).getKillStreak()) + ")", NamedTextColor.GRAY)),
                killer);

        Messenger.send(Component.text(messages.getSecond()[0] + NameTag.username(killer) + messages.getSecond()[1]), target);

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player != killer && player != target
                    && !ActiveData.getData(player.getUniqueId()).getSetting("deathMessages").equals("false")) {
                Messenger.send(NameTag.mmUsername(killer) + messages.getFirst()[0]
                        + NameTag.mmUsername(target) + messages.getFirst()[1], player);
            }
        }
    }

    /**
     * Send assist message to the player
     * @param uuid The unique ID of the player who dealt most damage
     * @param target The player who died
     */
    private void assistMessage(UUID uuid, Player target) {
        Player assist = Bukkit.getPlayer(uuid);
        if (assist != null) {
            Messenger.send("You assisted in killing " + NameTag.mmUsername(target), assist);
        }
    }

    /**
     *
     * @param e removes players that can't respawn from the list when they leave.
     */
    @EventHandler
    public void onLeave (PlayerQuitEvent e) {
        onCooldown.remove(e.getPlayer());
    }

}
