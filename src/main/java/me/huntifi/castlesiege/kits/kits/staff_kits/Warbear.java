package me.huntifi.castlesiege.kits.kits.staff_kits;

import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.kits.kits.StaffKit;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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
import java.util.Objects;

/**
 * The warbear kit
 */
public class Warbear extends StaffKit implements Listener {

    /**
     * Set the equipment and attributes of this kit
     */
    public Warbear() {
        super("Warbear", 500, 14, Material.DEAD_HORN_CORAL_FAN);
        super.canCap = false;
        super.canClimb = false;
        super.canSeeHealth = true;
        super.kbResistance = 2;

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Weapon
        es.hotbar[0] = ItemCreator.weapon(new ItemStack(Material.DEAD_HORN_CORAL_FAN),
                ChatColor.RED + "Claws", null, null, 30);
        // Voted weapon
        es.votedWeapon = new Tuple<>(
                ItemCreator.weapon(new ItemStack(Material.DEAD_HORN_CORAL_FAN),
                        ChatColor.RED + "Claws",
                        Collections.singletonList(ChatColor.AQUA + "- voted: +2 damage"),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), 32),
                0);

        // Claws
        es.hotbar[1] = ItemCreator.weapon(new ItemStack(Material.GHAST_TEAR),
                ChatColor.RED + "Fangs", null, null, 12);

        // Paw
        es.hotbar[2] = ItemCreator.item(new ItemStack(Material.RABBIT_FOOT),
                ChatColor.GREEN + "Paw", null, null);

        super.equipment = es;

        // Perm Potion Effect
        super.potionEffects.add(new PotionEffect(PotionEffectType.SLOW, 999999, 1));
        super.potionEffects.add(new PotionEffect(PotionEffectType.SLOW_DIGGING, 999999, 1));

        // Death Messages
        super.deathMessage[0] = "You were eaten alive by ";
        super.killMessage[0] = " ate ";
        super.killMessage[1] = " alive";
    }

    /**
     * Disguise the player as a polar bear
     * @param p The player to (un)disguise
     */
    @Override
    protected void setDisguise(Player p) {
        MobDisguise mobDisguise = new MobDisguise(DisguiseType.POLAR_BEAR);

        disguise(p, mobDisguise);
    }

    /**
     * Cause withering damage to the bitten opponent and try to activate the stun ability
     * @param e The event called when hitting another player
     */
    @EventHandler (ignoreCancelled = true)
    public void onAttack(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player && e.getDamager() instanceof Player)) {
            return;
        }

        Player player = (Player) e.getDamager();
        if (Objects.equals(Kit.equippedKits.get(player.getUniqueId()).name, name)) {

            // Warbear attacked an enemy player
            Material item = player.getInventory().getItemInMainHand().getType();
            if (player.getCooldown(item) > 0)
                e.setCancelled(true);
            else if (item == Material.GHAST_TEAR)
                bite(player, (Player) e.getEntity());
            else if (item == Material.DEAD_HORN_CORAL_FAN)
                scratch(player, (Player) e.getEntity());
        }
    }

    /**
     * Activate the warbear's bite ability
     * @param bear The warbear
     * @param player The bitten player
     */
    private void bite(Player bear, Player player) {
        bear.setCooldown(Material.GHAST_TEAR, 280);
        player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 180, 0));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 80, 2));
    }

    /**
     * Activate the warbear's scratch ability
     * @param bear The warbear
     * @param player The scratched player
     */
    private void scratch(Player bear, Player player) {
        bear.setCooldown(Material.DEAD_HORN_CORAL_FAN, 10);
        int amp = 0;
        int duration = 40;
        if (player.hasPotionEffect(PotionEffectType.WITHER)) {
            amp = player.getPotionEffect(PotionEffectType.WITHER).getAmplifier() + 1;
            if (amp >= 2) {
                duration = 30;
            }
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, duration, amp));
    }

    /**
     * Activate the warbear's flee ability
     */
    @EventHandler
    public void onFlee(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (Objects.equals(Kit.equippedKits.get(player.getUniqueId()).name, name)
                && (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)
                && player.getInventory().getItemInMainHand().getType() == Material.RABBIT_FOOT
                && player.getCooldown(Material.RABBIT_FOOT) == 0) {
            player.setCooldown(Material.RABBIT_FOOT, 500);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 180, 4));
        }
    }

    @Override
    public ArrayList<String> getGuiDescription() {
        ArrayList<String> description = new ArrayList<>();
        description.add("§7//TODO - Add kit description");
        return description;
    }
}
