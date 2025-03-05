package me.greenfoot5.castlesiege.maps.objects;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.greenfoot5.castlesiege.Main;
import me.greenfoot5.castlesiege.data_types.LocationFrame;
import me.greenfoot5.castlesiege.data_types.SchematicFrame;
import me.greenfoot5.castlesiege.database.UpdateStats;
import me.greenfoot5.castlesiege.maps.MapController;
import me.greenfoot5.castlesiege.maps.Scoreboard;
import me.greenfoot5.castlesiege.maps.Team;
import me.greenfoot5.castlesiege.maps.TeamController;
import me.greenfoot5.castlesiege.structures.SchematicSpawner;
import me.greenfoot5.conwymc.data_types.Tuple;
import me.greenfoot5.conwymc.util.Messenger;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.megavex.scoreboardlibrary.api.sidebar.component.LineDrawable;
import net.megavex.scoreboardlibrary.api.sidebar.component.SidebarComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static me.greenfoot5.castlesiege.Main.getBarColour;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;

/**
 * Stores all details and handles all flag actions.
 */
public class Flag implements SidebarComponent {
    protected final String name;

    // Location Data
    protected Location spawnPoint;
    protected final List<UUID> players;
    public ProtectedRegion region;

    // Game Data
    protected boolean active;
    protected String currentOwners;
    protected final String startingTeam;
    public final int maxCap;
    protected final int progressAmount;
    protected int progress;
    // Progresses needed per animationIndex
    protected static final int progressMultiplier = 100;
    // Multiplier for multiple people
    public static final double capMultiplier = 2;

    // Capturing data
    protected final AtomicInteger isRunning;
    public int animationIndex;

    // Animation
    public LocationFrame[] blockAnimation;
    public boolean animationAir = false;
    public HashMap<String, SchematicFrame[]> schematicAnimation;
    public boolean useSchematics = false;

    public static final HashMap<Flag, BossBar> bars = new HashMap<>();

    //The entity to create the flag's nametag
    protected ArmorStand hologram;

    //The location of the hologram
    public Location holoLoc;

    /**
     * Creates a new flag
     * @param name the name of the flag
     * @param secret whether the flag is a secret flag
     * @param startingTeam the team that controls the flag at the beginning of the game
     * @param maxCapValue the maximum blockAnimation amount
     * @param progressAmount How much progress is made by a single person
     * @param startAmount the starting blockAnimation amount
     */
    public Flag(String name, boolean secret, String startingTeam, int maxCapValue, int progressAmount, int startAmount) {
        this.name = name;
        this.active = !secret;
        this.currentOwners = startingTeam;
        this.maxCap = maxCapValue;
        animationIndex = startAmount;
        this.progressAmount = progressAmount;
        this.players = new ArrayList<>();
        progress = progressMultiplier * startAmount;
        isRunning = new AtomicInteger(0);
        this.startingTeam = currentOwners;
    }

    public String getName() {
        return name;
    }

    public Component getDisplayName() {
        return Component.text(name, getColor());
    }

    /**
     * @return The current owners of the flag
     */
    public String getCurrentOwners() {
        if (animationIndex == 0 || currentOwners == null) {
            return "neutral";
        }

        return currentOwners;
    }

