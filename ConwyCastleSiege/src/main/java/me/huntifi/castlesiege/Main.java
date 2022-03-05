package me.huntifi.castlesiege;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.huntifi.castlesiege.Database.DatabaseKeepAliveEvent;
import me.huntifi.castlesiege.Database.MySQL;
import me.huntifi.castlesiege.Database.SQLGetter;
import me.huntifi.castlesiege.Database.SQLstats;
import me.huntifi.castlesiege.Deathmessages.DeathmessageDisable;
import me.huntifi.castlesiege.Helmsdeep.HelmsdeepDeath;
import me.huntifi.castlesiege.Helmsdeep.HelmsdeepEndMVP;
import me.huntifi.castlesiege.Helmsdeep.HelmsdeepEndMap;
import me.huntifi.castlesiege.Helmsdeep.HelmsdeepJoin;
import me.huntifi.castlesiege.Helmsdeep.HelmsdeepLeave;
import me.huntifi.castlesiege.Helmsdeep.HelmsdeepMVPupdater;
import me.huntifi.castlesiege.Helmsdeep.HelmsdeepTimer;
import me.huntifi.castlesiege.maps.Woolmap;
import me.huntifi.castlesiege.Helmsdeep.Woolmap_Distance;
import me.huntifi.castlesiege.Helmsdeep.Ballistae.HelmsdeepBallistaEvent;
import me.huntifi.castlesiege.Helmsdeep.Boat.HelmsdeepCaveBoat;
import me.huntifi.castlesiege.Helmsdeep.Gates.HelmsdeepGreatHallBlocks;
import me.huntifi.castlesiege.Helmsdeep.Gates.HelmsdeepGreatHallDestroyEvent;
import me.huntifi.castlesiege.Helmsdeep.Gates.HelmsdeepMainGateBlocks;
import me.huntifi.castlesiege.Helmsdeep.Gates.HelmsdeepMainGateDestroyEvent;
import me.huntifi.castlesiege.Helmsdeep.Kits.GUI.KitGUI_Isengard_Command;
import me.huntifi.castlesiege.Helmsdeep.Kits.GUI.KitGUI_Rohan_Command;
import me.huntifi.castlesiege.Helmsdeep.Secrets.HelmsdeepSecretDoor;
import me.huntifi.castlesiege.Helmsdeep.Secrets.Herugrim;
import me.huntifi.castlesiege.Helmsdeep.Wall.WallEvent;
import me.huntifi.castlesiege.Helmsdeep.doors.GreatHallExtraDoor;
import me.huntifi.castlesiege.Helmsdeep.doors.HelmsdeepCavesDoor;
import me.huntifi.castlesiege.Helmsdeep.doors.HelmsdeepGreatHallLeftDoor;
import me.huntifi.castlesiege.Helmsdeep.doors.HelmsdeepGreatHallRightDoor;
import me.huntifi.castlesiege.Helmsdeep.doors.HelmsdeepMainGateLeftDoor;
import me.huntifi.castlesiege.Helmsdeep.doors.HelmsdeepMainGateRightDoor;
import me.huntifi.castlesiege.Helmsdeep.doors.HelmsdeepStorageDoor;
import me.huntifi.castlesiege.Helmsdeep.flags.FlagListCommand;
import me.huntifi.castlesiege.Helmsdeep.flags.FlagRadius;
import me.huntifi.castlesiege.Helmsdeep.flags.HelmsdeepReset;
import me.huntifi.castlesiege.Helmsdeep.flags.animations.CavesFlag;
import me.huntifi.castlesiege.Helmsdeep.flags.animations.CourtyardFlag;
import me.huntifi.castlesiege.Helmsdeep.flags.animations.GreatHallsFlag;
import me.huntifi.castlesiege.Helmsdeep.flags.animations.HornFlag;
import me.huntifi.castlesiege.Helmsdeep.flags.animations.MainGateFlag;
import me.huntifi.castlesiege.Helmsdeep.flags.animations.SupplyCampFlag;
import me.huntifi.castlesiege.Helmsdeep.rams.GreatHallGateRam;
import me.huntifi.castlesiege.Helmsdeep.rams.GreatHallGateReadyRam;
import me.huntifi.castlesiege.Helmsdeep.rams.GreatHallRamAnimation;
import me.huntifi.castlesiege.Helmsdeep.rams.MainGateRam;
import me.huntifi.castlesiege.Helmsdeep.rams.MainGateRamAnimation;
import me.huntifi.castlesiege.Helmsdeep.rams.MainGateReadyRam;
import me.huntifi.castlesiege.Thunderstone.TS_Woolmap;
import me.huntifi.castlesiege.Thunderstone.TS_Woolmap_Distance;
import me.huntifi.castlesiege.Thunderstone.ThunderstoneDeath;
import me.huntifi.castlesiege.Thunderstone.ThunderstoneEndMVP;
import me.huntifi.castlesiege.Thunderstone.ThunderstoneEndMap;
import me.huntifi.castlesiege.Thunderstone.ThunderstoneJoin;
import me.huntifi.castlesiege.Thunderstone.ThunderstoneLeave;
import me.huntifi.castlesiege.Thunderstone.ThunderstoneMVPupdater;
import me.huntifi.castlesiege.Thunderstone.Flags.TS_FlagRadius;
import me.huntifi.castlesiege.Thunderstone.Flags.ThunderstoneReset;
import me.huntifi.castlesiege.Thunderstone.Flags.animations.EastTowerFlag;
import me.huntifi.castlesiege.Thunderstone.Flags.animations.LonelyTowerFlag;
import me.huntifi.castlesiege.Thunderstone.Flags.animations.ShiftedTowerFlag;
import me.huntifi.castlesiege.Thunderstone.Flags.animations.SkyviewTowerFlag;
import me.huntifi.castlesiege.Thunderstone.Flags.animations.StairhallFlag;
import me.huntifi.castlesiege.Thunderstone.Flags.animations.TwinbridgeFlag;
import me.huntifi.castlesiege.Thunderstone.Flags.animations.WestTowerFlag;
import me.huntifi.castlesiege.Thunderstone.Gate.ThunderstoneGateBlocks;
import me.huntifi.castlesiege.Thunderstone.Gate.ThunderstoneGateDestroyEvent;
import me.huntifi.castlesiege.Thunderstone.KitsGUI.KitsGUI_Cloudcrawlers_Command;
import me.huntifi.castlesiege.Thunderstone.KitsGUI.KitsGUI_ThunderstoneGuard_Command;
import me.huntifi.castlesiege.Thunderstone.Rams.ThunderstoneGateReadyRam;
import me.huntifi.castlesiege.Thunderstone.Rams.ThunderstoneRam;
import me.huntifi.castlesiege.Thunderstone.Rams.ThunderstoneRamAnimation;
import me.huntifi.castlesiege.chat.PlayerChat;
import me.huntifi.castlesiege.combat.ApplyRegeneration;
import me.huntifi.castlesiege.combat.CustomRegeneration;
import me.huntifi.castlesiege.combat.EatCake;
import me.huntifi.castlesiege.combat.HitMessage;
import me.huntifi.castlesiege.combat.NoHurtTeam;
import me.huntifi.castlesiege.combat.arrowRemoval;
import me.huntifi.castlesiege.joinevents.login;
import me.huntifi.castlesiege.joinevents.newLogin;
import me.huntifi.castlesiege.joinevents.stats.StatsLoading;
import me.huntifi.castlesiege.joinevents.stats.StatsSaving;
import me.huntifi.castlesiege.kits.Enderchest;
import me.huntifi.castlesiege.kits.KitGUIcommand;
import me.huntifi.castlesiege.kits.KitsCommand;
import me.huntifi.castlesiege.kits.Archer.DeathArcher;
import me.huntifi.castlesiege.kits.Berserker.BerserkerAbility;
import me.huntifi.castlesiege.kits.Berserker.BerserkerDeath;
import me.huntifi.castlesiege.kits.Cavalry.CavalryAbility;
import me.huntifi.castlesiege.kits.Cavalry.CavalryDeath;
import me.huntifi.castlesiege.kits.Classic.ClassicGui_Command;
import me.huntifi.castlesiege.kits.Crossbowman.CrossbowmanAbility;
import me.huntifi.castlesiege.kits.Crossbowman.CrossbowmanDeath;
import me.huntifi.castlesiege.kits.Engineer.DeathEngineer;
import me.huntifi.castlesiege.kits.Engineer.EngineerCobweb;
import me.huntifi.castlesiege.kits.Executioner.ExecutionerAbility;
import me.huntifi.castlesiege.kits.Executioner.ExecutionerDeath;
import me.huntifi.castlesiege.kits.FireArcher.DeathFirearcher;
import me.huntifi.castlesiege.kits.FireArcher.FireArcherAbility;
import me.huntifi.castlesiege.kits.Halberdier.DeathHalberdier;
import me.huntifi.castlesiege.kits.Halberdier.HalberdierAbility;
import me.huntifi.castlesiege.kits.Maceman.MacemanAbility;
import me.huntifi.castlesiege.kits.Maceman.MacemanDeath;
import me.huntifi.castlesiege.kits.Ranger.DeathRanger;
import me.huntifi.castlesiege.kits.Ranger.RangerAbility;
import me.huntifi.castlesiege.kits.Shieldman.DeathShieldman;
import me.huntifi.castlesiege.kits.Skirmisher.SkirmisherDeath;
import me.huntifi.castlesiege.kits.Spearman.DeathSpearman;
import me.huntifi.castlesiege.kits.Spearman.SpearmanAbility;
import me.huntifi.castlesiege.kits.Swordsman.DeathSwordsman;
import me.huntifi.castlesiege.kits.Viking.VikingAbility;
import me.huntifi.castlesiege.kits.Viking.VikingDeath;
import me.huntifi.castlesiege.kits.VotedKits.VotedKitsGUI_Command;
import me.huntifi.castlesiege.kits.VotedKits.Ladderman.LaddermanAbility;
import me.huntifi.castlesiege.kits.VotedKits.Ladderman.LaddermanDeath;
import me.huntifi.castlesiege.kits.VotedKits.Scout.ScoutDeath;
import me.huntifi.castlesiege.kits.Warhound.Warhound;
import me.huntifi.castlesiege.kits.Warhound.WarhoundAbility;
import me.huntifi.castlesiege.kits.Warhound.WarhoundDeath;
import me.huntifi.castlesiege.kits.medic.MedicAbilities;
import me.huntifi.castlesiege.kits.medic.MedicDeath;
import me.huntifi.castlesiege.ladders.LadderEvent;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.mvpCommand.mvpCommand;
import me.huntifi.castlesiege.commands.MapsCommand;
import me.huntifi.castlesiege.commands.discordCommand;
import me.huntifi.castlesiege.commands.pingCommand;
import me.huntifi.castlesiege.commands.rulesCommand;
import me.huntifi.castlesiege.commands.suicideCommand;
import me.huntifi.castlesiege.commands.teamCommand;
import me.huntifi.castlesiege.commands.togglerankCommand;
import me.huntifi.castlesiege.commands.Teamchat.TeamChat;
import me.huntifi.castlesiege.commands.message.MessageCommand;
import me.huntifi.castlesiege.commands.message.ReplyCommand;
import me.huntifi.castlesiege.commands.staffCommands.FlyCommand;
import me.huntifi.castlesiege.commands.staffCommands.KickCommand;
import me.huntifi.castlesiege.commands.staffCommands.KickallCommand;
import me.huntifi.castlesiege.commands.staffCommands.NextMapCommand;
import me.huntifi.castlesiege.commands.staffCommands.SessionMuteCommand;
import me.huntifi.castlesiege.commands.staffCommands.StaffChat;
import me.huntifi.castlesiege.commands.staffCommands.UnsessionmuteCommand;
import me.huntifi.castlesiege.scoreboard.scoreboard;
import me.huntifi.castlesiege.security.CustomFallDamage;
import me.huntifi.castlesiege.security.DropItemSecurity;
import me.huntifi.castlesiege.security.Hunger;
import me.huntifi.castlesiege.security.NoCombat;
import me.huntifi.castlesiege.security.NoFireDestroy;
import me.huntifi.castlesiege.security.NoMoveInventory;
import me.huntifi.castlesiege.security.NoPaintingDestroy;
import me.huntifi.castlesiege.security.NoTouchArmorstand;
import me.huntifi.castlesiege.security.NoTouchPaintings;
import me.huntifi.castlesiege.security.ambientDamage;
import me.huntifi.castlesiege.security.armorTakeOff;
import me.huntifi.castlesiege.security.destroyBlocks;
import me.huntifi.castlesiege.security.instarespawn;
import me.huntifi.castlesiege.security.placeBlocks;
import me.huntifi.castlesiege.security.preventBlockOpening;
import me.huntifi.castlesiege.security.voidOfLimits;
import me.huntifi.castlesiege.security.wheat;
import me.huntifi.castlesiege.stats.MVP.MVPstats;
import me.huntifi.castlesiege.stats.MVP.StatsMvpJoinevent;
import me.huntifi.castlesiege.stats.levels.RegisterLevel;
import me.huntifi.castlesiege.stats.mystats.MystatsCommand;
import me.huntifi.castlesiege.tablist.Tablist;
import me.huntifi.castlesiege.teams.SwitchCommand;
import me.huntifi.castlesiege.voting.GiveVoteCommand;
import me.huntifi.castlesiege.voting.VoteListenerCommand;
import me.huntifi.castlesiege.voting.VotesLoading;
import me.huntifi.castlesiege.voting.VotesUnloading;



