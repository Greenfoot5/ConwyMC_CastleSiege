package me.greenfoot5.castlesiege.kits.kits.coin_kits;

import io.lumine.mythic.bukkit.BukkitAPIHelper;
import me.greenfoot5.castlesiege.Main;
import me.greenfoot5.castlesiege.database.CSActiveData;
import me.greenfoot5.castlesiege.events.EnderchestEvent;
import me.greenfoot5.castlesiege.events.combat.AssistKill;
import me.greenfoot5.castlesiege.events.combat.InCombat;
import me.greenfoot5.castlesiege.events.death.DeathEvent;
import me.greenfoot5.castlesiege.events.timed.BarCooldown;
import me.greenfoot5.castlesiege.kits.items.CSItemCreator;
import me.greenfoot5.castlesiege.kits.items.EquipmentSet;
import me.greenfoot5.castlesiege.kits.kits.CoinKit;
import me.greenfoot5.castlesiege.kits.kits.Kit;
import me.greenfoot5.castlesiege.maps.TeamController;
import me.greenfoot5.castlesiege.misc.CSNameTag;
import me.greenfoot5.conwymc.data_types.Tuple;
import me.greenfoot5.conwymc.util.Messenger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Rogue extends CoinKit implements Listener {

    public static final ArrayList<UUID> hasPoisonedWeapons = new ArrayList<>();
    public static final ArrayList<UUID> isShadow = new ArrayList<>();
    private static final int health = 210;
    private static final double regen = 8;
    private static final double meleeDamage = 32;

    private final ItemStack gouge;
    private final ItemStack shadowStep;
    private final ItemStack comboPoint;
    private final ItemStack netheriteSword;
    private final ItemStack netheriteSwordVoted;
    private final ItemStack poisonSwordVoted;

    private boolean canGouge = false;
    private BukkitRunnable br = null;
    private final BukkitAPIHelper mythicMobsApi = new BukkitAPIHelper();

    /**
     * Creates a new Rogue
     */
    public Rogue() {
        super("Rogue", health, regen, Material.NETHERITE_BOOTS);
        super.canSeeHealth = true;

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();

        netheriteSword = CSItemCreator.weapon(new ItemStack(Material.NETHERITE_SWORD),
                Component.text("Dagger", NamedTextColor.GREEN),
                List.of(Component.empty(),
                        Component.text("32 Melee Damage", NamedTextColor.DARK_GREEN)),
                null, 32);
        // Weapon
        es.hotbar[0] = netheriteSword;

        // Voted weapon
        netheriteSwordVoted = CSItemCreator.weapon(new ItemStack(Material.NETHERITE_SWORD),
                Component.text("Dagger", NamedTextColor.GREEN),
                List.of(Component.empty(),
                        Component.text("34 Melee Damage", NamedTextColor.DARK_GREEN),
								Component.text("⁎ Voted: +2 Melee Damage", NamedTextColor.DARK_AQUA)),
                Collections.singletonList(new Tuple<>(Enchantment.LOOTING, 0)), 34);
        es.votedWeapon = new Tuple<>(netheriteSwordVoted, 0);

        // Voted weapon
        poisonSwordVoted = CSItemCreator.weapon(new ItemStack(Material.GOLDEN_SWORD),
                Component.text("Poison Dagger", NamedTextColor.GREEN),
                Collections.singletonList(Component.text("⁎ Voted: +2 Melee Damage", NamedTextColor.GREEN)),
                Collections.singletonList(new Tuple<>(Enchantment.LOOTING, 0)), 34);
        es.votedWeapon = new Tuple<>(netheriteSwordVoted, 0);

        // Gouge
        gouge = CSItemCreator.weapon(new ItemStack(Material.NETHERITE_INGOT),
                Component.text("Gouge", NamedTextColor.GREEN),
                Arrays.asList(Component.empty(),
                        Component.text("Sneak + Right Click behind target", NamedTextColor.AQUA).decorate(TextDecoration.BOLD),
                        Component.empty(),
                        Component.text("This attack uses combo points, the", NamedTextColor.AQUA),
                        Component.text("more combo points the stronger the attack.", NamedTextColor.AQUA),
                        Component.empty(),
                        Component.text("This stuns and damages the target but", NamedTextColor.AQUA),
                        Component.text("in order for it to work you have to be behind", NamedTextColor.AQUA),
                        Component.text("your target.", NamedTextColor.AQUA),
                        Component.empty(),
                        Component.text("More combo points means more damage and", NamedTextColor.AQUA),
                        Component.text("self healing on a successful gouge.", NamedTextColor.AQUA),
                        Component.text("Can be performed whilst shadow-stepping.", NamedTextColor.BLUE)),
                null, 1);
        es.hotbar[1] = gouge;

        ItemStack poison = poisonPotion();
        es.hotbar[2] = poison;

        shadowStep = CSItemCreator.item(new ItemStack(Material.DRIED_KELP),
                Component.text("Shadowstep", NamedTextColor.GREEN),
                Arrays.asList(Component.empty(),
                        Component.text("Right click to activate.", NamedTextColor.AQUA).decorate(TextDecoration.BOLD),
                        Component.empty(),
                        Component.text("You move through the shadows, invisible to everyone.", NamedTextColor.AQUA),
                        Component.text("Whilst you shadowstep you can not attack targets, but you can gauge them.", NamedTextColor.AQUA),
                        Component.text("This ability lasts 8 seconds and has a cool-down of 13 seconds.", NamedTextColor.AQUA)),
                null);
        es.hotbar[3] = shadowStep;

        ItemStack trackArrow = CSItemCreator.item(new ItemStack(Material.TIPPED_ARROW),
                Component.text("Track Arrow", NamedTextColor.GREEN),
                Arrays.asList(Component.empty(),
                        Component.text("Left click/Right click to throw!", NamedTextColor.AQUA).decorate(TextDecoration.BOLD),
                        Component.empty(),
                        Component.text("Hitting enemy targets with this gives them", NamedTextColor.AQUA),
                        Component.text("glowing for a minute and stuns them briefly.", NamedTextColor.AQUA),
                        Component.text("Allowing you enough time to strike or escape.", NamedTextColor.AQUA),
                        Component.text("hitting enemies with this ability also awards you ", NamedTextColor.AQUA),
                        Component.text("with a combo point.", NamedTextColor.AQUA)),
                null);
        PotionMeta potionMeta = (PotionMeta) trackArrow.getItemMeta();
        assert potionMeta != null;
        potionMeta.setColor(Color.YELLOW);
        trackArrow.setItemMeta(potionMeta);
        potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.BLINDNESS, 25, 0), true);
        potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.GLOWING, 1200, 0), true);
        trackArrow.setItemMeta(potionMeta);

        es.hotbar[4] = trackArrow;

        comboPoint = CSItemCreator.item(new ItemStack(Material.GLOWSTONE_DUST),
                Component.text("Combo Point", NamedTextColor.GREEN),
                Arrays.asList(Component.empty(),
                        Component.text("Ability Power Currency", NamedTextColor.AQUA).decorate(TextDecoration.BOLD),
                        Component.empty(),
                        Component.text("This currency can be used to perform a stronger gouge.", NamedTextColor.AQUA),
                        Component.text("You can get combo points by killing opponents", NamedTextColor.AQUA),
                        Component.text("and hitting enemies with track arrows.", NamedTextColor.AQUA)),
                null);

        // Chestplate
        es.chest = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE),
                Component.text("Leather Chestplate", NamedTextColor.GREEN),
                List.of(Component.text("» Netherite Coast Trim", NamedTextColor.DARK_GRAY),
                        //Component.text("\uD800\uDF4A Netherite Coast Trim", NamedTextColor.DARK_GRAY),
                        Component.empty(),
                        Component.text(health + " HP", NamedTextColor.DARK_GREEN),
                        Component.text(regen + " Regen", NamedTextColor.DARK_GREEN)), null,
                Color.fromRGB(15, 15, 15));
        ItemMeta chest = es.chest.getItemMeta();
        ArmorMeta chestMeta = (ArmorMeta) chest;
        assert chest != null;
        ArmorTrim chestTrim = new ArmorTrim(TrimMaterial.NETHERITE, TrimPattern.COAST);
        ((ArmorMeta) chest).setTrim(chestTrim);
        es.chest.setItemMeta(chestMeta);

        // Leggings
        es.legs = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_LEGGINGS),
                Component.text("Leather Leggings", NamedTextColor.GREEN),
                List.of(Component.empty(),
                        Component.text(health + " HP", NamedTextColor.DARK_GREEN),
                        Component.text(regen + " Regen", NamedTextColor.DARK_GREEN)), null,
                Color.fromRGB(19, 19, 19));

        // Boots
        es.feet = CSItemCreator.item(new ItemStack(Material.NETHERITE_BOOTS),
                Component.text("Rogue Boots", NamedTextColor.GREEN),
                List.of(Component.empty(),
                        Component.text(health + " HP", NamedTextColor.DARK_GREEN),
                        Component.text(regen + " Regen", NamedTextColor.DARK_GREEN),
                        Component.empty(),
                        Component.text("◆ Feather Falling XV", NamedTextColor.DARK_PURPLE)),
                Collections.singletonList(new Tuple<>(Enchantment.FEATHER_FALLING, 15)));
        // Voted Boots
        es.votedFeet = CSItemCreator.item(new ItemStack(Material.NETHERITE_BOOTS),
                Component.text("Rogue Boots", NamedTextColor.GREEN),
                List.of(Component.empty(),
                        Component.text(health + " HP", NamedTextColor.DARK_GREEN),
                        Component.text(regen + " Regen", NamedTextColor.DARK_GREEN),
                        Component.empty(),
                        Component.text("◆ Feather Falling XV", NamedTextColor.DARK_PURPLE),
                        Component.text("⁎ Voted: Depth Strider II", NamedTextColor.DARK_AQUA)),
                Arrays.asList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2), new Tuple<>(Enchantment.FEATHER_FALLING, 15)));



        super.equipment = es;

        // Perm Potion Effect
        super.potionEffects.add(new PotionEffect(PotionEffectType.SPEED, 999999, 1));
        super.potionEffects.add(new PotionEffect(PotionEffectType.JUMP_BOOST, 999999, 0));
        super.potionEffects.add(new PotionEffect(PotionEffectType.NIGHT_VISION, 999999, 0));
        super.potionEffects.add(new PotionEffect(PotionEffectType.HASTE, 999999, 1));

        // Death Messages
        super.deathMessage[0] = "You were struck from the shadows by ";
        super.killMessage[0] = " struck ";
        super.killMessage[1] = " from the shadows";
    }

    private ItemStack poisonPotion() {
        ItemStack itemStack = new ItemStack(Material.POTION);
        PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
        assert potionMeta != null;

        potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.POISON, 1, 6), true);
        potionMeta.setColor(Color.GREEN);
        potionMeta.displayName(Component.text("Bottle of Poison", NamedTextColor.RED));
        potionMeta.lore(Arrays.asList(Component.empty(),
                Component.text("When right clicking this, you will deal poison attacks", NamedTextColor.AQUA),
                Component.text("for the next 10 seconds. This has a 50 second cooldown.", NamedTextColor.AQUA)));
        itemStack.setItemMeta(potionMeta);

        return itemStack;
    }

    /**
     * Activate poisoned weapons
     * @param e event triggered by right-clicking mode switch button.
     */
    @EventHandler
    public void onAbilityTrigger(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(uuid)) {
            return;
        }
        if (!Objects.equals(Kit.equippedKits.get(uuid).name, name)) {
            return;
        }
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if (p.getInventory().getItemInMainHand().getType() != Material.POTION) {
            return;
        }

        Messenger.sendActionInfo("You poisoned your weapons", p);
        applyPoison(p);
    }

    /**
     * Activate ShadowStep
     * @param e event triggered by right-clicking mode switch button.
     */
    @EventHandler
    public void onShadowstep(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(uuid)) {
            return;
        }
        if (!Objects.equals(Kit.equippedKits.get(uuid).name, name)) {
            return;
        }
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        Material item = p.getInventory().getItemInMainHand().getType();
        if (!item.equals(shadowStep.getType())) {
            return;
        }
        Messenger.sendActionInfo("You dived into the shadows", p);
        shadowstepAbility(p);
    }

    /**
     * Hide the player from everyone.
     * @param p The player to make invisible
     */
    private void shadowstepAbility(Player p) {
        int duration = 160;
        if (InCombat.isPlayerInCombat(p.getUniqueId())) {
            Messenger.sendActionError("You can't shadow-step whilst in combat!", p);
            return;
        }
        if (p.getCooldown(shadowStep.getType()) != 0) {
            Messenger.sendActionError("This ability is still under cool-down.", p);
            return;
        }
        p.setCooldown(shadowStep.getType(), 420);
        isShadow.add(p.getUniqueId());
        // invisibility is there for show
        p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999, 0));
        p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, 999999, 4));
        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 160, 2));
        //Makes invisible
        mythicMobsApi.castSkill(p,"RogueEffect");
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!Objects.equals(Kit.equippedKits.get(p.getUniqueId()).name, name)) {
                    return;
                }

                if (isShadow.contains(p.getUniqueId())) {
                    Messenger.sendActionWarning("You are no longer invisible! (Strike now!)", p);
                    mythicMobsApi.castSkill(p,"RogueEffect2");
                    isShadow.remove(p.getUniqueId());
                }

                for (PotionEffect effect : p.getActivePotionEffects()) {
                    if ((effect.getType().equals(PotionEffectType.INVISIBILITY) && effect.getAmplifier() == 0)
                            || (effect.getType().equals(PotionEffectType.JUMP_BOOST) && effect.getAmplifier() == 4)) {
                        p.removePotionEffect(effect.getType());
                    }
                }
            }
        }.runTaskLater(Main.plugin, duration);
    }

    private void applyPoison(Player p) {
        if (!Objects.equals(Kit.equippedKits.get(p.getUniqueId()).name, name)) {
            return;
        }
        if (p.getCooldown(poisonPotion().getType()) != 0 || hasPoisonedWeapons.contains(p.getUniqueId())) {
            return;
        }
        hasPoisonedWeapons.add(p.getUniqueId());
        p.setCooldown(poisonPotion().getType(), 1000);
        p.getInventory().setItem(0, poisonSwordVoted);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (Objects.equals(Kit.equippedKits.get(p.getUniqueId()).name, name)) {
                    hasPoisonedWeapons.remove(p.getUniqueId());
                    changeSword(p, poisonSwordVoted.getType(), netheriteSword, netheriteSwordVoted);
                }
            }
        }.runTaskLater(Main.plugin, 201);
    }

    /**
     * Change the player's poison dagger.
     * @param player The player
     * @param oldMaterial The material of the sword to remove
     * @param sword The sword to set if not voted
     * @param swordVoted The sword to set if voted
     */
    private void changeSword(Player player, Material oldMaterial, ItemStack sword, ItemStack swordVoted) {
        PlayerInventory inventory = player.getInventory();

        // Remove old sword
        inventory.remove(oldMaterial);
        if (inventory.getItemInOffHand().getType() == oldMaterial)
            inventory.setItemInOffHand(null);

        // Don't give a new sword when the player already has one
        if (inventory.contains(sword.getType()) || inventory.getItemInOffHand().getType() == sword.getType())
            return;

        // Give new sword
        if (CSActiveData.getData(player.getUniqueId()).hasVote("sword"))
            inventory.addItem(swordVoted);
        else
            inventory.setItem(0, sword);
    }

    /**
     * Cause poison damage to opponents and horses when the poison ability is activated.
     * @param e The event called when hitting another player
     */
    @EventHandler(ignoreCancelled = true)
    public void onAttack(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player player))
            return;

        if (!Objects.equals(Kit.equippedKits.get(player.getUniqueId()).name, name))
            return;

        // Prevent dealing damage while using the shadow ability
        if (isShadow.contains(player.getUniqueId())) {
            e.setCancelled(true);
            return;
        }

        // Ensure the poison sword is used
        Material item = player.getInventory().getItemInMainHand().getType();
        if (item != Material.GOLDEN_SWORD || !hasPoisonedWeapons.contains(player.getUniqueId()))
            return;

        // Deal poison damage to players and horses
        if (e.getEntity() instanceof Player || e.getEntity() instanceof Horse)
            ((LivingEntity) e.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.POISON, 260, 0));
    }

    /**
     * Activate the spearman ability of throwing a spear
     * @param e The event called when right-clicking with a stick
     */
    @EventHandler
    public void throwTrackArrow(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        ItemStack stick = p.getInventory().getItemInMainHand();
        int cooldown = p.getCooldown(Material.TIPPED_ARROW);

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(uuid)) {
            return;
        }

        if (!Objects.equals(Kit.equippedKits.get(uuid).name, name)) {
            return;
        }
        if (!stick.getType().equals(Material.TIPPED_ARROW)) {
            return;
        }
        if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (cooldown == 0 && !isShadow.contains(p.getUniqueId())) {
                p.setCooldown(Material.TIPPED_ARROW, 100);
                p.launchProjectile(Arrow.class).setVelocity(p.getLocation().getDirection().multiply(4.0));
            } else if (isShadow.contains(p.getUniqueId())) {
                Messenger.sendActionError("You can't throw your arrow while in shadows!", p);
            } else {
                Messenger.sendActionError("You can't throw your tracking arrow yet!", p);
            }
        } else if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (cooldown == 0 && !isShadow.contains(p.getUniqueId())) {
                p.setCooldown(Material.TIPPED_ARROW, 180);
                //shoot 3 track arrows instead of one, resulting in a larger cooldown.
                    Arrow arrow = p.launchProjectile(Arrow.class);
                    arrow.setVelocity(p.getLocation().getDirection().multiply(4.0));
                    Vector v = arrow.getVelocity();
                    p.launchProjectile(Arrow.class, v.rotateAroundY(0.157));
                    p.launchProjectile(Arrow.class, v.rotateAroundY(-0.314));

            } else if (isShadow.contains(p.getUniqueId())) {
                Messenger.sendActionError("You can't throw your tracking arrow while in shadows!", p);
            } else {
                Messenger.sendActionError("You can't throw your tracking arrow yet!", p);
            }
        }
    }

    /**
     * Set the thrown spear's damage
     * @param e The event called when an arrow hits a player
     */
    @EventHandler(priority = EventPriority.LOW)
    public void changeTrackArrowDamage(ProjectileHitEvent e) {
        if (e.getEntity().getShooter() instanceof Player &&
            !Objects.equals(Kit.equippedKits.get(((Player) e.getEntity().getShooter()).getUniqueId()).name, name))
            return;

        if (e.getEntity() instanceof Arrow arrow) {

            //The damage on Players is 10, whilst on other entities it is 25. Hitting players also awards comboPoints.
            if (arrow.getShooter() instanceof Player damages && e.getHitEntity() instanceof Player hit){
                if (!Objects.equals(Kit.equippedKits.get(damages.getUniqueId()).name, name)) {
                    return;
                }
                arrow.addCustomEffect(new PotionEffect(PotionEffectType.BLINDNESS, 30, 0), true);
                arrow.addCustomEffect(new PotionEffect(PotionEffectType.GLOWING, 1200, 0), true);
                if (!Objects.equals(Kit.equippedKits.get(damages.getUniqueId()).name, name)) {
                    return;
                }
                //check if players aren't on the same team
                if (TeamController.getTeam(damages.getUniqueId())
                        != TeamController.getTeam(hit.getUniqueId())) {
                    arrow.setDamage(5);
                    ((Player) arrow.getShooter()).getInventory().addItem(comboPoint);
                }
            } else if (arrow.getShooter() instanceof Player && !(e.getHitEntity() instanceof Player)){
                arrow.setDamage(10);
            }
        }
    }

    /**
     * @param toHeal The player to heal
     * @param healed How much they healed by
     */
    private void healPlayer(Player toHeal, int healed) {
        if ((baseHealth + healed) > baseHealth) {
            toHeal.setHealth(baseHealth);
        } else {
            toHeal.setHealth(toHeal.getHealth() + healed);
        }
    }

    /**
     * @param damager the player who is damaging the target
     * @param target the target to deal damage to
     * @param amount the amount of combo points used
     */
    private void gougedDamage(Player damager, Player target, int amount) {
        int damage = 25 + (25 * amount);
        int duration = 40 + (10 * amount);
        target.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, duration, 4));
        target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, duration, 0));
        target.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, duration, 0));
        AssistKill.addDamager(target.getUniqueId(), damager.getUniqueId(), damage);
        healPlayer(damager, damage);

        //remove used up combo points
        removeComboPoints(damager);
        if (isShadow.contains(damager.getUniqueId())) {
            mythicMobsApi.castSkill(damager,"RogueEffectQuit");
            isShadow.remove(damager.getUniqueId());
        }
        if ((target.getHealth() - damage > 0)) {
            target.damage(damage);
        } else {
            target.setHealth(0);
            DeathEvent.setKiller(target, damager);
            damager.getInventory().addItem(comboPoint);
        }
    }

    private void removeComboPoints(Player p) {
        if (p.getInventory().contains(Material.GLOWSTONE_DUST)) {
            for (ItemStack item : p.getInventory().getContents()) {
                if (item == null) { return; }
                if (item.getType().equals(Material.GLOWSTONE_DUST)) {
                    item.setAmount(0);
                }
            }
        }
    }

    /**
     * @param event the event where a player dies to a rogue and the rogue gets a combo point from that.
     */
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (event.getEntity().getKiller() == null) { return; }
        if (Objects.equals(Kit.equippedKits.get(event.getEntity().getKiller().getUniqueId()).name, name)) {
            // Prevent using in lobby
            if (InCombat.isPlayerInLobby(event.getEntity().getKiller().getUniqueId())) {
                return;
            }
            event.getEntity().getKiller().getInventory().addItem(comboPoint);
        }
    }


    /**
     * Instantly kills players when hit in the back by a sneaking ranger
     * @param ed The event called when a player attacks another player
     */
    @EventHandler(ignoreCancelled = true)
    public void gougeDamage(PlayerInteractEntityEvent ed) {
        if (!(ed.getRightClicked() instanceof Player hit)) {
            return;
        }
        Player p = ed.getPlayer();
        if (!Objects.equals(Kit.equippedKits.get(p.getUniqueId()).name, name)) {
            return;
        }
        if (p.getCooldown(gouge.getType()) != 0) {
            return;
        }

        Location hitLoc = hit.getLocation();
        Location damagerLoc = p.getLocation();

        // Basically what happens here is you check whether the player
        // is not looking at you at all (so having their back aimed at you.)
        if (!(damagerLoc.getYaw() <= hitLoc.getYaw() + 60) || !(damagerLoc.getYaw() >= hitLoc.getYaw() - 60)
                || !canGouge) {
            return;
        }

        if (p.getInventory().getItemInMainHand().getType() != Material.NETHERITE_INGOT) {
            return;
        }
        p.setCooldown(gouge.getType(), 100);

        ed.setCancelled(true);
        Messenger.sendWarning("You were gouged by " + CSNameTag.mmUsername(p), hit);
        Messenger.sendSuccess("You gouged " + CSNameTag.mmUsername(hit), p);
        if (!p.getInventory().contains(Material.GLOWSTONE_DUST)) {
            gougedDamage(p, hit, 0);
            return;
        }
        for (ItemStack item : p.getInventory().getContents()) {
            if (item != null) {
                if (item.getType().equals(Material.GLOWSTONE_DUST)) {
                    int amount = item.getAmount();
                    gougedDamage(p, hit, amount);
                }
            }
        }
    }


    /**
     * The event called when a player starts and stops sneaking
     * @param e exp bar shows as full indicating the rogue is ready to gouge
     */
    @EventHandler
    public void gougeDetection(PlayerToggleSneakEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(uuid)) {
            return;
        }

        if (Objects.equals(Kit.equippedKits.get(uuid).name, name)) {
            if (br != null) {
                br.cancel();
                br = null;
            }

            // p.isSneaking() gives the sneaking status before the SneakEvent is processed
            if (p.isSneaking()) {
                canGouge = false;
                BarCooldown.remove(uuid);
            } else {
                br = new BukkitRunnable() {
                    @Override
                    public void run() {
                        canGouge = true;
                        br = null;
                    }
                };
                br.runTaskLater(Main.plugin, 20);
                BarCooldown.add(uuid, 20);
            }
        }
    }

    /**
     * Rogue becomes visible again.
     * @param event The event called when an off-cooldown player interacts with an enderchest
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClickEnderchest(EnderchestEvent event) {
        if (Objects.equals(Kit.equippedKits.get(event.getPlayer().getUniqueId()).name, name)) {
            event.setCancelled(true);
            Player p = event.getPlayer();

            isShadow.remove(p.getUniqueId());
            hasPoisonedWeapons.remove(p.getUniqueId());
            mythicMobsApi.castSkill(p,"RogueEffectQuit");
        }
    }


    /**
     * @return The lore to add to the kit gui item
     */
    @Override
    public ArrayList<Component> getGuiDescription() {
        ArrayList<Component> kitLore = new ArrayList<>();
        kitLore.add(Component.text("A master of camouflage and tracking. Can", NamedTextColor.GRAY));
        kitLore.add(Component.text("become invisible and strike enemies from behind", NamedTextColor.GRAY));
        kitLore.addAll(getBaseStats(health, regen, meleeDamage, 0));
        kitLore.add(Component.empty());
        kitLore.add(Component.text("Effects:", NamedTextColor.DARK_PURPLE));
        kitLore.add(Component.text("- Speed II", NamedTextColor.GRAY));
        kitLore.add(Component.text("- Haste II", NamedTextColor.GRAY));
        kitLore.add(Component.text("- Night Vision I", NamedTextColor.GRAY));
        kitLore.add(Component.text("- Jump Boost I", NamedTextColor.GRAY));
        kitLore.add(Component.empty());
        kitLore.add(Component.text("Active:", NamedTextColor.GOLD));
        kitLore.add(Component.text("- Can throw track arrows", NamedTextColor.GRAY));
        kitLore.add(Component.text("- Can perform shadowstep (grants invisibility)", NamedTextColor.GRAY));
        kitLore.add(Component.text("- Can temporarily enhance their dagger", NamedTextColor.GRAY));
        kitLore.add(Component.text("with poison", NamedTextColor.GRAY));
        kitLore.add(Component.text("- When behind enemies can gauge them, which", NamedTextColor.GRAY));
        kitLore.add(Component.text("stuns them temporarily and grants a bit of healing", NamedTextColor.GRAY));
        kitLore.add(Component.empty());
        // TODO - Improve passive descriptions
        kitLore.add(Component.text("Passive:", NamedTextColor.DARK_GREEN));
        kitLore.add(Component.text("- Can see player health.", NamedTextColor.GRAY));
        kitLore.add(Component.text("- Can use gauge whilst invisible", NamedTextColor.GRAY));
        kitLore.add(Component.text("- Uses combo points to perform more", NamedTextColor.GRAY));
        kitLore.add(Component.text("powerful gauges", NamedTextColor.GRAY));
        kitLore.add(Component.text("- Killing opponents and hitting them", NamedTextColor.GRAY));
        kitLore.add(Component.text("with track arrows generates combo-points", NamedTextColor.GRAY));
        return kitLore;
    }
}
