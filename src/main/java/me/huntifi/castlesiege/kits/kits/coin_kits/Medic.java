package me.huntifi.castlesiege.kits.kits.coin_kits;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.database.UpdateStats;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.kits.kits.CoinKit;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.maps.NameTag;
import me.huntifi.castlesiege.maps.TeamController;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Cake;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * The medic kit
 */
public class Medic extends CoinKit implements Listener {

    private static final int health = 210;
    private static final double regen = 15;
    private static final double meleeDamage = 30;
    private static final int ladderCount = 4;
    private static final int cakeCount = 16;

    public static final HashMap<Player, Block> cakes = new HashMap<>();
    public static final ArrayList<Player> cooldown = new ArrayList<>();

    /**
     * Set the equipment and attributes of this kit
     */
    public Medic() {
        super("Medic", health, regen, Material.CAKE);
        super.canSeeHealth = true;

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Weapon
        es.hotbar[0] = ItemCreator.weapon(new ItemStack(Material.WOODEN_SWORD),
                Component.text("Scalpel", NamedTextColor.GREEN), null, null, meleeDamage);
        // Voted Weapon
        es.votedWeapon = new Tuple<>(
                ItemCreator.weapon(new ItemStack(Material.WOODEN_SWORD),
                        Component.text("Scalpel", NamedTextColor.GREEN),
                        Collections.singletonList(Component.text("- voted: +2 damage", NamedTextColor.AQUA)),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), meleeDamage),
                0);

