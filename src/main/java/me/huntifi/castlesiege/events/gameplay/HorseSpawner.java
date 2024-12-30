package me.huntifi.castlesiege.events.gameplay;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.entity.SkeletonHorse;
import org.bukkit.entity.ZombieHorse;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Handles spawning horses for kits
 */
class HorseSpawner {

    // Horse attributes
    private final double health;
    private final double knockback;
    private final double speed;
    private final double jump;

    // Horse armor
    private final Material armor;

    // Potion effects
    private final Collection<PotionEffect> effects = new ArrayList<>();

    /**
     * Create a new horse spawner with all the horse's attributes
     * @param health The horse's max health
     * @param knockback The horse's knockback resistance
     * @param speed The horse's movement speed
     * @param jump The horse's jump strength
     * @param armor The horse's horse armor
     * @param effects The horse's potion effects
     */
    public HorseSpawner(double health, double knockback, double speed, double jump,
                        Material armor, @Nullable Collection<PotionEffect> effects) {
        this.health = health;
        this.knockback = knockback;
        this.speed = speed;
        this.jump = jump;
        this.armor = armor;

        if (effects != null) {
            this.effects.addAll(effects);
        }
    }

    /**
     * Spawn the horse for the player
     * @param p The player
     */
    public void spawn(Player p, String type) {
        AbstractHorse horse;
        if (Objects.equals(type, "horse") || Objects.equals(type, "regular")) {
            // Spawn horse
            horse = (Horse) p.getWorld().spawnEntity(p.getLocation(), EntityType.HORSE);
            ((Horse) horse).getInventory().setArmor(new ItemStack(armor));
            // Apply potion effects
            horse.addPotionEffects(effects);


        } else if (Objects.equals(type, "skeletal") || Objects.equals(type, "skeleton")) {
            // Spawn horse
            horse = (SkeletonHorse) p.getWorld().spawnEntity(p.getLocation(), EntityType.SKELETON_HORSE);

            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_HORSE_DEATH, 2, 0.25f);
            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ZOMBIE_INFECT, 2, 0.25f);
            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PHANTOM_DEATH, 2, 0.25f);
            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GHAST_DEATH, 2, 0.25f);

        } else if (Objects.equals(type, "zombified") || Objects.equals(type, "undead") || Objects.equals(type, "zombie")) {
            // Spawn horse
            horse = (ZombieHorse) p.getWorld().spawnEntity(p.getLocation(), EntityType.ZOMBIE_HORSE);
        } else {
            return;
        }

        horse.setTamed(true);
        horse.setOwner(p);
        horse.setAdult();
        horse.addPassenger(p);

        // Set attributes
        AttributeInstance healthAttribute = horse.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        AttributeInstance kbAttribute = horse.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE);
        AttributeInstance speedAttribute = horse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
        AttributeInstance jumpAttribute = horse.getAttribute(Attribute.HORSE_JUMP_STRENGTH);

        assert healthAttribute != null;
        assert kbAttribute != null;
        assert speedAttribute != null;
        assert jumpAttribute != null;

        healthAttribute.setBaseValue(health);
        horse.setHealth(health);
        kbAttribute.setBaseValue(knockback);
        speedAttribute.setBaseValue(speed);
        jumpAttribute.setBaseValue(jump);

        // Give items
        horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));

        // Apply potion effects
        horse.addPotionEffects(effects);
    }
}
