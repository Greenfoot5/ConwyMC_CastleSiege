package me.huntifi.castlesiege.data_types;

public class KitBooster extends Booster{

    public String kitName;

    public KitBooster(int duration, String kitName) {
        super(duration);
        // TODO - Check if kit name is valid
        this.kitName = kitName;
    }

    @Override
    public String getBoostType() {
        return "KIT";
    }

    @Override
    public String getValue() {
        return kitName;
    }
}
