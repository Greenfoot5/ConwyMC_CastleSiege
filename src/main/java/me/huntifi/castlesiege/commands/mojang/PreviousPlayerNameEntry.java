package me.huntifi.castlesiege.commands.mojang;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * This class represents the typical response expected by Mojang servers when requesting the name history of a player.
 */
public class PreviousPlayerNameEntry {
    private String name;
    @SerializedName("changedToAt")
    private long changeTime;

    /**
     * Gets the player name of this entry.
     * @return The name of the player.
     */
    public String getPlayerName() {
        return name;
    }

    /**
     * Get the time of change of the name.
     * <br><b>Note: This will return 0 if the name is the original (initial) name of the player! Make sure you check if it is 0 before handling!
     * <br>Parsing 0 to a Date will result in the date "01/01/1970".</b>
     * @return a timestamp in miliseconds that you can turn into a date or handle however you want :)
     */
    public long getChangeTime() {
        return changeTime;
    }

    /**
     * Check if this name is the name used to register the account (the initial/original name)
     * @return a boolean, true if it is the very first name of the player, otherwise false.
     */
    public boolean isPlayersInitialName() {
        return getChangeTime() == 0;
    }

    @Override
    public String toString() {
        return "Name: " + name + " Date of change: " + new Date(changeTime);
    }
}
