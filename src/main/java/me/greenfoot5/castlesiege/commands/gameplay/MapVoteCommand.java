package me.greenfoot5.castlesiege.commands.gameplay;

import me.greenfoot5.castlesiege.Main;
import me.greenfoot5.castlesiege.maps.MapController;
import me.greenfoot5.castlesiege.events.map.NextMapEvent;
import me.greenfoot5.conwymc.util.Messenger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A command to vote if a player liked/disliked a map
 */
public class MapVoteCommand implements Listener, TabExecutor {
    private static List<UUID> votedNo = new ArrayList<>();
    private static List<UUID> votedYes = new ArrayList<>();

    /**
     * Ends the vote and records the votes
     * @param event The event when another map starts
     */
    @EventHandler
    public void onNextMap(NextMapEvent event) {
        if (MapController.getPlayers().size() < 4) {
            Main.plugin.getLogger().info("Not enough players for a fair map vote. Votes will be ignored.");
            return;
        }

        try {
            PreparedStatement get = Main.SQL.getConnection().prepareStatement(
                    "SELECT * FROM map_votes WHERE map_name = ?");
            get.setString(1, event.previousMap);
            ResultSet rs = get.executeQuery();
            int yes;
            int no;
            int aYes = 0;
            int aNo = 0;
            if (rs.next()) {
                yes = rs.getInt("yes");
                no = rs.getInt("no");
                aYes = rs.getInt("alltime_yes");
                aNo = rs.getInt("alltime_no");
            } else {
                yes = 0;
                no = 0;
                PreparedStatement add = Main.SQL.getConnection().prepareStatement(
                        "INSERT INTO map_votes (map_name, yes, no)\n" +
                                "VALUES (?, 0, 0)");
                add.setString(1, event.previousMap);
                add.executeUpdate();
            }
            get.close();

            yes += votedYes.size();
            no += votedNo.size();
            aYes += votedYes.size();
            aNo += votedNo.size();

            PreparedStatement ps = Main.SQL.getConnection().prepareStatement(
                    "UPDATE map_votes SET yes = ?, no = ?, alltime_yes = ?, alltime_no = ? WHERE map_name = ?");
            ps.setInt(1, yes);
            ps.setInt(2, no);
            ps.setInt(3, aYes);
            ps.setInt(4, aNo);
            ps.setString(5, event.previousMap);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Reset counts
        votedNo = new ArrayList<>();
        votedYes = new ArrayList<>();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (args[0] == null || !(args[0].equals("no") || args[0].equals("yes"))) {
            Messenger.sendError("Must either vote <red>no</red> or <red>yes</red>", sender);
            return false;
        }

        if (!(sender instanceof Player p)) {
            Messenger.sendError("Only players can vote!", sender);
            return true;
        }

        if (args[0].equals("no")) {
            if (votedNo.contains(p.getUniqueId())) {
                Messenger.sendWarning("You already voted <dark_aqua>no</dark_aqua>! Only one vote is counted per player.", sender);
                return true;
            }
            votedYes.remove(p.getUniqueId());
            votedNo.add(p.getUniqueId());
            Messenger.sendSuccess("Voted <dark_aqua>no</dark_aqua> for the map.", sender);
            return true;
        }

        if (votedYes.contains(p.getUniqueId())) {
            Messenger.sendWarning("You already voted <dark_aqua>yes</dark_aqua>! Only one vote is counted per player.", sender);
            return true;
        }
        votedNo.remove(p.getUniqueId());
        votedYes.add(p.getUniqueId());
        Messenger.sendSuccess("Voted <dark_aqua>yes</dark_aqua> for the map.", sender);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (args.length <= 1)
            return List.of("");
            //return List.of("no", "yes");
        return null;
    }
}
