package me.huntifi.castlesiege.commands.donator.duels;

import com.nametagedit.plugin.NametagEdit;
import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.maps.NameTag;
import me.huntifi.castlesiege.maps.TeamController;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;


public class AcceptDuel implements CommandExecutor {

    //These booleans determine whether an arena is occupied or not
    boolean arena1 = false;
    boolean arena2 = false;
    boolean arena3 = false;

    //The locations to spawn the challenger and the contender at, when a duel is initiated.
    Location arenaChallenger1 = new Location(Bukkit.getWorld("DuelsMap"), -168, 4, -29, 90, 0);
    Location arenaChallenger2 = new Location(Bukkit.getWorld("DuelsMap"), -175, 4, -107, 90, 0);
    Location arenaChallenger3 = new Location(Bukkit.getWorld("DuelsMap"), -174, 4, -173, 90, 0);
    Location arenaContender1 = new Location(Bukkit.getWorld("DuelsMap"), -210, 4, -29, -90, 0);
    Location arenaContender2 = new Location(Bukkit.getWorld("DuelsMap"), -217, 4, -107, -90, 0);
    Location arenaContender3 = new Location(Bukkit.getWorld("DuelsMap"), -216, 4, -173, -90, 0);

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

        if (arena1 && arena2 && arena3) {
            Messenger.sendSuccess("You accepted " + challenger.getName() + "'s invitation to a duel. However all arena's are currently " +
                    "in use. Our apologies.", sender);
            Messenger.sendSuccess(sender.getName() + " has accepted your invitation to a duel! However all arena's are currently " +
            "in use. Our apologies.", challenger);
            return true;
        }

        //Hashmap that has the data on whether someone is invited by a certain someone.
        DuelCmd.inviter.remove(sender, challenger);
        DuelCmd.challenging.put((Player) sender, challenger);
        Messenger.sendSuccess("You accepted " + challenger.getName() + "'s invitation to a duel.", sender);
        Messenger.sendSuccess(sender.getName() + " has accepted your invitation to a duel!", challenger);
        onDuelInitation(challenger, (Player) sender);
        return true;
    }

    /**
     * Duel process is initiated
     * @param challenger the person who challenged the contender
     * @param contender the person who accepted the challenge from the challenger
     */
    public void onDuelInitation(Player challenger, Player contender) {
          teleportContestants(challenger, contender);
          forceDuelTeam(challenger, contender);
          InCombat.playerSpawned(challenger.getUniqueId());
          InCombat.playerSpawned(contender.getUniqueId());
    }

    /**
     *
     * @param challenger the person who challenged the contender
     * @param contender the person who accepted the challenge from the challenger
     */
    public void teleportContestants(Player challenger, Player contender) {
        if (!arena1) {
        challenger.teleport(arenaChallenger1);
        contender.teleport(arenaContender1);
        } else if (!arena2) {
            challenger.teleport(arenaChallenger2);
            contender.teleport(arenaContender2);
        } else if (!arena3) {
            challenger.teleport(arenaChallenger3);
            contender.teleport(arenaContender3);
        }

    }

    /**
     *
     * @param challenger the person who challenged the contender
     * @param contender the person who accepted the challenge from the challenger
     */
    public void forceDuelTeam(Player challenger, Player contender) {
        TeamController.leaveTeam(challenger.getUniqueId());
        TeamController.leaveTeam(contender.getUniqueId());
        NametagEdit.getApi().setPrefix(challenger, determineColor(challenger));
        NametagEdit.getApi().setPrefix(contender, determineColor(contender));
    }

    /**
     *
     * @param participant the player to check the colour for
     * @return the colour blue for the challenger, red for the contender
     */
    public static String determineColor(Player participant) {
        if (DuelCmd.challenging.containsKey(participant)) {
            return ChatColor.BLUE.toString();
        } else {
            return ChatColor.RED.toString();
        }
    }
}
