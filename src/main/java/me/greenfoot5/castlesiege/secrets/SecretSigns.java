package me.greenfoot5.castlesiege.secrets;

import me.greenfoot5.castlesiege.data_types.CSPlayerData;
import me.greenfoot5.castlesiege.database.CSActiveData;
import me.greenfoot5.castlesiege.maps.MapController;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.UUID;

public class SecretSigns implements Listener {

    final String helmsDeep1 = "HD_HillSecret";
    final String helmsDeep2 = "HD_Herugrim";
    final String skyhold1 = "Skyhold_Vault";
    final String skyhold2 = "Skyhold_Ravens";
    final String skyhold3 = "Skyhold_Top";
    final String skyhold4 = "Skyhold_Inquisitor";
    final String skyhold5 = "Skyhold_Attic";
    final String skyhold6 = "Skyhold_Barn";
    final String skyhold7 = "Skyhold_Statue";
    final String skyhold8 = "Skyhold_Water";
    final String skyhold9 = "Skyhold_Tomb";
    final String skyhold10 = "Skyhold_Shield";
    final String thunderstone1 = "Thunderstone_Island";
    final String thunderstone2 = "Thunderstone_Huntifi";
    final String thunderstone3 = "Thunderstone_Skyview";
    final String thunderstone4 = "Thunderstone_Cookie";
    final String thunderstone5 = "Thunderstone_Fall";
    final String lakeborough1 = "Lakeborough_Well";
    final String lakeborough2 = "Lakeborough_Mill";
    final String lakeborough4 = "Lakeborough_Underwater";
    final String lakeborough5 = "Lakeborough_Fireplace";
    final String elwynn1 = "Elwynn_Abbey";
    final String elwynn2 = "Elwynn_Kobold";
    final String elwynn3 = "Elwynn_Sewers";
    final String elwynn4 = "Elwynn_Murloc";
    final String elwynn5 = "Elwynn_Lake";
    final String elwynn6 = "Elwynn_Inn";
    final String elwynn7 = "Elwynn_Tree";
    final String elwynn8 = "Elwynn_Training";
    final String elwynn9 = "Elwynn_Bridge";
    final String elwynn10 = "Elwynn_River";
    final String elwynn11 = "Elwynn_Hightree";
    final String abrakhan1 = "Abrakhan_Vault";
    final String abrakhan2 = "Abrakhan_Prison";
    final String abrakhan3 = "Abrakhan_Tunnel";
    final String abrakhan4 = "Abrakhan_Sewer";
    final String DigSite1 = "HallOfHerakles_Waterfall";
    final String DigSite2 = "HallOfHerakles_Pillar";
    final String DigSite3 = "HallOfHerakles_Sewers";
    final String DigSite4 = "HallOfHerakles_Roots";
    final String DigSite5 = "HallOfHerakles_TreeTop";
    final String DigSite6 = "HallOfHerakles_EagleHall";


    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {

        Player player = e.getPlayer();

        if (e.getClickedBlock() == null || player.getGameMode() == GameMode.SPECTATOR) { return; }

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {

        if (e.getClickedBlock() != null && e.getClickedBlock().getState() instanceof Sign) {

                if (MapController.getCurrentMap().worldName.equalsIgnoreCase("HelmsDeep")) {

                    Location hillSecret = new Location(Bukkit.getWorld("HelmsDeep"), 900, 62, 1116);
                    Location herugrimSecret = new Location(Bukkit.getWorld("HelmsDeep"), 980, 58, 986);

                    if (e.getClickedBlock().getLocation().equals(hillSecret)) {
                       registerFoundSecret(player, helmsDeep1, 100);
                    } else if (e.getClickedBlock().getLocation().equals(herugrimSecret)) {
                        registerFoundSecret(player, helmsDeep2, 200);
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
                    Location tombSecret = new Location(Bukkit.getWorld("Skyhold"), 1655, 81, -31);
                    Location shieldSecret = new Location(Bukkit.getWorld("Skyhold"), 1620, 47, -51);

                    if (e.getClickedBlock().getLocation().equals(vaultSecret)) {
                        registerFoundSecret(player, skyhold1, 300);
                    } else if (e.getClickedBlock().getLocation().equals(ravensSecret)) {
                        registerFoundSecret(player, skyhold2, 100);
                    } else if (e.getClickedBlock().getLocation().equals(topSecret)) {
                        registerFoundSecret(player, skyhold3, 150);
                    } else if (e.getClickedBlock().getLocation().equals(inquisitorSecret)) {
                        registerFoundSecret(player, skyhold4, 150);
                    } else if (e.getClickedBlock().getLocation().equals(atticSecret)) {
                        registerFoundSecret(player, skyhold5, 100);
                    } else if (e.getClickedBlock().getLocation().equals(barnSecret)) {
                        registerFoundSecret(player, skyhold6, 250);
                    } else if (e.getClickedBlock().getLocation().equals(statueSecret)) {
                        registerFoundSecret(player, skyhold7, 250);
                    } else if (e.getClickedBlock().getLocation().equals(waterSecret)) {
                        registerFoundSecret(player, skyhold8, 250);
                    } else if (e.getClickedBlock().getLocation().equals(tombSecret)) {
                        registerFoundSecret(player, skyhold9, 200);
                    } else if (e.getClickedBlock().getLocation().equals(shieldSecret)) {
                        registerFoundSecret(player, skyhold10, 500);
                    }

                } else if (MapController.getCurrentMap().worldName.equalsIgnoreCase("Thunderstone")) {
                    Location islandSecret = new Location(Bukkit.getWorld("Thunderstone"), 112, 16, 75);
                    Location huntifiSecret = new Location(Bukkit.getWorld("Thunderstone"), 88, 80, 107);
                    Location skyviewSecret = new Location(Bukkit.getWorld("Thunderstone"), 168, 170, 72);
                    Location cookieSecret = new Location(Bukkit.getWorld("Thunderstone"), 233, 68, 78);
                    Location fallSecret = new Location(Bukkit.getWorld("Thunderstone"), 189, 130, 75);

                    if (e.getClickedBlock().getLocation().equals(islandSecret)) {
                        registerFoundSecret(player, thunderstone1, 200);
                    } else if (e.getClickedBlock().getLocation().equals(huntifiSecret)) {
                        registerFoundSecret(player, thunderstone2, 75);
                    } else if (e.getClickedBlock().getLocation().equals(skyviewSecret)) {
                        registerFoundSecret(player, thunderstone3, 150);
                    } else if (e.getClickedBlock().getLocation().equals(cookieSecret)) {
                        registerFoundSecret(player, thunderstone4, 100);
                    } else if (e.getClickedBlock().getLocation().equals(fallSecret)) {
                        registerFoundSecret(player, thunderstone5, 100);
                    }

                } else if (MapController.getCurrentMap().worldName.equalsIgnoreCase("Lakeborough")) {
                    Location wellSecret = new Location(Bukkit.getWorld("Lakeborough"), -1598, 17, -305);
                    Location underwaterSecret = new Location(Bukkit.getWorld("Lakeborough"), -1616, 4, -270);
                    Location millSecret = new Location(Bukkit.getWorld("Lakeborough"), -1584, 25, -310);
                    //Location throneSecret = new Location(Bukkit.getWorld("Lakeborough"), -1589, 17, -413);
                    Location fireplaceSecret = new Location(Bukkit.getWorld("Lakeborough"), -1557, 22, -378);

                    if (e.getClickedBlock().getLocation().equals(wellSecret)) {
                        registerFoundSecret(player, lakeborough1, 75);
                    } else if (e.getClickedBlock().getLocation().equals(millSecret)) {
                        registerFoundSecret(player, lakeborough2, 50);
                    } else if (e.getClickedBlock().getLocation().equals(underwaterSecret)) {
                        registerFoundSecret(player, lakeborough4, 150);
                    } else if (e.getClickedBlock().getLocation().equals(fireplaceSecret)) {
                        registerFoundSecret(player, lakeborough5, 100);
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
                        registerFoundSecret(player, elwynn1, 100);
                    } else if (e.getClickedBlock().getLocation().equals(koboldSecret)) {
                        registerFoundSecret(player, elwynn2, 200);
                    } else if (e.getClickedBlock().getLocation().equals(sewerSecret)) {
                        registerFoundSecret(player, elwynn3, 250);
                    } else if (e.getClickedBlock().getLocation().equals(murlocSecret)) {
                        registerFoundSecret(player, elwynn4, 200);
                    } else if (e.getClickedBlock().getLocation().equals(lakeSecret)) {
                        registerFoundSecret(player, elwynn5, 200);
                    } else if (e.getClickedBlock().getLocation().equals(innSecret)) {
                        registerFoundSecret(player, elwynn6, 50);
                    } else if (e.getClickedBlock().getLocation().equals(treeSecret)) {
                        registerFoundSecret(player, elwynn7, 100);
                    } else if (e.getClickedBlock().getLocation().equals(trainingSecret)) {
                        registerFoundSecret(player, elwynn8, 75);
                    } else if (e.getClickedBlock().getLocation().equals(bridgeSecret)) {
                        registerFoundSecret(player, elwynn9, 50);
                    } else if (e.getClickedBlock().getLocation().equals(riverSecret)) {
                        registerFoundSecret(player, elwynn10, 100);
                    } else if (e.getClickedBlock().getLocation().equals(hightreeSecret)) {
                        registerFoundSecret(player, elwynn11, 75);
                    }
                } else if (MapController.getCurrentMap().worldName.equalsIgnoreCase("Abrakhan")) {
                    Location prisonSecret = new Location(Bukkit.getWorld("Abrakhan"), 108, 38, -113);
                    Location vaultSecret = new Location(Bukkit.getWorld("Abrakhan"), 78, 30, -103);
                    Location tunnelSecret = new Location(Bukkit.getWorld("Abrakhan"), 56, 11, -93);
                    Location sewerSecret = new Location(Bukkit.getWorld("Abrakhan"), 142, 5, -47);
                    if (e.getClickedBlock().getLocation().equals(prisonSecret)) {
                        registerFoundSecret(player, abrakhan2, 175);
                    } else if (e.getClickedBlock().getLocation().equals(vaultSecret)) {
                        registerFoundSecret(player, abrakhan1, 150);
                    } else if (e.getClickedBlock().getLocation().equals(tunnelSecret)) {
                        registerFoundSecret(player, abrakhan3, 100);
                    } else if (e.getClickedBlock().getLocation().equals(sewerSecret)) {
                        registerFoundSecret(player, abrakhan4, 100);
                    }
                } else if (MapController.getCurrentMap().worldName.equalsIgnoreCase("HallOfHerakles")) {
                    Location waterfallSecret = new Location(Bukkit.getWorld("HallOfHerakles"), -363, 61, -355);
                    Location pillarSecret = new Location(Bukkit.getWorld("HallOfHerakles"), -362, 74, -301);
                    Location sewersSecret = new Location(Bukkit.getWorld("HallOfHerakles"), -261, 40, -437);
                    Location rootsSecret = new Location(Bukkit.getWorld("HallOfHerakles"), -232, 38, -499);
                    Location treeTopSecret = new Location(Bukkit.getWorld("HallOfHerakles"), -232, 87, -497);
                    Location eagleHallSecret = new Location(Bukkit.getWorld("HallOfHerakles"), -532, 107, -319);
                    if (e.getClickedBlock().getLocation().equals(waterfallSecret)) {
                        registerFoundSecret(player, DigSite1, 200);
                    } else if (e.getClickedBlock().getLocation().equals(pillarSecret)) {
                        registerFoundSecret(player, DigSite2, 200);
                    } else if (e.getClickedBlock().getLocation().equals(sewersSecret)) {
                        registerFoundSecret(player, DigSite3, 200);
                    } else if (e.getClickedBlock().getLocation().equals(rootsSecret)) {
                        registerFoundSecret(player, DigSite4, 200);
                    } else if (e.getClickedBlock().getLocation().equals(treeTopSecret)) {
                        registerFoundSecret(player, DigSite5, 200);
                    } else if (e.getClickedBlock().getLocation().equals(eagleHallSecret)) {
                        registerFoundSecret(player, DigSite6, 200);
                    }
                }
            }
        }
    }


    public void registerFoundSecret(Player player, String secretName, int coins) {
                UUID uuid = player.getUniqueId();

                CSPlayerData data = CSActiveData.getData(uuid);

                if (!data.hasSecret(secretName)) {

                    Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + player.getName() + " has found secret: " + secretName);
                    player.sendMessage(ChatColor.GREEN + "You have found a secret! " + ChatColor.YELLOW + "(+" + coins + " coins)");
                    data.addCoinsClean(coins);
                    data.addFoundSecret(secretName);
                    data.addSecret();

                } else {
                    player.sendMessage(ChatColor.GREEN + "You have already found this secret!");
                }
        }

}
