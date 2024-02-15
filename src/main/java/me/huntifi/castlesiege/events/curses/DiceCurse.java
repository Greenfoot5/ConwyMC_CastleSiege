package me.huntifi.castlesiege.events.curses;

import me.huntifi.castlesiege.commands.staff.StaffChat;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class DiceCurse extends CurseCast {
    public final static String name = "Curse of the Dice";
    private final static String activateMessage = "Some players have swapped teams!";
    private final static String expireMessage = "";
    private final static List<List<String>> OPTIONS = new ArrayList<>();
    private final static int MIN_SIZE_TO_SWAP = 3;

    private DiceCurse(CurseBuilder builder) {
        super(builder);
    }

    @Override
    protected void cast() {
        if (MapController.getCurrentMap().smallestTeam().getTeamSize() < MIN_SIZE_TO_SWAP) {
            StaffChat.sendMessage("Failed to activate Curse of the Dice: Not Enough Players");
            return;
        }

        this.setStartTime();

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

    //Builder Class
    public static class CurseBuilder extends CurseCast.CurseBuilder {

        public CurseBuilder() {
            super(name, activateMessage, expireMessage);
            this.options = OPTIONS;
        }

        public DiceCurse cast() {
            DiceCurse curse = new DiceCurse(this);
            Bukkit.getPluginManager().callEvent(curse);
            if (!curse.isCancelled())
                curse.cast();
            return curse;
        }
    }
}
