package me.huntifi.castlesiege.kits.kits;

import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.castlesiege.maps.MapController;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

public abstract class VoterKit extends Kit {

    // Kit Tracking
    private static final Collection<String> kits = new ArrayList<>();

    /**
     * Create a kit with basic settings
     *
     * @param name       This kit's name
     * @param baseHealth This kit's base health
     */
    public VoterKit(String name, int baseHealth, double regenAmount) {
        super(name, baseHealth, regenAmount);
        kits.add(getSpacelessName());
    }

    /**
     * Register the player as using this kit and set their items
     * @param commandSender Source of the command
     * @param command Command which was executed
     * @param s Alias of the command which was used
     * @param strings Passed command arguments
     * @return true
     */
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof ConsoleCommandSender) {
            commandSender.sendMessage("Console cannot select kits!");
            return true;

        } else if (commandSender instanceof Player) {
            if (MapController.isSpectator(((Player) commandSender).getUniqueId())) {
                commandSender.sendMessage("Spectators cannot select kits!");
                return true;
            }

            Player player = (Player) commandSender;
            // If they already have the kit equipped, but haven't voted
            if (!ActiveData.getData(player.getUniqueId()).hasVote("kits")) {
                if (Kit.equippedKits.get(player.getUniqueId()) == null) {
                    player.performCommand("swordsman");
                    player.sendMessage(ChatColor.DARK_RED + "You need to vote to use " + ChatColor.RED + name
                            + ChatColor.DARK_RED + " again!");

                // If they are trying to equip the kit, but haven't voted
                } else {
                    player.sendMessage(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "You need to vote to use this kit!");
                }
                return true;
            }


            super.addPlayer(player.getUniqueId());
            return true;
        }
        return false;
    }

    /**
     * Get all free kit names
     * @return All free kit names without spaces
     */
    public static Collection<String> getKits() {
        return kits;
    }
}
