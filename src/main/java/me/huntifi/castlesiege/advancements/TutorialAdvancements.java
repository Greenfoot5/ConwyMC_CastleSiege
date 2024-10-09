package me.huntifi.castlesiege.advancements;

import com.fren_gor.ultimateAdvancementAPI.AdvancementTab;
import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.RootAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.util.AdvancementKey;
import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.advancements.displays.NodeDisplay;
import me.huntifi.castlesiege.advancements.displays.NodeDisplayTypes;
import me.huntifi.castlesiege.events.map.CannonEvent;
import me.huntifi.castlesiege.events.map.RamEvent;
import me.huntifi.conwymc.data_types.Tuple;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class TutorialAdvancements{

    public static AdvancementTab tab;
    private static BaseAdvancement[] advancements;

    // Event Tracking
    private HashMap<String, Set<UUID>> ramAttackers = new HashMap<>();

    public TutorialAdvancements() {

        tab = CSAdvancementController.api.createAdvancementTab("siege_tutorial");

        AdvancementNode rootNode = new AdvancementNode(Material.BOOK, AdvancementFrameType.TASK,
                "<white>Castle Siege", "<dark_green>Learn how to play Castle Siege",
                NodeDisplayTypes.Vanilla);

        // Cannons
        rootNode.addChild(new AdvancementNode(Material.BLACK_CONCRETE, AdvancementFrameType.TASK,
                "Shooting Cannons", "Fire a cannon", "Castle Siege"));
        rootNode.addChild(new AdvancementNode(Material.OAK_BOAT, AdvancementFrameType.CHALLENGE,
                "You sunk my battleship", "Kill a player with a cannon", "Shooting Cannons"));

        // Catapults
        rootNode.addChild(new AdvancementNode(Material.COBBLESTONE, AdvancementFrameType.TASK,
                "Firing Catapults", "Flip the lever on a catapult and launch a projectile into the sky!",
                "Castle Siege"));
        rootNode.addChild(new AdvancementNode(Material.TARGET, AdvancementFrameType.TASK,
                "Aim carefully", "Adjust pitch & yaw for a catapult and then fire", "Firing Catapults"));
        rootNode.addChild(new AdvancementNode(Material.SKELETON_SKULL, AdvancementFrameType.CHALLENGE,
                "Master catapulteer", "Kill a player with a catapult", "Aim carefully"));

        // Rams
        rootNode.addChild(new AdvancementNode(Material.OAK_WOOD, AdvancementFrameType.TASK,
                "Ramming Gates", "Deal damage to a gate", "Castle Siege"));
        rootNode.addChild(new AdvancementNode(Material.PLAYER_HEAD, AdvancementFrameType.GOAL,
                "Lift with your back", "Have at least 4 players on the same ram", "Ramming Gates"));
        rootNode.addChild(new AdvancementNode(Material.OAK_WOOD, AdvancementFrameType.TASK,
                "I'm sorry for barging in", "Work with others to break down a gate", "Ramming Gates"));
        rootNode.addChild(new AdvancementNode(Material.CHISELED_STONE_BRICKS, AdvancementFrameType.CHALLENGE,
                "I did knock...", "Deal at least <light_purple>90%<light_purple> of the damage to a gate",
                "I'm sorry for barging in"));

        // Flags
        rootNode.addChild(new AdvancementNode(Material.RED_WOOL, AdvancementFrameType.TASK,
                "Capturing Flags", "Help capture a flag", "Castle Siege"));
        rootNode.addChild(new AdvancementNode(Material.BLACK_WOOL, AdvancementFrameType.TASK,
                "Shh, it's a secret...", "Discover a secret flag", "Capturing Flags"));
        rootNode.addChild(new AdvancementNode(Material.BLUE_WOOL, AdvancementFrameType.GOAL,
                "A finger in every pie", "Help capture every capturable flag on a map",
                "Capturing Flags"));
        rootNode.addChild(new AdvancementNode(Material.GREEN_WOOL, AdvancementFrameType.TASK,
                "Everywhere all at once", "Earn the most capture points in a game",
                "A finger in every pie"));
        rootNode.addChild(new AdvancementNode(Material.YELLOW_WOOL, AdvancementFrameType.TASK,
                "I tried so hard", "Earn the most capture points in a game and still lose",
                "Everywhere all at once"));
        rootNode.addChild(new AdvancementNode(Material.LIGHT_BLUE_WOOL, AdvancementFrameType.TASK,
                "Everywhere once at all", "Spawn at every flag on a map", "Capturing Flags"));

        // Winning Games
        rootNode.addChild(new AdvancementNode(Material.GOLD_BLOCK, AdvancementFrameType.TASK,
                "Winner Winner Chicken Dinner", "Win a game", "Castle Siege"));
        rootNode.addChild(new AdvancementNode(Material.SHIELD, AdvancementFrameType.GOAL,
                "Hold fast!", "Win a game without losing any flags", "Winner Winner Chicken Dinner"));
        rootNode.addChild(new AdvancementNode(Material.CAKE, AdvancementFrameType.TASK,
                "Simply the best", "Win a game as an MVP", "Winner Winner Chicken Dinner"));
        rootNode.addChild(new AdvancementNode(Material.YELLOW_BANNER, AdvancementFrameType.GOAL,
                "I want it all", "Win a game by capturing all the flags", "Winner Winner Chicken Dinner"));

        Tuple<RootAdvancement, BaseAdvancement[]> advs = rootNode.asAdvancementList(tab, "textures/block/stone_bricks.png");
        advancements = advs.getSecond();

        tab.registerAdvancements(advs.getFirst(), advancements);

        // Events
        tab.getEventManager().register(this, CannonEvent.class, this::onCannonEvent);
        tab.getEventManager().register(this, RamEvent.class, this::onRamEvent);
    }

    private void onCannonEvent(CannonEvent event) {
        if (event.isCancelled()) return;

        tab.getAdvancement(new AdvancementKey("siege_tutorial", NodeDisplay.cleanKey("Shooting Cannons"))).grant(event.getShooter());
    }

    private void onRamEvent(RamEvent event) {
        if (event.isCancelled()) return;

        for (UUID uuid : event.getPlayerUUIDs()) {
            // Update Tracking
            if (!ramAttackers.containsKey(event.getGateName())) {
                ramAttackers.put(event.getGateName(), new HashSet<>());

            }
            ramAttackers.get(event.getGateName()).add(uuid);
            new BukkitRunnable() {
                @Override
                public void run() {
                    Player p = Bukkit.getPlayer(uuid);
                    tab.getAdvancement(new AdvancementKey("siege_tutorial", NodeDisplay.cleanKey("Ramming Gates"))).grant(p);

                    if (event.getPlayerUUIDs().size() >= 4) {
                        tab.getAdvancement(new AdvancementKey("siege_tutorial", NodeDisplay.cleanKey("Lift with your back"))).grant(p);
                    }
                }
            }.runTask(Main.plugin);
        }

        // Ram has been destroyed
        if (!event.wasGateBreached()) {
            return;
        }

        if (ramAttackers.get(event.getGateName()).size() > 1) {
            Advancement breachAdv = tab.getAdvancement(new AdvancementKey("siege_tutorial", NodeDisplay.cleanKey("Ramming Gates")));
            for (UUID uuid : ramAttackers.get(event.getGateName())) {
                Player contributor = Bukkit.getPlayer(uuid);
                if (contributor != null) {
                    breachAdv.grant(contributor);
                }
            }
        }

        ramAttackers.remove(event.getGateName());
    }
}
