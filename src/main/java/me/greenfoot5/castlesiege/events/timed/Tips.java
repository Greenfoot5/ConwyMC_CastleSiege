package me.greenfoot5.castlesiege.events.timed;

import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

/**
 * Sends out helpful tips to all players in chat
 */
public class Tips extends BukkitRunnable implements CommandExecutor {
    // time in seconds between sending out tips
    public static final int TIME_BETWEEN_TIPS = 300; //broadcast tip every 6 mins
    private final Random random = new Random();

    //used to prevent same tip 2x in a row
    private int oldIndex = -1; // -1 to allow for any initial tip to be sent

    final MiniMessage mm = MiniMessage.builder()
            .editTags(t -> t.resolver(TagResolver.resolver("cmd", this::cmd)))
            .editTags(t -> t.resolver(TagResolver.resolver("a", this::a)))
            .build();


    // Quick and dirty, needs to be expanded to allow for conditions and be read from a yml
    private static final String[] tips = new String[] {
            // Boosters
            "Wild Kit Boosters allow you to pick which kit to boost!",
            "Kit boosters temporary unlock the boosted kit for everyone!",
            "Coin boosters are a great way to increase the amount of coins everyone earns!",

            // Kits
            "Alchemists can give you many varieties of boosts and also heal you!",
            "Crossbowmen can toggle between Mobility & Sniper modes",
            "Engineers can repair broken stone and wood structures!",
            "Engineers are a great utility class, speeding up catapult reloads, repairing gates, placing traps and using ballista",
            "Rogue's can mark their enemies by hitting them with track arrows",
            "Rangers can backstab enemies, don't let them sneak up on you!",
            "Low on health? Find a medic!",
            "Low on health? Eat some cake!",
            "Low on health? Take a nap!",
            "You have a chance to dodge maceman's stuns by shifting",
            "Hit by an arrow out of nowhere? Could be a crossbowman...",
            "Being shot at by archers? A shieldman can block them!",

            // Commands
            "Stuck somewhere, or have no way out? Use <cmd:/sui>/suicide</cmd>",
            "Use <cmd:/settings>/settings</cmd> to have options to change certain mechanics",
            "<cmd:/settings>/settings</cmd> opens the settings menu",
            "Use <cmd:/maps>/maps</cmd> to see the current rotation",
            "Need a refresher on the rules? You can view them with <cmd:/rules>/rules</cmd>!",
            "Have you found all the <cmd:/secrets>/secrets</cmd> yet?",
            "Looking for a change in pace? Try <cmd:/random>/random</cmd>!",
            "Use <cmd:/cosmetics>/cosmetics</cmd> to equip and view your cosmetics",
            "Don't like the current map? You can use <cmd:/voteskip>/voteskip</cmd> can end the map early!",
            "Make sure to vote for the map with <cmd:/mapvote >/mapvote <yes/no></cmd> or in the GUI post-map!",
            "You can preview a kit's items with <cmd:/preview >/preview <kit></cmd>, even if you haven't bought it!",
            "Want to chat with just your team? Use <cmd:/team >/teamchat</cmd>",
            "Curious who's got the largest bounty? Check the leaderboard with <cmd:/bounties>/bounties</cmd>",

            // Support
            "Don't forget to vote to gain access to special boosts and classes!",
            "Want to help the server? Record or stream the play sessions and upload them!",
            "Having fun? Why not invite your friends?",
            "Does a kit look too strong? Request & discuss a nerf on our <a:https://conwymc.alchemix.dev/discord>Discord</a> server!",
            "Never have enough ladders? You can get two more by voting!",
            "Want a shiny sword? Vote for +2 damage!",
            "Donators can launch some neat fireworks using <cmd:/fw>/firework</cmd>",
            "Donators can set unique leave and join message with <cmd:/joinmsg >/joinmsg</cmd> and <cmd:/leavemsg >/leavemsg</cmd>!",
            "Donators can switch teams whenever they want, no matter the amount of players!",
            "<green>Noble</green>s and higher can challenge players to a duel with <cmd:/duel >/duel <player></cmd>",
            "Have you seen the <gradient:#FFED00:#FF0000>👑High King👑</gradient>? They're ConwyMC's biggest donator!",
            "<gradient:#F07654:#F5DF2E:#F07654>⚜King⚜</gradient> players are in the top 3 all-time donators! A massive thanks!",
            "<gradient:#be1fcc:#d94cd9>Viceroy</gradient>s are in the top 10 players to donate to the server. ConwyMC simply wouldn't exist without them!",
            "You can check your booster inventory with <cmd:/booster>/boosters</cmd>",

            // Other
            "Is the server empty or are you all by yourself? Mention the <color:#E67E23>@Fighter</color> role on our <a:https://conwymc.alchemix.dev/discord>Discord</a> server!",
            "Found a suspicious player? Report them through <cmd:/support >/support <msg></cmd>",
            "Found a bug or exploit? Report it through <cmd:/support >/support <msg></cmd>",
            "Did you know you can click the <cmd:/help>yellow</cmd> text to auto-fill commands?",
            "Want to try a kit before buying? Play on Fridays for free or use a kit booster!",
            "Don't like using Discord? You can always email us at <a:https://conwymc.alchemix.dev/contact>conwymc@alchemix.dev!</a>",
            "Get the latest ConwyMC news at <a:https://conwymc.alchemix.dev/news>https://conwymc.alchemix.dev/news</a>",

            // How to play
            "Low on ammo or ladders? Resupply with an enderchest!",
            "Ender Chests clear all temporary effects, both good and bad!",
            "Landing on a hay bale stops you taking falling damage",
            "If you're having a hard time getting to a flag, there's often more than one way to get there...",
            "Rams only need one person to work, but more people means a bigger impact!",

            //"Want to help the server? Help us advertise!",

            //"Want something more competitive? Join a Player Pool Match (PPM) hosted on the Discord!",
            "Check out the latest patch notes: <a:https://conwymc.alchemix.dev/news/latest>https://conwymc.alchemix.dev/news/latest</a>"
    };

