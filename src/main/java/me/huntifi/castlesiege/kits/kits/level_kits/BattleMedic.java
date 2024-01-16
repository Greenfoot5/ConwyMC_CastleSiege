package me.huntifi.castlesiege.kits.kits.level_kits;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.database.UpdateStats;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.kits.kits.LevelKit;
import me.huntifi.castlesiege.maps.NameTag;
import me.huntifi.castlesiege.maps.TeamController;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
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

import java.util.*;

public class BattleMedic extends LevelKit implements Listener {

    private static final int health = 300;
    private static final double regen = 10.5;
    private static final double meleeDamage = 42;
    private static final int ladderCount = 4;

    private static final int bandageCount = 16;

    private static final int level = 10;

    public static final ArrayList<Player> cooldown = new ArrayList<>();

    public BattleMedic() {
        super("Battle Medic", health, regen, Material.PAPER, level);

        super.canSeeHealth = true;

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Weapon
        es.hotbar[0] = ItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                ChatColor.GREEN + "Short-sword", null, null, meleeDamage);
        // Voted Weapon
        es.votedWeapon = new Tuple<>(
                ItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                        ChatColor.GREEN + "Short-sword",
                        Collections.singletonList(ChatColor.AQUA + "- voted: +2 damage"),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), meleeDamage + 2),
                0);

        // Chestplate
        es.chest = ItemCreator.item(new ItemStack(Material.IRON_CHESTPLATE),
                ChatColor.GREEN + "Iron Chestplate", null, null);
        ItemMeta chest = es.chest.getItemMeta();
        ArmorMeta ameta = (ArmorMeta) chest;
        assert chest != null;
        ArmorTrim trim = new ArmorTrim(TrimMaterial.REDSTONE, TrimPattern.SENTRY);
        ((ArmorMeta) chest).setTrim(trim);
        es.chest.setItemMeta(ameta);

        // Leggings
        es.legs = ItemCreator.item(new ItemStack(Material.CHAINMAIL_LEGGINGS),
                ChatColor.GREEN + "Chainmail Leggings", null, null);

        // Boots
        es.feet = ItemCreator.item(new ItemStack(Material.IRON_BOOTS),
                ChatColor.GREEN + "Iron Boots", null, null);
        // Voted Boots
        es.votedFeet = ItemCreator.item(new ItemStack(Material.IRON_BOOTS),
                ChatColor.GREEN + "Iron Boots",
                Collections.singletonList(ChatColor.AQUA + "- voted: Depth Strider II"),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)));

        // Bandages
        es.hotbar[1] = ItemCreator.item(new ItemStack(Material.PAPER, bandageCount),
                ChatColor.DARK_AQUA + "Bandages",
                Collections.singletonList(ChatColor.AQUA + "Right click teammates to heal."), null);

        // Ladders
        es.hotbar[2] = new ItemStack(Material.LADDER, 4);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, 6), 2);

        super.equipment = es;

        super.killMessage[0] = " dissected ";
        super.deathMessage[0] = "You had your insides examined by ";
    }


    /**
     * Activate the medic ability of healing a teammate
     * @param event The event called when clicking on a teammate with paper
     */
    @EventHandler
    public void onHeal(PlayerInteractEntityEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
            Player player = event.getPlayer();
            UUID uuid = player.getUniqueId();

            // Prevent using in lobby
            if (InCombat.isPlayerInLobby(uuid)) {
                return;
            }

            PlayerInventory i = player.getInventory();
            Entity q = event.getRightClicked();
            if (Objects.equals(Kit.equippedKits.get(uuid).name, name) &&                            // Player is medic
                    (i.getItemInMainHand().getType() == Material.PAPER) &&                    // Uses bandage
                    q instanceof Player &&                                                          // On player
                    TeamController.getTeam(uuid) == TeamController.getTeam(q.getUniqueId()) &&      // Same team
                    ((Player) q).getHealth() < Kit.equippedKits.get(q.getUniqueId()).baseHealth &&  // Below max hp
                    !cooldown.contains((Player) q)) {                                               // Not on cooldown

                // Apply cooldown
                Player r = (Player) q;
                cooldown.add(r);
                Bukkit.getScheduler().runTaskLaterAsynchronously(Main.plugin, () -> cooldown.remove(r), 39);
                player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
                addPotionEffect(r, new PotionEffect(PotionEffectType.REGENERATION, 40, 9));
                r.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                        NameTag.color(player) + player.getName() + ChatColor.AQUA + " is healing you"));
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                        ChatColor.AQUA + "You are healing " + NameTag.color(r) + r.getName()));
                UpdateStats.addHeals(uuid, 1);
            }
        });
    }

    /**
     * Add a potion effect to a player.
     * @param player The player
     * @param potion The potion effect
     */
    private void addPotionEffect(Player player, PotionEffect potion) {
        Bukkit.getScheduler().runTask(Main.plugin, () -> player.addPotionEffect(potion));
    }

    /**
     * @return The lore to add to the kit gui item
     */
    public static ArrayList<String> getGuiDescription() {
        ArrayList<String> kitLore = new ArrayList<>();
        kitLore.add("§7Melee-support kit that can");
        kitLore.add("§7heal allies with bandages.");
        kitLore.add(" ");
        kitLore.add("§a" + health + " §7HP");
        kitLore.add("§a" + meleeDamage + " §7Melee DMG");
        kitLore.add("§a" + regen + " §7Regen");
        kitLore.add("§a" + ladderCount + " §7Ladders");
        kitLore.add("§a" + bandageCount + " §7Bandages");
        kitLore.add(" ");
        kitLore.add("§6Ability: ");
        kitLore.add("§7- Right click your teammates");
        kitLore.add("§7with bandages to heal them.");
        kitLore.add("");
        kitLore.add("§2Passive: ");
        kitLore.add("§7- Can see health.");
        kitLore.add("");
        kitLore.add("§7Unlocks at level §a" + level);
        return kitLore;
    }
}
