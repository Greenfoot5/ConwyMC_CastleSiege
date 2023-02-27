package me.huntifi.castlesiege.commands.gameplay;

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
        boosters.sort(Booster::compareTo);
        for (int i = 0; i < boosters.size(); i++) {
            Booster booster = boosters.get(i);
            gui.addItem(booster.getName(), booster.material, booster.getLore(), i, "/booster use " + booster.id, true);
        }
        return gui;
    }

    private void useBooster(@NotNull CommandSender sender, @NotNull String[] args) {
        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();
        PlayerData data = ActiveData.getData(uuid);
        int id = Integer.parseInt(args[1]);
        for (Booster booster : data.getBoosters()) {
            if (booster.id == id) {
                data.useBooster(uuid, booster);

                return;
            }
        }
        Messenger.sendError("You don't own a booster with that id!", sender);
    }
}
