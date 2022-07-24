package me.huntifi.castlesiege.commands.info;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.PlayerData;
import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.castlesiege.database.LoadData;
import me.huntifi.castlesiege.database.StoreData;
import me.huntifi.castlesiege.events.chat.Messenger;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class SecretsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof ConsoleCommandSender) {
            Messenger.sendError("Consoles can't use this command!", sender);
        }
        Player p = (Player) sender;

                String helms = getHelmsdeepSecrets(p);
                String thunder = getThunderstoneSecrets(p);
                String lake = getLakeboroughSecrets(p);
                String elwynn = getElwynnSecrets(p);
                String skyhold = getSkyholdSecrets(p);

                        p.sendMessage(ChatColor.DARK_AQUA + " -----Secrets----- ");
                        p.sendMessage(ChatColor.DARK_AQUA + " Helm's Deep: " + helms);
                        p.sendMessage(ChatColor.DARK_AQUA + " Thunderstone: " + thunder);
                        p.sendMessage(ChatColor.DARK_AQUA + " Lakeborough: " + lake);
                        p.sendMessage(ChatColor.DARK_AQUA + " Elwynn: " + elwynn);
                        p.sendMessage(ChatColor.DARK_AQUA + " Skyhold: " + skyhold);


        return true;
    }

    /**
     *
     * @param p the player to check the secrets for
     * @return a string returning the amount of secrets one has and can find.
     */
    public String getThunderstoneSecrets(Player p) {
        UUID uuid = p.getUniqueId();

        PlayerData data = ActiveData.getData(uuid);

        int foundAmount = 0;

        if (data.getFoundSecrets().contains("Thunderstone_Lantern")) {
            foundAmount = foundAmount + 1;
         }
        if (data.getFoundSecrets().contains("Thunderstone_Island")) {
            foundAmount = foundAmount + 1;
        }
        if (data.getFoundSecrets().contains("Thunderstone_Huntifi")) {
            foundAmount = foundAmount + 1;
        }
        if (data.getFoundSecrets().contains("Thunderstone_Skyview")) {
            foundAmount = foundAmount + 1;
        }
        if (data.getFoundSecrets().contains("Thunderstone_Cookie")) {
            foundAmount = foundAmount + 1;
        }
        if (data.getFoundSecrets().contains("Thunderstone_Fall")) {
            foundAmount = foundAmount + 1;
        }

        return ChatColor.AQUA + "(" + ChatColor.WHITE + foundAmount + ChatColor.AQUA + "/6)";
    }

    public String getHelmsdeepSecrets(Player p) {
        UUID uuid = p.getUniqueId();

        PlayerData data = ActiveData.getData(uuid);

                int foundAmount = 0;

                if (data.getFoundSecrets().contains("HD_HornSecret")) {
                    foundAmount = foundAmount + 1;
                }
                if (data.getFoundSecrets().contains("HD_HillSecret")) {
                    foundAmount = foundAmount + 1;
                }
                if (data.getFoundSecrets().contains("HD_Herugrim")) {
                    foundAmount = foundAmount + 1;
                }

        return ChatColor.AQUA + "(" + ChatColor.WHITE + foundAmount + ChatColor.AQUA + "/3)";
    }

    public String getLakeboroughSecrets(Player p) {
        UUID uuid = p.getUniqueId();

        PlayerData data = ActiveData.getData(uuid);

        int foundAmount = 0;

        if (data.getFoundSecrets().contains("Lakeborough_Well")) {
            foundAmount = foundAmount + 1;
        }
        if (data.getFoundSecrets().contains("Lakeborough_Mill")) {
            foundAmount = foundAmount + 1;
        }
        if (data.getFoundSecrets().contains("Lakeborough_Underwater")) {
            foundAmount = foundAmount + 1;
        }
        if (data.getFoundSecrets().contains("Lakeborough_Fireplace")) {
            foundAmount = foundAmount + 1;
        }

        return ChatColor.AQUA + "(" + ChatColor.WHITE + foundAmount + ChatColor.AQUA + "/4)";
    }


    public String getElwynnSecrets(Player p) {
        UUID uuid = p.getUniqueId();

        PlayerData data = ActiveData.getData(uuid);
        int foundAmount = 0;

        if (data.getFoundSecrets().contains("Elwynn_Abbey")) {
            foundAmount = foundAmount + 1;
        }
        if (data.getFoundSecrets().contains("Elwynn_Kobold")) {
            foundAmount = foundAmount + 1;
        }
        if (data.getFoundSecrets().contains("Elwynn_Sewers")) {
            foundAmount = foundAmount + 1;
        }
        if (data.getFoundSecrets().contains("Elwynn_Murloc")) {
            foundAmount = foundAmount + 1;
        }
        if (data.getFoundSecrets().contains("Elwynn_Lake")) {
            foundAmount = foundAmount + 1;
        }
        if (data.getFoundSecrets().contains("Elwynn_Inn")) {
            foundAmount = foundAmount + 1;
        }
        if (data.getFoundSecrets().contains("Elwynn_Tree")) {
            foundAmount = foundAmount + 1;
        }
        if (data.getFoundSecrets().contains("Elwynn_Training")) {
            foundAmount = foundAmount + 1;
        }
        if (data.getFoundSecrets().contains("Elwynn_Bridge")) {
            foundAmount = foundAmount + 1;
        }
        if (data.getFoundSecrets().contains("Elwynn_River")) {
            foundAmount = foundAmount + 1;
        }
        if (data.getFoundSecrets().contains("Elwynn_Hightree")) {
            foundAmount = foundAmount + 1;
        }

        return ChatColor.AQUA + "(" + ChatColor.WHITE + foundAmount + ChatColor.AQUA + "/11)";
    }

    public String getSkyholdSecrets(Player p) {
        UUID uuid = p.getUniqueId();

        PlayerData data = ActiveData.getData(uuid);

        int foundAmount = 0;

        if (data.getFoundSecrets().contains("Skyhold_Vault")) {
            foundAmount = foundAmount + 1;
        }
        if (data.getFoundSecrets().contains("Skyhold_Ravens")) {
            foundAmount = foundAmount + 1;
        }
        if (data.getFoundSecrets().contains("Skyhold_Top")) {
            foundAmount = foundAmount + 1;
        }
        if (data.getFoundSecrets().contains("Skyhold_Inquisitor")) {
            foundAmount = foundAmount + 1;
        }
        if (data.getFoundSecrets().contains("Skyhold_Attic")) {
            foundAmount = foundAmount + 1;
        }
        if (data.getFoundSecrets().contains("Skyhold_Barn")) {
            foundAmount = foundAmount + 1;
        }
        if (data.getFoundSecrets().contains("Skyhold_Statue")) {
            foundAmount = foundAmount + 1;
        }
        if (data.getFoundSecrets().contains("Skyhold_Water")) {
            foundAmount = foundAmount + 1;
        }
        if (data.getFoundSecrets().contains("Skyhold_Shield")) {
            foundAmount = foundAmount + 1;
        }
        if (data.getFoundSecrets().contains("Skyhold_Tomb")) {
            foundAmount = foundAmount + 1;
        }

        return ChatColor.AQUA + "(" + ChatColor.WHITE + foundAmount + ChatColor.AQUA + "/10)";
    }
}
