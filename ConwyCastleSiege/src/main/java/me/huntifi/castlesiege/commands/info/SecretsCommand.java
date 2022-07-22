package me.huntifi.castlesiege.commands.info;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.castlesiege.database.LoadData;
import me.huntifi.castlesiege.database.StoreData;
import me.huntifi.castlesiege.events.chat.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.UUID;

public class SecretsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof ConsoleCommandSender) {
            Messenger.sendError("Consoles can't use this command!", sender);
        }
        Player p = (Player) sender;

        new BukkitRunnable() {
            @Override
            public void run() {

                String helms = getHelmsdeepSecrets(p);
                String thunder = getThunderstoneSecrets(p);

                new BukkitRunnable() {
                    @Override
                    public void run() {

                        p.sendMessage(ChatColor.DARK_AQUA + " -----Secrets----- ");
                        p.sendMessage(ChatColor.DARK_AQUA + " Helm's Deep: " + helms);
                        p.sendMessage(ChatColor.DARK_AQUA + " Thunderstone: " + thunder);

                    }
                }.runTask(Main.plugin);

            }
        }.runTaskAsynchronously(Main.plugin);

        return true;
    }

    /**
     *
     * @param p the player to check the secrets for
     * @return a string returning the amount of secrets one has and can find.
     */
    public String getThunderstoneSecrets(Player p) {
        UUID uuid = p.getUniqueId();

        int foundAmount = 0;

        if (LoadData.getFoundSecrets(uuid).contains("Thunderstone_Lantern")) {
            foundAmount = foundAmount + 1;
         }
        if (LoadData.getFoundSecrets(uuid).contains("Thunderstone_Island")) {
            foundAmount = foundAmount + 1;
        }
        if (LoadData.getFoundSecrets(uuid).contains("Thunderstone_Huntifi")) {
            foundAmount = foundAmount + 1;
        }
        if (LoadData.getFoundSecrets(uuid).contains("Thunderstone_Skyview")) {
            foundAmount = foundAmount + 1;
        }
        if (LoadData.getFoundSecrets(uuid).contains("Thunderstone_Cookie")) {
            foundAmount = foundAmount + 1;
        }
        if (LoadData.getFoundSecrets(uuid).contains("Thunderstone_Fall")) {
            foundAmount = foundAmount + 1;
        }

        return ChatColor.AQUA + "(" + ChatColor.WHITE + foundAmount + ChatColor.AQUA + "/6)";
    }

    public String getHelmsdeepSecrets(Player p) {
        UUID uuid = p.getUniqueId();

                int foundAmount = 0;

                if (LoadData.getFoundSecrets(uuid).contains("HD_HornSecret")) {
                    foundAmount = foundAmount + 1;
                }
                if (LoadData.getFoundSecrets(uuid).contains("HD_HillSecret")) {
                    foundAmount = foundAmount + 1;
                }
                if (LoadData.getFoundSecrets(uuid).contains("HD_Herugrim")) {
                    foundAmount = foundAmount + 1;
                }

        return ChatColor.AQUA + "(" + ChatColor.WHITE + foundAmount + ChatColor.AQUA + "/3)";
    }
}
