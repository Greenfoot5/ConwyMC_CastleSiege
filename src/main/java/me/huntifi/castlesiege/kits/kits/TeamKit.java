package me.huntifi.castlesiege.kits.kits;

import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.TeamController;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

public abstract class TeamKit extends DonatorKit implements Listener {

    //map for the map specific kits and the team
    protected final String map;
    protected final String team;
    //Might seem odd to add, but it's cause map names vary, and it isn't exactly helpful to get the exact kitname
    //out of mapname and name of the kit. Like this would work for Elytrier but not for any of the Helm's Deep kits.
    protected final String commandname;

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
    public TeamKit(String name, int baseHealth, double regenAmount, String playableMap, String playableTeam, double coins
                   , Material material, String commandname) {
        super(name, baseHealth, regenAmount, material);
        team = playableTeam;
        map = playableMap;

        if (!kits.contains(getSpacelessName()))
            kits.add(getSpacelessName());
        this.commandname = commandname;
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
     * @param isRandom Whether the kit is selected by /random
     * @return Whether the player can select this kit
     */
    @Override
    public boolean canSelect(CommandSender sender, boolean verbose, boolean isRandom) {
        if (!super.canSelect(sender, verbose, isRandom))
            return false;

        UUID uuid = ((Player) sender).getUniqueId();
        if (!map.equalsIgnoreCase(MapController.getCurrentMap().name)) {
            if (verbose)
                Messenger.sendError(String.format("You can't use %s on this map!", name), sender);
            return false;
        }

        if (!team.equalsIgnoreCase(TeamController.getTeam(uuid).name)) {
            if (verbose)
                Messenger.sendError(String.format("You can't use %s on this team!", name), sender);
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


    @EventHandler
    public void onClickSign(PlayerInteractEvent e) {
        // Prevent using in lobby
        if (!InCombat.isPlayerInLobby(e.getPlayer().getUniqueId())) {
            return;
        }
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK &&
                Objects.requireNonNull(e.getClickedBlock()).getState() instanceof Sign) {
            Sign sign = (Sign) e.getClickedBlock().getState();
            if (sign.getLine(0).contains("Team Kit") && sign.getLine(2).contains(name)) {
                e.getPlayer().performCommand(commandname.toLowerCase());
            }
        }

    }

    /**
     * @return Displays the cost for the footer of a kit gui's lore
     */
    public static ArrayList<String> getGuiCostText() {
        ArrayList<String> text = new ArrayList<>();
        text.add(" ");
        text.add("§e§lCan be unlocked with coins");
        return text;
    }
}
