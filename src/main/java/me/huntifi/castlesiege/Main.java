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
import me.huntifi.castlesiege.commands.donator.FireworkCommand;
import me.huntifi.castlesiege.commands.donator.JoinMessage;
import me.huntifi.castlesiege.commands.donator.LeaveMessage;
import me.huntifi.castlesiege.commands.donator.duels.AcceptDuel;
import me.huntifi.castlesiege.commands.donator.duels.DuelCommand;
import me.huntifi.castlesiege.commands.gameplay.BoosterCommand;
import me.huntifi.castlesiege.commands.gameplay.BountyCommand;
import me.huntifi.castlesiege.commands.gameplay.BuyKitCommand;
import me.huntifi.castlesiege.commands.gameplay.CoinShopCommand;
import me.huntifi.castlesiege.commands.gameplay.KitCommand;
import me.huntifi.castlesiege.commands.gameplay.RandomKitCommand;
import me.huntifi.castlesiege.commands.gameplay.SettingsCommand;
import me.huntifi.castlesiege.commands.gameplay.SuicideCommand;
import me.huntifi.castlesiege.commands.gameplay.SwitchCommand;
import me.huntifi.castlesiege.commands.gameplay.VoteSkipCommand;
import me.huntifi.castlesiege.commands.info.CoinMultiplier;
import me.huntifi.castlesiege.commands.info.CoinsCommand;
import me.huntifi.castlesiege.commands.info.DiscordCommand;
import me.huntifi.castlesiege.commands.info.MapsCommand;
import me.huntifi.castlesiege.commands.info.MyStatsCommand;
import me.huntifi.castlesiege.commands.info.PingCommand;
import me.huntifi.castlesiege.commands.info.RulesCommand;
import me.huntifi.castlesiege.commands.info.SecretsCommand;
import me.huntifi.castlesiege.commands.info.TeamsCommand;
import me.huntifi.castlesiege.commands.info.WebshopCommand;
import me.huntifi.castlesiege.commands.info.leaderboard.Donators;
import me.huntifi.castlesiege.commands.info.leaderboard.Leaderboard;
import me.huntifi.castlesiege.commands.info.leaderboard.MVPCommand;
import me.huntifi.castlesiege.commands.info.leaderboard.TopMatch;
import me.huntifi.castlesiege.commands.mojang.WhoisCommand;
import me.huntifi.castlesiege.commands.staff.BroadcastCommand;
import me.huntifi.castlesiege.commands.staff.FlyCommand;
import me.huntifi.castlesiege.commands.staff.GiveVoteCommand;
import me.huntifi.castlesiege.commands.staff.ReloadCommand;
import me.huntifi.castlesiege.commands.staff.SetStaffRank;
import me.huntifi.castlesiege.commands.staff.StaffChat;
import me.huntifi.castlesiege.commands.staff.ToggleRankCommand;
import me.huntifi.castlesiege.commands.staff.boosters.GrantBooster;
import me.huntifi.castlesiege.commands.staff.currencies.AddCoins;
import me.huntifi.castlesiege.commands.staff.currencies.SetCoinMultiplier;
import me.huntifi.castlesiege.commands.staff.currencies.SetCoins;
import me.huntifi.castlesiege.commands.staff.currencies.TakeCoins;
import me.huntifi.castlesiege.commands.staff.donations.RankPoints;
import me.huntifi.castlesiege.commands.staff.donations.SetKitCommand;
import me.huntifi.castlesiege.commands.staff.donations.UnlockedKitCommand;
import me.huntifi.castlesiege.commands.staff.maps.NextMapCommand;
import me.huntifi.castlesiege.commands.staff.maps.SetKitLimit;
import me.huntifi.castlesiege.commands.staff.maps.SetMapCommand;
import me.huntifi.castlesiege.commands.staff.maps.SetTimerCommand;
import me.huntifi.castlesiege.commands.staff.maps.SpectateCommand;
import me.huntifi.castlesiege.commands.staff.maps.StartCommand;
import me.huntifi.castlesiege.commands.staff.maps.ToggleAllKitsFree;
import me.huntifi.castlesiege.commands.staff.maps.ToggleForcedRandom;
import me.huntifi.castlesiege.commands.staff.maps.ToggleSwitching;
import me.huntifi.castlesiege.commands.staff.punishments.Ban;
import me.huntifi.castlesiege.commands.staff.punishments.Kick;
import me.huntifi.castlesiege.commands.staff.punishments.KickAll;
import me.huntifi.castlesiege.commands.staff.punishments.Mute;
import me.huntifi.castlesiege.commands.staff.punishments.Unban;
import me.huntifi.castlesiege.commands.staff.punishments.Unmute;
import me.huntifi.castlesiege.commands.staff.punishments.Warn;
import me.huntifi.castlesiege.data_types.Booster;
import me.huntifi.castlesiege.data_types.LocationFrame;
import me.huntifi.castlesiege.data_types.PlayerData;
import me.huntifi.castlesiege.data_types.SchematicFrame;
import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.database.KeepAlive;
import me.huntifi.castlesiege.database.MySQL;
import me.huntifi.castlesiege.database.StoreData;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.events.chat.PlayerChat;
import me.huntifi.castlesiege.events.combat.ArrowCollision;
import me.huntifi.castlesiege.events.combat.ArrowRemoval;
import me.huntifi.castlesiege.events.combat.AssistKill;
import me.huntifi.castlesiege.events.combat.DamageBalance;
import me.huntifi.castlesiege.events.combat.EatCake;
import me.huntifi.castlesiege.events.combat.FallDamage;
import me.huntifi.castlesiege.events.combat.HitMessage;
import me.huntifi.castlesiege.events.combat.HurtAnimation;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.events.combat.LobbyCombat;
import me.huntifi.castlesiege.events.combat.TeamCombat;
import me.huntifi.castlesiege.events.connection.PlayerConnect;
import me.huntifi.castlesiege.events.connection.PlayerDisconnect;
import me.huntifi.castlesiege.events.curses.CurseCommand;
import me.huntifi.castlesiege.events.death.DeathEvent;
import me.huntifi.castlesiege.events.death.VoidLocation;
import me.huntifi.castlesiege.events.gameplay.CamelHandler;
import me.huntifi.castlesiege.events.gameplay.Explosion;
import me.huntifi.castlesiege.events.gameplay.HorseHandler;
import me.huntifi.castlesiege.events.gameplay.LeaveMapBorder;
import me.huntifi.castlesiege.events.gameplay.Movement;
import me.huntifi.castlesiege.events.security.InteractContainer;
import me.huntifi.castlesiege.events.security.InventoryProtection;
import me.huntifi.castlesiege.events.security.MapProtection;
import me.huntifi.castlesiege.events.timed.ApplyRegeneration;
import me.huntifi.castlesiege.events.timed.BarCooldown;
import me.huntifi.castlesiege.events.timed.Hunger;
import me.huntifi.castlesiege.events.timed.Tips;
import me.huntifi.castlesiege.kits.items.Enderchest;
import me.huntifi.castlesiege.kits.items.WoolHat;
import me.huntifi.castlesiege.kits.kits.CoinKit;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.kits.kits.coin_kits.Alchemist;
import me.huntifi.castlesiege.kits.kits.coin_kits.Barbarian;
import me.huntifi.castlesiege.kits.kits.coin_kits.Berserker;
import me.huntifi.castlesiege.kits.kits.coin_kits.Cavalry;
import me.huntifi.castlesiege.kits.kits.coin_kits.Crossbowman;
import me.huntifi.castlesiege.kits.kits.coin_kits.Engineer;
import me.huntifi.castlesiege.kits.kits.coin_kits.Executioner;
import me.huntifi.castlesiege.kits.kits.coin_kits.Halberdier;
import me.huntifi.castlesiege.kits.kits.coin_kits.Maceman;
import me.huntifi.castlesiege.kits.kits.coin_kits.Medic;
import me.huntifi.castlesiege.kits.kits.coin_kits.Paladin;
import me.huntifi.castlesiege.kits.kits.coin_kits.Priest;
import me.huntifi.castlesiege.kits.kits.coin_kits.Ranger;
import me.huntifi.castlesiege.kits.kits.coin_kits.Rogue;
import me.huntifi.castlesiege.kits.kits.coin_kits.Sorcerer;
import me.huntifi.castlesiege.kits.kits.coin_kits.Vanguard;
import me.huntifi.castlesiege.kits.kits.coin_kits.Viking;
import me.huntifi.castlesiege.kits.kits.coin_kits.Warhound;
import me.huntifi.castlesiege.kits.kits.coin_kits.Warlock;
import me.huntifi.castlesiege.kits.kits.free_kits.Archer;
import me.huntifi.castlesiege.kits.kits.free_kits.Shieldman;
import me.huntifi.castlesiege.kits.kits.free_kits.Spearman;
import me.huntifi.castlesiege.kits.kits.free_kits.Swordsman;
import me.huntifi.castlesiege.kits.kits.in_development.Armorer;
import me.huntifi.castlesiege.kits.kits.in_development.Bannerman;
import me.huntifi.castlesiege.kits.kits.level_kits.BattleMedic;
import me.huntifi.castlesiege.kits.kits.level_kits.Hypaspist;
import me.huntifi.castlesiege.kits.kits.level_kits.SpearKnight;
import me.huntifi.castlesiege.kits.kits.map_kits.CamelRider;
import me.huntifi.castlesiege.kits.kits.map_kits.Constructor;
import me.huntifi.castlesiege.kits.kits.staff_kits.Warbear;
import me.huntifi.castlesiege.kits.kits.team_kits.conwy.ConwyArbalester;
import me.huntifi.castlesiege.kits.kits.team_kits.conwy.ConwyLongbowman;
import me.huntifi.castlesiege.kits.kits.team_kits.conwy.ConwyRoyalKnight;
import me.huntifi.castlesiege.kits.kits.team_kits.firelands.FirelandsAbyssal;
import me.huntifi.castlesiege.kits.kits.team_kits.firelands.FirelandsHellsteed;
import me.huntifi.castlesiege.kits.kits.team_kits.helmsdeep.HelmsDeepBerserker;
import me.huntifi.castlesiege.kits.kits.team_kits.helmsdeep.HelmsDeepLancer;
import me.huntifi.castlesiege.kits.kits.team_kits.helmsdeep.HelmsDeepRangedCavalry;
import me.huntifi.castlesiege.kits.kits.team_kits.hommet.HommetAxeman;
import me.huntifi.castlesiege.kits.kits.team_kits.hommet.HommetLongbowarcher;
import me.huntifi.castlesiege.kits.kits.team_kits.moria.MoriaAxeThrower;
import me.huntifi.castlesiege.kits.kits.team_kits.moria.MoriaBonecrusher;
import me.huntifi.castlesiege.kits.kits.team_kits.moria.MoriaCaveTroll;
import me.huntifi.castlesiege.kits.kits.team_kits.moria.MoriaGuardian;
import me.huntifi.castlesiege.kits.kits.team_kits.moria.MoriaOrc;
import me.huntifi.castlesiege.kits.kits.team_kits.moria.MoriaWindlancer;
import me.huntifi.castlesiege.kits.kits.team_kits.royalcrypts.CryptsFallen;
import me.huntifi.castlesiege.kits.kits.team_kits.thunderstone.ThunderstoneElytrier;
import me.huntifi.castlesiege.kits.kits.voter_kits.FireArcher;
import me.huntifi.castlesiege.kits.kits.voter_kits.Ladderman;
import me.huntifi.castlesiege.kits.kits.voter_kits.Scout;
import me.huntifi.castlesiege.kits.kits.voter_kits.Skirmisher;
import me.huntifi.castlesiege.maps.CoreMap;
import me.huntifi.castlesiege.maps.Gamemode;
import me.huntifi.castlesiege.maps.Hommet.CollapseEvent;
import me.huntifi.castlesiege.maps.Lobby;
import me.huntifi.castlesiege.maps.Map;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.NameTag;
import me.huntifi.castlesiege.maps.Scoreboard;
import me.huntifi.castlesiege.maps.Team;
import me.huntifi.castlesiege.maps.WoolMap;
import me.huntifi.castlesiege.maps.WoolMapBlock;
import me.huntifi.castlesiege.maps.helms_deep.CavesBoat;
import me.huntifi.castlesiege.maps.helms_deep.WallEvent;
import me.huntifi.castlesiege.maps.objects.ButtonDoor;
import me.huntifi.castlesiege.maps.objects.Catapult;
import me.huntifi.castlesiege.maps.objects.ChargeFlag;
import me.huntifi.castlesiege.maps.objects.Core;
import me.huntifi.castlesiege.maps.objects.Door;
import me.huntifi.castlesiege.maps.objects.Flag;
import me.huntifi.castlesiege.maps.objects.Gate;
import me.huntifi.castlesiege.maps.objects.LeverDoor;
import me.huntifi.castlesiege.maps.objects.PressurePlateDoor;
import me.huntifi.castlesiege.maps.objects.Ram;
import me.huntifi.castlesiege.maps.objects.RegionHandler;
import me.huntifi.castlesiege.misc.mythic.MythicListener;
import me.huntifi.castlesiege.secrets.Abrakhan.AbrakhanSecretDoor;
import me.huntifi.castlesiege.secrets.Helmsdeep.SecretDoor;
import me.huntifi.castlesiege.secrets.SecretBlocks;
import me.huntifi.castlesiege.secrets.SecretItems;
import me.huntifi.castlesiege.secrets.SecretSigns;
import me.huntifi.castlesiege.secrets.Skyhold.SkyholdDoors;
import me.huntifi.castlesiege.secrets.Thunderstone.SecretPortal;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.megavex.scoreboardlibrary.api.ScoreboardLibrary;
import net.megavex.scoreboardlibrary.api.exception.NoPacketAdapterAvailableException;
import net.megavex.scoreboardlibrary.api.noop.NoopScoreboardLibrary;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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
    private YamlDocument[] coreConfigs;

    private YamlDocument gameConfig;

