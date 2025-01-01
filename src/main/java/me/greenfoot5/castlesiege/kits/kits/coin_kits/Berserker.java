package me.greenfoot5.castlesiege.kits.kits.coin_kits;

import me.greenfoot5.castlesiege.Main;
import me.greenfoot5.castlesiege.database.CSActiveData;
import me.greenfoot5.castlesiege.events.combat.InCombat;
import me.greenfoot5.castlesiege.kits.items.CSItemCreator;
import me.greenfoot5.castlesiege.kits.items.EquipmentSet;
import me.greenfoot5.castlesiege.kits.kits.CoinKit;
import me.greenfoot5.castlesiege.kits.kits.Kit;
import me.greenfoot5.castlesiege.maps.MapController;
import me.greenfoot5.conwymc.data_types.Tuple;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
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
import java.util.List;
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

        // Weapon
        regularSword = CSItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                Component.text("Iron Sword", NamedTextColor.GREEN),
                List.of(Component.empty(),
                        Component.text(meleeDamage + " Melee Damage", NamedTextColor.DARK_GREEN)),
                Collections.singletonList(new Tuple<>(Enchantment.LOOTING, 0)), meleeDamage);
        es.hotbar[0] = regularSword;
        // Voted Weapon
        regularSwordVoted = CSItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                Component.text("Iron Sword", NamedTextColor.GREEN),
                List.of(Component.empty(),
                        Component.text((meleeDamage + 2) + " Melee Damage", NamedTextColor.DARK_GREEN),
                        Component.text("⁎ Voted: +2 Melee Damage", NamedTextColor.GREEN)),
                Collections.singletonList(new Tuple<>(Enchantment.LOOTING, 0)), meleeDamage + 2);
        es.votedWeapon = new Tuple<>(regularSwordVoted, 0);

        // Ladders
        es.hotbar[2] = new ItemStack(Material.LADDER, ladderCount);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, ladderCount + 2), 2);

        // Potion Item
        es.hotbar[1] = CSItemCreator.item(new ItemStack(Material.POTION, 1),
                Component.text("Berserker Potion", NamedTextColor.GOLD),
                List.of(Component.empty(),
                        Component.text("<< Right Click To Use >>", NamedTextColor.DARK_GRAY),
                        Component.text("Drink to enter berserk mode", NamedTextColor.GRAY),
                        Component.empty(),
                        Component.text("+ 54.5 Melee Damage", NamedTextColor.DARK_GREEN),
                        Component.text("○ Strength I (0:20)", TextColor.color(40, 169, 255)),
                        Component.text("○ Speed II (0:20)", TextColor.color(40, 169, 255)),
                        Component.text("○ Nausea II (0:20)", NamedTextColor.RED)),
                        null);

        // Berserk Weapon
        berserkSword = CSItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                Component.text("Berserker Sword", NamedTextColor.GREEN),
                List.of(Component.empty(),
                        Component.text("105.5 Melee Damage", NamedTextColor.DARK_GREEN)),
                Collections.singletonList(new Tuple<>(Enchantment.KNOCKBACK, 1)), meleeDamageZerk);
        // Voted Berserk Weapon
        berserkSwordVoted = CSItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                Component.text("Berserker Sword", NamedTextColor.GREEN),
                List.of(Component.empty(),
                        Component.text("107.5 Melee Damage", NamedTextColor.DARK_GREEN),
                        Component.text("⁎ Voted: +2 Melee Damage", NamedTextColor.DARK_AQUA)),
                Arrays.asList(new Tuple<>(Enchantment.LOOTING, 0),
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

        if (!MapController.getPlayers().contains(uuid))
            return;
        if (!Objects.equals(Kit.equippedKits.get(uuid).name, name)) {
            return;
        }
        if (e.getItem() == null || e.getItem().getType() != Material.POTION) {
            return;
        }
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
                        p.getPotionEffect(PotionEffectType.STRENGTH) != null) {
                    p.addPotionEffect((new PotionEffect(PotionEffectType.NAUSEA, 380, 1)));
                }
            }
        }.runTaskLater(Main.plugin, 20);
        p.addPotionEffect((new PotionEffect(PotionEffectType.SPEED, 400, 1)));
        p.addPotionEffect((new PotionEffect(PotionEffectType.STRENGTH, 400, 0)));

        // Sword
        changeSword(p, regularSword.getType(), berserkSword, berserkSwordVoted);

        // Revert sword
        Bukkit.getScheduler().runTaskLater(Main.plugin, () -> {
            if (Objects.equals(Kit.equippedKits.get(uuid).name, name) &&
                    p.getPotionEffect(PotionEffectType.STRENGTH) == null)
                changeSword(p, berserkSword.getType(), regularSword, regularSwordVoted);
        }, 401);
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
        if (CSActiveData.getData(player.getUniqueId()).hasVote("sword"))
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
        kitLore.add(Component.text("- Nausea II", NamedTextColor.GRAY));
        kitLore.add(Component.text("- Strength I", NamedTextColor.GRAY));
        kitLore.add(Component.text("- Melee hits deal 105+ damage", NamedTextColor.GRAY));
        return kitLore;
    }
}