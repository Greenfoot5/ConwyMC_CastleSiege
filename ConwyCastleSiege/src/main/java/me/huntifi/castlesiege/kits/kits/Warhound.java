package me.huntifi.castlesiege.kits.kits;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.NameTag;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import me.libraryaddict.disguise.disguisetypes.watchers.WolfWatcher;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Objects;

/**
 * The warhound kit
 */
public class Warhound extends Kit implements Listener, CommandExecutor {

    /**
     * Set the equipment and attributes of this kit
     */
    public Warhound() {
        super("Warhound", 110);

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Weapon
        es.hotbar[0] = ItemCreator.item(new ItemStack(Material.GHAST_TEAR),
                ChatColor.RED + "Fangs", null,
                Collections.singletonList(new Tuple<>(Enchantment.DAMAGE_ALL, 5)));
        // Voted weapon
        es.votedWeapon = new Tuple<>(
                ItemCreator.item(new ItemStack(Material.GHAST_TEAR),
                        ChatColor.RED + "Fangs",
                        Collections.singletonList(ChatColor.AQUA + "- voted: +2 damage"),
                        Collections.singletonList(new Tuple<>(Enchantment.DAMAGE_ALL, 7))),
                0);

        super.equipment = es;

        // Perm Potion Effect
        super.potionEffects.add(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 999999, 0));
        super.potionEffects.add(new PotionEffect(PotionEffectType.JUMP, 999999, 0));
        super.potionEffects.add(new PotionEffect(PotionEffectType.NIGHT_VISION, 999999, 0));
        super.potionEffects.add(new PotionEffect(PotionEffectType.SPEED, 999999, 3));

        // Death Messages
        super.deathMessage[0] = "You were bitten to death by ";
        super.killMessage[0] = "You bit ";
        super.killMessage[1] = " to death";
    }

    /**
     * Register the player as using this kit and set their items
     * @param commandSender Source of the command
     * @param command Command which was executed
     * @param s Alias of the command which was used
     * @param strings Passed command arguments
     * @return true
     */
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof ConsoleCommandSender) {
            commandSender.sendMessage("Console cannot select kits!");
            return true;
        }

        super.addPlayer(((Player) commandSender).getUniqueId());
        return true;
    }

    /**
     * Disguise the player as a wolf
     * @param p The player to (un)disguise
     */
    @Override
    protected void disguise(Player p) {
        MobDisguise mobDisguise = new MobDisguise(DisguiseType.WOLF);
        WolfWatcher wolfWatcher = (WolfWatcher) mobDisguise.getWatcher();

        wolfWatcher.setCollarColor(getCollarColor(p));
        wolfWatcher.setTamed(true);

        mobDisguise.getWatcher().setCustomName(NameTag.color(p) + p.getName());
        mobDisguise.setCustomDisguiseName(true);
        mobDisguise.setHearSelfDisguise(true);
        mobDisguise.setSelfDisguiseVisible(false);

        mobDisguise.setEntity(p);
        mobDisguise.startDisguise();
    }

    /**
     * Prevent the warhound from climbing up ladders
     * @param e The event called when moving on a ladder
     */
    @EventHandler
    public void onClimb(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (Objects.equals(Kit.equippedKits.get(p.getUniqueId()).name, name) &&
                p.getLocation().getBlock().getType() == Material.LADDER &&
                e.getTo() != null && e.getTo().getY() - e.getFrom().getY() > 0) {
            e.setCancelled(true);
        }
    }

    /**
     * Cause withering damage to the bitten opponent and try to activate the stun ability
     * @param e The event called when hitting another player
     */
    @EventHandler
    public void onBite(EntityDamageByEntityEvent e) {
        if (e.isCancelled() ||
                !(e.getEntity() instanceof Player && e.getDamager() instanceof Player)) {
            return;
        }
        Player p = (Player) e.getEntity();
        Player q = (Player) e.getDamager();

        // Warhound bit enemy player
        if (Objects.equals(Kit.equippedKits.get(q.getUniqueId()).name, name)) {
            p.addPotionEffect((new PotionEffect(PotionEffectType.WITHER, 25, 5)));
            stun(p, q);
        }
    }

    /**
     * Activate the warhound's stun ability, immobilizing the opponent and slowing the warhound
     * @param p The opponent
     * @param q The warhound
     */
    private void stun(Player p, Player q) {
        if (q.getInventory().getItemInMainHand().getType() == Material.GHAST_TEAR &&
                q.getCooldown(Material.GHAST_TEAR) == 0) {

            // Activate stun
            q.setCooldown(Material.GHAST_TEAR, 280);
            q.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                    ChatColor.AQUA + "You immobilised " + NameTag.color(p) + p.getName() + ChatColor.AQUA + "."));
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                    ChatColor.DARK_RED + "You have been immobilised by " + NameTag.color(q) + q.getName() + ChatColor.DARK_RED + "!"));
            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_WOLF_GROWL , 1, 1 );

            // Apply potion effects
            q.addPotionEffect((new PotionEffect(PotionEffectType.SLOW, 80, 1)));
            p.addPotionEffect((new PotionEffect(PotionEffectType.SLOW, 80, 1)));
            p.addPotionEffect((new PotionEffect(PotionEffectType.SLOW_DIGGING, 80, 0)));

            // Prevent movement
            AttributeInstance kb = p.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE);
            assert kb != null;
            double kbValue = kb.getBaseValue();
            float walkSpeed = p.getWalkSpeed();

            kb.setBaseValue(2);
            p.setWalkSpeed(0);

            new BukkitRunnable() {
                @Override
                public void run() {
                    kb.setBaseValue(kbValue);
                    p.setWalkSpeed(walkSpeed);
                }
            }.runTaskLater(Main.plugin, 80);
        }
    }

    /**
     * Convert the team's primary wool color to a dye color for the wolf's collar
     * @param p The player for whom to get the collar color
     * @return The dye color corresponding to the team's primary wool color, null if primary wool is no wool
     */
    private DyeColor getCollarColor(Player p) {
        switch (MapController.getCurrentMap().getTeam(p.getUniqueId()).primaryWool) {
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
}
