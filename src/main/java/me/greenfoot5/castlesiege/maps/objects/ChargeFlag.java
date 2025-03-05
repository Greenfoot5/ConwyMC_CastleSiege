package me.greenfoot5.castlesiege.maps.objects;

import me.greenfoot5.castlesiege.maps.MapController;
import me.greenfoot5.castlesiege.maps.Team;
import me.greenfoot5.castlesiege.maps.TeamController;
import me.greenfoot5.conwymc.util.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;

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

    private boolean hasBonusBeenClaimed = false;


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

    @Override
    protected synchronized void captureProgress() {
        Team largestTeam = MapController.getCurrentMap().getTeam(getLargestTeam());
        if (!canCapture(largestTeam)) {
            for (UUID uuid : players) {
                if (!Objects.equals(TeamController.getTeam(uuid).getName(), currentOwners)) {
                    Messenger.sendActionError("You must capture flags in order on this map, and the previous one doesn't belong to your team!",
                            Objects.requireNonNull(Bukkit.getPlayer(uuid)));
                }
            }
            return;
        }

        super.captureProgress();
    }

    /**
     * Function to make progress on the charge flag
     */
    protected void captureFlag() {
        super.captureFlag();

        if (animationIndex == maxCap && !Objects.equals(currentOwners, startingTeam)) {
            if ((additionalMinutes > 0 || additionalSeconds > 0) && !hasBonusBeenClaimed) {
                Messenger.broadcastInfo("<aqua>" + additionalMinutes + "</aqua> minutes and <aqua>" +
                        additionalSeconds + "</aqua> seconds have been added to the clock!");
                MapController.timer.seconds += additionalSeconds;
                MapController.timer.minutes += additionalMinutes;
                hasBonusBeenClaimed = true;
            } else if (additionalMinutes > 0 || additionalSeconds > 0) {
                Messenger.broadcastInfo("No additional time was granted as it's already been claimed");
            }
        }
    }

    /**
     * Get the previous flag in the order
     * @param index The index of the flag in flags
     * @param teamName The team to get the previous flag for
     * @param flags The array of flags to check in
     * @return The previous flag on the scoreboard
     */
    private Flag getPreviousFlag(int index, String teamName, Flag[] flags) {
        // Get previous flag based on if defenders or not
        if (Objects.equals(teamName, startingTeam)) {
            // If it's the last flag, there is no previous flag for the defenders
            if (index + 1 >= flags.length) {
                return null;
            } else {
                return flags[index + 1];
            }
        }
        if (index == 0) {
            return null;
        }
        return flags[index - 1];
    }

    @Override
    public boolean canCapture(@Nullable Team team) {
        if (!super.canCapture(team)) {
            return false;
        }

        // Find the position of the current flag in the order
        Flag[] flags = MapController.getCurrentMap().flags;
        for (int i = 0; i < flags.length; i++) {
            if (Objects.equals(flags[i].name, name)) {
                if (team != null) {
                    // Get the previous flag
                    Flag previousFlag = getPreviousFlag(i, team.getName(), flags);

                    if (previousFlag == null || !Objects.equals(previousFlag.getCurrentOwners(), team.getName())) {
                        return false;
                    }
                }
                // Flag is neutral
                else {
                    // Get the previous flag
                    Flag previousAttackerFlag = getPreviousFlag(i, MapController.getCurrentMap().teams[0].getName(), flags);
                    Flag previousDefenderFlag = getPreviousFlag(i, MapController.getCurrentMap().teams[1].getName(), flags);

                    // TODO - Check if surrounding flags are owned by the same team
                    if (!(previousDefenderFlag == null || previousAttackerFlag == null)
                            && Objects.equals(previousDefenderFlag.getCurrentOwners(), previousAttackerFlag.getCurrentOwners())) {
                        return false;
                    }
                }

                return true;
            }
        }

        return true;
    }

    @Override
    public String getIcon() {
        Team attackers = Objects.equals(MapController.getCurrentMap().teams[0].getName(), startingTeam) ? MapController.getCurrentMap().teams[1] : MapController.getCurrentMap().teams[0];

        if (canCapture(attackers)) {
            if (!hasBonusBeenClaimed && (additionalMinutes > 0 || additionalSeconds > 0)) {
                return " ⌚";
            }
        }
        return super.getIcon();
    }
}
