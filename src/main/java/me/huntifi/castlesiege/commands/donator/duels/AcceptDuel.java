package me.huntifi.castlesiege.commands.donator.duels;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.conwymc.util.Messenger;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.events.death.DeathEvent;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.structures.SchematicSpawner;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
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

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * This class's main function is to initiate a duel when the command /acceptduel is being executed
 * and afterward also properly reset the arenas but at the same time manage them.
 */
public class AcceptDuel implements CommandExecutor, Listener {

    private final static String MAP = "DuelsMap";
    private final static String GATE = "DuelingOpen";
    private final static String ARENA = "DuelsArena1";
    private final static int ARENA_COUNT = 5;

    private final static Location ORIGIN_CHALLENGER = new Location(Bukkit.getWorld(MAP), 94, 4, 100, 90, 0);
    private final static Location ORIGIN_CONTENDER = new Location(Bukkit.getWorld(MAP), 52, 4, 100, -90, 0);
    private final static Location ORIGIN_SCHEMATIC = new Location(Bukkit.getWorld(MAP), 100, 4, 100);
    private final static int X_SPACING = 100;

    //This arraylist is used to determine in which arena players are in.
    private static final List<Tuple<UUID, UUID>> arenaPlayers = new ArrayList<>();

    //The locations to spawn the challenger and the contender at, when a duel is initiated.
    Location[] arenaChallengers = new Location[ARENA_COUNT];
    Location[] arenaContenders = new Location[ARENA_COUNT];

    Location[] schematicLocations = new Location[ARENA_COUNT];

    public AcceptDuel() {
        for (int i = 0; i < ARENA_COUNT; i++) {
            arenaPlayers.add(new Tuple<>(null, null));

            Location loc = ORIGIN_CHALLENGER.clone();
            loc.setX(loc.x() + X_SPACING * i);
            arenaChallengers[i] = loc;

            loc = ORIGIN_CONTENDER.clone();
            loc.setX(loc.x() + X_SPACING * i);
            arenaContenders[i] = loc;

            loc = ORIGIN_SCHEMATIC.clone();
            loc.setX(loc.x() + X_SPACING * i);
            schematicLocations[i] = loc;
            SchematicSpawner.spawnSchematic(loc, ARENA);
        }
    }

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
        if (DuelCommand.inviter.get(sender) != null) {
            if (!DuelCommand.inviter.get(sender).equals(challenger)) {
                Messenger.sendError("You currently don't have an invitation from this player.", sender);
                return true;
            }
        }

        // Cannot accept a challenge from yourself
        if (challenger == null) {
            Messenger.sendError( "Could not find player: <red>" + args[0], sender);
            return true;
        } else if (Objects.equals(sender, challenger)) {
            Messenger.sendError("I doubt you sent an invitation to yourself...", sender);
            return true;
        }

        // Both players have to be in a spawnroom.
        if (!InCombat.isPlayerInLobby(challenger.getUniqueId()) || !InCombat.isPlayerInLobby(((Player) sender).getUniqueId())) {
            Messenger.sendError("Both the host and recipient need to be in a spawnroom!", sender);
            return true;
        }

        if (getEmptyArena() == -1) {
            Messenger.sendDuel("You accepted " + challenger.getName() + "'s invitation to a duel. However all arena's are currently " +
                    "in use. Our apologies.", sender);
            Messenger.sendDuel(sender.getName() + " has accepted your invitation to a duel! However all arena's are currently " +
            "in use. Our apologies.", challenger);
            return true;
        }