//    @Override
//    public void onLoad() {
//        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
//        //Are all listeners read only?
//        PacketEvents.getAPI().getSettings().reEncodeByDefault(false)
//                .checkForUpdates(true)
//                .bStats(true);
//        PacketEvents.getAPI().load();
//    }


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
                sessionManager.registerHandler(RegionHandler.FACTORY, null);

                //Scoreboard
                //PacketEvents.getAPI().getEventManager().registerListener(new PacketEventsListener());
                //PacketEvents.getAPI().init();
                try {
                    assert plugin != null;
                    scoreboardLibrary = ScoreboardLibrary.loadScoreboardLibrary(plugin);
                } catch (NoPacketAdapterAvailableException e) {
                    // If no packet adapter was found, you can fallback to the no-op implementation:
                    scoreboardLibrary = new NoopScoreboardLibrary();
                    plugin.getLogger().warning("No scoreboard packet adapter available!");
                }

                // Tips
                new Tips().runTaskTimer(plugin, Tips.TIME_BETWEEN_TIPS * 20L, Tips.TIME_BETWEEN_TIPS * 20L);

                // Rewrite Events
                getServer().getPluginManager().registerEvents(new Enderchest(), plugin);
                getServer().getPluginManager().registerEvents(new PlayerChat(), plugin);
                getServer().getPluginManager().registerEvents(new BoosterCommand(), plugin);
                getServer().getPluginManager().registerEvents(new TeamChat(), plugin);

                // Connection
                getServer().getPluginManager().registerEvents(new PlayerConnect(), plugin);
                getServer().getPluginManager().registerEvents(new PlayerDisconnect(), plugin);

                //Secrets
                getServer().getPluginManager().registerEvents(new AbrakhanSecretDoor(), plugin);
                getServer().getPluginManager().registerEvents(new SecretDoor(), plugin);
                getServer().getPluginManager().registerEvents(new SecretItems(), plugin);
                getServer().getPluginManager().registerEvents(new SecretSigns(), plugin);
                getServer().getPluginManager().registerEvents(new SecretBlocks(), plugin);
                getServer().getPluginManager().registerEvents(new SecretPortal(), plugin);
                getServer().getPluginManager().registerEvents(new SkyholdDoors(), plugin);
                getServer().getPluginManager().registerEvents(new AbrakhanSecretDoor(), plugin);

                //Duels
                getServer().getPluginManager().registerEvents(new AcceptDuel(), plugin);

                // Combat
                getServer().getPluginManager().registerEvents(new ArrowCollision(), plugin);
                getServer().getPluginManager().registerEvents(new ArrowRemoval(), plugin);
                getServer().getPluginManager().registerEvents(new AssistKill(), plugin);
                getServer().getPluginManager().registerEvents(new DamageBalance(), plugin);
                getServer().getPluginManager().registerEvents(new EatCake(), plugin);
                getServer().getPluginManager().registerEvents(new FallDamage(), plugin);
                getServer().getPluginManager().registerEvents(new HitMessage(), plugin);
                getServer().getPluginManager().registerEvents(new HurtAnimation(), plugin);
                getServer().getPluginManager().registerEvents(new InCombat(), plugin);
                getServer().getPluginManager().registerEvents(new LobbyCombat(), plugin);
                getServer().getPluginManager().registerEvents(new TeamCombat(), plugin);

                // Death
                getServer().getPluginManager().registerEvents(new DeathEvent(), plugin);
                getServer().getPluginManager().registerEvents(new VoidLocation(), plugin);

                // Gameplay
                //getServer().getPluginManager().registerEvents(new ArcaneTower(), plugin);
                getServer().getPluginManager().registerEvents(new CollapseEvent(), plugin);
                getServer().getPluginManager().registerEvents(new Explosion(), plugin);
                getServer().getPluginManager().registerEvents(new HorseHandler(), plugin);
                getServer().getPluginManager().registerEvents(new CamelHandler(), plugin);
                getServer().getPluginManager().registerEvents(new LeaveMapBorder(), plugin);
                getServer().getPluginManager().registerEvents(new Movement(), plugin);

                // Security
                getServer().getPluginManager().registerEvents(new InteractContainer(), plugin);
                getServer().getPluginManager().registerEvents(new InventoryProtection(), plugin);
                getServer().getPluginManager().registerEvents(new MapProtection(), plugin);

                // Kits
                getServer().getPluginManager().registerEvents(new Alchemist(), plugin);
                getServer().getPluginManager().registerEvents(new Armorer(), plugin);
                getServer().getPluginManager().registerEvents(new Berserker(), plugin);
                getServer().getPluginManager().registerEvents(new Barbarian(), plugin);
                getServer().getPluginManager().registerEvents(new Bannerman(), plugin);
                getServer().getPluginManager().registerEvents(new BattleMedic(), plugin);
                getServer().getPluginManager().registerEvents(new Crossbowman(), plugin);
                getServer().getPluginManager().registerEvents(new Cavalry(), plugin);
                getServer().getPluginManager().registerEvents(new CamelRider(), plugin);
                getServer().getPluginManager().registerEvents(new Constructor(), plugin);
                //getServer().getPluginManager().registerEvents(new Chef(), plugin);
                getServer().getPluginManager().registerEvents(new CryptsFallen(), plugin);
                getServer().getPluginManager().registerEvents(new ConwyArbalester(), plugin);
                getServer().getPluginManager().registerEvents(new ConwyLongbowman(), plugin);
                getServer().getPluginManager().registerEvents(new Engineer(), plugin);
                getServer().getPluginManager().registerEvents(new ThunderstoneElytrier(), plugin);
                getServer().getPluginManager().registerEvents(new Executioner(), plugin);
                getServer().getPluginManager().registerEvents(new FireArcher(), plugin);
                getServer().getPluginManager().registerEvents(new FirelandsAbyssal(), plugin);
                getServer().getPluginManager().registerEvents(new FirelandsHellsteed(), plugin);
                getServer().getPluginManager().registerEvents(new Hypaspist(), plugin);
                getServer().getPluginManager().registerEvents(new HommetAxeman(), plugin);
                getServer().getPluginManager().registerEvents(new HommetLongbowarcher(), plugin);
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
                getServer().getPluginManager().registerEvents(new Priest(), plugin);
                getServer().getPluginManager().registerEvents(new Paladin(), plugin);
                getServer().getPluginManager().registerEvents(new Rogue(), plugin);
                getServer().getPluginManager().registerEvents(new Ranger(), plugin);
                getServer().getPluginManager().registerEvents(new SpearKnight(), plugin);
                getServer().getPluginManager().registerEvents(new Spearman(), plugin);
                getServer().getPluginManager().registerEvents(new Sorcerer(), plugin);
                getServer().getPluginManager().registerEvents(new Vanguard(), plugin);
                getServer().getPluginManager().registerEvents(new Viking(), plugin);
                getServer().getPluginManager().registerEvents(new Warbear(), plugin);
                getServer().getPluginManager().registerEvents(new Warhound(), plugin);
                getServer().getPluginManager().registerEvents(new Warlock(), plugin);

                //mythic stuff
                getServer().getPluginManager().registerEvents(new MythicListener(), plugin);

                // Misc
                getServer().getPluginManager().registerEvents(new RandomKitCommand(), plugin);
                getServer().getPluginManager().registerEvents(new NameTag(), plugin);
                getServer().getPluginManager().registerEvents(new WoolHat(), plugin);

                // Chat
                Objects.requireNonNull(getCommand("GlobalChat")).setExecutor(new GlobalChat());
                Objects.requireNonNull(getCommand("Message")).setExecutor(new PrivateMessage());
                Objects.requireNonNull(getCommand("Reply")).setExecutor(new ReplyMessage());
                Objects.requireNonNull(getCommand("TeamChat")).setExecutor(new TeamChat());

                // Donator
                Objects.requireNonNull(getCommand("Firework")).setExecutor(new FireworkCommand());
                Objects.requireNonNull(getCommand("LeaveMessage")).setExecutor(new LeaveMessage());
                Objects.requireNonNull(getCommand("JoinMessage")).setExecutor(new JoinMessage());

                //duels
                Objects.requireNonNull(getCommand("DuelAccept")).setExecutor(new AcceptDuel());
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

                // Staff - Currencies
                Objects.requireNonNull(getCommand("AddCoins")).setExecutor(new AddCoins());
                Objects.requireNonNull(getCommand("SetCoins")).setExecutor(new SetCoins());
                Objects.requireNonNull(getCommand("SetCoinMultiplier")).setExecutor(new SetCoinMultiplier());
                Objects.requireNonNull(getCommand("TakeCoins")).setExecutor(new TakeCoins());

                // Staff - Boosters
                Objects.requireNonNull(getCommand("GrantBooster")).setExecutor(new GrantBooster());

                // Staff - Punishments
                Objects.requireNonNull(getCommand("Ban")).setExecutor(new Ban());
                Objects.requireNonNull(getCommand("Kick")).setExecutor(new Kick());
                Objects.requireNonNull(getCommand("KickAll")).setExecutor(new KickAll());
                Objects.requireNonNull(getCommand("Mute")).setExecutor(new Mute());
                Objects.requireNonNull(getCommand("Unban")).setExecutor(new Unban());
                Objects.requireNonNull(getCommand("Unmute")).setExecutor(new Unmute());
                Objects.requireNonNull(getCommand("Warn")).setExecutor(new Warn());

                // Staff
                Objects.requireNonNull(getCommand("Broadcast")).setExecutor(new BroadcastCommand());
                Objects.requireNonNull(getCommand("CSReload")).setExecutor(new ReloadCommand());
                Objects.requireNonNull(getCommand("Curse")).setExecutor(new CurseCommand());
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
                Objects.requireNonNull(getCommand("ToggleFree")).setExecutor(new ToggleAllKitsFree());
                Objects.requireNonNull(getCommand("ToggleForcedRandom")).setExecutor(new ToggleForcedRandom());

                // Kits
                Objects.requireNonNull(getCommand("Kit")).setExecutor(new KitCommand());
                Objects.requireNonNull(getCommand("Random")).setExecutor(new RandomKitCommand());
                Objects.requireNonNull(getCommand("Alchemist")).setExecutor(new Alchemist());
                Objects.requireNonNull(getCommand("Archer")).setExecutor(new Archer());
                Objects.requireNonNull(getCommand("Armorer")).setExecutor(new Armorer());
                Objects.requireNonNull(getCommand("Battlemedic")).setExecutor(new BattleMedic());
                Objects.requireNonNull(getCommand("Bannerman")).setExecutor(new Bannerman());
                Objects.requireNonNull(getCommand("Berserker")).setExecutor(new Berserker());
                Objects.requireNonNull(getCommand("Barbarian")).setExecutor(new Barbarian());
                Objects.requireNonNull(getCommand("Constructor")).setExecutor(new Constructor());
                Objects.requireNonNull(getCommand("ConwyArbalester")).setExecutor(new ConwyArbalester());
                Objects.requireNonNull(getCommand("ConwyLongbowman")).setExecutor(new ConwyLongbowman());
                Objects.requireNonNull(getCommand("ConwyRoyalKnight")).setExecutor(new ConwyRoyalKnight());
                Objects.requireNonNull(getCommand("Cavalry")).setExecutor(new Cavalry());
                Objects.requireNonNull(getCommand("Camelrider")).setExecutor(new CamelRider());
                //Objects.requireNonNull(getCommand("Chef")).setExecutor(new Chef());
                Objects.requireNonNull(getCommand("Scout")).setExecutor(new Scout());
                Objects.requireNonNull(getCommand("RoyalCryptsFallen")).setExecutor(new CryptsFallen());
                Objects.requireNonNull(getCommand("Crossbowman")).setExecutor(new Crossbowman());
                Objects.requireNonNull(getCommand("Engineer")).setExecutor(new Engineer());
                Objects.requireNonNull(getCommand("ThunderstoneElytrier")).setExecutor(new ThunderstoneElytrier());
                Objects.requireNonNull(getCommand("Executioner")).setExecutor(new Executioner());
                Objects.requireNonNull(getCommand("FireArcher")).setExecutor(new FireArcher());
                Objects.requireNonNull(getCommand("FirelandsAbyssal")).setExecutor(new FirelandsAbyssal());
                Objects.requireNonNull(getCommand("FirelandsHellsteed")).setExecutor(new FirelandsHellsteed());
                Objects.requireNonNull(getCommand("Halberdier")).setExecutor(new Halberdier());
                Objects.requireNonNull(getCommand("Hypaspist")).setExecutor(new Hypaspist());
                Objects.requireNonNull(getCommand("HelmsDeepUrukBerserker")).setExecutor(new HelmsDeepBerserker());
                Objects.requireNonNull(getCommand("HelmsDeepLancer")).setExecutor(new HelmsDeepLancer());
                Objects.requireNonNull(getCommand("HelmsDeepRangedCavalry")).setExecutor(new HelmsDeepRangedCavalry());
                Objects.requireNonNull(getCommand("HommetLongbowman")).setExecutor(new HommetLongbowarcher());
                Objects.requireNonNull(getCommand("HommetAxeman")).setExecutor(new HommetAxeman());
                Objects.requireNonNull(getCommand("Ladderman")).setExecutor(new Ladderman());
                Objects.requireNonNull(getCommand("Maceman")).setExecutor(new Maceman());
                Objects.requireNonNull(getCommand("Medic")).setExecutor(new Medic());
                Objects.requireNonNull(getCommand("MoriaWindlancer")).setExecutor(new MoriaWindlancer());
                Objects.requireNonNull(getCommand("MoriaGuardian")).setExecutor(new MoriaGuardian());
                Objects.requireNonNull(getCommand("MoriaCaveTroll")).setExecutor(new MoriaCaveTroll());
                Objects.requireNonNull(getCommand("MoriaBonecrusher")).setExecutor(new MoriaBonecrusher());
                Objects.requireNonNull(getCommand("MoriaAxeThrower")).setExecutor(new MoriaAxeThrower());
                Objects.requireNonNull(getCommand("MoriaOrc")).setExecutor(new MoriaOrc());
                Objects.requireNonNull(getCommand("Paladin")).setExecutor(new Paladin());
                Objects.requireNonNull(getCommand("Priest")).setExecutor(new Priest());
                Objects.requireNonNull(getCommand("Ranger")).setExecutor(new Ranger());
                Objects.requireNonNull(getCommand("Rogue")).setExecutor(new Rogue());
                Objects.requireNonNull(getCommand("Shieldman")).setExecutor(new Shieldman());
                Objects.requireNonNull(getCommand("Skirmisher")).setExecutor(new Skirmisher());
                Objects.requireNonNull(getCommand("Spearman")).setExecutor(new Spearman());
                Objects.requireNonNull(getCommand("Spearknight")).setExecutor(new SpearKnight());
                Objects.requireNonNull(getCommand("Sorcerer")).setExecutor(new Sorcerer());
                Objects.requireNonNull(getCommand("Swordsman")).setExecutor(new Swordsman());
                Objects.requireNonNull(getCommand("Vanguard")).setExecutor(new Vanguard());
                Objects.requireNonNull(getCommand("Viking")).setExecutor(new Viking());
                Objects.requireNonNull(getCommand("Warbear")).setExecutor(new Warbear());
                Objects.requireNonNull(getCommand("Warhound")).setExecutor(new Warhound());
                Objects.requireNonNull(getCommand("Warlock")).setExecutor(new Warlock());
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
                Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Scoreboard(), 0, 5);
                Bukkit.getServer().getScheduler().runTaskTimer(plugin, new ApplyRegeneration(), 0, 75);
                Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Hunger(), 0, 20);
                Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(plugin, new KeepAlive(), 0, 5900);

                // Boosters
                activateBoosters();

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
        //PacketEvents.getAPI().terminate();
        // Unregister all listeners
        HandlerList.unregisterAll(plugin);
        // Unload all worlds
        for (World world:Bukkit.getWorlds()) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "save-all flush");
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
        scoreboardLibrary.close();

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

    public YamlDocument getCoreConfig(Route corePath) {
        for (YamlDocument document : coreConfigs) {
            if (document.contains(corePath)) {
                return document;
            }
        }
        return null;
    }

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

        // Set up the vector adapter
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

        // Set up the frame adapters
        TypeAdapter<LocationFrame> locationFrameTypeAdapter = new TypeAdapter<LocationFrame>() {

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

        return configs.toArray(new YamlDocument[configs.size()]);
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
                if (getCoreConfig(mapRoute) != null) {
                    map = new CoreMap();
                }
                map.name = config.getString(mapRoute.add("name"));
                map.worldName = config.getString(mapRoute.add("world"));
                map.gamemode = Gamemode.valueOf(config.getString(mapRoute.add("gamemode")));
                map.startTime = config.getInt(mapRoute.add("start_time"), 0);
                map.daylightCycle = config.getBoolean(mapRoute.add("doDaylightCycle"), true);

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
                //duels map
                createWorld("DuelsMap");

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

    private void loadCores(Route mapRoute, Map map) {
        YamlDocument coreConfig = getCoreConfig(mapRoute);
        String[] corePaths = getPaths(coreConfig, mapRoute);

            // create new coremap
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
                core.scoreboard = coreConfig.getInt(coreRoute.add("scoreboard"));

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
            Flag flag;
            switch (map.gamemode) {
                case Charge:
                    flag = new ChargeFlag(
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
                    break;
                case Assault:
                default:
                    flag = new Flag(
                            flagConfig.getString(flagRoute.add("name")),
                            flagConfig.getBoolean(flagRoute.add("secret"), false),
                            flagConfig.getString(flagRoute.add("start_owners")),
                            maxCap,
                            flagConfig.getInt(flagRoute.add("progress_amount")),
                            flagConfig.getInt(flagRoute.add("start_amount"), maxCap)
                    );
                    break;
            }

            // Set the spawn point
            flag.setSpawnPoint(getLocation(flagRoute.add("spawn_point"), map.worldName, flagConfig));

            //Hologram Location
            flag.holoLoc = getLocation(flagRoute.add("hologram_location"), map.worldName, flagConfig);

            // Set the capture area and animations
            Route captureRoute = flagRoute.add("capture_area");
            if (flagConfig.contains(captureRoute)
                    && flagConfig.getString(captureRoute.add("type")).equalsIgnoreCase("cuboid")) {
                flag.region = getRegion(flagConfig, captureRoute, flag.name.replace(' ', '_'));

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

            flag.scoreboard = flagConfig.getInt(flagRoute.add("scoreboard"));

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

    public void reload() {
        Messenger.broadcast(Component.text("[CastleSiege] ", DARK_AQUA).append(Component.text("Reloading plugin...", GOLD)));
        onDisable();
        onEnable();
        Messenger.broadcast(Component.text("[CastleSiege] ", DARK_AQUA).append(Component.text("Plugin Reloaded!", GOLD)));
    }

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
                            PlayerData.setCoinMultiplier(PlayerData.getCoinMultiplier() + multiplier);
                            Messenger.broadcastInfo(" A " + percentage + "% coin booster " +
                                    "for " + getTimeString((int) remaining_duration) + " has been activated!");
                            Messenger.broadcastInfo("The total coin multiplier is now " + PlayerData.getCoinMultiplier() + ".");
                        });
                        Bukkit.getScheduler().runTaskLaterAsynchronously(Main.plugin, () -> {
                            PlayerData.setCoinMultiplier(PlayerData.getCoinMultiplier() - multiplier);
                            Messenger.broadcastWarning("A " + percentage + "% coin booster has expired!");
                            Messenger.broadcastInfo("The total coin multiplier is now " + PlayerData.getCoinMultiplier() + ".");
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
