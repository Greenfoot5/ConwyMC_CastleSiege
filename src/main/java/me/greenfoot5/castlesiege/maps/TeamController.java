package me.greenfoot5.castlesiege.maps;

import io.lumine.mythic.api.adapters.AbstractPlayer;
import io.lumine.mythic.core.players.factions.FactionProvider;
import me.greenfoot5.castlesiege.Main;
import me.greenfoot5.castlesiege.database.CSActiveData;
import me.greenfoot5.castlesiege.database.MVPStats;
import me.greenfoot5.castlesiege.database.StoreData;
import me.greenfoot5.castlesiege.events.combat.InCombat;
import me.greenfoot5.castlesiege.kits.kits.Kit;
import me.greenfoot5.castlesiege.kits.kits.SignKit;
import me.greenfoot5.castlesiege.kits.kits.free_kits.Swordsman;
import me.greenfoot5.conwymc.events.nametag.UpdateNameTagEvent;
import me.greenfoot5.conwymc.util.Messenger;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.bukkit.Bukkit.getPlayer;

/**
 * Manages the players on Castle Siege
 */
public class TeamController implements FactionProvider {

    // If we should keep the teams the same for each map
    public static boolean keepTeams = false;
    // Stop players swapping teams (forceswitching is allowed)
    public static boolean disableSwitching = false;

    private static final Set<UUID> spectators = new HashSet<>();
    // Contains all players and their team
    private static final HashMap<UUID, Team> uuidToTeam = new HashMap<>();

    /**
     * Checks if a player is playing castle siege
     * @param player The player to check
     * @return true if they are on a team
     */
    public static boolean isPlaying(Player player) {
        return isPlaying(player.getUniqueId());
    }

    /**
     * Checks if a player is playing castle siege
     * @param uuid The uuid of the player to check
     * @return true if they are on a team
     */
    public static boolean isPlaying(UUID uuid) {
        return uuidToTeam.containsKey(uuid);
    }

    /**
     * Checks if a player is spectating castle siege
     * @param player The player to check
     * @return true if they are spectating
     */
    public static boolean isSpectating(Player player) {
        return isSpectating(player.getUniqueId());
    }

    /**
     * Checks if a player is spectating castle siege
     * @param uuid The uuid of the player to check
     * @return true if they are spectating
     */
    public static boolean isSpectating(UUID uuid) {
        return spectators.contains(uuid);
    }

    /**
     * Gets all the players current playing
     * @return A set of UUIDs, one for each player
     */
    public static Set<UUID> getPlayers() {
        return uuidToTeam.keySet();
    }

    /**
     * Gets all the players not in their spawn
     * @return A set of UUIDs, one for each player not in their lobby
     */
    public static Set<UUID> getActivePlayers() {
        Set<UUID> activePlayers = new HashSet<>();
        for (UUID uuid : uuidToTeam.keySet()) {
            if (!InCombat.isPlayerInLobby(uuid)) {
                activePlayers.add(uuid);
            }
        }
        return activePlayers;
    }

    /**
     * Gets all the spectators
     * @return A set of UUIDs, one for each spectator
     */
    public static Set<UUID> getSpectators() {
        return spectators;
    }


    /**
     * Get all players and spectators
     * @return A set of UUIDs, one for each player/spectator
     */
    public static Set<UUID> getEveryone() {
        Set<UUID> everyone = new HashSet<>();
        everyone.addAll(uuidToTeam.keySet());
        everyone.addAll(spectators);
        return everyone;
    }
    /**
     * Makes the player a spectator
     * @param player The player to make a spectator
     */
    public static void joinSpectator(Player player) {
        leaveTeam(player.getUniqueId());
        spectators.add(player.getUniqueId());

        player.setGameMode(GameMode.SPECTATOR);
    }

