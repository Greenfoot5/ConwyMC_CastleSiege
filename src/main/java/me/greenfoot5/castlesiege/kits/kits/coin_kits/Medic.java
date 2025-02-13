package me.greenfoot5.castlesiege.kits.kits.coin_kits;

import me.greenfoot5.castlesiege.Main;
import me.greenfoot5.castlesiege.database.UpdateStats;
import me.greenfoot5.castlesiege.events.combat.InCombat;
import me.greenfoot5.castlesiege.kits.items.CSItemCreator;
import me.greenfoot5.castlesiege.kits.items.EquipmentSet;
import me.greenfoot5.castlesiege.kits.kits.CoinKit;
import me.greenfoot5.castlesiege.kits.kits.Kit;
import me.greenfoot5.castlesiege.maps.TeamController;
import me.greenfoot5.castlesiege.misc.CSNameTag;
import me.greenfoot5.conwymc.data_types.Tuple;
import me.greenfoot5.conwymc.util.Messenger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Cake;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * The medic kit
 */
public class Medic extends CoinKit implements Listener {

    private static final int health = 210;
    private static final double regen = 15;
    private static final double meleeDamage = 30;
    private static final int ladderCount = 4;
    private static final int cakeCount = 16;
    private static final int BANDAGE_COOLDOWN_TICKS = 39;

    public static final HashSet<Block> cakes = new HashSet<>();

