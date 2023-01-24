package me.huntifi.castlesiege.commands.staff.battlepoints;

import me.huntifi.castlesiege.data_types.PlayerData;
import me.huntifi.castlesiege.events.chat.Messenger;
import org.bukkit.command.CommandSender;

/**
 * Adds to a player's coins
 */
public class AddBattlepoints extends ChangeBattlepoints {

    @Override
    protected String getCommandUsage() {
        return "Use: /addbattlepoints <player> <amount>";
    }

    @Override
    protected void changeBattlepointsOnline(PlayerData data, double amount) {
        data.addBattlepointsClean(amount);
    }

    @Override
    protected void sendConfirmMessage(CommandSender sender, String playerName, double amount) {
        Messenger.sendInfo(String.format("%.0f battlepoints have been given to %s", amount, playerName), sender);
    }
}
