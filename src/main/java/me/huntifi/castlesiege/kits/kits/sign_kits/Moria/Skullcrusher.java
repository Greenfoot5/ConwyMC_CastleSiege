package me.huntifi.castlesiege.kits.kits.sign_kits.Moria;

import me.huntifi.castlesiege.kits.items.CSItemCreator;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.kits.kits.SignKit;
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
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Random;

public class Skullcrusher extends SignKit implements Listener {

    /**
     * Creates a new Moria Skullcrusher
     */
    public Skullcrusher() {
        super("Skullcrusher", 350, 10, Material.NETHERITE_SHOVEL, 2000);


        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();

        // Weapon
        es.hotbar[0] = CSItemCreator.weapon(new ItemStack(Material.NETHERITE_SHOVEL),
                Component.text("Orcish Mace", NamedTextColor.GREEN), null, null, 38);
        // Voted weapon
        es.votedWeapon = new Tuple<>(
                CSItemCreator.weapon(new ItemStack(Material.NETHERITE_SHOVEL),
                        Component.text("Orcish Mace", NamedTextColor.GREEN),
                        Collections.singletonList(Component.text("⁎ Voted: +2 damage", NamedTextColor.AQUA)),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), 40),
                0);

        // Shield
        es.offhand = CSItemCreator.item(new ItemStack(Material.SHIELD),
                Component.text("Orcish Shield", NamedTextColor.GREEN), null, null);

        // Chestplate
        es.chest = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE),
                Component.text("Orcish Chestplate", NamedTextColor.GREEN), null, null,
                Color.fromRGB(122, 122, 94));
        ItemMeta chest = es.chest.getItemMeta();
        ArmorMeta chestMeta = (ArmorMeta) chest;
        assert chest != null;
        ArmorTrim chestTrim = new ArmorTrim(TrimMaterial.NETHERITE, TrimPattern.SPIRE);
        ((ArmorMeta) chest).setTrim(chestTrim);
        es.chest.setItemMeta(chestMeta);

        // Leggings
        es.legs = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_LEGGINGS),
                Component.text("Orcish Legplates", NamedTextColor.GREEN), null, null,
                Color.fromRGB(127, 118, 40));
        ItemMeta legs = es.legs.getItemMeta();
        ArmorMeta legsMeta = (ArmorMeta) legs;
        assert legs != null;
        ArmorTrim legsTrim = new ArmorTrim(TrimMaterial.COPPER, TrimPattern.SPIRE);
        ((ArmorMeta) legs).setTrim(legsTrim);
        es.legs.setItemMeta(legsMeta);

        // Boots
        es.feet = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_BOOTS),
                Component.text("Orcish Boots", NamedTextColor.GREEN),
                Collections.singletonList(Component.text("- Depth Strider I", NamedTextColor.AQUA)),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 1)),
                Color.fromRGB(127, 118, 40));
        ItemMeta feet = es.feet.getItemMeta();
        ArmorMeta feetMeta = (ArmorMeta) feet;
        assert feet != null;
        ArmorTrim feetTrim = new ArmorTrim(TrimMaterial.COPPER, TrimPattern.SPIRE);
        ((ArmorMeta) feet).setTrim(feetTrim);
        es.feet.setItemMeta(feetMeta);
        // Voted Boots
        es.votedFeet = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_BOOTS),
                Component.text("Orcish Boots", NamedTextColor.GREEN),
                Collections.singletonList(Component.text("⁎ Voted: Depth Strider III", NamedTextColor.AQUA)),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 3)),
                Color.fromRGB(127, 118, 40));
        ItemMeta votedFeet = es.votedFeet.getItemMeta();
        ArmorMeta votedFeetMeta = (ArmorMeta) votedFeet;
        assert votedFeet != null;
        ArmorTrim votedFeetTrim = new ArmorTrim(TrimMaterial.COPPER, TrimPattern.SPIRE);
        ((ArmorMeta) votedFeet).setTrim(votedFeetTrim);
        es.votedFeet.setItemMeta(votedFeetMeta);

        // Ladders
        es.hotbar[1] = new ItemStack(Material.LADDER, 4);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, 6), 1);

        // Death Messages
        super.deathMessage[0] = "You were crushed to death by ";
        super.killMessage[0] = " crushed ";
        super.killMessage[1] = " to death";

        super.equipment = es;
    }

    /**
     * Activate the skullcrusher stun ability
     * @param e The event called when hitting another player
     */
    @EventHandler(ignoreCancelled = true)
    public void onStun(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            Player p = (Player) e.getEntity();
            Player q = (Player) e.getDamager();

            // Maceman tries to use stun an enemy
            if (Objects.equals(Kit.equippedKits.get(q.getUniqueId()).name, name) &&
                    q.getInventory().getItemInMainHand().getType() == Material.NETHERITE_SHOVEL &&
                    q.getCooldown(Material.NETHERITE_SHOVEL) == 0) {
                q.setCooldown(Material.NETHERITE_SHOVEL, 180);

                // Enemy blocks stun
                if (p.isBlocking()) {
                    Messenger.sendWarning(CSNameTag.username(p) + " blocked your stun", q);
                    if (new Random().nextInt(10) == 0) {
                        Messenger.sendWarning("Your shield broke whilst blocking " + CSNameTag.username(q) + "'s stun", p);
                        if (p.getInventory().getItemInMainHand().getType().equals(Material.SHIELD)) {
                            p.getInventory().getItemInMainHand().setAmount(0);
                        } else if (p.getInventory().getItemInOffHand().getType().equals(Material.SHIELD)) {
                            p.getInventory().getItemInOffHand().setAmount(0);
                        }
                        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK , 1, 1 );
                    }
                } else if (p.isSneaking() && new Random().nextInt(6) == 0) {
                    Messenger.sendWarning(CSNameTag.username(p) + " dodged your stun", q);
                    Messenger.sendSuccess("You dodged " + CSNameTag.username(q) + "'s stun", p);
                } else {
                    Messenger.sendSuccess("You have stunned " + CSNameTag.username(q), p);
                    Messenger.sendWarning("You have been stunned by " + CSNameTag.username(p) + "!", q);
                    p.getWorld().playSound(p.getLocation(), Sound.BLOCK_ANVIL_FALL , 1, 1.5F);
                    p.addPotionEffect((new PotionEffect(PotionEffectType.BLINDNESS, 30, 0)));
                    p.addPotionEffect((new PotionEffect(PotionEffectType.SLOW, 40, 2)));
                    p.addPotionEffect((new PotionEffect(PotionEffectType.WEAKNESS, 80, 0)));
                    p.addPotionEffect((new PotionEffect(PotionEffectType.SLOW_DIGGING, 80, 2)));
                    e.setDamage(e.getDamage() * 1.75);
                }
            }
        }
    }

    @Override
    public ArrayList<Component> getGuiDescription() {
        return null;
    }
}
