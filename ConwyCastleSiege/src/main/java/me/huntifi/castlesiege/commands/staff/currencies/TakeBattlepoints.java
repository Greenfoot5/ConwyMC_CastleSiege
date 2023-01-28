package me.huntifi.castlesiege.commands.staff.currencies;

import me.huntifi.castlesiege.data_types.PlayerData;
import me.huntifi.castlesiege.events.chat.Messenger;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Takes from a player's coins
 */
public class TakeBattlepoints extends ChangeCurrency {

    @Override
    protected String getCommandUsage() {
        return "Use: /takebattlepoints <player> <amount>";
    }

    @Override
    protected void changeCurrencyOnline(PlayerData data, double amount) {
        data.takeBattlepointsForce(amount);
    }

    @Override
    protected void sendConfirmMessage(CommandSender sender, String playerName, double amount) {
        Messenger.sendInfo(String.format("%.0f battlepoints have been taken from %s", amount, playerName), sender);
    }

    @Override
    protected void sendTargetMessage(Player target, double amount) {
        Messenger.sendInfo(String.format("%.0f battlepoints have been taken from you", amount), target);
    }
}
