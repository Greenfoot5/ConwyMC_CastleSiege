package me.greenfoot5.castlesiege.kits.kits.sign_kits;

import me.greenfoot5.castlesiege.kits.items.CSItemCreator;
import me.greenfoot5.castlesiege.kits.items.EquipmentSet;
import me.greenfoot5.castlesiege.kits.kits.Kit;
import me.greenfoot5.castlesiege.kits.kits.SignKit;
import me.greenfoot5.conwymc.data_types.Tuple;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class OrcPikeman extends SignKit implements Listener {

    // Damage multiplier when hitting horses
    private static final double HORSE_MULTIPLIER = 1.5;

    /**
     * Creates a new Moria Orc Pikeman
     */
    public OrcPikeman() {
        super("Orc Pikeman", 360, 8, Material.NETHERITE_HOE, 2000);


        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();

        // Weapon
        es.hotbar[0] = CSItemCreator.weapon(new ItemStack(Material.NETHERITE_HOE),
                Component.text("Orcish Pike", NamedTextColor.GREEN), null, null, 38);
        // Voted weapon
        es.votedWeapon = new Tuple<>(
                CSItemCreator.weapon(new ItemStack(Material.NETHERITE_HOE),
                        Component.text("Orcish Pike", NamedTextColor.GREEN),
                        Collections.singletonList(Component.text("⁎ Voted: +2 damage", NamedTextColor.AQUA)),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOTING, 0)), 40),
                0);

        // Chestplate
        es.chest = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE),
                Component.text("Orcish Chestplate", NamedTextColor.GREEN), null, null,
                Color.fromRGB(57, 58, 16));
        ItemMeta chest = es.chest.getItemMeta();
        ArmorMeta chestMeta = (ArmorMeta) chest;
        assert chest != null;
        ArmorTrim chestTrim = new ArmorTrim(TrimMaterial.NETHERITE, TrimPattern.RIB);
        ((ArmorMeta) chest).setTrim(chestTrim);
        es.chest.setItemMeta(chestMeta);

        // Leggings
        es.legs = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_LEGGINGS),
                Component.text("Orcish Legplates", NamedTextColor.GREEN), null, null,
                Color.fromRGB(127, 118, 40));
        ItemMeta legs = es.legs.getItemMeta();
        ArmorMeta legsMeta = (ArmorMeta) legs;
        assert legs != null;
        ArmorTrim legsTrim = new ArmorTrim(TrimMaterial.COPPER, TrimPattern.SPIRE);
        ((ArmorMeta) legs).setTrim(legsTrim);
        es.legs.setItemMeta(legsMeta);

        // Boots
        es.feet = CSItemCreator.item(new ItemStack(Material.NETHERITE_BOOTS),
                Component.text("Orcish Boots", NamedTextColor.GREEN),
                Collections.singletonList(Component.text("- Depth Strider I", NamedTextColor.AQUA)),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 1)));
        ItemMeta feet = es.feet.getItemMeta();
        ArmorMeta feetMeta = (ArmorMeta) feet;
        assert feet != null;
        ArmorTrim feetTrim = new ArmorTrim(TrimMaterial.NETHERITE, TrimPattern.WILD);
        ((ArmorMeta) feet).setTrim(feetTrim);
        es.feet.setItemMeta(feetMeta);
        // Voted Boots
        es.votedFeet = CSItemCreator.item(new ItemStack(Material.NETHERITE_BOOTS),
                Component.text("Orcish Boots", NamedTextColor.GREEN),
                Collections.singletonList(Component.text("⁎ Voted: Depth Strider III", NamedTextColor.AQUA)),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 3)));
        ItemMeta votedFeet = es.votedFeet.getItemMeta();
        ArmorMeta votedFeetMeta = (ArmorMeta) votedFeet;
        assert votedFeet != null;
        ArmorTrim votedFeetTrim = new ArmorTrim(TrimMaterial.NETHERITE, TrimPattern.WILD);
        ((ArmorMeta) votedFeet).setTrim(votedFeetTrim);
        es.votedFeet.setItemMeta(votedFeetMeta);

        // Ladders
        es.hotbar[1] = new ItemStack(Material.LADDER, 4);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, 6), 1);

        // Death Messages
        super.deathMessage[0] = "You were impaled by ";
        super.killMessage[0] = " impaled ";
        super.killMessage[1] = " without mercy";

        super.equipment = es;
    }

    /**
     * Activate the hammerer's stun ability
     * @param e The event called when hitting another player
     */
    @EventHandler(ignoreCancelled = true)
    public void onPiked(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player p && e.getDamager() instanceof Player q) {

            // Pikeman keeps the enemy distant
            if (Objects.equals(Kit.equippedKits.get(q.getUniqueId()).name, name) &&
                    q.getInventory().getItemInMainHand().getType() == Material.NETHERITE_HOE &&
                    q.getCooldown(Material.NETHERITE_HOE) == 0) {
                q.setCooldown(Material.NETHERITE_HOE, 80);

                // Enemy blocks piking?
                if (!p.isBlocking()) {
                    p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PHANTOM_FLAP , 1, 1.8f);
                    p.addPotionEffect((new PotionEffect(PotionEffectType.SLOWNESS, 30, 2)));
                    Vector unitVector = p.getLocation().toVector().subtract(q.getLocation().toVector()).normalize();
                    p.setVelocity(unitVector.multiply(2.2).setY(unitVector.getY() + 0.3));
                    e.setDamage(e.getDamage() * 1.5);
                }
            }
        }
    }

    /**
     * @param e When a player hits a horse, grants bonus damage
     */
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player && e.getEntity() instanceof Horse) {
            if (Objects.equals(Kit.equippedKits.get(e.getDamager().getUniqueId()).name, name)) {
                e.setDamage(e.getDamage() * HORSE_MULTIPLIER);
            }
        }
    }

    @Override
    public ArrayList<Component> getGuiDescription() {
        return null;
    }
}
