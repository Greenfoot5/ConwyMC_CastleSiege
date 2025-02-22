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
import me.greenfoot5.conwymc.data_types.Cosmetic;
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

        advancementTab = CSAdvancementController.api.getAdvancementTab("siege_levels");
        if (advancementTab != null) {
            return;
        }

        advancementTab = CSAdvancementController.api.createAdvancementTab("siege_levels");

        AdvancementNode rootNode = new AdvancementNodeBuilder("level_milestones")
                .setMaterial(Material.EXPERIENCE_BOTTLE)
                .setFrameType(AdvancementFrameType.TASK)
                .setTitle("Level Milestones")
                .setDescription("<dark_green>Advance through the Castle Siege levels gaining cool rewards as you progress!")
                .build();

        // Level 2 - Shieldman Kit
        rootNode.addChild(new AdvancementNodeBuilder("level_2", "level_milestones")
                .setMaterial(Material.SHIELD)
                .setFrameType(AdvancementFrameType.GOAL)
                .setTitle("Just Beginning")
                .setRequirements(new String[]{"<yellow>⭐ Reach Level 2</yellow>"})
                .setReward("<green>\uD83D\uDEE1 Shieldman Kit</green>"
                + "<br><gold>⛃ +1000 coins</gold>")
                .setMaxProgress(2)
                .build());

        // Level 3 - Switch Command
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

        // Level 5 - Spearman Kit
        rootNode.addChild(new AdvancementNodeBuilder("level_5", "level_4")
                .setMaterial(Material.STICK)
                .setFrameType(AdvancementFrameType.GOAL)
                .setTitle("Practice Throws")
                .setRequirements(new String[]{"<yellow>⭐ Reach Level 5</yellow>"})
                .setReward("<green>\uD83D\uDD31 Spearman Kit</green>" +
                        "<br><gold>⛃ +1000 coins</gold>")
                .setMaxProgress(5)
                .build());

        // Level 6 - Medic Booster
        rootNode.addChild(new AdvancementNodeBuilder("level_6", "level_5")
                .setMaterial(Material.SUNFLOWER)
                .setFrameType(AdvancementFrameType.GOAL)
                .setTitle("Feeling Stronger")
                .setRequirements(new String[]{"<yellow>⭐ Reach Level 6</yellow>"})
                .setReward("<gold>⛃ +500 coins</gold>" +
                        "<br>green>⇵ Medic Kit Booster (2 hours)</green>")
                .setMaxProgress(6)
                .build());

        // Level 7 - Bounties
        rootNode.addChild(new AdvancementNodeBuilder("level_7", "level_6")
                .setMaterial(Material.NAME_TAG)
                .setFrameType(AdvancementFrameType.GOAL)
                .setTitle("Soldier in Training")
                .setRequirements(new String[]{"<yellow>⭐ Reach Level 7</yellow>"})
                .setReward("<yellow>◎ Bounties</yellow>" +
                        "<br><gold>⛃ +1000 coins</gold>"
                        + "<br><br><blue>You can set bounties through the <br><yellow>/bounty <player> <amount></yellow><br>command.")
                .setMaxProgress(7)
                .build());

        // Level 8 - Random Booster
        rootNode.addChild(new AdvancementNodeBuilder("level_8", "level_milestones")
                .setMaterial(Material.LAPIS_LAZULI)
                .setFrameType(AdvancementFrameType.GOAL)
                .setTitle("Graceful Dancer")
                .setRequirements(new String[]{"<yellow>⭐ Reach Level 8</yellow>"})
                .setReward("<gold>⛃ +500 coins</gold>" +
                        "<br><green>⇵ <obf>!</obf>Random<obf>!</obf> Kit Booster (1 hour)</green>")
                .setMaxProgress(8)
                .build());

        // Level 9 - Maceman Booster
        rootNode.addChild(new AdvancementNodeBuilder("level_9", "level_8")
                .setMaterial(Material.COOKIE)
                .setFrameType(AdvancementFrameType.GOAL)
                .setTitle("Born for Battle")
                .setRequirements(new String[]{"<yellow>⭐ Reach Level 9</yellow>"})
                .setReward("<gold>⛃ +500 coins</gold>" +
                        "<br><green>⇵ Maceman Kit Booster (2 hours)</green>")
                .setMaxProgress(9)
                .build());

        // Level 10 - Spear Knight Kit & Wild Booster
        rootNode.addChild(new AdvancementNodeBuilder("level_10", "level_9")
                .setMaterial(Material.WOODEN_SWORD)
                .setFrameType(AdvancementFrameType.GOAL)
                .setTitle("Mighty Hunter")
                .setRequirements(new String[]{"<yellow>⭐ Reach Level 10</yellow>"})
                .setReward("<green>\uD83D\uDD31 Spear Knight Kit</green>" +
                        "<br><green>⇵ ⁎Wild⁎ Kit Booster (2 hours)</green>")
                .setMaxProgress(10)
                .build());

        // Level 11 - Berserker Booster
        rootNode.addChild(new AdvancementNodeBuilder("level_11", "level_10")
                .setMaterial(Material.STONE_SHOVEL)
                .setFrameType(AdvancementFrameType.GOAL)
                .setTitle("Ran out of name ideas")
                .setRequirements(new String[]{"<yellow>⭐ Reach Level 11</yellow>"})
                .setReward("<br><gold>⛃ +500 coins</gold>" +
                        "<br><green>⇵ Berserker Kit Booster (2 hours)</green>")
                .setMaxProgress(11)
                .build());

        // Level 12 - Random Booster
        rootNode.addChild(new AdvancementNodeBuilder("level_12", "level_11")
                .setMaterial(Material.STONE_SWORD)
                .setFrameType(AdvancementFrameType.GOAL)
                .setTitle("Something something, well done")
                .setRequirements(new String[]{"<yellow>⭐ Reach Level 12</yellow>"})
                .setReward("<gold>⛃ +500 coins</gold>" +
                        "<br><green>⇵ <obf>!</obf>Random<obf>!</obf> Kit Booster (1 hour)</green>")
                .setMaxProgress(12)
                .build());

        // Level 13 - Coins
        rootNode.addChild(new AdvancementNodeBuilder("level_13", "level_12")
                .setMaterial(Material.IRON_PICKAXE)
                .setFrameType(AdvancementFrameType.GOAL)
                .setTitle("Experienced Combatant")
                .setRequirements(new String[]{"<yellow>⭐ Reach Level 13</yellow>"})
                .setReward("<gold>⛃ +500 coins</gold>" +
                        "<br><green>⇵ Vanguard Kit Booster (2 hours)</green>")
                .setMaxProgress(13)
                .build());

        // Level 14 - Random Booster
        rootNode.addChild(new AdvancementNodeBuilder("level_14", "level_milestones")
                .setMaterial(Material.IRON_SWORD)
                .setFrameType(AdvancementFrameType.GOAL)
                .setTitle("Awoken Skillsmith")
                .setRequirements(new String[]{"<yellow>⭐ Reach Level 14</yellow>"})
                .setReward("<gold>⛃ +500 coins</gold>" +
                        "<br><green>⇵ <obf>!</obf>Random<obf>!</obf> Kit Booster (1 hour)</green>")
                .setMaxProgress(14)
                .build());

        // Level 15 - Battle Medic Kit & Wild Booster
        rootNode.addChild(new AdvancementNodeBuilder("level_15", "level_14")
                .setMaterial(Material.PAPER)
                .setFrameType(AdvancementFrameType.GOAL)
                .setTitle("Rising Up")
                .setRequirements(new String[]{"<yellow>⭐ Reach Level 15</yellow>"})
                .setReward("<green>⛨ Battle Medic Kit</green>" +
                        "<br><green>⇵ ⁎Wild⁎ Kit Booster (2 hours)</green>")
                .setMaxProgress(15)
                .build());

        // Level 16 - Random Booster
        rootNode.addChild(new AdvancementNodeBuilder("level_16", "level_15")
                .setMaterial(Material.GILDED_BLACKSTONE)
                .setFrameType(AdvancementFrameType.GOAL)
                .setTitle("Subtle Sheen")
                .setRequirements(new String[]{"<yellow>⭐ Reach Level 16</yellow>"})
                .setReward("<gold>⛃ +500 coins</gold>" +
                        "<br><green>⇵ <obf>!</obf>Random<obf>!</obf> Kit Booster (1 hour)</green>")
                .setMaxProgress(16)
                .build());

        // Level 17 - Paladin Booster
        rootNode.addChild(new AdvancementNodeBuilder("level_17", "level_16")
                .setMaterial(Material.GOLD_NUGGET)
                .setFrameType(AdvancementFrameType.GOAL)
                .setTitle("Ready to Rumble")
                .setRequirements(new String[]{"<yellow>⭐ Reach Level 17</yellow>"})
                .setReward("<gold>⛃ +500 coins</gold>" +
                        "<br><green>⇵ Paladin Kit Booster (2 hours)</green>")
                .setMaxProgress(17)
                .build());

        // Level 18 - Random Booster
        rootNode.addChild(new AdvancementNodeBuilder("level_18", "level_17")
                .setMaterial(Material.RAW_GOLD)
                .setFrameType(AdvancementFrameType.GOAL)
                .setTitle("Refiner")
                .setRequirements(new String[]{"<yellow>⭐ Reach Level 18</yellow>"})
                .setReward("<gold>⛃ +500 coins</gold>" +
                        "<br><green>⇵ <obf>!</obf>Random<obf>!</obf> Kit Booster (1 hour)</green>")
                .setMaxProgress(18)
                .build());

        // Level 19 - Ranger Booster
        rootNode.addChild(new AdvancementNodeBuilder("level_19", "level_18")
                .setMaterial(Material.GOLD_INGOT)
                .setFrameType(AdvancementFrameType.GOAL)
                .setTitle("Artist of War")
                .setRequirements(new String[]{"<yellow>⭐ Reach Level 19</yellow>"})
                .setReward("<gold>⛃ +500 coins</gold>" +
                        "<br><green>⇵ Ranger Kit Booster (2 hours)</green>")
                .setMaxProgress(19)
                .build());

        // Level 20 - Hypaspist Kit & Title
        rootNode.addChild(new AdvancementNodeBuilder("level_20", "level_milestones")
                .setMaterial(Material.GOLDEN_CHESTPLATE)
                .setFrameType(AdvancementFrameType.CHALLENGE)
                .setTitle("Shield Carrier")
                .setRequirements(new String[]{"<yellow>⭐ Reach Level 20</yellow>"})
                .setReward("<green>\uD83D\uDEE1 Hypaspist Kit</green>" +
                        "<br><gold>\uD83C\uDFF7 <gradient:#63D471:#55efc4>Practiced</gradient> Title")
                .setMaxProgress(20)
                .build());

        // Level 25 - Wild Booster
        rootNode.addChild(new AdvancementNodeBuilder("level_25", "level_20")
                .setMaterial(Material.BLACK_CANDLE)
                .setFrameType(AdvancementFrameType.CHALLENGE)
                .setTitle("Waxy Wings")
                .setRequirements(new String[]{"<yellow>⭐ Reach Level 25</yellow>"})
                .setReward("<gold>⛃ +1000 coins</gold>" +
                        "<br><green>⇵ ⁎Wild⁎ Kit Booster (2 hours)</green>")
                .setMaxProgress(25)
                .build());

        // Level 30 - Wild Booster
        rootNode.addChild(new AdvancementNodeBuilder("level_30", "level_25")
                .setMaterial(Material.LADDER)
                .setFrameType(AdvancementFrameType.CHALLENGE)
                .setTitle("Tower Climber")
                .setRequirements(new String[]{"<yellow>⭐ Reach Level 30</yellow>"})
                .setReward("<gold>⛃ +1000 coins</gold>" +
                        "<br><green>⇵ ⁎Wild⁎ Kit Booster (2 hours)</green>")
                .setMaxProgress(30)
                .build());

        // Level 35 - Wild Booster
        rootNode.addChild(new AdvancementNodeBuilder("level_35", "level_30")
                .setMaterial(Material.GOLD_BLOCK)
                .setFrameType(AdvancementFrameType.CHALLENGE)
                .setTitle("Summit so close...")
                .setRequirements(new String[]{"<yellow>⭐ Reach Level 35</yellow>"})
                .setReward("<gold>⛃ +1000 coins</gold>" +
                        "<br><green>⇵ ⁎Wild⁎ Kit Booster (2 hours)</green>")
                .setMaxProgress(35)
                .build());

        // Level 40 - Title & Chat Colour
        rootNode.addChild(new AdvancementNodeBuilder("level_40", "level_35")
                .setMaterial(Material.CAKE)
                .setFrameType(AdvancementFrameType.CHALLENGE)
                .setTitle("Shining Star")
                .setRequirements(new String[]{"<yellow>⭐ Reach Level 40</yellow>"})
                .setReward("<gold>\uD83C\uDFF7 <gradient:#CB218E:#6617CB>✦<b>Shiny</b>✨</gradient> Title" +
                        "<br>\uD83D\uDDE8 <gradient:#CB218E:#6617CB>Chat Colour</gradient>")
                .setMaxProgress(40)
                .build());

