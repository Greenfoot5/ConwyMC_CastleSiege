package me.huntifi.castlesiege.commands.staff.maps;

import me.huntifi.castlesiege.commands.gameplay.VoteSkipCommand;
import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.NameTag;
import me.huntifi.castlesiege.maps.Team;
import me.huntifi.castlesiege.maps.TeamController;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.UUID;

public class SpectateCommand implements CommandExecutor {

    public static final ArrayList<UUID> spectators = new ArrayList<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        Player player;
        if (args.length == 0) {
            player = (Player) sender;
        } else {
            player = Bukkit.getPlayer(args[0]);
        }

        if (player == null) {
            sender.sendMessage(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "Could not find player: " + ChatColor.RED + args[0]);
            return true;
        }

        if (InCombat.isPlayerInCombat(player.getUniqueId())) {
            sender.sendMessage(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "That player is currently in combat!");
            return true;
        }

        // If the player is a spectator, add them to a team
        if (spectators.contains(player.getUniqueId())) {
            spectators.remove(player.getUniqueId());
            MapController.joinATeam(player.getUniqueId());
            player.setGameMode(GameMode.SURVIVAL);
            InCombat.playerDied(player.getUniqueId());

            // Assign stored kit
            Kit kit = Kit.getKit(ActiveData.getData(player.getUniqueId()).getKit());
            if (kit != null && kit.canSelect(player, true, false))
                kit.addPlayer(player.getUniqueId());
            else
                Kit.getKit("Swordsman").addPlayer(player.getUniqueId());

        } else {
            Team team = TeamController.getTeam(player.getUniqueId());
            team.removePlayer(player.getUniqueId());
            VoteSkipCommand.removePlayer(player.getUniqueId());
            spectators.add(player.getUniqueId());
            player.setGameMode(GameMode.SPECTATOR);
            if (InCombat.isPlayerInLobby(player.getUniqueId())) {
                player.teleport(MapController.getCurrentMap().flags[0].getSpawnPoint());
            }
            NameTag.give(player);
        }
        return true;
    }
}
