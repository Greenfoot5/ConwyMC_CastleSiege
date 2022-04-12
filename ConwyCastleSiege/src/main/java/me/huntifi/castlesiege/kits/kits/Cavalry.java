package me.huntifi.castlesiege.kits.kits;

import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.kits.EquipmentSet;
import me.huntifi.castlesiege.kits.Kit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
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
import org.jetbrains.annotations.NotNull;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.util.Collections;
import java.util.Objects;

public class Cavalry extends Kit implements Listener, CommandExecutor {

    public Cavalry() {
        super("Cavalry");
        super.baseHealth = 110;


        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Weapon
        es.hotbar[0] = createItem(new ItemStack(Material.IRON_SWORD),
                ChatColor.GREEN + "Sabre", null,
                Collections.singletonList(new Tuple<>(Enchantment.DAMAGE_ALL, 16)));
        // Voted Weapon
        es.votedWeapon = new Tuple<>(
                createItem(new ItemStack(Material.IRON_SWORD),
                        ChatColor.GREEN + "Sabre",
                        Collections.singletonList(ChatColor.AQUA + "- voted: +2 damage"),
                        Collections.singletonList(new Tuple<>(Enchantment.DAMAGE_ALL, 18))),
                0);

        // Chestplate
        es.chest = createItem(new ItemStack(Material.CHAINMAIL_CHESTPLATE),
                ChatColor.GREEN + "Chainmail Chestplate", null, null);

        // Leggings
        es.legs = createItem(new ItemStack(Material.CHAINMAIL_LEGGINGS),
                ChatColor.GREEN + "Chainmail Leggings", null, null);

        // Boots
        es.feet = createItem(new ItemStack(Material.IRON_BOOTS),
                ChatColor.GREEN + "Iron Boots", null, null);
        // Voted Boots
        es.votedFeet = createItem(new ItemStack(Material.IRON_BOOTS),
                ChatColor.GREEN + "Iron Boots",
                Collections.singletonList(ChatColor.AQUA + "- voted: Depth Strider +2"),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)));

        // Ladders
        es.hotbar[1] = new ItemStack(Material.LADDER, 4);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, 6), 1);

        // Horse
        es.hotbar[2] = createItem(new ItemStack(Material.WHEAT),
                ChatColor.GREEN + "Spawn Horse", null, null);

        super.equipment = es;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        super.addPlayer(((Player) commandSender).getUniqueId());
        return true;
    }

    @EventHandler
    public void onRide(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        if (Objects.equals(Kit.equippedKits.get(p.getUniqueId()).name, name) &&
                e.getItem() != null && e.getItem().getType() == Material.WHEAT) {
            int cooldown = p.getCooldown(Material.WHEAT);
            if (cooldown == 0) {
                p.setCooldown(Material.WHEAT, 600);

                if (p.isInsideVehicle()) {
                    p.getVehicle().remove();
                }
                onHorse(p);
            }
        }
    }

    public void onHorse(Player p) {
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

    @EventHandler
    public void onDismount(EntityDismountEvent e) {
        removeHorse((Player) e.getEntity(), e.getDismounted());
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        super.onDeath(e);
        removeHorse(e.getEntity(), e.getEntity().getVehicle());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        removeHorse(e.getPlayer(), e.getPlayer().getVehicle());
    }

    private void removeHorse(Player p, Entity e) {
        if (Objects.equals(Kit.equippedKits.get(p.getUniqueId()).name, name) &&
                e instanceof Horse) {
            e.remove();
        }
    }
}
