package me.huntifi.castlesiege.maps;

import me.huntifi.castlesiege.flags.Flag;
import me.huntifi.castlesiege.joinevents.stats.StatsChanging;
import me.huntifi.castlesiege.kits.EnderchestRefill;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.UUID;

public class WoolMapBlock {
    public String flagName;
    public Location blockLocation;
    public Location signLocation;
    public BlockFace signDirection;

    /**
     * Attempts to spawn a player at a flag
     * @param uuid The uuid of the player
     */
    public void SpawnPlayer(UUID uuid)
    {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null)
        {
            return;
        }
        Team team = MapController.getCurrentMap().getTeam(uuid);
        Flag flag = MapController.getCurrentMap().getFlag(flagName);

        if (team != null && team.hasPlayer(uuid))
        {
            if (!Objects.equals(flag.currentOwners, team.name))
            {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                        TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "Your team does not own this flag at the moment."));
            }
            else if (flag.underAttack())
            {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                        TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "You can't spawn here. This flag is under attack!"));
            }
            else if (StatsChanging.getKit(player.getUniqueId()) != null)
            {
                player.sendMessage(ChatColor.DARK_RED + "You can't join the battlefield without a kit/class!");
                player.sendMessage(ChatColor.DARK_RED + "Choose a kit/class with the command " + ChatColor.RED + "/class <Classname>" + ChatColor.DARK_RED + "!");
            } else {
                player.teleport(flag.spawnPoint);
                EnderchestRefill.refill(player);
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                        TextComponent.fromLegacyText(flag.getSpawnMessage()));
            }
        }
    }
}
