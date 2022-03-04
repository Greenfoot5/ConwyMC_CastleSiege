package me.huntifi.castlesiege.tags;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.nametagedit.plugin.NametagEdit;

import me.huntifi.castlesiege.joinevents.stats.StatsChanging;
import me.huntifi.castlesiege.maps.currentMaps;
import me.huntifi.castlesiege.playerCommands.togglerankCommand;
import me.huntifi.castlesiege.teams.PlayerTeam;


public class NametagsEvent {

	static Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("ConwyCastleSiege");

	public static void GiveNametag(Player p) {
		
		if (p == null) { return; }

		int level = StatsChanging.getLevel(p.getUniqueId());
		
		if (!(togglerankCommand.rankers.contains(p))) {
			
		if (StatsChanging.getStaffRank(p.getUniqueId()).equalsIgnoreCase("ChatMod")) {

			p.setDisplayName("§e" + level + "§9" + " ChatMod " + colour(p) + p.getName());

			NametagEdit.getApi().setPrefix(p, "§9" + "ChatMod " + colour(p));
			
			return;

		} else

		if (StatsChanging.getStaffRank(p.getUniqueId()).equalsIgnoreCase("ChatMod+")) {

			p.setDisplayName("§e" + level + "§9" + " ChatMod" + "§a" + "+ " + colour(p) + p.getName());

			NametagEdit.getApi().setPrefix(p, "§9" + "ChatMod" + "§a" + "+ " + colour(p));
			
			return;

		} else

		if (StatsChanging.getStaffRank(p.getUniqueId()).equalsIgnoreCase("Moderator")) {


			p.setDisplayName("§e" + level + "§a" + " Mod " + colour(p) + p.getName());

			NametagEdit.getApi().setPrefix(p, "§a" + "Mod " + colour(p));
			
			return;

		} else

		if (StatsChanging.getStaffRank(p.getUniqueId()).equalsIgnoreCase("Developer")) {


			p.setDisplayName("§e" + level + "§2" + " Dev " + colour(p) + p.getName());

			NametagEdit.getApi().setPrefix(p, "§2" + "Dev " + colour(p));
			
			return;

		} else

		if (StatsChanging.getStaffRank(p.getUniqueId()).equalsIgnoreCase("Admin")) {


			p.setDisplayName("§e" + level + "§c" + " Admin " + colour(p) + p.getName());

			NametagEdit.getApi().setPrefix(p, "§c" + "Admin " + colour(p));
			
			return;

		} else

		if (StatsChanging.getStaffRank(p.getUniqueId()).equalsIgnoreCase("None")) {

			if (StatsChanging.getRank(p.getUniqueId()).equalsIgnoreCase("None")) {


				p.setDisplayName("§e" + level + " " + colour(p) + p.getName());

				NametagEdit.getApi().setPrefix(p, colour(p) + "");
				
				return;

			}
		}

			if (StatsChanging.getRank(p.getUniqueId()).equalsIgnoreCase("Esquire")) {


				p.setDisplayName("§e" + level + "§3" + " Esquire " + colour(p) + p.getName());

				NametagEdit.getApi().setPrefix(p, "§3" + "Esquire " + colour(p));
				
				return;

			} else


			if (StatsChanging.getRank(p.getUniqueId()).equalsIgnoreCase("Noble")) {

				p.setDisplayName("§e" + level + "§a" + " Noble " + colour(p) + p.getName());

				NametagEdit.getApi().setPrefix(p, "§a" + "Noble " + colour(p));
				
				return;
				
			} else

			if (StatsChanging.getRank(p.getUniqueId()).equalsIgnoreCase("Count")) {


				p.setDisplayName("§e" + level + "§6" + " Count " + colour(p) + p.getName());

				NametagEdit.getApi().setPrefix(p, "§6" + "Count " + colour(p));
				
				return;

			} else

			if (StatsChanging.getRank(p.getUniqueId()).equalsIgnoreCase("Baron")) {

				p.setDisplayName("§e" + level + "§5" + " Baron " + colour(p) + p.getName());

				NametagEdit.getApi().setPrefix(p, "§5" + "Baron " + colour(p));
				
				return;

			} else

			if (StatsChanging.getRank(p.getUniqueId()).equalsIgnoreCase("Duke")) {


				p.setDisplayName("§e" + level + "§4" + " Duke " + colour(p) + p.getName());

				NametagEdit.getApi().setPrefix(p, "§4" + "Duke " + colour(p));
				
				return;

			} else

			if (StatsChanging.getRank(p.getUniqueId()).equalsIgnoreCase("King")) {


				p.setDisplayName("§e" + level + "§e" + " King " + colour(p) + p.getName());

				NametagEdit.getApi().setPrefix(p, "§e" + "King " + colour(p));
				
				return;

			} else

			if (StatsChanging.getRank(p.getUniqueId()).equalsIgnoreCase("HighKing")) {


				p.setDisplayName("§e" + level + "§e" + " High King " + colour(p) + p.getName());

				NametagEdit.getApi().setPrefix(p, "§e" + "High King " + colour(p));

				return;
				
			}
			
		} else {
			
			p.setDisplayName("§e" + level + " " + colour(p) + p.getName());

			NametagEdit.getApi().setPrefix(p, colour(p) + "");
			
			return;
			
		}
		
	}


	public static String colour(Player p) {

		if (currentMaps.currentMapIs("HelmsDeep")) {

			if (PlayerTeam.playerIsInTeam(p, 1)) {

				return "§8";

			}

			if (PlayerTeam.playerIsInTeam(p, 2)) {

				return "§2";

			}

		} else
		
		if (currentMaps.currentMapIs("Thunderstone")) {

			if (PlayerTeam.playerIsInTeam(p, 1)) {

				return "§3";

			}

			if (PlayerTeam.playerIsInTeam(p, 2)) {

				return "§6";

			}

		}

		return "§7";

	}

}
