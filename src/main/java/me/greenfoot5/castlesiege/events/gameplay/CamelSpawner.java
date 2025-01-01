package me.greenfoot5.castlesiege.events.gameplay;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Camel;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Handles spawning of camels
 */
public class CamelSpawner {

    // Camel attributes
    private final double health;
    private final double knockback;
    private final double speed;
    private final double jump;

    // Potion effects
    private final Collection<PotionEffect> effects = new ArrayList<>();

    /**
     * Create a new camel spawner with all the camel's attributes
     * @param health The camel's max health
     * @param knockback The camel's knockback resistance
     * @param speed The camel's movement speed
     * @param jump The camel's jump strength
     * @param effects The camel's potion effects
     */
    public CamelSpawner(double health, double knockback, double speed, double jump, @Nullable Collection<PotionEffect> effects) {
        this.health = health;
        this.knockback = knockback;
        this.speed = speed;
        this.jump = jump;

        if (effects != null) {
            this.effects.addAll(effects);
        }
    }

    /**
     * Spawn the camel for the player
     * @param p The player
     */
    public void spawn(Player p) {
        // Spawn camel
        Camel camel = (Camel) p.getWorld().spawnEntity(p.getLocation(), EntityType.CAMEL);
        camel.setTamed(true);
        camel.setOwner(p);
        camel.setAdult();
        camel.addPassenger(p);

        // Set attributes
        AttributeInstance healthAttribute = camel.getAttribute(Attribute.MAX_HEALTH);
        AttributeInstance kbAttribute = camel.getAttribute(Attribute.KNOCKBACK_RESISTANCE);
        AttributeInstance speedAttribute = camel.getAttribute(Attribute.MOVEMENT_SPEED);
        AttributeInstance jumpAttribute = camel.getAttribute(Attribute.JUMP_STRENGTH);

        assert healthAttribute != null;
        assert kbAttribute != null;
        assert speedAttribute != null;
        assert jumpAttribute != null;

        healthAttribute.setBaseValue(health);
        camel.setHealth(health);
        kbAttribute.setBaseValue(knockback);
        speedAttribute.setBaseValue(speed);
        jumpAttribute.setBaseValue(jump);

        // Give items
        camel.getInventory().setSaddle(new ItemStack(Material.SADDLE));

        // Apply potion effects
        camel.addPotionEffects(effects);
    }
}
