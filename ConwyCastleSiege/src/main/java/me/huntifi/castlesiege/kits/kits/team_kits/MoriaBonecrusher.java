package me.huntifi.castlesiege.kits.kits.team_kits;

import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.kits.kits.TeamKit;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.NameTag;
import me.huntifi.castlesiege.maps.TeamController;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collections;
import java.util.Objects;
import java.util.Random;

public class MoriaBonecrusher extends TeamKit implements Listener {

    public MoriaBonecrusher() {
        super("Moria Bonecrusher", 240, 8, "Moria","The Orcs", 4000, 10);


        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Weapon
        es.hotbar[0] = ItemCreator.weapon(new ItemStack(Material.BONE),
                ChatColor.GREEN + "Crushing Bone", null, null, 34);
        // Voted weapon
        es.votedWeapon = new Tuple<>(
                ItemCreator.weapon(new ItemStack(Material.BONE),
                        ChatColor.GREEN + "Crushing Bone",
                        Collections.singletonList(ChatColor.AQUA + "- voted: +2 damage"),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), 36),
                0);

        // Chestplate
        es.chest = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE),
                ChatColor.GREEN + "Leather Chestplate", null, null,
                Color.fromRGB(21, 51, 10));

        // Leggings
        es.legs = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_LEGGINGS),
                ChatColor.GREEN + "Leather Leggings", null, null,
                Color.fromRGB(68, 65, 12));

        // Boots
        es.feet = ItemCreator.item(new ItemStack(Material.IRON_BOOTS),
                ChatColor.GREEN + "Iron Boots", null, Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 1)));
        // Voted Boots
        es.votedFeet = ItemCreator.item(new ItemStack(Material.IRON_BOOTS),
                ChatColor.GREEN + "Iron Boots",
                Collections.singletonList(ChatColor.AQUA + "- voted: Depth Strider III"),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 3)));

        // Ladders
        es.hotbar[1] = new ItemStack(Material.LADDER, 4);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, 6), 1);

        super.potionEffects.add(new PotionEffect(PotionEffectType.SPEED, 999999, 1));
        super.potionEffects.add(new PotionEffect(PotionEffectType.FAST_DIGGING, 999999, 0));

        // Death Messages
        super.deathMessage[0] = "You were crushed to death by ";
        super.killMessage[0] = " crushed ";
        super.killMessage[1] = " to death";

        super.equipment = es;
    }

    /**
     * Activate the Bonecrusher ability
     * @param e The event called when hitting another player
     */
    @EventHandler (ignoreCancelled = true)
    public void onCrush(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            Player p = (Player) e.getEntity();
            Player q = (Player) e.getDamager();

            // Bonecrusher tries to use stun an enemy
            if (Objects.equals(Kit.equippedKits.get(q.getUniqueId()).name, name) &&
                    q.getInventory().getItemInMainHand().getType() == Material.BONE &&
                    q.getCooldown(Material.BONE) == 0) {
                q.setCooldown(Material.BONE, 160);

                // Enemy blocks stun
                if (p.isBlocking()) {
                    q.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                            NameTag.color(p) + p.getName() + ChatColor.AQUA + " blocked your crushing stun"));
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                            ChatColor.AQUA + "You blocked " + NameTag.color(q) + q.getName() + ChatColor.AQUA + "'s crushing stun"));
                } else if (p.isSneaking() && new Random().nextInt(4) == 0) {
                    q.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                            NameTag.color(p) + p.getName() + ChatColor.AQUA + " dodged your crushing stun"));
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                            ChatColor.AQUA + "You dodged " + NameTag.color(q) + q.getName() + ChatColor.AQUA + "'s crushing stun"));
                } else {
                    q.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                            ChatColor.AQUA + "You have crushed & stunned " + NameTag.color(p) + p.getName()));
                    p.sendMessage(ChatColor.DARK_RED + "You have been crush-stunned by " + NameTag.color(q) + q.getName() + ChatColor.DARK_RED + "!");
                    p.getWorld().playSound(p.getLocation(), Sound.BLOCK_BONE_BLOCK_BREAK , 1, 1 );
                    p.addPotionEffect((new PotionEffect(PotionEffectType.WEAKNESS, 80, 4)));
                    p.addPotionEffect((new PotionEffect(PotionEffectType.SLOW_DIGGING, 80, 5)));
                }
            }
        }
    }
}
