package me.greenfoot5.advancements.tutorial;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.fren_gor.ultimateAdvancementAPI.AdvancementTab;
import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.RootAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.util.AdvancementKey;
import me.greenfoot5.advancements.CSAdvancementController;
import me.greenfoot5.advancements.api.advancements.StandardAdvancement;
import me.greenfoot5.advancements.api.nodes.AdvancementNode;
import me.greenfoot5.advancements.api.nodes.AdvancementNode.AdvancementNodeBuilder;
import me.greenfoot5.advancements.api.nodes.AdvancementAdvancementNode.AdvancementAdvancementNodeBuilder;
import me.greenfoot5.advancements.api.nodes.HiddenAdvancementNode.HiddenAdvancementNodeBuilder;
import me.greenfoot5.advancements.api.nodes.LevelAdvancementNode.LevelAdvancementNodeBuilder;
import me.greenfoot5.castlesiege.Main;
import me.greenfoot5.castlesiege.events.map.GateBreachEvent;
import me.greenfoot5.castlesiege.events.map.RamEvent;
import me.greenfoot5.conwymc.data_types.Tuple;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerTextures;
import org.bukkit.scheduler.BukkitRunnable;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * Manages the basic advancements for doing stuff for the first time
 */
public class TutorialAdvancements implements Listener {

    public static AdvancementTab tab;

    // Event tracking
    private final HashMap<String, Set<UUID>> ramAttackers = new HashMap<>();

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
        generateFlagAdvancements(rootNode);
        generateWinAdvancements(rootNode);
        generateBountyAdvancements(rootNode);

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

    @EventHandler
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
                    Objects.requireNonNull(tab.getAdvancement(new AdvancementKey(tab.getNamespace(), "gate_hit"))).grant(Objects.requireNonNull(p));

