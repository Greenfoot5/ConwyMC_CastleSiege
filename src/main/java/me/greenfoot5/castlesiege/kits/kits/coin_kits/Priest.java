package me.greenfoot5.castlesiege.kits.kits.coin_kits;

import io.lumine.mythic.bukkit.BukkitAPIHelper;
import me.greenfoot5.castlesiege.Main;
import me.greenfoot5.castlesiege.database.UpdateStats;
import me.greenfoot5.castlesiege.events.EnderchestEvent;
import me.greenfoot5.castlesiege.events.combat.InCombat;
import me.greenfoot5.castlesiege.kits.items.CSItemCreator;
import me.greenfoot5.castlesiege.kits.items.EquipmentSet;
import me.greenfoot5.castlesiege.kits.kits.CoinKit;
import me.greenfoot5.castlesiege.maps.TeamController;
import me.greenfoot5.castlesiege.misc.CSNameTag;
import me.greenfoot5.conwymc.data_types.Tuple;
import me.greenfoot5.conwymc.util.Messenger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
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
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Priest extends CoinKit implements Listener {

    private static final int health = 210;
    private static final double regen = 8;
    private static final double meleeDamage = 25;
    private static final int ladderCount = 4;
    private static final int blessingCooldown = 500;
    private static final int staffCooldown = 40;
    private final ItemStack holyBook;

    private static UUID blessed;
    private static BukkitRunnable blessing;

    private final BukkitAPIHelper mythicMobsApi = new BukkitAPIHelper();

    /**
     * Creates a new priest
     */
    public Priest() {
        super("Priest", health, regen, Material.SPECTRAL_ARROW);

        super.canSeeHealth = true;

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();

        // Weapon
        es.hotbar[0] = CSItemCreator.weapon(new ItemStack(Material.SPECTRAL_ARROW),
                Component.text("Holy Staff", NamedTextColor.GREEN),
                List.of(Component.empty(),
                        Component.text("Right click to shoot a bolt of light, ", NamedTextColor.BLUE),
                        Component.text("which does damage to enemies", NamedTextColor.BLUE),
                        Component.empty(),
                        Component.text("55 Melee Damage", NamedTextColor.DARK_GREEN)), null, meleeDamage);
        // Voted Weapon
        es.votedWeapon = new Tuple<>(
                CSItemCreator.weapon(new ItemStack(Material.SPECTRAL_ARROW),
                        Component.text("Holy Staff", NamedTextColor.GREEN),
                        List.of(Component.empty(),
                                Component.text("Right click to shoot a bolt of light, ", NamedTextColor.BLUE),
                                Component.text("which does damage to enemies", NamedTextColor.BLUE),
                                Component.empty(),
                                Component.text("55 Melee Damage", NamedTextColor.DARK_GREEN),
								Component.text("⁎ Voted: +2 Melee Damage", NamedTextColor.DARK_AQUA)),
                        Collections.singletonList(new Tuple<>(Enchantment.SMITE, 5)), meleeDamage + 2),
                0);

        // Chestplate
        es.chest = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE),
                Component.text("Priest's robe", NamedTextColor.GREEN),
                List.of(Component.text("» Gold Vex Trim", NamedTextColor.DARK_GRAY),
                        Component.empty(),
                        Component.text(health + " HP", NamedTextColor.DARK_GREEN),
                        Component.text(regen + " Regen", NamedTextColor.DARK_GREEN)), null,
                Color.fromRGB(20, 0, 24));
        ItemMeta chest = es.chest.getItemMeta();
        ArmorMeta chestMeta = (ArmorMeta) chest;
        assert chest != null;
        ArmorTrim chestTrim = new ArmorTrim(TrimMaterial.GOLD, TrimPattern.VEX);
        ((ArmorMeta) chest).setTrim(chestTrim);
        es.chest.setItemMeta(chestMeta);

        // Leggings
        es.legs = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_LEGGINGS),
                Component.text("Priest's Leggings", NamedTextColor.GREEN),
                List.of(Component.text("» Gold Vex Trim", NamedTextColor.DARK_GRAY),
                        Component.empty(),
                        Component.text(health + " HP", NamedTextColor.DARK_GREEN),
                        Component.text(regen + " Regen", NamedTextColor.DARK_GREEN)), null,
                Color.fromRGB(20, 0, 24));
        ItemMeta legs = es.legs.getItemMeta();
        ArmorMeta legsMeta = (ArmorMeta) legs;
        assert legs != null;
        ArmorTrim legsTrim = new ArmorTrim(TrimMaterial.GOLD, TrimPattern.VEX);
        legsMeta.setTrim(legsTrim);
        es.legs.setItemMeta(legsMeta);

        // Boots
        es.feet = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_BOOTS),
                Component.text("Priest's Boots", NamedTextColor.GREEN),
                List.of(Component.text("» Gold Vex Trim", NamedTextColor.DARK_GRAY),
                        Component.empty(),
                        Component.text(health + " HP", NamedTextColor.DARK_GREEN),
                        Component.text(regen + " Regen", NamedTextColor.DARK_GREEN)), null,
                Color.fromRGB(20, 0, 24));
        // Voted Boots
        es.votedFeet = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_BOOTS),
                Component.text("Priest's Boots", NamedTextColor.GREEN),
                List.of(Component.text("» Gold Vex Trim", NamedTextColor.DARK_GRAY),
                        Component.empty(),
                        Component.text(health + " HP", NamedTextColor.DARK_GREEN),
                        Component.text(regen + " Regen", NamedTextColor.DARK_GREEN),
                        Component.empty(),
                        Component.text("⁎ Voted: Depth Strider II", NamedTextColor.DARK_AQUA)),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)),
                Color.fromRGB(20, 0, 24));
        ItemMeta boots = es.feet.getItemMeta();
        ArmorMeta bootsMeta = (ArmorMeta) boots;
        assert boots != null;
        ArmorTrim bootsTrim = new ArmorTrim(TrimMaterial.GOLD, TrimPattern.VEX);
        bootsMeta.setTrim(bootsTrim);
        es.feet.setItemMeta(bootsMeta);
        es.votedFeet.setItemMeta(bootsMeta);

        // Gouge
        holyBook = CSItemCreator.weapon(new ItemStack(Material.BOOK),
                Component.text("Holy Bible", NamedTextColor.GREEN),
                List.of(Component.empty(),
                        Component.text("Select an ally with this holy book.", NamedTextColor.AQUA),
                        Component.text("This ally will receive regeneration III", NamedTextColor.AQUA),
                        Component.text("until you select a different ally.", NamedTextColor.AQUA)),
                null, 1);
        es.hotbar[1] = holyBook;

        // Ladders
        es.hotbar[2] = new ItemStack(Material.LADDER, ladderCount);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, ladderCount + 2), 2);

        super.equipment = es;

        // Perm Potion Effect
        super.potionEffects.add(new PotionEffect(PotionEffectType.MINING_FATIGUE, 999999, 0, true, false));

        // Death Messages
        super.deathMessage[0] = "";
        super.deathMessage[1] = " sent you to heaven";
        super.killMessage[0] = " sent ";
        super.killMessage[1] = " to heaven";
    }


    /**
     * Activate the priest's ability of shooting holy stuff
     * @param e The event called when right-clicking with a spectral arrow
     */
    @EventHandler
    public void clickHolyStaff(PlayerInteractEvent e) {
        if (e.getPlayer() != equippedPlayer)
            return;

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(equippedPlayer.getUniqueId())) {
            return;
        }

        ItemStack staff = equippedPlayer.getInventory().getItemInMainHand();

        if (!staff.getType().equals(Material.SPECTRAL_ARROW))
            return;

        if (e.getAction() == Action.RIGHT_CLICK_AIR
                || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (equippedPlayer.getCooldown(Material.SPECTRAL_ARROW) == 0) {
                equippedPlayer.setCooldown(Material.SPECTRAL_ARROW, staffCooldown);
                mythicMobsApi.castSkill(equippedPlayer ,"PriestSmite", equippedPlayer.getLocation());
            }
        }
    }


    /**
     * Activate the priest holy blessing ability to buff 1 target.
     * @param e The event called when right-clicking with a book
     */
    @EventHandler
    public void clickBible(PlayerInteractEntityEvent e) {
        if (e.getPlayer() != equippedPlayer)
            return;

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(equippedPlayer.getUniqueId())) {
            return;
        }

        ItemStack book = equippedPlayer.getInventory().getItemInMainHand();
        if (!book.getType().equals(Material.BOOK))
            return;

        if (!(e.getRightClicked() instanceof Player)
                || TeamController.getTeam(e.getRightClicked().getUniqueId()) != TeamController.getTeam(equippedPlayer.getUniqueId())) {
            return;
        }

        if (equippedPlayer.getCooldown(Material.BOOK) != 0) {
            return;
        }

        equippedPlayer.setCooldown(Material.BOOK, blessingCooldown);
        bless((Player) e.getRightClicked(), book);
    }

    private void cancelBlessing() {
        if (blessing != null)
            blessing.cancel();
        blessing = null;
    }

    private void bless(Player player, ItemStack book) {

        cancelBlessing();
        blessed = player.getUniqueId();
        assignBook(book);

        blessing = new BukkitRunnable() {
            @Override
            public void run() {
                Player blessedPlayer = Bukkit.getPlayer(blessed);
                Objects.requireNonNull(blessedPlayer).addPotionEffect((new PotionEffect(PotionEffectType.REGENERATION, 200, 3)));
                AttributeInstance healthAttribute = Objects.requireNonNull(blessedPlayer).getAttribute(Attribute.MAX_HEALTH);
                assert healthAttribute != null;
                //Priest doesn't get a heal for blessing someone who is full health.
                if (Objects.requireNonNull(blessedPlayer).getHealth() != healthAttribute.getBaseValue()) {
                    Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> UpdateStats.addHeals(equippedPlayer.getUniqueId(), 1));
                }
                Messenger.sendActionInfo("Your blessing is currently affecting: " + CSNameTag.mmUsername(blessedPlayer), equippedPlayer);
            }
        };

        blessing.runTaskTimer(Main.plugin, 10, 200);
    }

    /**
     * @param event When a player clicks an enderchest
     */
    @EventHandler
    public void onClickEnderchest(EnderchestEvent event) {
        if (blessing != null) {
            assignBook(holyBook);
            event.getPlayer().getInventory().setItem(1, holyBook);
        }
    }


    /**
     * @param book The book being used
     */
    private void assignBook(ItemStack book) {
        if (blessing != null) {
            ItemMeta bootMeta = book.getItemMeta();
            assert bootMeta != null;
            bootMeta.displayName(Objects.requireNonNull(holyBook.getItemMeta().displayName())
                    .append(Component.text(" : ", NamedTextColor.GREEN))
                    .append(Bukkit.getPlayer(blessed).displayName().color(NamedTextColor.AQUA)));
            book.setItemMeta(bootMeta);
        }
    }

    /**
     * @param book The book being used
     */
    private void unassignBook(ItemStack book) {

        ItemMeta bookMeta = book.getItemMeta();
        assert bookMeta != null;
        bookMeta.displayName((Component.text("Holy Bible", NamedTextColor.GREEN)));

        for (ItemStack item : equippedPlayer.getInventory().getContents()) {
            if (item != null && item.getType().equals(Material.BOOK)) {
                item.setItemMeta(bookMeta);
            }
        }
    }

    /**
     * @param e if a player dies remove them from the blessings list.
     */
    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        if (e.getPlayer() == equippedPlayer || e.getPlayer().getUniqueId() == blessed)
            cancelBlessing();
    }

    /**
     * @param e if a player quits remove them from the blessings list.
     */
    @EventHandler
    public void onDisconnect(PlayerQuitEvent e) {
        if (e.getPlayer() == equippedPlayer || e.getPlayer().getUniqueId() == blessed)
            cancelBlessing();
    }

    @Override
    public void unequip() {
        cancelBlessing();
        super.unequip();
    }

    /**
     * @return The lore to add to the kit gui item
     */
    @Override
    public ArrayList<Component> getGuiDescription() {
        ArrayList<Component> kitLore = new ArrayList<>();
        kitLore.add(Component.text("A support kit with the power of", NamedTextColor.GRAY));
        kitLore.add(Component.text("the holy book on their side", NamedTextColor.GRAY));
        kitLore.addAll(getBaseStats(health, regen, meleeDamage, 50, ladderCount, -1));
        kitLore.add(Component.empty());
        kitLore.add(Component.text("Effects:", NamedTextColor.DARK_PURPLE));
        kitLore.add(Component.text("- Mining Fatigue I", NamedTextColor.GRAY));
        kitLore.add(Component.empty());
        kitLore.add(Component.text("Active:", NamedTextColor.GOLD));
        kitLore.add(Component.text("- Can shoot a bolt of light at opponents", NamedTextColor.GRAY));
        kitLore.add(Component.text("- Buffs selected ally with regen IV.", NamedTextColor.GRAY));
        kitLore.add(Component.text("Lasts until another ally is selected", NamedTextColor.GRAY));
        kitLore.add(Component.empty());
        kitLore.add(Component.text("Passive:", NamedTextColor.DARK_GREEN));
        kitLore.add(Component.text("- Can see players' health", NamedTextColor.GRAY));
        return kitLore;
    }
}
