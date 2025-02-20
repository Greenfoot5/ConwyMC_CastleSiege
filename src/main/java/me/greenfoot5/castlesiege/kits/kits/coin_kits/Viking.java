package me.greenfoot5.castlesiege.kits.kits.coin_kits;

import me.greenfoot5.castlesiege.kits.items.CSItemCreator;
import me.greenfoot5.castlesiege.kits.items.EquipmentSet;
import me.greenfoot5.castlesiege.kits.kits.CoinKit;
import me.greenfoot5.conwymc.data_types.Tuple;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The viking kit
 */
public class Viking extends CoinKit implements Listener {

    private static final int health = 360;
    private static final double regen = 10.5;
    private static final double meleeDamage = 5;
    private static final int ladderAmount = 4;

    private static final double CAP = 1000;
    private static final double MIN_HEAL = 100;
    private static final double PERCENTAGE_DAMAGE = 0.15;
    private static final double PERCENTAGE_HEAL = 0.35;

    /**
     * Set the equipment and attributes of this kit
     */
    public Viking() {
        super("Viking", health, regen, Material.IRON_CHESTPLATE);

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();

        // Weapon
        es.hotbar[0] = CSItemCreator.weapon(new ItemStack(Material.IRON_AXE),
                Component.text("Giant Battle Axe", NamedTextColor.GREEN),
                List.of(Component.empty(),
                        Component.text(meleeDamage + " Melee Damage", NamedTextColor.DARK_GREEN)),
                null, meleeDamage);
        // Voted Weapon
        es.votedWeapon = new Tuple<>(
                CSItemCreator.weapon(new ItemStack(Material.IRON_AXE),
                        Component.text("Giant Battle Axe", NamedTextColor.GREEN),
                        List.of(Component.empty(),
                                Component.text((meleeDamage + 2) + " Melee Damage", NamedTextColor.DARK_GREEN),
								Component.text("⁎ Voted: +2 Melee Damage", NamedTextColor.DARK_AQUA)),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOTING, 0)), meleeDamage + 2),
                0);

        // Chestplate
        es.chest = CSItemCreator.item(new ItemStack(Material.IRON_CHESTPLATE),
                Component.text("Iron Chestplate", NamedTextColor.GREEN),
                List.of(Component.empty(),
                        Component.text(health + " HP", NamedTextColor.DARK_GREEN),
                        Component.text(regen + " Regen", NamedTextColor.DARK_GREEN)), null);

        // Leggings
        es.legs = CSItemCreator.item(new ItemStack(Material.CHAINMAIL_LEGGINGS),
                Component.text("Chainmail Leggings", NamedTextColor.GREEN),
                List.of(Component.empty(),
                        Component.text(health + " HP", NamedTextColor.DARK_GREEN),
                        Component.text(regen + " Regen", NamedTextColor.DARK_GREEN)), null);

        // Boots
        es.feet = CSItemCreator.item(new ItemStack(Material.CHAINMAIL_BOOTS),
                Component.text("Chainmail Boots", NamedTextColor.GREEN),
                List.of(Component.empty(),
                        Component.text(health + " HP", NamedTextColor.DARK_GREEN),
                        Component.text(regen + " Regen", NamedTextColor.DARK_GREEN)), null);
        // Voted Boots
        es.votedFeet = CSItemCreator.item(new ItemStack(Material.CHAINMAIL_BOOTS),
                Component.text("Chainmail Boots", NamedTextColor.GREEN),
                List.of(Component.empty(),
                        Component.text(health + " HP", NamedTextColor.DARK_GREEN),
                        Component.text(regen + " Regen", NamedTextColor.DARK_GREEN),
                        Component.empty(),
                        Component.text("⁎ Voted: Depth Strider II", NamedTextColor.DARK_AQUA)),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)));

        // Ladders
        es.hotbar[1] = new ItemStack(Material.LADDER, ladderAmount);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, ladderAmount + 2), 1);

        super.equipment = es;

        // Perm Potion Effects
        super.potionEffects.add(new PotionEffect(PotionEffectType.SLOWNESS, 999999, 0));
        super.potionEffects.add(new PotionEffect(PotionEffectType.MINING_FATIGUE, 999999, 0));
    }

    /**
     * Activate the viking ability of ignoring armor
     * @param e The event called when hitting another player
     */
    @EventHandler(ignoreCancelled = true)
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() != equippedPlayer)
            return;

        if (e.getEntity() instanceof Player hit) {
            if (equippedPlayer.getInventory().getItemInMainHand().getType() == Material.IRON_AXE) {
                AttributeInstance maxHealth = hit.getAttribute(Attribute.MAX_HEALTH);
                assert maxHealth != null;
                e.setDamage((maxHealth.getValue() * PERCENTAGE_DAMAGE) + e.getDamage());
                equippedPlayer.heal(maxHealth.getValue() * PERCENTAGE_HEAL, EntityRegainHealthEvent.RegainReason.CUSTOM);
            }
        } else if (e.getEntity() instanceof LivingEntity hit){
            if (equippedPlayer.getInventory().getItemInMainHand().getType() == Material.IRON_AXE) {

                AttributeInstance maxHealth = hit.getAttribute(Attribute.MAX_HEALTH);
                assert maxHealth != null;
                // Max percentage is at 1000 health
                // Min for healing is 100 * PERCENTAGE_HEAL health
                if (maxHealth.getValue() >= MIN_HEAL && maxHealth.getValue() <= CAP) {
                    e.setDamage((maxHealth.getValue() * PERCENTAGE_DAMAGE) + e.getDamage());
                    equippedPlayer.heal(e.getDamage() * PERCENTAGE_HEAL, EntityRegainHealthEvent.RegainReason.CUSTOM);
                } else if (maxHealth.getValue() > CAP) {
                    e.setDamage((CAP * PERCENTAGE_DAMAGE) + e.getDamage());
                    equippedPlayer.heal(e.getDamage() * PERCENTAGE_HEAL, EntityRegainHealthEvent.RegainReason.CUSTOM);
                } else {
                    e.setDamage((maxHealth.getValue() * PERCENTAGE_DAMAGE) + e.getDamage());
                }
            }
        }
    }

    @Override
    public ArrayList<Component> getGuiDescription() {
        ArrayList<Component> kitLore = new ArrayList<>();
        kitLore.add(Component.text("A mighty norse warrior with an axe so strong", NamedTextColor.GRAY));
        kitLore.add(Component.text("it ignores any armour that stands in the way", NamedTextColor.GRAY));
        kitLore.addAll(getBaseStats(health, regen, meleeDamage, ladderAmount));
        kitLore.add(Component.empty());
        kitLore.add(Component.text("Effects:", NamedTextColor.DARK_PURPLE));
        kitLore.add(Component.text("- Slow I", NamedTextColor.GRAY));
        kitLore.add(Component.text("- Mining Fatigue I", NamedTextColor.GRAY));
        kitLore.add(Component.empty());
        kitLore.add(Component.text("Passive:", NamedTextColor.DARK_GREEN));
        kitLore.add(Component.text("- Deals " + meleeDamage + " DMG each hit, plus " + (PERCENTAGE_DAMAGE * 100) + "% of target's health", NamedTextColor.GRAY));
        kitLore.add(Component.text("- Heal" + (PERCENTAGE_HEAL * 100) + "% of damage dealt", NamedTextColor.GRAY));
        return kitLore;
    }
}
