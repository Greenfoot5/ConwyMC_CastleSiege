package me.huntifi.castlesiege.kits.kits.sign_kits;

import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.kits.items.CSItemCreator;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.kits.kits.SignKit;
import me.huntifi.castlesiege.maps.TeamController;
import me.huntifi.castlesiege.misc.CSNameTag;
import me.huntifi.conwymc.data_types.Tuple;
import me.huntifi.conwymc.util.Messenger;
import me.libraryaddict.disguise.DisguiseConfig;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import me.libraryaddict.disguise.disguisetypes.watchers.HorseWatcher;
import net.citizensnpcs.api.npc.NPC;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

public class Hellsteed extends SignKit implements Listener {

    /**
     * Creates a new Firelands Hellsteed
     */
    public Hellsteed() {
        super("Hellsteed", 500, 10, Material.LEATHER_HORSE_ARMOR, 2500);
        super.canCap = false;
        super.canClimb = false;
        super.canSeeHealth = true;
        super.kbResistance = 0.5;

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();

        // Weapon
        es.hotbar[0] = CSItemCreator.weapon(new ItemStack(Material.GRAY_DYE),
                Component.text("Horseshoe", NamedTextColor.RED), null, null, 20);
        // Voted weapon
        es.votedWeapon = new Tuple<>(
                CSItemCreator.weapon(new ItemStack(Material.GRAY_DYE),
                        Component.text("Horseshoe", NamedTextColor.RED),
                        Collections.singletonList(Component.text("- voted: +2 damage", NamedTextColor.AQUA)),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), 22),
                0);

        // stomp
        es.hotbar[1] = CSItemCreator.weapon(new ItemStack(Material.ANVIL),
                Component.text("Stomp", NamedTextColor.GREEN), null, null, 20);

        // stomp
        es.hotbar[7] = CSItemCreator.weapon(new ItemStack(Material.BARRIER),
                Component.text("Eject", NamedTextColor.RED), null, null, 0);


        super.equipment = es;

        // Perm Potion Effect
        super.potionEffects.add(new PotionEffect(PotionEffectType.SPEED, 999999, 5));
        super.potionEffects.add(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 999999, 0));
        super.potionEffects.add(new PotionEffect(PotionEffectType.JUMP, 999999, 1));

        // Death Messages
        super.deathMessage[0] = "You were trampled by ";
        super.killMessage[0] = " trampled ";
        super.killMessage[1] = " to death";

    }


    /**
     * Disguise the player as a Hell steed
     * @param p The player to (un)disguise
     */
    @Override
    protected void setDisguise(Player p) {

        ItemStack horseArmor = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_HORSE_ARMOR),
                Component.text("Leather Armor", NamedTextColor.GREEN), null, null,
                Color.fromRGB(40, 2, 2));

        MobDisguise mobDisguise = new MobDisguise(DisguiseType.HORSE);

        mobDisguise.setNotifyBar(DisguiseConfig.NotifyBar.NONE);

        HorseWatcher horseWatcher = (HorseWatcher) mobDisguise.getWatcher();
        horseWatcher.setSaddled(true);
        horseWatcher.setHorseArmor(horseArmor);
        horseWatcher.setColor(Horse.Color.BLACK);
        horseWatcher.setStyle(Horse.Style.BLACK_DOTS);
        horseWatcher.setTamed(true);

        disguise(p, mobDisguise);
    }



    /**
     * Activate the Hellsteed stomp ability
     * @param e The event called when hitting another player
     */
    @EventHandler(ignoreCancelled = true)
    public void onStomp(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player) || !(e.getDamager() instanceof Player)) {
            return;
        }
        Player p = (Player) e.getEntity();
        Player q = (Player) e.getDamager();

        // Hellsteed tries to stomp every nearby enemy
        if (!Objects.equals(Kit.equippedKits.get(q.getUniqueId()).name, name) ||
                q.getInventory().getItemInMainHand().getType() != Material.ANVIL ||
                q.getCooldown(Material.ANVIL) != 0) {
            return;
        }
        q.setCooldown(Material.ANVIL, 400);

        // Enemy blocks stun
        if (p.isBlocking()) {
            Messenger.sendSuccess("You blocked " + CSNameTag.username(q) + "'s stomp", p);
        } else {
            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_HORSE_ANGRY , 1, (float) 0.8);
            e.setDamage(e.getDamage() * 1.5);
            p.addPotionEffect((new PotionEffect(PotionEffectType.BLINDNESS, 50, 1)));
            p.addPotionEffect((new PotionEffect(PotionEffectType.SLOW, 50, 2)));
            p.addPotionEffect((new PotionEffect(PotionEffectType.SLOW_DIGGING, 50, 4)));
            for (Player all : Bukkit.getOnlinePlayers()) {
                if (p.getWorld() != all.getWorld() || all == p || all == q)
                    return;
                if (all.getLocation().distance(p.getLocation()) < 2.1) {
                    all.damage(e.getDamage(), p);
                    // Players that weren't the one directly hit are affected less
                    all.addPotionEffect((new PotionEffect(PotionEffectType.BLINDNESS, 30, 1)));
                    all.addPotionEffect((new PotionEffect(PotionEffectType.SLOW, 40, 1)));
                    all.addPotionEffect((new PotionEffect(PotionEffectType.SLOW_DIGGING, 40, 3)));
                }
            }
        }
    }

    /**
     * @param event When a player attempts to ride the hellsteed
     */
    @EventHandler
    public void onPlayerInteractAtEntity(PlayerInteractEntityEvent event) {

        if (!(event.getRightClicked() instanceof Player) || (event.getRightClicked() instanceof NPC)) {
            return;
        }
        Player p = event.getPlayer();
        Player clicked = (Player) event.getRightClicked();

        if (InCombat.isPlayerInLobby(p.getUniqueId())) {
            return;
        }

        if (Kit.equippedKits.get(clicked.getUniqueId()).name == null) {
            return;
        }
        if (Objects.equals(Kit.equippedKits.get(clicked.getUniqueId()).name, name)
                && TeamController.getTeam(clicked.getUniqueId())
                == TeamController.getTeam(p.getUniqueId())) {

            clicked.addPassenger(p);
        }
    }


    /**
     * @param e When a player dismounts the hellsteed
     */
    @EventHandler
    public void onEject(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(uuid)) {
            return;
        }

        if (!Objects.equals(Kit.equippedKits.get(uuid).name, name) ||
                e.getItem() == null || e.getItem().getType() != Material.BARRIER) {
            return;
        }
        int cooldown = p.getCooldown(Material.BARRIER);
        if (cooldown == 0) {

            p.getPassengers();
            p.setCooldown(Material.BARRIER, 20);
            p.eject();
        }
    }

    @Override
    public ArrayList<Component> getGuiDescription() {
        ArrayList<Component> description = new ArrayList<>();
        description.add(Component.text("//TODO - Add kit description", NamedTextColor.GRAY));
        return description;
    }
}
