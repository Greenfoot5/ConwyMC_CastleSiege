package me.huntifi.castlesiege.kits.kits.coin_kits;

import io.lumine.mythic.bukkit.BukkitAPIHelper;
import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.castlesiege.events.EnderchestEvent;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.events.combat.AssistKill;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.events.death.DeathEvent;
import me.huntifi.castlesiege.events.timed.BarCooldown;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.kits.kits.CoinKit;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.maps.TeamController;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
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
import org.bukkit.event.entity.EntityInteractEvent;
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
import java.util.Objects;
import java.util.UUID;

public class Rogue extends CoinKit implements Listener {

    public static final ArrayList<UUID> hasPoisonedWeapons = new ArrayList<>();

    public static final ArrayList<UUID> isShadow = new ArrayList<>();
    private final ItemStack gouge;

    private final ItemStack shadowStep;

    private final ItemStack comboPoint;

    private final ItemStack netheriteSword;

    private final ItemStack netheriteSwordVoted;

    private final ItemStack poisonSwordVoted;

    private static final int health = 210;
    private static final double regen = 8;
    private static final double meleeDamage = 32;

    private boolean canGouge = false;
    private BukkitRunnable br = null;

    public Rogue() {
        super("Rogue", health, regen, Material.NETHERITE_BOOTS);
        super.canSeeHealth = true;

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        netheriteSword = ItemCreator.weapon(new ItemStack(Material.NETHERITE_SWORD),
                ChatColor.DARK_GRAY + "Dagger", null, null, 32);
        // Weapon
        es.hotbar[0] = netheriteSword;

        // Voted weapon
        netheriteSwordVoted = ItemCreator.weapon(new ItemStack(Material.NETHERITE_SWORD),
                ChatColor.DARK_GRAY + "Dagger",
                Collections.singletonList(Component.text("- voted: +2 damage", NamedTextColor.AQUA)),
                Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), 34);
        es.votedWeapon = new Tuple<>(netheriteSwordVoted, 0);

