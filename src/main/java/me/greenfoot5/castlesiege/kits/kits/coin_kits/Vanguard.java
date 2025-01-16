package me.greenfoot5.castlesiege.kits.kits.coin_kits;

import me.greenfoot5.castlesiege.Main;
import me.greenfoot5.castlesiege.events.combat.InCombat;
import me.greenfoot5.castlesiege.kits.items.CSItemCreator;
import me.greenfoot5.castlesiege.kits.items.EquipmentSet;
import me.greenfoot5.castlesiege.kits.kits.CoinKit;
import me.greenfoot5.castlesiege.kits.kits.Kit;
import me.greenfoot5.conwymc.data_types.Tuple;
import me.greenfoot5.conwymc.util.Messenger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * The Vanguard kit
 */
public class Vanguard extends CoinKit implements Listener, CommandExecutor {

    private static final int health = 270;
    private static final double regen = 10.5;
    private static final double meleeDamage = 50;
    private static final int ladderAmount = 4;

    private static final ArrayList<UUID> vanguards = new ArrayList<>();

    /**
     * Set the equipment and attributes of this kit
     */
    public Vanguard() {
        super("Vanguard", health, regen, Material.DIAMOND_SWORD);

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();

        // Weapon
        es.hotbar[0] = CSItemCreator.weapon(new ItemStack(Material.DIAMOND_SWORD),
                Component.text("Reinforced Iron Sword", NamedTextColor.GREEN),
                List.of(Component.empty(),
                        Component.text("57 Melee Damage", NamedTextColor.DARK_GREEN)),
                null, meleeDamage);
        // Voted Weapon
        es.votedWeapon = new Tuple<>(
                CSItemCreator.weapon(new ItemStack(Material.DIAMOND_SWORD),
                        Component.text("Reinforced Iron Sword", NamedTextColor.GREEN),
                        List.of(Component.empty(),
                                Component.text("59 Melee Damage", NamedTextColor.DARK_GREEN),
								Component.text("⁎ Voted: +2 Melee Damage", NamedTextColor.DARK_AQUA)),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOTING, 0)), meleeDamage + 2),
                0);

        // Chestplate
        es.chest = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE),
                Component.text("Leather Tunic", NamedTextColor.GREEN),
                List.of(Component.empty(),
                        Component.text(health + " HP", NamedTextColor.DARK_GREEN),
                        Component.text(regen + " Regen", NamedTextColor.DARK_GREEN)), null,
                Color.fromRGB(99, 179, 101));

        // Leggings
        es.legs = CSItemCreator.item(new ItemStack(Material.IRON_LEGGINGS),
                Component.text("Iron Leggings", NamedTextColor.GREEN),
                List.of(Component.empty(),
                        Component.text(health + " HP", NamedTextColor.DARK_GREEN),
                        Component.text(regen + " Regen", NamedTextColor.DARK_GREEN)), null);

        // Boots
        es.feet = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_BOOTS),
                Component.text("Leather Boots", NamedTextColor.GREEN),
                List.of(Component.empty(),
                        Component.text(health + " HP", NamedTextColor.DARK_GREEN),
                        Component.text(regen + " Regen", NamedTextColor.DARK_GREEN)), null,
                Color.fromRGB(99, 179, 101));
        // Voted boots
        es.votedFeet = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_BOOTS),
                Component.text("Leather Boots", NamedTextColor.GREEN),
                List.of(Component.empty(),
                        Component.text(health + " HP", NamedTextColor.DARK_GREEN),
                        Component.text(regen + " Regen", NamedTextColor.DARK_GREEN),
                        Component.empty(),
                        Component.text("⁎ Voted: Depth Strider II", NamedTextColor.DARK_AQUA)),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)),
                Color.fromRGB(99, 179, 101));

        // Ladders
        es.hotbar[1] = new ItemStack(Material.LADDER, ladderAmount);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, ladderAmount + 2), 1);

        super.equipment = es;

        //passive effects
        super.potionEffects.add(new PotionEffect(PotionEffectType.SPEED, 999999, 0));
        super.potionEffects.add(new PotionEffect(PotionEffectType.HASTE, 999999, 0));
    }


    /**
     *
     * @param e event triggered by right-clicking diamond sword.
     */
    @EventHandler
    public void charge(PlayerInteractEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();
        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(uuid)) {
            return;
        }
        Player p = e.getPlayer();
        if (Objects.equals(Kit.equippedKits.get(uuid).name, name)) {
            if (p.getInventory().getItemInMainHand().getType().equals(Material.DIAMOND_SWORD)) {
                if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    int cooldown = p.getCooldown(Material.DIAMOND_SWORD);
                    if (cooldown == 0) {
                        p.setCooldown(Material.DIAMOND_SWORD, 300);
                        Messenger.sendActionInfo("You are charging forward", p);
                        p.addPotionEffect((new PotionEffect(PotionEffectType.SPEED, 160, 4)));
                        p.addPotionEffect((new PotionEffect(PotionEffectType.JUMP_BOOST, 160, 1)));

                        //Diminishing strength effect
                        p.addPotionEffect((new PotionEffect(PotionEffectType.STRENGTH, 160, 2)));

                        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST , 1, 1 );
                        vanguards.add(uuid);
                        Bukkit.getScheduler().runTaskLaterAsynchronously(Main.plugin, () -> vanguards.remove(uuid), 165);
                    } else {
                        Messenger.sendWarning("You can't charge forward yet.", p);
                    }
                }
            }
        }
    }

    /**
     * @param ed remove the potion effects on hit.
     */
    @EventHandler(ignoreCancelled = true)
    public void chargeHit(EntityDamageByEntityEvent ed) {
        if (ed.getDamager() instanceof Player player) {
            UUID uuid = ed.getDamager().getUniqueId();
            // Prevent using in lobby
            if (InCombat.isPlayerInLobby(uuid))
                return;
            if (vanguards.contains(uuid)) {
                for (PotionEffect effect : player.getActivePotionEffects()) {
                    if ((effect.getType().equals(PotionEffectType.SPEED) && effect.getAmplifier() == 4)
                            || (effect.getType().equals(PotionEffectType.JUMP_BOOST) && effect.getAmplifier() == 1)
                            || (effect.getType().equals(PotionEffectType.STRENGTH) && effect.getAmplifier() == 2)) {
                        player.removePotionEffect(effect.getType());
                    }
                }
                player.addPotionEffect((new PotionEffect(PotionEffectType.SPEED, 9999999, 0)));
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 1, 1);
                vanguards.remove(uuid);
            }
        }
    }

    @Override
    public ArrayList<Component> getGuiDescription() {
        ArrayList<Component> kitLore = new ArrayList<>();
        kitLore.add(Component.text("A master of camouflage and tracking. Can", NamedTextColor.GRAY));
        kitLore.add(Component.text("become invisible and strike enemies from behind", NamedTextColor.GRAY));
        kitLore.addAll(getBaseStats(health, regen, meleeDamage, ladderAmount));
        kitLore.add(Component.empty());
        kitLore.add(Component.text("Effects:", NamedTextColor.DARK_PURPLE));
        kitLore.add(Component.text("- Speed I", NamedTextColor.GRAY));
        kitLore.add(Component.text("- Haste I", NamedTextColor.GRAY));
        kitLore.add(Component.empty());
        kitLore.add(Component.text("Active:", NamedTextColor.GOLD));
        kitLore.add(Component.text("- Can charge forward gaining Speed V and", NamedTextColor.GRAY));
        kitLore.add(Component.text("Jump Boost II and bonus damage to the first", NamedTextColor.GRAY));
        kitLore.add(Component.text("target hit", NamedTextColor.GRAY));
        return kitLore;
    }
}
