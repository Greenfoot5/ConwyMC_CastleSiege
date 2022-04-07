package me.huntifi.castlesiege.kits.kits;

import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.joinevents.stats.StatsChanging;
import me.huntifi.castlesiege.kits.EquipmentSet;
import me.huntifi.castlesiege.kits.Kit;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.voting.VotesChanging;
import me.huntifi.castlesiege.woolmap.LobbyPlayer;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static org.bukkit.Color.fromRGB;

public class FireArcher extends Kit implements Listener, CommandExecutor {

    public static HashMap<Player, Block> cauldrons = new HashMap<>();
    private final ItemStack fireArrow;
    private final ItemStack firepit;
    private final ItemStack firepitVoted;

    public FireArcher() {
        super("FireArcher");
        super.baseHeath = 105;


        // Equipment stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Chestplate
        ItemStack item = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta leatherItemMeta = (LeatherArmorMeta) item.getItemMeta();
        leatherItemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        leatherItemMeta.setDisplayName(ChatColor.GREEN + "Leather Chestplate");
        leatherItemMeta.setColor(fromRGB(204, 0, 0));
        leatherItemMeta.setUnbreakable(true);
        leatherItemMeta.setLore(new ArrayList<>());
        item.setItemMeta(leatherItemMeta);
        es.chest = item;

        // Leggings
        item = new ItemStack(Material.LEATHER_LEGGINGS);
        leatherItemMeta = (LeatherArmorMeta) item.getItemMeta();
        leatherItemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        leatherItemMeta.setDisplayName(ChatColor.GREEN + "Leather Leggings");
        leatherItemMeta.setColor(fromRGB(255, 128, 0));
        leatherItemMeta.setUnbreakable(true);
        leatherItemMeta.setLore(new ArrayList<>());
        item.setItemMeta(leatherItemMeta);
        es.legs = item;

        // Boots
        item = new ItemStack(Material.LEATHER_BOOTS);
        leatherItemMeta = (LeatherArmorMeta) item.getItemMeta();
        leatherItemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        leatherItemMeta.setDisplayName(ChatColor.GREEN + "Leather Boots");
        leatherItemMeta.setColor(fromRGB(204, 0, 0));
        leatherItemMeta.setUnbreakable(true);
        leatherItemMeta.setLore(new ArrayList<>());
        item.setItemMeta(leatherItemMeta);
        es.feet = item;
        // Voted Boots
        item.getItemMeta().addEnchant(Enchantment.DEPTH_STRIDER, 2, true);
        es.votedFeet = item;

        // Ladders
        es.hotbar[2] = new ItemStack(Material.LADDER, 4);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, 6), 2);

        // Bow
        item = new ItemStack(Material.BOW);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.setDisplayName(ChatColor.GREEN + "Bow");
        itemMeta.addEnchant(Enchantment.ARROW_DAMAGE, 16, true);
        itemMeta.setUnbreakable(true);
        itemMeta.setLore(new ArrayList<>());
        item.setItemMeta(itemMeta);
        es.hotbar[0] = item;

        // Firepit
        item = new ItemStack(Material.CAULDRON);
        itemMeta = item.getItemMeta();
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.setDisplayName(ChatColor.GREEN + "Firepit");
        itemMeta.addEnchant(Enchantment.DAMAGE_ALL, 20, true);
        itemMeta.addEnchant(Enchantment.KNOCKBACK, 1, true);
        itemMeta.setUnbreakable(true);
        itemMeta.setLore(Arrays.asList("",
                ChatColor.AQUA + "Place the firepit down, then",
                ChatColor.AQUA + "right click it with an arrow.", "",
                ChatColor.AQUA + "(tip): This firepit is very hard, so you",
                ChatColor.AQUA + "can beat your enemies to death with it."));
        item.setItemMeta(itemMeta);
        firepit = item.clone();
        es.hotbar[1] = item;
        // Voted Firepit
        item.getItemMeta().addEnchant(Enchantment.DAMAGE_ALL, 22, true);
        item.getItemMeta().setLore(Arrays.asList("",
                ChatColor.AQUA + "Place the firepit down, then",
                ChatColor.AQUA + "right click it with an arrow.", "",
                ChatColor.AQUA + "- voted: + 2 damage.",
                ChatColor.AQUA + "(tip): This firepit is very hard, so you",
                ChatColor.AQUA + "can beat your enemies to death with it."));
        firepitVoted = item.clone();
        es.votedWeapon = new Tuple<>(item, 1);

        // Arrows
        es.hotbar[7] = new ItemStack(Material.ARROW, 48);

        // Fire Arrows
        fireArrow = new ItemStack(Material.TIPPED_ARROW);
        PotionMeta potionMeta = (PotionMeta) fireArrow.getItemMeta();
        potionMeta.setDisplayName(ChatColor.GOLD + "Fire Arrow");
        potionMeta.setColor(Color.ORANGE);
        potionMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        fireArrow.setItemMeta(potionMeta);

        super.equipment = es;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        super.addPlayer(((Player) commandSender).getUniqueId());
        return true;
    }

    @EventHandler (priority = EventPriority.MONITOR)
    public void onPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        if (Objects.equals(Kit.equippedKits.get(p.getUniqueId()).name, name) &&
                e.getBlockPlaced().getType() == Material.CAULDRON) {

            // Destroy old cauldron
            destroyFirepit(p);

            // Place new cauldron
            e.setCancelled(false);
            cauldrons.put(p, e.getBlockPlaced());
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                    TextComponent.fromLegacyText(ChatColor.AQUA + "You placed down your Firepit!"));
        }
    }

    @EventHandler
    public void onRemove(PlayerInteractEvent e) {
        if (e.getAction() == Action.LEFT_CLICK_BLOCK &&
                e.getClickedBlock().getType() == Material.CAULDRON) {
            Player p = e.getPlayer();
            Player q = getPlacer(e.getClickedBlock());

            // Pick up own firepit
            if (Objects.equals(p, q)) {
                destroyFirepit(q);
                q.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                        TextComponent.fromLegacyText(ChatColor.AQUA + "You took back your Firepit!"));

                // Can only hold 1 firepit at a time
                PlayerInventory inv = p.getInventory();
                if (inv.getItemInOffHand().getType() != Material.CAULDRON &&
                        !inv.contains(Material.CAULDRON)) {
                    if (!VotesChanging.getVotes(q.getUniqueId()).contains("V#1")) {
                        p.getInventory().addItem(firepit);
                    } else {
                        p.getInventory().addItem(firepitVoted);
                    }
                }

            // Destroy enemy firepit
            } else if (q != null &&
                    MapController.getCurrentMap().getTeam(p.getUniqueId())
                    != MapController.getCurrentMap().getTeam(q.getUniqueId())){
                destroyFirepit(q);
                p.playSound(e.getClickedBlock().getLocation(), Sound.ENTITY_ZOMBIE_INFECT , 5, 1);
                p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                        TextComponent.fromLegacyText(ChatColor.RED + "You kicked over " + q.getName() + "'s Firepit!"));
            }
        }
    }

    @EventHandler
    public void onUseFirepit(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        // Check if a fire archer tries to light an arrow, while off-cooldown
        if (Objects.equals(Kit.equippedKits.get(p.getUniqueId()).name, name) &&
                e.getAction() == Action.RIGHT_CLICK_BLOCK &&
                e.getClickedBlock().getType() == Material.CAULDRON &&
                e.getItem() != null &&
                e.getItem().getType() == Material.ARROW &&
                p.getCooldown(Material.ARROW) == 0) {

            // Check if the player may light an arrow using this cauldron
            Player q = getPlacer(e.getClickedBlock());
            if (q != null &&
                    MapController.getCurrentMap().getTeam(p.getUniqueId())
                    == MapController.getCurrentMap().getTeam(q.getUniqueId())) {

                // Check if the player has may light any more arrows
                PlayerInventory inv = p.getInventory();
                ItemStack offHand = inv.getItemInOffHand();
                int fireOffHand = offHand.getType() == Material.TIPPED_ARROW ? offHand.getAmount() : 0;
                if (!inv.contains(Material.TIPPED_ARROW, 7 - fireOffHand)) {
                    p.setCooldown(Material.ARROW, 30);
                    inv.removeItem(new ItemStack(Material.ARROW));
                    inv.addItem(fireArrow);
                    p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 0.5f);
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                            TextComponent.fromLegacyText(ChatColor.AQUA + "You light an arrow."));
                } else {
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                            TextComponent.fromLegacyText(ChatColor.RED + "You can't hold more than 7 lit arrows at a time."));
                }
            }
        }
    }

    @EventHandler
    public void FlameBow(EntityShootBowEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            PlayerInventory inv = p.getInventory();

            if (Objects.equals(Kit.equippedKits.get(p.getUniqueId()).name, name) &&
                    e.getProjectile() instanceof Arrow &&
                    (inv.contains(Material.TIPPED_ARROW) ||
                            inv.getItemInOffHand().getType() == Material.TIPPED_ARROW) &&
                    inv.getItemInOffHand().getType() != Material.ARROW) {

                e.getProjectile().setFireTicks(180);
            }
        }
    }

    @EventHandler
    public void onFireDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player &&
                e.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK) {
            e.setDamage(6);
        }
    }

    @EventHandler
    public void onHitTnt(BlockIgniteEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        super.onDeath(e);
        destroyFirepit(e.getEntity());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        destroyFirepit(e.getPlayer());
    }

    private void destroyFirepit(Player p) {
        if(cauldrons.containsKey(p)) {
            cauldrons.get(p).setType(Material.AIR);
            cauldrons.remove(p);
        }
    }

    private Player getPlacer(Block cauldron) {
        return cauldrons.entrySet().stream()
                .filter(entry -> Objects.equals(entry.getValue(), cauldron))
                .findFirst().map(Map.Entry::getKey)
                .orElse(null);
    }
}
