package me.greenfoot5.castlesiege.kits.kits.sign_kits.Moria;

import me.greenfoot5.castlesiege.Main;
import me.greenfoot5.castlesiege.database.UpdateStats;
import me.greenfoot5.castlesiege.events.combat.InCombat;
import me.greenfoot5.castlesiege.kits.items.CSItemCreator;
import me.greenfoot5.castlesiege.kits.items.EquipmentSet;
import me.greenfoot5.castlesiege.kits.kits.Kit;
import me.greenfoot5.castlesiege.kits.kits.SignKit;
import me.greenfoot5.castlesiege.maps.TeamController;
import me.greenfoot5.conwymc.data_types.Tuple;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
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
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

public class Overseer extends SignKit implements Listener {

    /**
     * Creates a new Moria Overseer
     */
    public Overseer() {
        super("Moria Overseer", 380, 10, Material.GOAT_HORN, 2000);

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();

        // Weapon
        es.hotbar[0] = CSItemCreator.weapon(new ItemStack(Material.NETHERITE_AXE),
                Component.text("Dwarven Battle-Axe", NamedTextColor.GREEN), null, null, 40);
        // Voted weapon
        es.votedWeapon = new Tuple<>(
                CSItemCreator.weapon(new ItemStack(Material.NETHERITE_AXE),
                        Component.text("Dwarven Battle-Axe", NamedTextColor.GREEN),
                        Collections.singletonList(Component.text("⁎ Voted: +2 damage", NamedTextColor.AQUA)),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOTING, 0)), 42),
                0);

        es.offhand = CSItemCreator.weapon(new ItemStack(Material.GOAT_HORN),
                Component.text("Dwarven Horn", NamedTextColor.GREEN), null, null, 1);

        // Chestplate
        es.chest = CSItemCreator.item(new ItemStack(Material.CHAINMAIL_CHESTPLATE),
                Component.text("Mithril Chestplate", NamedTextColor.GREEN), null, null);
        ItemMeta chest = es.chest.getItemMeta();
        ArmorMeta chestMeta = (ArmorMeta) chest;
        assert chest != null;
        ArmorTrim chestTrim = new ArmorTrim(TrimMaterial.IRON, TrimPattern.SILENCE);
        ((ArmorMeta) chest).setTrim(chestTrim);
        es.chest.setItemMeta(chestMeta);

        // Leggings
        es.legs = CSItemCreator.item(new ItemStack(Material.NETHERITE_LEGGINGS),
                Component.text("Mithril Leggings", NamedTextColor.GREEN), null, null);
        ItemMeta legs = es.legs.getItemMeta();
        ArmorMeta legsMeta = (ArmorMeta) legs;
        assert legs != null;
        ArmorTrim legsTrim = new ArmorTrim(TrimMaterial.IRON, TrimPattern.SILENCE);
        ((ArmorMeta) legs).setTrim(legsTrim);
        es.legs.setItemMeta(legsMeta);

        // Boots
        es.feet = CSItemCreator.item(new ItemStack(Material.CHAINMAIL_BOOTS),
                Component.text("Iron Boots", NamedTextColor.GREEN), null, null);
        ItemMeta feet = es.feet.getItemMeta();
        ArmorMeta feetMeta = (ArmorMeta) feet;
        assert feet != null;
        ArmorTrim feetTrim = new ArmorTrim(TrimMaterial.IRON, TrimPattern.SILENCE);
        ((ArmorMeta) feet).setTrim(feetTrim);
        es.feet.setItemMeta(feetMeta);
        // Voted Boots
        es.votedFeet = CSItemCreator.item(new ItemStack(Material.CHAINMAIL_BOOTS),
                Component.text("Iron Boots", NamedTextColor.GREEN),
                Collections.singletonList(Component.text("⁎ Voted: Depth Strider II", NamedTextColor.AQUA)),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)));
        ItemMeta votedFeet = es.votedFeet.getItemMeta();
        ArmorMeta votedFeetMeta = (ArmorMeta) votedFeet;
        assert votedFeet != null;
        ArmorTrim votedFeetTrim = new ArmorTrim(TrimMaterial.IRON, TrimPattern.SILENCE);
        ((ArmorMeta) votedFeet).setTrim(votedFeetTrim);
        es.votedFeet.setItemMeta(votedFeetMeta);

        // Ladders
        es.hotbar[1] = new ItemStack(Material.LADDER, 4);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, 6), 1);

        super.potionEffects.add(new PotionEffect(PotionEffectType.HASTE, 999999, 0));

        super.equipment = es;
    }


    /**
     * Activate the spearman ability of throwing a spear
     * @param e The event called when right-clicking with a stick
     */
    @EventHandler
    public void throwAxe(PlayerInteractEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();
        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(uuid)) {
            return;
        }
        Player p = e.getPlayer();
        if (Objects.equals(Kit.equippedKits.get(uuid).name, name)) {
            ItemStack horn = p.getInventory().getItemInOffHand();
            if (horn.getType().equals(Material.GOAT_HORN)) {
                if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    //Not allowed to throw whilst clicking on enderchests or cakes.
                    if (e.getClickedBlock() != null) {
                        if (interactableBlock(e.getClickedBlock()) || p.getInventory().getItemInMainHand().getType() == Material.LADDER) {
                            return;
                        }
                    }
                    int cooldown = p.getCooldown(Material.GOAT_HORN);
                    if (cooldown == 0) {
                        p.setCooldown(Material.GOAT_HORN, 420);

                        for (Player near : Bukkit.getOnlinePlayers()) {
                            if (p.getWorld() != near.getWorld() || !TeamController.isPlaying(near))
                                continue;

                            if (near.getLocation().distanceSquared(p.getLocation()) < 8 * 8
                                    && TeamController.getTeam(near.getUniqueId()) == TeamController.getTeam(p.getUniqueId())) {
                                //Add potion effects to nearby allies in an 8 block radius of you. Including yourself.
                                addPotionEffect(near, new PotionEffect(PotionEffectType.SPEED, 100, 1));
                                addPotionEffect(near, new PotionEffect(PotionEffectType.DOLPHINS_GRACE, 100, 1));
                                p.getWorld().playSound(p.getLocation(), Sound.ITEM_GOAT_HORN_SOUND_3 , 1, 2.2f);
                                p.getWorld().playSound(p.getLocation(), Sound.ITEM_GOAT_HORN_SOUND_3 , 1, 1.2f);
                                if (near != p) {
                                    Bukkit.getScheduler().runTaskAsynchronously(Main.plugin,
                                            () -> UpdateStats.addSupports(uuid, 1));
                                }
                            }
                        }

                    }
                }
            }
        }
    }

    private void addPotionEffect(Player player, PotionEffect potion) {
        Bukkit.getScheduler().runTask(Main.plugin, () -> player.addPotionEffect(potion));
    }

    @Override
    public ArrayList<Component> getGuiDescription() {
        return null;
    }
}
