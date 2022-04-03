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
import org.bukkit.potion.PotionEffect;

import java.util.*;

public abstract class Kit implements Listener {
    public String name;
    public int baseHeath;
    public EquipmentSet equipment;
    public int heldItemSlot = 0;
    public String deathMessage;
    public String killMessage;
    public String projectileDeathMessage;
    public String projectileKillMessage;
    public boolean deathPrefix;
    public boolean killPrefix;
    public boolean projectileDeathPrefix;
    public boolean projectileKillPrefix;
    public List<UUID> players;
    public static Map<UUID, Kit> equippedKits = new HashMap<>();
    public PotionEffect[] potionEffects;

    public Kit(String name) {
        this.name = name;
        players = new ArrayList<>();
        equipment = new EquipmentSet();
        deathMessage = "You were killed by ";
        killMessage = "You killed ";
        projectileDeathMessage = "You were shot by ";
        projectileKillMessage = "You shot ";
        deathPrefix = true;
        killPrefix = true;
        projectileDeathPrefix = true;
        projectileKillPrefix = true;
    }

    public void setItems(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) { return; }

        // Health
        AttributeInstance healthAttribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        assert healthAttribute != null;
        healthAttribute.setBaseValue(baseHeath);
        player.setHealthScaled(true);
        player.setHealth(baseHeath);

        // Equipment
        equipment.setEquipment(uuid);
        player.getInventory().setHeldItemSlot(heldItemSlot);

        player.setHealthScaled(true);

        // Wool hat
        WoolHat.setHead(player);

        // Potion effects
        applyPotionEffects(uuid);
    }

    public void refillItems(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) { return; }

        // Equipment
        equipment.setEquipment(uuid);

        // Wool hat
        WoolHat.setHead(player);

        // Potion effects
        applyPotionEffects(uuid);
    }

    private void applyPotionEffects(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) { return; }
        player.getActivePotionEffects().clear();
        player.addPotionEffects(Arrays.asList(potionEffects));
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {

        Player whoWasHit = e.getEntity();
        Player whoHit = whoWasHit.getKiller();

        if (whoHit != null) {

            if (StatsChanging.getKit(whoHit.getUniqueId()).equalsIgnoreCase(name)) {

                if (Objects.requireNonNull(whoWasHit.getLastDamageCause()).getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {

                    DeathscoresAsync.doStats(whoHit, whoWasHit);

                    whoWasHit.sendMessage(projectileDeathPrefix ? projectileDeathMessage + NametagsEvent.color(whoHit) + whoHit.getName()
                            : NametagsEvent.color(whoHit) + whoHit.getName() + ChatColor.RESET + projectileDeathMessage);
                    whoHit.sendMessage(projectileKillPrefix ? projectileKillMessage + NametagsEvent.color(whoWasHit) + whoWasHit.getName() + ChatColor.GRAY + " (" + DeathscoresAsync.returnKillstreak(whoHit) + ")"
                            : NametagsEvent.color(whoWasHit) + whoWasHit.getName() + ChatColor.RESET + projectileKillMessage + ChatColor.GRAY + " (" + DeathscoresAsync.returnKillstreak(whoHit) + ")");
                } else {

                    DeathscoresAsync.doStats(whoHit, whoWasHit);

                    whoWasHit.sendMessage(deathPrefix ? deathMessage + NametagsEvent.color(whoHit) + whoHit.getName()
                            : NametagsEvent.color(whoHit) + whoHit.getName() + ChatColor.RESET + deathMessage);
                    whoHit.sendMessage(killPrefix ? killMessage + NametagsEvent.color(whoWasHit) + whoWasHit.getName() + ChatColor.GRAY + " (" + DeathscoresAsync.returnKillstreak(whoHit) + ")"
                            : NametagsEvent.color(whoWasHit) + whoWasHit.getName() + ChatColor.RESET + killMessage + ChatColor.GRAY + " (" + DeathscoresAsync.returnKillstreak(whoHit) + ")");
                }
            }
        }
    }

    public void addPlayer(UUID uuid) {
        players.add(uuid);
        Player player = Bukkit.getPlayer(uuid);
        setItems(uuid);
        equippedKits.put(uuid, this);

        // TODO - Check if a player has dealt damage and should be killed
        // TODO - If the player doesn't need to die, heal them
        assert player != null;
        player.setHealth(0);
    }
}
