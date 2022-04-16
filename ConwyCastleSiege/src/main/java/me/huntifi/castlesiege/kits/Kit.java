package me.huntifi.castlesiege.kits;

import me.huntifi.castlesiege.Deathmessages.DeathscoresAsync;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.tags.NametagsEvent;
import me.libraryaddict.disguise.DisguiseAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;

import java.util.*;

public abstract class Kit implements Listener {
    public String name;
    public int baseHealth;
    protected int kbResistance = 0;

    // Equipment
    protected EquipmentSet equipment;
    protected int heldItemSlot = 0;
    protected ArrayList<PotionEffect> potionEffects;

    // Messages
    protected String[] deathMessage;
    protected String[] killMessage;
    protected String[] projectileDeathMessage;
    protected String[] projectileKillMessage;

    // Player Tracking
    public List<UUID> players;
    public static Map<UUID, Kit> equippedKits = new HashMap<>();

    public Kit(String name, int baseHealth) {
        this.name = name;
        this.baseHealth = baseHealth;
        players = new ArrayList<>();

        // Equipment
        equipment = new EquipmentSet();
        potionEffects = new ArrayList<>();

        // Messages
        deathMessage = new String[]{"You were killed by ", ""};
        killMessage = new String[]{"You killed ", ""};
        projectileDeathMessage = new String[]{"You were shot by ", ""};
        projectileKillMessage = new String[]{"You shot ", ""};
    }

    public void setItems(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) { return; }

        // Health
        AttributeInstance healthAttribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        assert healthAttribute != null;
        healthAttribute.setBaseValue(baseHealth);
        player.setHealthScaled(true);
        player.setHealth(baseHealth);

        // Knockback resistance
        AttributeInstance kbAttribute = player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE);
        assert kbAttribute != null;
        kbAttribute.setBaseValue(kbResistance);

        // Items
        refillItems(uuid);

        // Change disguise
        disguise(player);
    }

    public void refillItems(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) { return; }

        // Equipment
        equipment.setEquipment(uuid);
        resetCooldown(uuid);

        // Wool hat
        WoolHat.setHead(player);

        // Potion effects
        applyPotionEffects(uuid);
    }

    private void resetCooldown(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) { return; }

        PlayerInventory inv = player.getInventory();
        for (int i = 0; i < 8; i++) {
            if (inv.getItem(i) != null) {
                player.setCooldown(Objects.requireNonNull(inv.getItem(i)).getType(), 0);
            }
        }
    }

    private void applyPotionEffects(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) { return; }
        player.getActivePotionEffects().clear();
        player.addPotionEffects(potionEffects);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {

        Player whoWasHit = e.getEntity();
        Player whoHit = whoWasHit.getKiller();

        if (whoHit != null) {

            if (Objects.equals(Kit.equippedKits.get(whoHit.getUniqueId()).name, name)) {

                if (Objects.requireNonNull(whoWasHit.getLastDamageCause()).getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
                    doKillMessage(whoWasHit, whoHit, projectileDeathMessage, projectileKillMessage);
                } else {
                    doKillMessage(whoWasHit, whoHit, deathMessage, killMessage);
                }
            }
        }
    }

    private void doKillMessage(Player whoWasHit, Player whoHit, String[] deathMessage, String[] killMessage) {
        DeathscoresAsync.doStats(whoHit, whoWasHit);

        whoWasHit.sendMessage(deathMessage[0] + NametagsEvent.color(whoHit) + whoHit.getName()
                + ChatColor.RESET + deathMessage[1]);
        whoHit.sendMessage(killMessage[0] + NametagsEvent.color(whoWasHit) + whoWasHit.getName()
                + ChatColor.RESET + killMessage[1] + ChatColor.GRAY + " (" + DeathscoresAsync.returnKillstreak(whoHit) + ")");
    }

    public void addPlayer(UUID uuid) {
        players.add(uuid);
        Player player = Bukkit.getPlayer(uuid);
        setItems(uuid);
        equippedKits.put(uuid, this);

        assert player != null;
        // Kills the player if they have spawned this life, otherwise heal them
        if (InCombat.hasPlayerSpawned(uuid)) {
            player.setHealth(0);
        } else {
            player.setHealth(Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue());
        }
    }

    protected ItemStack createItem(ItemStack item, String name, List<String> lore,
                                   List<Tuple<Enchantment, Integer>> enchants) {
        ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemMeta.setUnbreakable(true);
        itemMeta.setDisplayName(name);
        itemMeta.setLore(lore);
        if (enchants != null) {
            for (Tuple<Enchantment, Integer> e : enchants) {
                itemMeta.addEnchant(e.getFirst(), e.getSecond(), true);
            }
        }
        item.setItemMeta(itemMeta);
        return item;
    }

    protected ItemStack createLeatherItem(ItemStack item, String name, List<String> lore,
                                          List<Tuple<Enchantment, Integer>> enchants, Color color) {
        ItemStack leatherItem = createItem(item, name, lore, enchants);
        LeatherArmorMeta itemMeta = (LeatherArmorMeta) leatherItem.getItemMeta();
        assert itemMeta != null;
        itemMeta.setColor(color);
        leatherItem.setItemMeta(itemMeta);
        return leatherItem;
    }

    protected void disguise(Player p) {
        if (DisguiseAPI.isDisguised(p)) {
            DisguiseAPI.undisguiseToAll(p);
            NametagsEvent.GiveNametag(p);
        }
    }
}
