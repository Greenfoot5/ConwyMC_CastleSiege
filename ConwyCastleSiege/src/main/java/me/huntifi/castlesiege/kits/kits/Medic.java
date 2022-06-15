package me.huntifi.castlesiege.kits.kits;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.database.UpdateStats;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.NameTag;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
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
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

/**
 * The medic kit
 */
public class Medic extends Kit implements Listener {

    public static HashMap<Player, Block> cakes = new HashMap<>();
    public static ArrayList<Player> cooldown = new ArrayList<>();

    /**
     * Set the equipment and attributes of this kit
     */
    public Medic() {
        super("Medic", 110, 8);
        super.canSeeHealth = true;

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Weapon
        es.hotbar[0] = ItemCreator.weapon(new ItemStack(Material.WOODEN_SWORD),
                ChatColor.GREEN + "Scalpel", null, null, 25);
        // Voted Weapon
        es.votedWeapon = new Tuple<>(
                ItemCreator.weapon(new ItemStack(Material.WOODEN_SWORD),
                        ChatColor.GREEN + "Scalpel",
                        Collections.singletonList(ChatColor.AQUA + "- voted: +2 damage"),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), 27),
                0);

        // Chestplate
        es.chest = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE),
                ChatColor.GREEN + "Leather Chestplate", null, null,
                Color.fromRGB(255, 255, 255));

        // Leggings
        es.legs = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_LEGGINGS),
                ChatColor.GREEN + "Leather Leggings", null, null,
                Color.fromRGB(255, 255, 255));

        // Boots
        es.feet = ItemCreator.item(new ItemStack(Material.GOLDEN_BOOTS),
                ChatColor.GREEN + "Golden Boots", null, null);
        // Voted Boots
        es.votedFeet = ItemCreator.item(new ItemStack(Material.GOLDEN_BOOTS),
                ChatColor.GREEN + "Golden Boots",
                Collections.singletonList(ChatColor.AQUA + "- voted: Depth Strider II"),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)));

        // Bandages
        es.hotbar[1] = ItemCreator.item(new ItemStack(Material.PAPER),
                ChatColor.DARK_AQUA + "Bandages",
                Collections.singletonList(ChatColor.AQUA + "Right click teammates to heal."), null);

        // Cake
        es.hotbar[2] = ItemCreator.item(new ItemStack(Material.CAKE, 16),
                ChatColor.DARK_AQUA + "Healing Cake",
                Arrays.asList(ChatColor.AQUA + "Place the cake down, then",
                        ChatColor.AQUA + "teammates can heal from it."), null);

        // Ladders
        es.hotbar[3] = new ItemStack(Material.LADDER, 4);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, 6), 3);

        super.equipment = es;

        // Perm Potion Effect
        super.potionEffects.add(new PotionEffect(PotionEffectType.SPEED, 999999, 0));

        super.killMessage[0] = "You dissected ";
        super.deathMessage[0] = "You had your insides examined by ";
    }

    /**
     * Place a cake
     * @param e The event called when placing a cake
     */
    @EventHandler (priority = EventPriority.HIGHEST)
    public void onPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(p.getUniqueId())) {
            return;
        }

        if (Objects.equals(Kit.equippedKits.get(p.getUniqueId()).name, name) &&
                e.getBlockPlaced().getType() == Material.CAKE) {
            e.setCancelled(false);
            destroyCake(p);
            cakes.put(p, e.getBlockPlaced());
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
                cakeType = MapController.getCurrentMap().getTeam(p.getUniqueId())
                        == MapController.getCurrentMap().getTeam(q.getUniqueId()) ? " friendly" : "n enemy";
                q.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                        ChatColor.RED + "Your cake was destroyed by " + NameTag.color(p) + p.getName()));
            } else {
                cake.setType(Material.AIR);
                cakeType = " neutral";
            }
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                    ChatColor.RED + "You destroyed a" + cakeType + " cake"));
        }
    }

    /**
     * Take a bite from a cake and gain a short period of regeneration
     * @param e The event called when right-clicking on a cake
     */
    @EventHandler (priority = EventPriority.HIGHEST)
    public void onEatCake(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        Block cake = e.getClickedBlock();

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(p.getUniqueId())) {
            return;
        }

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK &&
                cake.getType().equals(Material.CAKE)) {
            Player q = getPlacer(cake);

            // Enemy cake
            if (q != null && MapController.getCurrentMap().getTeam(p.getUniqueId())
                    != MapController.getCurrentMap().getTeam(q.getUniqueId())) {
                e.setCancelled(true);
                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                        ChatColor.RED + "This is an enemy cake, destroy it!"));
                return;
            }

            // Able to eat this cake
            if (p.getHealth() < Kit.equippedKits.get(p.getUniqueId()).baseHealth &&
                    !cooldown.contains(p)) {
                // Apply cooldown
                cooldown.add(p);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        cooldown.remove(p);
                    }
                }.runTaskLater(Main.plugin, 40);

                // Eat cake
                p.setFoodLevel(17);
                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 40, 9));

                // Send messages and award heal
                if (q != null && !Objects.equals(p, q)) {
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                            NameTag.color(q) + q.getName() + ChatColor.AQUA + "'s cake is healing you!"));
                    q.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                            ChatColor.AQUA + "Your cake is healing " + NameTag.color(p) + p.getName()));
                    UpdateStats.addHeal(q.getUniqueId());
                } else {
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                            ChatColor.AQUA + "The cake is healing you!"));
                }

                // Remove cake from cakes list
                Cake cakeData = (Cake) cake.getBlockData();
                if (cakeData.getBites() == 6 && q != null) {
                    cakes.remove(q);
                }

            // Unable to eat the cake right now
            } else {
                e.setCancelled(true);
            }
        }
    }

    /**
     * Activate the medic ability of healing a teammate
     * @param e The event called when clicking on a teammate with paper
     */
    @EventHandler
    public void onHeal(PlayerInteractEntityEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        PlayerInventory i = p.getInventory();
        Entity q = e.getRightClicked();

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(uuid)) {
            return;
        }

        if (Objects.equals(Kit.equippedKits.get(uuid).name, name) &&                            // Player is medic
                (i.getItemInMainHand().getType() == Material.PAPER ||                           // Uses bandage
                        i.getItemInOffHand().getType() == Material.PAPER) &&                    // Uses bandage
                q instanceof Player &&                                                          // On player
                MapController.getCurrentMap().getTeam(uuid)                                     // Same team
                == MapController.getCurrentMap().getTeam(q.getUniqueId()) &&                    // Same team
                ((Player) q).getHealth() < Kit.equippedKits.get(q.getUniqueId()).baseHealth &&  // Below max hp
                !cooldown.contains((Player) q)) {                                               // Not on cooldown

            // Apply cooldown
            Player r = (Player) q;
            cooldown.add(r);
            new BukkitRunnable() {
                @Override
                public void run() {
                    cooldown.remove(r);
                }
            }.runTaskLater(Main.plugin, 100);

            // Heal
            r.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 40, 9));
            r.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                     NameTag.color(p) + p.getName() + ChatColor.AQUA + " is healing you"));
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                    ChatColor.AQUA + "You are healing " + NameTag.color(r) + r.getName()));
            UpdateStats.addHeal(p.getUniqueId());
        }
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
        if (cakes.containsKey(p)) {
            cakes.get(p).setType(Material.AIR);
            cakes.remove(p);
        }
    }

    /**
     * Get the placer of the cake
     * @param cake The cake whose placer to find
     * @return The placer of the cake, null if the placer is not a medic
     */
    private Player getPlacer(Block cake) {
        return cakes.entrySet().stream()
                .filter(entry -> Objects.equals(entry.getValue(), cake))
                .findFirst().map(Map.Entry::getKey)
                .orElse(null);
    }
}
