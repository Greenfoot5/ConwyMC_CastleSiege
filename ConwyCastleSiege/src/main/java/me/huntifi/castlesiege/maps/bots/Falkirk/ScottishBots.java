package me.huntifi.castlesiege.maps.bots.Falkirk;

import me.huntifi.castlesiege.maps.Team;
import me.huntifi.castlesiege.maps.bots.NamePool;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;

public class ScottishBots {

    //This determines the map
    private String mapName = "Falkirk";

    //The amount of bots that should be registered on this team
    public int amount = 10;

    Location spawn = new Location(Bukkit.getWorld("Falkirk"), 100, 100, 100);

    //The list with the bots from this team
    public ArrayList<NPC> scottishNPCs = new ArrayList<NPC>();

    //This creates the bots and assigns them to a team, they however have not spawned yet.
    public void createBots() {

        for (int i = 0; i <=amount ; i++) {

            NPC newNPC = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, NamePool.getRandomName());
            scottishNPCs.add(newNPC);

        }
    }
}
