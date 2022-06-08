package me.huntifi.castlesiege.kits.kits.teamKits;

import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.events.death.DeathEvent;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.kits.kits.MapKit;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.NameTag;
import me.libraryaddict.disguise.DisguiseConfig;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import me.libraryaddict.disguise.disguisetypes.watchers.AbstractHorseWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.HorseWatcher;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collections;
import java.util.Objects;

public class FirelandsHellsteed extends MapKit implements Listener {
    /**
     * Create a kit with basic settings
     *
     * **/
    public FirelandsHellsteed() {
        super("Hellsteed", 300, 7);
        super.mapSpecificKits.add("Hellsteed");
        super.canCap = false;
        super.canClimb = false;
        super.canSeeHealth = true;
        super.kbResistance = 1;

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Weapon
        es.hotbar[0] = ItemCreator.weapon(new ItemStack(Material.GRAY_DYE),
                ChatColor.RED + "Horseshoe", null, null, 10);
        // Voted weapon
        es.votedWeapon = new Tuple<>(
                ItemCreator.weapon(new ItemStack(Material.GRAY_DYE),
                        ChatColor.RED + "Horseshoe",
                        Collections.singletonList(ChatColor.AQUA + "- voted: +2 damage"),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), 12),
                0);

        // Claws
        es.hotbar[1] = ItemCreator.weapon(new ItemStack(Material.ANVIL),
                ChatColor.RED + "Stomp", null, null, 20);

        super.equipment = es;

        // Perm Potion Effect
        super.potionEffects.add(new PotionEffect(PotionEffectType.SPEED, 999999, 5));

        // Death Messages
        super.deathMessage[0] = "You were trampled by ";
        super.killMessage[0] = "You trampled ";
        super.killMessage[1] = " to death";

    }


    /**
     * Disguise the player as a Hell steed
     * @param p The player to (un)disguise
     */
    @Override
    protected void disguise(Player p) {

        ItemStack horseArmor = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_HORSE_ARMOR),
                ChatColor.GREEN + "Leather Armor", null, null,
                Color.fromRGB(40, 2, 2));

        MobDisguise mobDisguise = new MobDisguise(DisguiseType.HORSE);

        mobDisguise.getWatcher().setCustomName(NameTag.color(p) + p.getName());
        mobDisguise.setCustomDisguiseName(true);
        mobDisguise.setHearSelfDisguise(true);
        mobDisguise.setSelfDisguiseVisible(false);
        mobDisguise.setNotifyBar(DisguiseConfig.NotifyBar.NONE);

        HorseWatcher horseWatcher = (HorseWatcher) mobDisguise.getWatcher();
        horseWatcher.setSaddled(true);
        horseWatcher.setHorseArmor(horseArmor);
        horseWatcher.setColor(Horse.Color.BLACK);
        horseWatcher.setStyle(Horse.Style.BLACK_DOTS);
        horseWatcher.setTamed(true);

        mobDisguise.setEntity(p);
        mobDisguise.startDisguise();
        NameTag.give(p);
    }



    /**
     * Activate the Hellsteed stomp ability
     * @param e The event called when hitting another player
     */
    @EventHandler
    public void onStomp(EntityDamageByEntityEvent e) {
        if (e.isCancelled()) {
            return;
        }

        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            Player p = (Player) e.getEntity();
            Player q = (Player) e.getDamager();

            // Hellsteed tries to stomp every closeby enemy
            if (Objects.equals(Kit.equippedKits.get(q.getUniqueId()).name, name) &&
                    MapController.getCurrentMap().getTeam(q.getUniqueId())
                            != MapController.getCurrentMap().getTeam(p.getUniqueId()) &&
                    q.getInventory().getItemInMainHand().getType() == Material.ANVIL &&
                    q.getCooldown(Material.ANVIL) == 0) {
                q.setCooldown(Material.ANVIL, 400);

                // Enemy blocks stun
                if (p.isBlocking()) {
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                            ChatColor.AQUA + "You blocked " + NameTag.color(q) + q.getName() + ChatColor.AQUA + "'s stomp"));
                } else {
                    p.getWorld().playSound(p.getLocation(), Sound.ENTITY_HORSE_ANGRY , 1, (float) 0.8);
                    e.setDamage(e.getDamage() * 1.5);
                    p.addPotionEffect((new PotionEffect(PotionEffectType.BLINDNESS, 50, 1)));
                    p.addPotionEffect((new PotionEffect(PotionEffectType.SLOW, 50, 2)));
                    p.addPotionEffect((new PotionEffect(PotionEffectType.SLOW_DIGGING, 50, 4)));
                    for (Player all : Bukkit.getOnlinePlayers()) {
                        if (p.getWorld() != all.getWorld()) { return; }
                        if (all == p) { return; }
                        if (all == q) { return; }
                        if (all.getLocation().distance(p.getLocation()) < 2.1) {
                            if ((all.getHealth() - e.getDamage() > 0)) {
                                all.damage(e.getDamage());
                                //Players that weren't the one directly hit are affected less
                                all.addPotionEffect((new PotionEffect(PotionEffectType.BLINDNESS, 30, 1)));
                                all.addPotionEffect((new PotionEffect(PotionEffectType.SLOW, 40, 1)));
                                all.addPotionEffect((new PotionEffect(PotionEffectType.SLOW_DIGGING, 40, 3)));
                            } else {
                                e.setCancelled(true);
                                DeathEvent.setKiller(all, p);
                                all.setHealth(0);
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteractAtEntity(PlayerInteractEntityEvent event) {

        if (event.getRightClicked() instanceof Player) {

            Player p = event.getPlayer();
            Player clicked = (Player) event.getRightClicked();

            if (Objects.equals(Kit.equippedKits.get(clicked.getUniqueId()).name, name)
                    && MapController.getCurrentMap().getTeam(clicked.getUniqueId())
                    != MapController.getCurrentMap().getTeam(p.getUniqueId())) {

                clicked.addPassenger(p);

            }
        }
    }

}
