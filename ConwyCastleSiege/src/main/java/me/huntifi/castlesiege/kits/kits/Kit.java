package me.huntifi.castlesiege.kits.kits;

import me.huntifi.castlesiege.Deathmessages.DeathscoresAsync;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.WoolHat;
import me.huntifi.castlesiege.tags.NametagsEvent;
import me.libraryaddict.disguise.DisguiseAPI;
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

/**
 * The abstract kit
 */
public abstract class Kit implements Listener {
    public String name;
    public int baseHealth;
    protected int kbResistance = 0;

    // Equipment
    protected EquipmentSet equipment;
    protected int heldItemSlot = 0;
    protected ArrayList<PotionEffect> potionEffects;

    // Messages
    protected String[] deathMessage;
    protected String[] killMessage;
    protected String[] projectileDeathMessage;
    protected String[] projectileKillMessage;

    // Player Tracking
    public List<UUID> players;
    public static Map<UUID, Kit> equippedKits = new HashMap<>();

    /**
     * Create a kit with basic settings
     * @param name This kit's name
     * @param baseHealth This kit's base health
     */
    public Kit(String name, int baseHealth) {
        this.name = name;
        this.baseHealth = baseHealth;
        players = new ArrayList<>();

        // Equipment
        equipment = new EquipmentSet();
        potionEffects = new ArrayList<>();

        // Messages
        deathMessage = new String[]{"You were killed by ", ""};
        killMessage = new String[]{"You killed ", ""};
        projectileDeathMessage = new String[]{"You were shot by ", ""};
        projectileKillMessage = new String[]{"You shot ", ""};
    }

    /**
     * Give the items and attributes of this kit to a player
     * @param uuid The unique id of the player to whom this kit is given
     */
    public void setItems(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) { return; }

        // Health
        AttributeInstance healthAttribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        assert healthAttribute != null;
        healthAttribute.setBaseValue(baseHealth);
        player.setHealthScaled(true);
        player.setHealth(baseHealth);

        // Knockback resistance
        AttributeInstance kbAttribute = player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE);
        assert kbAttribute != null;
        kbAttribute.setBaseValue(kbResistance);

        // Items
        refillItems(uuid);

        // Change disguise
        disguise(player);
    }

    /**
     * Reset the player's items and reapply their potion effects
     * @param uuid The unique id of the player for whom to perform the refill
     */
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

    /**
     * Reset all item cooldowns of items the player's hotbar
     * @param uuid The unique id of the player for whom to reset the cooldowns
     */
    private void resetCooldown(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) { return; }

        PlayerInventory inv = player.getInventory();
        for (int i = 0; i < 8; i++) {
            if (inv.getItem(i) != null) {
                player.setCooldown(Objects.requireNonNull(inv.getItem(i)).getType(), 0);
            }
        }
    }

    /**
     * Remove all potion effects from the player and apply this kit's potion effects
     * @param uuid The unique id of the player for whom to apply the potion effects
     */
    private void applyPotionEffects(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) { return; }
        player.getActivePotionEffects().clear();
        player.addPotionEffects(potionEffects);
    }

    /**
     * Handle a player's death
     * @param e The event called when a player dies
     */
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

    /**
     * Send kill and death messages to the players
     * @param whoWasHit The player who died
     * @param whoHit The player who killed
     * @param deathMessage The message sent to the person who died
     * @param killMessage The message sent to the killer
     */
    private void doKillMessage(Player whoWasHit, Player whoHit, String[] deathMessage, String[] killMessage) {
        DeathscoresAsync.doStats(whoHit, whoWasHit);

        whoWasHit.sendMessage(deathMessage[0] + NametagsEvent.color(whoHit) + whoHit.getName()
                + ChatColor.RESET + deathMessage[1]);
        whoHit.sendMessage(killMessage[0] + NametagsEvent.color(whoWasHit) + whoWasHit.getName()
                + ChatColor.RESET + killMessage[1] + ChatColor.GRAY + " (" + DeathscoresAsync.returnKillstreak(whoHit) + ")");
    }

    /**
     * Register the player as using this kit and set their items
     * @param uuid The unique id of the player to register
     */
    public void addPlayer(UUID uuid) {
        players.add(uuid);
        Player player = Bukkit.getPlayer(uuid);
        setItems(uuid);
        equippedKits.put(uuid, this);

        assert player != null;
        // Kills the player if they have spawned this life, otherwise heal them
        if (!InCombat.isPlayerInLobby(uuid)) {
            player.setHealth(0);
        } else {
            player.setHealth(Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue());
        }
    }

    /**
     * Remove a player's disguise and reapply their nametag color
     * @param p The player to (un)disguise
     */
    protected void disguise(Player p) {
        if (DisguiseAPI.isDisguised(p)) {
            DisguiseAPI.undisguiseToAll(p);
            NametagsEvent.GiveNametag(p);
        }
    }
}
