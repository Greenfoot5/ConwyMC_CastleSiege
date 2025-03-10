package me.greenfoot5.advancements.events;

import com.fren_gor.ultimateAdvancementAPI.AdvancementTab;
import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.RootAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.events.PlayerLoadingCompletedEvent;
import me.greenfoot5.advancements.CSAdvancementController;
import me.greenfoot5.advancements.api.advancements.StandardAdvancement;
import me.greenfoot5.advancements.api.nodes.AdvancementNode.AdvancementNodeBuilder;
import me.greenfoot5.advancements.api.nodes.AdvancementNode;
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
    public static AdvancementNode testerNode;

    /**
     * Setup the events advancements tab
     */
    public V4ReleaseAdvancements() {

        tab = CSAdvancementController.api.getAdvancementTab("v4_beta");
        if (tab != null) {
            return;
        }

        tab = CSAdvancementController.api.createAdvancementTab("b4_beta");

        AdvancementNode rootNode = new AdvancementNodeBuilder("v4_beta")
                .setMaterial(Material.GOAT_HORN)
                .setFrameType(AdvancementFrameType.TASK)
                .setTitle("<white>ConwyMC V4                ")
                .setDescription("<dark_green>Celebrate the release of ConwyMC V4!<br><br><blue><font:uniform><b>Available until 1 month after release.")
                .build();

        // Tester
        testerNode = new AdvancementNodeBuilder("tester", "v4_beta")
                .setMaterial(Material.WARPED_FUNGUS)
                .setFrameType(AdvancementFrameType.TASK)
                .setTitle("V4 Tester                    ")
                .setRequirements(new String[]{"<yellow>⎆ Join the server during the event</yellow>"})
                .setReward("<gold>\uD83C\uDFF7 <color:#1ECDD5>⁎⁑<b><color:#00C3EE>V4</b>⁑⁎ Title")
                .build();
        rootNode.addChild(testerNode);

        // Donator
        rootNode.addChild(new AdvancementNodeBuilder("donator", "v4_beta")
                .setMaterial(Material.CRIMSON_FUNGUS)
                .setFrameType(AdvancementFrameType.TASK)
                .setTitle("V4 Donator                   ")
                .setRequirements(new String[]{"<yellow>\uD83D\uDCB0 Donate to the server during the event</yellow>"})
                .setReward("<gold>\uD83C\uDFF7 <color:#FF9999>◦<b><color:#FFC8C8>V4</b>◦ Title" +
                        "<br><br><dark_gray><font:uniform><b>Currently granted manually")
                .build());

        Tuple<RootAdvancement, StandardAdvancement[]> advs = rootNode.asAdvancementList(tab, "textures/block/stripped_warped_stem_top.png");

        tab.registerAdvancements(advs.getFirst(), advs.getSecond());
    }

    /**
     * Grants the V4 Tester advancement
     * @param e Called when a player has finished loading into the server
     */
    @EventHandler
    public void onJoin(PlayerLoadingCompletedEvent e) {
        // Grant the V4Tester advancement
        Advancement testerAdvancement = tab.getAdvancement(testerNode.getKey(tab));
        if (!testerAdvancement.isGranted(e.getPlayer())) {
            testerAdvancement.grant(e.getPlayer());
            Cosmetic V4Tester = new Cosmetic(TITLE, "V4 Tester", "<color:#1ECDD5>⁎⁑<b><color:#00C3EE>V4</b>⁑⁎");
            PlayerData data = ActiveData.getData(e.getPlayer().getUniqueId());
            data.addCosmetic(V4Tester);
        }
    }
}
