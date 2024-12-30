package me.huntifi.castlesiege.maps;

import me.huntifi.castlesiege.maps.objects.Core;

/**
 * A map with Cores on it
 */
public class CoreMap extends Map {

    private Core[] cores;

    /**
     * Creates a new core map
     */
    public CoreMap() {
        super();

        cores = new Core[0];
    }

    /**
     * @param cores An array of cores the map has
     */
    public void setCores(Core[] cores) {
        this.cores = cores;
    }

    /**
     * @param index The index of the core to set
     * @param core The new core
     */
    public void setCore(int index, Core core) {
        cores[index] = core;
    }

    /**
     * @return The array of cores
     */
    public Core[] getCores() {
        return cores;
    }

    /**
     * @param i The index of the core to get
     * @return The specified core
     */
    public Core getCore(int i) {
        return cores[i];
    }

    /**
     * Gets a core based on a name
     * @param name the name of the core
     * @return the core, null if none was found
     */
    public Core getCore(String name)
    {
        for (Core core : cores)
        {
            if (core.name.equalsIgnoreCase(name))
            {
                return core;
            }
        }
        return null;
    }

}
