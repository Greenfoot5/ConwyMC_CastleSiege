package me.greenfoot5.castlesiege.kits.kits.coin_kits;

import me.greenfoot5.castlesiege.Main;
import me.greenfoot5.castlesiege.database.UpdateStats;
import me.greenfoot5.castlesiege.events.combat.InCombat;
import me.greenfoot5.castlesiege.kits.items.CSItemCreator;
import me.greenfoot5.castlesiege.kits.items.EquipmentSet;
import me.greenfoot5.castlesiege.kits.kits.CoinKit;
import me.greenfoot5.castlesiege.kits.kits.Kit;
import me.greenfoot5.castlesiege.maps.TeamController;
import me.greenfoot5.castlesiege.misc.CSNameTag;
import me.greenfoot5.conwymc.data_types.Tuple;
import me.greenfoot5.conwymc.util.Messenger;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import me.libraryaddict.disguise.disguisetypes.watchers.WolfWatcher;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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

        // Weapon
        es.hotbar[0] = CSItemCreator.weapon(new ItemStack(Material.GHAST_TEAR),
                Component.text("Fangs ", NamedTextColor.GREEN),
                        Arrays.asList(Component.empty(),
                                Component.text("Immobilise your enemies, making them", NamedTextColor.BLUE),
                                Component.text("slow and also you for a short period of time and", NamedTextColor.BLUE),
                                Component.text("you can also slow down horses.", NamedTextColor.BLUE),
                                Component.text("Has a 12 second cooldown.", NamedTextColor.BLUE),
                                Component.empty(),
                                Component.text("20 Melee Damage", NamedTextColor.DARK_GREEN)), null, 20);
        // Voted weapon
        es.votedWeapon = new Tuple<>(
                CSItemCreator.weapon(new ItemStack(Material.GHAST_TEAR),
                        Component.text("Fangs ", NamedTextColor.GREEN).append(Component.text("(Right Click)", NamedTextColor.GRAY)),
                        Arrays.asList(Component.empty(),
                                Component.text("Immobilise your enemies, making them", NamedTextColor.BLUE),
                                Component.text("slow and also you for a short period of time and", NamedTextColor.BLUE),
                                Component.text("you can also slow down horses.", NamedTextColor.BLUE),
                                Component.text("Has a 12 second cooldown.", NamedTextColor.BLUE),
                                Component.empty(),
                                Component.text("22 Melee Damage", NamedTextColor.DARK_GREEN),
								Component.text("⁎ Voted: +2 Melee Damage", NamedTextColor.DARK_AQUA)),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOTING, 0)), 22),
                0);

        super.equipment = es;

        // Perm Potion Effect
        super.potionEffects.add(new PotionEffect(PotionEffectType.RESISTANCE, 999999, 0));
        super.potionEffects.add(new PotionEffect(PotionEffectType.JUMP_BOOST, 999999, 0));
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
        if (e.isCancelled() || !(e.getDamager() instanceof Player q)) {
            return;
        }

        // Warhound bit enemy player
        if (e.getEntity() instanceof Player p && Objects.equals(Kit.equippedKits.get(q.getUniqueId()).name, name)) {
            p.addPotionEffect((new PotionEffect(PotionEffectType.SLOWNESS, 20, 0)));
         }

        //Slows the horse down which the warhound hits.
        if (e.getEntity() instanceof Horse h) {
            Player horseOwner = (Player) h.getOwner();
            assert horseOwner != null;
            if (TeamController.getTeam(horseOwner.getUniqueId()) != TeamController.getTeam(h.getOwner().getUniqueId())) {
                h.addPotionEffect((new PotionEffect(PotionEffectType.SLOWNESS, 20, 1)));
            }
        }
    }

    /**
     *
     * @param e Warhound's new immobilise ability
     */
    @EventHandler
    public void onImmobilise(PlayerInteractEntityEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();
        if (Objects.equals(Kit.equippedKits.get(uuid).name, name)) {
            Player p = e.getPlayer();
            //Stuns the horse which the warhound hits.
            if (e.getRightClicked() instanceof Horse h) {
                Player horseOwner = (Player) h.getOwner();
                assert horseOwner != null;
                if (TeamController.getTeam(horseOwner.getUniqueId()) != TeamController.getTeam(p.getUniqueId())) {
                    immobiliseHorse(h, p);
                }
            }

            //Check if the hit entity is a player, otherwise do nothing.
            if (!(e.getRightClicked() instanceof Player q)) {
                return;
            }

            //The player who is being right-clicked on by the Warhound.

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
            Messenger.sendSuccess("You immobilised " + CSNameTag.mmUsername(p) + ".", q);
            Messenger.sendWarning("You have been immobilised by " + CSNameTag.mmUsername(q) + "!", p);
            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_WOLF_GROWL , 1, 1 );

            // Apply potion effects
            q.addPotionEffect((new PotionEffect(PotionEffectType.SLOWNESS, 80, 1)));
            p.addPotionEffect((new PotionEffect(PotionEffectType.SLOWNESS, 80, 1)));
            p.addPotionEffect((new PotionEffect(PotionEffectType.MINING_FATIGUE, 80, 0)));

            // Prevent movement
            AttributeInstance kb = p.getAttribute(Attribute.KNOCKBACK_RESISTANCE);
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
            q.addPotionEffect((new PotionEffect(PotionEffectType.SLOWNESS, 80, 2)));
            h.addPotionEffect((new PotionEffect(PotionEffectType.SLOWNESS, 100, 3)));

            UpdateStats.addSupports(q.getUniqueId(), 2);

        }
    }

    /**
     * Convert the team's primary wool color to a dye color for the wolf's collar
     * @param p The player for whom to get the collar color
     * @return The dye color corresponding to the team's primary wool color, null if primary wool is no wool
     */
    private DyeColor getCollarColor(Player p) {
        return switch (TeamController.getTeam(p.getUniqueId()).primaryWool) {
            case BLACK_WOOL -> DyeColor.BLACK;
            case BLUE_WOOL -> DyeColor.BLUE;
            case BROWN_WOOL -> DyeColor.BROWN;
            case CYAN_WOOL -> DyeColor.CYAN;
            case GRAY_WOOL -> DyeColor.GRAY;
            case GREEN_WOOL -> DyeColor.GREEN;
            case LIGHT_BLUE_WOOL -> DyeColor.LIGHT_BLUE;
            case LIGHT_GRAY_WOOL -> DyeColor.LIGHT_GRAY;
            case LIME_WOOL -> DyeColor.LIME;
            case MAGENTA_WOOL -> DyeColor.MAGENTA;
            case ORANGE_WOOL -> DyeColor.ORANGE;
            case PURPLE_WOOL -> DyeColor.PURPLE;
            case PINK_WOOL -> DyeColor.PINK;
            case RED_WOOL -> DyeColor.RED;
            case WHITE_WOOL -> DyeColor.WHITE;
            case YELLOW_WOOL -> DyeColor.YELLOW;
            default -> null;
        };
    }

    @Override
    public ArrayList<Component> getGuiDescription() {
        ArrayList<Component> kitLore = new ArrayList<>();
        kitLore.add(Component.text("A quick and ferocious hound.", NamedTextColor.GRAY));
        kitLore.add(Component.text("Hard to hit and bites hard!", NamedTextColor.GRAY));
        kitLore.addAll(getBaseStats(health, regen, meleeDamage, ladderAmount));
        kitLore.add(Component.empty());
        kitLore.add(Component.text("Effects:", NamedTextColor.DARK_PURPLE));
        kitLore.add(Component.text("- Resistance I", NamedTextColor.GRAY));
        kitLore.add(Component.text("- Jump Boost I", NamedTextColor.GRAY));
        kitLore.add(Component.text("- Night Vision I", NamedTextColor.GRAY));
        kitLore.add(Component.text("- Speed III", NamedTextColor.GRAY));
        kitLore.add(Component.empty());
        kitLore.add(Component.text("Active:", NamedTextColor.GOLD));
        kitLore.add(Component.text("- Can bite enemies to briefly immobilise them", NamedTextColor.GRAY));
        kitLore.add(Component.empty());
        kitLore.add(Component.text("Passive:", NamedTextColor.DARK_GREEN));
        kitLore.add(Component.text("- Slows enemies when hitting them", NamedTextColor.GRAY));
        kitLore.add(Component.text("- Can see players' health", NamedTextColor.GRAY));
        kitLore.add(Component.text("- Has a coloured collar to represent team", NamedTextColor.GRAY));
        kitLore.add(Component.text("- Cannot cap flags", NamedTextColor.RED));
        kitLore.add(Component.text("- Cannot climb", NamedTextColor.RED));
        return kitLore;
    }
}
