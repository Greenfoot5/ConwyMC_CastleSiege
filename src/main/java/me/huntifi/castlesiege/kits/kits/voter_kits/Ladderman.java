package me.huntifi.castlesiege.kits.kits.voter_kits;

import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.kits.kits.VoterKit;
import org.bukkit.ChatColor;
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
    private static final double regenAmount = 10.5;
    private static final double meleeDamage = 36;
    private static final int ladderCount = 25;

    /**
     * Set the equipment and attributes of this kit
     */
    public Ladderman() {
        super("Ladderman", health, regenAmount, Material.LADDER, "Lurker");

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Weapon
        es.hotbar[0] = ItemCreator.weapon(new ItemStack(Material.IRON_AXE),
                ChatColor.GREEN + "Short Axe", null, null, meleeDamage);
        // Voted Weapon
        es.votedWeapon = new Tuple<>(
                ItemCreator.weapon(new ItemStack(Material.IRON_AXE),
                        ChatColor.GREEN + "Short Axe",
                        Collections.singletonList(ChatColor.AQUA + "- voted: +2 damage"),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), meleeDamage + 2),
                0);

        // Chestplate
        es.chest = ItemCreator.item(new ItemStack(Material.LEATHER_CHESTPLATE),
                ChatColor.GREEN + "Leather Tunic", null, null);

        // Leggings
        es.legs = ItemCreator.item(new ItemStack(Material.IRON_LEGGINGS),
                ChatColor.GREEN + "Iron Leggings", null, null);

        // Boots
        es.feet = ItemCreator.item(new ItemStack(Material.LEATHER_BOOTS),
                ChatColor.GREEN + "Leather Boots", null, null);
        // Voted Boots
        es.votedFeet = ItemCreator.item(new ItemStack(Material.LEATHER_BOOTS),
                ChatColor.GREEN + "Leather Boots",
                Collections.singletonList(ChatColor.AQUA + "- voted: Depth Strider II"),
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

    public static ArrayList<String> loreStats() {
        ArrayList<String> kitLore = new ArrayList<>();
        kitLore.add("§7Melee kit with an axe");
        kitLore.add("§7and loads of ladders.");
        kitLore.add(" ");
        kitLore.add("§a" + health + " §7HP");
        kitLore.add("§a" + meleeDamage + " §7Melee DMG");
        kitLore.add("§a" + regenAmount + " §7Regen");
        kitLore.add("§a" + ladderCount + " §7Ladders");
        kitLore.add("§5Effects:");
        kitLore.add("§7- Jump Boost II");
        kitLore.add("");
        kitLore.add("§2Passive: ");
        kitLore.add("§7- Can break ladders and");
        kitLore.add("§7pick them up.");
        kitLore.add("");
        kitLore.add("§7Vote on PMC for this kit!");
        return kitLore;
    }
}
