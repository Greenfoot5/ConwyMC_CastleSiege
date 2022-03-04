package me.huntifi.castlesiege.stats.mystats;

import java.text.DecimalFormat;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import me.huntifi.castlesiege.joinevents.stats.StatsChanging;
import net.md_5.bungee.api.chat.BaseComponent;

public class MystatsBook {
	
	public static ItemStack newBook(Player p)
	{
	  BookMeta meta = (BookMeta) Bukkit.getItemFactory().getItemMeta(Material.WRITTEN_BOOK);
	  meta.setTitle(ChatColor.GREEN + p.getName() + ChatColor.WHITE + "'s Stats.");
	  meta.setAuthor(ChatColor.RED + "Hunt von Huntington");
	  meta.setPages("");
	  
	  
	  
	  List<BaseComponent[]> pages = meta.spigot().getPages();
	  
	  meta.spigot().setPages(pages);
	  
	  DecimalFormat currencyFormat = new DecimalFormat("0.00");
	  DecimalFormat NumberFormat = new DecimalFormat("0");
	  
	  
	  String KDR = currencyFormat.format((StatsChanging.getKills(p.getUniqueId()) / StatsChanging.getDeaths(p.getUniqueId())));
	  String Score = NumberFormat.format(StatsChanging.getScore(p.getUniqueId()));
	  //String NextLvl = NumberFormat.format(LevelingSystem.getEXPLevel(p));
	  String Kills = NumberFormat.format(StatsChanging.getKills(p.getUniqueId()));
	  String Deaths = NumberFormat.format(StatsChanging.getDeaths(p.getUniqueId()));
	  String Assists = NumberFormat.format(StatsChanging.getAssists(p.getUniqueId()));
	  String Heals = NumberFormat.format(StatsChanging.getHeals(p.getUniqueId()));
	  String Captures = NumberFormat.format(StatsChanging.getCaptures(p.getUniqueId()));
	  String Supports = NumberFormat.format(StatsChanging.getSupports(p.getUniqueId()));
	  String Killstreak = NumberFormat.format(StatsChanging.getKillstreak(p.getUniqueId()));
	  
	  
	  String Page1a = (ChatColor.BLACK + "Name: " + ChatColor.DARK_GRAY + p.getName());
	  String Page1b = (ChatColor.BLACK + "Rank: " + StatsChanging.getStaffRank(p.getUniqueId()));
	  String Page1c = (ChatColor.BLACK + "Donator: " + StatsChanging.getRank(p.getUniqueId()));
	  String Page1d = (ChatColor.DARK_GREEN + "Level: " + ChatColor.DARK_GRAY + StatsChanging.getLevel(p.getUniqueId()));
	 // String Page1le = (ChatColor.DARK_GREEN + "Next Level: " + ChatColor.DARK_GRAY + NextLvl);
	  String Page1e = (ChatColor.BLACK + "Score: " + ChatColor.DARK_GRAY + Score);
	  String Page1f = (ChatColor.DARK_GREEN + "Kills: " + ChatColor.DARK_GRAY + Kills);
	  String Page1g = (ChatColor.RED + "Deaths: " + ChatColor.DARK_GRAY + Deaths);
	  String PageKDR = (ChatColor.BLACK + "KDR: " + ChatColor.DARK_GRAY + KDR);
	  String Page1h = (ChatColor.BLACK + "Assists: " + ChatColor.DARK_GRAY + Assists);
	  String Page1i = (ChatColor.BLACK + "Supports: " + ChatColor.DARK_GRAY + Supports);
	  String Page1j = (ChatColor.BLACK + "Heals: " + ChatColor.DARK_GRAY + Heals);
	  String Page1ca = (ChatColor.BLACK + "Captures: " + ChatColor.DARK_GRAY + Captures);
	  String Page1k = (ChatColor.BLACK + "Killstreak: " + ChatColor.DARK_GRAY + Killstreak);
	  
	  meta.setPage(1, (Page1a + "\n\n" + Page1b + "\n" + Page1c + "\n\n" + Page1d + "\n" + Page1e + "\n" + Page1f + "\n" + Page1g +  "\n" + PageKDR + "\n" + Page1ca + "\n" + Page1h + "\n" + Page1i + "\n" + Page1j+ "\n"));
	
	  meta.addPage(Page1k);
  
	  ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
	  book.setItemMeta(meta);

	  return book;
	}
}

