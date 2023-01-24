package me.huntifi.castlesiege.commands.staff.battlepoints;

import me.huntifi.castlesiege.data_types.PlayerData;
import me.huntifi.castlesiege.events.chat.Messenger;
import org.bukkit.command.CommandSender;

/**
 * Takes from a player's coins
 */
public class TakeBattlepoints extends ChangeBattlepoints {

    @Override
    protected String getCommandUsage() {
        return "Use: /takebattlepoints <player> <amount>";
    }

    @Override
    protected void changeBattlepointsOnline(PlayerData data, double amount) {
        data.takeBattlepoints(amount);
    }

    @Override
    protected void sendConfirmMessage(CommandSender sender, String playerName, double amount) {
        Messenger.sendInfo(String.format("%.0f battlepoints have been taken from %s", amount, playerName), sender);
    }
}
