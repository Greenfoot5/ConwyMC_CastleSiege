package me.greenfoot5.castlesiege.data_types;

/**
 * Represents a map's border
 * @param north The north side of the border
 * @param east  The east side of the border
 * @param south The south side of the border
 * @param west  The west side of the border
 */
public record MapBorder(double north, double east, double south, double west) {

    /**
     * Create a map border with the corresponding coordinates.
     */
    public MapBorder {
    }
}
