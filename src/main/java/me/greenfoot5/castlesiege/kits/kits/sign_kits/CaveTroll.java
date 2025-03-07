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
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
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

/**
 * Cave Troll kit
 */
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
                        Collections.singletonList(new Tuple<>(Enchantment.LOOTING, 1)), 45),
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
        if (e.getPlayer() != equippedPlayer)
            return;

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(equippedPlayer.getUniqueId())) {
            return;
        }

        ItemStack main = equippedPlayer.getInventory().getItemInMainHand();

        if (!main.getType().equals(Material.IRON_INGOT))
            return;

        if (e.getAction() != Action.RIGHT_CLICK_AIR)
            return;

        int cooldown = equippedPlayer.getCooldown(Material.IRON_INGOT);
        if (cooldown != 0) {
            return;
        }

        boolean hasHit = false;
        for (Entity entity : equippedPlayer.getNearbyEntities(3.5, 3.5, 3.5)) {
            if (!(entity instanceof Player hit))
                continue;

            if (TeamController.isPlaying(hit)
                    && TeamController.getTeam(entity.getUniqueId()) != TeamController.getTeam(equippedPlayer.getUniqueId())) {
                hasHit = true;
                hit.addPotionEffect((new PotionEffect(PotionEffectType.SLOWNESS, 30, 2)));
                hit.damage(60, equippedPlayer);

                //the knockback
                Vector unitVector = entity.getLocation().toVector().subtract(equippedPlayer.getLocation().toVector()).normalize();
                entity.setVelocity(unitVector.multiply(1.2).setY(unitVector.getY() + 0.5));
            }
        }

        if (hasHit) {
            equippedPlayer.setCooldown(Material.IRON_INGOT, 360);
            //the sound
            equippedPlayer.getWorld().playSound(equippedPlayer.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL , 1, 2.5f);
        }
    }

    /**
     *
     * @param e Cave troll's new ability to kick back an enemy and deal damage to them.
     */
    @EventHandler
    public void onImmobilise(PlayerInteractEntityEvent e) {
        if (e.getPlayer() != equippedPlayer)
            return;

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(equippedPlayer.getUniqueId())) {
            return;
        }

        //Stuns the horse which the cave troll hits.
        if (e.getRightClicked() instanceof Horse h) {
            Player horseOwner = (Player) h.getOwner();
            assert horseOwner != null;
            if (TeamController.getTeam(horseOwner.getUniqueId()) != TeamController.getTeam(equippedPlayer.getUniqueId())) {
              immobiliseHorse(h);
            }
        }

        //Check if the hit entity is a player, otherwise do nothing.
        if (e.getRightClicked() instanceof Player hit) {
            //Check if the players are not on the same team, if true then perform the cave troll's attack.
            if (TeamController.getTeam(hit.getUniqueId()) != TeamController.getTeam(equippedPlayer.getUniqueId())) {
                immobilise(hit);
            }
        }
    }


    /**
     * Activate the cave troll's stun ability, immobilizing the opponent
     * @param victim The opponent
     */
    private void immobilise(Player victim) {
        if (equippedPlayer.getInventory().getItemInMainHand().getType() == Material.STONE_SHOVEL &&
                equippedPlayer.getCooldown(Material.STONE_SHOVEL) == 0) {

            // Activate stun
            equippedPlayer.setCooldown(Material.STONE_SHOVEL, 240);
            Messenger.sendSuccess("You immobilised " + CSNameTag.mmUsername(victim) + ".", equippedPlayer);
            Messenger.sendWarning("You have been immobilised by " + CSNameTag.mmUsername(equippedPlayer) + "!", victim);
            victim.getWorld().playSound(victim.getLocation(), Sound.ENTITY_GENERIC_EXPLODE , 1, 1.6f);

            //the knockback
            Vector unitVector = victim.getLocation().toVector().subtract(equippedPlayer.getLocation().toVector()).normalize();
            victim.setVelocity(unitVector.multiply(1.1).setY(unitVector.getY() + 0.4));

            // Apply potion effects
            equippedPlayer.addPotionEffect((new PotionEffect(PotionEffectType.SLOWNESS, 80, 1)));
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

            UpdateStats.addSupports(equippedPlayer.getUniqueId(), 2);
        }
    }

    /**
     * Activate the cave troll's stun ability, immobilizing the opponent
     * @param victim The opponent
     */
    private void immobiliseHorse(Horse victim) {
        if (equippedPlayer.getInventory().getItemInMainHand().getType() == Material.STONE_SHOVEL &&
                equippedPlayer.getCooldown(Material.STONE_SHOVEL) == 0) {

            // Activate stun
            equippedPlayer.setCooldown(Material.STONE_SHOVEL, 240);
            Messenger.sendActionSuccess("You immobilised <aqua>" + Objects.requireNonNull(victim.getOwner()) + "</aqua>'s horse.", equippedPlayer);
            victim.getWorld().playSound(victim.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL , 1, 2.5f);

            // Apply potion effects
            victim.addPotionEffect((new PotionEffect(PotionEffectType.SLOWNESS, 100, 3)));

            //the knockback
            Vector unitVector = victim.getLocation().toVector().subtract(equippedPlayer.getLocation().toVector()).normalize();
            victim.setVelocity(unitVector.multiply(1.4).setY(unitVector.getY() + 0.5));

            UpdateStats.addSupports(equippedPlayer.getUniqueId(), 2);
        }
    }

    @Override
    public ArrayList<Component> getGuiDescription() {
        ArrayList<Component> description = new ArrayList<>();
        description.add(Component.text("//TODO - Add kit description", NamedTextColor.GRAY));
        return description;
    }
}
