package me.greenfoot5.castlesiege.kits.kits.sign_kits;

import me.greenfoot5.castlesiege.events.combat.InCombat;
import me.greenfoot5.castlesiege.kits.items.CSItemCreator;
import me.greenfoot5.castlesiege.kits.items.EquipmentSet;
import me.greenfoot5.castlesiege.kits.kits.SignKit;
import me.greenfoot5.conwymc.data_types.Tuple;
import me.greenfoot5.conwymc.util.Messenger;
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

/**
 * Axeman kit
 */
public class Axeman extends SignKit implements Listener {

    private static final int meleeDamage = 40;
    private static final int ladders = 4;

    /**
     * Creates a new Axeman
     */
    public Axeman() {
        super("Axeman", 300, 10, Material.STONE_AXE, 2500);
        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();

        // Weapon
        es.hotbar[0] = CSItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                Component.text("Iron Sword", NamedTextColor.GREEN), null, null, meleeDamage);
        // Voted weapon
        es.votedWeapon = new Tuple<>(
                CSItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                        Component.text("Iron Sword", NamedTextColor.GREEN),
                        Collections.singletonList(Component.text("⁎ Voted: +2 damage", NamedTextColor.AQUA)),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOTING, 1)), meleeDamage + 2),
                0);
        // Weapon
        es.hotbar[1] = CSItemCreator.weapon(new ItemStack(Material.STONE_AXE, 2),
                Component.text("Throwable Axe", NamedTextColor.GREEN), null, null, meleeDamage);

        // Chestplate
        es.chest = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE),
                Component.text("Leather Chestplate", NamedTextColor.GREEN), null, null,
                Color.fromRGB(51, 198, 46));

        // Leggings
        es.legs = CSItemCreator.item(new ItemStack(Material.NETHERITE_LEGGINGS),
                Component.text("Reinforced Steel Leggings", NamedTextColor.GREEN), null, null);

        // Boots
        es.feet = CSItemCreator.item(new ItemStack(Material.IRON_BOOTS),
                Component.text("Iron Boots", NamedTextColor.GREEN), null, null);
        // Voted Boots
        es.votedFeet = CSItemCreator.item(new ItemStack(Material.IRON_BOOTS),
                Component.text("Iron Boots", NamedTextColor.GREEN),
                Collections.singletonList(Component.text("⁎ Voted: Depth Strider II", NamedTextColor.AQUA)),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)));

        // Ladders
        es.hotbar[2] = new ItemStack(Material.LADDER, 4);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, 6), 2);

        super.potionEffects.add(new PotionEffect(PotionEffectType.HASTE, 999999, 0));

        // Death Messages
        super.deathMessage[0] = "You were axed to death by ";
        super.killMessage[0] = " axed ";
        super.killMessage[1] = " to death";

        super.equipment = es;
    }
    /**
     * Activate the axeman ability of throwing an Axe
     * @param e The event called when right-clicking with a stick
     */
    @EventHandler
    public void throwAxe(PlayerInteractEvent e) {
        if (e.getPlayer() != equippedPlayer)
            return;

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(equippedPlayer.getUniqueId()))
            return;

        ItemStack axe = equippedPlayer.getInventory().getItemInMainHand();
        if (!axe.getType().equals(Material.STONE_AXE))
            return;

        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        if (equippedPlayer.getCooldown(Material.STONE_AXE) == 0) {
            axe.add(-1);
            equippedPlayer.setCooldown(Material.STONE_AXE, 60);
            Messenger.sendActionInfo("You threw your Axe!", equippedPlayer);
            equippedPlayer.launchProjectile(Snowball.class).setVelocity(equippedPlayer.getLocation().getDirection().multiply(2.5));

        } else {
            Messenger.sendActionError("You can't throw your Axe yet.", equippedPlayer);
        }
    }


    /**
     * Set the thrown axe's damage
     * @param e The event called when an arrow hits a player
     */
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void changeAxeDamage(ProjectileHitEvent e) {
        if (!(e.getEntity() instanceof Snowball ball))
            return;

        if (ball.getShooter() == equippedPlayer)
            return;

        if (e.getHitEntity() instanceof Player hit) {
            hit.damage(105, equippedPlayer);
        } else if (e.getHitEntity() instanceof Horse horse) {
            horse.damage(125, equippedPlayer);
        }
    }

    @Override
    public ArrayList<Component> getGuiDescription() {
        ArrayList<Component> description = new ArrayList<>();
        description.add(Component.text("Has a sword to stab and slash in one hand", NamedTextColor.GRAY));
        description.add(Component.text("Axes to throw in the other", NamedTextColor.GRAY));
        description.addAll(getBaseStats(this.baseHealth, this.regenAmount, meleeDamage, ladders));
        description.add(Component.empty());
        description.add(Component.text("Effects:", NamedTextColor.DARK_PURPLE));
        description.add(Component.text("- Haste I", NamedTextColor.GRAY));
        description.add(Component.empty());
        description.add(Component.text("Active:", NamedTextColor.GOLD));
        description.add(Component.text("- Can throw their axe", NamedTextColor.GRAY));
        return description;
    }
}
