package me.huntifi.castlesiege.kits.kits.event_kits;

import me.huntifi.castlesiege.events.combat.AssistKill;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.events.death.DeathEvent;
import me.huntifi.castlesiege.kits.items.CSItemCreator;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.kits.EventKit;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.maps.TeamController;
import me.huntifi.conwymc.data_types.Tuple;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
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
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Vampire extends EventKit implements Listener {

    private static final int health = 290;
    private static final double regen = 18;
    private static final double meleeDamage = 40;
    private static final int ladderCount = 4;

    public Vampire() {
        super("Vampire", health, regen, Material.GHAST_TEAR,  NamedTextColor.GOLD, LocalDate.ofYearDay(LocalDate.now().getYear(), 291), LocalDate.ofYearDay(LocalDate.now().getYear(), 304));

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();

        // Weapon
        es.hotbar[0] = CSItemCreator.weapon(new ItemStack(Material.GHAST_TEAR),
                Component.text("Vampire Fang", NamedTextColor.DARK_RED),
                List.of(Component.empty(),
                        Component.text("40 Melee Damage", NamedTextColor.RED),
                        Component.text("5s cooldown", NamedTextColor.RED),
                        Component.text("<< Right Click An Enemy >>", NamedTextColor.DARK_GRAY),
                        Component.text("Deals 50 DMG & Heals 50 HP.", NamedTextColor.GRAY)),
                null, meleeDamage);
        // Voted weapon
        es.votedWeapon = new Tuple<>(
                CSItemCreator.weapon(new ItemStack(Material.GHAST_TEAR),
                        Component.text("Vampire Fang", NamedTextColor.DARK_RED),
                        List.of(Component.empty(),
                                Component.text("40 Melee Damage", NamedTextColor.RED),
                                Component.text("5s cooldown", NamedTextColor.RED),
                                Component.text("⁎ Voted: +2 Melee Damage", NamedTextColor.GOLD),
                                Component.text("<< Right Click An Enemy >>", NamedTextColor.DARK_GRAY),
                                Component.text("Deals 50 DMG & Heals 50 HP.", NamedTextColor.GRAY)),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), meleeDamage + 2),
                0);

        // Chestplate
        es.chest = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE),
                Component.text("Vampire's Tuxedo", NamedTextColor.DARK_RED),
                List.of(Component.empty(),
                        Component.text(health + " HP", NamedTextColor.RED),
                        Component.text(regen + " Regen", NamedTextColor.RED)), null,
                Color.fromRGB(51, 46, 46));
        ItemMeta chest = es.chest.getItemMeta();
        ArmorMeta chestMeta = (ArmorMeta) chest;
        assert chest != null;
        ArmorTrim chestTrim = new ArmorTrim(TrimMaterial.REDSTONE, TrimPattern.VEX);
        ((ArmorMeta) chest).setTrim(chestTrim);
        es.chest.setItemMeta(chestMeta);

        // Leggings
        es.legs = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_LEGGINGS),
                Component.text("Antique Vampire Leggings", NamedTextColor.DARK_RED),
                List.of(Component.empty(),
                        Component.text(health + " HP", NamedTextColor.RED),
                        Component.text(regen + " Regen", NamedTextColor.RED)), null,
                Color.fromRGB(111, 94, 94));
        ItemMeta legs = es.legs.getItemMeta();
        ArmorMeta legsMeta = (ArmorMeta) legs;
        assert legs != null;
        ArmorTrim legsTrim = new ArmorTrim(TrimMaterial.NETHERITE, TrimPattern.SPIRE);
        ((ArmorMeta) legs).setTrim(legsTrim);
        es.legs.setItemMeta(legsMeta);

        // Boots
        es.feet = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_BOOTS),
                Component.text("Vampire Boots", NamedTextColor.DARK_RED),
                List.of(Component.empty(),
                        Component.text(health + " HP", NamedTextColor.RED),
                        Component.text(regen + " Regen", NamedTextColor.RED)), null,
                Color.fromRGB(24, 3, 3));
        ItemMeta feet = es.feet.getItemMeta();
        ArmorMeta feetMeta = (ArmorMeta) feet;
        assert feet != null;
        ArmorTrim feetTrim = new ArmorTrim(TrimMaterial.NETHERITE, TrimPattern.RAISER);
        ((ArmorMeta) feet).setTrim(feetTrim);
        es.feet.setItemMeta(feetMeta);
        // Voted Boots
        es.votedFeet = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_BOOTS),
                Component.text("Vampire Boots", NamedTextColor.DARK_RED),
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
        ArmorTrim votedFeetTrim = new ArmorTrim(TrimMaterial.NETHERITE, TrimPattern.RAISER);
        ((ArmorMeta) votedFeet).setTrim(votedFeetTrim);
        es.votedFeet.setItemMeta(votedFeetMeta);

        // Ladders
        es.hotbar[1] = new ItemStack(Material.LADDER, ladderCount);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, ladderCount + 2), 1);

        super.potionEffects.add(new PotionEffect(PotionEffectType.JUMP, 999999, 0));
        super.potionEffects.add(new PotionEffect(PotionEffectType.FAST_DIGGING, 999999, 1));
        super.potionEffects.add(new PotionEffect(PotionEffectType.SPEED, 999999, 0));

        // Death Messages
        super.deathMessage[0] = "";
        super.deathMessage[1] = " drained you of all your blood!";
        super.killMessage[0] = " has drained all of ";
        super.killMessage[1] = " 's blood!";

        super.equipment = es;

    }


    /**
     *
     * @param e vampire's ability to drain health.
     */
    @EventHandler
    public void onDrain(PlayerInteractEntityEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();
        if (Objects.equals(Kit.equippedKits.get(uuid).name, name)) {
            Player p = e.getPlayer();
            //Stuns the horse which the cave troll hits.
            if (e.getRightClicked() instanceof Horse) {
                Horse h = (Horse) e.getRightClicked();
                Player horseOwner = (Player) h.getOwner();
                assert horseOwner != null;
                if (TeamController.getTeam(horseOwner.getUniqueId()) != TeamController.getTeam(p.getUniqueId())) {
                    drainHorse(h, p);
                }
            }

            //Check if the hit entity is a player, otherwise do nothing.
            if (!(e.getRightClicked() instanceof Player)) {
                return;
            }

            //The player who is being right-clicked on by the troll.
            Player q = (Player) e.getRightClicked();

            // Prevent using in lobby
            if (InCombat.isPlayerInLobby(uuid)) {
                return;
            }

            //Check if the players are not on the same team, if true then perform the cave troll's attack.
            if (TeamController.getTeam(q.getUniqueId()) != TeamController.getTeam(p.getUniqueId())) {
                drain(q, p);
            }

        }
    }


    /**
     * Activate the vampire's ability, draining health
     * @param victim The opponent
     * @param vampire The cave troll
     */
    private void drain(Player victim, Player vampire) {
        if (vampire.getInventory().getItemInMainHand().getType() == Material.GHAST_TEAR &&
                vampire.getCooldown(Material.GHAST_TEAR) == 0) {

            // Activate stun
            vampire.setCooldown(Material.GHAST_TEAR, 100);
            victim.getWorld().playSound(victim.getLocation(), Sound.ENTITY_WOLF_GROWL , 1, 2.5f);
            victim.getWorld().playSound(victim.getLocation(), Sound.ENTITY_WOLF_HOWL , 1, 3f);
            if (50 >= victim.getHealth()) {
                DeathEvent.setKiller(victim, vampire);
            }
            victim.damage(50);
            AssistKill.addDamager(victim.getUniqueId(), vampire.getUniqueId(), 50);
            if (vampire.getHealth() + 50 <= health) {
                vampire.setHealth(vampire.getHealth() + 50);
            } else {
                vampire.setHealth(health);
            }
        }
    }

    /**
     * Activate the vampire's ability, draining health
     * @param victim The opponent
     * @param vampire The cave troll
     */
    private void drainHorse(Horse victim, Player vampire) {
        if (vampire.getInventory().getItemInMainHand().getType() == Material.GHAST_TEAR &&
                vampire.getCooldown(Material.GHAST_TEAR) == 0) {

            // Activate stun
            vampire.setCooldown(Material.GHAST_TEAR, 80);
            victim.getWorld().playSound(victim.getLocation(), Sound.ENTITY_WOLF_GROWL , 1, 2.5f);
            victim.getWorld().playSound(victim.getLocation(), Sound.ENTITY_WOLF_HOWL , 1, 3f);
            victim.damage(50);
            if (vampire.getHealth() + 50 <= health) {
                vampire.setHealth(vampire.getHealth() + 50);
            } else {
                vampire.setHealth(health);
            }
        }
    }


    @EventHandler
    public void onPlayerFall(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            if (Objects.equals(Kit.equippedKits.get(e.getEntity().getUniqueId()).name, name)) {
                if (e.getCause() == EntityDamageEvent.DamageCause.FALL) {
                    e.setDamage(0);
                }
            }
        }
    }



    @Override
    public ArrayList<Component> getGuiDescription() {
        ArrayList<Component> kitLore = new ArrayList<>();
        kitLore.add(Component.text("Halloween Only Kit!", NamedTextColor.GOLD));
        kitLore.add(Component.text("Fast life-stealing kit that doesn't", NamedTextColor.GRAY));
        kitLore.add(Component.text("take fall damage.", NamedTextColor.GRAY));
        kitLore.addAll(getBaseStats(health, regen, meleeDamage, ladderCount));
        kitLore.add(Component.empty());
        kitLore.add(Component.text("Effects:", NamedTextColor.DARK_PURPLE));
        kitLore.add(Component.text("- Speed I", NamedTextColor.GRAY));
        kitLore.add(Component.text("- Haste I", NamedTextColor.GRAY));
        kitLore.add(Component.text("- Jump I", NamedTextColor.GRAY));
        kitLore.add(Component.empty());
        kitLore.add(Component.text("Passive:", NamedTextColor.DARK_GREEN));
        kitLore.add(Component.text("- No fall damage", NamedTextColor.GRAY));
        kitLore.add(Component.text("Active:", NamedTextColor.GOLD));
        kitLore.add(Component.text("- Can right click enemies to", NamedTextColor.GRAY));
        kitLore.add(Component.text("deal damage & heal themselves.", NamedTextColor.GRAY));
        return kitLore;
    }
}
