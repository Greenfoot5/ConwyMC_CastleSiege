package me.huntifi.castlesiege.flags;

import me.huntifi.castlesiege.flags.captureareas.CaptureArea;
import org.bukkit.Color;
import org.bukkit.Location;

// you need to decide what every flag has
// (capture area,
// spawn point,
// starting team,
// primary/secondary colour for each team)
public class Flag {
    public String name;
    public Location spawnPoint;
    public int startingTeam;
    public CaptureArea captureArea;
    public Color primaryColor;
    public Color secondaryColor;


}