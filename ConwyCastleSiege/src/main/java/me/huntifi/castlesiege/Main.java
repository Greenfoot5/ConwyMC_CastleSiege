package me.huntifi.castlesiege;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.session.SessionManager;
import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.route.Route;
import dev.dejvokep.boostedyaml.serialization.standard.StandardSerializer;
import dev.dejvokep.boostedyaml.serialization.standard.TypeAdapter;
import me.huntifi.castlesiege.Database.DatabaseKeepAliveEvent;
import me.huntifi.castlesiege.Database.MySQL;
import me.huntifi.castlesiege.Database.SQLGetter;
import me.huntifi.castlesiege.Database.SQLStats;
import me.huntifi.castlesiege.Deathmessages.DeathmessageDisable;
import me.huntifi.castlesiege.Helmsdeep.Wall.WallEvent;
import me.huntifi.castlesiege.chat.PlayerChat;
import me.huntifi.castlesiege.combat.*;
import me.huntifi.castlesiege.commands.*;
import me.huntifi.castlesiege.commands.message.MessageCommand;
import me.huntifi.castlesiege.commands.message.ReplyCommand;
import me.huntifi.castlesiege.commands.staffCommands.*;
import me.huntifi.castlesiege.data_types.Frame;
import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.events.DeathEvent;
import me.huntifi.castlesiege.flags.CaptureHandler;
import me.huntifi.castlesiege.flags.Flag;
import me.huntifi.castlesiege.joinevents.login;
import me.huntifi.castlesiege.joinevents.newLogin;
import me.huntifi.castlesiege.joinevents.stats.StatsLoading;
import me.huntifi.castlesiege.joinevents.stats.StatsSaving;
import me.huntifi.castlesiege.kits.Enderchest;
import me.huntifi.castlesiege.kits.kits.Archer;
import me.huntifi.castlesiege.kits.kits.Spearman;
import me.huntifi.castlesiege.kits.kits.Swordsman;
import me.huntifi.castlesiege.ladders.LadderEvent;
import me.huntifi.castlesiege.maps.Map;
import me.huntifi.castlesiege.maps.*;
import me.huntifi.castlesiege.security.*;
import me.huntifi.castlesiege.stats.MVP.MVPstats;
import me.huntifi.castlesiege.stats.MVP.StatsMvpJoinevent;
import me.huntifi.castlesiege.stats.mystats.MystatsCommand;
import me.huntifi.castlesiege.tablist.Tablist;
import me.huntifi.castlesiege.teams.SwitchCommand;
import me.huntifi.castlesiege.voting.GiveVoteCommand;
import me.huntifi.castlesiege.voting.VoteListenerCommand;
import me.huntifi.castlesiege.voting.VotesLoading;
import me.huntifi.castlesiege.voting.VotesUnloading;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;


public class Main extends JavaPlugin implements Listener {

	public static Plugin plugin;

	public Tablist tab;
	public static MySQL SQL;

	private YamlConfiguration mapsConfig;
	private YamlDocument flagsConfig;

