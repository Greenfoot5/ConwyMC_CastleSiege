package me.huntifi.castlesiege.kits.kits;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.kits.EquipmentSet;
import me.huntifi.castlesiege.kits.Kit;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Objects;

public class Ranger extends Kit implements Listener, CommandExecutor {

    public Ranger() {
        super("Ranger");
        super.baseHealth = 105;


        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Weapon
        es.hotbar[0] = createItem(new ItemStack(Material.WOODEN_SWORD),
                ChatColor.GREEN + "Dagger", null,
                Collections.singletonList(new Tuple<>(Enchantment.DAMAGE_ALL, 18)));
        // Voted weapon
        es.votedWeapon = new Tuple<>(
                createItem(new ItemStack(Material.WOODEN_SWORD),
                        ChatColor.GREEN + "Dagger",
                        Collections.singletonList(ChatColor.AQUA + "- voted: +2 damage"),
                        Collections.singletonList(new Tuple<>(Enchantment.DAMAGE_ALL, 20))),
                0);

        // Chestplate
        es.chest = createLeatherItem(new ItemStack(Material.LEATHER_CHESTPLATE),
                ChatColor.GREEN + "Leather Chestplate", null, null,
                Color.fromRGB(28, 165, 33));

        // Leggings
        es.legs = createLeatherItem(new ItemStack(Material.LEATHER_LEGGINGS),
                ChatColor.GREEN + "Leather Leggings", null, null,
                Color.fromRGB(32, 183, 37));

        // Boots
        es.feet = createLeatherItem(new ItemStack(Material.LEATHER_BOOTS),
                ChatColor.GREEN + "Leather Boots", null, null,
                Color.fromRGB(28, 165, 33));
        // Voted Boots
        es.votedFeet = createLeatherItem(new ItemStack(Material.LEATHER_BOOTS),
                ChatColor.GREEN + "Leather Boots",
                Collections.singletonList(ChatColor.AQUA + "- voted: Depth Strider +2"),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)),
                Color.fromRGB(28, 165, 33));

        // Regular Bow
        es.hotbar[1] = createItem(new ItemStack(Material.BOW),
                ChatColor.GREEN + "Bow", null, null);

        // Volley Bow
        es.hotbar[2] = createItem(new ItemStack(Material.BOW),
                ChatColor.GREEN + "Volley Bow",
                Collections.singletonList(ChatColor.AQUA + "Shoot 3 arrows at once"), null);

        // Burst Bow
        es.hotbar[3] = createItem(new ItemStack(Material.BOW),
                ChatColor.GREEN + "Burst Bow",
                Collections.singletonList(ChatColor.AQUA + "Shoot 3 consecutive arrows"), null);

        // Ladders
        es.hotbar[4] = new ItemStack(Material.LADDER, 4);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, 6), 4);

        // Arrows
        es.hotbar[7] = new ItemStack(Material.ARROW, 48);

        super.equipment = es;

        // Perm Potion Effect
        super.potionEffects.add(new PotionEffect(PotionEffectType.SPEED, 999999, 0));

        // Death Messages
        super.projectileDeathMessage[0] = "You were turned into a porcupine by ";
        super.projectileKillMessage[0] = "You turned ";
        super.projectileKillMessage[1] = " into a porcupine";
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        super.addPlayer(((Player) commandSender).getUniqueId());
        return true;
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onArrowHit(ProjectileHitEvent e) {
        if (e.getEntity() instanceof Arrow &&
                e.getEntity().getShooter() instanceof Player &&
                Objects.equals(Kit.equippedKits.get(((Player) e.getEntity().getShooter()).getUniqueId()).name, name)) {
            ((Arrow) e.getEntity()).setDamage(13);
        }
    }

    @EventHandler
    public void onShootBow(EntityShootBowEvent e) {
        if (e.getEntity() instanceof Player &&
                Objects.equals(Kit.equippedKits.get(e.getEntity().getUniqueId()).name, name)) {
            Player p = (Player) e.getEntity();
            String b = e.getBow().getItemMeta().getDisplayName();
            if (Objects.equals(b, ChatColor.GREEN + "Volley Bow")) {
                Vector v = e.getProjectile().getVelocity();
                volleyAbility(p, v);
            } else if (Objects.equals(b, ChatColor.GREEN + "Burst Bow")) {
                e.setCancelled(true);
                burstAbility(p);
            }
        }
    }

    private void volleyAbility(Player p, Vector v) {
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                ChatColor.GREEN + "You shot your volley bow!"));
        p.setCooldown(Material.BOW, 100);

        // Shoot the extra arrows
        if (removeArrow(p)) {
            p.launchProjectile(Arrow.class, v.rotateAroundY(0.157));
        }
        if (removeArrow(p)) {
            p.launchProjectile(Arrow.class, v.rotateAroundY(-0.314));
        }
    }

    private void burstAbility(Player p) {
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                ChatColor.GREEN + "You shot your burst bow!"));
        p.setCooldown(Material.BOW, 100);
        burstArrow(p, 0);
        burstArrow(p, 10);
        burstArrow(p, 20);
    }

    private void burstArrow(Player p, int d) {
        new BukkitRunnable() {
            @Override
            public void run() {
                // Shoot iff the player has an arrow
                if (removeArrow(p)) {
                    p.launchProjectile(Arrow.class);
                }
            }
        }.runTaskLater(Main.plugin, d);
    }

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
}
