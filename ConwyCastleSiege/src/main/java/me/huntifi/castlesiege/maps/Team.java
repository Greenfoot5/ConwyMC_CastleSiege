package me.huntifi.castlesiege.maps;

import me.huntifi.castlesiege.data_types.PlayerData;
import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.database.MVPStats;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

/**
 * Represents a team on a map
 */
public class Team {
    // Basic Details
    public String name;
    private ArrayList<UUID> players;
    private ArrayList<NPC> bots;

    public Lobby lobby;

    // Colours
    public Material primaryWool;
    public Material secondaryWool;
    public ChatColor primaryChatColor;
    public ChatColor secondaryChatColor;

    /**
     * Creates a new team
     * @param name The name of the team
     */
    public Team(String name) {
        this.name = name;
        players = new ArrayList<>();
        bots = new ArrayList<>();
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
     */
    public void addPlayer(UUID uuid) {
        if (hasPlayer(uuid)) {
            return;
        }

        // We've done the checks, but it's easier to keep the adding in one place
        forceAddPlayer(uuid);
    }

    /**
     * Adds a player to a team without checking if they are on it.
     * @param uuid the uuid to add
     */
    public void forceAddPlayer(UUID uuid) {
        players.add(uuid);
        Player player = Bukkit.getPlayer(uuid);
        assert player != null;
        //player.setBedSpawnLocation(lobby.spawnPoint, true);
        NameTag.give(player);
    }

    /**
     * Removes a uuid from the team
     * @param uuid the uuid to remove
     */
    public void removePlayer(UUID uuid) {
        players.remove(uuid);
    }

    /**
     * Removes a uuid from the team
     * @param npc the npc to remove from the team
     */
    public void removeBot(NPC npc) {
        bots.remove(npc);
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
        return players.size() + bots.size();
    }

    /**
     * Gets the MVP for the current team
     * @return The unique ID and stats of the MVP, null if team is empty
     */
    public Tuple<UUID, PlayerData> getMVP() {
        Tuple<UUID, PlayerData> mvp = null;

        for (UUID uuid : players) {
            PlayerData data = MVPStats.getStats(uuid);
            if (mvp == null || data.getScore() > mvp.getSecond().getScore()) {
                mvp = new Tuple<>(uuid, data);
            }
        }

        return mvp;
    }

    /**
     * @return An ArrayList of all UUIDs on the time
     */
    public ArrayList<UUID> getPlayers() {
        return players;
    }

    /**
     * @return An ArrayList of all bots on the time
     */
    public ArrayList<NPC> getBots() {
        return bots;
    }

    /**
     * Clears the team's members
     */
    public void clear() {
        players = new ArrayList<>();
        bots = new ArrayList<>();
    }


    /**
     * Checks if a npc is on the team or not
     * @param npc the npc to check
     * @return true if the player is on the team
     */
    public boolean hasBot(NPC npc) {

        if (getTeamSize() < 1 || npc == null)
            return false;

        for (NPC teamMember: bots) {
            if (teamMember == npc) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a npc to a team without checking if they are on it.
     * @param npc
     */
    public void forceAddBot(NPC npc) {
        bots.add(npc);
        assert npc != null;
    }

    /**
     * Attempts to add a bot to the team.
     * Will fail if the bot is already on the team
     * @param npc the bot to add
     */
    public void addBot(NPC npc) {
        if (hasBot(npc)) {
            return;
        }

        // We've done the checks, but it's easier to keep the adding in one place
        forceAddBot(npc);
    }
}
