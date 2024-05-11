package me.huntifi.castlesiege.commands.gameplay;

import me.huntifi.conwymc.gui.Gui;
import me.huntifi.conwymc.gui.GuiController;
import me.huntifi.conwymc.util.Messenger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Displays the coin shop for players when requested
 */
public class CoinShopCommand implements CommandExecutor {

    /**
     * Opens the coin shop GUI
     * @param sender Source of the command
     * @param command Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return true
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            Messenger.sendError("Console cannot buy kits!", sender);
        } else {
            registerCoinShop((Player) sender);
            GuiController.get("coin shop" + sender.getName()).open((Player) sender);
        }

        return true;
    }

    /**
     * Register the coin shop as set in [TODO: yml to be created]
     * Not included in [TODO: loading GUIs from yml] because the kits must be registered first
     */
    private void registerCoinShop(Player player) {
        Gui gui = new Gui( Component.text("Coin Shop", NamedTextColor.DARK_GREEN), 5);

//        gui.addCoinShopItem("Alchemist", Kit.getMaterial("Alchemist"), 0, player.getUniqueId());
//        gui.addCoinShopItem("Berserker", Kit.getMaterial("Berserker"), 1, player.getUniqueId());
//        gui.addCoinShopItem("Cavalry", Kit.getMaterial("Cavalry"), 2, player.getUniqueId());
//        gui.addCoinShopItem("Chef", Kit.getMaterial("Chef"), 3, player.getUniqueId());
//        gui.addCoinShopItem("Crossbowman", Kit.getMaterial("Crossbowman"), 4, player.getUniqueId());
//        gui.addCoinShopItem("Engineer", Kit.getMaterial("Engineer"), 5, player.getUniqueId());
//        gui.addCoinShopItem("Executioner", Kit.getMaterial("Executioner"), 6, player.getUniqueId());
//        gui.addCoinShopItem("Halberdier", Kit.getMaterial("Halberdier"), 7, player.getUniqueId());
//        gui.addCoinShopItem("Maceman", Kit.getMaterial("Maceman"), 8, player.getUniqueId());
//        gui.addCoinShopItem("Medic", Kit.getMaterial("Medic"), 9, player.getUniqueId());
//        gui.addCoinShopItem("Ranger", Kit.getMaterial("Ranger"), 10, player.getUniqueId());
//        gui.addCoinShopItem("Rogue", Kit.getMaterial("Rogue"), 11, player.getUniqueId());
//        gui.addCoinShopItem("Vanguard", Kit.getMaterial("Vanguard"), 12, player.getUniqueId());
//        gui.addCoinShopItem("Viking", Kit.getMaterial("Viking"), 13, player.getUniqueId());
//        gui.addCoinShopItem("Warhound", Kit.getMaterial("Warhound"), 14, player.getUniqueId());

        List<Component> lore = new ArrayList<>();
        lore.add(Component.text("/\\ Elite Kits /\\"));
        lore.add(Component.text(" "));
        lore.add(Component.text("\\/ Team Kits \\/"));
        for (int i = 18; i < 27; i++) {
            gui.addItem(Component.text("-"), Material.GRAY_STAINED_GLASS_PANE, lore, i, "", false);
        }

//        gui.addCoinShopItem("Elytrier", Kit.getMaterial("Elytrier"), 27, player.getUniqueId());
//        gui.addCoinShopItem("Fallen", Kit.getMaterial("Fallen"), 28, player.getUniqueId());
//        gui.addCoinShopItem("MoriaOrc", Kit.getMaterial("MoriaOrc"), 29, player.getUniqueId());
//        gui.addCoinShopItem("DwarvenAxeThrower", Kit.getMaterial("DwarvenAxeThrower"), 30, player.getUniqueId());
//        gui.addCoinShopItem("DwarvenGuardian", Kit.getMaterial("DwarvenGuardian"), 31, player.getUniqueId());
//        gui.addCoinShopItem("MoriaCaveTroll", Kit.getMaterial("MoriaCaveTroll"), 32, player.getUniqueId());
//        gui.addCoinShopItem("MoriaBonecrusher", Kit.getMaterial("MoriaBonecrusher"), 33, player.getUniqueId());
//        gui.addCoinShopItem("DwarvenWindlancer", Kit.getMaterial("DwarvenWindlancer"), 34, player.getUniqueId());
//        gui.addCoinShopItem("UrukBerserker", Kit.getMaterial("UrukBerserker"), 35, player.getUniqueId());
//        gui.addCoinShopItem("Lancer", Kit.getMaterial("Lancer"), 36, player.getUniqueId());
//        gui.addCoinShopItem("RangedCavalry", Kit.getMaterial("RangedCavalry"), 37, player.getUniqueId());
//        gui.addCoinShopItem("RoyalKnight", Kit.getMaterial("RoyalKnight"), 38, player.getUniqueId());
//        gui.addCoinShopItem("Arbalester", Kit.getMaterial("Arbalester"), 39, player.getUniqueId());
//        gui.addCoinShopItem("ConwyLongbowman", Kit.getMaterial("ConwyLongbowman"), 40, player.getUniqueId());
//        gui.addCoinShopItem("Longbowman", Kit.getMaterial("Longbowman"), 41, player.getUniqueId());
//        gui.addCoinShopItem("Axeman", Kit.getMaterial("Axeman"), 42, player.getUniqueId());

        //gui.addItem("§4§lGo Back", Material.BARRIER, Collections.singletonList("§cReturn to the previous interface."), 45, "kit shop", true);

        GuiController.add("coin shop" + player.getName(), gui);
    }
}
