package me.huntifi.castlesiege.events.curses;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.commands.staff.maps.SpectateCommand;
import me.huntifi.castlesiege.data_types.PlayerData;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.gui.Gui;
import me.huntifi.castlesiege.kits.kits.Kit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;
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
        gui.addItem(ChatColor.DARK_RED + CurseEnum.GREED.getName(), Material.SUNFLOWER, Collections.singletonList(
                ChatColor.DARK_PURPLE + "Set the coin multiplier to a random number in the range [0.7-1.5]"
        ), 0, "curse greed", true);
        gui.addItem(ChatColor.DARK_RED + CurseEnum.BINDING.getName(), Material.BARRIER, Collections.singletonList(
                ChatColor.DARK_PURPLE + "Prevent players from changing kits"
        ), 2, "curse binding", true);
        gui.addItem(ChatColor.DARK_RED + CurseEnum.POSSESSION.getName(), Material.LEAD, Collections.singletonList(
                ChatColor.DARK_PURPLE + "Force a random kit onto all players"
        ), 3, "curse possession", true);
        gui.addItem(ChatColor.DARK_RED + CurseEnum.TEAMWORK.getName(), Material.CAMPFIRE, Collections.singletonList(
                ChatColor.DARK_PURPLE + "Prevent players from switching teams"
        ), 5, "curse teamwork", true);

        Main.plugin.getServer().getPluginManager().registerEvents(gui, Main.plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
            if (args.length == 0) {
                // Attempt to open the curses GUI
                if (!(sender instanceof Player)) {
                    Messenger.sendError("Only players can open the curses GUI!", sender);
                } else {
                    Bukkit.getScheduler().runTask(Main.plugin, () -> gui.open((Player) sender));
                }
            } else {
                // Attempt to activate the curse
                try {
                    CurseEnum curseEnum = CurseEnum.valueOf(args[0].toUpperCase());
                    if (curseEnum.isActive())
                        Messenger.sendError(curseEnum.getName() + " is already active!", sender);
                    else
                        activateCurse(curseEnum);
                } catch (IllegalArgumentException exception) {
                    Messenger.sendError("The curse \"" + args[0] + "\" does not exist!", sender);
                }
            }
        });

        return true;
    }

    /**
     * Activate a curseEnum.
     * @param curseEnum The curseEnum to activate
     */
    private void activateCurse(CurseEnum curseEnum) {
        activateCurse(curseEnum, ThreadLocalRandom.current().nextLong(5, 31) * 1200);
    }

    /**
     * Activate a curseEnum.
     * @param curseEnum The curseEnum to activate
     * @param duration The curseEnum's duration in ticks
     */
    private void activateCurse(CurseEnum curseEnum, long duration) {
        switch (curseEnum) {
            case GREED:
                double multiplier = ThreadLocalRandom.current().nextInt(7, 16) / 10.0;
                PlayerData.setCoinMultiplier(multiplier);
                Bukkit.getScheduler().runTaskLaterAsynchronously(Main.plugin, () -> PlayerData.setCoinMultiplier(1), duration);
                curseEnum.activate(duration, new DecimalFormat("0.0").format(multiplier));
                break;
            case DICE:
                // TODO: Swap teams for some players
                break;
            case POSSESSION:
                for (Player player : Bukkit.getOnlinePlayers()) {
                    UUID uuid = player.getUniqueId();
                    if (SpectateCommand.spectators.contains(uuid))
                        continue;

                    randomKit().addPlayer(uuid, InCombat.isPlayerInLobby(uuid));
                }
                curseEnum.activate(duration / 5);
                break;
            case VANISHING:
                // TODO: Prevent players from picking kits they have died with
                break;
            case TELEPORTATION:
                // TODO: Swap some player positions
                break;
            default:
                // The curseEnum's effect is handled by it being active and the broadcast has no arguments
                curseEnum.activate(duration);
        }
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