        //Hashmap that has the data on whether someone is invited by a certain someone.
        DuelCommand.inviter.remove(sender, challenger);
        DuelCommand.challenging.put((Player) sender, challenger);
        Messenger.sendDuel("You accepted " + challenger.getName() + "'s invitation to a duel.", sender);
        Messenger.sendDuel(sender.getName() + " has accepted your invitation to a duel!", challenger);
        onDuelInitiation(challenger, (Player) sender);
        return true;
    }

    /**
     * Duel process is initiated
     * @param challenger the person who challenged the contender
     * @param contender the person who accepted the challenge from the challenger
     */
    public void onDuelInitiation(Player challenger, Player contender) {
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
        DuelCommand.challenging.remove(contender);
    }

    /**
     *
     * @param challenger the person who challenged the contender
     * @param contender the person who accepted the challenge from the challenger
     */
    public void teleportContestants(Player challenger, Player contender) {
        for (int i = 0; i < ARENA_COUNT; i++) {
            if (arenaPlayers.get(i).getFirst() != null) {
                challenger.teleport(arenaChallengers[i]);
                contender.teleport(arenaContenders[i]);
                arenaPlayers.set(i, new Tuple<>(challenger.getUniqueId(), contender.getUniqueId()));
            }
        }
    }

    /**
     * Clears the lists, basically saying they are not occupying that arena anymore.
     * Also repairs the arena
     * @param challenger the player to clear from the list.
     */
    public void clearPlayerFromArenaList(Player challenger) {
        for (int i = 0; i < ARENA_COUNT; i++) {
            if (arenaPlayers.get(i).getFirst() == challenger.getUniqueId()) {
                arenaPlayers.set(i, new Tuple<>(null, null));
                SchematicSpawner.spawnSchematic(schematicLocations[i], ARENA);
            }
        }
    }

    /**
     * send a title bar to the player after 5 seconds, then another time after 30 seconds.
     * But only if they are in the spawn room still.
     * @param p The player
     */
    public void sendCountdownMessages(Player p) {
        for (int i = 3; i >= 0; i--) {
            int finalI = i;
            int delay = 80 - (20 * i);
            new BukkitRunnable() {
                @Override
                public void run() {
                    Title title;
                    Title.Times times;
                    if (finalI != 0) {
                        times = Title.Times.times(Duration.ZERO, Duration.ofMillis(1000), Duration.ofMillis(750));
                        title = Title.title(Component.text(finalI, NamedTextColor.DARK_RED), Component.text(""), times);
                    } else {
                        times = Title.Times.times(Duration.ZERO, Duration.ofMillis(1500), Duration.ofMillis(1000));
                        title = Title.title(Component.text("FIGHT!", NamedTextColor.DARK_RED), Component.text(""), times);
                        openGate(p);
                    }
                    p.showTitle(title);

                }
            }.runTaskLater(Main.plugin, delay);
        }
    }

    /**
     *
     * @param e event when a player leaves, they are removed from the lists and the arena's reset.
     */
    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        DuelCommand.challenging.entrySet()
                .removeIf(entry -> entry.getKey().equals(p));

        for (int i = 0; i < ARENA_COUNT; i++) {
            Tuple<UUID, UUID> players = arenaPlayers.get(i);
            if (players.getFirst() == p.getUniqueId()
            || players.getSecond() == p.getUniqueId()) {
                MapController.joinATeam(players.getFirst());
                MapController.joinATeam(players.getSecond());
                arenaPlayers.set(i, new Tuple<>(null, null));
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
                if (DuelCommand.isDueling(killer) && DuelCommand.isDueling(killed)
                        && killed.getHealth() - killed.getLastDamage() < 0) {

                    if (DuelCommand.challenging.containsKey(killer)) {
                        onDuelEndContender(killer);
                        onDuelEndChallenger(killed);
                    } else {
                        onDuelEndChallenger(killer);
                        onDuelEndContender(killed);
                    }
                    //respawn the players at their spawnroom
                    killer.spigot().respawn();
                    killed.spigot().respawn();
                    //who won? who lost. Also cancelled event as that would kill the killed player a few times after the duel had ended.
                    Messenger.sendDuel("You won the duel from " + killed.getName(), killer);
                    Messenger.sendDuel("You lost the duel from " + killer.getName(), killed);
                    Messenger.broadcastDuel("<white>" + killer.getName()
                    + "</white> beat <white>" + killed.getName() + "</white> in a duel!</gradient>");
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

        if (ploc.distance(arenaChallengers[0]) <= 5) {
          SchematicSpawner.spawnSchematic(new Location(Bukkit.getWorld(MAP), -172, 4, -29), GATE);
        } else if (ploc.distance(arenaChallengers[1]) <= 5) {
            SchematicSpawner.spawnSchematic(new Location(Bukkit.getWorld(MAP), -179, 4, -107), GATE);
        } else if (ploc.distance(arenaChallengers[2]) <= 5) {
            SchematicSpawner.spawnSchematic(new Location(Bukkit.getWorld(MAP), -178, 4, -173), GATE);
        } else if (ploc.distance(arenaContenders[0]) <= 5) {
            SchematicSpawner.spawnSchematic(new Location(Bukkit.getWorld(MAP), -204, 4, -29), GATE);
        } else if (ploc.distance(arenaContenders[1]) <= 5) {
            SchematicSpawner.spawnSchematic(new Location(Bukkit.getWorld(MAP), -211, 4, -107), GATE);
        } else if (ploc.distance(arenaContenders[2]) <= 5) {
            SchematicSpawner.spawnSchematic(new Location(Bukkit.getWorld(MAP), -210, 4, -173), GATE);
        }
    }

    public static int getEmptyArena() {
        for (int i = 0; i < ARENA_COUNT; i++) {
            if (arenaPlayers.get(i).getFirst() == null) {
                return i;
            }
        }
        Main.plugin.getLogger().warning("Player awaiting free arena, may need to increase count");
        return -1;
    }
}
