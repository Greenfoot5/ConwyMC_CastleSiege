package me.huntifi.castlesiege.kits.kits.team_kits;

import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.events.death.DeathEvent;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.kits.kits.TeamKit;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.NameTag;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;


public class HelmsDeepBerserker extends TeamKit implements Listener {


    public HelmsDeepBerserker() {
        super("Uruk Berserker", 145, 6, "Helm's Deep", "Uruk-hai", 2500);

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Weapon
        es.hotbar[0] = ItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                ChatColor.GREEN + "Cleaver", Collections.singletonList(ChatColor.AQUA + "Deals AOE damage but has a cooldown."),
                null, 35.0);
        // Voted Weapon
        es.votedWeapon = new Tuple<>(
                ItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                        ChatColor.GREEN + "Cleaver",
                        Arrays.asList(ChatColor.AQUA + "Deals AOE damage but has a cooldown.",
                                ChatColor.AQUA + "- voted: +2 damage"),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), 37.0),
                0);

        // Leggings
        es.legs = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_LEGGINGS),
                ChatColor.GREEN + "Leather Leggings", null, null,
                Color.fromRGB(95, 69, 6));

        // Boots
        es.feet = ItemCreator.item(new ItemStack(Material.IRON_BOOTS),
                ChatColor.GREEN + "Iron Boots", null, null);
        // Voted Boots
        es.votedFeet = ItemCreator.item(new ItemStack(Material.IRON_BOOTS),
                ChatColor.GREEN + "Iron Boots",
                Collections.singletonList(ChatColor.AQUA + "- voted: Depth Strider II"),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)));

        // Ladders
        es.hotbar[1] = new ItemStack(Material.LADDER, 4);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, 6), 1);

        super.potionEffects.add(new PotionEffect(PotionEffectType.SPEED, 999999, 0));

        super.equipment = es;

        super.canClimb = true;

        // Death Messages
        super.deathMessage[0] = "You were cut in half by ";
        super.killMessage[0] = " cut deeply into ";

    }

    /**
     * Activate the Uruk Berserker cleave ability
     * @param e The event called when hitting another player
     */
    @EventHandler
    public void onCleave(EntityDamageByEntityEvent e) {
        if (e.isCancelled()) {
            return;
        }

        if (e.getDamager() instanceof Player) {
            Player q = (Player) e.getDamager();
            if (e.getEntity() instanceof Player) {
                Player p = (Player) e.getEntity();

                // Uruk Berserker tries to cleave every enemy player around them
                if (Objects.equals(Kit.equippedKits.get(q.getUniqueId()).name, name) &&
                        MapController.getCurrentMap().getTeam(q.getUniqueId())
                                != MapController.getCurrentMap().getTeam(p.getUniqueId()) &&
                        q.getInventory().getItemInMainHand().getType() == Material.IRON_SWORD &&
                        q.getCooldown(Material.IRON_SWORD) == 0) {
                    q.setCooldown(Material.IRON_SWORD, 60);

                    // Enemy blocks stun
                    if (p.isBlocking()) {
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                                ChatColor.AQUA + "You blocked " + NameTag.color(q) + q.getName() + ChatColor.AQUA + "'s cleave"));
                    } else {
                        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_BIG_FALL, 1, 1);
                        e.setDamage(e.getDamage() * 2);

                        for (Player o : Bukkit.getOnlinePlayers()) {
                            if (MapController.getCurrentMap().getTeam(o.getUniqueId())
                                    != MapController.getCurrentMap().getTeam(q.getUniqueId())) {
                                if (p.getWorld() != o.getWorld() || o == p || o == q) {
                                    return;
                                }

                                if (o.getLocation().distance(p.getLocation()) < 2.1) {
                                    if ((o.getHealth() - (e.getDamage() * 1.5) > 0)) {
                                        o.damage(e.getDamage() * 1.5);
                                    } else {
                                        e.setCancelled(true);
                                        DeathEvent.setKiller(o, p);
                                        o.setHealth(0);
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                Entity creature = e.getEntity();
                if (creature instanceof Horse) {
                    Horse horse = (Horse) creature;
                    if (creature.getPassengers() != null) {
                        if (creature.getPassengers().get(0) instanceof Player) {
                            Player p = ((Player) creature.getPassengers().get(0)).getPlayer();
                            if (MapController.getCurrentMap().getTeam(p.getUniqueId())
                                    != MapController.getCurrentMap().getTeam(q.getUniqueId())) {
                                    horse.damage(e.getDamage() * 2.5);
                            }
                        }
                    }
                }
            }
        }
    }
}
