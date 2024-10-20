package me.huntifi.castlesiege.kits.kits;

import me.huntifi.castlesiege.database.CSActiveData;
import me.huntifi.conwymc.util.Messenger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public abstract class EventKit extends Kit {

    // Kit Tracking
    private static final Collection<String> kits = new ArrayList<>();
    private final LocalDate starting;
    private final LocalDate ending;

    /**
     * Create a kit with basic settings
     *
     * @param name        This kit's name
     * @param baseHealth  This kit's base health
     * @param regenAmount The amount the kit regenerates per regen tick
     * @param material    The material to represent the kit in GUIs
     * @param color       The chat colour for the kit
     * @param starting    The date where this event kit can start being used.
     * @param ending      The date where this event kit can no longer be used.
     */
    public EventKit(String name, int baseHealth, double regenAmount, Material material, NamedTextColor color, LocalDate starting, LocalDate ending) {
        super(name, baseHealth, regenAmount, material, color);
        if (!kits.contains(getSpacelessName()))
            kits.add(getSpacelessName());
        this.starting = starting;
        this.ending = ending;
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
        boolean itsThatTimeOfTheYear = (LocalDate.now().getDayOfYear() >= starting.getDayOfYear()) && (LocalDate.now().getDayOfYear() <= ending.getDayOfYear());
        if (!itsThatTimeOfTheYear) {
            if (verbose) {
                if (Kit.equippedKits.get(uuid) == null) {
                    Messenger.sendError("It is not that time of the year :(", sender);
                }
            }
            return false;
        }
        return true;
    }

    /**
     * Get all event kit names
     * @return All event kit names without spaces
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
        text.add(Component.text("Unlocked by certain events!", color).decorate(TextDecoration.BOLD));
        return text;
    }
}
