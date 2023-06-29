package me.huntifi.castlesiege.commands.donator;

import me.huntifi.castlesiege.maps.NameTag;
import me.huntifi.castlesiege.maps.TeamController;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.FireworkEffect;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class FireworkCmd implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage("Console cannot set their leave message!");
            return true;
        }

        Player p = (Player) sender;

        Firework firework = Objects.requireNonNull(p.getLocation().getWorld()).spawn(p.getLocation(), Firework.class);
        FireworkMeta meta = firework.getFireworkMeta();

        meta.setPower(4);
        FireworkEffect.Builder builder = FireworkEffect.builder();
        builder.withTrail().withFlicker().withFade(getColor(p), getColor2(p)).with(FireworkEffect.Type.BALL_LARGE);
        builder.withColor(getColor(p), getColor2(p));
        meta.addEffect(builder.build());
        firework.setFireworkMeta(meta);

        return true;
    }

    private Color getColor(Player p) {
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

    private Color getColor2(Player p) {
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
}

