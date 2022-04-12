package me.huntifi.castlesiege.kits.kits;

import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.kits.EquipmentSet;
import me.huntifi.castlesiege.kits.Kit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public class Scout extends Kit implements CommandExecutor {

    public Scout() {
        super("Scout");
        super.baseHealth = 100;


        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Weapon
        es.hotbar[0] = createItem(new ItemStack(Material.WOODEN_SWORD),
                ChatColor.GREEN + "Shortsword", null,
                Collections.singletonList(new Tuple<>(Enchantment.DAMAGE_ALL, 19)));
        // Voted weapon
        es.votedWeapon = new Tuple<>(
                createItem(new ItemStack(Material.WOODEN_SWORD),
                        ChatColor.GREEN + "Shortsword",
                        Collections.singletonList(ChatColor.AQUA + "- voted: +2 damage"),
                        Collections.singletonList(new Tuple<>(Enchantment.DAMAGE_ALL, 21))),
                0);

        // Chestplate
        es.chest = createLeatherItem(new ItemStack(Material.LEATHER_CHESTPLATE),
                ChatColor.GREEN + "Leather Chestplate", null, null,
                Color.fromRGB(64, 87, 1));

        // Leggings
        es.legs = createLeatherItem(new ItemStack(Material.LEATHER_LEGGINGS),
                ChatColor.GREEN + "Leather Leggings", null, null,
                Color.fromRGB(42, 57, 3));

        // Boots
        es.feet = createLeatherItem(new ItemStack(Material.LEATHER_BOOTS),
                ChatColor.GREEN + "Leather Boots", null,
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 1)),
                Color.fromRGB(64, 87, 1));
        // Voted Boots
        es.votedFeet = createLeatherItem(new ItemStack(Material.LEATHER_BOOTS),
                ChatColor.GREEN + "Leather Boots",
                Collections.singletonList(ChatColor.AQUA + "- voted: Depth Strider +2"),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 3)),
                Color.fromRGB(64, 87, 1));

        // Ladders
        es.hotbar[1] = new ItemStack(Material.LADDER, 6);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, 8), 1);

        super.equipment = es;

        // Perm Potion Effect
        super.potionEffects.add(new PotionEffect(PotionEffectType.SPEED, 999999, 1));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        super.addPlayer(((Player) commandSender).getUniqueId());
        return true;
    }
}
