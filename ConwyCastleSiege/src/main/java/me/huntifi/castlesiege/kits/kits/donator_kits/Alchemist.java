package me.huntifi.castlesiege.kits.kits.donator_kits;

import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.castlesiege.database.UpdateStats;
import me.huntifi.castlesiege.events.combat.AssistKill;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.events.death.DeathEvent;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.kits.kits.DonatorKit;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.maps.MapController;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
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
        super("Alchemist", 140, 9, 7500);
        super.canSeeHealth = true;

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Firepit
        stand = ItemCreator.weapon(new ItemStack(Material.BREWING_STAND),
                ChatColor.LIGHT_PURPLE + "Brewing Stand", Arrays.asList("",
                        ChatColor.AQUA + "Place the brewing stand down, then",
                        ChatColor.AQUA + "right click it to get a potion.", "",
                        ChatColor.AQUA + "(tip): This brewing stand is a weapon, so you",
                        ChatColor.AQUA + "can beat your enemies to death with it."),
                Collections.singletonList(new Tuple<>(Enchantment.LOYALTY, 1)), 26);
        es.hotbar[0] = stand;
        // Voted Firepit
        standVoted = ItemCreator.weapon(new ItemStack(Material.BREWING_STAND),
                ChatColor.LIGHT_PURPLE + "Brewing Stand", Arrays.asList("",
                        ChatColor.AQUA + "Place the brewing stand down, then",
                        ChatColor.AQUA + "right click it to get a potion.", "",
                        ChatColor.AQUA + "(tip): This brewing stand is a weapon, so you",
                        ChatColor.AQUA + "can beat your enemies to death with it."),
                Arrays.asList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0),
                        new Tuple<>(Enchantment.LOYALTY, 1)), 28);
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

        es.hotbar[1] = new ItemStack(Material.GLASS_BOTTLE, 5);
        es.votedLadders = new Tuple<>(new ItemStack(Material.GLASS_BOTTLE, 6), 1);

        super.equipment = es;

        // Perm Potion Effect
        super.potionEffects.add(new PotionEffect(PotionEffectType.SPEED, 999999, 0));

        super.killMessage[0] = " brewed ";
        super.killMessage[1] = " into a potion";
        super.deathMessage[0] = "You were brewed into a potion by ";
    }


    @EventHandler (priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onThrownPotion(PotionSplashEvent e) {
        if (e.getPotion().getShooter() instanceof Player) {
                for (Entity entity : e.getAffectedEntities()) {
                    if (entity instanceof Player) {
                        Player hit = ((Player) entity).getPlayer();
                        Player damager = (Player) e.getPotion().getShooter();
                        if (Objects.equals(Kit.equippedKits.get(damager.getUniqueId()).name, name)) {
                            e.setCancelled(true);
                                Collection<PotionEffect> effects = e.getPotion().getEffects();

                                for (PotionEffect effect : effects) {
                                    PotionEffectType potionType = effect.getType();

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

                                        //Enemies
                                        if (MapController.getCurrentMap().getTeam(damager.getUniqueId())
                                                != MapController.getCurrentMap().getTeam(entity.getUniqueId()) && ((Player) entity).getPlayer() != damager) {
                                            AssistKill.addDamager(hit.getUniqueId(), damager.getUniqueId(), 60);
                                            DeathEvent.setKiller((Player)entity, damager);
                                            ((Player) entity).addPotionEffect(effect);
                                        }
                                    }

                                    if (potionType.equals(PotionEffectType.HEAL)
                                            || potionType.equals(PotionEffectType.HEALTH_BOOST)
                                            || potionType.equals(PotionEffectType.REGENERATION)) {
                                        if (((Player) entity).getPlayer().getHealth() != baseHealth) {
                                            //Allies
                                            if (MapController.getCurrentMap().getTeam(damager.getUniqueId())
                                                    == MapController.getCurrentMap().getTeam(entity.getUniqueId())) {
                                                ((Player) entity).addPotionEffect(effect);
                                                if (((Player) entity).getPlayer() != damager) {
                                                    UpdateStats.addHeals(damager.getUniqueId(), 2);
                                                }
                                            }
                                        }
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
                                        //Allies
                                        if (MapController.getCurrentMap().getTeam(damager.getUniqueId())
                                                == MapController.getCurrentMap().getTeam(entity.getUniqueId())) {
                                            ((Player) entity).addPotionEffect(effect);
                                            if (((Player) entity).getPlayer() != damager) {
                                                UpdateStats.addSupports(damager.getUniqueId(), 2);
                                            }
                                        }
                                    }
                                }
                        }
                    }
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

            // Pick up own cauldron
            if (Objects.equals(p, q)) {
                destroyStand(q);
                q.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                        TextComponent.fromLegacyText(ChatColor.AQUA + "You took back your brewing stand!"));

                // Can only hold 1 cauldron at a time
                PlayerInventory inv = p.getInventory();
                if (inv.getItemInOffHand().getType() != Material.BREWING_STAND &&
                        !inv.contains(Material.BREWING_STAND)) {
                    if (!ActiveData.getData(p.getUniqueId()).hasVote("sword")) {
                        p.getInventory().addItem(stand);
                    } else {
                        p.getInventory().addItem(standVoted);
                    }
                }

                // Destroy enemy firepit
            } else if (q != null &&
                    MapController.getCurrentMap().getTeam(p.getUniqueId())
                            != MapController.getCurrentMap().getTeam(q.getUniqueId())){
                destroyStand(q);
                p.playSound(e.getClickedBlock().getLocation(), Sound.AMBIENT_UNDERWATER_ENTER , 5, 1);
                p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                        TextComponent.fromLegacyText(ChatColor.RED + "You destroyed " + q.getName() + "'s brewing stand!"));
            }
        }
    }


    /**
     * Destroy the player's firepit if present
     * @param p The player whose firepit to destroy
     */
    private void destroyStand(Player p) {
        if(stands.containsKey(p)) {
            stands.get(p).setType(Material.AIR);
            stands.remove(p);
        }
    }

    /**
     * Get the placer of a firepit
     * @param cauldron The firepit whose placer to find
     * @return The placer of the firepit, null of not placed by a fire archer
     */
    private Player getPlacer(Block cauldron) {
        return stands.entrySet().stream()
                .filter(entry -> Objects.equals(entry.getValue(), cauldron))
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
           event.getEntity().getKiller().getInventory().addItem(randomPotion());
        }
    }


    /**
     * prevent bottles from being filled up with water
     * @param e The event called when right-clicking a cauldron with an arrow
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
     * @param e The event called when right-clicking a cauldron with an arrow
     */
    @EventHandler
    public void onUseCauldron(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getItem() == null) { return; }
        ItemStack usedItem = e.getItem();

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(p.getUniqueId())) {
            return;
        }

        // Check if a fire archer tries to light an arrow, while off-cooldown
        if (Objects.equals(Kit.equippedKits.get(p.getUniqueId()).name, name) &&
                e.getAction() == Action.RIGHT_CLICK_BLOCK &&
                e.getClickedBlock().getType() == Material.BREWING_STAND &&
                usedItem != null &&
                usedItem.getType() == Material.GLASS_BOTTLE &&
                p.getCooldown(Material.GLASS_BOTTLE) == 0) {

            // Check if the player may brew potions
            Player q = getPlacer(e.getClickedBlock());
            if (q != null && q == p) {

                PlayerInventory inv = p.getInventory();
                    // Light an arrow
                    p.setCooldown(Material.GLASS_BOTTLE, 30);
                    usedItem.setAmount(usedItem.getAmount() - 1);
                    inv.addItem(randomPotion());
                    p.playSound(p.getLocation(), Sound.BLOCK_WATER_AMBIENT, 1, 1f);
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                            TextComponent.fromLegacyText(ChatColor.AQUA + "You brewed a potion!"));

            }
        }
    }
       //------------------------------------------------Potions------------------------------------------------\\

    java.util.Random rand = new java.util.Random();

    public ItemStack randomPotion() {

        int types = rand.nextInt(36);
        int amount = rand.nextInt(2);
        int time = rand.nextInt(3601);
        int smallTime = rand.nextInt(221);
        int mediumTime = rand.nextInt(441);
        int amplifier = rand.nextInt(2);
        int bigAmplifier = rand.nextInt(5);
        int amplifierRegen = rand.nextInt(9);


        ItemStack itemStack = new ItemStack(Material.SPLASH_POTION);
        PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
        assert potionMeta != null;
        for (int i = 0; i <= amount; i++) {

            switch (types) {
                case 0:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.HEAL, 1, amplifierRegen), true);
                    potionMeta.setColor(Color.RED);
                    potionMeta.setDisplayName(ChatColor.RED + "Healing Potion");
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 1:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.HEAL, 1, 8), true);
                    potionMeta.setColor(Color.RED);
                    potionMeta.setDisplayName(ChatColor.RED + "Healing Potion");
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 2:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.REGENERATION, smallTime, amplifierRegen), true);
                    potionMeta.setColor(Color.FUCHSIA);
                    potionMeta.setDisplayName(ChatColor.RED + "Regeneration Potion");
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 3:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, time, amplifierRegen), true);
                    potionMeta.setColor(Color.FUCHSIA);
                    potionMeta.setDisplayName(ChatColor.RED + "Health Boost Potion");
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 4:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, time, amplifier), true);
                    potionMeta.setColor(Color.fromRGB(119,211,255));
                    potionMeta.setDisplayName(ChatColor.BLUE + "Speed Potion");
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 5:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.REGENERATION, mediumTime, amplifier), true);
                    potionMeta.setColor(Color.fromRGB(119,126,225));
                    potionMeta.setDisplayName(ChatColor.BLUE + "Regeneration Potion");
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 6:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, smallTime, 5), true);
                    potionMeta.setColor(Color.fromRGB(119,211,255));
                    potionMeta.setDisplayName(ChatColor.BLUE + "Hyperspeed Potion");
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 7:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, time, amplifier), true);
                    potionMeta.setColor(Color.fromRGB(229,227,173));
                    potionMeta.setDisplayName(ChatColor.YELLOW + "Fast Digging Potion");
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 8:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, time, 0), true);
                    potionMeta.setColor(Color.fromRGB(216,174,118));
                    potionMeta.setDisplayName(ChatColor.YELLOW + "Damage Resistance Potion");
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 9:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, time, amplifier), true);
                    potionMeta.setColor(Color.fromRGB(208,13,45));
                    potionMeta.setDisplayName(ChatColor.DARK_RED + "Strength Potion");
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 10:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.INVISIBILITY, time, 0), true);
                    potionMeta.setColor(Color.fromRGB(146,93,143));
                    potionMeta.setDisplayName(ChatColor.WHITE + "Invisibility Potion");
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 11:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, time, 0), true);
                    potionMeta.setColor(Color.fromRGB(27,50,102));
                    potionMeta.setDisplayName(ChatColor.DARK_BLUE + "Night Vision Potion");
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 12:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.CONDUIT_POWER, time, 0), true);
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, time, amplifier), true);
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, time, 0), true);
                    potionMeta.setColor(Color.fromRGB(134,188,236));
                    potionMeta.setDisplayName(ChatColor.DARK_AQUA + "Ocean Power Potion");
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 13:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, time, 0), true);
                    potionMeta.setColor(Color.fromRGB(252,180,13));
                    potionMeta.setDisplayName(ChatColor.GOLD + "Fire Resistance Potion");
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 14:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.HARM, 1, amplifier), false);
                    potionMeta.setColor(Color.fromRGB(68,20,68));
                    potionMeta.setDisplayName(ChatColor.DARK_PURPLE + "Harming Potion");
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 15:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.CONFUSION, smallTime, bigAmplifier), false);
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.POISON, smallTime, amplifier), false);
                    potionMeta.setColor(Color.fromRGB(151,202,149));
                    potionMeta.setDisplayName(ChatColor.DARK_PURPLE + "Poison Potion");
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 16:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.BLINDNESS, smallTime, amplifier), false);
                    potionMeta.setColor(Color.fromRGB(7,9,30));
                    potionMeta.setDisplayName(ChatColor.DARK_PURPLE + "Blindness Potion");
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 17:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.LEVITATION, mediumTime, amplifier), false);
                    potionMeta.setColor(Color.fromRGB(57,250,218));
                    potionMeta.setDisplayName(ChatColor.BLUE + "Levitation Potion");
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 18:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SLOW, mediumTime, amplifier), false);
                    potionMeta.setColor(Color.fromRGB(160,57,250));
                    potionMeta.setDisplayName(ChatColor.DARK_PURPLE + "Slowness Potion");
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 19:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, mediumTime, amplifier), false);
                    potionMeta.setColor(Color.fromRGB(57,89,250));
                    potionMeta.setDisplayName(ChatColor.DARK_BLUE + "Slow Falling Potion");
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 20:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, mediumTime, bigAmplifier), false);
                    potionMeta.setColor(Color.fromRGB(212,250,57));
                    potionMeta.setDisplayName(ChatColor.YELLOW + "Fatigue Potion");
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 21:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.HUNGER, smallTime, bigAmplifier), false);
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.POISON, smallTime, amplifier), false);
                    potionMeta.setColor(Color.fromRGB(220,191,45));
                    potionMeta.setDisplayName(ChatColor.DARK_GREEN + "Hungering Death Potion");
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 22:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.GLOWING, time, 0), true);
                    potionMeta.setColor(Color.fromRGB(252,249,173));
                    potionMeta.setDisplayName(ChatColor.YELLOW + "Marking Potion");
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 23:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.WEAKNESS, smallTime, bigAmplifier), false);
                    potionMeta.setColor(Color.fromRGB(152,131,117));
                    potionMeta.setDisplayName(ChatColor.DARK_BLUE + "Weakness Potion");
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 24:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.WITHER, smallTime, amplifier), false);
                    potionMeta.setColor(Color.fromRGB(40,37,36));
                    potionMeta.setDisplayName(ChatColor.DARK_GRAY + "Death Potion");
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 25:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.POISON, smallTime, amplifier), false);
                    potionMeta.setColor(Color.fromRGB(16,137,32));
                    potionMeta.setDisplayName(ChatColor.DARK_GREEN + "Poison Potion");
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 26:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.HARM, 1, amplifier), false);
                    potionMeta.setColor(Color.fromRGB(68,20,68));
                    potionMeta.setDisplayName(ChatColor.DARK_PURPLE + "Harming Potion");
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 27:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.LEVITATION, mediumTime, amplifier), false);
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, mediumTime*2, amplifier), false);
                    potionMeta.setColor(Color.fromRGB(152,131,117));
                    potionMeta.setDisplayName(ChatColor.DARK_PURPLE + "Flying Potion");
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 28:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.BLINDNESS, smallTime, amplifier), false);
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.WEAKNESS, smallTime, amplifier), false);
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SLOW, smallTime, amplifier), false);
                    potionMeta.setColor(Color.fromRGB(212,250,57));
                    potionMeta.setDisplayName(ChatColor.DARK_PURPLE + "Maceman Potion");
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 29:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.HARM, 1, amplifier), false);
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.WITHER, smallTime, amplifier), false);
                    potionMeta.setColor(Color.fromRGB(40,37,36));
                    potionMeta.setDisplayName(ChatColor.DARK_PURPLE + "Destruction Potion");
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 30:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, smallTime, amplifier), true);
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.JUMP, mediumTime, amplifier), true);
                    potionMeta.setColor(Color.fromRGB(0,255,34));
                    potionMeta.setDisplayName(ChatColor.DARK_PURPLE + "Bunny Potion");
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 31:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.JUMP, time, amplifier), true);
                    potionMeta.setColor(Color.fromRGB(0,255,34));
                    potionMeta.setDisplayName(ChatColor.DARK_PURPLE + "Jumping Potion");
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 32:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, mediumTime, 1), true);
                    potionMeta.setColor(Color.fromRGB(212,250,57));
                    potionMeta.setDisplayName(ChatColor.DARK_PURPLE + "Immortality Potion");
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 33:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, mediumTime, 2), true);
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, mediumTime, 2), true);
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, mediumTime, 2), true);
                    potionMeta.setColor(Color.RED);
                    potionMeta.setDisplayName(ChatColor.DARK_PURPLE + "Berserker Potion");
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 34:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, mediumTime, 0), true);
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, mediumTime, 2), true);
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SLOW, mediumTime, 2), true);
                    potionMeta.setColor(Color.AQUA);
                    potionMeta.setDisplayName(ChatColor.DARK_PURPLE + "Halberdier Potion");
                    itemStack.setItemMeta(potionMeta);
                    break;
                case 35:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, 220, 3), true);
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 220, 1), true);
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.JUMP, 220, 0), true);
                    potionMeta.setColor(Color.AQUA);
                    potionMeta.setDisplayName(ChatColor.DARK_PURPLE + "Vanguard Potion");
                    itemStack.setItemMeta(potionMeta);
                    break;
                default:
                    break;
            }
        }
        return itemStack;
    }



}
