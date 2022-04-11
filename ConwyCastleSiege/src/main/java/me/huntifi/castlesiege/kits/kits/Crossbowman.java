package me.huntifi.castlesiege.kits.kits;

import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.kits.EquipmentSet;
import me.huntifi.castlesiege.kits.Kit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

public class Crossbowman extends Kit implements Listener, CommandExecutor {

    public Crossbowman() {
        super("Crossbowman");
        super.baseHeath = 105;


        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Crossbow
        es.hotbar[0] = createItem(new ItemStack(Material.CROSSBOW),
                ChatColor.GREEN + "Crossbow", null, null);

        // Chestplate
        es.chest = createItem(new ItemStack(Material.IRON_CHESTPLATE),
                ChatColor.GREEN + "Iron Chestplate", null, null);

        // Leggings
        es.legs = createLeatherItem(new ItemStack(Material.LEATHER_LEGGINGS),
                ChatColor.GREEN + "Leather Leggings", null, null,
                Color.fromRGB(255, 255, 51));

        // Boots
        es.feet = createLeatherItem(new ItemStack(Material.LEATHER_BOOTS),
                ChatColor.GREEN + "Leather Boots", null, null,
                Color.fromRGB(255, 255, 51));
        // Voted boots
        es.votedFeet = createLeatherItem(new ItemStack(Material.LEATHER_BOOTS),
                ChatColor.GREEN + "Leather Boots",
                Arrays.asList("", ChatColor.AQUA + "- voted: Depth Strider 2"),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)),
                Color.fromRGB(255, 255, 51));

        // Ladders
        es.hotbar[1] = new ItemStack(Material.LADDER, 4);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, 6), 1);

        // Arrows
        es.hotbar[7] = new ItemStack(Material.ARROW, 32);

        super.equipment = es;


        // Death Messages
        super.projectileDeathMessage[0] = "Your skull was pierced by ";
        super.projectileDeathMessage[1] = "'s bolt";
        super.projectileKillMessage[0] = "You pierced ";
        super.projectileKillMessage[1] = "'s skull";
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        super.addPlayer(((Player) commandSender).getUniqueId());
        return true;
    }

    @EventHandler
    public void shootCrossbow(EntityShootBowEvent e) {
        Player p = (Player) e.getEntity();

        if (Objects.equals(Kit.equippedKits.get(p.getUniqueId()).name, name)) {
            if (p.getCooldown(Material.CROSSBOW) == 0) {

                e.setCancelled(true);
                p.setCooldown(Material.CROSSBOW, 75);

                Arrow arrow= p.launchProjectile(Arrow.class);
                arrow.setShooter(p);
                arrow.setVelocity(new Vector(p.getLocation().getDirection().getX(),
                        p.getLocation().getDirection().getY(),
                        p.getLocation().getDirection().getZ()).normalize().multiply(35));
            }
        }
    }
}
