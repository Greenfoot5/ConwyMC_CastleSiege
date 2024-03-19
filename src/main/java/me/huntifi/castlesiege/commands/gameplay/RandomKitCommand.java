package me.huntifi.castlesiege.commands.gameplay;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.conwymc.util.Messenger;
import me.huntifi.castlesiege.events.curses.BindingCurse;
import me.huntifi.castlesiege.events.curses.CurseExpired;
import me.huntifi.castlesiege.kits.kits.CoinKit;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.maps.MapController;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

/**
 * Selects a random kit for the user
 */
public class RandomKitCommand implements CommandExecutor, Listener {

    private static final List<UUID> activeBindings = new ArrayList<>();

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
        Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> randomKit(sender));
        return true;
    }

    private void randomKit(CommandSender sender) {
        if (!(sender instanceof Player)) {
            Messenger.sendError("Console cannot select kits!", sender);
            return;
        }

        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();
        if (!MapController.getPlayers().contains(player.getUniqueId())) {
            Messenger.sendError("You must be on a team to select kits", sender);
            return;
        }

        if (activeBindings.contains(null) || activeBindings.contains(uuid)) {
            Messenger.sendCurse("A curse is preventing you from changing kits!", sender);
            return;
        }

        // Get unlocked kits, or all if it's Friday
        Collection<String> unlockedKits = CoinKit.isFree() ? Kit.getKits() : ActiveData.getData(uuid).getUnlockedKits();
        ArrayList<Kit> kits = new ArrayList<>();

        unlockedKits.forEach((kitName) -> {
            Kit kit = Kit.getKit(kitName);
            if (kit == null || !kit.canSelect(player, true, false, true)) {
                return;
            }
            kits.add(kit);
        });

        kits.get(new Random().nextInt(kits.size())).addPlayer(uuid, true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void bindingActive(BindingCurse curse) {
        activeBindings.add(curse.getPlayer());
    }

    @EventHandler(ignoreCancelled = true)
    public void bindingExpired(CurseExpired curse) {
        if (Objects.equals(curse.getDisplayName(), BindingCurse.name))
            activeBindings.remove(curse.getPlayer());
    }
}
