package me.huntifi.castlesiege.commands.staff.maps;

import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.maps.Map;
import me.huntifi.castlesiege.maps.MapController;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

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
