package me.huntifi.castlesiege.commands.info;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.PlayerData;
import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.Team;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.List;
import java.util.UUID;

/**
 * Shows the player's stats
 */
public class MyStatsCommand implements CommandExecutor {

    /**
     * Opens a book that shows the player's stats
     * @param sender Source of the command
     * @param cmd Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return true
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Player p = (Player) sender;
                p.openBook(myStatsBook(p));
            }
        }.runTaskAsynchronously(Main.plugin);

        return true;
    }

    /**
     * Get a book containing the player's stats
     * @param p The player whose stats to show
     * @return A book containing the player's stats
     */
    private ItemStack myStatsBook(Player p) {
        BookMeta meta = (BookMeta) Bukkit.getItemFactory().getItemMeta(Material.WRITTEN_BOOK);
        assert meta != null;
        meta.setTitle(ChatColor.GREEN + p.getName() + ChatColor.WHITE + "'s Stats.");
        meta.setAuthor(ChatColor.RED + "Hunt von Huntington");

        PlayerData data = ActiveData.getData(p.getUniqueId());
        DecimalFormat dec = new DecimalFormat("0.00");
        DecimalFormat num = new DecimalFormat("0");

        meta.addPage(ChatColor.BLACK + "Name: " + ChatColor.DARK_GRAY + p.getName() + "\n\n"
                + ChatColor.BLACK + "Rank: " + ChatColor.DARK_GRAY + data.getStaffRank() + "\n"
                + ChatColor.BLACK + "Donator: " + ChatColor.DARK_GRAY + data.getRank() + "\n\n"
                + ChatColor.BLACK + "Score: " + ChatColor.DARK_GRAY + num.format(data.getScore()) + "\n"
                + ChatColor.DARK_GREEN + "Kills: " + ChatColor.DARK_GRAY + data.getKills() + "\n"
                + ChatColor.RED + "Deaths: " + ChatColor.DARK_GRAY + data.getDeaths() + "\n"
                + ChatColor.BLACK + "KDR: " + ChatColor.DARK_GRAY + dec.format((data.getKills() / data.getDeaths())) + "\n"
                + ChatColor.BLACK + "Assists: " + ChatColor.DARK_GRAY + num.format(data.getAssists()) + "\n"
                + ChatColor.BLACK + "Supports: " + ChatColor.DARK_GRAY + num.format(data.getAssists()) + "\n"
                + ChatColor.BLACK + "Heals: " + ChatColor.DARK_GRAY + num.format(data.getHeals()) + "\n"
                + ChatColor.BLACK + "Captures: " + ChatColor.DARK_GRAY + num.format(data.getCaptures()) + "\n"
                + ChatColor.BLACK + "Killstreak: " + ChatColor.DARK_GRAY + num.format(data.getMaxKillStreak()),
                "Level: \n"             // TODO level
                + "Next Level: \n\n");  // TODO next level;

        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        book.setItemMeta(meta);
        return book;
    }
}
