package me.huntifi.castlesiege.kits.kits.team_kits.hommet;

import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.kits.kits.TeamKit;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

public class HommetAxeman extends TeamKit implements Listener {

    public HommetAxeman() {
        super("Axeman", 300, 10, "Hommet",
                "Saxons", 2500, Material.STONE_AXE, "hommetaxeman");
        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Weapon
        es.hotbar[0] = ItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                ChatColor.GREEN + "Iron Sword", null, null, 40);
        // Voted weapon
        es.votedWeapon = new Tuple<>(
                ItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                        ChatColor.GREEN + "Iron Sword",
                        Collections.singletonList(ChatColor.AQUA + "- voted: +2 damage"),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), 42),
                0);
        // Weapon
        es.hotbar[1] = ItemCreator.weapon(new ItemStack(Material.STONE_AXE, 2),
                ChatColor.GREEN + "Throwable Axe", null, null, 40);
        // Voted weapon
        es.votedWeapon = new Tuple<>(
                ItemCreator.weapon(new ItemStack(Material.STONE_AXE, 3),
                        ChatColor.GREEN + "Throwable Axe",
                        Collections.singletonList(ChatColor.AQUA + "- voted: +2 damage"),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), 42),
                1);

        // Chestplate
        es.chest = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE),
                ChatColor.GREEN + "Leather Chestplate", null, null,
                Color.fromRGB(51, 198, 46));

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
        es.hotbar[2] = new ItemStack(Material.LADDER, 4);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, 6), 2);

        super.potionEffects.add(new PotionEffect(PotionEffectType.FAST_DIGGING, 999999, 0));

        // Death Messages
        super.deathMessage[0] = "You were axed to death by ";
        super.killMessage[0] = " axed ";
        super.killMessage[1] = " to death";

        super.equipment = es;
    }
    /**
     * Activate the axeman ability of throwing an Axe
     * @param e The event called when right-clicking with a stick
     */
    @EventHandler
    public void throwAxe(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(uuid)) {
            return;
        }

        ItemStack axe = p.getInventory().getItemInMainHand();
        int cooldown = p.getCooldown(Material.STONE_AXE);
        if (Objects.equals(Kit.equippedKits.get(uuid).name, name)) {
            if (axe.getType().equals(Material.STONE_AXE)) {
                if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (cooldown == 0) {
                        axe.setAmount(axe.getAmount() - 1);
                        p.setCooldown(Material.STONE_AXE, 60);
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                                ChatColor.AQUA + "You threw your Axe!"));
                        p.launchProjectile(Snowball.class).setVelocity(p.getLocation().getDirection().multiply(2.5));

                    } else {
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                                ChatColor.DARK_RED + "" + ChatColor.BOLD + "You can't throw your Axe yet."));
                    }
                }
            }
        }
    }


    /**
     * Set the thrown axe's damage
     * @param e The event called when an arrow hits a player
     */
    @EventHandler (priority = EventPriority.LOW, ignoreCancelled = true)
    public void changeAxeDamage(ProjectileHitEvent e) {
        if (e.getEntity() instanceof Snowball) {
            Snowball ball = (Snowball) e.getEntity();

            if (ball.getShooter() instanceof Player){
                Player damager = (Player) ball.getShooter();

                if (Objects.equals(Kit.equippedKits.get(damager.getUniqueId()).name, name)) {
                    if (e.getHitEntity() instanceof Player) {
                        Player hit = (Player) e.getHitEntity();
                        hit.damage(105, damager);
                    } else if (e.getHitEntity() instanceof Horse) {
                        Horse horse = (Horse) e.getHitEntity();
                        horse.damage(125, damager);
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
