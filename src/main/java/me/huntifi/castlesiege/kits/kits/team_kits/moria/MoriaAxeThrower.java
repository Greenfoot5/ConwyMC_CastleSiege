package me.huntifi.castlesiege.kits.kits.team_kits.moria;

import me.huntifi.conwymc.data_types.Tuple;
import me.huntifi.conwymc.util.Messenger;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.CSItemCreator;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.kits.kits.TeamKit;
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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

public class MoriaAxeThrower extends TeamKit implements Listener {

    public MoriaAxeThrower() {
        super("Axe Thrower", 300, 10, "Moria",
                "The Dwarves", 5000, Material.STONE_AXE,
                "moriaaxethrower");

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Weapon
        es.hotbar[0] = CSItemCreator.weapon(new ItemStack(Material.STONE_AXE, 5),
                Component.text("Throwable Axe", NamedTextColor.GREEN), null, null, 40);
        // Voted weapon
        es.votedWeapon = new Tuple<>(
                CSItemCreator.weapon(new ItemStack(Material.STONE_AXE, 6),
                        Component.text("Throwable Axe", NamedTextColor.GREEN),
                        Collections.singletonList(Component.text("- voted: +2 damage", NamedTextColor.AQUA)),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), 42),
                0);

        // Chestplate
        es.chest = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE),
                Component.text("Leather Chestplate", NamedTextColor.GREEN), null, null,
                Color.fromRGB(218, 51, 10));

        // Leggings
        es.legs = CSItemCreator.item(new ItemStack(Material.NETHERITE_LEGGINGS),
                Component.text("Reinforced Steel Leggings", NamedTextColor.GREEN), null, null);

        // Boots
        es.feet = CSItemCreator.item(new ItemStack(Material.IRON_BOOTS),
                Component.text("Iron Boots", NamedTextColor.GREEN), null, null);
        // Voted Boots
        es.votedFeet = CSItemCreator.item(new ItemStack(Material.IRON_BOOTS),
                Component.text("Iron Boots", NamedTextColor.GREEN),
                Collections.singletonList(Component.text("- voted: Depth Strider II", NamedTextColor.AQUA)),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)));

        // Ladders
        es.hotbar[1] = new ItemStack(Material.LADDER, 4);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, 6), 1);

        super.potionEffects.add(new PotionEffect(PotionEffectType.FAST_DIGGING, 999999, 0));

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
    public void throwSpear(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(uuid)) {
            return;
        }

        ItemStack axe = p.getInventory().getItemInMainHand();
        int cooldown = p.getCooldown(Material.STONE_AXE);
        if (Objects.equals(Kit.equippedKits.get(uuid).name, name)) {
            if (axe.getType().equals(Material.STONE_AXE)) {
                if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (cooldown == 0) {
                        axe.setAmount(axe.getAmount() - 1);
                        p.setCooldown(Material.STONE_AXE, 15);
                        Messenger.sendActionInfo("You threw your Axe!", p);
                        p.launchProjectile(Snowball.class).setVelocity(p.getLocation().getDirection().multiply(2.5));

                    } else {
                        Messenger.sendActionError("You can't throw your Axe yet!", p);
                    }
                }
            }
        }
    }


    /**
     * Set the thrown axe's damage
     * @param e The event called when an arrow hits a player
     */
    @EventHandler (priority = EventPriority.LOW, ignoreCancelled = true)
    public void changeAxeDamage(ProjectileHitEvent e) {
        if (e.getEntity() instanceof Snowball) {
            Snowball ball = (Snowball) e.getEntity();

            if (ball.getShooter() instanceof Player){
                Player damager = (Player) ball.getShooter();

                if (Objects.equals(Kit.equippedKits.get(damager.getUniqueId()).name, name)) {
                    if (e.getHitEntity() instanceof Player) {
                        Player hit = (Player) e.getHitEntity();
                        hit.damage(65, damager);
                    } else if (e.getHitEntity() instanceof Horse) {
                        Horse horse = (Horse) e.getHitEntity();
                        horse.damage(90, damager);
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
