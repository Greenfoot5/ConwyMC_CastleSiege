package me.huntifi.castlesiege.commands.staff.maps;

import me.huntifi.castlesiege.database.CSActiveData;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.maps.CoreMap;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.conwymc.commands.chat.GlobalChatCommand;
import me.huntifi.conwymc.events.nametag.UpdateNameTagEvent;
import me.huntifi.conwymc.util.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Allows players to spectate a match
 */
public class SpectateCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        Player player;
        if (args.length == 0) {
            player = (Player) sender;
        } else {
            player = Bukkit.getPlayer(args[0]);
        }

        if (player == null) {
            Messenger.sendError("Could not find player: <red>" + args[0], sender);
            return true;
        }

        if (InCombat.isPlayerInCombat(player.getUniqueId())) {
            Messenger.sendError("That player is currently in combat!", sender);
            return true;
        }

        // If the player is a spectator, add them to a team
        if (MapController.isSpectator(player.getUniqueId())) {
            MapController.removeSpectator(player);

            // Assign stored kit
            Kit kit = Kit.getKit(CSActiveData.getData(player.getUniqueId()).getKit());
            if (kit != null && kit.canSelect(player, true, true, false))
                kit.addPlayer(player.getUniqueId(), true);
            else
                Kit.getKit("Fisherman").addPlayer(player.getUniqueId(), true);

        } else {
            CSActiveData.getData(player.getUniqueId()).setChatMode(GlobalChatCommand.CHAT_MODE);
            MapController.addSpectator(player);

            // Teleport the player
            if (InCombat.isPlayerInLobby(player.getUniqueId())) {
                if (MapController.getCurrentMap() instanceof CoreMap) {
                    CoreMap coreMap = (CoreMap) MapController.getCurrentMap();
                    player.teleport(coreMap.getCore(1).getSpawnPoint());
                } else {
                    player.teleport(MapController.getCurrentMap().flags[0].getSpawnPoint());
                }
            }
            Bukkit.getPluginManager().callEvent(new UpdateNameTagEvent(player));
        }
        return true;
    }
}
