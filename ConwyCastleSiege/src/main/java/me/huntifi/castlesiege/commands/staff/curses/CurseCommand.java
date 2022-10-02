package me.huntifi.castlesiege.commands.staff.curses;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.PlayerData;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.gui.Gui;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.concurrent.ThreadLocalRandom;

public class CurseCommand implements CommandExecutor {

    /** The curses GUI */
    private final Gui gui;

    /**
     * Register the curses GUI.
     */
    public CurseCommand() {
        gui = new Gui(ChatColor.DARK_RED + "Curses", 1);
        // TODO: Add curses

        Main.plugin.getServer().getPluginManager().registerEvents(gui, Main.plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length == 0) {
            // Attempt to open the curses GUI
            if (!(sender instanceof Player)) {
                Messenger.sendError("Only players can open the curses GUI!", sender);
            } else {
                gui.open((Player) sender);
            }
        } else {
            String name = String.join(" ", args);
            Curse curse = Curse.get(name);
            if (curse == null)
                Messenger.sendError("The curse \"" + name + "\" does not exist!", sender);
            else if (curse.isActive())
                Messenger.sendError(curse.getName() + " is already active!", sender);
            else
                activateCurse(curse);
        }

        return true;
    }

    /**
     * Activate a curse.
     * @param curse The curse to activate
     */
    private void activateCurse(Curse curse) {
        activateCurse(curse, ThreadLocalRandom.current().nextLong(5, 31) * 1200);
    }

    /**
     * Activate a curse.
     * @param curse The curse to activate
     * @param duration The curse's duration in ticks
     */
    private void activateCurse(Curse curse, long duration) {
        switch (curse) {
            case GREED:
                double multiplier = ThreadLocalRandom.current().nextInt(7, 16) / 10.0;
                PlayerData.setCoinMultiplier(multiplier);
                Bukkit.getScheduler().runTaskLaterAsynchronously(Main.plugin, () -> PlayerData.setCoinMultiplier(1), duration);
                curse.activate(duration, new DecimalFormat("0.0").format(multiplier));
                break;
            case DICE:
                // TODO: Swap teams for some players
                break;
            case POSSESSION:
                // TODO: Assign a random kit to players
                break;
            case VANISHING:
                // TODO: Prevent players from picking kits they have died with
                break;
            case TEAMWORK:
                // TODO: Prevent players from switching teams
                break;
            case TELEPORTATION:
                // TODO: Swap some player positions
                break;
            default:
                // The curse's effect is handled by it being active and the broadcast has no arguments
                curse.activate(duration);
        }
    }
}
