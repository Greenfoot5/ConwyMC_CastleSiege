package me.huntifi.castlesiege.kits.kits.sign_kits.Moria;

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
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class MoriaOrc extends SignKit implements Listener {


    /**
     * Creates a new Moria Orc
     */
    public MoriaOrc() {
        super("Moria Orc", 230, 7, Material.BOW, 2500);

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();

        // Weapon
        es.hotbar[0] = CSItemCreator.weapon(new ItemStack(Material.STONE_SWORD),
                Component.text("Dagger", NamedTextColor.GREEN), null, null, 29);
        // Voted weapon
        es.votedWeapon = new Tuple<>(
                CSItemCreator.weapon(new ItemStack(Material.STONE_SWORD),
                        Component.text("Dagger", NamedTextColor.GREEN),
                        Collections.singletonList(Component.text("⁎ Voted: +2 damage", NamedTextColor.AQUA)),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), 31),
                0);

        // Chestplate
        es.chest = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE),
                Component.text("Orcish Chestplate", NamedTextColor.GREEN), null, null,
                Color.fromRGB(21, 51, 10));
        ItemMeta chest = es.chest.getItemMeta();
        ArmorMeta chestMeta = (ArmorMeta) chest;
        assert chest != null;
        ArmorTrim chestTrim = new ArmorTrim(TrimMaterial.NETHERITE, TrimPattern.COAST);
        ((ArmorMeta) chest).setTrim(chestTrim);
        es.chest.setItemMeta(chestMeta);

        // Leggings
        es.legs = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_LEGGINGS),
                Component.text("Orcish Legplates", NamedTextColor.GREEN), null, null,
                Color.fromRGB(68, 65, 12));
        ItemMeta legs = es.legs.getItemMeta();
        ArmorMeta legsMeta = (ArmorMeta) legs;
        assert legs != null;
        ArmorTrim legsTrim = new ArmorTrim(TrimMaterial.NETHERITE, TrimPattern.WILD);
        ((ArmorMeta) legs).setTrim(legsTrim);
        es.legs.setItemMeta(legsMeta);

        // Boots
        es.feet = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_BOOTS),
                Component.text("Orcish Boots", NamedTextColor.GREEN), null, null,
                Color.fromRGB(68, 65, 12));
        ItemMeta feet = es.feet.getItemMeta();
        ArmorMeta feetMeta = (ArmorMeta) feet;
        assert feet != null;
        ArmorTrim feetTrim = new ArmorTrim(TrimMaterial.NETHERITE, TrimPattern.DUNE);
        ((ArmorMeta) feet).setTrim(feetTrim);
        es.feet.setItemMeta(feetMeta);
        // Voted Boots
        es.votedFeet = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_BOOTS),
                Component.text("Orcish Boots", NamedTextColor.GREEN),
                Collections.singletonList(Component.text("⁎ Voted: Depth Strider II", NamedTextColor.AQUA)),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)),
                Color.fromRGB(68, 65, 12));
        ItemMeta votedFeet = es.votedFeet.getItemMeta();
        ArmorMeta votedFeetMeta = (ArmorMeta) votedFeet;
        assert votedFeet != null;
        ArmorTrim votedFeetTrim = new ArmorTrim(TrimMaterial.NETHERITE, TrimPattern.DUNE);
        ((ArmorMeta) votedFeet).setTrim(votedFeetTrim);
        es.votedFeet.setItemMeta(votedFeetMeta);

        // Regular Bow
        es.hotbar[1] = CSItemCreator.item(new ItemStack(Material.BOW),
                Component.text("Bow", NamedTextColor.GREEN), null, null);

        // Ladders
        es.hotbar[2] = new ItemStack(Material.LADDER, 4);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, 6), 2);

        // Arrows
        es.hotbar[4] = new ItemStack(Material.ARROW, 8);

        // Fire Arrows
        ItemStack poisonArrow = CSItemCreator.item(new ItemStack(Material.TIPPED_ARROW, 16),
                Component.text("Poison Arrow", NamedTextColor.GREEN), null, null);
        PotionMeta potionMeta = (PotionMeta) poisonArrow.getItemMeta();
        assert potionMeta != null;
        potionMeta.setColor(Color.GREEN);
        poisonArrow.setItemMeta(potionMeta);
        potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.POISON, 180, 0), true);
        poisonArrow.setItemMeta(potionMeta);

        es.hotbar[3] = poisonArrow;

        super.equipment = es;
    }

    /**
     * Set the arrow-damage of a Moria Orc's arrows
     * @param e The event called when a player is hit by an arrow
     */
    @EventHandler(priority = EventPriority.LOW)
    public void onArrowHit(ProjectileHitEvent e) {
        if (e.getEntity() instanceof Arrow &&
                e.getEntity().getShooter() instanceof Player &&
                Objects.equals(Kit.equippedKits.get(((Player) e.getEntity().getShooter()).getUniqueId()).name, name)) {
            ((Arrow) e.getEntity()).setDamage(27);
        }
    }

    @Override
    public ArrayList<Component> getGuiDescription() {
        ArrayList<Component> description = new ArrayList<>();
        description.add(Component.text("//TODO - Add kit description", NamedTextColor.GRAY));
        return description;
    }
}
