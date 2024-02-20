package me.huntifi.castlesiege.commands.donator;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.maps.TeamController;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

/**
 *  The firework command for donators
 */
public class FireworkCommand implements CommandExecutor {

    private final ArrayList<Player> fireworkUsers = new ArrayList<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage("Console cannot launch any fireworks");
            return true;
        }
        Player p = (Player) sender;

        if (TeamController.getTeam(p.getUniqueId()) == null) {
            Messenger.sendError("You must be on a team to use this command!", p);
            return true;
        }

        if (fireworkUsers.contains(p)) {
            Messenger.sendError("You have to wait until you can use this again. (6s cooldown)", p);
            return true;
        }


        //Three rockets are shot from the player's location.
        spawnFirework(p);
        spawnFirework(p);
        spawnFirework(p);
        fireworkUsers.add(p);

        //After 6 seconds removes the player from the firework users list. Then afterward they can use the command again.
        new BukkitRunnable() {
            @Override
            public void run() {
                 fireworkUsers.remove(p);
            }
        }.runTaskLater(Main.plugin, 120);

        return true;
    }

    /**
     *
     * @param p checks this player's team to retrieve colours from it.
     * @return The primary colour retrieved from the specified player's team.
     */
    private Color getPrimaryColor(Player p) {
        switch (TeamController.getTeam(p.getUniqueId()).primaryChatColor) {
            case BLACK:
            case DARK_GRAY:
                return Color.BLACK;
            case DARK_AQUA:
            case AQUA:
                return Color.AQUA;
            case BLUE:
                return Color.BLUE;
            case DARK_BLUE:
                return Color.NAVY;
            case GRAY:
                return Color.GRAY;
            case DARK_GREEN:
                return Color.GREEN;
            case GREEN:
                return Color.LIME;
            case GOLD:
                return Color.ORANGE;
            case DARK_PURPLE:
                return Color.PURPLE;
            case RED:
                return Color.RED;
            case DARK_RED:
                return Color.MAROON;
            case YELLOW:
                return Color.YELLOW;
            default:
                return Color.WHITE;
        }
    }

    /**
     *
     * @param p checks this player's team to retrieve colours from it.
     * @return The secondary colour retrieved from the specified player's team.
     */
    private Color getSecondaryColor(Player p) {
        switch (TeamController.getTeam(p.getUniqueId()).secondaryChatColor) {
            case BLACK:
            case DARK_GRAY:
                return Color.BLACK;
            case DARK_AQUA:
            case AQUA:
                return Color.AQUA;
            case BLUE:
                return Color.BLUE;
            case DARK_BLUE:
                return Color.NAVY;
            case GRAY:
                return Color.GRAY;
            case DARK_GREEN:
                return Color.GREEN;
            case GREEN:
                return Color.LIME;
            case GOLD:
                return Color.ORANGE;
            case DARK_PURPLE:
                return Color.PURPLE;
            case RED:
                return Color.RED;
            case DARK_RED:
                return Color.MAROON;
            case YELLOW:
                return Color.YELLOW;
            default:
                return Color.WHITE;
        }
    }

    /**
     *
     * @param p the player to launch the rocket at.
     */
    public void spawnFirework(Player p) {
        Firework firework = Objects.requireNonNull(p.getLocation().getWorld()).spawn(p.getLocation(), Firework.class);
        FireworkMeta meta = firework.getFireworkMeta();

        meta.setPower(4);
        FireworkEffect.Builder builder = FireworkEffect.builder();
        builder.withTrail().withFlicker().withFade(getPrimaryColor(p), getSecondaryColor(p)).with(FireworkEffect.Type.BALL_LARGE);
        builder.withColor(getPrimaryColor(p), getSecondaryColor(p));
        meta.addEffect(builder.build());
        firework.setFireworkMeta(meta);
    }
}

