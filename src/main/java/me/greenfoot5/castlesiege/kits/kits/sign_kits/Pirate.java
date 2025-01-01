package me.greenfoot5.castlesiege.kits.kits.sign_kits;

import io.lumine.mythic.bukkit.BukkitAPIHelper;
import me.greenfoot5.castlesiege.events.combat.InCombat;
import me.greenfoot5.castlesiege.kits.items.CSItemCreator;
import me.greenfoot5.castlesiege.kits.items.EquipmentSet;
import me.greenfoot5.castlesiege.kits.kits.Kit;
import me.greenfoot5.castlesiege.kits.kits.SignKit;
import me.greenfoot5.conwymc.data_types.Tuple;
import me.greenfoot5.conwymc.util.Messenger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Pirate extends SignKit {

    private static final int health = 300;
    private static final double regen = 10.5;
    private static final double meleeDamage = 40.5;
    private static final int ladderCount = 4;
    private static final int flintlockCooldown = 140;
    private static final BukkitAPIHelper mythicMobsApi = new BukkitAPIHelper();

    public Pirate() {
        super("Pirate", health, regen, Material.WOODEN_HOE, 2000);

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();

        // Weapon
        es.hotbar[0] = CSItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                Component.text("Cutlass", NamedTextColor.GREEN), null, null, meleeDamage);
        // Voted Weapon
        es.votedWeapon = new Tuple<>(
                CSItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                        Component.text("Cutlass", NamedTextColor.GREEN),
                        Collections.singletonList(Component.text("⁎ Voted: +2 damage", NamedTextColor.AQUA)),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOTING, 0)), meleeDamage + 2),
                0);

        // Chestplate
        es.chest = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE),
                Component.text("Pirate's Vest", NamedTextColor.GREEN), null, null,
                Color.fromRGB(71,79,82));
        ItemMeta chest = es.chest.getItemMeta();
        ArmorMeta chestMeta = (ArmorMeta) chest;
        assert chest != null;
        ArmorTrim chestTrim = new ArmorTrim(TrimMaterial.COPPER, TrimPattern.SPIRE);
        ((ArmorMeta) chest).setTrim(chestTrim);
        es.chest.setItemMeta(chestMeta);

        // Leggings
        es.legs = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_LEGGINGS),
                Component.text("Pirate's Leggings", NamedTextColor.GREEN), null, null,
                Color.fromRGB(176, 46, 38));

        // Boots
        es.feet = CSItemCreator.item(new ItemStack(Material.NETHERITE_BOOTS),
                Component.text("Pirate's Boots", NamedTextColor.GREEN), null, null);
        // Voted Boots
        es.votedFeet = CSItemCreator.item(new ItemStack(Material.NETHERITE_BOOTS),
                Component.text("Pirate's Boots", NamedTextColor.GREEN),
                Collections.singletonList(Component.text("⁎ Voted: Depth Strider II", NamedTextColor.AQUA)),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)));

        // Flintlock Item
        es.hotbar[1] = CSItemCreator.item(new ItemStack(Material.WOODEN_HOE),
                Component.text("Flintlock Pistol", NamedTextColor.GOLD),
                List.of(Component.empty(),
                        Component.text("Uses bullets as ammo to shoot.", NamedTextColor.BLUE),
                        Component.empty(),
                        Component.text("60 DMG per shot", NamedTextColor.DARK_GREEN),
                        Component.text("40m max range", NamedTextColor.DARK_GREEN),
                        Component.text("7s cooldown", TextColor.color(49, 171, 189)),
                        Component.text("○ Speed I (0:03) after shooting", TextColor.color(40, 169, 255))),
                null);

        // Bullet Item
        es.hotbar[3] = CSItemCreator.item(new ItemStack(Material.FIREWORK_STAR, 8),
                Component.text("Lead Bullets", NamedTextColor.GOLD),
                List.of(Component.empty(),
                        Component.text("(ammo for your pistol).", NamedTextColor.BLUE)),
                null);

        // Ladders
        es.hotbar[2] = new ItemStack(Material.LADDER, ladderCount);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, ladderCount + 2), 2);

        super.equipment = es;
    }


    /**
     * Activate the pirate's flintlock, shoot a mythic mobs projectile! See skill file
     * @param e The event called when right-clicking with a flintlock
     */
    @EventHandler
    public void clickFlintlock(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        ItemStack flintlock = p.getInventory().getItemInMainHand();
        int cooldown = p.getCooldown(Material.WOODEN_HOE);

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(uuid)) {
            return;
        }

        if (!Objects.equals(Kit.equippedKits.get(uuid).name, name) || !flintlock.getType().equals(Material.WOODEN_HOE)) {
            return;
        }
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (!p.getInventory().contains(Material.FIREWORK_STAR, 1)) {
                Messenger.sendActionError("You require lead bullets to shoot the flintlock!", p);
                return;
            }

            if (cooldown == 0) {
                for (ItemStack item : p.getInventory().getContents()) {
                    mythicMobsApi.castSkill(p, "PirateFlintlockShot", p.getLocation());
                    p.setCooldown(Material.WOODEN_HOE, flintlockCooldown);
                    if (item == null) {
                        return;
                    }
                    if (item.getType().equals(Material.FIREWORK_STAR)) {
                        item.setAmount(item.getAmount() - 1);
                    }
                }
            }
        }
    }

    @Override
    public ArrayList<Component> getGuiDescription() {
        return null;
    }
}
