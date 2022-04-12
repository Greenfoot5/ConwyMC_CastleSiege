package me.huntifi.castlesiege.kits.kits;

import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.kits.EquipmentSet;
import me.huntifi.castlesiege.kits.Kit;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.tags.NametagsEvent;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

public class Maceman extends Kit implements Listener, CommandExecutor {

    public Maceman() {
        super("Maceman");
        super.baseHealth = 110;


        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Weapon
        es.hotbar[0] = createItem(new ItemStack(Material.IRON_SHOVEL),
                ChatColor.GREEN + "Mace", null,
                Collections.singletonList(new Tuple<>(Enchantment.DAMAGE_ALL, 26)));
        // Voted Weapon
        es.votedWeapon = new Tuple<>(
                createItem(new ItemStack(Material.IRON_SHOVEL),
                        ChatColor.GREEN + "Mace",
                        Arrays.asList("", ChatColor.AQUA + "- voted: +2 damage"),
                        Collections.singletonList(new Tuple<>(Enchantment.DAMAGE_ALL, 28))),
                0);

        // Chestplate
        es.chest = createItem(new ItemStack(Material.CHAINMAIL_CHESTPLATE),
                ChatColor.GREEN + "Chainmail Chestplate", null, null);

        // Leggings
        es.legs = createItem(new ItemStack(Material.LEATHER_LEGGINGS),
                ChatColor.GREEN + "Leather Leggings", null, null);

        // Boots
        es.feet = createItem(new ItemStack(Material.IRON_BOOTS),
                ChatColor.GREEN + "Iron Boots", null, null);
        // Voted Boots
        es.votedFeet = createItem(new ItemStack(Material.IRON_BOOTS),
                ChatColor.GREEN + "Iron Boots",
                Arrays.asList("", ChatColor.AQUA + "- voted: Depth Strider 2"),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)));

        // Ladders
        es.hotbar[1] = new ItemStack(Material.LADDER, 4);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, 6), 1);

        super.equipment = es;


        // Death Messages
        super.deathMessage[0] = "Your skull was cracked by ";
        super.deathMessage[1] = "'s mace";
        super.killMessage[0] = "You cracked ";
        super.killMessage[1] = "'s skull";
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        super.addPlayer(((Player) commandSender).getUniqueId());
        return true;
    }

    @EventHandler
    public void onStun(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            Player p = (Player) e.getEntity();
            Player q = (Player) e.getDamager();

            // Maceman tries to use stun an enemy
            if (Objects.equals(Kit.equippedKits.get(q.getUniqueId()).name, name) &&
                    MapController.getCurrentMap().getTeam(q.getUniqueId())
                            != MapController.getCurrentMap().getTeam(p.getUniqueId()) &&
                    q.getInventory().getItemInMainHand().getType() == Material.IRON_SHOVEL &&
                    q.getCooldown(Material.IRON_SHOVEL) == 0) {
                q.setCooldown(Material.IRON_SHOVEL, 200);

                // Enemy blocks stun
                if (p.isBlocking()) {
                    q.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                            NametagsEvent.color(p) + p.getName() + ChatColor.AQUA + " blocked your stun"));
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                            ChatColor.AQUA + "You blocked " + NametagsEvent.color(q) + q.getName() + ChatColor.AQUA + "'s stun"));
                } else {
                    q.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                            ChatColor.AQUA + "You have stunned " + NametagsEvent.color(p) + p.getName()));
                    p.sendMessage(ChatColor.DARK_RED + "You have been stunned by " + NametagsEvent.color(q) + q.getName() + ChatColor.DARK_RED + "!");
                    p.getWorld().playSound(p.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST_FAR , 1, 1 );
                    p.addPotionEffect((new PotionEffect(PotionEffectType.BLINDNESS, 30, 1)));
                    p.addPotionEffect((new PotionEffect(PotionEffectType.SLOW, 50, 1)));
                    p.addPotionEffect((new PotionEffect(PotionEffectType.SLOW_DIGGING, 50, 1)));
                    e.setDamage(e.getDamage() * 1.5);
                }
            }
        }
    }
}
