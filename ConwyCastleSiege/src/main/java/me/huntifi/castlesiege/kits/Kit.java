package me.huntifi.castlesiege.kits;

import me.huntifi.castlesiege.Deathmessages.DeathscoresAsync;
import me.huntifi.castlesiege.joinevents.stats.StatsChanging;
import me.huntifi.castlesiege.tags.NametagsEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public abstract class Kit implements Listener {
    public String name;
    public int baseHeath;
    public EquipmentSet equipment;
    public int heldItemSlot = 0;
    public String deathMessage;
    public String killMessage;
    public String projectileDeathMessage;
    public String projectileKillMessage;
    public List<UUID> players;

    public Kit() {
        players = new ArrayList<>();
        equipment = new EquipmentSet();
        deathMessage = "You were killed by ";
        killMessage = "You killed ";
        projectileDeathMessage = "You were shot by ";
        projectileKillMessage = "You shot ";
    }

    public void setItems(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) { return; }

        // Health
        AttributeInstance healthAttribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        assert healthAttribute != null;
        healthAttribute.setBaseValue(baseHeath);
        player.setHealthScaled(true);

        // Equipment
        equipment.setEquipment(uuid);
        player.getInventory().setHeldItemSlot(heldItemSlot);

        player.setHealthScaled(true);

        // Wool hat
        WoolHat.setHead(player);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {

        Player whoWasHit = e.getEntity();
        Player whoHit = whoWasHit.getKiller();

        if (whoHit != null) {

            if (StatsChanging.getKit(whoHit.getUniqueId()).equalsIgnoreCase(name)) {

                if (Objects.requireNonNull(whoWasHit.getLastDamageCause()).getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {

                    DeathscoresAsync.doStats(whoHit, whoWasHit);

                    whoWasHit.sendMessage(projectileDeathMessage + NametagsEvent.color(whoHit) + whoHit.getName());
                    whoHit.sendMessage(projectileKillMessage + NametagsEvent.color(whoWasHit) + whoWasHit.getName() + ChatColor.GRAY + " (" + DeathscoresAsync.returnKillstreak(whoHit) + ")");

                } else {

                    DeathscoresAsync.doStats(whoHit, whoWasHit);

                    whoWasHit.sendMessage(killMessage + NametagsEvent.color(whoHit) + whoHit.getName());
                    whoHit.sendMessage(deathMessage + NametagsEvent.color(whoWasHit) + whoWasHit.getName() + ChatColor.GRAY + " (" + DeathscoresAsync.returnKillstreak(whoHit) + ")");

                }
            }
        }
    }
}
