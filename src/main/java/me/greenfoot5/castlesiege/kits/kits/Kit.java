package me.greenfoot5.castlesiege.kits.kits;

import me.greenfoot5.castlesiege.Main;
import me.greenfoot5.castlesiege.database.CSActiveData;
import me.greenfoot5.castlesiege.database.UpdateStats;
import me.greenfoot5.castlesiege.events.combat.InCombat;
import me.greenfoot5.castlesiege.events.curses.BindingCurse;
import me.greenfoot5.castlesiege.events.curses.CurseExpired;
import me.greenfoot5.castlesiege.events.curses.HealingCurse;
import me.greenfoot5.castlesiege.events.curses.VulnerabilityCurse;
import me.greenfoot5.castlesiege.kits.items.EquipmentSet;
import me.greenfoot5.castlesiege.kits.items.WoolHat;
import me.greenfoot5.castlesiege.maps.MapController;
import me.greenfoot5.castlesiege.maps.Team;
import me.greenfoot5.castlesiege.maps.TeamController;
import me.greenfoot5.conwymc.data_types.Tuple;
import me.greenfoot5.conwymc.events.nametag.UpdateNameTagEvent;
import me.greenfoot5.conwymc.util.Messenger;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.DisguiseConfig;
import me.libraryaddict.disguise.disguisetypes.Disguise;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The abstract kit
 */
public abstract class Kit implements CommandExecutor, Listener {

    public final String name;
    public final int baseHealth;
    public final NamedTextColor color;
    public double kbResistance;
    protected final double regenAmount;

    public boolean canCap;
    public boolean canClimb;
    protected boolean canSeeHealth;

    // Equipment
    protected EquipmentSet equipment;
    protected final ArrayList<PotionEffect> potionEffects;

    // Messages
    protected final String[] deathMessage;
    protected final String[] killMessage;
    protected final String[] projectileDeathMessage;
    protected final String[] projectileKillMessage;

    // Player Tracking
    public static final Map<UUID, Kit> equippedKits = new HashMap<>();
    public static final Map<String, Integer> kitLimits = new HashMap<>();

    // Kit Tracking
    private static final Map<String, Kit> kits = new HashMap<>();

    // GUI
    public final Material material;

    // Curses
    private static final List<UUID> activeBindings = new ArrayList<>();
    private static double healthMultiplier = 1.0;
    private static boolean vulnerable = false;

    /** The player that's equipped this instance of the kit */
    protected Player equippedPlayer;

    /**
     * Create a kit with basic settings
     * @param name This kit's name
     * @param baseHealth This kit's base health
     * @param regenAmount The amount the kit regenerates per regen tick
     * @param material The material to represent the kit in GUIs
     * @param color The chat colour for the kit
     */
    public Kit(String name, int baseHealth, double regenAmount, Material material, NamedTextColor color) {
        this.name = name;
        this.color = color;
        this.baseHealth = baseHealth;
        this.regenAmount = regenAmount;

        kits.putIfAbsent(getSpacelessName(), this);

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

        this.material = material;
    }

    /**
     * Give the items and attributes of this kit to a player
     * @param force If the kit should be applied without checks
     */
    public void setItems(boolean force) {
        Bukkit.getScheduler().runTask(Main.plugin, () -> {
            if (equippedPlayer == null)
                return;

            // The player shouldn't get the items
            if (!(force || canSelect(equippedPlayer,false, false, false))) {
                equippedPlayer.performCommand("random");
                return;
            }

            // Attributes
            setAttributes();

            // Items
            refillItems();

            // Change disguise
            setDisguise();

            // Change health display
            displayHealth();
        });
    }

    public void setAttributes() {
        // Health
        AttributeInstance healthAttribute = equippedPlayer.getAttribute(Attribute.MAX_HEALTH);
        assert healthAttribute != null;
        double maxHealth = vulnerable ? 1 : baseHealth * healthMultiplier;
        healthAttribute.setBaseValue(maxHealth);
        equippedPlayer.setHealth(maxHealth);
        equippedPlayer.setFireTicks(0);
        if (maxHealth > 200) {
            equippedPlayer.setHealthScale(maxHealth / 10.0);
        } else if (maxHealth == 1) {
            equippedPlayer.setHealthScale(0.5);
        }
        else {
            equippedPlayer.setHealthScale(20.0);
        }

        // Knockback resistance
        AttributeInstance kbAttribute = equippedPlayer.getAttribute(Attribute.KNOCKBACK_RESISTANCE);
        assert kbAttribute != null;
        kbAttribute.setBaseValue(kbResistance);
    }

