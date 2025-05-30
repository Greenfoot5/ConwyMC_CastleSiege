package me.greenfoot5.castlesiege.commands.gameplay;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.greenfoot5.castlesiege.Main;
import me.greenfoot5.castlesiege.data_types.Booster;
import me.greenfoot5.castlesiege.data_types.CSPlayerData;
import me.greenfoot5.castlesiege.data_types.CoinBooster;
import me.greenfoot5.castlesiege.data_types.KitBooster;
import me.greenfoot5.castlesiege.database.CSActiveData;
import me.greenfoot5.castlesiege.kits.kits.CoinKit;
import me.greenfoot5.castlesiege.kits.kits.Kit;
import me.greenfoot5.conwymc.gui.Gui;
import me.greenfoot5.conwymc.util.Messenger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * A command to check your boosters, and/or use them
 */
public class BoosterCommand implements CommandExecutor, Listener {
    private static final int inputWaitDuration = 10;
    public static final Map<UUID, KitBooster> waitingForWildKit = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            showBoosters(sender);
            return true;
        }
        if (args[0].equals("use")) {
            useBooster(sender, args);
            return true;
        }
        Messenger.sendError("Invalid arg: " + args[0] + ". Should be 'use' or a type of booster.", sender);
        return true;
    }

    private void showBoosters(@NotNull CommandSender sender) {
        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();
        CSPlayerData data = CSActiveData.getData(uuid);

        Gui gui = createGUI(data.getBoosters());
        gui.open(player);
    }

    /**
     * @param boosters The boosters to add to the gui
     * @return The Gui with the boosters in
     */
    private static Gui createGUI(List<Booster> boosters) {
        Gui gui = new Gui(Component.text("Booster Selection"), (boosters.size() / 9 + 1), true);
        boosters.sort(Booster::compareTo);
        for (int i = 0; i < boosters.size(); i++) {
            Booster booster = boosters.get(i);
            gui.addItem(booster.getName(), booster.material, booster.getLore(), i, "booster use " + booster.id, true);
        }
        return gui;
    }

    private void useBooster(@NotNull CommandSender sender, @NotNull String[] args) {
        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();
        CSPlayerData data = CSActiveData.getData(uuid);
        int id = Integer.parseInt(args[1]);
        for (Booster booster : data.getBoosters()) {
            if (booster.id == id) {

                if (booster instanceof KitBooster kBooster) {

                    // Kit boosters don't stack
                    if (CoinKit.boostedKits.contains(kBooster.kitName)) {
                        Messenger.sendError("A kit booster of that type is already active!", sender);
                        return;
                    }

                    // Allow the user to input a kit
                    if (kBooster.kitName.equalsIgnoreCase("WILD")) {
                        if (waitingForWildKit.containsKey(uuid)) {
                            Messenger.sendError("You're already trying to input an coin kit!", sender);
                            return;
                        }

                        Messenger.requestInput("Enter the coin kit (one word, capitalise names) to boost in chat: (" + inputWaitDuration + "s)", sender);
                        waitingForWildKit.put(player.getUniqueId(), kBooster);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (waitingForWildKit.remove(player.getUniqueId()) != null) {
                                    Messenger.sendError("Ran out of time to input kit name.", sender);
                                }
                            }
                        }.runTaskLater(Main.plugin, inputWaitDuration * 20);
                        return;
                    }

                    // Select a random kit
                    if (kBooster.kitName.equalsIgnoreCase("RANDOM")) {
                        Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
                            ArrayList<String> dKits = (ArrayList<String>) CoinKit.getKits();
                            dKits.remove(CoinKit.boostedKits);
                            kBooster.kitName = dKits.get(new Random().nextInt(dKits.size()));
                            data.useBooster(uuid, kBooster);
                            removeBooster(kBooster.id, uuid);
                            activateBooster(kBooster, uuid);
                        });
                        return;
                    }
                }

                // Activate the booster
                Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
                    data.useBooster(uuid, booster);
                    removeBooster(booster.id, uuid);
                    activateBooster(booster, uuid);
                });
                return;
            }
        }
        Messenger.sendError("You don't own a booster with that id!", sender);
    }

    /**
     * Called when a player sends a message for input
     * @param e The chat event
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncChatEvent e) {

        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();

        if (!waitingForWildKit.containsKey(uuid)) {
            return;
        }

        String message = PlainTextComponentSerializer.plainText().serialize(e.message());
        e.setCancelled(true);

        Kit kit = Kit.getKit(message.trim());
        if (kit instanceof CoinKit && !CoinKit.boostedKits.contains(kit.getSpacelessName())) {
            KitBooster booster = waitingForWildKit.get(uuid);
            booster.kitName = message;
            CSPlayerData data = CSActiveData.getData(uuid);
            Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
                data.useBooster(uuid, booster);
                removeBooster(booster.id, uuid);
                activateBooster(booster, uuid);
            });
        } else {
            if (kit instanceof CoinKit) {
                Messenger.sendError("That kit is already being boosted!", player);
            }
            else {
                Messenger.sendError("Invalid coin kit name!", player);
            }
        }
        waitingForWildKit.remove(uuid);
    }

    private static void removeBooster(int boostId, UUID uuid) {
        PreparedStatement ps;
        try {
            ps = Main.SQL.getConnection().prepareStatement(
                    "DELETE FROM player_boosters WHERE ID = ? AND uuid = ?");
            ps.setInt(1, boostId);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ignored) { }
    }

    private static void activateBooster(Booster booster, UUID uuid) {
        PreparedStatement ps;
        try {
            ps = Main.SQL.getConnection().prepareStatement(
                    "INSERT INTO active_boosters (booster_id, player_uuid, booster_type, boost_value, expire_time)\n" +
                            "VALUES (?, ?, ?, ?, ?);");
            ps.setInt(1, booster.id);
            ps.setString(2, uuid.toString());
            ps.setString(3, getBoosterType(booster));
            ps.setString(4, booster.getValue());
            ps.setTimestamp(5, new Timestamp(System.currentTimeMillis() + booster.duration * 1000L));
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static String getBoosterType(Booster booster) {
        if (booster instanceof CoinBooster) {
            return "COIN";
        }  else if (booster instanceof KitBooster) {
            return "KIT";
        } else {
            return "NULL";
        }
    }
}
