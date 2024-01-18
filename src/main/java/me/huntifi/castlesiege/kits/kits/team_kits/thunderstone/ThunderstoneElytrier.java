package me.huntifi.castlesiege.kits.kits.team_kits.thunderstone;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.database.UpdateStats;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.kits.kits.TeamKit;
import me.huntifi.castlesiege.maps.TeamController;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

public class ThunderstoneElytrier extends TeamKit implements Listener {

    private static final int POTION_COOLDOWN = 300;

    public ThrownPotion potion;

    public ThunderstoneElytrier() {
        super("Elytrier", 170, 7, "Thunderstone",
                "Thunderstone Guard", 2000, Material.ELYTRA,
                "thunderstoneelytrier");
        super.canCap = false;
        super.canSeeHealth = true;
        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Weapon
        es.hotbar[0] = ItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                ChatColor.GREEN + "Sword", null, null, 30);
        // Voted Weapon
        es.votedWeapon = new Tuple<>(
                ItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                        ChatColor.GREEN + "Sword",
                        Collections.singletonList(ChatColor.AQUA + "- voted: +2 damage"),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), 32),
                0);

        // Chestplate
        es.chest = ItemCreator.item(new ItemStack(Material.ELYTRA),
                ChatColor.GOLD + "Elytra", null, null);

        // Leggings
        es.legs = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_LEGGINGS),
                ChatColor.GREEN + "Leather Leggings", null, null,
                Color.fromRGB(236, 173, 91));

        // Boots
        es.feet = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_BOOTS),
                ChatColor.GREEN + "Leather Boots", null, null,
                Color.fromRGB(236, 173, 91));
        // Voted boots
        es.votedFeet = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_BOOTS),
                ChatColor.GREEN + "Leather Boots",
                Collections.singletonList(ChatColor.AQUA + "- voted: Depth Strider II"),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)),
                Color.fromRGB(236, 173, 91));

        // Weapon
        es.hotbar[1] = ItemCreator.item(new ItemStack(Material.SNOWBALL, 3),
                ChatColor.GREEN + "Small Bomb",
                Collections.singletonList(ChatColor.AQUA + "Right-click to drop a bomb which explodes after 5 seconds!"), null);

        // Ability
        es.hotbar[3] = ItemCreator.item(new ItemStack(Material.FIREWORK_ROCKET),
                ChatColor.GREEN + "Small Boost",
                Collections.singletonList(ChatColor.AQUA + "Right-click to go a little faster!"), null);

        // Support Ability
        es.hotbar[4] = ItemCreator.item(new ItemStack(Material.RED_DYE),
                ChatColor.GREEN + "Drop support potion",
                Collections.singletonList(ChatColor.AQUA + "Right-click to drop a health/speed potion."), null);

        // Ladders
        es.hotbar[2] = new ItemStack(Material.LADDER, 4);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, 6), 2);

        super.equipment = es;

        super.potionEffects.add(new PotionEffect(PotionEffectType.FAST_DIGGING, 999999, 0));
        super.potionEffects.add(new PotionEffect(PotionEffectType.SPEED, 999999, 0));
        super.potionEffects.add(new PotionEffect(PotionEffectType.SLOW_FALLING, 999999, 0));
    }

    /**
     * Activate the elytrier ability of throwing a bomb
     * @param e The event called when right-clicking with snow ball
     */
    @EventHandler
    public void throwBomb(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        ItemStack fist = p.getInventory().getItemInMainHand();

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(uuid)) {
            return;
        }

        if (Objects.equals(Kit.equippedKits.get(uuid).name, name)) {
            if (fist.getType().equals(Material.SNOWBALL)) {
                if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    e.setCancelled(true);
                    p.getInventory().getItemInMainHand().setAmount(p.getInventory().getItemInMainHand().getAmount() - 1);
                    Item item = p.getWorld().spawn(p.getLocation(), Item.class);
                    item.setPickupDelay(99999);
                    item.setItemStack(new ItemStack(Material.SNOWBALL));

                    new BukkitRunnable() {
                        @Override
                        public void run() {

                            item.getWorld().createExplosion(item.getLocation(), 2.25F, false, false, p);

                        }
                    }.runTaskLater(Main.plugin, 90);

                }
            }
        }
    }

    /**
     * Activate the Elytrier ability of boosting itself a little forward
     * @param e The event called when right-clicking with rocket star
     */
    @EventHandler
    public void boost(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        ItemStack fist = p.getInventory().getItemInMainHand();
        int cooldown = p.getCooldown(Material.FIREWORK_ROCKET);

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(uuid)) {
            return;
        }

        if (Objects.equals(Kit.equippedKits.get(uuid).name, name)) {
            if (fist.getType().equals(Material.FIREWORK_ROCKET)) {
                if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (cooldown == 0 && p.isGliding()) {
                        p.setCooldown(Material.FIREWORK_ROCKET, 40);
                        p.setVelocity(p.getVelocity().multiply(3.5));
                    } else if (!p.isGliding()){
                        Messenger.sendActionError(ChatColor.BOLD + "You can't launch yourself whilst not gliding!", p);
                    } else {
                        Messenger.sendActionError(ChatColor.BOLD + "You can't launch yourself forward yet!", p);
                    }
                    e.setCancelled(true);
                }
            }
        }
    }


    /**
     * Activate the Elytrier ability of throwing a health potion down
     * @param e The event called when right-clicking with rocket star
     */
    @EventHandler
    public void healthDrop(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        ItemStack fist = p.getInventory().getItemInMainHand();
        int cooldown = p.getCooldown(Material.RED_DYE);

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(uuid)) {
            return;
        }

        if (Objects.equals(Kit.equippedKits.get(uuid).name, name)) {
            if (fist.getType().equals(Material.RED_DYE)) {
                if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (cooldown == 0 && p.isGliding()) {

                        Location loc = new Location(p.getWorld(), p.getLocation().getX(), p.getLocation().getY() - 1, p.getLocation().getZ());
                        ItemStack itemStack = new ItemStack(Material.SPLASH_POTION);
                        PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
                        assert potionMeta != null;
                        potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.HEAL, 1, 5), true);
                        potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, 60, 0), true);
                        itemStack.setItemMeta(potionMeta);
                        potion = (ThrownPotion) p.getWorld().spawnEntity(loc, EntityType.SPLASH_POTION);
                        potion.setItem(itemStack);
                        potion.setShooter(p);

                        p.setCooldown(Material.RED_DYE, POTION_COOLDOWN);
                    } else if (!p.isGliding()){
                        Messenger.sendActionError(ChatColor.BOLD + "You can't drop a heal whilst not gliding!", p);
                    } else {
                        Messenger.sendActionError(ChatColor.BOLD + "You can't do a health drop just yet!", p);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onThrownPotion(PotionSplashEvent e) {
        if (!(e.getPotion().getShooter() instanceof Player))
            return;

        Player damager = (Player) e.getPotion().getShooter();
        if (Objects.equals(Kit.equippedKits.get(damager.getUniqueId()).name, name)) {
            e.setCancelled(true);
            Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
                for (Entity entity : e.getAffectedEntities()) {
                    if (entity instanceof Player) {
                        Player hit = (Player) entity;
                        if (TeamController.getTeam(damager.getUniqueId())
                                == TeamController.getTeam(hit.getUniqueId())
                                && damager != hit) {
                            if (hit.getHealth() != Kit.equippedKits.get(hit.getUniqueId()).baseHealth)
                                UpdateStats.addHeals(damager.getUniqueId(), 1);
                            UpdateStats.addSupports(damager.getUniqueId(), 2);
                            Bukkit.getScheduler().runTask(Main.plugin, () -> hit.addPotionEffects(e.getPotion().getEffects()));
                        }
                    }
                }
            });
        }
    }

    @Override
    public ArrayList<String> getGuiDescription() {
        ArrayList<String> description = new ArrayList<>();
        description.add("§7//TODO - Add kit description");
        return description;
    }
}
