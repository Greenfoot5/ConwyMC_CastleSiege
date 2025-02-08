package me.greenfoot5.castlesiege.kits.kits.coin_kits;

import io.lumine.mythic.bukkit.BukkitAPIHelper;
import me.greenfoot5.castlesiege.Main;
import me.greenfoot5.castlesiege.database.UpdateStats;
import me.greenfoot5.castlesiege.events.combat.InCombat;
import me.greenfoot5.castlesiege.kits.items.CSItemCreator;
import me.greenfoot5.castlesiege.kits.items.EquipmentSet;
import me.greenfoot5.castlesiege.kits.kits.CoinKit;
import me.greenfoot5.castlesiege.kits.kits.Kit;
import me.greenfoot5.castlesiege.maps.TeamController;
import me.greenfoot5.conwymc.data_types.Tuple;
import me.greenfoot5.conwymc.util.Messenger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Paladin extends CoinKit implements Listener {

    private static final int health = 400;
    private static final double regen = 8;
    private static final double meleeDamage = 33;
    private static final int ladderCount = 4;
    private static final int blessingCooldown = 500;
    private static int blockAmount = 8;
    private final ItemStack shield;

    private final BukkitAPIHelper mythicMobsApi = new BukkitAPIHelper();

    /**
     * Creates a new Paladin
     */
    public Paladin() {
        super("Paladin", health, regen, Material.GOLDEN_AXE);

        super.canSeeHealth = true;
        super.kbResistance = 2;

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();

        // Weapon
        es.hotbar[0] = CSItemCreator.weapon(new ItemStack(Material.GOLDEN_AXE),
                Component.text("Holy Hammer", NamedTextColor.GOLD),
                List.of(Component.empty(),
                        Component.text("33 Melee Damage", NamedTextColor.YELLOW)),
                null, meleeDamage);
        // Voted Weapon
        es.votedWeapon = new Tuple<>(
                CSItemCreator.weapon(new ItemStack(Material.GOLDEN_AXE),
                        Component.text("Holy Hammer", NamedTextColor.GOLD),
                        List.of(Component.empty(),
                                Component.text("35 Melee Damage", NamedTextColor.DARK_GREEN),
								Component.text("⁎ Voted: +2 Melee Damage", NamedTextColor.GOLD)),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOTING, -1)), meleeDamage + 2),
                0);

        // Weapon
        shield = CSItemCreator.weapon(new ItemStack(Material.SHIELD, 1),
                Component.text("Blessed Shield", NamedTextColor.GOLD),
                List.of(Component.empty(),
                        Component.text("- 10 DMG", NamedTextColor.DARK_GREEN),
                        Component.text("◆ Knockback I", NamedTextColor.DARK_PURPLE),
                        Component.text("<< Right Click To Block >>", NamedTextColor.DARK_GRAY),
                        Component.text("Can block up to 8 times before", NamedTextColor.GRAY),
                        Component.text("the cooldown activates.", NamedTextColor.GRAY),
                        Component.text("Blesses you and your nearby", NamedTextColor.GRAY),
                        Component.text("allies, when the cooldown activates.", NamedTextColor.GRAY)),
                Collections.singletonList(new Tuple<>(Enchantment.KNOCKBACK, 0)) , 10);
        es.offhand = shield;

        // Chestplate
        es.chest = CSItemCreator.item(new ItemStack(Material.GOLDEN_CHESTPLATE),
                Component.text("Blessed Chestplate", NamedTextColor.GOLD),
                List.of(Component.text("» Diamond Dune Trim", NamedTextColor.DARK_GRAY),
                        Component.empty(),
                        Component.text(health + " HP", NamedTextColor.YELLOW),
                        Component.text(regen + " Regen", NamedTextColor.YELLOW)), null);
        ItemMeta chest = es.chest.getItemMeta();
        ArmorMeta chestMeta = (ArmorMeta) chest;
        assert chest != null;
        ArmorTrim chestTrim = new ArmorTrim(TrimMaterial.DIAMOND, TrimPattern.DUNE);
        ((ArmorMeta) chest).setTrim(chestTrim);
        es.chest.setItemMeta(chestMeta);

        // Leggings
        es.legs = CSItemCreator.item(new ItemStack(Material.IRON_LEGGINGS),
                Component.text("Blessed Iron Leggings", NamedTextColor.GOLD),
                List.of(Component.text("» Gold Eye Trim", NamedTextColor.DARK_GRAY),
                        Component.empty(),
                        Component.text(health + " HP", NamedTextColor.YELLOW),
                        Component.text(regen + " Regen", NamedTextColor.YELLOW)), null);
        ItemMeta legs = es.legs.getItemMeta();
        ArmorMeta legsMeta = (ArmorMeta) legs;
        assert legs != null;
        ArmorTrim legsTrim = new ArmorTrim(TrimMaterial.GOLD, TrimPattern.EYE);
        legsMeta.setTrim(legsTrim);
        es.legs.setItemMeta(legsMeta);

        // Boots
        es.feet = CSItemCreator.item(new ItemStack(Material.DIAMOND_BOOTS),
                Component.text("Paladin Boots", NamedTextColor.GOLD),
                List.of(Component.text("» Gold Coast Trim", NamedTextColor.DARK_GRAY),
                        Component.empty(),
                        Component.text(health + " HP", NamedTextColor.YELLOW),
                        Component.text(regen + " Regen", NamedTextColor.YELLOW)), null);
        // Voted Boots
        es.votedFeet = CSItemCreator.item(new ItemStack(Material.DIAMOND_BOOTS),
                Component.text("Paladin Boots", NamedTextColor.GOLD),
                List.of(Component.text("» Gold Coast Trim", NamedTextColor.DARK_GRAY),
                        Component.empty(),
                        Component.text(health + " HP", NamedTextColor.YELLOW),
                        Component.text(regen + " Regen", NamedTextColor.YELLOW),
                        Component.empty(),
                        Component.text("⁎ Voted: Depth Strider II", NamedTextColor.DARK_AQUA)),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)));
        ItemMeta boots = es.feet.getItemMeta();
        ArmorMeta bootsMeta = (ArmorMeta) boots;
        assert boots != null;
        ArmorTrim bootsTrim = new ArmorTrim(TrimMaterial.GOLD, TrimPattern.COAST);
        bootsMeta.setTrim(bootsTrim);
        es.feet.setItemMeta(bootsMeta);
        es.votedFeet.setItemMeta(bootsMeta);

        // divine blessing
        ItemStack divine = CSItemCreator.weapon(new ItemStack(Material.BOOK, 3),
                Component.text("Divine Blessing", NamedTextColor.GOLD),
                Arrays.asList(Component.empty(),
                        Component.text("<< Right Click To Activate >>", NamedTextColor.DARK_GRAY),
                        Component.text("Give yourself regeneration VI and give", NamedTextColor.GRAY),
                        Component.text("your allies in a 5 block radius of you", NamedTextColor.GRAY),
                        Component.text("regeneration V.", NamedTextColor.GRAY),
                        Component.text("This effect lasts 8 seconds,", NamedTextColor.GRAY),
                        Component.text("25s Cooldown", NamedTextColor.GOLD)),
                null, 1);
        es.hotbar[1] = divine;

        // Ladders
        es.hotbar[2] = new ItemStack(Material.LADDER, ladderCount);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, ladderCount + 2), 2);

        super.equipment = es;

        // Perm Potion Effect
        super.potionEffects.add(new PotionEffect(PotionEffectType.MINING_FATIGUE, 999999, 0, true, false));
    }

    /**
     * @param paladin the paladin to remove and give back the shield to.
     * This is to stop the paladin from blocking even when the cooldown is active.
     */
    public void tempRemoveShield(Player paladin) {
        paladin.getInventory().setItemInOffHand(null);
        new BukkitRunnable() {
            @Override
            public void run() {
                paladin.getInventory().setItemInOffHand(shield);
            }
        }.runTaskLater(Main.plugin, 10);
    }

    /**
     * This is basically a shield cool-down mechanism/method.
     * @param shielder the paladin holding the shield.
     */
    public void shieldMechanism(Player shielder) {
        if (Objects.equals(Kit.equippedKits.get(shielder.getUniqueId()).name, name)) {
            if (shielder.isBlocking() && blockAmount != 0) {
                blockAmount--;
                Messenger.sendActionInfo(blockAmount + " blocks left", shielder);
            } else if (shielder.isBlocking() && blockAmount <= 1) {
                shielder.setCooldown(Material.SHIELD, 300);
                tempRemoveShield(shielder);
                blockAmount = 8;
                for (UUID uuid : TeamController.getActivePlayers()) {
                    bless(shielder, uuid);
                }
            }
        }
    }

    /**
     *
     * @param e when a paladin gets hit whilst blocking.
     */
    @EventHandler
    public void combatShielding(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player) {
            if (Objects.equals(Kit.equippedKits.get(e.getEntity().getUniqueId()).name, name)) {
                Player p = (Player) e.getEntity();
                shieldMechanism(p);
            }
        }
    }
    /**
     *
     * @param e when a paladin gets hit by projectiles whilst blocking.
     */
    @EventHandler
    public void combatShielding2(ProjectileHitEvent e) {
        if (e.getHitEntity() instanceof Player) {
            if (Objects.equals(Kit.equippedKits.get(e.getHitEntity().getUniqueId()).name, name)) {
                Player p = (Player) e.getHitEntity();
                shieldMechanism(p);
            }
        }
    }
    /**
     *
     * @param e Paladin tries to block whilst the cooldown is active.
     */
    @EventHandler
    public void shielding(PlayerInteractEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();
        if (Objects.equals(Kit.equippedKits.get(uuid).name, name)) {
            Player p = e.getPlayer();
            if (p.getCooldown(Material.SHIELD) != 0 &&
                    (p.getInventory().getItemInMainHand().getType() == Material.SHIELD || p.getInventory().getItemInOffHand().getType() == Material.SHIELD)) {
                e.setCancelled(true);
            }
            if (!InCombat.isPlayerInCombat(e.getPlayer().getUniqueId())) {
                blockAmount = 8;
            }
        }
    }

    /**
     * @param blesser The player performing the blessing
     * @param blessedUUID The uuid of the player being blessed
     */
    private void bless(Player blesser, UUID blessedUUID) {
        Player blessed = Bukkit.getPlayer(blessedUUID);
        if (blessed == null)
            return;

        Messenger.sendActionInfo("You blessed your surroundings!", blesser);
        Bukkit.getScheduler().runTask(Main.plugin, () -> {
            blesser.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 160, 5, true, false));
            mythicMobsApi.castSkill(blesser, "PaladinBlessingEffect");
        });
        if (TeamController.getTeam(blesser.getUniqueId()) == TeamController.getTeam(blessed.getUniqueId())
                && blesser.getLocation().distanceSquared(blessed.getLocation()) <= 5 * 5
                && blesser != blessed) {
            AttributeInstance healthAttribute = blessed.getAttribute(Attribute.MAX_HEALTH);
            assert healthAttribute != null;
            //Paladin doesn't get a heal for blessing someone who is full health.
            if (blessed.getHealth() != healthAttribute.getBaseValue()) {
                Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () ->
                        UpdateStats.addHeals(blesser.getUniqueId(), 1));
            }
            Bukkit.getScheduler().runTask(Main.plugin, () ->
                    blessed.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 160, 4, true, false)));
        }
    }



    /**
     * Activate the spearman ability of throwing a spear
     * @param e The event called when right-clicking with a stick
     */
    @EventHandler
    public void clickBlessing(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();

        // Incorrect kit
        if (!Objects.equals(Kit.equippedKits.get(uuid).name, name))
            return;

        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        ItemStack book;
        if (e.getHand() == EquipmentSlot.HAND)
            book = p.getInventory().getItemInMainHand();
        else if (e.getHand() == EquipmentSlot.OFF_HAND)
            book = p.getInventory().getItemInOffHand();
        else
            return;

        int cooldown = p.getCooldown(Material.BOOK);

        if (!book.getType().equals(Material.BOOK))
            return;

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(uuid)) {
            return;
        }

        if (cooldown == 0) {
            book.setAmount(book.getAmount() - 1);
            p.setCooldown(Material.BOOK, blessingCooldown);
            for (UUID near : TeamController.getActivePlayers()) {
                bless(p, near);
            }
        }
    }

    /**
     * @return The lore to add to the kit gui item
     */
    @Override
    public ArrayList<Component> getGuiDescription() {
        ArrayList<Component> kitLore = new ArrayList<>();
        kitLore.add(Component.text("A tank/support kit that can", NamedTextColor.GRAY));
        kitLore.add(Component.text("bless allies around it", NamedTextColor.GRAY));
        kitLore.addAll(getBaseStats(health, regen, meleeDamage, ladderCount));
        kitLore.add(Component.empty());
        kitLore.add(Component.text("Effects:", NamedTextColor.DARK_PURPLE));
        kitLore.add(Component.text("- Mining Fatigue I", NamedTextColor.GRAY));
        kitLore.add(Component.empty());
        kitLore.add(Component.text("Active:", NamedTextColor.GOLD));
        kitLore.add(Component.text("- Can cast a blessing that gives", NamedTextColor.GRAY));
        kitLore.add(Component.text("regen VI to itself and regen V to all", NamedTextColor.GRAY));
        kitLore.add(Component.text("allies in a 5 block radius for 8 seconds", NamedTextColor.GRAY));
        kitLore.add(Component.empty());
        kitLore.add(Component.text("Passive:", NamedTextColor.DARK_GREEN));
        kitLore.add(Component.text("- Can see players' health", NamedTextColor.GRAY));
        kitLore.add(Component.text("On Shield Cooldown:", NamedTextColor.DARK_GREEN));
        kitLore.add(Component.text("- Bless the paladin and nearby teammates.", NamedTextColor.GRAY));
        return kitLore;
    }
}
