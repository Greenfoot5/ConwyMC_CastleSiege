package me.huntifi.castlesiege.maps;

import io.lumine.mythic.api.adapters.AbstractPlayer;
import io.lumine.mythic.core.players.factions.FactionProvider;

import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Checks if players are on the same team
 */
public class TeamController implements FactionProvider {

    private static final HashMap<UUID, Team> uuidToTeam = new HashMap<>();

    public static void joinTeam(UUID uuid, Team team) {
        uuidToTeam.put(uuid, team);
    }

    public static void leaveTeam(UUID uuid) {
        uuidToTeam.remove(uuid);
    }

    public static Team getTeam(UUID uuid) {
        return uuidToTeam.get(uuid);
    }

    @Override
    public boolean isInFaction(AbstractPlayer abstractPlayer, String s) {
        return Objects.equals(getTeam(abstractPlayer.getUniqueId()).getName(), s);
    }

    @Override
    public Optional<String> getFaction(AbstractPlayer player) {
        String team = getTeam(player.getUniqueId()).getName();
        if (team != null) return Optional.of(team);
        return Optional.empty();
    }

    @Override
    public Optional<String> getFaction(UUID uuid) {
        String team = getTeam(uuid).getName();
        if (team != null) return Optional.of(team);
        return Optional.empty();
    }
}
