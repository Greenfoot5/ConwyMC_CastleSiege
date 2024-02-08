package me.huntifi.castlesiege.events.curses;

import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class DiceCurse extends CurseCast {
    public final static String name = "CurseCast of the Dice";
    private final static String activateMessage = "Some players have swapped teams!";
    private final static String expireMessage = "";
    private final static int MIN_SIZE_TO_SWAP = 3;
    public DiceCurse() {
        super(name, activateMessage, expireMessage, new ArrayList<>(),0);
    }

    @Override
    public boolean activateCurse() {
        if (MapController.getCurrentMap().smallestTeam().getTeamSize() < MIN_SIZE_TO_SWAP) {
            return false;
        }

        super.startTime = System.currentTimeMillis() / 1000;
        Random random = new Random();
        int amountToSwap = random.nextInt(MapController.getCurrentMap().smallestTeam().getTeamSize() / 2);

        Player[][] playersToSwap = new Player[MapController.getCurrentMap().teams.length][amountToSwap];
        // Loop through teams and pick random players to get
        Team[] teams = MapController.getCurrentMap().teams;
        for (int t = 0; t < teams.length; t++) {
            Team team = teams[t];
            ArrayList<UUID> players = team.getPlayers();
            for (int i = 0; i < amountToSwap; i++) {
                UUID nextPlayer = players.get(random.nextInt(players.size()));
                // Make sure we don't pick duplicate players
                players.remove(nextPlayer);
                playersToSwap[t][i] = Bukkit.getPlayer(nextPlayer);
            }
        }

        // Loop through all the players to swap and swap them
        for (int i = 0; i < playersToSwap.length; i++) {
            for (Player player : playersToSwap[i]) {
                swapPlayer(player, i);
            }
        }

        Messenger.broadcastCurse(ChatColor.DARK_RED + name + "§r has been activated! " + activateMessage);
        return true;
    }

    /**
     * Swaps a player onto the next team
     * @param player The player to swap
     * @param teamIndex The index of the team they are on
     */
    private void swapPlayer(Player player, int teamIndex) {
        MapController.getCurrentMap().teams[teamIndex].removePlayer(player.getUniqueId());
        int nextTeam = teamIndex + 1 == MapController.getCurrentMap().teams.length ? 0 : teamIndex + 1;

        MapController.getCurrentMap().teams[nextTeam].addPlayer(player.getUniqueId());
        if (InCombat.isPlayerInLobby(player.getUniqueId())) {
            player.teleport(MapController.getCurrentMap().teams[nextTeam].lobby.spawnPoint);
        }

        Messenger.sendCurse("You have been affected by " + ChatColor.DARK_RED + name + "§r! You have swapped teams.", player);
    }

    @Override
    public boolean activateCurse(Player player) {
        return false;
    }
}
