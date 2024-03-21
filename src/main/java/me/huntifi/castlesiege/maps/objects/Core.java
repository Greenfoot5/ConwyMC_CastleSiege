package me.huntifi.castlesiege.maps.objects;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.database.UpdateStats;
import me.huntifi.castlesiege.maps.CoreMap;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.Team;
import me.huntifi.castlesiege.maps.TeamController;
import me.huntifi.conwymc.util.Messenger;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static me.huntifi.castlesiege.Main.getBarColour;
import static net.kyori.adventure.text.format.NamedTextColor.DARK_GREEN;

/**
 * A core to destroy for DTC maps
 */
public class Core implements Listener {


    public final String name;
    protected final String owners;
    public double health;
    public boolean isDestroyed = false;

    // Location Data
    protected Location spawnPoint;
    public ProtectedRegion region;
    public static final HashMap<Core, BossBar> bars = new HashMap<>();

    // Scoreboard value
    public int scoreboard;

    // Game Data
    protected boolean active;
    public List<String> materials;

    /**
     * Creates a new core
     * @param name The name of the core
     * @param team The team name that owns the core
     * @param health The starting health of the core
     */
    public Core(String name, String team, double health) {
        this.name = name;
        this.owners = team;
        this.health = health;
    }

    /**
     * @return The current owners of the flag
     */
    public String getOwners() {
        return owners;
    }

    /**
     * Gets the basic spawn point for the flag
     * @return The Spawn Point Locations
     */
    public Location getSpawnPoint() {
        return spawnPoint;
    }

    /**
     * Sets the spawn point for the flag
     * @param location The new spawn point location
     */
    public void setSpawnPoint(Location location){
        spawnPoint = location;
    }

    /**
     * Plays the capturing ping sound to a player
     * @param player The player to play the sound to
     */
    protected void playDamageSound(Player player, boolean isDestroyed) {
        Location location = player.getLocation();

        // Play wither spawn when destroyed and play explosion sound when not
        Sound effect = isDestroyed ? Sound.ENTITY_WITHER_SPAWN : Sound.ENTITY_GENERIC_EXPLODE;

        float volume = 2;

        player.playSound(location, effect, volume, 1);

    }

    /**
     * Get the core's color.
     * @return The primary chat color of the flag's owners, gray if neutral
     */
    public NamedTextColor getColor() {
        String owners = getOwners();
        Team team = MapController.getCurrentMap().getTeam(owners);
        return team.primaryChatColor;
    }

    /**
     *
     * @param core the core which the bossbar belongs to
     * @param value the amount of progress on the bossbar to display, should be the flag's progress or capture index/max caps
     */
    public void setCoreBarValue(Core core, float value) {
        bars.get(core).progress(value);
    }

    /**
     *
     * @param core the core which the bossbar belongs to
     * @param color the colour to put the bossbar to
     */
    public void setCoreBarColour(Core core, BossBar.Color color) {
        bars.get(core).color(color);
    }

    /**
     * called to register all bossbars for each core on the current map
     */
    public static void registerBossbars() {
        if (MapController.getCurrentMap() instanceof CoreMap) {
            CoreMap coreMap = (CoreMap) MapController.getCurrentMap();
            for (Core core : coreMap.getCores()) {
                createFlagBossbar(core, getBarColour(core.getColor()), BossBar.Overlay.PROGRESS, core.name, (float) core.health);
            }
        }
    }

    /**
     *
     * @param core the core to create the bossbar for
     * @param barColour the colour of the bar, should be the team colour.
     * @param barStyle the style to put the bossbar in
     * @param text this is actually just the flag name
     * @param progress the amount of progress done on the bossbar, should be (index / max caps)
     */
    public static void createFlagBossbar(Core core, BossBar.Color barColour, BossBar.Overlay barStyle, String text, float progress) {
        Main.plugin.getComponentLogger().info(Component.text(core.name + " Bossbar creation initialised", DARK_GREEN));
        BossBar bar = BossBar.bossBar(Component.text(text), progress, barColour, barStyle);
        bars.putIfAbsent(core, bar);
    }

    /**
     *
     * @param core the core which this bossbar belongs to
     * @param p the player to display the bossbar to
     */
    public void addPlayerToCoreBar(Core core, Player p) {
        if (bars.containsKey(core)) {
            bars.get(core).addViewer(p);
        }
    }

    /**
     *
     * @param b the block to check
     * @return whether the block is in the right region or not
     */
    public boolean isInRegion(Block b) {
        return region.contains(b.getX(), b.getY(), b.getZ());
    }

    /**
     *
     * @param b the block to check
     * @return whether the block is the right material or not
     */
    public boolean isCorrectMaterial(Block b) {
        for (String matter : materials) {
            if (matter.equals(b.getType().name())) {
                return true;
            }
        }
        return false;
    }

    /**
     * returns a core's health
     */
    private double getCoreHealth() {
        return health;
    }

    /**
     *
     * @param damage the damage to deal to this core
     */
    private void damageCore(double damage) {
        this.health = this.getCoreHealth() - damage;
    }

    /**
     * Handles destroying the core
     * @param event The event called when destroying a block that belongs to a core
     */
    @EventHandler (priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onDestroyCoreBlock(BlockBreakEvent event) {
        Block clickedBlock = event.getBlock();

        if (isInRegion(clickedBlock) && isCorrectMaterial(clickedBlock)) {

            if (this.getOwners().equalsIgnoreCase(TeamController.getTeam(event.getPlayer().getUniqueId()).name)) {
                Messenger.sendError("You cannot damage your own core!", event.getPlayer());
                event.setCancelled(true);
                return;
            }

            Player player = event.getPlayer();

            for (Team team : MapController.getCurrentMap().teams) {
                if (!team.hasPlayer(player.getUniqueId())) {
                    for (UUID member : team.getPlayers()) {
                        Messenger.sendActionError("Your core is under attack!", Objects.requireNonNull(Bukkit.getPlayer(member)));
                    }
                }
            }

            UpdateStats.addCaptures(player.getUniqueId(), 1);
            playDamageSound(player, false);
            damageCore(1);
            if (getCoreHealth() < 1) {
                isDestroyed = true;
                playDamageSound(player, true);

                if (MapController.getCurrentMap().hasMapEnded()) {
                    MapController.endMap();
                }
            }
        }
    }
}
