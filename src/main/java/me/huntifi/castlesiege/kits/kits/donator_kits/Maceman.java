package me.huntifi.castlesiege.kits.kits.donator_kits;

import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.kits.kits.DonatorKit;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.maps.NameTag;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collections;
import java.util.Objects;
import java.util.Random;

/**
 * The maceman kit
 */
public class Maceman extends DonatorKit implements Listener {

    /**
     * Set the equipment and attributes of this kit
     */
    public Maceman() {
        super("Maceman", 360, 12, 8500, 9, Material.DIAMOND_SHOVEL);

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Weapon
        es.hotbar[0] = ItemCreator.weapon(new ItemStack(Material.DIAMOND_SHOVEL),
                ChatColor.GREEN + "Mace", null, null, 37);
        // Voted Weapon
        es.votedWeapon = new Tuple<>(
                ItemCreator.weapon(new ItemStack(Material.DIAMOND_SHOVEL),
                        ChatColor.GREEN + "Mace",
                        Collections.singletonList(ChatColor.AQUA + "- voted: +2 damage"),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), 39),
                0);

        // Chestplate
        es.chest = ItemCreator.item(new ItemStack(Material.CHAINMAIL_CHESTPLATE),
                ChatColor.GREEN + "Chainmail Chestplate", null, null);

        // Leggings
        es.legs = ItemCreator.item(new ItemStack(Material.LEATHER_LEGGINGS),
                ChatColor.GREEN + "Leather Leggings", null, null);

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

        super.equipment = es;


        // Death Messages
        super.deathMessage[0] = "Your skull was cracked by ";
        super.deathMessage[1] = "'s mace";
        super.killMessage[0] = " cracked ";
        super.killMessage[1] = "'s skull";
    }

    /**
     * Activate the maceman stun ability
     * @param e The event called when hitting another player
     */
    @EventHandler (ignoreCancelled = true)
    public void onStun(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            Player p = (Player) e.getEntity();
            Player q = (Player) e.getDamager();

            // Maceman tries to use stun an enemy
            if (Objects.equals(Kit.equippedKits.get(q.getUniqueId()).name, name) &&
                    q.getInventory().getItemInMainHand().getType() == Material.DIAMOND_SHOVEL &&
                    q.getCooldown(Material.DIAMOND_SHOVEL) == 0) {
                q.setCooldown(Material.DIAMOND_SHOVEL, 200);

                // Enemy blocks stun
                if (p.isBlocking()) {
                    q.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                            NameTag.color(p) + p.getName() + ChatColor.AQUA + " blocked your stun"));
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                            ChatColor.AQUA + "You blocked " + NameTag.color(q) + q.getName() + ChatColor.AQUA + "'s stun"));
                } else if (p.isSneaking() && new Random().nextInt(4) == 0) {
                    q.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                            NameTag.color(p) + p.getName() + ChatColor.AQUA + " dodged your stun"));
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                            ChatColor.AQUA + "You dodged " + NameTag.color(q) + q.getName() + ChatColor.AQUA + "'s stun"));
                } else {
                    q.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                            ChatColor.AQUA + "You have stunned " + NameTag.color(p) + p.getName()));
                    p.sendMessage(ChatColor.DARK_RED + "You have been stunned by " + NameTag.color(q) + q.getName() + ChatColor.DARK_RED + "!");
                    p.getWorld().playSound(p.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST_FAR , 1, 1 );
                    p.addPotionEffect((new PotionEffect(PotionEffectType.BLINDNESS, 60, 1)));
                    p.addPotionEffect((new PotionEffect(PotionEffectType.SLOW, 60, 2)));
                    p.addPotionEffect((new PotionEffect(PotionEffectType.CONFUSION, 80, 4)));
                    p.addPotionEffect((new PotionEffect(PotionEffectType.SLOW_DIGGING, 60, 2)));
                    e.setDamage(e.getDamage() * 0.75);
                }
            }
        }
    }
}
