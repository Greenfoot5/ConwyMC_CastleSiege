package me.greenfoot5.castlesiege;

import com.fren_gor.ultimateAdvancementAPI.UltimateAdvancementAPI;
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
import me.greenfoot5.castlesiege.advancements.CSAdvancementController;
import me.greenfoot5.castlesiege.commands.chat.TeamChatCommand;
import me.greenfoot5.castlesiege.commands.donator.FireworkCommand;
import me.greenfoot5.castlesiege.commands.donator.DuelCommand;
import me.greenfoot5.castlesiege.commands.gameplay.BoosterCommand;
import me.greenfoot5.castlesiege.commands.gameplay.BountyCommand;
import me.greenfoot5.castlesiege.commands.gameplay.BuyKitCommand;
import me.greenfoot5.castlesiege.commands.gameplay.CoinShopCommand;
import me.greenfoot5.castlesiege.commands.gameplay.KitCommand;
import me.greenfoot5.castlesiege.commands.gameplay.MapVoteCommand;
import me.greenfoot5.castlesiege.commands.gameplay.RandomKitCommand;
import me.greenfoot5.castlesiege.commands.gameplay.SettingsCommand;
import me.greenfoot5.castlesiege.commands.gameplay.SuicideCommand;
import me.greenfoot5.castlesiege.commands.gameplay.SwitchCommand;
import me.greenfoot5.castlesiege.commands.gameplay.VoteSkipCommand;
import me.greenfoot5.castlesiege.commands.info.FlagsCommand;
import me.greenfoot5.castlesiege.commands.info.MapsCommand;
import me.greenfoot5.castlesiege.commands.info.MyStatsCommand;
import me.greenfoot5.castlesiege.commands.info.PreviewCommand;
import me.greenfoot5.castlesiege.commands.info.SecretsCommand;
import me.greenfoot5.castlesiege.commands.info.TeamsCommand;
import me.greenfoot5.castlesiege.commands.info.leaderboard.LeaderboardCommand;
import me.greenfoot5.castlesiege.commands.info.leaderboard.MVPCommand;
import me.greenfoot5.castlesiege.commands.info.leaderboard.TopMatchCommand;
import me.greenfoot5.castlesiege.commands.staff.GiveVoteCommand;
import me.greenfoot5.castlesiege.commands.staff.ReloadCommand;
import me.greenfoot5.castlesiege.commands.staff.boosters.GrantBoosterCommand;
import me.greenfoot5.castlesiege.commands.staff.donations.SetKitCommand;
import me.greenfoot5.castlesiege.commands.staff.donations.UnlockedKitCommand;
import me.greenfoot5.castlesiege.commands.staff.maps.NextMapCommand;
import me.greenfoot5.castlesiege.commands.staff.maps.SetKitLimitCommand;
import me.greenfoot5.castlesiege.commands.staff.maps.SetMapCommand;
import me.greenfoot5.castlesiege.commands.staff.maps.SetTimerCommand;
import me.greenfoot5.castlesiege.commands.staff.maps.SpectateCommand;
import me.greenfoot5.castlesiege.commands.staff.maps.StartCommand;
import me.greenfoot5.castlesiege.commands.staff.maps.ToggleAllKitsFreeCommand;
import me.greenfoot5.castlesiege.commands.staff.maps.ToggleForcedRandom;
import me.greenfoot5.castlesiege.commands.staff.maps.ToggleSwitchingCommand;
import me.greenfoot5.castlesiege.data_types.Booster;
import me.greenfoot5.castlesiege.data_types.CSPlayerData;
import me.greenfoot5.castlesiege.data_types.LocationFrame;
import me.greenfoot5.castlesiege.data_types.SchematicFrame;
import me.greenfoot5.castlesiege.database.MVPStats;
import me.greenfoot5.castlesiege.database.StoreData;
import me.greenfoot5.castlesiege.events.chat.CSGlobalChat;
import me.greenfoot5.castlesiege.events.combat.ArrowCollision;
import me.greenfoot5.castlesiege.events.combat.ArrowRemoval;
import me.greenfoot5.castlesiege.events.combat.AssistKill;
import me.greenfoot5.castlesiege.events.combat.DamageBalance;
import me.greenfoot5.castlesiege.events.combat.EatCake;
import me.greenfoot5.castlesiege.events.combat.FallDamage;
import me.greenfoot5.castlesiege.events.combat.HitMessage;
import me.greenfoot5.castlesiege.events.combat.HurtAnimation;
import me.greenfoot5.castlesiege.events.combat.InCombat;
import me.greenfoot5.castlesiege.events.combat.LobbyCombat;
import me.greenfoot5.castlesiege.events.combat.TeamCombat;
import me.greenfoot5.castlesiege.events.connection.PlayerConnect;
import me.greenfoot5.castlesiege.events.connection.PlayerDisconnect;
import me.greenfoot5.castlesiege.events.curses.CurseCommand;
import me.greenfoot5.castlesiege.events.death.DeathEvent;
import me.greenfoot5.castlesiege.events.death.VoidLocation;
import me.greenfoot5.castlesiege.events.gameplay.CamelHandler;
import me.greenfoot5.castlesiege.events.gameplay.Explosion;
import me.greenfoot5.castlesiege.events.gameplay.HorseHandler;
import me.greenfoot5.castlesiege.events.gameplay.LeaveMapBorder;
import me.greenfoot5.castlesiege.events.gameplay.Movement;
import me.greenfoot5.castlesiege.events.security.InteractContainer;
import me.greenfoot5.castlesiege.events.security.InventoryProtection;
import me.greenfoot5.castlesiege.events.security.MapProtection;
import me.greenfoot5.castlesiege.events.timed.ApplyRegeneration;
import me.greenfoot5.castlesiege.events.timed.BarCooldown;
import me.greenfoot5.castlesiege.events.timed.Hunger;
import me.greenfoot5.castlesiege.events.timed.Tips;
import me.greenfoot5.castlesiege.kits.items.Enderchest;
import me.greenfoot5.castlesiege.kits.items.MenuItem;
import me.greenfoot5.castlesiege.kits.items.WoolHat;
import me.greenfoot5.castlesiege.kits.kits.CoinKit;
import me.greenfoot5.castlesiege.kits.kits.Kit;
import me.greenfoot5.castlesiege.kits.kits.SignKit;
import me.greenfoot5.castlesiege.kits.kits.coin_kits.Alchemist;
import me.greenfoot5.castlesiege.kits.kits.coin_kits.Barbarian;
import me.greenfoot5.castlesiege.kits.kits.coin_kits.Berserker;
import me.greenfoot5.castlesiege.kits.kits.coin_kits.Cavalry;
import me.greenfoot5.castlesiege.kits.kits.coin_kits.Crossbowman;
import me.greenfoot5.castlesiege.kits.kits.coin_kits.Engineer;
import me.greenfoot5.castlesiege.kits.kits.coin_kits.Executioner;
import me.greenfoot5.castlesiege.kits.kits.coin_kits.Halberdier;
import me.greenfoot5.castlesiege.kits.kits.coin_kits.Maceman;
import me.greenfoot5.castlesiege.kits.kits.coin_kits.Medic;
import me.greenfoot5.castlesiege.kits.kits.coin_kits.Paladin;
import me.greenfoot5.castlesiege.kits.kits.coin_kits.Priest;
import me.greenfoot5.castlesiege.kits.kits.coin_kits.Ranger;
import me.greenfoot5.castlesiege.kits.kits.coin_kits.Rogue;
import me.greenfoot5.castlesiege.kits.kits.coin_kits.Sorcerer;
import me.greenfoot5.castlesiege.kits.kits.coin_kits.Vanguard;
import me.greenfoot5.castlesiege.kits.kits.coin_kits.Viking;
import me.greenfoot5.castlesiege.kits.kits.coin_kits.Warhound;
import me.greenfoot5.castlesiege.kits.kits.coin_kits.Warlock;
import me.greenfoot5.castlesiege.kits.kits.event_kits.HallowedHorseman;
import me.greenfoot5.castlesiege.kits.kits.event_kits.Vampire;
import me.greenfoot5.castlesiege.kits.kits.event_kits.Werewolf;
import me.greenfoot5.castlesiege.kits.kits.free_kits.Archer;
import me.greenfoot5.castlesiege.kits.kits.sign_kits.Buccaneer;
import me.greenfoot5.castlesiege.kits.kits.sign_kits.Gunner;
import me.greenfoot5.castlesiege.kits.kits.sign_kits.Moria.DwarvenXbow;
import me.greenfoot5.castlesiege.kits.kits.sign_kits.Moria.Hammerguard;
import me.greenfoot5.castlesiege.kits.kits.sign_kits.Moria.OrcPikeman;
import me.greenfoot5.castlesiege.kits.kits.sign_kits.Moria.Overseer;
import me.greenfoot5.castlesiege.kits.kits.sign_kits.Moria.Skullcrusher;
import me.greenfoot5.castlesiege.kits.kits.sign_kits.Pirate;
import me.greenfoot5.castlesiege.kits.kits.level_kits.Shieldman;
import me.greenfoot5.castlesiege.kits.kits.level_kits.Spearman;
import me.greenfoot5.castlesiege.kits.kits.free_kits.Swordsman;
import me.greenfoot5.castlesiege.kits.kits.in_development.Armourer;
import me.greenfoot5.castlesiege.kits.kits.in_development.Bannerman;
import me.greenfoot5.castlesiege.kits.kits.level_kits.BattleMedic;
import me.greenfoot5.castlesiege.kits.kits.level_kits.Hypaspist;
import me.greenfoot5.castlesiege.kits.kits.level_kits.SpearKnight;
import me.greenfoot5.castlesiege.kits.kits.sign_kits.Abyssal;
import me.greenfoot5.castlesiege.kits.kits.sign_kits.Arbalester;
import me.greenfoot5.castlesiege.kits.kits.sign_kits.Artillerist;
import me.greenfoot5.castlesiege.kits.kits.sign_kits.Moria.AxeThrower;
import me.greenfoot5.castlesiege.kits.kits.sign_kits.Axeman;
import me.greenfoot5.castlesiege.kits.kits.sign_kits.Moria.Bonecrusher;
import me.greenfoot5.castlesiege.kits.kits.sign_kits.CamelRider;
import me.greenfoot5.castlesiege.kits.kits.sign_kits.Moria.CaveTroll;
import me.greenfoot5.castlesiege.kits.kits.sign_kits.Constructor;
import me.greenfoot5.castlesiege.kits.kits.sign_kits.Elytrier;
import me.greenfoot5.castlesiege.kits.kits.sign_kits.Fallen;
import me.greenfoot5.castlesiege.kits.kits.sign_kits.Moria.Guardian;
import me.greenfoot5.castlesiege.kits.kits.sign_kits.Hellsteed;
import me.greenfoot5.castlesiege.kits.kits.sign_kits.Lancer;
import me.greenfoot5.castlesiege.kits.kits.sign_kits.Longbowman;
import me.greenfoot5.castlesiege.kits.kits.sign_kits.Moria.MoriaOrc;
import me.greenfoot5.castlesiege.kits.kits.sign_kits.RangedCavalry;
import me.greenfoot5.castlesiege.kits.kits.sign_kits.RoyalKnight;
import me.greenfoot5.castlesiege.kits.kits.sign_kits.UrukBerserker;
import me.greenfoot5.castlesiege.kits.kits.staff_kits.Warbear;
import me.greenfoot5.castlesiege.kits.kits.voter_kits.FireArcher;
import me.greenfoot5.castlesiege.kits.kits.voter_kits.Ladderman;
import me.greenfoot5.castlesiege.kits.kits.voter_kits.Scout;
import me.greenfoot5.castlesiege.kits.kits.voter_kits.Skirmisher;
import me.greenfoot5.castlesiege.maps.CoreMap;
import me.greenfoot5.castlesiege.maps.Gamemode;
import me.greenfoot5.castlesiege.maps.Hommet.CollapseEvent;
import me.greenfoot5.castlesiege.maps.Lobby;
import me.greenfoot5.castlesiege.maps.Map;
import me.greenfoot5.castlesiege.maps.MapController;
import me.greenfoot5.castlesiege.maps.Scoreboard;
import me.greenfoot5.castlesiege.maps.Team;
import me.greenfoot5.castlesiege.maps.TeamController;
import me.greenfoot5.castlesiege.maps.WoolMap;
import me.greenfoot5.castlesiege.maps.WoolMapBlock;
import me.greenfoot5.castlesiege.maps.helms_deep.WallEvent;
import me.greenfoot5.castlesiege.maps.objects.AssaultFlag;
import me.greenfoot5.castlesiege.maps.objects.ButtonDoor;
import me.greenfoot5.castlesiege.maps.objects.Cannon;
import me.greenfoot5.castlesiege.maps.objects.Catapult;
import me.greenfoot5.castlesiege.maps.objects.ChargeFlag;
import me.greenfoot5.castlesiege.maps.objects.Core;
import me.greenfoot5.castlesiege.maps.objects.Door;
import me.greenfoot5.castlesiege.maps.objects.Flag;
import me.greenfoot5.castlesiege.maps.objects.Gate;
import me.greenfoot5.castlesiege.maps.objects.LeverDoor;
import me.greenfoot5.castlesiege.maps.objects.PressurePlateDoor;
import me.greenfoot5.castlesiege.maps.objects.Ram;
import me.greenfoot5.castlesiege.maps.objects.RegionHandler;
import me.greenfoot5.castlesiege.misc.CSNameTag;
import me.greenfoot5.castlesiege.misc.mythic.MythicListener;
import me.greenfoot5.castlesiege.secrets.Abrakhan.AbrakhanSecretDoor;
import me.greenfoot5.castlesiege.secrets.Helmsdeep.SecretDoor;
import me.greenfoot5.castlesiege.secrets.SecretBlocks;
import me.greenfoot5.castlesiege.secrets.SecretItems;
import me.greenfoot5.castlesiege.secrets.SecretSigns;
import me.greenfoot5.castlesiege.secrets.Skyhold.SkyholdDoors;
import me.greenfoot5.castlesiege.secrets.Thunderstone.SecretPortal;
import me.greenfoot5.conwymc.data_types.Tuple;
import me.greenfoot5.conwymc.database.KeepAlive;
import me.greenfoot5.conwymc.database.MySQL;
import me.greenfoot5.conwymc.util.Messenger;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.util.TriState;
import net.megavex.scoreboardlibrary.api.ScoreboardLibrary;
import net.megavex.scoreboardlibrary.api.exception.NoPacketAdapterAvailableException;
import net.megavex.scoreboardlibrary.api.noop.NoopScoreboardLibrary;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import static net.kyori.adventure.text.format.NamedTextColor.AQUA;
import static net.kyori.adventure.text.format.NamedTextColor.BLACK;
import static net.kyori.adventure.text.format.NamedTextColor.BLUE;
import static net.kyori.adventure.text.format.NamedTextColor.DARK_AQUA;
import static net.kyori.adventure.text.format.NamedTextColor.DARK_BLUE;
import static net.kyori.adventure.text.format.NamedTextColor.DARK_GRAY;
import static net.kyori.adventure.text.format.NamedTextColor.DARK_GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.DARK_PURPLE;
import static net.kyori.adventure.text.format.NamedTextColor.DARK_RED;
import static net.kyori.adventure.text.format.NamedTextColor.GOLD;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.LIGHT_PURPLE;
import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static net.kyori.adventure.text.format.NamedTextColor.WHITE;
import static net.kyori.adventure.text.format.NamedTextColor.YELLOW;

