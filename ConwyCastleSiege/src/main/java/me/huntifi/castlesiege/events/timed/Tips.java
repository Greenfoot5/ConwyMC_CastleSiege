package me.huntifi.castlesiege.events.timed;

import me.huntifi.castlesiege.events.chat.Messenger;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

/**
 * Sends out helpful tips to all players in chat
 */
public class Tips extends BukkitRunnable {
    // time in seconds between sending out tips
    public static int TIME_BETWEEN_TIPS = 180; //broadcast tip every 3 mins
    private final Random random = new Random();
    private int oldIndex = -1; //used to prevent same tip 2x in a row
    // -1 to allow for any initial tip to be sent

    // Quick and dirty, needs to be expanded to allow for conditions and be read from a yml
    private static final String[] tips = new String[] {
            "Don't forget to vote to gain access to special boosts and classes!",
            "Stuck somewhere, or have no way out? Use /sui",
            "Want to help the server? Record or stream the play sessions and upload them!",
            "Want to help the server? Help us advertise!",
            "Use /settings to have options to change certain mechanics.",
            "Premium kits are now called Elite kits, and are much stronger than before!",
            "Elite kits need to be unlocked with coins first before you can spend your battlepoints on them. ",
            "Rogue's can mark their enemies by hitting them with track arrows. ",
            "Crossbowmen can now toggle between Mobility & Sniper modes. ",
            "/Settings opens a menu that allows you to change multiple settings ",
            "Rangers can backstab enemies, so watch your back! ",
            "Battlepoints can be earned from kills, assists, killstreaks and deaths! ",
            "Fully capturing a flag will award you 1 battlepoint! ",
            "Engineers can repair broken stone and wood structures! ",
            "Want to apply for staff? Go to discord ---> #join-us ",
            "Does a kit look too strong? Request & discuss a nerf on our discord server! ",
            "Is the server empty or are you al by yourself? Tag the @fighter tag on our discord server! ",
            "You have a chance to dodge maceman' stuns by shifting. ",
            "Use /maps to see the current rotation",
            "Hit by something that seems invisible? Check your surroundings for Crossbowmen!",
            "Engineers are a useful class that can speed up catapult reloads, repair gates, place traps and use ballista, make sure you've got one on your team!",
            "Low on health? Find a medic or some cake!",
            "Found a suspicious player? Make a report on the discord server or tell a staff member",
            "Low on arrows or ladders? Resupply with an enderchest!",
            "Enderchests clear all temporary effects, both good and bad!",
            "Landing on a hay bale stops you taking falling damage",
            "Found a bug or exploit? Report it on the discord server.",
            "Low on ladders? You can get two more by voting!",
            "Cheating or the use of hacks is strictly forbidden on this server, when seen using these you will be banned instantly.",
            "Need a refresher on the rules? Check out /rules!",
            "If you're having a hard time getting to a flag, there's often more than one way to get there...",
            "Have you found all the secrets yet? Use /secrets to check your progress! Don't forget to right-click to collect them!",
            "Being shot at by archers? Try to find some cover!",
            "Rams no longer require 4 people, instead the damage inflicted to the gate is increased with each player on the ram.",
            "Got lag? Get optifine!",
            "Donators can set an unique leave and join message with /joinmsg and /leavemsg!",
            "Donators can switch whenever they want, no matter the amount of players!",
            "Looking for a change in pace? Try /random!",
            "Want to try a kit before buying? Play on Fridays for free!"
    };

    // Sends a random tip to all players
    public void run() {
        int newIndex;
        do {
            newIndex = random.nextInt(tips.length);
        } while (newIndex == oldIndex);
        oldIndex = newIndex; //set oldIndex to newIndex to prepare for next tip
        Messenger.broadcastTip(tips[newIndex]);
    }
}