                    if (event.getPlayerUUIDs().size() >= 4) {
                        Objects.requireNonNull(tab.getAdvancement(new AdvancementKey(tab.getNamespace(), "get_four"))).grant(p);
                    }
                }
            }.runTask(Main.plugin);
        }

        // Gate has been destroyed
        if (event.wasGateBreached()) {
            ramAttackers.remove(event.getGateName());
        }
    }

    @EventHandler
    private void onGateBreach(GateBreachEvent event) {
        for (UUID uuid : event.getDamagers().keySet()) {
            // Breach event if >1 players dealt damage
            if (event.getDamagers().size() > 1) {
                Advancement breachAdv = tab.getAdvancement(new AdvancementKey(tab.getNamespace(), "gate_break"));
                Player contributor = Bukkit.getPlayer(uuid);
                if (contributor != null && breachAdv != null) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            breachAdv.grant(contributor);
                        }
                    }.runTask(Main.plugin);
                }
            }

            // More than 90% of damage dealt
            if (event.getDamage(uuid) >= event.getOriginalHealth() * 0.9) {
                Advancement breachAdv = tab.getAdvancement(new AdvancementKey(tab.getNamespace(), "gate_solo"));
                Player solo = Bukkit.getPlayer(uuid);
                if (solo != null && breachAdv != null) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            breachAdv.grant(solo);
                        }
                    }.runTask(Main.plugin);
                }
            }
        }
    }

    private void generateFlagAdvancements(AdvancementNode root) {
        root.addChild(new AdvancementNodeBuilder("flag_cap", "siege_tutorial")
                .setMaterial(Material.RED_WOOL)
                .setFrameType(AdvancementFrameType.TASK)
                .setTitle("Capturing Flags")
                .setRequirements(new String[]{"<yellow>\uD83C\uDFF4 Earn a capture point</yellow>"})
                .build());
        root.addChild(new HiddenAdvancementNodeBuilder("flag_secret", "flag_cap")
                .setMaterial(Material.BLACK_WOOL)
                .setFrameType(AdvancementFrameType.CHALLENGE)
                .setTitle("Shh, it's a secret...")
                .setRequirements(new String[]{"<yellow>❓ Discover a secret flag</yellow>"})
                .build());
        root.addChild(new AdvancementNodeBuilder("flag_all", "flag_cap")
                .setMaterial(Material.BLUE_WOOL)
                .setFrameType(AdvancementFrameType.TASK)
                .setTitle("A finger in every pie")
                .setRequirements(new String[]{"<yellow>✥ Help capture every capturable flag on a map</yellow>"})
                .build());
        root.addChild(new AdvancementAdvancementNodeBuilder("flag_solo", "flag_all")
                .addRequiredKey("flag_cap")
                .setMaterial(Material.LIME_WOOL)
                .setFrameType(AdvancementFrameType.CHALLENGE)
                .setTitle("Everywhere all at once")
                .setRequirements(new String[]{"<yellow>\uD83E\uDD47 Earn the most capture points on a map</yellow>"})
                .build());
        root.addChild(new AdvancementAdvancementNodeBuilder("flag_lose", "flag_solo")
                .addRequiredKey("flag_solo")
                .setMaterial(Material.YELLOW_WOOL)
                .setFrameType(AdvancementFrameType.CHALLENGE)
                .setTitle("Tried so hard")
                .setRequirements(new String[]{"<yellow>\uD83E\uDD47 Earn the most capture points on a map</yellow>",
                "<yellow>\uD83D\uDCA9 Lose that same map</yellow>"})
                .build());
        root.addChild(new AdvancementNodeBuilder("flag_spawn", "flag_cap")
                .setMaterial(Material.LIGHT_BLUE_WOOL)
                .setFrameType(AdvancementFrameType.TASK)
                .setTitle("Everywhere once at all")
                .setRequirements(new String[]{"<yellow>\uD83E\uDDED Spawn at every flag on a map</yellow>"})
                .build());
        root.addChild(new AdvancementAdvancementNodeBuilder("flag_full", "flag_all")
                .setMaterial(Material.GREEN_WOOL)
                .setFrameType(AdvancementFrameType.TASK)
                .setTitle("As high as can be")
                .setRequirements(new String[]{"<yellow>\uD83C\uDFF4 Fully capture a flag</yellow>"})
                .build());
        root.addChild(new AdvancementAdvancementNodeBuilder("flag_lock", "flag_full")
                .setMaterial(Material.BROWN_WOOL)
                .setFrameType(AdvancementFrameType.TASK)
                .setTitle("No take-backsies")
                .setRequirements(new String[]{"<yellow>\uD83C\uDFF4 Fully capture a flag</yellow>",
                        "<yellow>\uD83D\uDD12 The flag then becomes locked</yellow>"})
                .build());
        root.addChild(new AdvancementAdvancementNodeBuilder("flag_time", "flag_full")
                .setMaterial(Material.WHITE_WOOL)
                .setFrameType(AdvancementFrameType.TASK)
                .setTitle("Just in time")
                .setRequirements(new String[]{"<yellow>\uD83C\uDFF4 Fully capture a flag</yellow>",
                        "<yellow>⌚ Gain extra time from the flag</yellow>"})
                .build());
        root.addChild(new AdvancementAdvancementNodeBuilder("flag_life", "flag_full")
                .setMaterial(Material.PINK_WOOL)
                .setFrameType(AdvancementFrameType.TASK)
                .setTitle("Now you can die more")
                .setRequirements(new String[]{"<yellow>\uD83C\uDFF4 Fully capture a flag</yellow>",
                        "<yellow>❤ Gain additional lives for your team from the flag</yellow>"})
                .build());
    }

    private void generateWinAdvancements(AdvancementNode root) {
        root.addChild(new AdvancementNodeBuilder("win", "siege_tutorial")
                .setMaterial(Material.GOLD_BLOCK)
                .setFrameType(AdvancementFrameType.TASK)
                .setTitle("Winner Winner Chicken Dinner")
                .setRequirements(new String[]{"<yellow>\uD83D\uDC51 Win a game</yellow>",
                "<yellow>\uD83D\uDCAE Earn at least 20 score in that game</yellow>"})
                .build());
        root.addChild(new AdvancementAdvancementNodeBuilder("win_mvp", "win")
                .addRequiredKey("win")
                .setMaterial(Material.CAKE)
                .setFrameType(AdvancementFrameType.GOAL)
                .setTitle("Simply the best")
                .setRequirements(new String[]{"<yellow>\uD83D\uDC51 Win a game</yellow>",
                        "<yellow>\uD83D\uDCAE Earn at least 20 score in that game</yellow>",
                        "<yellow>\uD83C\uDF96 By your team's MVP</yellow>"})
                .build());
        root.addChild(new AdvancementAdvancementNodeBuilder("win_defense", "win")
                .addRequiredKey("win")
                .setMaterial(Material.SHIELD)
                .setFrameType(AdvancementFrameType.TASK)
                .setTitle("Hold fast!")
                .setRequirements(new String[]{"<yellow>\uD83D\uDC51 Win a game</yellow>",
                        "<yellow>\uD83D\uDCAE Earn at least 20 score in that game</yellow>",
                        "<yellow>\uD83C\uDFF4 Don't lose control of any flags</yellow>"})
                .build());
        root.addChild(new AdvancementAdvancementNodeBuilder("win_time", "win_defense")
                .addRequiredKey("win")
                .setMaterial(Material.CLOCK)
                .setFrameType(AdvancementFrameType.TASK)
                .setTitle("CHAAAAAAAARGE!")
                .setRequirements(new String[]{"<yellow>\uD83D\uDC51 Win a game</yellow>",
                        "<yellow>\uD83D\uDCAE Earn at least 20 score in that game</yellow>",
                        "<yellow>\uD83C\uDFF4 Win a <red>Charge</red> map by running out the time</yellow>"})
                .build());
        root.addChild(new AdvancementAdvancementNodeBuilder("win_domination", "win")
                .addRequiredKey("win")
                .setMaterial(Material.YELLOW_BANNER)
                .setFrameType(AdvancementFrameType.TASK)
                .setTitle("I want it all")
                .setRequirements(new String[]{"<yellow>\uD83D\uDC51 Win a game</yellow>",
                        "<yellow>\uD83D\uDCAE Earn at least 20 score in that game</yellow>",
                        "<yellow>\uD83C\uDFF4 Win a <red>Domination</red> map by capturing all the enemy flags</yellow>"})
                .build());
        root.addChild(new AdvancementAdvancementNodeBuilder("win_lives", "win_domination")
                .addRequiredKey("win")
                .setMaterial(Material.HEARTBREAK_POTTERY_SHERD)
                .setFrameType(AdvancementFrameType.TASK)
                .setTitle("Kill them all")
                .setRequirements(new String[]{"<yellow>\uD83D\uDC51 Win a game</yellow>",
                        "<yellow>\uD83D\uDCAE Earn at least 20 score in that game</yellow>",
                        "<yellow>\uD83C\uDFF4 Win an <red>Assault</red> map by depleting the enemy's lives</yellow>"})
                .build());
        root.addChild(new AdvancementAdvancementNodeBuilder("win_dtc", "win_domination")
                .addRequiredKey("win")
                .setMaterial(Material.BLACK_STAINED_GLASS)
                .setFrameType(AdvancementFrameType.TASK)
                .setTitle("CHAAAAAAAARGE!")
                .setRequirements(new String[]{"<yellow>\uD83D\uDC51 Win a game</yellow>",
                        "<yellow>\uD83D\uDCAE Earn at least 20 score in that game</yellow>",
                        "<yellow>\uD83C\uDFF4 Win a <red>Destroy the Core</red> map by... destroying the enemy's core(s)</yellow>"})
                .build());
    }

    private void generateBountyAdvancements(AdvancementNode root) {
        root.addChild(new LevelAdvancementNodeBuilder("bounty_place", "siege_tutorial", 6)
                .setMaterial(Material.COAL)
                .setFrameType(AdvancementFrameType.TASK)
                .setTitle("Pirate's Black Spot")
                .setRequirements(new String[]{"<yellow>\uD83C\uDFAF Place a bounty</yellow>"})
                .build());
        root.addChild(new AdvancementAdvancementNodeBuilder("bounty_1000", "bounty_place")
                .addRequiredKey("bounty_place")
                .setMaterial(Material.COAL_BLOCK)
                .setFrameType(AdvancementFrameType.TASK)
                .setTitle("A really big black spot")
                .setRequirements(new String[]{"<yellow>⛃ Place a bounty worth 1000</yellow>"})
                .build());
        root.addChild(new AdvancementAdvancementNodeBuilder("bounty_gain", "bounty_place")
                .addRequiredKey("bounty_place")
                .setMaterial(Material.TARGET)
                .setFrameType(AdvancementFrameType.TASK)
                .setTitle("You're next...")
                .setRequirements(new String[]{"<yellow>\uD83C\uDFAF Gain a bounty</yellow>"})
                .build());
        root.addChild(new AdvancementAdvancementNodeBuilder("bounty_hunter", "bounty_place")
                .addRequiredKey("bounty_place")
                .setMaterial(Material.BOW)
                .setFrameType(AdvancementFrameType.TASK)
                .setTitle("Hunting gold")
                .setRequirements(new String[]{"<yellow>\uD83C\uDFF9 Kill a player with a bounty</yellow>"})
                .build());
    }
}
