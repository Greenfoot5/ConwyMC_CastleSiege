package me.huntifi.castlesiege.commands.donator.duels;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.events.combat.LobbyCombat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;

public class DuelCmd implements CommandExecutor {

    static final HashMap<CommandSender, CommandSender> inviter = new HashMap<>();

    public static final HashMap<Player, Player> challenging = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage("Console cannot duel anyone");
            return true;
        }
        // Command needs a player
        if (args.length < 1) {
            return false;
        }

        // the receiving individual
        Player receiver = Bukkit.getPlayer(args[0]);

        // Cannot challenge yourself
        if (receiver == null) {
            Messenger.sendError( "Could not find player: " + ChatColor.RED + args[0], sender);
            return true;
        } else if (Objects.equals(sender, receiver)) {
            Messenger.sendWarning(ChatColor.RED + "Stuck in a fight against ourself are we? We should get some help.", sender);
            return true;
        }

        // Both players have to be in a spawnroom.
        if (!InCombat.isPlayerInLobby(receiver.getUniqueId())) {
            Messenger.sendWarning(ChatColor.RED + "This player is currently not in a spawnroom!", sender);
            return true;
        } else if (!InCombat.isPlayerInLobby(((Player) sender).getUniqueId())) {
            Messenger.sendWarning(ChatColor.RED + "You have to perform this command whilst in a spawnroom!", sender);
            return true;
        }

        if (inviter.get(receiver) != null) {
            if (inviter.get(receiver).equals(sender)) {
                Messenger.sendWarning(ChatColor.RED + "You have already challenged this person to a duel!", sender);
                return true;
            }
        }

        //Hashmap that has the data on whether someone is invited by a certain someone.
        inviter.put(receiver, sender);
        Messenger.sendSuccess("You challenged " + receiver.getName() + " to a duel!", sender);
        Messenger.sendSuccess(sender.getName() + " has challenged you to a duel! (/acceptduel <player>)", receiver);

        new BukkitRunnable() {
            @Override
            public void run() {

                if (challenging.get(receiver) == null) {
                        Messenger.sendWarning("Your invitation to a duel with " + receiver.getName() + " has expired!", sender);
                        Messenger.sendWarning("The invitation that you received from " + sender.getName() + " has expired.", receiver);
                }
                inviter.remove(receiver, sender);
            }
        }.runTaskLater(Main.plugin, 300);

        return true;
    }

    /**
     * Get the last person who challenged the specified person to a duel
     * @param s The receiving individual
     * @return The last sender, null if no previous invite received
     */
    public CommandSender getLastInviter(CommandSender s) {
        return inviter.get(s);
    }

    /**
     * simple method returning true or false to determine whether someone is dueling.
     * @p player to check for
     */
    public static boolean isDueling(Player p) {
        return challenging.containsKey(p) || challenging.containsValue(p);
    }
}
