package me.huntifi.castlesiege.kits.Warhound;

import org.bukkit.DyeColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.huntifi.castlesiege.joinevents.stats.StatsChanging;
import me.huntifi.castlesiege.kits.Woolheads;
import me.huntifi.castlesiege.tags.NametagsEvent;
import me.huntifi.castlesiege.voting.VotesChanging;
import me.huntifi.castlesiege.woolmap.LobbyPlayer;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import me.libraryaddict.disguise.disguisetypes.watchers.WolfWatcher;
import me.libraryaddict.disguise.events.DisguiseEvent;
import me.libraryaddict.disguise.events.UndisguiseEvent;

public class Warhound implements Listener {

	public static void setItems(Player p) {

		AttributeInstance healthAttribute = p.getAttribute(Attribute.GENERIC_MAX_HEALTH);
		healthAttribute.setBaseValue(110);
		p.setHealthScaled(true);

		if (LobbyPlayer.containsPlayer(p)) { p.setHealth(110); }

		Woolheads.setHead(p);

		for (PotionEffect effect : p.getActivePotionEffects())
			p.removePotionEffect(effect.getType());

		p.addPotionEffect((new PotionEffect(PotionEffectType.SPEED, 999999, 3)));
		p.addPotionEffect((new PotionEffect(PotionEffectType.NIGHT_VISION, 999999, 0)));
		p.addPotionEffect((new PotionEffect(PotionEffectType.JUMP, 999999, 0)));
		p.addPotionEffect((new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 999999, 0)));

		p.setHealthScaled(true);

		p.getInventory().setBoots(null);
		p.getInventory().setChestplate(null);
		p.getInventory().setLeggings(null);

		WarhoundKit giveItems = new WarhoundKit();
		WarhoundVotedKit giveVotedItems = new WarhoundVotedKit();

		ItemStack sword = giveItems.getWarhoundFangs();
		ItemStack swordVoted = giveVotedItems.getWarhoundVotedFangs();


		if (!VotesChanging.getVotes(p.getUniqueId()).contains("V#1")) {

			p.getInventory().setItem(0, sword);

		} else {

			p.getInventory().setItem(0, swordVoted);

		}

	}



	public static void wolfPlayer(Player p) {

		if (StatsChanging.getKit(p.getUniqueId()).equalsIgnoreCase("Warhound")) {

			MobDisguise mobDisguise = new MobDisguise(DisguiseType.WOLF);
			WolfWatcher wolfWatcher = (WolfWatcher) mobDisguise.getWatcher();

			wolfWatcher.setCollarColor(getWolfTeamColour(p));
			wolfWatcher.setTamed(true);
			
			mobDisguise.getWatcher().setCustomName(NametagsEvent.colour(p) + p.getName());
			mobDisguise.setHearSelfDisguise(true);
			mobDisguise.setEntity(p);
			mobDisguise.setCustomDisguiseName(true);
			mobDisguise.startDisguise();
			

		}

	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onDisguiseWolf(DisguiseEvent e) {

		if (e.getEntity() instanceof Player) {

			Player p = (Player) e.getEntity();

			if (StatsChanging.getKit(p.getUniqueId()).equalsIgnoreCase("Warhound")) {

				e.getDisguise().setDisguiseName(NametagsEvent.colour(p) + p.getName());

				e.getDisguise().setSelfDisguiseVisible(false);
				
				e.getDisguise().setDynamicName(true);

				e.getDisguise().setViewSelfDisguise(false);

			}	
		}	
	}

	@EventHandler
	public void onDisguiseWolf(UndisguiseEvent e) {

		if (e.getEntity() instanceof Player) {

			Player p = (Player) e.getEntity();

			if (StatsChanging.getKit(p.getUniqueId()).equalsIgnoreCase("Warhound")) {

				NametagsEvent.GiveNametag(p);

			}	
		}	
	}

	public static void removeWolfPlayer(Player p) {

		if (!StatsChanging.getKit(p.getUniqueId()).equalsIgnoreCase("Warhound")) {

			if (DisguiseAPI.isDisguised(p)) {

				DisguiseAPI.undisguiseToAll(p);

			}

		}

	}

	public static DyeColor getWolfTeamColour(Player p) {

		if (NametagsEvent.colour(p).equals("§8")) {

			return DyeColor.GRAY;
		}

		if (NametagsEvent.colour(p).equals("§0")) {

			return DyeColor.BLACK;
		}

		if (NametagsEvent.colour(p).equals("§2")) {

			return DyeColor.GREEN;
		}

		if (NametagsEvent.colour(p).equals("§4")) {

			return DyeColor.RED;
		}

		if (NametagsEvent.colour(p).equals("§6")) {

			return DyeColor.ORANGE;
		}

		if (NametagsEvent.colour(p).equals("§a")) {

			return DyeColor.LIME;
		}

		if (NametagsEvent.colour(p).equals("§c")) {

			return DyeColor.RED;
		}

		if (NametagsEvent.colour(p).equals("§e")) {

			return DyeColor.YELLOW;
		}
		
		if (NametagsEvent.colour(p).equals("§1")) {

			return DyeColor.BLUE;
		}
		
		if (NametagsEvent.colour(p).equals("§3")) {

			return DyeColor.CYAN;
		}
		
		if (NametagsEvent.colour(p).equals("§5")) {

			return DyeColor.PURPLE;
		}
		
		if (NametagsEvent.colour(p).equals("§7")) {

			return DyeColor.GRAY;
		}
		
		if (NametagsEvent.colour(p).equals("§9")) {

			return DyeColor.BLUE;
		}
		
		if (NametagsEvent.colour(p).equals("§b")) {

			return DyeColor.LIGHT_BLUE;
		}
		
		if (NametagsEvent.colour(p).equals("§d")) {

			return DyeColor.MAGENTA;
		}
		
		if (NametagsEvent.colour(p).equals("§f")) {

			return DyeColor.WHITE;
		}


		return DyeColor.LIGHT_GRAY;

	}

}
