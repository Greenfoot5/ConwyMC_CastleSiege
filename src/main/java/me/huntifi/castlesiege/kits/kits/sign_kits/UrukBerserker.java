package me.huntifi.castlesiege.kits.kits.sign_kits;

import me.huntifi.castlesiege.kits.items.CSItemCreator;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.kits.kits.SignKit;
import me.huntifi.castlesiege.maps.TeamController;
import me.huntifi.castlesiege.misc.CSNameTag;
import me.huntifi.conwymc.data_types.Tuple;
import me.huntifi.conwymc.util.Messenger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
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


public class UrukBerserker extends SignKit implements Listener {


    /**
     * Creates a new Helms Deep Berserker
     */
    public UrukBerserker() {
        super("Uruk Berserker", 220, 6, Material.REDSTONE, 2500);

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();

        // Weapon
        es.hotbar[0] = CSItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                Component.text("Cleaver", NamedTextColor.GREEN),
                Collections.singletonList(Component.text("Deals AOE damage but has a cooldown.", NamedTextColor.AQUA)),
                null, 50.0);
        // Voted Weapon
        es.votedWeapon = new Tuple<>(
                CSItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                        Component.text("Cleaver", NamedTextColor.GREEN),
                        Arrays.asList(Component.text("Deals AOE damage but has a cooldown.", NamedTextColor.AQUA),
                                Component.text("- voted: +2 damage", NamedTextColor.AQUA)),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), 52.0),
                0);

        // Leggings
        es.legs = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_LEGGINGS),
                Component.text("Leather Leggings", NamedTextColor.GREEN), null, null,
                Color.fromRGB(95, 69, 6));

        // Boots
        es.feet = CSItemCreator.item(new ItemStack(Material.IRON_BOOTS),
                Component.text("Iron Boots", NamedTextColor.GREEN), null, null);
        // Voted Boots
        es.votedFeet = CSItemCreator.item(new ItemStack(Material.IRON_BOOTS),
                Component.text("Iron Boots", NamedTextColor.GREEN),
                Collections.singletonList(Component.text("- voted: Depth Strider II", NamedTextColor.AQUA)),
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
    @EventHandler(ignoreCancelled = true)
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
                    Messenger.sendActionInfo("You blocked " + CSNameTag.mmUsername(damager) + "'s cleave", hit);
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
    public ArrayList<Component> getGuiDescription() {
        ArrayList<Component> description = new ArrayList<>();
        description.add(Component.text("//TODO - Add kit description", NamedTextColor.GRAY));
        return description;
    }
}
