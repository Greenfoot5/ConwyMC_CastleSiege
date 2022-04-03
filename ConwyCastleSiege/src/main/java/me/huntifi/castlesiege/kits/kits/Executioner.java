package me.huntifi.castlesiege.kits.kits;

import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.kits.EquipmentSet;
import me.huntifi.castlesiege.kits.Kit;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

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
		itemMeta = item.getItemMeta();
		itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		itemMeta.setDisplayName(ChatColor.GREEN + "Leather Chestplate");
                itemMeta.setColor(Color.fromRGB(32, 32, 32));
		itemMeta.setUnbreakable(true);
		itemMeta.setLore(new ArrayList<>());
		item.setItemMeta(itemMeta);
		es.chest = item;
                
                //Leggings
		item = new ItemStack(Material.LEATHER_LEGGINGS);
		itemMeta = item.getItemMeta();
		itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		itemMeta.setDisplayName(ChatColor.GREEN + "Leather Leggings");
                itemMeta.setColor(Color.fromRGB(32, 32, 32));
		itemMeta.setUnbreakable(true);
		itemMeta.setLore(new ArrayList<>());
		item.setItemMeta(itemMeta);
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
		item.getItemMeta().addEnchant(Enchantment.DEPTH_STRIDER, 2, true);
		es.votedFeet = item;
                
                // Ladders
		es.hotbar[1] = new ItemStack(Material.LADDER, 4);
		es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, 6), 1);
                
                super.equipment = es;
                
                
                // Death Messages
                super.deathMessage = "You were decapitated by ";
		super.killMessage = "You decapitated ";
        }
        
        @Override
	public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
		if (command.getName().equalsIgnoreCase("Executioner")) {
			super.addPlayer(((Player) commandSender).getUniqueId());
		}
		return true;
	}
        
        @EventHandler
	public void Exe1(EntityDamageByEntityEvent ed) {

		if (ed.getEntity() instanceof Player && ed.getDamager() instanceof Player) {
			Player whoWasHit = (Player) ed.getEntity();
			Player whoHit = (Player) ed.getDamager();

			if (StatsChanging.getKit(whoHit.getUniqueId()).equalsIgnoreCase("Executioner")) {

				if (whoHit.getItemInHand().getType() == Material.IRON_AXE) {

					if (PlayerTeam.getPlayerTeam(whoHit) != PlayerTeam.getPlayerTeam(whoWasHit)) {
						
						AttributeInstance healthAttribute = whoWasHit.getAttribute(Attribute.GENERIC_MAX_HEALTH);

						if (whoWasHit.getHealth() < ((healthAttribute.getBaseValue() * 0.01) * 37)) {
							ed.setCancelled(true);
							whoWasHit.setHealth(0); 
							Location loc = whoWasHit.getLocation();
							whoWasHit.getWorld().playSound(loc, Sound.ENTITY_IRON_GOLEM_DEATH , 1, 1);


						}
					} else {
						ed.isCancelled();
						ed.setCancelled(true);
					}

				} else {
					
					ed.isCancelled();
					ed.setCancelled(true);
					
					if (PlayerTeam.getPlayerTeam(whoHit) != PlayerTeam.getPlayerTeam(whoWasHit)) {
						ed.setCancelled(false);
					}
				}
			}
		}


	}
    
}
