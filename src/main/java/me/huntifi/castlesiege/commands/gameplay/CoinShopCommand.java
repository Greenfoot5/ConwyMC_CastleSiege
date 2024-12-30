package me.huntifi.castlesiege.commands.gameplay;

import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.maps.TeamController;
import me.huntifi.conwymc.gui.Gui;
import me.huntifi.conwymc.gui.GuiController;
import me.huntifi.conwymc.util.Messenger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Teleports the player to the kit shop when outside of combat of course.
 */
public class CoinShopCommand implements CommandExecutor {

    public Location kitshop = new Location(Bukkit.getWorld("ShopDraft"), 75, 160, 287, 90, 0);

    /**
     * Teleports the player to the kit shop
     * @param sender Source of the command
     * @param command Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return true
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            Messenger.sendError("Console can't teleport to the shop", sender);
        }
        assert sender instanceof Player;
        Player player = (Player) sender;
        // Prevent using in lobby
        if (!InCombat.isPlayerInLobby(player.getUniqueId())) {
            Messenger.sendError("You can't do this outside of spawn lobbies!", sender);
        }
        if (player.getWorld().equals(Bukkit.getWorld("ShopDraft"))) {
            player.teleport(TeamController.getTeam(player.getUniqueId()).lobby.spawnPoint);
        }
        else {
            player.teleport(kitshop);
        }
        return true;
    }


}
