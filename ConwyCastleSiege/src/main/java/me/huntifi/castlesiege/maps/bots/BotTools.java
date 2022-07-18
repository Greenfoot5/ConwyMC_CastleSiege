package me.huntifi.castlesiege.maps.bots;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;
import org.mcmonkey.sentinel.SentinelTrait;
import org.mcmonkey.sentinel.utilities.SentinelVersionCompat;

import javax.annotation.Nullable;

import java.util.UUID;

import static org.mcmonkey.sentinel.SentinelUtilities.*;

public class BotTools {

    @Nullable
    public static NPC getNPC(Entity entity) {
        if (entity == null) {
            return null;
        }
        return CreateBots.registry.getNPC(entity);
    }

    /**
     * Tries to get a Sentinel from an entity. Returns null if it is not a Sentinel.
     */
    public static SentinelTrait tryGetSentinel(Entity entity) {
        if (CreateBots.registry.isNPC(entity)) {
            return CreateBots.registry.getNPC(entity).getTrait(SentinelTrait.class);
        }
        return null;
    }

    /**
     * Returns a boolean indicating whether the first location is looking towards a second location, within a yaw and pitch limit.
     */
    public static boolean isLookingTowards(Location myLoc, Location theirLoc, float yawLimit, float pitchLimit) {
        Vector rel = theirLoc.toVector().subtract(myLoc.toVector()).normalize();
        float yaw = normalizeYaw(myLoc.getYaw());
        float yawHelp = getYaw(rel);
        if (!(Math.abs(yawHelp - yaw) < yawLimit ||
                Math.abs(yawHelp + 360 - yaw) < yawLimit ||
                Math.abs(yaw + 360 - yawHelp) < yawLimit)) {
            return false;
        }
        float pitch = myLoc.getPitch();
        float pitchHelp = getPitch(rel);
        if (!(Math.abs(pitchHelp - pitch) < yawLimit)) {
            return false;
        }
        return true;
    }

    /**
     * Gets the entity for a given UUID.
     */
    public static Entity getEntityForID(UUID id) {
        if (!SentinelVersionCompat.v1_12) {
            for (World world : Bukkit.getServer().getWorlds()) {
                for (Entity e : world.getEntities()) {
                    if (e.getUniqueId().equals(id)) {
                        return e;
                    }
                }
            }
            return null;
        }
        return Bukkit.getServer().getEntity(id);
    }
}
