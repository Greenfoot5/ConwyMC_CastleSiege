package me.huntifi.castlesiege.kits.kits.teamKits;

import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.events.death.DeathEvent;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.kits.kits.MapKit;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.NameTag;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;


import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;


public class HelmsdeepBerserker extends MapKit implements Listener {


    public HelmsdeepBerserker() {
        super("Uruk Berserker", 120, 3);
        super.mapSpecificKits.add("Uruk Berserker");

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Weapon
        es.hotbar[0] = ItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                ChatColor.GREEN + "Cleaver", Collections.singletonList(ChatColor.AQUA + "Deals AOE damage but has a cooldown."), null, 35.0);
        // Voted Weapon
        es.votedWeapon = new Tuple<>(
                ItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                        ChatColor.GREEN + "Cleaver",
                        Arrays.asList(ChatColor.AQUA + "Deals AOE damage but has a cooldown.",
                                ChatColor.AQUA + "- voted: +2 damage"),
                        Collections.singletonList(new Tuple<>(Enchantment.SWEEPING_EDGE, 2)), 35.0),
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
                Collections.singletonList(ChatColor.AQUA + "- voted: Depth Strider +2"),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)));

        // Ladders
        es.hotbar[1] = new ItemStack(Material.LADDER, 4);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, 6), 1);

        super.equipment = es;

        super.canClimb = true;

        // Death Messages
        super.deathMessage[0] = "You were cut in half by ";
        super.killMessage[0] = "You cut deeply into ";

        super.playableWorld = "Helm's Deep";
        super.teamName = "Uruk-hai";

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

        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            Player p = (Player) e.getEntity();
            Player q = (Player) e.getDamager();

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
                    p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_BIG_FALL , 1, 1 );
                    e.setDamage(e.getDamage() * 1.5);

                    for (Player all : Bukkit.getOnlinePlayers()) {
                        if (p.getWorld() != all.getWorld()) { return; }
                        if (all == p) { return; }
                        if (all == q) { return; }
                        if (all.getLocation().distance(p.getLocation()) < 2.1) {
                            if ((all.getHealth() - e.getDamage() > 0)) {
                                all.damage(e.getDamage());
                            } else {
                                e.setCancelled(true);
                                DeathEvent.setKiller(all, p);
                                all.setHealth(0);
                            }
                        }
                    }
                }
            }
        }
    }

}
