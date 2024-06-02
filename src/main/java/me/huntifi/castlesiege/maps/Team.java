package me.huntifi.castlesiege.maps;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.CSStats;
import me.huntifi.castlesiege.database.MVPStats;
import me.huntifi.castlesiege.kits.kits.SignKit;
import me.huntifi.conwymc.data_types.Tuple;
import me.huntifi.conwymc.events.nametag.UpdateNameTagEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Represents a team on a map
 */
public class Team implements Listener {
    // Basic Details
    public final String name;
    private ArrayList<UUID> players;

    public Lobby lobby;
    public HashMap<String, SignKit.CostType> kits = new HashMap<>();

    // Colours
    public Material primaryWool;
    public Material secondaryWool;
    public NamedTextColor primaryChatColor;
    public NamedTextColor secondaryChatColor;

    /**
     * @param event Prevents players from spawning at their bed when they sleep in a bad.
     */
    @EventHandler
    public void onEnterBed(PlayerBedEnterEvent event) {
        Player player = event.getPlayer();
        event.useBed();
        if (this.hasPlayer(event.getPlayer().getUniqueId())) {
            player.addPotionEffect((new PotionEffect(PotionEffectType.REGENERATION, 120, 6)));
            player.setRespawnLocation(this.lobby.spawnPoint, true);
        }
    }

    /**
     * Creates a new team
     * @param name The name of the team
     */
    public Team(String name) {
        this.name = name;
        players = new ArrayList<>();
        Bukkit.getPluginManager().registerEvents(this, Main.plugin);
    }


    /**
     * Checks if a player is on the team or not
     * @param uuid the uuid of the player to check
     * @return true if the player is on the team
     */
    public boolean hasPlayer(UUID uuid) {
        return players.contains(uuid);
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
        TeamController.joinTeam(uuid, this);
        Player player = Bukkit.getPlayer(uuid);
        assert player != null;
        if (lobby.spawnPoint.getWorld() == null)
            lobby.spawnPoint.setWorld(Bukkit.getWorld(MapController.getCurrentMap().worldName));
        player.setRespawnLocation(lobby.spawnPoint, true);
        Bukkit.getPluginManager().callEvent(new UpdateNameTagEvent(player));
    }

    /**
     * Removes a uuid from the team
     * @param uuid the uuid to remove
     */
    public void removePlayer(UUID uuid) {
        players.remove(uuid);
        TeamController.leaveTeam(uuid);
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
     * @return The unique ID and stats of the MVP, null if team is empty
     */
    public Tuple<UUID, CSStats> getMVP() {
        Tuple<UUID, CSStats> mvp = null;

        for (UUID uuid : players) {
            CSStats data = MVPStats.getStats(uuid);
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
     * Clears the team's members
     */
    public void clear() {
        players = new ArrayList<>();
    }
}
