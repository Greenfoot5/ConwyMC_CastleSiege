package me.huntifi.castlesiege.Helmsdeep.flags;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.huntifi.castlesiege.Helmsdeep.flags.TeamBonusses.Message_Caves;
import me.huntifi.castlesiege.Helmsdeep.flags.TeamBonusses.Message_Courtyard;
import me.huntifi.castlesiege.Helmsdeep.flags.TeamBonusses.Message_GreatHalls;
import me.huntifi.castlesiege.Helmsdeep.flags.TeamBonusses.Message_Horn;
import me.huntifi.castlesiege.Helmsdeep.flags.TeamBonusses.Message_MainGate;
import me.huntifi.castlesiege.teams.PlayerTeam;

public class FlagMethod {

	static Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("ConwyCastleSiege");

	public static final void CaptureEnemyTeamFlag(Player p, String flag, int number, int nextnumber, int flagTeam, int playerTeam, Material FlagMaterialLayer1Block1, Block flagLayer1Block1, Material FlagMaterialLayer1Block2, Block flagLayer1Block2, Material FlagMaterialLayer1Block3, Block flagLayer1Block3, Material FlagMaterialLayer1Block4, Block flagLayer1Block4, Material FlagMaterialLayer2Block1, Block flagLayer2Block1, Material FlagMaterialLayer2Block2, Block flagLayer2Block2, Material FlagMaterialLayer2Block3, Block flagLayer2Block3, Material FlagMaterialLayer2Block4, Block flagLayer2Block4, Block flagLayer1Block1a, Block flagLayer1Block2a, Block flagLayer1Block3a, Block flagLayer1Block4a , Material Air,  Block flagb1, Block flagb2, Material woolmapA, Material woolmapB) {

		if(FlagTeam.isFlagTeam(flag, flagTeam)) {

			new BukkitRunnable() {

				@Override
				public void run() {

					if(FlagCounter.isFlagCounter(flag, number)) {
						if(PlayerTeam.playerIsInTeam(p,  playerTeam)) {

							FlagCounter.setFlagCounter(flag, nextnumber);

							if (FlagRadius.isPlayerInRadius(p, flag)) {
								
								if (flag.equalsIgnoreCase("GreatHalls")) {  Message_GreatHalls.CapturesMessages(p, flag); }
								if (flag.equalsIgnoreCase("Caves")) {  Message_Caves.CapturesMessages(p, flag); }
								if (flag.equalsIgnoreCase("Courtyard")) {  Message_Courtyard.CapturesMessages(p, flag); }
								if (flag.equalsIgnoreCase("MainGate")) {  Message_MainGate.CapturesMessages(p, flag); }
								if (flag.equalsIgnoreCase("Horn")) {  Message_Horn.CapturesMessages(p, flag); }
								if (flag.equalsIgnoreCase("SupplyCamp")) {  FlagCaptures.CapturesMessages(p, flag); }

							}
							
							
							flagb1.setType(woolmapA);
							flagb2.setType(woolmapB);
							flagLayer1Block1.setType(FlagMaterialLayer1Block1);
							flagLayer1Block2.setType(FlagMaterialLayer1Block2);
							flagLayer1Block3.setType(FlagMaterialLayer1Block3);
							flagLayer1Block4.setType(FlagMaterialLayer1Block4);
							flagLayer2Block1.setType(FlagMaterialLayer2Block1);
							flagLayer2Block2.setType(FlagMaterialLayer2Block2);
							flagLayer2Block3.setType(FlagMaterialLayer2Block3);
							flagLayer2Block4.setType(FlagMaterialLayer2Block4);

							flagLayer1Block1a.setType(Air);
							flagLayer1Block2a.setType(Air);
							flagLayer1Block3a.setType(Air);
							flagLayer1Block4a.setType(Air);

							this.isCancelled();
						}
					}
				}	
			}.runTaskTimer(plugin, 60, 9999999);
		} 
	}




