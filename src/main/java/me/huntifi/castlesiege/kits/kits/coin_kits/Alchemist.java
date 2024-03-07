package me.huntifi.castlesiege.kits.kits.coin_kits;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.castlesiege.database.UpdateStats;
import me.huntifi.castlesiege.events.EnderchestEvent;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.events.combat.AssistKill;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.kits.kits.CoinKit;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.maps.NameTag;
import me.huntifi.castlesiege.maps.TeamController;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Alchemist kit, a brewing support kit
 */
public class Alchemist extends CoinKit implements Listener {

    public static final HashMap<Player, Block> stands = new HashMap<>();
    private final ItemStack stand;
    private final ItemStack standVoted;

    private static final int health = 210;
    private static final double regen = 10.5;
    private static final double meleeDamage = 26;
    private static final int ladderCount = 5;

    /**
     * Creates the basics for Alchemist
     */
    public Alchemist() {
        super("Alchemist", health, regen, Material.BREWING_STAND);
        super.canSeeHealth = true;

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Brewing Stand
        stand = ItemCreator.weapon(new ItemStack(Material.BREWING_STAND),
                Component.text("Brewing Stand", NamedTextColor.LIGHT_PURPLE),
                Arrays.asList(Component.empty(),
                        Component.text("Place the brewing stand down, then", NamedTextColor.AQUA),
                        Component.text("right click it to get a positive potion.", NamedTextColor.AQUA),
                        Component.empty(),
                        Component.text("left click it to get a negative potion.", NamedTextColor.AQUA),
                        Component.text("(tip): This brewing stand is a weapon, so you", NamedTextColor.AQUA),
                        Component.text("can beat your enemies to death with it.", NamedTextColor.AQUA)),
                Collections.singletonList(new Tuple<>(Enchantment.LOYALTY, 1)), meleeDamage);
        es.hotbar[0] = stand;
        // Voted Brewing Stand
        standVoted = ItemCreator.weapon(new ItemStack(Material.BREWING_STAND),
                Component.text("Brewing Stand", NamedTextColor.LIGHT_PURPLE),
                Arrays.asList(Component.text(""),
                        Component.text("Place the brewing stand down, then", NamedTextColor.AQUA),
                        Component.text("right click it to get a potion.", NamedTextColor.AQUA),
                                Component.text(""),
                        Component.text("(tip): This brewing stand is a weapon, so you", NamedTextColor.AQUA),
                        Component.text("can beat your enemies to death with it.", NamedTextColor.AQUA)),
                Arrays.asList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0),
                        new Tuple<>(Enchantment.LOYALTY, 1)), meleeDamage + 2);
        es.votedWeapon = new Tuple<>(standVoted, 0);

        // Chestplate
        es.chest = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE),
                Component.text("Leather Chestplate", NamedTextColor.GREEN), null, null,
                Color.fromRGB(226, 165, 43));

        // Leggings
        es.legs = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_LEGGINGS),
                Component.text("Leather Leggings", NamedTextColor.GREEN), null, null,
                Color.fromRGB(226, 173, 65));

        // Boots
        es.feet = ItemCreator.item(new ItemStack(Material.GOLDEN_BOOTS),
                Component.text("Golden Boots", NamedTextColor.GREEN), null, null);
        // Voted Boots
        es.votedFeet = ItemCreator.item(new ItemStack(Material.GOLDEN_BOOTS),
                Component.text("Golden Boots", NamedTextColor.GREEN),
                Collections.singletonList(Component.text("- voted: Depth Strider II", NamedTextColor.AQUA)),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)));

        es.hotbar[1] = new ItemStack(Material.GLASS_BOTTLE, ladderCount);
        es.votedLadders = new Tuple<>(new ItemStack(Material.GLASS_BOTTLE, ladderCount + 1), 1);

        super.equipment = es;

        // Perm Potion Effect
        super.potionEffects.add(new PotionEffect(PotionEffectType.SPEED, 999999, 0));

        super.killMessage[0] = " brewed ";
        super.killMessage[1] = " into a potion";
        super.deathMessage[0] = "You were brewed into a potion by ";
    }


    /**
     * Only applies the correct potion effect to the correct players
     * @param e Called when a player throws a potion
     */
    @EventHandler (priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onThrownPotion(PotionSplashEvent e) {
        // Is the potion thrown by an alchemist?
        if (!(e.getPotion().getShooter() instanceof Player)) {
            return;
        }

        Player damager = (Player) e.getPotion().getShooter();
        if (!Objects.equals(Kit.equippedKits.get(damager.getUniqueId()).name, name)) {
            return;
        }
        e.setCancelled(true);

        Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
            Collection<PotionEffect> effects = e.getPotion().getEffects();
            for (Entity entity : e.getAffectedEntities()) {
                if (!(entity instanceof Player))
                    continue;

                Player hit = ((Player) entity).getPlayer();
                assert hit != null;
                for (PotionEffect effect : effects) {
                    PotionEffectType potionType = effect.getType();
                    boolean isEnemy = TeamController.getTeam(damager.getUniqueId())
                            != TeamController.getTeam(hit.getUniqueId());

                    // Enemies
                    if (isEnemy) {
                        // Effects for enemies
                        if (potionType.equals(PotionEffectType.POISON)
                                || potionType.equals(PotionEffectType.HARM)
                                || potionType.equals(PotionEffectType.CONFUSION)
                                || potionType.equals(PotionEffectType.BLINDNESS)
                                || potionType.equals(PotionEffectType.SLOW)
                                || potionType.equals(PotionEffectType.SLOW_DIGGING)
                                || potionType.equals(PotionEffectType.HUNGER)
                                || potionType.equals(PotionEffectType.WEAKNESS)
                                || potionType.equals(PotionEffectType.GLOWING)
                                || potionType.equals(PotionEffectType.LEVITATION)
                                || potionType.equals(PotionEffectType.SLOW_FALLING)
                                || potionType.equals(PotionEffectType.WITHER)) {
                            AssistKill.addDamager(hit.getUniqueId(), damager.getUniqueId(), 60);
                            Bukkit.getScheduler().runTask(Main.plugin, () -> hit.addPotionEffect(effect));
                        }
                    }

                    // Healing Potions
                    if (hit.getHealth() != baseHealth
                            && (potionType.equals(PotionEffectType.HEAL)
                            || potionType.equals(PotionEffectType.HEALTH_BOOST)
                            || potionType.equals(PotionEffectType.REGENERATION))) {
                        Bukkit.getScheduler().runTask(Main.plugin, () -> hit.addPotionEffect(effect));
                        if (hit.getPlayer() != damager && !isEnemy)
                            UpdateStats.addHeals(damager.getUniqueId(), 2);
                    // Friendly Potions
                    } else if (potionType.equals(PotionEffectType.SPEED)
                            || potionType.equals(PotionEffectType.JUMP)
                            || potionType.equals(PotionEffectType.WATER_BREATHING)
                            || potionType.equals(PotionEffectType.FAST_DIGGING)
                            || potionType.equals(PotionEffectType.DAMAGE_RESISTANCE)
                            || potionType.equals(PotionEffectType.DOLPHINS_GRACE)
                            || potionType.equals(PotionEffectType.LEVITATION)
                            || potionType.equals(PotionEffectType.SLOW_FALLING)
                            || potionType.equals(PotionEffectType.INCREASE_DAMAGE)
                            || potionType.equals(PotionEffectType.INVISIBILITY)
                            || potionType.equals(PotionEffectType.GLOWING)
                            || potionType.equals(PotionEffectType.NIGHT_VISION)
                            || potionType.equals(PotionEffectType.CONDUIT_POWER)
                            || potionType.equals(PotionEffectType.FIRE_RESISTANCE)) {
                        Bukkit.getScheduler().runTask(Main.plugin, () -> hit.addPotionEffect(effect));
                        if (hit.getPlayer() != damager && !isEnemy)
                            UpdateStats.addSupports(damager.getUniqueId(), 3);
                    }
                }
            }
        });
    }


    /**
     * Place a brewing stand
     * @param e The event called when placing a brewing stand
     */
    @EventHandler (priority = EventPriority.HIGHEST)
    public void onPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(p.getUniqueId())) {
            return;
        }

        if (Objects.equals(Kit.equippedKits.get(p.getUniqueId()).name, name) &&
                e.getBlockPlaced().getType() == Material.BREWING_STAND) {

            // Destroy old stand
            destroyStand(p);

            // Place new stand
            e.setCancelled(false);
            stands.put(p, e.getBlockPlaced());
            Messenger.sendActionInfo("You placed down your brewing stand!", p);
        }
    }

    /**
     * Destroy a cauldron
     * @param e The event called when left-clicking a cauldron
     */
    @EventHandler
    public void onRemove(PlayerInteractEvent e) {
        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(e.getPlayer().getUniqueId())) {
            return;
        }

        if (e.getAction() == Action.LEFT_CLICK_BLOCK &&
                Objects.requireNonNull(e.getClickedBlock()).getType() == Material.BREWING_STAND) {
            Player destroyer = e.getPlayer();
            Player placer = getPlacer(e.getClickedBlock());

            // Pick up own brewing stand
            if (Objects.equals(destroyer, placer)) {
                if (!placer.getInventory().getItemInMainHand().getType().equals(Material.GLASS_BOTTLE)) {
                    destroyStand(placer);
                    Messenger.sendActionInfo("You took back your brewing stand!", placer);
                    destroyer.playSound(e.getClickedBlock().getLocation(), Sound.AMBIENT_UNDERWATER_ENTER, 3, 1);
                    // Can only hold 1 brewing stand at a time
                    PlayerInventory inv = destroyer.getInventory();
                    if (inv.getItemInOffHand().getType() != Material.BREWING_STAND &&
                            !inv.contains(Material.BREWING_STAND)) {
                        if (!ActiveData.getData(destroyer.getUniqueId()).hasVote("sword")) {
                            destroyer.getInventory().addItem(stand);
                        } else {
                            destroyer.getInventory().addItem(standVoted);
                        }
                    }
                }

                // Destroy enemy brewing stand
            } else if (placer != null &&
                    TeamController.getTeam(destroyer.getUniqueId())
                            != TeamController.getTeam(placer.getUniqueId())){
                destroyStand(placer);
                destroyer.playSound(e.getClickedBlock().getLocation(), Sound.AMBIENT_UNDERWATER_ENTER , 5, 1);
                Messenger.sendActionWarning("You destroyed " + NameTag.mmUsername(placer) + "'s brewing stand!", destroyer);
            }
        }
    }

    /**
     * Destroy a player's brewing stand when they click an enderchest.
     * @param event The event called when an off-cooldown player interacts with an enderchest
     */
    @EventHandler
    public void onClickEnderchest(EnderchestEvent event) {
        destroyStand(event.getPlayer());
    }

    /**
     * Destroy a player's brewing stand when they die
     * @param e The event called when a player dies
     */
    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        destroyStand(e.getEntity());
    }

    /**
     * Destroy a player's brewing stand when they leave the game
     * @param e The event called when a player leaves the game
     */
    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        destroyStand(e.getPlayer());
    }

    /**
     * Destroy the player's brewing stand if present
     * @param p The player whose brewing stand to destroy
     */
    private void destroyStand(Player p) {
        if(stands.containsKey(p)) {
            stands.get(p).setType(Material.AIR);
            stands.remove(p);
        }
    }

    /**
     * Get the placer of a brewing stand
     * @param stand The brewing stand whose placer to find
     * @return The placer of the brewing stand, null of not placed by a fire archer
     */
    private Player getPlacer(Block stand) {
        return stands.entrySet().stream()
                .filter(entry -> Objects.equals(entry.getValue(), stand))
                .findFirst().map(Map.Entry::getKey)
                .orElse(null);
    }

    /**
     * Disable death message
     * Auto-respawn the player
     * Apply stat changes
     * @param event The event called when a player dies
     */
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (event.getEntity().getKiller() == null) { return; }
       if (Objects.equals(Kit.equippedKits.get(event.getEntity().getKiller().getUniqueId()).name, name)) {
           // Prevent using in lobby
           if (InCombat.isPlayerInLobby(event.getEntity().getKiller().getUniqueId())) {
               return;
           }
           event.getEntity().getKiller().getInventory().addItem(randomPositivePotion());
           event.getEntity().getKiller().getInventory().addItem(randomNegativePotion());
        }
    }


    /**
     * prevent bottles from being filled up with water
     * @param e The event called when right-clicking a brewing stand with a bottle
     */
    @EventHandler
    public void onUseBottleOnWater(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getItem() == null) { return; }
        ItemStack usedItem = e.getItem();

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(p.getUniqueId())) {
            return;
        }

        if (usedItem.getType() == Material.GLASS_BOTTLE) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() == Material.WATER) {
            e.setCancelled(true);
          } else if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() == Material.CAULDRON) {
            e.setCancelled(true);
          } else if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() == Material.BREWING_STAND) {
            e.setCancelled(true);
        }
        }
    }

    /**
     * brew a potion
     * @param e The event called when right-clicking a brewing stand with a bottle
     */
    @EventHandler
    public void onUseStand(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (e.getItem() == null) { return; }
        ItemStack usedItem = e.getItem();

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(player.getUniqueId())) {
            return;
        }

        if (!Objects.equals(Kit.equippedKits.get(player.getUniqueId()).name, name) ||
                Objects.requireNonNull(e.getClickedBlock()).getType() == Material.BREWING_STAND) {
            return;
        }

        // Check if an alchemist tries to brew a potion, while off-cooldown
        if ((e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_BLOCK) &&
                usedItem != null && usedItem.getType() == Material.GLASS_BOTTLE &&
                player.getCooldown(Material.GLASS_BOTTLE) == 0) {

            // Check if the player may brew potions
            Player placer = getPlacer(e.getClickedBlock());
            if (placer != null && placer == player) {

                if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {

                    PlayerInventory inv = player.getInventory();
                    // Brew a potion
                    player.setCooldown(Material.GLASS_BOTTLE, 30);
                    usedItem.setAmount(usedItem.getAmount() - 1);
                    inv.addItem(randomPositivePotion());
                    player.playSound(player.getLocation(), Sound.BLOCK_WATER_AMBIENT, 1, 1f);
                    Messenger.sendActionSuccess("You brewed a positive potion!", player);

                } else if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
                    PlayerInventory inv = player.getInventory();
                    // Brew a potion
                    player.setCooldown(Material.GLASS_BOTTLE, 30);
                    usedItem.setAmount(usedItem.getAmount() - 1);
                    inv.addItem(randomNegativePotion());
                    player.playSound(player.getLocation(), Sound.BLOCK_WATER_AMBIENT, 1, 1f);
                    Messenger.sendActionSuccess("You brewed a negative potion!", player);
                }
            }
        }
    }
       //------------------------------------------------Potions------------------------------------------------\\

    final java.util.Random rand = new java.util.Random();

    /**
     * @return The item stack for a positive potion
     */
    public ItemStack randomPositivePotion() {

        int types = rand.nextInt(16);
        int amount = rand.nextInt(2);
        int time = rand.nextInt(3501) + 100;
        int smallTime = rand.nextInt(121) + 100;
        int mediumTime = rand.nextInt(341) + 100;
        int amplifier = rand.nextInt(2);
        //int bigAmplifier = rand.nextInt(5);
        int amplifierRegen = rand.nextInt(9);
        //int harmingAmplifier = rand.nextInt(3);

        ItemStack itemStack = new ItemStack(Material.SPLASH_POTION);
        PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
        assert potionMeta != null;
        for (int i = 0; i <= amount; i++) {

            switch (types) {
                case 0:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.HEAL, 1, 5), true);
                    potionMeta.setColor(Color.RED);
                    potionMeta.displayName(Component.text("Instant Healing I", NamedTextColor.RED));
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 1:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.HEAL, 1, 7), true);
                    potionMeta.setColor(Color.RED);
                    potionMeta.displayName(Component.text("Instant Healing II", NamedTextColor.RED));
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 2:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.REGENERATION, smallTime, amplifierRegen), true);
                    potionMeta.setColor(Color.fromRGB(119, 126, 225));
                    potionMeta.displayName(Component.text("Regeneration I", TextColor.color(119, 126, 255)));
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 3:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.REGENERATION, mediumTime, amplifierRegen), true);
                    potionMeta.setColor(Color.fromRGB(119, 126, 225));
                    potionMeta.displayName(Component.text("Regeneration II", TextColor.color(119, 126, 255)));
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 4:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.REGENERATION, mediumTime + 100, amplifierRegen), true);
                    potionMeta.setColor(Color.fromRGB(119, 126, 225));
                    potionMeta.displayName(Component.text("Regeneration III", TextColor.color(119, 126, 255)));
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 5:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, time, amplifierRegen), true);
                    potionMeta.setColor(Color.FUCHSIA);
                    potionMeta.displayName(Component.text("Health Boost I", TextColor.color(255, 0, 255)));
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 6:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, time, amplifier), true);
                    potionMeta.setColor(Color.fromRGB(119, 211, 255));
                    potionMeta.displayName(Component.text("Swiftness I", TextColor.color(119, 211, 255)));
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 7:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, time, amplifier), true);
                    potionMeta.setColor(Color.fromRGB(208, 13, 45));
                    potionMeta.displayName(Component.text("Strength I", TextColor.color(208, 13, 45)));
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 8:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, time, 0), true);
                    potionMeta.setColor(Color.fromRGB(27, 50, 102));
                    potionMeta.displayName(Component.text("Night Vision I", TextColor.color(27, 50, 102)));
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 9:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.CONDUIT_POWER, time, 0), true);
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, time, amplifier), true);
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, time, 0), true);
                    potionMeta.setColor(Color.fromRGB(134, 188, 236));
                    potionMeta.displayName(Component.text("Water Affinity I", TextColor.color(134, 188, 236)));
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 10:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, smallTime, amplifier), true);
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.JUMP, mediumTime, amplifier), true);
                    potionMeta.setColor(Color.fromRGB(25, 66, 29));
                    potionMeta.displayName(Component.text("Scout I", TextColor.color(25, 66, 29)));
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 11:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, mediumTime, 2), true);
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, mediumTime, 2), true);
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, mediumTime, 2), true);
                    potionMeta.setColor(Color.fromRGB(255, 112, 149));
                    potionMeta.displayName(Component.text("Berserker I", TextColor.color(255, 112, 149)));
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 12:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, mediumTime, 0), true);
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, mediumTime, 2), true);
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SLOW, mediumTime, 2), true);
                    potionMeta.setColor(Color.fromRGB(188, 111, 126));
                    potionMeta.displayName(Component.text("Halberdier I", TextColor.color(188, 111, 126)));
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 13:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, 220, 3), true);
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 220, 1), true);
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.JUMP, 220, 0), true);
                    potionMeta.setColor(Color.fromRGB(0, 255, 34));
                    potionMeta.displayName(Component.text("Vanguard I", TextColor.color(0, 255, 34)));
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 14:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, 160, 5), true);
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 160, 2), true);
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.JUMP, 160, 1), true);
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 160, 3), true);
                    potionMeta.setColor(Color.fromRGB(0, 255, 34));
                    potionMeta.displayName(Component.text("Vanguard II", TextColor.color(0, 255, 34)));
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 15:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, mediumTime, 0), false);
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, mediumTime * 2, amplifier), false);
                    potionMeta.setColor(Color.fromRGB(252,249,173));
                    potionMeta.displayName(Component.text("Divine Potion", TextColor.color(252, 249, 173)));
                    itemStack.setItemMeta(potionMeta);
                    break;
                default:
                    break;
            }
        }
        return itemStack;
    }

    /**
     * @return The ItemStack for a random negative potion
     */
    public ItemStack randomNegativePotion() {

        int types = rand.nextInt(12);
        int amount = rand.nextInt(2);
        int time = rand.nextInt(3501) + 100;
        int smallTime = rand.nextInt(121) + 100;
        int mediumTime = rand.nextInt(341) + 100;
        int amplifier = rand.nextInt(2);
        int bigAmplifier = rand.nextInt(5);
        //int amplifierRegen = rand.nextInt(9);
        //int harmingAmplifier = rand.nextInt(3);


        ItemStack itemStack = new ItemStack(Material.SPLASH_POTION);
        PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
        assert potionMeta != null;
        for (int i = 0; i <= amount; i++) {

            switch (types) {
                case 0:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.HARM, 1, 0), false);
                    potionMeta.setColor(Color.fromRGB(68,20,68));
                    potionMeta.displayName(Component.text("Instant Damage I", TextColor.color(68, 20, 68)));
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 1:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.HARM, 1, 0), false);
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.WITHER, smallTime, amplifier), false);
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.POISON, smallTime, amplifier), false);
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.CONFUSION, smallTime, amplifier), false);
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SLOW, smallTime, amplifier), false);
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, smallTime, amplifier), false);
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.WEAKNESS, smallTime, amplifier), false);
                    potionMeta.setColor(Color.fromRGB(143, 54, 143));
                    potionMeta.displayName(Component.text("Death's Breath I", TextColor.color(143, 54, 143)));
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 2:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.CONFUSION, smallTime, bigAmplifier), false);
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.POISON, smallTime, amplifier), false);
                    potionMeta.setColor(Color.fromRGB(151,202,149));
                    potionMeta.displayName(Component.text("Confusing Poison I", TextColor.color(151, 202, 149)));
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 3:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.BLINDNESS, smallTime, amplifier), false);
                    potionMeta.setColor(Color.fromRGB(7,9,30));
                    potionMeta.displayName(Component.text("Blindness I", TextColor.color(248, 246, 225)));
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 4:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SLOW, mediumTime, amplifier), false);
                    potionMeta.setColor(Color.fromRGB(160,57,250));
                    potionMeta.displayName(Component.text("Slowness I", TextColor.color(160, 57, 250)));
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 5:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.GLOWING, time, 0), true);
                    potionMeta.setColor(Color.fromRGB(252,249,173));
                    potionMeta.displayName(Component.text("Glowing I", TextColor.color(252, 249, 173)));
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 6:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.WEAKNESS, mediumTime + 100, amplifier), false);
                    potionMeta.setColor(Color.fromRGB(152,131,117));
                    potionMeta.displayName(Component.text("Weakness II", TextColor.color(152, 121, 117)));
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 7:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.WITHER, smallTime, amplifier), false);
                    potionMeta.setColor(Color.fromRGB(40,37,36));
                    potionMeta.displayName(Component.text("Wither I", TextColor.color(215, 218, 219)));
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 8:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.POISON, smallTime, amplifier), false);
                    potionMeta.setColor(Color.fromRGB(16,137,32));
                    potionMeta.displayName(Component.text("Poison I", TextColor.color(16, 137, 32)));
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 9:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.BLINDNESS, smallTime, amplifier), false);
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.WEAKNESS, smallTime, amplifier), false);
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SLOW, smallTime, amplifier), false);
                    potionMeta.setColor(Color.fromRGB(212,250,57));
                    potionMeta.displayName(Component.text("Maceman I", TextColor.color(212, 250, 57)));
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 10:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.HARM, 1, 0), false);
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.WITHER, smallTime, amplifier), false);
                    potionMeta.setColor(Color.fromRGB(40,37,36));
                    potionMeta.displayName(Component.text("Wolf Bite I", TextColor.color(40, 37, 36)));
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 11:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.WEAKNESS, smallTime, bigAmplifier), false);
                    potionMeta.setColor(Color.fromRGB(152,131,117));
                    potionMeta.displayName(Component.text("Weakness I", TextColor.color(152, 131, 117)));
                    itemStack.setItemMeta(potionMeta);
                    break;
                default:
                    break;
            }
        }
        return itemStack;
    }

    /**
     * @return The lore to add to the kit gui item
     */
    @Override
    public ArrayList<Component> getGuiDescription() {
        ArrayList<Component> kitLore = new ArrayList<>();
        kitLore.add(Component.text("A support kit that makes use", NamedTextColor.GRAY));
        kitLore.add(Component.text("of many different kinds of potions", NamedTextColor.GRAY));
        kitLore.addAll(getBaseStats(health, regen, meleeDamage, ladderCount));
        kitLore.add(Component.empty());
        kitLore.add(Component.text("Effects:", NamedTextColor.DARK_PURPLE));
        kitLore.add(Component.text("- Speed I", NamedTextColor.GRAY));
        kitLore.add(Component.empty());
        kitLore.add(Component.text("Active:", NamedTextColor.GOLD));
        kitLore.add(Component.text("- Can craft potions", NamedTextColor.GRAY));
        kitLore.add(Component.empty());
        kitLore.add(Component.text("Passive:", NamedTextColor.DARK_GREEN));
        kitLore.add(Component.text("- Gets two potions on kill", NamedTextColor.GRAY));
        kitLore.add(Component.text("- Can see players' health", NamedTextColor.GRAY));
        return kitLore;
    }

}
