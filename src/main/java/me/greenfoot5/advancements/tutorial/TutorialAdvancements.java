package me.greenfoot5.advancements.tutorial;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.fren_gor.ultimateAdvancementAPI.AdvancementTab;
import com.fren_gor.ultimateAdvancementAPI.advancement.RootAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import me.greenfoot5.advancements.CSAdvancementController;
import me.greenfoot5.advancements.api.advancements.StandardAdvancement;
import me.greenfoot5.advancements.api.nodes.AdvancementNode;
import me.greenfoot5.advancements.api.nodes.AdvancementNode.AdvancementNodeBuilder;
import me.greenfoot5.advancements.api.nodes.AdvancementAdvancementNode.AdvancementAdvancementNodeBuilder;
import me.greenfoot5.advancements.api.nodes.HiddenAdvancementNode.HiddenAdvancementNodeBuilder;
import me.greenfoot5.conwymc.data_types.Tuple;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URI;

/**
 * Manages the basic advancements for doing stuff for the first time
 */
public class TutorialAdvancements implements Listener {

    public static AdvancementTab tab;

    /**
     * Setup the events advancements tab
     */
    public TutorialAdvancements() {

        tab = CSAdvancementController.api.getAdvancementTab("siege_tutorial");
        if (tab != null) {
            return;
        }

        tab = CSAdvancementController.api.createAdvancementTab("siege_tutorial");

        AdvancementNode rootNode = new AdvancementNodeBuilder("siege_tutorial")
                .setMaterial(Material.IRON_SWORD)
                .setFrameType(AdvancementFrameType.TASK)
                .setTitle("<white>Castle Siege Basics")
                .setDescription("<dark_green>Learn the basics of Castle Siege and complete a few challenges for some exciting rewards...")
                .build();

        // Cannons
        generateCannonAdvancements(rootNode);
        generateCatapultAdvancements(rootNode);
        generateRamAdvancements(rootNode);

        Tuple<RootAdvancement, StandardAdvancement[]> advs = rootNode.asAdvancementList(tab, "textures/block/stone_bricks.png");

        tab.registerAdvancements(advs.getFirst(), advs.getSecond());
    }

    private static void generateCannonAdvancements(AdvancementNode root) {
        //cannonball
        ItemStack ball = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) ball.getItemMeta();
        PlayerProfile profile = Bukkit.getServer().createProfile("Cannonball");
        try {
            PlayerTextures textures = profile.getTextures();
            textures.setSkin(URI.create("https://textures.minecraft.net/texture/17d81286281b1c7b13420f70b0f43867aa38eee109b64c3a37b699ea05e8ec05").toURL());
            profile.setTextures(textures);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        skullMeta.setPlayerProfile(profile);
        ball.setItemMeta(skullMeta);

        root.addChild(new AdvancementNodeBuilder("cannon_fire", "siege_tutorial")
                .setIcon(ball)
                .setFrameType(AdvancementFrameType.TASK)
                .setTitle("Shooting Cannons")
                .setRequirements(new String[]{"<yellow>☄ Fire a cannon</yellow>"})
                .build());
        root.addChild(new AdvancementAdvancementNodeBuilder("cannon_kill", "cannon_fire")
                .addRequiredKey("cannon_fire")
                .setMaterial(Material.OAK_BOAT)
                .setFrameType(AdvancementFrameType.CHALLENGE)
                .setTitle("You sunk my battleship")
                .setRequirements(new String[]{"<yellow>\uD83D\uDDE1 Kill a player with a cannon</yellow>"})
                .build());
    }

    private static void generateCatapultAdvancements(AdvancementNode root) {
        root.addChild(new AdvancementNodeBuilder("catapult_fire", "siege_tutorial")
                .setMaterial(Material.COBBLESTONE)
                .setFrameType(AdvancementFrameType.TASK)
                .setTitle("Firing Catapults")
                .setRequirements(new String[]{"<yellow>☄ Flip the lever and fire a catapult</yellow>"})
                .build());
        root.addChild(new AdvancementNodeBuilder("catapult_aim", "catapult_fire")
                .setMaterial(Material.TARGET)
                .setFrameType(AdvancementFrameType.TASK)
                .setTitle("Aim Carefully")
                .setRequirements(new String[]{"<yellow>\uD83C\uDFAF Adjust pitch & yaw on a catapult before firing</yellow>"})
                .build());
        root.addChild(new AdvancementAdvancementNodeBuilder("catapult_kill", "catapult_aim")
                .addRequiredKey("catapult_fire")
                .setMaterial(Material.SKELETON_SKULL)
                .setFrameType(AdvancementFrameType.CHALLENGE)
                .setTitle("Master Catapulteer")
                .setRequirements(new String[]{"<yellow>\uD83D\uDDE1 Kill a player with a catapult</yellow>"})
                .build());
    }

    private static void generateRamAdvancements(AdvancementNode root) {
        root.addChild(new AdvancementNodeBuilder("gate_hit", "siege_tutorial")
                .setMaterial(Material.OAK_WOOD)
                .setFrameType(AdvancementFrameType.TASK)
                .setTitle("Ramming Gates")
                .setRequirements(new String[]{"<yellow>\uD83E\uDE93 Deal damage to a gate</yellow>"})
                .build());
        root.addChild(new AdvancementAdvancementNodeBuilder("gate_four", "gate_hit")
                .addRequiredKey("gate_hit")
                .setMaterial(Material.PLAYER_HEAD)
                .setFrameType(AdvancementFrameType.GOAL)
                .setTitle("Lift with your back")
                .setRequirements(new String[] {"<yellow>✣ Have at least 4 players on the same ram</yellow>"})
                .build());
        root.addChild(new AdvancementNodeBuilder("gate_break", "gate_hit")
                .setMaterial(Material.SPRUCE_LOG)
                .setFrameType(AdvancementFrameType.GOAL)
                .setTitle("I'm sorry for barging in")
                .setRequirements(new String[] {"<yellow>\uD83D\uDCA5 Work with others to bring down a gate</yellow>"})
                .build());
        root.addChild(new HiddenAdvancementNodeBuilder("gate_solo", "gate_break")
                .setMaterial(Material.CHISELED_STONE_BRICKS)
                .setFrameType(AdvancementFrameType.GOAL)
                .setTitle("I did knock...")
                .setRequirements(new String[] {"<yellow>\uD83E\uDE93 Deal at least <light_purple>90%</light_purple> of the damage to a gate</yellow>"})
                .build());
    }
}
