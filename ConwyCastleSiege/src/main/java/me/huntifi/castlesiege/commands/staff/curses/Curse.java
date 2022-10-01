package me.huntifi.castlesiege.commands.staff.curses;

/**
 * The different curses used to affect gameplay.
 */
public enum Curse {
    GREED ("Curse of Greed"),
    DICE ("Curse of the Dice"),
    BINDING ("Curse of Binding"),
    POSSESSION ("Curse of Possession"),
    VANISHING ("Curse of Vanishing"),
    TEAMWORK ("Curse of Teamwork"),
    TELEPORTATION ("Curse of Teleportation");

    /** The name of the curse */
    private final String name;

    /** The description of the curse */
    private String description;

    /**
     * Create a curse with a name.
     * @param name The name of the curse
     */
    Curse(String name) {
        this.name = name;
        this.description = "";
    }

    /**
     * Create a curse with a name and a description.
     * @param name The name of the curse
     * @param description The description of the curse
     */
    Curse(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Set the description of this curse.
     * @param description The description to set
     */
    public void setDescription(String description) {
        this.description = description;
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
     * Get the description of this curse.
     * @return The description of this curse
     */
    public String getDescription() {
        return this.description;
    }
}
