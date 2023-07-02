package me.huntifi.castlesiege.secrets.Skyhold;

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
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class Doors implements Listener {

    final Location soundDoorLoc = new Location(Main.plugin.getServer().getWorld("Skyhold"), 184, 113, 133);
    final Location soundVaultLoc = new Location(Main.plugin.getServer().getWorld("Skyhold"), 184, 113, 133);
    final Location vaultLockLoc = new Location(Main.plugin.getServer().getWorld("Skyhold"), 1660, 91, -111);
    final Location doorLockLoc = new Location(Main.plugin.getServer().getWorld("Skyhold"), 1709, 92, -89);
    final Location doorSchematicLoc = new Location(Main.plugin.getServer().getWorld("Skyhold"), 1709, 91, -90);
    final Location vaultSchematicLoc = new Location(Main.plugin.getServer().getWorld("Skyhold"), 1660, 92, -111);

    final Location statueLockLoc = new Location(Main.plugin.getServer().getWorld("Skyhold"), 1643, 96, -28);
    final Location statueSchematicLoc = new Location(Main.plugin.getServer().getWorld("Skyhold"), 1642, 93, -28);

    final Location tombSchematicLoc = new Location(Main.plugin.getServer().getWorld("Skyhold"), 1658, 80, -28);
    final Location tombCloseSchematicLoc = new Location(Main.plugin.getServer().getWorld("Skyhold"), 1658, 80, -27);

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

                            SchematicSpawner.spawnSchematic(doorSchematicLoc, "SkyholdSecretDoor");

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

                            SchematicSpawner.spawnSchematic(vaultSchematicLoc, "SkyholdVaultDoor");

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

                            Block b1 = Bukkit.getWorld("Skyhold").getBlockAt(new Location(Bukkit.getWorld("Skyhold"),1643 ,95 ,-27));
                            Block b2 = Bukkit.getWorld("Skyhold").getBlockAt(new Location(Bukkit.getWorld("Skyhold"),1643 ,94 ,-27));
                            Block b3 = Bukkit.getWorld("Skyhold").getBlockAt(new Location(Bukkit.getWorld("Skyhold"),1643 ,93 ,-27));
                            Block b12 = Bukkit.getWorld("Skyhold").getBlockAt(new Location(Bukkit.getWorld("Skyhold"),1643 ,95 ,-28));
                            Block b22 = Bukkit.getWorld("Skyhold").getBlockAt(new Location(Bukkit.getWorld("Skyhold"),1643 ,94 ,-28));
                            Block b32 = Bukkit.getWorld("Skyhold").getBlockAt(new Location(Bukkit.getWorld("Skyhold"),1643 ,93 ,-28));
                            Block b123 = Bukkit.getWorld("Skyhold").getBlockAt(new Location(Bukkit.getWorld("Skyhold"),1643 ,95 ,-29));
                            Block b223 = Bukkit.getWorld("Skyhold").getBlockAt(new Location(Bukkit.getWorld("Skyhold"),1643 ,94 ,-29));
                            Block b323 = Bukkit.getWorld("Skyhold").getBlockAt(new Location(Bukkit.getWorld("Skyhold"),1643 ,93 ,-29));
                            b1.setType(Material.AIR); b2.setType(Material.AIR); b3.setType(Material.AIR); b12.setType(Material.AIR); b22.setType(Material.AIR);
                            b32.setType(Material.AIR); b123.setType(Material.AIR); b223.setType(Material.AIR); b323.setType(Material.AIR);

                            Block b1a = Bukkit.getWorld("Skyhold").getBlockAt(new Location(Bukkit.getWorld("Skyhold"),1644 ,95 ,-27));
                            Block b2a = Bukkit.getWorld("Skyhold").getBlockAt(new Location(Bukkit.getWorld("Skyhold"),1644 ,94 ,-27));
                            Block b3a = Bukkit.getWorld("Skyhold").getBlockAt(new Location(Bukkit.getWorld("Skyhold"),1644 ,93 ,-27));
                            Block b12a = Bukkit.getWorld("Skyhold").getBlockAt(new Location(Bukkit.getWorld("Skyhold"),1644 ,95 ,-28));
                            Block b22a = Bukkit.getWorld("Skyhold").getBlockAt(new Location(Bukkit.getWorld("Skyhold"),1644 ,94 ,-28));
                            Block b32a = Bukkit.getWorld("Skyhold").getBlockAt(new Location(Bukkit.getWorld("Skyhold"),1644 ,93 ,-28));
                            Block b123a = Bukkit.getWorld("Skyhold").getBlockAt(new Location(Bukkit.getWorld("Skyhold"),1644 ,95 ,-29));
                            Block b223a = Bukkit.getWorld("Skyhold").getBlockAt(new Location(Bukkit.getWorld("Skyhold"),1644 ,94 ,-29));
                            Block b323a = Bukkit.getWorld("Skyhold").getBlockAt(new Location(Bukkit.getWorld("Skyhold"),1644 ,93 ,-29));
                            b1a.setType(Material.AIR); b2a.setType(Material.AIR); b3a.setType(Material.AIR); b12a.setType(Material.AIR); b22a.setType(Material.AIR);
                            b32a.setType(Material.AIR); b123a.setType(Material.AIR); b223a.setType(Material.AIR); b323a.setType(Material.AIR);

                            Block b1ab = Bukkit.getWorld("Skyhold").getBlockAt(new Location(Bukkit.getWorld("Skyhold"),1644 ,96 ,-27));
                            Block b2ab = Bukkit.getWorld("Skyhold").getBlockAt(new Location(Bukkit.getWorld("Skyhold"),1644 ,97 ,-27));
                            Block b3ab = Bukkit.getWorld("Skyhold").getBlockAt(new Location(Bukkit.getWorld("Skyhold"),1644 ,98 ,-27));
                            Block b12ab = Bukkit.getWorld("Skyhold").getBlockAt(new Location(Bukkit.getWorld("Skyhold"),1644 ,96 ,-28));
                            Block b22ab = Bukkit.getWorld("Skyhold").getBlockAt(new Location(Bukkit.getWorld("Skyhold"),1644 ,97 ,-28));
                            Block b32ab = Bukkit.getWorld("Skyhold").getBlockAt(new Location(Bukkit.getWorld("Skyhold"),1644 ,98 ,-28));
                            Block b123ab = Bukkit.getWorld("Skyhold").getBlockAt(new Location(Bukkit.getWorld("Skyhold"),1644 ,96 ,-29));
                            Block b223ab = Bukkit.getWorld("Skyhold").getBlockAt(new Location(Bukkit.getWorld("Skyhold"),1644 ,97 ,-29));
                            Block b323ab = Bukkit.getWorld("Skyhold").getBlockAt(new Location(Bukkit.getWorld("Skyhold"),1644 ,98 ,-29));
                            Block b324ab = Bukkit.getWorld("Skyhold").getBlockAt(new Location(Bukkit.getWorld("Skyhold"),1644 ,99 ,-28));
                            Block b325ab = Bukkit.getWorld("Skyhold").getBlockAt(new Location(Bukkit.getWorld("Skyhold"),1643 ,96 ,-28));
                            b1ab.setType(Material.AIR); b2ab.setType(Material.AIR); b3ab.setType(Material.AIR); b12ab.setType(Material.AIR); b22ab.setType(Material.AIR);
                            b32ab.setType(Material.AIR); b123ab.setType(Material.AIR); b223ab.setType(Material.AIR); b323ab.setType(Material.AIR);
                            b324ab.setType(Material.AIR); b325ab.setType(Material.AIR);

                            SchematicSpawner.spawnSchematic(statueSchematicLoc, "SkyholSecretStatue");

                        }
                    }.runTaskLater(Main.plugin, 30);
            }
        }
    }

    final Location button1Loc = new Location(Main.plugin.getServer().getWorld("Skyhold"), 1658, 82, -33);
    final Location button2Loc = new Location(Main.plugin.getServer().getWorld("Skyhold"), 1661, 81, -30);
    final Location button3Loc = new Location(Main.plugin.getServer().getWorld("Skyhold"), 1658, 80, -27);
    final Location primeButtonLoc = new Location(Main.plugin.getServer().getWorld("Skyhold"), 1658, 82, -30);
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

                          Block b1 = Bukkit.getWorld("Skyhold").getBlockAt(new Location(Bukkit.getWorld("Skyhold"),1657 ,81 ,-29));
                          Block b2 = Bukkit.getWorld("Skyhold").getBlockAt(new Location(Bukkit.getWorld("Skyhold"),1657 ,80 ,-29));
                          Block b3 = Bukkit.getWorld("Skyhold").getBlockAt(new Location(Bukkit.getWorld("Skyhold"),1658 ,81 ,-29));
                          Block b4 = Bukkit.getWorld("Skyhold").getBlockAt(new Location(Bukkit.getWorld("Skyhold"),1658 ,80 ,-29));
                          Block b5 = Bukkit.getWorld("Skyhold").getBlockAt(new Location(Bukkit.getWorld("Skyhold"),1659 ,81 ,-29));
                          Block b6 = Bukkit.getWorld("Skyhold").getBlockAt(new Location(Bukkit.getWorld("Skyhold"),1659,80 ,-29));
                          b1.setType(Material.AIR); b2.setType(Material.AIR); b3.setType(Material.AIR); b4.setType(Material.AIR);
                          b5.setType(Material.AIR); b6.setType(Material.AIR);

                          SchematicSpawner.spawnSchematic(tombSchematicLoc, "SkyholdTombSecret");

                          new BukkitRunnable() {

                              @Override
                              public void run() {
                                  Bukkit.getWorld("Skyhold").playSound(tombCloseSchematicLoc, Sound.BLOCK_GRINDSTONE_USE, 5, 1);

                                  Block b1 = Bukkit.getWorld("Skyhold").getBlockAt(new Location(Bukkit.getWorld("Skyhold"),1657 ,81 ,-31));
                                  Block b2 = Bukkit.getWorld("Skyhold").getBlockAt(new Location(Bukkit.getWorld("Skyhold"),1657 ,80 ,-31));
                                  Block b3 = Bukkit.getWorld("Skyhold").getBlockAt(new Location(Bukkit.getWorld("Skyhold"),1658 ,81 ,-31));
                                  Block b4 = Bukkit.getWorld("Skyhold").getBlockAt(new Location(Bukkit.getWorld("Skyhold"),1658 ,80 ,-31));
                                  Block b5 = Bukkit.getWorld("Skyhold").getBlockAt(new Location(Bukkit.getWorld("Skyhold"),1659 ,81 ,-31));
                                  Block b6 = Bukkit.getWorld("Skyhold").getBlockAt(new Location(Bukkit.getWorld("Skyhold"),1659,80 ,-31));
                                  b1.setType(Material.AIR); b2.setType(Material.AIR); b3.setType(Material.AIR); b4.setType(Material.AIR);
                                  b5.setType(Material.AIR); b6.setType(Material.AIR);

                                  SchematicSpawner.spawnSchematic(tombSchematicLoc, "SkyholdCloseTombSecret");

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
