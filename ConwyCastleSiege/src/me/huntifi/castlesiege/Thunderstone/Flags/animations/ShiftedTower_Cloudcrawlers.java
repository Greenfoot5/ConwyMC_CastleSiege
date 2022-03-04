package me.huntifi.castlesiege.Thunderstone.Flags.animations;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.huntifi.castlesiege.Thunderstone.Flags.TS_FlagMethod;

public class ShiftedTower_Cloudcrawlers {

	static Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("ConwyCastleSiege");

	public static void CapturingProcessA1(Player e) {

		Player p = e.getPlayer();

		String flag = "shiftedtower";

		int number = 15;
		int nextnumber = 0;

		int team = 1;
		//int EnemyTeam = 2;

		Material woolmapB = Material.CYAN_WOOL;
		Material woolmapA = Material.CYAN_WOOL;
		Material FlagMaterialLayer1Block1 = Material.CYAN_WOOL;
		Material FlagMaterialLayer1Block2 = Material.CYAN_WOOL;
		Material FlagMaterialLayer1Block3 = Material.CYAN_WOOL;
		Material FlagMaterialLayer1Block4 = Material.CYAN_WOOL;
		Material FlagMaterialLayer2Block1 = Material.LIGHT_BLUE_WOOL;
		Material FlagMaterialLayer2Block2 = Material.LIGHT_BLUE_WOOL;
		Material FlagMaterialLayer2Block3 = Material.LIGHT_BLUE_WOOL;
		Material FlagMaterialLayer2Block4 = Material.LIGHT_BLUE_WOOL;
		Block flagLayer1Block1 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 136, 79).getBlock();
		Block flagLayer1Block2 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 136, 78).getBlock();
		Block flagLayer1Block3 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 136, 77).getBlock();
		Block flagLayer1Block4 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 136, 76).getBlock();
		Block flagLayer2Block1 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 135, 79).getBlock();
		Block flagLayer2Block2 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 135, 78).getBlock();
		Block flagLayer2Block3 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 135, 77).getBlock();
		Block flagLayer2Block4 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 135, 76).getBlock();
		Block flagb1 = new Location(plugin.getServer().getWorld("Thunderstone"), -171, 204, 103).getBlock();
		Block flagb2 = new Location(plugin.getServer().getWorld("Thunderstone"), -174, 204, 156).getBlock();

		Block flagLayer1Block1a = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 134, 79).getBlock();
		Material Air = Material.AIR;
		Block flagLayer1Block2a = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 134, 78).getBlock();

		Block flagLayer1Block3a = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 134, 77).getBlock();

		Block flagLayer1Block4a = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 134, 76).getBlock();

		TS_FlagMethod.FullyCaptureOwnTeamFlag(p, flag, number, nextnumber, team, FlagMaterialLayer1Block1, flagLayer1Block1, FlagMaterialLayer1Block2, flagLayer1Block2, FlagMaterialLayer1Block3, flagLayer1Block3, FlagMaterialLayer1Block4, flagLayer1Block4, FlagMaterialLayer2Block1, flagLayer2Block1, FlagMaterialLayer2Block2, flagLayer2Block2, FlagMaterialLayer2Block3, flagLayer2Block3, FlagMaterialLayer2Block4, flagLayer2Block4, flagLayer1Block1a, flagLayer1Block2a, flagLayer1Block3a, flagLayer1Block4a , Air, flagb1, flagb2, woolmapA, woolmapB);

	}

	////


	public static void CapturingProcessA2(Player e) {

		Player p = e.getPlayer();

		String flag = "shiftedtower";

		int number = 14;
		int nextnumber = 15;

		int team = 1;
		//int EnemyTeam = 2;

		Material woolmapB = Material.LIGHT_BLUE_WOOL;
		Material woolmapA = Material.LIGHT_BLUE_WOOL;
		Material FlagMaterialLayer1Block1 = Material.CYAN_WOOL;
		Material FlagMaterialLayer1Block2 = Material.CYAN_WOOL;
		Material FlagMaterialLayer1Block3 = Material.CYAN_WOOL;
		Material FlagMaterialLayer1Block4 = Material.CYAN_WOOL;
		Material FlagMaterialLayer2Block1 = Material.LIGHT_BLUE_WOOL;
		Material FlagMaterialLayer2Block2 = Material.LIGHT_BLUE_WOOL;
		Material FlagMaterialLayer2Block3 = Material.LIGHT_BLUE_WOOL;
		Material FlagMaterialLayer2Block4 = Material.LIGHT_BLUE_WOOL;
		Block flagLayer1Block1 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 135, 79).getBlock();
		Block flagLayer1Block2 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 135, 78).getBlock();
		Block flagLayer1Block3 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 135, 77).getBlock();
		Block flagLayer1Block4 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 135, 76).getBlock();
		Block flagLayer2Block1 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 134, 79).getBlock();
		Block flagLayer2Block2 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 134, 78).getBlock();
		Block flagLayer2Block3 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 134, 77).getBlock();
		Block flagLayer2Block4 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 134, 76).getBlock();
		Block flagb1 = new Location(plugin.getServer().getWorld("Thunderstone"), -171, 204, 103).getBlock();
		Block flagb2 = new Location(plugin.getServer().getWorld("Thunderstone"), -174, 204, 156).getBlock();
		
		Block flagLayer1Block1a = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 133, 79).getBlock();
		Material Air = Material.AIR;
		Block flagLayer1Block2a = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 133, 78).getBlock();

		Block flagLayer1Block3a = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 133, 77).getBlock();

		Block flagLayer1Block4a = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 133, 76).getBlock();


		TS_FlagMethod.CaptureOwnTeamFlag(p, flag, number, nextnumber, team, FlagMaterialLayer1Block1, flagLayer1Block1, FlagMaterialLayer1Block2, flagLayer1Block2, FlagMaterialLayer1Block3, flagLayer1Block3, FlagMaterialLayer1Block4, flagLayer1Block4, FlagMaterialLayer2Block1, flagLayer2Block1, FlagMaterialLayer2Block2, flagLayer2Block2, FlagMaterialLayer2Block3, flagLayer2Block3, FlagMaterialLayer2Block4, flagLayer2Block4, flagLayer1Block1a, flagLayer1Block2a, flagLayer1Block3a, flagLayer1Block4a , Air, flagb1, flagb2, woolmapA, woolmapB);
	}
	
	
	public static void CapturingProcessA3(Player e) {

		Player p = e.getPlayer();

		String flag = "shiftedtower";

		int number = 13;
		int nextnumber = 14;

		int team = 1;
		//int EnemyTeam = 2;

		Material woolmapB = Material.LIGHT_BLUE_WOOL;
		Material woolmapA = Material.LIGHT_BLUE_WOOL;
		Material FlagMaterialLayer1Block1 = Material.CYAN_WOOL;
		Material FlagMaterialLayer1Block2 = Material.CYAN_WOOL;
		Material FlagMaterialLayer1Block3 = Material.CYAN_WOOL;
		Material FlagMaterialLayer1Block4 = Material.CYAN_WOOL;
		Material FlagMaterialLayer2Block1 = Material.LIGHT_BLUE_WOOL;
		Material FlagMaterialLayer2Block2 = Material.LIGHT_BLUE_WOOL;
		Material FlagMaterialLayer2Block3 = Material.LIGHT_BLUE_WOOL;
		Material FlagMaterialLayer2Block4 = Material.LIGHT_BLUE_WOOL;
		Block flagLayer1Block1 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 134, 79).getBlock();
		Block flagLayer1Block2 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 134, 78).getBlock();
		Block flagLayer1Block3 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 134, 77).getBlock();
		Block flagLayer1Block4 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 134, 76).getBlock();
		Block flagLayer2Block1 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 133, 79).getBlock();
		Block flagLayer2Block2 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 133, 78).getBlock();
		Block flagLayer2Block3 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 133, 77).getBlock();
		Block flagLayer2Block4 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 133, 76).getBlock();
		Block flagb1 = new Location(plugin.getServer().getWorld("Thunderstone"), -171, 204, 103).getBlock();
		Block flagb2 = new Location(plugin.getServer().getWorld("Thunderstone"), -174, 204, 156).getBlock();

		Block flagLayer1Block1a = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 132, 79).getBlock();
		Material Air = Material.AIR;
		Block flagLayer1Block2a = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 132, 78).getBlock();

		Block flagLayer1Block3a = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 132, 77).getBlock();

		Block flagLayer1Block4a = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 132, 76).getBlock();


		TS_FlagMethod.CaptureOwnTeamFlag(p, flag, number, nextnumber, team, FlagMaterialLayer1Block1, flagLayer1Block1, FlagMaterialLayer1Block2, flagLayer1Block2, FlagMaterialLayer1Block3, flagLayer1Block3, FlagMaterialLayer1Block4, flagLayer1Block4, FlagMaterialLayer2Block1, flagLayer2Block1, FlagMaterialLayer2Block2, flagLayer2Block2, FlagMaterialLayer2Block3, flagLayer2Block3, FlagMaterialLayer2Block4, flagLayer2Block4, flagLayer1Block1a, flagLayer1Block2a, flagLayer1Block3a, flagLayer1Block4a , Air, flagb1, flagb2, woolmapA, woolmapB);
	}
	
	
	
	
	public static void CapturingProcessA4(Player e) {

		Player p = e.getPlayer();

		String flag = "shiftedtower";

		int number = 12;
		int nextnumber = 13;

		int team = 1;
		//int EnemyTeam = 2;

		Material woolmapB = Material.LIGHT_BLUE_WOOL;
		Material woolmapA = Material.LIGHT_BLUE_WOOL;
		Material FlagMaterialLayer1Block1 = Material.CYAN_WOOL;
		Material FlagMaterialLayer1Block2 = Material.CYAN_WOOL;
		Material FlagMaterialLayer1Block3 = Material.CYAN_WOOL;
		Material FlagMaterialLayer1Block4 = Material.CYAN_WOOL;
		Material FlagMaterialLayer2Block1 = Material.LIGHT_BLUE_WOOL;
		Material FlagMaterialLayer2Block2 = Material.LIGHT_BLUE_WOOL;
		Material FlagMaterialLayer2Block3 = Material.LIGHT_BLUE_WOOL;
		Material FlagMaterialLayer2Block4 = Material.LIGHT_BLUE_WOOL;
		Block flagLayer1Block1 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 133, 79).getBlock();
		Block flagLayer1Block2 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 133, 78).getBlock();
		Block flagLayer1Block3 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 133, 77).getBlock();
		Block flagLayer1Block4 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 133, 76).getBlock();
		Block flagLayer2Block1 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 132, 79).getBlock();
		Block flagLayer2Block2 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 132, 78).getBlock();
		Block flagLayer2Block3 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 132, 77).getBlock();
		Block flagLayer2Block4 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 132, 76).getBlock();
		Block flagb1 = new Location(plugin.getServer().getWorld("Thunderstone"), -171, 204, 103).getBlock();
		Block flagb2 = new Location(plugin.getServer().getWorld("Thunderstone"), -174, 204, 156).getBlock();

		Block flagLayer1Block1a = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 131, 79).getBlock();
		Material Air = Material.AIR;
		Block flagLayer1Block2a = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 131, 78).getBlock();

		Block flagLayer1Block3a = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 131, 77).getBlock();

		Block flagLayer1Block4a = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 131, 76).getBlock();


		TS_FlagMethod.CaptureOwnTeamFlag(p, flag, number, nextnumber, team, FlagMaterialLayer1Block1, flagLayer1Block1, FlagMaterialLayer1Block2, flagLayer1Block2, FlagMaterialLayer1Block3, flagLayer1Block3, FlagMaterialLayer1Block4, flagLayer1Block4, FlagMaterialLayer2Block1, flagLayer2Block1, FlagMaterialLayer2Block2, flagLayer2Block2, FlagMaterialLayer2Block3, flagLayer2Block3, FlagMaterialLayer2Block4, flagLayer2Block4, flagLayer1Block1a, flagLayer1Block2a, flagLayer1Block3a, flagLayer1Block4a , Air, flagb1, flagb2, woolmapA, woolmapB);
	}
	
	
	
	public static void CapturingProcessA5(Player e) {

		Player p = e.getPlayer();

		String flag = "shiftedtower";

		int number = 11;
		int nextnumber = 12;

		int team = 1;
		//int EnemyTeam = 2;
		
		Material woolmapB = Material.LIGHT_BLUE_WOOL;
		Material woolmapA = Material.LIGHT_BLUE_WOOL;
		Material FlagMaterialLayer1Block1 = Material.CYAN_WOOL;
		Material FlagMaterialLayer1Block2 = Material.CYAN_WOOL;
		Material FlagMaterialLayer1Block3 = Material.CYAN_WOOL;
		Material FlagMaterialLayer1Block4 = Material.CYAN_WOOL;
		Material FlagMaterialLayer2Block1 = Material.LIGHT_BLUE_WOOL;
		Material FlagMaterialLayer2Block2 = Material.LIGHT_BLUE_WOOL;
		Material FlagMaterialLayer2Block3 = Material.LIGHT_BLUE_WOOL;
		Material FlagMaterialLayer2Block4 = Material.LIGHT_BLUE_WOOL;
		Block flagLayer1Block1 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 132, 79).getBlock();
		Block flagLayer1Block2 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 132, 78).getBlock();
		Block flagLayer1Block3 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 132, 77).getBlock();
		Block flagLayer1Block4 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 132, 76).getBlock();
		Block flagLayer2Block1 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 131, 79).getBlock();
		Block flagLayer2Block2 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 131, 78).getBlock();
		Block flagLayer2Block3 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 131, 77).getBlock();
		Block flagLayer2Block4 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 131, 76).getBlock();
		Block flagb1 = new Location(plugin.getServer().getWorld("Thunderstone"), -171, 204, 103).getBlock();
		Block flagb2 = new Location(plugin.getServer().getWorld("Thunderstone"), -174, 204, 156).getBlock();

		Block flagLayer1Block1a = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 130, 79).getBlock();
		Material Air = Material.AIR;
		Block flagLayer1Block2a = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 130, 78).getBlock();

		Block flagLayer1Block3a = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 130, 77).getBlock();

		Block flagLayer1Block4a = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 130, 76).getBlock();

		TS_FlagMethod.CaptureOwnTeamFlag(p, flag, number, nextnumber, team, FlagMaterialLayer1Block1, flagLayer1Block1, FlagMaterialLayer1Block2, flagLayer1Block2, FlagMaterialLayer1Block3, flagLayer1Block3, FlagMaterialLayer1Block4, flagLayer1Block4, FlagMaterialLayer2Block1, flagLayer2Block1, FlagMaterialLayer2Block2, flagLayer2Block2, FlagMaterialLayer2Block3, flagLayer2Block3, FlagMaterialLayer2Block4, flagLayer2Block4, flagLayer1Block1a, flagLayer1Block2a, flagLayer1Block3a, flagLayer1Block4a , Air, flagb1, flagb2, woolmapA, woolmapB);
	}
	
	
	public static void CapturingProcessA6(Player e) {

		Player p = e.getPlayer();

		String flag = "shiftedtower";

		int number = 10;
		int nextnumber = 11;

		int team = 1;
		//int EnemyTeam = 2;

		Material woolmapB = Material.LIGHT_BLUE_WOOL;
		Material woolmapA = Material.LIGHT_BLUE_WOOL;
		Material FlagMaterialLayer1Block1 = Material.CYAN_WOOL;
		Material FlagMaterialLayer1Block2 = Material.CYAN_WOOL;
		Material FlagMaterialLayer1Block3 = Material.CYAN_WOOL;
		Material FlagMaterialLayer1Block4 = Material.CYAN_WOOL;
		Material FlagMaterialLayer2Block1 = Material.LIGHT_BLUE_WOOL;
		Material FlagMaterialLayer2Block2 = Material.LIGHT_BLUE_WOOL;
		Material FlagMaterialLayer2Block3 = Material.LIGHT_BLUE_WOOL;
		Material FlagMaterialLayer2Block4 = Material.LIGHT_BLUE_WOOL;
		Block flagLayer1Block1 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 131, 79).getBlock();
		Block flagLayer1Block2 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 131, 78).getBlock();
		Block flagLayer1Block3 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 131, 77).getBlock();
		Block flagLayer1Block4 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 131, 76).getBlock();
		Block flagLayer2Block1 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 130, 79).getBlock();
		Block flagLayer2Block2 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 130, 78).getBlock();
		Block flagLayer2Block3 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 130, 77).getBlock();
		Block flagLayer2Block4 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 130, 76).getBlock();
		Block flagb1 = new Location(plugin.getServer().getWorld("Thunderstone"), -171, 204, 103).getBlock();
		Block flagb2 = new Location(plugin.getServer().getWorld("Thunderstone"), -174, 204, 156).getBlock();

		Block flagLayer1Block1a = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 129, 79).getBlock();
		Material Air = Material.AIR;
		Block flagLayer1Block2a = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 129, 78).getBlock();

		Block flagLayer1Block3a = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 129, 77).getBlock();

		Block flagLayer1Block4a = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 129, 76).getBlock();

		TS_FlagMethod.CaptureOwnTeamFlag(p, flag, number, nextnumber, team, FlagMaterialLayer1Block1, flagLayer1Block1, FlagMaterialLayer1Block2, flagLayer1Block2, FlagMaterialLayer1Block3, flagLayer1Block3, FlagMaterialLayer1Block4, flagLayer1Block4, FlagMaterialLayer2Block1, flagLayer2Block1, FlagMaterialLayer2Block2, flagLayer2Block2, FlagMaterialLayer2Block3, flagLayer2Block3, FlagMaterialLayer2Block4, flagLayer2Block4, flagLayer1Block1a, flagLayer1Block2a, flagLayer1Block3a, flagLayer1Block4a , Air, flagb1, flagb2, woolmapA, woolmapB);
	}
	
	
	public static void CapturingProcessA7(Player e) {

		Player p = e.getPlayer();

		String flag = "shiftedtower";

		int number = 9;
		int nextnumber = 10;

		int team = 1;
		//int EnemyTeam = 2;

		Material woolmapB = Material.LIGHT_BLUE_WOOL;
		Material woolmapA = Material.LIGHT_BLUE_WOOL;
		Material FlagMaterialLayer1Block1 = Material.CYAN_WOOL;
		Material FlagMaterialLayer1Block2 = Material.CYAN_WOOL;
		Material FlagMaterialLayer1Block3 = Material.CYAN_WOOL;
		Material FlagMaterialLayer1Block4 = Material.CYAN_WOOL;
		Material FlagMaterialLayer2Block1 = Material.LIGHT_BLUE_WOOL;
		Material FlagMaterialLayer2Block2 = Material.LIGHT_BLUE_WOOL;
		Material FlagMaterialLayer2Block3 = Material.LIGHT_BLUE_WOOL;
		Material FlagMaterialLayer2Block4 = Material.LIGHT_BLUE_WOOL;
		Block flagLayer1Block1 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 130, 79).getBlock();
		Block flagLayer1Block2 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 130, 78).getBlock();
		Block flagLayer1Block3 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 130, 77).getBlock();
		Block flagLayer1Block4 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 130, 76).getBlock();
		Block flagLayer2Block1 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 129, 79).getBlock();
		Block flagLayer2Block2 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 129, 78).getBlock();
		Block flagLayer2Block3 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 129, 77).getBlock();
		Block flagLayer2Block4 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 129, 76).getBlock();
		Block flagb1 = new Location(plugin.getServer().getWorld("Thunderstone"), -171, 204, 103).getBlock();
		Block flagb2 = new Location(plugin.getServer().getWorld("Thunderstone"), -174, 204, 156).getBlock();

		Block flagLayer1Block1a = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 128, 79).getBlock();
		Material Air = Material.AIR;
		Block flagLayer1Block2a = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 128, 78).getBlock();

		Block flagLayer1Block3a = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 128, 77).getBlock();

		Block flagLayer1Block4a = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 128, 76).getBlock();

		TS_FlagMethod.CaptureOwnTeamFlag(p, flag, number, nextnumber, team, FlagMaterialLayer1Block1, flagLayer1Block1, FlagMaterialLayer1Block2, flagLayer1Block2, FlagMaterialLayer1Block3, flagLayer1Block3, FlagMaterialLayer1Block4, flagLayer1Block4, FlagMaterialLayer2Block1, flagLayer2Block1, FlagMaterialLayer2Block2, flagLayer2Block2, FlagMaterialLayer2Block3, flagLayer2Block3, FlagMaterialLayer2Block4, flagLayer2Block4, flagLayer1Block1a, flagLayer1Block2a, flagLayer1Block3a, flagLayer1Block4a , Air, flagb1, flagb2, woolmapA, woolmapB);
	}
	
	
	
	public static void CapturingProcessA8(Player e) {

		Player p = e.getPlayer();

		String flag = "shiftedtower";

		int number = 8;
		int nextnumber = 9;

		int team = 1;
		//int EnemyTeam = 0;

		Material woolmapB = Material.LIGHT_BLUE_WOOL;
		Material woolmapA = Material.LIGHT_BLUE_WOOL;
		Material FlagMaterialLayer1Block1 = Material.CYAN_WOOL;
		Material FlagMaterialLayer1Block2 = Material.CYAN_WOOL;
		Material FlagMaterialLayer1Block3 = Material.CYAN_WOOL;
		Material FlagMaterialLayer1Block4 = Material.CYAN_WOOL;
		Material FlagMaterialLayer2Block1 = Material.LIGHT_BLUE_WOOL;
		Material FlagMaterialLayer2Block2 = Material.LIGHT_BLUE_WOOL;
		Material FlagMaterialLayer2Block3 = Material.LIGHT_BLUE_WOOL;
		Material FlagMaterialLayer2Block4 = Material.LIGHT_BLUE_WOOL;
		Block flagLayer1Block1 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 129, 79).getBlock();
		Block flagLayer1Block2 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 129, 78).getBlock();
		Block flagLayer1Block3 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 129, 77).getBlock();
		Block flagLayer1Block4 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 129, 76).getBlock();
		Block flagLayer2Block1 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 128, 79).getBlock();
		Block flagLayer2Block2 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 128, 78).getBlock();
		Block flagLayer2Block3 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 128, 77).getBlock();
		Block flagLayer2Block4 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 128, 76).getBlock();
		Block flagb1 = new Location(plugin.getServer().getWorld("Thunderstone"), -171, 204, 103).getBlock();
		Block flagb2 = new Location(plugin.getServer().getWorld("Thunderstone"), -174, 204, 156).getBlock();

		Block flagLayer1Block1a = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 130, 79).getBlock();
		Material Air = Material.AIR;
		Block flagLayer1Block2a = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 130, 78).getBlock();

		Block flagLayer1Block3a = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 130, 77).getBlock();

		Block flagLayer1Block4a = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 130, 76).getBlock();
		
		TS_FlagMethod.CaptureNeutralFlag(p, flag, number, nextnumber, team, FlagMaterialLayer1Block1, flagLayer1Block1, FlagMaterialLayer1Block2, flagLayer1Block2, FlagMaterialLayer1Block3, flagLayer1Block3, FlagMaterialLayer1Block4, flagLayer1Block4, FlagMaterialLayer2Block1, flagLayer2Block1, FlagMaterialLayer2Block2, flagLayer2Block2, FlagMaterialLayer2Block3, flagLayer2Block3, FlagMaterialLayer2Block4, flagLayer2Block4, flagLayer1Block1a, flagLayer1Block2a, flagLayer1Block3a, flagLayer1Block4a , Air, flagb1, flagb2, woolmapA, woolmapB);
	}
	
	
	public static void CapturingProcessA9(Player e) {

		Player p = e.getPlayer();

		String flag = "shiftedtower";

		int number = 7;
		int nextnumber = 8;

		int team = 1;
		int EnemyTeam = 2;

		Material woolmapB = Material.LIGHT_GRAY_WOOL;
		Material woolmapA = Material.LIGHT_GRAY_WOOL;
		Material FlagMaterialLayer1Block1 = Material.GRAY_WOOL;
		Material FlagMaterialLayer1Block2 = Material.GRAY_WOOL;
		Material FlagMaterialLayer1Block3 = Material.GRAY_WOOL;
		Material FlagMaterialLayer1Block4 = Material.GRAY_WOOL;
		Material FlagMaterialLayer2Block1 = Material.LIGHT_GRAY_WOOL;
		Material FlagMaterialLayer2Block2 = Material.LIGHT_GRAY_WOOL;
		Material FlagMaterialLayer2Block3 = Material.LIGHT_GRAY_WOOL;
		Material FlagMaterialLayer2Block4 = Material.LIGHT_GRAY_WOOL;
		Block flagLayer1Block1 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 129, 79).getBlock();
		Block flagLayer1Block2 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 129, 78).getBlock();
		Block flagLayer1Block3 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 129, 77).getBlock();
		Block flagLayer1Block4 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 129, 76).getBlock();
		Block flagLayer2Block1 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 128, 79).getBlock();
		Block flagLayer2Block2 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 128, 78).getBlock();
		Block flagLayer2Block3 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 128, 77).getBlock();
		Block flagLayer2Block4 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 128, 76).getBlock();
		Block flagb1 = new Location(plugin.getServer().getWorld("Thunderstone"), -171, 204, 103).getBlock();
		Block flagb2 = new Location(plugin.getServer().getWorld("Thunderstone"), -174, 204, 156).getBlock();

		Block flagLayer1Block1a = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 130, 79).getBlock();
		Material Air = Material.AIR;
		Block flagLayer1Block2a = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 130, 78).getBlock();

		Block flagLayer1Block3a = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 130, 77).getBlock();

		Block flagLayer1Block4a = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 130, 76).getBlock();
		
		TS_FlagMethod.FlagNeutralisation(p, flag, number, nextnumber, EnemyTeam, team, FlagMaterialLayer1Block1, flagLayer1Block1, FlagMaterialLayer1Block2, flagLayer1Block2, FlagMaterialLayer1Block3, flagLayer1Block3, FlagMaterialLayer1Block4, flagLayer1Block4, FlagMaterialLayer2Block1, flagLayer2Block1, FlagMaterialLayer2Block2, flagLayer2Block2, FlagMaterialLayer2Block3, flagLayer2Block3, FlagMaterialLayer2Block4, flagLayer2Block4, flagLayer1Block1a, flagLayer1Block2a, flagLayer1Block3a, flagLayer1Block4a , Air, flagb1, flagb2, woolmapA, woolmapB);
	}
	
	public static void CapturingProcessA10(Player e) {

		Player p = e.getPlayer();

		String flag = "shiftedtower";

		int number = 6;
		int nextnumber = 7;

		int team = 1;
		int EnemyTeam = 2;
		
		Material woolmapB = Material.YELLOW_WOOL;
		Material woolmapA = Material.YELLOW_WOOL;
		Material FlagMaterialLayer1Block1 = Material.ORANGE_WOOL;
		Material FlagMaterialLayer1Block2 = Material.ORANGE_WOOL;
		Material FlagMaterialLayer1Block3 = Material.ORANGE_WOOL;
		Material FlagMaterialLayer1Block4 = Material.ORANGE_WOOL;
		Material FlagMaterialLayer2Block1 = Material.YELLOW_WOOL;
		Material FlagMaterialLayer2Block2 = Material.YELLOW_WOOL;
		Material FlagMaterialLayer2Block3 = Material.YELLOW_WOOL;
		Material FlagMaterialLayer2Block4 = Material.YELLOW_WOOL;
		Block flagLayer1Block1 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 129, 79).getBlock();
		Block flagLayer1Block2 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 129, 78).getBlock();
		Block flagLayer1Block3 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 129, 77).getBlock();
		Block flagLayer1Block4 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 129, 76).getBlock();
		Block flagLayer2Block1 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 128, 79).getBlock();
		Block flagLayer2Block2 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 128, 78).getBlock();
		Block flagLayer2Block3 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 128, 77).getBlock();
		Block flagLayer2Block4 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 128, 76).getBlock();
		Block flagb1 = new Location(plugin.getServer().getWorld("Thunderstone"), -171, 204, 103).getBlock();
		Block flagb2 = new Location(plugin.getServer().getWorld("Thunderstone"), -174, 204, 156).getBlock();

		Block flagLayer1Block1a = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 130, 79).getBlock();
		Material Air = Material.AIR;
		Block flagLayer1Block2a = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 130, 78).getBlock();

		Block flagLayer1Block3a = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 130, 77).getBlock();

		Block flagLayer1Block4a = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 130, 76).getBlock();
		
		TS_FlagMethod.CaptureEnemyTeamFlag(p, flag, number, nextnumber, EnemyTeam, team, FlagMaterialLayer1Block1, flagLayer1Block1, FlagMaterialLayer1Block2, flagLayer1Block2, FlagMaterialLayer1Block3, flagLayer1Block3, FlagMaterialLayer1Block4, flagLayer1Block4, FlagMaterialLayer2Block1, flagLayer2Block1, FlagMaterialLayer2Block2, flagLayer2Block2, FlagMaterialLayer2Block3, flagLayer2Block3, FlagMaterialLayer2Block4, flagLayer2Block4, flagLayer1Block1a, flagLayer1Block2a, flagLayer1Block3a, flagLayer1Block4a , Air, flagb1, flagb2, woolmapA, woolmapB);
	}
	
	public static void CapturingProcessA11(Player e) {

		Player p = e.getPlayer();

		String flag = "shiftedtower";

		int number = 5;
		int nextnumber = 6;

		int team = 1;
		int EnemyTeam = 2;
		
		Material woolmapB = Material.YELLOW_WOOL;
		Material woolmapA = Material.YELLOW_WOOL;
		Material FlagMaterialLayer1Block1 = Material.ORANGE_WOOL;
		Material FlagMaterialLayer1Block2 = Material.ORANGE_WOOL;
		Material FlagMaterialLayer1Block3 = Material.ORANGE_WOOL;
		Material FlagMaterialLayer1Block4 = Material.ORANGE_WOOL;
		Material FlagMaterialLayer2Block1 = Material.YELLOW_WOOL;
		Material FlagMaterialLayer2Block2 = Material.YELLOW_WOOL;
		Material FlagMaterialLayer2Block3 = Material.YELLOW_WOOL;
		Material FlagMaterialLayer2Block4 = Material.YELLOW_WOOL;
		Block flagLayer1Block1 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 130, 79).getBlock();
		Block flagLayer1Block2 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 130, 78).getBlock();
		Block flagLayer1Block3 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 130, 77).getBlock();
		Block flagLayer1Block4 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 130, 76).getBlock();
		Block flagLayer2Block1 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 129, 79).getBlock();
		Block flagLayer2Block2 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 129, 78).getBlock();
		Block flagLayer2Block3 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 129, 77).getBlock();
		Block flagLayer2Block4 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 129, 76).getBlock();
		Block flagb1 = new Location(plugin.getServer().getWorld("Thunderstone"), -171, 204, 103).getBlock();
		Block flagb2 = new Location(plugin.getServer().getWorld("Thunderstone"), -174, 204, 156).getBlock();

		Block flagLayer1Block1a = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 131, 79).getBlock();
		Material Air = Material.AIR;
		Block flagLayer1Block2a = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 131, 78).getBlock();

		Block flagLayer1Block3a = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 131, 77).getBlock();

		Block flagLayer1Block4a = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 131, 76).getBlock();
		
		TS_FlagMethod.CaptureEnemyTeamFlag(p, flag, number, nextnumber, EnemyTeam, team, FlagMaterialLayer1Block1, flagLayer1Block1, FlagMaterialLayer1Block2, flagLayer1Block2, FlagMaterialLayer1Block3, flagLayer1Block3, FlagMaterialLayer1Block4, flagLayer1Block4, FlagMaterialLayer2Block1, flagLayer2Block1, FlagMaterialLayer2Block2, flagLayer2Block2, FlagMaterialLayer2Block3, flagLayer2Block3, FlagMaterialLayer2Block4, flagLayer2Block4, flagLayer1Block1a, flagLayer1Block2a, flagLayer1Block3a, flagLayer1Block4a , Air, flagb1, flagb2, woolmapA, woolmapB);
	}
	
	public static void CapturingProcessA12(Player e) {

		Player p = e.getPlayer();

		String flag = "shiftedtower";

		int number = 4;
		int nextnumber = 5;

		int team = 1;
		int EnemyTeam = 2;
		
		Material woolmapB = Material.YELLOW_WOOL;
		Material woolmapA = Material.YELLOW_WOOL;
		Material FlagMaterialLayer1Block1 = Material.ORANGE_WOOL;
		Material FlagMaterialLayer1Block2 = Material.ORANGE_WOOL;
		Material FlagMaterialLayer1Block3 = Material.ORANGE_WOOL;
		Material FlagMaterialLayer1Block4 = Material.ORANGE_WOOL;
		Material FlagMaterialLayer2Block1 = Material.YELLOW_WOOL;
		Material FlagMaterialLayer2Block2 = Material.YELLOW_WOOL;
		Material FlagMaterialLayer2Block3 = Material.YELLOW_WOOL;
		Material FlagMaterialLayer2Block4 = Material.YELLOW_WOOL;
		Block flagLayer1Block1 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 131, 79).getBlock();
		Block flagLayer1Block2 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 131, 78).getBlock();
		Block flagLayer1Block3 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 131, 77).getBlock();
		Block flagLayer1Block4 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 131, 76).getBlock();
		Block flagLayer2Block1 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 130, 79).getBlock();
		Block flagLayer2Block2 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 130, 78).getBlock();
		Block flagLayer2Block3 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 130, 77).getBlock();
		Block flagLayer2Block4 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 130, 76).getBlock();
		Block flagb1 = new Location(plugin.getServer().getWorld("Thunderstone"), -171, 204, 103).getBlock();
		Block flagb2 = new Location(plugin.getServer().getWorld("Thunderstone"), -174, 204, 156).getBlock();

		Block flagLayer1Block1a = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 132, 79).getBlock();
		Material Air = Material.AIR;
		Block flagLayer1Block2a = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 132, 78).getBlock();

		Block flagLayer1Block3a = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 132, 77).getBlock();

		Block flagLayer1Block4a = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 132, 76).getBlock();
		
		TS_FlagMethod.CaptureEnemyTeamFlag(p, flag, number, nextnumber, EnemyTeam, team, FlagMaterialLayer1Block1, flagLayer1Block1, FlagMaterialLayer1Block2, flagLayer1Block2, FlagMaterialLayer1Block3, flagLayer1Block3, FlagMaterialLayer1Block4, flagLayer1Block4, FlagMaterialLayer2Block1, flagLayer2Block1, FlagMaterialLayer2Block2, flagLayer2Block2, FlagMaterialLayer2Block3, flagLayer2Block3, FlagMaterialLayer2Block4, flagLayer2Block4, flagLayer1Block1a, flagLayer1Block2a, flagLayer1Block3a, flagLayer1Block4a , Air, flagb1, flagb2, woolmapA, woolmapB);
	}
	
	public static void CapturingProcessA13(Player e) {

		Player p = e.getPlayer();

		String flag = "shiftedtower";

		int number = 3;
		int nextnumber = 4;

		int team = 1;
		int EnemyTeam = 2;
		
		Material woolmapB = Material.YELLOW_WOOL;
		Material woolmapA = Material.YELLOW_WOOL;
		Material FlagMaterialLayer1Block1 = Material.ORANGE_WOOL;
		Material FlagMaterialLayer1Block2 = Material.ORANGE_WOOL;
		Material FlagMaterialLayer1Block3 = Material.ORANGE_WOOL;
		Material FlagMaterialLayer1Block4 = Material.ORANGE_WOOL;
		Material FlagMaterialLayer2Block1 = Material.YELLOW_WOOL;
		Material FlagMaterialLayer2Block2 = Material.YELLOW_WOOL;
		Material FlagMaterialLayer2Block3 = Material.YELLOW_WOOL;
		Material FlagMaterialLayer2Block4 = Material.YELLOW_WOOL;
		Block flagLayer1Block1 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 132, 79).getBlock();
		Block flagLayer1Block2 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 132, 78).getBlock();
		Block flagLayer1Block3 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 132, 77).getBlock();
		Block flagLayer1Block4 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 132, 76).getBlock();
		Block flagLayer2Block1 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 131, 79).getBlock();
		Block flagLayer2Block2 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 131, 78).getBlock();
		Block flagLayer2Block3 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 131, 77).getBlock();
		Block flagLayer2Block4 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 131, 76).getBlock();
		Block flagb1 = new Location(plugin.getServer().getWorld("Thunderstone"), -171, 204, 103).getBlock();
		Block flagb2 = new Location(plugin.getServer().getWorld("Thunderstone"), -174, 204, 156).getBlock();

		Block flagLayer1Block1a = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 133, 79).getBlock();
		Material Air = Material.AIR;
		Block flagLayer1Block2a = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 133, 78).getBlock();

		Block flagLayer1Block3a = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 133, 77).getBlock();

		Block flagLayer1Block4a = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 133, 76).getBlock();
		
		TS_FlagMethod.CaptureEnemyTeamFlag(p, flag, number, nextnumber, EnemyTeam, team, FlagMaterialLayer1Block1, flagLayer1Block1, FlagMaterialLayer1Block2, flagLayer1Block2, FlagMaterialLayer1Block3, flagLayer1Block3, FlagMaterialLayer1Block4, flagLayer1Block4, FlagMaterialLayer2Block1, flagLayer2Block1, FlagMaterialLayer2Block2, flagLayer2Block2, FlagMaterialLayer2Block3, flagLayer2Block3, FlagMaterialLayer2Block4, flagLayer2Block4, flagLayer1Block1a, flagLayer1Block2a, flagLayer1Block3a, flagLayer1Block4a , Air, flagb1, flagb2, woolmapA, woolmapB);
	}
	
	
	public static void CapturingProcessA14(Player e) {

		Player p = e.getPlayer();

		String flag = "shiftedtower";

		int number = 2;
		int nextnumber = 3;

		int team = 1;
		int EnemyTeam = 2;
		
		Material woolmapB = Material.YELLOW_WOOL;
		Material woolmapA = Material.YELLOW_WOOL;
		Material FlagMaterialLayer1Block1 = Material.ORANGE_WOOL;
		Material FlagMaterialLayer1Block2 = Material.ORANGE_WOOL;
		Material FlagMaterialLayer1Block3 = Material.ORANGE_WOOL;
		Material FlagMaterialLayer1Block4 = Material.ORANGE_WOOL;
		Material FlagMaterialLayer2Block1 = Material.YELLOW_WOOL;
		Material FlagMaterialLayer2Block2 = Material.YELLOW_WOOL;
		Material FlagMaterialLayer2Block3 = Material.YELLOW_WOOL;
		Material FlagMaterialLayer2Block4 = Material.YELLOW_WOOL;
		Block flagLayer1Block1 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 133, 79).getBlock();
		Block flagLayer1Block2 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 133, 78).getBlock();
		Block flagLayer1Block3 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 133, 77).getBlock();
		Block flagLayer1Block4 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 133, 76).getBlock();
		Block flagLayer2Block1 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 132, 79).getBlock();
		Block flagLayer2Block2 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 132, 78).getBlock();
		Block flagLayer2Block3 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 132, 77).getBlock();
		Block flagLayer2Block4 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 132, 76).getBlock();
		Block flagb1 = new Location(plugin.getServer().getWorld("Thunderstone"), -171, 204, 103).getBlock();
		Block flagb2 = new Location(plugin.getServer().getWorld("Thunderstone"), -174, 204, 156).getBlock();

		Block flagLayer1Block1a = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 134, 79).getBlock();
		Material Air = Material.AIR;
		Block flagLayer1Block2a = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 134, 78).getBlock();

		Block flagLayer1Block3a = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 134, 77).getBlock();

		Block flagLayer1Block4a = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 134, 76).getBlock();
		
		TS_FlagMethod.CaptureEnemyTeamFlag(p, flag, number, nextnumber, EnemyTeam, team, FlagMaterialLayer1Block1, flagLayer1Block1, FlagMaterialLayer1Block2, flagLayer1Block2, FlagMaterialLayer1Block3, flagLayer1Block3, FlagMaterialLayer1Block4, flagLayer1Block4, FlagMaterialLayer2Block1, flagLayer2Block1, FlagMaterialLayer2Block2, flagLayer2Block2, FlagMaterialLayer2Block3, flagLayer2Block3, FlagMaterialLayer2Block4, flagLayer2Block4, flagLayer1Block1a, flagLayer1Block2a, flagLayer1Block3a, flagLayer1Block4a , Air, flagb1, flagb2, woolmapA, woolmapB);
	}
	
	
	public static void CapturingProcessA15(Player e) {

		Player p = e.getPlayer();

		String flag = "shiftedtower";

		int number = 1;
		int nextnumber = 2;

		int team = 1;
		int EnemyTeam = 2;
		
		Material woolmapB = Material.YELLOW_WOOL;
		Material woolmapA = Material.YELLOW_WOOL;
		Material FlagMaterialLayer1Block1 = Material.ORANGE_WOOL;
		Material FlagMaterialLayer1Block2 = Material.ORANGE_WOOL;
		Material FlagMaterialLayer1Block3 = Material.ORANGE_WOOL;
		Material FlagMaterialLayer1Block4 = Material.ORANGE_WOOL;
		Material FlagMaterialLayer2Block1 = Material.YELLOW_WOOL;
		Material FlagMaterialLayer2Block2 = Material.YELLOW_WOOL;
		Material FlagMaterialLayer2Block3 = Material.YELLOW_WOOL;
		Material FlagMaterialLayer2Block4 = Material.YELLOW_WOOL;
		Block flagLayer1Block1 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 134, 79).getBlock();
		Block flagLayer1Block2 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 134, 78).getBlock();
		Block flagLayer1Block3 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 134, 77).getBlock();
		Block flagLayer1Block4 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 134, 76).getBlock();
		Block flagLayer2Block1 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 133, 79).getBlock();
		Block flagLayer2Block2 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 133, 78).getBlock();
		Block flagLayer2Block3 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 133, 77).getBlock();
		Block flagLayer2Block4 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 133, 76).getBlock();
		Block flagb1 = new Location(plugin.getServer().getWorld("Thunderstone"), -171, 204, 103).getBlock();
		Block flagb2 = new Location(plugin.getServer().getWorld("Thunderstone"), -174, 204, 156).getBlock();

		Block flagLayer1Block1a = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 135, 79).getBlock();
		Material Air = Material.AIR;
		Block flagLayer1Block2a = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 135, 78).getBlock();

		Block flagLayer1Block3a = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 135, 77).getBlock();

		Block flagLayer1Block4a = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 135, 76).getBlock();
		
		TS_FlagMethod.CaptureEnemyTeamFlag(p, flag, number, nextnumber, EnemyTeam, team, FlagMaterialLayer1Block1, flagLayer1Block1, FlagMaterialLayer1Block2, flagLayer1Block2, FlagMaterialLayer1Block3, flagLayer1Block3, FlagMaterialLayer1Block4, flagLayer1Block4, FlagMaterialLayer2Block1, flagLayer2Block1, FlagMaterialLayer2Block2, flagLayer2Block2, FlagMaterialLayer2Block3, flagLayer2Block3, FlagMaterialLayer2Block4, flagLayer2Block4, flagLayer1Block1a, flagLayer1Block2a, flagLayer1Block3a, flagLayer1Block4a , Air, flagb1, flagb2, woolmapA, woolmapB);
	}
	
	public static void CapturingProcessA16(Player e) {

		Player p = e.getPlayer();

		String flag = "shiftedtower";

		int number = 0;
		int nextnumber = 1;

		int team = 1;
		int EnemyTeam = 2;
		
		Material woolmapB = Material.YELLOW_WOOL;
		Material woolmapA = Material.YELLOW_WOOL;
		Material FlagMaterialLayer1Block1 = Material.ORANGE_WOOL;
		Material FlagMaterialLayer1Block2 = Material.ORANGE_WOOL;
		Material FlagMaterialLayer1Block3 = Material.ORANGE_WOOL;
		Material FlagMaterialLayer1Block4 = Material.ORANGE_WOOL;
		Material FlagMaterialLayer2Block1 = Material.YELLOW_WOOL;
		Material FlagMaterialLayer2Block2 = Material.YELLOW_WOOL;
		Material FlagMaterialLayer2Block3 = Material.YELLOW_WOOL;
		Material FlagMaterialLayer2Block4 = Material.YELLOW_WOOL;
		Block flagLayer1Block1 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 135, 79).getBlock();
		Block flagLayer1Block2 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 135, 78).getBlock();
		Block flagLayer1Block3 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 135, 77).getBlock();
		Block flagLayer1Block4 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 135, 76).getBlock();
		Block flagLayer2Block1 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 134, 79).getBlock();
		Block flagLayer2Block2 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 134, 78).getBlock();
		Block flagLayer2Block3 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 134, 77).getBlock();
		Block flagLayer2Block4 = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 134, 76).getBlock();
		Block flagb1 = new Location(plugin.getServer().getWorld("Thunderstone"), -171, 204, 103).getBlock();
		Block flagb2 = new Location(plugin.getServer().getWorld("Thunderstone"), -174, 204, 156).getBlock();

		Block flagLayer1Block1a = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 136, 79).getBlock();
		Material Air = Material.AIR;
		Block flagLayer1Block2a = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 136, 78).getBlock();

		Block flagLayer1Block3a = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 136, 77).getBlock();

		Block flagLayer1Block4a = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 136, 76).getBlock();
		
		TS_FlagMethod.CaptureEnemyTeamFlag(p, flag, number, nextnumber, EnemyTeam, team, FlagMaterialLayer1Block1, flagLayer1Block1, FlagMaterialLayer1Block2, flagLayer1Block2, FlagMaterialLayer1Block3, flagLayer1Block3, FlagMaterialLayer1Block4, flagLayer1Block4, FlagMaterialLayer2Block1, flagLayer2Block1, FlagMaterialLayer2Block2, flagLayer2Block2, FlagMaterialLayer2Block3, flagLayer2Block3, FlagMaterialLayer2Block4, flagLayer2Block4, flagLayer1Block1a, flagLayer1Block2a, flagLayer1Block3a, flagLayer1Block4a , Air, flagb1, flagb2, woolmapA, woolmapB);
	}
}
