package me.huntifi.castlesiege.kits.kits.coin_kits;

import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.kits.kits.CoinKit;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.maps.NameTag;
import net.kyori.adventure.text.Component;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Random;

/**
 * The maceman kit
 */
public class Maceman extends CoinKit implements Listener {

    private static final int health = 300;
    private static final double regen = 10.5;
    private static final double meleeDamage = 37;
    private static final int ladderCount = 4;

    /**
     * Set the equipment and attributes of this kit
     */
    public Maceman() {
        super("Maceman", health, regen, Material.DIAMOND_SHOVEL);

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Weapon
        es.hotbar[0] = ItemCreator.weapon(new ItemStack(Material.DIAMOND_SHOVEL),
                ChatColor.GREEN + "Mace", null, null, meleeDamage);
        // Voted Weapon
        es.votedWeapon = new Tuple<>(
                ItemCreator.weapon(new ItemStack(Material.DIAMOND_SHOVEL),
                        ChatColor.GREEN + "Mace",
                        Collections.singletonList(ChatColor.AQUA + "- voted: +2 damage"),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), meleeDamage + 2),
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
        es.hotbar[1] = new ItemStack(Material.LADDER, ladderCount);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, ladderCount + 2), 1);

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
                    Messenger.sendActionInfo(NameTag.username(p) + "§r blocked your stun", q);
                    Messenger.sendActionInfo("Your shield broke whilst blocking " + NameTag.username(q) + "§r's stun", p);
                    if (p.getInventory().getItemInMainHand().getType().equals(Material.SHIELD)) {
                        p.getInventory().getItemInMainHand().setAmount(0);
                    } else if (p.getInventory().getItemInOffHand().getType().equals(Material.SHIELD)) {
                        p.getInventory().getItemInOffHand().setAmount(0);
                    }
                    p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK , 1, 1 );
                } else if (p.isSneaking() && new Random().nextInt(4) == 0) {
                    Messenger.sendActionInfo(NameTag.username(p) + "§r dodged your stun", q);
                    Messenger.sendActionInfo("You dodged " + NameTag.username(q) + "§r's stun", p);
                } else {
                    Messenger.sendActionInfo("You have stunned " + NameTag.username(q), p);
                    Messenger.sendActionWarning("You have been stunned by " + NameTag.username(p) + "§r!", q);
                    p.getWorld().playSound(p.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST_FAR , 1, 1 );
                    p.addPotionEffect((new PotionEffect(PotionEffectType.BLINDNESS, 60, 1)));
                    p.addPotionEffect((new PotionEffect(PotionEffectType.SLOW, 60, 2)));
                    p.addPotionEffect((new PotionEffect(PotionEffectType.CONFUSION, 80, 4)));
                    p.addPotionEffect((new PotionEffect(PotionEffectType.SLOW_DIGGING, 60, 2)));
                    e.setDamage(e.getDamage() * 1.5);
                }
            }
        }
    }

    /**
     * @return The lore to add to the kit gui item
     */
    @Override
    public ArrayList<Component> getGuiDescription() {
        ArrayList<Component> kitLore = new ArrayList<>();
        kitLore.add(Component.text("A melee kit that can stun");
        kitLore.add(Component.text("opponents, making them weaker");
        kitLore.addAll(getBaseStats(health, regen, meleeDamage, ladderCount));
        kitLore.add(Component.text(" ");
        kitLore.add(Component.text("§6Active:");
        kitLore.add(Component.text("- Maceman can stun their target,");
        kitLore.add(Component.text("slowing, blinding and confusing the target for 3s.");
        kitLore.add(Component.text("Also deals bonus DMG");
        kitLore.add(Component.text(" ");
        kitLore.add(Component.text("§2Passive:");
        kitLore.add(Component.text("- Can break a shield when the opponent is");
        kitLore.add(Component.text("actively blocking when stunned");
        return kitLore;
    }
}
