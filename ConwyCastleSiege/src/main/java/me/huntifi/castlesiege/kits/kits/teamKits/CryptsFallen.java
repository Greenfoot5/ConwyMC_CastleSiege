package me.huntifi.castlesiege.kits.kits.teamKits;

import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.kits.kits.MapKit;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.NameTag;
import me.libraryaddict.disguise.DisguiseConfig;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
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

public class CryptsFallen extends MapKit implements Listener {
    /**
     * Create a kit with basic settings
     *
     */
    public CryptsFallen() {
        super("Fallen", 160, 20, "Royal Crypts", "Tomb Guardians");
        super.canCap = true;
        super.canClimb = true;
        super.canSeeHealth = false;

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Weapon
        es.hotbar[0] = ItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                ChatColor.GRAY + "Fallen Sword", null, null, 25);
        // Voted weapon
        es.votedWeapon = new Tuple<>(
                ItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                        ChatColor.GRAY + "Fallen Sword",
                        Collections.singletonList(ChatColor.AQUA + "- voted: +2 damage"),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), 27),
                0);

        super.equipment = es;

        // Perm Potion Effect
        super.potionEffects.add(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 999999, 0));

        // Death Messages
        super.deathMessage[0] = "You were withered away by ";
        super.killMessage[0] = "You withered ";
        super.killMessage[1] = " away";
    }

    /**
     * Disguise the player as a Fallen
     * @param p The player to (un)disguise
     */
    @Override
    protected void disguise(Player p) {
        MobDisguise mobDisguise = new MobDisguise(DisguiseType.WITHER_SKELETON);

        mobDisguise.getWatcher().setCustomName(NameTag.color(p) + p.getName());
        mobDisguise.setCustomDisguiseName(true);
        mobDisguise.setHearSelfDisguise(true);
        mobDisguise.setModifyBoundingBox(true);
        mobDisguise.setSelfDisguiseVisible(false);
        mobDisguise.setNotifyBar(DisguiseConfig.NotifyBar.NONE);

        mobDisguise.setEntity(p);
        mobDisguise.startDisguise();
        NameTag.give(p);
    }



    /**
     * Activate the Fallen wither ability
     * @param e The event called when hitting another player
     */
    @EventHandler
    public void onWither(EntityDamageByEntityEvent e) {
        if (e.isCancelled()) {
            return;
        }

        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            Player p = (Player) e.getEntity();
            Player q = (Player) e.getDamager();

            // tries withering an enemy
            if (Objects.equals(Kit.equippedKits.get(q.getUniqueId()).name, name) &&
                    MapController.getCurrentMap().getTeam(q.getUniqueId())
                            != MapController.getCurrentMap().getTeam(p.getUniqueId()) &&
                    q.getInventory().getItemInMainHand().getType() == Material.IRON_SWORD &&
                    q.getCooldown(Material.IRON_SWORD) == 0) {
                q.setCooldown(Material.IRON_SWORD, 50);

                if (!p.isBlocking()) {
                    p.addPotionEffect((new PotionEffect(PotionEffectType.WITHER, 120, 1)));
                }
            }
        }
    }
}
