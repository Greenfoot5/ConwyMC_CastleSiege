package me.huntifi.castlesiege.commands.gameplay;

import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.gui.GuiController;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CoinShopCommand implements CommandExecutor {

    /**
     * Opens the coin shop GUI
     * @param sender Source of the command
     * @param command Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return true
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player))
            Messenger.sendError("Console cannot buy kits!", sender);
        else
            GuiController.get("coin shop").open((Player) sender);

        return true;
    }
}
