package me.greenfoot5.castlesiege.kits.kits.sign_kits;

import me.greenfoot5.castlesiege.Main;
import me.greenfoot5.castlesiege.database.UpdateStats;
import me.greenfoot5.castlesiege.events.combat.InCombat;
import me.greenfoot5.castlesiege.kits.items.CSItemCreator;
import me.greenfoot5.castlesiege.kits.items.EquipmentSet;
import me.greenfoot5.castlesiege.kits.kits.Kit;
import me.greenfoot5.castlesiege.kits.kits.SignKit;
import me.greenfoot5.castlesiege.maps.TeamController;
import me.greenfoot5.castlesiege.misc.CSNameTag;
import me.greenfoot5.conwymc.data_types.Tuple;
import me.greenfoot5.conwymc.util.Messenger;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

public class CaveTroll extends SignKit implements Listener {

    /**
     * Creates a new Moria Cave Troll
     */
    public CaveTroll() {
        super("Cave Troll", 600, 20, Material.POPPY, 2000);
        super.canClimb = false;
        super.kbResistance = 0.8;

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();

        // Weapon
        es.hotbar[0] = CSItemCreator.weapon(new ItemStack(Material.STONE_SHOVEL),
                Component.text("Troll Fist", NamedTextColor.GREEN), null, null, 43);
        // Voted weapon
        es.votedWeapon = new Tuple<>(
                CSItemCreator.weapon(new ItemStack(Material.STONE_SHOVEL),
                        Component.text("Troll Fist", NamedTextColor.GREEN),
                        Collections.singletonList(Component.text("⁎ Voted: +2 damage", NamedTextColor.AQUA)),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOTING, 0)), 45),
                0);

        // Ability
        es.hotbar[1] = CSItemCreator.weapon(new ItemStack(Material.IRON_INGOT),
                Component.text("Stomp", NamedTextColor.GREEN), null, null, 1);

        super.potionEffects.add(new PotionEffect(PotionEffectType.MINING_FATIGUE, 999999, 2));
        super.potionEffects.add(new PotionEffect(PotionEffectType.NIGHT_VISION, 999999, 0));
        super.potionEffects.add(new PotionEffect(PotionEffectType.RESISTANCE, 999999, 0));


        // Death Messages
        super.deathMessage[0] = "You were crushed to death by ";
        super.killMessage[0] = " crushed ";
        super.killMessage[1] = " to death";

        super.equipment = es;
    }

    /**
     * Disguise the player as a polar bear
     */
    @Override
    protected void setDisguise() {
        MobDisguise mobDisguise = new MobDisguise(DisguiseType.IRON_GOLEM);
        mobDisguise.setModifyBoundingBox(true);

        disguise(mobDisguise);
    }


    /**
     * Activate the troll ability of throwing a player
     * @param e The event called when right-clicking with a stick
     */
    @EventHandler
    public void massiveStomp(PlayerInteractEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();
        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(uuid)) {
            return;
        }
        if (Objects.equals(Kit.equippedKits.get(uuid).name, name)) {
            Player p = e.getPlayer();
            ItemStack main = p.getInventory().getItemInMainHand();
            if (main.getType().equals(Material.IRON_INGOT)) {
                if ((e.getAction() != Action.RIGHT_CLICK_AIR)) {
                    return;
                }
                    int cooldown = p.getCooldown(Material.IRON_INGOT);
                    if (cooldown == 0) {
                        for (Player online : Bukkit.getOnlinePlayers()) {
                            if (TeamController.isPlaying(online) && p.getWorld() == online.getWorld() && online != p) {
                                if (online.getLocation().distanceSquared(p.getLocation()) < 3.5 * 3.5
                                        && TeamController.getTeam(online.getUniqueId())
                                        != TeamController.getTeam(p.getUniqueId())) {

                                    online.addPotionEffect((new PotionEffect(PotionEffectType.SLOWNESS, 30, 2)));
                                    online.damage(60, p);
                                    p.setCooldown(Material.IRON_INGOT, 360);
                                    //the sound
                                    p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL , 1, 2.5f);
                                    //the knockback
                                    Vector unitVector = online.getLocation().toVector().subtract(p.getLocation().toVector()).normalize();
                                    online.setVelocity(unitVector.multiply(1.2).setY(unitVector.getY() + 0.5));
                                }
                            }
                        }

                    }
                }

        }
    }

    /**
     *
     * @param e Cave troll's new ability to kick back an enemy and deal damage to them.
     */
    @EventHandler
    public void onImmobilise(PlayerInteractEntityEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();
        if (Objects.equals(Kit.equippedKits.get(uuid).name, name)) {
            Player p = e.getPlayer();
            //Stuns the horse which the cave troll hits.
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

            //The player who is being right-clicked on by the troll.

            // Prevent using in lobby
            if (InCombat.isPlayerInLobby(uuid)) {
                return;
            }

            //Check if the players are not on the same team, if true then perform the cave troll's attack.
            if (TeamController.getTeam(q.getUniqueId()) != TeamController.getTeam(p.getUniqueId())) {
               immobilise(q, p);
            }

        }
    }


    /**
     * Activate the cave troll's stun ability, immobilizing the opponent
     * @param victim The opponent
     * @param troll The cave troll
     */
    private void immobilise(Player victim, Player troll) {
        if (troll.getInventory().getItemInMainHand().getType() == Material.STONE_SHOVEL &&
                troll.getCooldown(Material.STONE_SHOVEL) == 0) {

            // Activate stun
            troll.setCooldown(Material.STONE_SHOVEL, 240);
            Messenger.sendSuccess("You immobilised " + CSNameTag.mmUsername(victim) + ".", troll);
            Messenger.sendWarning("You have been immobilised by " + CSNameTag.mmUsername(troll) + "!", victim);
            victim.getWorld().playSound(victim.getLocation(), Sound.ENTITY_GENERIC_EXPLODE , 1, 1.6f);

            //the knockback
            Vector unitVector = victim.getLocation().toVector().subtract(troll.getLocation().toVector()).normalize();
            victim.setVelocity(unitVector.multiply(1.1).setY(unitVector.getY() + 0.4));

            // Apply potion effects
            troll.addPotionEffect((new PotionEffect(PotionEffectType.SLOWNESS, 80, 1)));
            victim.addPotionEffect((new PotionEffect(PotionEffectType.SLOWNESS, 80, 1)));
            victim.addPotionEffect((new PotionEffect(PotionEffectType.MINING_FATIGUE, 80, 0)));

            // Prevent movement
            AttributeInstance kb = victim.getAttribute(Attribute.KNOCKBACK_RESISTANCE);
            assert kb != null;
            kb.setBaseValue(2);
            victim.setWalkSpeed(0);

            new BukkitRunnable() {
                @Override
                public void run() {
                    kb.setBaseValue(Kit.equippedKits.get(victim.getUniqueId()).kbResistance);
                    victim.setWalkSpeed(0.2f);
                }
            }.runTaskLater(Main.plugin, 80);

            UpdateStats.addSupports(troll.getUniqueId(), 2);
        }
    }

    /**
     * Activate the cave troll's stun ability, immobilizing the opponent
     * @param victim The opponent
     * @param troll The cave troll
     */
    private void immobiliseHorse(Horse victim, Player troll) {
        if (troll.getInventory().getItemInMainHand().getType() == Material.STONE_SHOVEL &&
                troll.getCooldown(Material.STONE_SHOVEL) == 0) {

            // Activate stun
            troll.setCooldown(Material.STONE_SHOVEL, 240);
            Messenger.sendActionSuccess("You immobilised <aqua>" + Objects.requireNonNull(victim.getOwner()) + "</aqua>'s horse.", troll);
            victim.getWorld().playSound(victim.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL , 1, 2.5f);

            // Apply potion effects
            victim.addPotionEffect((new PotionEffect(PotionEffectType.SLOWNESS, 100, 3)));

            //the knockback
            Vector unitVector = victim.getLocation().toVector().subtract(troll.getLocation().toVector()).normalize();
            victim.setVelocity(unitVector.multiply(1.4).setY(unitVector.getY() + 0.5));

            UpdateStats.addSupports(troll.getUniqueId(), 2);

        }
    }

    @Override
    public ArrayList<Component> getGuiDescription() {
        ArrayList<Component> description = new ArrayList<>();
        description.add(Component.text("//TODO - Add kit description", NamedTextColor.GRAY));
        return description;
    }
}
