package me.huntifi.castlesiege.commands.staff.currencies;

import me.huntifi.castlesiege.data_types.PlayerData;
import me.huntifi.castlesiege.events.chat.Messenger;
import org.bukkit.command.CommandSender;

/**
 * Adds to a player's coins
 */
public class AddCoins extends ChangePermanentCurrency {

    @Override
    protected String getCommandUsage() {
        return "Use: /addcoins <player> <amount>";
    }

    @Override
    protected String getQuery() {
        return "UPDATE player_stats SET coins = coins + ? WHERE uuid = ?";
    }

    @Override
    protected void changeCurrencyOnline(PlayerData data, double amount) {
        data.addCoinsClean(amount);
    }

    @Override
    protected void sendConfirmMessage(CommandSender sender, String playerName, double amount) {
        Messenger.sendInfo(String.format("%.0f coins have been given to %s", amount, playerName), sender);
    }
}