    protected void resetAttributes() {
        // Knockback resistance
        AttributeInstance kbAttribute = equippedPlayer.getAttribute(Attribute.KNOCKBACK_RESISTANCE);
        assert kbAttribute != null;
        kbAttribute.setBaseValue(0);
    }

    /**
     * Reset the player's items and reapply their potion effects
     */
    public void refillItems() {
        // Equipment
        equipment.setEquipment(equippedPlayer);
        resetCooldown();

        // Wool hat
        WoolHat.setHead(equippedPlayer);

        // Menu Item -> 8Th slot is reserved for this always! (9th slot in reality)
//        MenuItem.giveMenuItem(equippedPlayer);

        // Potion effects
        removePotionEffects();
        applyPotionEffects();
    }

    /**
     * Reset all item cooldowns of items the player's hotbar
     */
    private void resetCooldown() {
        PlayerInventory inv = equippedPlayer.getInventory();
        for (int i = 0; i < 8; i++) {
            if (inv.getItem(i) != null) {
                equippedPlayer.setCooldown(Objects.requireNonNull(inv.getItem(i)).getType(), 0);
            }
        }
    }

    /**
     * Applies the kit's potion effects to the player
     */
    protected void applyPotionEffects() {
        new BukkitRunnable() {
            @Override
            public void run() {
                equippedPlayer.addPotionEffects(potionEffects);
            }
        }.runTaskLater(Main.plugin, 1);
    }

    /**
     * Removes all potion effects from a player
     */
    protected void removePotionEffects() {
        new BukkitRunnable() {
            @Override
            public void run() {
                equippedPlayer.clearActivePotionEffects();
            }
        }.runTask(Main.plugin);
    }

    /**
     * Register the player as using this kit and set their items
     * @param uuid The unique id of the player to register
     * @param shouldRespawn If the player should be respawned
     */
    public void equip(UUID uuid, boolean shouldRespawn) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
            Kit kit;
            try {
                 kit = this.getClass().getConstructor().newInstance();
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }

            kit.equippedPlayer = Bukkit.getPlayer(uuid);
            assert kit.equippedPlayer != null;

            // Remove old kit
            Kit oldKit = equippedKits.get(uuid);
            if (oldKit != null)
                oldKit.unequip();

            // Equip new kit
            equippedKits.put(uuid, kit);
            kit.setItems(true);
            CSActiveData.getData(uuid).setKit(kit.getSpacelessName());
            Messenger.sendInfo("Selected Kit: " + kit.name, kit.equippedPlayer);

            // Register Listeners for the kit
            Bukkit.getServer().getPluginManager().registerEvents(kit, Main.plugin);

