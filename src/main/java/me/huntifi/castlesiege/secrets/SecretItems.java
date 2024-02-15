package me.huntifi.castlesiege.secrets;

import me.huntifi.castlesiege.events.EnderchestEvent;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.maps.MapController;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;

public class SecretItems implements Listener {

    public static final HashMap<Player, ArrayList<ItemStack>> secretItemHolder = new HashMap<>();

    public static final ArrayList<ItemStack> secretItems = new ArrayList<>();

    /**
     * Called when a map starts and spawns all items that are supposed to spawn on that map.
     */
    public static void spawnSecretItems() {
        secretItemHolder.clear();

        spawnSecretItem("HelmsDeep" , herugrim(),
                new Location(Bukkit.getWorld("HelmsDeep"), 983.903, 58, 986.954));

        spawnSecretItem("Thunderstone" , skyCookie(),
                new Location(Bukkit.getWorld("Thunderstone"), 233.50, 67, 78.50));

        spawnSecretItem("Skyhold" , skyholdKeyDoor(),
                new Location(Bukkit.getWorld("Skyhold"), 1658, 98, -5));

        spawnSecretItem("Skyhold" , skyholdKeyInquisitor(),
                new Location(Bukkit.getWorld("Skyhold"), 1601, 156, -124));

        spawnSecretItem("Skyhold" , skyholdShield(),
                new Location(Bukkit.getWorld("Skyhold"), 1617, 49, -51));
    }

    /**
     * This method is called in another method that's called when a map starts
     * @param mapName map name
     * @param item the item to spawn
     * @param loc location to spawn the item at
     */
    public static void spawnSecretItem(String mapName, ItemStack item, Location loc) {

        if (MapController.getCurrentMap().name.equalsIgnoreCase(mapName)) {
            Bukkit.getWorld(mapName).dropItem(loc.add(+0.5, +1, +0.5), item).setVelocity(new Vector(0, 0, 0));
        }
    }

    /**
     * When a player dies, they drop any secret items they hold on the ground.
     * @param e The event called when a player quits the game
     */
    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        dropSecretItems(e.getEntity());
    }

    /**
     * When a player quits, they drop any secret items they hold on the ground.
     * @param e The event called when a player quits the game
     */
    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        dropSecretItems(e.getPlayer());
    }

    /**
     * Drop any secret items the player holds on the ground
     * @param player The player for whom to drop the items
     */
    private void dropSecretItems(Player player) {
        if (!secretItemHolder.containsKey(player))
            return;

        for (ItemStack item : secretItemHolder.get(player)) {
            player.getInventory().remove(item);
            player.getWorld().dropItem(player.getLocation().add(0, 1, 0), item).setVelocity(new Vector(0, 0, 0));
            Messenger.broadcastSecret(ChatColor.YELLOW + item.getItemMeta().getDisplayName() + "§r has been dropped!");
        }

        secretItemHolder.remove(player);
    }

    /**
     * Register the holder of a secret item on pickup
     * @param event The event called when a player picks up an item
     */
    @EventHandler
    public void onPickup(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;

        Player player = (Player) event.getEntity();
        if (secretItems.contains(event.getItem().getItemStack())) {
            secretItemHolder.putIfAbsent(player, new ArrayList<>());
            secretItemHolder.get(player).add(event.getItem().getItemStack());
            Messenger.broadcastSecret(player.getName() + " has picked up " + event.getItem().getName());
        }
    }

    /**
     * Add every item to this, so it registers in the arraylist, then it will keep track of who holds the item.
     */
    public static void registerSecretItems() {

        secretItems.add(herugrim());
        secretItems.add(skyCookie());
        secretItems.add(skyholdKeyDoor());
        secretItems.add(skyholdKeyInquisitor());
        secretItems.add(skyholdShield());

    }

    /**
     * When a player clicks an enderchest, they shall be given back the secret items they had.
     * @param event The event called when an off-cooldown player interacts with an enderchest
     */
    @EventHandler
    public void onClickEnderchest(EnderchestEvent event) {
        Player player = event.getPlayer();
        if (!secretItemHolder.containsKey(player))
            return;
        for (ItemStack item : secretItemHolder.get(player))
            player.getInventory().addItem(item);
    }

                                                //Secret Items\\
    //-------------------------------------------------------------------------------------------------------\\

     public static ItemStack herugrim() {

         ItemStack sword = new ItemStack(Material.GOLDEN_SWORD);

         ItemMeta swordMeta = sword.getItemMeta();

         swordMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
         swordMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
         swordMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

         swordMeta.setUnbreakable(true);

         swordMeta.setDisplayName(ChatColor.DARK_PURPLE + "Herugrim");

         swordMeta.addEnchant(Enchantment.DAMAGE_ALL, 10, true);

         swordMeta.addEnchant(Enchantment.FIRE_ASPECT, 1, true);

         ArrayList<String> lore1 = new ArrayList<>();

         swordMeta.setLore(lore1);

         sword.setItemMeta(swordMeta);

        return sword;
     }

    public static ItemStack skyCookie() {

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

    public static ItemStack skyholdKeyInquisitor() {

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

    public static ItemStack skyholdKeyDoor() {

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

    public static ItemStack skyholdShield() {

        ItemStack shield = new ItemStack(Material.SHIELD);

        ItemMeta shieldMeta = shield.getItemMeta();

        shieldMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        shieldMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        shieldMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

        shieldMeta.setUnbreakable(true);

        shieldMeta.setDisplayName(ChatColor.GOLD + "Shield of Skyhold");

        shieldMeta.addEnchant(Enchantment.KNOCKBACK, 1, true);

        shieldMeta.addEnchant(Enchantment.THORNS, 10, true);

        ArrayList<String> lore1 = new ArrayList<>();

        shieldMeta.setLore(lore1);

        shield.setItemMeta(shieldMeta);

        return shield;
    }
}
