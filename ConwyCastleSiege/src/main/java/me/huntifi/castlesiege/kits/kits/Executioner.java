package me.huntifi.castlesiege.kits.kits;

import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.joinevents.stats.StatsChanging;
import me.huntifi.castlesiege.kits.EquipmentSet;
import me.huntifi.castlesiege.kits.Kit;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.teams.PlayerTeam;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static org.bukkit.Color.*;

public class Executioner extends Kit implements Listener, CommandExecutor {
	public Executioner() {
		super("Executioner");
		super.baseHeath = 115;

		// Equipment Stuff
		EquipmentSet es = new EquipmentSet();
		super.heldItemSlot = 0;
                
		// Weapon
		ItemStack item = new ItemStack(Material.IRON_AXE, 1);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		itemMeta.setUnbreakable(true);
		itemMeta.setDisplayName(ChatColor.GREEN + "Iron Axe");
		itemMeta.addEnchant(Enchantment.DAMAGE_ALL, 20, true);
		itemMeta.setLore(new ArrayList<>());
		item.setItemMeta(itemMeta);
		es.hotbar[0] = item;
		// Voted Weapon
		item.getItemMeta().addEnchant(Enchantment.DAMAGE_ALL, 22, true);
		item.getItemMeta().setLore(Arrays.asList("", ChatColor.AQUA + "- voted: +2 damage"));
		es.votedWeapon = new Tuple<>(item, 0);
                
		//Chestplate
		item = new ItemStack(Material.LEATHER_CHESTPLATE);
		LeatherArmorMeta leatherItemMeta = (LeatherArmorMeta) item.getItemMeta();
		leatherItemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		leatherItemMeta.setDisplayName(ChatColor.GREEN + "Leather Chestplate");
		leatherItemMeta.setColor(fromRGB(32, 32, 32));
		leatherItemMeta.setUnbreakable(true);
		leatherItemMeta.setLore(new ArrayList<>());
		item.setItemMeta(leatherItemMeta);
		es.chest = item;
                
		//Leggings
		item = new ItemStack(Material.LEATHER_LEGGINGS);
		leatherItemMeta = (LeatherArmorMeta) item.getItemMeta();
		leatherItemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		leatherItemMeta.setDisplayName(ChatColor.GREEN + "Leather Leggings");
		leatherItemMeta.setColor(fromRGB(32, 32, 32));
		leatherItemMeta.setUnbreakable(true);
		leatherItemMeta.setLore(new ArrayList<>());
		item.setItemMeta(leatherItemMeta);
		es.legs = item;
                
		// Boots
		item = new ItemStack(Material.IRON_BOOTS);
		itemMeta = item.getItemMeta();
		itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		itemMeta.setDisplayName(ChatColor.GREEN + "Iron Boots");
		itemMeta.setUnbreakable(true);
		itemMeta.setLore(new ArrayList<>());
		item.setItemMeta(itemMeta);
		es.feet = item;
		// Voted Boots
		item.getItemMeta().addEnchant(Enchantment.DEPTH_STRIDER, 2, true);
		es.votedFeet = item;
                
		// Ladders
		es.hotbar[1] = new ItemStack(Material.LADDER, 4);
		es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, 6), 1);
                
		super.equipment = es;


		// Death Messages
		super.deathMessage[0] = "You were decapitated by ";
		super.killMessage[0] = "You decapitated ";
        }
        
        @Override
	public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
		super.addPlayer(((Player) commandSender).getUniqueId());
		return true;
	}
        
	@EventHandler
	public void Exe1(EntityDamageByEntityEvent ed) {

		if (ed.getEntity() instanceof Player && ed.getDamager() instanceof Player) {
			Player whoWasHit = (Player) ed.getEntity();
			Player whoHit = (Player) ed.getDamager();

			if (Objects.equals(Kit.equippedKits.get(whoHit.getUniqueId()).name, name) &&
					whoHit.getInventory().getItemInMainHand().getType() == Material.IRON_AXE) {
				// Check they aren't on the same team
				// TODO - Check if needed, as players on the same team shouldn't be able to hit another
				if (MapController.getCurrentMap().getTeam(whoHit.getUniqueId())
						!= MapController.getCurrentMap().getTeam(whoWasHit.getUniqueId())) {

					AttributeInstance healthAttribute = whoWasHit.getAttribute(Attribute.GENERIC_MAX_HEALTH);
					assert healthAttribute != null;

					if (whoWasHit.getHealth() < healthAttribute.getValue() * 0.37) {
						ed.setCancelled(true);
						Location loc = whoWasHit.getLocation();
						whoWasHit.getWorld().playSound(loc, Sound.ENTITY_IRON_GOLEM_DEATH, 1, 1);
						whoWasHit.setLastDamageCause(new EntityDamageByEntityEvent(whoHit, whoWasHit,
								EntityDamageEvent.DamageCause.ENTITY_ATTACK, whoWasHit.getHealth()));
						whoWasHit.setHealth(0);
					}
				}
			}
		}
	}
}
