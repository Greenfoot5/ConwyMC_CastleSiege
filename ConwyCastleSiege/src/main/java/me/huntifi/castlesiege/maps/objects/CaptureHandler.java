package me.huntifi.castlesiege.maps.objects;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldedit.world.gamemode.GameMode;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.session.MoveType;
import com.sk89q.worldguard.session.Session;
import com.sk89q.worldguard.session.handler.Handler;
import me.huntifi.castlesiege.maps.MapController;

import java.util.Set;

/**
 * Handles players entering and leaving a capture zone
 */
public class CaptureHandler extends Handler {

    public static final Factory FACTORY = new Factory();

    /**
     * Required by WorldGuard
     */
    public static class Factory extends Handler.Factory<CaptureHandler> {
        @Override
        public CaptureHandler create(Session session) {
            return new CaptureHandler(session);
        }
    }

    /**
     * Creates a new Capture Handler
     * @param session
     */
    public CaptureHandler(Session session) {
        super(session);
    }

    /**
     * Notifies the flag when a player enters and leaves a zone
     * @param player The player that entered/left the zone
     * @param entered The region(s) the player entered
     * @param exited The region(s) the player left
     */
    @Override
    public boolean onCrossBoundary(LocalPlayer player, Location from, Location to, ApplicableRegionSet toSet,
                                   Set<ProtectedRegion> entered, Set<ProtectedRegion> exited, MoveType moveType) {
        // Player entered a flag capture zone
        for (ProtectedRegion region : entered) {
            Flag flag = MapController.getCurrentMap().getFlag(region.getId().replace('_', ' '));
            if (flag != null && player.isPlayer() && !player.getGameMode().toString().equals("spectator")) {
                flag.playerEnter(BukkitAdapter.adapt(player));
            }
        }

        // Player left a flag capture zone
        for (ProtectedRegion region : exited) {
            Flag flag = MapController.getCurrentMap().getFlag(region.getId().replace('_', ' '));
            if (flag != null && player.isPlayer() && player.getGameMode() != GameMode.REGISTRY.get("spectator")) {
                flag.playerExit(BukkitAdapter.adapt(player));
            }
        }
        return true;
    }
}