	@Override
	public void onEnable() {

		plugin = Bukkit.getServer().getPluginManager().getPlugin("ConwyCastleSiege");

		createWorld();
		createConfigs();

		getServer().getConsoleSender().sendMessage(ChatColor.DARK_GREEN + "[TheDarkAge] Plugin has been enabled!");


		// SQL Stuff
		SQL = new MySQL();

		try {
			SQL.connect();
		} catch (ClassNotFoundException | SQLException e) {
			Bukkit.getLogger().info("<!> Database is not connected! <!>");
			getServer().getConsoleSender().sendMessage(ChatColor.DARK_GREEN + "[TheDarkAge] <!> Database is not connected! <!>");
		}

		if (SQL.isConnected()) {
			Bukkit.getLogger().info("<!> Database is connected! <!>");
			getServer().getConsoleSender().sendMessage(ChatColor.DARK_GREEN + "[TheDarkAge] <!> Database is connected! <!>");
			SQLStats.createTable();
			this.getServer().getPluginManager().registerEvents(this, this);
		}

		// World Guard
		SessionManager sessionManager = WorldGuard.getInstance().getPlatform().getSessionManager();
		// second param allows for ordering of handlers - see the JavaDocs
		sessionManager.registerHandler(CaptureHandler.FACTORY, null);

		// Rewrite Events
		getServer().getPluginManager().registerEvents(new DeathEvent(), this);
		// Kits
		getServer().getPluginManager().registerEvents(new Spearman(), this);

		// Rewrite Commands
		Objects.requireNonNull(getCommand("Switch")).setExecutor(new SwitchCommand());
		// Kits
		Objects.requireNonNull(getCommand("Archer")).setExecutor(new Archer());
		Objects.requireNonNull(getCommand("Swordsman")).setExecutor(new Swordsman());
		Objects.requireNonNull(getCommand("Spearman")).setExecutor(new Spearman());

		// OLD EVENTS
		//getServer().getPluginManager().registerEvents(new Warhound(), this);
		getServer().getPluginManager().registerEvents(new SessionMuteCommand(), this);
		//getServer().getPluginManager().registerEvents(new HelmsdeepSecretDoor(), this);
		getServer().getPluginManager().registerEvents(new EatCake(), this);
		//getServer().getPluginManager().registerEvents(new RegisterLevel(), this);
		getServer().getPluginManager().registerEvents(new NoMoveInventory(), this);
		getServer().getPluginManager().registerEvents(new NoTouchArmorstand(), this);
		getServer().getPluginManager().registerEvents(new NoTouchPaintings(), this);
		//getServer().getPluginManager().registerEvents(new KitsGUI_ThunderstoneGuard_Command(), this);
		//getServer().getPluginManager().registerEvents(new KitsGUI_Cloudcrawlers_Command(), this);
		//getServer().getPluginManager().registerEvents(new ScoutDeath(), this);
		//getServer().getPluginManager().registerEvents(new LaddermanDeath(), this);
		//getServer().getPluginManager().registerEvents(new LaddermanAbility(), this);
		//getServer().getPluginManager().registerEvents(new HelmsdeepCaveBoat(), this);
		getServer().getPluginManager().registerEvents(new arrowRemoval(), this);
		getServer().getPluginManager().registerEvents(new HitMessage(), this);
		//getServer().getPluginManager().registerEvents(new EngineerCobweb(), this);
		//getServer().getPluginManager().registerEvents(new DeathEngineer(), this);
		getServer().getPluginManager().registerEvents(new StatsMvpJoinevent(), this);
		getServer().getPluginManager().registerEvents(new StatsSaving(), this);
		getServer().getPluginManager().registerEvents(new StatsLoading(), this);

		//getServer().getPluginManager().registerEvents(new RangerAbility(), this);
		//getServer().getPluginManager().registerEvents(new DeathRanger(), this);
		//getServer().getPluginManager().registerEvents(new HalberdierAbility(), this);
		//getServer().getPluginManager().registerEvents(new DeathHalberdier(), this);
		//getServer().getPluginManager().registerEvents(new CavalryAbility(), this);
		//getServer().getPluginManager().registerEvents(new CavalryDeath(), this);
		//getServer().getPluginManager().registerEvents(new CrossbowmanDeath(), this);
		//getServer().getPluginManager().registerEvents(new CrossbowmanAbility(), this);
		//getServer().getPluginManager().registerEvents(new VikingAbility(), this);
		//getServer().getPluginManager().registerEvents(new VikingDeath(), this);
		//getServer().getPluginManager().registerEvents(new MacemanAbility(), this);
		//getServer().getPluginManager().registerEvents(new ExecutionerAbility(), this);
		//getServer().getPluginManager().registerEvents(new ExecutionerDeath(), this);
		//getServer().getPluginManager().registerEvents(new BerserkerAbility(), this);
		//getServer().getPluginManager().registerEvents(new BerserkerDeath(), this);
		//getServer().getPluginManager().registerEvents(new MacemanDeath(), this);
		//getServer().getPluginManager().registerEvents(new VotedKitsGUI_Command(), this);
		//getServer().getPluginManager().registerEvents(new ClassicGui_Command(), this);
		//getServer().getPluginManager().registerEvents(new KitGUI_Rohan_Command(), this);
		//getServer().getPluginManager().registerEvents(new KitGUI_Isengard_Command(), this);
		getServer().getPluginManager().registerEvents(new newLogin(), this);
		getServer().getPluginManager().registerEvents(new login(), this);
		getServer().getPluginManager().registerEvents(new CustomFallDamage(), this);
		getServer().getPluginManager().registerEvents(new preventBlockOpening(), this);
		//getServer().getPluginManager().registerEvents(new Herugrim(), this);
		getServer().getPluginManager().registerEvents(new destroyBlocks(), this);
		getServer().getPluginManager().registerEvents(new placeBlocks(), this);
		getServer().getPluginManager().registerEvents(new wheat(), this);
		getServer().getPluginManager().registerEvents(new NoPaintingDestroy(), this);
		getServer().getPluginManager().registerEvents(new NoFireDestroy(), this);
		getServer().getPluginManager().registerEvents(new NoCombat(), this);

		getServer().getPluginManager().registerEvents(new ambientDamage(), this);
		getServer().getPluginManager().registerEvents(new voidOfLimits(), this);
		getServer().getPluginManager().registerEvents(new PlayerChat(), this);
		getServer().getPluginManager().registerEvents(new DropItemSecurity(), this);
		getServer().getPluginManager().registerEvents(new Enderchest(), this);
		getServer().getPluginManager().registerEvents(new NoHurtTeam(), this);

		//getServer().getPluginManager().registerEvents(new DeathArcher(), this);
		//getServer().getPluginManager().registerEvents(new SkirmisherDeath(), this);
		//getServer().getPluginManager().registerEvents(new DeathSwordsman(), this);
		//getServer().getPluginManager().registerEvents(new DeathShieldman(), this);
		//getServer().getPluginManager().registerEvents(new DeathFirearcher(), this);
		//getServer().getPluginManager().registerEvents(new DeathSpearman(), this);
		getServer().getPluginManager().registerEvents(new DeathmessageDisable(), this);
		getServer().getPluginManager().registerEvents(new CustomRegeneration(), this);

		//getServer().getPluginManager().registerEvents(new HelmsdeepJoin(), this);
		//getServer().getPluginManager().registerEvents(new HelmsdeepLeave(), this);
		//getServer().getPluginManager().registerEvents(new HelmsdeepDeath(), this);
		//getServer().getPluginManager().registerEvents(new FlagRadius(), this);
		//getServer().getPluginManager().registerEvents(new SupplyCampFlag(), this);
		//getServer().getPluginManager().registerEvents(new CavesFlag(), this);
		//getServer().getPluginManager().registerEvents(new MainGateFlag(), this);
		//getServer().getPluginManager().registerEvents(new CourtyardFlag(), this);
		//getServer().getPluginManager().registerEvents(new GreatHallsFlag(), this);
		//getServer().getPluginManager().registerEvents(new HornFlag(), this);
		//getServer().getPluginManager().registerEvents(new WoolMap(), this);

		//getServer().getPluginManager().registerEvents(new HelmsdeepCavesDoor(), this);
		//getServer().getPluginManager().registerEvents(new HelmsdeepGreatHallLeftDoor(), this);
		//getServer().getPluginManager().registerEvents(new HelmsdeepGreatHallRightDoor(), this);
		//getServer().getPluginManager().registerEvents(new HelmsdeepMainGateLeftDoor(), this);
		//getServer().getPluginManager().registerEvents(new HelmsdeepMainGateRightDoor(), this);
		//getServer().getPluginManager().registerEvents(new HelmsdeepStorageDoor(), this);
		//getServer().getPluginManager().registerEvents(new GreatHallExtraDoor(), this);
		getServer().getPluginManager().registerEvents(new WallEvent(), this);
		getServer().getPluginManager().registerEvents(new LadderEvent(), this);
		getServer().getPluginManager().registerEvents(new armorTakeOff(), this);
		//getServer().getPluginManager().registerEvents(new HelmsdeepMainGateDestroyEvent(), this);
		//getServer().getPluginManager().registerEvents(new HelmsdeepGreatHallDestroyEvent(), this);
		getServer().getPluginManager().registerEvents(new MVPstats(), this);
		//getServer().getPluginManager().registerEvents(new SpearmanAbility(), this);
		//getServer().getPluginManager().registerEvents(new KitGUIcommand(), this);
		getServer().getPluginManager().registerEvents(new MessageCommand(), this);
		//getServer().getPluginManager().registerEvents(new HelmsdeepBallistaEvent(), this);

		//getServer().getPluginManager().registerEvents(new TwinbridgeFlag(), this);
		//getServer().getPluginManager().registerEvents(new SkyviewTowerFlag(), this);
		//getServer().getPluginManager().registerEvents(new LonelyTowerFlag(), this);
		//getServer().getPluginManager().registerEvents(new WestTowerFlag(), this);
		//getServer().getPluginManager().registerEvents(new StairhallFlag(), this);
		//getServer().getPluginManager().registerEvents(new ShiftedTowerFlag(), this);
		//getServer().getPluginManager().registerEvents(new TS_FlagRadius(), this);
		//getServer().getPluginManager().registerEvents(new ThunderstoneDeath(), this);
		//getServer().getPluginManager().registerEvents(new ThunderstoneJoin(), this);
		//getServer().getPluginManager().registerEvents(new HelmsdeepEndMVP(), this);
		//getServer().getPluginManager().registerEvents(new ThunderstoneEndMVP(), this);
		//getServer().getPluginManager().registerEvents(new TS_Woolmap(), this);
		//getServer().getPluginManager().registerEvents(new TS_Woolmap_Distance(), this);
		//getServer().getPluginManager().registerEvents(new ThunderstoneLeave(), this);
		//getServer().getPluginManager().registerEvents(new ThunderstoneGateDestroyEvent(), this);
		//getServer().getPluginManager().registerEvents(new EastTowerFlag(), this);
		
		getServer().getPluginManager().registerEvents(new VotesLoading(), this);
		getServer().getPluginManager().registerEvents(new VotesUnloading(), this);

		//getServer().getPluginManager().registerEvents(new FireArcherAbility(), this);
		//getServer().getPluginManager().registerEvents(new MedicAbilities(), this);
		
		//getServer().getPluginManager().registerEvents(new WarhoundDeath(), this);
		//getServer().getPluginManager().registerEvents(new MedicDeath(), this);
		//getServer().getPluginManager().registerEvents(new WarhoundAbility(), this);

		//getCommand("KitThunderstoneGuards").setExecutor(new KitsGUI_ThunderstoneGuard_Command());
		//getCommand("KitCloudcrawlers").setExecutor(new KitsGUI_Cloudcrawlers_Command());
		//getCommand("VoterKitGUI").setExecutor(new VotedKitsGUI_Command());
		//getCommand("ClassicGUI").setExecutor(new ClassicGui_Command());
		getCommand("togglerank").setExecutor(new togglerankCommand());
		//getCommand("KitRohan").setExecutor(new KitGUI_Rohan_Command());
		//getCommand("KitIsengard").setExecutor(new KitGUI_Isengard_Command());
		getCommand("ping").setExecutor(new pingCommand());
		getCommand("rules").setExecutor(new rulesCommand());
		getCommand("discord").setExecutor(new discordCommand());
		getCommand("teams").setExecutor(new teamCommand());
		//getCommand("Kit").setExecutor(new KitsCommand());
		//getCommand("Mvp").setExecutor(new mvpCommand());
		//getCommand("KitGUI").setExecutor(new KitGUIcommand());
		getCommand("Mystats").setExecutor(new MystatsCommand());
		//getCommand("NextMap").setExecutor(new NextMapCommand());
		//getCommand("t").setExecutor(new TeamChat());
		getCommand("msg").setExecutor(new MessageCommand());
		getCommand("r").setExecutor(new ReplyCommand());
		//getCommand("maps").setExecutor(new MapsCommand());
		getCommand("sui").setExecutor(new suicideCommand());
		//getCommand("CheckFlagList").setExecutor(new FlagListCommand());
		getCommand("s").setExecutor(new StaffChat());
		getCommand("kick").setExecutor(new KickCommand());
		getCommand("Fly").setExecutor(new FlyCommand());
		getCommand("kickall").setExecutor(new KickallCommand());
		getCommand("sessionmute").setExecutor(new SessionMuteCommand());
		getCommand("Unsessionmute").setExecutor(new UnsessionmuteCommand());
		
		getCommand("givevote").setExecutor(new GiveVoteCommand());
		getCommand("givevoter").setExecutor(new VoteListenerCommand());


		//Bukkit.getServer().getScheduler().runTaskTimer(this, new ThunderstoneEndMap(), 0, 200);
		//Bukkit.getServer().getScheduler().runTaskTimer(this, new HelmsdeepCaveBoat(), 0, 200);
		Bukkit.getServer().getScheduler().runTaskTimer(this, new Scoreboard(), 0, 20);
		//Bukkit.getServer().getScheduler().runTaskTimer(this, new HelmsdeepEndMap(), 0, 20);
		Bukkit.getServer().getScheduler().runTaskTimer(this, new ApplyRegeneration(), 0, 75);
		Bukkit.getServer().getScheduler().runTaskTimer(this, new Hunger(), 0, 20);
		//Bukkit.getServer().getScheduler().runTaskTimer(this, new HelmsdeepMVPupdater(), 0, 20);
		//Bukkit.getServer().getScheduler().runTaskTimer(this, new ThunderstoneMVPupdater(), 0, 20);
		//Bukkit.getServer().getScheduler().runTaskTimer(this, new HalberdierAbility(), 100, 25);
		Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(this, new DatabaseKeepAliveEvent(), 190, 190);
		//Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(this, new Herugrim(), 10, 10);

		//Bukkit.getServer().getScheduler().runTaskTimer(this, new MainGateReadyRam(), 200, 60);
		//Bukkit.getServer().getScheduler().runTaskTimer(this, new MainGateRamAnimation(), 200, MainGateRam.rammingSpeed);
		//Bukkit.getServer().getScheduler().runTaskTimer(this, new MainGateRam(), 200, 40);
		
		//Bukkit.getServer().getScheduler().runTaskTimer(this, new GreatHallGateReadyRam(), 200, 60);
		//Bukkit.getServer().getScheduler().runTaskTimer(this, new GreatHallRamAnimation(), 200, GreatHallGateRam.rammingSpeed);
		//Bukkit.getServer().getScheduler().runTaskTimer(this, new GreatHallGateRam(), 200, 40);

		//Bukkit.getServer().getScheduler().runTaskTimer(this, new ThunderstoneGateReadyRam(), 200, 60);
		//Bukkit.getServer().getScheduler().runTaskTimer(this, new ThunderstoneRamAnimation(), 200, ThunderstoneRam.rammingSpeed);
		//Bukkit.getServer().getScheduler().runTaskTimer(this, new ThunderstoneRam(), 200, 40);

		// Config Files Stuff

		// Cheap Rewrite Stuff
		loadMaps();
		MapController.setMap(MapController.maps[0].name);

		//Tablist

//		this.tab = new Tablist(this);
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
//		HelmsdeepCaveBoat.spawnFirstBoat();

		//resets Thunderstone after restart
		//ThunderstoneReset.onReset();
		//ThunderstoneGateBlocks.gateblocks();
		
		//Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("ConwyCastleSiege");

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

	@Override
	public void onDisable() {
		try {
			SQL.disconnect();
		} catch (NullPointerException ex) {
			getServer().getConsoleSender().sendRawMessage(ChatColor.DARK_RED + "SQL could not disconnect, it doesn't exist!");
		}
		getServer().getConsoleSender().sendMessage(ChatColor.DARK_RED + "[TheDarkAge] Plugin has been disabled!");
	}

	private void createWorld() {
		WorldCreator d = new WorldCreator("HelmsDeep");
		d.generateStructures(false);
		WorldCreator t = new WorldCreator("Thunderstone");
		t.generateStructures(false);

		d.createWorld();
		t.createWorld();
	}


	@EventHandler
	public void onJoin(PlayerJoinEvent e) {

		Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("ConwyCastleSiege");

		Player p = e.getPlayer();

		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {

			if (!SQLStats.exists(p.getUniqueId())) {
				SQLStats.createPlayer(p);
			}
		});
	}

