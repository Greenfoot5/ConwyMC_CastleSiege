package me.greenfoot5.castlesiege.commands.staff.maps;

import me.greenfoot5.castlesiege.commands.chat.TeamChatCommand;
import me.greenfoot5.castlesiege.data_types.CSPlayerData;
import me.greenfoot5.castlesiege.database.CSActiveData;
import me.greenfoot5.castlesiege.events.combat.InCombat;
import me.greenfoot5.castlesiege.kits.kits.Kit;
import me.greenfoot5.castlesiege.maps.CoreMap;
import me.greenfoot5.castlesiege.maps.MapController;
import me.greenfoot5.castlesiege.maps.TeamController;
import me.greenfoot5.conwymc.commands.chat.GlobalChatCommand;
import me.greenfoot5.conwymc.events.nametag.UpdateNameTagEvent;
import me.greenfoot5.conwymc.util.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

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
        if (TeamController.isSpectating(player)) {
            TeamController.joinSmallestTeam(player.getUniqueId(), MapController.getCurrentMap());

            // Assign stored kit
            Kit kit = Kit.getKit(CSActiveData.getData(player.getUniqueId()).getKit());
            if (kit != null && kit.canSelect(player, true, true, false))
                kit.addPlayer(player.getUniqueId(), true);
            else
                Kit.getKit("Swordsman").addPlayer(player.getUniqueId(), true);

        } else {

            // If the player is talking in team chat, let's move them back to global
            if (TeamController.isPlaying(player)) {
                CSPlayerData data = CSActiveData.getData(player.getUniqueId());
                assert data != null;
                if (Objects.equals(data.getChatMode(), TeamChatCommand.CHAT_MODE)) {
                    data.setChatMode(GlobalChatCommand.CHAT_MODE);
                }
            }

            TeamController.joinSpectator(player);

            // Teleport the player
            if (InCombat.isPlayerInLobby(player.getUniqueId())) {
                if (MapController.getCurrentMap() instanceof CoreMap coreMap) {
                    player.teleport(coreMap.getCore(1).getSpawnPoint());
                } else {
                    player.teleport(MapController.getCurrentMap().flags[0].getSpawnPoint());
                }
            }
        }

        Bukkit.getPluginManager().callEvent(new UpdateNameTagEvent(player));
        return true;
    }
}
