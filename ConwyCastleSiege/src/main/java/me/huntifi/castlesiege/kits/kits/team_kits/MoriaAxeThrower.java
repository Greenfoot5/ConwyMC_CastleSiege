package me.huntifi.castlesiege.kits.kits.team_kits;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.events.death.DeathEvent;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.kits.kits.TeamKit;
import me.huntifi.castlesiege.maps.MapController;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

public class MoriaAxeThrower extends TeamKit implements Listener {

    public MoriaAxeThrower() {
        super("Dwarven Axe Thrower", 145, 10, "Moria", "The Dwarves", 5000);

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Weapon
        es.hotbar[0] = ItemCreator.weapon(new ItemStack(Material.STONE_AXE, 5),
                ChatColor.GREEN + "Throwable Axe", null, null, 30);
        // Voted weapon
        es.votedWeapon = new Tuple<>(
                ItemCreator.weapon(new ItemStack(Material.STONE_AXE, 6),
                        ChatColor.GREEN + "Throwable Axe",
                        Collections.singletonList(ChatColor.AQUA + "- voted: +2 damage"),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), 32),
                0);

        // Chestplate
        es.chest = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE),
                ChatColor.GREEN + "Leather Chestplate", null, null,
                Color.fromRGB(218, 51, 10));

        // Leggings
        es.legs = ItemCreator.item(new ItemStack(Material.NETHERITE_LEGGINGS),
                ChatColor.GREEN + "Reinforced Steel Leggings", null, null);

        // Boots
        es.feet = ItemCreator.item(new ItemStack(Material.IRON_BOOTS),
                ChatColor.GREEN + "Iron Boots", null, null);
        // Voted Boots
        es.votedFeet = ItemCreator.item(new ItemStack(Material.IRON_BOOTS),
                ChatColor.GREEN + "Iron Boots",
                Collections.singletonList(ChatColor.AQUA + "- voted: Depth Strider II"),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)));

        // Ladders
        es.hotbar[1] = new ItemStack(Material.LADDER, 4);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, 6), 1);

        super.potionEffects.add(new PotionEffect(PotionEffectType.FAST_DIGGING, 999999, 0));

        // Death Messages
        super.deathMessage[0] = "You were axed to death by ";
        super.killMessage[0] = " axed ";
        super.killMessage[1] = " to death";

        super.equipment = es;
    }

    /**
     * Activate the spearman ability of throwing a spear
     * @param e The event called when right-clicking with a stick
     */
    @EventHandler
    public void throwSpear(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        ItemStack stick = p.getInventory().getItemInMainHand();
        int cooldown = p.getCooldown(Material.STONE_AXE);

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(uuid)) {
            return;
        }

        if (Objects.equals(Kit.equippedKits.get(uuid).name, name)) {
            if (stick.getType().equals(Material.STONE_AXE)) {
                if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (cooldown == 0) {
                        stick.setAmount(stick.getAmount() - 1);
                        p.setCooldown(Material.STONE_AXE, 15);
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                                        ChatColor.AQUA + "You threw your Axe!"));
                        p.launchProjectile(Snowball.class).setVelocity(p.getLocation().getDirection().multiply(1.8));

                    } else {
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                                ChatColor.DARK_RED + "" + ChatColor.BOLD + "You can't throw your Axe yet."));
                    }
                }
            }
        }
    }


    /**
     * Set the thrown spear's damage
     * @param e The event called when an arrow hits a player
     */
    @EventHandler (priority = EventPriority.LOW)
    public void changeAxeDamage(ProjectileHitEvent e) {
        if (e.getEntity() instanceof Snowball) {
            Snowball ball = (Snowball) e.getEntity();

            if(ball.getShooter() instanceof Player){
                Player damages = (Player) ball.getShooter();

                if (Objects.equals(Kit.equippedKits.get(damages.getUniqueId()).name, name)) {
                    if (e.getHitEntity() instanceof Player) {
                        Player p = (Player) e.getHitEntity();
                        if (MapController.getCurrentMap().getTeam(ball.getUniqueId())
                                == MapController.getCurrentMap().getTeam(p.getUniqueId())) {
                            return;
                        }
                        if (p.getHealth() - 25 > 0) {
                            p.damage(45);
                        } else {
                            DeathEvent.setKiller(p, damages);
                            p.setHealth(0);
                        }
                    } else if (e.getHitEntity() instanceof Horse) {
                        Horse horse = (Horse) e.getHitEntity();
                        horse.damage(50);
                    }
                }
            }
        }
    }
}
