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

public class CSAdvancementController implements Listener {
    public static UltimateAdvancementAPI api;

    public CSAdvancementController() {
        api = UltimateAdvancementAPI.getInstance(ConwyMC.plugin);
        LevelAdvancements levelAdvancements = new LevelAdvancements();
        ConwyMC.plugin.getServer().getPluginManager().registerEvents(levelAdvancements, ConwyMC.plugin);
        Main.instance.getCommand("levelup").setExecutor(levelAdvancements);

        //new TutorialAdvancements();
        //new EventsAdvancements();
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
        //EventsAdvancements.tab.showTab(p);
        //EventsAdvancements.tab.grantRootAdvancement(p);
        // Grant the V4Tester advancement
//        Advancement V4TAdv = EventsAdvancements.tab.getAdvancement(new AdvancementKey("v4_beta", NodeDisplay.cleanKey("V4 Tester")));
//        if (!V4TAdv.isGranted(p)) {
//            V4TAdv.grant(p);
//            Cosmetic V4Tester = new Cosmetic(TITLE, "V4 Tester", "<color:#1ECDD5>⁎⁑<b><color:#00C3EE>V4</b>⁑⁎");
//            PlayerData data = ActiveData.getData(p.getUniqueId());
//            data.addCosmetic(V4Tester);
//        }
    }
}
