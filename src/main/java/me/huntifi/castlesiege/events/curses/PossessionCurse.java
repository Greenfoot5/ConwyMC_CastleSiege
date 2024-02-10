package me.huntifi.castlesiege.events.curses;

import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.kits.kits.Kit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class PossessionCurse extends CurseCast {
    public final static String name = "Curse of Possession";
    private final static String activateMessage = "Random kits have possessed players!";
    private final static String expireMessage = "";
    private final static List<List<String>> OPTIONS = new ArrayList<>(2);
    private final String kitName;

    private PossessionCurse(CurseBuilder builder) {
        super(builder);
        this.kitName = builder.kitName;
    }

    @Override
    public void cast() {
        Messenger.broadcastCurse(ChatColor.DARK_RED + name + "§r has been activated! " + activateMessage);
    }

    public String getKit() {
        return kitName;
    }

    /**
     * Get a random kit.
     * @return A random kit
     */
    private Kit randomKit() {
        Collection<String> kits = Kit.getKits();
        int num = ThreadLocalRandom.current().nextInt(0, kits.size());
        for (String kit: kits) if (--num < 0) return Kit.getKit(kit);
        throw new AssertionError();
    }

    //Builder Class
    public static class CurseBuilder extends CurseCast.CurseBuilder {

        private String kitName;

        public CurseBuilder() {
            super(name, activateMessage, expireMessage);
            kitName = null;
            this.options = OPTIONS;
            Collection<String> kits = Kit.getKits();
            kits.add("random");
            this.options.set(0, (List<String>) kits);
            this.options.set(1, List.of("[player]"));
        }

        public PossessionCurse cast() {
            PossessionCurse curse = new PossessionCurse(this);
            Bukkit.getPluginManager().callEvent(curse);
            if (!curse.isCancelled())
                curse.cast();
            return curse;
        }

        public CurseCast.CurseBuilder setPlayer(UUID player) {
            this.player = player;
            return this;
        }

        public CurseCast.CurseBuilder setKit(UUID player) {
            this.player = player;
            return this;
        }
    }
}
