package me.huntifi.castlesiege.data_types;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.commands.donator.DuelCommand;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.events.death.DeathEvent;
import me.huntifi.castlesiege.structures.SchematicSpawner;
import me.huntifi.conwymc.data_types.Tuple;
import me.huntifi.conwymc.util.Messenger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.Vector;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Manages an arena for a duel
 */
public class Arena implements Listener {

    public static final String MAP = "DuelsMap";
    private final Location origin;
    private final Map chosenMap;

    private UUID greenPlayer;
    private UUID cyanPlayer;

    // Delay after teleporting before starting (in ticks)
    private final static int startDelay = 80;
    private final static Map[] maps = new Map[] {
            new Map.MapBuilder("DuelsArena_Cobbled",
                    new Location(Bukkit.getWorld(MAP), -3, 0, 16, 90, 0),
                    new Location(Bukkit.getWorld(MAP), -47, 0, 16, -90, 0))
                    .addGateSchematic("DuelsGate_Cobbled", new Vector(-10, 0, 16))
                    .addGateSchematic("DuelsGate_Cobbled", new Vector(-42, 0, 16))
                    .build(),
            new Map.MapBuilder("DuelsArena_Stag",
                    new Location(Bukkit.getWorld(MAP), -3, -14, 27, 90, 0),
                    new Location(Bukkit.getWorld(MAP), -76, -14, 27, -90, 0))
                    .addGateSchematic("DuelsGate_Stag", new Vector(-6, -14, 27))
                    .addGateSchematic("DuelsGate_Stag", new Vector(-71, -14, 27))
                    .build(),
    };

    //            new Arena("DuelsArena_Athena", "DuelsGate_Athena_Challenger",
    //                    "DuelsGate_Athena_Contender",
    //                    new Location(Bukkit.getWorld(MAP), 86, -21, 133, 90, 0),
    //                    new Location(Bukkit.getWorld(MAP), 25, -5, 133, -90, 0)),

    /**
     * @param location The origin to spawn the schematic at for the duel
     */
    public Arena(Vector location) {
        origin = new Location(Bukkit.getWorld(MAP), location.getX(), location.getY(), location.getZ());

        Random random = new Random();
        chosenMap = maps[random.nextInt(maps.length)];
    }

    public void beginDuel(Player challenger, Player challenged) {
        Main.plugin.getServer().getPluginManager().registerEvents(this, Main.plugin);

        // Spawn the schematic
        SchematicSpawner.spawnSchematic(origin, chosenMap.getSchematicName());

        // Choose who's green/cyan
        Player green, cyan;
        if (new Random().nextBoolean())
        {
            green = challenger;
            cyan = challenged;
        } else {
            cyan = challenger;
            green = challenged;
        }
        greenPlayer = green.getUniqueId();
        cyanPlayer = cyan.getUniqueId();

        // Teleport Players
        green.teleport(origin.clone().add(chosenMap.getGreenSpawn()));
        cyan.teleport(origin.clone().add(chosenMap.getCyanSpawn()));

        InCombat.playerSpawned(greenPlayer);
        InCombat.playerSpawned(cyanPlayer);

        sendCountdownMessages(green, cyan);

        Bukkit.getScheduler().runTaskLater(Main.plugin, this::openGates, startDelay);
    }

    public UUID getGreenPlayer() {
        return greenPlayer;
    }

    public UUID getCyanPlayer() {
        return cyanPlayer;
    }

    /**
     * Sends a title bar countdown to the player(s)
     * @param players The player(s)
     */
    private void sendCountdownMessages(Player... players) {
        for (Player p : players) {
            for (int i = 3; i >= 0; i--) {
                int finalI = i;
                int delay = startDelay - (20 * i);

                Bukkit.getScheduler().runTaskLaterAsynchronously(Main.plugin,
                        () -> {
                            Title title;
                            Title.Times times;
                            if (finalI != 0) {
                                times = Title.Times.times(Duration.ZERO, Duration.ofMillis(1000), Duration.ofMillis(750));
                                title = Title.title(Component.text(finalI, NamedTextColor.DARK_RED), Component.text(""), times);
                            } else {
                                times = Title.Times.times(Duration.ZERO, Duration.ofMillis(1500), Duration.ofMillis(1000));
                                title = Title.title(Component.text("FIGHT!", NamedTextColor.DARK_RED), Component.text(""), times);
                            }
                            p.showTitle(title);
                        },
                        delay);
            }
        }
    }

    private void openGates() {
        Location loc;
        for (Tuple<String, Vector> gate : chosenMap.getGateSchematics()) {
            loc = origin.clone();
            loc.add(gate.getSecond());
            SchematicSpawner.spawnSchematic(loc, gate.getFirst());
        }
    }

