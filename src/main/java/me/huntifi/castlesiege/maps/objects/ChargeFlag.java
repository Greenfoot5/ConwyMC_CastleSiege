package me.huntifi.castlesiege.maps.objects;

import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.maps.Gamemode;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.TeamController;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.Objects;
import java.util.UUID;


/**
 * A flag specific to the Charge Gamemode
 */
public class ChargeFlag extends Flag{
    public int additionalMinutes;
    public int additionalSeconds;
    protected Location attackersSpawnPoint;
    protected Location defendersSpawnPoint;
    protected final String startingTeam;


    /**
     * Creates a new flag
     *
     * @param name           the name of the flag
     * @param secret         whether the flag is a secret flag
     * @param startingTeam   the team that controls the flag at the beginning of the game
     * @param maxCapValue    the maximum blockAnimation amount
     * @param progressAmount How much progress is made by a single person
     * @param startAmount    the starting blockAnimation amount
     */
    public ChargeFlag(String name, boolean secret, String startingTeam, int maxCapValue, int progressAmount, int startAmount) {
        super(name, secret, startingTeam, maxCapValue, progressAmount, startAmount);
        this.startingTeam = startingTeam;
    }

    /**
     * @param attackersSpawnPoint the spawn point for the attackers
     * @param defendersSpawnPoint the spawn point for the defenders
     * @param additionalMinutes   additional Minutes to add when the flag is fully captured
     * @param additionalSeconds   additional Seconds to add when the flag is fully captured
     * @return A ChargeFlag with the values set
     */
    public ChargeFlag setChargeValues(Location attackersSpawnPoint, Location defendersSpawnPoint, int additionalMinutes, int additionalSeconds) {
        this.attackersSpawnPoint = attackersSpawnPoint;
        this.defendersSpawnPoint = defendersSpawnPoint;
        this.additionalMinutes = additionalMinutes;
        this.additionalSeconds = additionalSeconds;
        return this;
    }

    /**
     * Sets the spawn point for the attackers
     * @param location The new spawn point
     */
    public void setAttackersSpawnPoint(Location location){
        attackersSpawnPoint = location;
    }

    /**
     * Sets the spawn point for the defenders
     * @param location The new spawn point
     */
    public void setDefendersSpawnPoint(Location location){
        defendersSpawnPoint = location;
    }

    /**
     * Gets the spawn point for the flag
     * @param teamName The team to get the spawn point for
     * @return The spawn location for the team
     */
    public Location getSpawnPoint(String teamName) {
        if (Objects.equals(teamName, startingTeam) && defendersSpawnPoint != null) {
            return defendersSpawnPoint;
        }
        if (!Objects.equals(teamName, startingTeam) && attackersSpawnPoint != null) {
            return attackersSpawnPoint;
        }
        return spawnPoint;
    }

    /**
     * Function to make progress on the charge flag
     */
    protected void captureFlag() {
        // If the game mode is Charge,
        if (MapController.getCurrentMap().gamemode.equals(Gamemode.Charge)) {
            // You can't recap a flag
            if (!Objects.equals(startingTeam, currentOwners) && animationIndex == maxCap)
                return;

            // You can't cap the next flag until the previous is capped
            Flag[] flags = MapController.getCurrentMap().flags;
            for (int i = 0; i < flags.length; i++) {
                // Get the index of this flag
                if (Objects.equals(flags[i].name, name)) {
                    // Get the previous flag
                    Flag previousFlag;
                    // If the largest team are the defenders
                    if (Objects.equals(getLargestTeam(), MapController.getCurrentMap().teams[0].name)) {
                        // The defenders can always cap the last flag
                        if (i + 1 < flags.length)
                            previousFlag = flags[i + 1];
                        else {
                            return;
                        }
                    } else {
                        previousFlag = flags[i-1];
                    }
                    if (!Objects.equals(previousFlag.getCurrentOwners(), getLargestTeam())) {
                        for (UUID uuid : players)
                        {
                            if (!Objects.equals(TeamController.getTeam(uuid).name, currentOwners)) {
                                Messenger.sendActionError("You must capture flags in order on this map, and the previous one doesn't belong to your team!",
                                        Objects.requireNonNull(Bukkit.getPlayer(uuid)));
                            }
                        }
                        return;
                    }
                }
            }
        }
        super.captureFlag();
        if (animationIndex == maxCap && !Objects.equals(currentOwners, startingTeam)) {
            Messenger.broadcastInfo(Component.text(name, MapController.getCurrentMap().getTeam(currentOwners).primaryChatColor)
                            .append(Component.text(" has been fully captured and can no longer be retaken by "))
                                            .append(Component.text(startingTeam, MapController.getCurrentMap().getTeam(startingTeam).primaryChatColor)));
            if (additionalMinutes > 0 || additionalSeconds > 0) {
                Messenger.broadcastInfo(additionalMinutes + " minutes and " + additionalSeconds + " seconds have been added to the clock!");
                MapController.timer.seconds += additionalSeconds;
                MapController.timer.minutes += additionalMinutes;
            }
        }
    }
}
