package me.huntifi.castlesiege.kits.kits.sign_kits;


import me.huntifi.castlesiege.kits.items.CSItemCreator;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.kits.kits.SignKit;
import me.huntifi.conwymc.data_types.Tuple;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

/**
 * A slight difference from the classic crossbow kit
 */
public class Arbalester extends SignKit implements Listener {

    /**
     * Creates a new Conwy Arbalester
     */
    public Arbalester() {
        super("Arbalester", 260, 3, Material.CROSSBOW, 5000);


        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();

        // Crossbow
        es.hotbar[0] = CSItemCreator.weapon(new ItemStack(Material.CROSSBOW),
                Component.text("Crossbow", NamedTextColor.GREEN), null, null
                , 3);
        es.votedWeapon = new Tuple<>(CSItemCreator.weapon(new ItemStack(Material.CROSSBOW),
                Component.text("Crossbow", NamedTextColor.GREEN),
                Collections.singletonList(Component.text("- voted: +2 damage", NamedTextColor.AQUA)),
                Collections.singletonList(new Tuple<>(Enchantment.ARROW_KNOCKBACK, 2)), 5), 0);

        // Chestplate
        es.chest = CSItemCreator.item(new ItemStack(Material.IRON_CHESTPLATE),
                Component.text("Iron Chestplate", NamedTextColor.GREEN), null, null);

        // Leggings
        es.legs = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_LEGGINGS),
                Component.text("Leather Leggings", NamedTextColor.GREEN), null, null,
                Color.fromRGB(174, 26, 26));

        // Boots
        es.feet = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_BOOTS),
                Component.text("Leather Boots", NamedTextColor.GREEN), null, null,
                Color.fromRGB(174, 23, 51));
        // Voted boots
        es.votedFeet = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_BOOTS),
                Component.text("Leather Boots", NamedTextColor.GREEN),
                Collections.singletonList(Component.text("- voted: Depth Strider II", NamedTextColor.AQUA)),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)),
                Color.fromRGB(174, 26, 26));

        // Ladders
        es.hotbar[1] = new ItemStack(Material.LADDER, 4);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, 6), 1);

        // Arrows
        es.hotbar[7] = new ItemStack(Material.ARROW, 24);

        super.equipment = es;

        // Perm Potion Effect
        super.potionEffects.add(new PotionEffect(PotionEffectType.SLOW, 999999, 1));
        super.potionEffects.add(new PotionEffect(PotionEffectType.SLOW_DIGGING, 999999, 3));

        // Death Messages
        super.projectileDeathMessage[0] = "Your skull was pierced by ";
        super.projectileDeathMessage[1] = "'s bolt";
        super.projectileKillMessage[0] = " pierced ";
        super.projectileKillMessage[1] = "'s skull";
    }


    /**
     * Activate the crossbowman ability, shooting an arrow in the direction the player is looking at high speed
     * @param e The even called when shooting an arrow from a crossbow
     */
    @EventHandler
    public void shootCrossbow(EntityShootBowEvent e) {
        if (e.isCancelled() || !(e.getEntity() instanceof Player)) {
            return;
        }

        Player p = (Player) e.getEntity();
        if (Objects.equals(Kit.equippedKits.get(p.getUniqueId()).name, name)) {

            if (!(e.getProjectile() instanceof Arrow)) {
                return;
            }

            p.setCooldown(Material.CROSSBOW, 150);
            Arrow a = (Arrow) e.getProjectile();
            ((Arrow) e.getProjectile()).setPierceLevel(1);
            a.setKnockbackStrength(2);
            a.setVelocity(p.getLocation().getDirection().normalize().multiply(47));
        }
    }

    @Override
    public ArrayList<Component> getGuiDescription() {
        ArrayList<Component> description = new ArrayList<>();
        description.add(Component.text("//TODO - Add kit description", NamedTextColor.GRAY));
        return description;
    }
}
