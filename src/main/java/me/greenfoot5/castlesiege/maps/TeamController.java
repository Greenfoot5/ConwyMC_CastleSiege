package me.greenfoot5.castlesiege.maps;

import io.lumine.mythic.api.adapters.AbstractPlayer;
import io.lumine.mythic.core.players.factions.FactionProvider;

import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Manages the players on Castle Siege
 */
public class TeamController implements FactionProvider {

    private static final HashMap<UUID, Team> uuidToTeam = new HashMap<>();

    /**
     * Adds a player to a team. If they are already on a team, removes the player from their team.
     * @param uuid The UUID of the player to add to a team
     * @param team The team to add the player to
     */
    public static void joinTeam(UUID uuid, Team team) {
        uuidToTeam.put(uuid, team);
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
    public boolean isInFaction(AbstractPlayer abstractPlayer, String s) {
        return Objects.equals(getTeam(abstractPlayer.getUniqueId()).getName(), s);
    }

    /**
     * Gets the team name of a player
     * @param player The player to check for
     * @return An Optional of the team's name
     */
    @Override
    public Optional<String> getFaction(AbstractPlayer player) {
        String team = getTeam(player.getUniqueId()).getName();
        if (team != null) return Optional.of(team);
        return Optional.empty();
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
