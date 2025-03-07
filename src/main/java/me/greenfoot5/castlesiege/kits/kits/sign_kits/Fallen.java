package me.greenfoot5.castlesiege.kits.kits.sign_kits;

import me.greenfoot5.castlesiege.kits.items.CSItemCreator;
import me.greenfoot5.castlesiege.kits.items.EquipmentSet;
import me.greenfoot5.castlesiege.kits.kits.SignKit;
import me.greenfoot5.conwymc.data_types.Tuple;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Royal Crypts specific kit that can apply wither
 */
public class Fallen extends SignKit implements Listener {

    /**
     * Creates a new Fallen
     */
    public Fallen() {
        super("Fallen", 350, 23, Material.WITHER_SKELETON_SKULL, 2000);
        super.canCap = true;
        super.canClimb = true;
        super.canSeeHealth = false;

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();

        // Weapon
        es.hotbar[0] = CSItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                Component.text("Fallen Sword", NamedTextColor.GRAY), null, null, 40.5);
        // Voted weapon
        es.votedWeapon = new Tuple<>(
                CSItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                        Component.text("Fallen Sword", NamedTextColor.GRAY),
                        Collections.singletonList(Component.text("⁎ Voted: +2 damage", NamedTextColor.AQUA)),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOTING, 1)), 39),
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
     */
    @Override
    protected void setDisguise() {
        MobDisguise mobDisguise = new MobDisguise(DisguiseType.WITHER_SKELETON);
        mobDisguise.setModifyBoundingBox(true);

        disguise(mobDisguise);
    }



    /**
     * Activate the Fallen wither ability
     * @param e The event called when hitting another player
     */
    @EventHandler(ignoreCancelled = true)
    public void onWither(EntityDamageByEntityEvent e) {
        if (e.getDamager() != equippedPlayer)
            return;

        if (e.getEntity() instanceof Player hit) {

            // tries withering an enemy
            if (equippedPlayer.getInventory().getItemInMainHand().getType() == Material.IRON_SWORD
                    && equippedPlayer.getCooldown(Material.IRON_SWORD) == 0) {
                equippedPlayer.setCooldown(Material.IRON_SWORD, 50);

                if (!hit.isBlocking())
                    hit.addPotionEffect((new PotionEffect(PotionEffectType.WITHER, 180, 1)));
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
