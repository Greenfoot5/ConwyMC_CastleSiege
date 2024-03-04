package me.huntifi.castlesiege.kits.kits;

import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.castlesiege.events.chat.Messenger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public abstract class LevelKit extends Kit {


    // Kit Tracking
    private static final Collection<String> kits = new ArrayList<>();

    private final int levelRequired;

    /**
     * Create a kit that can be unlocked at a certain level.
     *
     * @param name        This kit's name
     * @param baseHealth  This kit's base health
     * @param regenAmount amount of regen it gets per x amount seconds
     * @param material what is its symbol in the GUI?
     * @param level The level required to unlock
     */
    public LevelKit(String name, int baseHealth, double regenAmount, Material material, int level) {
        super(name, baseHealth, regenAmount, material, NamedTextColor.GREEN);

        if (!kits.contains(getSpacelessName()))
            kits.add(getSpacelessName());
        levelRequired = level;
    }

    /**
     * Check if the player can select this kit
     * @param sender Source of the command
     * @param applyLimit Whether to apply the kit limit in the check
     * @param verbose Whether error messages should be sent
     * @param isRandom Whether the check is done by the random command
     * @return Whether the player can select this kit
     */
    public boolean canSelect(CommandSender sender, boolean applyLimit, boolean verbose, boolean isRandom) {
        if (!super.canSelect(sender, applyLimit, verbose, isRandom))
            return false;

        UUID uuid = ((Player) sender).getUniqueId();
        int level = ActiveData.getData(uuid).getLevel();
        boolean unlocked = level >= levelRequired;
        if (!unlocked) {
            if (verbose) {
                if (Kit.equippedKits.get(uuid) == null) {
                    Messenger.sendError(String.format("You no longer have access or never had access to %s!", name), sender);
                } else {
                    Messenger.sendError(String.format("You don't own %s!", name), sender);
                }
            }
            return false;
        }

        return true;
    }

    /**
     * Get all level kit names
     * @return All level kit names without spaces
     */
    public static Collection<String> getKits() {
        return kits;
    }

    /**
     * @return Displays the cost for the footer of a kit gui's lore
     */
    public ArrayList<Component> getGuiCostText() {
        ArrayList<Component> text = new ArrayList<>();
        text.add(Component.text(" "));
        text.add(Component.text("Unlocked at level " + levelRequired, color).decorate(TextDecoration.BOLD));
        return text;
    }
}
