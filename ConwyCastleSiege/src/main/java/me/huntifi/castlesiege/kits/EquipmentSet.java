package me.huntifi.castlesiege.kits;

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
    public ItemStack[] hotbar = new ItemStack[8];
    public ItemStack offhand;

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
        }

        for (int i = 0; i < hotbar.length; i++) {
            if (hotbar[i] != null && hotbar[i].getAmount() > 0) {
                inv.setItem(i, hotbar[i]);
            }
        }

        if (offhand != null && offhand.getAmount() > 0) {
            inv.setItemInOffHand(offhand);
        }
    }
}
