package me.greenfoot5.advancements;

import com.fren_gor.ultimateAdvancementAPI.UltimateAdvancementAPI;
import com.fren_gor.ultimateAdvancementAPI.events.PlayerLoadingCompletedEvent;
import me.greenfoot5.advancements.events.V4ReleaseAdvancements;
import me.greenfoot5.advancements.levels.LevelAdvancements;
import me.greenfoot5.advancements.tutorial.TutorialAdvancements;
import me.greenfoot5.conwymc.ConwyMC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * Controls and manages global advancements
 */
public class CSAdvancementController implements Listener {
    public static UltimateAdvancementAPI api;

    /**
     * Set up the controller
     */
    public CSAdvancementController() {
        api = UltimateAdvancementAPI.getInstance(ConwyMC.plugin);
        LevelAdvancements levelAdvancements = new LevelAdvancements();
        ConwyMC.plugin.getServer().getPluginManager().registerEvents(levelAdvancements, ConwyMC.plugin);
        V4ReleaseAdvancements v4ReleaseAdvancements = new V4ReleaseAdvancements();
        ConwyMC.plugin.getServer().getPluginManager().registerEvents(v4ReleaseAdvancements, ConwyMC.plugin);
        TutorialAdvancements tutorialAdvancements = new TutorialAdvancements();
        ConwyMC.plugin.getServer().getPluginManager().registerEvents(tutorialAdvancements, ConwyMC.plugin);

        //new TutorialAdvancements();
    }

    /**
     * Handles granting advancement tabs to user
     * @param e Player has joined and the UAAPI has loaded for them
     */
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

        // Event - V4 Release
        V4ReleaseAdvancements.tab.showTab(p);
        V4ReleaseAdvancements.tab.grantRootAdvancement(p);
    }
}
