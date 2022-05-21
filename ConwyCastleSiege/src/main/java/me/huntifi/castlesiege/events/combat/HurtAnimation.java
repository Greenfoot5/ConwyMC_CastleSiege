package me.huntifi.castlesiege.events.combat;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

/**
 * Allows the hurt animation to trigger without dealing damage
 */
public class HurtAnimation {

    /**
     * Trigger the hurt animation for a player
     * @param player The player
     */
    public static void trigger(Player player) {
        PacketContainer entityStatus = new PacketContainer(PacketType.Play.Server.ENTITY_STATUS);

        entityStatus.getIntegers().write(0, player.getEntityId());
        entityStatus.getBytes().write(0, (byte) 2);

        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, entityStatus);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