	public static final void CaptureOwnTeamFlag(Player p, String flag, int number, int nextnumber, int flagTeam, Material FlagMaterialLayer1Block1, Block flagLayer1Block1, Material FlagMaterialLayer1Block2, Block flagLayer1Block2, Material FlagMaterialLayer1Block3, Block flagLayer1Block3, Material FlagMaterialLayer1Block4, Block flagLayer1Block4, Material FlagMaterialLayer2Block1, Block flagLayer2Block1, Material FlagMaterialLayer2Block2, Block flagLayer2Block2, Material FlagMaterialLayer2Block3, Block flagLayer2Block3, Material FlagMaterialLayer2Block4, Block flagLayer2Block4, Block flagLayer1Block1a, Block flagLayer1Block2a, Block flagLayer1Block3a, Block flagLayer1Block4a , Material Air,  Block flagb1, Block flagb2, Material woolmapA, Material woolmapB) {

		if(FlagTeam.isFlagTeam(flag, flagTeam)) {

			new BukkitRunnable() {

				@Override
				public void run() {
					if(FlagCounter.isFlagCounter(flag, number)) {
						if(PlayerTeam.playerIsInTeam(p,  flagTeam)) {

							FlagCounter.setFlagCounter(flag, nextnumber);

							if (FlagRadius.isPlayerInRadius(p, flag)) {
								if (flag.equalsIgnoreCase("GreatHalls")) {  Message_GreatHalls.CapturesMessages(p, flag); }
								if (flag.equalsIgnoreCase("Caves")) {  Message_Caves.CapturesMessages(p, flag); }
								if (flag.equalsIgnoreCase("Courtyard")) {  Message_Courtyard.CapturesMessages(p, flag); }
								if (flag.equalsIgnoreCase("MainGate")) {  Message_MainGate.CapturesMessages(p, flag); }
								if (flag.equalsIgnoreCase("Horn")) {  Message_Horn.CapturesMessages(p, flag); }
								if (flag.equalsIgnoreCase("SupplyCamp")) {  FlagCaptures.CapturesMessages(p, flag); }
							}
							
							flagb1.setType(woolmapA);
							flagb2.setType(woolmapB);
							flagLayer1Block1.setType(FlagMaterialLayer1Block1);
							flagLayer1Block2.setType(FlagMaterialLayer1Block2);
							flagLayer1Block3.setType(FlagMaterialLayer1Block3);
							flagLayer1Block4.setType(FlagMaterialLayer1Block4);
							flagLayer2Block1.setType(FlagMaterialLayer2Block1);
							flagLayer2Block2.setType(FlagMaterialLayer2Block2);
							flagLayer2Block3.setType(FlagMaterialLayer2Block3);
							flagLayer2Block4.setType(FlagMaterialLayer2Block4);

							flagLayer1Block1a.setType(Air);
							flagLayer1Block2a.setType(Air);
							flagLayer1Block3a.setType(Air);
							flagLayer1Block4a.setType(Air);

							this.isCancelled();
						}
					}
				}	
			}.runTaskTimer(plugin, 60, 9999999);
		} 
	}


