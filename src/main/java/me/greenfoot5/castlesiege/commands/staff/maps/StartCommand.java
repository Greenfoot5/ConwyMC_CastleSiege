package me.greenfoot5.castlesiege.commands.staff.maps;

import me.greenfoot5.castlesiege.maps.MapController;
import me.greenfoot5.conwymc.util.Messenger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * Moves the map onto the next phase if it hasn't already begun
 */
public class StartCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (MapController.isOngoing()) {
            Messenger.sendError("Map is ongoing, we can't start it!", sender);
            return true;
        } else if (MapController.getCurrentMap().hasMapEnded()) {
            Messenger.sendError("Map has ended, we can't start it!", sender);
            return true;
        }

        MapController.timer.startTimer();
        return true;
    }
}
