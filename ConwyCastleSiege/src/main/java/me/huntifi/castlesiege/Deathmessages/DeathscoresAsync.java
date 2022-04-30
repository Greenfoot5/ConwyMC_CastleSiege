package me.huntifi.castlesiege.Deathmessages;

import org.bukkit.entity.Player;

public class DeathscoresAsync {

    public static void doStats(Player whoHit, Player whoWasHit) {

        /*MVPstats.addKills(whoHit.getUniqueId(), 1.0);
        MVPstats.addAssists(whoHit.getUniqueId(), 1.0);
        MVPstats.addKillstreak(whoHit.getUniqueId(), 1);
        MVPstats.setKillstreak(whoWasHit.getUniqueId(), 0);

        if (StatsChanging.getKillstreak(whoWasHit.getUniqueId()) < MVPstats.getKillstreak(whoWasHit.getUniqueId())) {
            StatsChanging.setKillstreak(whoWasHit.getUniqueId(), MVPstats.getKillstreak(whoWasHit.getUniqueId()));
        }*/
    }

    public static int returnKillstreak(Player whoHit) {
        return 0;//MVPstats.getKillstreak(whoHit.getUniqueId());
    }

}