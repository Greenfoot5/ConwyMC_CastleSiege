package me.greenfoot5.castlesiege.commands.info;

import me.greenfoot5.castlesiege.data_types.CSPlayerData;
import me.greenfoot5.castlesiege.database.CSActiveData;
import me.greenfoot5.conwymc.util.Messenger;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
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
        String abraham = getAbrakhanSecrets(p);
        String hallOfHerakles = getHallOfHeraklesSecrets(p);

        p.sendMessage(Messenger.mm.deserialize("<dark_aqua> <st>━━━━━</st>Secrets<st>━━━━━</st> </dark_aqua>"));
        p.sendMessage(ChatColor.DARK_AQUA + " Abrakhan: " + abraham);
        p.sendMessage(ChatColor.DARK_AQUA + " Elwynn: " + elwynn);
        p.sendMessage(ChatColor.DARK_AQUA + " Helm's Deep: " + helms);
        p.sendMessage(ChatColor.DARK_AQUA + " Lakeborough: " + lake);
        p.sendMessage(ChatColor.DARK_AQUA + " Skyhold: " + skyhold);
        p.sendMessage(ChatColor.DARK_AQUA + " Thunderstone: " + thunder);
        p.sendMessage(ChatColor.DARK_AQUA + " Hall Of Herakles: " + hallOfHerakles);



        return true;
    }

    /**
     *
     * @param p the player to check the secrets for
     * @return a string returning the amount of secrets one has and can find.
     */
    private String getThunderstoneSecrets(Player p) {
        UUID uuid = p.getUniqueId();

        CSPlayerData data = CSActiveData.getData(uuid);

        int foundAmount = 0;

        if (data.hasSecret("Thunderstone_Lantern")) {
            foundAmount = foundAmount + 1;
         }
        if (data.hasSecret("Thunderstone_Island")) {
            foundAmount = foundAmount + 1;
        }
        if (data.hasSecret("Thunderstone_Huntifi")) {
            foundAmount = foundAmount + 1;
        }
        if (data.hasSecret("Thunderstone_Skyview")) {
            foundAmount = foundAmount + 1;
        }
        if (data.hasSecret("Thunderstone_Cookie")) {
            foundAmount = foundAmount + 1;
        }
        if (data.hasSecret("Thunderstone_Fall")) {
            foundAmount = foundAmount + 1;
        }

        return ChatColor.AQUA + "(" + ChatColor.WHITE + foundAmount + ChatColor.AQUA + "/6)";
    }

    private String getHelmsdeepSecrets(Player p) {
        UUID uuid = p.getUniqueId();

        CSPlayerData data = CSActiveData.getData(uuid);

                int foundAmount = 0;

                if (data.hasSecret("HD_HornSecret")) {
                    foundAmount = foundAmount + 1;
                }
                if (data.hasSecret("HD_HillSecret")) {
                    foundAmount = foundAmount + 1;
                }
                if (data.hasSecret("HD_Herugrim")) {
                    foundAmount = foundAmount + 1;
                }

        return ChatColor.AQUA + "(" + ChatColor.WHITE + foundAmount + ChatColor.AQUA + "/3)";
    }

    private String getLakeboroughSecrets(Player p) {
        UUID uuid = p.getUniqueId();

        CSPlayerData data = CSActiveData.getData(uuid);

        int foundAmount = 0;

        if (data.hasSecret("Lakeborough_Well")) {
            foundAmount = foundAmount + 1;
        }
        if (data.hasSecret("Lakeborough_Mill")) {
            foundAmount = foundAmount + 1;
        }
        if (data.hasSecret("Lakeborough_Underwater")) {
            foundAmount = foundAmount + 1;
        }
        if (data.hasSecret("Lakeborough_Fireplace")) {
            foundAmount = foundAmount + 1;
        }

        return ChatColor.AQUA + "(" + ChatColor.WHITE + foundAmount + ChatColor.AQUA + "/4)";
    }

    private String getAbrakhanSecrets(Player p) {
        UUID uuid = p.getUniqueId();

        CSPlayerData data = CSActiveData.getData(uuid);

        int foundAmount = 0;

        if (data.hasSecret("Abrakhan_Sewer")) {
            foundAmount = foundAmount + 1;
        }
        if (data.hasSecret("Abrakhan_Vault")) {
            foundAmount = foundAmount + 1;
        }
        if (data.hasSecret("Abrakhan_Tunnel")) {
            foundAmount = foundAmount + 1;
        }
        if (data.hasSecret("Abrakhan_Prison")) {
            foundAmount = foundAmount + 1;
        }

        return ChatColor.AQUA + "(" + ChatColor.WHITE + foundAmount + ChatColor.AQUA + "/4)";
    }


    private String getElwynnSecrets(Player p) {
        UUID uuid = p.getUniqueId();

        CSPlayerData data = CSActiveData.getData(uuid);
        int foundAmount = 0;

        if (data.hasSecret("Elwynn_Abbey")) {
            foundAmount = foundAmount + 1;
        }
        if (data.hasSecret("Elwynn_Kobold")) {
            foundAmount = foundAmount + 1;
        }
        if (data.hasSecret("Elwynn_Sewers")) {
            foundAmount = foundAmount + 1;
        }
        if (data.hasSecret("Elwynn_Murloc")) {
            foundAmount = foundAmount + 1;
        }
        if (data.hasSecret("Elwynn_Lake")) {
            foundAmount = foundAmount + 1;
        }
        if (data.hasSecret("Elwynn_Inn")) {
            foundAmount = foundAmount + 1;
        }
        if (data.hasSecret("Elwynn_Tree")) {
            foundAmount = foundAmount + 1;
        }
        if (data.hasSecret("Elwynn_Training")) {
            foundAmount = foundAmount + 1;
        }
        if (data.hasSecret("Elwynn_Bridge")) {
            foundAmount = foundAmount + 1;
        }
        if (data.hasSecret("Elwynn_River")) {
            foundAmount = foundAmount + 1;
        }
        if (data.hasSecret("Elwynn_Hightree")) {
            foundAmount = foundAmount + 1;
        }

        return ChatColor.AQUA + "(" + ChatColor.WHITE + foundAmount + ChatColor.AQUA + "/11)";
    }

    private String getSkyholdSecrets(Player p) {
        UUID uuid = p.getUniqueId();

        CSPlayerData data = CSActiveData.getData(uuid);

        int foundAmount = 0;

        if (data.hasSecret("Skyhold_Vault")) {
            foundAmount = foundAmount + 1;
        }
        if (data.hasSecret("Skyhold_Ravens")) {
            foundAmount = foundAmount + 1;
        }
        if (data.hasSecret("Skyhold_Top")) {
            foundAmount = foundAmount + 1;
        }
        if (data.hasSecret("Skyhold_Inquisitor")) {
            foundAmount = foundAmount + 1;
        }
        if (data.hasSecret("Skyhold_Attic")) {
            foundAmount = foundAmount + 1;
        }
        if (data.hasSecret("Skyhold_Barn")) {
            foundAmount = foundAmount + 1;
        }
        if (data.hasSecret("Skyhold_Statue")) {
            foundAmount = foundAmount + 1;
        }
        if (data.hasSecret("Skyhold_Water")) {
            foundAmount = foundAmount + 1;
        }
        if (data.hasSecret("Skyhold_Shield")) {
            foundAmount = foundAmount + 1;
        }
        if (data.hasSecret("Skyhold_Tomb")) {
            foundAmount = foundAmount + 1;
        }

        return ChatColor.AQUA + "(" + ChatColor.WHITE + foundAmount + ChatColor.AQUA + "/10)";
    }

    private String getHallOfHeraklesSecrets(Player p) {
        UUID uuid = p.getUniqueId();

        CSPlayerData data = CSActiveData.getData(uuid);

        int foundAmount = 0;
        String[] secrets = {"HallOfHerces_Waterfall", "HallOfHerakles_Pillar", "HallOfHerakles_Sewers",
                "HallOfHerakles_Roots", "HallOfHerakles_TreeTop", "HallOfHerakles_EagleHall"};

        for (String secret : secrets) if (data.hasSecret(secret)) foundAmount++;

        return ChatColor.AQUA + "(" + ChatColor.WHITE + foundAmount + ChatColor.AQUA + "/6)";
    }
}
