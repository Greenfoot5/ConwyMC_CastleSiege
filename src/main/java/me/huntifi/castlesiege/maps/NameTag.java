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
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
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
            Bukkit.getScheduler().runTask(Main.plugin, () -> NametagEdit.getApi().setPrefix(p, MiniMessage.miniMessage().serialize(username(p))));
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
                p.displayName(username(p));
                NametagEdit.getApi().setPrefix(p, legacyColor(p) + ChatColor.MAGIC);
            } else {
                p.displayName( Component.text(data.getLevel() + " ").color(NamedTextColor.YELLOW)
                        .append(rank).append(username(p)));
                String serialized = LegacyComponentSerializer.legacySection().serialize(rank);
                NametagEdit.getApi().setPrefix(p, serialized.substring(0, serialized.length() - 1) + legacyColor(p));
            }
        });
     });
    }

    /**
     * Get the player's username as a component
     * @param p The player
     * @return The player's username & colour
     */
    public static Component username(Player p) {
        if (MapController.getPlayers().contains(p.getUniqueId())) {
            if (hideTeamColour || hideBoth)
                return Component.text(getName(p)).color(NamedTextColor.WHITE);
            return Component.text(getName(p)).color(TeamController.getTeam(p.getUniqueId()).primaryChatColor);
        } else {
            return Component.text(getName(p)).color(NamedTextColor.GRAY).decorate(TextDecoration.ITALIC);
        }
    }

    /**
     * Get the player's username with minimessage
     * @param p The player
     * @return The player's username with minimessage colour
     */
    public static String mmUsername(Player p) {
        if (MapController.getPlayers().contains(p.getUniqueId())) {
            if (hideTeamColour || hideBoth)
                return "<white>" + getName(p) + "</white>";
            String tag = "color:" + TeamController.getTeam(p.getUniqueId()).primaryChatColor.asHexString();
            return "<" + tag + ">" + getName(p) + "</" + tag + ">";
        } else {
            return "<gray><i>" + getName(p) + "</gray>";
        }
    }

    private static String legacyColor(Player p) {
        if (MapController.getPlayers().contains(p.getUniqueId())) {
            if (hideTeamColour || hideBoth)
                return ChatColor.WHITE.toString();
            Component c = Component.text(" ").color(TeamController.getTeam(p.getUniqueId()).primaryChatColor);
            return LegacyComponentSerializer.legacySection().serialize(c);
        } else {
            return ChatColor.GRAY.toString() + ChatColor.ITALIC;
        }
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
            return mm.deserialize(mmUsername(sender));
        return level(senderData.getLevel(), viewerData.getLevel())
                .append(rank)
                .append(username(sender));
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
                return MiniMessage.miniMessage().deserialize("<b><gradient:#2db359:#1c8c70>Mod</gradient></b> ");
            case "developer":
                return MiniMessage.miniMessage().deserialize("<b><gradient:#32731d:#269926>Dev</gradient></b> ");
            case "communitymanager":
                return MiniMessage.miniMessage().deserialize("<b><gradient:#a422e6:#7830bf>Comm Man</gradient></b> ");
            case "admin":
                return MiniMessage.miniMessage().deserialize("<b><gradient:#A82533:#EF4848>Admin</gradient></b> ");
            case "owner":
                return MiniMessage.miniMessage().deserialize("<b><dark_red><obf><st>!</dark_red><gradient:#FFAA00:#FF5500>Owner</gradient><dark_red><obf><st>!</dark_red> ");
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
                return Component.text("<gradient:#be1fcc:#d94cd9>Viceroy</gradient> ");
            case "king":
                return Component.text("<gradient:#F07654:#F5DF2E:#F07654>⚜King⚜</gradient> ");
            case "high_king":
                return Component.text("<gradient:#FFED00:#FF0000>👑High King👑</gradient> ");
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
            NametagEdit.getApi().setPrefix(p, legacyColor(p));
            return true;
        }

        String rank = Arrays.toString(args).replace('&', '§');
        Component mmRank = MiniMessage.miniMessage().deserialize(rank);

        // Set the player's tags
        p.displayName(Component.text(data.getLevel()).color(NamedTextColor.YELLOW).append(mmRank).append(username(p)));
        new BukkitRunnable() {
            @Override
            public void run() {
                NametagEdit.getApi().setPrefix(p, rank + legacyColor(p));
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
