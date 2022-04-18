package me.huntifi.castlesiege.kits.kits;

import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.kits.EquipmentSet;
import me.huntifi.castlesiege.kits.ItemCreator;
import me.huntifi.castlesiege.kits.Kit;
import me.huntifi.castlesiege.maps.MapController;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Objects;

public class Viking extends Kit implements Listener, CommandExecutor {

    public Viking() {
        super("Viking", 110);

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Weapon
        es.hotbar[0] = ItemCreator.item(new ItemStack(Material.IRON_AXE),
                ChatColor.GREEN + "Giant Battle Axe", null,
                Collections.singletonList(new Tuple<>(Enchantment.DAMAGE_ALL, 20)));
        // Voted Weapon
        es.votedWeapon = new Tuple<>(
                ItemCreator.item(new ItemStack(Material.IRON_AXE),
                        ChatColor.GREEN + "Giant Battle Axe",
                        Collections.singletonList(ChatColor.AQUA + "- voted: +2 damage"),
                        Collections.singletonList(new Tuple<>(Enchantment.DAMAGE_ALL, 22))),
                0);

        // Chestplate
        es.chest = ItemCreator.item(new ItemStack(Material.IRON_CHESTPLATE),
                ChatColor.GREEN + "Iron Chestplate", null, null);

        // Leggings
        es.legs = ItemCreator.item(new ItemStack(Material.CHAINMAIL_LEGGINGS),
                ChatColor.GREEN + "Chainmail Leggings", null, null);

        // Boots
        es.feet = ItemCreator.item(new ItemStack(Material.CHAINMAIL_BOOTS),
                ChatColor.GREEN + "Chainmail Boots", null, null);
        // Voted Boots
        es.votedFeet = ItemCreator.item(new ItemStack(Material.CHAINMAIL_BOOTS),
                ChatColor.GREEN + "Chainmail Boots",
                Collections.singletonList(ChatColor.AQUA + "- voted: Depth Strider +2"),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)));

        // Ladders
        es.hotbar[1] = new ItemStack(Material.LADDER, 4);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, 6), 1);

        super.equipment = es;

        // Perm Potion Effects
        super.potionEffects.add(new PotionEffect(PotionEffectType.SLOW, 999999, 0));
        super.potionEffects.add(new PotionEffect(PotionEffectType.SLOW_DIGGING, 999999, 0));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        super.addPlayer(((Player) commandSender).getUniqueId());
        return true;
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player &&
                Objects.equals(Kit.equippedKits.get(e.getDamager().getUniqueId()).name, name) &&
                MapController.getCurrentMap().getTeam(e.getEntity().getUniqueId())
                != MapController.getCurrentMap().getTeam(e.getDamager().getUniqueId())) {
            Player p = (Player) e.getEntity();
            AttributeInstance armor = p.getAttribute(Attribute.GENERIC_ARMOR);
            assert armor != null;
            double armorValue = armor.getValue();

            // Remove player's armor if present and deal intended damage
            // Note: resistance effect still reduces damage taken
            if (armorValue == 0) {
                armor.setBaseValue(0);
            } else {
                e.setCancelled(true);
                armor.setBaseValue(-armorValue);
                p.damage(e.getDamage(), e.getDamager());
            }
        }
    }
}
