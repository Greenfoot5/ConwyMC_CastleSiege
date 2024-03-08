package me.huntifi.castlesiege.gui;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.kits.kits.CoinKit;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.kits.kits.TeamKit;
import me.huntifi.castlesiege.maps.Map;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.Team;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static net.kyori.adventure.text.format.TextDecoration.State.FALSE;

/**
 * A GUI made with a minecraft inventory
 */
public class Gui implements Listener {

    protected final Inventory inventory;
    protected final HashMap<Integer, GuiItem> locationToItem = new HashMap<>();

    /** Whether this GUI should stop listening for events after being closed */
    private final boolean shouldUnregister;

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
        inventory = Main.plugin.getServer().createInventory(null, 9 * rows, name);
        this.shouldUnregister = shouldUnregister;

        Main.plugin.getServer().getPluginManager().registerEvents(this, Main.plugin);
    }

    /**
     * Add an item to the inventory.
     * @param name The name of the item
     * @param material The material of the item
     * @param lore The lore of the item
     * @param location The location of the item
     * @param command The command to execute when clicking the item
     * @param shouldClose Whether the GUI should close after performing the command
     */
    public void addItem(Component name, Material material, List<Component> lore, int location, String command, boolean shouldClose) {
        List<Component> loreI = removeItalics(lore);
        inventory.setItem(location, ItemCreator.item(new ItemStack(material),
                name.decorationIfAbsent(TextDecoration.ITALIC, FALSE), loreI, null));
        locationToItem.put(location, new GuiItem(command, shouldClose));
    }

    /**
     * @param location The location of the item
     * @param command The command to execute when clicking the item
     */
    public void addBackItem(int location, String command) {
        ItemStack item = ItemCreator.item(new ItemStack(Material.TIPPED_ARROW),
                Component.text("Go back", NamedTextColor.DARK_RED).decorate(TextDecoration.BOLD),
                Collections.singletonList(
                        Component.text("Return to the previous interface.", NamedTextColor.RED)
                                .decorationIfAbsent(TextDecoration.ITALIC, FALSE)), null);
        PotionMeta potionMeta = (PotionMeta) item.getItemMeta();
        assert potionMeta != null;
        potionMeta.setColor(Color.RED);
        item.setItemMeta(potionMeta);
        inventory.setItem(location, item);
        locationToItem.put(location, new GuiItem(command, true));
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
            inventory.setItem(location, ItemCreator.item(new ItemStack(kit.material),
                    getKitDisplayName(kit), kit.getGuiDescription(), null));
            locationToItem.put(location, new GuiItem(command, true));
        } else {
            ArrayList<Component> desc = kit.getGuiDescription();
            List<Component> lore = removeItalics(desc);
            assert lore != null;
            lore.addAll(kit.getGuiCostText());
            inventory.setItem(location, ItemCreator.item(new ItemStack(Material.BLACK_STAINED_GLASS_PANE),
                    getKitDisplayName(kit), lore, null));
            locationToItem.put(location, new GuiItem(command, false));
        }
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

    /**
     * Open this GUI for the player
     * @param player The player
     */
    public void open(Player player) {
        player.openInventory(inventory);
    }

    /**
     * Performs an action corresponding to the clicked item
     * @param event A click event while in the GUI
     */
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (Objects.equals(event.getClickedInventory(), inventory)) {
            GuiItem item = locationToItem.get(event.getSlot());
            if (item != null) {
                event.setCancelled(false);
                event.setResult(Event.Result.DENY);
                Player player = (Player) event.getWhoClicked();
                if (item.shouldClose)
                    player.closeInventory();
                player.performCommand(item.command);
            }
        }
    }

    /**
     * Unregister this GUI when it is closed.
     * @param event The event called when an inventory is closed.
     */
    @EventHandler
    public void onCloseGui(InventoryCloseEvent event) {
        if (Objects.equals(event.getInventory(), inventory) && shouldUnregister)
            HandlerList.unregisterAll(this);
    }

    /**
     * @param components The components to affect
     * @return The list with each root component removing Italics if not already set
     */
    public static List<Component> removeItalics(List<Component> components) {
        if (components == null) {
            return null;
        }

        ArrayList<Component> list = new ArrayList<>();
        for (Component c : components) {
            if (c != null) {
                c = c.decorationIfAbsent(TextDecoration.ITALIC, FALSE);
                list.add(c);
            }
        }
        return list;
    }
}
