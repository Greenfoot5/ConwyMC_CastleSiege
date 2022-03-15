package me.huntifi.castlesiege.flags;

import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.session.MoveType;
import com.sk89q.worldguard.session.Session;
import com.sk89q.worldguard.session.handler.Handler;
import me.huntifi.castlesiege.maps.MapController;
import org.bukkit.entity.Player;

import java.util.Set;

public class CaptureHandler extends Handler {

    public static final Factory FACTORY = new Factory();
    public static class Factory extends Handler.Factory<CaptureHandler> {
        @Override
        public CaptureHandler create(Session session) {
            return new CaptureHandler(session);
        }
    }

    public CaptureHandler(Session session) {
        super(session);
    }

    @Override
    public boolean onCrossBoundary(LocalPlayer player, Location from, Location to, ApplicableRegionSet toSet,
                                   Set<ProtectedRegion> entered, Set<ProtectedRegion> exited, MoveType moveType) {
        for (ProtectedRegion region : entered) {
            Flag flag = MapController.getCurrentMap().getFlag(region.getId());
            if (flag != null && player.isPlayer()) {
                flag.playerEnter((Player) player);
            }
            System.out.println("Player entered region " + region.getId());
        }

        for (ProtectedRegion region : exited) {
            Flag flag = MapController.getCurrentMap().getFlag(region.getId());
            if (flag != null && player.isPlayer()) {
                flag.playerExit((Player) player);
            }
            System.out.println("Player exited region " + region.getId());
        }
        return true;
    }
}
