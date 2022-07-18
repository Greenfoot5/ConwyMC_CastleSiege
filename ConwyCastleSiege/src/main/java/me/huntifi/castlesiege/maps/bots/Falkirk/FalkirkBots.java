package me.huntifi.castlesiege.maps.bots.Falkirk;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.kits.items.WoolHat;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.NameTag;
import me.huntifi.castlesiege.maps.Team;
import me.huntifi.castlesiege.maps.bots.BotKits.SwordsmanBot;
import me.huntifi.castlesiege.maps.bots.Combat;
import me.huntifi.castlesiege.maps.bots.CreateBots;
import me.huntifi.castlesiege.maps.bots.NamePool;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.NPCDeathEvent;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.trait.Equipment;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.mcmonkey.sentinel.SentinelIntegration;
import org.mcmonkey.sentinel.SentinelTrait;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class FalkirkBots extends SentinelIntegration implements Runnable, Listener {

    public static boolean canSpawn = false;

    //This determines the map
    private String mapName = "Falkirk";

    //The amount of bots that should be registered on this team
    public static int amount = 20;

    public static Location spawn = new Location(Bukkit.getWorld("Falkirk"), 45.50, 6, 686);

    //The list with the bots from this team
    public static ArrayList<NPC> FalkirkNPCs = new ArrayList<>();

    //Entity IDs so we can track the NPCs just like Players.
    public static ArrayList<UUID> BotIDs = new ArrayList<>();

    //This creates the bots and assigns them to a team and spawn them in their spawnroom.
    public static void createFalkirkBots() {

        if (MapController.currentMapIs("Falkirk")) {

            for (int i = 0; i < amount; i++) {

                NPC newNPC = CreateBots.createSwordsmanBot();

                //Register the NPC as on this team.
                FalkirkNPCs.add(newNPC);
                //Register this newly created bot's ID
                BotIDs.add(newNPC.getUniqueId());
                //Physically Spawn the npc at their unique spawnpoint
                newNPC.spawn(spawn);
                //Registers the bot as in their lobby
                Combat.botInLobby.add(newNPC);
            }
        }
    }

    /**
     * Create a new npc
     */
    public static void createNewFalkirkBot() {

        if (MapController.currentMapIs("Falkirk")) {

            NPC newNPC = CreateBots.createSwordsmanBot();
            //Register the NPC as on this team.
            FalkirkNPCs.add(newNPC);
            //Register this newly created bot's ID
            BotIDs.add(newNPC.getUniqueId());
            //Physically Spawn the npc at their unique spawnpoint
            newNPC.spawn(spawn);
            //Registers the bot as in their lobby
            Combat.botInLobby.add(newNPC);

        }
    }

    /**
     * Removes every bot related to this map, this should be run on a map change or when a map ends.
     */
    public static void removeFalkirkBots() {

        if (FalkirkNPCs.size() == 0) {return;}

        for (NPC bot: FalkirkNPCs) {
            bot.destroy();
        }

        FalkirkNPCs.clear();
        BotIDs.clear();
    }

    /**
     * Make bots able to spawn and create them when the map starts.
     */
    public static void makeBotsSpawnable(){

        createFalkirkBots();
        new BukkitRunnable() {
            @Override
            public void run() {
                canSpawn = true;
            }
        }.runTaskLater(Main.plugin, 100);
    }


    /**
     * The action to do when they die or when they should spawn.
     * @param npc the bot to respawn
     */
    public static void respawnBot(NPC npc) {

        if (Combat.botInLobby.contains(npc)) {

            if (MapController.getCurrentMap().getTeam(npc).name.equalsIgnoreCase("The Scottish")) {
                Combat.botInLobby.remove(npc);
                npc.spawn(new Location(Bukkit.getWorld("Falkirk"), 47, 75, 121, -180, 0));
            }

            if (MapController.getCurrentMap().getTeam(npc).name.equalsIgnoreCase("The English")) {
                Combat.botInLobby.remove(npc);
                npc.spawn(new Location(Bukkit.getWorld("Falkirk"), 46, 75, -152, 0, 0));
            }

        }
    }


    /**
     * Spawns the Bots
     */
    @Override
    public void run() {

            if (MapController.currentMapIs("Falkirk")) {

                Bukkit.getConsoleSender().sendMessage("Amount of Bot IDs: " + BotIDs.size());
                Bukkit.getConsoleSender().sendMessage("Amount of Bots: " + FalkirkNPCs.size());

                if (canSpawn = true) {

                for (NPC bots : FalkirkNPCs) {

                    respawnBot(bots);

                }
            }
        }

    }

}
