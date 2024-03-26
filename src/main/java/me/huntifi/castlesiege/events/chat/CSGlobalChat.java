package me.huntifi.castlesiege.events.chat;

import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import me.huntifi.castlesiege.data_types.CSPlayerData;
import me.huntifi.castlesiege.database.CSActiveData;
import me.huntifi.castlesiege.misc.CSNameTag;
import me.huntifi.conwymc.commands.chat.GlobalChatCommand;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

/**
 * Handles adding a level prefix to player's names when chatting
 */
public class CSGlobalChat implements Listener, ChatRenderer {
    /**
     * Used to set the renderer for when a player chats in global
     * @param e The player chat event
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChat(AsyncChatEvent e) {

        CSPlayerData data = CSActiveData.getData(e.getPlayer().getUniqueId());
        // This can be overridden by changing the message before
        if (e.originalMessage() == e.message() &&
                data != null && data.getChatMode().equalsIgnoreCase(GlobalChatCommand.CHAT_MODE))
            e.renderer(this);
    }

    public @NotNull Component render(@NotNull Player source, @NotNull Component sourceDisplayName, @NotNull Component message, @NotNull Audience viewer) {
        sourceDisplayName = CSNameTag.level(source, viewer).append(sourceDisplayName);
        return new GlobalChatCommand().render(source, sourceDisplayName, message, viewer);
    }
}
