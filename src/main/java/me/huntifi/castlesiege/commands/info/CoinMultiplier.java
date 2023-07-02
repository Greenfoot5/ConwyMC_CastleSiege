package me.huntifi.castlesiege.commands.info;

import me.huntifi.castlesiege.data_types.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;

/**
 * Shows the player the active coin multiplier
 */
public class CoinMultiplier implements CommandExecutor {

    /**
     * Prints the active coin multiplier to the player
     * @param sender Source of the command
     * @param cmd Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return true
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        sender.sendMessage(ChatColor.GOLD + "Coin Multiplier: " + ChatColor.YELLOW
                + new DecimalFormat("0.0").format(PlayerData.getCoinMultiplier()));
        return true;
    }
}
