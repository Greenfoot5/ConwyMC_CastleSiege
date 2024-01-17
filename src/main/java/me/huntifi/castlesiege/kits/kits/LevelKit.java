package me.huntifi.castlesiege.kits.kits;

import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.castlesiege.events.chat.Messenger;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public class LevelKit extends Kit {


    // Kit Tracking
    private static final Collection<String> kits = new ArrayList<>();

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
        super(name, baseHealth, regenAmount, material);

        if (!kits.contains(getSpacelessName()))
            kits.add(getSpacelessName());
        levelRequirement = level;
    }

    /**
     * Check if the player can select this kit
     * @param sender Source of the command
     * @param verbose Whether error messages should be sent
     * @param isRandom If the kit is selected by the random command
     * @return Whether the player can select this kit
     */
    @Override
    public boolean canSelect(CommandSender sender, boolean verbose, boolean isRandom) {
        if (!super.canSelect(sender, verbose, isRandom))
            return false;

        UUID uuid = ((Player) sender).getUniqueId();
        int level = ActiveData.getData(uuid).getLevel();
        boolean unlocked = level >= levelRequirement;
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
     * @param level The level required to unlock the kit
     * @return Displays the cost for the footer of a kit gui's lore
     */
    public static ArrayList<String> getGuiCostText(int level) {
        ArrayList<String> text = new ArrayList<>();
        text.add(" ");
        text.add("§a§lUnlocked at level §a" + level);
        return text;
    }
}
