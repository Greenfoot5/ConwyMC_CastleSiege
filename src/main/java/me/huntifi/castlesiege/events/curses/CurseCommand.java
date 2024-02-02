package me.huntifi.castlesiege.events.curses;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.gui.Gui;
import me.huntifi.castlesiege.kits.kits.Kit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class CurseCommand implements TabExecutor {

    /** The curses GUI */
    private final Gui gui;

    /**
     * Register the curses GUI.
     */
    public CurseCommand() {
        gui = new Gui(ChatColor.DARK_RED + "Curses", 1);
        // TODO: Add curses
        gui.addItem(ChatColor.DARK_RED + BindingCurse.name, Material.BARRIER, Collections.singletonList(
                ChatColor.DARK_PURPLE + "Prevent players from changing kits"
        ), 2, "curse binding", true);
//        gui.addItem(ChatColor.DARK_RED + CurseEnum.POSSESSION.getName(), Material.LEAD, Collections.singletonList(
//                ChatColor.DARK_PURPLE + "Force a random kit onto all players"
//        ), 3, "curse possession", true);
//        gui.addItem(ChatColor.DARK_RED + CurseEnum.TEAMWORK.getName(), Material.CAMPFIRE, Collections.singletonList(
//                ChatColor.DARK_PURPLE + "Prevent players from switching teams"
//        ), 5, "curse teamwork", true);

        Main.plugin.getServer().getPluginManager().registerEvents(gui, Main.plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length == 0) {
            // Attempt to open the curses GUI
            if (!(sender instanceof Player)) {
                Messenger.sendError("Only players can open the curses GUI!", sender);
            } else {
                Bukkit.getScheduler().runTask(Main.plugin, () -> gui.open((Player) sender));
            }
            return true;
        }

        // Attempt to activate the curse
        try {
            Curse curse = createCurse(args);
            activateCurse(curse);
        } catch (NumberFormatException exception) {
            Messenger.sendError("One of your arguments should be a number and isn't!", sender);
        } catch (IllegalArgumentException exception) {
            Messenger.sendError("The curse \"" + args[0] + "\" does not exist!", sender);
        }

        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        List<String> options = new ArrayList<>();
        if (args.length < 2) {
            List<String> values = new ArrayList<>();
            values.add("binding");
            values.add("dice");
            StringUtil.copyPartialMatches(args[0], values, options);
            return options;
        }

        // Creates the curse excluding the most recent argument
        Curse curse = createCurse(Arrays.copyOfRange(args, 0, args.length - 1));
        if (args.length <= curse.options.length) {
            List<String> values = new ArrayList<>(List.of(curse.options[args.length - 2]));
            if (Objects.equals(values.get(0), "[player]") || Objects.equals(values.get(0), "<player>"))
                return null;
            StringUtil.copyPartialMatches(args[args.length - 1], values, options);
        }

        return options;
    }

    /**
     * Attempts to create a curse.
     * @param args The curseEnum to activate
     */
    private Curse createCurse(@NotNull String[] args) {
        switch (args[0].toLowerCase()) {
            case "binding":
                if (args.length == 1)
                    return new BindingCurse(300);
                // Can throw NumberFormatException
                return new BindingCurse(Integer.parseInt(args[1]));
            case "dice":
                return new DiceCurse();
            default:
                throw new IllegalArgumentException();
        }
    }

    private void activateCurse(Curse curse) {
        curse.activateCurse();
    }

    /**
     * Get a random kit.
     * @return A random kit
     */
    private Kit randomKit() {
        Collection<String> kits = Kit.getKits();
        int num = ThreadLocalRandom.current().nextInt(0, kits.size());
        for (String kit: kits) if (--num < 0) return Kit.getKit(kit);
        throw new AssertionError();
    }
}
