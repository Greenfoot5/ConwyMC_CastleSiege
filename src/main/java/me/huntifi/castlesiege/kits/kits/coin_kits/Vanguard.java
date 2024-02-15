package me.huntifi.castlesiege.kits.kits.coin_kits;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.kits.kits.CoinKit;
import me.huntifi.castlesiege.kits.kits.Kit;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

/**
 * The Vanguard kit
 */
public class Vanguard extends CoinKit implements Listener, CommandExecutor {

    private static final int health = 270;
    private static final double regen = 10.5;
    private static final double meleeDamage = 57;
    private static final int ladderAmount = 4;

    private static final ArrayList<UUID> vanguards = new ArrayList<>();

    /**
     * Set the equipment and attributes of this kit
     */
    public Vanguard() {
        super("Vanguard", health, regen, Material.DIAMOND_SWORD);

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Weapon
        es.hotbar[0] = ItemCreator.weapon(new ItemStack(Material.DIAMOND_SWORD),
                ChatColor.GREEN + "Reinforced Iron Sword",
                Collections.singletonList(ChatColor.AQUA + "Right-click to activate charge ability."),
                null, meleeDamage);
        // Voted Weapon
        es.votedWeapon = new Tuple<>(
                ItemCreator.weapon(new ItemStack(Material.DIAMOND_SWORD),
                        ChatColor.GREEN + "Reinforced Iron Sword",
                        Arrays.asList(ChatColor.AQUA + "Right-click to activate charge ability.",
                                ChatColor.AQUA + "- voted: +2 damage"),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), meleeDamage + 2),
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
        es.hotbar[1] = new ItemStack(Material.LADDER, ladderAmount);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, ladderAmount + 2), 1);

        super.equipment = es;

        //passive effects
        super.potionEffects.add(new PotionEffect(PotionEffectType.SPEED, 999999, 0));
        super.potionEffects.add(new PotionEffect(PotionEffectType.FAST_DIGGING, 999999, 0));
    }


    /**
     *
     * @param e event triggered by right-clicking diamond sword.
     */
    @EventHandler(ignoreCancelled = true)
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
                        p.setCooldown(Material.DIAMOND_SWORD, 300);
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                                ChatColor.AQUA + "You are charging forward"));
                        p.addPotionEffect((new PotionEffect(PotionEffectType.SPEED, 160, 4)));
                        p.addPotionEffect((new PotionEffect(PotionEffectType.JUMP, 160, 1)));

                        //Diminishing strength effect
                        p.addPotionEffect((new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 160, 2)));

                        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST , 1, 1 );
                        vanguards.add(uuid);
                        Bukkit.getScheduler().runTaskLaterAsynchronously(Main.plugin, () -> vanguards.remove(uuid), 165);
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
    @EventHandler(ignoreCancelled = true)
    public void chargeHit(EntityDamageByEntityEvent ed) {
        if (ed.getDamager() instanceof Player) {
            Player player = (Player) ed.getDamager();
            UUID uuid = player.getUniqueId();

            // Prevent using in lobby
            if (InCombat.isPlayerInLobby(uuid))
                return;

            if (vanguards.contains(uuid)) {
                for (PotionEffect effect : player.getActivePotionEffects()) {
                    if ((effect.getType().getName().equals(PotionEffectType.SPEED.getName()) && effect.getAmplifier() == 4)
                            || (effect.getType().getName().equals(PotionEffectType.JUMP.getName()) && effect.getAmplifier() == 1)
                            || (effect.getType().getName().equals(PotionEffectType.INCREASE_DAMAGE.getName()) && effect.getAmplifier() == 2)) {
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
    public ArrayList<String> getGuiDescription() {
        ArrayList<String> kitLore = new ArrayList<>();
        kitLore.add("§7A master of camouflage and tracking. Can");
        kitLore.add("§7become invisible and strike enemies from behind");
        kitLore.addAll(getBaseStats(health, regen, meleeDamage, ladderAmount));
        kitLore.add(" ");
        kitLore.add("§5Effects:");
        kitLore.add("§7- Speed I");
        kitLore.add("§7- Haste I");
        kitLore.add(" ");
        kitLore.add("§6Active:");
        kitLore.add("§7- Can charge forward gaining Speed V and");
        kitLore.add("§7Jump Boost II and bonus damage to the first");
        kitLore.add("§7target hit");
        return kitLore;
    }
}
