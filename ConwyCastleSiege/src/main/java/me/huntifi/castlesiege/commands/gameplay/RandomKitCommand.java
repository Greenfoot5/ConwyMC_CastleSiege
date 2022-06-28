package me.huntifi.castlesiege.commands.gameplay;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.database.LoadData;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.kits.kits.TeamKit;
import me.huntifi.castlesiege.maps.Map;
import me.huntifi.castlesiege.maps.MapController;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
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
     * @return true if no or a valid sub-GUI was specified, false otherwise
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage("Console cannot select kits!");
            return true;
        } else if (sender instanceof Player && MapController.isSpectator(((Player) sender).getUniqueId())) {
            sender.sendMessage("Spectators cannot select kits!");
            return true;
        }

        if (sender instanceof Player) {
            Random random = new Random();
            UUID uuid = ((Player) sender).getUniqueId();

            new BukkitRunnable() {
                @Override
                public void run() {
                    String map = MapController.getCurrentMap().name;
                    String team = MapController.getCurrentMap().getTeam(uuid).name;

                    ArrayList<String> unlockedKits = LoadData.getAllUnlockedKits(uuid);
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

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            kits.get(random.nextInt(kits.size())).addPlayer(uuid);
                        }
                    }.runTask(Main.plugin);
                }
            }.runTaskAsynchronously(Main.plugin);
            return true;
        }

        return false;
    }
}
