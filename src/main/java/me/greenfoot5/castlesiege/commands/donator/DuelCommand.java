package me.greenfoot5.castlesiege.commands.donator;

import me.greenfoot5.castlesiege.Main;
import me.greenfoot5.castlesiege.data_types.Arena;
import me.greenfoot5.castlesiege.events.combat.InCombat;
import me.greenfoot5.conwymc.util.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

/**
 * Sends a duel request to a player
 */
public class DuelCommand implements CommandExecutor {

    private static final HashMap<UUID, UUID> challenges = new HashMap<>();

    public static Arena arena;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            Messenger.sendError("Only players can participate in duels!", sender);
            return true;
        }

        // Command needs a player
        if (args.length < 1) {
            return false;
        }

        // the receiving individual
        Player challenged = Bukkit.getPlayer(args[0]);
        Player issuer = (Player) sender;

        // Cannot challenge yourself
        if (challenged == null) {
            Messenger.sendError( "Could not find player: <red>" + args[0], sender);
            return true;
        } else if (Objects.equals(sender, challenged)) {
            Messenger.sendError("Stuck in a fight against ourself are we? We should get some help.", sender);
            return true;
        }

        // Sender must be in a spawnroom
        if (!InCombat.isPlayerInLobby(issuer.getUniqueId())) {
            Messenger.sendError("You have to perform this command whilst in a spawnroom!", sender);
            return true;
        }

        if (arena != null) {
            Messenger.sendError("There is already a duel ongoing!", sender);
            return true;
        }

        // Check if there is a challenge against that player
        if (challenges.containsKey(challenged.getUniqueId())) {
            beginDuel(issuer, challenged);
        }

        // Player needs to be a noble to issue a challenge
        if (!sender.hasPermission("conwymc.noble") ) {
            Messenger.sendError("Only donators, of Noble rank or higher, can issue challenges!", sender);
        }

        // Check if the player has already challenged someone
        if (challenges.containsKey(issuer.getUniqueId())) {

            if (challenges.get(issuer.getUniqueId()).equals(challenged.getUniqueId())) {
                Messenger.sendDuel("You have rescinded your challenge", sender);
                Messenger.sendDuel(sender.getName() + " has rescinded their challenge", challenged);
                challenges.remove(issuer.getUniqueId());
                return true;
            }

            Messenger.sendError("You already have a challenge active!", sender);
            return true;
        }

        challenges.put(issuer.getUniqueId(), challenged.getUniqueId());
        Messenger.sendDuel("You challenged " + challenged.getName() + " to a duel!", sender);
        Messenger.sendDuel(sender.getName() + " has challenged you to a duel! " +
                "<yellow><click:suggest_command:/duel " + sender.getName() + ">/duel " + sender.getName() + "</click></yellow> to accept their challenge.", challenged);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (arena != null)
                    return;

                if (challenges.containsKey(issuer.getUniqueId()) && challenges.get(issuer.getUniqueId()).equals(challenged.getUniqueId())) {
                        Messenger.sendDuel("Your challenge to " + challenged.getName() + " has expired!", sender);
                        Messenger.sendDuel("The challenge from " + sender.getName() + " has expired.", challenged);
                }
                challenges.remove(issuer.getUniqueId());
            }
        }.runTaskLater(Main.plugin, 2400);

        return true;
    }

    private void beginDuel(Player challenger, Player challenged) {
        arena = new Arena(new Vector(100, 4, 100));

        arena.beginDuel(challenger, challenged);
    }

    public static boolean isDueling(UUID uuid) {
        if (arena == null)
            return false;
        return uuid.equals(arena.getGreenPlayer()) ||
                uuid.equals(arena.getCyanPlayer());
    }

    public static boolean isChallenging(UUID uuid) {
        return challenges.containsKey(uuid);
    }
}
