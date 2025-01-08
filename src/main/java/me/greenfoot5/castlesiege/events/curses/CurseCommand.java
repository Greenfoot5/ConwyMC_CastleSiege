package me.greenfoot5.castlesiege.events.curses;

import me.greenfoot5.conwymc.util.Messenger;
import me.greenfoot5.conwymc.util.PunishmentTime;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Casts a new curse
 */
public class CurseCommand implements TabExecutor {

//    /** The curses GUI */
//    private final Gui gui;

    /**
     * Register the curses GUI.
     */
    // TODO: Setup GUI with all curses
    public CurseCommand() {
//        gui = new Gui(ChatColor.DARK_RED + "Curses", 1);
//        gui.addItem(ChatColor.DARK_RED + BindingCurse.name, Material.BARRIER, Collections.singletonList(
//                ChatColor.DARK_PURPLE + "Prevent players from changing kits"
//        ), 2, "curse binding", true);
//        gui.addItem(ChatColor.DARK_RED + CurseEnum.POSSESSION.getName(), Material.LEAD, Collections.singletonList(
//                ChatColor.DARK_PURPLE + "Force a random kit onto all players"
//        ), 3, "curse possession", true);
//        gui.addItem(ChatColor.DARK_RED + CurseEnum.TEAMWORK.getName(), Material.CAMPFIRE, Collections.singletonList(
//                ChatColor.DARK_PURPLE + "Prevent players from switching teams"
//        ), 5, "curse teamwork", true);

//        Main.plugin.getServer().getPluginManager().registerEvents(gui, Main.plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length == 0) {
            Messenger.sendError("Select a Curse!", sender);
            // Attempt to open the curses GUI
//            if (!(sender instanceof Player)) {
//                Messenger.sendError("Only players can open the curses GUI!", sender);
//            } else {
//                Bukkit.getScheduler().runTask(Main.plugin, () -> gui.open((Player) sender));
//            }
            return true;
        }

        // Attempt to activate the curse
        try {
            CurseCast.CurseBuilder curse = createCurse(args);
            curse.cast();
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
            values.add("possession");
            values.add("teleportation");
            values.add("blindness");
            values.add("blindness_greater");
            values.add("blindness_true");
            values.add("healing");
            values.add("vulnerability");
            StringUtil.copyPartialMatches(args[0], values, options);
            return options;
        }

        // Creates the curse excluding the most recent argument
        CurseCast.CurseBuilder curse = createCurse(Arrays.copyOfRange(args, 0, args.length - 1));
        if (args.length <= curse.getCommandOptionsLength()) {
            List<String> values = curse.getCommandOptions(args.length - 2);
            if (Objects.equals(values.getFirst(), "[player]") || Objects.equals(values.getFirst(), "<player>"))
                return null;
            StringUtil.copyPartialMatches(args[args.length - 1], values, options);
        }

        return options;
    }

    /**
     * Attempts to create a curse.
     * @param args The curseEnum to activate
     */
    private CurseCast.CurseBuilder createCurse(@NotNull String[] args) {
        switch (args[0].toLowerCase()) {
            case "binding":
                if (args.length == 1)
                    return new BindingCurse.CurseBuilder(300);
                if (args.length == 2)
                    // Can throw NumberFormatException
                    return new BindingCurse.CurseBuilder((int) PunishmentTime.getDuration(args[1]));
                if (args.length == 3)
                    return new BindingCurse.CurseBuilder((int) PunishmentTime.getDuration(args[1])).setPlayer(UUID.fromString(args[2]));
            case "dice":
                return new DiceCurse.CurseBuilder();
            case "possession":
                if (args.length == 1)
                    return new PossessionCurse.CurseBuilder();
                if (args.length == 2)
                    return new PossessionCurse.CurseBuilder().setKit(args[1]);
                if (args.length == 3)
                    return new PossessionCurse.CurseBuilder().setKit(args[1]).setPlayer(Bukkit.getPlayer(args[2]).getUniqueId());
            case "teleport":
            case "teleportation":
                return new TeleportationCurse.CurseBuilder();
            case "blindness":
                if (args.length == 1)
                    return new BlindnessCurse.CurseBuilder(300);
                return new BlindnessCurse.CurseBuilder((int) PunishmentTime.getDuration(args[1]));
            case "blindness_greater":
                if (args.length == 1)
                    return new GreaterBlindnessCurse.CurseBuilder(300);
                return new GreaterBlindnessCurse.CurseBuilder((int) PunishmentTime.getDuration(args[1]));
            case "blindness_true":
                if (args.length == 1)
                    return new TrueBlindnessCurse.CurseBuilder(300);
                return new TrueBlindnessCurse.CurseBuilder((int) PunishmentTime.getDuration(args[1]));
            case "healing":
                if (args.length == 1)
                    return new HealingCurse.CurseBuilder(300);
                if (args.length == 2)
                    return new HealingCurse.CurseBuilder((int) PunishmentTime.getDuration(args[1]));
                if (args.length == 3)
                    return new HealingCurse.CurseBuilder((int) PunishmentTime.getDuration(args[1])).setMultiplier(Float.parseFloat(args[2]));
            case "vulnerable":
            case "vulnerability":
                if (args.length == 1)
                    return new VulnerabilityCurse.CurseBuilder(300);
                return new VulnerabilityCurse.CurseBuilder((int) PunishmentTime.getDuration(args[1]));
            default:
                throw new IllegalArgumentException();
        }
    }
}
