package me.huntifi.castlesiege.kits.kits;

import java.util.ArrayList;

/*
A list of the kits that exist
 */
public class KitList {

    //Kit name tracking
    public static ArrayList<String> kitNames = new ArrayList<>();

    public static void registerExistingKits() {

        // Free
        kitNames.add("Sworsman");
        kitNames.add("Archer");
        kitNames.add("Spearman");

        // Voter
        kitNames.add("Skirmisher");
        kitNames.add("Shieldman");
        kitNames.add("Fire Archer");
        kitNames.add("Scout");
        kitNames.add("Ladderman");

        // Donator
        kitNames.add("Berserker");
        kitNames.add("Vanguard");
        kitNames.add("Executioner");
        kitNames.add("Maceman");

        kitNames.add("Viking");
        kitNames.add("Medic");
        kitNames.add("Ranger");
        kitNames.add("Cavalry");

        kitNames.add("Halberdier");
        kitNames.add("Engineer");
        kitNames.add("Crossbowman");
        kitNames.add("Warhound");

        // Team Specific
        kitNames.add("Moria Orc");
        kitNames.add("Fallen");
        kitNames.add("Elytrier");
        kitNames.add("Hellsteed");
        kitNames.add("Lancer");
        kitNames.add("Ranged Cavalry");
        kitNames.add("Uruk Berserker");
        kitNames.add("Abyssal");

    }
}
