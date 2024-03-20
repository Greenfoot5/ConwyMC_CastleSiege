package me.huntifi.castlesiege.kits.kits.coin_kits;

import io.lumine.mythic.bukkit.BukkitAPIHelper;
import me.huntifi.conwymc.data_types.Tuple;
import me.huntifi.castlesiege.database.UpdateStats;
import me.huntifi.conwymc.util.Messenger;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.CSItemCreator;
import me.huntifi.castlesiege.kits.kits.CoinKit;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.maps.TeamController;
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
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

public class Paladin extends CoinKit implements Listener {

    private static final int health = 400;
    private static final double regen = 8;
    private static final double meleeDamage = 33;
    private static final int ladderCount = 4;
    private static final int blessingCooldown = 500;

    public Paladin() {
        super("Paladin", health, regen, Material.GOLDEN_AXE);

        super.canSeeHealth = true;
        super.kbResistance = 2;

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();

        // Weapon
        es.hotbar[0] = CSItemCreator.weapon(new ItemStack(Material.GOLDEN_AXE),
                Component.text("Holy Hammer", NamedTextColor.GREEN), null, null, meleeDamage);
        // Voted Weapon
        es.votedWeapon = new Tuple<>(
                CSItemCreator.weapon(new ItemStack(Material.GOLDEN_AXE),
                        Component.text("Holy Hammer", NamedTextColor.GREEN),
                        Collections.singletonList(Component.text("- voted: +2 damage", NamedTextColor.AQUA)),
                        Collections.singletonList(new Tuple<>(Enchantment.DAMAGE_UNDEAD, 5)), meleeDamage + 2),
                0);

        // Weapon
        es.offhand = CSItemCreator.weapon(new ItemStack(Material.SHIELD, 1),
                Component.text("Blessed Shield", NamedTextColor.GREEN),
                Collections.singletonList(Component.text("Right-click block.", NamedTextColor.AQUA)),
                Collections.singletonList(new Tuple<>(Enchantment.KNOCKBACK, 0)) , 10);

        // Chestplate
        es.chest = CSItemCreator.item(new ItemStack(Material.GOLDEN_CHESTPLATE),
                Component.text("Blessed Chestplate", NamedTextColor.GREEN), null, null);
        ItemMeta chest = es.chest.getItemMeta();
        ArmorMeta chestMeta = (ArmorMeta) chest;
        assert chest != null;
        ArmorTrim chestTrim = new ArmorTrim(TrimMaterial.DIAMOND, TrimPattern.DUNE);
        ((ArmorMeta) chest).setTrim(chestTrim);
        es.chest.setItemMeta(chestMeta);

        // Leggings
        es.legs = CSItemCreator.item(new ItemStack(Material.IRON_LEGGINGS),
                Component.text("Blessed Iron Leggings", NamedTextColor.GREEN), null, null);
        ItemMeta legs = es.legs.getItemMeta();
        ArmorMeta legsMeta = (ArmorMeta) legs;
        assert legs != null;
        ArmorTrim legsTrim = new ArmorTrim(TrimMaterial.GOLD, TrimPattern.EYE);
        legsMeta.setTrim(legsTrim);
        es.legs.setItemMeta(legsMeta);

        // Boots
        es.feet = CSItemCreator.item(new ItemStack(Material.DIAMOND_BOOTS),
                Component.text("Paladin Boots", NamedTextColor.GREEN), null, null);
        // Voted Boots
        es.votedFeet = CSItemCreator.item(new ItemStack(Material.DIAMOND_BOOTS),
                Component.text("Paladin Boots", NamedTextColor.GREEN),
                Collections.singletonList(Component.text("- voted: Depth Strider II", NamedTextColor.AQUA)),
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
                        Component.text("Give yourself regeneration VI and give", NamedTextColor.AQUA),
                        Component.text("your allies in a 5 block radius of you", NamedTextColor.AQUA),
                        Component.text("regeneration V.", NamedTextColor.AQUA),
                        Component.text("This effect lasts 8 seconds, ", NamedTextColor.AQUA),
                        Component.text("has a cooldown of 25 seconds.", NamedTextColor.AQUA)),
                null, 1);
        es.hotbar[1] = divine;

        // Ladders
        es.hotbar[2] = new ItemStack(Material.LADDER, ladderCount);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, ladderCount + 2), 2);

        super.equipment = es;

        // Perm Potion Effect
        super.potionEffects.add(new PotionEffect(PotionEffectType.SLOW_DIGGING, 999999, 0, true, false));

    }

    public void bless(Player blesser, Player blessed) {

        blesser.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 160, 5, true, false));
        Messenger.sendActionInfo("You blessed your surroundings!", blesser);
        mythicParticle(blesser);

        if (TeamController.getTeam(blesser.getUniqueId()) == TeamController.getTeam(blessed.getUniqueId())
                && blesser.getLocation().distance(blessed.getLocation()) <= 5 && blesser != blessed) {

            AttributeInstance healthAttribute = blessed.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            assert healthAttribute != null;
            //Paladin doesn't get a heal for blessing someone who is full health.
            if (blessed.getHealth() != healthAttribute.getBaseValue()) {
                UpdateStats.addHeals(blesser.getUniqueId(), 1);
            }
            blessed.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 160, 4, true, false));
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
        ItemStack book = p.getInventory().getItemInMainHand();
        int cooldown = p.getCooldown(Material.BOOK);

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(uuid)) {
            return;
        }

        if (Objects.equals(Kit.equippedKits.get(uuid).name, name)) {
            if (book.getType().equals(Material.BOOK)) {
                if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (cooldown == 0) {
                        book.setAmount(book.getAmount() - 1);
                        p.setCooldown(Material.BOOK, blessingCooldown);
                        for (Player near : Bukkit.getOnlinePlayers()) {
                            bless(p, near);
                        }
                    }
                }
            }
        }
    }

    public void mythicParticle(Player p) {
        BukkitAPIHelper mythicMobsApi = new BukkitAPIHelper();
        mythicMobsApi.castSkill(p,"PaladinBlessingEffect");
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
        return kitLore;
    }
}
