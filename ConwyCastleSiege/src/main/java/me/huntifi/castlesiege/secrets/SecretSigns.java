package me.huntifi.castlesiege.secrets;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.PlayerData;
import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.castlesiege.database.LoadData;
import me.huntifi.castlesiege.database.StoreData;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.TimerState;
import me.huntifi.castlesiege.maps.WoolMapBlock;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.UUID;

public class SecretSigns implements Listener {

    String helmsDeep1 = "HD_HillSecret";
    String helmsDeep2 = "HD_Herugrim";
    String skyhold1 = "Skyhold_Vault";
    String skyhold2 = "Skyhold_Ravens";
    String skyhold3 = "Skyhold_Top";
    String skyhold4 = "Skyhold_Inquisitor";
    String skyhold5 = "Skyhold_Attic";
    String skyhold6 = "Skyhold_Barn";
    String skyhold7 = "Skyhold_Statue";
    String skyhold8 = "Skyhold_Water";
    String thunderstone1 = "Thunderstone_Island";
    String thunderstone2 = "Thunderstone_Huntifi";
    String thunderstone3 = "Thunderstone_Skyview";
    String thunderstone4 = "Thunderstone_Cookie";
    String thunderstone5 = "Thunderstone_Fall";
    String lakeborough1 = "Lakeborough_Well";
    String lakeborough2 = "Lakeborough_Mill";
    String lakeborough4 = "Lakeborough_Underwater";
    String lakeborough5 = "Lakeborough_Fireplace";
    String elwynn1 = "Elwynn_Abbey";
    String elwynn2 = "Elwynn_Kobold";
    String elwynn3 = "Elwynn_Sewers";
    String elwynn4 = "Elwynn_Murloc";
    String elwynn5 = "Elwynn_Lake";
    String elwynn6 = "Elwynn_Inn";
    String elwynn7 = "Elwynn_Tree";
    String elwynn8 = "Elwynn_Training";
    String elwynn9 = "Elwynn_Bridge";
    String elwynn10 = "Elwynn_River";
    String elwynn11 = "Elwynn_Hightree";


    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {

        Player player = e.getPlayer();

        if (e.getClickedBlock() == null) { return; }

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {

        if (e.getClickedBlock() != null && e.getClickedBlock().getState() instanceof Sign) {

                if (MapController.getCurrentMap().worldName.equalsIgnoreCase("HelmsDeep")) {

                    Location hillSecret = new Location(Bukkit.getWorld("HelmsDeep"), 900, 62, 1116);
                    Location herugrimSecret = new Location(Bukkit.getWorld("HelmsDeep"), 980, 58, 986);

                    if (e.getClickedBlock().getLocation().equals(hillSecret)) {
                       registerFoundSecret(player, helmsDeep1);
                    } else if (e.getClickedBlock().getLocation().equals(herugrimSecret)) {
                        registerFoundSecret(player, helmsDeep2);
                    }
                } else if (MapController.getCurrentMap().worldName.equalsIgnoreCase("Skyhold")) {
                    Location vaultSecret = new Location(Bukkit.getWorld("Skyhold"), 1660, 94, -118);
                    Location ravensSecret = new Location(Bukkit.getWorld("Skyhold"), 1686, 124, -94);
                    Location topSecret = new Location(Bukkit.getWorld("Skyhold"), 1661, 131, -67);
                    Location inquisitorSecret = new Location(Bukkit.getWorld("Skyhold"), 1603, 179, -115);
                    Location atticSecret = new Location(Bukkit.getWorld("Skyhold"), 1615, 102, -32);
                    Location barnSecret = new Location(Bukkit.getWorld("Skyhold"), 1729, 101, -59);
                    Location statueSecret = new Location(Bukkit.getWorld("Skyhold"), 1574, 112, -88);
                    Location waterSecret = new Location(Bukkit.getWorld("Skyhold"), 1594, 76, -162);

                    if (e.getClickedBlock().getLocation().equals(vaultSecret)) {
                        registerFoundSecret(player, skyhold1);
                    } else if (e.getClickedBlock().getLocation().equals(ravensSecret)) {
                        registerFoundSecret(player, skyhold2);
                    } else if (e.getClickedBlock().getLocation().equals(topSecret)) {
                        registerFoundSecret(player, skyhold3);
                    } else if (e.getClickedBlock().getLocation().equals(inquisitorSecret)) {
                        registerFoundSecret(player, skyhold4);
                    } else if (e.getClickedBlock().getLocation().equals(atticSecret)) {
                        registerFoundSecret(player, skyhold5);
                    } else if (e.getClickedBlock().getLocation().equals(barnSecret)) {
                        registerFoundSecret(player, skyhold6);
                    } else if (e.getClickedBlock().getLocation().equals(statueSecret)) {
                        registerFoundSecret(player, skyhold7);
                    } else if (e.getClickedBlock().getLocation().equals(waterSecret)) {
                        registerFoundSecret(player, skyhold8);
                    }

                } else if (MapController.getCurrentMap().worldName.equalsIgnoreCase("Thunderstone")) {
                    Location islandSecret = new Location(Bukkit.getWorld("Thunderstone"), 112, 16, 75);
                    Location huntifiSecret = new Location(Bukkit.getWorld("Thunderstone"), 88, 80, 107);
                    Location skyviewSecret = new Location(Bukkit.getWorld("Thunderstone"), 168, 170, 72);
                    Location cookieSecret = new Location(Bukkit.getWorld("Thunderstone"), 233, 68, 78);
                    Location fallSecret = new Location(Bukkit.getWorld("Thunderstone"), 189, 130, 75);

                    if (e.getClickedBlock().getLocation().equals(islandSecret)) {
                        registerFoundSecret(player, thunderstone1);
                    } else if (e.getClickedBlock().getLocation().equals(huntifiSecret)) {
                        registerFoundSecret(player, thunderstone2);
                    } else if (e.getClickedBlock().getLocation().equals(skyviewSecret)) {
                        registerFoundSecret(player, thunderstone3);
                    } else if (e.getClickedBlock().getLocation().equals(cookieSecret)) {
                        registerFoundSecret(player, thunderstone4);
                    } else if (e.getClickedBlock().getLocation().equals(fallSecret)) {
                        registerFoundSecret(player, thunderstone5);
                    }

                } else if (MapController.getCurrentMap().worldName.equalsIgnoreCase("Lakeborough")) {
                    Location wellSecret = new Location(Bukkit.getWorld("Lakeborough"), -1598, 17, -305);
                    Location underwaterSecret = new Location(Bukkit.getWorld("Lakeborough"), -1616, 4, -270);
                    Location millSecret = new Location(Bukkit.getWorld("Lakeborough"), -1584, 25, -310);
                    //Location throneSecret = new Location(Bukkit.getWorld("Lakeborough"), -1589, 17, -413);
                    Location fireplaceSecret = new Location(Bukkit.getWorld("Lakeborough"), -1557, 22, -378);

                    if (e.getClickedBlock().getLocation().equals(wellSecret)) {
                        registerFoundSecret(player, lakeborough1);
                    } else if (e.getClickedBlock().getLocation().equals(millSecret)) {
                        registerFoundSecret(player, lakeborough2);
                    } else if (e.getClickedBlock().getLocation().equals(underwaterSecret)) {
                        registerFoundSecret(player, lakeborough4);
                    } else if (e.getClickedBlock().getLocation().equals(fireplaceSecret)) {
                        registerFoundSecret(player, lakeborough5);
                    }
                } else if (MapController.getCurrentMap().worldName.equalsIgnoreCase("Elwynn")) {
                    Location abbeySecret = new Location(Bukkit.getWorld("Elwynn"), 3, 102, -141);
                    Location koboldSecret = new Location(Bukkit.getWorld("Elwynn"), -86, 92, -300);
                    Location sewerSecret = new Location(Bukkit.getWorld("Elwynn"), -96, 72, -69);
                    Location murlocSecret = new Location(Bukkit.getWorld("Elwynn"), 35, 68, 264);
                    Location lakeSecret = new Location(Bukkit.getWorld("Elwynn"), 30, 82, 160);
                    Location innSecret = new Location(Bukkit.getWorld("Elwynn"), -191, 75, 140);
                    Location treeSecret = new Location(Bukkit.getWorld("Elwynn"), -94, 102, -79);
                    Location trainingSecret = new Location(Bukkit.getWorld("Elwynn"), 19, 104, -98);
                    Location bridgeSecret = new Location(Bukkit.getWorld("Elwynn"), 61, 74, -91);
                    Location riverSecret = new Location(Bukkit.getWorld("Elwynn"), 139, 68, -247);
                    Location hightreeSecret = new Location(Bukkit.getWorld("Elwynn"), -181, 97, 157);

                    if (e.getClickedBlock().getLocation().equals(abbeySecret)) {
                        registerFoundSecret(player, elwynn1);
                    } else if (e.getClickedBlock().getLocation().equals(koboldSecret)) {
                        registerFoundSecret(player, elwynn2);
                    } else if (e.getClickedBlock().getLocation().equals(sewerSecret)) {
                        registerFoundSecret(player, elwynn3);
                    } else if (e.getClickedBlock().getLocation().equals(murlocSecret)) {
                        registerFoundSecret(player, elwynn4);
                    } else if (e.getClickedBlock().getLocation().equals(lakeSecret)) {
                        registerFoundSecret(player, elwynn5);
                    } else if (e.getClickedBlock().getLocation().equals(innSecret)) {
                        registerFoundSecret(player, elwynn6);
                    } else if (e.getClickedBlock().getLocation().equals(treeSecret)) {
                        registerFoundSecret(player, elwynn7);
                    } else if (e.getClickedBlock().getLocation().equals(trainingSecret)) {
                        registerFoundSecret(player, elwynn8);
                    } else if (e.getClickedBlock().getLocation().equals(bridgeSecret)) {
                        registerFoundSecret(player, elwynn9);
                    } else if (e.getClickedBlock().getLocation().equals(riverSecret)) {
                        registerFoundSecret(player, elwynn10);
                    } else if (e.getClickedBlock().getLocation().equals(hightreeSecret)) {
                        registerFoundSecret(player, elwynn11);
                    }
                }
            }
        }
    }


    public void registerFoundSecret(Player player, String secretName) {
                UUID uuid = player.getUniqueId();

                PlayerData data = ActiveData.getData(uuid);

                if (!data.getFoundSecrets().contains(secretName)) {

                    Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + player.getName() + " has found secret: " + secretName);
                    player.sendMessage(ChatColor.GREEN + "You have found a secret! " + ChatColor.YELLOW + "(+200 coins)");
                    data.addCoins(200);
                    data.addFoundSecret(secretName);
                    data.addSecret();

                } else {
                    player.sendMessage(ChatColor.GREEN + "You have already found this secret!");
                }
        }

}