    // Sends a random tip to all players
    public void run() {
        int newIndex;
        do {
            newIndex = random.nextInt(tips.length);
        } while (newIndex == oldIndex);
        oldIndex = newIndex; //set oldIndex to newIndex to prepare for next tip

        Component msg = mm.deserialize(tips[newIndex]).color(NamedTextColor.AQUA);
        ForwardingAudience audience = Bukkit.getServer();
        audience.sendMessage(mm.deserialize("<gold>[i]</gold> ").append(msg));
        //Messenger.broadcastTip(mm.deserialize(tips[newIndex]));
    }

    private Tag cmd(final ArgumentQueue args, final Context ctx) {
        final String command = args.popOr("The <cmd> tag requires exactly one argument, the command to suggest").value();

        return Tag.styling(
                NamedTextColor.YELLOW,
                ClickEvent.suggestCommand(command),
                HoverEvent.showText(Component.text("Click to type command"))
        );
    }

    private Tag a(final ArgumentQueue args, final Context ctx) {
        final String link = args.popOr("The <a> tag requires exactly one argument, the link to open").value();

        return Tag.styling(
                NamedTextColor.YELLOW,
                TextDecoration.UNDERLINED,
                ClickEvent.openUrl(link),
                HoverEvent.showText(Component.text("Open " + link))
        );
    }

    /**
     * Executes the given command, returning its success.
     * <br>
     * If false is returned, then the "usage" plugin.yml entry for this command
     * (if defined) will be sent to the player.
     *
     * @param sender  Source of the command
     * @param command Command which was executed
     * @param label   Alias of the command which was used
     * @param args    Passed command arguments
     * @return true if a valid command, otherwise false
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        run();
        return true;
    }
}
