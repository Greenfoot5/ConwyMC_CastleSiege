package me.huntifi.castlesiege.secrets;

import me.huntifi.castlesiege.maps.MapController;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SecretItems implements Runnable, Listener {

    public static HashMap<Player, ItemStack> secretItemHolder = new HashMap<>();

    public static ArrayList<ItemStack> secretItems = new ArrayList<>();

    /**
     * Called when a map starts and spawns all items that are supposed to spawn on that map.
     */
    public static void spawnSecretItems() {
        spawnSecretItem("HelmsDeep" , herugrim(),
                new Location(Bukkit.getWorld("HelmsDeep"), 983.903, 58, 986.954));
    }

    /**
     * This method is called in another method that's called when a map starts
     * @param worldname map name
     * @param item the item to spawn
     * @param loc location to spawn the item at
     */
    public static void spawnSecretItem(String worldname, ItemStack item, Location loc) {

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




                                                //Secret Items\\
    //-------------------------------------------------------------------------------------------------------\\

     public static ItemStack herugrim() {

         ItemStack Sword = new ItemStack(Material.GOLDEN_SWORD);

         ItemMeta SwordMeta = Sword.getItemMeta();

         SwordMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

         SwordMeta.setUnbreakable(true);

         SwordMeta.setDisplayName(ChatColor.DARK_PURPLE + "Herugrim");

         SwordMeta.addEnchant(Enchantment.DAMAGE_ALL, 33, true);

         SwordMeta.addEnchant(Enchantment.FIRE_ASPECT, 1, true);

         ArrayList<String> lore1 = new ArrayList<String>();

         SwordMeta.setLore(lore1);

         Sword.setItemMeta(SwordMeta);

        return Sword;
     }

}
