package me.greenfoot5.castlesiege.events.curses;

import me.greenfoot5.castlesiege.Main;
import me.greenfoot5.castlesiege.events.combat.InCombat;
import me.greenfoot5.castlesiege.maps.MapController;
import me.greenfoot5.castlesiege.maps.Team;
import me.greenfoot5.castlesiege.maps.TeamController;
import me.greenfoot5.conwymc.util.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Swaps some players on teams
 */
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
            Main.plugin.getLogger().warning("Failed to activate Curse of the Dice: Not Enough Players");
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

        Messenger.broadcastCurse("<dark_red>" + name + "</dark_red> has been activated! " + activateMessage);
        playSound(TeamController.getPlayers());
    }

    /**
     * Swaps a player onto the next team
     * @param player The player to swap
     * @param teamIndex The index of the team they are on
     */
    private void swapPlayer(Player player, int teamIndex) {
        int nextTeam = teamIndex + 1 == MapController.getCurrentMap().teams.length ? 0 : teamIndex + 1;

        TeamController.joinTeam(player.getUniqueId(), MapController.getCurrentMap().teams[nextTeam]);
        if (InCombat.isPlayerInLobby(player.getUniqueId())) {
            player.teleport(MapController.getCurrentMap().teams[nextTeam].lobby.spawnPoint);
        }

        Messenger.sendCurse("You have been affected by <dark_red>" + name + "</dark_red>! You have swapped teams.", player);
    }

    /**
     * Builder class to create a new Dice Curse
     */
    public static class CurseBuilder extends CurseCast.CurseBuilder {

        /**
         * Creates a new CurseBuilder for Dice
         */
        public CurseBuilder() {
            super(name, activateMessage, expireMessage);
            this.options = OPTIONS;
        }

        public void cast() {
            DiceCurse curse = new DiceCurse(this);
            Bukkit.getPluginManager().callEvent(curse);
            if (!curse.isCancelled())
                curse.cast();
        }
    }
}
