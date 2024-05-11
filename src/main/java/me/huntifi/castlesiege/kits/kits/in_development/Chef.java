package me.huntifi.castlesiege.kits.kits.in_development;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.events.EnderchestEvent;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.kits.items.CSItemCreator;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.kits.CoinKit;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.maps.TeamController;
import me.huntifi.conwymc.data_types.Tuple;
import me.huntifi.conwymc.util.Messenger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
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
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

/**
 * A chef is someone who can cook food to buff their allies or place a campfire for better healing
 */
public class Chef extends CoinKit implements Listener {

    private static final int baseHealth = 260;
    private static final int regenAmount = 3;
    private static final Material material = Material.IRON_SHOVEL;
    private static ItemStack campfire;
    private static ItemStack fryingPan;
    private static ItemStack fryingPanVoted;
    private static final int knifeCooldown = 80;
    private static final double knifeVelocity = 3.0;
    private static final int knifeDamage = 25;
    private static final int knifeCount = 3;
    private static final int campfireRadius = 4;
    private static final int campfireRegen = 15;
    private static final int campfireRegenTimer = 70;
    private static final int eggCooldown = 150;
    private static final int eggCount = 10;

    private static ItemStack steak;
    private final PotionEffect steakEffect = new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 1200, 1);
    private static ItemStack goldenCarrot;
    private final PotionEffect carrotEffect = new PotionEffect(PotionEffectType.NIGHT_VISION, 2400, 0);
    private static ItemStack pie;
    private final PotionEffect pieAbsorb = new PotionEffect(PotionEffectType.ABSORPTION, 2400, 29);
    private final PotionEffect pieSlow = new PotionEffect(PotionEffectType.SLOW, 600, 0);

    public static final HashMap<Player, Block> campfires = new HashMap<>();
    public static final ArrayList<Location> activeLocations = new ArrayList<>();

    /**
     * Creates a new chef
     */
    public Chef() {
        super("Chef", baseHealth, regenAmount, material);
        super.canSeeHealth = true;

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();

        fryingPan = CSItemCreator.weapon(new ItemStack(Material.IRON_SHOVEL),
                Component.text("Frying Pan", NamedTextColor.GREEN),
                Arrays.asList(Component.empty(),
                        Component.text("Knock enemies away with the back of a pan", NamedTextColor.AQUA),
                        Component.text("Also halves bow damage while in the main hand", NamedTextColor.AQUA),
                        Component.text("Crossbow damage is 75%", NamedTextColor.AQUA)),
                Collections.singletonList(new Tuple<>(Enchantment.KNOCKBACK, 0)), 20);
        // Weapon
        es.hotbar[0] = fryingPan;

        fryingPanVoted = CSItemCreator.weapon(new ItemStack(Material.IRON_SHOVEL),
                Component.text("Frying Pan", NamedTextColor.GREEN), Arrays.asList(
                        Component.empty(),
                        Component.text("Knock enemies away with the back of a pan", NamedTextColor.AQUA),
                        Component.text("Also halves bow damage while in the main hand", NamedTextColor.AQUA),
                        Component.text("Crossbow damage is 75%", NamedTextColor.AQUA),
                        Component.text("- voted: +2 damage", NamedTextColor.AQUA)),
                Collections.singletonList(new Tuple<>(Enchantment.KNOCKBACK, 0)), 22);
        es.votedWeapon = new Tuple<>(fryingPanVoted, 0);

        ItemStack kitchenKnives = CSItemCreator.item(new ItemStack(Material.TIPPED_ARROW, knifeCount),
                Component.text("Kitchen Knife", NamedTextColor.GREEN),
                Arrays.asList(Component.text("Right click to throw!", NamedTextColor.AQUA).decorate(TextDecoration.BOLD),
                        Component.empty(),
                        Component.text("Hitting enemy targets with this gives them", NamedTextColor.AQUA),
                        Component.text("a brief poison effect and brief slowness.", NamedTextColor.AQUA)),
                null);
        PotionMeta potionMeta = (PotionMeta) kitchenKnives.getItemMeta();
        assert potionMeta != null;
        potionMeta.setColor(Color.GREEN);
        kitchenKnives.setItemMeta(potionMeta);
        potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.POISON, 100, 0), true);
        potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SLOW, 30, 1), true);
        kitchenKnives.setItemMeta(potionMeta);

        es.hotbar[1] = kitchenKnives;

        campfire = CSItemCreator.item(new ItemStack(Material.CAMPFIRE),
                Component.text("Campfire", NamedTextColor.GREEN),
                Arrays.asList(Component.empty(),
                        Component.text("Can be placed down on the ground", NamedTextColor.AQUA),
                        Component.text("When placed grants a slow regen effect to players nearby", NamedTextColor.AQUA)),
                null);

        es.hotbar[2] = campfire;

        ItemStack egg = CSItemCreator.item(new ItemStack(Material.EGG, eggCount),
                Component.text("Mystery Egg", NamedTextColor.GREEN),
                Arrays.asList(Component.empty(),
                        Component.text("Right click to prepare food", NamedTextColor.AQUA),
                        Component.text("Food grants a bonus to teammates", NamedTextColor.AQUA),
                        Component.text("by right-clicking on them", NamedTextColor.AQUA)),
                Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_BLOCKS, 0)));

        es.hotbar[3] = egg;

        steak = CSItemCreator.item(new ItemStack(Material.COOKED_BEEF),
                Component.text("Strength Steak", NamedTextColor.GREEN),
                Arrays.asList(Component.empty(),
                        Component.text("Right click on a teammate to feed them", NamedTextColor.AQUA),
                        Component.text("Grants strength to fed player", NamedTextColor.AQUA)),
                Collections.singletonList(new Tuple<>(Enchantment.KNOCKBACK, 0)));

        goldenCarrot = CSItemCreator.item(new ItemStack(Material.GOLDEN_CARROT),
                Component.text("Golden Carrot", NamedTextColor.GREEN),
                Arrays.asList(Component.empty(),
                        Component.text("Right click a teammate to feed them", NamedTextColor.AQUA),
                        Component.text("Grants night vision to the player", NamedTextColor.AQUA)),
                Collections.singletonList(new Tuple<>(Enchantment.KNOCKBACK, 0)));

        pie = CSItemCreator.item(new ItemStack(Material.PUMPKIN_PIE),
                Component.text("Filling Pie", NamedTextColor.GREEN),
                Arrays.asList(Component.empty(),
                        Component.text("Right click a teammate to feed them", NamedTextColor.AQUA),
                        Component.text("Grants absorption to the fed teammate", NamedTextColor.AQUA),
                        Component.text("Grants slow to the fed teammate", NamedTextColor.AQUA)),
                Collections.singletonList(new Tuple<>(Enchantment.KNOCKBACK, 0)));

        // Chestplate
        es.chest = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE),
                Component.text("Apron Top", NamedTextColor.GREEN), null, null,
                Color.fromRGB(255, 255, 255));

        // Leggings
        es.legs = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_LEGGINGS),
                Component.text("Apron Bottom", NamedTextColor.GREEN), null, null,
                Color.fromRGB(255, 255, 255));

        // Boots
        es.feet = CSItemCreator.item(new ItemStack(Material.NETHERITE_BOOTS),
                Component.text("Kitchen Soles", NamedTextColor.GREEN), null, null);
        // Voted Boots
        es.votedFeet = CSItemCreator.item(new ItemStack(Material.NETHERITE_BOOTS),
                Component.text("Kitchen Soles", NamedTextColor.GREEN),
                Collections.singletonList(Component.text("- voted: Depth Strider II", NamedTextColor.AQUA)),
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
            Messenger.sendActionInfo("You built a campfire", p);

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
                        Messenger.sendActionError("You can't throw your knife yet (" + p.getCooldown(Material.TIPPED_ARROW) / 20 + "s)", p);
                    }
                }
            }
        }
    }

    /**
     * Set the thrown knife's damage
     * @param e The event called when an arrow hits a player
     */
    @EventHandler(priority = EventPriority.LOW)
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

    /**
     * Activate the chef ability of throwing a knife
     * @param e The event called when right-clicking with a tipped arrow
     */
    @EventHandler
    public void prepFood(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        ItemStack egg = p.getInventory().getItemInMainHand();
        int cooldown = p.getCooldown(Material.EGG);

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(uuid)) {
            return;
        }

        if (Objects.equals(Kit.equippedKits.get(uuid).name, name)) {
            if (egg.getType().equals(Material.EGG)) {
                if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (cooldown == 0) {
                        egg.setAmount(egg.getAmount() - 1);
                        p.setCooldown(Material.EGG, eggCooldown);
                        Messenger.sendActionInfo("You made some food!", p);
                        grantFood(p);
                    } else {
                        Messenger.sendActionError("You can't make any food yet (" + p.getCooldown(Material.EGG) / 20 + "s)", p);
                    }
                }
            }
        }
    }

    private static void grantFood(Player p) {
        Random random = new Random();
        int choice = random.nextInt(3);

        switch (choice) {
            case 0:
                p.getInventory().addItem(steak);
            case 1:
                p.getInventory().addItem(goldenCarrot);
            case 2:
                p.getInventory().addItem(pie);
        }
    }

    /**
     * Activate the chef ability of throwing a knife
     * @param e The event called when right-clicking with a tipped arrow
     */
    @EventHandler
    public void useFood(PlayerInteractEntityEvent e) {
        //Check if the hit entity is a player, otherwise do nothing.
        if (!(e.getRightClicked() instanceof Player)) {
            return;
        }
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        ItemStack food = p.getInventory().getItemInMainHand();
        Player fed = (Player) e.getRightClicked();

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(uuid)) {
            return;
        }

        if (Objects.equals(Kit.equippedKits.get(uuid).name, name)) {
            if (food.getType().equals(Material.COOKED_BEEF)) {
                food.setAmount(food.getAmount() - 1);
                Messenger.sendActionInfo("You gave " + fed.getName() + " a strength steak!", p);
                Messenger.sendActionInfo("You were given a strength steak by Chef " + p.getName(), fed);
                fed.addPotionEffect(steakEffect);
            } else if (food.getType().equals(Material.GOLDEN_CARROT)) {
                food.setAmount(food.getAmount() - 1);
                Messenger.sendActionInfo("You gave " + fed.getName() + " a golden carrot!", p);
                Messenger.sendActionInfo("You were given a golden carrot by Chef " + p.getName(), fed);
                fed.addPotionEffect(carrotEffect);
            } else if (food.getType().equals(Material.PUMPKIN_PIE)) {
                food.setAmount(food.getAmount() - 1);
                Messenger.sendActionInfo("You gave " + fed.getName() + " some filling pie!", p);
                Messenger.sendActionInfo("You were given some filling pie by Chef " + p.getName(), fed);
                fed.addPotionEffect(pieAbsorb);
                fed.addPotionEffect(pieSlow);
            }
        }
    }

    @Override
    public ArrayList<Component> getGuiDescription() {
        ArrayList<Component> description = new ArrayList<>();
        description.add(Component.text("//TODO - Add kit description", NamedTextColor.GRAY));
        return description;
    }
}
