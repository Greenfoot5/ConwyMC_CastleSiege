package me.huntifi.castlesiege.mvpCommand;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.huntifi.castlesiege.maps.currentMaps;
import me.huntifi.castlesiege.teams.PlayerTeam;

public class mvpCommand implements CommandExecutor {

	@Override
	public boolean onCommand(final CommandSender sender, Command cmd, String label,
			String[] args) {

		Player p = (Player) sender;
		if (sender instanceof Player) {
			if (cmd.getName().equalsIgnoreCase("mvp")) {
				
				Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("ConwyCastleSiege");

				Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {

					if (currentMaps.currentMapIs("HelmsDeep")) {

						if (PlayerTeam.playerIsInTeam(p, 1)) {

							HelmsdeepMVP.returnMVP(1, p);

						}

						if (PlayerTeam.playerIsInTeam(p, 2)) {
							
							HelmsdeepMVP.returnMVP(2, p);

						}
					} else if (currentMaps.currentMapIs("Thunderstone")) {

						if (PlayerTeam.playerIsInTeam(p, 1)) {

							ThunderstoneMVP.returnMVP(1, p);

						}

						if (PlayerTeam.playerIsInTeam(p, 2)) {
							
							ThunderstoneMVP.returnMVP(2, p);

						}
					}
				});

				return true;
			}
		}
		return true;
	}
}
