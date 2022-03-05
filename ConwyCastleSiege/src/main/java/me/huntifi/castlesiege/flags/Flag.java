package me.huntifi.castlesiege.flags;

import me.huntifi.castlesiege.flags.captureareas.CaptureArea;
import me.huntifi.castlesiege.maps.Team;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;

// you need to decide what every flag has
// (capture area,
// spawn point,
// starting team,
// primary/secondary colour for each team)
public class Flag {
    public String name;

    // Color Data
    public Color primaryColor;
    public Color secondaryColor;
    public ChatColor primaryChatColor;
    public ChatColor secondaryChatColor;

    // Location Data
    public Location spawnPoint;
    public CaptureArea captureArea;

    // Game Data
    public Team currentOwners;
    private int maxCap = 13;
    public int captureStatus;
    public Team cappingTeam;
    public Flag(String name, Team startingTeam, int maxCapValue)
    {
        this.name = name;
        this.currentOwners = startingTeam;
        this.maxCap = maxCapValue;
    }

    /**
     * Attempts to change the team holding the flag.
     * @param newTeam The new flag owners
     * @return true if the team was changed, false if invalid team
     */
    public boolean ChangeTeam(Team newTeam)
    {
        if (currentOwners == null && newTeam != null)
        {
            currentOwners = newTeam;
            primaryColor = newTeam.primaryColor;
            secondaryColor = newTeam.secondaryColor;
            primaryChatColor = newTeam.primaryChatColor;
            secondaryChatColor = newTeam.secondaryChatColor;
            return true;
        }
        else if (currentOwners != null && newTeam == null)
        {
            currentOwners = null;
            primaryColor = Color.SILVER;
            secondaryColor = Color.GRAY;
            primaryChatColor = ChatColor.GRAY;
            secondaryChatColor = ChatColor.DARK_GRAY;
            return true;
        }
        // Invalid team change
        return false;
    }

    public String getSpawnMessage()
    {
        return secondaryChatColor + "Spawning at:" + primaryChatColor + " " + name;
    }
}