/**
 * The main class for Castle Siege
 */
public class Main extends JavaPlugin implements Listener {

    public static Plugin plugin;
    public static Main instance;
    public static ScoreboardLibrary scoreboardLibrary;

    public static MySQL SQL;

    private YamlDocument[] mapConfigs;
    private YamlDocument[] flagsConfigs;
    private YamlDocument[] doorsConfigs;
    private YamlDocument[] gatesConfigs;
    private YamlDocument[] catapultsConfigs;
    private YamlDocument[] cannonConfigs;
    private YamlDocument[] coreConfigs;
    private ArrayList<BukkitTask> timedTasks = new ArrayList<>();
    private final List<Listener> listeners = new ArrayList<>();

    private YamlDocument gameConfig;

    public boolean hasLoaded = false;
    public int mapsLoaded = 0;

    @Override
    public void onEnable()  {

        getLogger().info("Enabling Plugin...");

        plugin = Bukkit.getServer().getPluginManager().getPlugin("CastleSiege");
        instance = this;

        getLogger().info("Waiting until POSTWORLD to continue enabling...");
        new BukkitRunnable() {

            @Override
            public void run() {
                getLogger().info("Resuming loading plugin...");
                getLogger().info("Loading configuration files...");
                createConfigs();
                getLogger().info("Loading maps from configuration...");
                loadMaps(0);
                getLogger().info("Loading game configuration...");
                loadConfig();

                getLogger().info("Connecting to database...");
                // SQL Stuff
                try {
                    sqlConnect();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                // World Guard
                SessionManager sessionManager = WorldGuard.getInstance().getPlatform().getSessionManager();
                // second param allows for ordering of handlers - see the JavaDocs
                sessionManager.registerHandler(RegionHandler.FACTORY, null);

                //Scoreboard
                try {
                    assert plugin != null;
                    scoreboardLibrary = ScoreboardLibrary.loadScoreboardLibrary(plugin);
                } catch (NoPacketAdapterAvailableException e) {
                    // If no packet adapter was found, you can fall back to the no-op implementation:
                    scoreboardLibrary = new NoopScoreboardLibrary();
                    plugin.getLogger().warning("No scoreboard packet adapter available!");
                }

                // Rewrite Events
                registerListener(new Enderchest());
                registerListener(new BoosterCommand());
                registerListener(new TeamChatCommand());
                registerListener(new CSGlobalChat());

                // Connection
                registerListener(new PlayerConnect());
                registerListener(new PlayerDisconnect());

                // Secrets
                registerListener(new AbrakhanSecretDoor());
                registerListener(new SecretDoor());
                registerListener(new SecretItems());
                registerListener(new SecretSigns());
                registerListener(new SecretBlocks());
                registerListener(new SecretPortal());
                registerListener(new SkyholdDoors());
                registerListener(new AbrakhanSecretDoor());

                // Combat
                registerListener(new ArrowCollision());
                registerListener(new ArrowRemoval());
                registerListener(new AssistKill());
                registerListener(new DamageBalance());
                registerListener(new EatCake());
                registerListener(new FallDamage());
                registerListener(new HitMessage());
                registerListener(new HurtAnimation());
                registerListener(new InCombat());
                registerListener(new LobbyCombat());
                registerListener(new TeamCombat());

                // Death
                registerListener(new DeathEvent());
                registerListener(new VoidLocation());

                // Gameplay
                registerListener(new Explosion());
                registerListener(new HorseHandler());
                registerListener(new CamelHandler());
                registerListener(new LeaveMapBorder());
                registerListener(new MenuItem());
                registerListener(new Movement());
                registerListener(new MVPStats());
                registerListener(new MapVoteCommand());

                // Security
                registerListener(new InteractContainer());
                registerListener(new InventoryProtection());
                registerListener(new MapProtection());

                // Kits
                registerListener(new Abyssal());
                registerListener(new Alchemist());
                registerListener(new Archer());
                registerListener(new Artillerist());
                registerListener(new Armourer());
                registerListener(new Axeman());
                registerListener(new Berserker());
                registerListener(new Barbarian());
                registerListener(new Bannerman());
                registerListener(new BattleMedic());
                registerListener(new Buccaneer());
                registerListener(new Crossbowman());
                registerListener(new Cavalry());
                registerListener(new CamelRider());
                registerListener(new Constructor());
                registerListener(new DwarvenXbow());
                //registerListener(new Chef());
                registerListener(new Fallen());
                registerListener(new Arbalester());
                registerListener(new Longbowman());
                registerListener(new Engineer());
                registerListener(new Elytrier());
                registerListener(new Executioner());
                registerListener(new FireArcher());
                registerListener(new Gunner());
                registerListener(new Hellsteed());
                registerListener(new Hypaspist());
                registerListener(new Hammerguard());
                registerListener(new Halberdier());
                registerListener(new HallowedHorseman());
                registerListener(new UrukBerserker());
                registerListener(new Lancer());
                registerListener(new RangedCavalry());
                registerListener(new Ladderman());
                registerListener(new Maceman());
                registerListener(new Medic());
                registerListener(new AxeThrower());
                registerListener(new Bonecrusher());
                registerListener(new CaveTroll());
                registerListener(new Guardian());
                registerListener(new MoriaOrc());
                registerListener(new OrcPikeman());
                registerListener(new Overseer());
                registerListener(new Paladin());
                registerListener(new Pirate());
                registerListener(new Priest());
                registerListener(new Rogue());
                registerListener(new Ranger());
                registerListener(new Skullcrusher());
                registerListener(new Shieldman());
                registerListener(new SpearKnight());
                registerListener(new Spearman());
                registerListener(new Sorcerer());
                registerListener(new Vanguard());
                registerListener(new Vampire());
                registerListener(new Viking());
                registerListener(new Warbear());
                registerListener(new Warhound());
                registerListener(new Warlock());
                registerListener(new Werewolf());

                //mythic stuff
                registerListener(new MythicListener());

                // Misc
                registerListener(new CSNameTag());
                registerListener(new RandomKitCommand());
                registerListener(new WoolHat());
                registerListener(new CSAdvancementController());

                // Chat
                Objects.requireNonNull(getCommand("TeamChat")).setExecutor(new TeamChatCommand());

                // Donator
                Objects.requireNonNull(getCommand("Firework")).setExecutor(new FireworkCommand());

                //duels
                //Objects.requireNonNull(getCommand("DuelAccept")).setExecutor(new AcceptDuelCommand());
                Objects.requireNonNull(getCommand("Duel")).setExecutor(new DuelCommand());

                // Gameplay
                Objects.requireNonNull(getCommand("Bounties")).setExecutor(new BountyCommand());
                Objects.requireNonNull(getCommand("Bounty")).setExecutor(new BountyCommand());
                Objects.requireNonNull(getCommand("BuyKit")).setExecutor(new BuyKitCommand());
                Objects.requireNonNull(getCommand("CoinShop")).setExecutor(new CoinShopCommand());
                Objects.requireNonNull(getCommand("Settings")).setExecutor(new SettingsCommand());
                Objects.requireNonNull(getCommand("Suicide")).setExecutor(new SuicideCommand());
                Objects.requireNonNull(getCommand("Switch")).setExecutor(new SwitchCommand());
                Objects.requireNonNull(getCommand("Boosters")).setExecutor(new BoosterCommand());
                Objects.requireNonNull(getCommand("VoteSkip")).setExecutor(new VoteSkipCommand());
                Objects.requireNonNull(getCommand("MapVote")).setExecutor(new MapVoteCommand());

                // Info
                Objects.requireNonNull(getCommand("Flags")).setExecutor(new FlagsCommand());
                Objects.requireNonNull(getCommand("Maps")).setExecutor(new MapsCommand());
                Objects.requireNonNull(getCommand("MVP")).setExecutor(new MVPCommand());
                Objects.requireNonNull(getCommand("MyStats")).setExecutor(new MyStatsCommand());
                Objects.requireNonNull(getCommand("Secrets")).setExecutor(new SecretsCommand());
                Objects.requireNonNull(getCommand("Teams")).setExecutor(new TeamsCommand());
                Objects.requireNonNull(getCommand("Preview")).setExecutor(new PreviewCommand());

                // Leaderboards
                Objects.requireNonNull(getCommand("Top")).setExecutor(new LeaderboardCommand());
                Objects.requireNonNull(getCommand("TopAssists")).setExecutor(new LeaderboardCommand());
                Objects.requireNonNull(getCommand("TopCaptures")).setExecutor(new LeaderboardCommand());
                Objects.requireNonNull(getCommand("TopDeaths")).setExecutor(new LeaderboardCommand());
                Objects.requireNonNull(getCommand("TopHeals")).setExecutor(new LeaderboardCommand());
                Objects.requireNonNull(getCommand("TopKDR")).setExecutor(new LeaderboardCommand());
                Objects.requireNonNull(getCommand("TopKills")).setExecutor(new LeaderboardCommand());
                Objects.requireNonNull(getCommand("TopSupports")).setExecutor(new LeaderboardCommand());
                Objects.requireNonNull(getCommand("TopMatch")).setExecutor(new TopMatchCommand());
                Objects.requireNonNull(getCommand("TopTeam")).setExecutor(new TopMatchCommand());

                // Debug
                Objects.requireNonNull(getCommand("GrantBooster")).setExecutor(new GrantBoosterCommand());
                Objects.requireNonNull(getCommand("Tip")).setExecutor(new Tips());
                Objects.requireNonNull(getCommand("GiveVote")).setExecutor(new GiveVoteCommand());
                Objects.requireNonNull(getCommand("CSReload")).setExecutor(new ReloadCommand());

                // Staff
                Objects.requireNonNull(getCommand("Curse")).setExecutor(new CurseCommand());
                Objects.requireNonNull(getCommand("NextMap")).setExecutor(new NextMapCommand());
                Objects.requireNonNull(getCommand("SetKit")).setExecutor(new SetKitCommand());
                Objects.requireNonNull(getCommand("SetKitLimit")).setExecutor(new SetKitLimitCommand());
                Objects.requireNonNull(getCommand("SetMap")).setExecutor(new SetMapCommand());
                Objects.requireNonNull(getCommand("SetTimer")).setExecutor(new SetTimerCommand());
                Objects.requireNonNull(getCommand("Spectate")).setExecutor(new SpectateCommand());
                Objects.requireNonNull(getCommand("Unlockkit")).setExecutor(new UnlockedKitCommand());
                Objects.requireNonNull(getCommand("ForceSwitch")).setExecutor(new SwitchCommand());
                Objects.requireNonNull(getCommand("ToggleSwitching")).setExecutor(new ToggleSwitchingCommand());
                Objects.requireNonNull(getCommand("Start")).setExecutor(new StartCommand());
                //Objects.requireNonNull(getCommand("SetTag")).setExecutor(new NameTag());
                Objects.requireNonNull(getCommand("ToggleFree")).setExecutor(new ToggleAllKitsFreeCommand());
                Objects.requireNonNull(getCommand("ToggleForcedRandom")).setExecutor(new ToggleForcedRandom());

                // Kits
                Objects.requireNonNull(getCommand("Kit")).setExecutor(new KitCommand());
                Objects.requireNonNull(getCommand("Random")).setExecutor(new RandomKitCommand());
                Objects.requireNonNull(getCommand("Abyssal")).setExecutor(new Abyssal());
                Objects.requireNonNull(getCommand("Alchemist")).setExecutor(new Alchemist());
                Objects.requireNonNull(getCommand("Archer")).setExecutor(new Archer());
                Objects.requireNonNull(getCommand("Artillerist")).setExecutor(new Artillerist());
                //Objects.requireNonNull(getCommand("Armorer")).setExecutor(new Armorer());
                Objects.requireNonNull(getCommand("Battlemedic")).setExecutor(new BattleMedic());
                //Objects.requireNonNull(getCommand("Bannerman")).setExecutor(new Bannerman());
                Objects.requireNonNull(getCommand("Berserker")).setExecutor(new Berserker());
                Objects.requireNonNull(getCommand("Barbarian")).setExecutor(new Barbarian());
                Objects.requireNonNull(getCommand("Buccaneer")).setExecutor(new Buccaneer());
                Objects.requireNonNull(getCommand("Constructor")).setExecutor(new Constructor());
                Objects.requireNonNull(getCommand("Arbalester")).setExecutor(new Arbalester());
                Objects.requireNonNull(getCommand("Longbowman")).setExecutor(new Longbowman());
                Objects.requireNonNull(getCommand("RoyalKnight")).setExecutor(new RoyalKnight());
                Objects.requireNonNull(getCommand("Cavalry")).setExecutor(new Cavalry());
                Objects.requireNonNull(getCommand("CamelRider")).setExecutor(new CamelRider());
                //Objects.requireNonNull(getCommand("Chef")).setExecutor(new Chef());
                Objects.requireNonNull(getCommand("Scout")).setExecutor(new Scout());
                Objects.requireNonNull(getCommand("DwarvenX-bow")).setExecutor(new DwarvenXbow());
                Objects.requireNonNull(getCommand("Fallen")).setExecutor(new Fallen());
                Objects.requireNonNull(getCommand("Crossbowman")).setExecutor(new Crossbowman());
                Objects.requireNonNull(getCommand("Engineer")).setExecutor(new Engineer());
                Objects.requireNonNull(getCommand("Elytrier")).setExecutor(new Elytrier());
                Objects.requireNonNull(getCommand("Executioner")).setExecutor(new Executioner());
                Objects.requireNonNull(getCommand("FireArcher")).setExecutor(new FireArcher());
                Objects.requireNonNull(getCommand("Gunner")).setExecutor(new Gunner());
                Objects.requireNonNull(getCommand("HallowedHorseman")).setExecutor(new HallowedHorseman());
                Objects.requireNonNull(getCommand("Hammerguard")).setExecutor(new Hammerguard());
                Objects.requireNonNull(getCommand("Hellsteed")).setExecutor(new Hellsteed());
                Objects.requireNonNull(getCommand("Halberdier")).setExecutor(new Halberdier());
                Objects.requireNonNull(getCommand("Hypaspist")).setExecutor(new Hypaspist());
                Objects.requireNonNull(getCommand("UrukBerserker")).setExecutor(new UrukBerserker());
                Objects.requireNonNull(getCommand("Lancer")).setExecutor(new Lancer());
                Objects.requireNonNull(getCommand("RangedCavalry")).setExecutor(new RangedCavalry());
                Objects.requireNonNull(getCommand("Axeman")).setExecutor(new Axeman());
                Objects.requireNonNull(getCommand("Ladderman")).setExecutor(new Ladderman());
                Objects.requireNonNull(getCommand("Maceman")).setExecutor(new Maceman());
                Objects.requireNonNull(getCommand("Medic")).setExecutor(new Medic());
                Objects.requireNonNull(getCommand("Guardian")).setExecutor(new Guardian());
                Objects.requireNonNull(getCommand("CaveTroll")).setExecutor(new CaveTroll());
                Objects.requireNonNull(getCommand("Bonecrusher")).setExecutor(new Bonecrusher());
                Objects.requireNonNull(getCommand("AxeThrower")).setExecutor(new AxeThrower());
                Objects.requireNonNull(getCommand("MoriaOrc")).setExecutor(new MoriaOrc());
                Objects.requireNonNull(getCommand("MoriaOverseer")).setExecutor(new Overseer());
                Objects.requireNonNull(getCommand("OrcPikeman")).setExecutor(new OrcPikeman());
                Objects.requireNonNull(getCommand("Paladin")).setExecutor(new Paladin());
                Objects.requireNonNull(getCommand("Pirate")).setExecutor(new Pirate());
                Objects.requireNonNull(getCommand("Priest")).setExecutor(new Priest());
                Objects.requireNonNull(getCommand("Ranger")).setExecutor(new Ranger());
                Objects.requireNonNull(getCommand("Rogue")).setExecutor(new Rogue());
                Objects.requireNonNull(getCommand("Shieldman")).setExecutor(new Shieldman());
                Objects.requireNonNull(getCommand("Skullcrusher")).setExecutor(new Skullcrusher());
                Objects.requireNonNull(getCommand("Skirmisher")).setExecutor(new Skirmisher());
                Objects.requireNonNull(getCommand("Spearman")).setExecutor(new Spearman());
                Objects.requireNonNull(getCommand("Spearknight")).setExecutor(new SpearKnight());
                Objects.requireNonNull(getCommand("Sorcerer")).setExecutor(new Sorcerer());
                Objects.requireNonNull(getCommand("Swordsman")).setExecutor(new Swordsman());
                Objects.requireNonNull(getCommand("Vanguard")).setExecutor(new Vanguard());
                Objects.requireNonNull(getCommand("Vampire")).setExecutor(new Vampire());
                Objects.requireNonNull(getCommand("Viking")).setExecutor(new Viking());
                Objects.requireNonNull(getCommand("Warbear")).setExecutor(new Warbear());
                Objects.requireNonNull(getCommand("Warhound")).setExecutor(new Warhound());
                Objects.requireNonNull(getCommand("Werewolf")).setExecutor(new Werewolf());
                Objects.requireNonNull(getCommand("Warlock")).setExecutor(new Warlock());
                applyKitLimits();

                //Helm's Deep
                registerListener(new WallEvent());
                // Hommet
                registerListener(new CollapseEvent());

                //CavesBoat boatEvent = new CavesBoat();
                //registerListener(boatEvent, plugin);
                //getServer().getScheduler().runTaskTimer(plugin, boatEvent, 300, 300);
                //boatEvent.spawnBoat();

                startTimed();

                // Boosters
                activateBoosters();

                // Begin the map loop
                MapController.startLoop();

                // Register the secret items
                SecretItems.registerSecretItems();

                getLogger().info("Plugin has been enabled!");
                hasLoaded = true;

            }
        }.runTaskLater(plugin, 1);
    }

    private void startTimed() {
        // Timed
        timedTasks.add(new Tips().runTaskTimer(plugin, Tips.TIME_BETWEEN_TIPS * 20L, Tips.TIME_BETWEEN_TIPS * 20L));
        timedTasks.add(Bukkit.getServer().getScheduler().runTaskTimer(plugin, new BarCooldown(), 0, 1));
        timedTasks.add(Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Scoreboard(), 0, 5));
        timedTasks.add(Bukkit.getServer().getScheduler().runTaskTimer(plugin, new ApplyRegeneration(), 0, 75));
        timedTasks.add(Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Hunger(), 0, 20));
        timedTasks.add(Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(plugin, new KeepAlive(), 0, 5900));
    }

    /**
     * Called when the plugin is disabled
     */
    @Override
    public void onDisable() {
        getLogger().info("Disabling plugin...");
        hasLoaded = false;
        mapsLoaded = 0;
        MapController.hasStarted = false;

        // Move all players to their original world
        World defaultWorld = getServer().getWorlds().getFirst();
        for (UUID uuid : TeamController.getEveryone()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                player.teleport(defaultWorld.getSpawnLocation());
            }
            TeamController.leaveTeam(uuid);
            TeamController.leaveSpectator(uuid);
        }

        // Stop CS stuff
        UltimateAdvancementAPI.getInstance(plugin).unregisterPluginAdvancementTabs();
        disableWorlds();

        // Unregister all listeners
        listeners.forEach(this::unregisterListener);
        listeners.clear();
        HandlerList.unregisterAll(plugin);

        try {
            SQL.disconnect();
        } catch (NullPointerException | SQLException ex) {
            getLogger().warning("SQL could not disconnect, it doesn't exist!");
        }

        getLogger().info("Plugin has been disabled!");
    }

    private void disableWorlds() {
        MapController.hasStarted = false;
        mapsLoaded = 0;

        // Save data and end the map if it's not already
        StoreData.storeAll();
        MapController.forceEndMap();

        // Turn off the scoreboard
        Scoreboard.clearScoreboard();
        scoreboardLibrary.close();
        scoreboardLibrary = null;

        // Unload all maps
        for (World world : Bukkit.getWorlds()) {
            if (new File(Bukkit.getWorldContainer(), world.getName() + "_save").exists()) {
                Bukkit.unloadWorld(world, false);
                try {
                    FileUtils.forceDelete(new File(Bukkit.getWorldContainer(), world.getName()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        MapController.resetMapsInRotation();

        // Stop timers
        for (BukkitTask task : timedTasks) {
            task.cancel();
        }
        timedTasks = new ArrayList<>();
    }

    /**
     * Reloads the plugin
     */
    public void reload() {
        Messenger.broadcast(Component.text("[CastleSiege] ", DARK_AQUA)
                .append(Component.text("Reloading plugin...", GOLD)));

        Set<UUID> players = new HashSet<>(TeamController.getPlayers());
        Set<UUID> spectators = new HashSet<>(TeamController.getSpectators());
        onDisable();
        Messenger.broadcast(Component.text("[CastleSiege] ", DARK_AQUA)
                .append(Component.text("Plugin Disabled!", GOLD)));


        new BukkitRunnable() {
            @Override
            public void run() {
                Messenger.broadcast(Component.text("[CastleSiege] ", DARK_AQUA)
                        .append(Component.text("Loading Plugin... (Prepare for a lag spike)", GOLD)));
                onEnable();
            }
        }.runTaskLater(Main.plugin, 100);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!hasLoaded)
                    return;
                this.cancel();

                for(UUID uuid : players) {
                    Player player = Bukkit.getPlayer(uuid);
                    if (player != null) {
                        PlayerConnect.onPlayerReload(player);
                    }
                }

                for(UUID uuid : spectators) {
                    Player spectator = Bukkit.getPlayer(uuid);
                    if (spectator != null) {
                        PlayerConnect.onPlayerReload(spectator);
                        TeamController.joinSpectator(spectator);
                    }
                }

                Messenger.broadcast(Component.text("[CastleSiege] ",DARK_AQUA)
                        .append(Component.text("Plugin Reloaded!", GOLD)));
            }
        }.runTaskTimer(Main.plugin, 120, 20);
    }

    public void reloadMaps() {
        Messenger.broadcast(Component.text("[CastleSiege] ", DARK_AQUA)
                .append(Component.text("Reloading maps...", GOLD)));
        mapsLoaded = 0;

        // Save players to re-add once loaded
        Set<UUID> players = new HashSet<>(TeamController.getPlayers());
        Set<UUID> spectators = new HashSet<>(TeamController.getSpectators());

        // Move all players to their original world
        World defaultWorld = getServer().getWorld("ShopDraft");
        for (UUID uuid : TeamController.getEveryone()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                player.teleport(defaultWorld.getSpawnLocation());
            }
            TeamController.leaveTeam(uuid);
            TeamController.leaveSpectator(uuid);
        }

        disableWorlds();


        Messenger.broadcast(Component.text("[CastleSiege] ", DARK_AQUA)
                .append(Component.text("Loading new map rotation... (Lag spike incoming)", GOLD)));

        getLogger().info("Loading configuration files...");
        createConfigs();
        getLogger().info("Loading maps from configuration...");
        loadMaps(20);
        getLogger().info("Loading game configuration...");
        loadConfig();

        //Scoreboard
        try {
            assert plugin != null;
            scoreboardLibrary = ScoreboardLibrary.loadScoreboardLibrary(plugin);
        } catch (NoPacketAdapterAvailableException e) {
            // If no packet adapter was found, you can fall back to the no-op implementation:
            scoreboardLibrary = new NoopScoreboardLibrary();
            plugin.getLogger().warning("No scoreboard packet adapter available!");
        }

        new BukkitRunnable() {
            /**
             * Runs this operation.
             */
            @Override
            public void run() {
                if (mapsLoaded < mapConfigs.length)
                    return;
                this.cancel();

                startTimed();

                // Begin the map loop
                MapController.startLoop();

                // Register the secret items
                SecretItems.registerSecretItems();

                for(UUID uuid : players) {
                    Player player = Bukkit.getPlayer(uuid);
                    if (player != null) {
                        PlayerConnect.onPlayerReload(player);
                    }
                }

                for(UUID uuid : spectators) {
                    Player spectator = Bukkit.getPlayer(uuid);
                    if (spectator != null) {
                        PlayerConnect.onPlayerReload(spectator);
                        TeamController.joinSpectator(spectator);
                    }
                }

                Messenger.broadcast(Component.text("[CastleSiege] ",DARK_AQUA)
                        .append(Component.text("Maps Reloaded!", GOLD)));
            }
        }.runTaskTimer(Main.plugin, 20L * mapConfigs.length, 20);
    }

    private void registerListener(Listener listener) {
        listeners.add(listener);
        getServer().getPluginManager().registerEvents(listener, plugin);
    }

    private void unregisterListener(Listener listener) {
        HandlerList.unregisterAll(listener);
    }

    /**
     * Creates/loads a world
     * @param worldName The name of the world to load
     */
    private void createWorld(String worldName) {

        // Load world from save (if it exists)
        try {
            File savePath = new File(Bukkit.getWorldContainer(), worldName + "_save");
            if (savePath.exists())
                FileUtils.copyDirectory(savePath,
                        new File(Bukkit.getWorldContainer(), worldName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Load the world into memory
        WorldCreator worldCreator = new WorldCreator(worldName);
        worldCreator.generateStructures(false);
        worldCreator.keepSpawnLoaded(TriState.FALSE);
        World world = worldCreator.createWorld();
        assert world != null;
        world.setGameRule(GameRule.KEEP_INVENTORY, true);
        world.setGameRule(GameRule.DO_MOB_LOOT, false);
        world.setGameRule(GameRule.DO_TILE_DROPS, false);
        world.setAutoSave(false);
    }

    /**
     * Connects to the SQL database
     */
    private void sqlConnect() throws SQLException {
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
        } catch (SQLException e) {
            getLogger().warning("<!> Database is not connected! <!>");
        }

        if (SQL.isConnected()) {
            getLogger().info("<!> Database is connected! <!>");
            this.getServer().getPluginManager().registerEvents(this, this);
        }

        // Load bounties
        BountyCommand.loadBounties();
    }

    /**
     * @param flagPath The flag to find the config for
     * @return The YamlDocument containing the flag's config
     */
    public YamlDocument getFlagsConfig(Route flagPath) {
        for (YamlDocument document : flagsConfigs) {
            if (document.contains(flagPath)) {
                return document;
            }
        }
        return null;
    }

    /**
     * @param cataPath The catapult to find the config for
     * @return The YamlDocument containing the catapult's config
     */
    public YamlDocument getCatapultsConfig(Route cataPath) {
        for (YamlDocument document : catapultsConfigs) {
            if (document.contains(cataPath)) {
                return document;
            }
        }
        return null;
    }

    /**
     * @param cannonPath The cannon to find the config for
     * @return The YamlDocument containing the cannon's config
     */
    public YamlDocument getCannonConfig(Route cannonPath) {
        for (YamlDocument document : cannonConfigs) {
            if (document.contains(cannonPath)) {
                return document;
            }
        }
        return null;
    }

    /**
     * @param corePath The core to find the config for
     * @return The YamlDocument containing the core's config
     */
    public YamlDocument getCoreConfig(Route corePath) {
        for (YamlDocument document : coreConfigs) {
            if (document.contains(corePath)) {
                return document;
            }
        }
        return null;
    }

    /**
     * @param mapPath The door to find the config for
     * @return The YamlDocument containing the door's config
     */
    public YamlDocument getDoorsConfig(Route mapPath) {
        for (YamlDocument document : doorsConfigs) {
            if (document.contains(mapPath)) {
                return document;
            }
        }
        return null;
    }

    /**
     * @param gatePath The gate to find the config for
     * @return The YamlDocument containing the gate's config
     */
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

        // Set up the vector adapter
        TypeAdapter<Vector> vectorAdapter = new TypeAdapter<>() {
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

        // Set up the frame adapters
        TypeAdapter<LocationFrame> locationFrameTypeAdapter = new TypeAdapter<>() {

            @NotNull
            public java.util.Map<Object, Object> serialize(@NotNull LocationFrame object) {
                java.util.Map<Object, Object> map = new HashMap<>();
                map.put("primary_blocks", object.primary_blocks);
                map.put("secondary_blocks", object.secondary_blocks);
                map.put("air", object.air);
                return map;
            }

            @NotNull
            public LocationFrame deserialize(@NotNull java.util.Map<Object, Object> map) {
                LocationFrame frame = new LocationFrame();
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
        StandardSerializer.getDefault().register(LocationFrame.class, locationFrameTypeAdapter);

        flagsConfigs = loadYMLs("flags");
        getLogger().info("Loaded flags");

        doorsConfigs = loadYMLs("doors");
        getLogger().info("Loaded doors");

        gatesConfigs = loadYMLs("gates");
        getLogger().info("Loaded gates");

        coreConfigs = loadYMLs("cores");
        getLogger().info("Loaded cores");

        catapultsConfigs = loadYMLs("catapults");
        getLogger().info("Loaded catapults");

        cannonConfigs = loadYMLs("cannons");
        getLogger().info("Loaded cannons");

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
    }

    private YamlDocument[] loadYMLs(String folderName) {
        // Add all config ymls from the doors folder
        File directoryPath = new File(String.valueOf(getDataFolder()), folderName);
        // List of all files and directories
        String[] contents = directoryPath.list();
        assert contents != null;
        List<YamlDocument> configs = new ArrayList<>();
        for (String content : contents) {
            if (content.endsWith(".yml")) {
                // Load the yml with BoostedYAML
                try {
                    configs.add(YamlDocument.create(new File(directoryPath.getPath(), content),
                            getClass().getResourceAsStream(content)));
                    getLogger().info("Loaded YML: " + directoryPath.getPath() + "/" +  content);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return configs.toArray(new YamlDocument[0]);
    }

    /**
     * Sets values in MapController from config.yml
     * Anything missing is set to the defaults supplied in MapController
     */
    private void loadConfig() {
        // Map Count
        MapController.mapCount = gameConfig.getInt(Route.from("map_count"), MapController.mapCount);

        // Coin multiplier
        CSPlayerData.setCoinMultiplier(gameConfig.getDouble(Route.from("coin_multiplier"), 1.0));

        // All Kits are FREE
        MapController.allKitsFree = gameConfig.getBoolean(Route.from("free_kits"), MapController.allKitsFree);

        // Forced Random
        MapController.forcedRandom = gameConfig.getBoolean(Route.from("forced_random"), MapController.forcedRandom);

        // Delays
        Route route = Route.from("delays");
        MapController.preGameTime = gameConfig.getInt(route.add("game"), MapController.preGameTime);
        MapController.lobbyLockedTime = gameConfig.getInt(route.add("lobby"), MapController.lobbyLockedTime);
        MapController.explorationTime = gameConfig.getInt(route.add("exploration"), MapController.explorationTime);

        // Match Details
        route = Route.from("match");
        MapController.isMatch = gameConfig.getBoolean(route.add("enabled"), MapController.isMatch);
        if (MapController.isMatch) {
            TeamController.keepTeams = gameConfig.getBoolean(route.add("keep_teams"), TeamController.keepTeams);
            TeamController.disableSwitching = gameConfig.getBoolean(route.add("disable_switching"), TeamController.disableSwitching);
            if (gameConfig.contains(route.add("maps"))) {
                MapController.setMaps(gameConfig.getStringList(route.add("maps")));
                if (gameConfig.getBoolean(route.add("shuffle_maps"), false)) {
                    MapController.shuffle();
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
                    Main.plugin.getLogger().warning("Could not find the kit for a limit: " + path);
                else
                    kit.setLimit(gameConfig.getInt(route.add(path)));
            }
        }
    }

    /**
     * Loads all the maps we *could* play from the configs with their worlds
     * @param delay The delay between loading each world
     */
    private void loadMaps(int delay) {
        // Load the maps
        for (int i = 0; i < mapConfigs.length; i++) {
            YamlDocument config = mapConfigs[i];
            if (delay > 0) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        loadMap(config);
                    }
                }.runTaskLater(this, (long) delay * i);
            }
            else {
                loadMap(config);
            }
        }

        // Misc worlds
        createWorld("DuelsMap");
        createWorld("ShopDraft");

        mapsLoaded++;
    }

    private void loadMap(YamlDocument config) {
        // Probably shouldn't do this, but it makes it easier
        if (config == null)
            return;
        String[] mapPaths = getPaths(config, null);

        for (String mapPath : mapPaths) {
            // Basic Map Details
            Map map = new Map();
            Route mapRoute = Route.from(mapPath);
            if (getCoreConfig(mapRoute) != null) {
                map = new CoreMap();
            }
            map.name = config.getString(mapRoute.add("name"));
            map.worldName = config.getString(mapRoute.add("world"));
            map.gamemode = Gamemode.valueOf(config.getString(mapRoute.add("gamemode")));
            map.startTime = config.getInt(mapRoute.add("start_time"), 0);
            map.daylightCycle = config.getBoolean(mapRoute.add("doDaylightCycle"), true);
            map.canRecap = config.getBoolean(mapRoute.add("can_recap"), true);

            // Map Border
            Route borderRoute = mapRoute.add("map_border");
            if (config.contains(borderRoute)) {
                map.setMapBorder(config.getDouble(borderRoute.add("north")),
                        config.getDouble(borderRoute.add("east")),
                        config.getDouble(borderRoute.add("south")),
                        config.getDouble(borderRoute.add("west")));
            }


            // World Data
            createWorld(map.worldName);
            //Core Data
            if (getCoreConfig(mapRoute) != null) {
                loadCores(mapRoute, map);
            }

            // Flag Data
            if (getFlagsConfig(mapRoute) != null) {
                loadFlags(mapRoute, map);
            }

            // Doors
            loadDoors(mapRoute, map);
            // Gates
            loadGates(mapRoute, map);
            // Catapults
            loadCatapults(mapRoute, map);
            //cannons
            loadCannons(mapRoute, map);

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
            MapController.addMapToRotation(map);
        }

        mapsLoaded++;
    }

    private void loadCores(Route mapRoute, Map map) {
        YamlDocument coreConfig = getCoreConfig(mapRoute);
        String[] corePaths = getPaths(coreConfig, mapRoute);

            // Create new core map
        ((CoreMap) map).setCores(new Core[corePaths.length]);
            for (int i = 0; i < corePaths.length; i++) {
                Route coreRoute = mapRoute.add(corePaths[i]);
                double coreHealth = coreConfig.getDouble(coreRoute.add("core_health"));
                Core core;
                core = new Core(coreConfig.getString(coreRoute.add("name")),
                        coreConfig.getString(coreRoute.add("owners")), coreHealth);
                // Set the spawn point
                core.setSpawnPoint(getLocation(coreRoute.add("spawn_point"), map.worldName, coreConfig));
                // Set the capture area and animations
                Route captureRoute = coreRoute.add("core_area");
                if (coreConfig.contains(captureRoute) && coreConfig.getString(captureRoute.add("type")).equalsIgnoreCase("cuboid")) {
                    core.region = getRegion(coreConfig, captureRoute, core.name.replace(' ', '_'));
                }
                core.materials = coreConfig.getStringList(coreRoute.add("materials"));

                ((CoreMap) map).setCore(i, core);
            }
    }

    private void loadFlags(Route mapRoute, Map map) {
        YamlDocument flagConfig = getFlagsConfig(mapRoute);
        String[] flagPaths = getPaths(flagConfig, mapRoute);

        map.flags = new Flag[flagPaths.length];
        for (int i = 0; i < flagPaths.length; i++) {
            // Create the flag
            Route flagRoute = mapRoute.add(flagPaths[i]);
            int maxCap = flagConfig.getInt(flagRoute.add("max_cap"));
            Flag flag = switch (map.gamemode) {
                case Charge -> new ChargeFlag(
                        flagConfig.getString(flagRoute.add("name")),
                        flagConfig.getBoolean(flagRoute.add("secret"), false),
                        flagConfig.getString(flagRoute.add("start_owners")),
                        maxCap,
                        flagConfig.getInt(flagRoute.add("progress_amount")),
                        flagConfig.getInt(flagRoute.add("start_amount"), maxCap)
                ).setChargeValues(
                        getLocation(flagRoute.add("attackers_spawn_point"), map.worldName, flagConfig),
                        getLocation(flagRoute.add("defenders_spawn_point"), map.worldName, flagConfig),
                        flagConfig.getInt(flagRoute.add("add_mins"), 0),
                        flagConfig.getInt(flagRoute.add("add_secs"), 0)
                );
                case Assault -> new AssaultFlag(
                        flagConfig.getString(flagRoute.add("name")),
                        flagConfig.getBoolean(flagRoute.add("secret"), false),
                        flagConfig.getString(flagRoute.add("start_owners")),
                        maxCap,
                        flagConfig.getInt(flagRoute.add("progress_amount")),
                        flagConfig.getInt(flagRoute.add("start_amount"), maxCap),
                        flagConfig.getInt(flagRoute.add("additional_lives"), -1)
                ).setSpawns(
                        getLocation(flagRoute.add("attackers_spawn_point"), map.worldName, flagConfig),
                        getLocation(flagRoute.add("defenders_spawn_point"), map.worldName, flagConfig)
                );
                default -> new Flag(
                        flagConfig.getString(flagRoute.add("name")),
                        flagConfig.getBoolean(flagRoute.add("secret"), false),
                        flagConfig.getString(flagRoute.add("start_owners")),
                        maxCap,
                        flagConfig.getInt(flagRoute.add("progress_amount")),
                        flagConfig.getInt(flagRoute.add("start_amount"), maxCap)
                );
            };

            // Set the spawn point
            flag.setSpawnPoint(getLocation(flagRoute.add("spawn_point"), map.worldName, flagConfig));

            //Hologram Location
            flag.holoLoc = getLocation(flagRoute.add("hologram_location"), map.worldName, flagConfig);

            // Set the capture area and animations
            Route captureRoute = flagRoute.add("capture_area");
            if (flagConfig.contains(captureRoute)
                    && flagConfig.getString(captureRoute.add("type")).equalsIgnoreCase("cuboid")) {
                flag.region = getRegion(flagConfig, captureRoute, flag.getName().replace(' ', '_'));

                // Use block animation
                if (!flagConfig.getBoolean(flagRoute.add("use_schematics"), false)) {
                    flag.animationAir = flagConfig.getBoolean(flagRoute.add("animation_air"));
                    Route animationRoute = flagRoute.add("animation");
                    String[] animationPaths = getPaths(flagConfig, animationRoute);

                    flag.blockAnimation = new LocationFrame[animationPaths.length];
                    for (int j = 0; j < animationPaths.length; j++) {
                        LocationFrame locationFrame = flagConfig.getAs(animationRoute.add(animationPaths[j]), LocationFrame.class);
                        flag.blockAnimation[j] = locationFrame;
                    }
                // Use schematic animation
                } else {
                    // inside animation:
                    Route animationRoute = flagRoute.add("animation");
                    flag.useSchematics = true;
                    String[] teamFrames = getPaths(flagConfig, animationRoute);
                    flag.schematicAnimation = new HashMap<>();

                    for (String teamFrame : teamFrames) {
                        // Inside a team's animation
                        Route teamRoute = animationRoute.add(teamFrame);
                        Route framesRoute = teamRoute.add("frames");
                        String[] framePaths = getPaths(flagConfig, framesRoute);
                        SchematicFrame[] frames = new SchematicFrame[framePaths.length];

                        for (int j = 0; j < framePaths.length; j++) {
                            // Each frame
                            Route frameRoute = framesRoute.add(framePaths[j]);
                            String[] schemPaths = getPaths(flagConfig, frameRoute);
                            SchematicFrame frame = new SchematicFrame();

                            for (String schemPath : schemPaths) {
                                // Each schematic
                                Route schemRoute = frameRoute.add(schemPath);
                                Vector vector = new Vector();
                                vector.setX(flagConfig.getInt(schemRoute.add("x")));
                                vector.setY(flagConfig.getInt(schemRoute.add("y")));
                                vector.setZ(flagConfig.getInt(schemRoute.add("z")));
                                frame.schematics.add(new Tuple<>(flagConfig.getString(schemRoute.add("name")), vector));
                            }

                            frames[j] = frame;
                        }

                        flag.schematicAnimation.put(flagConfig.getString(teamRoute.add("team_name")), frames);
                    }
                }
            }

            map.flags[i] = flag;
        }
    }

    private Team loadTeam(Route teamPath, Map map, YamlDocument config) {
        String name = config.getString(teamPath.add("name"));
        Team team = new Team(name);

        // Colours
        Tuple<Material, NamedTextColor> colors = getColors(Objects.requireNonNull(config.getString(teamPath.add("primary_color")).toLowerCase()));
        team.primaryWool = colors.getFirst();
        team.primaryChatColor = colors.getSecond();
        colors = getColors(Objects.requireNonNull(config.getString(teamPath.add("secondary_color")).toLowerCase()));
        team.secondaryWool = colors.getFirst();
        team.secondaryChatColor = colors.getSecond();

        team.setLives(config.getInt(teamPath.add("lives"), -1));

        // Add team/map kits
        if (config.contains(teamPath.add("kits"))) {
            List<java.util.Map<?, ?>> kits = config.getMapList(teamPath.add("kits"));
            for (java.util.Map<?, ?> kit : kits) {
                String kitName = (String) kit.get("name");
                String cost = (String) kit.get("cost");
                if (cost == null || (!cost.equals("coins") && !cost.equals("free")))
                    getLogger().warning(kitName + " has an invalid cost for " + team.getDisplayName());
                else
                    team.kits.put(kitName.toLowerCase(), SignKit.CostType.valueOf(cost.toLowerCase()));
            }
        }

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
            if (map instanceof CoreMap) {
                block.coreName = config.getString(mapFlagRoute.add("core_name"));
            }

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
            int timer;
            Door door;
            switch (doorConfig.getString(doorRoute.add("trigger"), "plate").toLowerCase()) {
                case "switch":
                case "lever":
                    sounds = new Tuple<>(Sound.valueOf(doorConfig.getString(doorRoute.add("closed_sound"), LeverDoor.defaultClosedSound)),
                            Sound.valueOf(doorConfig.getString(doorRoute.add("open_sound"), LeverDoor.defaultOpenSound)));
                    Location leverPos = getLocation(doorRoute.add("lever_position"), map.worldName, doorConfig);
                    timer = doorConfig.getInt(doorRoute.add("timer"), LeverDoor.defaultTimer) * 20;
                    door = new LeverDoor(flagName, centre, schematicNames, sounds, timer, leverPos);
                    break;
                case "button":
                    sounds = new Tuple<>(Sound.valueOf(doorConfig.getString(doorRoute.add("closed_sound"), ButtonDoor.defaultClosedSound)),
                            Sound.valueOf(doorConfig.getString(doorRoute.add("open_sound"), ButtonDoor.defaultOpenSound)));
                    timer = doorConfig.getInt(doorRoute.add("timer"), ButtonDoor.defaultTimer) * 20;
                    String[] buttonPaths = getPaths(doorConfig, doorRoute.add("buttons"));
                    Tuple<Location, Integer>[] buttonData = new Tuple[buttonPaths.length];
                    for (int j = 0; j < buttonPaths.length; j++) {
                        Route buttonRoute = doorRoute.add("buttons").add(buttonPaths[j]);
                        Location position = getLocation(buttonRoute.add("position"), map.worldName, doorConfig);
                        int delay = doorConfig.getInt(buttonRoute.add("delay"), ButtonDoor.defaultDelay) * 20;
                        buttonData[j] = new Tuple<>(position, delay);
                    }
                    door = new ButtonDoor(flagName, centre, schematicNames, sounds, timer, buttonData);
                    break;
                case "pressureplate":
                case "pressure plate":
                case "plate":
                case "pressure":
                default:
                    sounds = new Tuple<>(Sound.valueOf(doorConfig.getString(doorRoute.add("closed_sound"), PressurePlateDoor.defaultClosedSound)),
                            Sound.valueOf(doorConfig.getString(doorRoute.add("open_sound"), PressurePlateDoor.defaultOpenSound)));
                    timer = doorConfig.getInt(doorRoute.add("timer"), PressurePlateDoor.defaultTimer) * 20;
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

    private void loadCannons(Route mapRoute, Map map) {
        YamlDocument cannonConfig = getCannonConfig(mapRoute);
        if (!Objects.nonNull(cannonConfig)) {
            map.cannons = new Cannon[0];
            return;
        }

        String[] cannonPaths = getPaths(cannonConfig, mapRoute);

        map.cannons = new Cannon[cannonPaths.length];

        for (int i = 0; i < cannonPaths.length; i++) {

            Route cannonRoute = mapRoute.add(cannonPaths[i]);
            String direction = cannonConfig.getString(cannonRoute.add("direction"));
            Location location = getLocation(cannonRoute.add("location"), map.worldName, cannonConfig);
            double horizontal = cannonConfig.getDouble(cannonRoute.add("yaw"));
            double vertical = cannonConfig.getDouble(cannonRoute.add("power"));

            Cannon cannon = new Cannon(direction, location, horizontal, vertical);
            map.cannons[i] = cannon;
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

    /**
     * Gets a String array of all the paths available at a route
     * @param file The YamlDocument to get the paths from
     * @param route The route in the document to check
     * @return a String array of all paths at the route
     */
    private String[] getPaths(YamlDocument file, Route route) {
        Set<String> set;
        if (route == null) {
            set = file.getRoutesAsStrings(false);
        } else if (file.getOptionalSection(route).isEmpty()) {
            return new String[0];
        } else {
            set = file.getSection(route).getRoutesAsStrings(false);
        }
        String[] paths = new String[set.size()];
        int index = 0;
        for (String str : set) {
            paths[index++] = str;
        }
        return paths;
    }

    private Location getLocation(Route locationPath, String worldName, YamlDocument config) {
        if (!config.contains(locationPath)) {
            return null;
        }

        int x = config.getInt(locationPath.add("x"));
        int y = config.getInt(locationPath.add("y"));
        int z = config.getInt(locationPath.add("z"));
        int yaw = config.getInt(locationPath.add("yaw"), 90);
        int pitch = config.getInt(locationPath.add("pitch"), 0);
        return new Location(Bukkit.getServer().getWorld(worldName), x, y, z, yaw, pitch);
    }

    private Tuple<Material, NamedTextColor> getColors(String color) {
        Tuple<Material, NamedTextColor> colors = new Tuple<>(Material.WHITE_WOOL, NamedTextColor.WHITE);
        switch (color) {
            case "black":
                colors.setFirst(Material.BLACK_WOOL);
                colors.setSecond(NamedTextColor.DARK_GRAY);
                break;
            case "dark_blue":
            case "darkblue":
                colors.setFirst(Material.BLUE_WOOL);
                colors.setSecond(NamedTextColor.DARK_BLUE);
                break;
            case "dark_green":
            case "darkgreen":
            case "green":
                colors.setFirst(Material.GREEN_WOOL);
                colors.setSecond(NamedTextColor.DARK_GREEN);
                break;
            case "cyan":
            case "dark_aqua":
            case "darkaqua":
                colors.setFirst(Material.CYAN_WOOL);
                colors.setSecond(NamedTextColor.DARK_AQUA);
                break;
            case "dark_red":
            case "darkred":
            case "red":
                colors.setFirst(Material.RED_WOOL);
                colors.setSecond(NamedTextColor.DARK_RED);
                break;
            case "brown":
                colors.setFirst(Material.BROWN_WOOL);
                colors.setSecond(NamedTextColor.GOLD);
                break;
            case "dark_purple":
            case "darkpurple":
            case "purple":
                colors.setFirst(Material.PURPLE_WOOL);
                colors.setSecond(NamedTextColor.DARK_PURPLE);
                break;
            case "gold":
            case "dark_gold":
            case "darkgold":
            case "dark_yellow":
            case "darkyellow":
            case "orange":
                colors.setFirst(Material.ORANGE_WOOL);
                colors.setSecond(NamedTextColor.GOLD);
                break;
            case "light_gold":
            case "lightgold":
                colors.setFirst(Material.YELLOW_WOOL);
                colors.setSecond(NamedTextColor.GOLD);
                break;
            case "light_gray":
            case "lightgray":
            case "light_grey":
            case "lightgrey":
            case "gray":
            case "grey":
                colors.setFirst(Material.LIGHT_GRAY_WOOL);
                colors.setSecond(NamedTextColor.GRAY);
                break;
            case "dark_grey":
            case "dark_gray":
            case "darkgrey":
            case "darkgray":
                colors.setFirst(Material.GRAY_WOOL);
                colors.setSecond(NamedTextColor.DARK_GRAY);
                break;
            case "blue":
                colors.setFirst(Material.BLUE_WOOL);
                colors.setSecond(NamedTextColor.BLUE);
                break;
            case "light_blue":
            case "lightblue":
                colors.setFirst(Material.LIGHT_BLUE_WOOL);
                colors.setSecond(NamedTextColor.BLUE);
                break;
            case "light_green":
            case "lightgreen":
            case "lime":
                colors.setFirst(Material.LIME_WOOL);
                colors.setSecond(NamedTextColor.GREEN);
                break;
            case "aqua":
                colors.setFirst(Material.LIGHT_BLUE_WOOL);
                colors.setSecond(NamedTextColor.AQUA);
                break;
            case "light_red":
            case "lightred":
                colors.setFirst(Material.PINK_WOOL);
                colors.setSecond(NamedTextColor.RED);
                break;
            case "light_purple":
            case "lightpurple":
            case "magenta":
                colors.setFirst(Material.MAGENTA_WOOL);
                colors.setSecond(NamedTextColor.LIGHT_PURPLE);
                break;
            case "pink":
                colors.setFirst(Material.PINK_WOOL);
                colors.setSecond(NamedTextColor.LIGHT_PURPLE);
                break;
            case "light_yellow":
            case "lightyellow":
            case "yellow":
                colors.setFirst(Material.YELLOW_WOOL);
                colors.setSecond(NamedTextColor.YELLOW);
                break;
            case "white":
            default:
                colors.setFirst(Material.WHITE_WOOL);
                colors.setSecond(NamedTextColor.WHITE);
        }
        return colors;
    }

    /**
     *
     * @param color the chat colour to use to pick boss colour
     * @return the bossbar colour to use
     */
    public static BossBar.Color getBarColour(NamedTextColor color) {
        if (color.equals(BLUE) || color.equals(DARK_AQUA) || color.equals(DARK_BLUE) || color.equals(AQUA)) {
            return BossBar.Color.BLUE;
        } else if (color.equals(GRAY) || color.equals(WHITE)) {
            return BossBar.Color.WHITE;
        } else if (color.equals(DARK_GREEN) || color.equals(GREEN)) {
            return BossBar.Color.GREEN;
        } else if (color.equals(LIGHT_PURPLE)) {
            return BossBar.Color.PINK;
        } else if (color.equals(BLACK) || color.equals(DARK_GRAY) || color.equals(DARK_PURPLE)) {
            return BossBar.Color.PURPLE;
        } else if (color.equals(DARK_RED) || color.equals(RED)) {
            return BossBar.Color.RED;
        } else if (color.equals(YELLOW) || color.equals(GOLD)) {
            return BossBar.Color.YELLOW;
        }
        return null;
    }

    /**
     * Activates all the boosters again that are still active
     */
    public void activateBoosters() {
        try (PreparedStatement ps = Main.SQL.getConnection().prepareStatement(
                "SELECT booster_id, booster_type, expire_time, boost_value FROM active_boosters WHERE expire_time > ?")) {
            ps.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int boostId = rs.getInt("booster_id");
                String type = rs.getString("booster_type");
                long remaining_duration = rs.getTimestamp("expire_time").getTime() - System.currentTimeMillis() / 1000;
                String other = rs.getString("boost_value");
                Booster.updateID(boostId);
                double multiplier;
                int percentage;
                switch (type.toUpperCase()) {
                    case "COIN":
                    case "COINS":
                    case "C":
                        multiplier = Double.parseDouble(other);
                        percentage = (int)(multiplier * 100);
                        Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
                            CSPlayerData.setCoinMultiplier(CSPlayerData.getCoinMultiplier() + multiplier);
                            Messenger.broadcastInfo(" A " + percentage + "% coin booster " +
                                    "for " + getTimeString((int) remaining_duration) + " has been activated!");
                            Messenger.broadcastInfo("The total coin multiplier is now " + CSPlayerData.getCoinMultiplier() + ".");
                        });
                        Bukkit.getScheduler().runTaskLaterAsynchronously(Main.plugin, () -> {
                            CSPlayerData.setCoinMultiplier(CSPlayerData.getCoinMultiplier() - multiplier);
                            Messenger.broadcastWarning("A " + percentage + "% coin booster has expired!");
                            Messenger.broadcastInfo("The total coin multiplier is now " + CSPlayerData.getCoinMultiplier() + ".");
                        }, remaining_duration * 20L);
                        break;
                    case "BATTLEPOINT":
                    case "KIT":
                    case "K":
                        Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
                            CoinKit.boostedKits.add(other);
                            Messenger.broadcastInfo("A " + other + " kit booster " +
                                    "for " + getTimeString((int) remaining_duration) + "!");
                        });
                        Bukkit.getScheduler().runTaskLaterAsynchronously(Main.plugin, () -> {
                            CoinKit.boostedKits.remove(other);
                            Messenger.broadcastWarning("A " + other + " kit booster has expired! ");
                        }, remaining_duration * 20L);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param duration The time in seconds
     * @return The time in string format
     */
    public static String getTimeString(long duration) {
        long days, hours, minutes, seconds, remainder;
        days = duration / 86400;
        remainder = duration % 86400;
        hours = remainder / 3600;
        remainder = remainder % 3600;
        minutes = remainder / 60;
        seconds = remainder % 60;

        if (days == 0) {
            if (hours == 0) {
                if (minutes == 0) {
                    return seconds + "s";
                }
                return minutes + " mins " + seconds + "s";
            }
            return hours + " hr " + minutes + " mins " + seconds + "s";
        }
        return days + "d " + hours + "h " + minutes + "m " + seconds + "s";
    }
}
