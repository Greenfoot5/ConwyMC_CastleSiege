package me.huntifi.castlesiege.kits.kits;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.castlesiege.database.UpdateStats;
import me.huntifi.castlesiege.events.combat.AssistKill;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.WoolHat;
import me.huntifi.castlesiege.maps.NameTag;
import me.libraryaddict.disguise.DisguiseAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

/**
 * The abstract kit
 */
public abstract class Kit {
    public String name;
    public int baseHealth;
    protected int kbResistance = 0;

    public boolean canCap;

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

    /**
     * Create a kit with basic settings
     * @param name This kit's name
     * @param baseHealth This kit's base health
     */
    public Kit(String name, int baseHealth) {
        this.name = name;
        this.baseHealth = baseHealth;
        players = new ArrayList<>();

        canCap = true;

        // Equipment
        equipment = new EquipmentSet();
        potionEffects = new ArrayList<>();

        // Messages
        deathMessage = new String[]{"You were killed by ", ""};
        killMessage = new String[]{"You killed ", ""};
        projectileDeathMessage = new String[]{"You were shot by ", ""};
        projectileKillMessage = new String[]{"You shot ", ""};
    }

    /**
     * Give the items and attributes of this kit to a player
     * @param uuid The unique id of the player to whom this kit is given
     */
    public void setItems(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) { return; }

        // Health
        AttributeInstance healthAttribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        assert healthAttribute != null;
        healthAttribute.setBaseValue(baseHealth);
        player.setHealthScaled(true);
        player.setHealth(baseHealth);
        player.setFireTicks(0);

        // Knockback resistance
        AttributeInstance kbAttribute = player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE);
        assert kbAttribute != null;
        kbAttribute.setBaseValue(kbResistance);

        // Items
        refillItems(uuid);

        // Change disguise
        disguise(player);
    }

    /**
     * Reset the player's items and reapply their potion effects
     * @param uuid The unique id of the player for whom to perform the refill
     */
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

    /**
     * Reset all item cooldowns of items the player's hotbar
     * @param uuid The unique id of the player for whom to reset the cooldowns
     */
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

    /**
     * Remove all potion effects from the player and apply this kit's potion effects
     * @param uuid The unique id of the player for whom to apply the potion effects
     */
    private void applyPotionEffects(UUID uuid) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Player player = Bukkit.getPlayer(uuid);
                if (player == null) { return; }
                player.getActivePotionEffects().clear();
                player.addPotionEffects(potionEffects);
            }
        }.runTaskLater(Main.plugin, 1);
    }

    /**
     * Register the player as using this kit and set their items
     * @param uuid The unique id of the player to register
     */
    public void addPlayer(UUID uuid) {
        players.add(uuid);
        Player player = Bukkit.getPlayer(uuid);
        setItems(uuid);
        equippedKits.put(uuid, this);
        ActiveData.getData(uuid).setKit(this.name);

        assert player != null;
        // Kills the player if they have spawned this life, otherwise heal them
        if (!InCombat.isPlayerInLobby(uuid)) {
            player.setHealth(0);
        } else {
            player.setHealth(Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue());
        }
    }

    /**
     * Remove a player's disguise and reapply their nametag color
     * @param p The player to (un)disguise
     */
    protected void disguise(Player p) {
        if (DisguiseAPI.isDisguised(p)) {
            DisguiseAPI.undisguiseToAll(p);
            NameTag.give(p);
        }
    }

    /**
     * Get the kill and death messages for a melee kill
     * @return Melee kill message, melee death message
     */
    public Tuple<String[], String[]> getMeleeMessage() {
        return new Tuple<>(killMessage, deathMessage);
    }

    /**
     * Get the kill and death messages for a projectile kill
     * @return Projectile kill message, projectile death message
     */
    public Tuple<String[], String[]> getProjectileMessage() {
        return new Tuple<>(projectileKillMessage, projectileDeathMessage);
    }
}
