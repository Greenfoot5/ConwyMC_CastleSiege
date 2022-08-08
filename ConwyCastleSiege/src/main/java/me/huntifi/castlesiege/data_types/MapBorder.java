package me.huntifi.castlesiege.data_types;

/**
 * Represents a map's border
 */
public class MapBorder {

    public final double north;
    public final double east;
    public final double south;
    public final double west;

    /**
     * Create a map border with the corresponding coordinates.
     * @param north The north side of the border
     * @param east The east side of the border
     * @param south The south side of the border
     * @param west The west side of the border
     */
    public MapBorder(double north, double east, double south, double west) {
        this.north = north;
        this.east = east;
        this.south = south;
        this.west = west;
    }
}
