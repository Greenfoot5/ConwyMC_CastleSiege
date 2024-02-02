package me.huntifi.castlesiege.events.curses;

import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class DiceCurse extends Curse {
    public final static String name = "Curse of the Dice";
    private final static String activateMessage = "Some players have swapped teams!";
    private final static String expireMessage = "";
    private final static int MIN_SIZE_TO_SWAP = 3;
    public DiceCurse() {
        super(name, activateMessage, expireMessage, new String[0][0],0);
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
        int index = 0;
        for (Team team : MapController.getCurrentMap().teams) {
            ArrayList<UUID> players = team.getPlayers();
            for (int i = 0; i < amountToSwap; i++) {
                UUID nextPlayer = players.get(random.nextInt(players.size()));
                players.remove(nextPlayer);
                playersToSwap[index][i] = Bukkit.getPlayer(nextPlayer);
            }
            index += 1;
        }

        for (int i = 0; i < playersToSwap.length; i++) {
            for (Player player : playersToSwap[i]) {
                MapController.getCurrentMap().teams[i].removePlayer(player.getUniqueId());
                if (i == playersToSwap.length - 1) {
                    MapController.getCurrentMap().teams[0].addPlayer(player.getUniqueId());
                } else {
                    MapController.getCurrentMap().teams[i + 1].addPlayer(player.getUniqueId());
                }
                Messenger.sendCurse("You have been affected by " + name + "! You have swapped teams.", player);
            }
        }

        Messenger.broadcastCurse(ChatColor.DARK_RED + name + "§r has been activated! " + activateMessage);
        return true;
    }

    @Override
    public boolean activateCurse(Player player) {
        return false;
    }
}
