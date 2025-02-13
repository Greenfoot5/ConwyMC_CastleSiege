package me.greenfoot5.castlesiege.kits.kits.coin_kits;

import io.lumine.mythic.bukkit.BukkitAPIHelper;
import me.greenfoot5.castlesiege.events.combat.InCombat;
import me.greenfoot5.castlesiege.kits.items.CSItemCreator;
import me.greenfoot5.castlesiege.kits.items.EquipmentSet;
import me.greenfoot5.castlesiege.kits.kits.CoinKit;
import me.greenfoot5.conwymc.data_types.Tuple;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Sorcerer extends CoinKit implements Listener {

    private static final int health = 230;
    private static final double regen = 10.5;
    private static final double meleeDamage = 26;
    private static final int ladderCount = 4;

    private static final int arcaneBoltCooldown = 60;
    private static final int frostNovaCooldown = 220;
    private static final int arcaneBarrageCooldown = 400;
    private static final int slowFallingCooldown = 300;

    private static final BukkitAPIHelper mythicMobsApi = new BukkitAPIHelper();

    /**
     * Creates a new Sorcerer
     */
    public Sorcerer() {
        super("Sorcerer", health, regen, Material.BOOK);

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();

        // Weapon
        es.hotbar[0] = CSItemCreator.weapon(new ItemStack(Material.STICK),
                Component.text("Sorcerer's Wand", NamedTextColor.LIGHT_PURPLE),
                List.of(Component.empty(),
                        Component.text(meleeDamage + " Melee Damage", NamedTextColor.DARK_PURPLE),
                        Component.text(arcaneBoltCooldown /20 + "s cooldown", NamedTextColor.DARK_PURPLE),
                        Component.text("<< Right Click To Shoot >>", NamedTextColor.DARK_GRAY),
                        Component.text("Right click to shoot an arcane bolt.", NamedTextColor.GRAY),
                        Component.text("- 60 Projectile DMG", NamedTextColor.GRAY),
                        Component.text("- 60m Max Range", NamedTextColor.GRAY)),
                null, meleeDamage);
        // Voted Weapon
        es.votedWeapon = new Tuple<>(
                CSItemCreator.weapon(new ItemStack(Material.STICK),
                        Component.text("Sorcerer's Wand", NamedTextColor.LIGHT_PURPLE),
                        List.of(Component.empty(),
                                Component.text((meleeDamage + 2) + " Melee Damage", NamedTextColor.DARK_PURPLE),
                                Component.text(arcaneBoltCooldown /20 + "s cooldown", NamedTextColor.DARK_PURPLE),
                                Component.text("⁎ Voted: +2 Melee Damage", NamedTextColor.LIGHT_PURPLE),
                                Component.text("<< Right Click To Shoot >>", NamedTextColor.DARK_GRAY),
                                Component.text("Right click to shoot an arcane bolt.", NamedTextColor.GRAY),
                                Component.text("- 60 Projectile DMG", NamedTextColor.GRAY),
                                Component.text("- 60m Max Range", NamedTextColor.GRAY)),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOTING, 0)), meleeDamage + 2),
                0);

        // 1st ability
        es.hotbar[1] = CSItemCreator.item(new ItemStack(Material.DIAMOND),
                Component.text("Frost nova", NamedTextColor.LIGHT_PURPLE),
                Arrays.asList(Component.empty(),
                        Component.text("<< Right Click To Use >>", NamedTextColor.DARK_GRAY),
                        Component.text("Unleash a freezing shockwave out of yourself,", NamedTextColor.GRAY),
                        Component.text("slowing every enemy in a 4 block radius", NamedTextColor.GRAY),
                        Component.text("around you and dealing 20 - 40 DMG to them.", NamedTextColor.GRAY),
                        Component.text(frostNovaCooldown /20 + "s Cooldown", NamedTextColor.LIGHT_PURPLE)), null);

        // 2nd ability
        es.hotbar[2] = CSItemCreator.item(new ItemStack(Material.AMETHYST_SHARD),
                Component.text("Arcane Barrage", NamedTextColor.LIGHT_PURPLE),
                        Arrays.asList(Component.empty(),
                        Component.text("<< Right Click To Shoot >>", NamedTextColor.DARK_GRAY),
                        Component.text("Fires 3 arcane bolts.", NamedTextColor.GRAY),
                        Component.text("- 60 Projectile DMG", NamedTextColor.GRAY),
                        Component.text("- 60m Max Range", NamedTextColor.GRAY),
                        Component.text(arcaneBarrageCooldown /20 + "s Cooldown", NamedTextColor.LIGHT_PURPLE)), null);

        // 3rd ability
        es.hotbar[3] = CSItemCreator.item(new ItemStack(Material.FEATHER),
                Component.text("Slow Falling", NamedTextColor.LIGHT_PURPLE),
                Arrays.asList(Component.empty(),
                        Component.text("<< Right Click To Use >>", NamedTextColor.DARK_GRAY),
                        Component.text("Right click to give yourself slow falling", NamedTextColor.GRAY),
                        Component.text("for 5 seconds.", NamedTextColor.GRAY),
                        Component.text(slowFallingCooldown/20 + "s Cooldown", NamedTextColor.LIGHT_PURPLE)), null);

        // Chestplate
        es.chest = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE),
                Component.text("Sorcerer's Robe", NamedTextColor.LIGHT_PURPLE),
                List.of(Component.text("» Netherite Dune Trim", NamedTextColor.DARK_GRAY),
                        Component.empty(),
                        Component.text(health + " HP", NamedTextColor.DARK_PURPLE),
                        Component.text(regen + " Regen", NamedTextColor.DARK_PURPLE)), null,
                Color.fromRGB(75, 60, 63));
        ItemMeta chest = es.chest.getItemMeta();
        ArmorMeta chestMeta = (ArmorMeta) chest;
        assert chest != null;
        ArmorTrim chestTrim = new ArmorTrim(TrimMaterial.NETHERITE, TrimPattern.DUNE);
        ((ArmorMeta) chest).setTrim(chestTrim);
        es.chest.setItemMeta(chestMeta);

        // Leggings
        es.legs = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_LEGGINGS),
                Component.text("Sorcerer's Leggings", NamedTextColor.LIGHT_PURPLE),
                List.of(Component.text("» Netherite Tide Trim", NamedTextColor.DARK_GRAY),
                        Component.empty(),
                        Component.text(health + " HP", NamedTextColor.DARK_PURPLE),
                        Component.text(regen + " Regen", NamedTextColor.DARK_PURPLE)), null,
                Color.fromRGB(106, 62, 156));
        ItemMeta legs = es.legs.getItemMeta();
        ArmorMeta legsMeta = (ArmorMeta) legs;
        assert legs != null;
        ArmorTrim legsTrim = new ArmorTrim(TrimMaterial.NETHERITE, TrimPattern.TIDE);
        legsMeta.setTrim(legsTrim);
        es.legs.setItemMeta(legsMeta);

        // Boots
        es.feet = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_BOOTS),
                Component.text("Sorcerer's Boots", NamedTextColor.LIGHT_PURPLE),
                List.of(Component.text("» Netherite Tide Trim", NamedTextColor.DARK_GRAY),
                        Component.empty(),
                        Component.text(health + " HP", NamedTextColor.DARK_PURPLE),
                        Component.text(regen + " Regen", NamedTextColor.DARK_PURPLE)), null,
                Color.fromRGB(106, 62, 156));
        // Voted Boots
        es.votedFeet = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_BOOTS),
                Component.text("Sorcerer's Boots", NamedTextColor.LIGHT_PURPLE),
                List.of(Component.text("» Netherite Tide Trim", NamedTextColor.DARK_GRAY),
                        Component.empty(),
                        Component.text(health + " HP", NamedTextColor.DARK_PURPLE),
                        Component.text(regen + " Regen", NamedTextColor.DARK_PURPLE),
                        Component.empty(),
                        Component.text("⁎ Voted: Depth Strider II", NamedTextColor.LIGHT_PURPLE)),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)),
                Color.fromRGB(106, 62, 156));
        ItemMeta boots = es.feet.getItemMeta();
        ArmorMeta bootsMeta = (ArmorMeta) boots;
        assert boots != null;
        ArmorTrim bootsTrim = new ArmorTrim(TrimMaterial.NETHERITE, TrimPattern.TIDE);
        bootsMeta.setTrim(bootsTrim);
        es.feet.setItemMeta(bootsMeta);
        es.votedFeet.setItemMeta(bootsMeta);

        // Ladders
        es.hotbar[4] = new ItemStack(Material.LADDER, ladderCount);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, ladderCount + 2), 4);

        super.equipment = es;

        // Perm Potion Effect
        super.potionEffects.add(new PotionEffect(PotionEffectType.SLOWNESS, 999999, 0, true, false));
    }

    /**
     * Activate the warlock ability of shooting a shadowBolt
     * @param e The event called when right-clicking with a wither skull
     */
    @EventHandler
    public void clickStaff(PlayerInteractEvent e) {
        if (e.getPlayer() != equippedPlayer)
            return;

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(equippedPlayer.getUniqueId()))
            return;

        ItemStack staff = equippedPlayer.getInventory().getItemInMainHand();
        if (!staff.getType().equals(Material.STICK))
            return;

        if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (equippedPlayer.getCooldown(Material.STICK) == 0) {
                equippedPlayer.setCooldown(Material.STICK, arcaneBoltCooldown);
                mythicMobsApi.castSkill(equippedPlayer ,"SorcererArcanebolt", equippedPlayer.getLocation());
            }
        }
    }

    /**
     * Activate the warlock ability of cursing the area
     * @param e The event called when right-clicking with a stick
     */
    @EventHandler
    public void clickFrostNova(PlayerInteractEvent e) {
        if (e.getPlayer() != equippedPlayer)
            return;

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(equippedPlayer.getUniqueId())) {
            return;
        }

        ItemStack pItem = equippedPlayer.getInventory().getItemInMainHand();
        if (!pItem.getType().equals(Material.DIAMOND))
            return;

        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        int cooldown = equippedPlayer.getCooldown(Material.DIAMOND);
        if (cooldown == 0) {
            equippedPlayer.setCooldown(Material.DIAMOND, frostNovaCooldown);
            mythicMobsApi.castSkill(equippedPlayer,"SorcererFrostnova", equippedPlayer.getLocation());
        }
    }

    /**
     * Activate the warlock ability of shooting a shadow bolt
     * @param e The event called when right-clicking with a wither skull
     */
    @EventHandler
    public void clickBarrage(PlayerInteractEvent e) {
        if (e.getPlayer() != equippedPlayer)
            return;

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(equippedPlayer.getUniqueId())) {
            return;
        }

        ItemStack staff = equippedPlayer.getInventory().getItemInMainHand();
        if (!staff.getType().equals(Material.AMETHYST_SHARD))
            return;

        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        int cooldown = equippedPlayer.getCooldown(Material.AMETHYST_SHARD);
        if (cooldown == 0) {
            equippedPlayer.setCooldown(Material.AMETHYST_SHARD, arcaneBarrageCooldown);
            mythicMobsApi.castSkill(equippedPlayer ,"SorcererArcaneboltBarrage", equippedPlayer.getLocation());
        }
    }

    /**
     * Activate the warlock ability of shooting a shadow bolt
     * @param e The event called when right-clicking with a wither skull
     */
    @EventHandler
    public void clickSlowFalling(PlayerInteractEvent e) {
        if (e.getPlayer() != equippedPlayer)
            return;

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(equippedPlayer.getUniqueId())) {
            return;
        }

        ItemStack staff = equippedPlayer.getInventory().getItemInMainHand();
        if (!staff.getType().equals(Material.FEATHER))
            return;

        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        int cooldown = equippedPlayer.getCooldown(Material.FEATHER);
        if (cooldown == 0) {
            equippedPlayer.setCooldown(Material.FEATHER, slowFallingCooldown);
            mythicMobsApi.castSkill(equippedPlayer ,"SorcererSlowfalling", equippedPlayer.getLocation());
        }
    }


    /**
     * @return The lore to add to the kit gui item
     */
    @Override
    public ArrayList<Component> getGuiDescription() {
        ArrayList<Component> kitLore = new ArrayList<>();
        kitLore.add(Component.text("A sorcerer specialised in the arcane arts,", NamedTextColor.GRAY));
        kitLore.add(Component.text("can also slow down enemies with ice.", NamedTextColor.GRAY));
        kitLore.addAll(getBaseStats(health, regen, meleeDamage, ladderCount));
        kitLore.add(Component.text("60", color)
                        .append(Component.text(" arcane-bolt DMG", NamedTextColor.GRAY)));
        kitLore.add(Component.text("20 - 40", color)
                        .append(Component.text(" frost nova DMG", NamedTextColor.GRAY)));
        kitLore.add(Component.empty());
        kitLore.add(Component.text("Effects:", NamedTextColor.DARK_PURPLE));
        kitLore.add(Component.text("- Slowness I", NamedTextColor.GRAY));
        kitLore.add(Component.empty());
        kitLore.add(Component.text("Active: ", NamedTextColor.GOLD));
        kitLore.add(Component.text("- Can shoot an arcane-bolt at opponents at a", NamedTextColor.GRAY));
        kitLore.add(Component.text("higher speed then other magic ranged attacks", NamedTextColor.GRAY));
        kitLore.add(Component.text("- In a wave of cold can damage and slow down enemies", NamedTextColor.GRAY));
        kitLore.add(Component.text("in a 4 block radius", NamedTextColor.GRAY));
        kitLore.add(Component.text("- Can shoot a barrage of arcane-bolts", NamedTextColor.GRAY));
        kitLore.add(Component.text("- Has a spell that gives slow falling to the caster", NamedTextColor.GRAY));
        return kitLore;
    }
}
