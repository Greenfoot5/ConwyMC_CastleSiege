package me.huntifi.castlesiege.maps;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class Team {
    // Basic Details
    public String name;
    private ArrayList<UUID> players;
    public Lobby lobby;

    // Colours
    public Material primaryWool;
    public Material secondaryWool;
    public ChatColor primaryChatColor;
    public ChatColor secondaryChatColor;

    public Team(String name) {
        this.name = name;
        players = new ArrayList<>();
    }


    /**
     * Checks if a player is on the team or not
     * @param uuid the uuid of the player to check
     * @return true if the player is on the team
     */
    public boolean hasPlayer(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (getTeamSize() < 1 || player == null)
            return false;

        for (UUID teamMember: players) {
            if (teamMember == uuid) {
                return true;
            }
        }
        return false;
    }

    /**
     * Attempts to add a player to the team.
     * Will fail if the player is already on the team
     * @param uuid the uuid to add
     * @return true if the uuid was added, false if they were already on the team
     */
    public boolean addPlayer(UUID uuid) {
        if (hasPlayer(uuid)) {
            return false;
        }

        players.add(uuid);
        return true;
    }

    /**
     * Adds a player to a team without checking if they are on it.
     * @param uuid the uuid to add
     */
    public void forceAddPlayer(UUID uuid) {
        players.add(uuid);
    }

    /**
     * Removes a uuid from the team
     * @param uuid the uuid to remove
     * @return true if the uuid was removed, false if the uuid wasn't on the team
     */
    public boolean removePlayer(UUID uuid) {
        if (!hasPlayer(uuid)) {
            return false;
        }

        players.remove(uuid);
        return true;
    }

    /**
     * Gets a random player on the team
     * @return the uuid of a random player
     */
    public UUID getRandomUUID() {
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
    public void getMVP() {
        // TODO - Setup MVP
    }

    /**
     * Clears the team's members
     */
    public void clear() {
        players = new ArrayList<>();
    }
}
