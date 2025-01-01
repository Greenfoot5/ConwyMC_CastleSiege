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

public class Gunner extends SignKit {

    private static final int health = 300;
    private static final double regen = 10.5;
    private static final double meleeDamage = 30.5;
    private static final int ladderCount = 4;
    private static final int musketCooldown = 180;
    private static final BukkitAPIHelper mythicMobsApi = new BukkitAPIHelper();

    public Gunner() {
        super("Gunner", health, regen, Material.NETHERITE_HOE);


        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();

        // Chestplate
        es.chest = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE),
                Component.text("Gunner's Vest", NamedTextColor.GREEN), null, null,
                Color.fromRGB(60,68,170));
        ItemMeta chest = es.chest.getItemMeta();
        ArmorMeta chestMeta = (ArmorMeta) chest;
        assert chest != null;
        ArmorTrim chestTrim = new ArmorTrim(TrimMaterial.COPPER, TrimPattern.SPIRE);
        ((ArmorMeta) chest).setTrim(chestTrim);
        es.chest.setItemMeta(chestMeta);

        // Leggings
        es.legs = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_LEGGINGS),
                Component.text("Gunner's Leggings", NamedTextColor.GREEN), null, null,
                Color.fromRGB(58, 189, 186));

        // Boots
        es.feet = CSItemCreator.item(new ItemStack(Material.NETHERITE_BOOTS),
                Component.text("Gunner's Boots", NamedTextColor.GREEN), null, null);
        // Voted Boots
        es.votedFeet = CSItemCreator.item(new ItemStack(Material.NETHERITE_BOOTS),
                Component.text("Gunner's Boots", NamedTextColor.GREEN),
                Collections.singletonList(Component.text("⁎ Voted: Depth Strider II", NamedTextColor.AQUA)),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)));

        // Flintlock Item
        es.hotbar[0] = CSItemCreator.weapon(new ItemStack(Material.NETHERITE_HOE),
                Component.text("Musket", NamedTextColor.GOLD),
                List.of(Component.empty(),
                        Component.text("Uses bullets as ammo to shoot.", NamedTextColor.BLUE),
                        Component.empty(),
                        Component.text(meleeDamage + " melee DMG", NamedTextColor.DARK_GREEN),
                        Component.text("80 DMG per shot", NamedTextColor.DARK_GREEN),
                        Component.text("60m max range", NamedTextColor.DARK_GREEN),
                        Component.text("9s cool down", TextColor.color(49, 171, 189))),
                null, meleeDamage);
        // Voted Weapon
        es.votedWeapon = new Tuple<>(
                CSItemCreator.weapon(new ItemStack(Material.NETHERITE_HOE),
                        Component.text("Musket", NamedTextColor.GOLD),
                        List.of(Component.empty(),
                                Component.text("Uses bullets as ammo to shoot.", NamedTextColor.BLUE),
                                Component.empty(),
                                Component.text((meleeDamage + 2) + " melee DMG (voted)", NamedTextColor.DARK_GREEN),
                                Component.text("80 DMG per shot", NamedTextColor.DARK_GREEN),
                                Component.text("60m max range", NamedTextColor.DARK_GREEN),
                                Component.text("9s cooldown", TextColor.color(49, 171, 189))),
                        null, meleeDamage),
                0);

        // Bullet Item
        es.hotbar[2] = CSItemCreator.item(new ItemStack(Material.FIREWORK_STAR, 12),
                Component.text("Lead Bullets", NamedTextColor.GOLD),
                List.of(Component.empty(),
                        Component.text("(ammo for your pistol).", NamedTextColor.BLUE)),
                null);

        // Ladders
        es.hotbar[1] = new ItemStack(Material.LADDER, ladderCount);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, ladderCount + 2), 1);

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
        int cooldown = p.getCooldown(Material.NETHERITE_HOE);

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(uuid)) {
            return;
        }

        if (!Objects.equals(Kit.equippedKits.get(uuid).name, name) || !flintlock.getType().equals(Material.NETHERITE_HOE)) {
            return;
        }
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (!p.getInventory().contains(Material.FIREWORK_STAR, 1)) {
                Messenger.sendActionError("You require lead bullets to shoot the flintlock!", p);
                return;
            }

            if (cooldown == 0) {
                for (ItemStack item : p.getInventory().getContents()) {
                    mythicMobsApi.castSkill(p, "GunnerMusketShot", p.getLocation());
                    p.setCooldown(Material.NETHERITE_HOE, musketCooldown);
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
