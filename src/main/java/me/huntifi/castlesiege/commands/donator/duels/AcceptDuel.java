package me.huntifi.castlesiege.commands.donator.duels;

import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.events.combat.InCombat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class AcceptDuel implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage("Console cannot accept a duel from anyone");
            return true;
        }

        // Command needs a player
        if (args.length < 1) {
            return false;
        }

        // the receiving individual
        Player challenger = Bukkit.getPlayer(args[0]);

        //Check if the player received an invitation from the host
        if (DuelCmd.inviter.get(sender) != null) {
            if (!DuelCmd.inviter.get(sender).equals(challenger)) {
                Messenger.sendWarning(ChatColor.RED + "You currently don't have an invitation from this player.", sender);
                return true;
            }
        }

        // Cannot accept a challenge from yourself
        if (challenger == null) {
            Messenger.sendError( "Could not find player: " + ChatColor.RED + args[0], sender);
            return true;
        } else if (Objects.equals(sender, challenger)) {
            Messenger.sendWarning(ChatColor.RED + "I doubt you sent an invitation to yourself...", sender);
            return true;
        }

        // Both players have to be in a spawnroom.
        if (!InCombat.isPlayerInLobby(challenger.getUniqueId())) {
            Messenger.sendWarning(ChatColor.RED + "The host is currently not in a spawnroom!", sender);
            return true;
        } else if (!InCombat.isPlayerInLobby(((Player) sender).getUniqueId())) {
            Messenger.sendWarning(ChatColor.RED + "You have to perform this command whilst in a spawnroom!", sender);
            return true;
        }

        //Hashmap that has the data on whether someone is invited by a certain someone.
        DuelCmd.inviter.remove(sender, challenger);
        DuelCmd.challenging.put((Player) sender, challenger);
        Messenger.sendSuccess("You accepted " + challenger.getName() + "'s invitation to a duel.", sender);
        Messenger.sendSuccess(sender.getName() + " has accepted your invitation to a duel!", challenger);
        return true;
    }
}
