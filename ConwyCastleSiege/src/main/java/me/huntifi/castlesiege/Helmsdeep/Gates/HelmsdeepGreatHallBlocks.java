package me.huntifi.castlesiege.Helmsdeep.Gates;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;

public class HelmsdeepGreatHallBlocks {

	public static ArrayList<Block> GateBlocks = new ArrayList<Block>();
	
	public static void gateblocks() {
		
		Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("CastleSiege");
		
		Block block1 = new Location(plugin.getServer().getWorld("HelmsDeep"), 970, 72, 1002).getBlock();
		Block block2 = new Location(plugin.getServer().getWorld("HelmsDeep"), 970, 72, 1001).getBlock();
		Block block3 = new Location(plugin.getServer().getWorld("HelmsDeep"), 970, 72, 1000).getBlock();
		Block block4 = new Location(plugin.getServer().getWorld("HelmsDeep"), 970, 72, 999).getBlock();
		Block block5 = new Location(plugin.getServer().getWorld("HelmsDeep"), 970, 72, 998).getBlock();
		
		Block block1a = new Location(plugin.getServer().getWorld("HelmsDeep"), 970, 73, 1002).getBlock();
		Block block2a = new Location(plugin.getServer().getWorld("HelmsDeep"), 970, 73, 1001).getBlock();
		Block block3a = new Location(plugin.getServer().getWorld("HelmsDeep"), 970, 73, 1000).getBlock();
		Block block4a = new Location(plugin.getServer().getWorld("HelmsDeep"), 970, 73, 999).getBlock();
		Block block5a = new Location(plugin.getServer().getWorld("HelmsDeep"), 970, 73, 998).getBlock();
		
		Block block1b = new Location(plugin.getServer().getWorld("HelmsDeep"), 970, 74, 1002).getBlock();
		Block block2b = new Location(plugin.getServer().getWorld("HelmsDeep"), 970, 74, 1001).getBlock();
		Block block3b = new Location(plugin.getServer().getWorld("HelmsDeep"), 970, 74, 1000).getBlock();
		Block block4b = new Location(plugin.getServer().getWorld("HelmsDeep"), 970, 74, 999).getBlock();
		Block block5b = new Location(plugin.getServer().getWorld("HelmsDeep"), 970, 74, 998).getBlock();
		
		Block block1c = new Location(plugin.getServer().getWorld("HelmsDeep"), 970, 75, 1002).getBlock();
		Block block2c = new Location(plugin.getServer().getWorld("HelmsDeep"), 970, 75, 1001).getBlock();
		Block block3c = new Location(plugin.getServer().getWorld("HelmsDeep"), 970, 75, 1000).getBlock();
		Block block4c = new Location(plugin.getServer().getWorld("HelmsDeep"), 970, 75, 999).getBlock();
		Block block5c = new Location(plugin.getServer().getWorld("HelmsDeep"), 970, 75, 998).getBlock();
		
		Block block1d = new Location(plugin.getServer().getWorld("HelmsDeep"), 970, 76, 1002).getBlock();
		Block block2d = new Location(plugin.getServer().getWorld("HelmsDeep"), 970, 76, 1001).getBlock();
		Block block3d = new Location(plugin.getServer().getWorld("HelmsDeep"), 970, 76, 1000).getBlock();
		Block block4d = new Location(plugin.getServer().getWorld("HelmsDeep"), 970, 76, 999).getBlock();
		Block block5d = new Location(plugin.getServer().getWorld("HelmsDeep"), 970, 76, 998).getBlock();
		
		Block block1e = new Location(plugin.getServer().getWorld("HelmsDeep"), 970, 77, 1002).getBlock();
		Block block2e = new Location(plugin.getServer().getWorld("HelmsDeep"), 970, 77, 1001).getBlock();
		Block block3e = new Location(plugin.getServer().getWorld("HelmsDeep"), 970, 77, 1000).getBlock();
		Block block4e = new Location(plugin.getServer().getWorld("HelmsDeep"), 970, 77, 999).getBlock();
		Block block5e = new Location(plugin.getServer().getWorld("HelmsDeep"), 970, 77, 998).getBlock();


		
		GateBlocks.clear();
		GateBlocks.add(block1);
		GateBlocks.add(block2);
		GateBlocks.add(block3);
		GateBlocks.add(block4);
		GateBlocks.add(block5);
		
		GateBlocks.add(block1a);
		GateBlocks.add(block2a);
		GateBlocks.add(block3a);
		GateBlocks.add(block4a);
		GateBlocks.add(block5a);
		
		GateBlocks.add(block1b);
		GateBlocks.add(block2b);
		GateBlocks.add(block3b);
		GateBlocks.add(block4b);
		GateBlocks.add(block5b);
		
		GateBlocks.add(block1c);
		GateBlocks.add(block2c);
		GateBlocks.add(block3c);
		GateBlocks.add(block4c);
		GateBlocks.add(block5c);
		
		GateBlocks.add(block1d);
		GateBlocks.add(block2d);
		GateBlocks.add(block3d);
		GateBlocks.add(block4d);
		GateBlocks.add(block5d);
		
		GateBlocks.add(block1e);
		GateBlocks.add(block2e);
		GateBlocks.add(block3e);
		GateBlocks.add(block4e);
		GateBlocks.add(block5e);


		
	}
}
