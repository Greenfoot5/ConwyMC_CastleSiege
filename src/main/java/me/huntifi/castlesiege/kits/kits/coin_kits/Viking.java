package me.huntifi.castlesiege.kits.kits.coin_kits;

import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.kits.kits.CoinKit;
import me.huntifi.castlesiege.kits.kits.Kit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
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

/**
 * The viking kit
 */
public class Viking extends CoinKit implements Listener {

    private static final int health = 330;
    private static final double regen = 10.5;
    private static final double meleeDamage = 16;
    private static final int ladderAmount = 4;

    private static final double PERCENTAGE_DAMAGE = 0.15;

    /**
     * Set the equipment and attributes of this kit
     */
    public Viking() {
        super("Viking", 330, 10.5, Material.IRON_CHESTPLATE);

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Weapon
        es.hotbar[0] = ItemCreator.weapon(new ItemStack(Material.IRON_AXE),
                ChatColor.GREEN + "Giant Battle Axe", null, null, meleeDamage);
        // Voted Weapon
        es.votedWeapon = new Tuple<>(
                ItemCreator.weapon(new ItemStack(Material.IRON_AXE),
                        ChatColor.GREEN + "Giant Battle Axe",
                        Collections.singletonList(ChatColor.AQUA + "- voted: +2 damage"),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), meleeDamage + 2),
                0);

        // Chestplate
        es.chest = ItemCreator.item(new ItemStack(Material.IRON_CHESTPLATE),
                ChatColor.GREEN + "Iron Chestplate", null, null);

        // Leggings
        es.legs = ItemCreator.item(new ItemStack(Material.CHAINMAIL_LEGGINGS),
                ChatColor.GREEN + "Chainmail Leggings", null, null);

        // Boots
        es.feet = ItemCreator.item(new ItemStack(Material.CHAINMAIL_BOOTS),
                ChatColor.GREEN + "Chainmail Boots", null, null);
        // Voted Boots
        es.votedFeet = ItemCreator.item(new ItemStack(Material.CHAINMAIL_BOOTS),
                ChatColor.GREEN + "Chainmail Boots",
                Collections.singletonList(ChatColor.AQUA + "- voted: Depth Strider II"),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)));

        // Ladders
        es.hotbar[1] = new ItemStack(Material.LADDER, ladderAmount);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, ladderAmount + 2), 1);

        super.equipment = es;

        // Perm Potion Effects
        super.potionEffects.add(new PotionEffect(PotionEffectType.SLOW, 999999, 0));
        super.potionEffects.add(new PotionEffect(PotionEffectType.SLOW_DIGGING, 999999, 0));
    }

    /**
     * Activate the viking ability of ignoring armor
     * @param e The event called when hitting another player
     */
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.isCancelled()) {
            return;
        }

        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            if (Objects.equals(Kit.equippedKits.get(e.getDamager().getUniqueId()).name, name) &&
                    ((Player) e.getDamager()).getInventory().getItemInMainHand().getType() == Material.IRON_AXE) {
                Player p = (Player) e.getEntity();
                int baseHealth = Kit.equippedKits.get(p.getUniqueId()).baseHealth;
                e.setDamage((baseHealth * PERCENTAGE_DAMAGE) + e.getDamage());
            }
        } else if (e.getEntity() instanceof LivingEntity && (!(e.getEntity() instanceof Player)) && e.getDamager() instanceof Player){
            if (Objects.equals(Kit.equippedKits.get(e.getDamager().getUniqueId()).name, name) &&
                    ((Player) e.getDamager()).getInventory().getItemInMainHand().getType() == Material.IRON_AXE) {
                LivingEntity mob = (LivingEntity) e.getEntity();

                AttributeInstance maxHealth = mob.getAttribute(Attribute.GENERIC_MAX_HEALTH);
                assert maxHealth != null;
                if (!(maxHealth.getValue() >= 500)) {
                 e.setDamage((maxHealth.getValue() * PERCENTAGE_DAMAGE) + e.getDamage());
              }
            }
        }
    }

    @Override
    public ArrayList<String> getGuiDescription() {
        ArrayList<String> kitLore = new ArrayList<>();
        kitLore.add("§7A mighty norse warrior with an axe so strong");
        kitLore.add("§7it ignores any armour that stands in the way");
        kitLore.addAll(getBaseStats(health, regen, meleeDamage, ladderAmount));
        kitLore.add(" ");
        kitLore.add("§5Effects:");
        kitLore.add("§7- Slow I");
        kitLore.add("§7- Mining Fatigue I");
        kitLore.add(" ");
        kitLore.add("§2Passive:");
        kitLore.add("§7- Deals " + PERCENTAGE_DAMAGE + "% damage plus ");
        kitLore.add(meleeDamage + " DMG each attack");
        return kitLore;
    }
}
