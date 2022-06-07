package me.huntifi.castlesiege.kits.kits.teamKits;

import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.kits.kits.MapKit;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

public class HelmsdeepLancer extends MapKit implements Listener {

    public HelmsdeepLancer() {
        super("Lancer", 170, 5);
        super.mapSpecificKits.add("Lancer");


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

        // Weapon
        es.hotbar[1] = ItemCreator.weapon(new ItemStack(Material.STICK, 3),
                ChatColor.GREEN + "Spear",
                Collections.singletonList(ChatColor.AQUA + "Right-click to throw a spear."), null, 30.5);
        // Voted Weapon
        es.votedWeapon = new Tuple<>(
                ItemCreator.weapon(new ItemStack(Material.STICK, 3),
                        ChatColor.GREEN + "Spear",
                        Arrays.asList(ChatColor.AQUA + "Right-click to throw a spear.",
                                ChatColor.AQUA + "- voted: +2 damage"),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), 32.5),
                0);

    // Chestplate
    es.chest = ItemCreator.item(new ItemStack(Material.GOLDEN_CHESTPLATE),
    ChatColor.GREEN + "Bronze Chestplate", null, null);

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
    es.hotbar[2] = new ItemStack(Material.LADDER, 4);
    es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, 6), 1);

    // Horse
    es.hotbar[3] = ItemCreator.item(new ItemStack(Material.WHEAT),
    ChatColor.GREEN + "Spawn Horse", null, null);

        super.equipment = es;

        super.canClimb = true;

        // Death Messages
        super.projectileDeathMessage[0] = "You were impaled by ";
        super.projectileKillMessage[0] = "You impaled ";

        super.playableWorld = "Helm's Deep";
        super.teamName = "Rohan";

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
        hAttribute.setBaseValue(125.0);
        horse.setHealth(125.0);
        AttributeInstance kbAttribute = horse.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE);
        kbAttribute.setBaseValue(1);
        AttributeInstance speedAttribute = horse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
        speedAttribute.setBaseValue(0.2425);
        AttributeInstance jumpAttribute = horse.getAttribute(Attribute.HORSE_JUMP_STRENGTH);
        jumpAttribute.setBaseValue(0.8);

        horse.getInventory().setSaddle(new ItemStack(Material.SADDLE)); // Gives horse saddle
        horse.getInventory().setArmor(new ItemStack(Material.GOLDEN_HORSE_ARMOR)); // Gives horse armor
        horse.addPotionEffect((new PotionEffect(PotionEffectType.SPEED, 999999, 0)));
        horse.addPotionEffect((new PotionEffect(PotionEffectType.JUMP, 999999, 1)));
        horse.addPotionEffect((new PotionEffect(PotionEffectType.REGENERATION, 999999, 0)));
        Location loc = p.getLocation();
        horse.teleport(loc);

        horse.addPassenger(p);
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

    @EventHandler
    public void onDismount(EntityDismountEvent e) {
        if (e.getEntity() instanceof Player) {
            removeHorse((Player) e.getEntity(), e.getDismounted());
        }
    }

    /**
     * Activate the Lancer ability of throwing a spear
     * @param e The event called when right-clicking with a stick
     */
    @EventHandler
    public void throwSpear(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        ItemStack stick = p.getInventory().getItemInMainHand();
        int cooldown = p.getCooldown(Material.STICK);

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(uuid)) {
            return;
        }

        if (Objects.equals(Kit.equippedKits.get(uuid).name, name)) {
            if (stick.getType().equals(Material.STICK)) {
                if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (cooldown == 0) {
                        p.setCooldown(Material.STICK, 200);
                        stick.setAmount(stick.getAmount() - 1);
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                                ChatColor.AQUA + "You threw your spear!"));
                        p.launchProjectile(Arrow.class).setVelocity(p.getLocation().getDirection().multiply(2.5));

                    } else {
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                                ChatColor.DARK_RED + "" + ChatColor.BOLD + "You can't throw your spear yet."));
                    }
                }
            }
        }
    }


    /**
     * Set the thrown spear's damage
     * @param e The event called when an arrow hits a player
     */
    @EventHandler (priority = EventPriority.LOW)
    public void changeSpearDamage(ProjectileHitEvent e) {
        if (e.getEntity() instanceof Arrow) {
            Arrow arrow = (Arrow) e.getEntity();

            if(arrow.getShooter() instanceof Player){
                Player damages = (Player) arrow.getShooter();

                if (Objects.equals(Kit.equippedKits.get(damages.getUniqueId()).name, name)) {
                    if (damages.isInsideVehicle()) {
                        arrow.setDamage(30);
                    } else {
                        arrow.setDamage(26);
                    }
                }
            }
        }
    }
}