    /**
     * @return The original owners of the flag
     */
    public String getStartingOwners() {
        if (startingTeam == null) {
            return "neutral";
        }

        return startingTeam;
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
     * Called when a player enters the capture zone
     * @param player the player that entered
     */
    public void playerEnter(Player player) {
        if (!TeamController.isPlaying(player)) {
            return;
        }

        players.add(player.getUniqueId());
        addPlayerToFlagBar(this, player);
        activate();
        capturing();
    }

    /**
     * Called when a player leaves the capture zone
     * @param player the player that exited
     */
    public void playerExit(Player player) {
        players.remove(player.getUniqueId());
        removePlayerFromFlagBar(this, player);
    }

    /**
     * Called when the game ends
     */
    public void clear() {
        players.clear();
        BossBar bar = bars.get(this);
        if (bar != null)
            bar.removeViewer(Bukkit.getServer());
    }

    /**
     * Find out if the flag is under attack
     * @return if the flag is under attack
     */
    public boolean underAttack() {
        if (isRunning.intValue() <= 0) {
            return false;
        }

        Tuple<Integer, Integer> counts = getPlayerCounts();

        return counts.getSecond() != 0;
    }

    /**
     * The capturing loop that runs when there's 1+ players inside the capture zone
     */
    protected synchronized void capturing() {
        // Only one loop can run at a time
        if (isRunning.get() > 0) {
            return;
        }
        isRunning.incrementAndGet();

        // Keep running as long as there are players in the area
        if (!players.isEmpty()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    // Calculate capture progress made on the flag
                    captureProgress();

                    // If we need to move to a different frame
                    if (progress / progressMultiplier != animationIndex || progress < progressMultiplier)
                        captureFlag();

                    // No more players in the zone
                    if (players.isEmpty() || isRunning.get() > 1) {
                        isRunning.decrementAndGet();
                        this.cancel();
                    }
                }
            // Run every 0.5s
            }.runTaskTimerAsynchronously(Main.plugin, 10, 10);
        } else {
            isRunning.decrementAndGet();
        }
    }

    /**
     * Called when a player makes progress capturing a flag
     */
    protected synchronized void captureProgress() {
        if (progress == 0) {
            currentOwners = getLargestTeam();
        }

        // You can't recap a flag
        if (!Objects.equals(startingTeam, currentOwners) && animationIndex == maxCap && !MapController.getCurrentMap().canRecap) {
            for (UUID uuid : players) {
                if (!Objects.equals(TeamController.getTeam(uuid).getName(), currentOwners))
                    Messenger.sendActionError("You cannot recapture flags once they've been fully captured on this map!",
                            Objects.requireNonNull(Bukkit.getPlayer(uuid)));
            }
            return;
        }

        Tuple<Integer, Integer> counts = getPlayerCounts();

        // Calculate the amount of progressed based on how many more players the defenders have than the attackers, or vice versa
        int amount;
        if (counts.getFirst() > counts.getSecond()) {
            amount = (int) (progressAmount * Math.pow(capMultiplier, counts.getFirst() - counts.getSecond() - 1));
            progress += Math.min(amount, 25);
        } else if (counts.getSecond() > counts.getFirst()) {
            amount = (int) (progressAmount * Math.pow(capMultiplier, counts.getSecond() - counts.getFirst() - 1));
            progress -= Math.min(amount, 25);
        }

        // Make sure the progress doesn't go too high or too low
        if (progress < 0) {
            progress = 0;
        } else if (progress > maxCap * progressMultiplier) {
            progress = maxCap * progressMultiplier;
        }
    }

    /**
     * Called when players make enough capture progress to trigger an blockAnimation change
     */
    protected void captureFlag() {
        // You can't recap a flag
        if (!Objects.equals(startingTeam, currentOwners) && animationIndex == maxCap && !MapController.getCurrentMap().canRecap)
            return;

        int capProgress = progress / progressMultiplier;

        // Flag has gone low enough to announce neutral, and animate neutral
        // However, the current owners still have partial ownership
        if (capProgress == 0 && animationIndex != capProgress) {
            animationIndex = 0;

            // Notify current capping players
            notifyPlayers(false);

            // Bossbar change
            setFlagBarValue(this, (float) animationIndex /maxCap);
            setFlagBarColour(this, BossBar.Color.WHITE);

            if (!Objects.equals(currentOwners, "neutral")) {
                broadcastTeam("neutral");
            }

            animate(false, "neutral");

        // Owners have capped enough to publicly take control
        } else if (capProgress == 1 && animationIndex == 0) {
            animationIndex += 1;
            broadcastTeam(currentOwners);
            notifyPlayers(true);
            setFlagBarValue(this, (float) animationIndex /maxCap);
            setFlagBarColour(this, getBarColour(this.getColor()));
            animate(true, currentOwners);
        // Players have increased the capture
        } else if (capProgress > animationIndex) {
            if (animationIndex >= maxCap) {
                return;
            }

            animationIndex += 1;
            setFlagBarValue(this, (float) animationIndex /maxCap);
            notifyPlayers(true);

            animate(true, currentOwners);

            // Players have fully captured the flag, and we should let them know
            if (animationIndex == maxCap) {
                setFlagBarValue(this, (float) animationIndex /maxCap);
                for (UUID uuid : players) {
                    Player player = Bukkit.getPlayer(uuid);
                    // Check they're a player
                    if (player != null) {
                        // Make sure they're on the capping team
                        if (TeamController.getTeam(uuid).getName().equals(currentOwners)) {
                            Messenger.sendAction("<gold>Flag fully captured! <aqua>Flag: " + name, player);
                        } else {
                            Messenger.sendActionError("Enemies have fully captured the flag!", player);
                        }
                        playCapSound(player, true);
                    }
                }
            }

        // Players have decreased the capture
        } else if (capProgress < animationIndex) {
            animationIndex -= 1;
            setFlagBarValue(this, (float) animationIndex /maxCap);

            // Notify current capping players
            notifyPlayers(false);

            animate(false, currentOwners);
        }

        if (animationIndex == maxCap && !Objects.equals(currentOwners, startingTeam) && !MapController.getCurrentMap().canRecap) {
            Messenger.broadcastInfo(Component.empty()
                    .append(getDisplayName())
                    .append(Component.text(" has been fully captured and can no longer be retaken by "))
                    .append(MapController.getCurrentMap().getTeam(startingTeam).getDisplayName()));
        }
    }

    /**
     * Notifies all players that there has been an blockAnimation change
     * Also adds players to InCombat interacted
     * @param areOwnersCapping If the current flag owners are capturing
     */
    protected void notifyPlayers(boolean areOwnersCapping) {
        // Get how many players are in the capture zone
        int count;
        if (areOwnersCapping) {
            count = getPlayerCounts().getFirst();
        } else {
            count = getPlayerCounts().getSecond();
        }

        for (UUID uuid : players) {
            Player player = Bukkit.getPlayer(uuid);
            // Check they're a player
            if (player != null) {
                // Make sure they're on the capping team
                if (TeamController.getTeam(uuid).getName().equals(currentOwners) == areOwnersCapping) {
                    Messenger.sendActionInfo("+" + count + " flag-capping point(s) <aqua>Flag: " + name, player);
                    UpdateStats.addCaptures(player.getUniqueId(), count);
                } else {
                    Messenger.sendActionError("Enemies are capturing the flag!", player);
                }
                playCapSound(player, false);
            }
        }
    }

    /**
     * Attempts to change the team holding the flag.
     * @param newTeam The new flag owners
     */
    protected void broadcastTeam(String newTeam) {
        if (!newTeam.equals("neutral"))
        {
            Team team = MapController.getCurrentMap().getTeam(newTeam);
            Messenger.broadcast(Component.text("~~~ " + newTeam + " have captured " + name + "! ~~~", team.primaryChatColor));

            //Hologram
            if (hologram == null) { return; }
            updateHologram(team.primaryChatColor);
        }
        else
        {
            Messenger.broadcast(Component.text("~~~ " + name + " has been neutralised! ~~~", NamedTextColor.GRAY));

            //Hologram
            if (hologram == null) { return; }
            updateHologram(GRAY);
        }
    }

    /**
     * Gets the counts of the players on the owners team, and opponents in the zone
     * @return A tuple of OwnerCount and OpponentCount
     */
    protected Tuple<Integer, Integer> getPlayerCounts() {
        Tuple<Integer, Integer> counts = new Tuple<>(0, 0);

        for (UUID uuid : players) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                Team team = TeamController.getTeam(uuid);
                if (team == null) {
                    playerExit(player);
                } else if (team.getName().equals(currentOwners)) {
                    counts.setFirst(counts.getFirst() + 1);
                } else {
                    counts.setSecond(counts.getSecond() + 1);
                }
            }
        }

        return counts;
    }

    /**
     * Gets the name of the team with the most players in the capture zone
     * @return A string of the largest team in the capture zone
     */
    protected String getLargestTeam() {
        HashMap<String, Integer> teamCounts = new HashMap<>();
        String largestTeam = null;

        for (UUID uuid : players) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null && TeamController.isPlaying(uuid)) {
                // Add the player to the team counts
                String team = TeamController.getTeam(uuid).getName();
                teamCounts.merge(team, 1, Integer::sum);

                // Get the largest team
                if (largestTeam == null || largestTeam.equals(team)) {
                    largestTeam = team;
                } else if (teamCounts.get(largestTeam) > teamCounts.get(team)) {
                    largestTeam = team;
                }
            }
        }

        return largestTeam;
    }

    /**
     * Plays the capturing ping sound to a player
     * @param player The player to play the sound to
     * @param fullyCapped true if the flag has been fully captured
     */
    protected void playCapSound(Player player, boolean fullyCapped) {
        Location location = player.getLocation();

        // Play level up sound if it's fully capped, or play a xp orb pickup
        Sound effect = fullyCapped ? Sound.ITEM_GOAT_HORN_SOUND_1 : Sound.ENTITY_EXPERIENCE_ORB_PICKUP;

        float volume = 1f; //1 = 100%0
        float pitch = fullyCapped ? 1f : 0.8f; //Float between 0.5 and 2.0

        player.playSound(location, effect, volume, pitch);
    }

    /**
     * Moves the flag onto the next blockAnimation frame
     * @param isCapUp If the blockAnimation index has increased
     * @param teamName The name of the team that owns the flag
     */
    protected synchronized void animate(boolean isCapUp, String teamName) {

        if (MapController.getCurrentMap().hasMapEnded()) {
            MapController.endMap();
        }

        MapController.getCurrentMap().updateWoolMaps(name);

        if (useSchematics) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    World world = spawnPoint.getWorld();
                    assert world != null;
                    SchematicFrame nextFrame = schematicAnimation.get(currentOwners)[animationIndex];
                    for (Tuple<String, Vector> schematic : nextFrame.schematics) {
                        Location loc = schematic.getSecond().toLocation(world);
                        SchematicSpawner.spawnSchematic(loc, schematic.getFirst());
                    }
                }
            }.runTask(Main.plugin);
        } else {
            new BukkitRunnable() {
                @Override
                public void run() {
                    // Gets the next frame and previous
                    // to overwrite previous blockAnimation and set new one
                    LocationFrame nextFrame = blockAnimation[animationIndex];
                    LocationFrame previousFrame;
                    if (isCapUp) {
                        previousFrame = blockAnimation[animationIndex - 1];
                    } else {
                        previousFrame = blockAnimation[animationIndex + 1];
                    }

                    // Get the team, or sets the neutralized materials
                    Team team = MapController.getCurrentMap().getTeam(teamName);
                    if (team == null) {
                        team = new Team("neutral");
                        team.primaryWool = Material.GRAY_WOOL;
                        team.secondaryWool = Material.LIGHT_GRAY_WOOL;
                    }
                    World world = spawnPoint.getWorld();
                    assert world != null;

                    LocationFrame previousLocationFrame = previousFrame;
                    // Set previous blockAnimation blocks to air
                    if (animationAir) {
                        for (Vector vector : previousLocationFrame.secondary_blocks) {
                            Location loc = vector.toLocation(world);
                            Block block = loc.getBlock();
                            block.setType(Material.AIR);
                        }

                        for (Vector vector : previousLocationFrame.primary_blocks) {
                            Location loc = vector.toLocation(world);
                            Block block = loc.getBlock();
                            block.setType(Material.AIR);
                        }
                    }

                    // Set new blocks
                    for (Vector vector : nextFrame.secondary_blocks) {
                        Location loc = vector.toLocation(world);
                        Block block = loc.getBlock();
                        block.setType(team.secondaryWool);
                    }

                    for (Vector vector : nextFrame.primary_blocks) {
                        Location loc = vector.toLocation(world);
                        Block block = loc.getBlock();
                        block.setType(team.primaryWool);
                    }

                    for (Vector vector : nextFrame.air) {
                        Location loc = vector.toLocation(world);
                        Block block = loc.getBlock();
                        block.setType(Material.AIR);
                    }
                }
            }.runTask(Main.plugin);
        }
    }

    /**
     * Activate this flag
     */
    private void activate() {
        if (!active) {
            active = true;
            createHologram();
            // We need to update the flags on the scoreboard
            Scoreboard.clearScoreboard();
        }
    }

    /**
     * Whether this flag is active
     * @return Whether the flag is active
     */
    public boolean isActive() {
        return this.active;
    }

    /**
     * Whether this flag is a static flag
     * @return Whether the flag is active
     */
    public boolean isStatic() {
        return this.progressAmount < 1;
    }

    /**
     * Get the wool block to display behind the sign on the WoolMap
     * @return The Material to set
     */
    public Material getWoolMapBlock() {
        // Flag is neutral
        if (getCurrentOwners().equals("neutral")) {
            return Material.GRAY_WOOL;
        }

        Team team = MapController.getCurrentMap().getTeam(getCurrentOwners());
        // Flag is fully captured
        if (progress / progressMultiplier == maxCap) {
            return team.primaryWool;
        }

        // Flag is not fully captured
        return team.secondaryWool;
    }

    /**
     * Creates the hologram for the flag
     */
    public void createHologram() {
        assert holoLoc.getWorld() != null;
        hologram = (ArmorStand) holoLoc.getWorld().spawnEntity(holoLoc, EntityType.ARMOR_STAND);
        hologram.setVisible(false);
        hologram.setGravity(false);
        hologram.setCollidable(false);
        hologram.setInvulnerable(true);
        hologram.setCustomNameVisible(true);
        hologram.setSmall(true);
        hologram.customName(Messenger.mm.deserialize("<b>Flag:</b> ").append(getDisplayName()));
    }

    /**
     * @param teamColor Updated the hologram for the flag
     */
    private void updateHologram(NamedTextColor teamColor) {
        hologram.customName(Messenger.mm.deserialize("<b>Flag:</b> ").append(Component.text(name, teamColor)));
        hologram.setCustomNameVisible(true);
    }

    /**
     * Get the flag's color.
     * @return The primary chat color of the flag's owners, gray if neutral
     */
    private NamedTextColor getColor() {
        String currentOwners = getCurrentOwners();
        if (currentOwners.equals("neutral"))
            return GRAY;

        Team team = MapController.getCurrentMap().getTeam(currentOwners);
        return team.primaryChatColor;
    }

    /**
     * @param flag      the flag to create the bossbar for
     * @param barColour the colour of the bar, should be the team colour.
     * @param text      this is actually just the flag name
     * @param progress  the amount of progress done on the bossbar, should be (index / max caps)
     */
    private static void createFlagBossbar(Flag flag, BossBar.Color barColour, String text, float progress) {
        BossBar bar = BossBar.bossBar(Component.text(flag.getName()), progress, barColour, BossBar.Overlay.PROGRESS);
        bars.putIfAbsent(flag, bar);
    }

    /**
     *
     * @param flag the flag which this bossbar belongs to
     * @param p the player to display the bossbar to
     */
    private void addPlayerToFlagBar(Flag flag, Player p) {
        if (bars.containsKey(flag)) {
            bars.get(flag).addViewer(p);
        }
    }

    /**
     *
     * @param flag the flag which this bossbar belongs to
     * @param p the player to remove the bossbar from
     */
    private void removePlayerFromFlagBar(Flag flag, Player p) {
        if (bars.containsKey(flag)) {
            bars.get(flag).removeViewer(p);
        }
    }

    /**
     *
     * @param flag the flag which the bossbar belongs to
     * @param value the amount of progress on the bossbar to display, should be the flag's progress or capture index/max caps
     */
    private void setFlagBarValue(Flag flag, float value) {
        bars.get(flag).progress(value);
    }

    /**
     *
     * @param flag the flag which the bossbar belongs to
     * @param color the colour to put the bossbar to
     */
    private void setFlagBarColour(Flag flag, BossBar.Color color) {
        bars.get(flag).color(color);
    }

    /**
     * called to register all bossbars for each flag on the current map
     */
    public static void registerBossbars() {
        for (Flag flag : MapController.getCurrentMap().flags) {
            createFlagBossbar(flag, getBarColour(flag.getColor()), flag.name, (float) flag.animationIndex/flag.maxCap);
        }
    }

    public boolean canCapture(@Nullable Team team) {
        if (progressAmount == 0)
            return false;

        // Prevent recaps if disabled
        if (animationIndex == maxCap && MapController.getCurrentMap().canRecap) {
            return Objects.equals(currentOwners, startingTeam);
        }

        return true;
    }

    public String getIcon() {
        // Get a non-owner team, it doesn't matter which one
        Team attackers = Objects.equals(MapController.getCurrentMap().teams[0].getName(), currentOwners) ? MapController.getCurrentMap().teams[1] : MapController.getCurrentMap().teams[0];

        if (!canCapture(attackers)) {
            return " \uD83D\uDD12";
        }
        // return "⓪";
        return "";
    }


    @Override
    public void draw(@NotNull LineDrawable lineDrawable) {
        Team owners = MapController.getCurrentMap().getTeam(getCurrentOwners());
        lineDrawable.drawLine(Component.text(name,
                        owners == null ? NamedTextColor.GRAY : owners.primaryChatColor)
                .append(Component.text(getIcon())));
    }
}
