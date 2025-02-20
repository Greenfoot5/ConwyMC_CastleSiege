package me.greenfoot5.castlesiege.kits.kits.level_kits;

import me.greenfoot5.castlesiege.Main;
import me.greenfoot5.castlesiege.database.UpdateStats;
import me.greenfoot5.castlesiege.events.combat.InCombat;
import me.greenfoot5.castlesiege.kits.items.CSItemCreator;
import me.greenfoot5.castlesiege.kits.items.EquipmentSet;
import me.greenfoot5.castlesiege.kits.kits.Kit;
import me.greenfoot5.castlesiege.kits.kits.LevelKit;
import me.greenfoot5.castlesiege.maps.TeamController;
import me.greenfoot5.castlesiege.misc.CSNameTag;
import me.greenfoot5.conwymc.data_types.Tuple;
import me.greenfoot5.conwymc.util.Messenger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Like a normal medic, but a better fighter
 */
public class BattleMedic extends LevelKit implements Listener {

    private static final int health = 300;
    private static final double regen = 10.5;
    private static final double meleeDamage = 40.5;
    private static final int ladderCount = 4;

    private static final int bandageCount = 16;

    private static final int level = 15;
    public static final int BANDAGE_COOLDOWN_TICKS = 39;

    /**
     * Creates a new battle medic
     */
    public BattleMedic() {
        super("Battle Medic", health, regen, Material.PAPER, level);

        super.canSeeHealth = true;

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();

        // Weapon
        es.hotbar[0] = CSItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                Component.text("Short-sword", NamedTextColor.GREEN), null, null, meleeDamage);
        // Voted Weapon
        es.votedWeapon = new Tuple<>(
                CSItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                        Component.text("Short-sword", NamedTextColor.GREEN),
                        Collections.singletonList(Component.text("⁎ Voted: +2 damage", NamedTextColor.AQUA)),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOTING, 0)), meleeDamage + 2),
                0);

        // Chestplate
        es.chest = CSItemCreator.item(new ItemStack(Material.IRON_CHESTPLATE),
                Component.text("Iron Chestplate", NamedTextColor.GREEN), null, null);
        ItemMeta chest = es.chest.getItemMeta();
        ArmorMeta chestMeta = (ArmorMeta) chest;
        assert chest != null;
        ArmorTrim chestTrim = new ArmorTrim(TrimMaterial.REDSTONE, TrimPattern.SENTRY);
        ((ArmorMeta) chest).setTrim(chestTrim);
        es.chest.setItemMeta(chestMeta);

        // Leggings
        es.legs = CSItemCreator.item(new ItemStack(Material.CHAINMAIL_LEGGINGS),
                Component.text("Chainmail Leggings", NamedTextColor.GREEN), null, null);

        // Boots
        es.feet = CSItemCreator.item(new ItemStack(Material.IRON_BOOTS),
                Component.text("Iron Boots", NamedTextColor.GREEN), null, null);
        // Voted Boots
        es.votedFeet = CSItemCreator.item(new ItemStack(Material.IRON_BOOTS),
                Component.text("Iron Boots", NamedTextColor.GREEN),
                Collections.singletonList(Component.text("⁎ Voted: Depth Strider II", NamedTextColor.AQUA)),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)));

        // Bandages
        es.hotbar[1] = CSItemCreator.item(new ItemStack(Material.PAPER, bandageCount),
                Component.text("Bandages", NamedTextColor.DARK_AQUA),
                Collections.singletonList(Component.text("Right click teammates to heal.", NamedTextColor.AQUA)), null);

        // Ladders
        es.hotbar[2] = new ItemStack(Material.LADDER, 4);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, 6), 2);

        super.equipment = es;

        super.killMessage[0] = " dissected ";
        super.deathMessage[0] = "You had your insides examined by ";
    }


    /**
     * Activate the medic ability of healing a teammate
     * @param event The event called when clicking on a teammate with paper
     */
    @EventHandler
    public void onHeal(PlayerInteractEntityEvent event) {
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
            equippedPlayer.getInventory().getItemInMainHand().setAmount(equippedPlayer.getInventory().getItemInMainHand().getAmount() - 1);
            Messenger.sendHealing(CSNameTag.mmUsername(equippedPlayer) + " is healing you", patient);
            Messenger.sendHealing("You are healing " + CSNameTag.mmUsername(patient), equippedPlayer);
            UpdateStats.addHeals(equippedPlayer.getUniqueId(), 1);
        }
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
     * @return The lore to add to the kit gui item
     */
    @Override
    public ArrayList<Component> getGuiDescription() {
        ArrayList<Component> kitLore = new ArrayList<>();
        kitLore.add(Component.text("Melee-support kit that can", NamedTextColor.GRAY));
        kitLore.add(Component.text("heal allies with bandages", NamedTextColor.GRAY));
        kitLore.addAll(getBaseStats(health, regen, meleeDamage, ladderCount));
        kitLore.add(Component.text(bandageCount, color)
                        .append(Component.text(" Bandages", NamedTextColor.GRAY)));
        kitLore.add(Component.empty());
        kitLore.add(Component.text("Active:", NamedTextColor.GRAY));
        kitLore.add(Component.text("- Can heal teammates with bandages", NamedTextColor.GRAY));
        kitLore.add(Component.empty());
        kitLore.add(Component.text("Passive:", NamedTextColor.DARK_GREEN));
        kitLore.add(Component.text("- Can see players' health", NamedTextColor.GRAY));
        return kitLore;
    }
}
