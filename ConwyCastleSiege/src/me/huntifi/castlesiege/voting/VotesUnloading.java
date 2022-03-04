package me.huntifi.castlesiege.voting;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import me.huntifi.castlesiege.Database.SQLGetter;

public class VotesUnloading implements Listener {

	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("ConwyCastleSiege");

	/*/
	 One of the most important votes classes of this entire plugin. Once the player leaves all their current votes
	 will be put into the database and unloaded from the lists. 
    /*/

	@EventHandler
	public void UnloadStatsOnLeave(PlayerQuitEvent e) {

		Player p = e.getPlayer();
		UnloadOnLeave(p);

	}


	public void UnloadOnLeave(Player p) {

		UUID uuid = p.getUniqueId();

		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {

			if (VotesLoading.PlayerVotes.containsKey(uuid)) {

				SQLGetter.setVotes(uuid, VotesLoading.PlayerVotes.get(uuid));
				VotesLoading.PlayerVotes.remove(uuid);  

			}

		});

	}

}
