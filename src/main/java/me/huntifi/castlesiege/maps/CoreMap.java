package me.huntifi.castlesiege.maps;

import me.huntifi.castlesiege.maps.objects.*;

public class CoreMap extends Map {

    public Core[] cores;

    public CoreMap() {
        cores = new Core[0];
        teams = new Team[0];
        flags = new Flag[0];
        doors = new Door[0];
        gates = new Gate[0];
        catapults = new Catapult[0];
    }
}
