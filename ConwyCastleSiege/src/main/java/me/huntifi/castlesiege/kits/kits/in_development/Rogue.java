package me.huntifi.castlesiege.kits.kits.in_development;

import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.kits.kits.DonatorKit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.Collections;

public class Rogue extends DonatorKit implements Listener {

    private final ItemStack gouge;

    private final ItemStack poison;

    private final ItemStack shadowstep;

    private final ItemStack shadowleap;

    private final ItemStack trackArrow;

    private final ItemStack comboPoint;

    public Rogue() {
        super("Rogue", 240, 15, 10000, 1);
        super.canSeeHealth = true;

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Weapon
        es.hotbar[0] = ItemCreator.weapon(new ItemStack(Material.NETHERITE_SWORD),
                ChatColor.DARK_GRAY + "Dagger", null, null, 42);
        // Voted weapon
        es.votedWeapon = new Tuple<>(
                ItemCreator.weapon(new ItemStack(Material.NETHERITE_SWORD),
                        ChatColor.DARK_GRAY + "Dagger",
                        Collections.singletonList(ChatColor.AQUA + "- voted: +2 damage"),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), 44),
                0);

        // Gouge
        gouge = ItemCreator.weapon(new ItemStack(Material.NETHERITE_INGOT),
                ChatColor.GOLD + "Gouge", Arrays.asList("",
                        ChatColor.AQUA + "This attack uses combo points, the",
                        ChatColor.AQUA + "more combo points the stronger the attack.", "",
                        ChatColor.AQUA + "This stuns and damages the target but",
                        ChatColor.AQUA + "in order for it to work you have to be behind",
                        ChatColor.AQUA + "your target.", "",
                        ChatColor.AQUA + "If you consume combo points you will deal extra",
                        ChatColor.AQUA + "damage depending on how many you have, on" +
                        ChatColor.AQUA + "top of that you will be healed 10hp per combo point consumed."),
                null, 36);
        es.hotbar[1] = gouge;

        poison = poisonPotion();
        es.hotbar[2] = poison;

        shadowstep = ItemCreator.item(new ItemStack(Material.NETHERITE_BOOTS),
                ChatColor.DARK_GRAY + "Shadowstep",
                Arrays.asList(ChatColor.AQUA + "", ChatColor.YELLOW + "" + ChatColor.BOLD + "Right click to activate.",
                        ChatColor.AQUA + "", "You move through the shadows, invisible to everyone.",
                        "Whilst you shadowstep you can not attack targets.",
                        "This ability lasts 6 seconds and has a cool-down of 12 seconds."),
                null);
        es.hotbar[3] = shadowstep;

        shadowleap = ItemCreator.item(new ItemStack(Material.DRIED_KELP),
                ChatColor.DARK_GRAY + "Shadowleap",
                Arrays.asList(ChatColor.AQUA + "", ChatColor.YELLOW + "" + ChatColor.BOLD + "Right click to activate.",
                        ChatColor.AQUA + "", "You move through the shadows, invisible to everyone.",
                        "Whilst you shadowleap you can not attack targets.",
                        "You get jump boost 5 so you can easily jump on top of roofs and towers.",
                        "This ability can be used as an extension to shadowstep,",
                        "lasts for 6 seconds and has a cool-down of 20 seconds."),
                null);
        es.hotbar[4] = shadowleap;

        trackArrow = ItemCreator.item(new ItemStack(Material.SPECTRAL_ARROW, 5),
                ChatColor.YELLOW + "Track Arrow",
                Arrays.asList(ChatColor.AQUA + "", ChatColor.YELLOW + "" + ChatColor.BOLD + "Right click to throw!",
                        ChatColor.AQUA + "", "Hitting enemy targets with this gives them",
                        "glowing for a minute and stuns them briefly.",
                        "Allowing you enough time to strike or escape.",
                        "hitting enemies with this ability also awards you ",
                        "with a combo point."),
                null);
        es.hotbar[5] = trackArrow;

        comboPoint = ItemCreator.item(new ItemStack(Material.GLOWSTONE_DUST),
                ChatColor.LIGHT_PURPLE + "Combo Point",
                Arrays.asList(ChatColor.AQUA + "", ChatColor.YELLOW + "" + ChatColor.BOLD + "Ability Power Currency",
                        ChatColor.AQUA + "", "This currency can be used to perform a stronger gouge.",
                        "You can get combo points by killing and",
                        "hitting enemies with track arrows."),
                null);

        // Chestplate
        es.chest = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE),
                ChatColor.GREEN + "Leather Chestplate", null, null,
                Color.fromRGB(15, 15, 15));

        // Leggings
        es.legs = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_LEGGINGS),
                ChatColor.DARK_GRAY + "Leather Leggings", null, null,
                Color.fromRGB(19, 19, 19));

        // Boots
        es.feet = ItemCreator.item(new ItemStack(Material.IRON_BOOTS),
                ChatColor.DARK_GRAY + "Rogue Boots", null, null);
        // Voted Boots
        es.votedFeet = ItemCreator.item(new ItemStack(Material.LEATHER_BOOTS),
                ChatColor.GREEN + "Leather Boots",
                Arrays.asList(ChatColor.AQUA + "- voted: Depth Strider II", ChatColor.AQUA + "- rogue: Feather Falling X"),
                Arrays.asList(new Tuple<>(Enchantment.DEPTH_STRIDER, 3), new Tuple<>(Enchantment.PROTECTION_FALL, 9)));



        super.equipment = es;

        // Perm Potion Effect
        super.potionEffects.add(new PotionEffect(PotionEffectType.SPEED, 999999, 1));
        super.potionEffects.add(new PotionEffect(PotionEffectType.JUMP, 999999, 0));
        super.potionEffects.add(new PotionEffect(PotionEffectType.NIGHT_VISION, 999999, 0));
        super.potionEffects.add(new PotionEffect(PotionEffectType.FAST_DIGGING, 999999, 1));

        // Death Messages
        super.deathMessage[0] = "You were struck from the shadows by ";
        super.killMessage[0] = " struck ";
        super.killMessage[1] = " from the shadows";
    }


    public ItemStack poisonPotion() {
        ItemStack itemStack = new ItemStack(Material.POTION);
        PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
        assert potionMeta != null;

        potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.POISON, 1, 6), true);
        potionMeta.setColor(Color.RED);
        potionMeta.setDisplayName(ChatColor.RED + "Bottle of Poison");
        potionMeta.setLore(Arrays.asList(
                ChatColor.AQUA + "", ChatColor.AQUA + "When right clicking this, you will deal poison attacks",
                ChatColor.AQUA + "for the next 20 seconds. This has a 60 second cooldown."));
        itemStack.setItemMeta(potionMeta);

        return itemStack;
    }

}
