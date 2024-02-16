package me.huntifi.castlesiege.events.curses;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.maps.MapController;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class HealingCurse extends CurseCast {
    public final static String name = "Curse of Healing";
    private final static String activateMessage = "Your maximum health has been modified by ";
    private final static String expireMessage = "Your health is back to normal.";
    private final static List<List<String>> OPTIONS = Arrays.asList(List.of("<duration>"), List.of("<multiplier>"));

    public final static int MIN_DURATION = 30;
    public final float multiplier;

    private HealingCurse(CurseBuilder builder) {
        super(builder);
        multiplier = builder.multiplier;
    }

    @Override
    protected void cast() {
        this.setStartTime();
        Messenger.broadcastCurse(ChatColor.DARK_RED + getDisplayName() + "§r has been activated! " + getActivateMessage() +
                new DecimalFormat("0.00").format(multiplier) + "!");

        // Update all player's current health
        for (UUID uuid : MapController.getPlayers())
            updateHealth(uuid, multiplier);

        CurseCast curse = this;
        new BukkitRunnable() {
            @Override
            public void run() {
                CurseExpired expired = new CurseExpired(curse);
                Bukkit.getPluginManager().callEvent(expired);

                // Update all player's current health
                for (UUID uuid : MapController.getPlayers())
                    updateHealth(uuid, 1f);

                Messenger.broadcastCurseEnd(ChatColor.DARK_GREEN + expired.getDisplayName() + "§r has been expired! " + expired.getExpireMessage());
            }
        }.runTaskLater(Main.plugin, getDuration());

    }

    private static void updateHealth(UUID uuid, float multiplier) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null)
            return;
        AttributeInstance healthAttribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (healthAttribute == null)
            return;

        Kit currentKit = Kit.equippedKits.get(uuid);
        double currentHealth = player.getHealth() / currentKit.baseHealth;
        float newMax = currentKit.baseHealth * multiplier;
        healthAttribute.setBaseValue(newMax);
        player.setHealth(currentHealth * newMax);
        if (newMax > 200) {
            player.setHealthScale(newMax / 10.0);
        } else {
            player.setHealthScale(20.0);
        }
    }

    //Builder Class
    public static class CurseBuilder extends CurseCast.CurseBuilder {

        public float multiplier;

        public CurseBuilder(int duration) {
            super(name, activateMessage, expireMessage);
            if (duration < MIN_DURATION)
                throw new IllegalArgumentException("Duration is too small!");
            this.duration = duration;
            this.options = OPTIONS;
            this.multiplier =  2 - new Random().nextFloat(0, 2);
        }

        public HealingCurse cast() {
            HealingCurse curse = new HealingCurse(this);
            Bukkit.getPluginManager().callEvent(curse);
            if (!curse.isCancelled())
                curse.cast();
            return curse;
        }

        public CurseBuilder setMultiplier(float multiplier) {
            this.multiplier = multiplier;
            return this;
        }
    }
}
