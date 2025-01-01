package me.greenfoot5.castlesiege.commands.info;

import me.greenfoot5.castlesiege.Main;
import me.greenfoot5.castlesiege.gui.BuyKitGui;
import me.greenfoot5.castlesiege.kits.kits.Kit;
import me.greenfoot5.conwymc.util.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * Purchases a kit for a player
 */
public class PreviewCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
            // Only players can buy kits
            if (!(sender instanceof Player)) {
                Messenger.sendError("Not a console command.", sender);
                return;
            }

            // Command must have kit parameter and may have player parameter
            if (args.length != 1) {
                Messenger.sendError("Use: /preview <kit>", sender);
                return;
            }

            // Get values
            String kitName = args[0];
            Kit kit = Kit.getKit(kitName);
            Player player = (Player) sender;

            BuyKitGui gui = new BuyKitGui(kit, -1, player);
        });

        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        List<String> options = new ArrayList<>();
        if (args.length == 1) {
            Stream<String> values = new ArrayList<>(Kit.getKits()).stream();
            StringUtil.copyPartialMatches(args[0], Arrays.asList(values.toArray(String[]::new)), options);
            return options;
        }

        return null;
    }
}
