package me.huntifi.castlesiege.commands.gameplay;

import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.kits.kits.TeamKit;
import me.huntifi.castlesiege.maps.MapController;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

/**
 * Selects a random kit for the user
 */
public class RandomKitCommand implements CommandExecutor {

    /**
     * Opens the kit selector GUI for the command source if no arguments are passed
     * Opens the specific kits GUI for the command source if a sub-GUI is specified
     * @param sender Source of the command
     * @param cmd Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return true
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            Messenger.sendError("Console cannot select kits!", sender);
            return true;
        }

        UUID uuid = ((Player) sender).getUniqueId();
        if (MapController.isSpectator(uuid)) {
            Messenger.sendError("Spectators cannot select kits!", sender);
            return true;
        }

        Random random = new Random();
        String map = MapController.getCurrentMap().name;
        String team = MapController.getCurrentMap().getTeam(uuid).name;

        ArrayList<String> unlockedKits = ActiveData.getData(uuid).getUnlockedKits();
        ArrayList<Kit> kits = new ArrayList<>();

        unlockedKits.forEach((kitName) -> {
            Kit kit = Kit.getKit(kitName);
            if (kit == null || (kit instanceof TeamKit
                    && !(map.equalsIgnoreCase(((TeamKit) kit).getMapName())
                    && team.equalsIgnoreCase(((TeamKit) kit).getTeamName())))) {
                return;
            }
            kits.add(kit);
        });

        kits.get(random.nextInt(kits.size())).addPlayer(uuid);
        return true;
    }
}
