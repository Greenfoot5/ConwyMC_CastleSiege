package me.huntifi.castlesiege.kits.kits.team_kits.helmsdeep;

import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.events.gameplay.HorseHandler;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.kits.kits.TeamKit;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

public class HelmsDeepLancer extends TeamKit implements Listener {

    public HelmsDeepLancer() {
        super("Lancer", 270, 9, "Helm's Deep", "Rohan", 5000,
                Material.STICK, "helmsdeeplancer");

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Weapon
        es.hotbar[0] = ItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                ChatColor.GREEN + "Sword", null, null, 35.5);
        // Voted Weapon
        es.votedWeapon = new Tuple<>(
                ItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                        ChatColor.GREEN + "Sword",
                        Collections.singletonList(ChatColor.AQUA + "- voted: +2 damage"),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), 37.5),
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
                1);

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
                Collections.singletonList(ChatColor.AQUA + "- voted: Depth Strider II"),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)));

        // Ladders
        es.hotbar[2] = new ItemStack(Material.LADDER, 4);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, 6), 2);

        // Horse
        es.hotbar[3] = ItemCreator.item(new ItemStack(Material.WHEAT),
                ChatColor.GREEN + "Spawn Horse", null, null);
        HorseHandler.add(name, 600, 125, 1, 0.2425, 0.8,
                Material.GOLDEN_HORSE_ARMOR, Arrays.asList(
                        new PotionEffect(PotionEffectType.JUMP, 999999, 1),
                        new PotionEffect(PotionEffectType.REGENERATION, 999999, 0),
                        new PotionEffect(PotionEffectType.SPEED, 999999, 0)
                )
        );

        super.equipment = es;

        // Death Messages
        super.projectileDeathMessage[0] = "You were impaled by ";
        super.projectileKillMessage[0] = " impaled ";
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
                if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (cooldown == 0) {
                        p.setCooldown(Material.STICK, 200);
                        stick.setAmount(stick.getAmount() - 1);
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                                ChatColor.AQUA + "You threw your spear!"));
                        p.launchProjectile(Arrow.class).setVelocity(p.getLocation().getDirection().multiply(2.5));

                    } else {
                        Messenger.sendActionError(ChatColor.BOLD + "You can't throw your spear yet.", p);
                    }
                }
            }
        }
    }


    /**
     * Set the thrown spear's damage
     * @param e The event called when an arrow hits a player
     */
    @EventHandler(priority = EventPriority.LOW)
    public void changeSpearDamage(ProjectileHitEvent e) {
        if (e.getEntity() instanceof Arrow) {
            Arrow arrow = (Arrow) e.getEntity();

            if (arrow.getShooter() instanceof Player) {
                Player damages = (Player) arrow.getShooter();

                if (Objects.equals(Kit.equippedKits.get(damages.getUniqueId()).name, name)) {
                    if (damages.isInsideVehicle()) {
                        arrow.setDamage(80);
                    } else {
                        arrow.setDamage(50);
                    }
                }
            }
        }
    }
}
