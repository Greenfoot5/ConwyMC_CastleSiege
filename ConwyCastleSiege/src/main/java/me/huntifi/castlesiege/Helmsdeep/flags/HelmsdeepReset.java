package me.huntifi.castlesiege.Helmsdeep.flags;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.Plugin;

import com.sk89q.worldedit.WorldEditException;

import me.huntifi.castlesiege.Helmsdeep.Boat.HelmsdeepCaveBoat;
import me.huntifi.castlesiege.kits.Engineer.EngineerCobweb;
import me.huntifi.castlesiege.kits.Engineer.EngineerTraps;
import me.huntifi.castlesiege.ladders.LadderEvent;
import me.huntifi.castlesiege.structures.MakeStructure;

public class HelmsdeepReset {

	static Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("ConwyCastleSiege");


	public static void onReset() {
		
		HelmsdeepCaveBoat.clearBoats();
		
		List<Block> traps = EngineerTraps.traps;
	    for(Block b : traps){
	        b.setType(Material.AIR);
	    }
	    EngineerTraps.traps.clear();
		
		List<Block> ladder = LadderEvent.Ladders;
	    for(Block b : ladder){
	        b.setType(Material.AIR);
	    }
	    LadderEvent.Ladders.clear();
	    
		List<Block> cobweb = EngineerCobweb.Cobwebs;
	    for(Block b : cobweb){
	        b.setType(Material.AIR);
	    }
	   EngineerCobweb.Cobwebs.clear();

		FlagCounter.setFlagCounter("SupplyCamp", 0);
		FlagCounter.setFlagCounter("Horn", 0);
		FlagCounter.setFlagCounter("MainGate", 0);
		FlagCounter.setFlagCounter("Courtyard", 0);
		FlagCounter.setFlagCounter("GreatHalls", 0);
		FlagCounter.setFlagCounter("Caves", 0);


		FlagTeam.setFlagTeam("SupplyCamp", 2);
		FlagTeam.setFlagTeam("Horn", 2);
		FlagTeam.setFlagTeam("MainGate", 2);
		FlagTeam.setFlagTeam("Courtyard", 2);
		FlagTeam.setFlagTeam("GreatHalls", 2);
		FlagTeam.setFlagTeam("Caves", 2);


		Location Woolmap1 = new Location(plugin.getServer().getWorld("HelmsDeep"), 272, 13, 955);
		Woolmap1.getBlock().setType(Material.GREEN_WOOL);
		Location Woolmap2 = new Location(plugin.getServer().getWorld("HelmsDeep"), 274, 15, 955);
		Woolmap2.getBlock().setType(Material.GREEN_WOOL);	
		Location Woolmap3 = new Location(plugin.getServer().getWorld("HelmsDeep"), 274, 17, 955);
		Woolmap3.getBlock().setType(Material.GREEN_WOOL);
		Location Woolmap4 = new Location(plugin.getServer().getWorld("HelmsDeep"), 274, 19, 955);
		Woolmap4.getBlock().setType(Material.GREEN_WOOL);
		Location Woolmap5 = new Location(plugin.getServer().getWorld("HelmsDeep"), 277, 16, 955);
		Woolmap5.getBlock().setType(Material.GREEN_WOOL);
		Location Woolmap6 = new Location(plugin.getServer().getWorld("HelmsDeep"), 282, 15, 955);
		Woolmap6.getBlock().setType(Material.GREEN_WOOL);

		Location Woolmap1b = new Location(plugin.getServer().getWorld("HelmsDeep"), 1753, 21, 952);
		Woolmap1b.getBlock().setType(Material.GREEN_WOOL);
		Location Woolmap2b = new Location(plugin.getServer().getWorld("HelmsDeep"), 1753, 20, 957);
		Woolmap2b.getBlock().setType(Material.GREEN_WOOL);	
		Location Woolmap3b = new Location(plugin.getServer().getWorld("HelmsDeep"), 1753, 23, 962);
		Woolmap3b.getBlock().setType(Material.GREEN_WOOL);
		Location Woolmap4b = new Location(plugin.getServer().getWorld("HelmsDeep"), 1753, 21, 960);
		Woolmap4b.getBlock().setType(Material.GREEN_WOOL);
		Location Woolmap5b = new Location(plugin.getServer().getWorld("HelmsDeep"), 1753, 19, 960);
		Woolmap5b.getBlock().setType(Material.GREEN_WOOL);
		Location Woolmap6b = new Location(plugin.getServer().getWorld("HelmsDeep"), 1753, 17, 960);
		Woolmap6b.getBlock().setType(Material.GREEN_WOOL);
		
		Location tnt = new Location(plugin.getServer().getWorld("HelmsDeep"), 1168, 35, 1125);
		tnt.getBlock().setType(Material.TNT);
		
		//clears all items on the ground
		
		List<Entity> entList = Bukkit.getServer().getWorld("HelmsDeep").getEntities();
		for(Entity current : entList){
			if (current.getType() == EntityType.DROPPED_ITEM){
                current.remove();
            }
		}

		Location CampFlag = new Location(plugin.getServer().getWorld("HelmsDeep"), 986, 36, 1141);
		
		try {
			MakeStructure.createSchematicStructure(CampFlag, "HelmsdeepCampFlag", "HelmsDeep");
		} catch (WorldEditException e) {
			e.printStackTrace();
		}
		
		Location MainGateFlag = new Location(plugin.getServer().getWorld("HelmsDeep"), 1027, 52, 1000);
		
		try {
			MakeStructure.createSchematicStructure(MainGateFlag, "HelmsdeepMainGateFlag", "HelmsDeep");
		} catch (WorldEditException e) {
			e.printStackTrace();
		}
		
		
		Location CourtyardFlag = new Location(plugin.getServer().getWorld("HelmsDeep"), 1023, 75, 1000);
		
		try {
			MakeStructure.createSchematicStructure(CourtyardFlag, "HelmsdeepCourtyardFlag", "HelmsDeep");
		} catch (WorldEditException e) {
			e.printStackTrace();
		}
		
		Location HornFlag = new Location(plugin.getServer().getWorld("HelmsDeep"), 983, 145, 1037);
		
		try {
			MakeStructure.createSchematicStructure(HornFlag, "HelmsdeepHornFlag", "HelmsDeep");
		} catch (WorldEditException e) {
			e.printStackTrace();
		}
		
		Location HallFlag = new Location(plugin.getServer().getWorld("HelmsDeep"), 969, 72, 1000);
		
		try {
			MakeStructure.createSchematicStructure(HallFlag, "HelmsdeepGreatHallFlag", "HelmsDeep");
		} catch (WorldEditException e) {
			e.printStackTrace();
		}
		
		Location CavesFlag = new Location(plugin.getServer().getWorld("HelmsDeep"), 868, 51, 940);
		
		try {
			MakeStructure.createSchematicStructure(CavesFlag, "HelmsdeepCavesFlag", "HelmsDeep");
		} catch (WorldEditException e) {
			e.printStackTrace();
		}
		
		final Location wallLoc = new Location(plugin.getServer().getWorld("HelmsDeep"), 1040, 34, 1140);

		try {
			MakeStructure.createSchematicStructure(wallLoc, "HelmsdeepWallFull", "HelmsDeep");
		} catch (WorldEditException e1) {
			e1.printStackTrace();
		}
		
		final Location maingateLoc = new Location(plugin.getServer().getWorld("HelmsDeep"), 1038, 52, 1000);

		try {
			MakeStructure.createSchematicStructure(maingateLoc, "HelmsdeepMainGateFull", "HelmsDeep");
		} catch (WorldEditException e1) {
			e1.printStackTrace();
		}
		
		final Location ladderss = new Location(plugin.getServer().getWorld("HelmsDeep"), 1038, 62, 1005);

		try {
			MakeStructure.createSchematicStructure(ladderss, "HelmsdeepMainGateLadders", "HelmsDeep");
		} catch (WorldEditException e1) {
			e1.printStackTrace();
		}
		
		Location loc = new Location(plugin.getServer().getWorld("HelmsDeep"), 969, 72, 1000); 
		
		try {
			MakeStructure.createSchematicStructure(loc, "HelmsdeepGreatHallFull", "HelmsDeep");
		} catch (WorldEditException e) {
			e.printStackTrace();
		}
		
		Location block = new Location(plugin.getServer().getWorld("HelmsDeep"), 972, 72, 1000);
		block.getBlock().setType(Material.CHISELED_STONE_BRICKS);
		
		Location cake1 = new Location(plugin.getServer().getWorld("HelmsDeep"), 976, 87, 991);
		cake1.getBlock().setType(Material.CAKE);
		
		Location cake2 = new Location(plugin.getServer().getWorld("HelmsDeep"), 951, 73, 994);
		cake2.getBlock().setType(Material.CAKE);
		
		Location cake3 = new Location(plugin.getServer().getWorld("HelmsDeep"), 942, 73, 1006);
		cake3.getBlock().setType(Material.CAKE);
	}

}
