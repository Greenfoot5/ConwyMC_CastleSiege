package me.huntifi.castlesiege.maps;

import com.nametagedit.plugin.NametagEdit;
import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.commands.staff.ToggleRankCommand;
import me.huntifi.castlesiege.data_types.PlayerData;
import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.castlesiege.events.curses.BlindnessCurse;
import me.huntifi.castlesiege.events.curses.CurseExpired;
import me.huntifi.castlesiege.events.curses.GreaterBlindnessCurse;
import me.huntifi.castlesiege.events.curses.TrueBlindnessCurse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

/**
 * Manages the name tags of players
 */
public class NameTag implements CommandExecutor, Listener {

    private static boolean hidePlayerName = false;
    private static boolean hideTeamColour = false;
    private static boolean hideBoth = false;


    /**
     * Give a player their name tag
     * @param p The player
     */
    public static void give(Player p) {
        if (p == null) {
            return;
        }

        Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {

        // Only set name tag color if data has not been loaded yet
        PlayerData data = ActiveData.getData(p.getUniqueId());
        if (data == null) {
            Bukkit.getScheduler().runTask(Main.plugin, () -> NametagEdit.getApi().setPrefix(p, color(p)));
            return;
        }

        // Get the player's wanted rank
        String rank;
        if (p.hasPermission("castlesiege.builder") && !ToggleRankCommand.showDonator.contains(p)) {
            rank = convertRank(data.getStaffRank());
        } else {
            rank = convertRank(data.getRank());
        }

        Bukkit.getScheduler().runTask(Main.plugin, () -> {
            // Set the player's tags

            if (hidePlayerName || hideBoth) {
                p.setDisplayName("§e" + color(p) + getName(p));
                NametagEdit.getApi().setPrefix(p, color(p) + ChatColor.MAGIC);
            } else {
                p.setDisplayName("§e" + data.getLevel() + " " + rank + color(p) + p.getName());
                NametagEdit.getApi().setPrefix(p, rank + color(p));
            }
        });

        
     });
    }

    /**
     * Get the player's primary chat color
     * @param p The player
     * @return The player's chat color
     */
    public static String color(Player p) {
        if (!MapController.getPlayers().contains(p.getUniqueId())) {
            if (hideTeamColour || hideBoth)
                return ChatColor.WHITE.toString();
            return TeamController.getTeam(p.getUniqueId()).primaryChatColor.toString();
        } else {
            return ChatColor.GRAY + ChatColor.ITALIC.toString();
        }
    }

    /**
     * Get the player's primary chat color
     * @param p The player
     * @return The player's chat color
     */
    public static String getName(Player p) {
        if (hidePlayerName || hideBoth) {
            return ChatColor.MAGIC + new String(new char[new Random().nextInt(5, 20)]).replace("\0", "-");
        } else {
            return p.getName();
        }
    }

    public static String level(int senderLevel, int viewerLevel) {
        if (hidePlayerName || hideBoth) {
            return "";
        }
        // Sender is 11+ levels below the viewer
        else if (senderLevel + 10 < viewerLevel) {
            return ChatColor.DARK_GREEN + String.valueOf(senderLevel) + " ";
        }
        // Sender is 6-10 levels below the viewer
        else if (senderLevel + 5 < viewerLevel && senderLevel + 10 >= viewerLevel) {
            return ChatColor.GREEN + String.valueOf(senderLevel) + " ";
        }
        // Sender is within 5 levels of the viewer
        else if (senderLevel - 5 <= viewerLevel && senderLevel + 5 >= viewerLevel) {
            return ChatColor.YELLOW + String.valueOf(senderLevel) + " ";
        }
        // Sender is 6-10 levels above the viewer
        else if (senderLevel - 5 > viewerLevel && senderLevel - 10 <= viewerLevel) {
            return ChatColor.RED + String.valueOf(senderLevel) + " ";
        }
        // Sender is 11+ levels above the viewer
        else {
            return ChatColor.DARK_RED + String.valueOf(senderLevel) + " ";
        }
    }

    public static String chatName(Player sender, Player viewer) {
        PlayerData senderData = ActiveData.getData(sender.getUniqueId());
        PlayerData viewerData = ActiveData.getData(viewer.getUniqueId());

        // Get the player's rank
        String rank;
        if (sender.hasPermission("castlesiege.builder") && !ToggleRankCommand.showDonator.contains(sender)) {
            rank = convertRank(senderData.getStaffRank());
        } else {
            rank = convertRank(senderData.getRank());
        }

        if (hidePlayerName || hideBoth)
            return color(sender) + ChatColor.MAGIC + getName(sender);
        return level(senderData.getLevel(), viewerData.getLevel()) + rank + color(sender) + sender.getName();
    }

    /**
     * Get the pretty representation of the player's staff or donator tag
     * @param rank The rank to convert
     * @return A formatted rank or an empty string if invalid
     */
    public static String convertRank(String rank) {
        if (hidePlayerName || hideBoth) {
            return "";
        }

        switch (rank) {
            // Staff Ranks
            case "builder":
                return "§b§lBuilder ";
            case "chatmod":
                return "§9§lChatMod ";
            case "chatmod+":
                return "§1§lChatMod+ ";
            case "moderator":
                return "§a§lMod ";
            case "developer":
                return "§2§lDev ";
            case "communitymanager":
                return "§5§lComm Man ";
            case "admin":
                return "§c§lAdmin ";
            case "owner":
                return "§6§lOwner ";
            // Donator Ranks
            case "esquire":
                return "§3Esquire ";
            case "noble":
                return "§aNoble ";
            case "baron":
                return "§5Baron ";
            case "count":
                return "§6Count ";
            case "duke":
                return "§4Duke ";
            case "viceroy":
                return "§dViceroy ";
            case "king":
                return "§eKing ";
            case "high_king":
                return "§eHigh King ";
            default:
                return "";
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player p = (Player) sender;
        // Only set name tag color if data has not been loaded yet
        PlayerData data = ActiveData.getData(p.getUniqueId());
        if (data == null) {
            NametagEdit.getApi().setPrefix(p, color(p));
            return true;
        }

        String rank = Arrays.toString(args).replace('&', '§');

        // Set the player's tags
        p.setDisplayName("§e" + data.getLevel() + " " + rank + color(p) + p.getName());
        new BukkitRunnable() {
            @Override
            public void run() {
                NametagEdit.getApi().setPrefix(p, rank + color(p));
            }
        }.runTask(Main.plugin);
        return true;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void beginHidingNames(BlindnessCurse curse) {
        hidePlayerName = true;

        for (UUID uuid : MapController.getPlayers()) {
            give(Bukkit.getPlayer(uuid));
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void beginHidingTeams(GreaterBlindnessCurse curse) {
        hideTeamColour = true;

        for (UUID uuid : MapController.getPlayers()) {
            give(Bukkit.getPlayer(uuid));
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void beginHidingBoth(TrueBlindnessCurse curse) {
        hideBoth = true;

        for (UUID uuid : MapController.getPlayers()) {
            give(Bukkit.getPlayer(uuid));
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void blindnessExpired(CurseExpired curse) {
        if (Objects.equals(curse.getDisplayName(), BlindnessCurse.name)) {
            hidePlayerName = false;
        } else if (Objects.equals(curse.getDisplayName(), GreaterBlindnessCurse.name)) {
            hideTeamColour = false;
        } else if (Objects.equals(curse.getDisplayName(), TrueBlindnessCurse.name)) {
            hideBoth = false;
        } else {
            return;
        }

        for (UUID uuid : MapController.getPlayers()) {
            give(Bukkit.getPlayer(uuid));
        }
    }
}
