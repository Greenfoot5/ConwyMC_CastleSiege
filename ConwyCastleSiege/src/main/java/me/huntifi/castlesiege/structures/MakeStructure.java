package me.huntifi.castlesiege.structures;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;

public class MakeStructure {
	
	public static void createSchematicStructure(Location spawnloc, String schematicName, String worldName) throws WorldEditException {
		
		Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("ConwyCastleSiege");
		
		Location loc = spawnloc;
		
		Plugin worldEditPlugin = Bukkit.getPluginManager().getPlugin("WorldEdit");
		
		File schematic = new File(worldEditPlugin.getDataFolder() + File.separator + "/schematics/" + schematicName + ".schem");
		
		System.out.println(schematic);
		
		ClipboardFormat format = ClipboardFormats.findByFile(schematic);
		
		try (ClipboardReader reader = format.getReader(new FileInputStream(schematic))) {
			
		   Clipboard clipboard = reader.read();
		   
		   com.sk89q.worldedit.world.World world = BukkitAdapter.adapt(plugin.getServer().getWorld(worldName));
  
			try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(world, -1)) {
			    Operation operation = new ClipboardHolder(clipboard)
			            .createPaste(editSession)
			            .to(BlockVector3.at(loc.getBlockX(), loc.getBlockY(), loc.getZ()))
			            .ignoreAirBlocks(false)
			            .build();
			    Operations.complete(operation);
			}
		   
		} catch (IOException e) {
			
			e.printStackTrace();
			
		}
	}

}
