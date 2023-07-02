package me.huntifi.castlesiege.kits.kits.voter_kits;

import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.castlesiege.events.EnderchestEvent;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.kits.kits.VoterKit;
import me.huntifi.castlesiege.maps.TeamController;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

/**
 * The fire archer kit
 */
public class FireArcher extends VoterKit implements Listener {

    private static final int health = 240;
    private static final double regen = 9;
    private static final double meleeDamage = 36;
    private static final int knockbackLevel = 1;
    private static final int ladderCount = 4;
    private static final int arrowCount = 39;
    private static final int bowPowerLevel = 12;
    private static final double arrowDamage = 15;

    private static final int fireArrowCreationCooldown = 30;
    private static final int fireArrowHoldLimit = 9;


    public static final HashMap<Player, Block> cauldrons = new HashMap<>();
    private final ItemStack fireArrow;
    private final ItemStack firepit;
    private final ItemStack firepitVoted;

    /**
     * Set the equipment and attributes of this kit
     */
    public FireArcher() {
        super("Fire Archer", health, regen, Material.BLAZE_POWDER);

        // Equipment stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Chestplate
        es.chest = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE),
                ChatColor.GREEN + "Leather Chestplate", null, null,
                Color.fromRGB(204, 0, 0));

        // Leggings
        es.legs = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_LEGGINGS),
                ChatColor.GREEN + "Leather Leggings", null, null,
                Color.fromRGB(255, 128, 0));

        // Boots
        es.feet = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_BOOTS),
                ChatColor.GREEN + "Leather Boots", null, null,
                Color.fromRGB(204, 0, 0));
        // Voted Boots
        es.votedFeet = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_BOOTS),
                ChatColor.GREEN + "Leather Boots",
                Collections.singletonList(ChatColor.AQUA + "- voted: Depth Strider II"),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)),
                Color.fromRGB(204, 0, 0));

        // Ladders
        es.hotbar[2] = new ItemStack(Material.LADDER, ladderCount);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, ladderCount + 2), 2);

        // Bow
        es.hotbar[0] = ItemCreator.item(new ItemStack(Material.BOW),
                ChatColor.GREEN + "Bow", null,
                Collections.singletonList(new Tuple<>(Enchantment.ARROW_DAMAGE, bowPowerLevel)));

        // Firepit
        firepit = ItemCreator.weapon(new ItemStack(Material.CAULDRON),
                ChatColor.GREEN + "Firepit", Arrays.asList("",
                        ChatColor.AQUA + "Place the firepit down, then",
                        ChatColor.AQUA + "right click it with an arrow.", "",
                        ChatColor.AQUA + "(tip): This firepit is very hard, so you",
                        ChatColor.AQUA + "can beat your enemies to death with it."),
                Collections.singletonList(new Tuple<>(Enchantment.KNOCKBACK, knockbackLevel)), meleeDamage);
        es.hotbar[1] = firepit;
        // Voted Firepit
        firepitVoted = ItemCreator.weapon(new ItemStack(Material.CAULDRON),
                ChatColor.GREEN + "Firepit", Arrays.asList("",
                        ChatColor.AQUA + "Place the firepit down, then",
                        ChatColor.AQUA + "right click it with an arrow.", "",
                        ChatColor.AQUA + "- voted: + 2 damage.",
                        ChatColor.AQUA + "(tip): This firepit is very hard, so you",
                        ChatColor.AQUA + "can beat your enemies to death with it."),
                Arrays.asList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0),
                        new Tuple<>(Enchantment.KNOCKBACK, knockbackLevel)), meleeDamage + 2);
        es.votedWeapon = new Tuple<>(firepitVoted, 1);

        // Fire Arrows
        fireArrow = ItemCreator.item(new ItemStack(Material.TIPPED_ARROW),
                ChatColor.GOLD + "Fire Arrow", null, null);
        PotionMeta potionMeta = (PotionMeta) fireArrow.getItemMeta();
        assert potionMeta != null;
        potionMeta.setColor(Color.ORANGE);
        fireArrow.setItemMeta(potionMeta);

        // Arrows
        es.hotbar[7] = new ItemStack(Material.ARROW, arrowCount);
        ItemStack initialFireArrows = fireArrow.clone();
        initialFireArrows.setAmount(fireArrowHoldLimit);
        es.hotbar[3] = initialFireArrows;

        super.equipment = es;

        // Perm Potion Effect
        super.potionEffects.add(new PotionEffect(PotionEffectType.SLOW, 999999, 0));
    }

    /**
     * Place a firepit
     * @param e The event called when placing a cauldron
     */
    @EventHandler (priority = EventPriority.HIGHEST)
    public void onPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(p.getUniqueId())) {
            return;
        }

        if (Objects.equals(Kit.equippedKits.get(p.getUniqueId()).name, name) &&
                e.getBlockPlaced().getType() == Material.CAULDRON) {

            // Destroy old cauldron
            destroyFirepit(p);

            // Place new cauldron
            e.setCancelled(false);
            cauldrons.put(p, e.getBlockPlaced());
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                    TextComponent.fromLegacyText(ChatColor.AQUA + "You placed down your Firepit!"));
            for (PotionEffect effect : p.getActivePotionEffects()) {
                if (effect.getType().getName().equals(PotionEffectType.SLOW.getName()) && effect.getAmplifier() == 0) {
                    p.removePotionEffect(effect.getType());
                }
            }
        }
    }

    /**
     * Destroy a cauldron
     * @param e The event called when left-clicking a cauldron
     */
    @EventHandler
    public void onRemove(PlayerInteractEvent e) {
        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(e.getPlayer().getUniqueId())) {
            return;
        }

        if (e.getAction() == Action.LEFT_CLICK_BLOCK &&
                e.getClickedBlock().getType() == Material.CAULDRON) {
            Player p = e.getPlayer();
            Player q = getPlacer(e.getClickedBlock());

            // Pick up own firepit
            if (Objects.equals(p, q)) {
                destroyFirepit(q);
                q.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                        TextComponent.fromLegacyText(ChatColor.AQUA + "You took back your Firepit!"));
                // Perm Potion Effect
                q.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 999999, 0));

                // Can only hold 1 firepit at a time
                PlayerInventory inv = p.getInventory();
                if (inv.getItemInOffHand().getType() != Material.CAULDRON &&
                        !inv.contains(Material.CAULDRON)) {
                    if (!ActiveData.getData(p.getUniqueId()).hasVote("sword")) {
                        p.getInventory().addItem(firepit);
                    } else {
                        p.getInventory().addItem(firepitVoted);
                    }
                }

            // Destroy enemy firepit
            } else if (q != null &&
                    TeamController.getTeam(p.getUniqueId()) != TeamController.getTeam(q.getUniqueId())){
                destroyFirepit(q);
                p.playSound(e.getClickedBlock().getLocation(), Sound.ENTITY_ZOMBIE_INFECT , 5, 1);
                p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                        TextComponent.fromLegacyText(ChatColor.RED + "You kicked over " + q.getName() + "'s Firepit!"));
            }
        }
    }

    /**
     * Light an arrow
     * @param e The event called when right-clicking a cauldron with an arrow
     */
    @EventHandler
    public void onUseFirepit(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getItem() == null) { return; }
        ItemStack usedItem = e.getItem();

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(p.getUniqueId())) {
            return;
        }

        // Check if a fire archer tries to light an arrow, while off-cooldown
        if (Objects.equals(Kit.equippedKits.get(p.getUniqueId()).name, name) &&
                e.getAction() == Action.RIGHT_CLICK_BLOCK &&
                e.getClickedBlock().getType() == Material.CAULDRON &&
                usedItem != null &&
                usedItem.getType() == Material.ARROW &&
                p.getCooldown(Material.ARROW) == 0) {

            // Check if the player may light an arrow using this cauldron
            Player q = getPlacer(e.getClickedBlock());
            if (q != null &&
                    TeamController.getTeam(p.getUniqueId()) == TeamController.getTeam(q.getUniqueId())) {

                // Check if the player may light any more arrows
                PlayerInventory inv = p.getInventory();
                ItemStack offHand = inv.getItemInOffHand();
                int fireOffHand = offHand.getType() == Material.TIPPED_ARROW ? offHand.getAmount() : 0;
                if (!inv.contains(Material.TIPPED_ARROW, fireArrowHoldLimit - fireOffHand)) {
                    // Light an arrow
                    p.setCooldown(Material.ARROW, fireArrowCreationCooldown);
                    usedItem.setAmount(usedItem.getAmount() - 1);
                    inv.addItem(fireArrow);
                    p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 0.5f);
                    Messenger.sendActionInfo("You light an arrow.", p);
                } else {
                    Messenger.sendActionError("You can't hold more than " + fireArrowHoldLimit + " lit arrows at a time.", p);
                }
            }
        }
    }

    /**
     * Turn orange arrows into flaming arrows
     * @param e The event called when shooting an arrow with a bow
     */
    @EventHandler
    public void FlameBow(EntityShootBowEvent e) {
        if (e.isCancelled()) {
            return;
        }

        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (Objects.equals(Kit.equippedKits.get(p.getUniqueId()).name, name)) {

                try {
                    Arrow a = (Arrow) e.getProjectile();
                    if (Objects.equals(a.getColor(), Color.ORANGE)) {
                        a.setFireTicks(260);
                    }
                } catch (Exception ex) {
                    // No fire arrow was shot
                }
            }
        }
    }

    /**
     * Set the arrow-damage of a Fire Archer's arrows
     * @param e The event called when a player is hit by an arrow
     */
    @EventHandler (priority = EventPriority.LOW)
    public void onArrowHit(ProjectileHitEvent e) {
        if (e.getEntity() instanceof Arrow &&
                e.getEntity().getShooter() instanceof Player &&
                Objects.equals(Kit.equippedKits.get(((Player) e.getEntity().getShooter()).getUniqueId()).name, name)) {
            ((Arrow) e.getEntity()).setDamage(arrowDamage);
        }
    }

    /**
     * Destroy a player's firepit when they click an enderchest.
     * @param event The event called when an off-cooldown player interacts with an enderchest
     */
    @EventHandler
    public void onClickEnderchest(EnderchestEvent event) {
        destroyFirepit(event.getPlayer());
    }

    /**
     * Destroy a player's firepit when they die
     * @param e The event called when a player dies
     */
    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        destroyFirepit(e.getEntity());
    }

    /**
     * Destroy a player's firepit when they leave the game
     * @param e The event called when a player leaves the game
     */
    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        destroyFirepit(e.getPlayer());
    }

    /**
     * Destroy the player's firepit if present
     * @param p The player whose firepit to destroy
     */
    private void destroyFirepit(Player p) {
        if(cauldrons.containsKey(p)) {
            cauldrons.get(p).setType(Material.AIR);
            cauldrons.remove(p);
        }
    }

    /**
     * Get the placer of a firepit
     * @param cauldron The firepit whose placer to find
     * @return The placer of the firepit, null of not placed by a fire archer
     */
    private Player getPlacer(Block cauldron) {
        return cauldrons.entrySet().stream()
                .filter(entry -> Objects.equals(entry.getValue(), cauldron))
                .findFirst().map(Map.Entry::getKey)
                .orElse(null);
    }
}
