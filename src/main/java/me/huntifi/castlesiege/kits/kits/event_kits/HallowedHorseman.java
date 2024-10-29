package me.huntifi.castlesiege.kits.kits.event_kits;

import me.huntifi.castlesiege.events.death.DeathEvent;
import me.huntifi.castlesiege.events.gameplay.HorseHandler;
import me.huntifi.castlesiege.kits.items.CSItemCreator;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.kits.EventKit;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.maps.TeamController;
import me.huntifi.conwymc.data_types.Tuple;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class HallowedHorseman extends EventKit implements Listener {

    private static final int health = 320;
    private static final double regen = 14;
    private static final double meleeDamage = 40;
    private static final int ladderCount = 4;
    private static final int horseHealth = 500;

    public HallowedHorseman() {
        super("Hallowed Horseman", health, regen, Material.SKELETON_HORSE_SPAWN_EGG,  NamedTextColor.GOLD, LocalDate.ofYearDay(LocalDate.now().getYear(), 291), LocalDate.ofYearDay(LocalDate.now().getYear(), 305));

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();

        // Weapon
        es.hotbar[0] = CSItemCreator.weapon(new ItemStack(Material.NETHERITE_SWORD),
                Component.text("Hallowed Sword", NamedTextColor.DARK_RED),
                List.of(Component.empty(),
                        Component.text("40 Melee Damage", NamedTextColor.RED),
                        Component.text("<< Hitting an enemy with less than", NamedTextColor.DARK_GRAY),
                        Component.text("20% HP, instantly kills them. >>", NamedTextColor.GRAY)),
                null, meleeDamage);
        // Voted weapon
        es.votedWeapon = new Tuple<>(
                CSItemCreator.weapon(new ItemStack(Material.NETHERITE_SWORD),
                        Component.text("Hallowed Sword", NamedTextColor.DARK_RED),
                        List.of(Component.empty(),
                                Component.text("40 Melee Damage", NamedTextColor.RED),
                                Component.text("⁎ Voted: +2 Melee Damage", NamedTextColor.GOLD),
                                Component.text("<< Hitting an enemy with less than", NamedTextColor.DARK_GRAY),
                                Component.text("20% HP, instantly kills them. >>", NamedTextColor.GRAY)),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), meleeDamage + 2),
                0);

        // Chestplate
        es.chest = CSItemCreator.item(new ItemStack(Material.NETHERITE_CHESTPLATE),
                Component.text("Hallowed Breastplate", NamedTextColor.DARK_RED),
                List.of(Component.empty(),
                        Component.text(health + " HP", NamedTextColor.RED),
                        Component.text(regen + " Regen", NamedTextColor.RED)), null);
        ItemMeta chest = es.chest.getItemMeta();
        ArmorMeta chestMeta = (ArmorMeta) chest;
        assert chest != null;
        ArmorTrim chestTrim = new ArmorTrim(TrimMaterial.NETHERITE, TrimPattern.SILENCE);
        ((ArmorMeta) chest).setTrim(chestTrim);
        es.chest.setItemMeta(chestMeta);

        // Leggings
        es.legs = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_LEGGINGS),
                Component.text("Hallowed Legplates", NamedTextColor.DARK_RED),
                List.of(Component.empty(),
                        Component.text(health + " HP", NamedTextColor.RED),
                        Component.text(regen + " Regen", NamedTextColor.RED)), null,
                Color.fromRGB(0, 0, 0));
        ItemMeta legs = es.legs.getItemMeta();
        ArmorMeta legsMeta = (ArmorMeta) legs;
        assert legs != null;
        ArmorTrim legsTrim = new ArmorTrim(TrimMaterial.NETHERITE, TrimPattern.SILENCE);
        ((ArmorMeta) legs).setTrim(legsTrim);
        es.legs.setItemMeta(legsMeta);

        // Boots
        es.feet = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_BOOTS),
                Component.text("Hallowed Boots", NamedTextColor.DARK_RED),
                List.of(Component.empty(),
                        Component.text(health + " HP", NamedTextColor.RED),
                        Component.text(regen + " Regen", NamedTextColor.RED)), null,
                Color.fromRGB(0, 0, 0));
        ItemMeta feet = es.feet.getItemMeta();
        ArmorMeta feetMeta = (ArmorMeta) feet;
        assert feet != null;
        ArmorTrim feetTrim = new ArmorTrim(TrimMaterial.NETHERITE, TrimPattern.SILENCE);
        ((ArmorMeta) feet).setTrim(feetTrim);
        es.feet.setItemMeta(feetMeta);
        // Voted Boots
        es.votedFeet = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_BOOTS),
                Component.text("Hallowed Boots", NamedTextColor.DARK_RED),
                List.of(Component.empty(),
                        Component.text(health + " HP", NamedTextColor.RED),
                        Component.text(regen + " Regen", NamedTextColor.RED),
                        Component.empty(),
                        Component.text("⁎ Voted: Depth Strider II", NamedTextColor.GOLD)),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)),
                Color.fromRGB(24, 3, 3));
        ItemMeta votedFeet = es.votedFeet.getItemMeta();
        ArmorMeta votedFeetMeta = (ArmorMeta) votedFeet;
        assert votedFeet != null;
        ArmorTrim votedFeetTrim = new ArmorTrim(TrimMaterial.NETHERITE, TrimPattern.SILENCE);
        ((ArmorMeta) votedFeet).setTrim(votedFeetTrim);
        es.votedFeet.setItemMeta(votedFeetMeta);

        // Horse
        es.hotbar[2] = CSItemCreator.item(new ItemStack(Material.WHEAT),
                Component.text("Spawn Horse", NamedTextColor.GREEN), null, null);
        HorseHandler.add(name, 600, horseHealth, 2, 0.2429, 0.8,
                Material.IRON_HORSE_ARMOR, Arrays.asList(
                        new PotionEffect(PotionEffectType.JUMP, 999999, 1),
                        new PotionEffect(PotionEffectType.REGENERATION, 999999, 3),
                        new PotionEffect(PotionEffectType.SPEED, 999999, 1)
                ), "skeletal"
        );

        // Ladders
        es.hotbar[1] = new ItemStack(Material.LADDER, ladderCount);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, ladderCount + 2), 1);

        // Death Messages
        super.deathMessage[0] = "";
        super.deathMessage[1] = " has ended you!";
        super.killMessage[0] = " has ended ";

        super.equipment = es;

    }


    /**
     * Decapitate an enemy if their hp is below 20%
     * @param e The event called when hitting another player
     */
    @EventHandler(ignoreCancelled = true)
    public void onExecute(EntityDamageByEntityEvent e) {
        // Both are players
        if (e.getEntity() instanceof Attributable && e.getDamager() instanceof Player) {
            Attributable defAttri = (Attributable) e.getEntity();
            Damageable defender = (Damageable) e.getEntity();
            Player attacker = (Player) e.getDamager();

            // Executioner hits with axe
            if (Objects.equals(Kit.equippedKits.get(attacker.getUniqueId()).name, name) &&
                    attacker.getInventory().getItemInMainHand().getType() == Material.NETHERITE_SWORD) {

                AttributeInstance healthAttribute = defAttri.getAttribute(Attribute.GENERIC_MAX_HEALTH);
                assert healthAttribute != null;

                // Execute
                if (defender.getHealth() < healthAttribute.getValue() * 0.2) {
                    e.setCancelled(true);

                    Location loc = defender.getLocation();
                    // Do extra stuff if they were a player
                    if (defender instanceof Player) {
                        Player p = (Player) defender;
                        loc = p.getEyeLocation();

                        Material wool = TeamController.getTeam(defender.getUniqueId()).primaryWool;
                        defender.getWorld().dropItem(loc, new ItemStack(wool)).setPickupDelay(32767);

                        DeathEvent.setKiller(p, attacker);
                    }
                    defender.getWorld().playSound(loc, Sound.ENTITY_WARDEN_DEATH, 1, 1.2F);
                    defender.setHealth(0);
                }
            }
        }
    }

    @Override
    public ArrayList<Component> getGuiDescription() {
        ArrayList<Component> kitLore = new ArrayList<>();
        kitLore.add(Component.text("Halloween Only Kit!", NamedTextColor.GOLD));
        kitLore.add(Component.text("A dark knight that can summon its undead steed", NamedTextColor.GRAY));
        kitLore.add(Component.text("and execute enemies below 20% HP.", NamedTextColor.GRAY));
        kitLore.addAll(getBaseStats(health, regen, meleeDamage, ladderCount));
        kitLore.add(Component.empty());
        kitLore.add(Component.text("Passive:", NamedTextColor.DARK_GREEN));
        kitLore.add(Component.text("- Execute enemies below 20% HP.", NamedTextColor.GRAY));
        kitLore.add(Component.text("Active:", NamedTextColor.GOLD));
        kitLore.add(Component.text("- Can summon a skeletal steed.", NamedTextColor.GRAY));
        return kitLore;
    }
}
