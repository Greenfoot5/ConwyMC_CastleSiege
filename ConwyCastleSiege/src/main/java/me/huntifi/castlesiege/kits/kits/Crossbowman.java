package me.huntifi.castlesiege.kits.kits;

import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Objects;

/**
 * The crossbowman kit
 */
public class Crossbowman extends Kit implements Listener, CommandExecutor {

    /**
     * Set the equipment and attributes of this kit
     */
    public Crossbowman() {
        super("Crossbowman", 105);

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Crossbow
        es.hotbar[0] = ItemCreator.item(new ItemStack(Material.CROSSBOW),
                ChatColor.GREEN + "Crossbow", null, null);

        // Chestplate
        es.chest = ItemCreator.item(new ItemStack(Material.IRON_CHESTPLATE),
                ChatColor.GREEN + "Iron Chestplate", null, null);

        // Leggings
        es.legs = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_LEGGINGS),
                ChatColor.GREEN + "Leather Leggings", null, null,
                Color.fromRGB(255, 255, 51));

        // Boots
        es.feet = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_BOOTS),
                ChatColor.GREEN + "Leather Boots", null, null,
                Color.fromRGB(255, 255, 51));
        // Voted boots
        es.votedFeet = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_BOOTS),
                ChatColor.GREEN + "Leather Boots",
                Collections.singletonList(ChatColor.AQUA + "- voted: Depth Strider +2"),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)),
                Color.fromRGB(255, 255, 51));

        // Ladders
        es.hotbar[1] = new ItemStack(Material.LADDER, 4);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, 6), 1);

        // Arrows
        es.hotbar[7] = new ItemStack(Material.ARROW, 32);

        super.equipment = es;

        // Perm Potion Effect
        super.potionEffects.add(new PotionEffect(PotionEffectType.SLOW, 999999, 1));

        // Death Messages
        super.projectileDeathMessage[0] = "Your skull was pierced by ";
        super.projectileDeathMessage[1] = "'s bolt";
        super.projectileKillMessage[0] = "You pierced ";
        super.projectileKillMessage[1] = "'s skull";
    }

    /**
     * Register the player as using this kit and set their items
     * @param commandSender Source of the command
     * @param command Command which was executed
     * @param s Alias of the command which was used
     * @param strings Passed command arguments
     * @return true
     */
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof ConsoleCommandSender) {
            commandSender.sendMessage("Console cannot select kits!");
            return true;
        }

        super.addPlayer(((Player) commandSender).getUniqueId());
        return true;
    }

    /**
     * Activate the crossbowman ability, shooting an arrow in the direction the player is looking at high speed
     * @param e The even called when shooting an arrow from a crossbow
     */
    @EventHandler
    public void shootCrossbow(EntityShootBowEvent e) {
        if (e.isCancelled()) {
            return;
        }

        Player p = (Player) e.getEntity();
        if (Objects.equals(Kit.equippedKits.get(p.getUniqueId()).name, name)) {
            e.setCancelled(true);
            p.setCooldown(Material.CROSSBOW, 55);

            Arrow a = p.launchProjectile(Arrow.class, p.getLocation().getDirection());
            a.setVelocity(a.getVelocity().normalize().multiply(35));
        }
    }
}
