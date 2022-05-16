package me.huntifi.castlesiege.commands.staff;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


public class BroadcastMessage implements CommandExecutor {

    /**
     * Sends a message to all players on the server, this can for example be used to let players know someone donated to the server.
     * I also allowed this to recognise colour codes.
     *
     * @param s Source of the command
     * @param cmd Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return
     */

    @Override
    public boolean onCommand(@NotNull CommandSender s, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        String broadcastPrefix = "§2[§4ConwyMC§2] - §a";

        String message = "";

                // Command needs a message
                if (args.length >= 0) {

                    for (int i = 0; i < args.length; i++) {

                        message += args[i] + " ";

                    }

                    if (s instanceof Player) {

                        Player p = (Player) s;

                        if (p.hasPermission("castlesiege.moderator")) {

                            Bukkit.broadcastMessage(broadcastPrefix + message);

                        }

                    } else {

                        Bukkit.broadcastMessage(broadcastPrefix + message);

                    }

                } else {

                    s.sendMessage("§4Right what are you doing... ? Can't send an empty broadcast message can you now?");
                    return false;
                }

        return true;

        }

}