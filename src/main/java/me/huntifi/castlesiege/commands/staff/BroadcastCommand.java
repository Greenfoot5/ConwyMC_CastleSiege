package me.huntifi.castlesiege.commands.staff;

import me.huntifi.conwymc.util.Messenger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;


public class BroadcastCommand implements CommandExecutor {

    public static Component broadcastPrefix = Messenger.mm.deserialize("<dark_green>[<dark_red>ConwyMC</dark_red>] - </dark_green>");

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

        Messenger.broadcast(broadcastPrefix.append(Messenger.mm.deserialize(
                "<gradient:#EC9F05:#FF4E00>" + String.join(" ", args))));
        return true;
    }
}