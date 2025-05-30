package me.greenfoot5.castlesiege.events.curses;

import me.greenfoot5.castlesiege.Main;
import me.greenfoot5.castlesiege.kits.kits.Kit;
import me.greenfoot5.castlesiege.maps.TeamController;
import me.greenfoot5.conwymc.util.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * The curse to increase damage
 */
public class DamageCurse extends CurseCast {
    public final static String name = "Curse of Damage";
    private final static String activateMessage = "Your melee damage has been modified by ";
    private final static String expireMessage = "Your melee damage is back to normal.";
    private final static List<List<String>> OPTIONS = Arrays.asList(List.of("<duration>"), List.of("<multiplier>"));

    public final static int MIN_DURATION = 30;
    public final float multiplier;

    private DamageCurse(CurseBuilder builder) {
        super(builder);
        multiplier = builder.multiplier;
    }

    @Override
    protected void cast() {
        this.setStartTime();
        Messenger.broadcastCurse("<dark_red>" + getDisplayName() + "</dark_red> has been activated! " + getActivateMessage() +
                new DecimalFormat("0.00").format(multiplier) + "!");
        playSound(TeamController.getPlayers());

        // Update all player's current health
        for (UUID uuid : TeamController.getPlayers())
            Kit.equippedKits.get(uuid).setItems(uuid, true);

        CurseCast curse = this;
        new BukkitRunnable() {
            @Override
            public void run() {
                CurseExpired expired = new CurseExpired(curse);
                Bukkit.getPluginManager().callEvent(expired);

                // Update all player's current health
                for (UUID uuid : TeamController.getPlayers())
                    Kit.equippedKits.get(uuid).setItems(uuid, true);

                Messenger.broadcastCurseEnd("<dark_green>" + expired.getDisplayName() + "</dark_green> has been expired! " + expired.getExpireMessage());
            }
        }.runTaskLater(Main.plugin, getDuration());

    }

    /**
     * Builder class to create a new Damage Curse
     */
    public static class CurseBuilder extends CurseCast.CurseBuilder {

        public float multiplier;

        /**
         * Creates a new CurseBuilder for Damage
         * @param duration The duration of the curse
         */
        public CurseBuilder(int duration) {
            super(name, activateMessage, expireMessage);
            if (duration < MIN_DURATION)
                throw new IllegalArgumentException("Duration is too small!");
            this.duration = duration;
            this.options = OPTIONS;
            //this.multiplier =  2 - new Random().nextFloat(0, 2);
            Random rando = new Random();
            this.multiplier =  2 - rando.nextFloat() * 2;
        }

        public void cast() {
            DamageCurse curse = new DamageCurse(this);
            Bukkit.getPluginManager().callEvent(curse);
            if (!curse.isCancelled())
                curse.cast();
        }

        /**
         * @param multiplier The new damage multiplier
         * @return The curse builder
         */
        public CurseBuilder setMultiplier(float multiplier) {
            this.multiplier = multiplier;
            return this;
        }
    }
}
