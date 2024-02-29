package me.huntifi.castlesiege.events.timed;

import me.huntifi.castlesiege.events.chat.Messenger;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

/**
 * Sends out helpful tips to all players in chat
 */
public class Tips extends BukkitRunnable {
    // time in seconds between sending out tips
    public static final int TIME_BETWEEN_TIPS = 300; //broadcast tip every 6 mins
    private final Random random = new Random();

    //used to prevent same tip 2x in a row
    private int oldIndex = -1; // -1 to allow for any initial tip to be sent


    // Quick and dirty, needs to be expanded to allow for conditions and be read from a yml
    private static final String[] tips = new String[] {
            "Wild boosters are pretty cool, you can choose what elite kit to boost!",
            "Did you know kit boosters temporary unlock the boosted kit for everyone?",
            "Boosters are a great way to increase the amount of coins everyone earns!",
            "Alchemists can give you many varieties of boosts and also heal you!",
            "Donators can launch some neat fireworks using <yellow>/firework</yellow> or <yellow>/fw</yellow>",
            "Don't forget to vote to gain access to special boosts and classes!",
            "Stuck somewhere, or have no way out? Use <yellow>/sui</yellow>",
            "Want to help the server? Record or stream the play sessions and upload them!",
            //"Want to help the server? Help us advertise!",
            "Use <yellow>/settings</yellow> to have options to change certain mechanics.",
            "Rogue's can mark their enemies by hitting them with track arrows.",
            "Crossbowmen can toggle between Mobility & Sniper modes.",
            "<yellow>/Settings</yellow> opens a menu that allows you to change multiple settings.",
            "Rangers can backstab enemies, so watch your back!",
            "Engineers can repair broken stone and wood structures!",
            "Want to apply for staff? Go to <yellow>/discord</yellow> ---> #join-us",
            "Does a kit look too strong? Request & discuss a nerf on our <yellow>/discord</yellow> server!",
            //"Is the server empty or are you all by yourself? Mention the <yellow>@fighter</yellow> role on our discord server!",
            "You have a chance to dodge maceman' stuns by shifting.",
            "Use <yellow>/maps</yellow> to see the current rotation",
            "Hit by something that seems invisible? Check your surroundings for Crossbowmen!",
            "Engineers are a useful class that can speed up catapult reloads, repair gates, place traps and use ballista, a great addition to your team!",
            "Low on health? Find a medic or some cake!",
            "Found a suspicious player? Make a report on the <yellow>/discord</yellow> server or <yellow>/msg</yellow> a staff member",
            "Low on ammo or ladders? Resupply with an enderchest!",
            "Ender Chests clear all temporary effects, both good and bad!",
            "Landing on a hay bale stops you taking falling damage",
            "Found a bug or exploit? Report it on the <yellow>/discord</yellow> server.",
            "Never have enough ladders? You can get two more by voting!",
            "Cheating or the use of hacks is strictly forbidden on this server, when seen using these you will be banned instantly.",
            "Need a refresher on the rules? You can view them with <yellow>/rules</yellow>!",
            "If you're having a hard time getting to a flag, there's often more than one way to get there...",
            "Have you found all the secrets yet? Use <yellow>/secrets</yellow> to check your progress! Don't forget to right-click to collect them!",
            "Being shot at by archers? Try to find some cover!",
            "Rams only need one person to work, but more people means a bigger impact!",
            "Got lag? Get Optifine!",
            "Donators can set an unique leave and join message with <yellow>/joinmsg</yellow> and <yellow>/leavemsg</yellow>!",
            "Donators can switch teams whenever they want, no matter the amount of players!",
            "Looking for a change in pace? Try <yellow>/random</yellow>!",
            "Want to try a kit before buying? Play on Fridays for free or buy a kit booster!",
            "Did you know you can use <yellow><b>/voteskip</b></yellow> to vote to skip the current map?",
            "Don't like using Discord? You can always access support at conwymc@alchemix.dev!",
            //"Want something more competitive? Join a Player Pool Match (PPM) hosted on the Discord!",
            //"Check out the latest patch notes: <yellow><u>https://conwymc.alchemix.dev/updates</u></yellow>"
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
