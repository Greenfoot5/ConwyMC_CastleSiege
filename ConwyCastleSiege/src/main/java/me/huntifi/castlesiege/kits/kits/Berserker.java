package me.huntifi.castlesiege.kits.kits;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.kits.EquipmentSet;
import me.huntifi.castlesiege.kits.ItemCreator;
import me.huntifi.castlesiege.voting.VotesChanging;
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
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

public class Berserker extends Kit implements Listener, CommandExecutor {

    private final ItemStack regularSword;
    private final ItemStack regularSwordVoted;
    private final ItemStack berserkSword;
    private final ItemStack berserkSwordVoted;

    public Berserker() {
        super("Berserker", 110);

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Weapon
        regularSword = ItemCreator.item(new ItemStack(Material.IRON_SWORD),
                ChatColor.GREEN + "Iron Sword", null,
                Collections.singletonList(new Tuple<>(Enchantment.DAMAGE_ALL, 20)));
        es.hotbar[0] = regularSword;
        // Voted Weapon
        regularSwordVoted = ItemCreator.item(new ItemStack(Material.IRON_SWORD),
                ChatColor.GREEN + "Iron Sword",
                Collections.singletonList(ChatColor.AQUA + "- voted: +2 damage"),
                Collections.singletonList(new Tuple<>(Enchantment.DAMAGE_ALL, 22)));
        es.votedWeapon = new Tuple<>(regularSwordVoted, 0);

        // Ladders
        es.hotbar[2] = new ItemStack(Material.LADDER, 4);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, 6), 2);

        // Potion Item
        es.hotbar[1] = ItemCreator.item(new ItemStack(Material.POTION, 1),
                ChatColor.GOLD + "Berserker Potion", null, null);

        // Berserk Weapon
        berserkSword = ItemCreator.item(new ItemStack(Material.IRON_SWORD),
                ChatColor.GREEN + "Berserker Sword", null,
                Arrays.asList(new Tuple<>(Enchantment.DAMAGE_ALL, 56),
                        new Tuple<>(Enchantment.KNOCKBACK, 1)));
        // Voted Berserk Weapon
        berserkSwordVoted = ItemCreator.item(new ItemStack(Material.IRON_SWORD),
                ChatColor.GREEN + "Berserker Sword",
                Collections.singletonList(ChatColor.AQUA + "- voted: +2 damage"),
                Arrays.asList(new Tuple<>(Enchantment.DAMAGE_ALL, 58),
                        new Tuple<>(Enchantment.KNOCKBACK, 1)));

        super.equipment = es;

        // Perm Potion Effect
        super.potionEffects.add(new PotionEffect(PotionEffectType.SPEED, 999999, 0));

        // Death Messages
        super.deathMessage[0] = "";
        super.deathMessage[1] = " went berserk on you!";
        super.killMessage[0] = "You went berserk on ";
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

        // Prevent using in lobby
        if (!InCombat.hasPlayerSpawned(uuid)) {
            return;
        }

        if (Objects.equals(Kit.equippedKits.get(uuid).name, name)) {
            if (e.getItem() != null && e.getItem().getType() == Material.POTION) {
                p.getInventory().getItemInMainHand().setType(Material.GLASS_BOTTLE);
                // Potion effects
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (Objects.equals(Kit.equippedKits.get(uuid).name, name) &&
                                p.getPotionEffect(PotionEffectType.INCREASE_DAMAGE) != null) {
                            p.addPotionEffect((new PotionEffect(PotionEffectType.CONFUSION, 320, 0)));
                        }
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
                        if (Objects.equals(Kit.equippedKits.get(uuid).name, name) &&
                                p.getPotionEffect(PotionEffectType.INCREASE_DAMAGE) == null) {
                            if (!VotesChanging.getVotes(p.getUniqueId()).contains("V#1")) {
                                p.getInventory().setItem(0, regularSword);
                            } else {
                                p.getInventory().setItem(0, regularSwordVoted);
                            }
                        }
                    }
                }.runTaskLater(Main.plugin, 401);
            }
        }
    }

    @EventHandler
    public void onDrinkPotion(PlayerItemConsumeEvent e) {
        if (e.getItem().getType() == Material.POTION &&
                Objects.equals(Kit.equippedKits.get(e.getPlayer().getUniqueId()).name, name) &&
                !InCombat.hasPlayerSpawned(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
        }
    }
}