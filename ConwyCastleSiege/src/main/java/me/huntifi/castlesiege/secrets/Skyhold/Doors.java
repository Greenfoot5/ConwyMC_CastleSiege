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

    Location statueLockLoc = new Location(Main.plugin.getServer().getWorld("Skyhold"), 1643, 96, -28);
    Location statueSchematicLoc = new Location(Main.plugin.getServer().getWorld("Skyhold"), 1642, 93, -28);

    Location tombSchematicLoc = new Location(Main.plugin.getServer().getWorld("Skyhold"), 1658, 80, -28);

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


    @EventHandler
    public void onClickStatue(PlayerInteractEvent event) {

        if(event.getAction() == Action.RIGHT_CLICK_BLOCK){

            if (event.getClickedBlock().getType().equals(Material.TRIPWIRE_HOOK) && event.getClickedBlock().getLocation().equals(statueLockLoc)) {

                    Player p = event.getPlayer();

                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_GREEN + "You have opened a secret passage...!"));

                    new BukkitRunnable() {

                        @Override
                        public void run() {
                            Bukkit.getWorld("Skyhold").playSound(statueLockLoc, Sound.BLOCK_GRINDSTONE_USE, 5, 1);

                            try {
                                SchematicSpawner.spawnSchematic(statueSchematicLoc, "SkyholStatueSecret", "Skyhold");
                            } catch (WorldEditException e) {
                                throw new RuntimeException(e);
                            }

                        }
                    }.runTaskLater(Main.plugin, 30);
            }
        }
    }

    Location button1Loc = new Location(Main.plugin.getServer().getWorld("Skyhold"), 1658, 82, -33);
    Location button2Loc = new Location(Main.plugin.getServer().getWorld("Skyhold"), 1661, 81, -30);
    Location button3Loc = new Location(Main.plugin.getServer().getWorld("Skyhold"), 1658, 80, -27);
    Location primeButtonLoc = new Location(Main.plugin.getServer().getWorld("Skyhold"), 1658, 82, -30);
    String code = "";
    @EventHandler
    public void onClickTomb(PlayerInteractEvent event) {

        if(event.getAction() == Action.RIGHT_CLICK_BLOCK){

            if (event.getClickedBlock().getType().equals(Material.STONE_BUTTON) && event.getClickedBlock().getLocation().equals(button1Loc)) {
                code = code + "3";
            } else if (event.getClickedBlock().getType().equals(Material.STONE_BUTTON) && event.getClickedBlock().getLocation().equals(button2Loc)) {
                code = code + "2";
            } else if (event.getClickedBlock().getType().equals(Material.STONE_BUTTON) && event.getClickedBlock().getLocation().equals(button3Loc)) {
                code = code + "1";
            } else if (event.getClickedBlock().getType().equals(Material.POLISHED_BLACKSTONE_BUTTON) && event.getClickedBlock().getLocation().equals(primeButtonLoc)) {
                code = code + "0";
              if (code.equalsIgnoreCase("3210")) {
                  new BukkitRunnable() {

                      @Override
                      public void run() {
                          Bukkit.getWorld("Skyhold").playSound(tombSchematicLoc, Sound.BLOCK_GRINDSTONE_USE, 5, 1);

                          try {
                              SchematicSpawner.spawnSchematic(tombSchematicLoc, "SkyholdTombSecret", "Skyhold");
                          } catch (WorldEditException e) {
                              throw new RuntimeException(e);
                          }

                          new BukkitRunnable() {

                              @Override
                              public void run() {
                                  Bukkit.getWorld("Skyhold").playSound(tombSchematicLoc, Sound.BLOCK_GRINDSTONE_USE, 5, 1);

                                  try {
                                      SchematicSpawner.spawnSchematic(tombSchematicLoc, "SkyholdCloseTombSecret", "Skyhold");
                                  } catch (WorldEditException e) {
                                      throw new RuntimeException(e);
                                  }

                              }
                          }.runTaskLater(Main.plugin, 600);

                      }
                  }.runTaskLater(Main.plugin, 30);
              } else {
                  code = "";
              }
            }
        }
    }
}
