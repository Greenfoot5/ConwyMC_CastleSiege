package me.huntifi.castlesiege.maps.bots;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.NameTag;
import me.huntifi.castlesiege.maps.bots.Falkirk.FalkirkBots;
import net.citizensnpcs.api.event.NPCDeathEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.mcmonkey.sentinel.SentinelIntegration;

import java.util.UUID;

public class Deaths extends SentinelIntegration implements Listener {

    @EventHandler
    public void whenBotDies(NPCDeathEvent e) {

        if (MapController.currentMapIs("Falkirk")) {

            if (BotTools.tryGetSentinel(e.getNPC().getEntity()) != null) {
                NPC npc = e.getNPC();
                Combat.botDied(npc);
                Bukkit.getConsoleSender().sendMessage("Bot died");

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        FalkirkBots.respawnBot(npc);
                    }
                }.runTaskLater(Main.plugin, 140);
            }
        }
    }


    /**
     * Send kill and death messages to the players
     * @param killer The player who killed
     * @param npc The bot who died
     * @param messages The messages sent to the killer and target
     */
    public static void killBotMessage(Player killer, NPC npc, Tuple<String[], String[]> messages) {
        killer.sendMessage("You" + messages.getFirst()[0] + NameTag.botColor(npc) + npc.getName()
                + ChatColor.RESET + messages.getFirst()[1]);

    }
}
