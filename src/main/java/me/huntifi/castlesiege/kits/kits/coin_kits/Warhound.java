package me.huntifi.castlesiege.kits.kits.coin_kits;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.database.UpdateStats;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.kits.kits.CoinKit;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.maps.NameTag;
import me.huntifi.castlesiege.maps.TeamController;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import me.libraryaddict.disguise.disguisetypes.watchers.WolfWatcher;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

/**
 * The warhound kit
 */
public class Warhound extends CoinKit implements Listener {

    private static final int health = 200;
    private static final double regen = 10.5;
    private static final double meleeDamage = 57;
    private static final int ladderAmount = 4;

    /**
     * Set the equipment and attributes of this kit
     */
    public Warhound() {
        super("Warhound", 200, 20, Material.GHAST_TEAR);
        super.canCap = false;
        super.canClimb = false;
        super.canSeeHealth = true;

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Weapon
        es.hotbar[0] = ItemCreator.weapon(new ItemStack(Material.GHAST_TEAR),
                ChatColor.RED + "Fangs " + ChatColor.GRAY + "(Right Click)", Arrays.asList("",
                        ChatColor.GRAY + "Warhound main ability",
                        ChatColor.WHITE + "",
                        ChatColor.WHITE + "Description:",
                        ChatColor.RED + "Immobilise your enemies, making them",
                        ChatColor.RED + "slow and also you for a short period of time and",
                        ChatColor.RED + "you can also slow down horses.",
                        ChatColor.RED + "Has a 12 second cooldown."), null, 20);
        // Voted weapon
        es.votedWeapon = new Tuple<>(
                ItemCreator.weapon(new ItemStack(Material.GHAST_TEAR),
                        ChatColor.RED + "Fangs" + ChatColor.GRAY + "(Right Click)",
                        Arrays.asList("",
                                ChatColor.GRAY + "Warhound main ability",
                                ChatColor.WHITE + "",
                                ChatColor.WHITE + "Description:",
                                ChatColor.RED + "Immobilise your enemies, making them",
                                ChatColor.RED + "slow and also you for a short period of time and",
                                ChatColor.RED + "you can also slow down horses.",
                                ChatColor.RED + "Has a 12 second cooldown.",
                                ChatColor.AQUA + "+2 damage"),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), 30),
                0);

        super.equipment = es;

