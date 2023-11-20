package me.huntifi.castlesiege.kits.kits.donator_kits;

import io.lumine.mythic.bukkit.BukkitAPIHelper;
import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.database.UpdateStats;
import me.huntifi.castlesiege.events.EnderchestEvent;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.kits.kits.DonatorKit;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.maps.TeamController;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Priest extends DonatorKit implements Listener {

    private static final int health = 210;
    private static final double regen = 8;
    private static final double meleeDamage = 25;
    private static final int ladderCount = 4;
    private static final int blessingCooldown = 500;
    private static final int staffCooldown = 40;
    private final ItemStack holybook;
    public static final HashMap<Player, UUID> blessings = new HashMap<>();

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


    /**
     * Activate the priest holy blessing ability to buff 1 target.
     * @param e The event called when right-clicking with a book
     */
    @EventHandler
    public void clickBible(PlayerInteractEntityEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        ItemStack book = p.getInventory().getItemInMainHand();
        int cooldown = p.getCooldown(Material.BOOK);

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(uuid)) {
            return;
        }

        if (Objects.equals(Kit.equippedKits.get(uuid).name, name)) {
            if (book.getType().equals(Material.BOOK)) {
                if (e.getRightClicked() instanceof Player &&
                        TeamController.getTeam(e.getRightClicked().getUniqueId()) == TeamController.getTeam(p.getUniqueId())) {
                    if (cooldown == 0) {
                        p.setCooldown(Material.BOOK, blessingCooldown);
                        blessings.put(p, e.getRightClicked().getUniqueId());
                        assignBook(p, book);

                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    if (blessings.containsKey(p)) {
                                        Objects.requireNonNull(Bukkit.getPlayer(blessings.get(p))).addPotionEffect((new PotionEffect(PotionEffectType.REGENERATION, 200, 3)));

                                        AttributeInstance healthAttribute = Objects.requireNonNull(Bukkit.getPlayer(blessings.get(p))).getAttribute(Attribute.GENERIC_MAX_HEALTH);
                                        assert healthAttribute != null;
                                        //Priest doesn't get a heal for blessing someone who is full health.
                                        if (Objects.requireNonNull(Bukkit.getPlayer(blessings.get(p))).getHealth() != healthAttribute.getBaseValue()) {
                                            UpdateStats.addHeals(p.getUniqueId(), 1);
                                        }
                                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                                                ChatColor.AQUA + "Your blessing is currently affecting: " + Objects.requireNonNull(Bukkit.getPlayer(blessings.get(p))).getName()));
                                    } else {
                                        this.cancel();
                                    }
                                }
                            }.runTaskTimer(Main.plugin, 10, 200);

                    }
                }
            }

        }
    }

    @EventHandler
    public void onClickEnderchest(EnderchestEvent event) {
        if (blessings.containsKey(event.getPlayer())) {
            assignBook(event.getPlayer(), holybook);
            event.getPlayer().getInventory().setItem(1, holybook);
        }
    }


    public void assignBook(Player priest, ItemStack book) {
        if (blessings.containsKey(priest)) {
            ItemMeta metatron = book.getItemMeta();
            assert metatron != null;
            metatron.setDisplayName(Objects.requireNonNull(holybook.getItemMeta()).getDisplayName() + " : " +
                    ChatColor.AQUA + Objects.requireNonNull(Bukkit.getPlayer(blessings.get(priest))).getName());
            book.setItemMeta(metatron);
        }
    }

    /**
     *
     * @param e if a player dies remove them from the blessings list.
     */
    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
            blessings.remove(e.getPlayer());
    }

    /**
     *
     * @param e if a player quits remove them from the blessings list.
     */
    @EventHandler
    public void onDisconnect(PlayerQuitEvent e) {
        blessings.remove(e.getPlayer());
    }

    public static ArrayList<String> loreStats() {
        ArrayList<String> kitLore = new ArrayList<>();
        kitLore.add("§7A support kit with the power of");
        kitLore.add("§7the holy book on their side.");
        kitLore.add(" ");
        kitLore.add("§a" + health + " §7HP");
        kitLore.add("§a" + meleeDamage + " §7Melee DMG");
        kitLore.add("§a" + regen + " §7Regen");
        kitLore.add("§a50 §7lightbolt DMG");
        kitLore.add("§a" + ladderCount + " §7Potions");
        kitLore.add("§5Effects:");
        kitLore.add("§7- Mining Fatigue I");
        kitLore.add("");
        kitLore.add("§6Abilities: ");
        kitLore.add("§7- Can shoot a bolt of light at opponents");
        kitLore.add("§7to damage them.");
        kitLore.add("§7- With the holy bible can select an ally");
        kitLore.add("§7to give them and them only regen IV for");
        kitLore.add("§7as long as they are selected.");
        kitLore.add("");
        kitLore.add("§2Passive: ");
        kitLore.add("§7- Can see player health.");
        kitLore.add("");
        kitLore.add("§7Can be unlocked with §e§lcoins");
        return kitLore;
    }
}
