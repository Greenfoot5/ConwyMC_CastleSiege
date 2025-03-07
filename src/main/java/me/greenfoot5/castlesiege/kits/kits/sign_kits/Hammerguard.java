package me.greenfoot5.castlesiege.kits.kits.sign_kits;

import me.greenfoot5.castlesiege.kits.items.CSItemCreator;
import me.greenfoot5.castlesiege.kits.items.EquipmentSet;
import me.greenfoot5.castlesiege.kits.kits.SignKit;
import me.greenfoot5.castlesiege.misc.CSNameTag;
import me.greenfoot5.conwymc.data_types.Tuple;
import me.greenfoot5.conwymc.util.Messenger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Hammerguard kit
 */
public class Hammerguard extends SignKit implements Listener {

    /**
     * Creates a new Hammerguard
     */
    public Hammerguard() {
        super("Hammerguard", 340, 10, Material.IRON_AXE, 2000);

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();

        // Weapon
        es.hotbar[0] = CSItemCreator.weapon(new ItemStack(Material.IRON_AXE),
                Component.text("Dwarven War-Hammer", NamedTextColor.GREEN), null, null, 40);
        // Voted weapon
        es.votedWeapon = new Tuple<>(
                CSItemCreator.weapon(new ItemStack(Material.IRON_AXE),
                        Component.text("Dwarven War-Hammer", NamedTextColor.GREEN),
                        Collections.singletonList(Component.text("⁎ Voted: +2 damage", NamedTextColor.AQUA)),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOTING, 1)), 42),
                0);

        // Chestplate
        es.chest = CSItemCreator.item(new ItemStack(Material.NETHERITE_CHESTPLATE),
                Component.text("Reinforced Steel Chestplate", NamedTextColor.GREEN), null, null);
        ItemMeta chest = es.chest.getItemMeta();
        ArmorMeta chestMeta = (ArmorMeta) chest;
        assert chest != null;
        ArmorTrim chestTrim = new ArmorTrim(TrimMaterial.GOLD, TrimPattern.SHAPER);
        ((ArmorMeta) chest).setTrim(chestTrim);
        es.chest.setItemMeta(chestMeta);

        // Leggings
        es.legs = CSItemCreator.item(new ItemStack(Material.NETHERITE_LEGGINGS),
                Component.text("Mithril Leggings", NamedTextColor.GREEN), null, null);
        ItemMeta legs = es.legs.getItemMeta();
        ArmorMeta legsMeta = (ArmorMeta) legs;
        assert legs != null;
        ArmorTrim legsTrim = new ArmorTrim(TrimMaterial.IRON, TrimPattern.SILENCE);
        ((ArmorMeta) legs).setTrim(legsTrim);
        es.legs.setItemMeta(legsMeta);

        // Boots
        es.feet = CSItemCreator.item(new ItemStack(Material.CHAINMAIL_BOOTS),
                Component.text("Iron Boots", NamedTextColor.GREEN), null, null);
        ItemMeta feet = es.feet.getItemMeta();
        ArmorMeta feetMeta = (ArmorMeta) feet;
        assert feet != null;
        ArmorTrim feetTrim = new ArmorTrim(TrimMaterial.IRON, TrimPattern.SILENCE);
        ((ArmorMeta) feet).setTrim(feetTrim);
        es.feet.setItemMeta(feetMeta);
        // Voted Boots
        es.votedFeet = CSItemCreator.item(new ItemStack(Material.CHAINMAIL_BOOTS),
                Component.text("Iron Boots", NamedTextColor.GREEN),
                Collections.singletonList(Component.text("⁎ Voted: Depth Strider II", NamedTextColor.AQUA)),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)));
        ItemMeta votedFeet = es.votedFeet.getItemMeta();
        ArmorMeta votedFeetMeta = (ArmorMeta) votedFeet;
        assert votedFeet != null;
        ArmorTrim votedFeetTrim = new ArmorTrim(TrimMaterial.IRON, TrimPattern.SILENCE);
        ((ArmorMeta) votedFeet).setTrim(votedFeetTrim);
        es.votedFeet.setItemMeta(votedFeetMeta);

        // Ladders
        es.hotbar[1] = new ItemStack(Material.LADDER, 4);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, 6), 1);

        super.potionEffects.add(new PotionEffect(PotionEffectType.MINING_FATIGUE, 999999, 0));

        // Death Messages
        super.deathMessage[0] = "You were hammered to death by ";
        super.killMessage[0] = " hammered ";
        super.killMessage[1] = " to death";

        super.equipment = es;
    }


    /**
     * Activate the hammerer's stun ability
     * @param e The event called when hitting another player
     */
    @EventHandler(ignoreCancelled = true)
    public void onStun(EntityDamageByEntityEvent e) {
        if (e.getDamager() != equippedPlayer)
            return;

        if (e.getEntity() instanceof Player hit) {

            // Hammerer tries to use stun on an enemy
            if (equippedPlayer.getInventory().getItemInMainHand().getType() == Material.IRON_AXE &&
                    equippedPlayer.getCooldown(Material.IRON_AXE) == 0) {
                equippedPlayer.setCooldown(Material.IRON_AXE, 180);

                // Enemy blocks stun
                if (hit.isBlocking()) {
                    Messenger.sendWarning(CSNameTag.mmUsername(hit) + " blocked your stun!", equippedPlayer);
                    Messenger.sendSuccess("You blocked " + CSNameTag.mmUsername(equippedPlayer) + "'s stun!", hit);
                }
                if (hit.isSneaking() && new Random().nextInt(6) == 0) {
                    Messenger.sendWarning(CSNameTag.mmUsername(hit) + " dodged your stun!", equippedPlayer);
                    Messenger.sendSuccess("You dodged " + CSNameTag.mmUsername(equippedPlayer) + "'s stun!", hit);
                } else {
                    Messenger.sendSuccess("You have stunned " + CSNameTag.mmUsername(hit), equippedPlayer);
                    Messenger.sendWarning("You have been stunned by " + CSNameTag.mmUsername(equippedPlayer) + "!", hit);
                    equippedPlayer.getWorld().playSound(equippedPlayer.getLocation(), Sound.BLOCK_ANVIL_BREAK , 1, 2F);
                    hit.addPotionEffect((new PotionEffect(PotionEffectType.BLINDNESS, 30, 0)));
                    hit.addPotionEffect((new PotionEffect(PotionEffectType.SLOWNESS, 40, 2)));
                    hit.addPotionEffect((new PotionEffect(PotionEffectType.WEAKNESS, 100, 0)));
                    hit.addPotionEffect((new PotionEffect(PotionEffectType.MINING_FATIGUE, 100, 2)));
                    e.setDamage(e.getDamage() * 2);
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
