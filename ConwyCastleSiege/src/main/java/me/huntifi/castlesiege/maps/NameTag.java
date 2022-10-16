package me.huntifi.castlesiege.maps;

import com.nametagedit.plugin.NametagEdit;
import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.commands.staff.ToggleRankCommand;
import me.huntifi.castlesiege.data_types.PlayerData;
import me.huntifi.castlesiege.database.ActiveData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * Manages the name tags of players
 */
public class NameTag implements CommandExecutor {

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
            p.setDisplayName("§e" + data.getLevel() + " " + rank + color(p) + p.getName());
            NametagEdit.getApi().setPrefix(p, rank + color(p));
        });

        
     });
    }

    /**
     * Get the player's primary chat color
     * @param p The player
     * @return The player's chat color
     */
    public static String color(Player p) {
        if (!MapController.isSpectator(p.getUniqueId())) {
            return TeamController.getTeam(p.getUniqueId()).primaryChatColor.toString();
        } else {
            return ChatColor.GRAY + ChatColor.ITALIC.toString();
        }
    }

    /**
     * Get the pretty representation of the player's staff or donator tag
     * @param rank The rank to convert
     * @return A formatted rank or an empty string if invalid
     */
    public static String convertRank(String rank) {
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
}
