package me.huntifi.castlesiege.commands.gameplay;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.Booster;
import me.huntifi.castlesiege.data_types.PlayerData;
import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.gui.Gui;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BoosterCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            showBoosters(sender);
            return true;
        }
        if (args[0].equals("use")) {
            useBooster(sender, args);
            return true;
        }
        Messenger.sendError("Invalid arg: " + args[0] + ". Should be 'use' or a type of booster.", sender);
        return true;
    }

    private void showBoosters(@NotNull CommandSender sender) {
        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();
        PlayerData data = ActiveData.getData(uuid);

        Gui gui = createGUI(data.getBoosters());
        gui.open(player);
    }

    public Gui createGUI(List<Booster> boosters) {
        Gui gui = new Gui("Booster Selection", (boosters.size() / 9 + 1));
        boosters.sort(null);
        for (int i = 0; i < boosters.size(); i++) {
            Booster booster = boosters.get(i);
            Main.instance.getLogger().info(booster.toString());
            List<String> lore = new ArrayList<>();
            gui.addItem(booster.toString(), booster.material, lore, i, "booster use " + booster.id, true);
        }
        return gui;
    }

    private void useBooster(@NotNull CommandSender sender, @NotNull String[] args) {
//        Booster booster = createBooster(sender, args);
//        if (booster == null) {
//            return;
//        }
//
//        UUID uuid = getUUID(args[0]);
//        assert uuid != null;
//        PlayerData data = ActiveData.getData(uuid);
//        if (data != null) {
//            if (!giveBooster(data, uuid, booster)) {
//                Messenger.sendError("Failed to add booster to database. Try again?", sender);
//                return;
//            }
//        } else if (!giveBooster(uuid, booster)) {
//            Messenger.sendError("Failed to add booster to offline player. Try again?", sender);
//            return;
//        }

        sendConfirmMessage(sender);
    }

    private void sendConfirmMessage(CommandSender sender) {
        Messenger.sendSuccess("Booster granted", sender);
    }
}
