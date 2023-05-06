package me.huntifi.castlesiege.kits.kits.team_kits;

import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.kits.kits.TeamKit;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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

public class CryptsFallen extends TeamKit implements Listener {
    /**
     * Create a kit with basic settings
     */
    public CryptsFallen() {
        super("Fallen", 360, 23, "Royal Crypts", "Tomb Guardians",
                3500, 0, Material.WITHER_SKELETON_SKULL);
        super.canCap = true;
        super.canClimb = true;
        super.canSeeHealth = false;

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Weapon
        es.hotbar[0] = ItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                ChatColor.GRAY + "Fallen Sword", null, null, 37);
        // Voted weapon
        es.votedWeapon = new Tuple<>(
                ItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                        ChatColor.GRAY + "Fallen Sword",
                        Collections.singletonList(ChatColor.AQUA + "- voted: +2 damage"),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), 39),
                0);

        super.equipment = es;

        // Perm Potion Effect
        super.potionEffects.add(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 999999, 0));
        super.potionEffects.add(new PotionEffect(PotionEffectType.WATER_BREATHING, 999999, 0));
        super.potionEffects.add(new PotionEffect(PotionEffectType.NIGHT_VISION, 999999, 0));

        // Death Messages
        super.deathMessage[0] = "You were withered away by ";
        super.killMessage[0] = " withered ";
        super.killMessage[1] = " away";
    }

    /**
     * Disguise the player as a Fallen
     * @param p The player to (un)disguise
     */
    @Override
    protected void setDisguise(Player p) {
        MobDisguise mobDisguise = new MobDisguise(DisguiseType.WITHER_SKELETON);
        mobDisguise.setModifyBoundingBox(true);

        disguise(p, mobDisguise);
    }



    /**
     * Activate the Fallen wither ability
     * @param e The event called when hitting another player
     */
    @EventHandler (ignoreCancelled = true)
    public void onWither(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            Player p = (Player) e.getEntity();
            Player q = (Player) e.getDamager();

            // tries withering an enemy
            if (Objects.equals(Kit.equippedKits.get(q.getUniqueId()).name, name) &&
                    q.getInventory().getItemInMainHand().getType() == Material.IRON_SWORD &&
                    q.getCooldown(Material.IRON_SWORD) == 0) {
                q.setCooldown(Material.IRON_SWORD, 50);

                if (!p.isBlocking())
                    p.addPotionEffect((new PotionEffect(PotionEffectType.WITHER, 180, 1)));
            }
        }
    }
}
