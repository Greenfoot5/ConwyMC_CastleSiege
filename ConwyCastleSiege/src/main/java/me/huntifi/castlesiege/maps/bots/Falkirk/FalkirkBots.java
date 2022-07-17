package me.huntifi.castlesiege.maps.bots.Falkirk;

import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.kits.items.WoolHat;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.Team;
import me.huntifi.castlesiege.maps.bots.BotKits.SwordsmanBot;
import me.huntifi.castlesiege.maps.bots.NamePool;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.trait.Equipment;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class FalkirkBots {

    //This determines the map
    private String mapName = "Falkirk";

    //The amount of bots that should be registered on this team
    public static int amount = 20;

    static Location spawn = new Location(Bukkit.getWorld("Falkirk"), 100, 100, 100);

    //The list with the bots from this team
    public static ArrayList<NPC> FalkirkNPCs = new ArrayList<NPC>();

    //This creates the bots and assigns them to a team and spawn them in their spawnroom.
    public static void createFalkirkBots() {

        if (MapController.currentMapIs("Falkirk")) {

            for (int i = 0; i <= amount; i++) {

                //Creates an npc with a name randomly chosen from the namepool
                NPC newNPC = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, NamePool.getRandomName());
                //Register the NPC as on this team.
                FalkirkNPCs.add(newNPC);
                //Physically Spawn the npc at their unique spawnpoint
                newNPC.spawn(spawn);
                //The bots are assigned to the right team
                MapController.botJoinsATeam(newNPC);
                //Registers the bot as in their lobby
                InCombat.botInLobby.add(newNPC);
                //give them their woolhead
                WoolHat.setHead(newNPC);
                //give the NPC their kit (for now this is swordsman)
                SwordsmanBot.setSwordsman(newNPC);
            }
        }
    }

    /**
     * Removes every bot related to this map, this should be run on a map change or when a map ends.
     */
    public static void removeFalkirkBots() {

        for (NPC bot: FalkirkNPCs) {
            bot.despawn();
        }

        FalkirkNPCs.clear();
    }
}