        // Perm Potion Effect
        super.potionEffects.add(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 999999, 0));
        super.potionEffects.add(new PotionEffect(PotionEffectType.JUMP, 999999, 0));
        super.potionEffects.add(new PotionEffect(PotionEffectType.NIGHT_VISION, 999999, 0));
        super.potionEffects.add(new PotionEffect(PotionEffectType.SPEED, 999999, 3));

        // Death Messages
        super.deathMessage[0] = "You were bitten to death by ";
        super.killMessage[0] = " bit ";
        super.killMessage[1] = " to death";
    }

    /**
     * Disguise the player as a wolf
     * @param p The player to (un)disguise
     */
    @Override
    protected void setDisguise(Player p) {
        MobDisguise mobDisguise = new MobDisguise(DisguiseType.WOLF);
        WolfWatcher wolfWatcher = (WolfWatcher) mobDisguise.getWatcher();

        wolfWatcher.setCollarColor(getCollarColor(p));
        wolfWatcher.setTamed(true);

        disguise(p, mobDisguise);
    }

    /**
     * Cause slowness to the bitten enemy
     * @param e The event called when hitting another player
     */
    @EventHandler
    public void onBite(EntityDamageByEntityEvent e) {
        if (e.isCancelled() || !(e.getDamager() instanceof Player)) {
            return;
        }

        Player q = (Player) e.getDamager();

        // Warhound bit enemy player
        if (e.getEntity() instanceof Player && Objects.equals(Kit.equippedKits.get(q.getUniqueId()).name, name)) {
            Player p = (Player) e.getEntity();
            p.addPotionEffect((new PotionEffect(PotionEffectType.SLOW, 20, 0)));
         }

        //Slows the horse down which the warhound hits.
        if (e.getEntity() instanceof Horse) {
            Horse h = (Horse) e.getEntity();
            Player horseOwner = (Player) h.getOwner();
            assert horseOwner != null;
            if (TeamController.getTeam(horseOwner.getUniqueId()) != TeamController.getTeam(h.getOwner().getUniqueId())) {
                h.addPotionEffect((new PotionEffect(PotionEffectType.SLOW, 20, 1)));
            }
        }
    }

    /**
     *
     * @param e Warhound's new immobilise ability
     */
    @EventHandler
    public void onImmobilise(PlayerInteractEntityEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();

        if (Objects.equals(Kit.equippedKits.get(uuid).name, name)) {

            //Stuns the horse which the warhound hits.
            if (e.getRightClicked() instanceof Horse) {
                Horse h = (Horse) e.getRightClicked();
                Player horseOwner = (Player) h.getOwner();
                assert horseOwner != null;
                if (TeamController.getTeam(horseOwner.getUniqueId()) != TeamController.getTeam(p.getUniqueId())) {
                    immobiliseHorse(h, p);
                }
            }

            //Check if the hit entity is a player, otherwise do nothing.
            if (!(e.getRightClicked() instanceof Player)) {
                return;
            }

            //The player who is being right-clicked on by the Warhound.
            Player q = (Player) e.getRightClicked();

            // Prevent using in lobby
            if (InCombat.isPlayerInLobby(uuid)) {
                return;
            }

            //Check if the players are not on the same team, if true then perform the stun.
            if (TeamController.getTeam(q.getUniqueId()) != TeamController.getTeam(p.getUniqueId())) {
                immobilise(q, p);
            }

        }
    }

    /**
     * Activate the warhound's stun ability, immobilizing the opponent and slowing the warhound
     * @param p The opponent
     * @param q The warhound
     */
    private void immobilise(Player p, Player q) {
        if (q.getInventory().getItemInMainHand().getType() == Material.GHAST_TEAR &&
                q.getCooldown(Material.GHAST_TEAR) == 0) {

            // Activate stun
            q.setCooldown(Material.GHAST_TEAR, 240);
            Messenger.sendSuccess("You immobilised " + NameTag.mmUsername(p) + ".", q);
            Messenger.sendWarning("You have been immobilised by " + NameTag.mmUsername(q) + "!", p);
            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_WOLF_GROWL , 1, 1 );

            // Apply potion effects
            q.addPotionEffect((new PotionEffect(PotionEffectType.SLOW, 80, 1)));
            p.addPotionEffect((new PotionEffect(PotionEffectType.SLOW, 80, 1)));
            p.addPotionEffect((new PotionEffect(PotionEffectType.SLOW_DIGGING, 80, 0)));

            // Prevent movement
            AttributeInstance kb = p.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE);
            assert kb != null;
            kb.setBaseValue(2);
            p.setWalkSpeed(0);

            new BukkitRunnable() {
                @Override
                public void run() {
                    kb.setBaseValue(Kit.equippedKits.get(p.getUniqueId()).kbResistance);
                    p.setWalkSpeed(0.2f);
                }
            }.runTaskLater(Main.plugin, 80);

            UpdateStats.addSupports(q.getUniqueId(), 2);
        }
    }

    /**
     * Activate the warhound's stun ability, immobilizing the opponent and slowing the warhound
     * @param h The opponent's horse
     * @param q The warhound
     */
    private void immobiliseHorse(Horse h, Player q) {
        if (q.getInventory().getItemInMainHand().getType() == Material.GHAST_TEAR &&
                q.getCooldown(Material.GHAST_TEAR) == 0) {

            // Activate stun
            q.setCooldown(Material.GHAST_TEAR, 240);
            Messenger.sendActionSuccess("You immobilised <aqua>" + Objects.requireNonNull(h.getOwner()) + "</aqua>'s horse.", q);
            h.getWorld().playSound(h.getLocation(), Sound.ENTITY_WOLF_GROWL , 1, 1 );

            // Apply potion effects
            q.addPotionEffect((new PotionEffect(PotionEffectType.SLOW, 80, 2)));
            h.addPotionEffect((new PotionEffect(PotionEffectType.SLOW, 100, 3)));

            UpdateStats.addSupports(q.getUniqueId(), 2);

        }
    }

    /**
     * Convert the team's primary wool color to a dye color for the wolf's collar
     * @param p The player for whom to get the collar color
     * @return The dye color corresponding to the team's primary wool color, null if primary wool is no wool
     */
    private DyeColor getCollarColor(Player p) {
        switch (TeamController.getTeam(p.getUniqueId()).primaryWool) {
            case BLACK_WOOL:
                return DyeColor.BLACK;
            case BLUE_WOOL:
                return DyeColor.BLUE;
            case BROWN_WOOL:
                return DyeColor.BROWN;
            case CYAN_WOOL:
                return DyeColor.CYAN;
            case GRAY_WOOL:
                return DyeColor.GRAY;
            case GREEN_WOOL:
                return DyeColor.GREEN;
            case LIGHT_BLUE_WOOL:
                return DyeColor.LIGHT_BLUE;
            case LIGHT_GRAY_WOOL:
                return DyeColor.LIGHT_GRAY;
            case LIME_WOOL:
                return DyeColor.LIME;
            case MAGENTA_WOOL:
                return DyeColor.MAGENTA;
            case ORANGE_WOOL:
                return DyeColor.ORANGE;
            case PURPLE_WOOL:
                return DyeColor.PURPLE;
            case PINK_WOOL:
                return DyeColor.PINK;
            case RED_WOOL:
                return DyeColor.RED;
            case WHITE_WOOL:
                return DyeColor.WHITE;
            case YELLOW_WOOL:
                return DyeColor.YELLOW;
            default:
                return null;
        }
    }

    @Override
    public ArrayList<Component> getGuiDescription() {
        ArrayList<Component> kitLore = new ArrayList<>();
        kitLore.add(Component.text("A quick a ferocious hound. Hard to hit and bites hard", NamedTextColor.GRAY));
        kitLore.addAll(getBaseStats(health, regen, meleeDamage, ladderAmount));
        kitLore.add(Component.text(" "));
        kitLore.add(Component.text("Effects:", NamedTextColor.DARK_PURPLE));
        kitLore.add(Component.text("- Resistance I", NamedTextColor.GRAY));
        kitLore.add(Component.text("- Jump Boost I", NamedTextColor.GRAY));
        kitLore.add(Component.text("- Night Vision I", NamedTextColor.GRAY));
        kitLore.add(Component.text("- Speed III", NamedTextColor.GRAY));
        kitLore.add(Component.text(" "));
        kitLore.add(Component.text("Active:", NamedTextColor.GOLD));
        kitLore.add(Component.text("- Can bite enemies to briefly immobilise them", NamedTextColor.GRAY));
        kitLore.add(Component.text(" "));
        kitLore.add(Component.text("Passive:", NamedTextColor.DARK_GREEN));
        kitLore.add(Component.text("- Slows enemies when hitting them", NamedTextColor.GRAY));
        kitLore.add(Component.text("- Can see players' health", NamedTextColor.GRAY));
        kitLore.add(Component.text("- Has a coloured collar to represent team", NamedTextColor.GRAY));
        kitLore.add(Component.text("- Cannot cap flags", NamedTextColor.RED));
        kitLore.add(Component.text("- Cannot climb", NamedTextColor.RED));
        return kitLore;
    }
}
