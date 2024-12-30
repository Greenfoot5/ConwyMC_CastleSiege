package me.huntifi.castlesiege.kits.kits.level_kits;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.events.timed.BarCooldown;
import me.huntifi.castlesiege.kits.items.CSItemCreator;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.kits.kits.LevelKit;
import me.huntifi.conwymc.data_types.Tuple;
import me.huntifi.conwymc.util.Messenger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

public class SpearKnight extends LevelKit implements Listener {

    private static final int health = 330;
    private static final double regen = 10.5;
    private static final double meleeDamage = 40.5;
    private static final int ladderCount = 4;
    private static final int level = 10;

    // Spear Throw
    private static final int throwCooldown = 160;
    private static final double throwVelocity = 2.2;
    private static final int throwDelay = 5;
    private static final double throwDamage = 65;

    // Damage multiplier when hitting horses
    private static final double HORSE_MULTIPLIER = 1.5;

    /**
     * Creates a new Spear Knight
     */
    public SpearKnight() {
        super("Spear Knight", health, regen, Material.BLAZE_ROD, level);
        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();

        // Weapon
        es.hotbar[0] = CSItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                Component.text("Iron Sword", NamedTextColor.GREEN), null, null, meleeDamage);
        // Voted Weapon
        es.votedWeapon = new Tuple<>(
                CSItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                        Component.text("Iron Sword", NamedTextColor.GREEN),
                        Collections.singletonList(Component.text("⁎ Voted: +2 damage", NamedTextColor.AQUA)),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), meleeDamage + 2),
                0);

        // Weapon
        es.offhand = CSItemCreator.weapon(new ItemStack(Material.STICK, 1),
                Component.text("Spear", NamedTextColor.GREEN),
                Collections.singletonList(Component.text("Right-click to throw a spear.", NamedTextColor.AQUA)),
                null, meleeDamage);

        // Chestplate
        es.chest = CSItemCreator.item(new ItemStack(Material.IRON_CHESTPLATE),
                Component.text("Iron Chestplate", NamedTextColor.GREEN), null, null);
        ItemMeta chest = es.chest.getItemMeta();
        ArmorMeta chestMeta = (ArmorMeta) chest;
        assert chest != null;
        ArmorTrim chestTrim = new ArmorTrim(TrimMaterial.COPPER, TrimPattern.SHAPER);
        ((ArmorMeta) chest).setTrim(chestTrim);
        es.chest.setItemMeta(chestMeta);

        // Leggings
        es.legs = CSItemCreator.item(new ItemStack(Material.CHAINMAIL_LEGGINGS),
                Component.text("Chainmail Leggings", NamedTextColor.GREEN), null, null);

        // Boots
        es.feet = CSItemCreator.item(new ItemStack(Material.LEATHER_BOOTS),
                Component.text("Leather Boots", NamedTextColor.GREEN), null, null);
        // Voted Boots
        es.votedFeet = CSItemCreator.item(new ItemStack(Material.LEATHER_BOOTS),
                Component.text("Leather Boots", NamedTextColor.GREEN),
                Collections.singletonList(Component.text("⁎ Voted: Depth Strider II", NamedTextColor.AQUA)),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)));

        // Ladders
        es.hotbar[1] = new ItemStack(Material.LADDER, ladderCount);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, ladderCount + 2), 1);

        super.equipment = es;


        // Death Messages
        super.projectileDeathMessage[0] = "You were impaled by ";
        super.projectileKillMessage[0] = " impaled ";
    }



    /**
     * Activate the spearman ability of throwing a spear
     * @param e The event called when right-clicking with a stick
     */
    @EventHandler
    public void throwSpear(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        int cooldown = p.getCooldown(Material.STICK);

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(uuid)) {
            return;
        }

        if (Objects.equals(Kit.equippedKits.get(uuid).name, name)) {
            ItemStack stack = null;
            if (p.getInventory().getItemInMainHand().getType().equals(Material.STICK)) {
                stack = p.getInventory().getItemInMainHand();
            } else if (p.getInventory().getItemInOffHand().getType().equals(Material.STICK)) {
                stack = p.getInventory().getItemInOffHand();
            }
                if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (cooldown == 0) {
                        Messenger.sendActionInfo("Preparing to throw your spear...", p);
                        if(stack == null) { return; }
                        stack.setAmount(stack.getAmount() - 1);
                        p.setCooldown(Material.STICK, throwCooldown);
                        BarCooldown.add(uuid, throwDelay);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                Messenger.sendActionInfo("You threw your spear!", p);
                                p.launchProjectile(Arrow.class).setVelocity(p.getLocation().getDirection().multiply(throwVelocity));
                            }
                        }.runTaskLater(Main.plugin, throwDelay);
                    } else {
                        Messenger.sendActionError("You can't throw you spear yet", p);
                    }
                }

        }
    }

    /**
     * Set the thrown spear's damage
     * @param e The event called when an arrow hits a player
     */
    @EventHandler(priority = EventPriority.LOW)
    public void changeSpearDamage(ProjectileHitEvent e) {
        if (e.getEntity() instanceof Arrow) {
            Arrow arrow = (Arrow) e.getEntity();

            if(arrow.getShooter() instanceof Player){
                Player damages = (Player) arrow.getShooter();

                if (Objects.equals(Kit.equippedKits.get(damages.getUniqueId()).name, name)) {
                    arrow.setDamage(throwDamage);
                }
            }
        }
    }

    /**
     * @param e When a player hits a horse, grants bonus damage
     */
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player && e.getEntity() instanceof Horse) {
            if (Objects.equals(Kit.equippedKits.get(e.getDamager().getUniqueId()).name, name)) {
                e.setDamage(e.getDamage() * HORSE_MULTIPLIER);
            }
        }
    }

    /**
     * @return The lore to add to the kit gui item
     */
    @Override
    public ArrayList<Component> getGuiDescription() {
        ArrayList<Component> kitLore = new ArrayList<>();
        kitLore.add(Component.text("A sword and spear wielder, ", NamedTextColor.GRAY));
        kitLore.add(Component.text("can throw a powerful spear", NamedTextColor.GRAY));
        kitLore.addAll(getBaseStats(health, regen, meleeDamage, throwDamage, ladderCount, 1));
        kitLore.add(Component.empty());
        kitLore.add(Component.text("Active:", NamedTextColor.GOLD));
        kitLore.add(Component.text("- Can throw their spear", NamedTextColor.GRAY));
        kitLore.add(Component.empty());
        kitLore.add(Component.text("Passive:", NamedTextColor.GRAY));
        kitLore.add(Component.text("- Deals bonus damage to horses", NamedTextColor.GRAY));
        return kitLore;
    }
}
