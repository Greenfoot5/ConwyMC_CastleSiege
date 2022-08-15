package me.huntifi.castlesiege.kits.kits.donator_kits;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.kits.kits.DonatorKit;
import me.huntifi.castlesiege.kits.kits.Kit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

/**
 * The berserker kit
 */
public class Berserker extends DonatorKit implements Listener {

    private final ItemStack regularSword;
    private final ItemStack regularSwordVoted;
    private final ItemStack berserkSword;
    private final ItemStack berserkSwordVoted;

    /**
     * Set the equipment and attributes of this kit
     */
    public Berserker() {
        super("Berserker", 100, 12, 7500);

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Weapon
        regularSword = ItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                ChatColor.GREEN + "Iron Sword", null, null, 33);
        es.hotbar[0] = regularSword;
        // Voted Weapon
        regularSwordVoted = ItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                ChatColor.GREEN + "Iron Sword",
                Collections.singletonList(ChatColor.AQUA + "- voted: +2 damage"),
                Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), 35);
        es.votedWeapon = new Tuple<>(regularSwordVoted, 0);

        // Ladders
        es.hotbar[2] = new ItemStack(Material.LADDER, 4);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, 6), 2);

        // Potion Item
        es.hotbar[1] = ItemCreator.item(new ItemStack(Material.POTION, 1),
                ChatColor.GOLD + "Berserker Potion", null, null);

        // Berserk Weapon
        berserkSword = ItemCreator.weapon(new ItemStack(Material.DIAMOND_SWORD),
                ChatColor.GREEN + "Berserker Sword", null,
                Collections.singletonList(new Tuple<>(Enchantment.KNOCKBACK, 1)), 65.5);
        // Voted Berserk Weapon
        berserkSwordVoted = ItemCreator.weapon(new ItemStack(Material.DIAMOND_SWORD),
                ChatColor.GREEN + "Berserker Sword",
                Collections.singletonList(ChatColor.AQUA + "- voted: +2 damage"),
                Arrays.asList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0),
                        new Tuple<>(Enchantment.KNOCKBACK, 1)), 67.5);

        super.equipment = es;

        // Perm Potion Effect
        super.potionEffects.add(new PotionEffect(PotionEffectType.SPEED, 999999, 0));

        // Death Messages
        super.deathMessage[0] = "";
        super.deathMessage[1] = " went berserk on you!";
        super.killMessage[0] = " went berserk on ";
    }

    /**
     * Activate the berserker ability, giving the berserk sword, speed, strength, and nausea
     * @param e The event called when clicking with the potion in hand
     */
    @EventHandler
    public void berserkerPotion(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(uuid)) {
            return;
        }

        if (Objects.equals(Kit.equippedKits.get(uuid).name, name)) {
            if (e.getItem() != null && e.getItem().getType() == Material.POTION) {
                if (e.getHand() == EquipmentSlot.HAND) {
                    p.getInventory().getItemInMainHand().setType(Material.GLASS_BOTTLE);
                } else if (e.getHand() == EquipmentSlot.OFF_HAND) {
                    p.getInventory().getItemInOffHand().setType(Material.GLASS_BOTTLE);
                }
                // Potion effects
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (Objects.equals(Kit.equippedKits.get(uuid).name, name) &&
                                p.getPotionEffect(PotionEffectType.INCREASE_DAMAGE) != null) {
                            p.addPotionEffect((new PotionEffect(PotionEffectType.CONFUSION, 380, 1)));
                        }
                    }
                }.runTaskLater(Main.plugin, 20);
                p.addPotionEffect((new PotionEffect(PotionEffectType.SPEED, 400, 1)));
                p.addPotionEffect((new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 400, 0)));

                // Sword
                changeSword(p, regularSword.getType(), berserkSword, berserkSwordVoted);

                // Revert sword
                Bukkit.getScheduler().runTaskLater(Main.plugin, () -> {
                    if (Objects.equals(Kit.equippedKits.get(uuid).name, name) &&
                            p.getPotionEffect(PotionEffectType.INCREASE_DAMAGE) == null)
                        changeSword(p, berserkSword.getType(), regularSword, regularSwordVoted);
                }, 401);
            }
        }
    }

    /**
     * Change the player's berserker sword.
     * @param player The player
     * @param oldMaterial The material of the sword to remove
     * @param sword The sword to set if not voted
     * @param swordVoted The sword to set if voted
     */
    private void changeSword(Player player, Material oldMaterial, ItemStack sword, ItemStack swordVoted) {
        PlayerInventory inventory = player.getInventory();

        // Remove old sword
        inventory.remove(oldMaterial);
        if (inventory.getItemInOffHand().getType() == oldMaterial)
            inventory.setItemInOffHand(null);

        // Don't give a new sword when the player already has one
        if (inventory.contains(sword.getType()) || inventory.getItemInOffHand().getType() == sword.getType())
            return;

        // Give new sword
        if (ActiveData.getData(player.getUniqueId()).hasVote("sword"))
            inventory.addItem(swordVoted);
        else
            inventory.addItem(sword);
    }

    /**
     * Prevents actually drinking the potion in the lobby
     * @param e The event called when a player drinks a potion
     */
    @EventHandler
    public void onDrinkPotion(PlayerItemConsumeEvent e) {
        if (e.getItem().getType() == Material.POTION &&
                Objects.equals(Kit.equippedKits.get(e.getPlayer().getUniqueId()).name, name) &&
                InCombat.isPlayerInLobby(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
        }
    }
}