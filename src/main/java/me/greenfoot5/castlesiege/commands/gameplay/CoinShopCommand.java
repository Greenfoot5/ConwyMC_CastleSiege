package me.greenfoot5.castlesiege.commands.gameplay;

import me.greenfoot5.castlesiege.events.combat.InCombat;
import me.greenfoot5.castlesiege.maps.TeamController;
import me.greenfoot5.conwymc.util.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

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
