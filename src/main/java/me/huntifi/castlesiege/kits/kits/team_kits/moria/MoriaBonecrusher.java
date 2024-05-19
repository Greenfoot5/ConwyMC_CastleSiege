package me.huntifi.castlesiege.kits.kits.team_kits.moria;

import me.huntifi.castlesiege.kits.items.CSItemCreator;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.kits.kits.TeamKit;
import me.huntifi.castlesiege.misc.CSNameTag;
import me.huntifi.conwymc.data_types.Tuple;
import me.huntifi.conwymc.util.Messenger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Random;

public class MoriaBonecrusher extends TeamKit implements Listener {

    /**
     * Creates a new Moria Bonecrusher
     */
    public MoriaBonecrusher() {
        super("Bonecrusher", 300, 8, "Moria",
                "The Orcs", 4000, Material.BONE, "moriabonecrusher");


        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();

        // Weapon
        es.hotbar[0] = CSItemCreator.weapon(new ItemStack(Material.BONE),
                Component.text("Crushing Bone", NamedTextColor.GREEN), null, null, 38);
        // Voted weapon
        es.votedWeapon = new Tuple<>(
                CSItemCreator.weapon(new ItemStack(Material.BONE),
                        Component.text("Crushing Bone", NamedTextColor.GREEN),
                        Collections.singletonList(Component.text("- voted: +2 damage", NamedTextColor.AQUA)),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), 40),
                0);

        // Chestplate
        es.chest = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE),
                Component.text("Leather Chestplate", NamedTextColor.GREEN), null, null,
                Color.fromRGB(21, 51, 10));

        // Leggings
        es.legs = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_LEGGINGS),
                Component.text("Leather Leggings", NamedTextColor.GREEN), null, null,
                Color.fromRGB(68, 65, 12));

        // Boots
        es.feet = CSItemCreator.item(new ItemStack(Material.IRON_BOOTS),
                Component.text("Iron Boots", NamedTextColor.GREEN),
                Collections.singletonList(Component.text("- Depth Strider I", NamedTextColor.AQUA)),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 1)));
        // Voted Boots
        es.votedFeet = CSItemCreator.item(new ItemStack(Material.IRON_BOOTS),
                Component.text("Iron Boots", NamedTextColor.GREEN),
                Collections.singletonList(Component.text("- voted: Depth Strider III", NamedTextColor.AQUA)),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 3)));

        // Ladders
        es.hotbar[1] = new ItemStack(Material.LADDER, 4);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, 6), 1);

        super.potionEffects.add(new PotionEffect(PotionEffectType.SPEED, 999999, 1));
        super.potionEffects.add(new PotionEffect(PotionEffectType.FAST_DIGGING, 999999, 0));

        // Death Messages
        super.deathMessage[0] = "You were crushed to death by ";
        super.killMessage[0] = " crushed ";
        super.killMessage[1] = " to death";

        super.equipment = es;
    }

    /**
     * Activate the Bonecrusher ability
     * @param e The event called when hitting another player
     */
    @EventHandler(ignoreCancelled = true)
    public void onCrush(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            Player p = (Player) e.getEntity();
            Player q = (Player) e.getDamager();

            // Bonecrusher tries to use stun an enemy
            if (Objects.equals(Kit.equippedKits.get(q.getUniqueId()).name, name) &&
                    q.getInventory().getItemInMainHand().getType() == Material.BONE &&
                    q.getCooldown(Material.BONE) == 0) {
                q.setCooldown(Material.BONE, 160);

                // Enemy blocks stun
                if (p.isBlocking()) {
                    Messenger.sendWarning(CSNameTag.mmUsername(p) + " blocked your crushing stun!", q);
                    Messenger.sendSuccess("You blocked " + CSNameTag.mmUsername(q) + "'s crushing stun!", p);
                } else if (p.isSneaking() && new Random().nextInt(4) == 0) {
                    Messenger.sendWarning(CSNameTag.mmUsername(p) + " dodged your crushing stun!", q);
                    Messenger.sendSuccess("You dodged " + CSNameTag.mmUsername(q) + "'s crushing stun!", p);
                } else {
                    Messenger.sendSuccess("You have crushed " + CSNameTag.mmUsername(p), q);
                    Messenger.sendWarning("You have been crushed by " + CSNameTag.mmUsername(q) + "!", p);
                    p.getWorld().playSound(p.getLocation(), Sound.BLOCK_BONE_BLOCK_BREAK , 1, 1 );
                    p.addPotionEffect((new PotionEffect(PotionEffectType.WEAKNESS, 50, 6)));
                    p.addPotionEffect((new PotionEffect(PotionEffectType.SLOW_DIGGING, 100, 5)));
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
