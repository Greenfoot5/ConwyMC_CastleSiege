package me.huntifi.castlesiege.kits.kits.team_kits.moria;

import me.huntifi.castlesiege.kits.items.CSItemCreator;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.kits.kits.TeamKit;
import me.huntifi.conwymc.data_types.Tuple;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class MoriaGuardian extends TeamKit implements Listener {

    public MoriaGuardian() {
        super("Guardian", 620, 15, "Moria",
                "The Dwarves", 5000, Material.SHIELD,
                "moriaguardian");

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();

        // Weapon
        es.hotbar[0] = CSItemCreator.weapon(new ItemStack(Material.STONE_SWORD),
                Component.text("Sword", NamedTextColor.GREEN), null, null, 41);
        // Voted Weapon
        es.votedWeapon = new Tuple<>(
                CSItemCreator.weapon(new ItemStack(Material.STONE_SWORD),
                        Component.text("Sword", NamedTextColor.GREEN),
                        Collections.singletonList(Component.text("- voted: +2 damage", NamedTextColor.AQUA)),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), 43),
                0);

        // Shield
        es.offhand = CSItemCreator.item(new ItemStack(Material.SHIELD),
                Component.text("Shield", NamedTextColor.GREEN), null, null);

        // Chestplate
        es.chest = CSItemCreator.item(new ItemStack(Material.IRON_CHESTPLATE),
                Component.text("Iron Chestplate", NamedTextColor.GREEN), null, Collections.singletonList(new Tuple<>(Enchantment.PROTECTION_PROJECTILE, 2)));

        // Leggings
        es.legs = CSItemCreator.item(new ItemStack(Material.NETHERITE_LEGGINGS),
                Component.text("Reinforced Iron Leggings", NamedTextColor.GREEN), null,
                Collections.singletonList(new Tuple<>(Enchantment.PROTECTION_PROJECTILE, 2)));

        // Boots
        es.feet = CSItemCreator.item(new ItemStack(Material.IRON_BOOTS),
                Component.text("Iron Boots", NamedTextColor.GREEN), null, null);
        // Voted Boots
        es.votedFeet = CSItemCreator.item(new ItemStack(Material.IRON_BOOTS),
                Component.text("Iron Boots", NamedTextColor.GREEN),
                Collections.singletonList(Component.text("- voted: Depth Strider II", NamedTextColor.AQUA)),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)));

        // Ladders
        es.hotbar[1] = new ItemStack(Material.LADDER, 4);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, 6), 1);

        super.potionEffects.add(new PotionEffect(PotionEffectType.SLOW_DIGGING, 999999, 1));
        super.potionEffects.add(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 999999, 0));

        // Death Messages
        super.deathMessage[0] = "You were beaten to death by ";
        super.killMessage[0] = " beat ";
        super.killMessage[1] = " to death";

        super.equipment = es;
    }

    /**
     * Whilst a guardian is blocking they get speed when they are hit by an arrow.
     * If they are not blocking and hit by an arrow they get damage resistance.
     * @param e The event called when a guardian is hit by an arrow whilst blocking
     */
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onArrowHit(ProjectileHitEvent e) {
        if (e.getHitEntity() instanceof Player && e.getEntity().getShooter() instanceof Player) {
            Player hit = (Player) e.getHitEntity();
            if (Objects.equals(Kit.equippedKits.get(hit.getUniqueId()).name, name)) {
                hit.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 160, 0));
                hit.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 30, 1));
            }
        }
    }

    @Override
    public ArrayList<Component> getGuiDescription() {
        ArrayList<Component> description = new ArrayList<>();
        description.add(Component.text("//TODO - Add kit description", NamedTextColor.GRAY));
        return description;
    }
}
