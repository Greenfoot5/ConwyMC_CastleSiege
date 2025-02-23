package me.greenfoot5.castlesiege.kits.kits.voter_kits;

import me.greenfoot5.castlesiege.database.CSActiveData;
import me.greenfoot5.castlesiege.events.EnderchestEvent;
import me.greenfoot5.castlesiege.events.combat.InCombat;
import me.greenfoot5.castlesiege.kits.items.CSItemCreator;
import me.greenfoot5.castlesiege.kits.items.EquipmentSet;
import me.greenfoot5.castlesiege.kits.kits.VoterKit;
import me.greenfoot5.castlesiege.maps.TeamController;
import me.greenfoot5.castlesiege.misc.CSNameTag;
import me.greenfoot5.conwymc.data_types.Tuple;
import me.greenfoot5.conwymc.util.Messenger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

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

    private Block placedPit;
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

        // Chestplate
        es.chest = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE),
                Component.text("Leather Chestplate", NamedTextColor.GREEN), null, null,
                Color.fromRGB(204, 0, 0));

        // Leggings
        es.legs = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_LEGGINGS),
                Component.text("Leather Leggings", NamedTextColor.GREEN), null, null,
                Color.fromRGB(255, 128, 0));

        // Boots
        es.feet = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_BOOTS),
                Component.text("Leather Boots", NamedTextColor.GREEN), null, null,
                Color.fromRGB(204, 0, 0));
        // Voted Boots
        es.votedFeet = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_BOOTS),
                Component.text("Leather Boots", NamedTextColor.GREEN),
                Collections.singletonList(Component.text("⁎ Voted: Depth Strider II", NamedTextColor.AQUA)),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)),
                Color.fromRGB(204, 0, 0));

        // Ladders
        es.hotbar[2] = new ItemStack(Material.LADDER, ladderCount);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, ladderCount + 2), 2);

        // Bow
        es.hotbar[0] = CSItemCreator.item(new ItemStack(Material.BOW),
                Component.text("Bow", NamedTextColor.GREEN), null,
                Collections.singletonList(new Tuple<>(Enchantment.POWER, bowPowerLevel)));

        // Firepit
        firepit = CSItemCreator.weapon(new ItemStack(Material.CAULDRON),
                Component.text("Firepit", NamedTextColor.GREEN),
                Arrays.asList(Component.empty(),
                        Component.text("Place the firepit down, then", NamedTextColor.AQUA),
                        Component.text( "right click it with an arrow.", NamedTextColor.AQUA),
                        Component.empty(),
                        Component.text( "(tip): This firepit is very hard, so you", NamedTextColor.AQUA),
                        Component.text( "can beat your enemies to death with it.", NamedTextColor.AQUA)),
                Collections.singletonList(new Tuple<>(Enchantment.KNOCKBACK, knockbackLevel)), meleeDamage);
        es.hotbar[1] = firepit;
        // Voted Firepit
        firepitVoted = CSItemCreator.weapon(new ItemStack(Material.CAULDRON),
                Component.text("Firepit", NamedTextColor.GREEN),
                Arrays.asList(Component.empty(),
                        Component.text("Place the firepit down, then", NamedTextColor.AQUA),
                        Component.text( "right click it with an arrow.", NamedTextColor.AQUA),
                        Component.empty(),
                        Component.text( "⁎ Voted: + 2 damage.", NamedTextColor.AQUA),
                        Component.text( "(tip): This firepit is very hard, so you", NamedTextColor.AQUA),
                        Component.text( "can beat your enemies to death with it.", NamedTextColor.AQUA)),
                Arrays.asList(new Tuple<>(Enchantment.LOOTING, 0),
                        new Tuple<>(Enchantment.KNOCKBACK, knockbackLevel)), meleeDamage + 2);
        es.votedWeapon = new Tuple<>(firepitVoted, 1);

        // Fire Arrows
        fireArrow = CSItemCreator.item(new ItemStack(Material.TIPPED_ARROW),
                Component.text("Fire Arrow", NamedTextColor.GOLD), null, null);
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
        super.potionEffects.add(new PotionEffect(PotionEffectType.SLOWNESS, 999999, 0));
    }

    /**
     * Place a firepit
     * @param e The event called when placing a cauldron
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlace(BlockPlaceEvent e) {
        if (e.getPlayer() != equippedPlayer)
            return;

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(equippedPlayer.getUniqueId()))
            return;


        if (e.getBlockPlaced().getType() != Material.CAULDRON)
            return;


        // Destroy old cauldron
        destroyFirepit();

        // Place new cauldron
        e.setCancelled(false);
        placedPit = e.getBlockPlaced();
        Messenger.sendActionInfo("You placed down your Firepit", equippedPlayer);
        for (PotionEffect effect : equippedPlayer.getActivePotionEffects()) {
            if (effect.getType().equals(PotionEffectType.SLOWNESS) && effect.getAmplifier() == 0) {
                equippedPlayer.removePotionEffect(effect.getType());
            }
        }
    }

    /**
     * Pick up your own cauldron
     * @param e The event called when left-clicking a cauldron
     */
    @EventHandler
    public void onRemoveOwn(PlayerInteractEvent e) {
        if (e.getPlayer() != equippedPlayer)
            return;

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(equippedPlayer.getUniqueId()))
            return;

        if (e.getAction() != Action.LEFT_CLICK_BLOCK
                || e.getClickedBlock() == null
                || e.getClickedBlock().getType() != Material.CAULDRON) {
            return;
        }

        // Pick up own firepit
        if (e.getClickedBlock() == placedPit) {
            destroyFirepit();
            Messenger.sendActionInfo("You took back your Firepit", equippedPlayer);
            // Perm Potion Effect
            equippedPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 999999, 0));

            // Can only hold 1 firepit at a time
            PlayerInventory inv = equippedPlayer.getInventory();
            if (inv.getItemInOffHand().getType() != Material.CAULDRON &&
                    !inv.contains(Material.CAULDRON)) {
                if (!CSActiveData.getData(equippedPlayer.getUniqueId()).hasVote("sword")) {
                    equippedPlayer.getInventory().addItem(firepit);
                } else {
                    equippedPlayer.getInventory().addItem(firepitVoted);
                }
            }
        }
    }

    /**
     * Not equippedPlayer destroying cauldron
     * @param e The event called when left-clicking a cauldron
     */
    @EventHandler
    public void onEnemyRemove(PlayerInteractEvent e) {
        // Check if hit cauldron
        if (e.getAction() != Action.LEFT_CLICK_BLOCK
                || e.getClickedBlock() == null
                || e.getClickedBlock().getType() != Material.CAULDRON) {
            return;
        }

        if (TeamController.getTeam(equippedPlayer.getUniqueId()) == TeamController.getTeam(e.getPlayer().getUniqueId())) {
            Messenger.sendActionError("You can't destroy your team's cauldron!", e.getPlayer());
        } else if (TeamController.getTeam(equippedPlayer.getUniqueId()) != TeamController.getTeam(e.getPlayer().getUniqueId())) {
            destroyFirepit();
            e.getPlayer().playSound(e.getClickedBlock().getLocation(), Sound.ENTITY_ZOMBIE_INFECT , 5, 1);
            Messenger.sendActionSuccess("You kicked over " + CSNameTag.mmUsername(equippedPlayer) + "'s Firepit!", e.getPlayer());
            Messenger.sendWarning("Your firepit was kicked over!", equippedPlayer);
        } else {
            Messenger.sendActionError("That cauldron looks like it's been there a while, best to leave it there...", e.getPlayer());
        }
    }

    /**
     * Prevents crafting fire arrows by enemies
     * @param e Interact event
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void checkFriendlyFirepit(PlayerInteractEvent e) {
        if (e.getItem() == null || e.getItem().getType() != Material.ARROW)
            return;

        if (placedPit == null)
            return;

        if (TeamController.getTeam(e.getPlayer().getUniqueId()) != TeamController.getTeam(equippedPlayer.getUniqueId()))
        {
            Messenger.sendActionError("This is an enemy fire pit! Smack it!", e.getPlayer());
            e.setCancelled(true);
        }
    }

    /**
     * Light an arrow
     * @param e The event called when right-clicking a cauldron with an arrow
     */
    @EventHandler(ignoreCancelled = true)
    public void onUseFirepit(PlayerInteractEvent e) {
        if (e.getPlayer() != equippedPlayer)
            return;

        if (e.getItem() == null || e.getItem().getType() != Material.ARROW)
            return;

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(equippedPlayer.getUniqueId())) {
            return;
        }

        // Check if hit cauldron
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK
                || e.getClickedBlock() == null
                || e.getClickedBlock().getType() != Material.CAULDRON) {
            return;
        }

        // Check if a fire archer tries to light an arrow, while off-cooldown
        if (equippedPlayer.getCooldown(Material.ARROW) != 0)
            return;

        // Check if the player may light any more arrows
        PlayerInventory inv = equippedPlayer.getInventory();
        ItemStack offHand = inv.getItemInOffHand();
        int fireOffHand = offHand.getType() == Material.TIPPED_ARROW ? offHand.getAmount() : 0;
        if (!inv.contains(Material.TIPPED_ARROW, fireArrowHoldLimit - fireOffHand)) {
            // Light an arrow
            equippedPlayer.setCooldown(Material.ARROW, fireArrowCreationCooldown);
            e.getItem().setAmount(e.getItem().getAmount() - 1);
            inv.addItem(fireArrow);
            equippedPlayer.playSound(equippedPlayer.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 0.5f);
            Messenger.sendActionInfo("You light an arrow.", equippedPlayer);
        } else {
            Messenger.sendActionError("You can't hold more than " + fireArrowHoldLimit + " lit arrows at a time.", equippedPlayer);
        }
    }

    /**
     * Turn orange arrows into flaming arrows
     * @param e The event called when shooting an arrow with a bow
     */
    @EventHandler(ignoreCancelled = true)
    public void FlameBow(EntityShootBowEvent e) {
        if (e.getEntity() != equippedPlayer)
            return;

        if (!(e.getEntity() instanceof Arrow))
            return;

        Arrow a = (Arrow) e.getProjectile();
        if (Objects.equals(a.getColor(), Color.ORANGE)) {
            a.setFireTicks(260);
        }
    }

    /**
     * Set the arrow-damage of a Fire Archer's arrows
     * @param e The event called when a player is hit by an arrow
     */
    @EventHandler(priority = EventPriority.LOW)
    public void onArrowHit(ProjectileHitEvent e) {
        if (e.getEntity().getShooter() != equippedPlayer)
            return;

        if (e.getEntity() instanceof Arrow) {
            ((Arrow) e.getEntity()).setDamage(arrowDamage);
        }
    }

    /**
     * Destroy a player's firepit when they click an enderchest.
     * @param event The event called when an off-cooldown player interacts with an enderchest
     */
    @EventHandler
    public void onClickEnderchest(EnderchestEvent event) {
        destroyFirepit();
    }

    /**
     * Destroy a player's firepit when they die
     * @param e The event called when a player dies
     */
    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        destroyFirepit();
    }

    /**
     * Destroy a player's firepit when they leave the game
     * @param e The event called when a player leaves the game
     */
    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        destroyFirepit();
    }

    /**
     * Destroy the player's firepit if present
     */
    private void destroyFirepit() {
        if (placedPit != null) {
            placedPit.setType(Material.AIR);
            placedPit = null;
        }
    }

    /**
     * @return The lore to add to the kit gui item
     */
    @Override
    public ArrayList<Component> getGuiDescription() {
        ArrayList<Component> kitLore = new ArrayList<>();
        kitLore.add(Component.text("Ranged kit that can craft and", NamedTextColor.GRAY));
        kitLore.add(Component.text("shoot flaming arrows", NamedTextColor.GRAY));
        kitLore.addAll(getBaseStats(health, regen, meleeDamage, arrowDamage, ladderCount, arrowCount));
        kitLore.add(Component.empty());
        kitLore.add(Component.text("Effects:", NamedTextColor.DARK_PURPLE));
        kitLore.add(Component.text("- Slowness I (with fire pit in ", NamedTextColor.GRAY));
        kitLore.add(Component.text("inventory)", NamedTextColor.GRAY));
        kitLore.add(Component.empty());
        kitLore.add(Component.text("Active:", NamedTextColor.GOLD));
        kitLore.add(Component.text("- Can place and pickup their fire pit", NamedTextColor.GRAY));
        kitLore.add(Component.text("- Can craft fire arrows using the fire pit", NamedTextColor.GRAY));
        return kitLore;
    }
}