//        // Level 80 - ???
//        rootNode.addChild(new AdvancementNodeBuilder("level_80", "level_40")
//                .setMaterial(Material.NETHERITE_SWORD)
//                .setFrameType(AdvancementFrameType.GOAL)
//                .setTitle("Not quite Sisyphus")
//                .setRequirements(new String[]{"<yellow>⭐ Reach Level 80</yellow>"})
//                .setMaxProgress(80)
//                .build());

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
                advancement.setProgression(p, event.getNewLevel());
            } else if (!advancement.isGranted(p)) {
                // Grant Advancement & reward
                advancement.grant(p);
                switch (advancement.getMaxProgression()) {
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 7:
                        data.addCoinsClean(1000);
                        Messenger.sendCongrats("You gained <gold>1000 coins</gold> for levelling up!", p);
                        break;
                    case 6:
                        data.addCoinsClean(500);
                        Messenger.sendCongrats("You gained <gold>500 coins</gold> for levelling up!", p);
                        GrantBoosterCommand.grantBooster(p, new String[]{p.getName(), "kit", "2h", "medic"}, false);
                        Messenger.sendCongrats("You gained <green>Medic Kit Booster (2 hours)</green> for levelling up!", p);
                        break;
                    case 8:
                    case 12:
                    case 14:
                    case 16:
                    case 18:
                        data.addCoinsClean(500);
                        Messenger.sendCongrats("You gained <gold>500 coins</gold> for levelling up!", p);
                        GrantBoosterCommand.grantBooster(p, new String[]{p.getName(), "kit", "1h", "random"}, false);
                        Messenger.sendCongrats("You gained <green><obf>!</obf>Random<obf>!</obf> Kit Booster (1 hour)</green> for levelling up!", p);
                        break;
                    case 9:
                        data.addCoinsClean(500);
                        Messenger.sendCongrats("You gained <gold>500 coins</gold> for levelling up!", p);
                        GrantBoosterCommand.grantBooster(p, new String[]{p.getName(), "kit", "2h", "maceman"}, false);
                        Messenger.sendCongrats("You gained <green>Maceman Kit Booster (2 hours)</green> for levelling up!", p);
                        break;
                    case 11:
                        data.addCoinsClean(500);
                        Messenger.sendCongrats("You gained <gold>500 coins</gold> for levelling up!", p);
                        GrantBoosterCommand.grantBooster(p, new String[]{p.getName(), "kit", "2h", "berserker"}, false);
                        Messenger.sendCongrats("You gained <green>Berserker Kit Booster (2 hours)</green> for levelling up!", p);
                        break;
                    case 13:
                        data.addCoinsClean(500);
                        Messenger.sendCongrats("You gained <gold>500 coins</gold> for levelling up!", p);
                        GrantBoosterCommand.grantBooster(p, new String[]{p.getName(), "kit", "2h", "vanguard"}, false);
                        Messenger.sendCongrats("You gained <green>Vanguard Kit Booster (2 hours)</green> for levelling up!", p);
                        break;
                    case 17:
                        data.addCoinsClean(500);
                        Messenger.sendCongrats("You gained <gold>500 coins</gold> for levelling up!", p);
                        GrantBoosterCommand.grantBooster(p, new String[]{p.getName(), "kit", "2h", "paladin"}, false);
                        Messenger.sendCongrats("You gained <green>Paladin Kit Booster (2 hours)</green> for levelling up!", p);
                        break;
                    case 19:
                        data.addCoinsClean(500);
                        Messenger.sendCongrats("You gained <gold>500 coins</gold> for levelling up!", p);
                        GrantBoosterCommand.grantBooster(p, new String[]{p.getName(), "kit", "2h", "ranger"}, false);
                        Messenger.sendCongrats("You gained <green>Ranger Kit Booster (2 hours)</green> for levelling up!", p);
                        break;

                    // Multiples of 5
                    case 25:
                    case 30:
                    case 35:
                        data.addCoinsClean(500);
                        Messenger.sendCongrats("You gained <gold>500 coins</gold> for levelling up!", p);
                        break;
                    case 10:
                    case 15:
                        GrantBoosterCommand.grantBooster(p, new String[]{p.getName(), "kit", "2h", "wild"}, false);
                        Messenger.sendCongrats("You gained <green>⇵ ⁎Wild⁎ Kit Booster (2 hours)</green> for levelling up!", p);
                        break;

                    // Cosmetics
                    case 20:
                        Cosmetic level20 = new Cosmetic(Cosmetic.CosmeticType.TITLE, "Level 20", "<gradient:#63D471:#55efc4>Practiced</gradient>");
                        data.addCosmetic(level20);
                        break;
                    case 40:
                        Cosmetic level40 = new Cosmetic(Cosmetic.CosmeticType.TITLE, "Level 40", "<gradient:#CB218E:#6617CB>✦<b>Shiny</b>✨</gradient>");
                        data.addCosmetic(level40);
                        level40 = new Cosmetic(Cosmetic.CosmeticType.CHAT_COLOUR, "Level 40", "<gradient:#CB218E:#6617CB>");
                        data.addCosmetic(level40);
                        break;
                }
            }
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
