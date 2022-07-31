package me.huntifi.castlesiege.kits.kits.donator_kits;

import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.castlesiege.database.UpdateStats;
import me.huntifi.castlesiege.events.combat.InCombat;
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
import org.bukkit.block.data.Levelled;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class Alchemist extends DonatorKit implements Listener {

    public static HashMap<Player, Block> cauldrons = new HashMap<>();
    private final ItemStack cauldron;
    private final ItemStack cauldronVoted;

    public Alchemist() {
        super("Alchemist", 140, 9, 100);
        super.canSeeHealth = true;

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Firepit
        cauldron = ItemCreator.weapon(new ItemStack(Material.CAULDRON),
                ChatColor.LIGHT_PURPLE + "cauldron", Arrays.asList("",
                        ChatColor.AQUA + "Place the cauldron down, then",
                        ChatColor.AQUA + "right click it to get a potion.", "",
                        ChatColor.AQUA + "(tip): This cauldron is a weapon, so you",
                        ChatColor.AQUA + "can beat your enemies to death with it."),
                Collections.singletonList(new Tuple<>(Enchantment.KNOCKBACK, 1)), 26);
        es.hotbar[0] = cauldron;
        // Voted Firepit
        cauldronVoted = ItemCreator.weapon(new ItemStack(Material.CAULDRON),
                ChatColor.LIGHT_PURPLE + "cauldron", Arrays.asList("",
                        ChatColor.AQUA + "Place the cauldron down, then",
                        ChatColor.AQUA + "right click it to get a potion.", "",
                        ChatColor.AQUA + "(tip): This cauldron is a weapon, so you",
                        ChatColor.AQUA + "can beat your enemies to death with it."),
                Arrays.asList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0),
                        new Tuple<>(Enchantment.KNOCKBACK, 1)), 28);
        es.votedWeapon = new Tuple<>(cauldronVoted, 1);

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

        es.hotbar[2] = new ItemStack(Material.GLASS_BOTTLE, 5);

        // Ladders
        es.hotbar[1] = new ItemStack(Material.LADDER, 4);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, 6), 3);

        super.equipment = es;

        // Perm Potion Effect
        super.potionEffects.add(new PotionEffect(PotionEffectType.SPEED, 999999, 0));

        super.killMessage[0] = " brewed ";
        super.killMessage[1] = " into a potion";
        super.deathMessage[0] = "You were brewed into a potion by ";
    }


    @EventHandler
    public void onThrownPotion(PotionSplashEvent e) {
        if (e.getPotion().getShooter() instanceof Player) {
                for (Entity entity : e.getAffectedEntities()) {
                    if (entity instanceof Player) {
                        Player damager = (Player) e.getPotion().getShooter();
                        Player hit = (Player) entity;
                        if (Objects.equals(Kit.equippedKits.get(damager.getUniqueId()).name, name)
                                && MapController.getCurrentMap().getTeam(damager.getUniqueId())
                                != MapController.getCurrentMap().getTeam(hit.getUniqueId())) {

                            for (PotionEffect effect : e.getPotion().getEffects()) {
                                if (effect.getType() == PotionEffectType.POISON
                                        || effect.getType() == PotionEffectType.HARM
                                        || effect.getType() == PotionEffectType.CONFUSION
                                        || effect.getType() == PotionEffectType.BLINDNESS
                                        || effect.getType() == PotionEffectType.LEVITATION
                                        || effect.getType() == PotionEffectType.SLOW
                                        || effect.getType() == PotionEffectType.SLOW_FALLING
                                        || effect.getType() == PotionEffectType.SLOW_DIGGING
                                        || effect.getType() == PotionEffectType.HUNGER
                                        || effect.getType() == PotionEffectType.GLOWING
                                        || effect.getType() == PotionEffectType.WEAKNESS
                                        || effect.getType() == PotionEffectType.WITHER) {
                                    e.setCancelled(false);
                                } else {
                                    e.setCancelled(true);
                                }
                            }
                        } else if (Objects.equals(Kit.equippedKits.get(damager.getUniqueId()).name, name)
                                && MapController.getCurrentMap().getTeam(damager.getUniqueId())
                                == MapController.getCurrentMap().getTeam(hit.getUniqueId())) {

                            for (PotionEffect effect : e.getPotion().getEffects()) {
                                if (effect.getType() == PotionEffectType.HEAL
                                        || effect.getType() == PotionEffectType.HEALTH_BOOST
                                        || effect.getType() == PotionEffectType.REGENERATION) {
                                    e.setCancelled(false);
                                    if (hit.getHealth() != baseHealth) {
                                        UpdateStats.addHeals(damager.getUniqueId(), 2);
                                    }
                                } else if (effect.getType() == PotionEffectType.SPEED
                                        || effect.getType() == PotionEffectType.JUMP
                                        || effect.getType() == PotionEffectType.WATER_BREATHING
                                        || effect.getType() == PotionEffectType.SATURATION
                                        || effect.getType() == PotionEffectType.FAST_DIGGING
                                        || effect.getType() == PotionEffectType.DAMAGE_RESISTANCE
                                        || effect.getType() == PotionEffectType.INCREASE_DAMAGE
                                        || effect.getType() == PotionEffectType.INVISIBILITY
                                        || effect.getType() == PotionEffectType.NIGHT_VISION
                                        || effect.getType() == PotionEffectType.CONDUIT_POWER
                                        || effect.getType() == PotionEffectType.FIRE_RESISTANCE) {
                                    e.setCancelled(false);
                                    UpdateStats.addSupports(damager.getUniqueId(), 2);
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
                e.getBlockPlaced().getType() == Material.CAULDRON) {

            Block yourCauldron = e.getBlockPlaced();
            Levelled cauldronData = (Levelled) yourCauldron.getBlockData();
            cauldronData.setLevel(cauldronData.getMaximumLevel()); // Fill it up!
            yourCauldron.setBlockData(cauldronData);

            // Destroy old cauldron
            destroyCauldron(p);

            // Place new cauldron
            e.setCancelled(false);
            cauldrons.put(p, e.getBlockPlaced());
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                    TextComponent.fromLegacyText(ChatColor.AQUA + "You placed down your cauldron!"));
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
                e.getClickedBlock().getType() == Material.CAULDRON) {
            Player p = e.getPlayer();
            Player q = getPlacer(e.getClickedBlock());

            // Pick up own cauldron
            if (Objects.equals(p, q)) {
                destroyCauldron(q);
                q.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                        TextComponent.fromLegacyText(ChatColor.AQUA + "You took back your Cauldron!"));

                // Can only hold 1 cauldron at a time
                PlayerInventory inv = p.getInventory();
                if (inv.getItemInOffHand().getType() != Material.CAULDRON &&
                        !inv.contains(Material.CAULDRON)) {
                    if (!ActiveData.getData(p.getUniqueId()).hasVote("sword")) {
                        p.getInventory().addItem(cauldron);
                    } else {
                        p.getInventory().addItem(cauldronVoted);
                    }
                }

                // Destroy enemy firepit
            } else if (q != null &&
                    MapController.getCurrentMap().getTeam(p.getUniqueId())
                            != MapController.getCurrentMap().getTeam(q.getUniqueId())){
                destroyCauldron(q);
                p.playSound(e.getClickedBlock().getLocation(), Sound.AMBIENT_UNDERWATER_ENTER , 5, 1);
                p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                        TextComponent.fromLegacyText(ChatColor.RED + "You kicked over " + q.getName() + "'s cauldron!"));
            }
        }
    }


    /**
     * Destroy the player's firepit if present
     * @param p The player whose firepit to destroy
     */
    private void destroyCauldron(Player p) {
        if(cauldrons.containsKey(p)) {
            cauldrons.get(p).setType(Material.AIR);
            cauldrons.remove(p);
        }
    }

    /**
     * Get the placer of a firepit
     * @param cauldron The firepit whose placer to find
     * @return The placer of the firepit, null of not placed by a fire archer
     */
    private Player getPlacer(Block cauldron) {
        return cauldrons.entrySet().stream()
                .filter(entry -> Objects.equals(entry.getValue(), cauldron))
                .findFirst().map(Map.Entry::getKey)
                .orElse(null);
    }


    /**
     * prevent bottles from being filled up with water
     * @param e The event called when right-clicking a cauldron with an arrow
     */
    @EventHandler
    public void onUseBottleOnWater(PlayerInteractEvent e) {
        Player p = e.getPlayer();
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
        ItemStack usedItem = e.getItem();

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(p.getUniqueId())) {
            return;
        }

        // Check if a fire archer tries to light an arrow, while off-cooldown
        if (Objects.equals(Kit.equippedKits.get(p.getUniqueId()).name, name) &&
                e.getAction() == Action.RIGHT_CLICK_BLOCK &&
                e.getClickedBlock().getType() == Material.CAULDRON &&
                usedItem != null &&
                usedItem.getType() == Material.GLASS_BOTTLE &&
                p.getCooldown(Material.GLASS_BOTTLE) == 0) {

            // Check if the player may light an arrow using this cauldron
            Player q = getPlacer(e.getClickedBlock());
            if (q != null &&
                    MapController.getCurrentMap().getTeam(p.getUniqueId())
                            == MapController.getCurrentMap().getTeam(q.getUniqueId())) {

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

        int types = rand.nextInt(26);
        int amount = rand.nextInt(2);
        int time = rand.nextInt(3600);
        int smallTime = rand.nextInt(220);
        int amplifier = rand.nextInt(3);
        int amplifierRegen = rand.nextInt(10);


        ItemStack itemStack = new ItemStack(Material.SPLASH_POTION);
        ItemMeta itemmeta = itemStack.getItemMeta();
        PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
        assert potionMeta != null;
        for (int i = 0; i <= amount; i++) {

            switch (types) {
                case 0:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.HEAL, 1, amplifierRegen), true);
                    potionMeta.setColor(Color.RED);
                    itemmeta.setDisplayName(ChatColor.RED + "Healing Potion");
                    break;
                case 1:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.HEAL, 1, 8), true);
                    potionMeta.setColor(Color.RED);
                    itemmeta.setDisplayName(ChatColor.RED + "Healing Potion");
                    break;
                case 2:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.REGENERATION, time, amplifierRegen), true);
                    potionMeta.setColor(Color.FUCHSIA);
                    itemmeta.setDisplayName(ChatColor.RED + "Regeneration Potion");
                    break;
                case 3:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, time, amplifierRegen), true);
                    potionMeta.setColor(Color.FUCHSIA);
                    itemmeta.setDisplayName(ChatColor.RED + "Health Boost Potion");
                    break;
                case 4:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, time, amplifier), true);
                    potionMeta.setColor(Color.fromRGB(119,211,255));
                    itemmeta.setDisplayName(ChatColor.BLUE + "Speed Potion");
                    break;
                case 5:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, time, 0), true);
                    potionMeta.setColor(Color.fromRGB(119,126,225));
                    itemmeta.setDisplayName(ChatColor.BLUE + "Water Breathing Potion");
                    break;
                case 6:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SATURATION, time, amplifier), true);
                    potionMeta.setColor(Color.fromRGB(173,177,229));
                    itemmeta.setDisplayName(ChatColor.BLUE + "Saturation Potion");
                    break;
                case 7:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, time, amplifier), true);
                    potionMeta.setColor(Color.fromRGB(229,227,173));
                    itemmeta.setDisplayName(ChatColor.YELLOW + "Fast Digging Potion");
                    break;
                case 8:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, time, 0), true);
                    potionMeta.setColor(Color.fromRGB(216,174,118));
                    itemmeta.setDisplayName(ChatColor.YELLOW + "Damage Resistance Potion");
                    break;
                case 9:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, time, amplifier), true);
                    potionMeta.setColor(Color.fromRGB(208,13,45));
                    itemmeta.setDisplayName(ChatColor.DARK_RED + "Strength Potion");
                    break;
                case 10:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.INVISIBILITY, time, amplifier), true);
                    potionMeta.setColor(Color.fromRGB(146,93,143));
                    itemmeta.setDisplayName(ChatColor.WHITE + "Invisibility Potion");
                    break;
                case 11:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, time, 0), true);
                    potionMeta.setColor(Color.fromRGB(27,50,102));
                    itemmeta.setDisplayName(ChatColor.DARK_BLUE + "Night Vision Potion");
                    break;
                case 12:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.CONDUIT_POWER, time, amplifier), true);
                    potionMeta.setColor(Color.fromRGB(134,188,236));
                    itemmeta.setDisplayName(ChatColor.DARK_AQUA + "Conduit Power Potion");
                    break;
                case 13:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, time, 0), true);
                    potionMeta.setColor(Color.fromRGB(252,180,13));
                    itemmeta.setDisplayName(ChatColor.GOLD + "Fire Resistance Potion");
                    break;
                case 14:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.HARM, 1, amplifier), true);
                    potionMeta.setColor(Color.fromRGB(68,20,68));
                    itemmeta.setDisplayName(ChatColor.DARK_PURPLE + "Harming Potion");
                    break;
                case 15:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.CONFUSION, smallTime, amplifier), true);
                    potionMeta.setColor(Color.fromRGB(151,202,149));
                    itemmeta.setDisplayName(ChatColor.DARK_PURPLE + "Confusion Potion");
                    break;
                case 16:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.BLINDNESS, smallTime, amplifier), true);
                    potionMeta.setColor(Color.fromRGB(7,9,30));
                    itemmeta.setDisplayName(ChatColor.DARK_PURPLE + "Blindness Potion");
                    break;
                case 17:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.LEVITATION, smallTime, amplifier), true);
                    potionMeta.setColor(Color.fromRGB(57,250,218));
                    itemmeta.setDisplayName(ChatColor.BLUE + "Levitation Potion");
                    break;
                case 18:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SLOW, smallTime, amplifier), true);
                    potionMeta.setColor(Color.fromRGB(160,57,250));
                    itemmeta.setDisplayName(ChatColor.DARK_PURPLE + "Slowness Potion");
                    break;
                case 19:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, smallTime, amplifier), true);
                    potionMeta.setColor(Color.fromRGB(57,89,250));
                    itemmeta.setDisplayName(ChatColor.DARK_BLUE + "Slow Falling Potion");
                    break;
                case 20:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, smallTime, amplifier), true);
                    potionMeta.setColor(Color.fromRGB(212,250,57));
                    itemmeta.setDisplayName(ChatColor.YELLOW + "Fatigue Potion");
                    break;
                case 21:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.HUNGER, smallTime, amplifier), true);
                    potionMeta.setColor(Color.fromRGB(220,191,45));
                    itemmeta.setDisplayName(ChatColor.DARK_GREEN + "Hunger Potion");
                    break;
                case 22:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.GLOWING, smallTime, amplifier), true);
                    potionMeta.setColor(Color.fromRGB(252,249,173));
                    itemmeta.setDisplayName(ChatColor.YELLOW + "Marking Potion");
                    break;
                case 23:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.WEAKNESS, smallTime, amplifier), true);
                    potionMeta.setColor(Color.fromRGB(152,131,117));
                    itemmeta.setDisplayName(ChatColor.DARK_BLUE + "Weakness Potion");
                    break;
                case 24:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.WITHER, smallTime, amplifier), true);
                    potionMeta.setColor(Color.fromRGB(40,37,36));
                    itemmeta.setDisplayName(ChatColor.DARK_GRAY + "Death Potion");
                    break;
                case 25:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.POISON, smallTime, amplifier), true);
                    potionMeta.setColor(Color.fromRGB(16,137,32));
                    itemmeta.setDisplayName(ChatColor.DARK_GREEN + "Poison Potion");
                    break;
                case 26:
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.HARM, 1, amplifier), true);
                    potionMeta.setColor(Color.fromRGB(68,20,68));
                    itemmeta.setDisplayName(ChatColor.DARK_PURPLE + "Harming Potion");
                    break;
                default:
                    break;
            }
            itemStack.setItemMeta(itemmeta);
            itemStack.setItemMeta(potionMeta);
        }
        return itemStack;
    }



}
