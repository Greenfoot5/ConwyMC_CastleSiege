package me.huntifi.castlesiege.kits.kits;

import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.castlesiege.events.chat.Messenger;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public abstract class VoterKit extends Kit {

    // Kit Tracking
    private static final Collection<String> kits = new ArrayList<>();

    /**
     * Create a kit with basic settings
     *
     * @param name       This kit's name
     * @param baseHealth This kit's base health
     */
    public VoterKit(String name, int baseHealth, double regenAmount, Material material) {
        super(name, baseHealth, regenAmount, material);
        if (!kits.contains(getSpacelessName()))
            kits.add(getSpacelessName());
    }

    /**
     * Check if the player can select this kit
     * @param sender Source of the command
     * @param verbose Whether error messages should be sent
     * @param isRandom Whether the kit is selected by /random
     * @return Whether the player can select this kit
     */
    @Override
    public boolean canSelect(CommandSender sender, boolean verbose, boolean isRandom) {
        if (!super.canSelect(sender, verbose, isRandom))
            return false;

        UUID uuid = ((Player) sender).getUniqueId();
        if (!ActiveData.getData(uuid).hasVote("kits")) {
            if (verbose) {
                if (Kit.equippedKits.get(uuid) == null)
                    Messenger.sendError(String.format("You need to vote to use %s again!", name), sender);
                else
                    Messenger.sendError(String.format("You need to vote to use %s!", name), sender);
            }
            return false;
        }

        return true;
    }

    /**
     * Get all free kit names
     * @return All free kit names without spaces
     */
    public static Collection<String> getKits() {
        return kits;
    }

    /**
     * @return Displays the cost for the footer of a kit gui's lore
     */
    public static ArrayList<String> getGuiCostText() {
        ArrayList<String> text = new ArrayList<>();
        text.add(" ");
        text.add("§9Vote on PMC for this kit");
        return text;
    }
}
