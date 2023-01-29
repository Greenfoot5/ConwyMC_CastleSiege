package me.huntifi.castlesiege.commands.staff.currencies;

import me.huntifi.castlesiege.data_types.PlayerData;
import me.huntifi.castlesiege.events.chat.Messenger;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Sets a player's coins
 */
public class SetCoins extends ChangePermanentCurrency {

    @Override
    protected String getCommandUsage() {
        return "Use: /setcoins <player> <amount>";
    }

    @Override
    protected String getQuery() {
        return "UPDATE player_stats SET coins = ? WHERE uuid = ?";
    }

    @Override
    protected void changeCurrencyOnline(PlayerData data, double amount) {
        data.setCoins(amount);
    }

    @Override
    protected void sendConfirmMessage(CommandSender sender, String playerName, double amount) {
        Messenger.sendInfo(String.format("%s's coins have been set to %.0f", playerName, amount), sender);
    }

    @Override
    protected void sendTargetMessage(Player target, double amount) {
        Messenger.sendInfo(String.format("Your coins have been set to %.0f by staff", amount), target);
    }
}
