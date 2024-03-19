package me.huntifi.castlesiege.kits.items;

import me.huntifi.conwymc.data_types.Tuple;
import me.huntifi.castlesiege.database.ActiveData;
import org.bukkit.Bukkit;
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
     * @param uuid The unique id of the player whose inventory to change
     */
    public void setEquipment(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        assert player != null;

        for (PotionEffect effect : player.getActivePotionEffects())
            player.removePotionEffect(effect.getType());

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
            if (ActiveData.getData(uuid).hasVote("boots") && votedFeet != null && votedFeet.getAmount() > 0) {
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
        }

        // Votes Weapon
        if (ActiveData.getData(uuid).hasVote("sword") &&
                votedWeapon != null && votedWeapon.getFirst().getAmount() > 0) {
            if (votedWeapon.getSecond() == -1) {
                inv.setItemInOffHand(votedWeapon.getFirst());
            } else {
                inv.setItem(votedWeapon.getSecond(), votedWeapon.getFirst());
            }
        }

        // Votes Ladders
        if (ActiveData.getData(uuid).hasVote("ladders") &&
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
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier("RemoveArmour", 0.0, AttributeModifier.Operation.ADD_SCALAR));
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier("RemoveToughness", 0.0, AttributeModifier.Operation.ADD_SCALAR));
        item.setItemMeta(meta);
        return item;
    }
}
