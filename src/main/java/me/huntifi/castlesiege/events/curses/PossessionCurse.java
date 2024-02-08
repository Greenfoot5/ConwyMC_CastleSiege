package me.huntifi.castlesiege.events.curses;

import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.kits.kits.Kit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PossessionCurse extends CurseCast {
    public final static String name = "CurseCast of Possession";
    private final static String activateMessage = "Random kits have possessed players!";
    private final static String expireMessage = "The kits have released their hold on players";
    private final String kitName;

    public PossessionCurse(String kitName) {
        super(name, activateMessage, expireMessage, new ArrayList<>(2),0);
        Collection<String> kits = Kit.getKits();
        kits.add("random");
        options.set(0, (List<String>) kits);
        options.set(1, List.of("[player]"));
        this.kitName = kitName;
    }

    @Override
    public boolean activateCurse() {

        Messenger.broadcastCurse(ChatColor.DARK_RED + name + "§r has been activated! " + activateMessage);
        return true;
    }

    @Override
    public boolean activateCurse(Player player) {
        return false;
    }
}
