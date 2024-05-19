package me.huntifi.castlesiege.maps.objects;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.session.MoveType;
import com.sk89q.worldguard.session.Session;
import com.sk89q.worldguard.session.handler.Handler;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.maps.MapController;

import java.util.Set;

/**
 * Handles players entering and leaving a region
 */
public class RegionHandler extends Handler {

    public static final Factory FACTORY = new Factory();

    /**
     * Required by WorldGuard
     */
    public static class Factory extends Handler.Factory<RegionHandler> {
        @Override
        public RegionHandler create(Session session) {
            return new RegionHandler(session);
        }
    }

    /**
     * Creates a new Region Handler
     * @param session The current session
     */
    public RegionHandler(Session session) {
        super(session);
    }

    /**
     * Notifies the flag/ram when a player enters and leaves a zone
     * @param player The player that entered/left the zone
     * @param entered The region(s) the player entered
     * @param exited The region(s) the player left
     */
    @Override
    public boolean onCrossBoundary(LocalPlayer player, Location from, Location to, ApplicableRegionSet toSet,
                                   Set<ProtectedRegion> entered, Set<ProtectedRegion> exited, MoveType moveType) {
        // Ensure the player is actually a player
        if (!player.isPlayer())
            return true;

        // Player entered a region
        for (ProtectedRegion region : entered) {
            // Ensure the player is not a spectator
            if (player.getGameMode().toString().equals("spectator"))
                continue;

            // Flag regions
            Flag flag = MapController.getCurrentMap().getFlag(region.getId().replace('_', ' '));
            if (flag != null && Kit.equippedKits.get(player.getUniqueId()).canCap)
                flag.playerEnter(BukkitAdapter.adapt(player));

            // Ram regions
            Ram ram = MapController.getCurrentMap().getRam(region);
            if (ram != null)
                ram.playerEnter(BukkitAdapter.adapt(player));
        }

        // Player left a region
        for (ProtectedRegion region : exited) {
            // Flag regions
            Flag flag = MapController.getCurrentMap().getFlag(region.getId().replace('_', ' '));
            if (flag != null)
                flag.playerExit(BukkitAdapter.adapt(player));

            // Ram regions
            Ram ram = MapController.getCurrentMap().getRam(region);
            if (ram != null)
                ram.playerExit(BukkitAdapter.adapt(player));
        }

        return true;
    }
}
