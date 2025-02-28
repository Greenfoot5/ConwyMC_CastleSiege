package me.greenfoot5.castlesiege.kits.kits.coin_kits;

import me.greenfoot5.castlesiege.Main;
import me.greenfoot5.castlesiege.kits.items.CSItemCreator;
import me.greenfoot5.castlesiege.kits.items.EquipmentSet;
import me.greenfoot5.castlesiege.kits.kits.CoinKit;
import me.greenfoot5.conwymc.data_types.Tuple;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The halberdier kit
 */
public class Halberdier extends CoinKit implements Listener {

    private static final int health = 630;
    private static final double regen = 23;
    private static final double meleeDamage = 90;
    private static final AttributeModifier scaleMod = new AttributeModifier(new NamespacedKey(Main.plugin, "halb_scale"), 0.3, AttributeModifier.Operation.ADD_SCALAR);
    private static final AttributeModifier speedMod = new AttributeModifier(new NamespacedKey(Main.plugin, "halb_speed"), -0.8, AttributeModifier.Operation.ADD_SCALAR);
    private static final AttributeModifier stepHeightMod = new AttributeModifier(new NamespacedKey(Main.plugin, "halb_step"), 1, AttributeModifier.Operation.ADD_SCALAR);
    private static final AttributeModifier jumpMod = new AttributeModifier(new NamespacedKey(Main.plugin, "halb_jump"), -1, AttributeModifier.Operation.ADD_SCALAR);
    private static final AttributeModifier gravityMod = new AttributeModifier(new NamespacedKey(Main.plugin, "halb_gravity"), 1, AttributeModifier.Operation.ADD_SCALAR);
    private static final AttributeModifier burnMod = new AttributeModifier(new NamespacedKey(Main.plugin, "halb_burn"), -1, AttributeModifier.Operation.ADD_SCALAR);
    private static final AttributeModifier explosionKnockMod = new AttributeModifier(new NamespacedKey(Main.plugin, "halb_explode_knock"), 0.5, AttributeModifier.Operation.ADD_NUMBER);
    private static final AttributeModifier moveEffMod = new AttributeModifier(new NamespacedKey(Main.plugin, "halb_move_eff"), 1, AttributeModifier.Operation.ADD_NUMBER);
    private static final AttributeModifier sneakMod = new AttributeModifier(new NamespacedKey(Main.plugin, "halb_sneak"), 1, AttributeModifier.Operation.ADD_SCALAR);
    private static final double arrowDamageMult = 1.5;

