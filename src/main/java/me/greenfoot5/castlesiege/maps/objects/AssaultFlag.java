package me.greenfoot5.castlesiege.maps.objects;

import me.greenfoot5.castlesiege.maps.Gamemode;
import me.greenfoot5.castlesiege.maps.MapController;
import me.greenfoot5.castlesiege.maps.Team;
import me.greenfoot5.conwymc.util.Messenger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;

import java.util.Objects;


/**
 * A flag specific to the Charge Gamemode
 */
public class AssaultFlag extends Flag {
    public final int additionalLives;
    public boolean livesClaimed = false;

    protected Location attackersSpawnPoint;
    protected Location defendersSpawnPoint;


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
    public AssaultFlag(String name, boolean secret, String startingTeam, int maxCapValue, int progressAmount, int startAmount, int additionalLives) {
        super(name, secret, startingTeam, maxCapValue, progressAmount, startAmount);
        this.additionalLives = additionalLives;
    }

    /**
     * @param attackersSpawnPoint the spawn point for the attackers
     * @param defendersSpawnPoint the spawn point for the defenders
     * @return A ChargeFlag with the values set
     */
    public AssaultFlag setSpawns(Location attackersSpawnPoint, Location defendersSpawnPoint) {
        this.attackersSpawnPoint = attackersSpawnPoint;
        this.defendersSpawnPoint = defendersSpawnPoint;
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

    /**
     * Function to make progress on the charge flag
     */
    protected void captureFlag() {
        // If the game mode is Charge,
        if (MapController.getCurrentMap().gamemode.equals(Gamemode.Assault)) {
            // You can't recap a flag
            if (!Objects.equals(startingTeam, currentOwners) && animationIndex == maxCap && !MapController.getCurrentMap().canRecap)
                return;
        }

        super.captureFlag();

        if (animationIndex == maxCap && !Objects.equals(currentOwners, startingTeam)) {
            if (additionalLives > 0 && !livesClaimed) {
                Messenger.broadcastInfo(Component.empty().append(Component.text(additionalLives + " lives", NamedTextColor.AQUA)).append(Component.text(" have been added to ")).append(MapController.getCurrentMap().getTeam(currentOwners).getDisplayName()));
                MapController.getCurrentMap().getTeam(currentOwners).grantLives(additionalLives);
                livesClaimed = true;
            } else if (additionalLives > 0) {
                Messenger.broadcastInfo("No additional lives were granted as they have already been claimed");
            }
        }
    }

    @Override
    public String getIcon() {
        Team attackers = Objects.equals(MapController.getCurrentMap().teams[0].getName(), startingTeam) ? MapController.getCurrentMap().teams[1] : MapController.getCurrentMap().teams[0];

        if (canCapture(attackers)) {
            if (!livesClaimed && additionalLives > 0) {
                return " ❤";
            }
        }
        return super.getIcon();
    }
}
