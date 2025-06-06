package me.greenfoot5.castlesiege.events.curses;

import me.greenfoot5.castlesiege.Main;
import me.greenfoot5.castlesiege.kits.kits.Kit;
import me.greenfoot5.castlesiege.kits.kits.SignKit;
import me.greenfoot5.castlesiege.maps.TeamController;
import me.greenfoot5.conwymc.util.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A curse that gives a player a random kit
 */
public class PossessionCurse extends CurseCast {
    public final static String name = "Curse of Possession";
    private final static String activateMessage = "Random kits have possessed player(s)!";
    private final static String expireMessage = "";
    private final static List<List<String>> OPTIONS = new ArrayList<>();
    private final String kitName;

    private final static float TEAM_KIT_CHANCE = 0.1f; //10%

    private PossessionCurse(CurseBuilder builder) {
        super(builder);
        this.kitName = builder.kitName;
    }

    @Override
    protected void cast() {
        Messenger.broadcastCurse("<dark_red>" + name + "</dark_red> has been activated! " + activateMessage);
        playSound(TeamController.getPlayers());

        Collection<UUID> players = TeamController.getPlayers();
        if (getPlayer() != null)
            players = Collections.singleton(Objects.requireNonNull(Bukkit.getPlayer(getPlayer())).getUniqueId());

        for (UUID uuid : players) {
            Player player = Bukkit.getPlayer(uuid);
            assert player != null;
            Kit kit = getKit() == null ? randomKit() : Kit.getKit(kitName);
            kit.addPlayer(player.getUniqueId(), false);
            Bukkit.getScheduler().runTask(Main.plugin, () ->
                    player.setHealth(Objects.requireNonNull(player.getAttribute(Attribute.MAX_HEALTH)).getValue())
            );
        }
    }

    /**
     * @return The kit's name
     */
    public String getKit() {
        return kitName;
    }

    /**
     * Get a random kit.
     * @return A random kit
     */
    private Kit randomKit() {
        // Create a copy of kits
        Collection<String> kits = new ArrayList<>(Kit.getKits());
        Collection<String> signKits = SignKit.getKits();
        kits.removeAll(signKits);

        // Team kits only have a 10% chance of appearing
        if (ThreadLocalRandom.current().nextFloat() > TEAM_KIT_CHANCE) {
            int num = ThreadLocalRandom.current().nextInt(0, kits.size());
            for (String kit : kits) if (--num < 0) return Kit.getKit(kit);
        }
        else {
            int num = ThreadLocalRandom.current().nextInt(0, signKits.size());
            for (String kit : signKits) {
                if (--num < 0) {
                    return Kit.getKit(kit);
                }
            }

        }
        throw new AssertionError();
    }

    /**
     * Builder class to create a new Possession Curse
     */
    public static class CurseBuilder extends CurseCast.CurseBuilder {

        private String kitName;

        /**
         * Creates a new CurseBuilder for Possession
         */
        public CurseBuilder() {
            super(name, activateMessage, expireMessage);
            kitName = null;
            this.options = OPTIONS;
            List<String> kits = new ArrayList<>(Kit.getKits());
            kits.add("random");
            this.options.add(kits);
            this.options.add(List.of("[player]"));
        }

        public void cast() {
            PossessionCurse curse = new PossessionCurse(this);
            Bukkit.getPluginManager().callEvent(curse);
            if (!curse.isCancelled())
                curse.cast();
        }

        /**
         * @param player The uuid of the player to curse. Use {@code null} if it should target everyone
         * @return The CurseBuilder
         */
        public CurseBuilder setPlayer(UUID player) {
            this.player = player;
            return this;
        }

        /**
         * @param kitName The name of the kit to possess the player. Use {@code null} or {@code random} if it should give a random kit
         * @return The CurseBuilder
         */
        public CurseBuilder setKit(String kitName) {
            if (kitName.equalsIgnoreCase("random")) {
                kitName = null;
            }

            this.kitName = kitName;
            return this;
        }
    }
}
