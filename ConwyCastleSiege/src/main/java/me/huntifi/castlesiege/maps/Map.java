package me.huntifi.castlesiege.maps;

import me.huntifi.castlesiege.flags.Flag;
import org.bukkit.Color;
import org.bukkit.entity.Player;

public abstract class Map {
    public Team[] teams;
    public Flag[] flags;
    public Color primaryColor;
    public Color secondaryColor;

    public Team GetTeam(Player player)
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
}
