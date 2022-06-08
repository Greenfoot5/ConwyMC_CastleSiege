package me.huntifi.castlesiege.kits.kits;

import me.huntifi.castlesiege.data_types.PlayerData;
import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.Team;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class MapKit extends Kit {

    /**
     * Create a kit with basic settings
     *
     * @param name        This kit's name
     * @param baseHealth  This kit's base health
     * @param regenAmount
     */
    public MapKit(String name, int baseHealth, double regenAmount) {
        super(name, baseHealth, regenAmount);
        super.mapSpecificKits.add(name);
    }

    /**
     *
     * @param commandSender Source of the command
     * @param command Command which was executed
     * @param s Alias of the command which was used
     * @param strings Passed command arguments
     * @return
     */
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof ConsoleCommandSender) {
            commandSender.sendMessage("Console cannot select kits!");
            return true;

        } else if (commandSender instanceof Player) {
            if (MapController.isSpectator(((Player) commandSender).getUniqueId())) {
                commandSender.sendMessage("Spectators cannot select kits!");
                return true;


            } else {
                Player player = (Player) commandSender;
                Player p = (Player) commandSender;
                PlayerData data = ActiveData.getData(p.getUniqueId());
                if (playableWorld.equalsIgnoreCase(MapController.getCurrentMap().name)) {

                    if (teamName.equalsIgnoreCase(MapController.getCurrentMap().getTeam(p.getUniqueId()).name)) {
                        super.addPlayer(player.getUniqueId());
                        return true;

                    } else if (!teamName.equalsIgnoreCase(MapController.getCurrentMap().getTeam(p.getUniqueId()).name)) {
                            player.sendMessage(ChatColor.DARK_RED + "Can't use this kit as this team!");
                            return true;
                    }

                } else if (!playableWorld.equalsIgnoreCase(MapController.getCurrentMap().name)) {

                        player.sendMessage(ChatColor.DARK_RED + "You can't use your current kit on this map!");
                        return true;

                }
            }

        }

        return true;
    }
}
