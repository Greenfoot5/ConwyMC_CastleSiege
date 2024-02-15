package me.huntifi.castlesiege.events.combat;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import me.huntifi.castlesiege.Main;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.lang.reflect.InvocationTargetException;

/**
 * Allows the hurt blockAnimation to trigger without dealing damage
 */
public class HurtAnimation implements Listener {

    /**
     * Trigger the hurt blockAnimation for a player
     *
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


    @EventHandler (ignoreCancelled = true)
    public void doDamageParticle(EntityDamageByEntityEvent e) {
        // Both are players
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            Player whoWasHit = (Player) e.getEntity();

            whoWasHit.getWorld().spawnParticle(Particle.BLOCK_DUST,
                    whoWasHit.getLocation().add(0, 0.75, 0),
                    150, 0.1, 0.5, 0.1, Material.REDSTONE_WIRE.createBlockData());
            removeParticle();

        }
    }

    public void removeParticle() {
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        onRemoveParticle(manager);
    }


    public void onRemoveParticle(ProtocolManager manager) {
    manager.addPacketListener(new PacketAdapter(Main.plugin, ListenerPriority.HIGH, PacketType.Play.Server.WORLD_PARTICLES) {
    @Override
    public void onPacketSending(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        if (event.getPacketType() != PacketType.Play.Server.WORLD_PARTICLES)
            return;

        if (packet.getNewParticles().read(0).getParticle() == Particle.DAMAGE_INDICATOR)
            event.setCancelled(true);

       }
     });
   }
}
