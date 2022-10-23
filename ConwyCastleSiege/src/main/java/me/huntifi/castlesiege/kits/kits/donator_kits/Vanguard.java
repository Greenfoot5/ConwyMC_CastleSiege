package me.huntifi.castlesiege.kits.kits.donator_kits;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.kits.kits.DonatorKit;
import me.huntifi.castlesiege.kits.kits.Kit;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.command.CommandExecutor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

/**
 * The Vanguard kit
 */
public class Vanguard extends DonatorKit implements Listener, CommandExecutor {

    private static final ArrayList<UUID> vanguards = new ArrayList<>();

    /**
     * Set the equipment and attributes of this kit
     */
    public Vanguard() {
        super("Vanguard", 260, 11, 8500, 10);

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Weapon
        es.hotbar[0] = ItemCreator.weapon(new ItemStack(Material.DIAMOND_SWORD),
                ChatColor.GREEN + "Reinforced Iron Sword",
                Collections.singletonList(ChatColor.AQUA + "Right-click to activate charge ability."),
                null, 47);
        // Voted Weapon
        es.votedWeapon = new Tuple<>(
                ItemCreator.weapon(new ItemStack(Material.DIAMOND_SWORD),
                        ChatColor.GREEN + "Reinforced Iron Sword",
                        Arrays.asList(ChatColor.AQUA + "Right-click to activate charge ability.",
                                ChatColor.AQUA + "- voted: +2 damage"),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), 49),
                0);

        // Chestplate
        es.chest = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE),
                ChatColor.GREEN + "Leather Tunic", null, null,
                Color.fromRGB(99, 179, 101));

        // Leggings
        es.legs = ItemCreator.item(new ItemStack(Material.IRON_LEGGINGS),
                ChatColor.GREEN + "Iron Leggings", null, null);

        // Boots
        es.feet = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_BOOTS),
                ChatColor.GREEN + "Leather Boots", null, null,
                Color.fromRGB(99, 179, 101));
        // Voted boots
        es.votedFeet = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_BOOTS),
                ChatColor.GREEN + "Leather Boots",
                Collections.singletonList(ChatColor.AQUA + "- voted: Depth Strider II"),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)),
                Color.fromRGB(99, 179, 101));

        // Ladders
        es.hotbar[1] = new ItemStack(Material.LADDER, 4);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, 6), 1);

        super.equipment = es;

        //passive effects
        super.potionEffects.add(new PotionEffect(PotionEffectType.JUMP, 999999, 0));
        super.potionEffects.add(new PotionEffect(PotionEffectType.FAST_DIGGING, 999999, 0));
    }


    /**
     *
     * @param e event triggered by right clicking diamond sword.
     */
    @EventHandler
    public void charge(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        int cooldown = p.getCooldown(Material.DIAMOND_SWORD);

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(uuid)) {
            return;
        }

        if (Objects.equals(Kit.equippedKits.get(uuid).name, name)) {
            if (p.getInventory().getItemInMainHand().getType().equals(Material.DIAMOND_SWORD)) {
                if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {

                    if (cooldown == 0) {
                        p.setCooldown(Material.DIAMOND_SWORD, 220);
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                                ChatColor.AQUA + "You are charging forward"));
                        p.addPotionEffect((new PotionEffect(PotionEffectType.SPEED, 70, 3)));
                        p.addPotionEffect((new PotionEffect(PotionEffectType.JUMP, 70, 1)));

                        //Diminishing strength effect
                        p.addPotionEffect((new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 70, 5)));

                        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST , 1, 1 );
                        vanguards.add(uuid);
                        Bukkit.getScheduler().runTaskLaterAsynchronously(Main.plugin, () -> vanguards.remove(uuid), 100);
                    } else {
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                                ChatColor.DARK_RED + "" + ChatColor.BOLD + "You can't charge forward yet."));
                    }
                }
            }
        }
    }

    /**
     * @param ed remove the potion effects on hit.
     */
    @EventHandler (ignoreCancelled = true)
    public void chargeHit(EntityDamageByEntityEvent ed) {
        if (ed.getDamager() instanceof Player) {
            Player player = (Player) ed.getDamager();
            UUID uuid = player.getUniqueId();

            // Prevent using in lobby
            if (InCombat.isPlayerInLobby(uuid))
                return;

            if (vanguards.contains(uuid)) {
                for (PotionEffect effect : player.getActivePotionEffects()) {
                    if ((effect.getType().getName().equals(PotionEffectType.SPEED.getName()) && effect.getAmplifier() == 3)
                            || (effect.getType().getName().equals(PotionEffectType.JUMP.getName()) && effect.getAmplifier() == 1)
                            || (effect.getType().getName().equals(PotionEffectType.INCREASE_DAMAGE.getName()) && effect.getAmplifier() == 3)) {
                        player.removePotionEffect(effect.getType());
                    }
                }
                player.addPotionEffect((new PotionEffect(PotionEffectType.JUMP, 9999999, 0)));
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 1, 1);
                vanguards.remove(uuid);
            }
        }
    }
}
