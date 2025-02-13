package me.greenfoot5.castlesiege.kits.items;

import me.greenfoot5.castlesiege.database.CSActiveData;
import me.greenfoot5.conwymc.data_types.Tuple;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

import java.util.UUID;

/**
 * Stores a kit's items and their locations
 */
public class EquipmentSet {

    public ItemStack chest;
    public ItemStack legs;
    public ItemStack feet;
    public final ItemStack[] hotbar;
    public ItemStack offhand;
    // What to grant and where for voting
    public Tuple<ItemStack, Integer> votedWeapon;
    public ItemStack votedOffhand;
    public Tuple<ItemStack, Integer> votedLadders;
    public ItemStack votedFeet;

    /**
     * Create an array representation of the first 8 slots of the hotbar
     */
    public EquipmentSet() {
        hotbar = new ItemStack[8];
    }

    /**
     * Sets a player's inventory to this equipment set
     * @param player The player whose inventory to change
     */
    public void setEquipment(Player player) {
        PlayerInventory inv = player.getInventory();
        inv.clear();

        if (chest != null && chest.getAmount() > 0) {
            inv.setChestplate(clearDefence(chest));
        }

        if (legs != null && legs.getAmount() > 0) {
            inv.setLeggings(clearDefence(legs));
        }

        if (feet != null && feet.getAmount() > 0) {
            inv.setBoots(clearDefence(feet));
            // Voted boots
            if (CSActiveData.getData(player.getUniqueId()).hasVote("boots") && votedFeet != null && votedFeet.getAmount() > 0) {
                inv.setBoots(clearDefence(votedFeet));
            }
        }

        for (int i = 0; i < hotbar.length; i++) {
            if (hotbar[i] != null && hotbar[i].getAmount() > 0) {
                inv.setItem(i, hotbar[i]);
            }
        }

        if (offhand != null && offhand.getAmount() > 0) {
            inv.setItemInOffHand(offhand);
            if (CSActiveData.getData(player.getUniqueId()).hasVote("sword")) {
                if (votedOffhand != null && votedOffhand.getAmount() > 0) {
                    inv.setItemInOffHand(votedOffhand);
                }
            }
        }

        // Votes Weapon
        if (CSActiveData.getData(player.getUniqueId()).hasVote("sword") &&
                votedWeapon != null && votedWeapon.getFirst().getAmount() > 0) {
            if (votedWeapon.getSecond() == -1) {
                inv.setItemInOffHand(votedWeapon.getFirst());
            } else {
                inv.setItem(votedWeapon.getSecond(), votedWeapon.getFirst());
            }
        }

        // Votes Ladders
        if (CSActiveData.getData(player.getUniqueId()).hasVote("ladders") &&
                votedLadders != null && votedLadders.getFirst().getAmount() > 0) {
            if (votedLadders.getSecond() == -1) {
                inv.setItemInOffHand(votedLadders.getFirst());
            } else {
                inv.setItem(votedLadders.getSecond(), votedLadders.getFirst());
            }
        }
    }

    private ItemStack clearDefence(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        try {
            meta.addAttributeModifier(Attribute.ARMOR, new AttributeModifier(new NamespacedKey(NamespacedKey.MINECRAFT, "armor"), 0.0, AttributeModifier.Operation.ADD_SCALAR));
            meta.addAttributeModifier(Attribute.ARMOR_TOUGHNESS, new AttributeModifier(new NamespacedKey(NamespacedKey.MINECRAFT, "armor_toughness"), 0.0, AttributeModifier.Operation.ADD_SCALAR));
            item.setItemMeta(meta);
        }
        catch (IllegalArgumentException ignored) { }

        return item;
    }
}