    private void duelFinish(UUID winner, UUID loser) {
        Player killer = Bukkit.getPlayer(winner);
        Player dead = Bukkit.getPlayer(loser);

        assert killer != null;
        assert dead != null;
        killer.spigot().respawn();
        dead.spigot().respawn();
        // Let the players know what happened
        Messenger.sendDuel("You won the duel from " + dead.getName(), killer);
        Messenger.sendDuel("You lost the duel from " + killer.getName(), dead);
        Messenger.broadcastDuel("<white>" + killer.getName()
                + "</white> beat <white>" + dead.getName() + "</white> in a duel!");

        DuelCommand.arena = null;
        HandlerList.unregisterAll(this);
    }

    private void duelForfeit(UUID winner, Player loser) {
        Player won = Bukkit.getPlayer(winner);

        assert won != null;
        won.spigot().respawn();
        //who won? who lost. Also cancelled event as that would kill the killed player a few times after the duel had ended.
        Messenger.sendDuel(loser.getName() + " forfeit the duel!", won);
        Messenger.broadcastDuel("<white>" + loser.getName()
                + "</white> forfeit their duel against <white>" + won.getName() + "</white>!");

        HandlerList.unregisterAll(this);
    }

    /**
     * One player has won
     * @param e The event called when a player dies
     */
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player killed = e.getEntity();

        if (!killed.getUniqueId().equals(greenPlayer)
        && !killed.getUniqueId().equals(cyanPlayer)) {
            return;
        }

        if (killed.getUniqueId().equals(greenPlayer)) {
            duelFinish(cyanPlayer, greenPlayer);
            e.setCancelled(true);
            DeathEvent.onCooldown.remove(killed);
        }
    }

    /**
     *
     * @param e event when a player leaves, they are removed from the lists and the arena's reset.
     */
    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        if (p.getUniqueId().equals(greenPlayer)) {
            duelForfeit(cyanPlayer, p);
        } else if (p.getUniqueId().equals(cyanPlayer)) {
            duelForfeit(greenPlayer, p);
        }
    }


    /**
     * Contains the data for a duels map
     * <br>
     * Locations are relative to the North-East corner of the map
     */
    public static class Map {
        private final String schematicName;

        /** Spawn points for the players */
        private final Location greenSpawn;
        private final Location cyanSpawn;

        private final List<Tuple<String, Vector>> gateSchematics;

        private Map(MapBuilder mapBuilder) {
            this.schematicName = mapBuilder.schematicName;
            this.greenSpawn = mapBuilder.greenSpawn;
            this.cyanSpawn = mapBuilder.cyanSpawn;
            gateSchematics = mapBuilder.gateSchematics;
        }

        /**
         * @return The name of the map's schematic
         */
        public String getSchematicName() {
            return schematicName;
        }

        /**
         * @return The spawn point for the green player
         */
        public Location getGreenSpawn() {
            return greenSpawn;
        }

        /**
         * @return The spawn point for the cyan player
         */
        public Location getCyanSpawn() {
            return cyanSpawn;
        }

        /**
         * @return Gets all the gate schematics for the map
         */
        public List<Tuple<String, Vector>> getGateSchematics() {
            return gateSchematics;
        }

        /**
         * The builder class for a map
         */
        public static class MapBuilder {
            private final String schematicName;

            /** Spawn points for the players */
            private final Location greenSpawn;
            private final Location cyanSpawn;

            private final List<Tuple<String, Vector>> gateSchematics;
            /**
             * @param schematicName The name of the schematic to spawn when the arena starts
             * @param greenSpawn The location to spawn the green player relative to the schematic origin
             * @param cyanSpawn The location to spawn the cyan player relative to the schematic origin
             */
            public MapBuilder(String schematicName, Location greenSpawn, Location cyanSpawn) {
                this.schematicName = schematicName;
                this.greenSpawn = greenSpawn;
                this.cyanSpawn = cyanSpawn;
                gateSchematics = new ArrayList<>();
            }

            /**
             * @param schematicName The name of the schematic to paste when the duel begins
             * @param relativeLocation The location to spawn the schematic relative to the map's schematic origin
             * @return The map
             */
            public MapBuilder addGateSchematic(String schematicName, Vector relativeLocation) {
                gateSchematics.add(new Tuple<>(schematicName, relativeLocation));
                return this;
            }

            /**
             * @return Creates a map from the builder
             */
            public Map build() {
                return new Map(this);
            }
        }
    }
}
