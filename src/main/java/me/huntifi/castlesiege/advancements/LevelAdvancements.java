package me.huntifi.castlesiege.advancements;

import com.fren_gor.ultimateAdvancementAPI.AdvancementTab;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.RootAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import me.huntifi.castlesiege.events.LevelUpEvent;
import me.huntifi.conwymc.advancements.AdventureAdvancementDisplay;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class LevelAdvancements implements Listener {

    public static AdvancementTab advancementTab;
    private static BaseAdvancement[] advancements;
    private static final int[] milestones = new int[] {
            2, 5, 10, 15, 20, 25, 40, 80
    };

    public LevelAdvancements() {

        advancementTab = CSAdvancementController.api.createAdvancementTab("siege_levels");

        AdvancementDisplay rootDisplay = new AdventureAdvancementDisplay(Material.STICK,
                AdvancementFrameType.TASK, false, false, 0, 0,
                Component.text("Level Milestones").color(NamedTextColor.WHITE),
                Component.text("Advance through the Castle Siege levels gaining cool rewards as you progress!").color(NamedTextColor.DARK_GREEN));
        AdvancementDisplay[] displays = new AdvancementDisplay[]{
                new AdventureAdvancementDisplay(Material.WOODEN_SWORD,
                        AdvancementFrameType.GOAL, true, false, 1, 0,
                        Component.text("Just beginning"),
                        "Reach Level 2<br><br><gold><b>Reward: </gold><br><green>Shieldman Kit"),
                new AdventureAdvancementDisplay(Material.STONE_SWORD,
                        AdvancementFrameType.GOAL, true, false, 2, 0,
                        Component.text("Warming up"),
                        "Reach Level 5<br><br><gold><b>Reward: </gold><br><green>Spearman Kit"),
                new AdventureAdvancementDisplay(Material.IRON_SWORD,
                        AdvancementFrameType.GOAL, true, false, 3, 0,
                        Component.text("Building muscle"),
                        "Reach Level 10<br><br><gold><b>Reward: </gold><br><green>Spear Knight Kit"),
                new AdventureAdvancementDisplay(Material.IRON_SWORD,
                        AdvancementFrameType.GOAL, true, false, 4, 0,
                        Component.text("Just getting started"),
                        "Reach Level 15<br><br><gold><b>Reward: </gold><br><green>Battle Medic Kit"),
                new AdventureAdvancementDisplay(Material.GOLDEN_SWORD,
                        AdvancementFrameType.GOAL, true, false, 5, 0,
                        Component.text("Shield carrier"),
                        "Reach Level 20<br><br><gold><b>Reward: </gold><br><green>Hypaspist Kit"),
                new AdventureAdvancementDisplay(Material.GOLDEN_SWORD,
                        AdvancementFrameType.TASK, true, false, 6, 0,
                        Component.text("Just getting started"),
                        "Reach Level 25"),
                new AdventureAdvancementDisplay(Material.DIAMOND_SWORD,
                        AdvancementFrameType.TASK, true, false, 7, 0,
                        Component.text("Are we nearly there yet?"),
                        "Reach Level 40"),
                new AdventureAdvancementDisplay(Material.NETHERITE_SWORD,
                        AdvancementFrameType.TASK, true, false, 8, 0,
                        Component.text("Not quite Sisyphus"),
                        "Reach Level 80")
        };


        RootAdvancement root = new RootAdvancement(advancementTab, "root", rootDisplay, "textures/block/green_concrete.png");
        advancements = new BaseAdvancement[displays.length];
        advancements[0] = new BaseAdvancement("0", displays[0], root);
        for (int i = 1; i < displays.length; i++) {
            advancements[i] = new BaseAdvancement("" + i, displays[i], advancements[i - 1]);
        }
        advancementTab.registerAdvancements(root, advancements);
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
