package me.huntifi.castlesiege.kits.kits.in_development;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.events.timed.BarCooldown;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.kits.kits.DonatorKit;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.maps.NameTag;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Rogue extends DonatorKit implements Listener {

    public static ArrayList<Player> hasPoisonedWeapons = new ArrayList<>();

    public static ArrayList<Player> isShadow = new ArrayList<>();
    private final ItemStack gouge;

    private final ItemStack poison;

    private final ItemStack shadowstep;

    private final ItemStack shadowleap;

    private final ItemStack trackArrow;

    private final ItemStack comboPoint;

    public Rogue() {
        super("Rogue", 240, 15, 10000, 1);
        super.canSeeHealth = true;

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Weapon
        es.hotbar[0] = ItemCreator.weapon(new ItemStack(Material.NETHERITE_SWORD),
                ChatColor.DARK_GRAY + "Dagger", null, null, 22);
        // Voted weapon
        es.votedWeapon = new Tuple<>(
                ItemCreator.weapon(new ItemStack(Material.NETHERITE_SWORD),
                        ChatColor.DARK_GRAY + "Dagger",
                        Collections.singletonList(ChatColor.AQUA + "- voted: +2 damage"),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), 24),
                0);

        // Gouge
        gouge = ItemCreator.weapon(new ItemStack(Material.NETHERITE_INGOT),
                ChatColor.GOLD + "Gouge", Arrays.asList("",
                        ChatColor.AQUA + "This attack uses combo points, the",
                        ChatColor.AQUA + "more combo points the stronger the attack.", "",
                        ChatColor.AQUA + "This stuns and damages the target but",
                        ChatColor.AQUA + "in order for it to work you have to be behind",
                        ChatColor.AQUA + "your target.", "",
                        ChatColor.AQUA + "If you consume combo points you will deal extra",
                        ChatColor.AQUA + "damage depending on how many you have, on" +
                        ChatColor.AQUA + "top of that you will be",
                        ChatColor.AQUA + "healed 10hp per combo point consumed."),
                null, 1);
        es.hotbar[1] = gouge;

        poison = poisonPotion();
        es.hotbar[2] = poison;

        shadowstep = ItemCreator.item(new ItemStack(Material.NETHERITE_BOOTS),
                ChatColor.DARK_GRAY + "Shadowstep",
                Arrays.asList(ChatColor.AQUA + "", ChatColor.YELLOW + "" + ChatColor.BOLD + "Right click to activate.",
                        ChatColor.AQUA + "", "You move through the shadows, invisible to everyone.",
                        "Whilst you shadowstep you can not attack targets.",
                        "This ability lasts 6 seconds and has a cool-down of 13 seconds."),
                null);
        es.hotbar[3] = shadowstep;

        shadowleap = ItemCreator.item(new ItemStack(Material.DRIED_KELP),
                ChatColor.DARK_GRAY + "Shadowleap",
                Arrays.asList(ChatColor.AQUA + "", ChatColor.YELLOW + "" + ChatColor.BOLD + "Right click to activate.",
                        ChatColor.AQUA + "", "You move through the shadows, invisible to everyone.",
                        "Whilst you shadowleap you can not attack targets.",
                        "You get jump boost 5 so you can easily jump on top of roofs and towers.",
                        "This ability can be used as an extension to shadowstep,",
                        "lasts for 6 seconds and has a cool-down of 20 seconds."),
                null);
        es.hotbar[4] = shadowleap;

        trackArrow = ItemCreator.item(new ItemStack(Material.TIPPED_ARROW, 5),
                ChatColor.YELLOW + "Track Arrow",
                Arrays.asList(ChatColor.AQUA + "", ChatColor.YELLOW + "" + ChatColor.BOLD + "Right click to throw!",
                        ChatColor.AQUA + "", "Hitting enemy targets with this gives them",
                        "glowing for a minute and stuns them briefly.",
                        "Allowing you enough time to strike or escape.",
                        "hitting enemies with this ability also awards you ",
                        "with a combo point."),
                null);
        PotionMeta potionMeta = (PotionMeta) trackArrow.getItemMeta();
        assert potionMeta != null;
        potionMeta.setColor(Color.YELLOW);
        trackArrow.setItemMeta(potionMeta);
        potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.BLINDNESS, 25, 0), true);
        potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.GLOWING, 1200, 0), true);
        trackArrow.setItemMeta(potionMeta);

        es.hotbar[5] = trackArrow;

        comboPoint = ItemCreator.item(new ItemStack(Material.GLOWSTONE_DUST),
                ChatColor.LIGHT_PURPLE + "Combo Point",
                Arrays.asList(ChatColor.AQUA + "", ChatColor.YELLOW + "" + ChatColor.BOLD + "Ability Power Currency",
                        ChatColor.AQUA + "", "This currency can be used to perform a stronger gouge.",
                        "You can get combo points by killing and",
                        "hitting enemies with track arrows."),
                null);

        // Chestplate
        es.chest = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE),
                ChatColor.GREEN + "Leather Chestplate", null, null,
                Color.fromRGB(15, 15, 15));

        // Leggings
        es.legs = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_LEGGINGS),
                ChatColor.DARK_GRAY + "Leather Leggings", null, null,
                Color.fromRGB(19, 19, 19));

        // Boots
        es.feet = ItemCreator.item(new ItemStack(Material.NETHERITE_BOOTS),
                ChatColor.DARK_GRAY + "Rogue Boots", Arrays.asList(ChatColor.AQUA + "- rogue: Feather Falling X"),
                Arrays.asList(new Tuple<>(Enchantment.PROTECTION_FALL, 10)));
        // Voted Boots
        es.votedFeet = ItemCreator.item(new ItemStack(Material.NETHERITE_BOOTS),
                ChatColor.GREEN + "Leather Boots",
                Arrays.asList(ChatColor.AQUA + "- voted: Depth Strider II", ChatColor.AQUA + "- rogue: Feather Falling X"),
                Arrays.asList(new Tuple<>(Enchantment.DEPTH_STRIDER, 3), new Tuple<>(Enchantment.PROTECTION_FALL, 10)));



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
                ChatColor.AQUA + "for the next 20 seconds. This has a 60 second cooldown."));
        itemStack.setItemMeta(potionMeta);

        return itemStack;
    }

    /**
     *
     * @param e event triggered by right clicking mode switch button.
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
                if (p.getInventory().getItemInMainHand().equals(poisonPotion())) {
                    applyPoison(p);
                }
                else if (p.getInventory().getItemInMainHand().equals(shadowstep.getType())) {
                    shadowstepAbility(p);
                }
                else if (p.getInventory().getItemInMainHand().equals(shadowleap.getType())) {
                    shadowleapAbility(p);
                }
            }
        }
    }

    /**
     * Disguise the player as a vex and make them invisible to prevent footsteps for a set amount of time.
     * @param p The player to (un)disguise
     */
    protected void shadowstepAbility(Player p) {
        if (p.getCooldown(shadowstep.getType()) == 0) {
            p.setCooldown(shadowstep.getType(), 260);
            MobDisguise mobDisguise = new MobDisguise(DisguiseType.VEX);
            disguise(p, mobDisguise);
            isShadow.add(p);
            p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999, 0));
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (Objects.equals(Kit.equippedKits.get(p.getUniqueId()).name, name)) {
                        isShadow.remove(p);
                        if (DisguiseAPI.isDisguised(p)) {
                            DisguiseAPI.undisguiseToAll(p);
                            NameTag.give(p);
                        }
                        for (PotionEffect effect : p.getActivePotionEffects()) {
                            if ((effect.getType().getName().equals(PotionEffectType.SPEED.getName()) && effect.getAmplifier() == 2)
                                    || (effect.getType().getName().equals(PotionEffectType.INVISIBILITY.getName()) && effect.getAmplifier() == 0)
                                    || (effect.getType().getName().equals(PotionEffectType.JUMP.getName()) && effect.getAmplifier() == 4)) {
                                p.removePotionEffect(effect.getType());
                            }
                        }
                    }
                }
            }.runTaskLater(Main.plugin, 120);
        } else {
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                    ChatColor.RED + "This ability is still under cool-down."));
        }
    }

    /**
     * Disguise the player as a vex and make them invisible to prevent footsteps for a set amount of time. + jump boost
     * @param p The player to (un)disguise
     */
    protected void shadowleapAbility(Player p) {
        if (p.getCooldown(shadowleap.getType()) == 0 && isShadow.contains(p)) {
            p.setCooldown(shadowleap.getType(), 400);
            p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 999999, 4));
            p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999, 2));
        } else if (!isShadow.contains(p)) {
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                    ChatColor.RED + "You can only use this ability when shadowstepping!"));
        } else {
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                    ChatColor.RED + "This ability is still under cool-down."));
        }
    }

    public void applyPoison(Player p) {
        if (p.getCooldown(poisonPotion().getType()) == 0) {
            hasPoisonedWeapons.add(p);
            p.setCooldown(poisonPotion().getType(), 1200);
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (Objects.equals(Kit.equippedKits.get(p.getUniqueId()).name, name)) {
                        hasPoisonedWeapons.remove(p);
                    }
                }
            }.runTaskLater(Main.plugin, 400);
        }
    }

    public void dealPoisonDamage(Player target, Player damager) {
        if (hasPoisonedWeapons.contains(damager) && Objects.equals(Kit.equippedKits.get(damager.getUniqueId()).name, name)) {
            target.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 260, 0));
        }
    }

    public void dealPoisonDamageToHorse(Horse target, Player damager) {
        if (hasPoisonedWeapons.contains(damager) && Objects.equals(Kit.equippedKits.get(damager.getUniqueId()).name, name)) {
            target.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 260, 0));
        }
    }

    /**
     * Cause poison damage to opponents and horses when the poison ability is activated.
     * @param e The event called when hitting another player
     */
    @EventHandler (ignoreCancelled = true)
    public void onAttack(EntityDamageByEntityEvent e) {
        if ((e.getEntity() instanceof Player && e.getDamager() instanceof Player && isShadow.contains(e.getDamager()))) {
            e.setCancelled(true);
        }
        if (!(e.getEntity() instanceof Player && e.getDamager() instanceof Player)) {
            return;
        } else if (e.getEntity() instanceof Horse && e.getDamager() instanceof Player) {
            Player player = (Player) e.getDamager();
            if (Objects.equals(Kit.equippedKits.get(player.getUniqueId()).name, name)) {
                Material item = player.getInventory().getItemInMainHand().getType();
                if (item == Material.NETHERITE_SWORD) {
                    dealPoisonDamageToHorse((Horse) e.getEntity(), player);
                }
            }
        }

        Player player = (Player) e.getDamager();
        if (Objects.equals(Kit.equippedKits.get(player.getUniqueId()).name, name)) {

            // Rogue attacked an enemy player
            Material item = player.getInventory().getItemInMainHand().getType();
            if (item == Material.NETHERITE_SWORD) {
                dealPoisonDamage((Player) e.getEntity(), player);
            }
        }
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
                if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (cooldown == 0) {
                        stick.setAmount(stick.getAmount() - 1);
                        p.setCooldown(Material.TIPPED_ARROW, 20);
                        p.launchProjectile(Arrow.class).setVelocity(p.getLocation().getDirection().multiply(4.0));
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
                arrow.addCustomEffect(new PotionEffect(PotionEffectType.GLOWING, 1200, 0), true);
                if (Objects.equals(Kit.equippedKits.get(damages.getUniqueId()).name, name)) {
                    arrow.setDamage(1);
                    ((Player) arrow.getShooter()).getInventory().addItem(comboPoint);
                }
            } else if(arrow.getShooter() instanceof Player && !(e.getHitEntity() instanceof Player)){
                arrow.setDamage(25);
            }
        }
    }
}
