package me.greenfoot5.castlesiege.kits.kits.coin_kits;

import me.greenfoot5.castlesiege.kits.items.CSItemCreator;
import me.greenfoot5.castlesiege.kits.items.EquipmentSet;
import me.greenfoot5.castlesiege.kits.kits.CoinKit;
import me.greenfoot5.castlesiege.kits.kits.Kit;
import me.greenfoot5.castlesiege.misc.CSNameTag;
import me.greenfoot5.conwymc.data_types.Tuple;
import me.greenfoot5.conwymc.util.Messenger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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
import java.util.List;
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

        // Weapon
        es.hotbar[0] = CSItemCreator.weapon(new ItemStack(Material.DIAMOND_SHOVEL),
                Component.text("Mace", NamedTextColor.GREEN),
                List.of(Component.empty(),
                        Component.text("37 Melee Damage", NamedTextColor.DARK_GREEN)),
                null, meleeDamage);
        // Voted Weapon
        es.votedWeapon = new Tuple<>(
                CSItemCreator.weapon(new ItemStack(Material.DIAMOND_SHOVEL),
                        Component.text("Mace", NamedTextColor.GREEN),
                        List.of(Component.empty(),
                                Component.text("39 Melee Damage", NamedTextColor.DARK_GREEN),
								Component.text("⁎ Voted: +2 Melee Damage", NamedTextColor.DARK_AQUA)),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOTING, 0)), meleeDamage + 2),
                0);

        // Chestplate
        es.chest = CSItemCreator.item(new ItemStack(Material.CHAINMAIL_CHESTPLATE),
                Component.text("Chainmail Chestplate", NamedTextColor.GREEN),
                List.of(Component.empty(),
                        Component.text(health + " HP", NamedTextColor.DARK_GREEN),
                        Component.text(regen + " Regen", NamedTextColor.DARK_GREEN)), null);

        // Leggings
        es.legs = CSItemCreator.item(new ItemStack(Material.LEATHER_LEGGINGS),
                Component.text("Leather Leggings", NamedTextColor.GREEN),
                List.of(Component.empty(),
                        Component.text(health + " HP", NamedTextColor.DARK_GREEN),
                        Component.text(regen + " Regen", NamedTextColor.DARK_GREEN)), null);

        // Boots
        es.feet = CSItemCreator.item(new ItemStack(Material.IRON_BOOTS),
                Component.text("Iron Boots", NamedTextColor.GREEN),
                List.of(Component.empty(),
                        Component.text(health + " HP", NamedTextColor.DARK_GREEN),
                        Component.text(regen + " Regen", NamedTextColor.DARK_GREEN)), null);
        // Voted Boots
        es.votedFeet = CSItemCreator.item(new ItemStack(Material.IRON_BOOTS),
                Component.text("Iron Boots", NamedTextColor.GREEN),
                List.of(Component.empty(),
                        Component.text(health + " HP", NamedTextColor.DARK_GREEN),
                        Component.text(regen + " Regen", NamedTextColor.DARK_GREEN),
                        Component.empty(),
                        Component.text("⁎ Voted: Depth Strider II", NamedTextColor.DARK_AQUA)),
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
    @EventHandler(ignoreCancelled = true)
    public void onStun(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player maceman && e.getDamager() instanceof Player stunned) {

            // Maceman tries to use stun an enemy
            if (Objects.equals(Kit.equippedKits.get(stunned.getUniqueId()).name, name) &&
                    stunned.getInventory().getItemInMainHand().getType() == Material.DIAMOND_SHOVEL &&
                    stunned.getCooldown(Material.DIAMOND_SHOVEL) == 0) {
                stunned.setCooldown(Material.DIAMOND_SHOVEL, 200);

                // Enemy blocks stun
                if (maceman.isBlocking()) {
                    Messenger.sendWarning(CSNameTag.mmUsername(stunned) + " blocked your stun", maceman);
                    Messenger.sendSuccess("Your shield broke whilst blocking " + CSNameTag.mmUsername(maceman) + "'s stun", stunned);
                    if (maceman.getInventory().getItemInMainHand().getType().equals(Material.SHIELD)) {
                        maceman.getInventory().getItemInMainHand().setAmount(0);
                    } else if (maceman.getInventory().getItemInOffHand().getType().equals(Material.SHIELD)) {
                        maceman.getInventory().getItemInOffHand().setAmount(0);
                    }
                    maceman.getWorld().playSound(maceman.getLocation(), Sound.ENTITY_ITEM_BREAK , 1, 1 );
                } else if (maceman.isSneaking() && new Random().nextInt(4) == 0) {
                    Messenger.sendWarning(CSNameTag.mmUsername(stunned) + "dodged your stun", maceman);
                    Messenger.sendSuccess("You dodged " + CSNameTag.mmUsername(maceman) + "'s stun", stunned);
                } else {
                    Messenger.sendSuccess("You have stunned " + CSNameTag.mmUsername(stunned), maceman);
                    Messenger.sendWarning("You have been stunned by " + CSNameTag.mmUsername(maceman) + "!", stunned);
                    maceman.getWorld().playSound(maceman.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST_FAR , 1, 1 );
                    maceman.addPotionEffect((new PotionEffect(PotionEffectType.BLINDNESS, 60, 1)));
                    maceman.addPotionEffect((new PotionEffect(PotionEffectType.SLOWNESS, 60, 2)));
                    maceman.addPotionEffect((new PotionEffect(PotionEffectType.NAUSEA, 80, 4)));
                    maceman.addPotionEffect((new PotionEffect(PotionEffectType.MINING_FATIGUE, 60, 2)));
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
        kitLore.add(Component.text("A melee kit that can stun", NamedTextColor.GRAY));
        kitLore.add(Component.text("opponents, making them weaker", NamedTextColor.GRAY));
        kitLore.addAll(getBaseStats(health, regen, meleeDamage, ladderCount));
        kitLore.add(Component.empty());
        kitLore.add(Component.text("Active:", NamedTextColor.GOLD));
        kitLore.add(Component.text("- Maceman can stun their target,", NamedTextColor.GRAY));
        kitLore.add(Component.text("slowing, blinding and confusing the target for 3s.", NamedTextColor.GRAY));
        kitLore.add(Component.text("Also deals bonus DMG", NamedTextColor.GRAY));
        kitLore.add(Component.empty());
        kitLore.add(Component.text("Passive:", NamedTextColor.DARK_GREEN));
        kitLore.add(Component.text("- Can break a shield when the opponent is", NamedTextColor.GRAY));
        kitLore.add(Component.text("actively blocking when stunned", NamedTextColor.GRAY));
        return kitLore;
    }
}