    /**
     * Set the equipment and attributes of this kit
     */
    public Halberdier() {
        super("Halberdier", 630, 23, Material.NETHERITE_CHESTPLATE);
        super.kbResistance = 1;

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();

        // Weapon
        es.hotbar[0] = CSItemCreator.weapon(new ItemStack(Material.NETHERITE_AXE),
                Component.text("Halberd", NamedTextColor.GREEN),
                List.of(Component.empty(),
                        Component.text("90 Melee Damage", NamedTextColor.DARK_GREEN)),
                null, meleeDamage);
        // Voted weapon
        es.votedWeapon = new Tuple<>(
                CSItemCreator.weapon(new ItemStack(Material.NETHERITE_AXE),
                        Component.text("Halberd", NamedTextColor.GREEN),
                        List.of(Component.empty(),
                                Component.text("92 Melee Damage", NamedTextColor.DARK_GREEN),
								Component.text("⁎ Voted: +2 Melee Damage", NamedTextColor.DARK_AQUA)),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOTING, 1)), meleeDamage + 2),
                0);

        // Chestplate
        es.chest = CSItemCreator.item(new ItemStack(Material.NETHERITE_CHESTPLATE),
                Component.text("Indestructible Chestplate", NamedTextColor.GREEN),
                List.of(Component.empty(),
                        Component.text(health + " HP", NamedTextColor.DARK_GREEN),
                        Component.text(regen + " Regen", NamedTextColor.DARK_GREEN)),
                Collections.singletonList(new Tuple<>(Enchantment.PROTECTION, 2)));
        ItemMeta chestMeta = es.chest.getItemMeta();
        chestMeta.addAttributeModifier(Attribute.SCALE, scaleMod);
        chestMeta.addAttributeModifier(Attribute.ENTITY_INTERACTION_RANGE, scaleMod);
        chestMeta.addAttributeModifier(Attribute.BLOCK_INTERACTION_RANGE, scaleMod);
        chestMeta.addAttributeModifier(Attribute.MOVEMENT_SPEED, speedMod);
        chestMeta.addAttributeModifier(Attribute.STEP_HEIGHT, stepHeightMod);
        chestMeta.addAttributeModifier(Attribute.JUMP_STRENGTH, jumpMod);
        chestMeta.addAttributeModifier(Attribute.GRAVITY, gravityMod);
        chestMeta.addAttributeModifier(Attribute.BURNING_TIME, burnMod);
        chestMeta.addAttributeModifier(Attribute.EXPLOSION_KNOCKBACK_RESISTANCE, explosionKnockMod);
        chestMeta.addAttributeModifier(Attribute.MOVEMENT_EFFICIENCY, moveEffMod);
        chestMeta.addAttributeModifier(Attribute.SNEAKING_SPEED, sneakMod);
        es.chest.setItemMeta(chestMeta);

        // Leggings
        es.legs = CSItemCreator.item(new ItemStack(Material.CHAINMAIL_LEGGINGS),
                Component.text("Chainmail Leggings", NamedTextColor.GREEN),
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

        super.equipment = es;

        // Perm Potion Effects
        super.potionEffects.add(new PotionEffect(PotionEffectType.MINING_FATIGUE, 999999, 1));
        super.potionEffects.add(new PotionEffect(PotionEffectType.RESISTANCE, 999999, 0));

        // Death Messages
        super.deathMessage[0] = "You were sliced in half by ";
        super.killMessage[0] = " sliced ";
        super.killMessage[1] = " in half";
    }

    /**
     * Activate the halberdier ability of dealing +50% damage to cavalry
     * @param e The event called when dealing damage to another player
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void antiCav(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player cav && e.getDamager() instanceof Player halb) {

            if (halb == equippedPlayer
                    && cav.isInsideVehicle()
                    && cav.getVehicle() instanceof Horse) {
                e.setDamage(e.getDamage() * 1.5);
            }
        }
    }

    /**
     * The cooldown event for halberdier.
     * @param e The event called when dealing damage to another player
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void halbCooldown(EntityDamageByEntityEvent e) {
        if (e.getDamager() == equippedPlayer) {

            if (equippedPlayer.getInventory().getItemInMainHand().getType() == Material.DIAMOND_AXE) {
                if (equippedPlayer.getCooldown(Material.DIAMOND_AXE) == 0) {
                    equippedPlayer.setCooldown(Material.DIAMOND_AXE, e.getEntity() instanceof Player ? 20 : 10);
                } else {
                    e.setCancelled(true);
                }
            }
        }
    }

    /**
     * Activate the halberdier ability of taking +100% damage from arrows
     * @param e The event called when taking damage from an arrow
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void weakToArrows(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Arrow && e.getEntity() == equippedPlayer) {
            e.setDamage(e.getDamage() * arrowDamageMult);
        }
    }


    /**
     * @return The lore to add to the kit gui item
     */
    @Override
    public ArrayList<Component> getGuiDescription() {
        ArrayList<Component> kitLore = new ArrayList<>();
        kitLore.add(Component.text("Our classic tank capable of", NamedTextColor.GRAY));
        kitLore.add(Component.text("taking a large beating", NamedTextColor.GRAY));
        kitLore.addAll(getBaseStats(health, regen, meleeDamage, 0));
        kitLore.add(Component.text(" ", NamedTextColor.GRAY));
        kitLore.add(Component.text("Effects:", NamedTextColor.DARK_PURPLE));
        kitLore.add(Component.text("- Slowness III", NamedTextColor.GRAY));
        kitLore.add(Component.text("- Mining Fatigue II", NamedTextColor.GRAY));
        kitLore.add(Component.text("- Resistance I", NamedTextColor.GRAY));
        kitLore.add(Component.text(" ", NamedTextColor.GRAY));
        kitLore.add(Component.text("Passive:", NamedTextColor.DARK_GREEN));
        kitLore.add(Component.text("- Cannot jump", NamedTextColor.RED));
        kitLore.add(Component.text("- Has a slower attack speed", NamedTextColor.RED));
        kitLore.add(Component.text("- Halberdier takes 100% more DMG from arrows", NamedTextColor.RED));
        return kitLore;
    }
}
