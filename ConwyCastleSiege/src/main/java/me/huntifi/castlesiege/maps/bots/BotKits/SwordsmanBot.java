package me.huntifi.castlesiege.maps.bots.BotKits;

import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.kits.kits.Kit;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.trait.trait.Equipment;
import net.citizensnpcs.api.trait.trait.MobType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.mcmonkey.sentinel.SentinelIntegration;
import org.mcmonkey.sentinel.SentinelTrait;

import java.util.Collections;

public class SwordsmanBot extends SentinelIntegration {

    public static void setSwordsman(NPC npc) {

        npc.addTrait(SentinelTrait.class);

        SentinelTrait sentinel = npc.getTrait(SentinelTrait.class);
        sentinel.setHealth(Kit.getKit("Swordsman").getBaseHealth());
        int id = sentinel.getNPC().getId();
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        Bukkit.dispatchCommand(console, "npc select " + id);
        Bukkit.dispatchCommand(console, "sentinel speed 1.1");
        Bukkit.dispatchCommand(console, "sentinel damage 1.1");
        Bukkit.dispatchCommand(console, "sentinel knockback 0.1");
        Bukkit.dispatchCommand(console, "sentinel fightback true");
        Bukkit.dispatchCommand(console, "sentinel respawnTime 10");
        Bukkit.dispatchCommand(console, "sentinel realistic true");

        npc.getTrait(MobType.class).setType(EntityType.PLAYER);
        npc.getTrait(Equipment.class).set(Equipment.EquipmentSlot.BOOTS, new ItemStack(Material.IRON_BOOTS, 1));
        npc.getTrait(Equipment.class).set(Equipment.EquipmentSlot.CHESTPLATE, new ItemStack(Material.IRON_CHESTPLATE, 1));
        npc.getTrait(Equipment.class).set(Equipment.EquipmentSlot.LEGGINGS, new ItemStack(Material.IRON_LEGGINGS, 1));

        // Voted Weapon
       ItemStack votedWeapon = ItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                        ChatColor.GREEN + "Iron Sword",
                        Collections.singletonList(ChatColor.AQUA + "- voted: +2 damage"),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), 35);

       npc.getTrait(Equipment.class).set(Equipment.EquipmentSlot.HAND, votedWeapon);
    }


}
