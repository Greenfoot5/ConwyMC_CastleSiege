package me.huntifi.castlesiege.kits.kits;

import java.util.ArrayList;

/*
 * A list of the kits that exist
 */
public class KitList {

    //Kit name tracking
    private final static ArrayList<String> freeKits = new ArrayList<>();
    private final static ArrayList<String> voterKits = new ArrayList<>();
    private final static ArrayList<String> donatorKits = new ArrayList<>();
    private final static ArrayList<String> teamKits = new ArrayList<>();


    public static void registerExistingKits() {

        // Free
        freeKits.add("Swordsman");
        freeKits.add("Archer");
        freeKits.add("Spearman");

        // Voter
        voterKits.add("Skirmisher");
        voterKits.add("Shieldman");
        voterKits.add("FireArcher");
        voterKits.add("Scout");
        voterKits.add("Ladderman");

        // Donator
        donatorKits.add("Berserker");
        donatorKits.add("Vanguard");
        donatorKits.add("Executioner");
        donatorKits.add("Maceman");

        donatorKits.add("Viking");
        donatorKits.add("Medic");
        donatorKits.add("Ranger");
        donatorKits.add("Cavalry");

        donatorKits.add("Halberdier");
        donatorKits.add("Engineer");
        donatorKits.add("Crossbowman");
        donatorKits.add("Warhound");

        // Team Specific
        teamKits.add("MoriaOrc");
        teamKits.add("Fallen");
        teamKits.add("Elytrier");
        teamKits.add("Hellsteed");
        teamKits.add("Lancer");
        teamKits.add("RangedCavalry");
        teamKits.add("UrukBerserker");
        teamKits.add("Abyssal");
    }

    public static ArrayList<String> getAllKits() {
        ArrayList<String> kits = new ArrayList<>(freeKits);
        kits.addAll(voterKits);
        kits.addAll(donatorKits);
        kits.addAll(teamKits);
        return kits;
    }

    public static ArrayList<String> getFreeKits() {
        return freeKits;
    }

    public static ArrayList<String> getVoterKits() {
        return voterKits;
    }

    public static ArrayList<String> getDonatorKits() {
        return donatorKits;
    }

    public static ArrayList<String> getTeamKits() {
        return teamKits;
    }
}
