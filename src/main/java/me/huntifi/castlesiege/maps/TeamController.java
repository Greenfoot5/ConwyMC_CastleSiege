package me.huntifi.castlesiege.maps;

import java.util.HashMap;
import java.util.UUID;

public class TeamController {

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
}
