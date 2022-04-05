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
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;

import java.util.*;

public abstract class Kit implements Listener {
    public String name;
    public int baseHeath;

    // Equipment
    public EquipmentSet equipment;
    public int heldItemSlot = 0;
    public PotionEffect[] potionEffects;

    // Messages
    public String[] deathMessage;
    public String[] killMessage;
    public String[] projectileDeathMessage;
    public String[] projectileKillMessage;

    // Player Tracking
    public List<UUID> players;
    public static Map<UUID, Kit> equippedKits = new HashMap<>();

    public Kit(String name) {
        this.name = name;
        players = new ArrayList<>();

        // Equipment
        equipment = new EquipmentSet();
        potionEffects = new PotionEffect[0];

        // Messages
        deathMessage = new String[]{"You were killed by ", ""};
        killMessage = new String[]{"You killed ", ""};
        projectileDeathMessage = new String[]{"You were shot by ", ""};
        projectileKillMessage = new String[]{"You shot ", ""};
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
        resetCooldown(uuid);

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
        resetCooldown(uuid);

        // Wool hat
        WoolHat.setHead(player);

        // Potion effects
        applyPotionEffects(uuid);
    }

    private void resetCooldown(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) { return; }

        PlayerInventory inv = player.getInventory();
        for (int i = 0; i < 8; i++) {
            if (inv.getItem(i) != null) {
                player.setCooldown(inv.getItem(i).getType(), 0);
            }
        }
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

            if (Objects.equals(Kit.equippedKits.get(whoHit.getUniqueId()).name, name)) {

                if (Objects.requireNonNull(whoWasHit.getLastDamageCause()).getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
                    doKillMessage(whoWasHit, whoHit, projectileDeathMessage, projectileKillMessage);
                } else {
                    doKillMessage(whoWasHit, whoHit, deathMessage, killMessage);
                }
            }
        }
    }

    private void doKillMessage(Player whoWasHit, Player whoHit, String[] deathMessage, String[] killMessage) {
        DeathscoresAsync.doStats(whoHit, whoWasHit);

        whoWasHit.sendMessage(deathMessage[0] + NametagsEvent.color(whoHit) + whoHit.getName()
                + ChatColor.RESET + deathMessage[1]);
        whoHit.sendMessage(killMessage[0] + NametagsEvent.color(whoWasHit) + whoWasHit.getName()
                + ChatColor.RESET + killMessage[1] + ChatColor.GRAY + " (" + DeathscoresAsync.returnKillstreak(whoHit) + ")");
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
