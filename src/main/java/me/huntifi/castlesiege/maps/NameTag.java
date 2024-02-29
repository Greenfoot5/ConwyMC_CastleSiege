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
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
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
            Bukkit.getScheduler().runTask(Main.plugin, () -> NametagEdit.getApi().setPrefix(p, color(p).toString()));
            return;
        }

        // Get the player's wanted rank
        Component rank;
        if (p.hasPermission("castlesiege.builder") && !ToggleRankCommand.showDonator.contains(p)) {
            rank = convertRank(data.getStaffRank());
        } else {
            rank = convertRank(data.getRank());
        }

        Bukkit.getScheduler().runTask(Main.plugin, () -> {
            // Set the player's tags

            if (hidePlayerName || hideBoth) {
                p.setDisplayName("§e" + color(p) + getName(p));
                NametagEdit.getApi().setPrefix(p, color(p).toString() + ChatColor.MAGIC);
            } else {
                p.setDisplayName("§e" + data.getLevel() + " " + rank + color(p) + p.getName());
                NametagEdit.getApi().setPrefix(p, rank + color(p).toString());
            }
        });
     });
    }

    /**
     * Get the player's primary chat color
     * @param p The player
     * @return The player's chat color
     */
    public static NamedTextColor color(Player p) {
        return NamedTextColor.WHITE;
//        if (!MapController.getPlayers().contains(p.getUniqueId())) {
//            if (hideTeamColour || hideBoth)
//                return NamedTextColor.WHITE.toString();
//            return TeamController.getTeam(p.getUniqueId()).primaryChatColor.toString();
//        } else {
//            return NamedTextColor.GRAY + TextDecoration.ITALIC;
//        }
    }

    /**
     * Get the player's primary chat color
     * @param p The player
     * @return The player's chat color
     */
    public static String getName(Player p) {
        if (hidePlayerName || hideBoth) {
            //return ChatColor.MAGIC + new String(new char[new Random().nextInt(5, 20)]).replace("\0", "-");
            int randomNum = new Random().nextInt((20 - 5) + 1) + 5;
            return ChatColor.MAGIC + new String(new char[randomNum]).replace("\0", "-");
        } else {
            return p.getName();
        }
    }

    public static Component level(int senderLevel, int viewerLevel) {
        if (hidePlayerName || hideBoth) {
            return Component.text("");
        }
        // Sender is 11+ levels below the viewer
        else if (senderLevel + 10 < viewerLevel) {
            return Component.text(senderLevel + " ").color(NamedTextColor.DARK_GREEN);
        }
        // Sender is 6-10 levels below the viewer
        else if (senderLevel + 5 < viewerLevel && senderLevel + 10 >= viewerLevel) {
            return Component.text(senderLevel + " ").color(NamedTextColor.GREEN);
        }
        // Sender is within 5 levels of the viewer
        else if (senderLevel - 5 <= viewerLevel && senderLevel + 5 >= viewerLevel) {
            return Component.text(senderLevel + " ").color(NamedTextColor.YELLOW);
        }
        // Sender is 6-10 levels above the viewer
        else if (senderLevel - 5 > viewerLevel && senderLevel - 10 <= viewerLevel) {
            return Component.text(senderLevel + " ").color(NamedTextColor.RED);
        }
        // Sender is 11+ levels above the viewer
        else {
            return Component.text(senderLevel + " ").color(NamedTextColor.DARK_RED);
        }
    }

    public static Component chatName(Player sender, Player viewer) {
        PlayerData senderData = ActiveData.getData(sender.getUniqueId());
        PlayerData viewerData = ActiveData.getData(viewer.getUniqueId());

        // Get the player's rank
        Component rank;
        if (sender.hasPermission("castlesiege.builder") && !ToggleRankCommand.showDonator.contains(sender)) {
            rank = convertRank(senderData.getStaffRank());
        } else {
            rank = convertRank(senderData.getRank());
        }
        MiniMessage mm = MiniMessage.miniMessage();

        if (hidePlayerName || hideBoth)
            return mm.deserialize(color(sender).toString() + ChatColor.MAGIC + getName(sender));
        return level(senderData.getLevel(), viewerData.getLevel())
                .append(rank)
                .append(Component.text(sender.getName())
                        .color(color(sender))
                );
    }

    /**
     * Get the pretty representation of the player's staff or donator tag
     * @param rank The rank to convert
     * @return A formatted rank or an empty string if invalid
     */
    public static Component convertRank(String rank) {
        if (hidePlayerName || hideBoth) {
            return Component.text("");
        }

        switch (rank) {
            // Staff Ranks
            case "builder":
                return Component.text("§b§lBuilder ");
            case "chatmod":
                return Component.text("§9§lChatMod ");
            case "chatmod+":
                return Component.text("§1§lChatMod+ ");
            case "moderator":
                return Component.text("§a§lMod ");
            case "developer":
                return Component.text("§2§lDev ");
            case "communitymanager":
                return Component.text("§5§lComm Man ");
            case "admin":
                return Component.text("§c§lAdmin ");
            case "owner":
                return MiniMessage.miniMessage().deserialize("<dark_red><b><obf><st>!</st></obf></dark_red><gradient:#FFAA00:#FF5500>Owner</gradient><obf><st><dark_red>!</dark_red></st></obf><b> ");
            // Donator Ranks
            case "esquire":
                return Component.text("§3Esquire ");
            case "noble":
                return Component.text("§aNoble ");
            case "baron":
                return Component.text("§5Baron ");
            case "count":
                return Component.text("§6Count ");
            case "duke":
                return Component.text("§4Duke ");
            case "viceroy":
                return Component.text("§dViceroy ");
            case "king":
                return Component.text("§eKing ");
            case "high_king":
                return Component.text("§eHigh King ");
            default:
                return Component.text("");
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
            NametagEdit.getApi().setPrefix(p, color(p).toString());
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
