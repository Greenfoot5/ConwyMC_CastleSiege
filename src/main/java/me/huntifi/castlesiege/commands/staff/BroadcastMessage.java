package me.huntifi.castlesiege.commands.staff;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;


public class BroadcastMessage implements CommandExecutor {

    /**
     * Sends a message to all players on the server, this can for example be used to let players know someone donated to the server.
     * I also allowed this to recognise colour codes.
     *
     * @param sender Source of the command
     * @param cmd Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0)
            return false;

        String broadcastPrefix = "§2[§4ConwyMC§2] - §a";

        Bukkit.getServer().sendMessage(Component.text(broadcastPrefix).append(MiniMessage.miniMessage().deserialize(String.join(" ", args))));
        return true;
    }
}