package me.huntifi.castlesiege.secrets.Skyhold;

import com.sk89q.worldedit.WorldEditException;
import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.secrets.SecretItems;
import me.huntifi.castlesiege.structures.SchematicSpawner;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class Doors implements Listener {

    Location soundDoorLoc = new Location(Main.plugin.getServer().getWorld("Skyhold"), 184, 113, 133);
    Location soundVaultLoc = new Location(Main.plugin.getServer().getWorld("Skyhold"), 184, 113, 133);
    Location vaultLockLoc = new Location(Main.plugin.getServer().getWorld("Skyhold"), 1660, 91, -111);
    Location doorLockLoc = new Location(Main.plugin.getServer().getWorld("Skyhold"), 1709, 92, -89);
    Location doorSchematicLoc = new Location(Main.plugin.getServer().getWorld("Skyhold"), 1709, 91, -90);
    Location vaultSchematicLoc = new Location(Main.plugin.getServer().getWorld("Skyhold"), 1660, 92, -111);

    @EventHandler
    public void onClickDoor(PlayerInteractEvent event) {

        if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {

            if (event.getClickedBlock().getType().equals(Material.TRIPWIRE_HOOK) && event.getClickedBlock().getLocation().equals(doorLockLoc)) {

                if (event.getPlayer().getInventory().getItemInMainHand().equals(SecretItems.skyholdKeyDoor())) {

                    Player p = event.getPlayer();
                    p.getInventory().remove(SecretItems.skyholdKeyDoor());

                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_GREEN + "You have opened this door...!"));

                    new BukkitRunnable() {

                        @Override
                        public void run() {
                            Bukkit.getWorld("Skyhold").playSound(soundDoorLoc, Sound.BLOCK_WOODEN_DOOR_OPEN, 5, 1);

                            try {
                                SchematicSpawner.spawnSchematic(doorSchematicLoc, "SkyholdSecretDoor", "Skyhold");
                            } catch (WorldEditException e) {
                                throw new RuntimeException(e);
                            }

                        }
                    }.runTaskLater(Main.plugin, 10);

                }
            }
        }
    }


    @EventHandler
    public void onClickVault(PlayerInteractEvent event) {

        if(event.getAction() == Action.RIGHT_CLICK_BLOCK){

            if (event.getClickedBlock().getType().equals(Material.DISPENSER) && event.getClickedBlock().getLocation().equals(vaultLockLoc)) {

                if (event.getPlayer().getInventory().getItemInMainHand().equals(SecretItems.skyholdKeyInquisitor())) {

                    Player p = event.getPlayer();
                    p.getInventory().remove(SecretItems.skyholdKeyInquisitor());

                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_GREEN + "You have opened the vault...!"));

                    new BukkitRunnable() {

                        @Override
                        public void run() {
                             Bukkit.getWorld("Skyhold").playSound(soundVaultLoc, Sound.BLOCK_IRON_DOOR_OPEN, 5, 1);

                            try {
                                SchematicSpawner.spawnSchematic(vaultSchematicLoc, "SkyholdVaultDoor", "Skyhold");
                            } catch (WorldEditException e) {
                                throw new RuntimeException(e);
                            }

                        }
                    }.runTaskLater(Main.plugin, 30);

                }
            }
        }
    }
}
