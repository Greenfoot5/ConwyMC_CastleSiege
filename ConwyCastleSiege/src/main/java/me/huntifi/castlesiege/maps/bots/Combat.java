package me.huntifi.castlesiege.maps.bots;

import me.huntifi.castlesiege.Main;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class Combat {

    //Set the bot as in the lobby
    public static Collection<NPC> botInLobby = new ArrayList<>();

    //Set the bot as in combat
    public static HashMap<NPC, Integer> botInCombat = new HashMap<>();


    /**
     * Adds a bot to the inCombat
     * @param npc the bot to add
     */
    public static void addBotToCombat(NPC npc) {
        botInCombat.merge(npc, 1, Integer::sum);

        // Players are in combat for 10 seconds only
        new BukkitRunnable() {
            @Override
            public void run() {
                botInCombat.merge(npc, -1, Integer::sum);
            }
        }.runTaskLater(Main.plugin, 200);
    }

    /**
     * When a bot takes any damage, they are placed in combat
     */
    @EventHandler
    public void botTakesDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof NPC) || event.isCancelled()) { return; }
        NPC npc = (NPC) event.getEntity();
        addBotToCombat(npc);
    }

    /**
     * Returns true if the bot is still in the lobby
     */
    public static boolean isBotInLobby(NPC npc) {
        return botInLobby.contains(npc);
    }

    /**
     * Returns true if the bot has taken damage in the last 8s
     */
    public static boolean isBotInCombat(UUID uuid) {
        return botInCombat.get(uuid) != null && botInCombat.get(uuid) > 0;
    }

    /**
     * Removes the specified bot from all combat lists
     */
    public static void botDied(NPC npc) {
        botInCombat.put(npc, 0);
        if (!isBotInLobby(npc)) {
            botInLobby.add(npc);
        }
    }


    /**
     * Adds a bot to the list of those that interacted when alive.
     * Means they die when changing kit or team
     */
    public static void botSpawned(NPC npc) {
        botInLobby.remove(npc);
    }

}
