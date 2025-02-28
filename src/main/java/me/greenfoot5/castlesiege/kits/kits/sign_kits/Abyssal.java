package me.greenfoot5.castlesiege.kits.kits.sign_kits;

import me.greenfoot5.castlesiege.Main;
import me.greenfoot5.castlesiege.events.combat.InCombat;
import me.greenfoot5.castlesiege.kits.items.CSItemCreator;
import me.greenfoot5.castlesiege.kits.items.EquipmentSet;
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

/**
 * Abyssal Kit
 */
public class Abyssal extends SignKit implements Listener {

    private static final int MAGMA_DELAY = 60;

    private FallingBlock magma;
    private Fireball ball;

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
                        Collections.singletonList(new Tuple<>(Enchantment.LOOTING, 1)), 32.5),
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
     * Spawns the magma projectile
     */
    public void spawnMagmaProjectile() {
        Location above = equippedPlayer.getLocation().add(0, 2, 0);

        BlockData blockData = Bukkit.createBlockData(Material.MAGMA_BLOCK);
        magma = equippedPlayer.getWorld().spawn(above, FallingBlock.class);
        magma.setBlockData(blockData);

        //Give the abyssal slowness whilst charging the attack and mount the block on top of the abyssal
        float walkSpeed = equippedPlayer.getWalkSpeed();
        equippedPlayer.setWalkSpeed(0);
        equippedPlayer.addPassenger(magma);

        new BukkitRunnable() {
            @Override
            public void run() {
                equippedPlayer.setWalkSpeed(walkSpeed);
            }
        }.runTaskLater(Main.plugin, MAGMA_DELAY);

    }

    /**
     * Launches the magma projectile
     */
    public void launchProjectile() {
            if (InCombat.isPlayerInLobby(equippedPlayer.getUniqueId()))
                return;

            if (magma == null)
                return;

            Location eye = equippedPlayer.getEyeLocation();
            Location pos = equippedPlayer.getLocation();
            Location loc = eye.add(eye.getDirection().multiply(1.2));
            ball = equippedPlayer.getWorld().spawn(loc, Fireball.class);
            ball.addPassenger(magma);
            ball.setShooter(equippedPlayer);
            ball.setYield(0.1F);
            ball.setVelocity(equippedPlayer.getLocation().getDirection().normalize().multiply(1.5));
            pos.getWorld().playSound(pos, Sound.ENTITY_BLAZE_SHOOT, 1, 1);
            pos.getWorld().playSound(pos, Sound.ENTITY_BLAZE_SHOOT, 1, 1);
            pos.getWorld().playSound(pos, Sound.ENTITY_BLAZE_SHOOT, 1, 1);
    }

    /**
     * @param event When the magma projectile hits something
     */
    @EventHandler
    public void onImpact(ProjectileHitEvent event) {
        if (Objects.equals(ball, event.getEntity())) {
            ball.getPassengers().getFirst().remove();
            ball.getWorld().createExplosion(ball.getLocation(), 2F, true, false, equippedPlayer);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void FireballDamage(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Fireball fireball))
            return;

        if (!e.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE) && !e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION))
            return;

        if (fireball.getShooter() != equippedPlayer)
            return;

        if (!(e.getEntity() instanceof Player hit)) {
            e.setDamage(200);
            return;
        }

        e.setCancelled(true);
        if (TeamController.getTeam(equippedPlayer.getUniqueId()) != TeamController.getTeam(hit.getUniqueId())) {
            hit.damage(150, equippedPlayer);
        }
        if (TeamController.getTeam(equippedPlayer.getUniqueId()) == TeamController.getTeam(hit.getUniqueId())) {
            e.setDamage(0);
        }
    }

    /**
     * Activate the abyssal ability of throwing a magma block
     * @param e The event called when right-clicking with green_dye
     */
    @EventHandler
    public void throwMagma(PlayerInteractEvent e) {
        if (e.getPlayer() != equippedPlayer)
            return;

        if (InCombat.isPlayerInLobby(equippedPlayer.getUniqueId()))
            return;

        ItemStack item = e.getItem();
        int cooldown = equippedPlayer.getCooldown(Material.GREEN_DYE);

        if (item != null && !item.getType().equals(Material.GREEN_DYE))
            return;

        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        if (cooldown == 0) {
            equippedPlayer.setCooldown(Material.GREEN_DYE, 400);
            Messenger.sendActionInfo("You are charging, aim at the right location!", equippedPlayer);
            equippedPlayer.getWorld().playSound(equippedPlayer.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1, (float) 0.5);
            spawnMagmaProjectile();
            new BukkitRunnable() {
                @Override
                public void run() {
                    launchProjectile();
                }
            }.runTaskLater(Main.plugin, MAGMA_DELAY);

        } else {
            Messenger.sendActionError("You can't launch your magma block just yet.", equippedPlayer);
        }
    }

    @Override
    public ArrayList<Component> getGuiDescription() {
        ArrayList<Component> kitLore = new ArrayList<>();
        kitLore.add(Component.text("True wizards only have one spell:", NamedTextColor.GRAY));
        kitLore.add(Component.text("FIREBALL!", NamedTextColor.GRAY));
        kitLore.addAll(getBaseStats(this.baseHealth, this.regenAmount, 30.5, 0));
        // TODO - Externalise values
        kitLore.add(Component.text("150", color)
                .append(Component.text(" Fireball damage", NamedTextColor.GRAY)));
        kitLore.add(Component.empty());
        kitLore.add(Component.text("Effects:", NamedTextColor.DARK_PURPLE));
        kitLore.add(Component.text("- Mining Fatigue III", NamedTextColor.GRAY));
        kitLore.add(Component.text("- Resistance I", NamedTextColor.GRAY));
        kitLore.add(Component.text("- Fire Resistance I", NamedTextColor.GRAY));
        kitLore.add(Component.empty());
        kitLore.add(Component.text("Active:", NamedTextColor.GOLD));
        kitLore.add(Component.text("- Can shoot an explosive fireball", NamedTextColor.GRAY));
        kitLore.add(Component.text("that sets an area on fire", NamedTextColor.GRAY));
        kitLore.add(Component.empty());
        kitLore.add(Component.text("Passive:", NamedTextColor.DARK_GREEN));
        kitLore.add(Component.text("- Can see players' health", NamedTextColor.GRAY));
        kitLore.add(Component.text("- Has 100% knockback resistance", NamedTextColor.GRAY));
        kitLore.add(Component.text("- Cannot climb", NamedTextColor.RED));
        return kitLore;
    }
}
