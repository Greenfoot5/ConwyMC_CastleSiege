package me.huntifi.castlesiege.kits.kits.donator_kits;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.castlesiege.database.UpdateStats;
import me.huntifi.castlesiege.events.EnderchestEvent;
import me.huntifi.castlesiege.events.combat.AssistKill;
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

import java.util.*;

public class Alchemist extends DonatorKit implements Listener {

    public static HashMap<Player, Block> stands = new HashMap<>();
    private final ItemStack stand;
    private final ItemStack standVoted;

    public Alchemist() {
        super("Alchemist", 230, 2, 10000, 10);
        super.canSeeHealth = true;

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Brewing Stand
        stand = ItemCreator.weapon(new ItemStack(Material.BREWING_STAND),
                ChatColor.LIGHT_PURPLE + "Brewing Stand", Arrays.asList("",
                        ChatColor.AQUA + "Place the brewing stand down, then",
                        ChatColor.AQUA + "right click it to get a positive potion.", "",
                        ChatColor.AQUA + "left click it to get a negative potion.",
                        ChatColor.AQUA + "(tip): This brewing stand is a weapon, so you",
                        ChatColor.AQUA + "can beat your enemies to death with it."),
                Collections.singletonList(new Tuple<>(Enchantment.LOYALTY, 1)), 36);
        es.hotbar[0] = stand;
        // Voted Brewing Stand
        standVoted = ItemCreator.weapon(new ItemStack(Material.BREWING_STAND),
                ChatColor.LIGHT_PURPLE + "Brewing Stand", Arrays.asList("",
                        ChatColor.AQUA + "Place the brewing stand down, then",
                        ChatColor.AQUA + "right click it to get a potion.", "",
                        ChatColor.AQUA + "(tip): This brewing stand is a weapon, so you",
                        ChatColor.AQUA + "can beat your enemies to death with it."),
                Arrays.asList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0),
                        new Tuple<>(Enchantment.LOYALTY, 1)), 38);
        es.votedWeapon = new Tuple<>(standVoted, 0);

        // Chestplate
        es.chest = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE),
                ChatColor.GREEN + "Leather Chestplate", null, null,
                Color.fromRGB(226, 165, 43));

        // Leggings
        es.legs = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_LEGGINGS),
                ChatColor.GREEN + "Leather Leggings", null, null,
                Color.fromRGB(226, 173, 65));

        // Boots
        es.feet = ItemCreator.item(new ItemStack(Material.GOLDEN_BOOTS),
                ChatColor.GREEN + "Golden Boots", null, null);
        // Voted Boots
        es.votedFeet = ItemCreator.item(new ItemStack(Material.GOLDEN_BOOTS),
                ChatColor.GREEN + "Golden Boots",
                Collections.singletonList(ChatColor.AQUA + "- voted: Depth Strider II"),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)));

        es.hotbar[1] = new ItemStack(Material.GLASS_BOTTLE, 6);
        es.votedLadders = new Tuple<>(new ItemStack(Material.GLASS_BOTTLE, 7), 1);

        super.equipment = es;

        // Perm Potion Effect
        super.potionEffects.add(new PotionEffect(PotionEffectType.SPEED, 999999, 0));

        super.killMessage[0] = " brewed ";
        super.killMessage[1] = " into a potion";
        super.deathMessage[0] = "You were brewed into a potion by ";
    }


    @EventHandler (priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onThrownPotion(PotionSplashEvent e) {
        // Is the potion thrown by an alchemist?
        if (e.getPotion().getShooter() instanceof Player) {
            Player damager = (Player) e.getPotion().getShooter();
            if (Objects.equals(Kit.equippedKits.get(damager.getUniqueId()).name, name)) {
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
         }
    }


    /**
     * Place a cauldron
     * @param e The event called when placing a cauldron
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

            // Destroy old cauldron
            destroyStand(p);

            // Place new cauldron
            e.setCancelled(false);
            stands.put(p, e.getBlockPlaced());
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                    TextComponent.fromLegacyText(ChatColor.AQUA + "You placed down your brewing stand!"));
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
                e.getClickedBlock().getType() == Material.BREWING_STAND) {
            Player p = e.getPlayer();
            Player q = getPlacer(e.getClickedBlock());

            // Pick up own brewing stand
            if (Objects.equals(p, q)) {
                if (!q.getInventory().getItemInMainHand().getType().equals(Material.GLASS_BOTTLE)) {
                    destroyStand(q);
                    q.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                            TextComponent.fromLegacyText(ChatColor.AQUA + "You took back your brewing stand!"));
                    p.playSound(e.getClickedBlock().getLocation(), Sound.AMBIENT_UNDERWATER_ENTER, 3, 1);
                    // Can only hold 1 brewing stand at a time
                    PlayerInventory inv = p.getInventory();
                    if (inv.getItemInOffHand().getType() != Material.BREWING_STAND &&
                            !inv.contains(Material.BREWING_STAND)) {
                        if (!ActiveData.getData(p.getUniqueId()).hasVote("sword")) {
                            p.getInventory().addItem(stand);
                        } else {
                            p.getInventory().addItem(standVoted);
                        }
                    }
                }

                // Destroy enemy brewing stand
            } else if (q != null &&
                    TeamController.getTeam(p.getUniqueId())
                            != TeamController.getTeam(q.getUniqueId())){
                destroyStand(q);
                p.playSound(e.getClickedBlock().getLocation(), Sound.AMBIENT_UNDERWATER_ENTER , 5, 1);
                p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                        TextComponent.fromLegacyText(ChatColor.RED + "You destroyed " + q.getName() + "'s brewing stand!"));
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
        Player p = e.getPlayer();
        if (e.getItem() == null) { return; }
        ItemStack usedItem = e.getItem();

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(p.getUniqueId())) {
            return;
        }

        // Check if an alchemist tries to brew a potion, while off-cooldown
        if (Objects.equals(Kit.equippedKits.get(p.getUniqueId()).name, name) && (e.getAction() ==
                Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_BLOCK) &&
                e.getClickedBlock().getType() == Material.BREWING_STAND &&
                usedItem != null &&
                usedItem.getType() == Material.GLASS_BOTTLE &&
                p.getCooldown(Material.GLASS_BOTTLE) == 0) {

            // Check if the player may brew potions
            Player q = getPlacer(e.getClickedBlock());
            if (q != null && q == p) {

                if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {

                    PlayerInventory inv = p.getInventory();
                    // Brew a potion
                    p.setCooldown(Material.GLASS_BOTTLE, 30);
                    usedItem.setAmount(usedItem.getAmount() - 1);
                    inv.addItem(randomPositivePotion());
                    p.playSound(p.getLocation(), Sound.BLOCK_WATER_AMBIENT, 1, 1f);
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                            TextComponent.fromLegacyText(ChatColor.AQUA + "You brewed a positive potion!"));

                } else if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
                    PlayerInventory inv = p.getInventory();
                    // Brew a potion
                    p.setCooldown(Material.GLASS_BOTTLE, 30);
                    usedItem.setAmount(usedItem.getAmount() - 1);
                    inv.addItem(randomNegativePotion());
                    p.playSound(p.getLocation(), Sound.BLOCK_WATER_AMBIENT, 1, 1f);
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                            TextComponent.fromLegacyText(ChatColor.AQUA + "You brewed a negative potion!"));
                }
            }
        }
    }
       //------------------------------------------------Potions------------------------------------------------\\

    java.util.Random rand = new java.util.Random();

    public ItemStack randomPositivePotion() {

        int types = rand.nextInt(24);
        int amount = rand.nextInt(2);
        int time = rand.nextInt(3501) + 100;
        int smallTime = rand.nextInt(121) + 100;
        int mediumTime = rand.nextInt(341) + 100;
        int amplifier = rand.nextInt(2);
        int bigAmplifier = rand.nextInt(5);
        int amplifierRegen = rand.nextInt(9);
        int harmingAmplifier = rand.nextInt(3);

        ItemStack itemStack = new ItemStack(Material.SPLASH_POTION);
        PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
        assert potionMeta != null;
        for (int i = 0; i <= amount; i++) {

            switch (types) {
                case 0:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.HEAL, 1, amplifierRegen), true);
                    potionMeta.setColor(Color.RED);
                    potionMeta.setDisplayName(ChatColor.RED + "Instant Healing I");
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 1:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.HEAL, 1, 8), true);
                    potionMeta.setColor(Color.RED);
                    potionMeta.setDisplayName(ChatColor.RED + "Instant Healing II");
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 2:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.REGENERATION, smallTime, amplifierRegen), true);
                    potionMeta.setColor(Color.FUCHSIA);
                    potionMeta.setDisplayName(ChatColor.RED + "Regeneration I");
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 3:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.REGENERATION, mediumTime, amplifierRegen), true);
                    potionMeta.setColor(Color.fromRGB(119, 126, 225));
                    potionMeta.setDisplayName(ChatColor.BLUE + "Regeneration II");
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 4:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, time, amplifierRegen), true);
                    potionMeta.setColor(Color.FUCHSIA);
                    potionMeta.setDisplayName(ChatColor.RED + "Health Boost I");
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 5:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, time, amplifier), true);
                    potionMeta.setColor(Color.fromRGB(119, 211, 255));
                    potionMeta.setDisplayName(ChatColor.BLUE + "Swiftness I");
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 6:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, smallTime, 5), true);
                    potionMeta.setColor(Color.fromRGB(119, 211, 255));
                    potionMeta.setDisplayName(ChatColor.BLUE + "Hyperspeed I");
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 7:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, time, amplifier), true);
                    potionMeta.setColor(Color.fromRGB(229, 227, 173));
                    potionMeta.setDisplayName(ChatColor.YELLOW + "Haste I");
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 8:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, time, 0), true);
                    potionMeta.setColor(Color.fromRGB(216, 174, 118));
                    potionMeta.setDisplayName(ChatColor.YELLOW + "Resistance I");
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 9:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, time, amplifier), true);
                    potionMeta.setColor(Color.fromRGB(208, 13, 45));
                    potionMeta.setDisplayName(ChatColor.DARK_RED + "Strength I");
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 10:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.INVISIBILITY, time, 0), true);
                    potionMeta.setColor(Color.fromRGB(146, 93, 143));
                    potionMeta.setDisplayName(ChatColor.WHITE + "Invisibility I");
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 11:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, time, 0), true);
                    potionMeta.setColor(Color.fromRGB(27, 50, 102));
                    potionMeta.setDisplayName(ChatColor.DARK_BLUE + "Night Vision I");
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 12:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.CONDUIT_POWER, time, 0), true);
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, time, amplifier), true);
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, time, 0), true);
                    potionMeta.setColor(Color.fromRGB(134, 188, 236));
                    potionMeta.setDisplayName(ChatColor.DARK_AQUA + "Water Affinity I");
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 13:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, time, 0), true);
                    potionMeta.setColor(Color.fromRGB(252, 180, 13));
                    potionMeta.setDisplayName(ChatColor.GOLD + "Fire Resistance I");
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 14:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, smallTime, amplifier), true);
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.JUMP, mediumTime, amplifier), true);
                    potionMeta.setColor(Color.fromRGB(0,255,34));
                    potionMeta.setDisplayName(ChatColor.DARK_PURPLE + "Scout I");
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 15:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.JUMP, time, amplifier), true);
                    potionMeta.setColor(Color.fromRGB(0,255,34));
                    potionMeta.setDisplayName(ChatColor.DARK_PURPLE + "Jump Boost I");
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 16:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, mediumTime, 1), true);
                    potionMeta.setColor(Color.fromRGB(212,250,57));
                    potionMeta.setDisplayName(ChatColor.DARK_PURPLE + "Resistance I");
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 17:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, mediumTime, 2), true);
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, mediumTime, 2), true);
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, mediumTime, 2), true);
                    potionMeta.setColor(Color.RED);
                    potionMeta.setDisplayName(ChatColor.DARK_PURPLE + "Berserker I");
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 18:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, mediumTime, 0), true);
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, mediumTime, 2), true);
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SLOW, mediumTime, 2), true);
                    potionMeta.setColor(Color.AQUA);
                    potionMeta.setDisplayName(ChatColor.DARK_PURPLE + "Halberdier I");
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 19:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, 220, 3), true);
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 220, 1), true);
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.JUMP, 220, 0), true);
                    potionMeta.setColor(Color.AQUA);
                    potionMeta.setDisplayName(ChatColor.DARK_PURPLE + "Vanguard I");
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 20:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.LEVITATION, mediumTime, amplifier), false);
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, mediumTime * 2, amplifier), false);
                    potionMeta.setColor(Color.fromRGB(152,131,117));
                    potionMeta.setDisplayName(ChatColor.DARK_PURPLE + "Gliding Potion");
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 21:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, mediumTime, 0), false);
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, mediumTime * 2, amplifier), false);
                    potionMeta.setColor(Color.fromRGB(252,249,173));
                    potionMeta.setDisplayName(ChatColor.DARK_PURPLE + "Divine I");
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 22:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, mediumTime, 1), false);
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, mediumTime * 2, amplifier), false);
                    potionMeta.setColor(Color.fromRGB(252,249,173));
                    potionMeta.setDisplayName(ChatColor.DARK_PURPLE + "Divine II");
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 23:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, 160, 5), true);
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 160, 2), true);
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.JUMP, 160, 1), true);
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 160, 3), true);
                    potionMeta.setColor(Color.AQUA);
                    potionMeta.setDisplayName(ChatColor.DARK_PURPLE + "Vanguard II");
                    itemStack.setItemMeta(potionMeta);
                    break;
                default:
                    break;
            }
        }
        return itemStack;
    }

    public ItemStack randomNegativePotion() {

        int types = rand.nextInt(16);
        int amount = rand.nextInt(2);
        int time = rand.nextInt(3501) + 100;
        int smallTime = rand.nextInt(121) + 100;
        int mediumTime = rand.nextInt(341) + 100;
        int amplifier = rand.nextInt(2);
        int bigAmplifier = rand.nextInt(5);
        int amplifierRegen = rand.nextInt(9);
        int harmingAmplifier = rand.nextInt(3);


        ItemStack itemStack = new ItemStack(Material.SPLASH_POTION);
        PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
        assert potionMeta != null;
        for (int i = 0; i <= amount; i++) {

            switch (types) {
                case 0:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.HARM, 1, amplifier), false);
                    potionMeta.setColor(Color.fromRGB(68,20,68));
                    potionMeta.setDisplayName(ChatColor.DARK_PURPLE + "Instant Damage I");
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 1:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.HARM, 1, amplifier), false);
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.WITHER, smallTime, amplifier), false);
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.POISON, smallTime, amplifier), false);
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.CONFUSION, smallTime, amplifier), false);
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SLOW, smallTime, amplifier), false);
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, smallTime, amplifier), false);
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.WEAKNESS, smallTime, amplifier), false);
                    potionMeta.setColor(Color.fromRGB(68,20,68));
                    potionMeta.setDisplayName(ChatColor.DARK_PURPLE + "Death's Breath I");
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 2:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.CONFUSION, smallTime, bigAmplifier), false);
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.POISON, smallTime, amplifier), false);
                    potionMeta.setColor(Color.fromRGB(151,202,149));
                    potionMeta.setDisplayName(ChatColor.DARK_PURPLE + "Confusing Poison I");
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 3:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.BLINDNESS, smallTime, amplifier), false);
                    potionMeta.setColor(Color.fromRGB(7,9,30));
                    potionMeta.setDisplayName(ChatColor.DARK_PURPLE + "Blindness I");
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 4:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.LEVITATION, mediumTime, amplifier), false);
                    potionMeta.setColor(Color.fromRGB(57,250,218));
                    potionMeta.setDisplayName(ChatColor.BLUE + "Levitation I");
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 5:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SLOW, mediumTime, amplifier), false);
                    potionMeta.setColor(Color.fromRGB(160,57,250));
                    potionMeta.setDisplayName(ChatColor.DARK_PURPLE + "Slowness I");
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 6:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, mediumTime, amplifier), false);
                    potionMeta.setColor(Color.fromRGB(57,89,250));
                    potionMeta.setDisplayName(ChatColor.DARK_BLUE + "Slow Falling I");
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 7:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, mediumTime, bigAmplifier), false);
                    potionMeta.setColor(Color.fromRGB(212,250,57));
                    potionMeta.setDisplayName(ChatColor.YELLOW + "Mining Fatigue I");
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 8:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.HUNGER, smallTime, bigAmplifier), false);
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.POISON, smallTime, amplifier), false);
                    potionMeta.setColor(Color.fromRGB(220,191,45));
                    potionMeta.setDisplayName(ChatColor.DARK_GREEN + "Hungering Poison I");
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 9:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.GLOWING, time, 0), true);
                    potionMeta.setColor(Color.fromRGB(252,249,173));
                    potionMeta.setDisplayName(ChatColor.YELLOW + "Glowing I");
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 10:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.WEAKNESS, smallTime, bigAmplifier), false);
                    potionMeta.setColor(Color.fromRGB(152,131,117));
                    potionMeta.setDisplayName(ChatColor.DARK_BLUE + "Weakness I");
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 11:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.WITHER, smallTime, amplifier), false);
                    potionMeta.setColor(Color.fromRGB(40,37,36));
                    potionMeta.setDisplayName(ChatColor.DARK_GRAY + "Wither I");
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 12:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.POISON, smallTime, amplifier), false);
                    potionMeta.setColor(Color.fromRGB(16,137,32));
                    potionMeta.setDisplayName(ChatColor.DARK_GREEN + "Poison I");
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 13:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.BLINDNESS, smallTime, amplifier), false);
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.WEAKNESS, smallTime, amplifier), false);
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SLOW, smallTime, amplifier), false);
                    potionMeta.setColor(Color.fromRGB(212,250,57));
                    potionMeta.setDisplayName(ChatColor.DARK_PURPLE + "Maceman I");
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 14:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.HARM, 1, amplifier), false);
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.WITHER, smallTime, amplifier), false);
                    potionMeta.setColor(Color.fromRGB(40,37,36));
                    potionMeta.setDisplayName(ChatColor.DARK_PURPLE + "Wolf Bite I");
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 15:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.HARM, 2, bigAmplifier), false);
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.WITHER, mediumTime, amplifier), false);
                    potionMeta.setColor(Color.fromRGB(40,37,36));
                    potionMeta.setDisplayName(ChatColor.DARK_PURPLE + "Wolf Bite II");
                    itemStack.setItemMeta(potionMeta);
                    break;
                default:
                    break;
            }
        }
        return itemStack;
    }



}
