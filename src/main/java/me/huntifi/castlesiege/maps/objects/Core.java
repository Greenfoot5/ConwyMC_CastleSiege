package me.huntifi.castlesiege.maps.objects;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.database.UpdateStats;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.Team;
import me.huntifi.castlesiege.maps.TeamController;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Core implements Listener {


    public final String name;
    protected String owners;
    public double health;
    public boolean isDestroyed = false;

    // Location Data
    protected Location spawnPoint;;
    public ProtectedRegion region;
    public static final HashMap<Flag, BossBar> bars = new HashMap<>();

    // Scoreboard value
    public int scoreboard;

    // Game Data
    protected boolean active;
    public List<String> materials;

    public Core(String name, String team, double health) {
        this.name = name;
        this.owners = team;
        this.health = health;
    }

    /**
     * Gets the message to send to the user when they spawn in
     * @return the message to send
     */
    public String getSpawnMessage() {
        Team team = MapController.getCurrentMap().getTeam(owners);
        return team.secondaryChatColor + "Spawning at:" + team.primaryChatColor + " " + name;
    }

    /**
     * @return The current owners of the flag
     */
    public String getOwners() {
        return owners;
    }

    /**
     * Gets the spawn point based on the team name
     * @param teamName The team to get the spawn point for
     * @return The Spawn Point Locations
     */
    public Location getSpawnPoint(String teamName) {
        return spawnPoint;
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

        // Play level up sound if it's fully capped, or play a xp orb pickup
        Sound effect = isDestroyed ? Sound.ENTITY_WITHER_SPAWN : Sound.ENTITY_GENERIC_EXPLODE;

        float volume = 2; //1 = 100%0
        float pitch = isDestroyed ? 3 : 1; //Float between 0.5 and 2.0

        player.playSound(location, effect, volume, pitch);

    }

    /**
     * Get the flag's color.
     * @return The primary chat color of the flag's owners, gray if neutral
     */
    public ChatColor getColor() {
        String owners = getOwners();
        Team team = MapController.getCurrentMap().getTeam(owners);
        return team.primaryChatColor;
    }

    /**
     *
     * @param flag the flag which the bossbar belongs to
     * @param value the amount of progress on the bossbar to display, should be the flag's progress or capture index/max caps
     */
    public void setFlagBarValue(Flag flag, float value) {
        bars.get(flag).progress(value);
    }

    /**
     *
     * @param flag the flag which the bossbar belongs to
     * @param color the colour to put the bossbar to
     */
    public void setFlagBarColour(Flag flag, BossBar.Color color) {
        bars.get(flag).color(color);
    }

    /**
     * called to register all bossbars for each flag on the current map
     */
    public static void registerBossbars() {
        for (Flag flag : MapController.getCurrentMap().flags) {
            createFlagBossbar(flag, Flag.getBarColour(flag), BossBar.Overlay.PROGRESS, flag.name, (float) flag.animationIndex/flag.maxCap);
        }
    }

    /**
     *
     * @param flag the flag to create the bossbar for
     * @param barColour the colour of the bar, should be the team colour.
     * @param barStyle the style to put the bossbar in
     * @param text this is actually just the flag name
     * @param progress the amount of progress done on the bossbar, should be (index / max caps)
     */
    public static void createFlagBossbar(Flag flag, BossBar.Color barColour, BossBar.Overlay barStyle, String text, float progress) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_GREEN + flag.name + " Bossbar creation initialised");
        BossBar bar = BossBar.bossBar(Component.text(text), progress, barColour, barStyle);
        bars.putIfAbsent(flag, bar);
    }

    /**
     *
     * @param flag the flag which this bossbar belongs to
     * @param p the player to display the bossbar to
     */
    public void addPlayerToFlagBar(Flag flag, Player p) {
        if (bars.containsKey(flag)) {
            bars.get(flag).addViewer((Audience) p);
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
     *
     * @param b the block to check the region for
     * @return the core which the block in the region belongs to
     */
    public Core getRelativeCore(Block b) {
        isInRegion(b);
        return this;
    }

    /**
     * returns a core's health
     */
    private double getCoreHealth() {
        return health;
    }

    /**
     *
     * @param health the health to set the core to
     */
    private void setCoreHealth(double health) {
        this.health = health;
    }

    /**
     *
     * @param damage the damage to deal to this core
     */
    private void damageCoreHealth(double damage, Core core) {
        core.setCoreHealth(core.getCoreHealth() - damage);
    }

    /**
     * Handles shooting the catapult
     * @param event The event called when pulling a lever
     */
    @EventHandler
    public void onDestroyCoreBlock(BlockBreakEvent event) {
        Block clickedBlock = event.getBlock();
        //Messenger.sendActionError("block name: " + clickedBlock.getType().name(), event.getPlayer());
        if (!isInRegion(clickedBlock) || !isCorrectMaterial(clickedBlock) ||
                this.getOwners().equalsIgnoreCase(TeamController.getTeam(event.getPlayer().getUniqueId()).name)) {
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
        damageCoreHealth(1, getRelativeCore(clickedBlock));
        if (getCoreHealth() < 1) {
            isDestroyed = true;
            playDamageSound(player, true);

            if (MapController.hasMapEnded()) {
                MapController.endMap();
            }
        }
    }
}
