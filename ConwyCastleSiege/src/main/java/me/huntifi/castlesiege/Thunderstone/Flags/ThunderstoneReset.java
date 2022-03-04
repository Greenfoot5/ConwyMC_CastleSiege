package me.huntifi.castlesiege.Thunderstone.Flags;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;

import com.sk89q.worldedit.WorldEditException;

import me.huntifi.castlesiege.kits.Engineer.EngineerCobweb;
import me.huntifi.castlesiege.kits.Engineer.EngineerTraps;
import me.huntifi.castlesiege.ladders.LadderEvent;
import me.huntifi.castlesiege.structures.MakeStructure;

public class ThunderstoneReset {
	
	static Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("ConwyCastleSiege");

	public static void onReset() {
		
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

		TS_FlagCounter.setFlagCounter("stairhall", 0);
		TS_FlagCounter.setFlagCounter("skyviewtower", 0);
		TS_FlagCounter.setFlagCounter("easttower", 0);
		TS_FlagCounter.setFlagCounter("westtower", 0);
		TS_FlagCounter.setFlagCounter("shiftedtower", 0);
		TS_FlagCounter.setFlagCounter("lonelytower", 0);
		TS_FlagCounter.setFlagCounter("twinbridge", 0);


		TS_FlagTeam.setFlagTeam("stairhall", 2);
		TS_FlagTeam.setFlagTeam("Horn", 2);
		TS_FlagTeam.setFlagTeam("easttower", 2);
		TS_FlagTeam.setFlagTeam("westtower", 2);
		TS_FlagTeam.setFlagTeam("shiftedtower", 2);
		TS_FlagTeam.setFlagTeam("lonelytower", 2);
		TS_FlagTeam.setFlagTeam("twinbridge", 2);

		Location Woolmap1 = new Location(plugin.getServer().getWorld("Thunderstone"), -171, 204, 101);
		Woolmap1.getBlock().setType(Material.ORANGE_WOOL);
		Location Woolmap2 = new Location(plugin.getServer().getWorld("Thunderstone"), -171, 204, 103);
		Woolmap2.getBlock().setType(Material.ORANGE_WOOL);	
		Location Woolmap3 = new Location(plugin.getServer().getWorld("Thunderstone"), -171, 204, 107);
		Woolmap3.getBlock().setType(Material.ORANGE_WOOL);
		Location Woolmap4 = new Location(plugin.getServer().getWorld("Thunderstone"), -171, 203, 105);
		Woolmap4.getBlock().setType(Material.ORANGE_WOOL);
		Location Woolmap5 = new Location(plugin.getServer().getWorld("Thunderstone"), -171, 202, 107);
		Woolmap5.getBlock().setType(Material.ORANGE_WOOL);
		Location Woolmap6 = new Location(plugin.getServer().getWorld("Thunderstone"), -171, 201, 109);
		Woolmap6.getBlock().setType(Material.ORANGE_WOOL);
		Location Woolmap7 = new Location(plugin.getServer().getWorld("Thunderstone"), -171, 201, 104);
		Woolmap7.getBlock().setType(Material.ORANGE_WOOL);

		Location Woolmap1b = new Location(plugin.getServer().getWorld("Thunderstone"), -174, 204, 154);
		Woolmap1b.getBlock().setType(Material.ORANGE_WOOL);
		Location Woolmap2b = new Location(plugin.getServer().getWorld("Thunderstone"), -174, 204, 156);
		Woolmap2b.getBlock().setType(Material.ORANGE_WOOL);	
		Location Woolmap3b = new Location(plugin.getServer().getWorld("Thunderstone"), -174, 204, 160);
		Woolmap3b.getBlock().setType(Material.ORANGE_WOOL);
		Location Woolmap4b = new Location(plugin.getServer().getWorld("Thunderstone"), -174, 203, 158);
		Woolmap4b.getBlock().setType(Material.ORANGE_WOOL);
		Location Woolmap5b = new Location(plugin.getServer().getWorld("Thunderstone"), -174, 202, 160);
		Woolmap5b.getBlock().setType(Material.ORANGE_WOOL);
		Location Woolmap6b = new Location(plugin.getServer().getWorld("Thunderstone"), -174, 201, 157);
		Woolmap6b.getBlock().setType(Material.ORANGE_WOOL);
		Location Woolmap7b = new Location(plugin.getServer().getWorld("Thunderstone"), -174, 201, 162);
		Woolmap7b.getBlock().setType(Material.ORANGE_WOOL);
		
		
		Location StairhallFlag = new Location(plugin.getServer().getWorld("Thunderstone"), 169, 66, 68);
		
		try {
			MakeStructure.createSchematicStructure(StairhallFlag, "Thunderstone_Stairhall", "Thunderstone");
		} catch (WorldEditException e) {
			e.printStackTrace();
		}
		
		
		Location Twinbridge = new Location(plugin.getServer().getWorld("Thunderstone"), 290, 116, 79);
		Location shiftedtower = new Location(plugin.getServer().getWorld("Thunderstone"), 231, 126, 80);
		Location easttower = new Location(plugin.getServer().getWorld("Thunderstone"), 182, 105, 53);
		Location westtower = new Location(plugin.getServer().getWorld("Thunderstone"), 155, 110, 85);
		Location lonelytower = new Location(plugin.getServer().getWorld("Thunderstone"), 120, 110, 56);
		
		Location gate = new Location(plugin.getServer().getWorld("Thunderstone"), 156, 65, 67);
		
		Location skyview = new Location(plugin.getServer().getWorld("Thunderstone"), 169, 153, 77);
		
		
		Location lonelytowerLadders = new Location(plugin.getServer().getWorld("Thunderstone"), 121, 102, 60);
		
		Location westtowerLadders = new Location(plugin.getServer().getWorld("Thunderstone"), 153, 102, 86);
		
		Location eastowerLadders = new Location(plugin.getServer().getWorld("Thunderstone"), 187, 98, 53);
		
		Location hallLadders = new Location(plugin.getServer().getWorld("Thunderstone"), 161, 87, 57);
		
		Location twinbridgeLadders = new Location(plugin.getServer().getWorld("Thunderstone"), 291, 110, 84);
		
		Location shiftedtowerLadders1 = new Location(plugin.getServer().getWorld("Thunderstone"), 227, 108, 74);
		
		Location shiftedtowerLadders2 = new Location(plugin.getServer().getWorld("Thunderstone"), 237, 108, 72);
		
		Location shiftedtowerLadders3 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 120, 76);
		