        // Chestplate
        es.chest = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE),
                Component.text("Leather Chestplate", NamedTextColor.GREEN), null, null,
                Color.fromRGB(255, 255, 255));

        // Leggings
        es.legs = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_LEGGINGS),
                Component.text("Leather Leggings", NamedTextColor.GREEN), null, null,
                Color.fromRGB(255, 255, 255));

        // Boots
        es.feet = ItemCreator.item(new ItemStack(Material.GOLDEN_BOOTS),
                Component.text("Golden Boots", NamedTextColor.GREEN), null, null);
        // Voted Boots
        es.votedFeet = ItemCreator.item(new ItemStack(Material.GOLDEN_BOOTS),
                Component.text("Golden Boots", NamedTextColor.GREEN),
                Collections.singletonList(Component.text("- voted: Depth Strider II", NamedTextColor.AQUA)),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)));

        // Bandages
        es.hotbar[1] = ItemCreator.item(new ItemStack(Material.PAPER),
                Component.text("Bandages", NamedTextColor.DARK_AQUA),
                Collections.singletonList(Component.text("Right click teammates to heal.", NamedTextColor.AQUA)), null);

        // Cake
        es.hotbar[2] = ItemCreator.item(new ItemStack(Material.CAKE, cakeCount),
                Component.text("Healing Cake", NamedTextColor.DARK_AQUA),
                List.of(Component.text("Place the cake down, then", NamedTextColor.AQUA),
                        Component.text("teammates can heal from it.", NamedTextColor.AQUA)), null);

        // Self Potion
        es.hotbar[3] = healthPotion();

        // Ladders
        es.hotbar[4] = new ItemStack(Material.LADDER, ladderCount);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, ladderCount + 2), 4);

        super.equipment = es;

        // Perm Potion Effect
        super.potionEffects.add(new PotionEffect(PotionEffectType.SPEED, 999999, 0));

        super.killMessage[0] = " dissected ";
        super.deathMessage[0] = "You had your insides examined by ";
    }

    public ItemStack healthPotion() {
        ItemStack itemStack = new ItemStack(Material.POTION);
        PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
        assert potionMeta != null;

        potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.HEAL, 1, 6), true);
        potionMeta.setColor(Color.RED);
        potionMeta.displayName(Component.text("Health Potion", NamedTextColor.RED));
        itemStack.setItemMeta(potionMeta);

        return itemStack;
    }

    /**
     * Place a cake
     * @param event The event called when placing a cake
     */
    @EventHandler (priority = EventPriority.HIGHEST)
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(player.getUniqueId())) {
            return;
        }

        if (Objects.equals(Kit.equippedKits.get(player.getUniqueId()).name, name) &&
                event.getBlockPlaced().getType() == Material.CAKE) {

            // Check you aren't placing on a cake
            if (event.getBlockAgainst() instanceof Cake) {
                event.setCancelled(true);
                return;
            }

            event.setCancelled(false);
            destroyCake(player);
            cakes.put(player, event.getBlockPlaced());
        }
    }

    /**
     * Break a cake
     * @param e The event called when breaking a cake
     */
    @EventHandler (priority = EventPriority.LOWEST)
    public void onBreakCake(BlockBreakEvent e) {
        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(e.getPlayer().getUniqueId())) {
            return;
        }

        Block cake = e.getBlock();
        if (cake.getType() == Material.CAKE) {
            e.setCancelled(true);

            Player p = e.getPlayer();
            Player q = getPlacer(cake);
            String cakeType;
            if (q != null) {
                destroyCake(q);
                cakeType = TeamController.getTeam(p.getUniqueId())
                        == TeamController.getTeam(q.getUniqueId()) ? " friendly" : "n enemy";
                Messenger.sendWarning("Your cake was destroyed by " + NameTag.mmUsername(p), q);
            } else {
                cake.setType(Material.AIR);
                cakeType = " neutral";
            }
            Messenger.sendActionInfo("You destroyed a" + cakeType + " cake", p);
        }
    }

    /**
     * Activate the medic ability of healing a teammate
     * @param event The event called when clicking on a teammate with paper
     */
    @EventHandler
    public void onHeal(PlayerInteractEntityEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
            Player player = event.getPlayer();
            UUID uuid = player.getUniqueId();

            // Prevent using in lobby
            if (InCombat.isPlayerInLobby(uuid)) {
                return;
            }

            PlayerInventory i = player.getInventory();
            Entity q = event.getRightClicked();
            if (Objects.equals(Kit.equippedKits.get(uuid).name, name) &&                            // Player is medic
                    (i.getItemInMainHand().getType() == Material.PAPER) &&                    // Uses bandage
                    q instanceof Player &&                                                          // On player
                    TeamController.getTeam(uuid) == TeamController.getTeam(q.getUniqueId()) &&      // Same team
                    ((Player) q).getHealth() < Kit.equippedKits.get(q.getUniqueId()).baseHealth &&  // Below max hp
                    !cooldown.contains((Player) q)) {                                               // Not on cooldown

                // Apply cooldown
                Player r = (Player) q;
                cooldown.add(r);
                Bukkit.getScheduler().runTaskLaterAsynchronously(Main.plugin, () -> cooldown.remove(r), 39);

                // Heal
                addPotionEffect(r, new PotionEffect(PotionEffectType.REGENERATION, 40, 9));
                addPotionEffect(player, new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60, 0));
                Messenger.sendHealing(NameTag.mmUsername(player) + " is healing you", r);
                Messenger.sendHealing("You are healing " + NameTag.mmUsername(r), player);
                UpdateStats.addHeals(uuid, 1);
            }
        });
    }

    /**
     * Add a potion effect to a player.
     * @param player The player
     * @param potion The potion effect
     */
    private void addPotionEffect(Player player, PotionEffect potion) {
        Bukkit.getScheduler().runTask(Main.plugin, () -> player.addPotionEffect(potion));
    }

    /**
     * Remove a cake when its placer dies
     * @param e The event called when a player dies
     */
    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        destroyCake(e.getEntity());
    }

    /**
     * Remove a cake when its placer leaves the game
     * @param e The event called when a player leaves the game
     */
    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        destroyCake(e.getPlayer());
    }

    /**
     * Destroy the player's cake if present
     * @param p The player whose cake to destroy
     */
    private void destroyCake(Player p) {
        Block cake = cakes.remove(p);
        if (cake != null) {
            cake.setType(Material.AIR);
        }
    }

    /**
     * Get the placer of the cake
     * @param cake The cake whose placer to find
     * @return The placer of the cake, null if the placer is not a medic
     */
    public static Player getPlacer(Block cake) {
        return cakes.entrySet().stream()
                .filter(entry -> Objects.equals(entry.getValue(), cake))
                .findFirst().map(Map.Entry::getKey)
                .orElse(null);
    }

    /**
     * Activate the medic potion
     * @param e The event called when clicking with the potion in hand
     */
    @EventHandler
    public void drinkPotion(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();


        if (Objects.equals(Kit.equippedKits.get(uuid).name, name)) {
            if (e.getItem() != null && e.getItem().getType() == Material.POTION) {
                if (e.getHand() == EquipmentSlot.HAND) {
                    p.getInventory().getItemInMainHand().setType(Material.GLASS_BOTTLE);
                } else if (e.getHand() == EquipmentSlot.OFF_HAND) {
                    p.getInventory().getItemInOffHand().setType(Material.GLASS_BOTTLE);
                }

                // Prevent using in lobby
                if (InCombat.isPlayerInLobby(uuid)) {
                    e.setCancelled(true);
                    return;
                }

                // Potion effects
                p.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1, 6));

            }
        }
    }

    /**
     * @return The lore to add to the kit gui item
     */
    @Override
    public ArrayList<Component> getGuiDescription() {
        ArrayList<Component> kitLore = new ArrayList<>();
        kitLore.add(Component.text("Our classic healer, which makes", NamedTextColor.GRAY));
        kitLore.add(Component.text("use of bandages and cake to heal allies", NamedTextColor.GRAY));
        kitLore.addAll(getBaseStats(health, regen, meleeDamage, ladderCount));
        kitLore.add(Component.text(cakeCount, color)
                        .append(Component.text(" Cakes", NamedTextColor.GRAY)));
        kitLore.add(Component.empty());
        kitLore.add(Component.text("Effects:", NamedTextColor.DARK_PURPLE));
        kitLore.add(Component.text("- Speed I", NamedTextColor.GRAY));
        kitLore.add(Component.empty());
        kitLore.add(Component.text("Active:", NamedTextColor.GOLD));
        kitLore.add(Component.text("- Can use bandages to heal teammates", NamedTextColor.GRAY));
        kitLore.add(Component.text("- Can place cakes (1 active max)", NamedTextColor.GRAY));
        kitLore.add(Component.empty());
        kitLore.add(Component.text("Passive:", NamedTextColor.DARK_GREEN));
        kitLore.add(Component.text("- When healing an ally receives resistance I", NamedTextColor.GRAY));
        return kitLore;
    }
}
