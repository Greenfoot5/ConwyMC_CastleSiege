package me.greenfoot5.castlesiege.events.curses;

import me.greenfoot5.castlesiege.Main;
import me.greenfoot5.castlesiege.maps.TeamController;
import me.greenfoot5.conwymc.data_types.Tuple;
import me.greenfoot5.conwymc.util.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * A curse the swaps some player's positions
 */
public class TeleportationCurse extends CurseCast {
    public final static String name = "Curse of Teleportation";
    private final static String activateMessage = "Some players have swapped positions!";
    private final static String expireMessage = "";
    private final static List<List<String>> OPTIONS = new ArrayList<>();
    private final static int MIN_SIZE_TO_SWAP = 2;

    private TeleportationCurse(CurseBuilder builder) {
        super(builder);
    }

    @Override
    protected void cast() {
        List<UUID> totalPlayers = TeamController.getActivePlayers().stream().toList();


        if (totalPlayers.size() < MIN_SIZE_TO_SWAP) {
            Main.plugin.getLogger().warning("Failed to activate Curse of Teleportation: Not Enough Players");
            return;
        }

        this.setStartTime();


        Random random = new Random();
        int amountToSwap = random.nextInt((totalPlayers.size() - MIN_SIZE_TO_SWAP) + 1) + MIN_SIZE_TO_SWAP;
        Collections.shuffle(totalPlayers);

        Player[] playersToSwap = new Player[amountToSwap];
        List<Tuple<UUID, Location>> positionsNotUsed = new ArrayList<>();
        for (int i = 0; i < amountToSwap; i++) {
            playersToSwap[i] = Bukkit.getPlayer(totalPlayers.get(i));
            positionsNotUsed.add(new Tuple<>(totalPlayers.get(i), playersToSwap[i].getLocation()));
        }

        // Loop through all the players to swap and swap them
        for (Player value : playersToSwap) {
            Player p = swapPlayer(value, positionsNotUsed);
            positionsNotUsed.remove(p);
        }

        Messenger.broadcastCurse("<dark_red>" + name + "</dark_red> has been activated! " + activateMessage);
        playSound(TeamController.getPlayers());
    }

    /**
     * Swaps a player onto the next team
     * @param player The player to swap
     * @param positions The set of positions that can be picked from
     */
    private Player swapPlayer(Player player, List<Tuple<UUID, Location>> positions) {
        Random random = new Random();
        Tuple<UUID, Location> pos = positions.get(random.nextInt(positions.size()));
        while (pos.getFirst() == player.getUniqueId() && positions.size() > 1) {
            positions.remove(pos);
            pos = positions.get(random.nextInt(positions.size()));
        }

        if (pos.getFirst() == player.getUniqueId()) {
            return null;
        }

        player.teleport(pos.getSecond());

        Messenger.sendCurse("You have been affected by <dark_red>" + name + "</dark_red>! You have been teleported.", player);
        return Bukkit.getPlayer(pos.getFirst());
    }

    /**
     * Builder class to create a new Teleportation Curse
     */
    public static class CurseBuilder extends CurseCast.CurseBuilder {

        /**
         * Creates a new CurseBuilder for Teleportation
         */
        public CurseBuilder() {
            super(name, activateMessage, expireMessage);
            this.options = OPTIONS;
        }

        public void cast() {
            TeleportationCurse curse = new TeleportationCurse(this);
            Bukkit.getPluginManager().callEvent(curse);
            if (!curse.isCancelled())
                curse.cast();
        }
    }
}
