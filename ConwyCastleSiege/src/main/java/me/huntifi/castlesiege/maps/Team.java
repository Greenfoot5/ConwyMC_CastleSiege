package me.huntifi.castlesiege.maps;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Random;

public class Team {
    // Basic Details
    public String name;
    private ArrayList<Player> players;
    public Lobby lobby;

    // Colours
    public Material primaryWool;
    public Material secondaryWool;
    public ChatColor primaryChatColor;
    public ChatColor secondaryChatColor;

    public Team() {
        players = new ArrayList<>();
    }


    /**
     * Checks if a player is on the team or not
     * @param player the player to check
     * @return true if the player is on the team
     */
    public boolean hasPlayer(Player player) {
        if (getTeamSize() < 1)
            return false;

        for (Player teamMember: players) {
            if (teamMember == player) {
                return true;
            }
        }
        return false;
    }

    /**
     * Attempts to add a player to the team.
     * Will fail if the player is already on the team
     * @param player the player to add
     * @return true if the player was added, false if they were already on the team
     */
    public boolean addPlayer(Player player) {
        if (hasPlayer(player)) {
            return false;
        }

        players.add(player);
        return true;
    }

    /**
     * Adds a player to a team without checking if they are on it.
     * @param player the player to add
     */
    public void forceAddPlayer(Player player) {
        players.add(player);
    }

    /**
     * Removes a player from the team
     * @param player the player to remove
     * @return true if the player was removed, false if the player wasn't on the team
     */
    public boolean removePlayer(Player player) {
        if (!hasPlayer(player)) {
            return false;
        }

        players.remove(player);
        return true;
    }

    /**
     * Gets a random player on the team
     * @return a random player
     */
    public Player getRandomPlayer() {
        return players.get(new Random().nextInt(players.size()));
    }

    /**
     * Gets the current size of the team
     * @return How many players are on the team
     */
    public int getTeamSize() {
        return players.size();
    }

    /**
     * Gets the MVP for the current team
     */
    public void getMVP() {}

    /**
     * Clears the team's members
     */
    public void clear() {
        players = new ArrayList<>();
    }
}
