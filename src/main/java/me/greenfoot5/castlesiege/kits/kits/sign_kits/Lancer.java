package me.greenfoot5.castlesiege.kits.kits.sign_kits;

import me.greenfoot5.castlesiege.events.combat.InCombat;
import me.greenfoot5.castlesiege.events.gameplay.HorseHandler;
import me.greenfoot5.castlesiege.kits.items.CSItemCreator;
import me.greenfoot5.castlesiege.kits.items.EquipmentSet;
import me.greenfoot5.castlesiege.kits.kits.Kit;
import me.greenfoot5.castlesiege.kits.kits.SignKit;
import me.greenfoot5.conwymc.data_types.Tuple;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Lancer extends SignKit implements Listener {

    private static final int health = 270;
    private static final double regen = 9;
    private static final double meleeDamage = 40.5;
    private static final double throwDamage = 50;
    private static final double horseThrowDamage = 70;
    private static final int ladderCount = 4;
    private static final int horseHealth = 200;

    /**
     * Creates a new Helms Deep Lancer
     */
    public Lancer() {
        super("Lancer", health, regen, Material.STICK, 2500);

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();

        // Weapon
        es.hotbar[0] = CSItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                Component.text("Sword", NamedTextColor.GREEN),
                List.of(Component.empty(),
                Component.text(meleeDamage + " Melee Damage", NamedTextColor.DARK_GREEN)),
                null, meleeDamage);
        // Voted Weapon
        es.votedWeapon = new Tuple<>(
                CSItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                        Component.text("Sword", NamedTextColor.GREEN),
                        List.of(Component.empty(),
                        Component.text((meleeDamage + 2) + " Melee Damage", NamedTextColor.DARK_GREEN),
                        Component.text("⁎ Voted: +2 Melee Damage", NamedTextColor.GREEN)),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOTING, 1)), meleeDamage + 2),
                0);

        // Weapon
        es.hotbar[1] = CSItemCreator.weapon(new ItemStack(Material.STICK, 3),
                Component.text("Spear", NamedTextColor.GREEN),
                List.of(Component.empty(),
                Component.text(meleeDamage + " Melee Damage", NamedTextColor.DARK_GREEN),
                Component.text(throwDamage + " Throw Damage", NamedTextColor.DARK_GREEN),
                Component.text(horseThrowDamage + " Mounted Throw Damage", NamedTextColor.DARK_GREEN),
                Component.text("<< Right Click To Throw >>", NamedTextColor.DARK_GRAY),
                Component.text("Throws a regular spear in front of you.", NamedTextColor.GRAY),
                Component.text("Will throw a stronger spear when mounted.", NamedTextColor.GRAY)), null, meleeDamage);

        // Chestplate
        es.chest = CSItemCreator.item(new ItemStack(Material.GOLDEN_CHESTPLATE),
                Component.text("Bronze Chestplate", NamedTextColor.GREEN),
                List.of(Component.empty(),
                        Component.text(health + " HP", NamedTextColor.DARK_GREEN),
                        Component.text(regen + " Regen", NamedTextColor.DARK_GREEN)), null);

        // Leggings
        es.legs = CSItemCreator.item(new ItemStack(Material.CHAINMAIL_LEGGINGS),
                Component.text("Chainmail Leggings", NamedTextColor.GREEN),
                List.of(Component.empty(),
                        Component.text(health + " HP", NamedTextColor.DARK_GREEN),
                        Component.text(regen + " Regen", NamedTextColor.DARK_GREEN)), null);

        // Boots
        es.feet = CSItemCreator.item(new ItemStack(Material.IRON_BOOTS),
                Component.text("Iron Boots", NamedTextColor.GREEN),
                List.of(Component.empty(),
                        Component.text(health + " HP", NamedTextColor.DARK_GREEN),
                        Component.text(regen + " Regen", NamedTextColor.DARK_GREEN)), null);
        // Voted Boots
        es.votedFeet = CSItemCreator.item(new ItemStack(Material.IRON_BOOTS),
                Component.text("Iron Boots", NamedTextColor.GREEN),
                List.of(Component.empty(),
                        Component.text(health + " HP", NamedTextColor.DARK_GREEN),
                        Component.text(regen + " Regen", NamedTextColor.DARK_GREEN),
                        Component.empty(),
                        Component.text("⁎ Voted: Depth Strider II", NamedTextColor.GREEN)),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)));

        // Ladders
        es.hotbar[2] = new ItemStack(Material.LADDER, ladderCount);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, ladderCount), 2);

        // Horse
        es.hotbar[3] = CSItemCreator.item(new ItemStack(Material.WHEAT),
                Component.text("Spawn Horse", NamedTextColor.GREEN),
                List.of(Component.empty(),
                        Component.text("<< Right Click To Summon >>", NamedTextColor.DARK_GRAY),
                        Component.text("Summons your horse, with stats:", NamedTextColor.GRAY),
                        Component.text("- HP: " + horseHealth , NamedTextColor.GRAY),
                        Component.text("- Speed: 0.2425", NamedTextColor.GRAY),
                        Component.text("- Jump Strength: 0.8", NamedTextColor.GRAY)), null);
        HorseHandler.add(name, 600, horseHealth, 1, 0.2425, 0.8,
                Material.GOLDEN_HORSE_ARMOR, Arrays.asList(
                        new PotionEffect(PotionEffectType.JUMP_BOOST, 999999, 1),
                        new PotionEffect(PotionEffectType.REGENERATION, 999999, 0),
                        new PotionEffect(PotionEffectType.SPEED, 999999, 0)
                ), "regular");

        super.equipment = es;

        // Death Messages
        super.projectileDeathMessage[0] = "You were impaled by ";
        super.projectileKillMessage[0] = " impaled ";
    }

    /**
     * Activate the lancer's ability of throwing a spear
     * @param e The event called when right-clicking with a stick
     */
    @EventHandler
    public void throwSpear(PlayerInteractEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();
        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(uuid)) {
            return;
        }
        Player p = e.getPlayer();
        if (Objects.equals(Kit.equippedKits.get(uuid).name, name)) {
            ItemStack stick = p.getInventory().getItemInMainHand();
            if (stick.getType().equals(Material.STICK)) {
                if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    //Not allowed to throw whilst clicking on enderchests or cakes.
                    if (e.getClickedBlock() != null) {
                        if (interactableBlock(e.getClickedBlock()) || p.getInventory().getItemInMainHand().getType() == Material.LADDER
                                || p.getInventory().getItemInMainHand().getType() == Material.WHEAT) {
                            return;
                        }
                    }
                    if (p.getInventory().getItemInMainHand().getType() == Material.WHEAT && p.getInventory().getItemInMainHand().getType() == Material.LADDER) {
                            return;
                    }
                    int cooldown = p.getCooldown(Material.STICK);
                    if (cooldown == 0) {
                        stick.setAmount(stick.getAmount() - 1);
                        p.setCooldown(Material.STICK, 200);
                        p.launchProjectile(Arrow.class).setVelocity(p.getLocation().getDirection().multiply(2.5));
                    }
                }
            }
        }
    }


    /**
     * Set the thrown spear's damage
     * @param e The event called when an arrow hits a player
     */
    @EventHandler(priority = EventPriority.LOW)
    public void changeSpearDamage(ProjectileHitEvent e) {
        if (e.getEntity() instanceof Arrow arrow) {

            if (arrow.getShooter() instanceof Player damages) {

                if (Objects.equals(Kit.equippedKits.get(damages.getUniqueId()).name, name)) {
                    if (damages.isInsideVehicle()) {
                        arrow.setDamage(horseThrowDamage);
                    } else {
                        arrow.setDamage(throwDamage);
                    }
                }
            }
        }
    }

    @Override
    public ArrayList<Component> getGuiDescription() {
        ArrayList<Component> description = new ArrayList<>();
        description.add(Component.text("//TODO - Add kit description", NamedTextColor.GRAY));
        return description;
    }
}
