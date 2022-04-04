package me.huntifi.castlesiege.kits.kits;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.kits.EquipmentSet;
import me.huntifi.castlesiege.kits.Kit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.huntifi.castlesiege.voting.VotesChanging;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

public class Berserker extends Kit implements Listener, CommandExecutor {

    private final ItemStack regularSword;
    private final ItemStack regularSwordVoted;
    private final ItemStack berserkSword;
    private final ItemStack berserkSwordVoted;

    public Berserker() {
        super("Berserker");
        super.baseHeath = 110;

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Weapon
        ItemStack item = new ItemStack(Material.IRON_SWORD);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.setUnbreakable(true);
        itemMeta.setDisplayName(ChatColor.GREEN + "Iron Sword");
        itemMeta.addEnchant(Enchantment.DAMAGE_ALL, 20, true);
        itemMeta.setLore(new ArrayList<>());
        item.setItemMeta(itemMeta);
        regularSword = item.clone();
        es.hotbar[0] = item;
        // Voted Weapon
        item.getItemMeta().addEnchant(Enchantment.DAMAGE_ALL, 22, true);
        item.getItemMeta().setLore(Arrays.asList("", ChatColor.AQUA + "- voted: +2 damage"));
        regularSwordVoted = item.clone();
        es.votedWeapon = new Tuple<>(item, 0);

        // Ladders
        es.hotbar[2] = new ItemStack(Material.LADDER, 4);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, 6), 2);

        // Potion Item
        item = new ItemStack(Material.POTION, 1);
        itemMeta = item.getItemMeta();
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        itemMeta.setDisplayName(ChatColor.GOLD + "Berserker Potion");
        es.hotbar[1] = item;

        // Perm Potion Effect
        super.potionEffects = new PotionEffect[1];
        super.potionEffects[0] = new PotionEffect(PotionEffectType.SPEED, 999999, 0);

        // Berserk Weapon
        item = new ItemStack(Material.IRON_SWORD);
        itemMeta = item.getItemMeta();
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.setUnbreakable(true);
        itemMeta.setDisplayName(ChatColor.GREEN + "Berserker Sword");
        itemMeta.addEnchant(Enchantment.DAMAGE_ALL, 56, true);
        itemMeta.addEnchant(Enchantment.KNOCKBACK, 1, true);
        itemMeta.setLore(new ArrayList<>());
        item.setItemMeta(itemMeta);
        berserkSword = item.clone();
        // Voted Weapon
        item.getItemMeta().addEnchant(Enchantment.DAMAGE_ALL, 58, true);
        item.getItemMeta().setLore(Arrays.asList("", ChatColor.AQUA + "- voted: +2 damage"));
        berserkSwordVoted = item.clone();

        super.equipment = es;


        // Death Messages
        super.deathPrefix = false;
        super.deathMessage = " went berserk on you!";
        super.killMessage = "You went berserk on ";
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        super.addPlayer(((Player) commandSender).getUniqueId());
        return true;
    }

    @EventHandler
    public void berserkerPotion(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        if (Objects.equals(Kit.equippedKits.get(uuid).name, name)) {
            if (e.getItem() != null && e.getItem().getType() == Material.POTION) {
                p.getInventory().getItemInMainHand().setType(Material.GLASS_BOTTLE);
                // Potion effects
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        p.addPotionEffect((new PotionEffect(PotionEffectType.CONFUSION, 320, 0)));
                    }
                }.runTaskLater(Main.plugin, 80);
                p.addPotionEffect((new PotionEffect(PotionEffectType.SPEED, 400, 1)));
                p.addPotionEffect((new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 400, 0)));
                // Sword
                if (!VotesChanging.getVotes(uuid).contains("V#1")) {
                    p.getInventory().setItem(0, berserkSword);
                } else {
                    p.getInventory().setItem(0, berserkSwordVoted);
                }

                // Revert sword
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (Objects.equals(Kit.equippedKits.get(uuid).name, name)) {
                            if (!VotesChanging.getVotes(p.getUniqueId()).contains("V#1")) {
                                p.getInventory().setItem(0, regularSword);
                            } else {
                                p.getInventory().setItem(0, regularSwordVoted);
                            }
                        }
                    }
                }.runTaskLater(Main.plugin, 400);
            }
        }
    }
}