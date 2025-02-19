package me.greenfoot5.castlesiege.secrets;

import me.greenfoot5.castlesiege.events.EnderchestEvent;
import me.greenfoot5.castlesiege.maps.MapController;
import me.greenfoot5.conwymc.util.Messenger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Handles secret item drops &amp; pickups for the server
 */
public class SecretItems implements Listener {

    public static final HashMap<Player, ArrayList<ItemStack>> secretItemHolder = new HashMap<>();

    public static final HashSet<ItemStack> secretItems = new HashSet<>();

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

        spawnSecretItem("Halls Of Herakles" , heraklesApple(),
                new Location(Bukkit.getWorld("HallOfHerakles"), -232, 38, -497));
    }

    /**
     * This method is called in another method that's called when a map starts
     * @param mapName map name
     * @param item the item to spawn
     * @param loc location to spawn the item at
     */
    public static void spawnSecretItem(String mapName, ItemStack item, Location loc) {
        if (MapController.getCurrentMap().name.equalsIgnoreCase(mapName)) {
            loc.getWorld().dropItem(loc.add(+0.5, +1, +0.5), item).setVelocity(new Vector(0, 0, 0));
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
            Messenger.broadcastSecret(Component.empty()
                    .append(item.getItemMeta().displayName())
                    .append(Component.text(" has been dropped!")));
        }

        secretItemHolder.remove(player);
    }

    /**
     * Register the holder of a secret item on pickup
     * @param event The event called when a player picks up an item
     */
    @EventHandler
    public void onPickup(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player player))
            return;

        if (secretItems.contains(event.getItem().getItemStack())) {
            secretItemHolder.putIfAbsent(player, new ArrayList<>());
            secretItemHolder.get(player).add(event.getItem().getItemStack());
            Messenger.broadcastSecret(Component.empty()
                    .append(player.name())
                    .append(Component.text(" has picked up "))
                    .append(event.getItem().getItemStack().getItemMeta().displayName()));
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
        secretItems.add(heraklesApple());
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


    /**
     * Prevent eating Hera's Golden Apple
     * @param event Called when a player consumes an item
     */
    @EventHandler
    public void onEatHeraApple(PlayerItemConsumeEvent event) {
        if (event.getItem().getType() == Material.ENCHANTED_GOLDEN_APPLE) {
            event.setCancelled(true);
        }
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

         swordMeta.displayName(Component.text("Herugrim").color(NamedTextColor.DARK_PURPLE));

         swordMeta.addEnchant(Enchantment.SHARPNESS, 10, true);

         swordMeta.addEnchant(Enchantment.FIRE_ASPECT, 1, true);

         ArrayList<Component> lore = new ArrayList<>();

         swordMeta.lore(lore);

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

        cookieMeta.displayName(Component.text("Sky Cookie").color(NamedTextColor.GOLD));

        cookieMeta.addEnchant(Enchantment.SHARPNESS, 40, true);

        cookieMeta.addEnchant(Enchantment.KNOCKBACK, 1, true);

        ArrayList<Component> lore = new ArrayList<>();

        cookieMeta.lore(lore);

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

        vaultKeyMeta.displayName(Component.text("Vault Key").color(NamedTextColor.GOLD));

        vaultKeyMeta.addEnchant(Enchantment.KNOCKBACK, 1, true);

        ArrayList<Component> lore = new ArrayList<>();

        vaultKeyMeta.lore(lore);

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

        vaultKeyMeta.displayName(Component.text("Door Key").color(NamedTextColor.GOLD));

        vaultKeyMeta.addEnchant(Enchantment.KNOCKBACK, 1, true);

        ArrayList<Component> lore = new ArrayList<>();

        vaultKeyMeta.lore(lore);

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

        shieldMeta.displayName(Component.text("Shield of Skyhold").color(NamedTextColor.GOLD));

        shieldMeta.addEnchant(Enchantment.KNOCKBACK, 1, true);

        shieldMeta.addEnchant(Enchantment.THORNS, 10, true);

        ArrayList<Component> lore = new ArrayList<>();

        shieldMeta.lore(lore);

        shield.setItemMeta(shieldMeta);

        return shield;
    }

    public static ItemStack heraklesApple() {

        ItemStack apple = new ItemStack(Material.ENCHANTED_GOLDEN_APPLE);

        ItemMeta appleMeta = apple.getItemMeta();

        appleMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        appleMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

        appleMeta.setUnbreakable(true);

        appleMeta.displayName(Component.text("Hera's Golden Apple").color(NamedTextColor.GOLD));

        List<Component> lore = List.of(
                Component.text("An apple of Hera's sacred tree, given to her as a gift from Zeus").color(NamedTextColor.GRAY),
                Component.text("§7The tree grows apples made entirely of gold.").color(NamedTextColor.GRAY),
                Component.text(""),
                Component.text("The dragon Ladon was sent to guard it from anyone who might").color(NamedTextColor.GRAY),
                Component.text("try to steal the apples.").color(NamedTextColor.GRAY));

        appleMeta.lore(lore);

        apple.setItemMeta(appleMeta);

        return apple;
    }
}
