package me.huntifi.castlesiege.events.curses;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.events.chat.Messenger;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class BindingCurse extends Curse {
    public final static String name = "Curse of Binding";
    private final static String activateMessage = "You can no longer switch kits";
    private final static String expireMessage = "You can now switch kits again";
    public BindingCurse(int duration) {
        super(name, activateMessage, expireMessage, duration);
    }

    @Override
    public void activateCurse() {
        super.startTime = System.currentTimeMillis() / 1000;
        Curse.globalActivatedCurses.add(this);
        Messenger.broadcastCurse(ChatColor.DARK_RED + name + "§r has been activated! " + activateMessage);
        Curse curse = this;
        new BukkitRunnable() {
            @Override
            public void run() {
                Curse.globalActivatedCurses.remove(curse);
                Messenger.broadcastCurseEnd(ChatColor.DARK_GREEN + name + "§r has been expired! " + expireMessage);
            }
        }.runTaskLater(Main.plugin, duration * 20L);
    }

    @Override
    public void activateCurse(Player player) {
        super.startTime = System.currentTimeMillis() / 1000;
        Tuple<Curse, UUID> curse = new Tuple<>(this, player.getUniqueId());
        Curse.playerActivatedCurses.add(curse);
        Messenger.sendCurseEnd(ChatColor.DARK_RED + name + "§r has been activated! " + activateMessage, player);
        new BukkitRunnable() {
            @Override
            public void run() {
                Curse.playerActivatedCurses.remove(curse);
                Messenger.sendCurse(ChatColor.DARK_GREEN + name + "§r has been expired! " + expireMessage, player);
            }
        }.runTaskLater(Main.plugin, duration * 20L);
    }
}
