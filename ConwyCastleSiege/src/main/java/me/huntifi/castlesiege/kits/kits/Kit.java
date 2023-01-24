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
import me.huntifi.castlesiege.maps.Team;
import me.huntifi.castlesiege.maps.TeamController;
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
import java.util.concurrent.atomic.AtomicInteger;

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
    private int limit = -1;

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
        Bukkit.getScheduler().runTask(Main.plugin, () -> {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) {
                return;
            }

            // Health
            AttributeInstance healthAttribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            assert healthAttribute != null;
            healthAttribute.setBaseValue(baseHealth);
            player.setHealth(baseHealth);
            player.setFireTicks(0);
            if (baseHealth > 200) {
                player.setHealthScale(baseHealth / 10.0);
            } else {
                player.setHealthScale(20.0);
            }

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
        });
    }

    /**
     * Reset the player's items and reapply their potion effects
     * @param uuid The unique id of the player for whom to perform the refill
     */
    public void refillItems(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null)
            return;

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
        Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
            Player player = Bukkit.getPlayer(uuid);
            assert player != null;
            players.add(uuid);
            equippedKits.put(uuid, this);
            setItems(uuid);
            ActiveData.getData(uuid).setKit(getSpacelessName());
            Messenger.sendInfo("Selected Kit: " + this.name, player);

            // Kills the player if they have spawned this life, otherwise heal them
            if (!InCombat.isPlayerInLobby(uuid)) {
                Bukkit.getScheduler().runTask(Main.plugin, () -> player.setHealth(0));
                if (MapController.isOngoing()) {
                    Messenger.sendInfo("You have committed suicide " + ChatColor.DARK_AQUA + "(+2 deaths)", player);
                    UpdateStats.addDeaths(player.getUniqueId(), 1, true); // Note: 1 death added on player respawn
                    if (Kit.equippedKits.get(player.getUniqueId()) instanceof DonatorKit) {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                ActiveData.getData(uuid).setKit(getSpacelessName());
                            }
                        }.runTaskLater(Main.plugin, 20);
                    }
                } else {
                    Messenger.sendInfo("You have committed suicide!", player);
                }
            } else {
                Bukkit.getScheduler().runTask(Main.plugin, () ->
                    player.setHealth(Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue())
                );
            }
        });
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
            scoreboard.clearSlot(DisplaySlot.BELOW_NAME);
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
        Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
            if (canSelect(sender, true, false))
                addPlayer(((Player) sender).getUniqueId());
        });
        return true;
    }

    /**
     * Check if the player can select this kit
     * @param sender Source of the command
     * @param verbose Whether error messages should be sent
     * @param isRandom Whether the check is done by the random command
     * @return Whether the player can select this kit
     */
    public boolean canSelect(CommandSender sender, boolean verbose, boolean isRandom) {
        if (!(sender instanceof Player)) {
            if (verbose)
                Messenger.sendError("Console cannot select kits!", sender);
            return false;
        }

        UUID uuid = ((Player) sender).getUniqueId();
        if (MapController.isSpectator(uuid)) {
            if (verbose)
                Messenger.sendError("Spectators cannot select kits!", sender);
            return false;
        }

        if (limit >= 0 && violatesLimit(TeamController.getTeam(uuid))) {
            if (verbose)
                Messenger.sendError("Could not select " + this.name + " as its limit has been reached!", sender);
            return false;
        }

        if (MapController.forcedRandom && !isRandom) {
            if (verbose)
                Messenger.sendError("Could not select " + this.name + " as forced random is enabled!", sender);
            return false;
        }

        return true;
    }

    /**
     * Set a limit to the amount of players that can use this kit at the same time.
     * Setting a negative limit disables the limit.
     * @param limit The limit to set
     */
    public void setLimit(int limit) {
        this.limit = limit;
    }

    /**
     * Check whether choosing this kit violates the limit
     * @param team The team the player is in
     * @return Whether the limit would be violated
     */
    private boolean violatesLimit(Team team) {
        AtomicInteger count = new AtomicInteger();
        Kit.equippedKits.forEach((uuid, kit) -> {
            if (Objects.equals(kit, this) && Objects.equals(TeamController.getTeam(uuid), team))
                count.getAndIncrement();
        });

        return limit <= count.get();
    }
}
