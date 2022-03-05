package me.huntifi.castlesiege.maps;

import me.huntifi.castlesiege.flags.Flag;
import org.bukkit.entity.Player;

public abstract class Map {
    public String name;
    public Team[] teams;
    public Flag[] flags;

    /**
     * Get the team a player is on
     * @param player The player to search for
     * @return The team (if any) that has the team, otherwise returns null
     */
    public Team getTeam(Player player)
    {
        for (Team team:
             teams) {
            if (team.hasPlayer(player))
            {
                return team;
            }
        }
        return null;
    }

    public Team smallestTeam()
    {
        Team smallest = teams[0];
        // Loop through the teams and find the smallest. If they are equal, returns the first team
        for (Team team: teams) {
            if (smallest.getTeamSize() < team.getTeamSize())
            {
                smallest = team;
            }
        }

        return smallest;
    }
}
