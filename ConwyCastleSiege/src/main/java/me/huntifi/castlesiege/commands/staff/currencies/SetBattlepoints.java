package me.huntifi.castlesiege.commands.staff.currencies;

import me.huntifi.castlesiege.data_types.PlayerData;
import me.huntifi.castlesiege.events.chat.Messenger;
import org.bukkit.command.CommandSender;

/**
 * Sets a player's coins
 */
public class SetBattlepoints extends ChangeCurrency {

    @Override
    protected String getCommandUsage() {
        return "Use: /setbattlepoints <player> <amount>";
    }

    @Override
    protected void changeCurrencyOnline(PlayerData data, double amount) {
        data.setBattlepoints(amount);
    }

    @Override
    protected void sendConfirmMessage(CommandSender sender, String playerName, double amount) {
        Messenger.sendInfo(String.format("%s's battlepoints have been set to %.0f", playerName, amount), sender);
    }
}
