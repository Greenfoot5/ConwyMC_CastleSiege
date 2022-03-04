package me.huntifi.castlesiege.chat;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.huntifi.castlesiege.Database.AsyncGetters;
import me.huntifi.castlesiege.Database.AsyncGetters.BooleanCallback;
import me.huntifi.castlesiege.Database.SQLGetter;
import me.huntifi.castlesiege.playerCommands.togglerankCommand;


public class PlayerChat implements Listener {

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		e.setFormat("%s:" + ChatColor.GRAY + " %s");
	}



	@EventHandler        (priority = EventPriority.HIGHEST)
	public void onPlayerChatOwner(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();

		BooleanCallback callback = new BooleanCallback() {

			@Override
			public void gotBoolean(Boolean string) {


				if (!togglerankCommand.rankers.contains(p)) {
					if (string) {

						e.setFormat("%s:" + ChatColor.WHITE + " %s");

					}

				}
			}
		};

		AsyncGetters.performLookupRank(callback, SQLGetter.getStaffRank(p.getUniqueId()).equalsIgnoreCase("Admin"), p, true);
		
		BooleanCallback callback2 = new BooleanCallback() {

			@Override
			public void gotBoolean(Boolean string) {


				if (!togglerankCommand.rankers.contains(p)) {
					if (string) {

						e.setFormat("%s:" + ChatColor.WHITE + " %s");

					}

				}
			}
		};

		AsyncGetters.performLookupRank(callback2, SQLGetter.getStaffRank(p.getUniqueId()).equalsIgnoreCase("Moderator"), p, true);

		
		BooleanCallback callback3 = new BooleanCallback() {

			@Override
			public void gotBoolean(Boolean string) {


				if (!togglerankCommand.rankers.contains(p)) {
					if (string) {

						e.setFormat("%s:" + ChatColor.WHITE + " %s");

					}

				}
			}
		};

		AsyncGetters.performLookupRank(callback3, SQLGetter.getStaffRank(p.getUniqueId()).equalsIgnoreCase("Developer"), p, true);
		
		
		BooleanCallback callback4 = new BooleanCallback() {

			@Override
			public void gotBoolean(Boolean string) {


				if (!togglerankCommand.rankers.contains(p)) {
					if (string) {

						e.setFormat("%s:" + ChatColor.WHITE + " %s");

					}

				}
			}
		};

		AsyncGetters.performLookupRank(callback4, SQLGetter.getStaffRank(p.getUniqueId()).equalsIgnoreCase("ChatMod"), p, true);
		
		
		BooleanCallback callback5 = new BooleanCallback() {

			@Override
			public void gotBoolean(Boolean string) {


				if (!togglerankCommand.rankers.contains(p)) {
					if (string) {

						e.setFormat("%s:" + ChatColor.WHITE + " %s");

					}

				}
			}
		};

		AsyncGetters.performLookupRank(callback5, SQLGetter.getStaffRank(p.getUniqueId()).equalsIgnoreCase("ChatMod+"), p, true);
		
	}

}


