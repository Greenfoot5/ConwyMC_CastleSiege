package me.greenfoot5.castlesiege.advancements;

import com.fren_gor.ultimateAdvancementAPI.AdvancementTab;
import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.RootAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.events.PlayerLoadingCompletedEvent;
import com.fren_gor.ultimateAdvancementAPI.util.AdvancementKey;
import me.greenfoot5.castlesiege.advancements.displays.NodeDisplay;
import me.greenfoot5.castlesiege.advancements.displays.NodeDisplayTypes;
import me.greenfoot5.conwymc.data_types.Cosmetic;
import me.greenfoot5.conwymc.data_types.PlayerData;
import me.greenfoot5.conwymc.data_types.Tuple;
import me.greenfoot5.conwymc.database.ActiveData;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static me.greenfoot5.conwymc.data_types.Cosmetic.CosmeticType.TITLE;

/**
 * Manages advancements for events
 */
public class V4ReleaseAdvancements implements Listener {

    public static AdvancementTab tab;

    /**
     * Setup the events advancements tab
     */
    public V4ReleaseAdvancements() {

        tab = CSAdvancementController.api.createAdvancementTab("v4_beta");

        AdvancementNode rootNode = new AdvancementNode(Material.GOAT_HORN, AdvancementFrameType.TASK,
                "<white>ConwyMC V4", "<dark_green>Celebrate the release of ConwyMC V4!",
                NodeDisplayTypes.ParentGranted);

        // Cannons
        rootNode.addChild(new AdvancementNode(Material.WARPED_FUNGUS, AdvancementFrameType.TASK,
                "V4 Tester",
                "Join the game before V4's release<br><br><gold><b>Reward: </gold><br><green>V4 Tester Title<br>",
                "ConwyMC V4"));
        rootNode.addChild(new AdvancementNode(Material.CRIMSON_FUNGUS, AdvancementFrameType.TASK,
                "V4 Donator",
                "Donate to the server before 30 days after V4's release<br><br><gold><b>Reward: </gold><br><green>V4 Release Title<br>",
                "ConwyMC V4"));

        Tuple<RootAdvancement, BaseAdvancement[]> advs = rootNode.asAdvancementList(tab, "textures/block/stripped_warped_stem_top.png");

        tab.registerAdvancements(advs.getFirst(), advs.getSecond());
    }

    /**
     * Grants the V4 Tester advancement
     * @param e Called when a player has finished loading into the server
     */
    @EventHandler
    public void onJoin(PlayerLoadingCompletedEvent e) {
        // Grant the V4Tester advancement
        Advancement V4TAdv = tab.getAdvancement(new AdvancementKey("v4_beta", NodeDisplay.cleanKey("V4 Tester")));
        if (!V4TAdv.isGranted(e.getPlayer())) {
            V4TAdv.grant(e.getPlayer());
            Cosmetic V4Tester = new Cosmetic(TITLE, "V4 Tester", "<color:#1ECDD5>⁎⁑<b><color:#00C3EE>V4</b>⁑⁎");
            PlayerData data = ActiveData.getData(e.getPlayer().getUniqueId());
            data.addCosmetic(V4Tester);
        }
    }
}
