package me.huntifi.castlesiege.kits.kits.in_development;

import io.lumine.mythic.bukkit.BukkitAPIHelper;
import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.kits.kits.DonatorKit;
import me.huntifi.castlesiege.kits.kits.Kit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class Sorcerer extends DonatorKit implements Listener {

    private static final int health = 230;
    private static final double regen = 10.5;
    private static final double meleeDamage = 26;
    private static final int ladderCount = 4;
    private static final int arcaneboltCooldown = 60;
    private static final int frostnovaCooldown = 220;
    private static final int arcaneBarrageCooldown = 400;
    private static final int slowFallingCooldown = 300;
    public Sorcerer() {
        super("Sorcerer", health, regen, Material.BOOK);

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Weapon
        es.hotbar[0] = ItemCreator.weapon(new ItemStack(Material.STICK),
                ChatColor.DARK_PURPLE + "Sorcerer's Wand", Arrays.asList("",
                        ChatColor.YELLOW + "Right click to shoot an arcanebolt which",
                        ChatColor.YELLOW + "does 60 DMG on hit.",
                        ChatColor.YELLOW + " ",
                        ChatColor.YELLOW + "Has a cooldown of " + arcaneboltCooldown/20 + " seconds."), null, meleeDamage);
        // Voted Weapon
        es.votedWeapon = new Tuple<>(
                ItemCreator.weapon(new ItemStack(Material.STICK),
                        ChatColor.DARK_PURPLE + "Sorcerer's Wand",
                        Arrays.asList("",
                                ChatColor.YELLOW + "Right click to shoot an arcane bolt which",
                                ChatColor.YELLOW + "does 60 DMG on hit.",
                                ChatColor.YELLOW + " ",
                                ChatColor.YELLOW + "Has a cooldown of " + arcaneboltCooldown/20 + " seconds.",
                                ChatColor.AQUA + "- voted: +2 damage"),
                        Collections.singletonList(new Tuple<>(Enchantment.KNOCKBACK, 0)), meleeDamage + 2),
                0);

        // 1st ability
        es.hotbar[1] = ItemCreator.item(new ItemStack(Material.DIAMOND),
                ChatColor.BLUE + "Frost nova", Arrays.asList("",
                        ChatColor.YELLOW + "Right click to send a freezing shockwave out ",
                        ChatColor.YELLOW + "of yourself, slowing every enemy in a 4 block radius",
                        ChatColor.YELLOW + "around you and dealing 30 DMG to them.",
                        ChatColor.YELLOW + " ",
                        ChatColor.YELLOW + "Has a cooldown of " + frostnovaCooldown/20 + " seconds."), null);

        // 2nd ability
        es.hotbar[2] = ItemCreator.item(new ItemStack(Material.AMETHYST_SHARD),
                ChatColor.LIGHT_PURPLE + "Arcane Barrage", Arrays.asList("",
                        ChatColor.YELLOW + "Fire 3 arcane bolts at a target.",
                        ChatColor.YELLOW + " ",
                        ChatColor.YELLOW + "Has a cooldown of " + arcaneBarrageCooldown/20 + " seconds."), null);

        // 3rd ability
        es.hotbar[3] = ItemCreator.item(new ItemStack(Material.FEATHER),
                ChatColor.DARK_BLUE + "Slow Falling", Arrays.asList("",
                        ChatColor.YELLOW + "Right click to give yourself slow falling",
                        ChatColor.YELLOW + "for 5 seconds.",
                        ChatColor.YELLOW + " ",
                        ChatColor.YELLOW + "Has a cooldown of " + slowFallingCooldown/20 + " seconds."), null);

        // Chestplate
        es.chest = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE),
                ChatColor.GREEN + "Sorcerer's robe", null, null,
                Color.fromRGB(75, 60, 63));
        ItemMeta chest = es.chest.getItemMeta();
        ArmorMeta ameta = (ArmorMeta) chest;
        assert chest != null;
        ArmorTrim trim = new ArmorTrim(TrimMaterial.NETHERITE, TrimPattern.DUNE);
        ((ArmorMeta) chest).setTrim(trim);
        es.chest.setItemMeta(ameta);

        // Leggings
        es.legs = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_LEGGINGS),
                ChatColor.GREEN + "Sorcerer's pants", null, null,
                Color.fromRGB(106, 62, 156));
        ItemMeta legs = es.legs.getItemMeta();
        ArmorMeta aleg = (ArmorMeta) legs;
        assert legs != null;
        ArmorTrim trimleg = new ArmorTrim(TrimMaterial.NETHERITE, TrimPattern.TIDE);
        aleg.setTrim(trimleg);
        es.legs.setItemMeta(aleg);

        // Boots
        es.feet = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_BOOTS),
                ChatColor.GREEN + "Sorcerer's Boots", null, null,
                Color.fromRGB(106, 62, 156));
        // Voted Boots
        es.votedFeet = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_BOOTS),
                ChatColor.GREEN + "Sorcerer's Boots",
                Collections.singletonList(ChatColor.AQUA + "- voted: Depth Strider II"),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)),
                Color.fromRGB(106, 62, 156));
        ItemMeta boots = es.feet.getItemMeta();
        ArmorMeta aboot = (ArmorMeta) boots;
        assert boots != null;
        ArmorTrim boottrim = new ArmorTrim(TrimMaterial.NETHERITE, TrimPattern.TIDE);
        aboot.setTrim(boottrim);
        es.feet.setItemMeta(aboot);
        es.votedFeet.setItemMeta(aboot);

        // Ladders
        es.hotbar[4] = new ItemStack(Material.LADDER, ladderCount);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, ladderCount + 2), 4);

        super.equipment = es;

        // Perm Potion Effect
        super.potionEffects.add(new PotionEffect(PotionEffectType.SLOW, 999999, 0, true, false));
    }

    public void shootArcanebolt(Player p) {
        BukkitAPIHelper mythicMobsApi = new BukkitAPIHelper();
        mythicMobsApi.castSkill(p ,"SorcererArcanebolt", p.getLocation());
    }


    /**
     * Activate the warlock ability of shooting a shadowbolt
     * @param e The event called when right-clicking with a wither skull
     */
    @EventHandler
    public void clickStaff(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        ItemStack staff = p.getInventory().getItemInMainHand();
        int cooldown = p.getCooldown(Material.STICK);

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(uuid)) {
            return;
        }

        if (Objects.equals(Kit.equippedKits.get(uuid).name, name)) {
            if (staff.getType().equals(Material.STICK)) {
                if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (cooldown == 0) {
                        p.setCooldown(Material.STICK, arcaneboltCooldown);
                        shootArcanebolt(p);
                    }
                }
            }
        }
    }

    public void frostnova(Player p) {
        BukkitAPIHelper mythicMobsApi = new BukkitAPIHelper();
        mythicMobsApi.castSkill(p ,"SorcererFrostnova", p.getLocation());
    }


    /**
     * Activate the warlock ability of cursing the area
     * @param e The event called when right-clicking with a stick
     */
    @EventHandler
    public void clickFrostnova(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        ItemStack patat = p.getInventory().getItemInMainHand();
        int cooldown = p.getCooldown(Material.DIAMOND);

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(uuid)) {
            return;
        }

        if (Objects.equals(Kit.equippedKits.get(uuid).name, name)) {
            if (patat.getType().equals(Material.DIAMOND)) {
                if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (cooldown == 0) {
                        p.setCooldown(Material.DIAMOND, frostnovaCooldown);
                        frostnova(p);
                    }
                }
            }
        }
    }


    public void shootArcanebarrage(Player p) {
        BukkitAPIHelper mythicMobsApi = new BukkitAPIHelper();
        mythicMobsApi.castSkill(p ,"SorcererArcaneboltBarrage", p.getLocation());
    }


    /**
     * Activate the warlock ability of shooting a shadowbolt
     * @param e The event called when right-clicking with a wither skull
     */
    @EventHandler
    public void clickBarrage(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        ItemStack staff = p.getInventory().getItemInMainHand();
        int cooldown = p.getCooldown(Material.AMETHYST_SHARD);

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(uuid)) {
            return;
        }

        if (Objects.equals(Kit.equippedKits.get(uuid).name, name)) {
            if (staff.getType().equals(Material.AMETHYST_SHARD)) {
                if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (cooldown == 0) {
                        p.setCooldown(Material.AMETHYST_SHARD, arcaneBarrageCooldown);
                        shootArcanebarrage(p);
                    }
                }
            }
        }
    }


    public void slowfalling(Player p) {
        BukkitAPIHelper mythicMobsApi = new BukkitAPIHelper();
        mythicMobsApi.castSkill(p ,"SorcererSlowfalling", p.getLocation());
    }


    /**
     * Activate the warlock ability of shooting a shadowbolt
     * @param e The event called when right-clicking with a wither skull
     */
    @EventHandler
    public void clickSlowfalling(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        ItemStack staff = p.getInventory().getItemInMainHand();
        int cooldown = p.getCooldown(Material.FEATHER);

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(uuid)) {
            return;
        }

        if (Objects.equals(Kit.equippedKits.get(uuid).name, name)) {
            if (staff.getType().equals(Material.FEATHER)) {
                if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (cooldown == 0) {
                        p.setCooldown(Material.FEATHER, slowFallingCooldown);
                        slowfalling(p);
                    }
                }
            }
        }
    }


    /**
     * @return The lore to add to the kit gui item
     */
    public static ArrayList<String> getGuiDescription() {
        ArrayList<String> kitLore = new ArrayList<>();
        kitLore.add("§7A sorcerer specialised in the arcane arts, ");
        kitLore.add("§7can also slow down enemies with ice");
        kitLore.add(" ");
        kitLore.add("§a" + health + " §7HP");
        kitLore.add("§a" + meleeDamage + " §7Melee DMG");
        kitLore.add("§a" + regen + " §7Regen");
        kitLore.add("§a60 §7arcane-bolt DMG");
        kitLore.add("§a30 §7frost nova DMG");
        kitLore.add("§a" + ladderCount + " §7ladders");
        kitLore.add("§5Effects:");
        kitLore.add("§7- Slowness I");
        kitLore.add("");
        kitLore.add("§6Abilities: ");
        kitLore.add("§7- Can shoot an arcane-bolt at opponents at a");
        kitLore.add("§7higher speed then other magic ranged attacks");
        kitLore.add("§7- In a wave of cold can damage and slow down enemies");
        kitLore.add("§7in a 4 block radius");
        kitLore.add("§7- Can shoot a barrage of arcane-bolts");
        kitLore.add("§7- Has a spell that gives slow falling to the caster");
        kitLore.add("");
        kitLore.add("§7Can be unlocked with §e§lcoins");
        return kitLore;
    }

}