		try {
			MakeStructure.createSchematicStructure(Twinbridge, "Thunderstone_Flag", "Thunderstone");
		} catch (WorldEditException e) {
			e.printStackTrace();
		}
		
		
		try {
			MakeStructure.createSchematicStructure(shiftedtower, "Thunderstone_Flag", "Thunderstone");
		} catch (WorldEditException e) {
			e.printStackTrace();
		}
		
		try {
			MakeStructure.createSchematicStructure(easttower, "Thunderstone_Flag", "Thunderstone");
		} catch (WorldEditException e) {
			e.printStackTrace();
		}
		
		try {
			MakeStructure.createSchematicStructure(westtower, "Thunderstone_Flag", "Thunderstone");
		} catch (WorldEditException e) {
			e.printStackTrace();
		}
		
		try {
			MakeStructure.createSchematicStructure(lonelytower, "Thunderstone_Flag", "Thunderstone");
		} catch (WorldEditException e) {
			e.printStackTrace();
		}
		
		try {
			MakeStructure.createSchematicStructure(skyview, "Thunderstone_skyviewtower", "Thunderstone");
		} catch (WorldEditException e) {
			e.printStackTrace();
		}
		
		try {
			MakeStructure.createSchematicStructure(gate, "Thunderstone_GateFull", "Thunderstone");
		} catch (WorldEditException e) {
			e.printStackTrace();
		}
		
		try {
			MakeStructure.createSchematicStructure(lonelytowerLadders, "lonelytower_ladders", "Thunderstone");
		} catch (WorldEditException e) {
			e.printStackTrace();
		}
		
		
		try {
			MakeStructure.createSchematicStructure(westtowerLadders, "westtower_ladders", "Thunderstone");
		} catch (WorldEditException e) {
			e.printStackTrace();
		}
		
		try {
			MakeStructure.createSchematicStructure(eastowerLadders, "easttower_ladders", "Thunderstone");
		} catch (WorldEditException e) {
			e.printStackTrace();
		}
		
		try {
			MakeStructure.createSchematicStructure(hallLadders, "hallLadders", "Thunderstone");
		} catch (WorldEditException e) {
			e.printStackTrace();
		}
		
		try {
			MakeStructure.createSchematicStructure(twinbridgeLadders, "twinbridgeLadders", "Thunderstone");
		} catch (WorldEditException e) {
			e.printStackTrace();
		}
		
