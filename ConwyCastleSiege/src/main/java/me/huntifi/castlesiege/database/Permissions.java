package me.huntifi.castlesiege.database;

import me.huntifi.castlesiege.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class Permissions {

    private static final HashMap<UUID, PermissionAttachment> perms = new HashMap<>();

    public static void addPlayer(UUID uuid) {
        Player p = Bukkit.getPlayer(uuid);
        assert p != null;

        PermissionAttachment attachment = p.addAttachment(Main.plugin);
        perms.put(uuid, attachment);
    }

    public static void removePlayer(UUID uuid) {
        Player p = Bukkit.getPlayer(uuid);
        assert p != null;
        p.removeAttachment(perms.get(uuid));
        perms.remove(uuid);
    }

    public static void setPermission(UUID uuid, String permission) {
        // Ensure a valid permission was given
        permission = "castlesiege." + permission.toLowerCase();
        Collection<String> allPerms = Arrays.asList("castlesiege.admin", "castlesiege.developer",
                "castlesiege.moderator", "castlesiege.chatmod+", "castlesiege.chatmod");
        if (!allPerms.contains(permission)) {
            return;
        }

        // Remove old permission and set new one
        PermissionAttachment attachment = perms.get(uuid);
        for (String perm : allPerms) {
            attachment.unsetPermission(perm);
        }
        attachment.setPermission(permission, true);

        // Update commands list
        Player p = Bukkit.getPlayer(uuid);
        assert p != null;
        p.updateCommands();
    }
}
