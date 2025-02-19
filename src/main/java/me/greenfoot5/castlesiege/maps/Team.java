package me.greenfoot5.castlesiege.maps;

import me.greenfoot5.castlesiege.Main;
import me.greenfoot5.castlesiege.data_types.CSStats;
import me.greenfoot5.castlesiege.database.MVPStats;
import me.greenfoot5.castlesiege.kits.kits.SignKit;
import me.greenfoot5.conwymc.data_types.Tuple;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.megavex.scoreboardlibrary.api.sidebar.component.LineDrawable;
import net.megavex.scoreboardlibrary.api.sidebar.component.SidebarComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Represents a team on a map
 */
public class Team implements Listener, SidebarComponent {
    // Basic Details
    private final String name;
    private ArrayList<UUID> players;

    public Lobby lobby;
    public final HashMap<String, SignKit.CostType> kits = new HashMap<>();

    // Colours
    public Material primaryWool;
    public Material secondaryWool;
    public NamedTextColor primaryChatColor;
    public NamedTextColor secondaryChatColor;

    // Lives (Assault)
    private final AtomicInteger lives = new AtomicInteger(-1);

    public int getLives() {
        return lives.get();
    }

    public void setLives(int lives) {
        this.lives.set(lives);
    }

    public void playerDied() {
        // We only want to take a life if the game is ongoing
        if (MapController.timer.state != TimerState.ONGOING)
            return;

        int left = lives.decrementAndGet();
        if (left == 0) {
            MapController.endMap();
        }
    }

    public void grantLives(int amount) {
        lives.addAndGet(amount);
    }

    /**
     * @param event Prevents players from spawning at their bed when they sleep in a bad.
     */
    @EventHandler
    public void onEnterBed(PlayerBedEnterEvent event) {
        Player player = event.getPlayer();
        event.useBed();
        if (this.hasPlayer(event.getPlayer().getUniqueId())) {
            player.addPotionEffect((new PotionEffect(PotionEffectType.REGENERATION, 120, 6)));
            player.setRespawnLocation(this.lobby.spawnPoint, true);
        }
    }

    /**
     * Creates a new team
     * @param name The name of the team
     */
    public Team(String name) {
        this.name = name;
        players = new ArrayList<>();
        Bukkit.getPluginManager().registerEvents(this, Main.plugin);
    }

    public String getName() {
        return name;
    }

    public Component getDisplayName() {
        return Component.text(name, primaryChatColor);
    }


    /**
     * Checks if a player is on the team or not
     * @param uuid the uuid of the player to check
     * @return true if the player is on the team
     */
    public boolean hasPlayer(UUID uuid) {
        return players.contains(uuid);
    }

    /**
     * Attempts to add a player to the team.
     * Will fail if the player is already on the team
     * @param uuid the uuid to add
     */
    public void addPlayer(UUID uuid) {
        if (hasPlayer(uuid)) {
            return;
        }

        // We've done the checks, but it's easier to keep the adding in one place
        forceAddPlayer(uuid);
    }

    /**
     * Adds a player to a team without checking if they are on it.
     * @param uuid the uuid to add
     */
    public void forceAddPlayer(UUID uuid) {
        players.add(uuid);
        Player player = Bukkit.getPlayer(uuid);
        assert player != null;
        if (lobby.spawnPoint.getWorld() == null)
            lobby.spawnPoint.setWorld(Bukkit.getWorld(MapController.getCurrentMap().worldName));
        player.setRespawnLocation(lobby.spawnPoint, true);
    }

    /**
     * Removes a uuid from the team
     * @param uuid the uuid to remove
     */
    public void removePlayer(UUID uuid) {
        players.remove(uuid);
    }

    /**
     * Gets the current size of the team
     * @return How many players are on the team
     */
    public int getTeamSize() {
        return players.size();
    }

    /**
     * Gets the MVP for the current team
     * @return The unique ID and stats of the MVP, null if team is empty
     */
    public Tuple<UUID, CSStats> getMVP() {
        Tuple<UUID, CSStats> mvp = null;

        for (UUID uuid : players) {
            CSStats data = MVPStats.getStats(uuid);
            if (mvp == null || data.getScore() > mvp.getSecond().getScore()) {
                mvp = new Tuple<>(uuid, data);
            }
        }

        return mvp;
    }

    /**
     * @return An ArrayList of all UUIDs on the time
     */
    public ArrayList<UUID> getPlayers() {
        return players;
    }

    /**
     * Clears the team's members
     */
    public void clear() {
        players = new ArrayList<>();
    }

    @Override
    public void draw(@NotNull LineDrawable lineDrawable) {
        if (getLives() >= 0) {
            lineDrawable.drawLine(getDisplayName()
                    .append(Component.text(": "))
                    .append(Component.text(getLives(), NamedTextColor.WHITE)));
        }
    }
}
