package me.huntifi.castlesiege.commands.staff.currencies;

import me.huntifi.castlesiege.data_types.PlayerData;
import me.huntifi.castlesiege.events.chat.Messenger;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Adds to a player's coins
 */
public class AddBattlepoints extends ChangeCurrency {

    @Override
    protected String getCommandUsage() {
        return "Use: /addbattlepoints <player> <amount>";
    }

    @Override
    protected void changeCurrencyOnline(PlayerData data, double amount) {
        data.addBattlepointsClean(amount);
    }

    @Override
    protected void sendConfirmMessage(CommandSender sender, String playerName, double amount) {
        Messenger.sendInfo(String.format("%.0f battlepoints have been given to %s", amount, playerName), sender);
    }

    @Override
    protected void sendTargetMessage(Player target, double amount) {
        Messenger.sendInfo(String.format("You have been given %.0f battlepoints by staff", amount), target);
    }
}
