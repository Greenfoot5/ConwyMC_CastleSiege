package me.huntifi.castlesiege.maps.bots.BotKits;

import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.kits.kits.Kit;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.trait.trait.Equipment;
import net.citizensnpcs.api.trait.trait.MobType;
import net.citizensnpcs.api.trait.trait.Owner;
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


        SentinelTrait trait = new SentinelTrait();
        npc.addTrait(trait);
        trait.setHealth(120);
        trait.realistic = true;
        trait.speed = 1.1;
        trait.damage = 25;
        trait.attackRate = 3;
        trait.fightback = true;
        npc.addTrait(trait);

        npc.getTrait(MobType.class).setType(EntityType.PLAYER);
        npc.getTrait(Owner.class).setOwner("Owner");
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
