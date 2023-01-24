package me.huntifi.castlesiege.commands.staff.battlepoints;

import me.huntifi.castlesiege.data_types.PlayerData;
import me.huntifi.castlesiege.events.chat.Messenger;
import org.bukkit.command.CommandSender;

/**
 * Sets a player's coins
 */
public class SetBattlepoints extends ChangeBattlepoints {

    @Override
    protected String getCommandUsage() {
        return "Use: /setbattlepoints <player> <amount>";
    }

    @Override
    protected void changeBattlepointsOnline(PlayerData data, double amount) {
        data.setBattlepoints(amount);
    }

    @Override
    protected void sendConfirmMessage(CommandSender sender, String playerName, double amount) {
        Messenger.sendInfo(String.format("%s's battlepoints have been set to %.0f", playerName, amount), sender);
    }
}