    /**
     * Set the equipment and attributes of this kit
     */
    public Medic() {
        super("Medic", health, regen, Material.CAKE);
        super.canSeeHealth = true;

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();

        // Weapon
        es.hotbar[0] = CSItemCreator.weapon(new ItemStack(Material.WOODEN_SWORD),
                Component.text("Scalpel", NamedTextColor.GREEN),
                List.of(Component.empty(),
                        Component.text("30 Melee Damage", NamedTextColor.DARK_GREEN)),
                null, meleeDamage);
        // Voted Weapon
        es.votedWeapon = new Tuple<>(
                CSItemCreator.weapon(new ItemStack(Material.WOODEN_SWORD),
                        Component.text("Scalpel", NamedTextColor.GREEN),
                        List.of(Component.empty(),
                                Component.text("32 Melee Damage", NamedTextColor.DARK_GREEN),
								Component.text("⁎ Voted: +2 Melee Damage", NamedTextColor.DARK_AQUA)),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOTING, 0)), meleeDamage + 2),
                0);

        // Chestplate
        es.chest = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE),
                Component.text("Leather Chestplate", NamedTextColor.GREEN),
                List.of(Component.empty(),
                        Component.text(health + " HP", NamedTextColor.DARK_GREEN),
                        Component.text(regen + " Regen", NamedTextColor.DARK_GREEN)), null,
                Color.fromRGB(255, 255, 255));

        // Leggings
        es.legs = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_LEGGINGS),
                Component.text("Leather Leggings", NamedTextColor.GREEN),
                List.of(Component.empty(),
                        Component.text(health + " HP", NamedTextColor.DARK_GREEN),
                        Component.text(regen + " Regen", NamedTextColor.DARK_GREEN)), null,
                Color.fromRGB(255, 255, 255));

        // Boots
        es.feet = CSItemCreator.item(new ItemStack(Material.GOLDEN_BOOTS),
                Component.text("Golden Boots", NamedTextColor.GREEN),
                List.of(Component.empty(),
                        Component.text(health + " HP", NamedTextColor.DARK_GREEN),
                        Component.text(regen + " Regen", NamedTextColor.DARK_GREEN)), null);
        // Voted Boots
        es.votedFeet = CSItemCreator.item(new ItemStack(Material.GOLDEN_BOOTS),
                Component.text("Golden Boots", NamedTextColor.GREEN),
                List.of(Component.empty(),
                        Component.text(health + " HP", NamedTextColor.DARK_GREEN),
                        Component.text(regen + " Regen", NamedTextColor.DARK_GREEN),
                        Component.empty(),
                        Component.text("⁎ Voted: Depth Strider II", NamedTextColor.DARK_AQUA)),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)));

        // Bandages
        es.hotbar[1] = CSItemCreator.item(new ItemStack(Material.PAPER),
                Component.text("Bandages", NamedTextColor.DARK_AQUA),
                Collections.singletonList(Component.text("Right click teammates to heal.", NamedTextColor.AQUA)), null);

        // Cake
        es.hotbar[2] = CSItemCreator.item(new ItemStack(Material.CAKE, cakeCount),
                Component.text("Healing Cake", NamedTextColor.DARK_AQUA),
                List.of(Component.text("Place the cake down, then", NamedTextColor.AQUA),
                        Component.text("teammates can heal from it.", NamedTextColor.AQUA)), null);

        // Self Potion
        es.hotbar[3] = healthPotion();

        // Ladders
        es.hotbar[4] = new ItemStack(Material.LADDER, ladderCount);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, ladderCount + 2), 4);

        super.equipment = es;

        // Perm Potion Effect
        super.potionEffects.add(new PotionEffect(PotionEffectType.SPEED, 999999, 0));

        super.killMessage[0] = " dissected ";
        super.deathMessage[0] = "You had your insides examined by ";
    }

    /**
     * @return The medic's health potion
     */
    private ItemStack healthPotion() {
        ItemStack itemStack = new ItemStack(Material.POTION);
        PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
        assert potionMeta != null;

        potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.INSTANT_HEALTH, 1, 6), true);
        potionMeta.setColor(Color.RED);
        potionMeta.displayName(Component.text("Health Potion", NamedTextColor.RED));
        itemStack.setItemMeta(potionMeta);

        return itemStack;
    }

    /**
     * Place a cake
     * @param event The event called when placing a cake
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlace(BlockPlaceEvent event) {
        if (event.getPlayer() == equippedPlayer
                && InCombat.isPlayerInLobby(equippedPlayer.getUniqueId())
                && event.getBlockPlaced().getType() == Material.CAKE) {

            // Check you aren't placing on a cake
            if (event.getBlockAgainst() instanceof Cake) {
                event.setCancelled(true);
                return;
            }

            event.setCancelled(false);
            cakes.add(event.getBlockPlaced());
        }
    }

    /**
     * Break a cake
     * @param e The event called when breaking a cake
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onBreakCake(BlockBreakEvent e) {
        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(e.getPlayer().getUniqueId())) {
            return;
        }

        Block cake = e.getBlock();
        if (cake.getType() == Material.CAKE
                && cakes.contains(cake)) {
            e.setCancelled(true);

            Player p = e.getPlayer();
            destroyCake(cake);
            String cakeType = TeamController.getTeam(p.getUniqueId())
                    == TeamController.getTeam(equippedPlayer.getUniqueId()) ? " friendly" : "n enemy";
            Messenger.sendWarning("Your cake was destroyed by " + CSNameTag.mmUsername(p), equippedPlayer);

            Messenger.sendActionInfo("You destroyed a" + cakeType + " cake", p);
        }
    }

    /**
     * Activate the medic ability of healing a teammate
     * @param event The event called when clicking on a teammate with paper
     */
    @EventHandler
    public void onHeal(PlayerInteractEntityEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
            // Prevent using in lobby
            if (event.getPlayer() != equippedPlayer
                    || InCombat.isPlayerInLobby(equippedPlayer.getUniqueId())) {
                return;
            }

            PlayerInventory i = equippedPlayer.getInventory();
            if ((i.getItemInMainHand().getType() == Material.PAPER)
                    && event.getRightClicked() instanceof Player patient
                    && TeamController.getTeam(equippedPlayer.getUniqueId()) == TeamController.getTeam(patient.getUniqueId())
                    && patient.getHealth() < Kit.equippedKits.get(patient.getUniqueId()).baseHealth
                    && equippedPlayer.getCooldown(Material.PAPER) == 0) {

                // Apply cooldown
                equippedPlayer.setCooldown(Material.PAPER, BANDAGE_COOLDOWN_TICKS);

                // Heal
                addPotionEffect(patient, new PotionEffect(PotionEffectType.REGENERATION, 40, 9));
                addPotionEffect(equippedPlayer, new PotionEffect(PotionEffectType.RESISTANCE, 60, 0));
                Messenger.sendHealing(CSNameTag.mmUsername(equippedPlayer) + " is healing you", patient);
                Messenger.sendHealing("You are healing " + CSNameTag.mmUsername(patient), equippedPlayer);
                UpdateStats.addHeals(equippedPlayer.getUniqueId(), 1);
            }
        });
    }

    /**
     * Add a potion effect to a player.
     * @param player The player
     * @param potion The potion effect
     */
    private void addPotionEffect(Player player, PotionEffect potion) {
        Bukkit.getScheduler().runTask(Main.plugin, () -> player.addPotionEffect(potion));
    }

    /**
     * Remove a cake when its placer dies
     * @param e The event called when a player dies
     */
    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        if (e.getEntity() == equippedPlayer)
            destroyCakes();
    }

    /**
     * Remove a cake when its placer leaves the game
     * @param e The event called when a player leaves the game
     */
    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        if (e.getPlayer() == equippedPlayer)
            destroyCakes();
    }

    /**
     * Destroy the player's cake if present
     */
    private void destroyCake(Block block) {
        boolean removed = cakes.remove(block);
        if (removed) {
            block.setType(Material.AIR);
        }
    }

    /**
     * Destroy the player's cake if present
     */
    private void destroyCakes() {
        for (Block cake : cakes) {
            cake.setType(Material.AIR);
        }
        cakes.clear();
    }

    /**
     * Activate the medic potion
     * @param e The event called when clicking with the potion in hand
     */
    @EventHandler
    public void drinkPotion(PlayerInteractEvent e) {
        if (InCombat.isPlayerInLobby(equippedPlayer.getUniqueId()))
            return;
        if (e.getPlayer() == equippedPlayer)
            return;

        if (e.getHand() == null || e.getItem() == null || e.getItem().getType() != Material.POTION) {
            return;
        }

        ItemStack bottle = new ItemStack(Material.GLASS_BOTTLE);

        switch (e.getHand()) {
            case HAND:
                equippedPlayer.getInventory().setItemInMainHand(bottle);
                break;
            case OFF_HAND:
                equippedPlayer.getInventory().setItemInOffHand(bottle);
                break;
            default:
                return;
        }

        // Potion effects
        equippedPlayer.addPotionEffect(new PotionEffect(PotionEffectType.INSTANT_HEALTH, 1, 6));
    }

    /**
     * @return The lore to add to the kit gui item
     */
    @Override
    public ArrayList<Component> getGuiDescription() {
        ArrayList<Component> kitLore = new ArrayList<>();
        kitLore.add(Component.text("Our classic healer, which makes", NamedTextColor.GRAY));
        kitLore.add(Component.text("use of bandages and cake to heal allies", NamedTextColor.GRAY));
        kitLore.addAll(getBaseStats(health, regen, meleeDamage, ladderCount));
        kitLore.add(Component.text(cakeCount, color)
                        .append(Component.text(" Cakes", NamedTextColor.GRAY)));
        kitLore.add(Component.empty());
        kitLore.add(Component.text("Effects:", NamedTextColor.DARK_PURPLE));
        kitLore.add(Component.text("- Speed I", NamedTextColor.GRAY));
        kitLore.add(Component.empty());
        kitLore.add(Component.text("Active:", NamedTextColor.GOLD));
        kitLore.add(Component.text("- Can use bandages to heal teammates", NamedTextColor.GRAY));
        kitLore.add(Component.text("- Can place cakes (1 active max)", NamedTextColor.GRAY));
        kitLore.add(Component.empty());
        kitLore.add(Component.text("Passive:", NamedTextColor.DARK_GREEN));
        kitLore.add(Component.text("- When healing an ally receives resistance I", NamedTextColor.GRAY));
        return kitLore;
    }
}
