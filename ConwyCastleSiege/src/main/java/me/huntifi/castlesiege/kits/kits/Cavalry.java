package me.huntifi.castlesiege.kits.kits;

import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

/**
 * The cavalry kit
 */
public class Cavalry extends Kit implements Listener {

    /**
     * Set the equipment and attributes of this kit
     */
    public Cavalry() {
        super("Cavalry", 170, 5);

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Weapon
        es.hotbar[0] = ItemCreator.item(new ItemStack(Material.IRON_SWORD),
                ChatColor.GREEN + "Sabre", null,
                Collections.singletonList(new Tuple<>(Enchantment.DAMAGE_ALL, 16)));
        // Voted Weapon
        es.votedWeapon = new Tuple<>(
                ItemCreator.item(new ItemStack(Material.IRON_SWORD),
                        ChatColor.GREEN + "Sabre",
                        Collections.singletonList(ChatColor.AQUA + "- voted: +2 damage"),
                        Collections.singletonList(new Tuple<>(Enchantment.DAMAGE_ALL, 18))),
                0);

        // Chestplate
        es.chest = ItemCreator.item(new ItemStack(Material.CHAINMAIL_CHESTPLATE),
                ChatColor.GREEN + "Chainmail Chestplate", null, null);

        // Leggings
        es.legs = ItemCreator.item(new ItemStack(Material.CHAINMAIL_LEGGINGS),
                ChatColor.GREEN + "Chainmail Leggings", null, null);

        // Boots
        es.feet = ItemCreator.item(new ItemStack(Material.IRON_BOOTS),
                ChatColor.GREEN + "Iron Boots", null, null);
        // Voted Boots
        es.votedFeet = ItemCreator.item(new ItemStack(Material.IRON_BOOTS),
                ChatColor.GREEN + "Iron Boots",
                Collections.singletonList(ChatColor.AQUA + "- voted: Depth Strider +2"),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)));

        // Ladders
        es.hotbar[1] = new ItemStack(Material.LADDER, 4);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, 6), 1);

        // Horse
        es.hotbar[2] = ItemCreator.item(new ItemStack(Material.WHEAT),
                ChatColor.GREEN + "Spawn Horse", null, null);

        super.equipment = es;
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
                    p.getVehicle().remove();
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
        hAttribute.setBaseValue(150.0);
        horse.setHealth(150.0);
        AttributeInstance kbAttribute = horse.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE);
        kbAttribute.setBaseValue(1);
        AttributeInstance speedAttribute = horse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
        speedAttribute.setBaseValue(0.2425);
        AttributeInstance jumpAttribute = horse.getAttribute(Attribute.HORSE_JUMP_STRENGTH);
        jumpAttribute.setBaseValue(0.8);

        horse.getInventory().setSaddle(new ItemStack(Material.SADDLE)); // Gives horse saddle
        horse.getInventory().setArmor(new ItemStack(Material.IRON_HORSE_ARMOR)); // Gives horse armor
        horse.addPotionEffect((new PotionEffect(PotionEffectType.SPEED, 999999, 0)));
        horse.addPotionEffect((new PotionEffect(PotionEffectType.JUMP, 999999, 1)));
        horse.addPotionEffect((new PotionEffect(PotionEffectType.REGENERATION, 999999, 0)));
        Location loc = p.getLocation();
        horse.teleport(loc);

        horse.addPassenger(p);
    }

    /**
     * Remove the horse when its rider dismounts
     * @param e The event called when dismounting a horse
     */
    @EventHandler
    public void onDismount(EntityDismountEvent e) {
        removeHorse((Player) e.getEntity(), e.getDismounted());
    }

    /**
     * Remove the horse when its rider dies
     * @param e The event called when a player dies
     */
    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        removeHorse(e.getEntity(), e.getEntity().getVehicle());
    }

    /**
     * Remove the horse when its rider leaves the game
     * @param e The event called when a player leaves the game
     */
    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        removeHorse(e.getPlayer(), e.getPlayer().getVehicle());
    }

    /**
     * Remove the horse
     * @param p The player that was riding the horse
     * @param e The horse that is to be removed
     */
    private void removeHorse(Player p, Entity e) {
        if (Kit.equippedKits.containsKey(p.getUniqueId()) &&
                Objects.equals(Kit.equippedKits.get(p.getUniqueId()).name, name) &&
                e instanceof Horse) {
            e.remove();
        }
    }
}
