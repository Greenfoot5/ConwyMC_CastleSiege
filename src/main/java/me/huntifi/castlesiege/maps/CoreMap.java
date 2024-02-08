package me.huntifi.castlesiege.maps;

import me.huntifi.castlesiege.maps.objects.Core;

public class CoreMap extends Map {

    private Core[] cores;

    private CoreMap() {
        super();
        cores = new Core[0];
    }

    public void setCores(Core[] coreList) {
        cores = coreList;
    }

    public void setCore(int index, Core core) {
        cores[index] = core;
    }

    public Core[] getCores() {
        return cores;
    }

    public Core getCores(int i) {
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
