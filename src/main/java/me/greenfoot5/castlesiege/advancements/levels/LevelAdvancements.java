package me.greenfoot5.castlesiege.advancements.levels;

import com.fren_gor.ultimateAdvancementAPI.AdvancementTab;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.RootAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import me.greenfoot5.castlesiege.advancements.api.AdvancementNode;
import me.greenfoot5.castlesiege.advancements.CSAdvancementController;
import me.greenfoot5.castlesiege.advancements.api.AdvancementNode.AdvancementNodeBuilder;
import me.greenfoot5.castlesiege.commands.staff.boosters.GrantBoosterCommand;
import me.greenfoot5.castlesiege.data_types.CSPlayerData;
import me.greenfoot5.castlesiege.database.CSActiveData;
import me.greenfoot5.castlesiege.events.LevelUpEvent;
import me.greenfoot5.conwymc.data_types.Tuple;
import me.greenfoot5.conwymc.util.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public class LevelAdvancements implements Listener, CommandExecutor {

    public static final int SWITCH_LEVEL = 3;
    public static final int BUYKIT_LEVEL = 4;
    public static final int BOUNTY_LEVEL = 7;

    public static AdvancementTab advancementTab;
    private static BaseAdvancement[] advancements;

    public LevelAdvancements() {

        advancementTab = CSAdvancementController.api.createAdvancementTab("siege_levels");

        AdvancementNode rootNode = new AdvancementNodeBuilder("level_milestones")
                .setMaterial(Material.EXPERIENCE_BOTTLE)
                .setFrameType(AdvancementFrameType.TASK)
                .setTitle("Level Milestones")
                .setDescription("<dark_green>Advance through the Castle Siege levels gaining cool rewards as you progress!")
                .build();

        // Level 2 - Shieldman
        rootNode.addChild(new AdvancementNodeBuilder("level_2", "level_milestones")
                .setMaterial(Material.SHIELD)
                .setFrameType(AdvancementFrameType.GOAL)
                .setTitle("Just Beginning")
                .setRequirements(new String[]{"<yellow>⭐ Reach Level 2</yellow>"})
                .setReward("<green>\uD83D\uDEE1 Shieldman Kit</green>"
                + "<br><gold>⛃ +1000 coins</gold>")
                .setMaxProgress(2)
                .build());

        // Level 3 - Switch
        rootNode.addChild(new AdvancementNodeBuilder("level_3", "level_2")
                .setMaterial(Material.LEVER)
                .setFrameType(AdvancementFrameType.GOAL)
                .setTitle("Warming Up")
                .setRequirements(new String[]{"<yellow>⭐ Reach Level 3</yellow>"})
                .setReward("<yellow>◎ Switch</yellow>" +
                        "<br><gold>⛃ +1000 coins</gold>"
                        + "<br><br><blue>Swap to the smallest team with the <br><yellow>/switch</yellow><br>command. "
                        + "If you're a donator, you can swap no matter the size of teams.</blue>")
                .setMaxProgress(3)
                .build());

        // Level 4 - Purchase Kits
        rootNode.addChild(new AdvancementNodeBuilder("level_4", "level_3")
                .setMaterial(Material.GOLD_BLOCK)
                .setFrameType(AdvancementFrameType.GOAL)
                .setTitle("Building Muscle")
                .setRequirements(new String[]{"<yellow>⭐ Reach Level 4</yellow>"})
                .setReward("<yellow>◎ Purchase Coin Kits</yellow>" +
                        "<br><gold>⛃ +1000 coins</gold>"
                        + "<br><br><blue>You can now purchase coin kits. Use <br><yellow>/<kit></yellow>"
                        + "<br>or select it from the kit menu to preview the kit and purchase with the blocks at the bottom.</blue>")
                .setMaxProgress(4)
                .build());

        // Level 5 - Spearman
        rootNode.addChild(new AdvancementNodeBuilder("level_5", "level_4")
                .setMaterial(Material.STICK)
                .setFrameType(AdvancementFrameType.GOAL)
                .setTitle("Practice Throws")
                .setRequirements(new String[]{"<yellow>⭐ Reach Level 5</yellow>"})
                .setReward("<green>\uD83D\uDD31 Spearman Kit</green>" +
                        "<br><gold>⛃ +1000 coins</gold>")
                .setMaxProgress(5)
                .build());

        // Level 6 - Coins
        rootNode.addChild(new AdvancementNodeBuilder("level_6", "level_5")
                .setMaterial(Material.SUNFLOWER)
                .setFrameType(AdvancementFrameType.GOAL)
                .setTitle("Feeling Stronger")
                .setRequirements(new String[]{"<yellow>⭐ Reach Level 6</yellow>"})
                .setReward("<gold>⛃ +1500 coins</gold>")
                .setMaxProgress(6)
                .build());

        // Level 7 - Bounties
        rootNode.addChild(new AdvancementNodeBuilder("level_7", "level_6")
                .setMaterial(Material.NAME_TAG)
                .setFrameType(AdvancementFrameType.GOAL)
                .setTitle("Graceful Dancer")
                .setRequirements(new String[]{"<yellow>⭐ Reach Level 7</yellow>"})
                .setReward("<yellow>◎ Bounties</yellow>" +
                        "<br><gold>⛃ +1000 coins</gold>"
                        + "<br><br><blue>You can set bounties through the <br><yellow>/bounty <player> <amount></yellow><br>command.")
                .setMaxProgress(7)
                .build());

        // Level 8 - Random Kit Booster
        rootNode.addChild(new AdvancementNodeBuilder("level_8", "level_milestones")
                .setMaterial(Material.BLAZE_ROD)
                .setFrameType(AdvancementFrameType.GOAL)
                .setTitle("Mighty Hunter")
                .setRequirements(new String[]{"<yellow>⭐ Reach Level 8</yellow>"})
                .setReward("<br><green>⇵ <obf>!</obf>Random<obf>!</obf> Kit Booster (30 minutes)</green>")
                .setMaxProgress(8)
                .build());

        // Level 9 - Coins
        rootNode.addChild(new AdvancementNodeBuilder("level_9", "level_8")
                .setMaterial(Material.BLAZE_ROD)
                .setFrameType(AdvancementFrameType.GOAL)
                .setTitle("Mighty Hunter")
                .setRequirements(new String[]{"<yellow>⭐ Reach Level 9</yellow>"})
                .setReward("<br><gold>⛃ +1500 coins</gold>")
                .setMaxProgress(9)
                .build());

        // Level 10 - Shieldman & Wild Booster
        rootNode.addChild(new AdvancementNodeBuilder("level_10", "level_9")
                .setMaterial(Material.BLAZE_ROD)
                .setFrameType(AdvancementFrameType.GOAL)
                .setTitle("Mighty Hunter")
                .setRequirements(new String[]{"<yellow>⭐ Reach Level 10</yellow>"})
                .setReward("<green>\uD83D\uDD31 Spear Knight Kit</green>" +
                        "<br><green>⇵ ⁎Wild⁎ Kit Booster (2 hours)</green>")
                .setMaxProgress(10)
                .build());

        // Level 15 - Battle Medic
        rootNode.addChild(new AdvancementNodeBuilder("level_15", "level_10")
                .setMaterial(Material.PAPER)
                .setFrameType(AdvancementFrameType.GOAL)
                .setTitle("Rising Up")
                .setRequirements(new String[]{"<yellow>⭐ Reach Level 15</yellow>"})
                .setReward("<green>⛨ Battle Medic Kit</green>")
                .setMaxProgress(15)
                .build());

        // Level 20 - Hypaspist
        rootNode.addChild(new AdvancementNodeBuilder("level_20", "level_15")
                .setMaterial(Material.GOLDEN_CHESTPLATE)
                .setFrameType(AdvancementFrameType.GOAL)
                .setTitle("Shield Carrier")
                .setRequirements(new String[]{"<yellow>⭐ Reach Level 20</yellow>"})
                .setReward("<green>\uD83D\uDEE1 Hypaspist Kit</green>")
                .setMaxProgress(20)
                .build());

        // Level 40 - ???
        rootNode.addChild(new AdvancementNodeBuilder("level_40", "level_20")
                .setMaterial(Material.DIAMOND_HOE)
                .setFrameType(AdvancementFrameType.GOAL)
                .setTitle("Mighty Star")
                .setRequirements(new String[]{"<yellow>⭐ Reach Level 40</yellow>"})
//                .setReward("<green>\uD83D\uDD31 Battle Medic Kit</green>")
                .setMaxProgress(40)
                .build());

        // Level 80 - ???
        rootNode.addChild(new AdvancementNodeBuilder("level_80", "level_40")
                .setMaterial(Material.NETHERITE_SWORD)
                .setFrameType(AdvancementFrameType.GOAL)
                .setTitle("Not quite Sisyphus")
                .setRequirements(new String[]{"<yellow>⭐ Reach Level 80</yellow>"})
//                .setReward("<green>\uD83D\uDD31 Battle Medic Kit</green>")
                .setMaxProgress(80)
                .build());

        Tuple<RootAdvancement, BaseAdvancement[]> advs = rootNode.asAdvancementList(advancementTab, "textures/block/green_concrete.png");
        advancements = advs.getSecond();

        advancementTab.registerAdvancements(advs.getFirst(), advancements);
    }

    @EventHandler
    public void onLevelUp(LevelUpEvent event) {
        Player p = event.getPlayer();
        CSPlayerData data = CSActiveData.getData(p.getUniqueId());
        assert data != null;
        // Grant Advancements
        for (BaseAdvancement advancement : advancements) {
            if (event.getNewLevel() < advancement.getMaxProgression()) {
                advancement.setProgression(p, CSActiveData.getData(p.getUniqueId()).getLevel());
            } else if (!advancement.isGranted(p)) {
                advancement.grant(p);
            }
        }

        switch (event.getNewLevel()) {
            case 2:
            case 3:
            case 4:
            case 5:
            case 7:
                data.addCoins(1000);
                Messenger.sendCongrats("You gained <gold>1000 coins</gold> for levelling up!", p);
                break;
            case 6:
            case 9:
                data.addCoins(1500);
                Messenger.sendCongrats("You gained <gold>1500 coins</gold> for levelling up!", p);
                break;
            case 8:
                GrantBoosterCommand.grantBooster(p, new String[]{"kit", "30m", "random"});
                Messenger.sendCongrats("You gained <green><obf>!</obf>Random<obf>!</obf> Kit Booster (30 minutes)</green> for levelling up!", p);
            case 10:
                GrantBoosterCommand.grantBooster(p, new String[]{"kit", "2h", "wild"});
                Messenger.sendCongrats("You gained <green>⇵ ⁎Wild⁎ Kit Booster (2 hours)</green> for levelling up!", p);
        }
    }

    /**
     * Executes the given command, returning its success.
     * <br>
     * If false is returned, then the "usage" plugin.yml entry for this command
     * (if defined) will be sent to the player.
     *
     * @param sender  Source of the command
     * @param command Command which was executed
     * @param label   Alias of the command which was used
     * @param args    Passed command arguments
     * @return true if a valid command, otherwise false
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player p = (Player) sender;
        Bukkit.getPluginManager().callEvent(new LevelUpEvent(p, CSActiveData.getData(p.getUniqueId()).getLevel()));
        return true;
    }
}
