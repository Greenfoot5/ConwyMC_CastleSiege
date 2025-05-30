package me.greenfoot5.castlesiege.maps;

/**
 * A set of states for a map timer
 */
public enum TimerState {
    PREGAME, // Before the map rotation starts
    EXPLORATION, // Players can explore the map (no placing/destroy)
    LOBBY_LOCKED, // Players are confined to the lobby
    ONGOING, // The map is ongoing
    ENDED // The map has finished
}