    /**
     * Removes a player from being a spectator
     * @param uuid The of the player
     */
    public static void leaveSpectator(UUID uuid) {
        spectators.remove(uuid);

        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            Scoreboard.clearScoreboard(player);
            player.setGameMode(GameMode.SURVIVAL);
        }
    }

    /**
     * Adds a player to a team on a map
     * @param uuid the player to add to a team
     * @param map The map to add players to
     */
    public static void joinSmallestTeam(UUID uuid, Map map) {
        joinTeam(uuid, map.smallestTeam());
    }

    /**
     * Adds a player to a team. If they are already on a team, removes the player from their team.
     * @param uuid The UUID of the player to add to a team
     * @param team The team to add the player to
     */
    public static void joinTeam(UUID uuid, Team team) {
        if (isPlaying(uuid)) {
            leaveTeam(uuid);
        } else if (isSpectating(uuid)) {
            leaveSpectator(uuid);
        }

        Player player = Bukkit.getPlayer(uuid);
        assert player != null;

        team.addPlayer(uuid);
        uuidToTeam.put(uuid, team);
        MVPStats.addPlayer(uuid);

        player.teleport(team.lobby.spawnPoint);

        Messenger.send(Component.text("You joined ").append(team.getDisplayName()), player);
        Bukkit.getPluginManager().callEvent(new UpdateNameTagEvent(player));

        checkTeamKit(player);
    }

    /**
     * Removes a player from their team
     * @param uuid The UUID of the player to remove
     */
    public static void leaveTeam(UUID uuid) {
        Team team = uuidToTeam.get(uuid);
        if (team != null)
            team.removePlayer(uuid);

        InCombat.playerDied(uuid);
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            Scoreboard.clearScoreboard(player);
        }

        // Only bother async saving if the plugin has loaded
        if (Main.instance.hasLoaded) {
            Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
                try { StoreData.store(uuid); } catch (SQLException ignored) { }
            });
        } else {
            try { StoreData.store(uuid); } catch (SQLException ignored) { }
        }


        uuidToTeam.remove(uuid);
    }

    /**
     * Gets the team of a player
     * @param uuid The UUID of the player
     * @return The team if the player is on one. Returns null otherwise.
     */
    public static Team getTeam(UUID uuid) {
        return uuidToTeam.get(uuid);
    }

    /**
     * Loads the teams from one map to another
     * @param oldMap The map to load teams from
     * @param newMap The map to populate the teams for
     */
    public static void loadTeams(Map oldMap, Map newMap) {
        // We can and want to keep the teams the same
        if (keepTeams && newMap.teams.length >= oldMap.teams.length) {
            // Shuffle teams to avoid attackers always being attackers
            List<Team> oldTeams = Arrays.asList(oldMap.teams);
            Collections.shuffle(oldTeams);
            oldMap.teams = oldTeams.toArray(oldMap.teams);

            // Move player onto their new team
            for (int t = 0; t < oldMap.teams.length; t++) {
                for (UUID uuid : oldMap.teams[t].getPlayers()) {
                    joinTeam(uuid, newMap.teams[t]);
                }
                oldMap.teams[t].clear();
            }

            return;
        }

        loadTeams(newMap);
    }

    /**
     * Loads all players onto a new map
     * @param newMap The map to add players to
     */
    public static void loadTeams(Map newMap) {
        Set<UUID> players = new HashSet<>(uuidToTeam.keySet());
        for (UUID uuid : players) {
            joinSmallestTeam(uuid, newMap);
        }
    }

    /**
     * Teleports all players to the current map
     */
    public static void teleportPlayers() {
        for (UUID uuid : getPlayers()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                player.teleport(TeamController.getTeam(uuid).lobby.spawnPoint);
            }
        }

        // Teleport Spectators
        for (UUID spectator : TeamController.getSpectators()) {
            Player player = getPlayer(spectator);
            if (player != null) {
                if (MapController.getCurrentMap() instanceof CoreMap coreMap) {
                    player.teleport(coreMap.getCore(1).getSpawnPoint());
                } else {
                    player.teleport(MapController.getCurrentMap().flags[0].getSpawnPoint());
                }
            }
        }
    }

    /**
     * Restocks the players kit, or sets them to swordsman if they were using a team kit
     * @param player The player to restock/reset kits for
     */
    private static void checkTeamKit(Player player) {
        Kit kit = Kit.equippedKits.get(player.getUniqueId());
        if (kit == null)
            return;

        if (kit instanceof SignKit signKit && !signKit.canSelect(player, true, false, false)) {
            Kit.equippedKits.put(player.getUniqueId(), new Swordsman());
            CSActiveData.getData(player.getUniqueId()).setKit("swordsman");
        }

        Kit.equippedKits.get(player.getUniqueId()).setItems(true);
    }

    /**
     * Checks if a player is in a specific faction
     * @param abstractPlayer The player to check for
     * @param factionName The name of the faction (team)
     * @return true if the player is in that faction
     */
    @Override
    public boolean isInFaction(AbstractPlayer abstractPlayer, String factionName) {
        return Objects.equals(getTeam(abstractPlayer.getUniqueId()).getName(), factionName);
    }

    /**
     * Gets the team name of a player
     * @param player The player to check for
     * @return An Optional of the team's name
     */
    @Override
    public Optional<String> getFaction(AbstractPlayer player) {
        return getFaction(player.getUniqueId());
    }

    /**
     * Gets the team name of a player
     * @param uuid The player's UUID to check for
     * @return An Optional of the team's name
     */
    @Override
    public Optional<String> getFaction(UUID uuid) {
        String team = getTeam(uuid).getName();
        if (team != null) return Optional.of(team);
        return Optional.empty();
    }
}
