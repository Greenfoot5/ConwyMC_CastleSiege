package me.greenfoot5.castlesiege.kits.kits.sign_kits;

import me.greenfoot5.castlesiege.events.combat.InCombat;
import me.greenfoot5.castlesiege.kits.items.CSItemCreator;
import me.greenfoot5.castlesiege.kits.items.EquipmentSet;
import me.greenfoot5.castlesiege.kits.kits.Kit;
import me.greenfoot5.castlesiege.kits.kits.SignKit;
import me.greenfoot5.castlesiege.maps.CoreMap;
import me.greenfoot5.castlesiege.maps.MapController;
import me.greenfoot5.castlesiege.maps.objects.Core;
import me.greenfoot5.castlesiege.maps.objects.Flag;
import me.greenfoot5.conwymc.data_types.Tuple;
import me.greenfoot5.conwymc.util.Messenger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

/**
 * A kit that can build wooden defenses to help block off defenders
 */
public class Constructor extends SignKit implements Listener {

    private static final int health = 260;
    private static final double regen = 10.5;
    private static final double meleeDamage = 34;
    private static final int ladderCount = 4;
    private static final int planksCount = 48;
    private static final ArrayList<Block> placedPlanks = new ArrayList<>();

    /**
     * Creates a new Constructor kit
     */
    public Constructor() {
        super("Constructor", health, regen, Material.OAK_PLANKS);

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();

        // Weapon
        es.hotbar[0] = CSItemCreator.weapon(new ItemStack(Material.STONE_AXE),
                Component.text("Constructor's Axe", NamedTextColor.GREEN), null, null, meleeDamage);
        // Voted Weapon
        es.votedWeapon = new Tuple<>(
                CSItemCreator.weapon(new ItemStack(Material.STONE_AXE),
                        Component.text("Constructor's Axe", NamedTextColor.GREEN),
                        Collections.singletonList(Component.text("⁎ Voted: +2 damage", NamedTextColor.AQUA)),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOTING, 0)), meleeDamage + 2),
                0);

        // Chestplate
        es.chest = CSItemCreator.item(new ItemStack(Material.NETHERITE_CHESTPLATE),
                Component.text("Reinforced Iron Chestplate", NamedTextColor.GREEN), null, null);
        ItemMeta chest = es.chest.getItemMeta();
        ArmorMeta chestMeta = (ArmorMeta) chest;
        assert chest != null;
        ArmorTrim chestTrim = new ArmorTrim(TrimMaterial.LAPIS, TrimPattern.RIB);
        ((ArmorMeta) chest).setTrim(chestTrim);
        es.chest.setItemMeta(chestMeta);

        // Leggings
        es.legs = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_LEGGINGS),
                Component.text("Leather Leggings", NamedTextColor.GREEN), null, null,
                Color.fromRGB(20, 19, 19));

        // Boots
        es.feet = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_BOOTS),
                Component.text("Leather Boots", NamedTextColor.GREEN), null, null, Color.fromRGB(20, 19, 19));
        // Voted Boots
        es.votedFeet = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_BOOTS),
                Component.text("Leather Boots", NamedTextColor.GREEN),
                Collections.singletonList(Component.text("⁎ Voted: Depth Strider II", NamedTextColor.AQUA)),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)), Color.fromRGB(20, 19, 19));

        // Ladders
        es.hotbar[1] = new ItemStack(Material.LADDER, ladderCount);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, ladderCount + 2), 1);

        // Wood
        es.hotbar[2] = new ItemStack(Material.OAK_PLANKS, planksCount);

        super.equipment = es;

        // Perm Potion Effect
        super.potionEffects.add(new PotionEffect(PotionEffectType.JUMP_BOOST, 999999, 0, true, false));

    }

    /**
     * Activate the constructor ability of placing oak_planks
     * @param e The event called when placing oak planks
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlacePlank(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(uuid)) {
            return;
        }

        if (Objects.equals(Kit.equippedKits.get(uuid).name, name) &&
                e.getBlock().getType() == Material.OAK_PLANKS
                && checkPlaces(e.getBlockAgainst().getLocation(), p)) {
            placedPlanks.add(e.getBlockPlaced());
            e.setCancelled(false);
        }
    }

    /**
     *
     * @param placerLoc the location to check for, should in this case be a block.
     * @param player the player to send the error message to.
     * @return Whether they can place this block here or not. True/false
     */
    private boolean checkPlaces(Location placerLoc, Player player) {
        for (Flag flag : MapController.getCurrentMap().flags) {
            if (placerLoc.distance(flag.getSpawnPoint()) <= 6 ||
                    flag.region.contains((int) placerLoc.getX(), (int) placerLoc.getY(), (int) placerLoc.getZ())) {
                Messenger.sendActionError("You can't place planks here!", player);
                return false;
            }
        }
        if (MapController.getCurrentMap() instanceof CoreMap map) {
            for (Core core : map.getCores()) {
                if (placerLoc.distance(core.getSpawnPoint()) <= 6 ||
                        core.region.contains((int) placerLoc.getX(), (int) placerLoc.getY(), (int) placerLoc.getZ())) {
                    Messenger.sendActionError("You can't place planks here!", player);
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Activate the constructor ability of picking up planks
     * @param e The event called when breaking oak planks
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBreakPlank(BlockBreakEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(uuid)) {
            return;
        }

        if (Objects.equals(Kit.equippedKits.get(uuid).name, name) &&
                e.getBlock().getType() == Material.OAK_PLANKS && placedPlanks.contains(e.getBlock())) {
                p.getInventory().addItem(new ItemStack(Material.OAK_PLANKS));
                placedPlanks.remove(e.getBlock());
                e.getBlock().setType(Material.AIR);
        } else if (e.getBlock().getType() == Material.OAK_PLANKS && placedPlanks.contains(e.getBlock())) {
            placedPlanks.remove(e.getBlock());
            e.getBlock().setType(Material.AIR);
        }
    }

    @Override
    public ArrayList<Component> getGuiDescription() {
        ArrayList<Component> kitLore = new ArrayList<>();
        kitLore.add(Component.text("Constructors can barricade cores and flags!", NamedTextColor.GRAY));
        kitLore.addAll(getBaseStats(health, regen, meleeDamage, ladderCount));
        kitLore.add(Component.text(planksCount, color)
                        .append(Component.text(" Planks", NamedTextColor.GRAY)));
        kitLore.add(Component.empty());
        kitLore.add(Component.text("Effects:", NamedTextColor.DARK_PURPLE));
        kitLore.add(Component.text("- JUMP I", NamedTextColor.GRAY));
        kitLore.add(Component.empty());
        kitLore.add(Component.text("Passive:", NamedTextColor.DARK_GREEN));
        kitLore.add(Component.text("- Can pick up planks placed by", NamedTextColor.GRAY));
        kitLore.add(Component.text("other constructors.", NamedTextColor.GRAY));
        return kitLore;
    }
}
