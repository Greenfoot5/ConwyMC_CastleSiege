package me.greenfoot5.castlesiege.gui;

import io.lumine.mythic.bukkit.utils.lib.lang3.text.WordUtils;
import me.greenfoot5.castlesiege.Main;
import me.greenfoot5.castlesiege.data_types.CSPlayerData;
import me.greenfoot5.castlesiege.database.CSActiveData;
import me.greenfoot5.castlesiege.kits.kits.Kit;
import me.greenfoot5.conwymc.database.ActiveData;
import me.greenfoot5.conwymc.util.ItemCreator;
import me.greenfoot5.conwymc.util.Messenger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.join;
import static java.util.Collections.nCopies;
import static me.greenfoot5.castlesiege.advancements.levels.LevelAdvancements.BUYKIT_LEVEL;

public class BuyKitGui {
    private final Gui gui;

    public BuyKitGui(Kit kit, int cost, Player player) {
        this.gui = new Gui(Component.text(kit.name), cost > 0 ? 6 : 5);

        CSPlayerData data = CSActiveData.getData(player.getUniqueId());
        if (data != null && data.getLevel() < BUYKIT_LEVEL) {
            for (int loc = 45; loc < (54); loc++) {
                gui.addItem(Component.text("Purchase Kit", NamedTextColor.GOLD, TextDecoration.BOLD), Material.LAPIS_BLOCK,
                        List.of(Component.text(" "),
                                Messenger.mm.deserialize("<green>Requires Level " + BUYKIT_LEVEL + " to purchase kits.</green>"),
                                Messenger.mm.deserialize("<dark_green>Current Level: " + data.getLevel())),
                        loc, null, true);
            }
        } else if (data != null && cost > 0) {
            // Buy blocks
            for (int loc = 45; loc < (54); loc++) {
                int balance = (int) ActiveData.getData(player.getUniqueId()).getCoins();
                gui.addItem(Component.text("Purchase Kit", NamedTextColor.GOLD, TextDecoration.BOLD), Material.GOLD_BLOCK,
                        List.of(Component.text(" "),
                                Messenger.mm.deserialize("<blue>Cost: <gold><b>" + cost + "</b>⛃"),
                                Messenger.mm.deserialize("<dark_aqua>Balance: " + (balance < cost ? "<red>" : "<green>") + balance + "⛃")),
                        loc, "buykit " + kit.getSpacelessName() + " " + player.getName(), true);
            }
        }

        // Equipment
        gui.addItem(Component.text("WoolHat", NamedTextColor.GREEN), Material.WHITE_WOOL,
                List.of(Component.text("Changes colour based on your team colour")),
                0, null, false);
        setItem(kit.getEquipment().chest, 9,"Chest");
        setItem(kit.getEquipment().legs, 18, "Legs");
        setItem(kit.getEquipment().feet, 27, "Feet");

        setItem(kit.getEquipment().offhand, 29, "Offhand");
        for (int i = 0; i < 8; i++) {
            setItem(kit.getEquipment().hotbar[i], i + 36, "Hotbar");
        }

        // Stats
        gui.addItem(Component.text("Description", NamedTextColor.BLUE, TextDecoration.BOLD),
                Material.PAPER, kit.getGuiDescription(), 15, null, false);

        // Potion Effects
        if (kit.getEffects().isEmpty()) {
            gui.addItem(Component.text("Potion Effects", NamedTextColor.GRAY, TextDecoration.BOLD),
                    Material.GRAY_STAINED_GLASS_PANE,
                    List.of(Component.text("This kit has no permanent potion effects")),
                    16, null, false);
        } else {
            ArrayList<Component> effects = new ArrayList<>();
            for (PotionEffect effect : kit.getEffects()) {
                // Capitalise the name
                String name = WordUtils.capitalize(effect.getType().getKey().value().replace("_", " "));
                // Add lore line using potion colour
                effects.add(Component.text(name + " " + getRoman(effect.getAmplifier() + 1))
                        .color(TextColor.color(effect.getType().getColor().asRGB())));
            }

            gui.addItem(Component.text("Potion Effects", NamedTextColor.GOLD, TextDecoration.BOLD),
                    Material.POTION, effects, 16, null, false);
        }

        // Display
        Bukkit.getScheduler().runTask(Main.plugin, () -> gui.open(player));
    }

    private void setItem(ItemStack item, int location, String slotName) {
        if (item == null || item.getType() == Material.AIR) {
            gui.setItem(ItemCreator.item(new ItemStack(Material.GRAY_STAINED_GLASS_PANE),
                    Component.text(slotName, NamedTextColor.GRAY, TextDecoration.BOLD),
                    new ArrayList<>(), null), location, null, false);
        } else {
            gui.setItem(item, location, null, false);
        }
    }

    private String getRoman(int number) {
        return join("", nCopies(number, "I"))
                .replace("IIIII", "V")
                .replace("IIII", "IV")
                .replace("VV", "X")
                .replace("VIV", "IX")
                .replace("XXXXX", "L")
                .replace("XXXX", "XL")
                .replace("LL", "C")
                .replace("LXL", "XC")
                .replace("CCCCC", "D")
                .replace("CCCC", "CD")
                .replace("DD", "M")
                .replace("DCD", "CM");
    }
}
