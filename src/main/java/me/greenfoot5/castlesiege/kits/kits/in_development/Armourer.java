package me.greenfoot5.castlesiege.kits.kits.in_development;

import me.greenfoot5.castlesiege.Main;
import me.greenfoot5.castlesiege.database.UpdateStats;
import me.greenfoot5.castlesiege.events.combat.InCombat;
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
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

/**
 * A kit that can improve teammates armour
 */
public class Armourer extends CoinKit implements Listener {

    private static final int health = 230;
    private static final double regen = 10.5;
    private static final double meleeDamage = 30;
    private static final int ladderCount = 4;
    private static final int cooldownTicks = 50;

    public static final ArrayList<Player> cooldown = new ArrayList<>();

    /**
     * Creates a new armourer
     */
    public Armourer() {
        super("Armourer", health, regen, Material.ANVIL);

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();

        // Weapon
        es.hotbar[0] = CSItemCreator.weapon(new ItemStack(Material.NETHERITE_SHOVEL),
                Component.text("Smith's Hammer", NamedTextColor.GREEN), null, null, meleeDamage);
        // Voted Weapon
        es.votedWeapon = new Tuple<>(
                CSItemCreator.weapon(new ItemStack(Material.NETHERITE_SHOVEL),
                        Component.text("Smith's Hammer", NamedTextColor.GREEN),
                        Collections.singletonList(Component.text("⁎ Voted: +2 damage", NamedTextColor.AQUA)),
                        Collections.singletonList(new Tuple<>(Enchantment.KNOCKBACK, 0)), meleeDamage + 2),
                0);

        // Chestplate
        es.chest = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE),
                Component.text("Armourer's Robe", NamedTextColor.GREEN), null, null,
                Color.fromRGB(50, 54, 57));
        ItemMeta chest = es.chest.getItemMeta();
        ArmorMeta chestMeta = (ArmorMeta) chest;
        assert chest != null;
        ArmorTrim chestTrim = new ArmorTrim(TrimMaterial.NETHERITE, TrimPattern.DUNE);
        ((ArmorMeta) chest).setTrim(chestTrim);
        es.chest.setItemMeta(chestMeta);

        // Leggings
        es.legs = CSItemCreator.item(new ItemStack(Material.CHAINMAIL_LEGGINGS),
                Component.text("Armourer's Leggings", NamedTextColor.GREEN), null, null);
        ItemMeta legs = es.legs.getItemMeta();
        ArmorMeta legsMeta = (ArmorMeta) legs;
        assert legs != null;
        ArmorTrim legsTrim = new ArmorTrim(TrimMaterial.IRON, TrimPattern.RAISER);
        legsMeta.setTrim(legsTrim);
        es.legs.setItemMeta(legsMeta);

        // Boots
        es.feet = CSItemCreator.item(new ItemStack(Material.NETHERITE_BOOTS),
                Component.text("Armourer's Boots", NamedTextColor.GREEN), null, null);
        // Voted Boots
        es.votedFeet = CSItemCreator.item(new ItemStack(Material.NETHERITE_BOOTS),
                Component.text("Armourer's Boots", NamedTextColor.GREEN),
                Collections.singletonList(Component.text("⁎ Voted: Depth Strider II", NamedTextColor.AQUA)),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)));
        ItemMeta boots = es.feet.getItemMeta();
        ArmorMeta bootsMeta = (ArmorMeta) boots;
        assert boots != null;
        ArmorTrim bootsTrim = new ArmorTrim(TrimMaterial.IRON, TrimPattern.RAISER);
        bootsMeta.setTrim(bootsTrim);
        es.feet.setItemMeta(bootsMeta);
        es.votedFeet.setItemMeta(bootsMeta);

        // Ladders
        es.hotbar[1] = new ItemStack(Material.LADDER, ladderCount);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, ladderCount + 2), 1);

        super.equipment = es;
    }

    /**
     * Activate the Armourer ability of reinforcing a teammate
     * @param event The event called when clicking on a teammate with a netherite shovel
     */
    @EventHandler
    public void onArmor(PlayerInteractEntityEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
            Player player = event.getPlayer();
            UUID uuid = player.getUniqueId();

            // Prevent using in lobby
            if (InCombat.isPlayerInLobby(uuid)) {
                return;
            }

            PlayerInventory i = player.getInventory();
            Entity q = event.getRightClicked();
            if (Objects.equals(Kit.equippedKits.get(uuid).name, name) &&
                    (i.getItemInMainHand().getType() == Material.NETHERITE_SHOVEL) &&
                    q instanceof Player r &&
                    TeamController.getTeam(uuid) == TeamController.getTeam(q.getUniqueId())
                    && !cooldown.contains((Player) q)) {

                // Apply cooldown
                cooldown.add(r);
                Bukkit.getScheduler().runTaskLaterAsynchronously(Main.plugin, () -> cooldown.remove(r), cooldownTicks);

                // reinforce
                reinforceArmor(r, player);
                Bukkit.getScheduler().runTaskLater(Main.plugin, () ->
                        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_SMITHING_TABLE_USE , 0.5F, 0.5F), 1);
            }
        });
    }

    /**
     *
     * @param item The armour to reinforce, it should be the chestplate of a player.
     * @param smith Who is improving the amour
     * @param target The player whose armour is being reinforced
     * @return The reinforced item
     */
    private ItemStack addDefence(ItemStack item, Player smith, Player target) {
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        AttributeModifier modifier = new AttributeModifier(new NamespacedKey(NamespacedKey.MINECRAFT, "generic.armor"), 2.0, AttributeModifier.Operation.ADD_NUMBER);
        meta.addAttributeModifier(Attribute.ARMOR, modifier);
        AttributeModifier modifier2 = new AttributeModifier(new NamespacedKey(NamespacedKey.MINECRAFT, "generic.armor_toughness"), 0.30, AttributeModifier.Operation.ADD_NUMBER);
        meta.addAttributeModifier(Attribute.ARMOR_TOUGHNESS, modifier2);
        double armor = Objects.requireNonNull(target.getAttribute(Attribute.ARMOR)).getValue();
        if (armor <= 10) {
            meta.lore(Collections.singletonList(Component.text("- Reinforced", NamedTextColor.AQUA)));
            meta.addEnchant(Enchantment.PROTECTION, level(armor), false);
            item.setItemMeta(meta);
            Messenger.sendActionInfo(CSNameTag.mmUsername(smith) + " has reinforced your armor", target);
            Messenger.sendActionInfo("You are reinforcing " + CSNameTag.mmUsername(target) + "'s armor", smith);
            addPotionEffect(smith, new PotionEffect(PotionEffectType.RESISTANCE, 60, 0));
            Bukkit.getScheduler().runTaskLaterAsynchronously(Main.plugin, () ->
                    UpdateStats.addSupports(smith.getUniqueId(), 2), cooldownTicks);
        } else {
            Messenger.sendActionError("You can't reinforce this player anymore as their armor" +
                            " has reached its maximum reinforcement.", smith);
        }
        return item;
    }

    /**
     * @param armor The armour to improve
     * @return The level of improvement to provide
     */
    private int level (double armor) {
        return switch ((int) armor) {
            case 2 -> 0;
            case 4 -> 1;
            case 6 -> 2;
            case 8 -> 3;
            case 10 -> 4;
            default -> 0;
        };
    }

    /**
     * This is basically the main ability for this kit
     * @param target the player to reinforce
     * @param self the smith/Armourer that is reinforcing
     */
    private void reinforceArmor(Player target, Player self) {
        if (target.getInventory().getChestplate() == null) {
            return;
        }
        ItemStack chest = target.getInventory().getChestplate();
                ItemStack upgradedChest = addDefence(chest, self, target);
                target.getInventory().setChestplate(upgradedChest);
    }

    /**
     * Add a potion effect to a player.
     * @param player The player
     * @param potion The potion effect
     */
    private void addPotionEffect(Player player, PotionEffect potion) {
        Bukkit.getScheduler().runTask(Main.plugin, () -> player.addPotionEffect(potion));
    }

    @Override
    public ArrayList<Component> getGuiDescription() {
        ArrayList<Component> description = new ArrayList<>();
        description.add(Component.text("An armorsmith that reinforces", NamedTextColor.GRAY));
        description.add(Component.text("his allies' armor!", NamedTextColor.GRAY));
        description.addAll(getBaseStats(health, regen, meleeDamage, ladderCount));
        description.add(Component.empty());
        description.add(Component.text("Active:", NamedTextColor.GOLD));
        description.add(Component.text("- Can use a smith's hammer to reinforce teammates", NamedTextColor.GRAY));
        description.add(Component.empty());
        description.add(Component.text("Passive:", NamedTextColor.DARK_GREEN));
        description.add(Component.text("- When reinforcing an ally receives resistance I", NamedTextColor.GRAY));
        description.add(Component.text("- Can see a player's health", NamedTextColor.GRAY));
        return description;
    }
}
