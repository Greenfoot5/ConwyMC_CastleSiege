package me.huntifi.castlesiege.kits.kits.team_kits;

import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.kits.kits.TeamKit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

public class ConwyRoyalKnight extends TeamKit implements Listener {

    public ConwyRoyalKnight() {
        super("Royal Knight", 200, 5, "Conwy", "The English", 5000);


        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Weapon
        es.hotbar[0] = ItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                ChatColor.GREEN + "Sword", null, null, 25.5);
        // Voted Weapon
        es.votedWeapon = new Tuple<>(
                ItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                        ChatColor.GREEN + "Sword",
                        Collections.singletonList(ChatColor.AQUA + "- voted: +2 damage"),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), 27.5),
                0);

        // Shield
        es.offhand = ItemCreator.item(new ItemStack(Material.SHIELD),
                ChatColor.GREEN + "Shield", null,
                null);

        // Chestplate
        es.chest = ItemCreator.item(new ItemStack(Material.NETHERITE_CHESTPLATE),
                ChatColor.GREEN + "Reinforced Iron Chestplate", null, null);

        // Leggings
        es.legs = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_LEGGINGS),
                ChatColor.GREEN + "Leather Leggings", null,
                Collections.singletonList(new Tuple<>(Enchantment.PROTECTION_PROJECTILE, 2)),
                Color.fromRGB(174, 26, 26));

        // Boots
        es.feet = ItemCreator.item(new ItemStack(Material.NETHERITE_BOOTS),
                ChatColor.GREEN + "Reinforced Iron Boots", null, null);
        // Voted Boots
        es.votedFeet = ItemCreator.item(new ItemStack(Material.NETHERITE_BOOTS),
                ChatColor.GREEN + "Iron Boots",
                Collections.singletonList(ChatColor.AQUA + "- voted: Depth Strider II"),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)));

        // Ladders
        es.hotbar[1] = new ItemStack(Material.LADDER, 4);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, 6), 2);

        // Horse
        es.hotbar[2] = ItemCreator.item(new ItemStack(Material.WHEAT),
                ChatColor.GREEN + "Spawn Royal Steed", null, null);

        super.equipment = es;

        super.canClimb = true;

    }

    /**
     * Activate the cavalry ability, spawning and embarking a horse
     * @param e The event called when clicking with wheat in hand
     */
    @EventHandler
    public void onRide(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(uuid)) {
            return;
        }

        if (Objects.equals(Kit.equippedKits.get(uuid).name, name) &&
                e.getItem() != null && e.getItem().getType() == Material.WHEAT) {
            int cooldown = p.getCooldown(Material.WHEAT);
            if (cooldown == 0) {
                p.setCooldown(Material.WHEAT, 600);

                if (p.isInsideVehicle()) {
                    Objects.requireNonNull(p.getVehicle()).remove();
                }
                spawnHorse(p);
            }
        }
    }

    /**
     * Spawn a horse, apply attributes, and embark
     * @param p The player for whom to spawn a horse
     */
    public void spawnHorse(Player p) {
        Horse horse = (Horse) p.getWorld().spawnEntity(p.getLocation(), EntityType.HORSE);

        horse.setTamed(true);
        horse.setOwner(p);
        horse.setAdult();
        AttributeInstance hAttribute = horse.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        assert hAttribute != null;
        hAttribute.setBaseValue(200.0);
        horse.setHealth(200.0);
        AttributeInstance kbAttribute = horse.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE);
        assert kbAttribute != null;
        kbAttribute.setBaseValue(1);
        AttributeInstance speedAttribute = horse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
        assert speedAttribute != null;
        speedAttribute.setBaseValue(0.2425);
        AttributeInstance jumpAttribute = horse.getAttribute(Attribute.HORSE_JUMP_STRENGTH);
        assert jumpAttribute != null;
        jumpAttribute.setBaseValue(1.1);

        horse.getInventory().setSaddle(new ItemStack(Material.SADDLE)); // Gives horse saddle
        horse.getInventory().setArmor(new ItemStack(Material.DIAMOND_HORSE_ARMOR)); // Gives horse armor
        addThisHorseEffects(p, horse);
    }

    public static void addThisHorseEffects(Player p, Horse horse) {
        horse.addPotionEffect((new PotionEffect(PotionEffectType.JUMP, 999999, 0)));
        horse.addPotionEffect((new PotionEffect(PotionEffectType.REGENERATION, 999999, 4)));
        Location loc = p.getLocation();
        horse.teleport(loc);

        horse.addPassenger(p);
    }
}
