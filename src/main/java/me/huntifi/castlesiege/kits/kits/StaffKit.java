package me.huntifi.castlesiege.kits.kits;

import me.huntifi.castlesiege.database.CSActiveData;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.conwymc.util.Messenger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public abstract class StaffKit extends Kit {

    // Kit Tracking
    private static final Collection<String> kits = new ArrayList<>();
    public static final List<String> donatorKits = new ArrayList<>();

    /**
     * Create a Staff Kit
     * @param name       This kit's name
     * @param baseHealth This kit's base health
     * @param regenAmount The amount to regen every regen tick
     * @param material The material to display in GUIS
     */
    public StaffKit(String name, int baseHealth, double regenAmount, Material material) {
        super(name, baseHealth, regenAmount, material, NamedTextColor.DARK_AQUA);

        if (!kits.contains(getSpacelessName()))
            kits.add(getSpacelessName());
        if (!donatorKits.contains(getSpacelessName()))
            donatorKits.add(getSpacelessName());
    }

    /**
     * Check if the player can select this kit
     * @param sender Source of the command
     * @param applyLimit If the kit limits should be applied
     * @param verbose Whether error messages should be sent
     * @param isRandom If the kit is selected by the random command
     * @return Whether the player can select this kit
     */
    @Override
    public boolean canSelect(CommandSender sender, boolean applyLimit, boolean verbose, boolean isRandom) {
        if (!super.canSelect(sender, applyLimit, verbose, isRandom))
            return false;

        UUID uuid = ((Player) sender).getUniqueId();
        boolean hasKit = CSActiveData.getData(uuid).hasKit(getSpacelessName());
        boolean allKitsFree = MapController.allKitsFree;
        if (!hasKit && !allKitsFree) {
            if (verbose) {
                if (Kit.equippedKits.get(uuid) == null) {
                    Messenger.sendError(String.format("You no longer have access to %s!", name), sender);
                } else {
                    Messenger.sendError(String.format("You don't own %s!", name), sender);
                }
            }
            return false;
        }
        return true;
    }

    /**
     * Get all staff kit names
     * @return All staff kit names without spaces
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
        text.add(Component.text("Unlocked by becoming staff", color).decorate(TextDecoration.BOLD));
        return text;
    }
}