            // Kills the player if they have spawned this life, otherwise heal them
            if (shouldRespawn) {
                if (!InCombat.isPlayerInLobby(uuid)) {
                    Bukkit.getScheduler().runTask(Main.plugin, () -> kit.equippedPlayer.setHealth(0));
                    if (MapController.isOngoing()) {
                        Messenger.sendInfo("You have committed suicide <dark_aqua>(+2 deaths)", kit.equippedPlayer);
                        UpdateStats.addDeaths(kit.equippedPlayer.getUniqueId(), 1); // Note: 1 death added on player respawn
                    } else {
                        Messenger.sendInfo("You have committed suicide!", kit.equippedPlayer);
                    }
                } else {
                    Bukkit.getScheduler().runTask(Main.plugin, () ->
                            kit.equippedPlayer.setHealth(Objects.requireNonNull(kit.equippedPlayer.getAttribute(Attribute.MAX_HEALTH)).getValue())
                    );
                }
            }
        });
    }

    /**
     * Removes the kit from the player
     */
    public void unequip() {

        // Remove old kit
        resetAttributes();
        removePotionEffects();

        HandlerList.unregisterAll(this);
    }

    /**
     * Sets the kit's disguise (overridable)
     * By default it removes any disguise a player had
     */
    protected void setDisguise() {
        disguise(null);
    }

    /**
     * Disguises a player
     * @param disguise The disguise to disguise the player as
     */
    protected void disguise(Disguise disguise) {
        if (disguise == null) {
            if (DisguiseAPI.isDisguised(equippedPlayer)) {
                DisguiseAPI.undisguiseToAll(equippedPlayer);
                Bukkit.getPluginManager().callEvent(new UpdateNameTagEvent(equippedPlayer));
            }
        }
        else {
            disguise.getWatcher().setCustomName(Messenger.mm.serialize(equippedPlayer.displayName()));
            disguise.setCustomDisguiseName(true);
            disguise.setHearSelfDisguise(true);
            disguise.setSelfDisguiseVisible(false);
            disguise.setNotifyBar(DisguiseConfig.NotifyBar.NONE);

            disguise.setEntity(equippedPlayer);
            disguise.startDisguise();
            Bukkit.getPluginManager().callEvent(new UpdateNameTagEvent(equippedPlayer));
        }
    }


    /**
     * Sets the player's ability to see people's health
     */
    private void displayHealth() {
        Scoreboard scoreboard = equippedPlayer.getScoreboard();
        Objective healthDisplay = scoreboard.getObjective("healthDisplay");

        if (canSeeHealth && healthDisplay == null) {
            scoreboard.registerNewObjective("healthDisplay", Criteria.HEALTH,
                    Component.text("❤").color(NamedTextColor.DARK_RED)).setDisplaySlot(DisplaySlot.BELOW_NAME);
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

    /**
     * @return The regen per tick for the kit
     */
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
            if (canSelect(sender, true, true, false))
                equip(((Player) sender).getUniqueId(), true);
        });
        return true;
    }

    /**
     * Check if the player can select this kit
     * @param sender Source of the command
     * @param applyLimit Whether to apply the kit limit in the check
     * @param verbose Whether error messages should be sent
     * @param isRandom Whether the check is done by the random command
     * @return Whether the player can select this kit
     */
    public boolean canSelect(CommandSender sender, boolean applyLimit, boolean verbose, boolean isRandom) {
        if (!(sender instanceof Player)) {
            if (verbose)
                Messenger.sendError("Console cannot select kits!", sender);
            return false;
        }

        UUID uuid = ((Player) sender).getUniqueId();
        if (TeamController.isSpectating(uuid)) {
            if (verbose)
                Messenger.sendError("Spectators cannot select kits!", sender);
            return false;
        }

        if (activeBindings.contains(null) || activeBindings.contains(uuid)) {
            if (verbose)
                Messenger.sendCurse("A curse is stopping you from changing kits!", sender);
            return false;
        }

        if (applyLimit && kitLimits.getOrDefault(getSpacelessName(), -1) >= 0 && violatesLimit(TeamController.getTeam(uuid))) {
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
     * setting a negative limit disables the limit.
     * @param limit The limit to set
     */
    public void setLimit(int limit) {
        if (limit < 0)
            kitLimits.remove(getSpacelessName());
        else
            kitLimits.put(getSpacelessName(), limit);
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

        return kitLimits.getOrDefault(getSpacelessName(), -1) <= count.get();
    }

    /**
     * Gets the GUI material for a kit
     * @param kitName The name of the kit
     * @return The material to use
     */
    public static Material getMaterial(String kitName) {
        Kit kit = getKit(kitName);
        if (kit != null) {
            return kit.material;
        }
        return null;
    }

    /**
     * @return The description to display in the kit gui
     */
    public abstract ArrayList<Component> getGuiDescription();

    /**
     * @param health The health of the kit
     * @param meleeDamage The melee damage the kit deals
     * @param regen The regen of the kit
     * @param ladders The number of ladders the kit starts with
     * @return A simple display with these stats listed
     */
    protected ArrayList<Component> getBaseStats(int health, double regen, double meleeDamage,  int ladders) {
        return getBaseStats(health, regen, meleeDamage, -1, ladders, -1);
    }

    /**
     * @param health The health of the kit
     * @param meleeDamage The melee damage the kit deals
     * @param rangedDamage The ranged damage the kit deals
     * @param regen The regen of the kit
     * @param ladders The number of ladders the kit starts with
     * @param ammo The total ammo a kit starts with
     * @return A simple display with these stats listed
     */
    protected ArrayList<Component> getBaseStats(int health, double regen, double meleeDamage, double rangedDamage,
                                                    int ladders, int ammo) {
        ArrayList<Component> baseStats = new ArrayList<>();
        baseStats.add(Component.empty());
        baseStats.add(Component.text(health, color).append(Component.text(" HP", NamedTextColor.GRAY)));
        baseStats.add(Component.text(regen, color).append(Component.text(" Regen", NamedTextColor.GRAY)));
        baseStats.add(Component.text(meleeDamage, color).append(Component.text(" Melee DMG", NamedTextColor.GRAY)));
        if (rangedDamage > 0)
            baseStats.add(Component.text(rangedDamage + "+", color).append(Component.text(" Ranged DMG", NamedTextColor.GRAY)));
        if (ladders > 0)
            baseStats.add(Component.text(ladders, color).append(Component.text(" Ladders", NamedTextColor.GRAY)));
        if (ammo > 0)
            baseStats.add(Component.text(ammo, color).append(Component.text(" Ammo", NamedTextColor.GRAY)));
        return baseStats;
    }

    /**
     * @return Text displaying cost to unlock the kit
     */
    public ArrayList<Component> getGuiCostText() {
        ArrayList<Component> text = new ArrayList<>();
        text.add(Component.empty());
        text.add(Component.text("Apparently, it's a secret...").decorate(TextDecoration.BOLD));
        return text;
    }

    public EquipmentSet getEquipment() {
        return equipment;
    }

    public ArrayList<PotionEffect> getEffects() {
        return potionEffects;
    }

    /**
     * @param curse The blinding curse that's been activated
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void bindingActive(BindingCurse curse) {
        activeBindings.add(curse.getPlayer());
    }

    /**
     * @param curse The healing curse that's been activated
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void healingActive(HealingCurse curse) {
        healthMultiplier = curse.multiplier;
    }

    /**
     * @param curse The vulnerability curse that's been activated
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void vulnerabilityActive(VulnerabilityCurse curse) {
        vulnerable = true;
    }

    /**
     * @param curse The binding curse that's been activated
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void bindingExpired(CurseExpired curse) {
        if (Objects.equals(curse.getDisplayName(), BindingCurse.name))
            activeBindings.remove(curse.getPlayer());
        if (Objects.equals(curse.getDisplayName(), HealingCurse.name))
            healthMultiplier = 1f;
        if (Objects.equals(curse.getDisplayName(), VulnerabilityCurse.name))
            vulnerable = false;
    }

    /**
     * Gets the currently equipped player
     * @return The player who's equipped the kit
     */
    public Player getEquippedPlayer() {
        return equippedPlayer;
    }

    /**
     * @param block The block to check for, if this is interactable then:
     * @return return true for interactable and return false if it is not.
     * The reason this exists is for a check to see if a right click ability should activate or not,
     * when clicking interactables.
     */
    public boolean interactableBlock(Block block) {
        //noinspection EnhancedSwitchMigration
        switch (block.getType()) {
            case OAK_BUTTON:
            case STONE_BUTTON:
            case POLISHED_BLACKSTONE_BUTTON:
            case BIRCH_BUTTON:
            case SPRUCE_BUTTON:
            case JUNGLE_BUTTON:
            case ACACIA_BUTTON:
            case DARK_OAK_BUTTON:
            case MANGROVE_BUTTON:
            case WARPED_BUTTON:
            case CRIMSON_BUTTON:
            case BAMBOO_BUTTON:
            case CHERRY_BUTTON:
            case OAK_DOOR:
            case BIRCH_DOOR:
            case SPRUCE_DOOR:
            case JUNGLE_DOOR:
            case ACACIA_DOOR:
            case DARK_OAK_DOOR:
            case MANGROVE_DOOR:
            case WARPED_DOOR:
            case CRIMSON_DOOR:
            case BAMBOO_DOOR:
            case CHERRY_DOOR:
            case OAK_TRAPDOOR:
            case BIRCH_TRAPDOOR:
            case SPRUCE_TRAPDOOR:
            case JUNGLE_TRAPDOOR:
            case ACACIA_TRAPDOOR:
            case DARK_OAK_TRAPDOOR:
            case MANGROVE_TRAPDOOR:
            case WARPED_TRAPDOOR:
            case CRIMSON_TRAPDOOR:
            case BAMBOO_TRAPDOOR:
            case CHERRY_TRAPDOOR:
            case OAK_FENCE_GATE:
            case BIRCH_FENCE_GATE:
            case SPRUCE_FENCE_GATE:
            case JUNGLE_FENCE_GATE:
            case ACACIA_FENCE_GATE:
            case DARK_OAK_FENCE_GATE:
            case MANGROVE_FENCE_GATE:
            case WARPED_FENCE_GATE:
            case CRIMSON_FENCE_GATE:
            case BAMBOO_FENCE_GATE:
            case CHERRY_FENCE_GATE:
            case CHEST:
            case LEVER:
            case CAKE:
            case ENDER_CHEST:
                return true;
            default:
                return false;
        }
    }
}
