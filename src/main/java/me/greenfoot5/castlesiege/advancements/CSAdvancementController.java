package me.greenfoot5.castlesiege.advancements;

import com.fren_gor.ultimateAdvancementAPI.UltimateAdvancementAPI;
import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.events.PlayerLoadingCompletedEvent;
import com.fren_gor.ultimateAdvancementAPI.util.AdvancementKey;
import me.greenfoot5.castlesiege.Main;
import me.greenfoot5.castlesiege.advancements.displays.NodeDisplay;
import me.greenfoot5.castlesiege.advancements.levels.LevelAdvancements;
import me.greenfoot5.conwymc.ConwyMC;
import me.greenfoot5.conwymc.data_types.Cosmetic;
import me.greenfoot5.conwymc.data_types.PlayerData;
import me.greenfoot5.conwymc.database.ActiveData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static me.greenfoot5.conwymc.data_types.Cosmetic.CosmeticType.TITLE;

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
        Main.instance.getCommand("levelup").setExecutor(levelAdvancements);
        V4ReleaseAdvancements v4ReleaseAdvancements = new V4ReleaseAdvancements();
        ConwyMC.plugin.getServer().getPluginManager().registerEvents(levelAdvancements, ConwyMC.plugin);

        //new TutorialAdvancements();
    }

    @EventHandler
    public void onJoin(PlayerLoadingCompletedEvent e) {
        // Called after a player has successfully been loaded by the API
        Player p = e.getPlayer();
        // Here you can show tabs to players
        LevelAdvancements.advancementTab.showTab(p);
        LevelAdvancements.advancementTab.grantRootAdvancement(p);
        // Here you can show tabs to players
        //TutorialAdvancements.tab.showTab(p);
        //TutorialAdvancements.tab.grantRootAdvancement(p);

        // Events
        V4ReleaseAdvancements.tab.showTab(p);
        V4ReleaseAdvancements.tab.grantRootAdvancement(p);
    }
}
