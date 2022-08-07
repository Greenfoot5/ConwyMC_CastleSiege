package me.huntifi.castlesiege.kits.kits;

import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.maps.MapController;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public abstract class TeamKit extends DonatorKit {

    //map for the map specific kits and the team
    protected String map;
    protected String team;

    // Kit Tracking
    private static final Collection<String> kits = new ArrayList<>();

    /**
     * Create a kit with basic settings
     *
     * @param name         This kit's name
     * @param baseHealth   This kit's base health
     * @param regenAmount  The kit's regeneration
     * @param playableMap  The map the kit can be played on
     * @param playableTeam The team the kit can be played on
     * @param coins the amount of coins this kit costs
     */
    public TeamKit(String name, int baseHealth, double regenAmount, String playableMap, String playableTeam, double coins) {
        super(name, baseHealth, regenAmount, coins);
        team = playableTeam;
        map = playableMap;

        if (!kits.contains(getSpacelessName()))
            kits.add(getSpacelessName());
    }

    public String getMapName() {
        return map;
    }

    public String getTeamName() {
        return team;
    }

    /**
     * Check if the player can select this kit
     * @param sender Source of the command
     * @param verbose Whether error messages should be sent
     * @return Whether the player can select this kit
     */
    @Override
    public boolean canSelect(CommandSender sender, boolean verbose) {
        if (!super.canSelect(sender, verbose))
            return false;

        UUID uuid = ((Player) sender).getUniqueId();
        if (!map.equalsIgnoreCase(MapController.getCurrentMap().name)) {
            if (verbose)
                Messenger.sendError(String.format("You can't use %s on this map!", name), sender);
            if (Kit.equippedKits.get(uuid) == null)
                Kit.getKit("Swordsman").addPlayer(uuid);
            return false;
        }

        if (!team.equalsIgnoreCase(MapController.getCurrentMap().getTeam(uuid).name)) {
            if (verbose)
                Messenger.sendError(String.format("You can't use %s on this team!", name), sender);
            if (Kit.equippedKits.get(uuid) == null)
                Kit.getKit("Swordsman").addPlayer(uuid);
            return false;
        }

        return true;
    }

    /**
     * Get all team kit names
     * @return All team kit names without spaces
     */
    public static Collection<String> getKits() {
        return kits;
    }
}
