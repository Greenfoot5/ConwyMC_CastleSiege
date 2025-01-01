package me.greenfoot5.castlesiege.kits.items;

import me.greenfoot5.castlesiege.gui.MenuGUI;
import me.greenfoot5.conwymc.gui.Gui;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class MenuItem implements Listener {

    /**
     * Sets the menu item in the player's inventory
     * @param player The player for whom to give the menu compass to
     */
    public static void giveMenuItem(Player player) {
        ItemStack menu = CSItemCreator.item(new ItemStack(Material.COMPASS),
                Component.text("⥽⤛⤙ Menu ⤚⤜⥼", TextColor.color(77, 178, 134)),
                List.of(Component.empty(),
                        Component.text("Hello, " + player.getName() + " this is your menu!", TextColor.color(125, 228, 183)),
                        Component.text("« Right click to open »", TextColor.color(193, 185, 185))),
                null);

        player.getInventory().setItem(8, menu);
    }

    /**
     * Right-click the menu to open the menu GUI
     * @param e The event called when right-clicking the menu
     */
    @EventHandler
    public void clickCompass(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        ItemStack menu = p.getInventory().getItemInMainHand();

        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (menu.getType() == Material.COMPASS) {
                Gui gui = MenuGUI.getGUI();
                gui.open(p);
            }
        }
    }
}
