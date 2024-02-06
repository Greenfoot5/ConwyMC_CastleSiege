package me.huntifi.castlesiege.maps;

import me.huntifi.castlesiege.maps.objects.Catapult;
import me.huntifi.castlesiege.maps.objects.Door;
import me.huntifi.castlesiege.maps.objects.Flag;
import me.huntifi.castlesiege.maps.objects.Gate;

public class FlagMap extends Map {

    public FlagMap() {
        teams = new Team[0];
        flags = new Flag[0];
        doors = new Door[0];
        gates = new Gate[0];
        catapults = new Catapult[0];
    }
}
