package me.huntifi.castlesiege.kits.kits.donator_kits;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.events.EnderchestEvent;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.kits.kits.DonatorKit;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.maps.TeamController;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

/**
 * Creates the Chef kit
 */
public class Chef extends DonatorKit implements Listener {

    private final static int baseHealth = 260;
    private final static int regenAmount = 3;
    private final static int coinCost = 20000;
    private final static int bpCost = 6;
    private final static Material material = Material.IRON_SHOVEL;
    private static ItemStack campfire;
    private static ItemStack fryingPan;
    private static ItemStack fryingPanVoted;
    private final static int knifeCount = 3;
    private final static int knifeCooldown = 80;
    private static final double knifeVelocity = 3.0;
    private static final int knifeDamage = 25;
    private static final int campfireRadius = 4;
    private static final int campfireRegen = 15;
    private static final int campfireRegenTimer = 70;

    public static final HashMap<Player, Block> campfires = new HashMap<>();
    public static final ArrayList<Location> activeLocations = new ArrayList<>();

    public Chef() {
        super("Chef", baseHealth, regenAmount, coinCost, bpCost, material);
        super.canSeeHealth = true;

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        fryingPan = ItemCreator.weapon(new ItemStack(Material.IRON_SHOVEL),
                ChatColor.DARK_GRAY + "Frying Pan", Arrays.asList(
                        ChatColor.AQUA + "Knock enemies away with the back of a pan",
                        ChatColor.AQUA + "Also halves bow damage while in the main hand",
                        ChatColor.AQUA + "Crossbow damage is 75%"),
                Collections.singletonList(new Tuple<>(Enchantment.KNOCKBACK, 0)), 20);
        // Weapon
        es.hotbar[0] = fryingPan;

        fryingPanVoted = ItemCreator.weapon(new ItemStack(Material.IRON_SHOVEL),
                ChatColor.DARK_GRAY + "Frying Pan", Arrays.asList(
                        ChatColor.AQUA + "Knock enemies away with the back of a pan",
                        ChatColor.AQUA + "Also halves bow damage while in the main hand",
                        ChatColor.AQUA + "Crossbow damage is 75%",
                        ChatColor.AQUA + "- voted: +2 damage"),
                Collections.singletonList(new Tuple<>(Enchantment.KNOCKBACK, 0)), 22);
        es.votedWeapon = new Tuple<>(fryingPanVoted, 0);

        ItemStack kitchenKnives = ItemCreator.item(new ItemStack(Material.TIPPED_ARROW, knifeCount),
                ChatColor.DARK_GRAY + "KitchenKnife",
                Arrays.asList(ChatColor.BOLD + "Right click to throw!",
                        String.valueOf(ChatColor.AQUA),
                        ChatColor.AQUA + "Hitting enemy targets with this gives them",
                        ChatColor.AQUA + "a brief poison effect and brief slowness."),
                null);
        PotionMeta potionMeta = (PotionMeta) kitchenKnives.getItemMeta();
        assert potionMeta != null;
        potionMeta.setColor(Color.GREEN);
        kitchenKnives.setItemMeta(potionMeta);
        potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.POISON, 100, 0), true);
        potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SLOW, 30, 1), true);
        kitchenKnives.setItemMeta(potionMeta);

        es.hotbar[1] = kitchenKnives;

        campfire = ItemCreator.item(new ItemStack(Material.CAMPFIRE),
                ChatColor.GREEN + "Campfire",
                Arrays.asList(ChatColor.AQUA + "Can be placed down on the ground",
                        ChatColor.AQUA + "When placed grants a slow regen effect to players nearby"),
                null);

        // Chestplate
        es.chest = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE),
                ChatColor.DARK_GRAY + "Apron Top", null, null,
                Color.fromRGB(255, 255, 255));

        // Leggings
        es.legs = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_LEGGINGS),
                ChatColor.DARK_GRAY + "Apron Bottom", null, null,
                Color.fromRGB(255, 255, 255));

        // Boots
        es.feet = ItemCreator.item(new ItemStack(Material.NETHERITE_BOOTS),
                ChatColor.DARK_GRAY + "Kitchen Soles", null, null);
        // Voted Boots
        es.votedFeet = ItemCreator.item(new ItemStack(Material.NETHERITE_BOOTS),
                ChatColor.GREEN + "Kitchen Soles",
                Collections.singletonList(ChatColor.AQUA + "- voted: Depth Strider II"),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 3)));

        super.equipment = es;

        // Perm Potion Effect
        super.potionEffects.add(new PotionEffect(PotionEffectType.SPEED, 999999, 0));

        // Death Messages
        super.deathMessage[0] = "You fried, skin side down by ";
        super.killMessage[0] = " fried ";
        super.killMessage[1] = " skin side down";
        super.projectileDeathMessage[0] = "You were sliced and diced by ";
        super.projectileKillMessage[1] = " was sliced and diced by ";
    }

    /**
     * Place a campfire
     * @param e The event called when placing a campfire
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(p.getUniqueId())) {
            return;
        }

        if (Objects.equals(Kit.equippedKits.get(p.getUniqueId()).name, name) &&
                e.getBlockPlaced().getType() == Material.CAMPFIRE) {

            // Destroy old campfire
            destroyCampfire(p);

            // Place new campfire
            e.setCancelled(false);
            campfires.put(p, e.getBlockPlaced());
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                    TextComponent.fromLegacyText(ChatColor.AQUA + "You setup your Campfire!"));

            new BukkitRunnable() {
                @Override
                public void run() {
                    Location loc = e.getBlockPlaced().getLocation();
                    if (!activeLocations.contains(loc)) {
                        this.cancel();
                    }
                    int squaredDistance = campfireRadius * campfireRadius;
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (loc.distanceSquared(player.getLocation()) <= squaredDistance) {
                            player.setHealth(p.getHealth() + campfireRegen);
                        }
                    }
                }
            }.runTaskTimerAsynchronously(Main.plugin, campfireRegenTimer, campfireRegenTimer);
        }
    }

    /**
     * Destroy a campfire
     * @param e The event called when left-clicking a campfire
     */
    @EventHandler
    public void onRemove(PlayerInteractEvent e) {
        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(e.getPlayer().getUniqueId())) {
            return;
        }

        if (e.getAction() == Action.LEFT_CLICK_BLOCK &&
                Objects.requireNonNull(e.getClickedBlock()).getType() == Material.CAMPFIRE) {
            Player p = e.getPlayer();
            Player q = getPlacer(e.getClickedBlock());

            // Pick up own campfire
            if (Objects.equals(p, q)) {
                destroyCampfire(q);
                Messenger.sendActionInfo("You took back your Campfire!", q);

                // Can only hold 1 campfire at a time
                PlayerInventory inv = p.getInventory();
                if (inv.getItemInOffHand().getType() != Material.CAMPFIRE &&
                        !inv.contains(Material.CAMPFIRE)) {
                    p.getInventory().addItem(campfire);
                }

                // Destroy enemy campfire
            } else if (q != null &&
                    TeamController.getTeam(p.getUniqueId()) != TeamController.getTeam(q.getUniqueId())){
                destroyCampfire(q);
                p.playSound(e.getClickedBlock().getLocation(), Sound.BLOCK_FIRE_EXTINGUISH , 5, 1);
                Messenger.sendActionInfo("You put out " + q.getName() + "'s Campfire!", p);
            }
        }
    }

    /**
     * Destroy a player's campfire when they click an enderchest.
     * @param event The event called when an off-cooldown player interacts with an enderchest
     */
    @EventHandler
    public void onClickEnderchest(EnderchestEvent event) {
        destroyCampfire(event.getPlayer());
    }

    /**
     * Destroy a player's campfire when they die
     * @param e The event called when a player dies
     */
    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        destroyCampfire(e.getEntity());
    }

    /**
     * Destroy a player's campfire when they leave the game
     * @param e The event called when a player leaves the game
     */
    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        destroyCampfire(e.getPlayer());
    }

    /**
     * Destroy the player's campfire if present
     * @param p The player whose campfire to destroy
     */
    private void destroyCampfire(Player p) {
        if(campfires.containsKey(p)) {
            campfires.get(p).setType(Material.AIR);
            activeLocations.remove(campfires.get(p).getLocation());
            campfires.remove(p);
        }
    }

    /**
     * Get the placer of a campfire
     * @param campfire The campfire whose placer to find
     * @return The placer of the campfire, null of not placed by a chef
     */
    private Player getPlacer(Block campfire) {
        return campfires.entrySet().stream()
                .filter(entry -> Objects.equals(entry.getValue(), campfire))
                .findFirst().map(Map.Entry::getKey)
                .orElse(null);
    }

    /**
     * Activate the chef ability of throwing a knife
     * @param e The event called when right-clicking with a tipped arrow
     */
    @EventHandler
    public void throwKnife(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        ItemStack knife = p.getInventory().getItemInMainHand();
        int cooldown = p.getCooldown(Material.TIPPED_ARROW);

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(uuid)) {
            return;
        }

        if (Objects.equals(Kit.equippedKits.get(uuid).name, name)) {
            if (knife.getType().equals(Material.TIPPED_ARROW)) {
                if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (cooldown == 0) {
                        knife.setAmount(knife.getAmount() - 1);
                        p.setCooldown(Material.TIPPED_ARROW, knifeCooldown);
                        Messenger.sendActionInfo("You threw your knife!", p);
                        p.launchProjectile(Arrow.class).setVelocity(p.getLocation().getDirection().multiply(knifeVelocity));
                    } else {
                        Messenger.sendActionError("You can't throw your spear yet (" + p.getCooldown(Material.TIPPED_ARROW) / 20 + "s)", p);
                    }
                }
            }
        }
    }

    /**
     * Set the thrown knife's damage
     * @param e The event called when an arrow hits a player
     */
    @EventHandler (priority = EventPriority.LOW)
    public void changeKnifeDamage(ProjectileHitEvent e) {
        if (e.getEntity() instanceof Arrow) {
            Arrow arrow = (Arrow) e.getEntity();

            if (arrow.getShooter() instanceof Player) {
                Player damages = (Player) arrow.getShooter();

                if (Objects.equals(Kit.equippedKits.get(damages.getUniqueId()).name, name)) {
                    arrow.setDamage(knifeDamage);
                }
            }
        }
    }

    /**
     * Reduced projectile damage if the chef was holding the frying pan
     * @param e The projectile that hit the chef
     * @param hit The chef that was hit
     */
    @EventHandler()
    public void panProjectileDefence(ProjectileHitEvent e, Entity hit) {
        if (e.getEntity() instanceof Arrow && hit instanceof Player) {
            Arrow arrow = (Arrow) e.getEntity();

            if (Objects.equals(Kit.equippedKits.get(hit.getUniqueId()).name, name)) {
                if (((Player) hit).getInventory().getItemInMainHand() == fryingPan ||
                        ((Player) hit).getInventory().getItemInMainHand() == fryingPanVoted) {
                    if (arrow.isShotFromCrossbow()) {
                        arrow.setDamage(arrow.getDamage() * 0.75);
                    } else {
                        arrow.setDamage(arrow.getDamage() * 0.5);
                    }
                }
            }
        }
    }
}
