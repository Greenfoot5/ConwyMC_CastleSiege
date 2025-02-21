package me.greenfoot5.castlesiege.kits.kits.sign_kits;

import com.destroystokyo.paper.ParticleBuilder;
import me.greenfoot5.castlesiege.Main;
import me.greenfoot5.castlesiege.database.UpdateStats;
import me.greenfoot5.castlesiege.events.combat.InCombat;
import me.greenfoot5.castlesiege.kits.items.CSItemCreator;
import me.greenfoot5.castlesiege.kits.items.EquipmentSet;
import me.greenfoot5.castlesiege.kits.kits.Kit;
import me.greenfoot5.castlesiege.kits.kits.SignKit;
import me.greenfoot5.castlesiege.maps.TeamController;
import me.greenfoot5.conwymc.data_types.Tuple;
import me.greenfoot5.conwymc.util.Messenger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
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

/**
 * A kit with an Elytra with bombs and heals
 */
public class Elytrier extends SignKit implements Listener {

    private static final int POTION_COOLDOWN = 300;

    /**
     * Creates a new Elytrier instance
     */
    public Elytrier() {
        super("Elytrier", 170, 7, Material.ELYTRA, 2000);
        super.canCap = false;
        super.canSeeHealth = true;
        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();

        // Weapon
        es.hotbar[0] = CSItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                Component.text("Sword", NamedTextColor.GREEN), null, null, 30);
        // Voted Weapon
        es.votedWeapon = new Tuple<>(
                CSItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                        Component.text("Sword", NamedTextColor.GREEN),
                        Collections.singletonList(Component.text("⁎ Voted: +2 damage", NamedTextColor.AQUA)),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOTING, 0)), 32),
                0);

        // Chestplate
        es.chest = CSItemCreator.item(new ItemStack(Material.ELYTRA),
                Component.text("Elytra", NamedTextColor.GOLD), null, null);

        // Leggings
        es.legs = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_LEGGINGS),
                Component.text("Leather Leggings", NamedTextColor.GREEN), null, null,
                Color.fromRGB(236, 173, 91));

        // Boots
        es.feet = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_BOOTS),
                Component.text("Leather Boots", NamedTextColor.GREEN), null, null,
                Color.fromRGB(236, 173, 91));
        // Voted boots
        es.votedFeet = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_BOOTS),
                Component.text("Leather Boots", NamedTextColor.GREEN),
                Collections.singletonList(Component.text("⁎ Voted: Depth Strider II", NamedTextColor.AQUA)),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)),
                Color.fromRGB(236, 173, 91));

        // Weapon
        es.hotbar[1] = CSItemCreator.item(new ItemStack(Material.SNOWBALL, 3),
                Component.text("Small Bomb", NamedTextColor.GREEN),
                Collections.singletonList(Component.text("Right-click to drop a bomb which explodes after 5 seconds",
                        NamedTextColor.AQUA)), null);

        // Ability
        es.hotbar[3] = CSItemCreator.item(new ItemStack(Material.FIREWORK_ROCKET),
                Component.text("Small Boost", NamedTextColor.GREEN),
                Collections.singletonList(Component.text("Right-click to go a little faster",
                        NamedTextColor.AQUA)), null);

        // Support Ability
        es.hotbar[4] = CSItemCreator.item(new ItemStack(Material.RED_DYE),
                Component.text("Support Potion", NamedTextColor.GREEN),
                Collections.singletonList(Component.text("Right-click to drop a health/speed potion",
                        NamedTextColor.AQUA)), null);

        // Ladders
        es.hotbar[2] = new ItemStack(Material.LADDER, 4);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, 6), 2);

        super.equipment = es;

        super.potionEffects.add(new PotionEffect(PotionEffectType.HASTE, 999999, 0));
        super.potionEffects.add(new PotionEffect(PotionEffectType.SPEED, 999999, 0));
        //super.potionEffects.add(new PotionEffect(PotionEffectType.SLOW_FALLING, 999999, 0));
    }

    /**
     * Activate the elytrier ability of throwing a bomb
     * @param e The event called when right-clicking with snow ball
     */
    @EventHandler
    public void throwBomb(PlayerInteractEvent e) {
        if (e.getPlayer() != equippedPlayer)
            return;

        if (InCombat.isPlayerInLobby(equippedPlayer.getUniqueId()))
            return;

        ItemStack fist = equippedPlayer.getInventory().getItemInMainHand();

        if (!fist.getType().equals(Material.SNOWBALL))
            return;

        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        e.setCancelled(true);
        fist.setAmount(fist.getAmount() - 1);
        Item item = equippedPlayer.getWorld().spawn(equippedPlayer.getLocation(), Item.class);
        item.setPickupDelay(2000);
        item.setItemStack(new ItemStack(Material.SNOWBALL));

        new BukkitRunnable() {
            @Override
            public void run() {
                item.getWorld().createExplosion(item.getLocation(), 2.25F, false, false, equippedPlayer);
            }
        }.runTaskLater(Main.plugin, 90);

    }

    /**
     * Activate the Elytrier ability of boosting itself a little forward
     * @param e The event called when right-clicking with rocket star
     */
    @EventHandler
    public void boost(PlayerInteractEvent e) {
        if (e.getPlayer() != equippedPlayer)
            return;

        if (InCombat.isPlayerInLobby(equippedPlayer.getUniqueId()))
            return;

        ItemStack fist = equippedPlayer.getInventory().getItemInMainHand();
        int cooldown = equippedPlayer.getCooldown(Material.FIREWORK_ROCKET);

        if (!fist.getType().equals(Material.FIREWORK_ROCKET))
            return;

        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        if (cooldown == 0 && equippedPlayer.isGliding()) {
            equippedPlayer.setCooldown(Material.FIREWORK_ROCKET, 30);
            equippedPlayer.setVelocity(equippedPlayer.getVelocity().multiply(3.5));
        } else if (!equippedPlayer.isGliding()){
            Messenger.sendActionError("You can't launch yourself whilst not gliding!", equippedPlayer);
        } else {
            Messenger.sendActionError("You can't launch yourself forward yet!", equippedPlayer);
        }
        e.setCancelled(true);
    }


    /**
     * Activate the Elytrier ability of throwing a health potion down
     * @param e The event called when right-clicking with red dye
     */
    @EventHandler
    public void healthDrop(PlayerInteractEvent e) {
        if (e.getPlayer() != equippedPlayer)
            return;

        if (InCombat.isPlayerInLobby(equippedPlayer.getUniqueId()))
            return;

        ItemStack fist = equippedPlayer.getInventory().getItemInMainHand();
        int cooldown = equippedPlayer.getCooldown(Material.RED_DYE);

        if (!fist.getType().equals(Material.RED_DYE))
            return;

        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        if (cooldown == 0 && equippedPlayer.isGliding()) {

            Location loc = equippedPlayer.getLocation().add(0, -1, 0);
            ItemStack itemStack = new ItemStack(Material.SPLASH_POTION);
            PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
            assert potionMeta != null;
            potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.INSTANT_HEALTH, 1, 5), true);
            potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, 60, 0), true);
            itemStack.setItemMeta(potionMeta);
            ThrownPotion potion = (ThrownPotion) equippedPlayer.getWorld().spawnEntity(loc, EntityType.POTION);
            potion.setItem(itemStack);
            potion.setShooter(equippedPlayer);

            equippedPlayer.setCooldown(Material.RED_DYE, POTION_COOLDOWN);
        } else if (!equippedPlayer.isGliding()){
            Messenger.sendActionError("You can't drop a heal whilst not gliding!", equippedPlayer);
        } else {
            Messenger.sendActionError("You can't do a health drop just yet!", equippedPlayer);
        }
    }

    /**
     * Applies a healing effect to all hit allies
     * @param e Called when a thrown potion breaks
     */
    @EventHandler
    public void onThrownPotion(PotionSplashEvent e) {
        if (e.getPotion().getShooter() != equippedPlayer)
            return;

        e.setCancelled(true);
        Location loc = e.getHitBlock() != null ? e.getHitBlock().getLocation() : e.getHitEntity().getLocation();
        loc.getWorld().playSound(loc, Sound.ENTITY_SPLASH_POTION_BREAK, 1, 1);

        new ParticleBuilder(Particle.ENTITY_EFFECT)
                .color(e.getPotion().getPotionMeta().getColor())
                .location(loc.add(0.5, 1, 0.5))
                .offset(0.2, 0.5, 0.2)
                .count(30)
                .allPlayers()
                .spawn();

        Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
            for (Entity entity : e.getAffectedEntities()) {
                if (entity instanceof Player hit) {
                    if (TeamController.getTeam(equippedPlayer.getUniqueId())
                            == TeamController.getTeam(hit.getUniqueId())
                            && equippedPlayer != hit) {
                        if (hit.getHealth() != Kit.equippedKits.get(hit.getUniqueId()).baseHealth)
                            UpdateStats.addHeals(equippedPlayer.getUniqueId(), 1);
                        UpdateStats.addSupports(equippedPlayer.getUniqueId(), 2);
                        Bukkit.getScheduler().runTask(Main.plugin, () -> hit.addPotionEffects(e.getPotion().getEffects()));
                    }
                }
            }
        });
    }

    @Override
    public ArrayList<Component> getGuiDescription() {
        ArrayList<Component> description = new ArrayList<>();
        description.add(Component.text("//TODO - Add kit description", NamedTextColor.GRAY));
        return description;
    }
}
