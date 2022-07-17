package me.huntifi.castlesiege.kits.kits;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.castlesiege.database.UpdateStats;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.WoolHat;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.NameTag;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.DisguiseConfig;
import me.libraryaddict.disguise.disguisetypes.Disguise;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Criterias;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * The abstract kit
 */
public abstract class Kit implements CommandExecutor {

    public String name;
    public int baseHealth;
    public int kbResistance = 0;
    protected double regenAmount;

    public boolean canCap;
    public boolean canClimb;
    protected boolean canSeeHealth;

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

    // Kit Tracking
    private static final Map<String, Kit> kits = new HashMap<>();

    /**
     * Create a kit with basic settings
     * @param name This kit's name
     * @param baseHealth This kit's base health
     */
    public Kit(String name, int baseHealth, double regenAmount) {
        this.name = name;
        this.baseHealth = baseHealth;
        this.regenAmount = regenAmount;

        players = new ArrayList<>();
        kits.put(getSpacelessName(), this);

        canCap = true;
        canClimb = true;
        canSeeHealth = false;

        // Equipment
        equipment = new EquipmentSet();
        potionEffects = new ArrayList<>();

        // Messages
        deathMessage = new String[]{"You were killed by ", ""};
        killMessage = new String[]{" killed ", ""};
        projectileDeathMessage = new String[]{"You were shot by ", ""};
        projectileKillMessage = new String[]{" shot ", ""};
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
        setDisguise(player);

        // Change player health display
        displayHealth(player);
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
    protected void applyPotionEffects(UUID uuid) {
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
        ActiveData.getData(uuid).setKit(getSpacelessName());
        assert player != null;
        Messenger.sendInfo("Selected Kit: " + this.name, player);

        // Kills the player if they have spawned this life, otherwise heal them
        if (!InCombat.isPlayerInLobby(uuid)) {
            player.setHealth(0);
            if (MapController.isOngoing()) {
                Messenger.sendInfo("You have committed suicide " + ChatColor.DARK_AQUA + "(+2 deaths)", player);
                UpdateStats.addDeaths(player.getUniqueId(), 1); // Note: 1 death added on player respawn
            } else {
                Messenger.sendInfo("You have committed suicide!", player);
            }
        } else {
            player.setHealth(Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue());
        }
    }

    /**
     * Sets a disguise
     */
    protected void setDisguise(Player p) {
        disguise(p, null);
    }

    /**
     * Disguises a player
     * @param p The player to (un)disguise
     * @param disguise The disguise to disguise the player as
     */
    protected void disguise(Player p, Disguise disguise) {
        if (disguise == null) {
            if (DisguiseAPI.isDisguised(p)) {
                DisguiseAPI.undisguiseToAll(p);
                NameTag.give(p);
            }
        }
        else {
            disguise.getWatcher().setCustomName(NameTag.color(p) + p.getName());
            disguise.setCustomDisguiseName(true);
            disguise.setHearSelfDisguise(true);
            disguise.setSelfDisguiseVisible(false);
            disguise.setNotifyBar(DisguiseConfig.NotifyBar.NONE);

            disguise.setEntity(p);
            disguise.startDisguise();
            NameTag.give(p);
        }
    }

    /**
     * Sets the player's ability to see people's health
     * @param p The player
     */
    private void displayHealth(Player p) {
        Scoreboard scoreboard = p.getScoreboard();
        Objective healthDisplay = scoreboard.getObjective("healthDisplay");

        if (canSeeHealth && healthDisplay == null) {
            scoreboard.registerNewObjective("healthDisplay", Criterias.HEALTH,
                    ChatColor.DARK_RED + "❤").setDisplaySlot(DisplaySlot.BELOW_NAME);
        } else if (!canSeeHealth && healthDisplay != null){
            healthDisplay.unregister();
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

    public double getRegen() {
        return regenAmount;
    }

    public double getBaseHealth() {
        return baseHealth;
    }

    /**
     * Get a kit by its name
     * @param kitName The kit's name without spaces
     * @return The corresponding kit object, null if none was found
     */
    public static Kit getKit(String kitName) {
        return kits.get(kitName);
    }

    /**
     * Get all kit names
     * @return All kit names without spaces
     */
    public static Collection<String> getKits() {
        return kits.keySet();
    }

    /**
     * Get this kit's name without spaces
     * @return The kit name without spaces
     */
    public String getSpacelessName() {
        return name.replaceAll(" ", "");
    }

    /**
     * Register the player as using this kit and set their items
     * @param sender Source of the command
     * @param command Command which was executed
     * @param s Alias of the command which was used
     * @param strings Passed command arguments
     * @return true
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (canSelect(sender))
            addPlayer(((Player) sender).getUniqueId());
        return true;
    }

    /**
     * Check if the player can select this kit
     * @param sender Source of the command
     * @return Whether the player can select this kit
     */
    protected boolean canSelect(CommandSender sender) {
        if (!(sender instanceof Player)) {
            Messenger.sendError("Console cannot select kits!", sender);
            return false;
        }

        UUID uuid = ((Player) sender).getUniqueId();
        if (MapController.isSpectator(uuid)) {
            Messenger.sendError("Spectators cannot select kits!", sender);
            return false;
        }

        return true;
    }
}
