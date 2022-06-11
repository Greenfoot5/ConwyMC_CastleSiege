package me.huntifi.castlesiege.maps;

import me.huntifi.castlesiege.events.combat.InCombat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MapBorder extends Map implements Runnable {


    @Override
    public void run() {

        for (Player soldier : Bukkit.getOnlinePlayers()) {
            if (MapController.currentMapIs("Helm's Deep")) {
                if (InCombat.isPlayerInLobby(soldier.getUniqueId())) {
                    return;
                }
                northZ = 925.0;
                southZ = 1235.0;
                westX = 774.0;
                eastX = 1211.0;

                double x = soldier.getLocation().getX();
                double z = soldier.getLocation().getZ();

                if (northZ == 0.0 || southZ == 0.0 || westX == 0.0 || eastX == 0.0) {
                    return;
                }

                //for example if z = 605 and I go lower than that i get a warning
                if (northZ > z) {
                    soldier.sendTitle(ChatColor.DARK_RED + "Deserters will be beheaded!",
                            ChatColor.RED + "Turn back now!", 20, 40, 10);

                    if ((northZ-30) > z) {
                        soldier.setHealth(0);
                    }

                }

                //for example if z = 605 and I go higher than that i get a warning
                if (southZ < z) {
                    soldier.sendTitle(ChatColor.DARK_RED + "Deserters will be beheaded!",
                            ChatColor.RED + "Turn back now!", 20, 40, 10);

                    if ((southZ+ 30) < z) {
                        soldier.setHealth(0);
                    }

                }

                //for example if x = 605 and I go lower than that i get a warning
                if (westX > x) {
                    soldier.sendTitle(ChatColor.DARK_RED + "Deserters will be beheaded!",
                            ChatColor.RED + "Turn back now!", 20, 40, 10);

                    if ((westX- 30) > x) {
                        soldier.setHealth(0);
                    }

                }

                //for example if x = 605 and I go higher than that i get a warning
                if (eastX < x) {
                    soldier.sendTitle(ChatColor.DARK_RED + "Deserters will be beheaded!",
                            ChatColor.RED + "Turn back now!", 20, 40, 10);

                    if ((eastX+ 30) < x) {
                        soldier.setHealth(0);
                    }

                }
            }
        }
    }
}
