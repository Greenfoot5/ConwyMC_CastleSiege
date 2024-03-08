package me.huntifi.castlesiege.commands.info;

import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.castlesiege.events.chat.Messenger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;

/**
 * Shows the player their coins
 */
public class CoinsCommand implements CommandExecutor {

    /**
     * Print the player's coins
     * @param sender Source of the command
     * @param cmd Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return true
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            Messenger.sendError("Console doesn't have any coins!", sender);
        } else {
            Player player = (Player) sender;
            double coins = ActiveData.getData(player.getUniqueId()).getCoins();
            Messenger.sendInfo("<gold>Coins: </gold><yellow>" + new DecimalFormat("0").format(coins), sender);
            Messenger.sendInfo("You can use coins to purchase kits from the <yellow><click:suggest_command:/coinshop>/coinshop</click></yellow>" +
                    " or adding a <yellow><click:suggest_command:/bounty>/bounty</click></yellow> to someone", sender);
        }

        return true;
    }
}
