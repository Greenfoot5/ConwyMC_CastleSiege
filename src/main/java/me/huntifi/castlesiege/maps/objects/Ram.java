package me.huntifi.castlesiege.maps.objects;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.database.UpdateStats;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.events.RamEvent;
import me.huntifi.castlesiege.structures.SchematicSpawner;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Represents a ram used to break open a gate
 */
public class Ram {

    private final Gate gate;
    private final ProtectedRegion region;

    private final int damage;
    private int progress;
    private final int progressAmount;
    private static final int progressCap = 100;
    private static final double progressMultiplier = 1.5;

    private final ArrayList<UUID> players;
    private final AtomicInteger isRunning;
    private boolean isActive;

    // Schematics
    private final String schematicNameIdle;
    private final String schematicNameActiveRest;
    private final String schematicNameActiveHit;
    private final Vector schematicLocation;

    public Ram(Gate gate, ProtectedRegion region, int damage, int progressAmount, Vector schematicLocation,
               String schematicNameIdle, String schematicNameActiveRest, String schematicNameActiveHit) {
        this.gate = gate;
        this.region = region;

        this.damage = damage;
        this.progressAmount = progressAmount;
        this.progress = 0;

        this.players = new ArrayList<>();
        this.isRunning = new AtomicInteger(0);
        this.isActive = false;

        this.schematicNameIdle = schematicNameIdle;
        this.schematicNameActiveRest = schematicNameActiveRest;
        this.schematicNameActiveHit = schematicNameActiveHit;
        this.schematicLocation = schematicLocation;
    }

    /**
     * Get this ram's region
     * @return This ram's region
     */
    public ProtectedRegion getRegion() {
        return region;
    }

    /**
     * Add the player to the ram
     * @param player The player that entered the region
     */
    public void playerEnter(Player player) {
        if (gate.isBreached())
            return;
        // Players who can't cap shouldn't be able to pick up a ram
        if (!Kit.equippedKits.get(player.getUniqueId()).canCap) {
            return;
        }

        players.add(player.getUniqueId());
        startRamming();
    }

    /**
     * Remove the player from the ram
     * @param player The player that left the region
     */
    public void playerExit(Player player) {
        players.remove(player.getUniqueId());
    }

    /**
     * The ramming loop that runs when there's 1+ players inside the region
     */
    private synchronized void startRamming() {
        // Only one loop can run at a time
        if (isRunning.get() > 0) {
            return;
        }
        isRunning.incrementAndGet();

        // Keep running as long as there are players in the region
        // and the gate has not been breached yet
        new BukkitRunnable() {
            @Override
            public void run() {
                // Stop ramming when no more players in region or gate is breached
                // Don't stop with only defenders because corresponding flag owner can change
                if (players.isEmpty() || gate.isBreached()) {
                    setIdle();
                    isRunning.decrementAndGet();
                    this.cancel();
                    return;
                }

                // Change ram state, perform a ram tick, or remain idle
                Tuple<ArrayList<UUID>, ArrayList<UUID>> contenders = getContenders();
                if (!isActive && contenders.getFirst().size() > contenders.getSecond().size())
                    setActive();
                else if (isActive && contenders.getFirst().size() <= contenders.getSecond().size())
                    setIdle();
                else if (isActive)
                    doRamTick(contenders);
            }
        }.runTaskTimerAsynchronously(Main.plugin, 10, 10);
    }

    /**
     * Performs one tick of the ram's activity
     * @param contenders A tuple of attacker UUIDs and defender UUIDs
     */
    private void doRamTick(Tuple<ArrayList<UUID>, ArrayList<UUID>> contenders) {
        if (shouldHit(contenders.getFirst().size() - contenders.getSecond().size())) {
            // Perform the ram blockAnimation
            spawnSchematic(schematicNameActiveHit);
            Bukkit.getScheduler().runTaskLater(Main.plugin, () -> spawnSchematic(schematicNameActiveRest), 7);

            RamEvent ramEvent = new RamEvent(gate.getName(), damage, gate.getHealth(), contenders.getFirst());
            Bukkit.getPluginManager().callEvent(ramEvent);
            if (!ramEvent.isCancelled()) {
                // Deal damage to the gate
                gate.dealDamage(ramEvent.getPlayers(), ramEvent.getDamageDealt());
            }

            // Award supports to attacking players
            for (UUID uuid : players)
                UpdateStats.addSupports(uuid, contenders.getFirst().size());
        }
    }

    /**
     * Increase the rams progress and check if it should hit the gate.
     * @param playerDif The difference in attacker and defender count
     * @return Whether the ram should hit the gate
     */
    private boolean shouldHit(int playerDif) {
        progress += Math.min((int) (progressAmount * Math.pow(progressMultiplier, playerDif - 1)), progressCap / 2);
        if (progress >= progressCap)
            progress = 0;

        return progress == 0;
    }

    /**
     * Sets the ram to the active state
     */
    private void setActive() {
        isActive = true;
        spawnSchematic(schematicNameActiveRest);
        playSound(Sound.BLOCK_FENCE_GATE_OPEN);
    }

    /**
     * Sets the ram to the idle state
     */
    private void setIdle() {
        isActive = false;
        progress = 0;
        spawnSchematic(schematicNameIdle);
        playSound(Sound.BLOCK_FENCE_GATE_CLOSE);
    }

    /**
     * Paste a schematic for this ram.
     * @param schematicName The name of the schematic
     */
    private void spawnSchematic(String schematicName) {
        World world = Bukkit.getWorld(MapController.getCurrentMap().worldName);
        assert world != null;
        SchematicSpawner.spawnSchematic(schematicLocation.toLocation(world), schematicName);
    }

    /**
     * Play a sound for this ram.
     * @param sound The sound to play
     */
    private void playSound(Sound sound) {
        World world = Bukkit.getWorld(MapController.getCurrentMap().worldName);
        assert world != null;
        world.playSound(schematicLocation.toLocation(world), sound, 1, 0.5f);
    }

    /**
     * Gets the attacking and defending players in the region
     * @return A tuple of attacker UUIDs and defender UUIDs
     */
    private Tuple<ArrayList<UUID>, ArrayList<UUID>> getContenders() {
        Tuple<ArrayList<UUID>, ArrayList<UUID>> contenders = new Tuple<>(new ArrayList<>(), new ArrayList<>());

        for (UUID uuid : players) {
            if (gate.canBreach(uuid))
                contenders.getFirst().add(uuid);
            else
                contenders.getSecond().add(uuid);
        }

        return contenders;
    }
}
