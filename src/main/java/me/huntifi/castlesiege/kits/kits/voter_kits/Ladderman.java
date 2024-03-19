package me.huntifi.castlesiege.kits.kits.voter_kits;

import me.huntifi.conwymc.data_types.Tuple;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.kits.kits.VoterKit;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

/**
 * The ladderman kit
 */
public class Ladderman extends VoterKit implements Listener {

    private static final int health = 270;
    private static final double regen = 10.5;
    private static final double meleeDamage = 36;
    private static final int ladderCount = 25;

    /**
     * Set the equipment and attributes of this kit
     */
    public Ladderman() {
        super("Ladderman", health, regen, Material.LADDER);

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Weapon
        es.hotbar[0] = ItemCreator.weapon(new ItemStack(Material.IRON_AXE),
                Component.text("Short Axe", NamedTextColor.GREEN), null, null, meleeDamage);
        // Voted Weapon
        es.votedWeapon = new Tuple<>(
                ItemCreator.weapon(new ItemStack(Material.IRON_AXE),
                        Component.text("Short Axe", NamedTextColor.GREEN),
                        Collections.singletonList(Component.text("- voted: +2 damage", NamedTextColor.AQUA)),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), meleeDamage + 2),
                0);

        // Chestplate
        es.chest = ItemCreator.item(new ItemStack(Material.LEATHER_CHESTPLATE),
                Component.text("Leather Tunic", NamedTextColor.GREEN), null, null);

        // Leggings
        es.legs = ItemCreator.item(new ItemStack(Material.IRON_LEGGINGS),
                Component.text("Iron Leggings", NamedTextColor.GREEN), null, null);

        // Boots
        es.feet = ItemCreator.item(new ItemStack(Material.LEATHER_BOOTS),
                Component.text("Leather Boots", NamedTextColor.GREEN), null, null);
        // Voted Boots
        es.votedFeet = ItemCreator.item(new ItemStack(Material.LEATHER_BOOTS),
                Component.text("Leather Boots", NamedTextColor.GREEN),
                Collections.singletonList(Component.text("- voted: Depth Strider II", NamedTextColor.AQUA)),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)));

        // Ladders
        es.hotbar[1] = new ItemStack(Material.LADDER, ladderCount);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, ladderCount + 2), 1);

        super.equipment = es;

        // Perm Potion Effect
        super.potionEffects.add(new PotionEffect(PotionEffectType.JUMP, 999999, 1));
    }

    /**
     * Activate the ladderman ability of retrieving broken ladders
     * @param e The event called when breaking ladders
     */
    @EventHandler (priority = EventPriority.LOWEST)
    public void onBreakLadder(BlockBreakEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(uuid)) {
            return;
        }

        if (Objects.equals(Kit.equippedKits.get(uuid).name, name) &&
                e.getBlock().getType() == Material.LADDER) {
            p.getInventory().addItem(new ItemStack(Material.LADDER));
        }
    }

    /**
     * @return The lore to add to the kit gui item
     */
    @Override
    public ArrayList<Component> getGuiDescription() {
        ArrayList<Component> kitLore = new ArrayList<>();
        kitLore.add(Component.text("Melee kit with an axe", NamedTextColor.GRAY));
        kitLore.add(Component.text("and loads of ladders", NamedTextColor.GRAY));
        kitLore.addAll(getBaseStats(health, regen, meleeDamage, ladderCount));
        kitLore.add(Component.empty());
        kitLore.add(Component.text("Effects:", NamedTextColor.DARK_PURPLE));
        kitLore.add(Component.text("- Jump Boost II", NamedTextColor.GRAY));
        kitLore.add(Component.empty());
        kitLore.add(Component.text("Passive:", NamedTextColor.DARK_GREEN));
        kitLore.add(Component.text("- Can pickup ladders", NamedTextColor.GRAY));
        return kitLore;
    }
}
