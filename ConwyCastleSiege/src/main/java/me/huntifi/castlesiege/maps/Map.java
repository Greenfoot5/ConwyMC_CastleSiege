package me.huntifi.castlesiege.maps;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.maps.objects.Catapult;
import me.huntifi.castlesiege.maps.objects.Door;
import me.huntifi.castlesiege.maps.objects.Flag;
import me.huntifi.castlesiege.maps.objects.Gate;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;
import java.util.UUID;

/**
 * A class to represent a single playable map
 */
public class Map {
    public String name;
    public String worldName;
    public int startTime;
    public boolean daylightCycle;

    //map border
    public double northZ;
    public double southZ;
    public double westX;
    public double eastX;

    public Team[] teams;
    public Flag[] flags;
    public Door[] doors;
    public Gate[] gates;

    public Catapult[] catapults;
    public Gamemode gamemode;
    public Tuple<Integer, Integer> duration = new Tuple<>(20, 0);

    public Map() {
        teams = new Team[0];
        flags = new Flag[0];
        doors = new Door[0];
        gates = new Gate[0];
        catapults = new Catapult[0];
    }

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

    /**
     * Get the team from a team name
     * @param teamName The string name of the team
     * @return The team, or null if none is found
     */
    public Team getTeam(String teamName) {
        if (teamName == null) {
            return null;
        }

        for (Team team : teams) {
            if (team.name.equals(teamName)) {
                return team;
            }
        }
        return null;
    }

    /**
     * Gets the team with the smallest player count
     * @return the smallest team
     */
    public Team smallestTeam()
    {
        Team smallest = teams[1];
        // Loop through the teams and find the smallest. If they are equal, returns the second team
        for (Team team : teams) {
            if (smallest.getTeamSize() > team.getTeamSize())
            {
                smallest = team;
            }
        }

        return smallest;
    }

    /**
     * Gets a flag based on a name
     * @param name the name of the flag
     * @return the flag, null if none was found
     */
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

    /**
     * Updates all WoolMaps of a flag to the correct wool
     * @param flagName The flag to update
     */
    public void updateWoolMaps(String flagName) {
        for (Team team : teams) {
            for (WoolMapBlock block : team.lobby.woolmap.woolMapBlocks) {
                if (Objects.equals(block.flagName, flagName)) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            block.blockLocation.getBlock().setType(getFlag(flagName).GetWoolMapBlock());
                        }
                    }.runTask(Main.plugin);
                }
            }
        }
    }


    public void setMapBorder(double east, double south, double north, double west) {
        northZ = north;
        southZ = south;
        westX = west;
        eastX = east;
    }


}
