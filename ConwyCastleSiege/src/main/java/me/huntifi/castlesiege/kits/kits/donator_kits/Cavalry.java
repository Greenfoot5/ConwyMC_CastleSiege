package me.huntifi.castlesiege.kits.kits.donator_kits;

import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.events.death.DeathEvent;
import me.huntifi.castlesiege.events.gameplay.HorseHandler;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.kits.kits.DonatorKit;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.NameTag;
import me.huntifi.castlesiege.maps.TeamController;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

/**
 * The cavalry kit
 */
public class Cavalry extends DonatorKit implements Listener {

    /**
     * Set the equipment and attributes of this kit
     */
    public Cavalry() {
        super("Cavalry", 270, 9, 10000, 10);

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Weapon
        es.hotbar[0] = ItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                ChatColor.GREEN + "Sabre", null, null, 43);
        // Voted Weapon
        es.votedWeapon = new Tuple<>(
                ItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                        ChatColor.GREEN + "Sabre",
                        Collections.singletonList(ChatColor.AQUA + "- voted: +2 damage"),
                        Collections.singletonList(new Tuple<>(Enchantment.SWEEPING_EDGE, 0)), 45),
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
                Collections.singletonList(ChatColor.AQUA + "- voted: Depth Strider II"),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)));

        // Ladders
        es.hotbar[1] = new ItemStack(Material.LADDER, 4);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, 6), 1);

        // Horse
        es.hotbar[2] = ItemCreator.item(new ItemStack(Material.WHEAT),
                ChatColor.GREEN + "Spawn Horse", null, null);
        HorseHandler.add(name, 600, 150, 1, 0.2425, 0.8,
                Material.IRON_HORSE_ARMOR, Arrays.asList(
                        new PotionEffect(PotionEffectType.JUMP, 999999, 1),
                        new PotionEffect(PotionEffectType.REGENERATION, 999999, 0),
                        new PotionEffect(PotionEffectType.SPEED, 999999, 0)
                )
        );

        // stomp
        es.hotbar[3] = ItemCreator.weapon(new ItemStack(Material.ANVIL),
                ChatColor.GREEN + "Horse Kick", null, null, 0);

        super.equipment = es;
    }


    /**
     * Activate the Cavalry stomp ability
     * @param e The event called when hitting another player
     */
    @EventHandler
    public void onStomp(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        ItemStack stomp = p.getInventory().getItemInMainHand();

        if (Objects.equals(Kit.equippedKits.get(uuid).name, name)) {

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(uuid)) {
            return;
        }
            if (stomp.getType().equals(Material.ANVIL)) {

                //prevent from using it when not on a horse
                if (p.getVehicle() == null) {
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                            TextComponent.fromLegacyText(ChatColor.DARK_RED + "You can't use this when not on your horse."));
                    return;
                }

                if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {

                    if (p.getCooldown(Material.ANVIL) != 0) {
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                                ChatColor.DARK_RED + "Your horse's ability to stomp is still recharging!"));
                        return;
                    }

                    boolean hasEnemyInRange = false;
                    for (Player all : Bukkit.getOnlinePlayers()) {

                        //if the player is not in the same world ignore them.
                        if (p.getWorld() != all.getWorld())
                            continue;

                        //the player executing the ability should have enemy players in range.
                        if (p.getLocation().distance(all.getLocation()) <= 2.3 &&
                                TeamController.getTeam(all.getUniqueId())
                                != TeamController.getTeam(p.getUniqueId())) {

                            hasEnemyInRange = true;

                            // The stomp can be blocked using a shield
                            if (all.isBlocking()) {
                                all.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                                        ChatColor.AQUA + "You blocked " + NameTag.color(p) + p.getName() + ChatColor.AQUA + "'s horse stomp"));
                            } else {
                                all.addPotionEffect((new PotionEffect(PotionEffectType.CONFUSION, 80, 4)));
                                all.addPotionEffect((new PotionEffect(PotionEffectType.SLOW, 80, 1)));
                                all.addPotionEffect((new PotionEffect(PotionEffectType.SLOW_DIGGING, 80, 3)));
                                all.damage(70, p);
                            }
                        }

                        if (hasEnemyInRange) {
                            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_HORSE_ANGRY, 1, (float) 0.8);
                            p.setCooldown(Material.ANVIL, 360);
                        } else {
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                                    ChatColor.DARK_RED + "No enemy players are close enough for you to perform this ability!"));
                        }
                    }
                }
            }
        }
    }
}
