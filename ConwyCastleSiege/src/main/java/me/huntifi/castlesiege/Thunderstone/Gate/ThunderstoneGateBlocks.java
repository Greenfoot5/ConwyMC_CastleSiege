package me.huntifi.castlesiege.Thunderstone.Gate;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;

public class ThunderstoneGateBlocks {
	
	public static ArrayList<Block> GateBlocks = new ArrayList<Block>();
	
	public static void gateblocks() {
		
		Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("CastleSiege");
		
		Block block1 = new Location(plugin.getServer().getWorld("Thunderstone"), 157, 65, 65).getBlock();
		Block block2 = new Location(plugin.getServer().getWorld("Thunderstone"), 157, 65, 66).getBlock();
		Block block3 = new Location(plugin.getServer().getWorld("Thunderstone"), 157, 65, 67).getBlock();
		Block block4 = new Location(plugin.getServer().getWorld("Thunderstone"), 157, 65, 68).getBlock();
		Block block4a = new Location(plugin.getServer().getWorld("Thunderstone"), 157, 65, 69).getBlock();
		
		Block block5 = new Location(plugin.getServer().getWorld("Thunderstone"), 157, 66, 65).getBlock();
		Block block6 = new Location(plugin.getServer().getWorld("Thunderstone"), 157, 66, 66).getBlock();
		Block block7 = new Location(plugin.getServer().getWorld("Thunderstone"), 157, 66, 67).getBlock();
		Block block8 = new Location(plugin.getServer().getWorld("Thunderstone"), 157, 66, 68).getBlock();
		Block block8a = new Location(plugin.getServer().getWorld("Thunderstone"), 157, 66, 69).getBlock();
		
		Block block9 = new Location(plugin.getServer().getWorld("Thunderstone"), 157, 67, 65).getBlock();
		Block block10 = new Location(plugin.getServer().getWorld("Thunderstone"), 157, 67, 66).getBlock();
		Block block11 = new Location(plugin.getServer().getWorld("Thunderstone"), 157, 67, 67).getBlock();
		Block block12 = new Location(plugin.getServer().getWorld("Thunderstone"), 157, 67, 68).getBlock();
		Block block12a = new Location(plugin.getServer().getWorld("Thunderstone"), 157, 67, 69).getBlock();
		
		Block block13 = new Location(plugin.getServer().getWorld("Thunderstone"), 157, 68, 65).getBlock();
		Block block14 = new Location(plugin.getServer().getWorld("Thunderstone"), 157, 68, 66).getBlock();
		Block block15 = new Location(plugin.getServer().getWorld("Thunderstone"), 157, 68, 67).getBlock();
		Block block16 = new Location(plugin.getServer().getWorld("Thunderstone"), 157, 68, 68).getBlock();
		Block block16a = new Location(plugin.getServer().getWorld("Thunderstone"), 157, 68, 69).getBlock();
		
		Block block17 = new Location(plugin.getServer().getWorld("Thunderstone"), 157, 69, 65).getBlock();
		Block block18 = new Location(plugin.getServer().getWorld("Thunderstone"), 157, 69, 66).getBlock();
		Block block19 = new Location(plugin.getServer().getWorld("Thunderstone"), 157, 69, 67).getBlock();
		Block block20 = new Location(plugin.getServer().getWorld("Thunderstone"), 157, 69, 68).getBlock();
		Block block20a = new Location(plugin.getServer().getWorld("Thunderstone"), 157, 69, 69).getBlock();
		
		Block block21 = new Location(plugin.getServer().getWorld("Thunderstone"), 157, 70, 65).getBlock();
		Block block22 = new Location(plugin.getServer().getWorld("Thunderstone"), 157, 70, 66).getBlock();
		Block block23 = new Location(plugin.getServer().getWorld("Thunderstone"), 157, 70, 67).getBlock();
		Block block24 = new Location(plugin.getServer().getWorld("Thunderstone"), 157, 70, 68).getBlock();
		Block block25 = new Location(plugin.getServer().getWorld("Thunderstone"), 157, 70, 69).getBlock();



		
		GateBlocks.clear();
		GateBlocks.add(block1);
		GateBlocks.add(block2);
		GateBlocks.add(block3);
		GateBlocks.add(block4a);
		GateBlocks.add(block4);
		
		
		GateBlocks.add(block5);
		GateBlocks.add(block6);
		GateBlocks.add(block7);
		GateBlocks.add(block8a);
		GateBlocks.add(block8);
		
		GateBlocks.add(block9);
		GateBlocks.add(block10);
		GateBlocks.add(block11);
		GateBlocks.add(block12a);
		GateBlocks.add(block12);
		
		GateBlocks.add(block13);
		GateBlocks.add(block15);
		GateBlocks.add(block16);
		GateBlocks.add(block14);
		GateBlocks.add(block16a);
		
		GateBlocks.add(block17);
		GateBlocks.add(block18);
		GateBlocks.add(block19);
		GateBlocks.add(block20);
		GateBlocks.add(block20a);
		
		GateBlocks.add(block21);
		GateBlocks.add(block22);
		GateBlocks.add(block23);
		GateBlocks.add(block24);
		GateBlocks.add(block25);

	


		
	}

}
