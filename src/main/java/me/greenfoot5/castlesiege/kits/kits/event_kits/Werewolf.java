package me.greenfoot5.castlesiege.kits.kits.event_kits;

import me.greenfoot5.castlesiege.Main;
import me.greenfoot5.castlesiege.database.CSActiveData;
import me.greenfoot5.castlesiege.events.combat.InCombat;
import me.greenfoot5.castlesiege.kits.items.CSItemCreator;
import me.greenfoot5.castlesiege.kits.items.EquipmentSet;
import me.greenfoot5.castlesiege.kits.kits.EventKit;
import me.greenfoot5.castlesiege.kits.kits.Kit;
import me.greenfoot5.conwymc.data_types.Tuple;
import me.greenfoot5.conwymc.events.nametag.UpdateNameTagEvent;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
import me.libraryaddict.disguise.disguisetypes.watchers.PlayerWatcher;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Werewolf extends EventKit implements Listener {

    private static final int health = 290;
    private static final double regen = 18;
    private static final double meleeDamage = 40;
    private static final int ladderCount = 4;
    private final ItemStack regularSword;
    private final ItemStack regularSwordVoted;
    private final ItemStack werewolfSword;
    private final ItemStack werewolfSwordVoted;

    public Werewolf() {
        super("Werewolf", health, regen, Material.RABBIT_HIDE,  NamedTextColor.GOLD, LocalDate.ofYearDay(LocalDate.now().getYear(), 291), LocalDate.ofYearDay(LocalDate.now().getYear(), 305));

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();

        // Weapon
        regularSword = CSItemCreator.weapon(new ItemStack(Material.STONE_SWORD),
                Component.text("Shortsword", NamedTextColor.DARK_RED),
                List.of(Component.empty(),
                        Component.text(meleeDamage + " Melee Damage", NamedTextColor.RED)),
                null, meleeDamage);
        es.hotbar[0] = regularSword;
        // Voted weapon
        regularSwordVoted = CSItemCreator.weapon(new ItemStack(Material.STONE_SWORD),
                        Component.text("Shortsword", NamedTextColor.DARK_RED),
                        List.of(Component.empty(),
                                Component.text((meleeDamage + 2) + " Melee Damage", NamedTextColor.RED),
                                Component.text("⁎ Voted: +2 Melee Damage", NamedTextColor.GOLD)),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOTING, 0)), meleeDamage + 2);

        es.votedWeapon = new Tuple<>(regularSwordVoted, 0);

        // Chestplate
        es.chest = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE),
                Component.text("Shredded Shirt", NamedTextColor.DARK_RED),
                List.of(Component.empty(),
                        Component.text(health + " HP", NamedTextColor.RED),
                        Component.text(regen + " Regen", NamedTextColor.RED)), null,
                Color.fromRGB(106, 85, 69));
        ItemMeta chest = es.chest.getItemMeta();
        ArmorMeta chestMeta = (ArmorMeta) chest;
        assert chest != null;
        ArmorTrim chestTrim = new ArmorTrim(TrimMaterial.REDSTONE, TrimPattern.RIB);
        ((ArmorMeta) chest).setTrim(chestTrim);
        es.chest.setItemMeta(chestMeta);

        // Leggings
        es.legs = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_LEGGINGS),
                Component.text("Worn Leggings", NamedTextColor.DARK_RED),
                List.of(Component.empty(),
                        Component.text(health + " HP", NamedTextColor.RED),
                        Component.text(regen + " Regen", NamedTextColor.RED)), null,
                Color.fromRGB(122, 112, 103));
        ItemMeta legs = es.legs.getItemMeta();
        ArmorMeta legsMeta = (ArmorMeta) legs;
        assert legs != null;
        ArmorTrim legsTrim = new ArmorTrim(TrimMaterial.NETHERITE, TrimPattern.HOST);
        ((ArmorMeta) legs).setTrim(legsTrim);
        es.legs.setItemMeta(legsMeta);

        // Boots
        es.feet = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_BOOTS),
                Component.text("Old Boots", NamedTextColor.DARK_RED),
                List.of(Component.empty(),
                        Component.text(health + " HP", NamedTextColor.RED),
                        Component.text(regen + " Regen", NamedTextColor.RED)), null,
                Color.fromRGB(125, 88, 56));
        ItemMeta feet = es.feet.getItemMeta();
        ArmorMeta feetMeta = (ArmorMeta) feet;
        assert feet != null;
        ArmorTrim feetTrim = new ArmorTrim(TrimMaterial.NETHERITE, TrimPattern.HOST);
        ((ArmorMeta) feet).setTrim(feetTrim);
        es.feet.setItemMeta(feetMeta);
        // Voted Boots
        es.votedFeet = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_BOOTS),
                Component.text("Old Boots", NamedTextColor.DARK_RED),
                List.of(Component.empty(),
                        Component.text(health + " HP", NamedTextColor.RED),
                        Component.text(regen + " Regen", NamedTextColor.RED),
                        Component.empty(),
                        Component.text("⁎ Voted: Depth Strider II", NamedTextColor.GOLD)),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)),
                Color.fromRGB(125, 88, 56));
        ItemMeta votedFeet = es.votedFeet.getItemMeta();
        ArmorMeta votedFeetMeta = (ArmorMeta) votedFeet;
        assert votedFeet != null;
        ArmorTrim votedFeetTrim = new ArmorTrim(TrimMaterial.NETHERITE, TrimPattern.HOST);
        ((ArmorMeta) votedFeet).setTrim(votedFeetTrim);
        es.votedFeet.setItemMeta(votedFeetMeta);

        // Ladders
        es.hotbar[1] = new ItemStack(Material.LADDER, ladderCount);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, ladderCount + 2), 1);

        es.hotbar[2] = CSItemCreator.item(new ItemStack(Material.RABBIT_HIDE, 1),
                Component.text("Werewolf Form", NamedTextColor.GOLD),
                List.of(Component.empty(),
                        Component.text("Transform into a werewolf for 20s", NamedTextColor.BLUE),
                        Component.empty(),
                        Component.text("- +20.0 Melee Damage", NamedTextColor.DARK_GREEN),
                        Component.text("- Instantly heal 100 HP", NamedTextColor.DARK_RED),
                        Component.text("- 40s cooldown", NamedTextColor.BLUE),
                        Component.text("○ Speed II (0:20)", TextColor.color(40, 169, 255)),
                        Component.text("○ Strength I (0:20)", TextColor.color(40, 169, 255)),
                        Component.text("○ Jump I (0:20)", TextColor.color(40, 169, 255)),
                        Component.text("○ Night Vision (0:20)", TextColor.color(40, 169, 255))),
                null);

        // Berserk Weapon
        werewolfSword = CSItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                Component.text("Werewolf Shortsword", NamedTextColor.GREEN),
                List.of(Component.empty(),
                        Component.text((meleeDamage + 20) + " Melee Damage", NamedTextColor.DARK_GREEN)),
                Collections.singletonList(new Tuple<>(Enchantment.KNOCKBACK, 1)), (meleeDamage + 20));
        // Voted Berserk Weapon
        werewolfSwordVoted = CSItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                Component.text("Werewolf Shortsword", NamedTextColor.GREEN),
                List.of(Component.empty(),
                        Component.text((meleeDamage + 22) + " Melee Damage", NamedTextColor.DARK_GREEN),
                        Component.text("⁎ Voted: +2 Melee Damage", NamedTextColor.DARK_AQUA)),
                Arrays.asList(new Tuple<>(Enchantment.LOOTING, 0),
                        new Tuple<>(Enchantment.KNOCKBACK, 1)), (meleeDamage + 20) + 2);

        super.potionEffects.add(new PotionEffect(PotionEffectType.JUMP_BOOST, 999999, 0));
        super.potionEffects.add(new PotionEffect(PotionEffectType.SPEED, 999999, 0));

        // Death Messages
        super.deathMessage[0] = "";
        super.deathMessage[1] = " shredded you to bits!";
        super.killMessage[0] = " shredded ";
        super.killMessage[1] = " to bits!";

        super.equipment = es;

    }

    /**
     * Activate the werewolf's ability, giving the werewolf sword, speed, strength, and nausea
     * @param e The event called when clicking with the RABBIT_HIDE in hand
     */
    @EventHandler
    public void werewolfForm(PlayerInteractEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();
        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(uuid)) {
            e.setCancelled(true);
            return;
        }
        if (!Objects.equals(Kit.equippedKits.get(uuid).name, name)) {
            return;
        }
        if (e.getItem() == null || e.getItem().getType() != Material.RABBIT_HIDE) {
            return;
        }
        Player p = e.getPlayer();
        //Has the cooldown expired?
        if (p.getCooldown(Material.RABBIT_HIDE) != 0) {
            return;
        }
        // Potion effects
        p.addPotionEffect((new PotionEffect(PotionEffectType.SPEED, 400, 1)));
        p.addPotionEffect((new PotionEffect(PotionEffectType.STRENGTH, 400, 0)));
        p.addPotionEffect((new PotionEffect(PotionEffectType.NIGHT_VISION, 400, 0)));
        // Sword
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_WOLF_HOWL , 1, 1.0F);
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ALLAY_ITEM_TAKEN , 1, 1.0F);
        healWerewolf(p);
        changeSword(p, regularSword.getType(), werewolfSword, werewolfSwordVoted);
        p.setCooldown(Material.RABBIT_HIDE, 800);
        setDisguise(p);
        // Revert sword
        Bukkit.getScheduler().runTaskLater(Main.plugin, () -> {
            if (Objects.equals(Kit.equippedKits.get(uuid).name, name) &&
                    p.getPotionEffect(PotionEffectType.STRENGTH) == null) {
                changeSword(p, werewolfSword.getType(), regularSword, regularSwordVoted);
                p.addPotionEffect((new PotionEffect(PotionEffectType.SPEED, 99999, 0)));
                removeDisguise(p);
            }
        }, 401);
    }

    /**
     * Change the player's werewolf sword.
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
            inventory.addItem(sword);
    }

    /**
     * Disguise the player as a werewolf
     * @param p The player to (un)disguise
     */
    protected void setDisguise(Player p) {
        PlayerDisguise mobDisguise = new PlayerDisguise("Chingling8");
        PlayerWatcher wolfWatcher = mobDisguise.getWatcher();

        wolfWatcher.setCapeEnabled(false);

        disguise(p, mobDisguise);
    }

    /**
     * Disguise the player as a werewolf
     * @param p The player to (un)disguise
     */
    protected void removeDisguise(Player p) {
        DisguiseAPI.undisguiseToAll(p);
        Bukkit.getPluginManager().callEvent(new UpdateNameTagEvent(p));
    }

    /**
     * heal the werewolf when transforming
     * @param werewolf The player to (un)disguise
     */
    protected void healWerewolf(Player werewolf) {
        if (werewolf.getHealth() + 100 <= health) {
            werewolf.setHealth(werewolf.getHealth() + 100);
        } else {
            werewolf.setHealth(health);
        }
    }

    @EventHandler
    public void onPlayerFall(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            if (Objects.equals(Kit.equippedKits.get(e.getEntity().getUniqueId()).name, name)) {
                Player fallen = (Player) e.getEntity();
                if (e.getCause() == EntityDamageEvent.DamageCause.FALL &&
                        fallen.getPotionEffect(PotionEffectType.STRENGTH) != null) {
                    e.setDamage(0);
                }
            }
        }
    }

    @Override
    public ArrayList<Component> getGuiDescription() {
        ArrayList<Component> kitLore = new ArrayList<>();
        kitLore.add(Component.text("Halloween Only Kit!", NamedTextColor.GOLD));
        kitLore.add(Component.text("Can transform into a powerful", NamedTextColor.GRAY));
        kitLore.add(Component.text("werewolf for a few seconds.", NamedTextColor.GRAY));
        kitLore.addAll(getBaseStats(health, regen, meleeDamage, ladderCount));
        kitLore.add(Component.empty());
        kitLore.add(Component.text("Effects:", NamedTextColor.DARK_PURPLE));
        kitLore.add(Component.text("- Speed I", NamedTextColor.GRAY));
        kitLore.add(Component.text("- Jump I", NamedTextColor.GRAY));
        kitLore.add(Component.empty());
        kitLore.add(Component.text("Transformed Effects:", NamedTextColor.DARK_PURPLE));
        kitLore.add(Component.text("- Speed II", NamedTextColor.GRAY));
        kitLore.add(Component.text("- Jump I", NamedTextColor.GRAY));
        kitLore.add(Component.text("- Strength I", NamedTextColor.GRAY));
        kitLore.add(Component.text("- Night Vision I", NamedTextColor.GRAY));
        kitLore.add(Component.empty());
        kitLore.add(Component.text("Passive:", NamedTextColor.DARK_GREEN));
        kitLore.add(Component.text("- No fall damage whilst transformed", NamedTextColor.GRAY));
        kitLore.add(Component.text("Transformation:", NamedTextColor.GOLD));
        kitLore.add(Component.text("- Instantly heals 100 health.", NamedTextColor.GRAY));
        kitLore.add(Component.text("- +20.0 Melee damage.", NamedTextColor.GRAY));
        return kitLore;
    }
}
