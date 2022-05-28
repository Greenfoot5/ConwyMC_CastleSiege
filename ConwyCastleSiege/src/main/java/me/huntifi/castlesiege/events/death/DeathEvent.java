package me.huntifi.castlesiege.events.death;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.castlesiege.database.UpdateStats;
import me.huntifi.castlesiege.events.combat.AssistKill;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.NameTag;
import me.huntifi.castlesiege.maps.Team;
import me.huntifi.castlesiege.maps.objects.Flag;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

/**
 * Manages what happens when a player dies
 */
public class DeathEvent implements Listener {

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
        Team team = MapController.getCurrentMap().getTeam(player.getUniqueId());

        if (team == null && MapController.isSpectator(player.getUniqueId())) {
            event.setRespawnLocation(MapController.getCurrentMap().flags[0].spawnPoint);
        }
        assert team != null && team.lobby.spawnPoint != null;
        if (team.lobby.spawnPoint.getWorld() == null) {
            team.lobby.spawnPoint.setWorld(Bukkit.getWorld(MapController.getCurrentMap().worldName));
        }

        event.setRespawnLocation(team.lobby.spawnPoint);
        Kit.equippedKits.get(player.getUniqueId()).setItems(player.getUniqueId());
        InCombat.playerDied(player.getUniqueId());
    }

    /**
     * Disable death message
     * Auto-respawn the player
     * Apply stat changes
     * @param event The event called when a player dies
     */
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.setDeathMessage(null);
        respawn(event.getEntity());
        stopCapping(event.getEntity());
        updateStats(event);
    }

    /**
     * Auto-respawn the player
     * @param p The player to respawn
     */
    private void respawn(Player p) {
        new BukkitRunnable() {
            @Override
            public void run() {
                p.spigot().respawn();
            }
        }.runTaskLater(Main.plugin, 10);
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
     * Update the stats of everyone involved in the kill
     * +1 death for the player that died
     * +1 kill for the player that dealt the final damage
     * +1 assist for the player that dealt the most damage
     * @param e The event called when a player dies
     */
    private void updateStats(PlayerDeathEvent e) {
        // Death
        Player target = e.getEntity();
        UpdateStats.addDeaths(target.getUniqueId(), 1);

        // Kill
        Player killer = killerMap.getOrDefault(target, target.getKiller());
        killerMap.remove(target);
        if (killer != null) {
            UpdateStats.addKill(killer.getUniqueId());

            // Kill and death messages
            Kit kit = Kit.equippedKits.get(killer.getUniqueId());
            if (target.getLastDamageCause() != null
                    && target.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
                killDeathMessage(killer, target, kit.getProjectileMessage());
            } else {
                killDeathMessage(killer, target, kit.getMeleeMessage());
            }
        }

        // Assist
        UUID assist = AssistKill.get(target.getUniqueId());
        if (assist != null) {
            UpdateStats.addAssist(assist);
            if (killer == null || !killer.getUniqueId().equals(assist)) {
                assistMessage(assist, target);
            }
        }
    }

    /**
     * Send kill and death messages to the players
     * @param killer The player who killed
     * @param target The player who died
     * @param messages The messages sent to the killer and target
     */
    private void killDeathMessage(Player killer, Player target, Tuple<String[], String[]> messages) {
        killer.sendMessage(messages.getFirst()[0] + NameTag.color(target) + target.getName()
                + ChatColor.RESET + messages.getFirst()[1] + ChatColor.GRAY +
                " (" + ActiveData.getData(killer.getUniqueId()).getKillStreak() + ")");

        target.sendMessage(messages.getSecond()[0] + NameTag.color(killer) + killer.getName()
                + ChatColor.RESET + messages.getSecond()[1]);
    }

    /**
     * Send assist message to the player
     * @param uuid The unique ID of the player who dealt most damage
     * @param target The player who died
     */
    private void assistMessage(UUID uuid, Player target) {
        Player assist = Bukkit.getPlayer(uuid);
        if (assist != null) {
            assist.sendMessage("You assisted in killing " + NameTag.color(target) + target.getName());
        }
    }
}