	public static final void FullyCaptureOwnTeamFlag(Player p, String flag, int number, int nextnumber, int flagTeam, Material FlagMaterialLayer1Block1, Block flagLayer1Block1, Material FlagMaterialLayer1Block2, Block flagLayer1Block2, Material FlagMaterialLayer1Block3, Block flagLayer1Block3, Material FlagMaterialLayer1Block4, Block flagLayer1Block4, Material FlagMaterialLayer2Block1, Block flagLayer2Block1, Material FlagMaterialLayer2Block2, Block flagLayer2Block2, Material FlagMaterialLayer2Block3, Block flagLayer2Block3, Material FlagMaterialLayer2Block4, Block flagLayer2Block4, Block flagLayer1Block1a, Block flagLayer1Block2a, Block flagLayer1Block3a, Block flagLayer1Block4a , Material Air,  Block flagb1, Block flagb2, Material woolmapA, Material woolmapB) {

		if(FlagTeam.isFlagTeam(flag, flagTeam)) {

			new BukkitRunnable() {

				@Override
				public void run() {
					if(FlagCounter.isFlagCounter(flag, number)) {
						if(PlayerTeam.playerIsInTeam(p,  flagTeam)) {

							FlagCounter.setFlagCounter(flag, nextnumber);

							if (FlagRadius.isPlayerInRadius(p, flag)) {
								FlagCaptures.CapturesMessagesFinal(p, flag);
								
								if (flag.equalsIgnoreCase("GreatHalls")) {  Message_GreatHalls.CapturesMessagesFinal(p, flag); }
								if (flag.equalsIgnoreCase("Caves")) {  Message_Caves.CapturesMessagesFinal(p, flag); }
								if (flag.equalsIgnoreCase("Courtyard")) {  Message_Courtyard.CapturesMessagesFinal(p, flag); }
								if (flag.equalsIgnoreCase("MainGate")) {  Message_MainGate.CapturesMessagesFinal(p, flag); }
								if (flag.equalsIgnoreCase("Horn")) {  Message_Horn.CapturesMessagesFinal(p, flag); }
								if (flag.equalsIgnoreCase("SupplyCamp")) {  FlagCaptures.CapturesMessagesFinal(p, flag); }
							}
							
							
							
							flagb1.setType(woolmapA);
							flagb2.setType(woolmapB);
							flagLayer1Block1.setType(FlagMaterialLayer1Block1);
							flagLayer1Block2.setType(FlagMaterialLayer1Block2);
							flagLayer1Block3.setType(FlagMaterialLayer1Block3);
							flagLayer1Block4.setType(FlagMaterialLayer1Block4);
							flagLayer2Block1.setType(FlagMaterialLayer2Block1);
							flagLayer2Block2.setType(FlagMaterialLayer2Block2);
							flagLayer2Block3.setType(FlagMaterialLayer2Block3);
							flagLayer2Block4.setType(FlagMaterialLayer2Block4);

							flagLayer1Block1a.setType(Air);
							flagLayer1Block2a.setType(Air);
							flagLayer1Block3a.setType(Air);
							flagLayer1Block4a.setType(Air);

							this.isCancelled();
						}
					}
				}	
			}.runTaskTimer(plugin, 60, 9999999);
		} 
	}



	public static final void FlagNeutralisation(Player p, String flag, int number, int nextnumber, int flagTeam, int playerTeam, Material FlagMaterialLayer1Block1, Block flagLayer1Block1, Material FlagMaterialLayer1Block2, Block flagLayer1Block2, Material FlagMaterialLayer1Block3, Block flagLayer1Block3, Material FlagMaterialLayer1Block4, Block flagLayer1Block4, Material FlagMaterialLayer2Block1, Block flagLayer2Block1, Material FlagMaterialLayer2Block2, Block flagLayer2Block2, Material FlagMaterialLayer2Block3, Block flagLayer2Block3, Material FlagMaterialLayer2Block4, Block flagLayer2Block4, Block flagLayer1Block1a, Block flagLayer1Block2a, Block flagLayer1Block3a, Block flagLayer1Block4a , Material Air,  Block flagb1, Block flagb2, Material woolmapA, Material woolmapB) {

		if(FlagTeam.isFlagTeam(flag, flagTeam)) {

			new BukkitRunnable() {

				@Override
				public void run() {
					if(FlagCounter.isFlagCounter(flag, number)) {
						if(PlayerTeam.playerIsInTeam(p,  playerTeam)) {

							FlagCounter.setFlagCounter(flag, nextnumber);
							FlagTeam.setFlagTeam(flag, 0);
							FlagMessages.onNeutralisation(flag, 0);

							if (FlagRadius.isPlayerInRadius(p, flag)) {
								if (flag.equalsIgnoreCase("GreatHalls")) {  Message_GreatHalls.CapturesMessages(p, flag); }
								if (flag.equalsIgnoreCase("Caves")) {  Message_Caves.CapturesMessages(p, flag); }
								if (flag.equalsIgnoreCase("Courtyard")) {  Message_Courtyard.CapturesMessages(p, flag); }
								if (flag.equalsIgnoreCase("MainGate")) {  Message_MainGate.CapturesMessages(p, flag); }
								if (flag.equalsIgnoreCase("Horn")) {  Message_Horn.CapturesMessages(p, flag); }
								if (flag.equalsIgnoreCase("SupplyCamp")) {  FlagCaptures.CapturesMessages(p, flag); }
							}
							
							flagb1.setType(woolmapA);
							flagb2.setType(woolmapB);
							flagLayer1Block1.setType(FlagMaterialLayer1Block1);
							flagLayer1Block2.setType(FlagMaterialLayer1Block2);
							flagLayer1Block3.setType(FlagMaterialLayer1Block3);
							flagLayer1Block4.setType(FlagMaterialLayer1Block4);
							flagLayer2Block1.setType(FlagMaterialLayer2Block1);
							flagLayer2Block2.setType(FlagMaterialLayer2Block2);
							flagLayer2Block3.setType(FlagMaterialLayer2Block3);
							flagLayer2Block4.setType(FlagMaterialLayer2Block4);

							flagLayer1Block1a.setType(Air);
							flagLayer1Block2a.setType(Air);
							flagLayer1Block3a.setType(Air);
							flagLayer1Block4a.setType(Air);

							this.isCancelled();
						}
					}
				}	
			}.runTaskTimer(plugin, 60, 9999999);
		} 
	}


