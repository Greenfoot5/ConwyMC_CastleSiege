package me.greenfoot5.castlesiege.maps.Hommet;

import me.greenfoot5.castlesiege.Main;
import me.greenfoot5.castlesiege.database.UpdateStats;
import me.greenfoot5.castlesiege.maps.MapController;
import me.greenfoot5.castlesiege.maps.TeamController;
import me.greenfoot5.castlesiege.structures.SchematicSpawner;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Duration;
import java.util.Objects;
import java.util.UUID;

/**
 * Handles the collapsing of the stables in Hommet
 */
public class CollapseEvent implements Listener {

    private static final Location CENTRAL = new Location(Bukkit.getWorld("Hommet"), -34, 84, -59);

    private static final Location SCHEMATIC = new Location(Bukkit.getWorld("Hommet"), -34, 83, -46);

    /**
     * Begins the collapse when a player clicks the button
     * @param e The PlayerInteractEvent the class is listening for
     */
    @EventHandler
    public void wallClickEvent(PlayerInteractEvent e) {

        Player player = e.getPlayer();

        // Check we're on Hommet
        if ((MapController.getCurrentMap() != null && !MapController.getCurrentMap().worldName.equals("Hommet")) || !MapController.isOngoing()) {
            return;
        }

        // Check the player has right-clicked a SPRUCE BUTTON while standing within 5 blocks of the CENTRE
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK && player.getLocation().distance(CENTRAL) <= 5
                && e.getClickedBlock().getType() == Material.SPRUCE_BUTTON) {

            if(!Objects.equals(TeamController.getTeam(player.getUniqueId()).getName(),
                    MapController.getCurrentMap().teams[0].getName())) {

                for (UUID all : MapController.getCurrentMap().teams[1].getPlayers()) {
                    Player near = Bukkit.getPlayer(all);
                    if (near.getLocation().distance(CENTRAL) <= 10) {
                        Title.Times times = Title.Times.times(Duration.ofMillis(500), Duration.ofMillis(4000), Duration.ofMillis(10));
                        Title title = Title.title(Component.text("RUN"), Component.text("The tunnel is about to collapse!"), times);
                        near.showTitle(title);
                    }
                }

                UpdateStats.addSupports(player.getUniqueId(), 60);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        SchematicSpawner.spawnSchematic(SCHEMATIC, "HommetWallCollapse2");

                    }
                }.runTaskLater(Main.plugin, 20);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        SchematicSpawner.spawnSchematic(SCHEMATIC, "HommetWallCollapse3");

                    }
                }.runTaskLater(Main.plugin, 60);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        SchematicSpawner.spawnSchematic(SCHEMATIC, "HommetCollapsedWall");

                        // Play various sound effects to make it sound like a massive explosion
                        Objects.requireNonNull(Bukkit.getWorld("Hommet")).playSound(CENTRAL, Sound.ENTITY_GENERIC_EXPLODE , 10000, 2 );
                        Objects.requireNonNull(Bukkit.getWorld("Hommet")).playSound(CENTRAL, Sound.ENTITY_FIREWORK_ROCKET_BLAST_FAR , 10000, 2 );
                        Objects.requireNonNull(Bukkit.getWorld("Hommet")).playSound(CENTRAL, Sound.ENTITY_FIREWORK_ROCKET_BLAST , 10000, 2 );
                        Objects.requireNonNull(Bukkit.getWorld("Hommet")).playSound(CENTRAL, Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST , 10000, 2 );
                        Objects.requireNonNull(Bukkit.getWorld("Hommet")).playSound(CENTRAL, Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST_FAR , 10000, 2 );
                        Objects.requireNonNull(Bukkit.getWorld("Hommet")).playSound(CENTRAL, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE , 10000, 2 );
                    }
                }.runTaskLater(Main.plugin, 140);
            }
        }
    }
}
