package me.huntifi.castlesiege.maps;

public enum TimerState {
    PREGAME, // Before the game starts
    EXPLORATION, // Players can explore the map (no placing/destroy)
    LOBBY_LOCKED, // Players are confined to the lobby
    ONGOING, // The map is ongoing
    ENDED // The map has finished
}
