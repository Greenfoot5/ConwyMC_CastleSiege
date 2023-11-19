package me.huntifi.castlesiege.kits.kits.in_development;

import io.lumine.mythic.bukkit.BukkitAPIHelper;
import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.kits.kits.DonatorKit;
import me.huntifi.castlesiege.kits.kits.Kit;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

public class Priest extends DonatorKit implements Listener {

    private static final int health = 210;
    private static final double regen = 8;
    private static final double meleeDamage = 25;
    private static final int ladderCount = 4;
    private static final int blessingCooldown = 500;
    private static final int staffCooldown = 40;
    private final ItemStack holybook;
    public Priest() {
        super("Priest", health, regen, Material.SPECTRAL_ARROW);

        super.canSeeHealth = true;

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Weapon
        es.hotbar[0] = ItemCreator.weapon(new ItemStack(Material.SPECTRAL_ARROW),
                ChatColor.GREEN + "Holy Staff", Arrays.asList("",
                        ChatColor.YELLOW + "Right click to shoot a bolt of light, ",
                        ChatColor.YELLOW + "which does damage to enemies."), null, meleeDamage);
        // Voted Weapon
        es.votedWeapon = new Tuple<>(
                ItemCreator.weapon(new ItemStack(Material.SPECTRAL_ARROW),
                        ChatColor.GREEN + "Holy Staff",
                        Arrays.asList("",
                                ChatColor.YELLOW + "Right click to shoot a bolt of light, ",
                                ChatColor.YELLOW + "which does damage to enemies.",
                                ChatColor.AQUA + "- voted: +2 damage"),
                        Collections.singletonList(new Tuple<>(Enchantment.DAMAGE_UNDEAD, 5)), meleeDamage + 2),
                0);

        // Chestplate
        es.chest = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE),
                ChatColor.GREEN + "Priest's robe", null, null,
                Color.fromRGB(20, 0, 24));
        ItemMeta chest = es.chest.getItemMeta();
        ArmorMeta ameta = (ArmorMeta) chest;
        assert chest != null;
        ArmorTrim trim = new ArmorTrim(TrimMaterial.GOLD, TrimPattern.VEX);
        ((ArmorMeta) chest).setTrim(trim);
        es.chest.setItemMeta(ameta);

        // Leggings
        es.legs = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_LEGGINGS),
                ChatColor.GREEN + "Priest's Pants", null, null,
                Color.fromRGB(20, 0, 24));
        ItemMeta legs = es.legs.getItemMeta();
        ArmorMeta aleg = (ArmorMeta) legs;
        assert legs != null;
        ArmorTrim trimleg = new ArmorTrim(TrimMaterial.GOLD, TrimPattern.VEX);
        aleg.setTrim(trimleg);
        es.legs.setItemMeta(aleg);

        // Boots
        es.feet = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_BOOTS),
                ChatColor.GREEN + "Priest's Boots", null, null,
                Color.fromRGB(20, 0, 24));
        // Voted Boots
        es.votedFeet = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_BOOTS),
                ChatColor.GREEN + "Priest's Boots",
                Collections.singletonList(ChatColor.AQUA + "- voted: Depth Strider II"),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)),
                Color.fromRGB(20, 0, 24));
        ItemMeta boots = es.feet.getItemMeta();
        ArmorMeta aboot = (ArmorMeta) boots;
        assert boots != null;
        ArmorTrim boottrim = new ArmorTrim(TrimMaterial.GOLD, TrimPattern.VEX);
        aboot.setTrim(boottrim);
        es.feet.setItemMeta(aboot);
        es.votedFeet.setItemMeta(aboot);

        // Gouge
        holybook = ItemCreator.weapon(new ItemStack(Material.BOOK),
                ChatColor.GOLD + "Holy Bible", Arrays.asList("",
                        ChatColor.YELLOW + "Select an ally with this holy book.",
                        ChatColor.YELLOW + "This ally will receive regeneration III",
                        ChatColor.YELLOW + "until you select a different ally.",
                        ChatColor.YELLOW + "Then the effect will wear off and go",
                        ChatColor.YELLOW + "on the different ally you chose."),
                null, 1);
        es.hotbar[1] = holybook;

        // Ladders
        es.hotbar[2] = new ItemStack(Material.LADDER, ladderCount);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, ladderCount + 2), 2);

        super.equipment = es;

        // Perm Potion Effect
        super.potionEffects.add(new PotionEffect(PotionEffectType.SLOW_DIGGING, 999999, 0, true, false));

        // Death Messages
        super.deathMessage[0] = "";
        super.deathMessage[1] = " sent you to heaven";
        super.killMessage[0] = " sent ";
        super.killMessage[1] = " to heaven";
    }

    public void shootSmiteBolt(Player p) {
        BukkitAPIHelper mythicMobsApi = new BukkitAPIHelper();
        mythicMobsApi.castSkill(p ,"PriestSmite", p.getLocation());
    }


    /**
     * Activate the spearman ability of throwing a spear
     * @param e The event called when right-clicking with a stick
     */
    @EventHandler
    public void clickHolyStaff(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        ItemStack staff = p.getInventory().getItemInMainHand();
        int cooldown = p.getCooldown(Material.SPECTRAL_ARROW);

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(uuid)) {
            return;
        }

        if (Objects.equals(Kit.equippedKits.get(uuid).name, name)) {
            if (staff.getType().equals(Material.SPECTRAL_ARROW)) {
                if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (cooldown == 0) {
                        p.setCooldown(Material.SPECTRAL_ARROW, staffCooldown);
                            shootSmiteBolt(p);
                    }
                }
            }
        }
    }
}
