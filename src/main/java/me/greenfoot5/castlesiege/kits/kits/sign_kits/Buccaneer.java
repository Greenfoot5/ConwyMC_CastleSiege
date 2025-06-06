package me.greenfoot5.castlesiege.kits.kits.sign_kits;

import me.greenfoot5.castlesiege.events.combat.InCombat;
import me.greenfoot5.castlesiege.kits.items.CSItemCreator;
import me.greenfoot5.castlesiege.kits.items.EquipmentSet;
import me.greenfoot5.castlesiege.kits.kits.Kit;
import me.greenfoot5.castlesiege.kits.kits.SignKit;
import me.greenfoot5.conwymc.data_types.Tuple;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

public class Buccaneer extends SignKit {

    private static final int health = 360;
    private static final double regen = 10.5;
    private static final double meleeDamage = 40.5;
    private static final int ladderCount = 4;

    public Buccaneer() {
        super("Buccaneer", health, regen, Material.OAK_BOAT);

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();

        // Weapon
        es.hotbar[0] = CSItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                Component.text("Buccaneer's Sword", NamedTextColor.GREEN), null, null, meleeDamage);
        // Voted Weapon
        es.votedWeapon = new Tuple<>(
                CSItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                        Component.text("Buccaneer's Sword", NamedTextColor.GREEN),
                        Collections.singletonList(Component.text("⁎ Voted: +2 damage", NamedTextColor.AQUA)),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOTING, 0)), meleeDamage + 2),
                0);

        // Chestplate
        es.chest = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE),
                Component.text("Buccaneer's Robe", NamedTextColor.GREEN), null, null,
                Color.fromRGB(153, 76, 0));
        ItemMeta chest = es.chest.getItemMeta();
        ArmorMeta chestMeta = (ArmorMeta) chest;
        assert chest != null;
        ArmorTrim chestTrim = new ArmorTrim(TrimMaterial.COPPER, TrimPattern.TIDE);
        ((ArmorMeta) chest).setTrim(chestTrim);
        es.chest.setItemMeta(chestMeta);

        // Leggings
        es.legs = CSItemCreator.item(new ItemStack(Material.CHAINMAIL_LEGGINGS),
                Component.text("Buccaneer's Leggings", NamedTextColor.GREEN), null, null);

        // Boots
        es.feet = CSItemCreator.item(new ItemStack(Material.NETHERITE_BOOTS),
                Component.text("Buccaneer's Boots", NamedTextColor.GREEN), null, null);
        // Voted Boots
        es.votedFeet = CSItemCreator.item(new ItemStack(Material.NETHERITE_BOOTS),
                Component.text("Buccaneer's Boots", NamedTextColor.GREEN),
                Collections.singletonList(Component.text("⁎ Voted: Depth Strider II", NamedTextColor.AQUA)),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)));

        // Ladders
        es.hotbar[1] = new ItemStack(Material.OAK_BOAT);
        es.hotbar[2] = new ItemStack(Material.LADDER, ladderCount);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, ladderCount + 2), 2);

        super.equipment = es;
    }


    @EventHandler
    public void onPlaceBoat(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();

        if (Objects.equals(Kit.equippedKits.get(uuid).name, name)) {

            // Prevent using in lobby
            if (InCombat.isPlayerInLobby(uuid)) {
                e.setCancelled(true);
                return;
            }

            if (p.getInventory().getItemInMainHand().getType() == Material.OAK_BOAT
                    || p.getInventory().getItemInOffHand().getType() == Material.OAK_BOAT) {
                if (e.getAction() == Action.RIGHT_CLICK_BLOCK && Objects.requireNonNull(e.getClickedBlock()).getType() == Material.WATER) {
                    e.setCancelled(false);
                }
            }
        }
    }

    @EventHandler
    public void onKillBoat(VehicleDestroyEvent e) {
        if (!(e.getAttacker() instanceof Player p)) {
            return;
        }
        UUID uuid = p.getUniqueId();

            // Prevent using in lobby
            if (InCombat.isPlayerInLobby(uuid)) {
                return;
            }
            //no boat? no action!
            if (e.getVehicle().getType() != EntityType.OAK_BOAT) {
                return;
            }

            if (Objects.equals(Kit.equippedKits.get(uuid).name, name)) {
                p.getInventory().addItem(new ItemStack(Material.OAK_BOAT));
                e.getVehicle().remove();
            } else {
                e.getVehicle().remove();
            }
    }
    @EventHandler
        public void onSpawnBoatItem(EntityDropItemEvent e) {
        // Get the entity that dropped the item
        Entity entity = e.getEntity();

        // Check if the entity is a boat and the dropped item is a boat
        if (entity.getType() == EntityType.OAK_BOAT) {
            Item droppedItem = e.getItemDrop();
            e.setCancelled(true);
        }
    }

    @Override
    public ArrayList<Component> getGuiDescription() {
        return null;
    }
}
