package me.huntifi.castlesiege.secrets;

import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.maps.MapController;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.*;

public class SecretItems implements Runnable, Listener {

    public static HashMap<Player, ItemStack> secretItemHolder = new HashMap<>();

    public static ArrayList<ItemStack> secretItems = new ArrayList<>();

    /**
     * Called when a map starts and spawns all items that are supposed to spawn on that map.
     */
    public static void spawnSecretItems() {
        spawnSecretItem("HelmsDeep" , herugrim(),
                new Location(Bukkit.getWorld("HelmsDeep"), 983.903, 58, 986.954));

        spawnSecretItem("Thunderstone" , skycookie(),
                new Location(Bukkit.getWorld("Thunderstone"), 233.50, 67, 78.50));

        spawnSecretItem("Skyhold" , skyholdKeyDoor(),
                new Location(Bukkit.getWorld("Skyhold"), 1658, 98, -5));

        spawnSecretItem("Skyhold" , skyholdKeyInquisitor(),
                new Location(Bukkit.getWorld("Skyhold"), 1601, 156, -124));
    }

    /**
     * This method is called in another method that's called when a map starts
     * @param worldname map name
     * @param item the item to spawn
     * @param loc location to spawn the item at
     */
    public static void spawnSecretItem(String worldname, ItemStack item, Location loc) {

        secretItemHolder.clear();

        if (MapController.getCurrentMap().worldName.equalsIgnoreCase(worldname)) {

            Bukkit.getWorld(worldname).dropItem(loc.add(+0.5, +1, +0.5), item).setVelocity(new Vector(0, 0, 0));

        } else {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "map name for secret item: " + item.getType() + " is incorrect");
        }
    }

    @EventHandler
    public static void dropSecretItem(PlayerDeathEvent e) {

            Player p = e.getEntity();

            if (secretItemHolder.containsKey(p)) {

               for (Map.Entry entry : secretItemHolder.entrySet()) {

                   if (entry.getKey().equals(p)) {

                       p.getInventory().remove(secretItemHolder.get(p));

                       p.getWorld().dropItem(p.getLocation().add(+0.5, +1, +0.5), secretItemHolder.get(p)).setVelocity(new Vector(0, 0, 0));

                       secretItemHolder.remove(p);
                   }
               }
            }
       }


    /**
     *
     * @param e when a player quits it drops the secret item on the ground.
     */

    @EventHandler
    public static void dropSecretItemOnQuit(PlayerQuitEvent e) {

        Player p = e.getPlayer();

        if (secretItemHolder.containsKey(p)) {

            for (Map.Entry entry : secretItemHolder.entrySet()) {

                if (entry.getKey().equals(p)) {

                    p.getInventory().remove(secretItemHolder.get(p));

                    p.getWorld().dropItem(p.getLocation().add(+0.5, +1, +0.5), secretItemHolder.get(p)).setVelocity(new Vector(0, 0, 0));

                    secretItemHolder.remove(p);
                }
            }
        }
    }

    /**
     * Checks if you hold any of the secret items in your inventory
     */
    @Override
    public void run() {

            for (Player p : Bukkit.getOnlinePlayers()) {

                for (ItemStack secret : secretItems) {

                    if (p.getInventory().contains(secret)) {

                        secretItemHolder.put(p, secret);

                    }
                }
            }
    }

    /**
     * Add every item to this so it registers in the arraylist, then it will keep track of who holds the item.
     */
    public static void registerSecretItems() {

        secretItems.add(herugrim());
        secretItems.add(skycookie());
        secretItems.add(skyholdKeyDoor());
        secretItems.add(skyholdKeyInquisitor());

    }

    /**
     *
     * @param p the player to check
     * @return true if the player has a secret item, false when they don't.
     */
    public static boolean playerHasSecret(Player p) {
        if (secretItemHolder.containsKey(p)) {
            return true;
        }
        return false;
    }

    /**
     *
     * @param p when this player clicks an enderchest it shall be given back the secret item it had.
     * This method is called in the enderchest class.
     */
    public static void giveSecretOnEnderchest(Player p) {
        if (playerHasSecret(p)) {
            p.getInventory().addItem(secretItemHolder.get(p));
        }
    }


    private static ItemStack setDamage(ItemStack item, double damage) {
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE,
                new AttributeModifier(UUID.randomUUID(), "SetHandDamage", damage, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE,
                new AttributeModifier(UUID.randomUUID(), "SetOffHandDamage", damage, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.OFF_HAND));
        item.setItemMeta(meta);
        return item;
    }

                                                //Secret Items\\
    //-------------------------------------------------------------------------------------------------------\\

     final public static ItemStack herugrim() {

         ItemStack sword = new ItemStack(Material.GOLDEN_SWORD);

         ItemMeta swordMeta = sword.getItemMeta();

         swordMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
         swordMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
         swordMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

         swordMeta.setUnbreakable(true);

         swordMeta.setDisplayName(ChatColor.DARK_PURPLE + "Herugrim");

         swordMeta.addEnchant(Enchantment.DAMAGE_ALL, 50, true);

         swordMeta.addEnchant(Enchantment.FIRE_ASPECT, 1, true);

         ArrayList<String> lore1 = new ArrayList<>();

         swordMeta.setLore(lore1);

         sword.setItemMeta(swordMeta);

        return sword;
     }

    final public static ItemStack skycookie() {

        ItemStack cookie = new ItemStack(Material.COOKIE);

        ItemMeta cookieMeta = cookie.getItemMeta();

        cookieMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        cookieMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        cookieMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

        cookieMeta.setUnbreakable(true);

        cookieMeta.setDisplayName(ChatColor.GOLD + "Sky Cookie");

        cookieMeta.addEnchant(Enchantment.DAMAGE_ALL, 40, true);

        cookieMeta.addEnchant(Enchantment.KNOCKBACK, 1, true);

        ArrayList<String> lore1 = new ArrayList<>();

        cookieMeta.setLore(lore1);

        cookie.setItemMeta(cookieMeta);

        return cookie;
    }

    final public static ItemStack skyholdKeyInquisitor() {

        ItemStack vaultKey = new ItemStack(Material.GOLDEN_HOE);

        ItemMeta vaultKeyMeta = vaultKey.getItemMeta();

        vaultKeyMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        vaultKeyMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        vaultKeyMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

        vaultKeyMeta.setUnbreakable(true);

        vaultKeyMeta.setDisplayName(ChatColor.GOLD + "Vault Key");

        vaultKeyMeta.addEnchant(Enchantment.KNOCKBACK, 1, true);

        ArrayList<String> lore1 = new ArrayList<>();

        vaultKeyMeta.setLore(lore1);

        vaultKey.setItemMeta(vaultKeyMeta);

        return vaultKey;
    }

    final public static ItemStack skyholdKeyDoor() {

        ItemStack vaultKey = new ItemStack(Material.IRON_HOE);

        ItemMeta vaultKeyMeta = vaultKey.getItemMeta();

        vaultKeyMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        vaultKeyMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        vaultKeyMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

        vaultKeyMeta.setUnbreakable(true);

        vaultKeyMeta.setDisplayName(ChatColor.GOLD + "Door Key");

        vaultKeyMeta.addEnchant(Enchantment.KNOCKBACK, 1, true);

        ArrayList<String> lore1 = new ArrayList<>();

        vaultKeyMeta.setLore(lore1);

        vaultKey.setItemMeta(vaultKeyMeta);

        return vaultKey;
    }



}
