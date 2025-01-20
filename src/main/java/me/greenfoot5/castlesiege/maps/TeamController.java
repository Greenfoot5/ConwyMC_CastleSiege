package me.greenfoot5.castlesiege.maps;

import io.lumine.mythic.api.adapters.AbstractPlayer;
import io.lumine.mythic.core.players.factions.FactionProvider;
import me.greenfoot5.castlesiege.database.CSActiveData;
import me.greenfoot5.castlesiege.events.combat.InCombat;
import me.greenfoot5.castlesiege.kits.kits.Kit;
import me.greenfoot5.castlesiege.kits.kits.SignKit;
import me.greenfoot5.castlesiege.kits.kits.free_kits.Swordsman;
import me.greenfoot5.conwymc.util.Messenger;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

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
     */
    public static Set<UUID> getPlayers() {
        return uuidToTeam.keySet();
    }

    /**
     * Gets all the players not in their spawn
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

    public static Set<UUID> getSpectators() {
        return spectators;
    }


    /**
     * Get all players and spectators
     */
    public static Set<UUID> getEveryone() {
        Set<UUID> everyone = uuidToTeam.keySet();
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
            player.setGameMode(GameMode.SURVIVAL);
        }
    }

    /**
     * Adds a player to a team on the current map
     * @param uuid the player to add to a team
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

        uuidToTeam.put(uuid, team);

        Player player = Bukkit.getPlayer(uuid);
        assert player != null;
        player.teleport(team.lobby.spawnPoint);
        Messenger.send(Component.text("You joined ").append(team.getDisplayName()), player);

        checkTeamKit(player);
    }

    /**
     * Removes a player from their team
     * @param uuid The UUID of the player to remove
     */
    public static void leaveTeam(UUID uuid) {
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

    public static void loadTeams() {
        // We can and want to keep the teams the same
        if (keepTeams && MapController.getCurrentMap().teams.length >= kept_teams.size()) {

        }
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
