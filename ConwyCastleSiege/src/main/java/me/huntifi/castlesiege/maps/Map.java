package me.huntifi.castlesiege.maps;

import me.huntifi.castlesiege.flags.Flag;

import java.util.UUID;

public class Map {
    public String name;
    public String worldName;
    public Team[] teams;
    public Flag[] flags;

    /**
     * Get the team a player is on
     * @param uuid The uuid to search for
     * @return The team (if any) that has the team, otherwise returns null
     */
    public Team getTeam(UUID uuid) {
        for (Team team : teams) {
            if (team.hasPlayer(uuid)) {
                return team;
            }
        }
        return null;
    }

    public Team getTeam(String teamName) {
        for (Team team : teams) {
            if (team.name.equals(teamName)) {
                return team;
            }
        }
        return null;
    }

    public Team smallestTeam()
    {
        Team smallest = teams[0];
        // Loop through the teams and find the smallest. If they are equal, returns the first team
        for (Team team : teams) {
            if (smallest.getTeamSize() < team.getTeamSize())
            {
                smallest = team;
            }
        }

        return smallest;
    }

    public Flag getFlag(String name)
    {
        for (Flag flag : flags)
        {
            if (flag.name.equalsIgnoreCase(name))
            {
                return flag;
            }
        }
        return null;
    }
}
