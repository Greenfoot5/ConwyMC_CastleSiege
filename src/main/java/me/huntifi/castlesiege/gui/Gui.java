package me.huntifi.castlesiege.gui;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.kits.items.CSItemCreator;
import me.huntifi.castlesiege.kits.kits.CoinKit;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.kits.kits.TeamKit;
import me.huntifi.castlesiege.maps.Map;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.Team;
import me.huntifi.conwymc.gui.GuiItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * A GUI made with a minecraft inventory
 */
public class Gui extends me.huntifi.conwymc.gui.Gui implements Listener {
    /**
     * Create an inventory.
     * @param name The name of the inventory
     * @param rows The amount of rows of the inventory
     */
    public Gui(Component name, int rows) {
        this(name, rows, false);
    }

    /**
     * Create an inventory.
     * @param name The name of the inventory
     * @param rows The amount of rows of the inventory
     * @param shouldUnregister Whether this GUI should stop listening for events after being closed
     */
    public Gui(Component name, int rows, boolean shouldUnregister) {
        super(name, rows, shouldUnregister);
    }

    /**
     * Add an item to the inventory only if the user can select it
     * @param player The player opening the GUI
     * @param kit The kit to select
     * @param location The location of the item
     * @param command The command to execute when clicking the item
     */
    public void addKitItem(Player player, Kit kit, int location, String command) {
        if (kit.canSelect(player, false, false, false)) {
            inventory.setItem(location, CSItemCreator.item(new ItemStack(kit.material),
                    getKitDisplayName(kit), kit.getGuiDescription(), null));
            locationToItem.put(location, new GuiItem(command, true));
        } else {
            ArrayList<Component> desc = kit.getGuiDescription();
            List<Component> lore = removeItalics(desc);
            assert lore != null;
            lore.addAll(kit.getGuiCostText());
            inventory.setItem(location, CSItemCreator.item(new ItemStack(Material.BLACK_STAINED_GLASS_PANE),
                    getKitDisplayName(kit), lore, null));
            locationToItem.put(location, new GuiItem(command, false));
        }
    }

    public void addKitItem(Kit kit, int location, String command) {
        inventory.setItem(location, CSItemCreator.item(new ItemStack(kit.material),
                getKitDisplayName(kit), kit.getGuiDescription(), null));
        locationToItem.put(location, new GuiItem(command, true));
    }

    /**
     * @param kit The kit to display the name of
     * @return A string displaying in for them [Color]CLASS: kit.name
     */
    private Component getKitDisplayName(Kit kit) {
        return Component.text("CLASS: ", kit.color).decorate(TextDecoration.BOLD)
                .append(Component.text(kit.name).decoration(TextDecoration.BOLD, false));
    }

    /**
     * Add a coin shop item to the inventory.
     * @param kitName The name of the kit without spaces
     * @param material The material of the item
     * @param location The location of the item
     * @param uuid The UUID of the player
     */
    public void addCoinShopItem(String kitName, Material material, int location, UUID uuid) {
        Kit kit = Kit.getKit(kitName);
        if (!(kit instanceof CoinKit))
            throw new IllegalArgumentException(kitName + " is not a donator kit, it's " + kit.getClass());
        Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
            NamedTextColor color = kit instanceof TeamKit ? NamedTextColor.BLUE : NamedTextColor.GOLD;
            Component itemName = Component.text(kit.name, color).decorate(TextDecoration.BOLD);
            Component price = Component.text("Coins: ", NamedTextColor.GREEN)
                    .append(Component.text(CoinKit.getPrice(uuid), NamedTextColor.YELLOW));
            Component duration = Component.text("Duration: permanent", NamedTextColor.GREEN);

            List<Component> lore = removeItalics(getKitLore(price, duration, kit));
            Bukkit.getScheduler().runTask(Main.plugin, () ->
                        addItem(itemName, material, lore, location, "buykit " + kitName, false)
                    );
        });
    }

    /**
     * @param price The price of the kit
     * @param duration The length of time you receive the kit for
     * @param kit The kit that's unlocked
     * @return The lore for an item of this kit
     */
    @NotNull
    private static List<Component> getKitLore(Component price, Component duration, Kit kit) {
        ArrayList<Component> lore = new ArrayList<>();
        lore.add(price);
        lore.add(duration);
        if (kit instanceof TeamKit) {
            Map map = MapController.getMap(((TeamKit) kit).getMapName());
            if (map != null) {
                Team team = map.getTeam(((TeamKit) kit).getTeamName());
                lore.add(Component.text("Map: ", NamedTextColor.GREEN)
                        .append(Component.text(map.name).decorate(TextDecoration.BOLD)));
                lore.add(Component.text("Team: ", NamedTextColor.GREEN)
                        .append(Component.text(team.name, team.primaryChatColor)));
            } else
                lore.add(Component.text("Map: ", NamedTextColor.GREEN)
                        .append(Component.text("OUT OF ROTATION").decorate(TextDecoration.BOLD)));
        }
        lore.add(Component.text("Click here to buy!", NamedTextColor.YELLOW));
        return Objects.requireNonNull(removeItalics(lore));
    }
}
