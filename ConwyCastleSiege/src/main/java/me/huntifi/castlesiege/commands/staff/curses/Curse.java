package me.huntifi.castlesiege.commands.staff.curses;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.events.chat.Messenger;
import org.bukkit.Bukkit;

/**
 * The different curses used to affect gameplay.
 */
public enum Curse {
    GREED ("Curse of Greed", "The coin multiplier is now %s", "The coin multiplier is now 1.0"),
    DICE ("Curse of the Dice", "", ""),
    BINDING ("Curse of Binding", "You can no longer switch kits", "You can now switch kits again"),
    POSSESSION ("Curse of Possession", "", ""),
    VANISHING ("Curse of Vanishing", "", ""),
    TEAMWORK ("Curse of Teamwork", "", ""),
    TELEPORTATION ("Curse of Teleportation", "", "");

    /** The name of the curse */
    private final String name;

    /** The activation description of the curse */
    private final String activateDescription;

    /** The expiry description of the curse */
    private final String expireDescription;

    /** Whether the curse is active */
    private boolean isActive = false;

    /**
     * Create a curse with a name and descriptions.
     * @param name The name of the curse
     * @param activateDescription The activation description of the curse
     * @param expireDescription The expiry description of the curse
     */
    Curse(String name, String activateDescription, String expireDescription) {
        this.name = name;
        this.activateDescription = activateDescription;
        this.expireDescription = expireDescription;
    }

    /**
     * Get a curse by its name.
     * @param curse The name of the curse
     * @return The curse corresponding to the given name, null if none was found.
     */
    public static Curse get(String curse) {
        switch (curse.toLowerCase()) {
            case "greed":
            case "curse of greed":
                return GREED;
            case "dice":
            case "curse of dice":
            case "curse of the dice":
                return DICE;
            case "binding":
            case "curse of binding":
                return BINDING;
            case "possession":
            case "curse of possession":
                return POSSESSION;
            case "vanishing":
            case "curse of vanishing":
                return VANISHING;
            case "teamwork":
            case "curse of teamwork":
                return TEAMWORK;
            case "teleportation":
            case "curse of teleportation":
                return TELEPORTATION;
            default:
                return null;
        }
    }

    /**
     * Get the name of this curse.
     * @return The name of this curse
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get the activation description of this curse.
     * @return The activation description of this curse
     */
    public String getActivateDescription() {
        return this.activateDescription;
    }

    /**
     * Get the expiry description of this curse.
     * @return The expiry description of this curse
     */
    public String getExpireDescription() {
        return this.expireDescription;
    }

    /**
     * Check if this curse is active.
     * @return Whether this curse is active
     */
    public boolean isActive() {
        return this.isActive;
    }

    /**
     * Activate this curse for a certain duration.
     * @param duration The duration of this curse in ticks
     * @param args Optional arguments for the activation message
     */
    public void activate(long duration, Object... args) {
        this.isActive = true;
        Messenger.broadcastCurseActivated(this, args);

        Bukkit.getScheduler().runTaskLaterAsynchronously(Main.plugin, () -> {
            this.isActive = false;
            Messenger.broadcastCurseExpired(this);
        }, duration);
    }
}
