package me.huntifi.castlesiege.commands.donator;

import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.conwymc.util.Messenger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages custom join messages
 */
public class JoinMessage implements TabExecutor {

    /**
     * Change the player's custom join message if valid arguments are supplied
     * @param sender Source of the command
     * @param cmd Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return Whether valid arguments are supplied
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            Messenger.sendError("Console cannot set their join message!", sender);
            return true;
        }

        if (args.length != 0 && args[0].equalsIgnoreCase("reset")) {
            setMessage((Player) sender, "");
            return true;
        } else if (args.length < 2) {
            return false;
        } else if (args[0].equalsIgnoreCase("set")) {
            setMessage((Player) sender, String.join(" ", String.join(" ", args).split(" ", 2)[1]));
            return true;
        }

        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        List<String> options = new ArrayList<>();
        // Command needs a player and a message
        if (args.length == 0) {
            return null;
        }

        if (args.length == 1) {
            List<String> values = new ArrayList<>();
            values.add("set");
            values.add("reset");
            StringUtil.copyPartialMatches(args[0], values, options);
        }

        return options;
    }

    /**
     * Set the player's custom join message
     * @param p The player
     * @param message The message
     */
    private void setMessage(Player p, String message) {
        if (message.length() > 128) {
            Messenger.sendError("Your message cannot be longer than 128 characters!", p);
            return;
        }

        // Remove any MM formatting
        Component c = Messenger.mm.deserialize(message);
        message = PlainTextComponentSerializer.plainText().serialize(c);

        ActiveData.getData(p.getUniqueId()).setJoinMessage(message);
        if (message.isEmpty()) {
            Messenger.sendSuccess("Your join message has been reset.", p);
        } else {
            Messenger.sendSuccess("Your join message has been set to: <yellow>" + message, p);
        }
    }
}
