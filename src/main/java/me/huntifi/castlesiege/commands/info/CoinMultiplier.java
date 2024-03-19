package me.huntifi.castlesiege.commands.info;

import me.huntifi.castlesiege.data_types.PlayerData;
import me.huntifi.conwymc.util.Messenger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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
        Messenger.send(Component.text("Coin Multiplier: ", NamedTextColor.GOLD)
                .append(Component.text(new DecimalFormat("0.0").format(PlayerData.getCoinMultiplier()), NamedTextColor.YELLOW)),
                sender);
        return true;
    }
}
