package me.huntifi.castlesiege.kits.kits.donator_kits;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.events.combat.AssistKill;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.events.death.DeathEvent;
import me.huntifi.castlesiege.events.timed.BarCooldown;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.kits.kits.DonatorKit;
import me.huntifi.castlesiege.kits.kits.Kit;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
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

import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

/**
 * The ranger kit
 */
public class Ranger extends DonatorKit implements Listener {

    private boolean canBackstab = false;
    private BukkitRunnable br = null;

    /**
     * Set the equipment and attributes of this kit
     */
    public Ranger() {
        super("Ranger", 210, 10.5, Material.LIME_DYE);

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Weapon
        es.hotbar[0] = ItemCreator.weapon(new ItemStack(Material.STONE_SWORD),
                ChatColor.GREEN + "Dagger", null, null, 36);
        // Voted weapon
        es.votedWeapon = new Tuple<>(
                ItemCreator.weapon(new ItemStack(Material.STONE_SWORD),
                        ChatColor.GREEN + "Dagger",
                        Collections.singletonList(ChatColor.AQUA + "- voted: +2 damage"),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), 38),
                0);

        // Chestplate
        es.chest = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE),
                ChatColor.GREEN + "Leather Chestplate", null, null,
                Color.fromRGB(28, 165, 33));

        // Leggings
        es.legs = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_LEGGINGS),
                ChatColor.GREEN + "Leather Leggings", null, null,
                Color.fromRGB(32, 183, 37));

        // Boots
        es.feet = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_BOOTS),
                ChatColor.GREEN + "Leather Boots", null, null,
                Color.fromRGB(28, 165, 33));
        // Voted Boots
        es.votedFeet = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_BOOTS),
                ChatColor.GREEN + "Leather Boots",
                Collections.singletonList(ChatColor.AQUA + "- voted: Depth Strider II"),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)),
                Color.fromRGB(28, 165, 33));

        // Regular Bow
        es.hotbar[1] = ItemCreator.item(new ItemStack(Material.BOW),
                ChatColor.GREEN + "Bow", null, null);

        // Volley Bow
        es.hotbar[2] = ItemCreator.item(new ItemStack(Material.BOW),
                ChatColor.GREEN + "Volley Bow",
                Collections.singletonList(ChatColor.AQUA + "Shoot 5 arrows at once"), null);

        // Burst Bow
        es.hotbar[3] = ItemCreator.item(new ItemStack(Material.BOW),
                ChatColor.GREEN + "Burst Bow",
                Collections.singletonList(ChatColor.AQUA + "Shoot 4 consecutive arrows"), null);

        // Ladders
        es.hotbar[4] = new ItemStack(Material.LADDER, 4);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, 6), 4);

        // Arrows
        es.hotbar[7] = new ItemStack(Material.ARROW, 48);

        super.equipment = es;

        // Perm Potion Effect
        super.potionEffects.add(new PotionEffect(PotionEffectType.SPEED, 999999, 0));
        super.potionEffects.add(new PotionEffect(PotionEffectType.FAST_DIGGING, 999999, 0));

        // Death Messages
        super.projectileDeathMessage[0] = "You were turned into a porcupine by ";
        super.projectileKillMessage[0] = " turned ";
        super.projectileKillMessage[1] = " into a porcupine";
    }

    /**
     * Set the arrow-damage of a ranger's arrows
     * @param e The event called when a player is hit by an arrow
     */
    @EventHandler (priority = EventPriority.LOW)
    public void onArrowHit(ProjectileHitEvent e) {
        if (e.getEntity() instanceof Arrow &&
                e.getEntity().getShooter() instanceof Player &&
                Objects.equals(Kit.equippedKits.get(((Player) e.getEntity().getShooter()).getUniqueId()).name, name)) {
            ((Arrow) e.getEntity()).setDamage(18);
        }
    }

    /**
     * Activate the volley or burst ability
     * @param e The event called when a player shoots a bow
     */
    @EventHandler
    public void onShootBow(EntityShootBowEvent e) {
        if (e.isCancelled()) {
            return;
        }

        if (e.getEntity() instanceof Player &&
                Objects.equals(Kit.equippedKits.get(e.getEntity().getUniqueId()).name, name)) {
            Player p = (Player) e.getEntity();
            String b = Objects.requireNonNull(Objects.requireNonNull(e.getBow()).getItemMeta()).getDisplayName();

            if (Objects.equals(b, ChatColor.GREEN + "Volley Bow")) {
                Vector v = e.getProjectile().getVelocity();
                volleyAbility(p, v);
            } else if (Objects.equals(b, ChatColor.GREEN + "Burst Bow")) {
                burstAbility(p, e.getForce());
            }
        }
    }

    /**
     * Activate the volley ability, shooting 5 arrows at once
     * @param p The ranger shooting their volley bow
     * @param v The vector of the original arrow
     */
    private void volleyAbility(Player p, Vector v) {
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                ChatColor.GREEN + "You shot your volley bow!"));
        p.setCooldown(Material.BOW, 60);

        // Shoot the extra arrows
        if (removeArrow(p)) {
            p.launchProjectile(Arrow.class, v.rotateAroundY(0.157));
        }
        if (removeArrow(p)) {
            p.launchProjectile(Arrow.class, v.rotateAroundY(0.157));
        }
        if (removeArrow(p)) {
            p.launchProjectile(Arrow.class, v.rotateAroundY(-0.471));
        }
        if (removeArrow(p)) {
            p.launchProjectile(Arrow.class, v.rotateAroundY(-0.157));
        }
    }

    /**
     * Activate the burst ability, shooting 3 arrows consecutively
     * @param p The ranger shooting their burst bow
     * @param force The force of the original arrow
     */
    private void burstAbility(Player p, float force) {
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                ChatColor.GREEN + "You shot your burst bow!"));
        p.setCooldown(Material.BOW, 100);
        burstArrow(p, force, 11);
        burstArrow(p, force, 21);
        burstArrow(p, force, 31);
    }

    /**
     * Shoot a single arrow from the burst ability
     * @param p The ranger shooting their burst bow
     * @param force The force of the original arrow
     * @param d The delay with which to shoot the arrow
     */
    private void burstArrow(Player p, float force, int d) {
        new BukkitRunnable() {
            @Override
            public void run() {
                // Shoot iff the player has an arrow
                if (removeArrow(p)) {
                    Arrow a = p.launchProjectile(Arrow.class);
                    a.setVelocity(a.getVelocity().multiply(force));
                }
            }
        }.runTaskLater(Main.plugin, d);
    }

    /**
     * Remove an arrow from the player's inventory
     * @param p The player from whom to remove an arrow
     * @return true if the player has an arrow to remove, false otherwise
     */
    private boolean removeArrow(Player p) {
        PlayerInventory inv = p.getInventory();

        // Try offhand first
        if (inv.getItemInOffHand().getType() == Material.ARROW) {
            ItemStack o = inv.getItemInOffHand();
            o.setAmount(o.getAmount() - 1);
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
     * @param ed The event called when a player attacks another player
     */
    @EventHandler (ignoreCancelled = true)
    public void backStabDamage(EntityDamageByEntityEvent ed) {
        if (ed.getDamager() instanceof Player && ed.getEntity() instanceof Player) {
            Player p = (Player) ed.getDamager();
            Player hit = (Player) ed.getEntity();

            if (Objects.equals(Kit.equippedKits.get(p.getUniqueId()).name, name)) {
                Location hitLoc = hit.getLocation();
                Location damagerLoc = p.getLocation();

                // Basically what happens here is you check whether the player
                // is not looking at you at all (so having their back aimed at you.)
                if (damagerLoc.getYaw() <= hitLoc.getYaw() + 45 && damagerLoc.getYaw() >= hitLoc.getYaw() - 45
                        && canBackstab) {

                    ed.setCancelled(true);
                    hit.sendMessage(ChatColor.RED + "You got backstabbed.");
                    AssistKill.addDamager(hit.getUniqueId(), p.getUniqueId(), hit.getHealth());
                    DeathEvent.setKiller(hit, p);
                    hit.setHealth(0);

                }
            }
        }
    }

    /**
     * The event called when a player starts and stops sneaking
     * @param e exp bar shows as full indicating the ranger is ready to backstab.
     */
    @EventHandler
    public void backStabDetection(PlayerToggleSneakEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(uuid)) {
            return;
        }

        if (Objects.equals(Kit.equippedKits.get(uuid).name, name)) {
            if (br != null) {
                br.cancel();
                br = null;
            }

            // p.isSneaking() gives the sneaking status before the SneakEvent is processed
            if (p.isSneaking()) {
                canBackstab = false;
                BarCooldown.remove(uuid);
            } else {
                br = new BukkitRunnable() {
                    @Override
                    public void run() {
                        canBackstab = true;
                        br = null;
                    }
                };
                br.runTaskLater(Main.plugin, 40);
                BarCooldown.add(uuid, 40);
            }
        }
    }
}
