package me.huntifi.castlesiege.kits.kits;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * The Vanguard kit
 */
public class Vanguard extends Kit implements Listener, CommandExecutor {

    private ArrayList<Player> vanguards = new ArrayList<>();

    /**
     * Set the equipment and attributes of this kit
     */
    public Vanguard() {
        super("Vanguard", 115);

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Weapon
        es.hotbar[0] = ItemCreator.item(new ItemStack(Material.DIAMOND_SWORD),
                ChatColor.GREEN + "Reinforced Iron Sword",
                Collections.singletonList(ChatColor.AQUA + "Right-click to activate charge ability."),
                Collections.singletonList(new Tuple<>(Enchantment.DAMAGE_ALL, 20)));
        // Voted Weapon
        es.votedWeapon = new Tuple<>(
                ItemCreator.item(new ItemStack(Material.DIAMOND_SWORD),
                        ChatColor.GREEN + "Reinforced Iron Sword",
                        Arrays.asList(ChatColor.AQUA + "Right-click to activate charge ability.",
                                ChatColor.AQUA + "- voted: +2 damage"),
                        Collections.singletonList(new Tuple<>(Enchantment.DAMAGE_ALL, 22))),
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
                Collections.singletonList(ChatColor.AQUA + "- voted: Depth Strider +2"),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)),
                Color.fromRGB(99, 179, 101));

        // Ladders
        es.hotbar[1] = new ItemStack(Material.LADDER, 4);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, 6), 1);

        super.equipment = es;

        //passive effects
        super.potionEffects.add(new PotionEffect(PotionEffectType.JUMP, 999999, 0));

        // Death Messages
        super.projectileDeathMessage[0] = "You were killed by ";
        super.projectileKillMessage[0] = "You killed ";
    }


    /**
     *
     * @param e event triggered by right clicking diamond sword.
     */
    @EventHandler
    public void Charge(PlayerInteractEvent e) {
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
                        p.setCooldown(Material.DIAMOND_SWORD, 160);
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                                ChatColor.AQUA + "You are charging forward"));
                        p.addPotionEffect((new PotionEffect(PotionEffectType.SPEED, 70, 3)));
                        p.addPotionEffect((new PotionEffect(PotionEffectType.JUMP, 70, 1)));
                        p.addPotionEffect((new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 70, 3)));
                        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST , 1, 1 );

                        if (!vanguards.contains(p)) {
                            vanguards.add(p);
                        }

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (vanguards.contains(p)) {
                                    vanguards.remove(p);
                                }
                            }
                        }.runTaskLater(Main.plugin, 160);

                    } else {
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                                ChatColor.DARK_RED + "" + ChatColor.BOLD + "You can't charge forward yet."));
                    }
                }
            }
        }
    }

    /**
     * @param e to prevent being in the arraylist double.
     */
    @EventHandler
    public void onUserQuit(PlayerQuitEvent e){
        Player p = e.getPlayer();
        if (vanguards.contains(p)) {
            vanguards.remove(p);
        }

    }

    /**
     * @param e to prevent being in the arraylist double.
     */
    @EventHandler
    public void onUserLeave(PlayerDeathEvent e){
        Player p = (Player) e.getEntity();
        if (vanguards.contains(p)) {
            vanguards.remove(p);
        }

    }

    /**
     *
     * @param ed remove the potion effects on hit.
     */

    @EventHandler
    public void ChargeHit(EntityDamageByEntityEvent ed) {

        Player p = (Player) ed.getDamager();
        UUID uuid = p.getUniqueId();

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(uuid)) {
            return;
        }

        Location loc = p.getLocation();

        if (vanguards.contains(p)) {

            for (PotionEffect effect : p.getActivePotionEffects())
                p.removePotionEffect(effect.getType());
            p.addPotionEffect((new PotionEffect(PotionEffectType.JUMP, 9999999, 0)));
            p.getWorld().playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_BLAST, 1, 1);
            vanguards.remove(p);
        }
    }

    /**
     * Register the player as using this kit and set their items
     * @param commandSender Source of the command
     * @param command Command which was executed
     * @param s Alias of the command which was used
     * @param strings Passed command arguments
     * @return true
     */
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof ConsoleCommandSender) {
            commandSender.sendMessage("Console cannot select kits!");
            return true;
        }

        super.addPlayer(((Player) commandSender).getUniqueId());
        return true;
    }
}
