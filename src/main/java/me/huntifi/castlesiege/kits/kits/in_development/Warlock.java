package me.huntifi.castlesiege.kits.kits.in_development;

import io.lumine.mythic.bukkit.BukkitAPIHelper;
import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.database.UpdateStats;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.kits.kits.DonatorKit;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.maps.NameTag;
import me.huntifi.castlesiege.maps.TeamController;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
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

import java.util.*;

public class Warlock extends DonatorKit implements Listener {

    private static final int health = 210;
    private static final double regen = 10.5;
    private static final double meleeDamage = 20;
    private static final int curseCooldown = 320;
    private static final int staffCooldown = 80;
    private static final int lifedrainCooldown = 400;
    private static final int healthfunnelCooldown = 200;

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
                        ChatColor.YELLOW + "weakness II for 5 seconds and 70 DMG to them.",
                        ChatColor.YELLOW + "Direct hits give a soulshard and have",
                ChatColor.YELLOW + "a 10% chance to give a healthstone."), null, meleeDamage);
        // Voted Weapon
        es.votedWeapon = new Tuple<>(
                ItemCreator.weapon(new ItemStack(Material.WITHER_SKELETON_SKULL),
                        ChatColor.DARK_PURPLE + "Cursed Skull",
                        Arrays.asList("",
                                ChatColor.YELLOW + "Right click to shoot a bolt of shadows, ",
                                ChatColor.YELLOW + "which does damage to enemies and inflicts",
                                ChatColor.YELLOW + "weakness II for 5 seconds and 70 to them.",
                                ChatColor.YELLOW + "Direct hits give a soulshard and have",
                                ChatColor.YELLOW + "a 10% chance to give a healthstone.",
                                ChatColor.AQUA + "- voted: +2 damage"),
                        Collections.singletonList(new Tuple<>(Enchantment.KNOCKBACK, 0)), meleeDamage + 2),
                0);

        // 1st ability
        es.hotbar[1] = ItemCreator.item(new ItemStack(Material.POISONOUS_POTATO),
                ChatColor.LIGHT_PURPLE + "Curse of Weakness", Arrays.asList("",
                        ChatColor.YELLOW + "Right click to give weakness IV to all ",
                        ChatColor.YELLOW + "enemies in a 7 block radius of you for 8s.",
                        ChatColor.YELLOW + " ",
                        ChatColor.YELLOW + "Has a cooldown of 16 seconds."), null);

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
                        ChatColor.YELLOW + "Has a cooldown of 10 seconds."), null);

        // 3rd ability
        es.hotbar[4] = ItemCreator.item(new ItemStack(Material.BLAZE_POWDER),
                ChatColor.GOLD + "Hellfire", Arrays.asList("",
                        ChatColor.DARK_GRAY + "Requires 5 soulshards to cast.",
                        ChatColor.YELLOW + "Shoot a large burst of hellfire in",
                        ChatColor.YELLOW + "front of you, burning anything on its path.",
                        ChatColor.YELLOW + " ",
                        ChatColor.YELLOW + "Has no cooldown."), null);

        // Chestplate
        es.chest = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE),
                ChatColor.GREEN + "Warlock's robe", null, null,
                Color.fromRGB(85, 30, 127));
        ItemMeta chest = es.chest.getItemMeta();
        ArmorMeta ameta = (ArmorMeta) chest;
        assert chest != null;
        ArmorTrim trim = new ArmorTrim(TrimMaterial.EMERALD, TrimPattern.EYE);
        ((ArmorMeta) chest).setTrim(trim);
        es.chest.setItemMeta(ameta);

        // Leggings
        es.legs = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_LEGGINGS),
                ChatColor.GREEN + "Warlock's pants", null, null,
                Color.fromRGB(85, 30, 127));
        ItemMeta legs = es.legs.getItemMeta();
        ArmorMeta aleg = (ArmorMeta) legs;
        assert legs != null;
        ArmorTrim trimleg = new ArmorTrim(TrimMaterial.EMERALD, TrimPattern.DUNE);
        aleg.setTrim(trimleg);
        es.legs.setItemMeta(aleg);

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
        ArmorMeta aboot = (ArmorMeta) boots;
        assert boots != null;
        ArmorTrim boottrim = new ArmorTrim(TrimMaterial.EMERALD, TrimPattern.DUNE);
        aboot.setTrim(boottrim);
        es.feet.setItemMeta(aboot);
        es.votedFeet.setItemMeta(aboot);

        super.equipment = es;

        // Perm Potion Effect
        super.potionEffects.add(new PotionEffect(PotionEffectType.SLOW, 999999, 1, true, false));
    }


    public void shootShadowBolt(Player p) {
        BukkitAPIHelper mythicMobsApi = new BukkitAPIHelper();
        mythicMobsApi.castSkill(p ,"WarlockShadowbolt", p.getLocation());
    }


    /**
     * Activate the warlock ability of shooting a shadowbolt
     * @param e The event called when right-clicking with a wither skull
     */
    @EventHandler
    public void clickCursedSkull(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        ItemStack staff = p.getInventory().getItemInMainHand();
        int cooldown = p.getCooldown(Material.WITHER_SKELETON_SKULL);

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(uuid)) {
            return;
        }

        if (Objects.equals(Kit.equippedKits.get(uuid).name, name)) {
            if (staff.getType().equals(Material.WITHER_SKELETON_SKULL)) {
                if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (cooldown == 0) {
                        p.setCooldown(Material.WITHER_SKELETON_SKULL, staffCooldown);
                        shootShadowBolt(p);
                    }
                }
            }
        }
    }

    public void curse(Player curser, Player cursed) {

        curser.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                ChatColor.DARK_PURPLE + "You cursed the enemies around you!"));
        mythicParticle(curser);

        if (TeamController.getTeam(curser.getUniqueId()) != TeamController.getTeam(cursed.getUniqueId())
                && curser.getLocation().distance(cursed.getLocation()) <= 7 && curser != cursed) {

            //Warlock gets supports for cursing enemies
            UpdateStats.addSupports(curser.getUniqueId(), 1);

            cursed.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 160, 2, true, true));
        }
    }


    /**
     * Activate the warlock ability of cursing the area
     * @param e The event called when right-clicking with a stick
     */
    @EventHandler
    public void clickCurse(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        ItemStack patat = p.getInventory().getItemInMainHand();
        int cooldown = p.getCooldown(Material.POISONOUS_POTATO);

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(uuid)) {
            return;
        }

        if (Objects.equals(Kit.equippedKits.get(uuid).name, name)) {
            if (patat.getType().equals(Material.POISONOUS_POTATO)) {
                if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (cooldown == 0) {
                        p.setCooldown(Material.POISONOUS_POTATO, curseCooldown);
                        for (Player near : Bukkit.getOnlinePlayers()) {
                            curse(p, near);
                        }
                    }
                }
            }
        }
    }

    public void mythicParticle(Player p) {
        BukkitAPIHelper mythicMobsApi = new BukkitAPIHelper();
        mythicMobsApi.castSkill(p,"WarlockCurseEffect");
    }

    public void lifedrain(Player p) {
        BukkitAPIHelper mythicMobsApi = new BukkitAPIHelper();
        mythicMobsApi.castSkill(p ,"WarlockLifedrain", p.getLocation());
    }

    /**
     * Activate the warlock ability of lifedrain
     * @param e The event called when right-clicking with a scute
     */
    @EventHandler
    public void clickLifedrain(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        ItemStack staff = p.getInventory().getItemInMainHand();
        int cooldown = p.getCooldown(Material.SCUTE);

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(uuid)) {
            return;
        }

        if (Objects.equals(Kit.equippedKits.get(uuid).name, name)) {
            if (staff.getType().equals(Material.SCUTE)) {
                if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (cooldown == 0) {
                        p.setCooldown(Material.SCUTE, lifedrainCooldown);
                        lifedrain(p);
                    }
                }
            }
        }
    }

    /**
     * Activate the warlock ability of sacrificing health to heal a teammate
     * @param event The event called when clicking on a teammate with redstone
     */
    @EventHandler
    public void onHealthfunnel(PlayerInteractEntityEvent event) {
            Player player = event.getPlayer();
            UUID uuid = player.getUniqueId();
            ItemStack staff = player.getInventory().getItemInMainHand();
            int cooldown = player.getCooldown(Material.REDSTONE);
            
            // Prevent using in lobby
            if (InCombat.isPlayerInLobby(uuid)) {
                return;
            }

            Entity q = event.getRightClicked();
            if (Objects.equals(Kit.equippedKits.get(uuid).name, name) &&             
                    (staff.getType().equals(Material.REDSTONE)) &&
                    q instanceof Player &&                                                        
                    TeamController.getTeam(uuid) == TeamController.getTeam(q.getUniqueId()) &&      
                    ((Player) q).getHealth() < Kit.equippedKits.get(q.getUniqueId()).baseHealth) {                                               // Not on cooldown

                // Apply cooldown
                Player r = (Player) q;
                
                if (player.getHealth() < (double) Kit.equippedKits.get(player.getUniqueId()).baseHealth / 4) {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                            ChatColor.RED + "You cannot sacrifice your health cause you are too low on HP!"));
                    return;
                }


                if (cooldown == 0) {
                    r.setHealth(r.getHealth() + (double) Kit.equippedKits.get(player.getUniqueId()).baseHealth / 4);
                    player.setHealth(player.getHealth() - (double) Kit.equippedKits.get(player.getUniqueId()).baseHealth / 4);
                    r.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                            NameTag.color(player) + player.getName() + ChatColor.AQUA + " has sacrificed his health for you"));
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                            ChatColor.AQUA + "You have sacrificed your health for player: " + NameTag.color(r) + r.getName()));
                    UpdateStats.addHeals(uuid, 2);
                    player.setCooldown(Material.REDSTONE, healthfunnelCooldown);
                    mythicParticle2(r);
                }
            } else {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                        ChatColor.RED + "This player already has full health."));
            }
    }

    public void mythicParticle2(Player p) {
        BukkitAPIHelper mythicMobsApi = new BukkitAPIHelper();
        mythicMobsApi.castSkill(p,"WarlockHealthFunnelEffect");
    }


    public void hellfire(Player p) {
        BukkitAPIHelper mythicMobsApi = new BukkitAPIHelper();
        mythicMobsApi.castSkill(p ,"Hellfire", p.getLocation());
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

        if (Objects.equals(Kit.equippedKits.get(uuid).name, name)) {
            if (staff.getType().equals(Material.BLAZE_POWDER)) {
                if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (p.getInventory().contains(Material.AMETHYST_SHARD, 5)) {
                        for (ItemStack item : p.getInventory().getContents()) {
                            hellfire(p);
                            if (item == null) { return; }
                            if (item.getType().equals(Material.AMETHYST_SHARD)) {
                                item.setAmount(0);
                            }
                        }
                    } else {
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                                ChatColor.RED + "You require 5 soul shards to perform this spell!"));
                    }
                }
            }
        }
    }



    /**
     * Consume a healthstone
     * @param e The event called when right-clicking a healthstone/emerald
     */
    @EventHandler
    public void clickHealthstone(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        ItemStack staff = p.getInventory().getItemInMainHand();

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(uuid)) {
            return;
        }

        if (Objects.equals(Kit.equippedKits.get(uuid).name, name)) {
            if (staff.getType().equals(Material.EMERALD)) {
                if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                     staff.setAmount(staff.getAmount() - 1);
                     if (p.getHealth() + ((double) health /4) < health) {
                         p.setHealth(p.getHealth() + ((double) health / 4));
                     } else {
                         p.setHealth(health);
                         mythicParticle3(p);
                     }
                }
            }
        }
    }

    public void mythicParticle3(Player p) {
        BukkitAPIHelper mythicMobsApi = new BukkitAPIHelper();
        mythicMobsApi.castSkill(p,"WarlockHealthstoneEffect");
    }


    public static ArrayList<String> loreStats() {
        ArrayList<String> kitLore = new ArrayList<>();
        kitLore.add("§7A debuff kit that can curse enemies, ");
        kitLore.add("§7sacrifice its own health to heal teammates");
        kitLore.add("§7and obliterate enemies with hellfire.");
        kitLore.add(" ");
        kitLore.add("§a" + health + " §7HP");
        kitLore.add("§a" + meleeDamage + " §7Melee DMG");
        kitLore.add("§a" + regen + " §7Regen");
        kitLore.add("§a70 §7shadowbolt DMG");
        kitLore.add("§a15dmg/s §7life-drain DMG");
        kitLore.add("§a70dmg/s §7Hellfire DMG");
        kitLore.add("§ano §7ladders");
        kitLore.add("§5Effects:");
        kitLore.add("§7- Slowness II");
        kitLore.add("");
        kitLore.add("§6Abilities: ");
        kitLore.add("§7- Can shoot a shadowbolt at opponents");
        kitLore.add("§7to damage them and give them weakness II.");
        kitLore.add("§7- Can curse all enemies in a 7 block radius");
        kitLore.add("§7giving them weakness IV for 8 seconds.");
        kitLore.add("§7- Can drain life from targets for 5 seconds.");
        kitLore.add("§7- Has the possibility to sacrifice 25% of their HP");
        kitLore.add("§7to heal a teammate for 25% of the warlock's max health.");
        kitLore.add("§7- Can pay hell with 5 soulshards to summon devastating");
        kitLore.add("§7hellfire that incinerates all enemy players it comes across.");
        kitLore.add("");
        kitLore.add("§2Passive: ");
        kitLore.add("§7- Can see player health.");
        kitLore.add("§7- Hitting targets with shadowbolt gives soulshards");
        kitLore.add("§7and has a 10% chance to give a healthstone.");
        kitLore.add("");
        kitLore.add("§7Can be unlocked with §e§lcoins");
        return kitLore;
    }
}