	public static final void CaptureNeutralFlag(Player p, String flag, int number, int nextnumber, int playerTeam, Material FlagMaterialLayer1Block1, Block flagLayer1Block1, Material FlagMaterialLayer1Block2, Block flagLayer1Block2, Material FlagMaterialLayer1Block3, Block flagLayer1Block3, Material FlagMaterialLayer1Block4, Block flagLayer1Block4, Material FlagMaterialLayer2Block1, Block flagLayer2Block1, Material FlagMaterialLayer2Block2, Block flagLayer2Block2, Material FlagMaterialLayer2Block3, Block flagLayer2Block3, Material FlagMaterialLayer2Block4, Block flagLayer2Block4, Block flagLayer1Block1a, Block flagLayer1Block2a, Block flagLayer1Block3a, Block flagLayer1Block4a , Material Air,  Block flagb1, Block flagb2, Material woolmapA, Material woolmapB) {

		if(FlagTeam.isFlagTeam(flag, 0)) {

			new BukkitRunnable() {

				@Override
				public void run() {
					if(FlagCounter.isFlagCounter(flag, number)) {
						if(PlayerTeam.playerIsInTeam(p,  playerTeam)) {

							FlagCounter.setFlagCounter(flag, nextnumber);
							FlagTeam.setFlagTeam(flag, playerTeam);
							FlagMessages.onCaptured(flag, playerTeam);

							if (FlagRadius.isPlayerInRadius(p, flag)) {
								if (flag.equalsIgnoreCase("GreatHalls")) {  Message_GreatHalls.CapturesMessages(p, flag); }
								if (flag.equalsIgnoreCase("Caves")) {  Message_Caves.CapturesMessages(p, flag); }
								if (flag.equalsIgnoreCase("Courtyard")) {  Message_Courtyard.CapturesMessages(p, flag); }
								if (flag.equalsIgnoreCase("MainGate")) {  Message_MainGate.CapturesMessages(p, flag); }
								if (flag.equalsIgnoreCase("Horn")) {  Message_Horn.CapturesMessages(p, flag); }
								if (flag.equalsIgnoreCase("SupplyCamp")) {  FlagCaptures.CapturesMessages(p, flag); }
							}

							flagb1.setType(woolmapA);
							flagb2.setType(woolmapB);
							flagLayer1Block1.setType(FlagMaterialLayer1Block1);
							flagLayer1Block2.setType(FlagMaterialLayer1Block2);
							flagLayer1Block3.setType(FlagMaterialLayer1Block3);
							flagLayer1Block4.setType(FlagMaterialLayer1Block4);
							flagLayer2Block1.setType(FlagMaterialLayer2Block1);
							flagLayer2Block2.setType(FlagMaterialLayer2Block2);
							flagLayer2Block3.setType(FlagMaterialLayer2Block3);
							flagLayer2Block4.setType(FlagMaterialLayer2Block4);

							flagLayer1Block1a.setType(Air);
							flagLayer1Block2a.setType(Air);
							flagLayer1Block3a.setType(Air);
							flagLayer1Block4a.setType(Air);

							this.isCancelled();
						}
					}
				}	
			}.runTaskTimer(plugin, 60, 9999999);
		} 
	}
}
