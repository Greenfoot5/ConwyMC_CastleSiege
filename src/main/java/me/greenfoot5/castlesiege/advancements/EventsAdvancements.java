package me.greenfoot5.castlesiege.advancements;

import com.fren_gor.ultimateAdvancementAPI.AdvancementTab;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.RootAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import me.greenfoot5.castlesiege.advancements.displays.NodeDisplayTypes;
import me.greenfoot5.conwymc.data_types.Tuple;
import org.bukkit.Material;

public class EventsAdvancements {

    public static AdvancementTab tab;
    private static BaseAdvancement[] advancements;

    public EventsAdvancements() {

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
                "V4 Release",
                "Donate to the server in the first month after V4's release<br><br><gold><b>Reward: </gold><br><green>V4 Release Title<br>",
                "ConwyMC V4"));

        Tuple<RootAdvancement, BaseAdvancement[]> advs = rootNode.asAdvancementList(tab, "textures/block/stripped_warped_stem_top.png");
        advancements = advs.getSecond();

        tab.registerAdvancements(advs.getFirst(), advancements);
    }
}
