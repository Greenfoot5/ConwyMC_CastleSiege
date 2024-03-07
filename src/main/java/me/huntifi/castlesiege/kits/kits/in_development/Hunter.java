package me.huntifi.castlesiege.kits.kits.in_development;

import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.kits.kits.CoinKit;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
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
    private int cooldownTicks = 50;

    public Hunter() {
        super("hunter", health, regen, Material.LEATHER);

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Weapon
        es.hotbar[0] = ItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                ChatColor.DARK_PURPLE + "Hunter's Knife", null, null, meleeDamage);
        // Voted Weapon
        es.votedWeapon = new Tuple<>(
                ItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                        ChatColor.DARK_PURPLE + "Hunter's Knife",
                        null,
                        Collections.singletonList(new Tuple<>(Enchantment.KNOCKBACK, 0)), meleeDamage + 2),
                0);

        // Regular Bow
        es.hotbar[1] = ItemCreator.item(new ItemStack(Material.BOW),
                Component.text("Bow", NamedTextColor.GREEN), null, null);

        // Chestplate
        es.chest = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE),
                Component.text("Hunter's Chestpiece", NamedTextColor.GREEN), null, null,
                Color.fromRGB(54, 154, 42));
        ItemMeta chest = es.chest.getItemMeta();
        ArmorMeta chestMeta = (ArmorMeta) chest;
        assert chest != null;
        ArmorTrim chestTrim = new ArmorTrim(TrimMaterial.COPPER, TrimPattern.WAYFINDER);
        ((ArmorMeta) chest).setTrim(chestTrim);
        es.chest.setItemMeta(chestMeta);

        // Leggings
        es.legs = ItemCreator.item(new ItemStack(Material.LEATHER_LEGGINGS),
                Component.text("Hunter's Slacks", NamedTextColor.GREEN), null, null);


        // Boots
        es.feet = ItemCreator.item(new ItemStack(Material.NETHERITE_BOOTS),
                Component.text("Hunter's Boots", NamedTextColor.GREEN), null, null);
        // Voted Boots
        es.votedFeet = ItemCreator.item(new ItemStack(Material.NETHERITE_BOOTS),
                Component.text("Hunter's Boots", NamedTextColor.GREEN),
                Collections.singletonList(Component.text("- voted: Depth Strider II", NamedTextColor.AQUA)),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)));

        // Ladders
        es.hotbar[2] = new ItemStack(Material.LADDER, ladderCount);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, ladderCount + 2), 1);

        // Traps
        es.hotbar[3] = new ItemStack(Material.STONE_PRESSURE_PLATE, trapCount);

        // control hound
        es.hotbar[4] = ItemCreator.item(new ItemStack(Material.BONE),
                ChatColor.GOLD + "Control Companion", Arrays.asList("",
                        ChatColor.DARK_GRAY + "Left click: ",
                        ChatColor.YELLOW + "Attacks the opponent you are pointing at.",
                        ChatColor.DARK_GRAY + "Right click: ",
                        ChatColor.YELLOW + "Summon or un-summon companion.",
                        ChatColor.YELLOW + "Has no cooldown."), null);

        super.equipment = es;

        // Perm Potion Effect
        super.potionEffects.add(new PotionEffect(PotionEffectType.JUMP, 999999, 0));
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
        description.add(Component.text(" "));
        description.add(Component.text("Effects:", NamedTextColor.DARK_PURPLE));
        description.add(Component.text("- Speed I", NamedTextColor.GRAY));
        description.add(Component.text("- Jump Boost I", NamedTextColor.GRAY));
        description.add(Component.text(" "));
        description.add(Component.text("Active:", NamedTextColor.GOLD));
        description.add(Component.text("- Can place down poison, mark or bleeding traps.", NamedTextColor.GRAY));
        description.add(Component.text(" "));
        description.add(Component.text("Passive:", NamedTextColor.DARK_GREEN));
        description.add(Component.text("- Can summon a companion hound, which", NamedTextColor.GRAY));
        description.add(Component.text("fights for the hunter.", NamedTextColor.GRAY));
        return description;
    }
}
