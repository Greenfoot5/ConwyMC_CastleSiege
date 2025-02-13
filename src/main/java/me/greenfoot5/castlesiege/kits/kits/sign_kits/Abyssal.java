package me.greenfoot5.castlesiege.kits.kits.sign_kits;

import me.greenfoot5.castlesiege.Main;
import me.greenfoot5.castlesiege.events.combat.InCombat;
import me.greenfoot5.castlesiege.events.death.DeathEvent;
import me.greenfoot5.castlesiege.kits.items.CSItemCreator;
import me.greenfoot5.castlesiege.kits.items.EquipmentSet;
import me.greenfoot5.castlesiege.kits.kits.Kit;
import me.greenfoot5.castlesiege.kits.kits.SignKit;
import me.greenfoot5.castlesiege.maps.TeamController;
import me.greenfoot5.conwymc.data_types.Tuple;
import me.greenfoot5.conwymc.util.Messenger;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.data.BlockData;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LargeFireball;
import org.bukkit.entity.Player;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

public class Abyssal extends SignKit implements Listener {

    private FallingBlock magma;
    private Fireball ball;
    private Player shooter;

    /**
     * Creates a new Firelands Abyssal
     */
    public Abyssal() {
        super("Abyssal", 600, 9, Material.MAGMA_BLOCK, 5000);
        super.canCap = true;
        super.canClimb = false;
        super.canSeeHealth = true;
        super.kbResistance = 1;

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();

        // Weapon
        es.hotbar[0] = CSItemCreator.weapon(new ItemStack(Material.GREEN_DYE),
                Component.text("Fist", NamedTextColor.RED),
                Collections.singletonList(Component.text("Right-click to start spawning a magma projectile", NamedTextColor.AQUA)),
                null, 30.5);
        // Voted weapon
        es.votedWeapon = new Tuple<>(
                CSItemCreator.weapon(new ItemStack(Material.GREEN_DYE),
                        Component.text("Fist", NamedTextColor.RED),
                        Arrays.asList(Component.text("Right-click to start spawning a magma projectile!", NamedTextColor.AQUA),
                                Component.text("⁎ Voted: +2 damage", NamedTextColor.AQUA)),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOTING, 0)), 32.5),
                0);

        // Weapon
        es.offhand = CSItemCreator.item(new ItemStack(Material.GREEN_DYE),
                Component.text("Fist", NamedTextColor.GREEN),
                Collections.singletonList(Component.text("Right-click to start spawning a magma projectile!", NamedTextColor.AQUA)), null);

        super.equipment = es;

        super.potionEffects.add(new PotionEffect(PotionEffectType.MINING_FATIGUE, 999999, 2));
        super.potionEffects.add(new PotionEffect(PotionEffectType.RESISTANCE, 999999, 0));
        super.potionEffects.add(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 999999, 0));

        // Death Messages
        super.deathMessage[0] = "You were crushed to death by ";
        super.killMessage[0] = " crushed ";
        super.killMessage[1] = " to death";
    }

    /**
     * Disguise the player as an Abyssal
     */
    @Override
    protected void setDisguise() {
        PlayerDisguise playerDisguise = new PlayerDisguise("hiderr");

        playerDisguise.getWatcher().setCapeEnabled(false);
        playerDisguise.getWatcher().setParticlesColor(Color.GREEN);

        disguise(playerDisguise);
    }

    /**
     * @param p The player spawning the magma projectile
     */
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

    /**
     * @param p The player launching the projectile
     */
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
        ball = p.getWorld().spawn(loc, Fireball.class);
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

    /**
     * @param event When the magma projectile hits something
     */
    @EventHandler
    public void onImpact(ProjectileHitEvent event) {
        if (Objects.equals(ball, event.getEntity())) {
            ball.getPassengers().getFirst().remove();
            ball.getWorld().createExplosion(ball.getLocation(), 2F, true, false, shooter);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void FireballDamage(EntityDamageByEntityEvent e) {
        if (e.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE) || e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)) {
            if (e.getDamager() instanceof Fireball) {
                LargeFireball fire = (LargeFireball) e.getDamager();
                if (fire.getShooter() instanceof Player damager) {
                    if (!(e.getEntity() instanceof Player hit)) {
                        e.setDamage(200);
                        return; }
                    if (Objects.equals(Kit.equippedKits.get(damager.getUniqueId()).name, name)
                            && TeamController.getTeam(damager.getUniqueId())
                            != TeamController.getTeam(hit.getUniqueId())) {
                        if ((hit.getHealth() - e.getDamage() > 0)) {
                            e.setCancelled(true);
                            hit.damage(150);
                            return;
                        } else {
                            e.setCancelled(true);
                            DeathEvent.setKiller(hit, damager);
                            hit.setHealth(0);
                            return;
                        }
                    }
                    if (Objects.equals(Kit.equippedKits.get(damager.getUniqueId()).name, name)
                    && TeamController.getTeam(damager.getUniqueId())
                            == TeamController.getTeam(hit.getUniqueId())) {
                        e.setCancelled(true);
                        e.setDamage(0);
                    }
                }
            }
        }
    }

    /**
     * Activate the abyssal ability of throwing a magma block
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
                        Messenger.sendActionInfo("You are charging, aim at the right location!", p);
                        p.getWorld().playSound(p.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1, (float) 0.5);
                        spawnMagmaProjectile(p);
                        launchProjectile(p);
                    } else {
                        Messenger.sendActionError("You can't launch your magma block just yet.", p);
                    }
                }
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
