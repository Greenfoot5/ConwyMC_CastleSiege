package me.greenfoot5.castlesiege.kits.kits.sign_kits;

import me.greenfoot5.castlesiege.events.combat.InCombat;
import me.greenfoot5.castlesiege.kits.items.CSItemCreator;
import me.greenfoot5.castlesiege.kits.items.EquipmentSet;
import me.greenfoot5.castlesiege.kits.kits.SignKit;
import me.greenfoot5.castlesiege.maps.TeamController;
import me.greenfoot5.castlesiege.misc.CSNameTag;
import me.greenfoot5.conwymc.data_types.Tuple;
import me.greenfoot5.conwymc.util.Messenger;
import me.libraryaddict.disguise.DisguiseConfig;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import me.libraryaddict.disguise.disguisetypes.watchers.HorseWatcher;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
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

/**
 * Hellsteed kit
 */
public class Hellsteed extends SignKit implements Listener {

    /**
     * Creates a new Hellsteed
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
                        Collections.singletonList(Component.text("⁎ Voted: +2 damage", NamedTextColor.AQUA)),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOTING, 1)), 22),
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
        super.potionEffects.add(new PotionEffect(PotionEffectType.JUMP_BOOST, 999999, 1));

        // Death Messages
        super.deathMessage[0] = "You were trampled by ";
        super.killMessage[0] = " trampled ";
        super.killMessage[1] = " to death";

    }


    /**
     * Disguise the player as a Hell steed
     */
    @Override
    protected void setDisguise() {

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

        disguise(mobDisguise);
    }



    /**
     * Activate the Hellsteed stomp ability
     * @param e The event called when hitting another player
     */
    @EventHandler(ignoreCancelled = true)
    public void onStomp(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player hit) || (e.getDamager() != equippedPlayer)) {
            return;
        }

        // Hellsteed tries to stomp every nearby enemy
        if (equippedPlayer.getInventory().getItemInMainHand().getType() != Material.ANVIL
                || equippedPlayer.getCooldown(Material.ANVIL) != 0) {
            return;
        }
        equippedPlayer.setCooldown(Material.ANVIL, 400);

        // Enemy blocks stun
        if (hit.isBlocking()) {
            Messenger.sendSuccess("You blocked " + CSNameTag.username(equippedPlayer) + "'s stomp", hit);
        } else {
            equippedPlayer.getWorld().playSound(equippedPlayer.getLocation(), Sound.ENTITY_HORSE_ANGRY , 1, (float) 0.8);
            e.setDamage(e.getDamage() * 1.5);
            equippedPlayer.addPotionEffect((new PotionEffect(PotionEffectType.BLINDNESS, 50, 1)));
            equippedPlayer.addPotionEffect((new PotionEffect(PotionEffectType.SLOWNESS, 50, 2)));
            equippedPlayer.addPotionEffect((new PotionEffect(PotionEffectType.MINING_FATIGUE, 50, 4)));
            for (Entity all : equippedPlayer.getNearbyEntities(2.1, 2.1, 2.1)) {
                if (!(all instanceof Player p) || !TeamController.isPlaying(p)
                        || all == hit || all == equippedPlayer)
                    continue;
                p.damage(e.getDamage(), equippedPlayer);
                // Players that weren't the one directly hit are affected less
                p.addPotionEffect((new PotionEffect(PotionEffectType.BLINDNESS, 30, 1)));
                p.addPotionEffect((new PotionEffect(PotionEffectType.SLOWNESS, 40, 1)));
                p.addPotionEffect((new PotionEffect(PotionEffectType.MINING_FATIGUE, 40, 3)));
            }
        }
    }

    /**
     * @param event When a player attempts to ride the hellsteed
     */
    @EventHandler
    public void onPlayerInteractAtEntity(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() != equippedPlayer)
            return;

        Player p = event.getPlayer();

        if (InCombat.isPlayerInLobby(p.getUniqueId())) {
            return;
        }

        if (TeamController.getTeam(equippedPlayer.getUniqueId()) == TeamController.getTeam(p.getUniqueId())) {
            equippedPlayer.addPassenger(p);
        }
    }


    /**
     * @param e When a player dismounts the hellsteed
     */
    @EventHandler
    public void onEject(PlayerInteractEvent e) {
        if (e.getPlayer() != equippedPlayer)
            return;

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(equippedPlayer.getUniqueId())) {
            return;
        }

        if (e.getItem() == null || e.getItem().getType() != Material.BARRIER) {
            return;
        }

        int cooldown = equippedPlayer.getCooldown(Material.BARRIER);
        if (cooldown == 0) {
            equippedPlayer.getPassengers();
            equippedPlayer.setCooldown(Material.BARRIER, 20);
            equippedPlayer.eject();
        }
    }

    @Override
    public ArrayList<Component> getGuiDescription() {
        ArrayList<Component> description = new ArrayList<>();
        description.add(Component.text("//TODO - Add kit description", NamedTextColor.GRAY));
        return description;
    }
}
