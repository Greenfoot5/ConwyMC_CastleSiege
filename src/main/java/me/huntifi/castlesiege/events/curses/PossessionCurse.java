package me.huntifi.castlesiege.events.curses;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.kits.kits.TeamKit;
import me.huntifi.castlesiege.maps.MapController;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

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
    public void cast() {
        Messenger.broadcastCurse(ChatColor.DARK_RED + name + "§r has been activated! " + activateMessage);
        playSound(MapController.getPlayers());

        Collection<UUID> players = MapController.getPlayers();
        if (getPlayer() != null)
            players = Collections.singleton(Objects.requireNonNull(Bukkit.getPlayer(getPlayer())).getUniqueId());

        for (UUID uuid : players) {
            Player player = Bukkit.getPlayer(uuid);
            assert player != null;
            Kit kit = getKit() == null ? randomKit() : Kit.getKit(kitName);
            kit.addPlayer(player.getUniqueId(), false);
            Bukkit.getScheduler().runTask(Main.plugin, () ->
                    player.setHealth(Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue())
            );
        }
    }

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
        Collection<String> teamKits = TeamKit.getKits();
        kits.removeAll(teamKits);

        // Team kits only have a 10% chance of appearing
        if (ThreadLocalRandom.current().nextFloat() > TEAM_KIT_CHANCE) {
            int num = ThreadLocalRandom.current().nextInt(0, kits.size());
            for (String kit : kits) if (--num < 0) return Kit.getKit(kit);
        }
        else {
            int num = ThreadLocalRandom.current().nextInt(0, teamKits.size());
            for (String kit : teamKits) {
                if (--num < 0) {
                    return Kit.getKit(kit);
                }
            }

        }
        throw new AssertionError();
    }

    //Builder Class
    public static class CurseBuilder extends CurseCast.CurseBuilder {

        private String kitName;

        public CurseBuilder() {
            super(name, activateMessage, expireMessage);
            kitName = null;
            this.options = OPTIONS;
            List<String> kits = new ArrayList<>(Kit.getKits());
            kits.add("random");
            this.options.add(kits);
            this.options.add(List.of("[player]"));
        }

        public PossessionCurse cast() {
            PossessionCurse curse = new PossessionCurse(this);
            Bukkit.getPluginManager().callEvent(curse);
            if (!curse.isCancelled())
                curse.cast();
            return curse;
        }

        public CurseBuilder setPlayer(UUID player) {
            this.player = player;
            return this;
        }

        public CurseBuilder setKit(String kitName) {
            if (kitName.equalsIgnoreCase("random")) {
                kitName = null;
            }

            this.kitName = kitName;
            return this;
        }
    }
}
