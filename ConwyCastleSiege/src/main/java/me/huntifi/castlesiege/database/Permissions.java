package me.huntifi.castlesiege.database;

import me.huntifi.castlesiege.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

/**
 * Configures player permissions
 */
public class Permissions {

    private static final HashMap<UUID, PermissionAttachment> perms = new HashMap<>();

    /**
     * Store the player's permission attachment to enable permission manipulations
     * @param uuid The unique ID of the player
     */
    public static void addPlayer(UUID uuid) {
        Player p = Bukkit.getPlayer(uuid);
        assert p != null;

        PermissionAttachment attachment = p.addAttachment(Main.plugin);
        perms.put(uuid, attachment);
    }

    /**
     * Remove the player's permission attachment from storage
     * @param uuid The unique ID of the player
     */
    public static void removePlayer(UUID uuid) {
        Player p = Bukkit.getPlayer(uuid);
        assert p != null;
        p.removeAttachment(perms.get(uuid));
        perms.remove(uuid);
    }

    /**
     * Set the player's staff rank permission
     * @param uuid The unique ID of the player
     * @param permission The permission to set
     */
    public static boolean setStaffPermission(UUID uuid, String permission) {
        Collection<String> staffPerms = Arrays.asList("admin", "developer", "moderator", "chatmod+", "chatmod", "");
        if (staffPerms.contains(permission.toLowerCase())) {
            setPermission(uuid, ActiveData.getData(uuid).getStaffRank(), permission.toLowerCase());
            return true;
        }

        return false;
    }

    /**
     * Set the player's donator rank permission
     * @param uuid The unique ID of the player
     * @param permission The permission to set
     */
    public static void setDonatorPermission(UUID uuid, String permission) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
            Collection<String> donatorPerms = Arrays.asList("high_king", "king", "viceroy", "duke",
                    "count", "baron", "noble", "esquire", "");
            if (donatorPerms.contains(permission)) {
                setPermission(uuid, ActiveData.getData(uuid).getRank(), permission);
            }
        });
    }

    /**
     * Set the player's rank permission
     * @param uuid The unique ID of the player
     * @param currentPerm The current permission to remove
     * @param newPerm The permission to set
     */
    private static void setPermission(UUID uuid, String currentPerm, String newPerm) {
        // Remove old permission and set new one
        PermissionAttachment attachment = perms.get(uuid);
        if (!currentPerm.isEmpty()) {
            attachment.unsetPermission("castlesiege." + currentPerm);
        }
        if (!newPerm.isEmpty()) {
            attachment.setPermission("castlesiege." + newPerm, true);
        }

        // Update commands list
        Player p = Bukkit.getPlayer(uuid);
        assert p != null;
        p.updateCommands();
    }
}
