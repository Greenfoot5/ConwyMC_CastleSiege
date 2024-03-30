package me.huntifi.castlesiege.kits.kits;

import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.kits.items.CSItemCreator;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.objects.Flag;
import me.huntifi.conwymc.data_types.Tuple;
import me.huntifi.conwymc.util.Messenger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Fisherman extends FreeKit {

    private static final List<UUID> balls = new ArrayList<>();

    public Fisherman() {
        super("Fisherman", 400, 5, Material.BOW);

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();

        // Weapon
        es.hotbar[0] = CSItemCreator.weapon(new ItemStack(Material.FISHING_ROD),
                Component.text("Rod", NamedTextColor.GREEN), null,
                Collections.singletonList(new Tuple<>(Enchantment.LURE, 3)), 0);
        // Voted Weapon
        es.votedWeapon = new Tuple<>(CSItemCreator.weapon(new ItemStack(Material.FISHING_ROD),
                Component.text("Rod", NamedTextColor.GREEN),
                Collections.singletonList(Component.text("- voted: +1 Lure", NamedTextColor.AQUA)),
                Collections.singletonList(new Tuple<>(Enchantment.LURE, 4)), 0),
                0);

        // Chestplate
        es.chest = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE),
                Component.text("Bib 'n' Brace Top", NamedTextColor.GREEN), null, null,
                Color.fromRGB(83, 77, 51));

        // Leggings
        es.legs = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_LEGGINGS),
                Component.text("Bib 'n' Brace Bottom", NamedTextColor.GREEN), null, null,
                Color.fromRGB(83, 77, 51));

        // Boots
        es.feet = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_BOOTS),
                Component.text("Leather Boots", NamedTextColor.GREEN), null, null,
                Color.fromRGB(254, 208, 0));
        // Voted Boots
        es.votedFeet = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_BOOTS),
                Component.text("Leather Boots", NamedTextColor.GREEN),
                Collections.singletonList(Component.text("- voted: Depth Strider II", NamedTextColor.AQUA)),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)),
                Color.fromRGB(254, 208, 0));

        // Ladders
        es.hotbar[1] = new ItemStack(Material.LADDER, 4);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, 4 + 2), 2);

        super.equipment = es;
    }

    /**
     * @param e When a player tries to fish
     */
    @EventHandler()
    public void onFish(PlayerFishEvent e) {
        if (!e.getState().equals(PlayerFishEvent.State.CAUGHT_FISH) || !(e.getCaught() instanceof Item))
            return;

        if (InCombat.isPlayerInLobby(e.getPlayer().getUniqueId()))
            return;

        e.getHook().remove();
        e.setCancelled(true);

        double length = giveFish(e.getPlayer());
        // TODO - This is a bad way to do things
        for (Flag f : MapController.getCurrentMap().flags) {
            f.captureProgress(e.getPlayer(), length);
        }
    }

    private double giveFish(Player p) {
        double x = Math.random();
        double y = 600 * Math.pow(x - 0.5, 3) + 125;
        y = Math.round(y * 100) / 100.0;
        double result = Math.random();
        PlayerInventory inv = p.getInventory();
        ItemStack fish;
        if (result < 0.60) {
            fish = CSItemCreator.fish(new ItemStack(Material.STONE_AXE), Component.text("Cod", NamedTextColor.BLUE),
                    null, 30, 126); // 5 remaining
            Messenger.send("<gradient:#58D9FF:#AFDFFF>[\uD83C\uDFA3]</gradient> <color:#DAB997>You caught Cod of length <color:#FFDBBB>" + y + "</color>cm</color>", p);
        } else if (result < (0.6 + 0.25)) {
            fish = CSItemCreator.fish(new ItemStack(Material.IRON_AXE), Component.text("Salmon", NamedTextColor.RED),
                    null, 45, 247); // 3 remaining
            Messenger.send("<gradient:#58D9FF:#AFDFFF>[\uD83C\uDFA3]</gradient> <color:#FFDBEA>You caught Salmon of length <color:#FFA4A3>" + y + "</color>cm</color>", p);
        } else {
            fish = CSItemCreator.item(new ItemStack(Material.SNOWBALL), Component.text("Pufferfish", NamedTextColor.YELLOW),
                    null, null);
            Messenger.send("<gradient:#58D9FF:#AFDFFF>[\uD83C\uDFA3]</gradient> <color:#FCA60C>You caught Pufferfish of circumference <color:#FFD927>" + y + "</color>cm</color>", p);
        }
        inv.addItem(fish);
        return y;
    }

    @EventHandler()
    public void throwPuffer(ProjectileLaunchEvent e) {
        if (!(e.getEntity() instanceof Snowball))
            return;

        Snowball ball = (Snowball) e.getEntity();
        if (!(ball.getShooter() instanceof Player)) {
            return;
        }

        Player p = (Player) ball.getShooter();
        if (Objects.equals(Kit.equippedKits.get(p.getUniqueId()).name, this.name)) {
            balls.add(ball.getUniqueId());
        }
    }

    @EventHandler()
    public void pufferHit(ProjectileHitEvent e) {
        if (!(e.getEntity() instanceof Snowball) || !balls.contains(e.getEntity().getUniqueId()))
            return;

        Snowball ball = (Snowball) e.getEntity();

        if (!balls.contains(ball.getUniqueId()))
            return;

        if (e.getHitEntity() == null || !(e.getHitEntity() instanceof LivingEntity))
            return;

        LivingEntity entity = (LivingEntity) e.getHitEntity();
        entity.damage(30);
        entity.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 100, 0));
    }

    /**
     * @return The description to display in the kit gui
     */
    @Override
    public ArrayList<Component> getGuiDescription() {
        ArrayList<Component> kitLore = new ArrayList<>();
        kitLore.add(Component.text("Standard ranged kit", NamedTextColor.GRAY));
        kitLore.addAll(getBaseStats(400, 5, 0, 4));
        return kitLore;
    }
}
