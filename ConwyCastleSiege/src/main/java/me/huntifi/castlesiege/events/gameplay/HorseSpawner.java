package me.huntifi.castlesiege.events.gameplay;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;

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
     * @param jump The horse's jump strenght
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
    public void spawn(Player p) {
        // Spawn horse
        Horse horse = (Horse) p.getWorld().spawnEntity(p.getLocation(), EntityType.HORSE);
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
        horse.getInventory().setArmor(new ItemStack(armor));

        // Apply potion effects
        horse.addPotionEffects(effects);
    }
}
