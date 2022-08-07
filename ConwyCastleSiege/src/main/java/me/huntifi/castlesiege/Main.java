package me.huntifi.castlesiege;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.session.SessionManager;
import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.route.Route;
import dev.dejvokep.boostedyaml.serialization.standard.StandardSerializer;
import dev.dejvokep.boostedyaml.serialization.standard.TypeAdapter;
import me.huntifi.castlesiege.commands.chat.GlobalChat;
import me.huntifi.castlesiege.commands.chat.PrivateMessage;
import me.huntifi.castlesiege.commands.chat.ReplyMessage;
import me.huntifi.castlesiege.commands.chat.TeamChat;
import me.huntifi.castlesiege.commands.donator.JoinMessage;
import me.huntifi.castlesiege.commands.donator.LeaveMessage;
import me.huntifi.castlesiege.commands.gameplay.*;
import me.huntifi.castlesiege.commands.info.*;
import me.huntifi.castlesiege.commands.info.leaderboard.Donators;
import me.huntifi.castlesiege.commands.info.leaderboard.Leaderboard;
import me.huntifi.castlesiege.commands.info.leaderboard.MVPCommand;
import me.huntifi.castlesiege.commands.info.leaderboard.TopMatch;
import me.huntifi.castlesiege.commands.mojang.WhoisCommand;
import me.huntifi.castlesiege.commands.staff.*;
import me.huntifi.castlesiege.commands.staff.coins.AddCoins;
import me.huntifi.castlesiege.commands.staff.coins.SetCoinMultiplier;
import me.huntifi.castlesiege.commands.staff.coins.SetCoins;
import me.huntifi.castlesiege.commands.staff.coins.TakeCoins;
import me.huntifi.castlesiege.commands.staff.donations.SetKitCommand;
import me.huntifi.castlesiege.commands.staff.donations.UnlockedKitCommand;
import me.huntifi.castlesiege.commands.staff.punishments.*;
import me.huntifi.castlesiege.data_types.Frame;
import me.huntifi.castlesiege.data_types.PlayerData;
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
import me.huntifi.castlesiege.events.gameplay.Explosion;
import me.huntifi.castlesiege.events.gameplay.HorseHandler;
import me.huntifi.castlesiege.events.gameplay.Movement;
import me.huntifi.castlesiege.events.security.InteractContainer;
import me.huntifi.castlesiege.events.security.InventoryProtection;
import me.huntifi.castlesiege.events.security.MapProtection;
import me.huntifi.castlesiege.events.timed.ApplyRegeneration;
import me.huntifi.castlesiege.events.timed.BarCooldown;
import me.huntifi.castlesiege.events.timed.Hunger;
import me.huntifi.castlesiege.events.timed.Tips;
import me.huntifi.castlesiege.kits.gui.KitGui;
import me.huntifi.castlesiege.kits.gui.KitGuiController;
import me.huntifi.castlesiege.kits.gui.coinshop.CoinbuyCommand;
import me.huntifi.castlesiege.kits.gui.coinshop.CoinshopGui;
import me.huntifi.castlesiege.kits.items.Enderchest;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.kits.kits.donator_kits.*;
import me.huntifi.castlesiege.kits.kits.free_kits.Archer;
import me.huntifi.castlesiege.kits.kits.free_kits.Spearman;
import me.huntifi.castlesiege.kits.kits.free_kits.Swordsman;
import me.huntifi.castlesiege.kits.kits.in_development.Warbear;
import me.huntifi.castlesiege.kits.kits.team_kits.*;
import me.huntifi.castlesiege.kits.kits.voter_kits.*;
import me.huntifi.castlesiege.maps.Map;
import me.huntifi.castlesiege.maps.*;
import me.huntifi.castlesiege.maps.helms_deep.CavesBoat;
import me.huntifi.castlesiege.maps.helms_deep.WallEvent;
import me.huntifi.castlesiege.maps.objects.*;
import me.huntifi.castlesiege.secrets.Abrakhan.AbrakhanSecretDoor;
import me.huntifi.castlesiege.secrets.Helmsdeep.SecretDoor;
import me.huntifi.castlesiege.secrets.SecretBlocks;
import me.huntifi.castlesiege.secrets.SecretItems;
import me.huntifi.castlesiege.secrets.SecretSigns;
import me.huntifi.castlesiege.secrets.Skyhold.Doors;
import me.huntifi.castlesiege.secrets.Thunderstone.SecretPortal;
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
    private YamlDocument[] flagsConfigs;
    private YamlDocument[] doorsConfigs;
    private YamlDocument[] gatesConfigs;
    private YamlDocument[] catapultsConfigs;

    private YamlDocument gameConfig;
    private YamlDocument kitsConfig;


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
                getLogger().info("Loading kit GUI configuration...");
                loadKits();

                getLogger().info("Connecting to database...");
                // SQL Stuff
                sqlConnect();

                // World Guard
                SessionManager sessionManager = WorldGuard.getInstance().getPlatform().getSessionManager();
                // second param allows for ordering of handlers - see the JavaDocs
                sessionManager.registerHandler(RegionHandler.FACTORY, null);

                // Tips
                new Tips().runTaskTimer(plugin, Tips.TIME_BETWEEN_TIPS * 20L, Tips.TIME_BETWEEN_TIPS * 20L);

                // Rewrite Events
                getServer().getPluginManager().registerEvents(new Enderchest(), plugin);
                getServer().getPluginManager().registerEvents(new PlayerChat(), plugin);

                // Connection
                getServer().getPluginManager().registerEvents(new PlayerConnect(), plugin);
                getServer().getPluginManager().registerEvents(new PlayerDisconnect(), plugin);

                //Secrets
                getServer().getPluginManager().registerEvents(new SecretDoor(), plugin);
                getServer().getPluginManager().registerEvents(new SecretItems(), plugin);
                getServer().getPluginManager().registerEvents(new SecretSigns(), plugin);
                getServer().getPluginManager().registerEvents(new SecretBlocks(), plugin);
                getServer().getPluginManager().registerEvents(new SecretPortal(), plugin);
                getServer().getPluginManager().registerEvents(new Doors(), plugin);
                getServer().getPluginManager().registerEvents(new AbrakhanSecretDoor(), plugin);


                // Combat
                getServer().getPluginManager().registerEvents(new ArrowRemoval(), plugin);
                getServer().getPluginManager().registerEvents(new AssistKill(), plugin);
                getServer().getPluginManager().registerEvents(new FallDamage(), plugin);
                getServer().getPluginManager().registerEvents(new HitMessage(), plugin);
                getServer().getPluginManager().registerEvents(new HurtAnimation(), plugin);
                getServer().getPluginManager().registerEvents(new InCombat(), plugin);
                getServer().getPluginManager().registerEvents(new LobbyCombat(), plugin);
                getServer().getPluginManager().registerEvents(new TeamCombat(), plugin);
                getServer().getPluginManager().registerEvents(new DamageBalance(), plugin);

                // Death
                getServer().getPluginManager().registerEvents(new DeathEvent(), plugin);
                getServer().getPluginManager().registerEvents(new VoidLocation(), plugin);

                // Gameplay
                //getServer().getPluginManager().registerEvents(new ArcaneTower(), plugin);
                getServer().getPluginManager().registerEvents(new HorseHandler(), plugin);
                getServer().getPluginManager().registerEvents(new Explosion(), plugin);
                getServer().getPluginManager().registerEvents(new Movement(), plugin);

                // Security
                getServer().getPluginManager().registerEvents(new InteractContainer(), plugin);
                getServer().getPluginManager().registerEvents(new InventoryProtection(), plugin);
                getServer().getPluginManager().registerEvents(new MapProtection(), plugin);

                // Kits
                getServer().getPluginManager().registerEvents(new Alchemist(), plugin);
                getServer().getPluginManager().registerEvents(new Berserker(), plugin);
                getServer().getPluginManager().registerEvents(new Crossbowman(), plugin);
                getServer().getPluginManager().registerEvents(new Cavalry(), plugin);
                getServer().getPluginManager().registerEvents(new CoinshopGui(), plugin);
                getServer().getPluginManager().registerEvents(new CryptsFallen(), plugin);
                getServer().getPluginManager().registerEvents(new ConwyArbalester(), plugin);
                getServer().getPluginManager().registerEvents(new Engineer(), plugin);
                getServer().getPluginManager().registerEvents(new ThunderstoneElytrier(), plugin);
                getServer().getPluginManager().registerEvents(new Executioner(), plugin);
                getServer().getPluginManager().registerEvents(new FireArcher(), plugin);
                getServer().getPluginManager().registerEvents(new FirelandsAbyssal(), plugin);
                getServer().getPluginManager().registerEvents(new FirelandsHellsteed(), plugin);
                getServer().getPluginManager().registerEvents(new Halberdier(), plugin);
                getServer().getPluginManager().registerEvents(new HelmsDeepBerserker(), plugin);
                getServer().getPluginManager().registerEvents(new HelmsDeepLancer(), plugin);
                getServer().getPluginManager().registerEvents(new HelmsDeepRangedCavalry(), plugin);
                getServer().getPluginManager().registerEvents(new Ladderman(), plugin);
                getServer().getPluginManager().registerEvents(new Maceman(), plugin);
                getServer().getPluginManager().registerEvents(new Medic(), plugin);
                getServer().getPluginManager().registerEvents(new MoriaWindlancer(), plugin);
                getServer().getPluginManager().registerEvents(new MoriaAxeThrower(), plugin);
                getServer().getPluginManager().registerEvents(new MoriaBonecrusher(), plugin);
                getServer().getPluginManager().registerEvents(new MoriaCaveTroll(), plugin);
                getServer().getPluginManager().registerEvents(new MoriaGuardian(), plugin);
                getServer().getPluginManager().registerEvents(new MoriaOrc(), plugin);
                getServer().getPluginManager().registerEvents(new Ranger(), plugin);
                getServer().getPluginManager().registerEvents(new Spearman(), plugin);
                getServer().getPluginManager().registerEvents(new Vanguard(), plugin);
                getServer().getPluginManager().registerEvents(new Viking(), plugin);
                getServer().getPluginManager().registerEvents(new Warbear(), plugin);
                getServer().getPluginManager().registerEvents(new Warhound(), plugin);

                // Rewrite Commands

                // Chat
                Objects.requireNonNull(getCommand("GlobalChat")).setExecutor(new GlobalChat());
                Objects.requireNonNull(getCommand("Message")).setExecutor(new PrivateMessage());
                Objects.requireNonNull(getCommand("Reply")).setExecutor(new ReplyMessage());
                Objects.requireNonNull(getCommand("TeamChat")).setExecutor(new TeamChat());

                // Donator
                Objects.requireNonNull(getCommand("LeaveMessage")).setExecutor(new LeaveMessage());
                Objects.requireNonNull(getCommand("JoinMessage")).setExecutor(new JoinMessage());

                // Gameplay
                Objects.requireNonNull(getCommand("Suicide")).setExecutor(new SuicideCommand());
                Objects.requireNonNull(getCommand("Switch")).setExecutor(new SwitchCommand());
                Objects.requireNonNull(getCommand("Bounty")).setExecutor(new Bounty());
                Objects.requireNonNull(getCommand("Bounties")).setExecutor(new Bounty());
                Objects.requireNonNull(getCommand("Settings")).setExecutor(new SettingsCommand());

                // Info
                Objects.requireNonNull(getCommand("CoinMultiplier")).setExecutor(new CoinMultiplier());
                Objects.requireNonNull(getCommand("Coins")).setExecutor(new CoinsCommand());
                Objects.requireNonNull(getCommand("Discord")).setExecutor(new DiscordCommand());
                Objects.requireNonNull(getCommand("Maps")).setExecutor(new MapsCommand());
                Objects.requireNonNull(getCommand("MVP")).setExecutor(new MVPCommand());
                Objects.requireNonNull(getCommand("MyStats")).setExecutor(new MyStatsCommand());
                Objects.requireNonNull(getCommand("Ping")).setExecutor(new PingCommand());
                Objects.requireNonNull(getCommand("Rules")).setExecutor(new RulesCommand());
                Objects.requireNonNull(getCommand("Secrets")).setExecutor(new SecretsCommand());
                Objects.requireNonNull(getCommand("Teams")).setExecutor(new TeamsCommand());
                Objects.requireNonNull(getCommand("webshop")).setExecutor(new WebshopCommand());
                Objects.requireNonNull(getCommand("whois")).setExecutor(new WhoisCommand());

                // Leaderboards
                Objects.requireNonNull(getCommand("Top")).setExecutor(new Leaderboard());
                Objects.requireNonNull(getCommand("TopAssists")).setExecutor(new Leaderboard());
                Objects.requireNonNull(getCommand("TopCaptures")).setExecutor(new Leaderboard());
                Objects.requireNonNull(getCommand("TopDeaths")).setExecutor(new Leaderboard());
                Objects.requireNonNull(getCommand("TopHeals")).setExecutor(new Leaderboard());
                Objects.requireNonNull(getCommand("TopKDR")).setExecutor(new Leaderboard());
                Objects.requireNonNull(getCommand("TopKills")).setExecutor(new Leaderboard());
                Objects.requireNonNull(getCommand("TopSupports")).setExecutor(new Leaderboard());
                Objects.requireNonNull(getCommand("TopDonators")).setExecutor(new Donators());
                Objects.requireNonNull(getCommand("TopMatch")).setExecutor(new TopMatch());
                Objects.requireNonNull(getCommand("TopTeam")).setExecutor(new TopMatch());

                // Staff - Coins
                Objects.requireNonNull(getCommand("AddCoins")).setExecutor(new AddCoins());
                Objects.requireNonNull(getCommand("SetCoins")).setExecutor(new SetCoins());
                Objects.requireNonNull(getCommand("SetCoinMultiplier")).setExecutor(new SetCoinMultiplier());
                Objects.requireNonNull(getCommand("TakeCoins")).setExecutor(new TakeCoins());

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
                Objects.requireNonNull(getCommand("SetStaffRank")).setExecutor(new SetStaffRank());
                Objects.requireNonNull(getCommand("GiveVote")).setExecutor(new GiveVoteCommand());
                Objects.requireNonNull(getCommand("NextMap")).setExecutor(new NextMapCommand());
                Objects.requireNonNull(getCommand("RankPoints")).setExecutor(new RankPoints());
                Objects.requireNonNull(getCommand("SetKit")).setExecutor(new SetKitCommand());
                Objects.requireNonNull(getCommand("SetKitLimit")).setExecutor(new SetKitLimit());
                Objects.requireNonNull(getCommand("SetMap")).setExecutor(new SetMapCommand());
                Objects.requireNonNull(getCommand("SetTimer")).setExecutor(new SetTimerCommand());
                Objects.requireNonNull(getCommand("StaffChat")).setExecutor(new StaffChat());
                Objects.requireNonNull(getCommand("Spectate")).setExecutor(new SpectateCommand());
                Objects.requireNonNull(getCommand("ToggleRank")).setExecutor(new ToggleRankCommand());
                Objects.requireNonNull(getCommand("Unlockkit")).setExecutor(new UnlockedKitCommand());
                Objects.requireNonNull(getCommand("ForceSwitch")).setExecutor(new SwitchCommand());
                Objects.requireNonNull(getCommand("ToggleSwitching")).setExecutor(new ToggleSwitching());
                Objects.requireNonNull(getCommand("Start")).setExecutor(new StartCommand());
                Objects.requireNonNull(getCommand("SetTag")).setExecutor(new NameTag());

                // Kits
                Objects.requireNonNull(getCommand("Alchemist")).setExecutor(new Alchemist());
                Objects.requireNonNull(getCommand("Random")).setExecutor(new RandomKitCommand());
                Objects.requireNonNull(getCommand("buykit")).setExecutor(new CoinbuyCommand());
                Objects.requireNonNull(getCommand("coinshop")).setExecutor(new CoinshopGui());
                Objects.requireNonNull(getCommand("Kit")).setExecutor(new KitCommand());
                Objects.requireNonNull(getCommand("Archer")).setExecutor(new Archer());
                Objects.requireNonNull(getCommand("Berserker")).setExecutor(new Berserker());
                Objects.requireNonNull(getCommand("ConwyArbalester")).setExecutor(new ConwyArbalester());
                Objects.requireNonNull(getCommand("ConwyRoyalKnight")).setExecutor(new ConwyRoyalKnight());
                Objects.requireNonNull(getCommand("Cavalry")).setExecutor(new Cavalry());
                Objects.requireNonNull(getCommand("CryptsFallen")).setExecutor(new CryptsFallen());
                Objects.requireNonNull(getCommand("Crossbowman")).setExecutor(new Crossbowman());
                Objects.requireNonNull(getCommand("Engineer")).setExecutor(new Engineer());
                Objects.requireNonNull(getCommand("ThunderstoneElytrier")).setExecutor(new ThunderstoneElytrier());
                Objects.requireNonNull(getCommand("Executioner")).setExecutor(new Executioner());
                Objects.requireNonNull(getCommand("FireArcher")).setExecutor(new FireArcher());
                Objects.requireNonNull(getCommand("FirelandsAbyssal")).setExecutor(new FirelandsAbyssal());
                Objects.requireNonNull(getCommand("FirelandsHellsteed")).setExecutor(new FirelandsHellsteed());
                Objects.requireNonNull(getCommand("Halberdier")).setExecutor(new Halberdier());
                Objects.requireNonNull(getCommand("HelmsDeepBerserker")).setExecutor(new HelmsDeepBerserker());
                Objects.requireNonNull(getCommand("HelmsDeepLancer")).setExecutor(new HelmsDeepLancer());
                Objects.requireNonNull(getCommand("HelmsDeepRangedCavalry")).setExecutor(new HelmsDeepRangedCavalry());
                Objects.requireNonNull(getCommand("Ladderman")).setExecutor(new Ladderman());
                Objects.requireNonNull(getCommand("Maceman")).setExecutor(new Maceman());
                Objects.requireNonNull(getCommand("Medic")).setExecutor(new Medic());
                Objects.requireNonNull(getCommand("MoriaWindlancer")).setExecutor(new MoriaWindlancer());
                Objects.requireNonNull(getCommand("MoriaGuardian")).setExecutor(new MoriaGuardian());
                Objects.requireNonNull(getCommand("MoriaCaveTroll")).setExecutor(new MoriaCaveTroll());
                Objects.requireNonNull(getCommand("MoriaBonecrusher")).setExecutor(new MoriaBonecrusher());
                Objects.requireNonNull(getCommand("MoriaAxeThrower")).setExecutor(new MoriaAxeThrower());
                Objects.requireNonNull(getCommand("Moriaorc")).setExecutor(new MoriaOrc());
                Objects.requireNonNull(getCommand("Ranger")).setExecutor(new Ranger());
                Objects.requireNonNull(getCommand("Scout")).setExecutor(new Scout());
                Objects.requireNonNull(getCommand("Shieldman")).setExecutor(new Shieldman());
                Objects.requireNonNull(getCommand("Skirmisher")).setExecutor(new Skirmisher());
                Objects.requireNonNull(getCommand("Spearman")).setExecutor(new Spearman());
                Objects.requireNonNull(getCommand("Swordsman")).setExecutor(new Swordsman());
                Objects.requireNonNull(getCommand("Vanguard")).setExecutor(new Vanguard());
                Objects.requireNonNull(getCommand("Viking")).setExecutor(new Viking());
                Objects.requireNonNull(getCommand("Warbear")).setExecutor(new Warbear());
                Objects.requireNonNull(getCommand("Warhound")).setExecutor(new Warhound());
                applyKitLimits();

                // Map Specific
                // Helms Deep Only
                getServer().getPluginManager().registerEvents(new WallEvent(), plugin);
                CavesBoat boatEvent = new CavesBoat();
                getServer().getPluginManager().registerEvents(boatEvent, plugin);
                getServer().getScheduler().runTaskTimer(plugin, boatEvent, 300, 300);
                boatEvent.spawnBoat();

                // Timed
                Bukkit.getServer().getScheduler().runTaskTimer(plugin, new BarCooldown(), 0, 1);
                Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Scoreboard(), 0, 20);
                Bukkit.getServer().getScheduler().runTaskTimer(plugin, new ApplyRegeneration(), 0, 75);
                Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Hunger(), 0, 20);
                Bukkit.getServer().getScheduler().runTaskTimer(plugin, new MapBorder(), 0, 80);
                Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(plugin, new KeepAlive(), 0, 5900);

                getLogger().info("Plugin has been enabled!");

                // Begin the map loop
                MapController.startLoop();

                //This registers the secret items
                SecretItems.registerSecretItems();
            }
        }.runTaskLater(plugin, 1);
    }

    /**
     * Called when the plugin is disabled
     */
    @Override
    public void onDisable() {
        getLogger().info("Disabling plugin...");
        // Unregister all listeners
        HandlerList.unregisterAll(plugin);
        // Unload all worlds
        for (World world:Bukkit.getWorlds()) {
            Bukkit.unloadWorld(world, false);
        }
        // Reload the original world - HelmsDeep
        WorldCreator worldCreator = new WorldCreator("HelmsDeep");
        worldCreator.generateStructures(false);
        worldCreator.createWorld();
        // Save data and disconnect the SQL
        StoreData.storeAll();
        try {
            SQL.disconnect();
        } catch (NullPointerException ex) {
            getLogger().warning("SQL could not disconnect, it doesn't exist!");
        }
        getLogger().info("Plugin has been disabled!");
    }

    /**
     * Loads all worlds from their save folder
     */
    private void resetWorlds() {
        //Creating a File object for directory
        File directoryPath = new File(String.valueOf(Bukkit.getWorldContainer()));
        //List of all files and directories
        String[] contents = directoryPath.list();
        assert contents != null;
        for (String content : contents) {
            // If the server is a save
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

    /**
     * Creates/loads a world
     * @param worldName The name of the world to load
     */
    private void createWorld(String worldName) {
        WorldCreator worldCreator = new WorldCreator(worldName);
        worldCreator.generateStructures(false);
        World world = worldCreator.createWorld();
        assert world != null;
        world.setAutoSave(false);
    }

    /**
     * Connects to the SQL database
     */
    private void sqlConnect() {
        try {
            YamlDocument dbConfig = YamlDocument.create(new File(getDataFolder(), "database.yml"),
                    getClass().getResourceAsStream("database.yml"));

            String host = dbConfig.getString("host");
            int port = dbConfig.getInt("port");
            String database = dbConfig.getString("database");
            String username = dbConfig.getString("username");
            String password = dbConfig.getString("password");

            SQL = new MySQL(host, port, database, username, password);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            SQL.connect();
        } catch (ClassNotFoundException | SQLException e) {
            getLogger().warning("<!> Database is not connected! <!>");
        }

        if (SQL.isConnected()) {
            getLogger().info("<!> Database is connected! <!>");
            this.getServer().getPluginManager().registerEvents(this, this);
        }

        // Load bounties
        Bounty.loadBounties();
    }

    public YamlDocument getFlagsConfig(Route flagPath) {
        for (YamlDocument document : flagsConfigs) {
            if (document.contains(flagPath)) {
                return document;
            }
        }
        return null;
    }

    public YamlDocument getCatapultsConfig(Route cataPath) {
        for (YamlDocument document : catapultsConfigs) {
        if (document.contains(cataPath)) {
            return document;
        }
    }
        return null;}

    public YamlDocument getDoorsConfig(Route mapPath) {
        for (YamlDocument document : doorsConfigs) {
            if (document.contains(mapPath)) {
                return document;
            }
        }
        return null;
    }

    public YamlDocument getGatesConfig(Route gatePath) {
        for (YamlDocument document : gatesConfigs) {
            if (document.contains(gatePath)) {
                return document;
            }
        }
        return null;
    }

    /**
     * Creates/loads our configs
     */
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

        getLogger().info("Loaded Vector Adapter");

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

        getLogger().info("Loaded Frame Apapter");

        flagsConfigs = loadYMLs("flags");
        getLogger().info("Loaded flags");

        doorsConfigs = loadYMLs("doors");
        getLogger().info("Loaded doors");

        gatesConfigs = loadYMLs("gates");
        getLogger().info("Loaded gates");

        catapultsConfigs = loadYMLs("catapults");
        getLogger().info("Loaded catapults");

        mapConfigs = loadYMLs("maps");
        getLogger().info("Loaded maps");

        // Load config.yml with BoostedYAML
        try {
            gameConfig = YamlDocument.create(new File(getDataFolder(), "config.yml"),
                    getClass().getResourceAsStream("config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        getLogger().info("Loaded config.yml");

        // Load kits.yml with BoostedYAML
        try {
            kitsConfig = YamlDocument.create(new File(getDataFolder(), "kits.yml"),
                    getClass().getResourceAsStream("kits.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        getLogger().info("Loaded kits.yml");
    }

    private YamlDocument[] loadYMLs(String folderName) {
        // Add all config ymls from the doors folder
        File directoryPath = new File(String.valueOf(getDataFolder()), folderName);
        System.out.println("Directory path");
        // List of all files and directories
        String[] contents = directoryPath.list();
        System.out.println("contents");
        assert contents != null;
        List<YamlDocument> configs = new ArrayList<>();
        for (String content : contents) {
            if (content.endsWith(".yml")) {
                System.out.println("New YML: " + content);
                System.out.println(directoryPath.getPath());
                // Load the yml with BoostedYAML
                try {
                    configs.add(YamlDocument.create(new File(directoryPath.getPath(), content),
                            getClass().getResourceAsStream(content)));
                    System.out.println("Added config");
                } catch (IOException e) {
                    System.out.println("IOException!");
                    e.printStackTrace();
                }
            }
        }

        return configs.toArray(new YamlDocument[configs.size()]);
    }

    /**
     * Create the inventories corresponding to the GUIs in kits.yml
     */
    private void loadKits() {
        String[] guiPaths = getPaths(kitsConfig, null);
        for (String guiPath : guiPaths) {
            Route guiRoute = Route.from(guiPath);
            KitGui gui = new KitGui(kitsConfig.getString(guiRoute.add("name")));
            getServer().getPluginManager().registerEvents(gui, plugin);

            String[] itemPaths = getPaths(kitsConfig, guiRoute.add("items"));
            for (String itemPath : itemPaths) {
                Route itemRoute = guiRoute.add("items").add(itemPath);
                String itemName = kitsConfig.getString(itemRoute.add("name"));
                Material material = Material.getMaterial(kitsConfig.getString(itemRoute.add("material")));
                List<String> lore = kitsConfig.getStringList(itemRoute.add("lore"));
                int location = kitsConfig.getInt(itemRoute.add("location"));
                String command = kitsConfig.getString(itemRoute.add("command"));
                gui.addItem(itemName, material, lore, location, command);
            }

            KitGuiController.add(kitsConfig.getString(guiRoute.add("key")), gui);
        }
    }

    /**
     * Sets values in MapController from config.yml
     * Anything missing is set to the defaults supplied in MapController
     */
    private void loadConfig() {
        // Map Count
        MapController.mapCount = gameConfig.getInt(Route.from("map_count"), MapController.mapCount);

        // Coin multiplier
        PlayerData.setCoinMultiplier(gameConfig.getDouble(Route.from("coin_multiplier"), 1.0));

        // Delays
        Route route = Route.from("delays");
        MapController.preGameTime = gameConfig.getInt(route.add("game"), MapController.preGameTime);
        MapController.lobbyLockedTime = gameConfig.getInt(route.add("lobby"), MapController.lobbyLockedTime);
        MapController.explorationTime = gameConfig.getInt(route.add("exploration"), MapController.explorationTime);

        // Match Details
        route = Route.from("match");
        MapController.isMatch = gameConfig.getBoolean(route.add("enabled"), MapController.isMatch);
        if (MapController.isMatch) {
            MapController.keepTeams = gameConfig.getBoolean(route.add("keep_teams"), MapController.keepTeams);
            MapController.disableSwitching = gameConfig.getBoolean(route.add("disable_switching"), MapController.disableSwitching);
            if (gameConfig.contains(route.add("maps"))) {
                MapController.setMaps(gameConfig.getStringList(route.add("maps")));
                if (gameConfig.getBoolean(route.add("shuffle_maps"), false)) {
                    Collections.shuffle(MapController.maps);
                }
            }
        }
    }

    /**
     * Apply the kit limits per team as set in config.yml
     * Not included in loadConfig() because the kits must be registered first
     */
    private void applyKitLimits() {
        Route route = Route.from("kit_limits");
        if (gameConfig.getBoolean(route.add("enabled"))) {
            for (String path : getPaths(gameConfig, route)) {
                if (path.equals("enabled"))
                    continue;

                Kit kit = Kit.getKit(path);
                if (kit == null)
                    System.out.println("Could not find the kit: " + path);
                else
                    kit.setLimit(gameConfig.getInt(route.add(path)));
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
                map.startTime = config.getInt(mapRoute.add("start_time"), 0);
                map.daylightCycle = config.getBoolean(mapRoute.add("doDaylightCycle"), true);
                //map.northZ = config.getDouble(mapRoute.add("northborder"), 0.0);
                //map.southZ = config.getDouble(mapRoute.add("southborder"), 0.0);
                //map.westX = config.getDouble(mapRoute.add("westborder"), 0.0);
                //map.eastX = config.getDouble(mapRoute.add("eastborder"), 0.0);

                // World Data
                createWorld(map.worldName);

                // Flag Data
                loadFlags(mapRoute, map);
                // Doors
                loadDoors(mapRoute, map);
                // Gates
                loadGates(mapRoute, map);
                // Catapults
                loadCatapults(mapRoute, map);

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
        YamlDocument flagConfig = getFlagsConfig(mapRoute);
        String[] flagPaths = getPaths(flagConfig, mapRoute);

        map.flags = new Flag[flagPaths.length];
        for (int i = 0; i < flagPaths.length; i++) {
            // Create the flag
            Route flagRoute = mapRoute.add(flagPaths[i]);
            String name = flagConfig.getString(flagRoute.add("name"));
            Flag flag;
            if (flagConfig.contains(flagRoute.add("start_amount"))) {
                flag = new Flag(name,
                        flagConfig.getString(flagRoute.add("start_owners")),
                        flagConfig.getInt(flagRoute.add("max_cap")),
                        flagConfig.getInt(flagRoute.add("progress_amount")),
                        flagConfig.getInt(flagRoute.add("start_amount")));
            } else {
                flag = new Flag(name,
                        flagConfig.getString(flagRoute.add("start_owners")),
                        flagConfig.getInt(flagRoute.add("max_cap")),
                        flagConfig.getInt(flagRoute.add("progress_amount")));
            }

            // Set the spawn point
            flag.spawnPoint = getLocation(flagRoute.add("spawn_point"), map.worldName, flagConfig);

            //Hologram Location
            flag.holoLoc = getLocation(flagRoute.add("hologram_location"), map.worldName, flagConfig);

            // Set the capture area and animations
            Route captureRoute = flagRoute.add("capture_area");
            if (flagConfig.contains(captureRoute)
                    && flagConfig.getString(captureRoute.add("type")).equalsIgnoreCase("cuboid")) {
                flag.region = getRegion(flagConfig, captureRoute, flag.name.replace(' ', '_'));

                flag.animationAir = flagConfig.getBoolean(flagRoute.add("animation_air"));
                Route animationRoute = flagRoute.add("animation");
                String[] animationPaths = getPaths(flagConfig, animationRoute);

                flag.animation = new Frame[animationPaths.length];
                for (int j = 0; j < animationPaths.length; j++) {
                    Frame frame = flagConfig.getAs(animationRoute.add(animationPaths[j]), Frame.class);
                    flag.animation[j] = frame;
                }
            }

            flag.scoreboard = flagConfig.getInt(flagRoute.add("scoreboard"));

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
                    break;
                case "up":
                    block.signLocation.add(0, 1, 0);
                    break;
            }
            woolMap.woolMapBlocks[i] = block;
        }

        return woolMap;
    }

    private void loadDoors(Route mapRoute, Map map) {
        YamlDocument doorConfig = getDoorsConfig(mapRoute);
        if (doorConfig == null) {
            map.doors = new Door[0];
            return;
        }
        String[] doorPaths = getPaths(doorConfig, mapRoute);

        map.doors = new Door[doorPaths.length];
        for (int i = 0; i < doorPaths.length; i++) {
            // Create the flag
            Route doorRoute = mapRoute.add(doorPaths[i]);
            String flagName = doorConfig.getString(doorRoute.add("flag"), map.name);
            // Get the location at the centre of the object
            Location centre = getLocation(doorRoute.add("schematic_location"), map.worldName, doorConfig);
            // Fancy shit that makes the Tuple array of locations and materials
            Route openSchematicRoute = doorRoute.add("open_schematic");
            Route closeSchematicRoute = doorRoute.add("closed_schematic");
            Tuple<String, String> schematicNames = new Tuple<>(doorConfig.getString(closeSchematicRoute, null),
                    doorConfig.getString(openSchematicRoute, null));
            // Split up the variations for lever and pressure plate doors
            Tuple<Sound, Sound> sounds;
            Door door;
            switch (doorConfig.getString(doorRoute.add("trigger"), "plate").toLowerCase()) {
                case "switch":
                case "lever":
                    sounds = new Tuple<>(Sound.valueOf(doorConfig.getString(doorRoute.add("closed_sound"), "ENTITY_ZOMBIE_ATTACK_IRON_DOOR")),
                            Sound.valueOf(doorConfig.getString(doorRoute.add("open_sound"), "ENTITY_ZOMBIE_ATTACK_IRON_DOOR")));
                    Location leverPos = getLocation(doorRoute.add("lever_position"), map.worldName, doorConfig);
                    int timer = (int) (doorConfig.getFloat(doorRoute.add("timer"), 10f) * 20);
                    door = new LeverDoor(flagName, centre, schematicNames, sounds, timer, leverPos);
                    break;
                case "pressureplate":
                case "pressure plate":
                case "plate":
                case "pressure":
                default:
                    sounds = new Tuple<>(Sound.valueOf(doorConfig.getString(doorRoute.add("closed_sound"), "BLOCK_WOODEN_DOOR_OPEN")),
                            Sound.valueOf(doorConfig.getString(doorRoute.add("open_sound"), "BLOCK_WOODEN_DOOR_OPEN")));
                    timer = (int) (doorConfig.getFloat(doorRoute.add("timer"), 2f) * 20);
                    door = new PressurePlateDoor(flagName, centre, schematicNames, sounds, timer);
                    break;
            }
            map.doors[i] = door;
        }
    }



    private void loadGates(Route mapRoute, Map map) {
        YamlDocument gateConfig = getGatesConfig(mapRoute);
        if (!Objects.nonNull(gateConfig)) {
            map.gates = new Gate[0];
            return;
        }
        String[] gatePaths = getPaths(gateConfig, mapRoute);

        map.gates = new Gate[gatePaths.length];
        for (int i = 0; i < gatePaths.length; i++) {
            // Create the gate
            Route gateRoute = mapRoute.add(gatePaths[i]);
            Gate gate = new Gate(gateConfig.getString(gateRoute.add("display_name"), ""));
            gate.setFlagName(gateConfig.getString(gateRoute.add("flag_name"), ""), map.name);
            gate.setHealth(gateConfig.getInt(gateRoute.add("start_health")));

            gate.setHitBox(gateConfig.getAs(gateRoute.add("min"), Vector.class),
                    gateConfig.getAs(gateRoute.add("max"), Vector.class));
            gate.setSchematic(gateConfig.getString(gateRoute.add("schematic").add("name")),
                    gateConfig.getAs(gateRoute.add("schematic").add("location"), Vector.class));
            gate.setBreachSoundLocation(gateConfig.getAs(gateRoute.add("breach_sound"), Vector.class));

            // Create corresponding ram
            Route ramRoute = gateRoute.add("ram");
            if (gateConfig.contains(ramRoute)) {
                // Ram region
                Route regionRoute = ramRoute.add("region");
                ProtectedRegion region = getRegion(gateConfig, regionRoute, gateConfig.getString(regionRoute.add("id")));

                // Ram damage and progress
                int damage = gateConfig.getInt(ramRoute.add("damage"));
                int progressAmount = gateConfig.getInt(ramRoute.add("progress_amount"));

                // Ram schematics
                Route schematicRoute = ramRoute.add("schematic");
                Vector location = gateConfig.getAs(schematicRoute.add("location"), Vector.class);
                String schematicNameIdle = gateConfig.getString(schematicRoute.add("name_idle"));
                String schematicNameActiveRest = gateConfig.getString(schematicRoute.add("name_active_rest"));
                String schematicNameActiveHit = gateConfig.getString(schematicRoute.add("name_active_hit"));

                // Currently only Main Gate on Helm's Deep; used to test with hardcoded ram
                Ram ram = new Ram(gate, region, damage, progressAmount, location,
                        schematicNameIdle, schematicNameActiveRest, schematicNameActiveHit);
                gate.setRam(ram);
            }

            map.gates[i] = gate;
        }
    }

    private void loadCatapults(Route mapRoute, Map map) {
        YamlDocument cataConfig = getCatapultsConfig(mapRoute);
        if (!Objects.nonNull(cataConfig)) {
            map.catapults = new Catapult[0];
            return;
        }

        String[] catapultPaths = getPaths(cataConfig, mapRoute);

        map.catapults = new Catapult[catapultPaths.length];

        for (int i = 0; i < catapultPaths.length; i++) {

            Route catapultRoute = mapRoute.add(catapultPaths[i]);
            String world = cataConfig.getString(catapultRoute.add("world"));
            String direction = cataConfig.getString(catapultRoute.add("direction"));
            Vector location = cataConfig.getAs(catapultRoute.add("location"), Vector.class);

            Catapult catapult = new Catapult(world, direction, location);
            map.catapults[i] = catapult;
        }

    }

    private ProtectedRegion getRegion(YamlDocument config, Route route, String id) {
        Route minRoute = route.add("min");
        BlockVector3 min = BlockVector3.at(
                config.getInt(minRoute.add("x")),
                config.getInt(minRoute.add("y")),
                config.getInt(minRoute.add("z")));

        Route maxRoute = route.add("max");
        BlockVector3 max = BlockVector3.at(
                config.getInt(maxRoute.add("x")),
                config.getInt(maxRoute.add("y")),
                config.getInt(maxRoute.add("z")));

        ProtectedRegion region = new ProtectedCuboidRegion(id, true, min, max);
        setFlags(region);

        return region;
    }

    private void setFlags(ProtectedRegion region) {
        region.setFlag(Flags.BLOCK_BREAK, StateFlag.State.ALLOW);
        region.setFlag(Flags.BLOCK_PLACE, StateFlag.State.ALLOW);
        region.setFlag(Flags.DAMAGE_ANIMALS, StateFlag.State.ALLOW);
        region.setFlag(Flags.DESTROY_VEHICLE, StateFlag.State.ALLOW);
        region.setFlag(Flags.FALL_DAMAGE, StateFlag.State.ALLOW);
        region.setFlag(Flags.HEALTH_REGEN, StateFlag.State.ALLOW);
        region.setFlag(Flags.INTERACT, StateFlag.State.ALLOW);
        region.setFlag(Flags.PVP, StateFlag.State.ALLOW);
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
