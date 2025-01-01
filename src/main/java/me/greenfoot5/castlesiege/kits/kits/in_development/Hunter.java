package me.greenfoot5.castlesiege.kits.kits.in_development;

import me.greenfoot5.castlesiege.kits.items.CSItemCreator;
import me.greenfoot5.castlesiege.kits.items.EquipmentSet;
import me.greenfoot5.castlesiege.kits.kits.CoinKit;
import me.greenfoot5.conwymc.data_types.Tuple;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Hunter extends CoinKit implements Listener {

    private static final int health = 230;
    private static final double regen = 10.5;
    private static final double meleeDamage = 30;
    private static final int ladderCount = 4;
    private static final int trapCount = 4;
    private static final int cooldownTicks = 50;

    /**
     * Creates a new Hunter
     */
    public Hunter() {
        super("hunter", health, regen, Material.LEATHER);

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();

        // Weapon
        es.hotbar[0] = CSItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                Component.text("Hunter's Knife", NamedTextColor.DARK_PURPLE), null, null, meleeDamage);
        // Voted Weapon
        es.votedWeapon = new Tuple<>(
                CSItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                        Component.text("Hunter's Knife", NamedTextColor.DARK_PURPLE), null,
                        Collections.singletonList(new Tuple<>(Enchantment.KNOCKBACK, 0)), meleeDamage + 2), 0);

        // Regular Bow
        es.hotbar[1] = CSItemCreator.item(new ItemStack(Material.BOW),
                Component.text("Bow", NamedTextColor.GREEN), null, null);

        // Chestplate
        es.chest = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE),
                Component.text("Hunter's Chest Piece", NamedTextColor.GREEN), null, null,
                Color.fromRGB(54, 154, 42));
        ItemMeta chest = es.chest.getItemMeta();
        ArmorMeta chestMeta = (ArmorMeta) chest;
        assert chest != null;
        ArmorTrim chestTrim = new ArmorTrim(TrimMaterial.COPPER, TrimPattern.WAYFINDER);
        ((ArmorMeta) chest).setTrim(chestTrim);
        es.chest.setItemMeta(chestMeta);

        // Leggings
        es.legs = CSItemCreator.item(new ItemStack(Material.LEATHER_LEGGINGS),
                Component.text("Hunter's Slacks", NamedTextColor.GREEN), null, null);


        // Boots
        es.feet = CSItemCreator.item(new ItemStack(Material.NETHERITE_BOOTS),
                Component.text("Hunter's Boots", NamedTextColor.GREEN), null, null);
        // Voted Boots
        es.votedFeet = CSItemCreator.item(new ItemStack(Material.NETHERITE_BOOTS),
                Component.text("Hunter's Boots", NamedTextColor.GREEN),
                Collections.singletonList(Component.text("⁎ Voted: Depth Strider II", NamedTextColor.AQUA)),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)));

        // Ladders
        es.hotbar[2] = new ItemStack(Material.LADDER, ladderCount);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, ladderCount + 2), 1);

        // Traps
        es.hotbar[3] = new ItemStack(Material.STONE_PRESSURE_PLATE, trapCount);

        // control hound
        es.hotbar[4] = CSItemCreator.item(new ItemStack(Material.BONE),
                Component.text("Control Companion", NamedTextColor.GOLD),
                Arrays.asList(Component.empty(),
                        Component.text("Left click: ", NamedTextColor.DARK_GRAY),
                        Component.text("Attacks the opponent you are pointing at.", NamedTextColor.YELLOW),
                        Component.text("Right click: ", NamedTextColor.DARK_GRAY),
                        Component.text("Summon or un-summon companion.", NamedTextColor.YELLOW),
                        Component.text("Has no cooldown.", NamedTextColor.YELLOW)), null);

        super.equipment = es;

        // Perm Potion Effect
        super.potionEffects.add(new PotionEffect(PotionEffectType.JUMP_BOOST, 999999, 0));
        // Perm Potion Effect
        super.potionEffects.add(new PotionEffect(PotionEffectType.SPEED, 999999, 0));

        // Death Messages
        super.deathMessage[0] = "You were hunted down and killed by ";
        super.killMessage[0] = " hunted ";
        super.killMessage[1] = " down and killed them";
    }

    @Override
    public ArrayList<Component> getGuiDescription() {
        ArrayList<Component> description = new ArrayList<>();
        description.add(Component.text("Has a companion and traps on their side!", NamedTextColor.GRAY));
        description.addAll(getBaseStats(health, regen, meleeDamage, ladderCount));
        description.add(Component.empty());
        description.add(Component.text("Effects:", NamedTextColor.DARK_PURPLE));
        description.add(Component.text("- Speed I", NamedTextColor.GRAY));
        description.add(Component.text("- Jump Boost I", NamedTextColor.GRAY));
        description.add(Component.empty());
        description.add(Component.text("Active:", NamedTextColor.GOLD));
        description.add(Component.text("- Can place down poison, mark or bleeding traps.", NamedTextColor.GRAY));
        description.add(Component.empty());
        description.add(Component.text("Passive:", NamedTextColor.DARK_GREEN));
        description.add(Component.text("- Can summon a companion hound, which", NamedTextColor.GRAY));
        description.add(Component.text("fights for the hunter.", NamedTextColor.GRAY));
        return description;
    }
}
