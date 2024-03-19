package me.huntifi.castlesiege.kits.kits.coin_kits;

import me.huntifi.castlesiege.Main;
import me.huntifi.conwymc.data_types.Tuple;
import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.kits.kits.CoinKit;
import me.huntifi.castlesiege.kits.kits.Kit;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

/**
 * The berserker kit
 */
public class Berserker extends CoinKit implements Listener {

    private static final int health = 200;
    private static final double regen = 20;
    private static final double meleeDamage = 53;
    private static final double meleeDamageZerk = 105.5;
    private static final int ladderCount = 4;

    private final ItemStack regularSword;
    private final ItemStack regularSwordVoted;
    private final ItemStack berserkSword;
    private final ItemStack berserkSwordVoted;

    /**
     * Set the equipment and attributes of this kit
     */
    public Berserker() {
        super("Berserker", health, regen, Material.POTION);

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Weapon
        regularSword = ItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                Component.text("Iron Sword", NamedTextColor.GREEN), null,
                Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), meleeDamage);
        es.hotbar[0] = regularSword;
        // Voted Weapon
        regularSwordVoted = ItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                Component.text("Iron Sword", NamedTextColor.GREEN),
                Collections.singletonList(Component.text("- voted: +2 damage", NamedTextColor.AQUA)),
                Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), meleeDamage + 2);
        es.votedWeapon = new Tuple<>(regularSwordVoted, 0);

        // Ladders
        es.hotbar[2] = new ItemStack(Material.LADDER, ladderCount);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, ladderCount + 2), 2);

        // Potion Item
        es.hotbar[1] = ItemCreator.item(new ItemStack(Material.POTION, 1),
                Component.text("Berserker Potion", NamedTextColor.GOLD), null, null);

        // Berserk Weapon
        berserkSword = ItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                Component.text("Berserker Sword", NamedTextColor.GREEN), null,
                Collections.singletonList(new Tuple<>(Enchantment.KNOCKBACK, 1)), meleeDamageZerk);
        // Voted Berserk Weapon
        berserkSwordVoted = ItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                Component.text("Berserker Sword", NamedTextColor.GREEN),
                Collections.singletonList(Component.text("- voted: +2 damage", NamedTextColor.AQUA)),
                Arrays.asList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0),
                        new Tuple<>(Enchantment.KNOCKBACK, 1)), meleeDamageZerk + 2);

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


        if (Objects.equals(Kit.equippedKits.get(uuid).name, name)) {
            if (e.getItem() != null && e.getItem().getType() == Material.POTION) {
                if (e.getHand() == EquipmentSlot.HAND) {
                    p.getInventory().getItemInMainHand().setType(Material.GLASS_BOTTLE);
                } else if (e.getHand() == EquipmentSlot.OFF_HAND) {
                    p.getInventory().getItemInOffHand().setType(Material.GLASS_BOTTLE);
                }

                p.getInventory().remove(Material.GLASS_BOTTLE);
                if (p.getInventory().getItemInOffHand().getType() == Material.GLASS_BOTTLE)
                    p.getInventory().setItemInOffHand(null);

                // Prevent using in lobby
                if (InCombat.isPlayerInLobby(uuid)) {
                    e.setCancelled(true);
                    return;
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
     * @return The lore to add to the kit gui item
     */
    @Override
    public ArrayList<Component> getGuiDescription() {
        ArrayList<Component> kitLore = new ArrayList<>();
        kitLore.add(Component.text("A warrior with no armor and", NamedTextColor.GRAY));
        kitLore.add(Component.text("a berserker potion", NamedTextColor.GRAY));
        kitLore.addAll(getBaseStats(health, regen, meleeDamage, ladderCount));
        kitLore.add(Component.empty());
        kitLore.add(Component.text("Effects:", NamedTextColor.DARK_PURPLE));
        kitLore.add(Component.text("- Speed I", NamedTextColor.GRAY));
        kitLore.add(Component.text(" ", NamedTextColor.GRAY));
        kitLore.add(Component.text("Berserk Effects:", NamedTextColor.DARK_PURPLE));
        kitLore.add(Component.text("- Speed II", NamedTextColor.GRAY));
        kitLore.add(Component.text("- Confusion II", NamedTextColor.GRAY));
        kitLore.add(Component.text("- Strength I", NamedTextColor.GRAY));
        kitLore.add(Component.text("- Melee hits deal 105+ damage", NamedTextColor.GRAY));
        return kitLore;
    }
}