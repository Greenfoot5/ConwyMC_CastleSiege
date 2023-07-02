package me.huntifi.castlesiege.commands.info;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class WebshopCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        sender.sendMessage("--------------------------------");
        sender.sendMessage(ChatColor.GREEN + "Link to the web shop:");
        sender.sendMessage(ChatColor.BLUE + "https://conwymc.tebex.io/");
        sender.sendMessage("--------------------------------");

        return true;
    }
}
