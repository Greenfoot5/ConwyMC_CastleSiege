package me.huntifi.castlesiege.kits;

import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.voting.VotesChanging;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;

import java.util.UUID;

public class EquipmentSet {

    public ItemStack chest;
    public ItemStack legs;
    public ItemStack feet;
    public ItemStack[] hotbar;
    public ItemStack offhand;
    // What to grant and where for voting
    public Tuple<ItemStack, Integer> votedWeapon;
    public Tuple<ItemStack, Integer> votedLadders;
    public ItemStack votedFeet;

    public EquipmentSet() {
        hotbar = new ItemStack[8];
    }

    public void setEquipment(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        assert player != null;

        for (PotionEffect effect : player.getActivePotionEffects())
            player.removePotionEffect(effect.getType());

        PlayerInventory inv = player.getInventory();
        inv.clear();

        if (chest != null && chest.getAmount() > 0) {
            inv.setChestplate(chest);
        }

        if (legs != null && legs.getAmount() > 0) {
            inv.setLeggings(legs);
        }

        if (feet != null && feet.getAmount() > 0) {
            inv.setBoots(feet);
            // Voted boots
            if (VotesChanging.getVotes(uuid).contains("V#2") && votedFeet != null && votedFeet.getAmount() > 0) {
                inv.setBoots(votedFeet);
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
        if (VotesChanging.getVotes(uuid).contains("V#1") &&
                votedWeapon != null && votedWeapon.getFirst().getAmount() > 0) {
            if (votedWeapon.getSecond() == -1) {
                inv.setItemInOffHand(votedWeapon.getFirst());
            } else {
                inv.setItem(votedWeapon.getSecond(), votedWeapon.getFirst());
            }
        }

        // Votes Ladders
        if (VotesChanging.getVotes(uuid).contains("V#3") &&
                votedLadders != null && votedLadders.getFirst().getAmount() > 0) {
            if (votedLadders.getSecond() == -1) {
                inv.setItemInOffHand(votedLadders.getFirst());
            } else {
                inv.setItem(votedLadders.getSecond(), votedLadders.getFirst());
            }
        }
    }
}
