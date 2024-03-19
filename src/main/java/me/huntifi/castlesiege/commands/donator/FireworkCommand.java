package me.huntifi.castlesiege.commands.donator;

import me.huntifi.castlesiege.Main;
import me.huntifi.conwymc.util.Messenger;
import me.huntifi.castlesiege.maps.Team;
import me.huntifi.castlesiege.maps.TeamController;
import net.kyori.adventure.text.format.NamedTextColor;
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

import static net.kyori.adventure.text.format.NamedTextColor.AQUA;
import static net.kyori.adventure.text.format.NamedTextColor.BLACK;
import static net.kyori.adventure.text.format.NamedTextColor.BLUE;
import static net.kyori.adventure.text.format.NamedTextColor.DARK_AQUA;
import static net.kyori.adventure.text.format.NamedTextColor.DARK_BLUE;
import static net.kyori.adventure.text.format.NamedTextColor.DARK_GRAY;
import static net.kyori.adventure.text.format.NamedTextColor.DARK_GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.DARK_PURPLE;
import static net.kyori.adventure.text.format.NamedTextColor.DARK_RED;
import static net.kyori.adventure.text.format.NamedTextColor.GOLD;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static net.kyori.adventure.text.format.NamedTextColor.YELLOW;

/**
 *  The firework command for donators
 */
public class FireworkCommand implements CommandExecutor {

    private final ArrayList<Player> fireworkUsers = new ArrayList<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            Messenger.sendError("Console cannot launch any fireworks", sender);
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
     * @param color The chat colour to get the Color for
     * @return The Color to use in the firework
     */
    private Color getColor(NamedTextColor color) {
        if (color.equals(BLACK) || color.equals(DARK_GRAY)) {
            return Color.BLACK;
        } else if (color.equals(DARK_AQUA) || color.equals(AQUA)) {
            return Color.AQUA;
        } else if (color.equals(BLUE)) {
            return Color.BLUE;
        } else if (color.equals(DARK_BLUE)) {
            return Color.NAVY;
        } else if (color.equals(GRAY)) {
            return Color.GRAY;
        } else if (color.equals(DARK_GREEN)) {
            return Color.GREEN;
        } else if (color.equals(GREEN)) {
            return Color.LIME;
        } else if (color.equals(GOLD)) {
            return Color.ORANGE;
        } else if (color.equals(DARK_PURPLE)) {
            return Color.PURPLE;
        } else if (color.equals(RED)) {
            return Color.RED;
        } else if (color.equals(DARK_RED)) {
            return Color.MAROON;
        } else if (color.equals(YELLOW)) {
            return Color.YELLOW;
        }
        return Color.WHITE;
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
        Team team = TeamController.getTeam(p.getUniqueId());
        builder.withTrail().withFlicker().withFade(getColor(team.primaryChatColor), getColor(team.secondaryChatColor)).with(FireworkEffect.Type.BALL_LARGE);
        builder.withColor(getColor(team.primaryChatColor), getColor(team.secondaryChatColor));
        meta.addEffect(builder.build());
        firework.setFireworkMeta(meta);
    }
}

