package me.huntifi.castlesiege.kits.kits.coin_kits;

import io.lumine.mythic.bukkit.BukkitAPIHelper;
import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.database.UpdateStats;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.kits.kits.CoinKit;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.maps.NameTag;
import me.huntifi.castlesiege.maps.TeamController;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

public class Warlock extends CoinKit implements Listener {

    // Abilities mentioned for this kit are configured / handled through mythic mobs.

    private static final int health = 210;
    private static final double regen = 10.5;
    private static final double meleeDamage = 20;
    private static final int curseCooldown = 260;
    private static final int staffCooldown = 80;
    private static final int lifeDrainCooldown = 400;
    private static final int healthFunnelCooldown = 100;

    private static final BukkitAPIHelper mythicMobsApi = new BukkitAPIHelper();

    public Warlock() {
        super("Warlock", health, regen, Material.WITHER_SKELETON_SKULL);

        super.canSeeHealth = true;
        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Weapon
        es.hotbar[0] = ItemCreator.weapon(new ItemStack(Material.WITHER_SKELETON_SKULL),
                ChatColor.DARK_PURPLE + "Cursed Skull", Arrays.asList("",
                        ChatColor.YELLOW + "Right click to shoot a bolt of shadows, ",
                        ChatColor.YELLOW + "which does damage to enemies and inflicts",
                        ChatColor.YELLOW + "slowness I for 5 seconds and 70 DMG to them.",
                        ChatColor.YELLOW + "Direct hits give a soul shard and have",
                ChatColor.YELLOW + "a 10% chance to give a health stone."), null, meleeDamage);
        // Voted Weapon
        es.votedWeapon = new Tuple<>(
                ItemCreator.weapon(new ItemStack(Material.WITHER_SKELETON_SKULL),
                        ChatColor.DARK_PURPLE + "Cursed Skull",
                        Arrays.asList("",
                                ChatColor.YELLOW + "Right click to shoot a bolt of shadows, ",
                                ChatColor.YELLOW + "which does damage to enemies and inflicts",
                                ChatColor.YELLOW + "slowness I for 5 seconds and 70 to them.",
                                ChatColor.YELLOW + "Direct hits give a soul shard and have",
                                ChatColor.YELLOW + "a 10% chance to give a health stone.",
                                ChatColor.AQUA + "- voted: +2 damage"),
                        Collections.singletonList(new Tuple<>(Enchantment.KNOCKBACK, 0)), meleeDamage + 2),
                0);

        // 1st ability
        es.hotbar[1] = ItemCreator.item(new ItemStack(Material.POISONOUS_POTATO),
                ChatColor.LIGHT_PURPLE + "Curse of Slowing", Arrays.asList("",
                        ChatColor.YELLOW + "Right click to give negative damage to all ",
                        ChatColor.YELLOW + "enemies in a 7 block radius of you for 4s.",
                        ChatColor.YELLOW + " ",
                        ChatColor.YELLOW + "Has a cooldown of 13 seconds."), null);

        // 2nd ability
        es.hotbar[2] = ItemCreator.item(new ItemStack(Material.SCUTE),
                ChatColor.DARK_GREEN + "Drain life", Arrays.asList("",
                        ChatColor.YELLOW + "Fire a beam of life draining energy that deals",
                        ChatColor.YELLOW + "10 DMG/hit and heals you for 10 HP/hit.",
                        ChatColor.YELLOW + " ",
                        ChatColor.YELLOW + "Has a cooldown of 20 seconds."), null);

        // 3rd ability
        es.hotbar[3] = ItemCreator.item(new ItemStack(Material.REDSTONE),
                ChatColor.DARK_RED + "Health Funnel", Arrays.asList("",
                        ChatColor.YELLOW + "Sacrifice 25% of your health to heal",
                        ChatColor.YELLOW + "your ally for 25% of your max health.",
                        ChatColor.YELLOW + " ",
                        ChatColor.YELLOW + "Has a cooldown of 5 seconds."), null);

        // 4th ability
        es.hotbar[4] = ItemCreator.item(new ItemStack(Material.BLAZE_POWDER),
                ChatColor.GOLD + "Hellfire", Arrays.asList("",
                        ChatColor.DARK_GRAY + "Requires 5 soul shards to cast.",
                        ChatColor.YELLOW + "Shoot a large burst of hellfire in",
                        ChatColor.YELLOW + "front of you, burning anything on its path.",
                        ChatColor.YELLOW + " ",
                        ChatColor.YELLOW + "Has no cooldown."), null);

        // Chestplate
        es.chest = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE),
                ChatColor.GREEN + "Warlock's robe", null, null,
                Color.fromRGB(85, 30, 127));
        ItemMeta chest = es.chest.getItemMeta();
        ArmorMeta chestMeta = (ArmorMeta) chest;
        assert chest != null;
        ArmorTrim chestTrim= new ArmorTrim(TrimMaterial.EMERALD, TrimPattern.EYE);
        ((ArmorMeta) chest).setTrim(chestTrim);
        es.chest.setItemMeta(chestMeta);

        // Leggings
        es.legs = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_LEGGINGS),
                ChatColor.GREEN + "Warlock's pants", null, null,
                Color.fromRGB(85, 30, 127));
        ItemMeta legs = es.legs.getItemMeta();
        ArmorMeta legsMeta = (ArmorMeta) legs;
        assert legs != null;
        ArmorTrim legsTrim = new ArmorTrim(TrimMaterial.EMERALD, TrimPattern.DUNE);
        legsMeta.setTrim(legsTrim);
        es.legs.setItemMeta(legsMeta);

        // Boots
        es.feet = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_BOOTS),
                ChatColor.GREEN + "Warlock's Boots", null, null,
                Color.fromRGB(85, 30, 127));
        // Voted Boots
        es.votedFeet = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_BOOTS),
                ChatColor.GREEN + "Warlock's Boots",
                Collections.singletonList(ChatColor.AQUA + "- voted: Depth Strider II"),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)),
                Color.fromRGB(85, 30, 127));
        ItemMeta boots = es.feet.getItemMeta();
        ArmorMeta bootsMeta = (ArmorMeta) boots;
        assert boots != null;
        ArmorTrim bootsTrim = new ArmorTrim(TrimMaterial.EMERALD, TrimPattern.DUNE);
        bootsMeta.setTrim(bootsTrim);
        es.feet.setItemMeta(bootsMeta);
        es.votedFeet.setItemMeta(bootsMeta);

        super.equipment = es;

        // Perm Potion Effect
        super.potionEffects.add(new PotionEffect(PotionEffectType.SLOW, 999999, 1, true, false));
    }


    /**
     * Activate the warlock ability of shooting a shadow bolt
     * @param e The event called when right-clicking with a wither skull
     */
    @EventHandler
    public void castShadowBolt(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        ItemStack staff = p.getInventory().getItemInMainHand();
        int cooldown = p.getCooldown(Material.WITHER_SKELETON_SKULL);

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(uuid)) {
            return;
        }

        if (!Objects.equals(Kit.equippedKits.get(uuid).name, name) || !staff.getType().equals(Material.WITHER_SKELETON_SKULL))
        {
            return;
        }

        if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (cooldown == 0) {
                p.setCooldown(Material.WITHER_SKELETON_SKULL, staffCooldown);
                mythicMobsApi.castSkill(p ,"WarlockShadowbolt", p.getLocation());
            }
        }
    }

    /**
     * Activate the warlock ability of cursing the area
     * @param e The event called when right-clicking with a stick
     */
    @EventHandler
    public void castCurse(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        ItemStack pItem = p.getInventory().getItemInMainHand();
        int cooldown = p.getCooldown(Material.POISONOUS_POTATO);

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(p.getUniqueId())) {
            return;
        }

        if (!Objects.equals(Kit.equippedKits.get(p.getUniqueId()).name, name) || !pItem.getType().equals(Material.POISONOUS_POTATO)) {
            return;
        }

        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (cooldown == 0) {
                p.setCooldown(Material.POISONOUS_POTATO, curseCooldown);
                for (Player online : Bukkit.getOnlinePlayers()) {
                    cursePlayer(p, online);
                }

                Messenger.sendSuccess("You cursed the enemies around you!", p);
                mythicMobsApi.castSkill(p,"WarlockCurseEffect");
            }
        }
    }

    /**
     * Applies the curse to the user
     * @param caster The user casting the curse
     * @param cursed The user cursed
     */
    public void cursePlayer(Player caster, Player cursed) {

        if (TeamController.getTeam(caster.getUniqueId()) == TeamController.getTeam(cursed.getUniqueId())) {
            return;
        }

        if (caster.getLocation().distance(cursed.getLocation()) <= 7 && caster != cursed) {

            //Warlock gets supports for cursing enemies
            UpdateStats.addSupports(caster.getUniqueId(), 1);

            Messenger.sendWarning("You were cursed by " + caster.getDisplayName(), cursed);
            cursed.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 80, 1, true, true));
        }
    }

    /**
     * Activate the warlock ability of life drain
     * @param e The event called when right-clicking with a scute
     */
    @EventHandler
    public void castLifeDrain(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        ItemStack staff = p.getInventory().getItemInMainHand();
        int cooldown = p.getCooldown(Material.SCUTE);

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(uuid)) {
            return;
        }

        if (!Objects.equals(Kit.equippedKits.get(uuid).name, name) || !staff.getType().equals(Material.SCUTE)) {
            return;
        }

        if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (cooldown == 0) {
                p.setCooldown(Material.SCUTE, lifeDrainCooldown);
                mythicMobsApi.castSkill(p ,"WarlockLifedrain", p.getLocation());
            }
        }
    }

    /**
     * Activate the warlock ability of sacrificing health to heal a teammate
     * @param event The event called when clicking on a teammate with redstone
     */
    @EventHandler
    public void castHealthFunnel(PlayerInteractEntityEvent event) {
        Player caster = event.getPlayer();
        ItemStack staff = caster.getInventory().getItemInMainHand();
        int cooldown = caster.getCooldown(Material.REDSTONE);

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(caster.getUniqueId())) {
            return;
        }

        // If the user isn't a warlock
        if (!Objects.equals(Kit.equippedKits.get(caster.getUniqueId()).name, name) || !staff.getType().equals(Material.REDSTONE)) {
            return;
        }

        // They've clicked a player on the same team
        Entity clickedEntity = event.getRightClicked();
        if (clickedEntity instanceof Player &&
                TeamController.getTeam(caster.getUniqueId()) == TeamController.getTeam(clickedEntity.getUniqueId())) {

            // The healed isn't at full health
            Player healed = (Player) clickedEntity;
            if (healed.getHealth() >= Kit.equippedKits.get(healed.getUniqueId()).baseHealth) {
                Messenger.sendActionError("This player already has full health.", caster);
                return;
            }
            if (caster.getHealth() < (double) Kit.equippedKits.get(caster.getUniqueId()).baseHealth / 4) {
                Messenger.sendError("You cannot sacrifice your health cause you are too low on HP!", caster);
                return;
            }


            if (cooldown == 0) {
                caster.setCooldown(Material.REDSTONE, healthFunnelCooldown);
                UpdateStats.addHeals(caster.getUniqueId(), 2);

                double healthTransferred = (double) Kit.equippedKits.get(caster.getUniqueId()).baseHealth / 4;
                healed.setHealth(healed.getHealth() + healthTransferred);
                caster.setHealth(caster.getHealth() - healthTransferred);


                Messenger.sendSuccess(NameTag.color(caster) + caster.getName() + "§r has sacrificed health for you", healed);
                Messenger.sendSuccess("You have sacrificed your health for " + NameTag.color(healed) + healed.getName(), caster);

                mythicMobsApi.castSkill(healed,"WarlockHealthFunnelEffect");
            }
        }
    }

    /**
     * Activate the warlock ability of hellfire
     * @param e The event called when right-clicking with a scute
     */
    @EventHandler
    public void clickHellfire(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        ItemStack staff = p.getInventory().getItemInMainHand();

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(uuid)) {
            return;
        }

        if (!Objects.equals(Kit.equippedKits.get(uuid).name, name) || !staff.getType().equals(Material.BLAZE_POWDER)) {
            return;
        }
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (!p.getInventory().contains(Material.AMETHYST_SHARD, 5)) {
                Messenger.sendError("You require 5 soul shards to perform this spell!", p);
            }

            for (ItemStack item : p.getInventory().getContents()) {
                mythicMobsApi.castSkill(p ,"Hellfire", p.getLocation());
                if (item == null) { return; }
                if (item.getType().equals(Material.AMETHYST_SHARD)) {
                    item.setAmount(item.getAmount() - 5);
                }
            }
        }
    }



    /**
     * Consume a health stone
     * @param e The event called when right-clicking a health stone/emerald
     */
    @EventHandler
    public void clickHealthStone(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        ItemStack staff = p.getInventory().getItemInMainHand();

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(uuid)) {
            return;
        }

        if (!Objects.equals(Kit.equippedKits.get(uuid).name, name) || !staff.getType().equals(Material.EMERALD)) {
            return;
        }

        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
             staff.setAmount(staff.getAmount() - 1);

             // Heal for 25% health, or fully heal
             if (p.getHealth() + ((double) health / 4) < health) {
                 p.setHealth(p.getHealth() + ((double) health / 4));
             } else {
                 p.setHealth(health);
                 mythicMobsApi.castSkill(p,"WarlockHealthstoneEffect");
             }
        }
    }


    /**
     * @return The lore to add to the kit gui item
     */
    @Override
    public ArrayList<String> getGuiDescription() {
        ArrayList<String> kitLore = new ArrayList<>();
        kitLore.add("§7A debuff kit that can curse enemies, ");
        kitLore.add("§7sacrifice its own health to heal teammates");
        kitLore.add("§7and obliterate enemies with hellfire");
        kitLore.addAll(getBaseStats(health, regen, meleeDamage, 0));
        // TODO - Externalise values
        kitLore.add(color + "70 §7shadow bolt DMG");
        kitLore.add(color + "15dmg/s §7life-drain DMG");
        kitLore.add(color + "70dmg/s §7Hellfire DMG");
        kitLore.add(" ");
        kitLore.add("§5Effects:");
        kitLore.add("§7- Slowness II");
        kitLore.add(" ");
        kitLore.add("§aActive:");
        kitLore.add("§7- Can shoot a shadow bolt at opponents");
        kitLore.add("§7to damage them and give them slowness I");
        kitLore.add("§7- Can curse all enemies in a 7 block radius");
        kitLore.add("§7giving them negative damage for 4 seconds");
        kitLore.add("§7- Can drain life from targets for 5 seconds");
        kitLore.add("§7- Has the possibility to sacrifice 25% of their HP");
        kitLore.add("§7to heal a teammate for 25% of the warlock's max health");
        kitLore.add("§7- Can pay hell with 5 soul shards to summon devastating");
        kitLore.add("§7hellfire that incinerates all enemy players it comes across");
        kitLore.add(" ");
        kitLore.add("§2Passive ");
        kitLore.add("§7- Can see players' health");
        kitLore.add("§7- Hitting targets with shadow bolt gives soul shards");
        kitLore.add("§7and has a 10% chance to give a health stone");
        return kitLore;
    }
}
