package me.huntifi.castlesiege.maps.objects;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.Frame;
import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.database.UpdateStats;
import me.huntifi.castlesiege.maps.Gamemode;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.Team;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Stores all details and handles all flag actions.
 */
public class Flag {
    public String name;

    // Location Data
    public Location spawnPoint;
    private final List<UUID> players;
    public ProtectedRegion region;

    // Game Data
    private String currentOwners;
    private final int maxCap;
    private final int progressAmount;
    private int progress;
    // Progresses needed per animationIndex
    private final static int progressMultiplier = 100;
    // Multiplier for multiple people
    public final static double capMultiplier = 1.5;

    // Capturing data
    private final AtomicInteger isRunning;
    public int animationIndex;

    // Animation
    public Frame[] animation;
    public boolean animationAir = false;

    // Scoreboard value
    public int scoreboard;

    // Charge Data
    private final String startingTeam;

    /**
     * Creates a new flag
     * @param name the name of the flag
     * @param startingTeam the team that controls the flag at the beginning of the game
     * @param maxCapValue the maximum animation amount
     * @param progressAmount How much progress is made by a single person
     */
    public Flag(String name, String startingTeam, int maxCapValue, int progressAmount) {
        this.name = name;
        this.currentOwners = startingTeam;
        this.startingTeam = startingTeam;
        this.maxCap = maxCapValue;
        animationIndex = maxCapValue;
        this.progressAmount = progressAmount;
        this.players = new ArrayList<>();
        progress = progressMultiplier * maxCapValue;
        isRunning = new AtomicInteger(0);
    }

    /**
     * Creates a new flag with a different start_amount
     * @param name the name of the flag
     * @param startingTeam the team that controls the flag at the beginning of the game
     * @param maxCapValue the maximum animation amount
     * @param progressAmount How much progress is made by a single person
     */
    public Flag(String name, String startingTeam, int maxCapValue, int progressAmount, int startAmount) {
        this.name = name;
        this.currentOwners = startingTeam;
        this.startingTeam = startingTeam;
        this.maxCap = maxCapValue;
        animationIndex = startAmount;
        this.progressAmount = progressAmount;
        this.players = new ArrayList<>();
        progress = progressMultiplier * startAmount;
        isRunning = new AtomicInteger(0);
    }

    /**
     * Gets the message to send to the user when they spawn in
     * @return the message to send
     */
    public String getSpawnMessage() {
        Team team = MapController.getCurrentMap().getTeam(currentOwners);
        return team.secondaryChatColor + "Spawning at:" + team.primaryChatColor + " " + name;
    }

    public String getCurrentOwners() {
        if (animationIndex == 0) {
            return null;
        }

        return currentOwners;
    }

    /**
     * Called when a player enters the capture zone
     * @param player the player that entered
     */
    public void playerEnter(Player player) {
        players.add(player.getUniqueId());
        capturing();
    }

    /**
     * Called when a player leaves the capture zone
     * @param player the player that exited
     */
    public void playerExit(Player player) {
        players.remove(player.getUniqueId());
    }

    /**
     * Called when the game ends
     */
    public void clear() {
        players.clear();
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

        return counts.getFirst() < counts.getSecond();
    }

    /**
     * The capturing loop that runs when there's 1+ players inside the capture zone
     */
    private synchronized void capturing() {
        // Only one loop can run at a time
        if (isRunning.get() > 0) {
            return;
        }
        isRunning.incrementAndGet();

        // Keep running as long as there are players in the area
        if (players.size() > 0) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    // Calculate capture progress made on the flag
                    captureProgress();

                    // If we need to move to a different frame
                    if (progress / progressMultiplier != animationIndex || progress < progressMultiplier)
                        captureFlag();

                    // No more players in the zone
                    if (players.size() <= 0 || isRunning.get() > 1) {
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
    private synchronized void captureProgress() {
        if (progress == 0) {
            currentOwners = getLargestTeam();
        }

        // If the game mode is Charge, you can't recap a flag
        if (MapController.getCurrentMap().gamemode.equals(Gamemode.Charge)) {
            if (!Objects.equals(startingTeam, currentOwners)) {
                if (!Objects.equals(currentOwners, getLargestTeam()))
                    return;
            }
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
     * Called when players make enough capture progress to trigger an animation change
     */
    private void captureFlag() {
        int capProgress = progress / progressMultiplier;

        // Flag has gone low enough to announce neutral, and animate neutral
        // However, the current owners still have partial ownership
        if (capProgress == 0 && animationIndex != capProgress) {
            animationIndex = 0;

            // Notify current capping players
            notifyPlayers(false);

            broadcastTeam(null);

            animate(false, null);

        // Owners have capped enough to publicly take control
        } else if (capProgress == 1 && animationIndex == 0) {
            broadcastTeam(currentOwners);
            animationIndex += 1;

            notifyPlayers(true);

            animate(true, currentOwners);

        // Players have increased the capture
        } else if (capProgress > animationIndex) {
            if (animationIndex >= maxCap) {
                return;
            }

            animationIndex += 1;
            notifyPlayers(true);

            animate(true, currentOwners);

            // Players have fully captured the flag, and we should let them know
            if (animationIndex == maxCap) {
                for (UUID uuid : players) {
                    Player player = Bukkit.getPlayer(uuid);
                    // Check they're a player
                    if (player != null) {
                        // Make sure they're on the capping team
                        if (MapController.getCurrentMap().getTeam(uuid).name.equals(currentOwners)) {
                            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "Flag fully captured!" + ChatColor.AQUA + " Flag: " + name));
                        } else {
                            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_RED + "Enemies have fully captured the flag!"));
                        }
                        playCapSound(player);
                    }
                }
            }

        // Players have decreased the capture
        } else if (capProgress < animationIndex) {
            animationIndex -= 1;

            // Notify current capping players
            notifyPlayers(false);

            animate(false, currentOwners);
        }
    }