	// File Configuration
	public YamlConfiguration getMapsConfig() {
		return this.mapsConfig;
	}

	public YamlDocument getFlagsConfig() { return this.flagsConfig; }

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
				return map;
			}

			@NotNull
			public Frame deserialize(@NotNull java.util.Map<Object, Object> map) {
				Frame frame = new Frame();
				for (Object v : (ArrayList) map.get("primary_blocks")) {
					frame.primary_blocks.add(vectorAdapter.deserialize((LinkedHashMap<Object, Object>) v));
				}
				for (Object v : (ArrayList) map.get("secondary_blocks")) {
					frame.secondary_blocks.add(vectorAdapter.deserialize((LinkedHashMap<Object, Object>) v));
				}return frame;
			}
		};
		StandardSerializer.getDefault().register(Frame.class, frameAdapter);

		// Load flags.yml with BoostedYAML
		try {
			flagsConfig = YamlDocument.create(new File(getDataFolder(), "flags.yml"),
					getClass().getResourceAsStream("flags.yml"));
//			flagsConfig = YamlDocument.create(mapsFile, Objects.requireNonNull(getResource("flags.yml")),
//					GeneralSettings.builder().setSerializer(SpigotSerializer.getInstance()).build(),
//					LoaderSettings.DEFAULT, DumperSettings.DEFAULT, UpdaterSettings.DEFAULT);
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

	private void loadMaps()
	{
		// Load the maps
		java.util.Map<String, Object> stringObjectMap = this.getMapsConfig().getConfigurationSection("").getValues(false);
		String[] mapPaths = stringObjectMap.keySet().toArray(new String[stringObjectMap.size()]);

		MapController.maps = new Map[mapPaths.length];
		for (int i = 0; i < mapPaths.length; i++) {
			// Basic Map Details
			Map map = new Map();
			map.name = this.getMapsConfig().getString(mapPaths[i] + ".name");
			map.worldName = this.getMapsConfig().getString(mapPaths[i] + ".world");

			// Flag Data
			loadFlags(mapPaths[i], map);

			// Team Data
			java.util.Map<String, Object> stringObjectTeam = this.getMapsConfig().getConfigurationSection(mapPaths[i] + ".teams").getValues(false);
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

		Set<String> flagSet = getFlagsConfig().getSection(mapRoute).getRoutesAsStrings(false);
		String[] flagPaths = new String[flagSet.size()];
		int index = 0;
		for (String str : flagSet)
			flagPaths[index++] = str;

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
			if (getFlagsConfig().getString(captureRoute.add("type")).equalsIgnoreCase("cuboid"))
			{
				BlockVector3 min = BlockVector3.at(getFlagsConfig().getInt(captureRoute.add("min").add("x")),
						getFlagsConfig().getInt(captureRoute.add("min").add("y")),
						getFlagsConfig().getInt(captureRoute.add("min").add("z")));
				BlockVector3 max = BlockVector3.at(getFlagsConfig().getInt(captureRoute.add("max").add("x")),
						getFlagsConfig().getInt(captureRoute.add("max").add("y")),
						getFlagsConfig().getInt(captureRoute.add("max").add("z")));
				ProtectedRegion region = new ProtectedCuboidRegion(flag.name.replace(' ', '_'), min, max);
				WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(Bukkit.getWorld(map.worldName))).addRegion(region);
			}

			flag.animationAir = getFlagsConfig().getBoolean(flagRoute.add("animation_air"));
			Route animationRoute = flagRoute.add("animation");
			Set<String> animationSet = getFlagsConfig().getSection(animationRoute).getRoutesAsStrings(false);
			String[] animationPaths = new String[animationSet.size()];
			index = 0;
			for (String str : animationSet)
				animationPaths[index++] = str;

			flag.animation = new Frame[animationPaths.length];
			for (int j = 0; j < animationPaths.length; j++) {
				Frame frame = getFlagsConfig().getAs(animationRoute.add(animationPaths[j]), Frame.class);
				flag.animation[j] = frame;
			}

			map.flags[i] = flag;
		}
	}

	private Team loadTeam(String teamPath, Map map) {
		String name = this.getMapsConfig().getString(teamPath + ".name");
		Team team = new Team(name);

		// Colours
		Tuple<Material, ChatColor> colors = getColors(this.getMapsConfig().getString(teamPath + ".primary_color").toLowerCase());
		team.primaryWool = colors.getFirst();
		team.primaryChatColor = colors.getSecond();
		colors = getColors(this.getMapsConfig().getString(teamPath + ".secondary_color").toLowerCase());
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
		java.util.Map<String, Object> stringObjectMap = this.getMapsConfig().getConfigurationSection(woolMapPath).getValues(false);
		String[] mapFlags = stringObjectMap.keySet().toArray(new String[stringObjectMap.size()]);
		woolMap.woolMapBlocks = new WoolMapBlock[mapFlags.length];

		// Loop through all the wool blocks
		for (int i = 0; i < mapFlags.length; i++) {
			WoolMapBlock block = new WoolMapBlock();

			block.flagName = this.getMapsConfig().getString(woolMapPath + "." + mapFlags[i] + ".flag_name");
			block.blockLocation = getLocation(woolMapPath + "." + mapFlags[i] + ".wool_position", map.worldName);
			block.signLocation = block.blockLocation;

			// Get the wall sign's direction
			switch(this.getMapsConfig().getString(woolMapPath + "." + mapFlags[i] + ".sign_direction").toLowerCase()) {
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

	private Tuple<Material, ChatColor> getColors(String color)
	{
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
}
