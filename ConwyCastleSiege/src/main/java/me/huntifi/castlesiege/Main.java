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
import me.huntifi.castlesiege.commands.*;
import me.huntifi.castlesiege.commands.chat.PrivateMessage;
import me.huntifi.castlesiege.commands.chat.ReplyMessage;
import me.huntifi.castlesiege.commands.chat.TeamChat;
import me.huntifi.castlesiege.commands.info.*;
import me.huntifi.castlesiege.commands.staff.*;
import me.huntifi.castlesiege.data_types.Frame;
import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.database.MySQL;
import me.huntifi.castlesiege.events.chat.PlayerChat;
import me.huntifi.castlesiege.events.combat.*;
import me.huntifi.castlesiege.database.StoreData;
import me.huntifi.castlesiege.events.connection.PlayerConnect;
import me.huntifi.castlesiege.events.connection.PlayerDisconnect;
import me.huntifi.castlesiege.events.death.DeathEvent;
import me.huntifi.castlesiege.events.death.VoidLocation;
import me.huntifi.castlesiege.events.security.InteractContainer;
import me.huntifi.castlesiege.events.security.InventoryProtection;
import me.huntifi.castlesiege.events.security.MapProtection;
import me.huntifi.castlesiege.kits.gui.FreeKitGUI;
import me.huntifi.castlesiege.kits.gui.SelectorKitGUI;
import me.huntifi.castlesiege.kits.gui.UnlockedKitGUI;
import me.huntifi.castlesiege.kits.items.Enderchest;
import me.huntifi.castlesiege.kits.kits.*;
import me.huntifi.castlesiege.maps.Map;
import me.huntifi.castlesiege.maps.*;
import me.huntifi.castlesiege.maps.helms_deep.CavesBoat;
import me.huntifi.castlesiege.maps.helms_deep.WallEvent;
import me.huntifi.castlesiege.maps.objects.CaptureHandler;
import me.huntifi.castlesiege.maps.objects.Door;
import me.huntifi.castlesiege.maps.objects.Flag;
import me.huntifi.castlesiege.security.Hunger;
import org.apache.commons.io.FileUtils;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
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

    private YamlConfiguration mapsConfig;
    private YamlDocument flagsConfig;
    private YamlDocument doorsConfig;

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
                getLogger().info("Loading all worlds...");
                createWorld();
                getLogger().info("Loading configuration files...");
                createConfigs();
                getLogger().info("Loading maps from configuration...");
                loadMaps();

                getLogger().info("Connecting to database...");
                // SQL Stuff
                sqlConnect();

                // World Guard
                SessionManager sessionManager = WorldGuard.getInstance().getPlatform().getSessionManager();
                // second param allows for ordering of handlers - see the JavaDocs
                sessionManager.registerHandler(CaptureHandler.FACTORY, null);

                // Rewrite Events
                getServer().getPluginManager().registerEvents(new Enderchest(), plugin);
                getServer().getPluginManager().registerEvents(new PlayerChat(), plugin);

                // Connection
                getServer().getPluginManager().registerEvents(new PlayerConnect(), plugin);
                getServer().getPluginManager().registerEvents(new PlayerDisconnect(), plugin);

                // Combat
                getServer().getPluginManager().registerEvents(new ArrowRemoval(), plugin);
                getServer().getPluginManager().registerEvents(new FallDamage(), plugin);
                getServer().getPluginManager().registerEvents(new HitMessage(), plugin);
                getServer().getPluginManager().registerEvents(new InCombat(), plugin);
                getServer().getPluginManager().registerEvents(new LobbyCombat(), plugin);
                getServer().getPluginManager().registerEvents(new TeamCombat(), plugin);

                // Death
                getServer().getPluginManager().registerEvents(new DeathEvent(), plugin);
                getServer().getPluginManager().registerEvents(new VoidLocation(), plugin);

                // Security
                getServer().getPluginManager().registerEvents(new InteractContainer(), plugin);
                getServer().getPluginManager().registerEvents(new InventoryProtection(), plugin);
                getServer().getPluginManager().registerEvents(new MapProtection(), plugin);

                // Kits
                getServer().getPluginManager().registerEvents(new Archer(), plugin);
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
                getServer().getPluginManager().registerEvents(new Scout(), plugin);
                getServer().getPluginManager().registerEvents(new Shieldman(), plugin);
                getServer().getPluginManager().registerEvents(new Skirmisher(), plugin);
                getServer().getPluginManager().registerEvents(new Spearman(), plugin);
                getServer().getPluginManager().registerEvents(new Swordsman(), plugin);
                getServer().getPluginManager().registerEvents(new Viking(), plugin);
                getServer().getPluginManager().registerEvents(new Warhound(), plugin);
                // Kit GUIs
                getServer().getPluginManager().registerEvents(new FreeKitGUI(), plugin);
                getServer().getPluginManager().registerEvents(new UnlockedKitGUI(), plugin);
                getServer().getPluginManager().registerEvents(new SelectorKitGUI(), plugin);

                // Rewrite Commands
                Objects.requireNonNull(getCommand("Suicide")).setExecutor(new SuicideCommand());
                Objects.requireNonNull(getCommand("Switch")).setExecutor(new SwitchCommand());
                Objects.requireNonNull(getCommand("CSReload")).setExecutor(new ReloadCommand());
                Objects.requireNonNull(getCommand("NextMap")).setExecutor(new NextMapCommand());
                Objects.requireNonNull(getCommand("SaveMap")).setExecutor(new MapController());

                // Chat
                Objects.requireNonNull(getCommand("Message")).setExecutor(new PrivateMessage());
                Objects.requireNonNull(getCommand("Reply")).setExecutor(new ReplyMessage());
                Objects.requireNonNull(getCommand("TeamChat")).setExecutor(new TeamChat());

                // Info
                Objects.requireNonNull(getCommand("Discord")).setExecutor(new DiscordCommand());
                Objects.requireNonNull(getCommand("Maps")).setExecutor(new MapsCommand());
                Objects.requireNonNull(getCommand("MVP")).setExecutor(new MVPCommand());
                Objects.requireNonNull(getCommand("Ping")).setExecutor(new PingCommand());
                Objects.requireNonNull(getCommand("Rules")).setExecutor(new RulesCommand());
                Objects.requireNonNull(getCommand("Teams")).setExecutor(new TeamsCommand());

                // Staff
                Objects.requireNonNull(getCommand("Fly")).setExecutor(new FlyCommand());
                Objects.requireNonNull(getCommand("Kick")).setExecutor(new KickCommand());
                Objects.requireNonNull(getCommand("KickAll")).setExecutor(new KickAllCommand());

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
                getServer().getPluginManager().registerEvents(new SessionMuteCommand(), plugin);
                //getServer().getPluginManager().registerEvents(new RegisterLevel(), plugin);

                //getServer().getPluginManager().registerEvents(new Herugrim(), plugin);

                //getServer().getPluginManager().registerEvents(new HelmsdeepJoin(), plugin);
                //getServer().getPluginManager().registerEvents(new HelmsdeepLeave(), plugin);
                //getServer().getPluginManager().registerEvents(new HelmsdeepDeath(), plugin);
                //getServer().getPluginManager().registerEvents(new FlagRadius(), plugin);
                //getServer().getPluginManager().registerEvents(new SupplyCampFlag(), plugin);
                //getServer().getPluginManager().registerEvents(new CavesFlag(), plugin);
                //getServer().getPluginManager().registerEvents(new MainGateFlag(), plugin);
                //getServer().getPluginManager().registerEvents(new CourtyardFlag(), plugin);
                //getServer().getPluginManager().registerEvents(new GreatHallsFlag(), plugin);
                //getServer().getPluginManager().registerEvents(new HornFlag(), plugin);
                //getServer().getPluginManager().registerEvents(new WoolMap(), plugin);

                //getServer().getPluginManager().registerEvents(new HelmsdeepCavesDoor(), plugin);
                //getServer().getPluginManager().registerEvents(new HelmsdeepGreatHallLeftDoor(), plugin);
                //getServer().getPluginManager().registerEvents(new HelmsdeepGreatHallRightDoor(), plugin);
                //getServer().getPluginManager().registerEvents(new HelmsdeepMainGateLeftDoor(), plugin);
                //getServer().getPluginManager().registerEvents(new HelmsdeepMainGateRightDoor(), plugin);
                //getServer().getPluginManager().registerEvents(new HelmsdeepStorageDoor(), plugin);
                //getServer().getPluginManager().registerEvents(new GreatHallExtraDoor(), plugin);

                //getServer().getPluginManager().registerEvents(new HelmsdeepMainGateDestroyEvent(), plugin);
                //getServer().getPluginManager().registerEvents(new HelmsdeepGreatHallDestroyEvent(), plugin);
                //getServer().getPluginManager().registerEvents(new HelmsdeepBallistaEvent(), plugin);

                //getServer().getPluginManager().registerEvents(new TwinbridgeFlag(), plugin);
                //getServer().getPluginManager().registerEvents(new SkyviewTowerFlag(), plugin);
                //getServer().getPluginManager().registerEvents(new LonelyTowerFlag(), plugin);
                //getServer().getPluginManager().registerEvents(new WestTowerFlag(), plugin);
                //getServer().getPluginManager().registerEvents(new StairhallFlag(), plugin);
                //getServer().getPluginManager().registerEvents(new ShiftedTowerFlag(), plugin);
                //getServer().getPluginManager().registerEvents(new TS_FlagRadius(), plugin);
                //getServer().getPluginManager().registerEvents(new ThunderstoneDeath(), plugin);
                //getServer().getPluginManager().registerEvents(new ThunderstoneJoin(), plugin);
                //getServer().getPluginManager().registerEvents(new HelmsdeepEndMVP(), plugin);
                //getServer().getPluginManager().registerEvents(new ThunderstoneEndMVP(), plugin);
                //getServer().getPluginManager().registerEvents(new TS_Woolmap(), plugin);
                //getServer().getPluginManager().registerEvents(new TS_Woolmap_Distance(), plugin);
                //getServer().getPluginManager().registerEvents(new ThunderstoneLeave(), plugin);
                //getServer().getPluginManager().registerEvents(new ThunderstoneGateDestroyEvent(), plugin);
                //getServer().getPluginManager().registerEvents(new EastTowerFlag(), plugin);

                getCommand("togglerank").setExecutor(new togglerankCommand());

                //getCommand("Mvp").setExecutor(new mvpCommand());
                //getCommand("Mystats").setExecutor(new MystatsCommand());

                //getCommand("CheckFlagList").setExecutor(new FlagListCommand());
                getCommand("s").setExecutor(new StaffChat());

                getCommand("sessionmute").setExecutor(new SessionMuteCommand());

                getCommand("Unsessionmute").setExecutor(new UnsessionmuteCommand());


                //Bukkit.getServer().getScheduler().runTaskTimer(plugin, new ThunderstoneEndMap(), 0, 200);
                //Bukkit.getServer().getScheduler().runTaskTimer(plugin, new HelmsdeepCaveBoat(), 0, 200);
                Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Scoreboard(), 0, 20);
                //Bukkit.getServer().getScheduler().runTaskTimer(plugin, new HelmsdeepEndMap(), 0, 20);
                Bukkit.getServer().getScheduler().runTaskTimer(plugin, new ApplyRegeneration(), 0, 75);
                Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Hunger(), 0, 20);
                //Bukkit.getServer().getScheduler().runTaskTimer(plugin, new HelmsdeepMVPupdater(), 0, 20);
                //Bukkit.getServer().getScheduler().runTaskTimer(plugin, new ThunderstoneMVPupdater(), 0, 20);
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

                // Cheap Rewrite Stuff
                MapController.startLoop();

                //Tablist

