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

public abstract class VoterKit extends Kit {

    // Kit Tracking
    private static final Collection<String> kits = new ArrayList<>();

    /**
     * Create a kit with basic settings
     *
     * @param name       This kit's name
     * @param baseHealth This kit's base health
     * @param regenAmount The regen rate of the kit
     * @param material The material to display in menus
     */
    public VoterKit(String name, int baseHealth, double regenAmount, Material material) {
        super(name, baseHealth, regenAmount, material, NamedTextColor.BLUE);
        if (!kits.contains(getSpacelessName()))
            kits.add(getSpacelessName());
    }

    /**
     * Check if the player can select this kit
     * @param sender Source of the command
     * @param applyLimit Whether to apply the kit limit in the check
     * @param verbose Whether error messages should be sent
     * @param isRandom Whether the kit is selected by /random
     * @return Whether the player can select this kit
     */
    @Override
    public boolean canSelect(CommandSender sender, boolean applyLimit, boolean verbose, boolean isRandom) {
        if (!super.canSelect(sender, applyLimit, verbose, isRandom))
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
    public ArrayList<Component> getGuiCostText() {
        ArrayList<Component> text = new ArrayList<>();
        text.add(Component.empty());
        text.add(Component.text("Unlocked by voting on PMC", color).decorate(TextDecoration.BOLD));
        return text;
    }
}
