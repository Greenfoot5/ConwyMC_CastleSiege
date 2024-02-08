package me.huntifi.castlesiege.maps;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.MapBorder;
import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.maps.objects.*;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

/**
 * A class to represent a single playable map
 */
public class Map {
    public String name;
    public String worldName;
    public int startTime;
    public boolean daylightCycle;

    private MapBorder mapBorder;

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
     * Gets a ram based on its region
     * @param region The ram's region
     * @return The ram, null if none was found
     */
    public Ram getRam(ProtectedRegion region) {
        for (Gate gate : gates) {
            Ram ram = gate.getRam();
            if (ram != null && ram.getRegion().equals(region))
                return ram;
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
                            block.blockLocation.getBlock().setType(getFlag(flagName).getWoolMapBlock());
                        }
                    }.runTask(Main.plugin);
                }
            }
        }
    }

    /**
     * Set this map's border.
     * @param north The north side of the border
     * @param east The east side of the border
     * @param south The south side of the border
     * @param west The west side of the border
     */
    public void setMapBorder(double north, double east, double south, double west) {
        mapBorder = new MapBorder(north, east, south, west);
    }

    /**
     * Get this map's border.
     * @return This map's border
     */
    public MapBorder getMapBorder() {
        return mapBorder;
    }

    /**
     * Checks if the current map has ended
     * @return If all the flags belong to the same team or if the enemy cores are destroyed.
     */
    public static Boolean hasMapEnded() {
        if (MapController.timer.state == TimerState.ENDED) {
            return true;
        }

        if (MapController.getCurrentMap() instanceof CoreMap) {
            CoreMap map = (CoreMap) MapController.getCurrentMap();
            for (Core core : map.getCores()) {
                if (core.health <= 0) {
                    return true;
                }
            }
            return false;
        }

        String startingTeam = MapController.getCurrentMap().flags[0].getCurrentOwners();
        if (startingTeam == null) {
            return false;
        }
        for (Flag flag : MapController.getCurrentMap().flags) {
            if (!startingTeam.equalsIgnoreCase(flag.getCurrentOwners()) && flag.isActive()) {
                return false;
            }
        }

        return true;
    }
}
