package me.huntifi.castlesiege.kits.kits.team_kits;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.events.combat.AssistKill;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.events.death.DeathEvent;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.kits.kits.TeamKit;
import me.huntifi.castlesiege.maps.TeamController;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

public class MoriaCaveTroll extends TeamKit implements Listener {

    public final ArrayList<Player> grabbed = new ArrayList<>();

    public MoriaCaveTroll() {
        super("Moria Cave Troll", 450, 16, "Moria",
                "The Orcs", 7500, 10, Material.POPPY);
        super.canClimb = false;
        super.kbResistance = 0.5;

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Weapon
        es.hotbar[0] = ItemCreator.weapon(new ItemStack(Material.WOODEN_SHOVEL),
                ChatColor.GREEN + "Troll Fist", null, null, 40);
        // Voted weapon
        es.votedWeapon = new Tuple<>(
                ItemCreator.weapon(new ItemStack(Material.WOODEN_SHOVEL),
                        ChatColor.GREEN + "Troll Fist",
                        Collections.singletonList(ChatColor.AQUA + "- voted: +2 damage"),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), 42),
                0);

        // Ability
        es.hotbar[1] = ItemCreator.weapon(new ItemStack(Material.DEAD_BRAIN_CORAL_FAN),
                ChatColor.GREEN + "Grab", null, null, 1);

        // Ability
        es.hotbar[2] = ItemCreator.weapon(new ItemStack(Material.DEAD_BUBBLE_CORAL_FAN),
                ChatColor.GREEN + "Throw", null, null, 1);

        super.potionEffects.add(new PotionEffect(PotionEffectType.SLOW_DIGGING, 999999, 2));
        super.potionEffects.add(new PotionEffect(PotionEffectType.NIGHT_VISION, 999999, 0));
        super.potionEffects.add(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 999999, 0));


        // Death Messages
        super.deathMessage[0] = "You were crushed to death by ";
        super.killMessage[0] = " crushed ";
        super.killMessage[1] = " to death";

        super.equipment = es;
    }

    /**
     * Disguise the player as a polar bear
     * @param p The player to (un)disguise
     */
    @Override
    protected void setDisguise(Player p) {
        MobDisguise mobDisguise = new MobDisguise(DisguiseType.IRON_GOLEM);
        mobDisguise.setModifyBoundingBox(true);

        disguise(p, mobDisguise);
    }


    /**
     * Activate the troll ability of throwing a player
     * @param e The event called when right-clicking with a stick
     */
    @EventHandler
    public void throwGrabPlayer(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(uuid)) {
            return;
        }

        ItemStack stick = p.getInventory().getItemInMainHand();
        int cooldown = p.getCooldown(Material.DEAD_BUBBLE_CORAL_FAN);
        int cooldown2 = p.getCooldown(Material.DEAD_BRAIN_CORAL_FAN);
        if (Objects.equals(Kit.equippedKits.get(uuid).name, name)) {
            if (stick.getType().equals(Material.DEAD_BUBBLE_CORAL_FAN)) {
                if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (cooldown == 0) {
                        if (grabbed.size() == 0) { return; }
                        for (Player passengers : grabbed) {
                            throwPlayer(p, passengers);
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                                    ChatColor.AQUA + "You threw the enemy!"));
                        }

                    } else {
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                                ChatColor.DARK_RED + "" + ChatColor.BOLD + "You can't throw a player yet."));
                    }
                }
            } else if (stick.getType().equals(Material.DEAD_BRAIN_CORAL_FAN)) {
                if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (cooldown2 == 0) {
                        if (p.getPassengers().size() > 0) { return;}
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                                ChatColor.AQUA + "You grabbed the enemy!"));
                        for (Player all : Bukkit.getOnlinePlayers()) {
                            if (TeamController.getTeam(p.getUniqueId())
                                    != TeamController.getTeam(all.getUniqueId())
                                    && all.getLocation().distance(p.getLocation()) <= 4.25) {
                                grab(p, all);
                            }
                        }
                    } else {
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                                ChatColor.DARK_RED + "" + ChatColor.BOLD + "You can't grab a player yet."));
                    }
                }
            }
        }
    }

    /**
     * Activate the troll's grab ability
     * @param troll the Cave Troll
     * @param player The grabbed player
     */
    private void grab(Player troll, Player player) {
        if (troll.getCooldown(Material.DEAD_BRAIN_CORAL_FAN) == 0) {
            troll.setCooldown(Material.DEAD_BRAIN_CORAL_FAN, 240);
            troll.addPassenger(player);
            if (!grabbed.contains(player)) { grabbed.add(player); }
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 0));
            player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 150, 3));
        }
    }

    /**
     * Activate the troll's throw ability
     * @param troll the Cave Troll
     * @param player The bitten player
     */
    private void throwPlayer(Player troll, Player player) {
        troll.setCooldown(Material.DEAD_BUBBLE_CORAL_FAN, 120);
        troll.removePassenger(player);
        AssistKill.addDamager(player.getUniqueId(),troll.getUniqueId(), 100);
        player.setVelocity(troll.getLocation().getDirection().multiply(1.3));
        DeathEvent.setKiller(player, troll);

        new BukkitRunnable() {
            @Override
            public void run() {
                grabbed.remove(player);
            }
        }.runTaskLaterAsynchronously(Main.plugin, 20);
    }
}
