package me.huntifi.castlesiege.commands.donator.duels;

import me.huntifi.castlesiege.Main;
import me.huntifi.conwymc.util.Messenger;
import me.huntifi.castlesiege.events.combat.InCombat;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;

public class DuelCommand implements CommandExecutor {

    public static final HashMap<CommandSender, CommandSender> inviter = new HashMap<>();

    public static final HashMap<Player, Player> challenging = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            Messenger.sendError("Console cannot duel anyone", sender);
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
            Messenger.sendError( "Could not find player: <red>" + args[0], sender);
            return true;
        } else if (Objects.equals(sender, receiver)) {
            Messenger.sendError("Stuck in a fight against ourself are we? We should get some help.", sender);
            return true;
        }

        // Both players have to be in a spawnroom.
        if (!InCombat.isPlayerInLobby(receiver.getUniqueId())) {
            Messenger.sendError("This player is currently not in a spawnroom!", sender);
            return true;
        } else if (!InCombat.isPlayerInLobby(((Player) sender).getUniqueId())) {
            Messenger.sendError("You have to perform this command whilst in a spawnroom!", sender);
            return true;
        }

        if (inviter.get(receiver) != null) {
            if (inviter.get(receiver).equals(sender)) {
                Messenger.sendError("You have already challenged this person to a duel!", sender);
                return true;
            }
        }

        //Hashmap that has the data on whether someone is invited by a certain someone.
        inviter.put(receiver, sender);
        Messenger.sendDuel("You challenged " + receiver.getName() + " to a duel!", sender);
        Messenger.sendDuel(sender.getName() + " has challenged you to a duel! " +
                "<yellow><click:suggest_command:/acceptduel " + sender.getName() + ">/acceptduel <player></click></yellow>", receiver);

        new BukkitRunnable() {
            @Override
            public void run() {

                if (challenging.get(receiver) == null) {
                        Messenger.sendDuel("Your invitation to a duel with " + receiver.getName() + " has expired!", sender);
                        Messenger.sendDuel("The invitation that you received from " + sender.getName() + " has expired.", receiver);
                }
                inviter.remove(receiver, sender);
            }
        }.runTaskLater(Main.plugin, 300);

        return true;
    }

    /**
     * simple method returning true or false to determine whether someone is dueling.
     * @param p The player to check if they're dueling
     * @return If the player is dueling
     */
    public static boolean isDueling(Player p) {
        return challenging.containsKey(p) || challenging.containsValue(p);
    }
}
