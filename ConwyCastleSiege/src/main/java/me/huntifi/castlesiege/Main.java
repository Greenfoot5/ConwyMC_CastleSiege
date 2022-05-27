package me.huntifi.castlesiege;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.session.SessionManager;
import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.route.Route;
import dev.dejvokep.boostedyaml.serialization.standard.StandardSerializer;
import dev.dejvokep.boostedyaml.serialization.standard.TypeAdapter;
import me.huntifi.castlesiege.commands.chat.PrivateMessage;
import me.huntifi.castlesiege.commands.chat.ReplyMessage;
import me.huntifi.castlesiege.commands.chat.TeamChat;
import me.huntifi.castlesiege.commands.donator.JoinMessage;
import me.huntifi.castlesiege.commands.donator.LeaveMessage;
import me.huntifi.castlesiege.commands.gameplay.KitCommand;
import me.huntifi.castlesiege.commands.gameplay.SuicideCommand;
import me.huntifi.castlesiege.commands.gameplay.SwitchCommand;
import me.huntifi.castlesiege.commands.info.*;
import me.huntifi.castlesiege.commands.staff.*;
import me.huntifi.castlesiege.commands.staff.punishments.*;
import me.huntifi.castlesiege.data_types.Frame;
import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.database.KeepAlive;
import me.huntifi.castlesiege.database.MySQL;
import me.huntifi.castlesiege.database.StoreData;
import me.huntifi.castlesiege.events.chat.PlayerChat;
import me.huntifi.castlesiege.events.combat.*;
import me.huntifi.castlesiege.events.connection.PlayerConnect;
import me.huntifi.castlesiege.events.connection.PlayerDisconnect;
import me.huntifi.castlesiege.events.death.DeathEvent;
import me.huntifi.castlesiege.events.death.VoidLocation;
import me.huntifi.castlesiege.events.security.InteractContainer;
import me.huntifi.castlesiege.events.security.InventoryProtection;
import me.huntifi.castlesiege.events.security.MapProtection;
import me.huntifi.castlesiege.events.timed.ApplyRegeneration;
import me.huntifi.castlesiege.events.timed.BarCooldown;
import me.huntifi.castlesiege.events.timed.Hunger;
import me.huntifi.castlesiege.events.timed.Tips;
import me.huntifi.castlesiege.kits.gui.FreeKitGUI;
import me.huntifi.castlesiege.kits.gui.SelectorKitGUI;
import me.huntifi.castlesiege.kits.gui.UnlockedKitGUI;
import me.huntifi.castlesiege.kits.items.Enderchest;
import me.huntifi.castlesiege.kits.kits.*;
import me.huntifi.castlesiege.maps.Map;
import me.huntifi.castlesiege.maps.*;
import me.huntifi.castlesiege.maps.helms_deep.CavesBoat;
import me.huntifi.castlesiege.maps.helms_deep.WallEvent;
import me.huntifi.castlesiege.maps.objects.*;
import org.apache.commons.io.FileUtils;
import org.bukkit.*;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class Main extends JavaPlugin implements Listener {

    public static Plugin plugin;
    public static Main instance;

    public static MySQL SQL;

    private YamlDocument[] mapConfigs;
    private YamlDocument flagsConfig;
    private YamlDocument doorsConfig;
    private YamlDocument gatesConfig;
    private YamlDocument gameConfig;

    @Override
    public void onEnable() {

        getLogger().info("Enabling Plugin...");

        plugin = Bukkit.getServer().getPluginManager().getPlugin("CastleSiege");
        instance = this;

        getLogger().info("Resetting all maps...");
        resetWorlds();
        getLogger().info("Waiting until POSTWORLD to continue enabling...");
        new BukkitRunnable() {

            @Override
            public void run() {
                getLogger().info("Resuming loading plugin...");
                getLogger().info("Loading configuration files...");
                createConfigs();
                getLogger().info("Loading maps from configuration...");
                loadMaps();
                getLogger().info("Loading game configuration...");
                loadConfig();

                getLogger().info("Connecting to database...");
                // SQL Stuff
                sqlConnect();

                // World Guard
                SessionManager sessionManager = WorldGuard.getInstance().getPlatform().getSessionManager();
                // second param allows for ordering of handlers - see the JavaDocs
                sessionManager.registerHandler(CaptureHandler.FACTORY, null);

                // Tips
                new Tips().runTaskTimer(plugin, Tips.TIME_BETWEEN_TIPS * 20L, Tips.TIME_BETWEEN_TIPS * 20L);

                // Rewrite Events
                getServer().getPluginManager().registerEvents(new Enderchest(), plugin);
                getServer().getPluginManager().registerEvents(new PlayerChat(), plugin);

                // Connection
                getServer().getPluginManager().registerEvents(new PlayerConnect(), plugin);
                getServer().getPluginManager().registerEvents(new PlayerDisconnect(), plugin);

                // Combat
                getServer().getPluginManager().registerEvents(new ArrowRemoval(), plugin);
                getServer().getPluginManager().registerEvents(new AssistKill(), plugin);
                getServer().getPluginManager().registerEvents(new FallDamage(), plugin);
                getServer().getPluginManager().registerEvents(new HitMessage(), plugin);
                getServer().getPluginManager().registerEvents(new InCombat(), plugin);
                getServer().getPluginManager().registerEvents(new LobbyCombat(), plugin);
                getServer().getPluginManager().registerEvents(new TeamCombat(), plugin);
                getServer().getPluginManager().registerEvents(new DamageBalance(), plugin);

                // Death
                getServer().getPluginManager().registerEvents(new DeathEvent(), plugin);
                getServer().getPluginManager().registerEvents(new VoidLocation(), plugin);

                // Security
                getServer().getPluginManager().registerEvents(new InteractContainer(), plugin);
                getServer().getPluginManager().registerEvents(new InventoryProtection(), plugin);
                getServer().getPluginManager().registerEvents(new MapProtection(), plugin);

                // Kits
                getServer().getPluginManager().registerEvents(new Berserker(), plugin);
                getServer().getPluginManager().registerEvents(new Cavalry(), plugin);
                getServer().getPluginManager().registerEvents(new Crossbowman(), plugin);
                getServer().getPluginManager().registerEvents(new Engineer(), plugin);
                getServer().getPluginManager().registerEvents(new Executioner(), plugin);
                getServer().getPluginManager().registerEvents(new FireArcher(), plugin);
                getServer().getPluginManager().registerEvents(new Halberdier(), plugin);
                getServer().getPluginManager().registerEvents(new Ladderman(), plugin);
                getServer().getPluginManager().registerEvents(new Maceman(), plugin);
                getServer().getPluginManager().registerEvents(new Medic(), plugin);
                getServer().getPluginManager().registerEvents(new Ranger(), plugin);
                getServer().getPluginManager().registerEvents(new Spearman(), plugin);
                getServer().getPluginManager().registerEvents(new Viking(), plugin);
                getServer().getPluginManager().registerEvents(new Warhound(), plugin);
                getServer().getPluginManager().registerEvents(new Vanguard(), plugin);
                // Kit GUIs
                getServer().getPluginManager().registerEvents(new FreeKitGUI(), plugin);
                getServer().getPluginManager().registerEvents(new UnlockedKitGUI(), plugin);
                getServer().getPluginManager().registerEvents(new SelectorKitGUI(), plugin);

                // Rewrite Commands

                // Chat
                Objects.requireNonNull(getCommand("Message")).setExecutor(new PrivateMessage());
                Objects.requireNonNull(getCommand("Reply")).setExecutor(new ReplyMessage());
                Objects.requireNonNull(getCommand("TeamChat")).setExecutor(new TeamChat());

                // Donator
                Objects.requireNonNull(getCommand("LeaveMessage")).setExecutor(new LeaveMessage());
                Objects.requireNonNull(getCommand("JoinMessage")).setExecutor(new JoinMessage());

                // Gameplay
                Objects.requireNonNull(getCommand("SaveMap")).setExecutor(new MapController());
                Objects.requireNonNull(getCommand("Suicide")).setExecutor(new SuicideCommand());
                Objects.requireNonNull(getCommand("Switch")).setExecutor(new SwitchCommand());

                // Info
                Objects.requireNonNull(getCommand("CoinMultiplier")).setExecutor(new CoinMultiplier());
                Objects.requireNonNull(getCommand("Coins")).setExecutor(new CoinsCommand());
                Objects.requireNonNull(getCommand("Discord")).setExecutor(new DiscordCommand());
                Objects.requireNonNull(getCommand("Maps")).setExecutor(new MapsCommand());
                Objects.requireNonNull(getCommand("MVP")).setExecutor(new MVPCommand());
                Objects.requireNonNull(getCommand("MyStats")).setExecutor(new MyStatsCommand());
                Objects.requireNonNull(getCommand("Ping")).setExecutor(new PingCommand());
                Objects.requireNonNull(getCommand("Rules")).setExecutor(new RulesCommand());
                Objects.requireNonNull(getCommand("Teams")).setExecutor(new TeamsCommand());

                // Leaderboards
                Objects.requireNonNull(getCommand("Top")).setExecutor(new Leaderboard());
                Objects.requireNonNull(getCommand("TopCaptures")).setExecutor(new Leaderboard());
                Objects.requireNonNull(getCommand("TopDeaths")).setExecutor(new Leaderboard());
                Objects.requireNonNull(getCommand("TopKills")).setExecutor(new Leaderboard());

                // Staff - Punishments
                Objects.requireNonNull(getCommand("Ban")).setExecutor(new Ban());
                Objects.requireNonNull(getCommand("Kick")).setExecutor(new Kick());
                Objects.requireNonNull(getCommand("KickAll")).setExecutor(new KickAll());
                Objects.requireNonNull(getCommand("Mute")).setExecutor(new Mute());
                Objects.requireNonNull(getCommand("Unban")).setExecutor(new Unban());
                Objects.requireNonNull(getCommand("Unmute")).setExecutor(new Unmute());
                Objects.requireNonNull(getCommand("Warn")).setExecutor(new Warn());

                // Staff
                Objects.requireNonNull(getCommand("Broadcast")).setExecutor(new BroadcastMessage());
                Objects.requireNonNull(getCommand("CSReload")).setExecutor(new ReloadCommand());
                Objects.requireNonNull(getCommand("Fly")).setExecutor(new FlyCommand());
                Objects.requireNonNull(getCommand("SetCoinMultiplier")).setExecutor(new SetCoinMultiplier());
                Objects.requireNonNull(getCommand("SetStaffRank")).setExecutor(new SetStaffRank());
                Objects.requireNonNull(getCommand("GiveVote")).setExecutor(new GiveVoteCommand());
                Objects.requireNonNull(getCommand("NextMap")).setExecutor(new NextMapCommand());
                Objects.requireNonNull(getCommand("RankPoints")).setExecutor(new RankPoints());
                Objects.requireNonNull(getCommand("SetMap")).setExecutor(new SetMapCommand());
                Objects.requireNonNull(getCommand("SetTimer")).setExecutor(new SetTimerCommand());
                Objects.requireNonNull(getCommand("StaffChat")).setExecutor(new StaffChat());
                Objects.requireNonNull(getCommand("ToggleRank")).setExecutor(new ToggleRankCommand());

                // Kits
                Objects.requireNonNull(getCommand("Kit")).setExecutor(new KitCommand());
                Objects.requireNonNull(getCommand("Archer")).setExecutor(new Archer());
                Objects.requireNonNull(getCommand("Berserker")).setExecutor(new Berserker());
                Objects.requireNonNull(getCommand("Cavalry")).setExecutor(new Cavalry());
                Objects.requireNonNull(getCommand("Crossbowman")).setExecutor(new Crossbowman());
                Objects.requireNonNull(getCommand("Engineer")).setExecutor(new Engineer());
                Objects.requireNonNull(getCommand("Executioner")).setExecutor(new Executioner());
                Objects.requireNonNull(getCommand("FireArcher")).setExecutor(new FireArcher());
                Objects.requireNonNull(getCommand("Halberdier")).setExecutor(new Halberdier());
                Objects.requireNonNull(getCommand("Ladderman")).setExecutor(new Ladderman());
                Objects.requireNonNull(getCommand("Maceman")).setExecutor(new Maceman());
                Objects.requireNonNull(getCommand("Medic")).setExecutor(new Medic());
                Objects.requireNonNull(getCommand("Ranger")).setExecutor(new Ranger());
                Objects.requireNonNull(getCommand("Scout")).setExecutor(new Scout());
                Objects.requireNonNull(getCommand("Shieldman")).setExecutor(new Shieldman());
                Objects.requireNonNull(getCommand("Skirmisher")).setExecutor(new Skirmisher());
                Objects.requireNonNull(getCommand("Spearman")).setExecutor(new Spearman());
                Objects.requireNonNull(getCommand("Swordsman")).setExecutor(new Swordsman());
                Objects.requireNonNull(getCommand("Vanguard")).setExecutor(new Vanguard());
                Objects.requireNonNull(getCommand("Viking")).setExecutor(new Viking());
                Objects.requireNonNull(getCommand("Warhound")).setExecutor(new Warhound());

                // Map Specific
                // Helms Deep Only
                getServer().getPluginManager().registerEvents(new WallEvent(), plugin);
                CavesBoat boatEvent = new CavesBoat();
                getServer().getPluginManager().registerEvents(boatEvent, plugin);
                getServer().getScheduler().runTaskTimer(plugin, boatEvent, 300, 300);
                boatEvent.spawnBoat();

                // OLD EVENTS

                //getServer().getPluginManager().registerEvents(new Herugrim(), plugin);


                Bukkit.getServer().getScheduler().runTaskTimer(plugin, new BarCooldown(), 0, 1);
                Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Scoreboard(), 0, 20);
                Bukkit.getServer().getScheduler().runTaskTimer(plugin, new ApplyRegeneration(), 0, 75);
                Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Hunger(), 0, 20);
                Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(plugin, new KeepAlive(), 0, 5900);
                //Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(plugin, new Herugrim(), 10, 10);

                //Bukkit.getServer().getScheduler().runTaskTimer(plugin, new MainGateReadyRam(), 200, 60);
                //Bukkit.getServer().getScheduler().runTaskTimer(plugin, new MainGateRamAnimation(), 200, MainGateRam.rammingSpeed);
                //Bukkit.getServer().getScheduler().runTaskTimer(plugin, new MainGateRam(), 200, 40);

                //Bukkit.getServer().getScheduler().runTaskTimer(plugin, new GreatHallGateReadyRam(), 200, 60);
                //Bukkit.getServer().getScheduler().runTaskTimer(plugin, new GreatHallRamAnimation(), 200, GreatHallGateRam.rammingSpeed);
                //Bukkit.getServer().getScheduler().runTaskTimer(plugin, new GreatHallGateRam(), 200, 40);

                //Bukkit.getServer().getScheduler().runTaskTimer(plugin, new ThunderstoneGateReadyRam(), 200, 60);
                //Bukkit.getServer().getScheduler().runTaskTimer(plugin, new ThunderstoneRamAnimation(), 200, ThunderstoneRam.rammingSpeed);
                //Bukkit.getServer().getScheduler().runTaskTimer(plugin, new ThunderstoneRam(), 200, 40);

                getLogger().info("Plugin has been enabled!");

                // Begin the map loop
                MapController.startLoop();

//		new BukkitRunnable() {
//
//			@Override
//			public void run() {
//
//				Herugrim.spawnHerugrim();
//
//			}
//		}.runTaskLater(plugin, 200);
            }
        }.runTaskLater(plugin, 1);
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabling plugin...");
        // Unloads everything
        HandlerList.unregisterAll(plugin);
        for (World world:Bukkit.getWorlds()) {
            Bukkit.unloadWorld(world, false);
        }
        WorldCreator worldCreator = new WorldCreator("HelmsDeep");
        worldCreator.generateStructures(false);
        worldCreator.createWorld();
        StoreData.storeAll();
        try {
            SQL.disconnect();
        } catch (NullPointerException ex) {
            getLogger().warning("SQL could not disconnect, it doesn't exist!");
        }
        getLogger().info("Plugin has been disabled!");
    }

    private void resetWorlds() {
        //Creating a File object for directory
        File directoryPath = new File(String.valueOf(Bukkit.getWorldContainer()));
        //List of all files and directories
        String[] contents = directoryPath.list();
        assert contents != null;
        for (String content : contents) {
            if (content.endsWith("_save")) {
                String worldName = content.substring(0, content.length() - 5);
                try {
                    FileUtils.copyDirectory(new File(Bukkit.getWorldContainer(), worldName + "_save"),
                            new File(Bukkit.getWorldContainer(), worldName));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void createWorld(String worldName) {
        WorldCreator worldCreator = new WorldCreator(worldName);
        worldCreator.generateStructures(false);
        World world = worldCreator.createWorld();
        assert world != null;
        world.setAutoSave(false);
    }

    private void sqlConnect() {
        SQL = new MySQL();

        try {
            SQL.connect();
        } catch (ClassNotFoundException | SQLException e) {
            getLogger().warning("<!> Database is not connected! <!>");
        }

        if (SQL.isConnected()) {
            getLogger().info("<!> Database is connected! <!>");
            this.getServer().getPluginManager().registerEvents(this, this);
        }
    }

    public YamlDocument getFlagsConfig() {
        return this.flagsConfig;
    }

    public YamlDocument getDoorsConfig() {
        return this.doorsConfig;
    }

    public YamlDocument getGatesConfig() {
        return this.gatesConfig;
    }

    private void createConfigs() {

        // Setup the vector adapter
        TypeAdapter<Vector> vectorAdapter = new TypeAdapter<Vector>() {
            @NotNull
            @Override
            public java.util.Map<Object, Object> serialize(@NotNull Vector vector) {
                java.util.Map<Object, Object> map = new HashMap<>();
                map.put("x", vector.getX());
                map.put("y", vector.getY());
                map.put("z", vector.getZ());
                return map;
            }

            @NotNull
            @Override
            public Vector deserialize(@NotNull java.util.Map<Object, Object> map) {
                Vector vector = new Vector();
                vector.setX((Integer) map.get("x"));
                vector.setY((Integer) map.get("y"));
                vector.setZ((Integer) map.get("z"));
                return vector;
            }

            //public Vector deserialize(@NotNull ArrayList<>)
        };
        StandardSerializer.getDefault().register(Vector.class, vectorAdapter);

        // Setup the frame adapter
        TypeAdapter<Frame> frameAdapter = new TypeAdapter<Frame>() {

            @NotNull
            public java.util.Map<Object, Object> serialize(@NotNull Frame object) {
                java.util.Map<Object, Object> map = new HashMap<>();
                map.put("primary_blocks", object.primary_blocks);
                map.put("secondary_blocks", object.secondary_blocks);
                map.put("air", object.air);
                return map;
            }

            @NotNull
            public Frame deserialize(@NotNull java.util.Map<Object, Object> map) {
                Frame frame = new Frame();
                if (map.get("primary_blocks") != null) {
                    for (Object v : (ArrayList) map.get("primary_blocks")) {
                        frame.primary_blocks.add(vectorAdapter.deserialize((LinkedHashMap<Object, Object>) v));
                    }
                }
                if (map.get("secondary_blocks") != null) {
                    for (Object v : (ArrayList) map.get("secondary_blocks")) {
                        frame.secondary_blocks.add(vectorAdapter.deserialize((LinkedHashMap<Object, Object>) v));
                    }
                }
                if (map.get("air") != null) {
                    for (Object v : (ArrayList) map.get("air")) {
                        frame.air.add(vectorAdapter.deserialize((LinkedHashMap<Object, Object>) v));
                    }
                }
                return frame;
            }
        };
        StandardSerializer.getDefault().register(Frame.class, frameAdapter);

        // Load flags.yml with BoostedYAML
        try {
            flagsConfig = YamlDocument.create(new File(getDataFolder(), "flags.yml"),
                    getClass().getResourceAsStream("flags.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Load doors.yml with BoostedYAML
        try {
            doorsConfig = YamlDocument.create(new File(getDataFolder(), "doors.yml"),
                    getClass().getResourceAsStream("doors.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Load gates.yml with BoostedYAML
        try {
            gatesConfig = YamlDocument.create(new File(getDataFolder(), "gates.yml"),
                    getClass().getResourceAsStream("gates.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Add all config ymls from the maps folder
        File directoryPath = new File(String.valueOf(getDataFolder()), "maps");
        // List of all files and directories
        String[] contents = directoryPath.list();
        assert contents != null;
        mapConfigs = new YamlDocument[contents.length];
        for (int i = 0; i < contents.length; i++) {
            if (contents[i].endsWith(".yml")) {
                // Load the yml with BoostedYAML
                try {
                    mapConfigs[i] = YamlDocument.create(new File(directoryPath.getPath(), contents[i]),
                            getClass().getResourceAsStream(contents[i]));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // Load config.yml with BoostedYAML
        try {
            gameConfig = YamlDocument.create(new File(getDataFolder(), "config.yml"),
                    getClass().getResourceAsStream("config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets values in MapController from config.yml
     * Anything missing is set to the defaults supplied in MapController
     */
    private void loadConfig() {
        // Map Count
        MapController.mapCount = gameConfig.getInt(Route.from("map_count"), MapController.mapCount);

        // Delays
        Route route = Route.from("delays");
        MapController.preGameDelay = gameConfig.getInt(route.add("game"), MapController.preGameDelay);
        MapController.preMapDelay = gameConfig.getInt(route.add("map"), MapController.preMapDelay);
        MapController.explorationTime = gameConfig.getInt(route.add("exploration"), MapController.explorationTime);

        // Match Details
        route = Route.from("match");
        MapController.isMatch = gameConfig.getBoolean(route.add("enabled"), MapController.isMatch);
        if (MapController.isMatch) {
            MapController.keepTeams = gameConfig.getBoolean(route.add("keep_teams"), MapController.keepTeams);
            if (gameConfig.contains(route.add("maps"))) {
                MapController.setMaps(gameConfig.getStringList(route.add("maps")));
            }
        }
    }

    private void loadMaps() {
        // Load the maps
        for (YamlDocument config : mapConfigs) {
            // Probably shouldn't do this, but it makes it easier
            if (config == null)
                continue;
            String[] mapPaths = getPaths(config, null);

            for (String mapPath : mapPaths) {
                // Basic Map Details
                Map map = new Map();
                Route mapRoute = Route.from(mapPath);
                map.name = config.getString(mapRoute.add("name"));
                map.worldName = config.getString(mapRoute.add("world"));
                map.gamemode = Gamemode.valueOf(config.getString(mapRoute.add("gamemode")));

                // World Data
                createWorld(map.worldName);

                // Flag Data
                loadFlags(mapRoute, map);
                // Doors
                loadDoors(mapRoute, map);
                // Gates
                loadGates(mapRoute, map);

                // Team Data
                String[] teamPaths = getPaths(config, Route.from(mapPath).add("teams"));
                map.teams = new Team[teamPaths.length];
                for (int j = 0; j < teamPaths.length; j++) {
                    Route route = mapRoute.add("teams").add(teamPaths[j]);
                    map.teams[j] = loadTeam(route, map, config);
                }

                // Timer data
                map.duration = new Tuple<>(config.getInt(mapRoute.add("duration").add("minutes")),
                        config.getInt(mapRoute.add("duration").add("seconds")));

                // Save the map
                MapController.maps.add(map);
            }
        }
    }

    private void loadFlags(Route mapRoute, Map map) {
        String[] flagPaths = getPaths(getFlagsConfig(), mapRoute);

        map.flags = new Flag[flagPaths.length];
        for (int i = 0; i < flagPaths.length; i++) {
            // Create the flag
            Route flagRoute = mapRoute.add(flagPaths[i]);
            String name = getFlagsConfig().getString(flagRoute.add("name"));
            Flag flag;
            if (getFlagsConfig().contains(flagRoute.add("start_amount"))) {
                flag = new Flag(name,
                        getFlagsConfig().getString(flagRoute.add("start_owners")),
                        getFlagsConfig().getInt(flagRoute.add("max_cap")),
                        getFlagsConfig().getInt(flagRoute.add("progress_amount")),
                        getFlagsConfig().getInt(flagRoute.add("start_amount")));
            } else {
                flag = new Flag(name,
                        getFlagsConfig().getString(flagRoute.add("start_owners")),
                        getFlagsConfig().getInt(flagRoute.add("max_cap")),
                        getFlagsConfig().getInt(flagRoute.add("progress_amount")));
            }

            // Set the spawn point
            flag.spawnPoint = getLocation(flagRoute.add("spawn_point"), map.worldName, flagsConfig);

            // Set the capture area
            Route captureRoute = flagRoute.add("capture_area");
            if (getFlagsConfig().contains(captureRoute)
                    && getFlagsConfig().getString(captureRoute.add("type")).equalsIgnoreCase("cuboid")) {
                BlockVector3 min = BlockVector3.at(getFlagsConfig().getInt(captureRoute.add("min").add("x")),
                        getFlagsConfig().getInt(captureRoute.add("min").add("y")),
                        getFlagsConfig().getInt(captureRoute.add("min").add("z")));
                BlockVector3 max = BlockVector3.at(getFlagsConfig().getInt(captureRoute.add("max").add("x")),
                        getFlagsConfig().getInt(captureRoute.add("max").add("y")),
                        getFlagsConfig().getInt(captureRoute.add("max").add("z")));
                flag.region = new ProtectedCuboidRegion(flag.name.replace(' ', '_'), true, min, max);

                flag.region.setFlag(Flags.BLOCK_BREAK, StateFlag.State.ALLOW);
                flag.region.setFlag(Flags.BLOCK_PLACE, StateFlag.State.ALLOW);
                flag.region.setFlag(Flags.INTERACT, StateFlag.State.ALLOW);
                flag.region.setFlag(Flags.PVP, StateFlag.State.ALLOW);
                //Objects.requireNonNull(WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(Objects.requireNonNull(Bukkit.getWorld(map.worldName))))).addRegion(region);
            }

            if (getFlagsConfig().contains(captureRoute)) {
                flag.animationAir = getFlagsConfig().getBoolean(flagRoute.add("animation_air"));
                Route animationRoute = flagRoute.add("animation");
                String[] animationPaths = getPaths(getFlagsConfig(), animationRoute);

                flag.animation = new Frame[animationPaths.length];
                for (int j = 0; j < animationPaths.length; j++) {
                    Frame frame = getFlagsConfig().getAs(animationRoute.add(animationPaths[j]), Frame.class);
                    flag.animation[j] = frame;
                }
            }

            flag.scoreboard = getFlagsConfig().getInt(flagRoute.add("scoreboard"));

            map.flags[i] = flag;
        }
    }

    private Team loadTeam(Route teamPath, Map map, YamlDocument config) {
        String name = config.getString(teamPath.add("name"));
        Team team = new Team(name);

        // Colours
        Tuple<Material, ChatColor> colors = getColors(Objects.requireNonNull(config.getString(teamPath.add("primary_color")).toLowerCase()));
        team.primaryWool = colors.getFirst();
        team.primaryChatColor = colors.getSecond();
        colors = getColors(Objects.requireNonNull(config.getString(teamPath.add("secondary_color")).toLowerCase()));
        team.secondaryWool = colors.getFirst();
        team.secondaryChatColor = colors.getSecond();

        // Setup lobby
        team.lobby = loadLobby(teamPath.add("lobby"), map, config);
        return team;
    }

    private Lobby loadLobby(Route lobbyPath, Map map, YamlDocument config) {
        Lobby lobby = new Lobby();
        lobby.spawnPoint = getLocation(lobbyPath.add("spawn_point"), map.worldName, config);
        lobby.woolmap = loadWoolMap(lobbyPath.add("woolmap"), map, config);
        return lobby;
    }

    private WoolMap loadWoolMap(Route woolMapPath, Map map, YamlDocument config) {
        WoolMap woolMap = new WoolMap();
        String[] mapFlags = getPaths(config, woolMapPath);
        woolMap.woolMapBlocks = new WoolMapBlock[mapFlags.length];

        // Loop through all the wool blocks
        for (int i = 0; i < mapFlags.length; i++) {
            WoolMapBlock block = new WoolMapBlock();
            Route mapFlagRoute = woolMapPath.add(mapFlags[i]);

            block.flagName = config.getString(mapFlagRoute.add("flag_name"));
            block.blockLocation = getLocation(mapFlagRoute.add("wool_position"), map.worldName, config);
            block.signLocation = block.blockLocation.clone();

            // Get the wall sign's direction
            switch (Objects.requireNonNull(config.getString(mapFlagRoute.add("sign_direction")).toLowerCase())) {
                case "east":
                    block.signLocation.add(1, 0, 0);
                    break;
                case "south":
                    block.signLocation.add(0, 0, 1);
                    break;
                case "west":
                    block.signLocation.add(-1, 0, 0);
                    break;
                case "north":
                    block.signLocation.add(0, 0, -1);
                case "up":
                    block.signLocation.add(0, 1, 0);
                default:
                    block.signLocation.add(0, 0, 0);
            }
            woolMap.woolMapBlocks[i] = block;
        }

        return woolMap;
    }

    private void loadDoors(Route mapRoute, Map map) {
        if (!getDoorsConfig().contains(mapRoute)) {
            map.doors = new Door[0];
            return;
        }
        String[] doorPaths = getPaths(getDoorsConfig(), mapRoute);

        map.doors = new Door[doorPaths.length];
        for (int i = 0; i < doorPaths.length; i++) {
            // Create the flag
            Route doorRoute = mapRoute.add(doorPaths[i]);
            String flagName = getDoorsConfig().getString(doorRoute.add("flag"), map.name);
            // Get the location at the centre of the object
            Location centre = getLocation(doorRoute.add("centre"), map.worldName, getDoorsConfig());
            // Fancy shit that makes the Tuple array of locations and materials
            Route locationRoute = doorRoute.add("locations");
            Route openMaterialRoute = doorRoute.add("open_materials");
            Route closeMaterialRoute = doorRoute.add("closed_materials");
            ArrayList<LinkedHashMap> doorVectors = (ArrayList<LinkedHashMap>) getDoorsConfig().get(locationRoute);
            ArrayList<String> openDoorMaterials = (ArrayList<String>) getDoorsConfig().getStringList(openMaterialRoute, null);
            ArrayList<String> closeDoorMaterials = (ArrayList<String>) getDoorsConfig().getStringList(closeMaterialRoute, null);
            Tuple<Vector, Tuple<Material, Material>>[] doorBlocks = new Tuple[doorVectors.size()];
            for (int j = 0; j < doorVectors.size(); j++) {
                try {
                    Tuple<Material, Material> mats = new Tuple<>(Material.AIR, Material.AIR);
                    if (closeDoorMaterials != null && j < closeDoorMaterials.size())
                        mats.setFirst(Material.getMaterial(closeDoorMaterials.get(j)));
                    else if (closeDoorMaterials != null)
                        mats.setFirst(Material.getMaterial(closeDoorMaterials.get(j % closeDoorMaterials.size())));

                    if (openDoorMaterials != null && j < openDoorMaterials.size())
                        mats.setSecond(Material.getMaterial(openDoorMaterials.get(j)));
                    else if (openDoorMaterials != null)
                        mats.setSecond(Material.getMaterial(openDoorMaterials.get(j % openDoorMaterials.size())));

                    doorBlocks[j] = new Tuple<>(new Vector(
                            (int) doorVectors.get(j).get("x"),
                            (int) doorVectors.get(j).get("y"),
                            (int) doorVectors.get(j).get("z")),
                            mats);
                }
                catch (NullPointerException e) {
                    getLogger().warning("Missing material on " + doorPaths[i]);
                }
            }
            // Split up the variations for lever and pressure plate doors
            Tuple<Sound, Sound> sounds;
            Door door;
            switch (getDoorsConfig().getString(doorRoute.add("trigger"), "plate").toLowerCase()) {
                case "switch":
                case "lever":
                    sounds = new Tuple<>(Sound.valueOf(getDoorsConfig().getString(doorRoute.add("closed_sound"), "ENTITY_ZOMBIE_ATTACK_IRON_DOOR")),
                            Sound.valueOf(getDoorsConfig().getString(doorRoute.add("open_sound"), "ENTITY_ZOMBIE_ATTACK_IRON_DOOR")));
                    Location leverPos = getLocation(doorRoute.add("lever_position"), map.worldName, getDoorsConfig());
                    int timer = (int) (getDoorsConfig().getFloat(doorRoute.add("timer"), 10f) * 20);
                    door = new LeverDoor(flagName, centre, doorBlocks, sounds, timer, leverPos);
                    break;
                case "pressureplate":
                case "pressure plate":
                case "plate":
                case "pressure":
                default:
                    sounds = new Tuple<>(Sound.valueOf(getDoorsConfig().getString(doorRoute.add("closed_sound"), "BLOCK_WOODEN_DOOR_OPEN")),
                            Sound.valueOf(getDoorsConfig().getString(doorRoute.add("open_sound"), "BLOCK_WOODEN_DOOR_OPEN")));
                    timer = (int) (getDoorsConfig().getFloat(doorRoute.add("timer"), 2f) * 20);
                    door = new PressurePlateDoor(flagName, centre, doorBlocks, sounds, timer);
                    break;
            }
            map.doors[i] = door;
        }
    }

    private void loadGates(Route mapRoute, Map map) {
        if (!getGatesConfig().contains(mapRoute)) {
            map.gates = new Gate[0];
            return;
        }
        String[] gatePaths = getPaths(getGatesConfig(), mapRoute);

        map.gates = new Gate[gatePaths.length];
        for (int i = 0; i < gatePaths.length; i++) {
            // Create the gate
            Route gateRoute = mapRoute.add(gatePaths[i]);
            Gate gate = new Gate(getGatesConfig().getString(gateRoute.add("display_name")));
            gate.setFlagName(getGatesConfig().getString(gateRoute.add("flag_name")), map.name);
            gate.setHealth(getGatesConfig().getInt(gateRoute.add("start_health")));

            gate.setHitBox(getGatesConfig().getAs(gateRoute.add("min"), Vector.class),
                    getGatesConfig().getAs(gateRoute.add("max"), Vector.class));
            gate.setSchematic(getGatesConfig().getString(gateRoute.add("schematic").add("name")),
                    getGatesConfig().getAs(gateRoute.add("schematic").add("location"), Vector.class));
            gate.setBreachSoundLocation(getGatesConfig().getAs(gateRoute.add("breach_sound"), Vector.class));

            map.gates[i] = gate;
        }
    }

    private String[] getPaths(YamlDocument file, Route route) {
        Set<String> set;
        if (route != null) {
            set = file.getSection(route).getRoutesAsStrings(false);
        } else {
            set = file.getRoutesAsStrings(false);
        }
        String[] paths = new String[set.size()];
        int index = 0;
        for (String str : set) {
            paths[index++] = str;
        }
        return paths;
    }

    private Location getLocation(Route locationPath, String worldName, YamlDocument config) {
        int x = config.getInt(locationPath.add("x"));
        int y = config.getInt(locationPath.add("y"));
        int z = config.getInt(locationPath.add("z"));
        int yaw = config.getInt(locationPath.add("yaw"), 90);
        int pitch = config.getInt(locationPath.add("pitch"), 0);
        return new Location(Bukkit.getServer().getWorld(worldName), x, y, z, yaw, pitch);
    }

    private Tuple<Material, ChatColor> getColors(String color) {
        Tuple<Material, ChatColor> colors = new Tuple<>(Material.WHITE_WOOL, ChatColor.WHITE);
        switch (color) {
            case "black":
                colors.setFirst(Material.BLACK_WOOL);
                colors.setSecond(ChatColor.DARK_GRAY);
                break;
            case "dark_blue":
                colors.setFirst(Material.BLUE_WOOL);
                colors.setSecond(ChatColor.DARK_BLUE);
                break;
            case "dark_green":
            case "green":
                colors.setFirst(Material.GREEN_WOOL);
                colors.setSecond(ChatColor.DARK_GREEN);
                break;
            case "cyan":
            case "dark_aqua":
                colors.setFirst(Material.CYAN_WOOL);
                colors.setSecond(ChatColor.DARK_AQUA);
                break;
            case "dark_red":
            case "red":
                colors.setFirst(Material.RED_WOOL);
                colors.setSecond(ChatColor.DARK_RED);
                break;
            case "brown":
                colors.setFirst(Material.BROWN_WOOL);
                colors.setSecond(ChatColor.DARK_RED);
                break;
            case "dark_purple":
            case "purple":
                colors.setFirst(Material.PURPLE_WOOL);
                colors.setSecond(ChatColor.DARK_PURPLE);
                break;
            case "gold":
            case "dark_gold":
            case "dark_yellow":
            case "orange":
                colors.setFirst(Material.ORANGE_WOOL);
                colors.setSecond(ChatColor.GOLD);
                break;
            case "light_gold":
                colors.setFirst(Material.YELLOW_WOOL);
                colors.setSecond(ChatColor.GOLD);
                break;
            case "light_gray":
            case "light_grey":
            case "gray":
                colors.setFirst(Material.LIGHT_GRAY_WOOL);
                colors.setSecond(ChatColor.GRAY);
                break;
            case "dark_grey":
            case "dark_gray":
            case "darkgrey":
            case "darkgray":
                colors.setFirst(Material.GRAY_WOOL);
                colors.setSecond(ChatColor.DARK_GRAY);
                break;
            case "blue":
                colors.setFirst(Material.BLUE_WOOL);
                colors.setSecond(ChatColor.BLUE);
                break;
            case "light_blue":
                colors.setFirst(Material.LIGHT_BLUE_WOOL);
                colors.setSecond(ChatColor.BLUE);
                break;
            case "light_green":
            case "lime":
                colors.setFirst(Material.LIME_WOOL);
                colors.setSecond(ChatColor.GREEN);
                break;
            case "aqua":
                colors.setFirst(Material.LIGHT_BLUE_WOOL);
                colors.setSecond(ChatColor.AQUA);
                break;
            case "light_red":
                colors.setFirst(Material.PINK_WOOL);
                colors.setSecond(ChatColor.RED);
                break;
            case "light_purple":
            case "magenta":
                colors.setFirst(Material.MAGENTA_WOOL);
                colors.setSecond(ChatColor.LIGHT_PURPLE);
                break;
            case "pink":
                colors.setFirst(Material.PINK_WOOL);
                colors.setSecond(ChatColor.LIGHT_PURPLE);
                break;
            case "light_yellow":
            case "yellow":
                colors.setFirst(Material.YELLOW_WOOL);
                colors.setSecond(ChatColor.YELLOW);
                break;
            case "white":
            default:
                colors.setFirst(Material.WHITE_WOOL);
                colors.setSecond(ChatColor.WHITE);
        }

        return colors;
    }

    public void reload() {
        plugin.getServer().broadcastMessage(ChatColor.DARK_AQUA + "[CastleSiege] " + ChatColor.GOLD + "Reloading plugin...");
        onDisable();
        onEnable();
        plugin.getServer().broadcastMessage(ChatColor.DARK_AQUA + "[CastleSiege] " + ChatColor.GOLD + "Plugin Reloaded!");
    }
}
