package me.huntifi.castlesiege.kits.kits;

import java.util.ArrayList;
import java.util.Collection;

public class FreeKit extends Kit {

    // Kit Tracking
    private static final Collection<String> kits = new ArrayList<>();

    /**
     * Create a kit with basic settings
     *
     * @param name       This kit's name
     * @param baseHealth This kit's base health
     */
    public FreeKit(String name, int baseHealth, double regenAmount) {
        super(name, baseHealth, regenAmount);
        kits.add(getSpacelessName());
    }

    /**
     * Get all free kit names
     * @return All free kit names without spaces
     */
    public static Collection<String> getKits() {
        return kits;
    }
}