//		plugin.tab = new Tablist(plugin);
//		tab.addHeader("&e*_︵_︵_︵_ &5ConwyMC&e _︵_︵_︵_*\n&5Join the discord: &bhttps://discord.gg/AUDqTpC");
//		tab.addHeader("&e*_︵_︵_︵_ &dConwyMC&e _︵_︵_︵_*\n&dJoin the discord: &bhttps://discord.gg/AUDqTpC");
//
//		tab.addFooter("&5Castle Siege  -  Alpha Testing");
//		tab.addFooter("&dCastle Siege  -  Alpha Testing");
//
//		tab.showTab();

                //Activate the map Helmsdeep + reset

//		HelmsdeepReset.onReset();
//		HelmsdeepTimer.HelmsdeepTimerEvent();
//		HelmsdeepGreatHallBlocks.gateblocks();
//		HelmsdeepMainGateBlocks.gateblocks();

                //resets Thunderstone after restart
                //ThunderstoneReset.onReset();
                //ThunderstoneGateBlocks.gateblocks();

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
        for (MapsList mapName : MapsList.values()) {
            try {
                FileUtils.copyDirectory(new File(Bukkit.getWorldContainer(), mapName + "_save"),
                        new File(Bukkit.getWorldContainer(), mapName.toString()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void createWorld() {
        for (MapsList mapName : MapsList.values()) {
            WorldCreator worldCreator = new WorldCreator(mapName.name());
            worldCreator.generateStructures(false);
            worldCreator.createWorld();
        }
        for (World world : Bukkit.getWorlds()) {
            world.setAutoSave(false);
        }
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

    // File Configuration
    public YamlConfiguration getMapsConfig() {
        return this.mapsConfig;
    }

    public YamlDocument getFlagsConfig() {
        return this.flagsConfig;
    }

    public YamlDocument getDoorsConfig() {
        return this.doorsConfig;
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

        // Load maps.yml with Spigot's yaml parser
        File mapsFile = new File(getDataFolder(), "maps.yml");
        if (!mapsFile.exists()) {
            mapsFile.getParentFile().mkdirs();
            saveResource("maps.yml", false);
        }

        mapsConfig = new YamlConfiguration();
        try {
            mapsConfig.load(mapsFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    private void loadMaps() {
        // Load the maps
        java.util.Map<String, Object> stringObjectMap = Objects.requireNonNull(this.getMapsConfig().getConfigurationSection("")).getValues(false);
        String[] mapPaths = stringObjectMap.keySet().toArray(new String[stringObjectMap.size()]);

        MapController.maps = new Map[mapPaths.length];
        for (int i = 0; i < mapPaths.length; i++) {
            // Basic Map Details
            Map map = new Map();
            map.name = this.getMapsConfig().getString(mapPaths[i] + ".name");
            map.worldName = this.getMapsConfig().getString(mapPaths[i] + ".world");
            map.gamemode = Gamemode.valueOf(this.getMapsConfig().getString(mapPaths[i] + ".gamemode"));

            // Flag Data
            loadFlags(mapPaths[i], map);
            // Doors
            loadDoors(mapPaths[i], map);

            // Team Data
            java.util.Map<String, Object> stringObjectTeam = Objects.requireNonNull(this.getMapsConfig().getConfigurationSection(mapPaths[i] + ".teams")).getValues(false);
            String[] teamPaths = stringObjectTeam.keySet().toArray(new String[stringObjectTeam.size()]);
            map.teams = new Team[teamPaths.length];
            for (int j = 0; j < teamPaths.length; j++) {
                String path = mapPaths[i] + ".teams." + teamPaths[j];
                map.teams[j] = loadTeam(path, map);
            }

            // Timer data
            map.duration = new Tuple<>(this.getMapsConfig().getInt(mapPaths[i] + ".duration.minutes"),
                    this.getMapsConfig().getInt(mapPaths[i] + ".duration.seconds"));

            // Save the map
            MapController.maps[i] = map;
        }
    }

    private void loadFlags(String mapPath, Map map) {
        Route mapRoute = Route.from(mapPath);
        String[] flagPaths = getPaths(getFlagsConfig(), mapRoute);

        map.flags = new Flag[flagPaths.length];
        for (int i = 0; i < flagPaths.length; i++) {
            // Create the flag
            Route flagRoute = mapRoute.add(flagPaths[i]);
            String name = getFlagsConfig().getString(flagRoute.add("name"));
            Flag flag = new Flag(name,
                    getFlagsConfig().getString(flagRoute.add("start_owners")),
                    getFlagsConfig().getInt(flagRoute.add("max_cap")),
                    getFlagsConfig().getInt(flagRoute.add("progress_amount")));

            // Set the spawn point
            flag.spawnPoint = new Location(Bukkit.getWorld(map.worldName),
                    getFlagsConfig().getDouble(flagRoute.add("spawn_point").add("x")),
                    getFlagsConfig().getDouble(flagRoute.add("spawn_point").add("y")),
                    getFlagsConfig().getDouble(flagRoute.add("spawn_point").add("z")));

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

    private Team loadTeam(String teamPath, Map map) {
        String name = this.getMapsConfig().getString(teamPath + ".name");
        Team team = new Team(name);

        // Colours
        Tuple<Material, ChatColor> colors = getColors(Objects.requireNonNull(this.getMapsConfig().getString(teamPath + ".primary_color")).toLowerCase());
        team.primaryWool = colors.getFirst();
        team.primaryChatColor = colors.getSecond();
        colors = getColors(Objects.requireNonNull(this.getMapsConfig().getString(teamPath + ".secondary_color")).toLowerCase());
        team.secondaryWool = colors.getFirst();
        team.secondaryChatColor = colors.getSecond();

        // Setup lobby
        team.lobby = loadLobby(teamPath + ".lobby", map);
        return team;
    }

    private Lobby loadLobby(String lobbyPath, Map map) {
        Lobby lobby = new Lobby();
        lobby.spawnPoint = getLocationYawPitch(lobbyPath + ".spawn_point", map.worldName);
        lobby.woolmap = loadWoolMap(lobbyPath + ".woolmap", map);
        return lobby;
    }

    private WoolMap loadWoolMap(String woolMapPath, Map map) {
        WoolMap woolMap = new WoolMap();
        java.util.Map<String, Object> stringObjectMap = Objects.requireNonNull(this.getMapsConfig().getConfigurationSection(woolMapPath)).getValues(false);
        String[] mapFlags = stringObjectMap.keySet().toArray(new String[stringObjectMap.size()]);
        woolMap.woolMapBlocks = new WoolMapBlock[mapFlags.length];

        // Loop through all the wool blocks
        for (int i = 0; i < mapFlags.length; i++) {
            WoolMapBlock block = new WoolMapBlock();

            block.flagName = this.getMapsConfig().getString(woolMapPath + "." + mapFlags[i] + ".flag_name");
            block.blockLocation = getLocation(woolMapPath + "." + mapFlags[i] + ".wool_position", map.worldName);
            block.signLocation = block.blockLocation.clone();

            // Get the wall sign's direction
            switch (Objects.requireNonNull(this.getMapsConfig().getString(woolMapPath + "." + mapFlags[i] + ".sign_direction")).toLowerCase()) {
                case "east":
                    block.signDirection = BlockFace.EAST;
                    block.signLocation.add(1, 0, 0);
                    break;
                case "south":
                    block.signDirection = BlockFace.SOUTH;
                    block.signLocation.add(0, 0, 1);
                    break;
                case "west":
                    block.signDirection = BlockFace.WEST;
                    block.signLocation.add(-1, 0, 0);
                    break;
                default:
                    block.signDirection = BlockFace.NORTH;
                    block.signLocation.add(0, 0, -1);
            }
            woolMap.woolMapBlocks[i] = block;
        }

        return woolMap;
    }

    private void loadDoors(String mapPath, Map map) {
        Route mapRoute = Route.from(mapPath);
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
            //Location centre = getDoorsConfig().getAs(doorRoute.add("centre"), Vector.class).toLocation(Objects.requireNonNull(Bukkit.getWorld(map.worldName)));
            Location centre = new Location(Bukkit.getWorld(map.worldName),
                    getDoorsConfig().getInt(doorRoute.add("centre").add("x")),
                    getDoorsConfig().getInt(doorRoute.add("centre").add("y")),
                    getDoorsConfig().getInt(doorRoute.add("centre").add("z")));
            // Fancy shit that makes the Tuple array of locations and materials
            Route locationRoute = doorRoute.add("locations");
            Route materialRoute = doorRoute.add("materials");
            ArrayList<LinkedHashMap> doorVectors = (ArrayList<LinkedHashMap>) getDoorsConfig().get(locationRoute);
            ArrayList<String> doorMaterials = (ArrayList<String>) getDoorsConfig().getStringList(materialRoute);
            Tuple[] doorBlocks = new Tuple[doorVectors.size()];
            for (int j = 0; j < doorVectors.size(); j++) {
                try {
                    doorBlocks[j] = new Tuple<>(new Vector(
                            (int) doorVectors.get(j).get("x"),
                            (int) doorVectors.get(j).get("y"),
                            (int) doorVectors.get(j).get("z")),
                            Material.getMaterial(doorMaterials.get(j)));
                }
                catch (NullPointerException e) {
                    getLogger().warning("Missing material on " + doorPaths[i]);
                }
            }
            Door door = new Door(flagName, centre, doorBlocks);
            map.doors[i] = door;
        }
    }

    private String[] getPaths(YamlDocument file, Route route) {
        Set<String> set = file.getSection(route).getRoutesAsStrings(false);
        String[] paths = new String[set.size()];
        int index = 0;
        for (String str : set) {
            paths[index++] = str;
        }
        return paths;
    }

    private Location getLocationYawPitch(String locationPath, String worldName) {
        int x = this.getMapsConfig().getInt(locationPath + ".x");
        int y = this.getMapsConfig().getInt(locationPath + ".y");
        int z = this.getMapsConfig().getInt(locationPath + ".z");
        int yaw = this.getMapsConfig().getInt(locationPath + ".yaw");
        int pitch = this.getMapsConfig().getInt(locationPath + ".pitch");
        return new Location(Bukkit.getServer().getWorld(worldName), x, y, z, yaw, pitch);
    }

    private Location getLocation(String locationPath, String worldName) {
        int x = this.getMapsConfig().getInt(locationPath + ".x");
        int y = this.getMapsConfig().getInt(locationPath + ".y");
        int z = this.getMapsConfig().getInt(locationPath + ".z");
        return new Location(Bukkit.getServer().getWorld(worldName), x, y, z);
    }

    private Tuple<Material, ChatColor> getColors(String color) {
        Tuple<Material, ChatColor> colors = new Tuple<>(Material.WHITE_WOOL, ChatColor.WHITE);
        switch (color) {
            case "black":
                colors.setFirst(Material.BLACK_WOOL);
                colors.setSecond(ChatColor.DARK_GRAY);
                break;
            case "dark_blue":
            case "blue":
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
            case "dark_purple":
            case "purple":
                colors.setFirst(Material.PURPLE_WOOL);
                colors.setSecond(ChatColor.DARK_PURPLE);
                break;
            case "gold":
            case "dark_yellow":
            case "orange":
                colors.setFirst(Material.ORANGE_WOOL);
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
