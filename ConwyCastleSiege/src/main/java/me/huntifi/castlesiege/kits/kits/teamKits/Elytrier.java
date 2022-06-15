package me.huntifi.castlesiege.kits.kits.teamKits;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.kits.kits.MapKit;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

public class Elytrier extends MapKit implements Listener {

    public Elytrier() {
        super("", 160, 5, "Thunderstone", "Thunderstone Guard");

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Weapon
        es.hotbar[0] = ItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                ChatColor.GREEN + "Sword", null, null, 26);
        // Voted Weapon
        es.votedWeapon = new Tuple<>(
                ItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                        ChatColor.GREEN + "Sword",
                        Collections.singletonList(ChatColor.AQUA + "- voted: +2 damage"),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), 28),
                0);

        // Chestplate
        es.chest = ItemCreator.item(new ItemStack(Material.ELYTRA),
                ChatColor.GOLD + "Elytra", null, null);

        // Leggings
        es.legs = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_LEGGINGS),
                ChatColor.GREEN + "Leather Leggings", null, null,
                Color.fromRGB(236, 173, 91));

        // Boots
        es.feet = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_BOOTS),
                ChatColor.GREEN + "Leather Boots", null, null,
                Color.fromRGB(236, 173, 91));
        // Voted boots
        es.votedFeet = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_BOOTS),
                ChatColor.GREEN + "Leather Boots",
                Collections.singletonList(ChatColor.AQUA + "- voted: Depth Strider II"),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)),
                Color.fromRGB(236, 173, 91));

        // Weapon
        es.hotbar[1] = ItemCreator.item(new ItemStack(Material.SNOWBALL),
                ChatColor.GREEN + "Small Bomb",
                Collections.singletonList(ChatColor.AQUA + "Right-click to drop a bomb which explodes after 5 seconds!"), null);

        // Weapon
        es.hotbar[3] = ItemCreator.item(new ItemStack(Material.FIREWORK_ROCKET),
                ChatColor.GREEN + "Small Boost",
                Collections.singletonList(ChatColor.AQUA + "Right-click to go a little faster!"), null);

        // Ladders
        es.hotbar[2] = new ItemStack(Material.LADDER, 4);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, 6), 2);

        super.equipment = es;

        super.potionEffects.add(new PotionEffect(PotionEffectType.FAST_DIGGING, 999999, 0));
        super.potionEffects.add(new PotionEffect(PotionEffectType.SPEED, 999999, 0));
        super.potionEffects.add(new PotionEffect(PotionEffectType.SLOW_FALLING, 999999, 0));
    }


    /**
     * Activate the elytrier ability of throwing a bomb
     * @param e The event called when right-clicking with snow ball
     */
    @EventHandler
    public void throwBomb(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        ItemStack fist = p.getInventory().getItemInMainHand();

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(uuid)) {
            return;
        }

        if (Objects.equals(Kit.equippedKits.get(uuid).name, name)) {
            if (fist.getType().equals(Material.SNOWBALL)) {
                if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    e.setCancelled(true);
                    p.getInventory().getItemInMainHand().setAmount(0);
                    Item item = p.getWorld().spawn(p.getLocation(), Item.class);
                    item.setPickupDelay(99999);
                    item.setItemStack(new ItemStack(Material.SNOWBALL));

                    new BukkitRunnable() {
                        @Override
                        public void run() {

                            item.getWorld().createExplosion(item.getLocation(), 2F, false, false, p);

                        }
                    }.runTaskLater(Main.plugin, 100);

                }
            }
        }
    }

    /**
     * Activate the Elytrier ability of boosting itself a little forward
     * @param e The event called when right-clicking with rocket star
     */
    @EventHandler
    public void throwMagma(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        ItemStack fist = p.getInventory().getItemInMainHand();
        int cooldown = p.getCooldown(Material.FIREWORK_ROCKET);

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(uuid)) {
            return;
        }

        if (Objects.equals(Kit.equippedKits.get(uuid).name, name)) {
            if (fist.getType().equals(Material.FIREWORK_ROCKET)) {
                if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (cooldown == 0) {
                        p.setCooldown(Material.FIREWORK_ROCKET, 40);
                        p.setVelocity(p.getVelocity().multiply(3.5));
                        e.setCancelled(true);
                    } else if (!p.isGliding()){
                        Messenger.sendActionError(ChatColor.BOLD + "You can't launch yourself whilst not gliding!", p);
                    } else {
                        Messenger.sendActionError(ChatColor.BOLD + "You can't launch yourself forward yet!", p);
                    }
                }
            }
        }
    }
}
