package me.greenfoot5.castlesiege.kits.kits;

import me.greenfoot5.castlesiege.Main;
import me.greenfoot5.castlesiege.database.CSActiveData;
import me.greenfoot5.castlesiege.events.combat.InCombat;
import me.greenfoot5.castlesiege.gui.BuyKitGui;
import me.greenfoot5.castlesiege.maps.TeamController;
import me.greenfoot5.conwymc.util.Messenger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

/**
 * A kit that's only available on a specific map
 */
public abstract class SignKit extends Kit implements Listener {

    private final int cost;

    // Kit Tracking
    private static final Collection<String> kits = new ArrayList<>();

    /**
     * Create a Sign Kit
     * @param name       This kit's name
     * @param baseHealth This kit's base health
     * @param regenAmount The amount to regen every regen tick
     * @param material The material to display in GUIS
     * @param cost The cost of the kit
     */
    public SignKit(String name, int baseHealth, double regenAmount, Material material, int cost) {
        super(name, baseHealth, regenAmount, material, NamedTextColor.DARK_AQUA);

        if (!kits.contains(getSpacelessName()))
            kits.add(getSpacelessName());

        this.cost = cost;
    }

    /**
     * Create a Map Kit
     * @param name       This kit's name
     * @param baseHealth This kit's base health
     * @param regenAmount The amount to regen every regen tick
     * @param material The material to display in GUIS
     */
    public SignKit(String name, int baseHealth, double regenAmount, Material material) {
        super(name, baseHealth, regenAmount, material, NamedTextColor.DARK_AQUA);

        if (!kits.contains(getSpacelessName()))
            kits.add(getSpacelessName());

        this.cost = -1;
    }

    /**
     * Check if the player can select this kit
     * @param sender Source of the command
     * @param verbose Whether error messages should be sent
     * @param isRandom If the kit is selected by the random command
     * @return Whether the player can select this kit
     */
    @Override
    public boolean canSelect(CommandSender sender, boolean applyLimit, boolean verbose, boolean isRandom) {
        UUID uuid = ((Player) sender).getUniqueId();

        // Check the kit is valid for this map
        CostType costType = TeamController.getTeam(uuid).kits.get(getSpacelessName().toLowerCase());
        costType = costType == null ? TeamController.getTeam(uuid).kits.get(name.toLowerCase()) : costType;
        if (costType == null && verbose) {
            Messenger.sendError("This kit isn't available on this map!", sender);
            return false;
        }

        // If it costs on the current map, has the player unlocked the kit?
        boolean hasKit = CSActiveData.getData(uuid).hasKit(getSpacelessName());
        if (costType == CostType.coins && !hasKit && !CoinKit.isFree()) {
            if (verbose) {
                if (Kit.equippedKits.get(uuid) == null) {
                    Messenger.sendError(String.format("You no longer have access to %s!", name), sender);
                } else {
                    new BuyKitGui(this, this.cost, (Player) sender);
                }
            }
            return false;
        }

        return super.canSelect(sender, applyLimit, verbose, isRandom);
    }

    /**
     * Get all map kit names
     * @return All map kit names without spaces
     */
    public static Collection<String> getKits() {
        return kits;
    }

    public int getCost() {
        return cost;
    }

    /**
     * @return Displays the cost for the footer of a kit gui's lore
     */
    public ArrayList<Component> getGuiCostText() {
        ArrayList<Component> text = new ArrayList<>();
        text.add(Component.empty());
        text.add(Component.text("Can be played via signs in map lobbies", color).decorate(TextDecoration.BOLD));
        return text;
    }

    /**
     * Selects the kit if the player clicks the signName
     * @param e When a player clicks a signName
     */
    @EventHandler
    public void onClickSign(PlayerInteractEvent e) {
        // Prevent spawning by physical actions, e.g. stepping on a pressure plate
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK && e.getAction() != Action.LEFT_CLICK_BLOCK
        && e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.LEFT_CLICK_AIR )
            return;

        Player player = e.getPlayer();
        Block target = player.getTargetBlockExact(50);

        if (target == null || !(target.getState() instanceof Sign sign))
            return;

        if (!InCombat.isPlayerInLobby(e.getPlayer().getUniqueId()))
            return;

        // Only allow use in lobby
        if (e.getHand() != EquipmentSlot.HAND) {
            return;
        }

        // Check if sign has sign name
        StringBuilder content = new StringBuilder();
        for (Component line : sign.getSide(Side.FRONT).lines()) {
            content.append(PlainTextComponentSerializer.plainText().serialize(line).toLowerCase().replace(" ", ""));
        }

        if (content.toString().contains(this.name.toLowerCase()) || content.toString().contains(getSpacelessName().toLowerCase())) {
            Bukkit.getScheduler().runTask(Main.plugin, () -> e.getPlayer().performCommand(getSpacelessName()));
        }
    }

    public enum CostType {
        free,
        coins,
    }
}
