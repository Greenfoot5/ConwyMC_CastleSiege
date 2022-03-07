package me.huntifi.castlesiege.maps;

import me.huntifi.castlesiege.flags.Flag;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import static org.bukkit.Bukkit.getServer;

public class Map {
    public String name;
    public String worldName;
    public Team[] teams;
    public Flag[] flags;

    /**
     * Get the team a player is on
     * @param player The player to search for
     * @return The team (if any) that has the team, otherwise returns null
     */
    public Team getTeam(Player player) {
        for (Team team : teams) {
            System.out.println(team);
            if (team.hasPlayer(player)) {
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
            System.out.println("[TheDarkAge] tt: " + team.name);
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
            if (flag.name.equals(name))
            {
                return flag;
            }
        }
        return null;
    }
}
