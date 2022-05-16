package me.huntifi.castlesiege.events.timed;

import me.huntifi.castlesiege.Main;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

/**
 * Sends out helpful tips to all players in chat
 */
public class Tips extends BukkitRunnable {
    // time in seconds between sending out tips
    public static int TIME_BETWEEN_TIPS = 180;
    private final Random random = new Random();

    // Quick and dirty, needs to be expanded to allow for conditions and be read from a yml
    private static final String[] tips = new String[] {
            "Don't forget to vote to gain access to special boosts and classes!",
            "Stuck somewhere, or have no way out? Use /sui",
            "Use /maps to see the current rotation",
            "Hit by something that seems invisible? Check your surroundings for Crossbowmen!",
            "Don't forget, all donator kits are unlocked for testing sessions!",
            "Engineers are a useful class that can speed up catapult reloads, repair gates, place traps and use ballistae, make sure you've got one on your team!",
            "Low on health? Find a medic or some cake!",
            "Found a suspicious player? Make a report on the discord server or tell a staff member",
            "Low on arrows or ladders? Resupply with an enderchest!",
            "Landing on a hay bale stops you taking falling damage",
            "Found a bug or exploit? Report it on the discord server.",
            "Low on ladders? You can get two more by voting!",
            "Cheating or the use of hacks is strictly forbidden on this server, when seen using these you will be banned instantly.",
            "Need a refresher on the rules? Check out /rules!",
            "Cheating or the use of hacks is strictly forbidden on this server, when seen using these you will be banned instantly.",
            "If you're having a hard time getting to a flag, there's often more than one way to get there...",
            "Have you found all the secrets yet?",
            "Being shot at by archers? Try to find some cover!",
            "Rams no longer requier 4 people, instead the damage inflicted to the gate is increased with each player on the ram.",
            "Got lag? Get optifine!"
    };

    // Sends a random tip to all players
    public void run() {
        Main.plugin.getServer().broadcastMessage(ChatColor.GOLD + "[i] " + ChatColor.AQUA + tips[random.nextInt(tips.length)]);
    }
}