        // Voted weapon
        poisonSwordVoted = ItemCreator.weapon(new ItemStack(Material.GOLDEN_SWORD),
                ChatColor.DARK_GRAY + "Poison Dagger",
                Collections.singletonList(Component.text("- Special: +2 damage", NamedTextColor.GREEN)),
                Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), 34);
        es.votedWeapon = new Tuple<>(netheriteSwordVoted, 0);

        // Gouge
        gouge = ItemCreator.weapon(new ItemStack(Material.NETHERITE_INGOT),
                ChatColor.GOLD + "Gouge", Arrays.asList("",
                        ChatColor.YELLOW + "" + ChatColor.BOLD + "Sneak + Right Click behind target",
                        "",
                        ChatColor.AQUA + "This attack uses combo points, the",
                        ChatColor.AQUA + "more combo points the stronger the attack.", "",
                        ChatColor.AQUA + "This stuns and damages the target but",
                        ChatColor.AQUA + "in order for it to work you have to be behind",
                        ChatColor.AQUA + "your target.", "",
                        ChatColor.AQUA + "More combo points means more damage and",
                        ChatColor.AQUA + "self healing on a succesful gouge.",
                        ChatColor.BLUE + "Can be performed whilst shadow-stepping."),
                null, 1);
        es.hotbar[1] = gouge;

        ItemStack poison = poisonPotion();
        es.hotbar[2] = poison;

        shadowStep = ItemCreator.item(new ItemStack(Material.DRIED_KELP),
                ChatColor.DARK_GRAY + "Shadowstep",
                Arrays.asList(ChatColor.AQUA + "", ChatColor.YELLOW + "" + ChatColor.BOLD + "Right click to activate.",
                        ChatColor.AQUA + "", ChatColor.AQUA + "You move through the shadows, invisible to everyone.",
                        ChatColor.AQUA + "Whilst you shadowstep you can not attack targets, but you can gauge them.",
                        ChatColor.AQUA + "This ability lasts 8 seconds and has a cool-down of 13 seconds."),
                null);
        es.hotbar[3] = shadowStep;

        ItemStack trackArrow = ItemCreator.item(new ItemStack(Material.TIPPED_ARROW),
                ChatColor.YELLOW + "Track Arrow",
                Arrays.asList(ChatColor.AQUA + "", ChatColor.YELLOW + "" + ChatColor.BOLD + "Left click/Right click to throw!",
                        ChatColor.AQUA + "", ChatColor.AQUA + "Hitting enemy targets with this gives them",
                        ChatColor.AQUA + "glowing for a minute and stuns them briefly.",
                        ChatColor.AQUA + "Allowing you enough time to strike or escape.",
                        ChatColor.AQUA + "hitting enemies with this ability also awards you ",
                        ChatColor.AQUA + "with a combo point."),
                null);
        PotionMeta potionMeta = (PotionMeta) trackArrow.getItemMeta();
        assert potionMeta != null;
        potionMeta.setColor(Color.YELLOW);
        trackArrow.setItemMeta(potionMeta);
        potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.BLINDNESS, 25, 0), true);
        potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.GLOWING, 1200, 0), true);
        trackArrow.setItemMeta(potionMeta);

        es.hotbar[4] = trackArrow;

        comboPoint = ItemCreator.item(new ItemStack(Material.GLOWSTONE_DUST),
                ChatColor.LIGHT_PURPLE + "Combo Point",
                Arrays.asList(ChatColor.AQUA + "", ChatColor.YELLOW + "" + ChatColor.BOLD + "Ability Power Currency",
                        ChatColor.AQUA + "", ChatColor.AQUA + "This currency can be used to perform a stronger gouge.",
                        ChatColor.AQUA + "You can get combo points by killing opponents",
                        ChatColor.AQUA + "and hitting enemies with track arrows."),
                null);

        // Chestplate
        es.chest = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE),
                Component.text("Leather Chestplate", NamedTextColor.GREEN), null, null,
                Color.fromRGB(15, 15, 15));
        ItemMeta chest = es.chest.getItemMeta();
        ArmorMeta chestMeta = (ArmorMeta) chest;
        assert chest != null;
        ArmorTrim chestTrim = new ArmorTrim(TrimMaterial.NETHERITE, TrimPattern.COAST);
        ((ArmorMeta) chest).setTrim(chestTrim);
        es.chest.setItemMeta(chestMeta);

        // Leggings
        es.legs = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_LEGGINGS),
                Component.text("Leather Leggings", NamedTextColor.GREEN), null, null,
                Color.fromRGB(19, 19, 19));

        // Boots
        es.feet = ItemCreator.item(new ItemStack(Material.NETHERITE_BOOTS),
                Component.text("Rogue Boots", NamedTextColor.GREEN),
                Collections.singletonList(ChatColor.AQUA + "- Feather Falling XV"),
                Collections.singletonList(new Tuple<>(Enchantment.PROTECTION_FALL, 15)));
        // Voted Boots
        es.votedFeet = ItemCreator.item(new ItemStack(Material.NETHERITE_BOOTS),
                Component.text("Rogue Boots", NamedTextColor.GREEN),
                Arrays.asList(ChatColor.AQUA + "- Feather Falling XV", ChatColor.AQUA + "- voted: Depth Strider II"),
                Arrays.asList(new Tuple<>(Enchantment.DEPTH_STRIDER, 3), new Tuple<>(Enchantment.PROTECTION_FALL, 15)));



        super.equipment = es;

        // Perm Potion Effect
        super.potionEffects.add(new PotionEffect(PotionEffectType.SPEED, 999999, 1));
        super.potionEffects.add(new PotionEffect(PotionEffectType.JUMP, 999999, 0));
        super.potionEffects.add(new PotionEffect(PotionEffectType.NIGHT_VISION, 999999, 0));
        super.potionEffects.add(new PotionEffect(PotionEffectType.FAST_DIGGING, 999999, 1));

        // Death Messages
        super.deathMessage[0] = "You were struck from the shadows by ";
        super.killMessage[0] = " struck ";
        super.killMessage[1] = " from the shadows";
    }


    public ItemStack poisonPotion() {
        ItemStack itemStack = new ItemStack(Material.POTION);
        PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
        assert potionMeta != null;

        potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.POISON, 1, 6), true);
        potionMeta.setColor(Color.GREEN);
        potionMeta.setDisplayName(ChatColor.RED + "Bottle of Poison");
        potionMeta.setLore(Arrays.asList(
                ChatColor.AQUA + "", ChatColor.AQUA + "When right clicking this, you will deal poison attacks",
                ChatColor.AQUA + "for the next 10 seconds. This has a 50 second cooldown."));
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
        if (Objects.equals(Kit.equippedKits.get(uuid).name, name)) {
            if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (p.getInventory().getItemInMainHand().getType() != Material.POTION) {
                    return;
                }
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                            ChatColor.DARK_GREEN + "You poisoned your weapons!"));
                    applyPoison(p);
            }
        }
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
        if (Objects.equals(Kit.equippedKits.get(uuid).name, name)) {
            if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                Material item = p.getInventory().getItemInMainHand().getType();
                if (item.equals(shadowStep.getType())) {
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                            ChatColor.RED + "You triggered the Shadowstep ability!"));
                    shadowstepAbility(p);
                }
            }
        }
    }

    /**
     * Hide the player from everyone.
     * @param p The player to make invisible
     */
    public void shadowstepAbility(Player p) {
        int duration = 160;
        if (InCombat.isPlayerInCombat(p.getUniqueId())) {
            Messenger.sendActionError("You can't shadow-step whilst in combat!", p);
            return;
        }
        if (p.getCooldown(shadowStep.getType()) == 0) {
            p.setCooldown(shadowStep.getType(), 420);
            isShadow.add(p.getUniqueId());
            //invis is there for show
            p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999, 0));
            p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 999999, 4));
            p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 160, 2));
            //Makes invisible
            mythicParticle(p);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (Objects.equals(Kit.equippedKits.get(p.getUniqueId()).name, name)) {
                            if (isShadow.contains(p.getUniqueId())) {
                                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                                        ChatColor.GOLD + "" + ChatColor.BOLD + "You are no longer invisible! (Strike now!)"));
                                mythicParticle2(p);
                                isShadow.remove(p.getUniqueId());
                            }
                            for (PotionEffect effect : p.getActivePotionEffects()) {
                                if ((effect.getType().getName().equals(PotionEffectType.INVISIBILITY.getName()) && effect.getAmplifier() == 0)
                                        || (effect.getType().getName().equals(PotionEffectType.JUMP.getName()) && effect.getAmplifier() == 4)) {
                                    p.removePotionEffect(effect.getType());
                                }
                            }
                        }
                    }
                }.runTaskLater(Main.plugin, duration);

        } else {
            Messenger.sendActionError("This ability is still under cool-down.", p);
        }

    }

    public void mythicParticle(Player p) {
        BukkitAPIHelper mythicMobsApi = new BukkitAPIHelper();
        mythicMobsApi.castSkill(p,"RogueEffect");
    }

    public void mythicParticle2(Player p) {
        BukkitAPIHelper mythicMobsApi = new BukkitAPIHelper();
        mythicMobsApi.castSkill(p,"RogueEffect2");
    }

    public void mythicParticleQuit(Player p) {
        BukkitAPIHelper mythicMobsApi = new BukkitAPIHelper();
        mythicMobsApi.castSkill(p,"RogueEffectQuit");
    }

    public void applyPoison(Player p) {
        if (Objects.equals(Kit.equippedKits.get(p.getUniqueId()).name, name)) {
        if (p.getCooldown(poisonPotion().getType()) == 0 && !hasPoisonedWeapons.contains(p.getUniqueId())) {
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
        }
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
        if (ActiveData.getData(player.getUniqueId()).hasVote("sword"))
            inventory.addItem(swordVoted);
        else
            inventory.setItem(0, sword);
    }

    /**
     * Cause poison damage to opponents and horses when the poison ability is activated.
     * @param e The event called when hitting another player
     */
    @EventHandler (ignoreCancelled = true)
    public void onAttack(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player))
            return;

        Player player = (Player) e.getDamager();
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

        if (Objects.equals(Kit.equippedKits.get(uuid).name, name)) {
            if (stick.getType().equals(Material.TIPPED_ARROW)) {
                if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
                    if (cooldown == 0 && !isShadow.contains(p.getUniqueId())) {
                        p.setCooldown(Material.TIPPED_ARROW, 100);
                        p.launchProjectile(Arrow.class).setVelocity(p.getLocation().getDirection().multiply(4.0));
                    } else if (isShadow.contains(p.getUniqueId())) {
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                                ChatColor.DARK_RED + "" + ChatColor.BOLD + "You can't throw your track arrow whilst shadowstepping!"));
                    } else {
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                                ChatColor.DARK_RED + "" + ChatColor.BOLD + "You can't throw your track arrow yet."));
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
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                                ChatColor.DARK_RED + "" + ChatColor.BOLD + "You can't throw your track arrow whilst shadowstepping!"));
                    } else {
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                                ChatColor.DARK_RED + "" + ChatColor.BOLD + "You can't throw your track arrow yet."));
                    }
                }
            }
        }
    }

    /**
     * Set the thrown spear's damage
     * @param e The event called when an arrow hits a player
     */
    @EventHandler (priority = EventPriority.LOW)
    public void changeTrackArrowDamage(ProjectileHitEvent e) {
        if (e.getEntity() instanceof Arrow) {
            Arrow arrow = (Arrow) e.getEntity();

            //The damage on Players is 10, whilst on other entities it is 25. Hitting players also awards comboPoints.
            if(arrow.getShooter() instanceof Player && e.getHitEntity() instanceof Player){
                Player damages = (Player) arrow.getShooter();
                if (Objects.equals(Kit.equippedKits.get(damages.getUniqueId()).name, name)) {
                    arrow.addCustomEffect(new PotionEffect(PotionEffectType.BLINDNESS, 30, 0), true);
                    arrow.addCustomEffect(new PotionEffect(PotionEffectType.GLOWING, 1200, 0), true);
                    if (Objects.equals(Kit.equippedKits.get(damages.getUniqueId()).name, name)) {
                        Player hit = (Player) e.getHitEntity();
                        //check if players aren't on the same team
                        if (TeamController.getTeam(damages.getUniqueId())
                                != TeamController.getTeam(hit.getUniqueId())) {
                            arrow.setDamage(5);
                            ((Player) arrow.getShooter()).getInventory().addItem(comboPoint);
                        }
                    }
                }
            } else if(arrow.getShooter() instanceof Player && !(e.getHitEntity() instanceof Player)){
                arrow.setDamage(10);
            }
        }
    }

    public void healPlayer(Player toHeal, int healed) {
        if ((baseHealth + healed) > baseHealth) {
            toHeal.setHealth(baseHealth);
        } else {
            toHeal.setHealth(toHeal.getHealth() + healed);
        }
    }

    /**
     *
     * @param damager the player who is damaging the target
     * @param target the target to deal damage to
     * @param amount the amount of combo points used
     */
    public void gougedDamage(Player damager, Player target, int amount) {
        int damage = 25 + (25 * amount);
        int duration = 40 + (10 * amount);
        target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, duration, 4));
        target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, duration, 0));
        target.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, duration, 0));
        AssistKill.addDamager(target.getUniqueId(), damager.getUniqueId(), damage);
        healPlayer(damager, damage);

        //remove used up combo points
        removeComboPoints(damager);
        if (isShadow.contains(damager.getUniqueId())) {
            mythicParticleQuit(damager);
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

    public void removeComboPoints(Player p) {
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
     *
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
    @EventHandler (ignoreCancelled = true)
    public void gougeDamage(PlayerInteractEntityEvent ed) {
        if (ed.getRightClicked() instanceof Player) {
            Player p = ed.getPlayer();
            Player hit = (Player) ed.getRightClicked();
            if (Objects.equals(Kit.equippedKits.get(p.getUniqueId()).name, name)) {
                if (p.getCooldown(gouge.getType()) == 0) {

                    Location hitLoc = hit.getLocation();
                    Location damagerLoc = p.getLocation();

                    // Basically what happens here is you check whether the player
                    // is not looking at you at all (so having their back aimed at you.)
                    if (damagerLoc.getYaw() <= hitLoc.getYaw() + 60 && damagerLoc.getYaw() >= hitLoc.getYaw() - 60
                            && canGouge) {

                        if (p.getInventory().getItemInMainHand().getType() != Material.NETHERITE_INGOT) {
                            return;
                        }
                        p.setCooldown(gouge.getType(), 100);

                        ed.setCancelled(true);
                        hit.sendMessage(ChatColor.RED + "You were gouged by " + p.getName());
                        p.sendMessage(ChatColor.RED + "You gouged " + hit.getName());
                        if (p.getInventory().contains(Material.GLOWSTONE_DUST)) {
                            for (ItemStack item : p.getInventory().getContents()) {
                                if (item == null) {
                                    return;
                                }
                                if (item.getType().equals(Material.GLOWSTONE_DUST)) {
                                    int amount = item.getAmount();
                                    gougedDamage(p, hit, amount);
                                }
                            }
                        } else {
                            gougedDamage(p, hit, 0);
                        }
                    }
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
    @EventHandler (priority = EventPriority.HIGHEST)
    public void onClickEnderchest(EnderchestEvent event) {
        if (Objects.equals(Kit.equippedKits.get(event.getPlayer().getUniqueId()).name, name)) {
            event.setCancelled(true);
            Player p = event.getPlayer();

            isShadow.remove(p.getUniqueId());
            hasPoisonedWeapons.remove(p.getUniqueId());
            mythicParticleQuit(p);
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
        kitLore.add(Component.text(" "));
        kitLore.add(Component.text("Effects:", NamedTextColor.DARK_PURPLE));
        kitLore.add(Component.text("- Speed II", NamedTextColor.GRAY));
        kitLore.add(Component.text("- Haste II", NamedTextColor.GRAY));
        kitLore.add(Component.text("- Night Vision I", NamedTextColor.GRAY));
        kitLore.add(Component.text("- Jump Boost I", NamedTextColor.GRAY));
        kitLore.add(Component.text(" "));
        kitLore.add(Component.text("Active:", NamedTextColor.GOLD));
        kitLore.add(Component.text("- Can throw track arrows", NamedTextColor.GRAY));
        kitLore.add(Component.text("- Can perform shadowstep (grants invisibility)", NamedTextColor.GRAY));
        kitLore.add(Component.text("- Can temporarily enhance their dagger", NamedTextColor.GRAY));
        kitLore.add(Component.text("with poison", NamedTextColor.GRAY));
        kitLore.add(Component.text("- When behind enemies can gauge them, which", NamedTextColor.GRAY));
        kitLore.add(Component.text("stuns them temporarily and grants a bit of healing", NamedTextColor.GRAY));
        kitLore.add(Component.text(" "));
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
