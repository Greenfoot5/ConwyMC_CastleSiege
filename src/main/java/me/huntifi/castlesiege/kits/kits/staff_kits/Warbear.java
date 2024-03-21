package me.huntifi.castlesiege.kits.kits.staff_kits;

import me.huntifi.castlesiege.kits.items.CSItemCreator;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.kits.kits.StaffKit;
import me.huntifi.castlesiege.maps.events.RamEvent;
import me.huntifi.conwymc.data_types.Tuple;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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
import java.util.UUID;

/**
 * The warbear kit
 */
public class Warbear extends StaffKit implements Listener {

    private static final int health = 500;
    private static final int regen = 14;
    private static final int meleeDamage = 30;

    /**
     * Set the equipment and attributes of this kit
     */
    public Warbear() {
        super("Warbear", health, regen, Material.DEAD_HORN_CORAL_FAN);
        super.canCap = false;
        super.canClimb = false;
        super.canSeeHealth = true;
        super.kbResistance = 2;

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();

        // Weapon
        es.hotbar[0] = CSItemCreator.weapon(new ItemStack(Material.DEAD_HORN_CORAL_FAN),
                Component.text("Claws", NamedTextColor.RED), null, null, meleeDamage);
        // Voted weapon
        es.votedWeapon = new Tuple<>(
                CSItemCreator.weapon(new ItemStack(Material.DEAD_HORN_CORAL_FAN),
                        Component.text("Claws", NamedTextColor.RED),
                        Collections.singletonList(Component.text("- voted: +2 damage", NamedTextColor.AQUA)),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), meleeDamage + 2),
                0);

        // Claws
        es.hotbar[1] = CSItemCreator.weapon(new ItemStack(Material.GHAST_TEAR),
                Component.text("Fangs", NamedTextColor.RED), null, null, 12);

        // Paw
        es.hotbar[2] = CSItemCreator.item(new ItemStack(Material.RABBIT_FOOT),
                Component.text("Paw", NamedTextColor.GREEN), null, null);

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
     * @param e The player interact event
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

    /**
     * Grants bonus damage when a warbear punches a gate
     * @param e The ram event
     */
    @EventHandler
    public void onPunchGate(RamEvent e) {
        for (UUID uuid : e.getPlayerUUIDs()) {
            if (Objects.equals(Kit.equippedKits.get(uuid).name, name) &&
            e.getRamType() == RamEvent.RamType.FIST) {
                e.setDamageDealt((int) (e.getDamageDealt() * 1.5));
            }
        }
    }

    @Override
    public ArrayList<Component> getGuiDescription() {
        ArrayList<Component> kitLore = new ArrayList<>();
        kitLore.add(Component.text("A mighty polar bear ready for action", NamedTextColor.GRAY));
        kitLore.addAll(getBaseStats(health, regen, meleeDamage, 0));
        kitLore.add(Component.empty());
        kitLore.add(Component.text("Active:", NamedTextColor.GOLD));
        kitLore.add(Component.text("- Stacks wither with basic attacks", NamedTextColor.GRAY));
        kitLore.add(Component.text("- Can bite to stun an opponent", NamedTextColor.GRAY));
        kitLore.add(Component.text("- Can gain a short burst of speed", NamedTextColor.GRAY));
        kitLore.add(Component.empty());
        kitLore.add(Component.text("Passive:", NamedTextColor.DARK_GREEN));
        kitLore.add(Component.text("- Can see players' health", NamedTextColor.GRAY));
        kitLore.add(Component.text("- Cannot climb", NamedTextColor.RED));
        kitLore.add(Component.text("- Deals bonus damage to gates when punching", NamedTextColor.GRAY));
        kitLore.add(Component.text("- Cannot cap flags", NamedTextColor.RED));
        return kitLore;
    }
}