    /**
     * Notifies all players that there has been an animation change
     * Also adds players to InCombat interacted
     * @param areOwnersCapping If the current flag owners are capturing
     */
    private void notifyPlayers(boolean areOwnersCapping) {
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
                if (MapController.getCurrentMap().getTeam(uuid).name.equals(currentOwners) == areOwnersCapping) {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_AQUA + "+" + count + " flag-capping point(s)" + ChatColor.AQUA + " Flag: " + name));
                    UpdateStats.addCaptures(player.getUniqueId(), count);
                } else {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_RED + "Enemies are capturing the flag!"));
                }
                playCapSound(player);
            }
        }
    }

    /**
     * Attempts to change the team holding the flag.
     * @param newTeam The new flag owners
     */
    private void broadcastTeam(String newTeam) {
        if (newTeam != null)
        {
            Team team = MapController.getCurrentMap().getTeam(newTeam);
            Bukkit.broadcastMessage(team.primaryChatColor + "~~~ " + newTeam + " has captured " + name + "! ~~~");
        }
        else if (currentOwners != null)
        {
            Bukkit.broadcastMessage(ChatColor.GRAY + "~~~ " + name + " has been neutralised! ~~~");
        }

        if (MapController.hasMapEnded()) {
            MapController.endMap();
        }
    }

    /**
     * Gets the counts of the players on the owners team, and opponents in the zone
     * @return A tuple of OwnerCount and OpponentCount
     */
    private Tuple<Integer, Integer> getPlayerCounts() {
        Tuple<Integer, Integer> counts = new Tuple<>(0, 0);

        for (UUID uuid : players) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                String team = MapController.getCurrentMap().getTeam(uuid).name;
                if (team.equals(currentOwners)) {
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
    private String getLargestTeam() {
        HashMap<String, Integer> teamCounts = new HashMap<>();
        String largestTeam = null;

        for (UUID uuid : players) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                // Add the player to the team counts
                String team = MapController.getCurrentMap().getTeam(uuid).name;
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
     */
    private void playCapSound(Player player) {
        Location location = player.getLocation();

        Sound effect = Sound.ENTITY_EXPERIENCE_ORB_PICKUP;

        float volume = 1f; //1 = 100%
        float pitch = 0.5f; //Float between 0.5 and 2.0

        player.playSound(location, effect, volume, pitch);
    }

    /**
     * Moves the flag onto the next animation frame
     * @param isCapUp If the animation index has increased
     * @param teamName The name of the team that owns the flag
     */
    private synchronized void animate(boolean isCapUp, String teamName) {

        MapController.getCurrentMap().UpdateWoolMaps(name);

        new BukkitRunnable() {
            @Override
            public void run() {
                // Gets the next frame and previous
                // to overwrite previous animation and set new one
                Frame nextFrame = animation[animationIndex];
                Frame previousFrame;
                if (isCapUp) {
                    previousFrame = animation[animationIndex - 1];
                } else {
                    previousFrame = animation[animationIndex + 1];
                }

                // Get the team, or sets the neutralized materials
                Team team = MapController.getCurrentMap().getTeam(teamName);
                if (team == null) {
                    team = new Team("null");
                    team.primaryWool = Material.GRAY_WOOL;
                    team.secondaryWool = Material.LIGHT_GRAY_WOOL;
                }
                World world = spawnPoint.getWorld();
                assert world != null;

                // Set previous animation blocks to air
                if (animationAir) {
                    for (Vector vector : previousFrame.secondary_blocks) {
                        Location loc = vector.toLocation(world);
                        Block block = loc.getBlock();
                        block.setType(Material.AIR);
                    }

                    for (Vector vector : previousFrame.primary_blocks) {
                        Location loc = vector.toLocation(world);
                        Block block = loc.getBlock();
                        block.setType(Material.AIR);
                    }
                }

                // Set new blocks
                for (Vector vector:nextFrame.secondary_blocks) {
                    Location loc = vector.toLocation(world);
                    Block block = loc.getBlock();
                    block.setType(team.secondaryWool);
                }

                for (Vector vector:nextFrame.primary_blocks) {
                    Location loc = vector.toLocation(world);
                    Block block = loc.getBlock();
                    block.setType(team.primaryWool);
                }

                for (Vector vector:nextFrame.air) {
                    Location loc = vector.toLocation(world);
                    Block block = loc.getBlock();
                    block.setType(Material.AIR);
                }
            }
        }.runTask(Main.plugin);
    }

    /**
     * Get the wool block to display behind the sign on the WoolMap
     * @return The Material to set
     */
    public Material GetWoolMapBlock() {
        // Flag is neutral
        if (getCurrentOwners() == null) {
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
}