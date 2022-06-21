package me.huntifi.castlesiege.kits.kits;

import me.huntifi.castlesiege.database.LoadData;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.maps.MapController;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class DonatorKit extends Kit {

    protected double price;


    public DonatorKit(String name, int baseHealth, double regenAmount, double coins) {
        super(name, baseHealth, regenAmount);
        price = coins;
    }

    /**
     *
     * @param sender Source of the command
     * @param command Command which was executed
     * @param s Alias of the command which was used
     * @param strings Passed command arguments
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (sender instanceof ConsoleCommandSender) {
            Messenger.sendError("Console cannot select kits!", sender);
            return true;

        } else if (sender instanceof Player) {
            if (MapController.isSpectator(((Player) sender).getUniqueId())) {
                Messenger.sendError("Spectators cannot select kits!", sender);
                return true;
            }

            Player player = (Player) sender;
            boolean hasKit = LoadData.hasKit(((Player) sender).getUniqueId(), name.replace(" ", ""));
            if (!hasKit) {
                Messenger.sendError("You don't own this kit!" , sender);
                return true;
            }

            super.addPlayer(player.getUniqueId());
            return true;
        }
        return false;
    }
}
