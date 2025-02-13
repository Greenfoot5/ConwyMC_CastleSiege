package me.greenfoot5.castlesiege.kits.kits.coin_kits;

import me.greenfoot5.castlesiege.Main;
import me.greenfoot5.castlesiege.events.combat.AssistKill;
import me.greenfoot5.castlesiege.events.combat.InCombat;
import me.greenfoot5.castlesiege.events.timed.BarCooldown;
import me.greenfoot5.castlesiege.kits.items.CSItemCreator;
import me.greenfoot5.castlesiege.kits.items.EquipmentSet;
import me.greenfoot5.castlesiege.kits.kits.CoinKit;
import me.greenfoot5.castlesiege.misc.CSNameTag;
import me.greenfoot5.conwymc.data_types.Tuple;
import me.greenfoot5.conwymc.util.Messenger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * The ranger kit
 */
public class Ranger extends CoinKit implements Listener {

    private static final int health = 210;
    private static final double regen = 10.5;
    private static final double meleeDamage = 36;
    private static final double arrowDamage = 17;
    private static final int ladderCount = 4;
    private static final int arrowCount = 48;

    private boolean canBackstab = false;
    private BukkitRunnable backstabCharge = null;

    /**
     * Create a new Ranger
     */
    public Ranger() {
        super("Ranger", health, regen, Material.LIME_DYE);

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();

        // Weapon
        es.hotbar[0] = CSItemCreator.weapon(new ItemStack(Material.STONE_SWORD),
                Component.text("Dagger", NamedTextColor.GREEN),
                List.of(Component.empty(),
                        Component.text("36 Melee Damage", NamedTextColor.DARK_GREEN)),
                null, meleeDamage);
        // Voted weapon
        es.votedWeapon = new Tuple<>(
                CSItemCreator.weapon(new ItemStack(Material.STONE_SWORD),
                        Component.text("Dagger", NamedTextColor.GREEN),
                        List.of(Component.empty(),
                                Component.text("38 Melee Damage", NamedTextColor.DARK_GREEN),
								Component.text("⁎ Voted: +2 Melee Damage", NamedTextColor.DARK_AQUA)),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOTING, 0)), meleeDamage + 2),
                0);

        // Chestplate
        es.chest = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE),
                Component.text("Leather Chestplate", NamedTextColor.GREEN),
                List.of(Component.empty(),
                        Component.text(health + " HP", NamedTextColor.DARK_GREEN),
                        Component.text(regen + " Regen", NamedTextColor.DARK_GREEN)), null,
                Color.fromRGB(28, 165, 33));

        // Leggings
        es.legs = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_LEGGINGS),
                Component.text("Leather Leggings", NamedTextColor.GREEN),
                List.of(Component.empty(),
                        Component.text(health + " HP", NamedTextColor.DARK_GREEN),
                        Component.text(regen + " Regen", NamedTextColor.DARK_GREEN)), null,
                Color.fromRGB(32, 183, 37));

        // Boots
        es.feet = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_BOOTS),
                Component.text("Leather Boots", NamedTextColor.GREEN),
                List.of(Component.empty(),
                        Component.text(health + " HP", NamedTextColor.DARK_GREEN),
                        Component.text(regen + " Regen", NamedTextColor.DARK_GREEN)), null,
                Color.fromRGB(28, 165, 33));
        // Voted Boots
        es.votedFeet = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_BOOTS),
                Component.text("Leather Boots", NamedTextColor.GREEN),
                List.of(Component.empty(),
                        Component.text(health + " HP", NamedTextColor.DARK_GREEN),
                        Component.text(regen + " Regen", NamedTextColor.DARK_GREEN),
                        Component.empty(),
                        Component.text("⁎ Voted: Depth Strider II", NamedTextColor.DARK_AQUA)),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)),
                Color.fromRGB(28, 165, 33));

        // Regular Bow
        es.hotbar[1] = CSItemCreator.item(new ItemStack(Material.BOW),
                Component.text("Bow", NamedTextColor.GREEN),
                List.of(Component.empty(),
                        Component.text(arrowDamage + " Ranged Damage", NamedTextColor.DARK_GREEN)), null);

        // Volley Bow
        es.hotbar[2] = CSItemCreator.item(new ItemStack(Material.BOW),
                Component.text("Volley Bow", NamedTextColor.GREEN),
                List.of(Component.empty(),
                        Component.text("Shoot 5 arrows in a spread", NamedTextColor.BLUE),
                        Component.empty(),
                        Component.text(arrowDamage + " Ranged Damage", NamedTextColor.DARK_GREEN)), null);

        // Burst Bow
        es.hotbar[3] = CSItemCreator.item(new ItemStack(Material.BOW),
                Component.text("Rapid Fire Bow", NamedTextColor.GREEN),
                List.of(Component.empty(),
                        Component.text("Shoot 5 arrows consecutively", NamedTextColor.BLUE),
                        Component.empty(),
                        Component.text(arrowDamage + " Ranged Damage", NamedTextColor.DARK_GREEN)),null);

        // Ladders
        es.hotbar[4] = new ItemStack(Material.LADDER, ladderCount);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, ladderCount + 2), 4);

        // Arrows
        es.hotbar[7] = new ItemStack(Material.ARROW, arrowCount);

        super.equipment = es;

        // Perm Potion Effect
        super.potionEffects.add(new PotionEffect(PotionEffectType.SPEED, 999999, 0));
        super.potionEffects.add(new PotionEffect(PotionEffectType.HASTE, 999999, 0));

        // Death Messages
        super.projectileDeathMessage[0] = "You were turned into a porcupine by ";
        super.projectileKillMessage[0] = " turned ";
        super.projectileKillMessage[1] = " into a porcupine";
    }

    /**
     * Set the arrow-damage of a ranger's arrows
     * @param e The event called when a player is hit by an arrow
     */
    @EventHandler(priority = EventPriority.LOW)
    public void onArrowHit(ProjectileHitEvent e) {
        if (e.getEntity().getShooter() != equippedPlayer)
            return;

        if (e.getEntity() instanceof Arrow) {
            ((Arrow) e.getEntity()).setDamage(arrowDamage);
        }
    }

    /**
     * Activate the volley or burst ability
     * @param e The event called when a player shoots a bow
     */
    @EventHandler(ignoreCancelled = true)
    public void onShootBow(EntityShootBowEvent e) {
        if (e.getEntity() != equippedPlayer)
            return;

        Component bow = Objects.requireNonNull(Objects.requireNonNull(e.getBow()).getItemMeta()).displayName();
        String bowName = PlainTextComponentSerializer.plainText().serialize(bow);
        boolean isCritical = false;
        if (e.getProjectile() instanceof AbstractArrow arrow) {
            isCritical = arrow.isCritical();
        }

        if (Objects.equals(bowName, "Volley Bow")) {
            Vector v = e.getProjectile().getVelocity();
            volleyAbility(v, isCritical);
        } else if (Objects.equals(bowName, "Rapid Fire Bow")) {
            rapidFire(e.getForce(), isCritical);
        }
    }

    /**
     * Activate the volley ability, shooting 5 arrows at once
     * @param v The vector of the original arrow
     * @param isCritical If the arrows should be critical
     */
    private void volleyAbility(Vector v, boolean isCritical) {
        Messenger.sendActionSuccess("You shot your volley bow!", equippedPlayer);
        equippedPlayer.setCooldown(Material.BOW, 60);

        // Shoot the extra arrows
        if (removeArrow()) {
            Arrow arrow = equippedPlayer.launchProjectile(Arrow.class, v.rotateAroundY(0.157));
            arrow.setCritical(isCritical);
        }
        if (removeArrow()) {
            Arrow arrow = equippedPlayer.launchProjectile(Arrow.class, v.rotateAroundY(0.157));
            arrow.setCritical(isCritical);
        }
        if (removeArrow()) {
            Arrow arrow = equippedPlayer.launchProjectile(Arrow.class, v.rotateAroundY(-0.471));
            arrow.setCritical(isCritical);
        }
        if (removeArrow()) {
            Arrow arrow = equippedPlayer.launchProjectile(Arrow.class, v.rotateAroundY(-0.157));
            arrow.setCritical(isCritical);
        }
    }

    /**
     * Activate the burst ability, shooting 3 arrows consecutively
     * @param force The force of the original arrow
     * @param isCritical If the arrows should be critical
     */
    private void rapidFire(float force, boolean isCritical) {
        Messenger.sendActionInfo("You shot your rapid fire bow!", equippedPlayer);
        equippedPlayer.setCooldown(Material.BOW, 100);
        rapidArrow(force, 11, isCritical);
        rapidArrow(force, 21, isCritical);
        rapidArrow(force, 31, isCritical);
    }

    /**
     * Shoot a single arrow from the burst ability
     * @param force The force of the original arrow
     * @param delay The delay with which to shoot the arrow
     * @param isCritical If the arrows should be critical
     */
    private void rapidArrow(float force, int delay, boolean isCritical) {
        new BukkitRunnable() {
            @Override
            public void run() {
                // Shoot if the player has an arrow
                if (removeArrow()) {
                    Arrow a = equippedPlayer.launchProjectile(Arrow.class);
                    a.setCritical(isCritical);
                    a.setVelocity(a.getVelocity().normalize().multiply(force));
                }
            }
        }.runTaskLater(Main.plugin, delay);
    }

    /**
     * Remove an arrow from the player's inventory
     * @return true if the player has an arrow to remove, false otherwise
     */
    private boolean removeArrow() {
        PlayerInventory inv = equippedPlayer.getInventory();

        // Try offhand first
        if (inv.getItemInOffHand().getType() == Material.ARROW) {
            ItemStack item = inv.getItemInOffHand();
            item.setAmount(item.getAmount() - 1);
            return true;
        // Try inventory
        } else if (inv.contains(Material.ARROW)) {
            inv.removeItem(new ItemStack(Material.ARROW));
            return true;
        }

        // No arrow found
        return false;
    }


    /**
     * Instantly kills players when hit in the back by a sneaking ranger
     * @param e The event called when a player attacks another player
     */
    @EventHandler(ignoreCancelled = true)
    public void backStabDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() != equippedPlayer)
            return;

        if (!(e.getEntity() instanceof Player hit))
            return;

        Location hitLoc = hit.getLocation();
        Location damagerLoc = equippedPlayer.getLocation();

        // Basically what happens here is you check whether the player
        // is not looking at you at all (so having their back aimed at you.)
        if (damagerLoc.getYaw() <= hitLoc.getYaw() + 45 && damagerLoc.getYaw() >= hitLoc.getYaw() - 45
                && canBackstab) {

            e.setCancelled(true);
            Messenger.sendWarning("You got backstabbed by " + CSNameTag.mmUsername(equippedPlayer), hit);
            Messenger.sendSuccess("You backstabbed " + CSNameTag.mmUsername(hit), equippedPlayer);
            AssistKill.addDamager(hit.getUniqueId(), equippedPlayer.getUniqueId(), hit.getHealth());
            hit.damage(hit.getHealth(), equippedPlayer);

        }
    }

    /**
     * The event called when a player starts and stops sneaking
     * @param e exp bar shows as full indicating the ranger is ready to backstab.
     */
    @EventHandler
    public void backStabDetection(PlayerToggleSneakEvent e) {
        if (e.getPlayer() != equippedPlayer)
            return;

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(equippedPlayer.getUniqueId())) {
            return;
        }

        if (backstabCharge != null) {
            backstabCharge.cancel();
            backstabCharge = null;
            return;
        }

        // p.isSneaking() gives the sneaking status before the toggle is processed
        if (equippedPlayer.isSneaking()) {
            canBackstab = false;
            BarCooldown.remove(equippedPlayer.getUniqueId());
        } else {
            backstabCharge = new BukkitRunnable() {
                @Override
                public void run() {
                    canBackstab = true;
                    backstabCharge = null;
                }
            };
            backstabCharge.runTaskLater(Main.plugin, 40);
            BarCooldown.add(equippedPlayer.getUniqueId(), 40);
        }
    }


    /**
     * @return The lore to add to the kit gui item
     */
    @Override
    public ArrayList<Component> getGuiDescription() {
        ArrayList<Component> kitLore = new ArrayList<>();
        kitLore.add(Component.text("Ranger is a versatile ranged kit that", NamedTextColor.GRAY));
        kitLore.add(Component.text("can shoot volleys and bursts of arrows", NamedTextColor.GRAY));
        // TODO - Check ranged damage
        kitLore.addAll(getBaseStats(health, regen, meleeDamage, 18, ladderCount, arrowCount));
        kitLore.add(Component.empty());
        kitLore.add(Component.text("Effects:", NamedTextColor.DARK_PURPLE));
        kitLore.add(Component.text("- Speed I", NamedTextColor.GRAY));
        kitLore.add(Component.text("- Haste I", NamedTextColor.GRAY));
        kitLore.add(Component.empty());
        kitLore.add(Component.text("Active:", NamedTextColor.GOLD));
        kitLore.add(Component.text("- Can fire a volley of arrows", NamedTextColor.GRAY));
        kitLore.add(Component.text("- Can fire a bust of arrows", NamedTextColor.GRAY));
        kitLore.add(Component.text("- Can instakill enemies with a backstab", NamedTextColor.GRAY));
        return kitLore;
    }
}
