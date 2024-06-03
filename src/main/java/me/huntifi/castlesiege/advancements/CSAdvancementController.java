package me.huntifi.castlesiege.advancements;

import com.fren_gor.ultimateAdvancementAPI.UltimateAdvancementAPI;
import com.fren_gor.ultimateAdvancementAPI.events.PlayerLoadingCompletedEvent;
import me.huntifi.conwymc.ConwyMC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class CSAdvancementController implements Listener {
    public static UltimateAdvancementAPI api;

    public CSAdvancementController() {
        api = UltimateAdvancementAPI.getInstance(ConwyMC.plugin);
        ConwyMC.plugin.getServer().getPluginManager().registerEvents(new LevelAdvancements(), ConwyMC.plugin);

        TutorialAdvancements tutorial = new TutorialAdvancements();
    }

    @EventHandler
    public void onJoin(PlayerLoadingCompletedEvent e) {
        // Called after a player has successfully been loaded by the API
        Player p = e.getPlayer();
        // Here you can show tabs to players
        LevelAdvancements.advancementTab.showTab(p);
        LevelAdvancements.advancementTab.grantRootAdvancement(p);
        // Here you can show tabs to players
        TutorialAdvancements.tab.showTab(p);
        TutorialAdvancements.tab.grantRootAdvancement(p);
    }
}