		try {
			MakeStructure.createSchematicStructure(shiftedtowerLadders1, "shiftedtowerLadders1", "Thunderstone");
		} catch (WorldEditException e) {
			e.printStackTrace();
		}
		
		try {
			MakeStructure.createSchematicStructure(shiftedtowerLadders2, "shiftedtowerLadders2", "Thunderstone");
		} catch (WorldEditException e) {
			e.printStackTrace();
		}
		
		try {
			MakeStructure.createSchematicStructure(shiftedtowerLadders3, "shiftedtowerLadders3", "Thunderstone");
		} catch (WorldEditException e) {
			e.printStackTrace();
		}
		
		//The cakes
		
		Location cake1 = new Location(plugin.getServer().getWorld("Thunderstone"), 161, 66, 55);
		cake1.getBlock().setType(Material.CAKE);
		//
		Location cake2 = new Location(plugin.getServer().getWorld("Thunderstone"), 184, 66, 65);
		cake2.getBlock().setType(Material.CAKE);
		//
		Location cake3 = new Location(plugin.getServer().getWorld("Thunderstone"), 168, 57, 66);
		cake3.getBlock().setType(Material.CAKE);
		//
		Location cake4 = new Location(plugin.getServer().getWorld("Thunderstone"), 168, 57, 63);
		cake4.getBlock().setType(Material.CAKE);
		//
		Location cake5 = new Location(plugin.getServer().getWorld("Thunderstone"), 168, 57, 62);
		cake5.getBlock().setType(Material.CAKE);
		//
		Location cake6 = new Location(plugin.getServer().getWorld("Thunderstone"), 174, 57, 62);
		cake6.getBlock().setType(Material.CAKE);
		
		Location cake7 = new Location(plugin.getServer().getWorld("Thunderstone"),174 ,57 , 63);
		cake7.getBlock().setType(Material.CAKE);
		
		Location cake8 = new Location(plugin.getServer().getWorld("Thunderstone"), 174, 57, 66);
		cake8.getBlock().setType(Material.CAKE);
		
		Location cake25 = new Location(plugin.getServer().getWorld("Thunderstone"),163 , 81,81);
		cake25.getBlock().setType(Material.CAKE);
		
		Location cake9 = new Location(plugin.getServer().getWorld("Thunderstone"),166 ,81 ,81 );
		cake9.getBlock().setType(Material.CAKE);
		
		Location cake10 = new Location(plugin.getServer().getWorld("Thunderstone"),175 , 81, 81);
		cake10.getBlock().setType(Material.CAKE);
		
		Location cake11 = new Location(plugin.getServer().getWorld("Thunderstone"),166 ,88 ,57 );
		cake11.getBlock().setType(Material.CAKE);
		
		Location cake12 = new Location(plugin.getServer().getWorld("Thunderstone"),165 , 88,79 );
		cake12.getBlock().setType(Material.CAKE);
		
		Location cake13 = new Location(plugin.getServer().getWorld("Thunderstone"), 173, 97, 67);
		cake13.getBlock().setType(Material.CAKE);
		
		Location cake14 = new Location(plugin.getServer().getWorld("Thunderstone"),174 ,108 , 68);
		cake14.getBlock().setType(Material.CAKE);
		
		Location cake15 = new Location(plugin.getServer().getWorld("Thunderstone"), 174,144 ,76);
		cake15.getBlock().setType(Material.CAKE);
		
		Location cake16 = new Location(plugin.getServer().getWorld("Thunderstone"), 226, 68, 72);
		cake16.getBlock().setType(Material.CAKE);
		
		Location cake17 = new Location(plugin.getServer().getWorld("Thunderstone"), 242 ,68 , 79);
		cake17.getBlock().setType(Material.CAKE);
		
		
		Location cake18 = new Location(plugin.getServer().getWorld("Thunderstone"),242 , 68,81);
		cake18.getBlock().setType(Material.CAKE);
		
		Location cake19 = new Location(plugin.getServer().getWorld("Thunderstone"),242 ,70 ,79);
		cake19.getBlock().setType(Material.CAKE);
		
		Location cake20 = new Location(plugin.getServer().getWorld("Thunderstone"),242 ,70 ,80);
		cake20.getBlock().setType(Material.CAKE);
		
		Location cake21 = new Location(plugin.getServer().getWorld("Thunderstone"),242 ,70 , 82);
		cake21.getBlock().setType(Material.CAKE);
		
		
		
		
	}
}
