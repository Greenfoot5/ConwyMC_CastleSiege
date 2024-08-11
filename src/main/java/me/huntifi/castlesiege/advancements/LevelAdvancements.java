package me.huntifi.castlesiege.advancements;

import com.fren_gor.ultimateAdvancementAPI.AdvancementTab;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.RootAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import me.huntifi.castlesiege.advancements.displays.NodeDisplayTypes;
import me.huntifi.castlesiege.events.LevelUpEvent;
import me.huntifi.conwymc.data_types.Tuple;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class LevelAdvancements implements Listener {

    public static final int BOUNTY_LEVEL = 7;

    public static AdvancementTab advancementTab;
    private static BaseAdvancement[] advancements;
    private static final int[] milestones = new int[] {
            2, 5, 7, 10, 15, 20, 25, 40, 80
    };

    public LevelAdvancements() {

        advancementTab = CSAdvancementController.api.createAdvancementTab("siege_levels");

        AdvancementNode rootNode = new AdvancementNode(Material.EXPERIENCE_BOTTLE, AdvancementFrameType.TASK,
                "<white>Level Milestones",
                "<dark_green>Advance through the Castle Siege levels gaining cool rewards as you progress!",
                NodeDisplayTypes.Shown);

        // Cannons
        rootNode.addChild(new AdvancementNode(Material.WOODEN_SWORD, AdvancementFrameType.GOAL,
                "Just Beginning", "Reach Level 2<br><br><gold><b>Reward: </gold><br><green>Shieldman Kit", "Level Milestones"));
        rootNode.addChild(new AdvancementNode(Material.STONE_SWORD, AdvancementFrameType.GOAL,
                "Warming Up", "Reach Level 5<br><br><gold><b>Reward: </gold><br><green>Spearman Kit", "Just Beginning"));
        rootNode.addChild(new AdvancementNode(Material.STONE_SWORD, AdvancementFrameType.GOAL,
                "Building Muscle        ",
                "Reach Level 7<br><br><gold><b>Reward: </gold><br><yellow>Bounties" +
                        "<br><br><i><blue>You can set bounties through the <br><yellow>/bounty <player> <amount></yellow><br>command.",
                "Warming Up"));
        rootNode.addChild(new AdvancementNode(Material.IRON_SWORD, AdvancementFrameType.GOAL,
                "Mighty Hunter", "Reach Level 10<br><br><gold><b>Reward: </gold><br><green>Spear Knight Kit", "Building Muscle"));
        rootNode.addChild(new AdvancementNode(Material.IRON_SWORD, AdvancementFrameType.GOAL,
                "Just Getting Started", "Reach Level 15<br><br><gold><b>Reward: </gold><br><green>Battle Medic Kit", "Mighty Hunter"));
        rootNode.addChild(new AdvancementNode(Material.GOLDEN_SWORD, AdvancementFrameType.GOAL,
                "Shield Carrier", "Reach Level 20<br><br><gold><b>Reward: </gold><br><green>Hypaspist Kit", "Just Getting Started"));
        rootNode.addChild(new AdvancementNode(Material.GOLDEN_SWORD, AdvancementFrameType.GOAL,
                "Moving Higher", "Reach Level 25", "Shield Carrier"));
        rootNode.addChild(new AdvancementNode(Material.DIAMOND_SWORD, AdvancementFrameType.GOAL,
                "Are we nearly there yet?", "Reach Level 40", "Moving Higher"));
        rootNode.addChild(new AdvancementNode(Material.NETHERITE_SWORD, AdvancementFrameType.GOAL,
                "Not quite Sisyphus", "Reach Level 80", "Are we nearly there yet?"));

        Tuple<RootAdvancement, BaseAdvancement[]> advs = rootNode.asAdvancementList(advancementTab, "textures/block/green_concrete.png");
        advancements = advs.getSecond();

        advancementTab.registerAdvancements(advs.getFirst(), advancements);
    }

    @EventHandler
    public void onLevelUp(LevelUpEvent event) {
        Player p = event.getPlayer();
        for (int i = 0; i < milestones.length; i++) {
            if (milestones[i] > event.getNewLevel()) return;
            advancements[i].grant(p);
        }
    }
}
