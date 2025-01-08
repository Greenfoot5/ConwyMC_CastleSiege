package me.greenfoot5.castlesiege.kits.kits.sign_kits.Moria;

import me.greenfoot5.castlesiege.events.combat.InCombat;
import me.greenfoot5.castlesiege.kits.items.CSItemCreator;
import me.greenfoot5.castlesiege.kits.items.EquipmentSet;
import me.greenfoot5.castlesiege.kits.kits.Kit;
import me.greenfoot5.castlesiege.kits.kits.SignKit;
import me.greenfoot5.conwymc.data_types.Tuple;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
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
import java.util.UUID;

public class AxeThrower extends SignKit implements Listener {

    /**
     * Creates a new Moria Axe Thrower
     */
    public AxeThrower() {
        super("Axe Thrower", 300, 10, Material.NETHERITE_AXE, 2000);

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();

        // Weapon
        es.hotbar[0] = CSItemCreator.weapon(new ItemStack(Material.NETHERITE_AXE),
                Component.text("Throwable Axe", NamedTextColor.GREEN), null, null, 40);
        // Voted weapon
        es.votedWeapon = new Tuple<>(
                CSItemCreator.weapon(new ItemStack(Material.NETHERITE_AXE),
                        Component.text("Throwable Axe", NamedTextColor.GREEN),
                        Collections.singletonList(Component.text("⁎ Voted: +2 damage", NamedTextColor.AQUA)),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOTING, 0)), 42),
                0);

        es.offhand = CSItemCreator.weapon(new ItemStack(Material.NETHERITE_AXE, 3),
                Component.text("Throwable Axe", NamedTextColor.GREEN), null, null, 40);

        es.votedOffhand = CSItemCreator.weapon(new ItemStack(Material.NETHERITE_AXE, 4),
                        Component.text("Throwable Axe", NamedTextColor.GREEN),
                        Collections.singletonList(Component.text("⁎ Voted: +2 damage", NamedTextColor.AQUA)),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOTING, 0)), 42);

        // Chestplate
        es.chest = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE),
                Component.text("Leather Chestplate", NamedTextColor.GREEN), null, null,
                Color.fromRGB(218, 51, 10));
        ItemMeta chest = es.chest.getItemMeta();
        ArmorMeta chestMeta = (ArmorMeta) chest;
        assert chest != null;
        ArmorTrim chestTrim = new ArmorTrim(TrimMaterial.NETHERITE, TrimPattern.SPIRE);
        ((ArmorMeta) chest).setTrim(chestTrim);
        es.chest.setItemMeta(chestMeta);

        // Leggings
        es.legs = CSItemCreator.item(new ItemStack(Material.NETHERITE_LEGGINGS),
                Component.text("Reinforced Steel Leggings", NamedTextColor.GREEN), null, null);
        ItemMeta legs = es.legs.getItemMeta();
        ArmorMeta legsMeta = (ArmorMeta) legs;
        assert legs != null;
        ArmorTrim legsTrim = new ArmorTrim(TrimMaterial.NETHERITE, TrimPattern.HOST);
        ((ArmorMeta) legs).setTrim(legsTrim);
        es.legs.setItemMeta(legsMeta);

        // Boots
        es.feet = CSItemCreator.item(new ItemStack(Material.NETHERITE_BOOTS),
                Component.text("Iron Boots", NamedTextColor.GREEN), null, null);
        ItemMeta feet = es.feet.getItemMeta();
        ArmorMeta feetMeta = (ArmorMeta) feet;
        assert feet != null;
        ArmorTrim feetTrim = new ArmorTrim(TrimMaterial.NETHERITE, TrimPattern.HOST);
        ((ArmorMeta) feet).setTrim(feetTrim);
        es.feet.setItemMeta(feetMeta);
        // Voted Boots
        es.votedFeet = CSItemCreator.item(new ItemStack(Material.NETHERITE_BOOTS),
                Component.text("Iron Boots", NamedTextColor.GREEN),
                Collections.singletonList(Component.text("⁎ Voted: Depth Strider II", NamedTextColor.AQUA)),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)));
        ItemMeta votedFeet = es.votedFeet.getItemMeta();
        ArmorMeta votedFeetMeta = (ArmorMeta) votedFeet;
        assert votedFeet != null;
        ArmorTrim votedFeetTrim = new ArmorTrim(TrimMaterial.NETHERITE, TrimPattern.HOST);
        ((ArmorMeta) votedFeet).setTrim(votedFeetTrim);
        es.votedFeet.setItemMeta(votedFeetMeta);

        // Ladders
        es.hotbar[1] = new ItemStack(Material.LADDER, 4);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, 6), 1);

        super.potionEffects.add(new PotionEffect(PotionEffectType.HASTE, 999999, 0));

        // Death Messages
        super.deathMessage[0] = "You were axed to death by ";
        super.killMessage[0] = " axed ";
        super.killMessage[1] = " to death";

        super.equipment = es;
    }

    /**
     * Activate the spearman ability of throwing a spear
     * @param e The event called when right-clicking with a stick
     */
    @EventHandler
    public void throwAxe(PlayerInteractEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();
        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(uuid)) {
            return;
        }
        Player p = e.getPlayer();
        if (Objects.equals(Kit.equippedKits.get(uuid).name, name)) {
            ItemStack axe = p.getInventory().getItemInOffHand();
            if (axe.getType().equals(Material.NETHERITE_AXE)) {
                if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    //Not allowed to throw whilst clicking on enderchests or cakes.
                    if (e.getClickedBlock() != null) {
                        if (interactableBlock(e.getClickedBlock()) || p.getInventory().getItemInMainHand().getType() == Material.LADDER) {
                            return;
                        }
                    }
                    int cooldown = p.getCooldown(Material.NETHERITE_AXE);
                    if (cooldown == 0) {
                        axe.setAmount(axe.getAmount() - 1);
                        p.setCooldown(Material.NETHERITE_AXE, 15);
                        p.launchProjectile(Snowball.class).setVelocity(p.getLocation().getDirection().multiply(2.5));
                    }
                }
            }
        }
    }


    /**
     * Set the thrown axe's damage
     * @param e The event called when an arrow hits a player
     */
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void changeAxeDamage(ProjectileHitEvent e) {
        if (e.getEntity() instanceof Snowball ball) {

            if (ball.getShooter() instanceof Player damager){

                if (Objects.equals(Kit.equippedKits.get(damager.getUniqueId()).name, name)) {
                    if (e.getHitEntity() instanceof Player hit) {
                        hit.damage(60, damager);
                    } else if (e.getHitEntity() instanceof Horse horse) {
                        horse.damage(75, damager);
                    }
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
