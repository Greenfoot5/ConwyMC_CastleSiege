package me.greenfoot5.castlesiege.structures;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import me.greenfoot5.castlesiege.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

/**
 * Handles spawning a schematic
 */
public class SchematicSpawner {

	/**
	 * Spawns a schematic at a certain location
	 * @param spawnLocation The location to spawn the schematic
	 * @param schematicName The name of the schematic to spawn
	 */
	public static void spawnSchematic(Location spawnLocation, String schematicName) {
		Bukkit.getScheduler().runTask(Main.plugin, () -> {
			Plugin worldEditPlugin = Bukkit.getPluginManager().getPlugin("WorldEdit");
			assert worldEditPlugin != null;
			File schematic = new File(worldEditPlugin.getDataFolder() + File.separator + "/schematics/" + schematicName + ".schem");
			ClipboardFormat format = ClipboardFormats.findByFile(schematic);

			try {
				assert format != null;
				try (ClipboardReader reader = format.getReader(Files.newInputStream(schematic.toPath()))) {
					Clipboard clipboard = reader.read();
					com.sk89q.worldedit.world.World world = BukkitAdapter.adapt(Objects.requireNonNull(spawnLocation.getWorld()));

					try (EditSession editSession = WorldEdit.getInstance().newEditSession(world)) {
						Operation operation = new ClipboardHolder(clipboard)
								.createPaste(editSession)
								.to(BlockVector3.at(spawnLocation.getBlockX(), spawnLocation.getBlockY(), spawnLocation.getZ()))
								.ignoreAirBlocks(false)
								.build();
						Operations.complete(operation);
					}
				}
			} catch (IOException | WorldEditException e) {
				e.printStackTrace();
			}
		});
	}
}
