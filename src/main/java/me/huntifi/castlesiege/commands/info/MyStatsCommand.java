package me.huntifi.castlesiege.commands.info;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.PlayerData;
import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.castlesiege.database.UpdateStats;
import me.huntifi.conwymc.util.Messenger;
import me.huntifi.castlesiege.maps.NameTag;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;

import static me.huntifi.castlesiege.commands.info.leaderboard.Leaderboard.gradient;

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
        if (sender instanceof ConsoleCommandSender) {
            Messenger.sendError("Console does not have any stats!", sender);
            return true;
        }

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
        meta.title(Component.text(p.getName(), NamedTextColor.GREEN)
                .append(Component.text("'s Stats", NamedTextColor.WHITE)));
        meta.author(Component.text("Hunt von Huntington", NamedTextColor.RED));

        PlayerData data = ActiveData.getData(p.getUniqueId());
        DecimalFormat dec = new DecimalFormat("0.00");
        DecimalFormat num = new DecimalFormat("0");
        MiniMessage mm = Messenger.mm;

        meta.addPages(Component.text("Name: ", NamedTextColor.BLACK)
                .append(mm.deserialize(NameTag.mmUsername(p)))
                .append(Component.newline()).append(Component.newline())
                .append(Component.text("Rank: ")).append(NameTag.convertRank(data.getStaffRank()))
                .append(Component.newline())
                .append(Component.text("Donator: ")).append(NameTag.convertRank(data.getRank()))
                .append(Component.newline()).append(Component.newline())
                .append(mm.deserialize("<transition:" + gradient +":0>Score: </transition>"))
                .append(Component.text(dec.format(data.getScore()), NamedTextColor.DARK_GRAY))
                .append(Component.newline())
                .append(mm.deserialize("<transition:" + gradient + ":0.15>Kills: </transition>"))
                .append(Component.text(num.format(data.getKills()), NamedTextColor.DARK_GRAY))
                .append(Component.newline())
                .append(mm.deserialize("<transition:" + gradient + ":0.4>Deaths: </transition>"))
                .append(Component.text(num.format(data.getDeaths()), NamedTextColor.DARK_GRAY))
                .append(Component.newline())
                .append(mm.deserialize("<transition:" + gradient + ":0.6>KDR: </transition>"))
                .append(Component.text(dec.format(data.getKills() / data.getDeaths()), NamedTextColor.DARK_GRAY))
                .append(Component.newline())
                .append(mm.deserialize("<transition:" + gradient + ":0.7>Assists: </transition>"))
                .append(Component.text(num.format(data.getAssists()), NamedTextColor.DARK_GRAY))
                .append(Component.newline())
                .append(mm.deserialize("<transition:" + gradient + ":0.8>Captures: </transition>"))
                .append(Component.text(num.format(data.getCaptures()), NamedTextColor.DARK_GRAY))
                .append(Component.newline())
                .append(mm.deserialize("<transition:" + gradient + ":0.9>Heals: </transition>"))
                .append(Component.text(num.format(data.getHeals()), NamedTextColor.DARK_GRAY))
                .append(Component.newline())
                .append(mm.deserialize("<transition:" + gradient + ":1>Supports: </transition>"))
                .append(Component.text(num.format(data.getSupports()), NamedTextColor.DARK_GRAY))
                .append(Component.newline())
                .append(Component.text("Kill Streak: ", NamedTextColor.GOLD))
                .append(Component.text(num.format(data.getMaxKillStreak()))));

        meta.addPages(Component.text("Level: ", NamedTextColor.BLACK)
                .append(Component.text(num.format(data.getLevel()), NamedTextColor.DARK_GREEN))
                .append(Component.newline())
                .append(Component.text("Next Level: "))
                .append(Component.text(dec.format(UpdateStats.levelScore(data.getLevel() + 1) - data.getScore()), NamedTextColor.RED)));

        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        book.setItemMeta(meta);
        return book;
    }
}
