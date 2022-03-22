package me.huntifi.castlesiege.flags;

import me.huntifi.castlesiege.Helmsdeep.flags.FlagName;
import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.Frame;
import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.Team;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Flag {
    public String name;
    private String startingTeam;

    // Location Data
    public Location spawnPoint;
    private final List<UUID> players;

    // Game Data
    public String currentOwners;
    private final int maxCap;
    private final int progressAmount;
    private int progress;
    private final static int progressMultiplier = 100;
    public final static double capMultiplier = 1.1;

    // Capturing data
    private AtomicInteger isRunning;
    public int animationIndex;

    public Frame[] animation;

    private Plugin plugin;

    public Flag(String name, String startingTeam, int maxCapValue, int progressAmount) {
        this.name = name;
        this.startingTeam = startingTeam;
        this.currentOwners = startingTeam;
        this.maxCap = maxCapValue;
        animationIndex = maxCapValue;
        this.progressAmount = progressAmount;
        this.players = new ArrayList<>();
        progress = progressMultiplier * maxCapValue;
        isRunning = new AtomicInteger(0);
        plugin = Bukkit.getServer().getPluginManager().getPlugin("ConwyCastleSiege");
    }

    /**
     * Attempts to change the team holding the flag.
     * @param newTeam The new flag owners
     */
    private void changeTeam(String newTeam) {
        // TODO - Notify all players of the change
        if (newTeam != null)
        {
            currentOwners = newTeam;
            Team team = MapController.getCurrentMap().getTeam(newTeam);
            Bukkit.broadcastMessage(team.primaryChatColor + "~~~ " + newTeam + " has captured " + name + "! ~~~");
        }
        else if (currentOwners != null)
        {
            currentOwners = null;
            Bukkit.broadcastMessage(ChatColor.GRAY + "~~~ " + name + " has been neutralised! ~~~");
        }
    }

    private void captureFlag() {
        int capProgress = progress / progressMultiplier;

        if (progress == 0) {
            System.out.println("Neutral Flag");
            // Flag became neutral
            animationIndex = 0;

            // Notify current capping players
            notifyPlayers(false);

            changeTeam(null);

            // TODO - Animate

        } else if (capProgress == 1 && animationIndex == 0) {
            System.out.println("Captured Flag: " + currentOwners);
            changeTeam(currentOwners);
            animationIndex += 1;

            notifyPlayers(true);

            // TODO - Animate

        } else if (capProgress > animationIndex) {
            System.out.println("Cap Up");
            // Cap Up
            if (animationIndex >= maxCap) {
                return;
            }

            animationIndex += 1;

            // Notify current capping players
            for (UUID uuid : players) {
                Player player = Bukkit.getPlayer(uuid);
                // Check they're a player
                notifyPlayers(true);
            }

            // TODO - Increase the animation

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
                System.out.println("Max Cap");
            }

        } else if (capProgress < animationIndex) {
            System.out.println("Cap Down!");
            // Cap down
            animationIndex -= 1;

            // Notify current capping players
            notifyPlayers(false);

            // TODO - Increase the animation
            // TODO - Notify cappers

        } else {
            System.out.println("else");
            // Equal, no capping done
            // TODO - Notify cappers
        }
    }

    private void notifyPlayers(boolean areOwnersCapping) {
        for (UUID uuid : players) {
            Player player = Bukkit.getPlayer(uuid);
            // Check they're a player
            if (player != null) {
                // Make sure they're on the capping team
                if (MapController.getCurrentMap().getTeam(uuid).name.equals(currentOwners) == areOwnersCapping) {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_AQUA + "+" + getPlayerCounts().getSecond() + " flag-capping point(s)" + ChatColor.AQUA + " Flag: " + name));
                } else {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_RED + "Enemies are capturing the flag!"));
                }
                playCapSound(player);
            }
        }
    }

    /**
     * Gets the message to send to the user when they spawn in
     * @return the message to send
     */
    public String getSpawnMessage() {
        Team team = MapController.getCurrentMap().getTeam(currentOwners);
        return team.secondaryChatColor + "Spawning at:" + team.primaryChatColor + " " + name;
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
     * Handles the capturing of a flag
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

                    captureProgress();

                    if (progress / progressMultiplier != animationIndex || progress < progressMultiplier)
                        captureFlag();

                    // No more players in the zone
                    if (players.size() <= 0 || isRunning.get() > 1) {
                        isRunning.decrementAndGet();
                        this.cancel();
                    }
                }
            }.runTaskTimerAsynchronously(Main.plugin, 10, 10);
        } else {
            isRunning.decrementAndGet();
        }
    }

    private synchronized void captureProgress() {
        if (currentOwners == null) {
            currentOwners = getLargestTeam();
        }

        Tuple<Integer, Integer> counts = getPlayerCounts();

        int amount = 69;
        if (counts.getFirst() > counts.getSecond()) {
            amount = (int) (progressAmount * Math.pow(capMultiplier, counts.getFirst() - counts.getSecond() - 1));
            progress += Math.min(amount, 25);
        } else if (counts.getSecond() > counts.getFirst()) {
            amount = (int) (progressAmount * Math.pow(capMultiplier, counts.getSecond() - counts.getFirst() - 1));
            progress -= Math.min(amount, 25);
        }

        if (progress < 0) {
            progress = 0;
        } else if (progress > maxCap * progressMultiplier) {
            progress = maxCap * progressMultiplier;
        }
    }

    public boolean underAttack() {
        if (isRunning.intValue() <= 0) {
            return false;
        }

        Tuple<Integer, Integer> counts = getPlayerCounts();

        return counts.getFirst() > counts.getSecond();
    }

    private Tuple<Integer, Integer> getPlayerCounts() {
        Tuple<Integer, Integer> counts = new Tuple<>(0, 0);

        for (UUID uuid : players) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                String team = MapController.getCurrentMap().getTeam(uuid).name;
                if (currentOwners.equals(team)) {
                    counts.setFirst(counts.getFirst() + 1);
                } else {
                    counts.setSecond(counts.getSecond() + 1);
                }
            }
        }

        return counts;
    }

    private String getLargestTeam() {
        HashMap<String, Integer> teamCounts = new HashMap<>();
        String largestTeam = null;

        for (UUID uuid : players) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                // Add the player to the team counts
                String team = MapController.getCurrentMap().getTeam(uuid).name;
                if (teamCounts.get(name) == null) {
                    teamCounts.put(team, 1);
                } else {
                    teamCounts.put(name, teamCounts.get(name) + 1);
                }

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

    private void playCapSound(Player player) {
        Location location = player.getLocation();

        Sound effect = Sound.ENTITY_EXPERIENCE_ORB_PICKUP;

        float volume = 1f; //1 = 100%
        float pitch = 0.5f; //Float between 0.5 and 2.0

        player.playSound(location, effect, volume, pitch);
    }
}