package me.huntifi.castlesiege.commands.donator.duels;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.events.death.DeathEvent;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.structures.SchematicSpawner;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

/**
 * This class's main function is to initiate a duel when the command /acceptduel is being executed
 * and afterwards also properly reset the arenas but at the same time manage them.
 */
public class AcceptDuel implements CommandExecutor, Listener {

    final static String MAP = "DuelsMap";
    final public String GATE = "DuelingOpen";
    final static String ARENA = "DuelsArena1";

    //This array determines whether an arena is occupied or not
    boolean[] arena = {false, false, false};

    //This arraylist is used to determine in which arena players are in.
    public static ArrayList<Player> arenaList1 = new ArrayList<>();
    public static ArrayList<Player> arenaList2 = new ArrayList<>();
    public static ArrayList<Player> arenaList3 = new ArrayList<>();

    //The locations to spawn the challenger and the contender at, when a duel is initiated.
    Location arenaChallenger1 = new Location(Bukkit.getWorld("DuelsMap"), -168, 4, -29, 90, 0);
    Location arenaChallenger2 = new Location(Bukkit.getWorld("DuelsMap"), -175, 4, -107, 90, 0);
    Location arenaChallenger3 = new Location(Bukkit.getWorld("DuelsMap"), -174, 4, -173, 90, 0);
    Location arenaContender1 = new Location(Bukkit.getWorld("DuelsMap"), -210, 4, -29, -90, 0);
    Location arenaContender2 = new Location(Bukkit.getWorld("DuelsMap"), -217, 4, -107, -90, 0);
    Location arenaContender3 = new Location(Bukkit.getWorld("DuelsMap"), -216, 4, -173, -90, 0);

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof ConsoleCommandSender) {
            Messenger.sendError("Console cannot accept a duel from anyone", sender);
            return true;
        }

        // Command needs a player
        if (args.length < 1) {
            return false;
        }

        // the receiving individual
        Player challenger = Bukkit.getPlayer(args[0]);

        //Check if the player received an invitation from the host
        if (DuelCmd.inviter.get(sender) != null) {
            if (!DuelCmd.inviter.get(sender).equals(challenger)) {
                Messenger.sendWarning("You currently don't have an invitation from this player.", sender);
                return true;
            }
        }

        // Cannot accept a challenge from yourself
        if (challenger == null) {
            Messenger.sendError( "Could not find player: " + ChatColor.RED + args[0], sender);
            return true;
        } else if (Objects.equals(sender, challenger)) {
            Messenger.sendWarning("I doubt you sent an invitation to yourself...", sender);
            return true;
        }

        // Both players have to be in a spawnroom.
        if (!InCombat.isPlayerInLobby(challenger.getUniqueId()) || !InCombat.isPlayerInLobby(((Player) sender).getUniqueId())) {
            Messenger.sendWarning("Both the host and recipient need to be in a spawnroom!", sender);
            return true;
        }

        if (arena[0] && arena[1] && arena[2]) {
            Messenger.sendWarning("You accepted " + challenger.getName() + "'s invitation to a duel. However all arena's are currently " +
                    "in use. Our apologies.", sender);
            Messenger.sendWarning(sender.getName() + " has accepted your invitation to a duel! However all arena's are currently " +
            "in use. Our apologies.", challenger);
            return true;
        }

        //Hashmap that has the data on whether someone is invited by a certain someone.
        DuelCmd.inviter.remove(sender, challenger);
        DuelCmd.challenging.put((Player) sender, challenger);
        Messenger.sendSuccess("You accepted " + challenger.getName() + "'s invitation to a duel.", sender);
        Messenger.sendSuccess(sender.getName() + " has accepted your invitation to a duel!", challenger);
        onDuelInitation(challenger, (Player) sender);
        return true;
    }

    /**
     * Duel process is initiated
     * @param challenger the person who challenged the contender
     * @param contender the person who accepted the challenge from the challenger
     */
    public void onDuelInitation(Player challenger, Player contender) {
          teleportContestants(challenger, contender);
          InCombat.playerSpawned(challenger.getUniqueId());
          InCombat.playerSpawned(contender.getUniqueId());
          sendCountdownMessages(challenger);
          sendCountdownMessages(contender);
    }

    /**
     * NOTE: These two can technically be put together, but I separated them to avoid confusion.
     * Mainly for myself.
     * @param challenger the player to clear from the lists
     *        this is the person that requested the duel.
     */
    public void onDuelEndChallenger(Player challenger) {
        clearPlayerFromArenaList(challenger);
        MapController.rejoinAfterDuel(challenger.getUniqueId());
    }

    /**
     *
     * @param contender the player to clear from the lists
     *                  This is the person that accepted the duel
     */
    public void onDuelEndContender(Player contender) {
        clearPlayerFromArenaList(contender);
        MapController.rejoinAfterDuel(contender.getUniqueId());
        DuelCmd.challenging.remove(contender);
    }

    /**
     *
     * @param challenger the person who challenged the contender
     * @param contender the person who accepted the challenge from the challenger
     */
    public void teleportContestants(Player challenger, Player contender) {
        for (int i = 0; i < 3; i++) {
            if (!arena[i]) {
                switch (i) {
                    case 0:
                        challenger.teleport(arenaChallenger1);
                        contender.teleport(arenaContender1);
                        arenaList1.add(challenger); arenaList1.add(contender);
                        break;
                    case 1:
                        challenger.teleport(arenaChallenger2);
                        contender.teleport(arenaContender2);
                        arenaList2.add(challenger); arenaList2.add(contender);
                        break;
                    case 2:
                        challenger.teleport(arenaChallenger3);
                        contender.teleport(arenaContender3);
                        arenaList3.add(challenger); arenaList3.add(contender);
                        break;
                }
            }
        }
    }

    /**
     * Clears the lists, basically saying they are not occupying that arena anymore.
     * Also repairs the arena
     * @param challenger the player to clear from the list.
     */
    public void clearPlayerFromArenaList(Player challenger) {
        if (arenaList1.contains(challenger)) {
            arenaList1.remove(challenger);
            arena[0] = false;
            SchematicSpawner.spawnSchematic(new Location(Bukkit.getWorld(MAP), -162, 4, -29), ARENA);
        } else if (arenaList2.contains(challenger)) {
            arenaList2.remove(challenger);
            arena[1] = false;
            SchematicSpawner.spawnSchematic(new Location(Bukkit.getWorld(MAP), -169, 4, -107), ARENA);
        } else arenaList3.remove(challenger);
        arena[2] = false;
        SchematicSpawner.spawnSchematic(new Location(Bukkit.getWorld(MAP), -168, 4, -173), ARENA);
    }

    /**
     * send a title bar to the player after 5 seconds, then another time after 30 seconds.
     * But only if they are in the spawn room still.
     * @param p The player
     */
    public void sendCountdownMessages(Player p) {
        new BukkitRunnable() {
            @Override
            public void run() {
                p.sendTitle(String.valueOf(ChatColor.DARK_RED) + 3, "", 0, 20, 15);
            }
        }.runTaskLater(Main.plugin, 20);
        new BukkitRunnable() {
            @Override
            public void run() {
                p.sendTitle(String.valueOf(ChatColor.DARK_RED) + 2, "", 0, 20, 15);

            }
        }.runTaskLater(Main.plugin, 40);
        new BukkitRunnable() {
            @Override
            public void run() {
                p.sendTitle(String.valueOf(ChatColor.DARK_RED) + 1, "", 0, 20, 15);
            }
        }.runTaskLater(Main.plugin, 60);
        new BukkitRunnable() {
            @Override
            public void run() {
                p.sendTitle(ChatColor.DARK_RED + "FIGHT!", "", 0, 30, 20);
                openGate(p);
            }
        }.runTaskLater(Main.plugin, 80);

    }

    /**
     *
     * @param e event when a player leaves, they are removed from the lists and the arena's reset.
     */
    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        DuelCmd.challenging.entrySet()
                .removeIf(entry -> entry.getKey().equals(p));

        if (arenaList1.contains(p)) {
            arenaList1.remove(p);
            for (Player present : arenaList1) {
                MapController.joinATeam(present.getUniqueId());
                clearPlayerFromArenaList(present);
            }
        } else if (arenaList2.contains(p)) {
            arenaList2.remove(p);
            for (Player present : arenaList2) {
                MapController.joinATeam(present.getUniqueId());
                clearPlayerFromArenaList(present);
            }
        } else if (arenaList3.contains(p)) {
            arenaList3.remove(p);
            for (Player present : arenaList3) {
                MapController.joinATeam(present.getUniqueId());
                clearPlayerFromArenaList(present);
            }
        }
    }

    /**
     * Auto-respawn the player
     * Apply stat changes
     * @param e The event called when a player dies
     */
    @EventHandler
    public void onPlayerDeath(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            Player killer = (Player) e.getDamager();
            if (e.getEntityType() == EntityType.PLAYER) {
                Player killed = (Player) e.getEntity();
                if (DuelCmd.isDueling(killer) && DuelCmd.isDueling(killed)
                        && killed.getHealth() - killed.getLastDamage() < 0) {

                    if (DuelCmd.challenging.containsKey(killer)) {
                        onDuelEndContender(killer);
                        onDuelEndChallenger(killed);
                        //respawn the players at their spawnroom
                        killer.spigot().respawn();
                        killed.spigot().respawn();
                    } else {
                        onDuelEndChallenger(killer);
                        onDuelEndContender(killed);
                        //respawn the players at their spawnroom
                        killer.spigot().respawn();
                        killed.spigot().respawn();
                    }
                    //who won? who lost. Also cancelled event as that would kill the killed player a few times after the duel had ended.
                    Messenger.sendSuccess("You won the duel from " + killed.getName(), killer);
                    Messenger.sendSuccess("You lost the duel from " + killer.getName(), killed);
                    e.setCancelled(true);
                }
                DeathEvent.onCooldown.remove((Player) e.getEntity());
            }
        }
    }

    /**
     * Spawns a schematic where the gate has to be opened for this specific player.
     * @param contender the player to check the location for
     */
    public void openGate(Player contender) {
        if (contender == null) { return; }
        Location ploc = contender.getLocation();

        if (ploc.distance(arenaChallenger1) <= 5) {
          SchematicSpawner.spawnSchematic(new Location(Bukkit.getWorld(MAP), -172, 4, -29), GATE);
        } else if (ploc.distance(arenaChallenger2) <= 5) {
            SchematicSpawner.spawnSchematic(new Location(Bukkit.getWorld(MAP), -179, 4, -107), GATE);
        } else if (ploc.distance(arenaChallenger3) <= 5) {
            SchematicSpawner.spawnSchematic(new Location(Bukkit.getWorld(MAP), -178, 4, -173), GATE);
        } else if (ploc.distance(arenaContender1) <= 5) {
            SchematicSpawner.spawnSchematic(new Location(Bukkit.getWorld(MAP), -204, 4, -29), GATE);
        } else if (ploc.distance(arenaContender2) <= 5) {
            SchematicSpawner.spawnSchematic(new Location(Bukkit.getWorld(MAP), -211, 4, -107), GATE);
        } else if (ploc.distance(arenaContender3) <= 5) {
            SchematicSpawner.spawnSchematic(new Location(Bukkit.getWorld(MAP), -210, 4, -173), GATE);
        }
    }

    /**
     * resets the arenas when the server reopens/closes
     */
    public static void resetArenas() {
        SchematicSpawner.spawnSchematic(new Location(Bukkit.getWorld(MAP), -162, 4, -29), ARENA);
        SchematicSpawner.spawnSchematic(new Location(Bukkit.getWorld(MAP), -169, 4, -107), ARENA);
        SchematicSpawner.spawnSchematic(new Location(Bukkit.getWorld(MAP), -168, 4, -173), ARENA);
    }
}
