package me.huntifi.castlesiege.voting;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import me.huntifi.castlesiege.Database.SQLGetter;

public class VotesLoading implements Listener {
	
	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("CastleSiege");

	/*/
	 One of the most important votes classes of this entire plugin. Once the player joins all their current votes
	 will be put into hashmaps together with the UUID. That way the server doesn't have to communicate with the database all the time.
	 Resulting in better server performance.
    /*/

	public static HashMap<UUID, String> PlayerVotes = new HashMap<UUID, String>();

	@EventHandler
	public void LoadStatsOnJoin(PlayerJoinEvent e) {

		Player p = e.getPlayer();
		StoreVotesOnJoin(p);

	}
	
	
	public void StoreVotesOnJoin(Player p) {
		
		UUID uuid = p.getUniqueId();

		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
			
			if (!PlayerVotes.containsKey(uuid)) { PlayerVotes.put(uuid, SQLGetter.getVotes(uuid));  }
			
		});
		
	}

}
