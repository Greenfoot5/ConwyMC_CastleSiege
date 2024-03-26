package me.huntifi.castlesiege.kits.kits.coin_kits;

import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.events.gameplay.HorseHandler;
import me.huntifi.castlesiege.kits.items.CSItemCreator;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.kits.CoinKit;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.maps.TeamController;
import me.huntifi.castlesiege.misc.CSNameTag;
import me.huntifi.conwymc.data_types.Tuple;
import me.huntifi.conwymc.util.Messenger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

/**
 * The cavalry kit
 */
public class Cavalry extends CoinKit implements Listener {

    private static final int health = 300;
    private static final double regen = 10.5;
    private static final double meleeDamage = 43;
    private static final int ladderCount = 4;
    private static final int horseHealth = 400;

    /**
     * Set the equipment and attributes of this kit
     */
    public Cavalry() {
        super("Cavalry", health, regen, Material.IRON_HORSE_ARMOR);

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();

        // Weapon
        es.hotbar[0] = CSItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                Component.text("Sabre", NamedTextColor.GREEN), null, null, meleeDamage);
        // Voted Weapon
        es.votedWeapon = new Tuple<>(
                CSItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                        Component.text("Sabre", NamedTextColor.GREEN),
                        Collections.singletonList(Component.text("- voted: +2 damage", NamedTextColor.AQUA)),
                        Collections.singletonList(new Tuple<>(Enchantment.SWEEPING_EDGE, 0)), meleeDamage + 2),
                0);

        // Chestplate
        es.chest = CSItemCreator.item(new ItemStack(Material.CHAINMAIL_CHESTPLATE),
                Component.text("Chainmail Chestplate", NamedTextColor.GREEN), null, null);

        // Leggings
        es.legs = CSItemCreator.item(new ItemStack(Material.CHAINMAIL_LEGGINGS),
                Component.text("Chainmail Leggings", NamedTextColor.GREEN), null, null);

        // Boots
        es.feet = CSItemCreator.item(new ItemStack(Material.IRON_BOOTS),
                Component.text("Iron Boots", NamedTextColor.GREEN), null, null);
        // Voted Boots
        es.votedFeet = CSItemCreator.item(new ItemStack(Material.IRON_BOOTS),
                Component.text("Iron Boots", NamedTextColor.GREEN),
                Collections.singletonList(Component.text("- voted: Depth Strider II", NamedTextColor.AQUA)),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)));

        // Ladders
        es.hotbar[1] = new ItemStack(Material.LADDER, ladderCount);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, ladderCount + 2), 1);

        // Horse
        es.hotbar[2] = CSItemCreator.item(new ItemStack(Material.WHEAT),
                Component.text("Spawn Horse", NamedTextColor.GREEN), null, null);
        HorseHandler.add(name, 600, horseHealth, 2, 0.2425, 0.8,
                Material.IRON_HORSE_ARMOR, Arrays.asList(
                        new PotionEffect(PotionEffectType.JUMP, 999999, 1),
                        new PotionEffect(PotionEffectType.REGENERATION, 999999, 0),
                        new PotionEffect(PotionEffectType.SPEED, 999999, 1)
                )
        );

        // stomp
        es.hotbar[3] = CSItemCreator.weapon(new ItemStack(Material.ANVIL),
                Component.text("Horse Kick", NamedTextColor.GREEN), null, null, 0);

        super.equipment = es;
    }


    /**
     * Activate the Cavalry stomp ability
     * @param e The event called when hitting another player
     */
    @EventHandler
    public void onStomp(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        ItemStack stomp = p.getInventory().getItemInMainHand();

        if (Objects.equals(Kit.equippedKits.get(uuid).name, name)) {

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(uuid)) {
            return;
        }
            if (stomp.getType().equals(Material.ANVIL)) {

                //prevent from using it when not on a horse
                if (p.getVehicle() == null) {
                    Messenger.sendActionError("You can't use this when not on your horse", p);
                    return;
                }

                if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {

                    if (p.getCooldown(Material.ANVIL) != 0) {
                        Messenger.sendActionError("Your horse's ability to stomp is still recharging!", p);
                        return;
                    }

                    boolean hasEnemyInRange = false;
                    for (Player hit : Bukkit.getOnlinePlayers()) {

                        //if the player is not in the same world ignore them.
                        if (p.getWorld() != hit.getWorld())
                            continue;

                        //the player executing the ability should have enemy players in range.
                        if (p.getLocation().distance(hit.getLocation()) <= 2.3 &&
                                TeamController.getTeam(hit.getUniqueId())
                                != TeamController.getTeam(p.getUniqueId())) {

                            hasEnemyInRange = true;

                            // The stomp can be blocked using a shield
                            if (hit.isBlocking()) {
                                Messenger.sendActionSuccess("You blocked " + CSNameTag.mmUsername(p) + "'s horse stomp", hit);
                            } else {
                                hit.addPotionEffect((new PotionEffect(PotionEffectType.CONFUSION, 80, 4)));
                                hit.addPotionEffect((new PotionEffect(PotionEffectType.SLOW, 80, 1)));
                                hit.addPotionEffect((new PotionEffect(PotionEffectType.SLOW_DIGGING, 80, 3)));
                                hit.addPotionEffect((new PotionEffect(PotionEffectType.BLINDNESS, 20, 0)));
                                hit.damage(100, p);
                            }
                        }

                        if (hasEnemyInRange) {
                            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_HORSE_ANGRY, 1, (float) 0.8);
                            p.setCooldown(Material.ANVIL, 200);
                        } else {
                            Messenger.sendActionError("No enemy players close enough!", p);
                        }
                    }
                }
            }
        }
    }

    /**
     * @return The lore to add to the kit gui item
     */
    @Override
    public ArrayList<Component> getGuiDescription() {
        ArrayList<Component> kitLore = new ArrayList<>();
        kitLore.add(Component.text("Can summon a horse to ride on", NamedTextColor.GRAY));
        kitLore.addAll(getBaseStats(health, regen, meleeDamage, ladderCount));
        kitLore.add(Component.text(horseHealth, color).append(Component.text(" Horse HP", NamedTextColor.GRAY)));
        kitLore.add(Component.empty());
        kitLore.add(Component.text("Horse Active:", NamedTextColor.GOLD));
        kitLore.add(Component.text("- When riding, can perform a kick", NamedTextColor.GRAY));
        kitLore.add(Component.text("dealing AOE damage and slowing enemies", NamedTextColor.GRAY));
        return kitLore;
    }
}
