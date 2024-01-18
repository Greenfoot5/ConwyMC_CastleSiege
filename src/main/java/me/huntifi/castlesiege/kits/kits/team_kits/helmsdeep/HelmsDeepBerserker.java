package me.huntifi.castlesiege.kits.kits.team_kits.helmsdeep;

import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.kits.kits.TeamKit;
import me.huntifi.castlesiege.maps.NameTag;
import me.huntifi.castlesiege.maps.TeamController;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;


public class HelmsDeepBerserker extends TeamKit implements Listener {


    public HelmsDeepBerserker() {
        super("Uruk Berserker", 220, 6, "Helm's Deep", "Uruk-hai",
                2500, Material.REDSTONE, "helmsdeepurukberserker");

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Weapon
        es.hotbar[0] = ItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                ChatColor.GREEN + "Cleaver", Collections.singletonList(ChatColor.AQUA + "Deals AOE damage but has a cooldown."),
                null, 50.0);
        // Voted Weapon
        es.votedWeapon = new Tuple<>(
                ItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                        ChatColor.GREEN + "Cleaver",
                        Arrays.asList(ChatColor.AQUA + "Deals AOE damage but has a cooldown.",
                                ChatColor.AQUA + "- voted: +2 damage"),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), 52.0),
                0);

        // Leggings
        es.legs = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_LEGGINGS),
                ChatColor.GREEN + "Leather Leggings", null, null,
                Color.fromRGB(95, 69, 6));

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

        super.potionEffects.add(new PotionEffect(PotionEffectType.SPEED, 999999, 0));

        super.equipment = es;

        super.canClimb = true;

        // Death Messages
        super.deathMessage[0] = "You were cut in half by ";
        super.killMessage[0] = " cut deeply into ";

    }

    /**
     * Activate the Uruk Berserker cleave ability
     * @param event The event called when hitting another player
     */
    @EventHandler (ignoreCancelled = true)
    public void onCleave(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            Player damager = (Player) event.getDamager();
            Player hit = (Player) event.getEntity();

            // Uruk Berserker tries to cleave every enemy player around them
            if (Objects.equals(Kit.equippedKits.get(damager.getUniqueId()).name, name) &&
                    damager.getInventory().getItemInMainHand().getType() == Material.IRON_SWORD &&
                    damager.getCooldown(Material.IRON_SWORD) == 0) {
                damager.setCooldown(Material.IRON_SWORD, 60);

                // Enemy blocks cleave
                if (hit.isBlocking()) {
                    hit.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                            ChatColor.AQUA + "You blocked " + NameTag.color(damager) + damager.getName() + ChatColor.AQUA + "'s cleave"));
                } else {
                    hit.getWorld().playSound(hit.getLocation(), Sound.ENTITY_PLAYER_BIG_FALL, 1, 1);
                    event.setDamage(event.getDamage() * 2);

                    if (hit.getVehicle() != null && hit.getVehicle() instanceof Horse) {
                        Horse horse = (Horse) hit.getVehicle();
                        horse.damage(event.getDamage() * 2, damager);
                    }

                    for (Player online : Bukkit.getOnlinePlayers()) {
                        if (hit.getWorld() != online.getWorld() || online == hit || online == damager)
                            continue;

                        if (online.getLocation().distance(damager.getLocation()) < 2.6
                                && TeamController.getTeam(online.getUniqueId())
                                        != TeamController.getTeam(damager.getUniqueId()))
                            online.damage(event.getDamage(), damager);
                    }
                }
            }
        }
    }

    @Override
    public ArrayList<String> getGuiDescription() {
        ArrayList<String> description = new ArrayList<>();
        description.add("§7//TODO - Add kit description");
        return description;
    }
}
