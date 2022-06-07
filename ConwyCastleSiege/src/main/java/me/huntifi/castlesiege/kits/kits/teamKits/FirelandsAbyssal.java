package me.huntifi.castlesiege.kits.kits.teamKits;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.events.death.DeathEvent;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.kits.kits.MapKit;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.NameTag;
import me.libraryaddict.disguise.DisguiseConfig;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.data.BlockData;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

public class FirelandsAbyssal extends MapKit implements Listener {
    /**
     * Create a kit with basic settings
     *
     */
    public FirelandsAbyssal() {
        super("Abyssal", 200, 3);
        super.mapSpecificKits.add("Abyssal");
        super.canCap = true;
        super.canClimb = false;
        super.canSeeHealth = true;
        super.kbResistance = 2;

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Weapon
        es.hotbar[0] = ItemCreator.weapon(new ItemStack(Material.GREEN_DYE),
                ChatColor.RED + "Fist",
                Collections.singletonList(ChatColor.AQUA + "Right-click to start spawning a magma projectile"),
                null, 30.5);
        // Voted weapon
        es.votedWeapon = new Tuple<>(
                ItemCreator.weapon(new ItemStack(Material.GREEN_DYE),
                        ChatColor.RED + "Fist",
                        Arrays.asList(ChatColor.AQUA + "Right-click to start spawning a magma projectile!",
                                ChatColor.AQUA + "- voted: +2 damage"),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), 32.5),
                0);

        // Weapon
        es.offhand = ItemCreator.item(new ItemStack(Material.GREEN_DYE),
                ChatColor.GREEN + "Fist",
                Collections.singletonList(ChatColor.AQUA + "Right-click to start spawning a magma projectile!"), null);

        super.equipment = es;

        super.potionEffects.add(new PotionEffect(PotionEffectType.SLOW_DIGGING, 999999, 2));
        super.potionEffects.add(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 999999, 0));
        super.potionEffects.add(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 999999, 0));

        // Death Messages
        super.deathMessage[0] = "You were crushed to death by ";
        super.killMessage[0] = "You crushed ";
        super.killMessage[1] = " to death ";

        super.playableWorld = "Firelands";
        super.teamName = "Burning Legion";

    }


    /**
     * Disguise the player as an Abyssal
     * @param p The player to (un)disguise
     */
    @Override
    protected void disguise(Player p) {
        PlayerDisguise playerDisguise = new PlayerDisguise("hiderr");

        playerDisguise.getWatcher().setCustomName(NameTag.color(p) + p.getName());
        playerDisguise.getWatcher().setCapeEnabled(false);
        playerDisguise.getWatcher().setParticlesColor(Color.GREEN);
        playerDisguise.setSelfDisguiseVisible(false);
        playerDisguise.setHearSelfDisguise(true);
        playerDisguise.setCustomDisguiseName(true);
        playerDisguise.setNotifyBar(DisguiseConfig.NotifyBar.NONE);

        playerDisguise.setEntity(p);
        playerDisguise.startDisguise();
        NameTag.give(p);
    }

    private FallingBlock magma;

    private Fireball ball;

    private Player shooter;

    public void spawnMagmaProjectile(Player p) {

        if (InCombat.isPlayerInLobby(p.getUniqueId())) {
            return;
        }

        Location above = new Location(p.getWorld(), p.getLocation().getX(), p.getLocation().getY() + 2, p.getLocation().getZ());

        BlockData blockData = Bukkit.createBlockData(Material.MAGMA_BLOCK);
        magma = p.getWorld().spawnFallingBlock(above, blockData);

        //Give the abyssal slowness whilst charging the attack and mount the block on top of the abyssal
        float walkSpeed = p.getWalkSpeed();
        p.setWalkSpeed(0);
        p.addPassenger(magma);

        new BukkitRunnable() {
            @Override
            public void run() {
                p.setWalkSpeed(walkSpeed);
            }
        }.runTaskLater(Main.plugin, 60);

    }

    public void launchProjectile(Player p) {

        //Location above = new Location(p.getWorld(), p.getLocation().getX(), p.getLocation().getY() + 2, p.getLocation().getZ());

        new BukkitRunnable() {
            @Override
            public void run() {
                if (InCombat.isPlayerInLobby(p.getUniqueId())) {
                    return;
                }
        if (magma == null) { return; }
        Location eye = p.getEyeLocation();
        Location loc = eye.add(eye.getDirection().multiply(1.2));
        ball = (Fireball) p.getWorld().spawn(loc, Fireball.class);
        ball.addPassenger(magma);
        ball.setShooter(p);
        ball.setYield(0.1F);
        ball.setVelocity(shooter.getLocation().getDirection().normalize().multiply(1.5));
                p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1, 1);
                p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1, 1);
                p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1, 1);
            }
        }.runTaskLater(Main.plugin, 60);
    }

    @EventHandler
    public void onImpact(ProjectileHitEvent event) {
        if (Objects.equals(ball, event.getEntity())) {
            ball.getPassengers().get(0).remove();
            ball.getWorld().createExplosion(ball.getLocation(), 2F, true, false, shooter);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void FireballDamage(EntityDamageByEntityEvent e) {
        if (e.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE) || e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)) {
            if (e.getDamager() instanceof Fireball) {
                LargeFireball fire = (LargeFireball) e.getDamager();
                if (fire.getShooter() instanceof Player) {
                    Player damager = (Player) fire.getShooter();
                    if (!(e.getEntity() instanceof Player)) {
                        e.setDamage(200);
                        return; }
                    Player hit = (Player) e.getEntity();
                    if (Objects.equals(Kit.equippedKits.get(damager.getUniqueId()).name, name)
                            && MapController.getCurrentMap().getTeam(damager.getUniqueId())
                            != MapController.getCurrentMap().getTeam(hit.getUniqueId())) {
                        if ((hit.getHealth() - e.getDamage() > 0)) {
                            e.setCancelled(true);
                            hit.damage(100);
                            return;
                        } else {
                            e.setCancelled(true);
                            DeathEvent.setKiller(hit, damager);
                            hit.setHealth(0);
                            return;
                        }
                    }
                    if (Objects.equals(Kit.equippedKits.get(damager.getUniqueId()).name, name)
                    && MapController.getCurrentMap().getTeam(damager.getUniqueId())
                            == MapController.getCurrentMap().getTeam(hit.getUniqueId())) {
                        e.setCancelled(true);
                        e.setDamage(0);
                        return;
                    }
                }
            }
        }
    }

    /**
     * Activate the abbysal ability of throwing a magma block
     * @param e The event called when right-clicking with green_dye
     */
    @EventHandler
    public void throwMagma(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        ItemStack fist = p.getInventory().getItemInMainHand();
        int cooldown = p.getCooldown(Material.GREEN_DYE);

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(uuid)) {
            return;
        }

        if (Objects.equals(Kit.equippedKits.get(uuid).name, name)) {
            if (fist.getType().equals(Material.GREEN_DYE)) {
                if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (cooldown == 0) {
                        shooter = p;
                        p.setCooldown(Material.GREEN_DYE, 400);
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                                ChatColor.AQUA + "You are charging, aim at the right location!"));
                                p.getWorld().playSound(p.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1, (float) 0.5);
                                spawnMagmaProjectile(p);
                                launchProjectile(p);
                    } else {
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                                ChatColor.DARK_RED + "" + ChatColor.BOLD + "You can't launch your magma block just yet."));
                    }
                }
            }
        }
    }

}
