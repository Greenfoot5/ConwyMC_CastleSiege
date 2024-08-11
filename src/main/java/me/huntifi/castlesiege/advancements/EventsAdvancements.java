package me.huntifi.castlesiege.advancements;

import com.fren_gor.ultimateAdvancementAPI.AdvancementTab;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.RootAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import me.huntifi.castlesiege.advancements.displays.NodeDisplayTypes;
import me.huntifi.conwymc.data_types.Tuple;
import org.bukkit.Material;

public class EventsAdvancements {

    public static AdvancementTab tab;
    private static BaseAdvancement[] advancements;

    public EventsAdvancements() {

        tab = CSAdvancementController.api.createAdvancementTab("v4_beta");

        AdvancementNode rootNode = new AdvancementNode(Material.GOAT_HORN, AdvancementFrameType.TASK,
                "<white>V4 Beta", "<dark_green>Join the game during the event period",
                NodeDisplayTypes.ParentGranted);

        // Cannons
        rootNode.addChild(new AdvancementNode(Material.WARPED_BUTTON, AdvancementFrameType.TASK,
                "V4 Tester",
                "Join the game before V4's release<br><br><gold><b>Reward: </gold><br><green>V4 Tester Title<br>" +
                        "<gold><b>Preview: </gold><blue>⁎⁑<b>V4</b>⁑⁎",
                "V4 Beta"));

        Tuple<RootAdvancement, BaseAdvancement[]> advs = rootNode.asAdvancementList(tab, "textures/block/warped_stem.png");
        advancements = advs.getSecond();

        tab.registerAdvancements(advs.getFirst(), advancements);
    }
}