public class Main extends JavaPlugin implements Listener {

	public Tablist tab;
	public static MySQL SQL;

	public SQLGetter data;
	public SQLstats data3;


	@Override
	public void onEnable() {

		SQL = new MySQL();
		this.data = new SQLGetter(this);
		this.data3 = new SQLstats(this);

		createWorld();

		getServer().getConsoleSender().sendMessage(ChatColor.DARK_GREEN + "Thedarkage Plugin has been enabled!");

		try {
			SQL.connect();
		} catch (ClassNotFoundException | SQLException e) {
			Bukkit.getLogger().info("<!> Database is not connected! <!>");
			getServer().getConsoleSender().sendMessage(ChatColor.DARK_GREEN + "<!> Database is not connected! <!>");
		}

		if (SQL.isConnected()) {

			Bukkit.getLogger().info("<!> Database is connected! <!>");
			getServer().getConsoleSender().sendMessage(ChatColor.DARK_GREEN + "<!> Database is connected! <!>");
			SQLstats.createTable();
			this.getServer().getPluginManager().registerEvents(this, this);

		}

		getServer().getPluginManager().registerEvents(new Warhound(), this);
		getServer().getPluginManager().registerEvents(new SessionMuteCommand(), this);
		getServer().getPluginManager().registerEvents(new HelmsdeepSecretDoor(), this);
		getServer().getPluginManager().registerEvents(new EatCake(), this);
		getServer().getPluginManager().registerEvents(new RegisterLevel(), this);
		getServer().getPluginManager().registerEvents(new NoMoveInventory(), this);
		getServer().getPluginManager().registerEvents(new NoTouchArmorstand(), this);
		getServer().getPluginManager().registerEvents(new NoTouchPaintings(), this);
		getServer().getPluginManager().registerEvents(new KitsGUI_ThunderstoneGuard_Command(), this);
		getServer().getPluginManager().registerEvents(new KitsGUI_Cloudcrawlers_Command(), this);
		getServer().getPluginManager().registerEvents(new ScoutDeath(), this);
		getServer().getPluginManager().registerEvents(new ScoutDeath(), this);
		getServer().getPluginManager().registerEvents(new LaddermanDeath(), this);
		getServer().getPluginManager().registerEvents(new LaddermanAbility(), this);
		getServer().getPluginManager().registerEvents(new HelmsdeepCaveBoat(), this);
		getServer().getPluginManager().registerEvents(new arrowRemoval(), this);
		getServer().getPluginManager().registerEvents(new HitMessage(), this);
		getServer().getPluginManager().registerEvents(new EngineerCobweb(), this);
		getServer().getPluginManager().registerEvents(new DeathEngineer(), this);
		getServer().getPluginManager().registerEvents(new StatsMvpJoinevent(), this);
		getServer().getPluginManager().registerEvents(new StatsSaving(), this);
		getServer().getPluginManager().registerEvents(new StatsLoading(), this);

		getServer().getPluginManager().registerEvents(new RangerAbility(), this);
		getServer().getPluginManager().registerEvents(new DeathRanger(), this);
		getServer().getPluginManager().registerEvents(new HalberdierAbility(), this);
		getServer().getPluginManager().registerEvents(new DeathHalberdier(), this);
		getServer().getPluginManager().registerEvents(new Woolmap_Distance(), this);
		getServer().getPluginManager().registerEvents(new CavalryAbility(), this);
		getServer().getPluginManager().registerEvents(new CavalryDeath(), this);
		getServer().getPluginManager().registerEvents(new CrossbowmanDeath(), this);
		getServer().getPluginManager().registerEvents(new CrossbowmanAbility(), this);
		getServer().getPluginManager().registerEvents(new VikingAbility(), this);
		getServer().getPluginManager().registerEvents(new VikingDeath(), this);
		getServer().getPluginManager().registerEvents(new MacemanAbility(), this);
		getServer().getPluginManager().registerEvents(new ExecutionerAbility(), this);
		getServer().getPluginManager().registerEvents(new ExecutionerDeath(), this);
		getServer().getPluginManager().registerEvents(new BerserkerAbility(), this);
		getServer().getPluginManager().registerEvents(new BerserkerDeath(), this);
		getServer().getPluginManager().registerEvents(new MacemanDeath(), this);
		getServer().getPluginManager().registerEvents(new VotedKitsGUI_Command(), this);
		getServer().getPluginManager().registerEvents(new ClassicGui_Command(), this);
		getServer().getPluginManager().registerEvents(new KitGUI_Rohan_Command(), this);
		getServer().getPluginManager().registerEvents(new KitGUI_Isengard_Command(), this);
		getServer().getPluginManager().registerEvents(new newLogin(), this);
		getServer().getPluginManager().registerEvents(new login(), this);
		getServer().getPluginManager().registerEvents(new CustomFallDamage(), this);
		getServer().getPluginManager().registerEvents(new preventBlockOpening(), this);
		getServer().getPluginManager().registerEvents(new Herugrim(), this);
		getServer().getPluginManager().registerEvents(new destroyBlocks(), this);
		getServer().getPluginManager().registerEvents(new placeBlocks(), this);
		getServer().getPluginManager().registerEvents(new wheat(), this);
		getServer().getPluginManager().registerEvents(new NoPaintingDestroy(), this);
		getServer().getPluginManager().registerEvents(new NoFireDestroy(), this);
		getServer().getPluginManager().registerEvents(new NoCombat(), this);
		getServer().getPluginManager().registerEvents(new instarespawn(), this);
		getServer().getPluginManager().registerEvents(new ambientDamage(), this);
		getServer().getPluginManager().registerEvents(new voidOfLimits(), this);
		getServer().getPluginManager().registerEvents(new PlayerChat(), this);
		getServer().getPluginManager().registerEvents(new DropItemSecurity(), this);
		getServer().getPluginManager().registerEvents(new Enderchest(), this);
		getServer().getPluginManager().registerEvents(new NoHurtTeam(), this);

		getServer().getPluginManager().registerEvents(new DeathArcher(), this);
		getServer().getPluginManager().registerEvents(new SkirmisherDeath(), this);
		getServer().getPluginManager().registerEvents(new DeathSwordsman(), this);
		getServer().getPluginManager().registerEvents(new DeathShieldman(), this);
		getServer().getPluginManager().registerEvents(new DeathFirearcher(), this);
		getServer().getPluginManager().registerEvents(new DeathSpearman(), this);
		getServer().getPluginManager().registerEvents(new DeathmessageDisable(), this);
		getServer().getPluginManager().registerEvents(new CustomRegeneration(), this);

		getServer().getPluginManager().registerEvents(new HelmsdeepJoin(), this);
		getServer().getPluginManager().registerEvents(new HelmsdeepLeave(), this);
		getServer().getPluginManager().registerEvents(new HelmsdeepDeath(), this);
		getServer().getPluginManager().registerEvents(new FlagRadius(), this);
		getServer().getPluginManager().registerEvents(new SupplyCampFlag(), this);
		getServer().getPluginManager().registerEvents(new CavesFlag(), this);
		getServer().getPluginManager().registerEvents(new MainGateFlag(), this);
		getServer().getPluginManager().registerEvents(new CourtyardFlag(), this);
		getServer().getPluginManager().registerEvents(new GreatHallsFlag(), this);
		getServer().getPluginManager().registerEvents(new HornFlag(), this);
		getServer().getPluginManager().registerEvents(new Woolmap(), this);

		getServer().getPluginManager().registerEvents(new HelmsdeepCavesDoor(), this);
		getServer().getPluginManager().registerEvents(new HelmsdeepGreatHallLeftDoor(), this);
		getServer().getPluginManager().registerEvents(new HelmsdeepGreatHallRightDoor(), this);
		getServer().getPluginManager().registerEvents(new HelmsdeepMainGateLeftDoor(), this);
		getServer().getPluginManager().registerEvents(new HelmsdeepMainGateRightDoor(), this);
		getServer().getPluginManager().registerEvents(new HelmsdeepStorageDoor(), this);
		getServer().getPluginManager().registerEvents(new GreatHallExtraDoor(), this);
		getServer().getPluginManager().registerEvents(new WallEvent(), this);
		getServer().getPluginManager().registerEvents(new LadderEvent(), this);
		getServer().getPluginManager().registerEvents(new armorTakeOff(), this);
		getServer().getPluginManager().registerEvents(new HelmsdeepMainGateDestroyEvent(), this);
		getServer().getPluginManager().registerEvents(new HelmsdeepGreatHallDestroyEvent(), this);
		getServer().getPluginManager().registerEvents(new MVPstats(), this);
		getServer().getPluginManager().registerEvents(new SpearmanAbility(), this);
		getServer().getPluginManager().registerEvents(new KitGUIcommand(), this);
		getServer().getPluginManager().registerEvents(new MessageCommand(), this);
		getServer().getPluginManager().registerEvents(new HelmsdeepBallistaEvent(), this);

		getServer().getPluginManager().registerEvents(new TwinbridgeFlag(), this);
		getServer().getPluginManager().registerEvents(new SkyviewTowerFlag(), this);
		getServer().getPluginManager().registerEvents(new LonelyTowerFlag(), this);
		getServer().getPluginManager().registerEvents(new WestTowerFlag(), this);
		getServer().getPluginManager().registerEvents(new StairhallFlag(), this);
		getServer().getPluginManager().registerEvents(new ShiftedTowerFlag(), this);
		getServer().getPluginManager().registerEvents(new TS_FlagRadius(), this);
		getServer().getPluginManager().registerEvents(new ThunderstoneDeath(), this);
		getServer().getPluginManager().registerEvents(new ThunderstoneJoin(), this);
		getServer().getPluginManager().registerEvents(new HelmsdeepEndMVP(), this);
		getServer().getPluginManager().registerEvents(new ThunderstoneEndMVP(), this);
		getServer().getPluginManager().registerEvents(new TS_Woolmap(), this);
		getServer().getPluginManager().registerEvents(new TS_Woolmap_Distance(), this);
		getServer().getPluginManager().registerEvents(new ThunderstoneLeave(), this);
		getServer().getPluginManager().registerEvents(new ThunderstoneGateDestroyEvent(), this);
		getServer().getPluginManager().registerEvents(new EastTowerFlag(), this);
		
		getServer().getPluginManager().registerEvents(new VotesLoading(), this);
		getServer().getPluginManager().registerEvents(new VotesUnloading(), this);

		getServer().getPluginManager().registerEvents(new FireArcherAbility(), this);
		getServer().getPluginManager().registerEvents(new MedicAbilities(), this);
		
		getServer().getPluginManager().registerEvents(new WarhoundDeath(), this);
		getServer().getPluginManager().registerEvents(new MedicDeath(), this);
		getServer().getPluginManager().registerEvents(new WarhoundAbility(), this);

		getCommand("KitThunderstoneGuards").setExecutor(new KitsGUI_ThunderstoneGuard_Command());
		getCommand("KitCloudcrawlers").setExecutor(new KitsGUI_Cloudcrawlers_Command());
		getCommand("Switch").setExecutor(new SwitchCommand());
		getCommand("VoterKitGUI").setExecutor(new VotedKitsGUI_Command());
		getCommand("ClassicGUI").setExecutor(new ClassicGui_Command());
		getCommand("togglerank").setExecutor(new togglerankCommand());
		getCommand("KitRohan").setExecutor(new KitGUI_Rohan_Command());
		getCommand("KitIsengard").setExecutor(new KitGUI_Isengard_Command());
		getCommand("ping").setExecutor(new pingCommand());
		getCommand("rules").setExecutor(new rulesCommand());
		getCommand("discord").setExecutor(new discordCommand());
		getCommand("teams").setExecutor(new teamCommand());
		getCommand("Kit").setExecutor(new KitsCommand());
		getCommand("Mvp").setExecutor(new mvpCommand());
		getCommand("KitGUI").setExecutor(new KitGUIcommand());
		getCommand("Mystats").setExecutor(new MystatsCommand());
		getCommand("NextMap").setExecutor(new NextMapCommand());
		getCommand("t").setExecutor(new TeamChat());
		getCommand("msg").setExecutor(new MessageCommand());
		getCommand("r").setExecutor(new ReplyCommand());
		getCommand("maps").setExecutor(new MapsCommand());
		getCommand("sui").setExecutor(new suicideCommand());
		getCommand("CheckFlagList").setExecutor(new FlagListCommand());
		getCommand("s").setExecutor(new StaffChat());
		getCommand("kick").setExecutor(new KickCommand());
		getCommand("Fly").setExecutor(new FlyCommand());
		getCommand("kickall").setExecutor(new KickallCommand());
		getCommand("sessionmute").setExecutor(new SessionMuteCommand());
		getCommand("Unsessionmute").setExecutor(new UnsessionmuteCommand());
		
		getCommand("givevote").setExecutor(new GiveVoteCommand());
		getCommand("givevoter").setExecutor(new VoteListenerCommand());


		Bukkit.getServer().getScheduler().runTaskTimer(this, new ThunderstoneEndMap(), 0, 200);
		Bukkit.getServer().getScheduler().runTaskTimer(this, new HelmsdeepCaveBoat(), 0, 200);
		Bukkit.getServer().getScheduler().runTaskTimer(this, new scoreboard(), 0, 20);
		Bukkit.getServer().getScheduler().runTaskTimer(this, new HelmsdeepEndMap(), 0, 20);
		Bukkit.getServer().getScheduler().runTaskTimer(this, new ApplyRegeneration(), 0, 75);
		Bukkit.getServer().getScheduler().runTaskTimer(this, new Hunger(), 0, 20);
		Bukkit.getServer().getScheduler().runTaskTimer(this, new HelmsdeepMVPupdater(), 0, 20);
		Bukkit.getServer().getScheduler().runTaskTimer(this, new ThunderstoneMVPupdater(), 0, 20);
		Bukkit.getServer().getScheduler().runTaskTimer(this, new HalberdierAbility(), 100, 25);
		Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(this, new DatabaseKeepAliveEvent(), 500, 500);
		Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(this, new Herugrim(), 10, 10);

		Bukkit.getServer().getScheduler().runTaskTimer(this, new MainGateReadyRam(), 200, 60);
		Bukkit.getServer().getScheduler().runTaskTimer(this, new MainGateRamAnimation(), 200, MainGateRam.rammingSpeed);
		Bukkit.getServer().getScheduler().runTaskTimer(this, new MainGateRam(), 200, 40);
		
		Bukkit.getServer().getScheduler().runTaskTimer(this, new GreatHallGateReadyRam(), 200, 60);
		Bukkit.getServer().getScheduler().runTaskTimer(this, new GreatHallRamAnimation(), 200, GreatHallGateRam.rammingSpeed);
		Bukkit.getServer().getScheduler().runTaskTimer(this, new GreatHallGateRam(), 200, 40);

		Bukkit.getServer().getScheduler().runTaskTimer(this, new ThunderstoneGateReadyRam(), 200, 60);
		Bukkit.getServer().getScheduler().runTaskTimer(this, new ThunderstoneRamAnimation(), 200, ThunderstoneRam.rammingSpeed);
		Bukkit.getServer().getScheduler().runTaskTimer(this, new ThunderstoneRam(), 200, 40);
		
		MapController.setMap("HelmsDeep");

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

		HelmsdeepReset.onReset();
		HelmsdeepTimer.HelmsdeepTimerEvent();
		HelmsdeepGreatHallBlocks.gateblocks();
		HelmsdeepMainGateBlocks.gateblocks();
		HelmsdeepCaveBoat.spawnFirstBoat();

		//resets Thunderstone after restart
		ThunderstoneReset.onReset();
		ThunderstoneGateBlocks.gateblocks();
		
		Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("ConwyCastleSiege");

		new BukkitRunnable() {

			@Override
			public void run() {
				
				Herugrim.spawnHerugrim();

			}
		}.runTaskLater(plugin, 200);

	}

	@Override
	public void onDisable() {

		SQL.disconnect();

		getServer().getConsoleSender().sendMessage(ChatColor.DARK_RED + "Thedarkage Plugin has been disabled!");

	}
	World world;

	private void createWorld() {
		WorldCreator d = new WorldCreator("HelmsDeep");
		d.generateStructures(false);
		WorldCreator t = new WorldCreator("Thunderstone");
		t.generateStructures(false);

		world = d.createWorld();
		world = t.createWorld();
	}


	@EventHandler
	public void onJoin(PlayerJoinEvent e) {

		Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("ConwyCastleSiege");

		Player p = e.getPlayer();

		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {

			if (!SQLstats.exists(p.getUniqueId())) {

				SQLstats.createPlayer(p);

			}
		});
	}
	
}
