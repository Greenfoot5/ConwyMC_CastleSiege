package me.huntifi.castlesiege.commands.info;

import me.huntifi.castlesiege.events.chat.Messenger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class WebshopCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        Component c = MiniMessage.miniMessage().deserialize("<green>Support the server here: </green>" +
                "<color:#13C3FF><click:open_url:https://ko-fi.com/conwymc>☕ https://ko-fi.com/conwymc ☕</click></color>");
        Messenger.send(c, sender);

        return true;
    }
}
