package me.huntifi.castlesiege.kits.kits;

import me.huntifi.castlesiege.database.LoadData;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.maps.MapController;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class TeamKit extends DonatorKit {

    //map for the map specific kits and the team
    protected String map;
    protected String team;

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
    }

    public String getTeamName() {
        return team;
    }

    /**
     *
     * @param sender Source of the command
     * @param command Command which was executed
     * @param s Alias of the command which was used
     * @param strings Passed command arguments
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (sender instanceof ConsoleCommandSender) {
            Messenger.sendError("Console cannot select kits!", sender);
            return true;

        } else if (sender instanceof Player) {
            if (MapController.isSpectator(((Player) sender).getUniqueId())) {
                Messenger.sendError("Spectators cannot select kits!", sender);
                return true;
            }

            if (!map.equalsIgnoreCase(MapController.getCurrentMap().name)) {
                ((Player)sender).performCommand("swordsman");
                Messenger.sendError("You can't use this kit on this map!", sender);
                return true;
            }

            Player player = (Player) sender;
            if (!team.equalsIgnoreCase(MapController.getCurrentMap().getTeam(player.getUniqueId()).name)) {
                ((Player)sender).performCommand("swordsman");
                Messenger.sendError("Can't use this kit on this team!", sender);
                return true;
            }


            boolean hasKit = LoadData.hasKit(((Player) sender).getUniqueId(), name.replace(" ", ""));
            if (!hasKit) {
                Messenger.sendError("You don't own this kit!", sender);
                return true;
            }

            super.addPlayer(player.getUniqueId());
            return true;
        }
        return false;
    }

    public double getPrice() {
        return price;
    }
}
