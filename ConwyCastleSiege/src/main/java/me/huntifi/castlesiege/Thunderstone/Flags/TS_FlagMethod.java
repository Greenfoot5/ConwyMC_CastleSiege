package me.huntifi.castlesiege.Thunderstone.Flags;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.huntifi.castlesiege.Thunderstone.TeamBonusses.Messages_Easttower;
import me.huntifi.castlesiege.Thunderstone.TeamBonusses.Messages_Lonelytower;
import me.huntifi.castlesiege.Thunderstone.TeamBonusses.Messages_Shiftedtower;
import me.huntifi.castlesiege.Thunderstone.TeamBonusses.Messages_Skyviewtower;
import me.huntifi.castlesiege.Thunderstone.TeamBonusses.Messages_Twinbridge;
import me.huntifi.castlesiege.Thunderstone.TeamBonusses.Messages_Westtower;
//import me.huntifi.castlesiege.teams.PlayerTeam;

public class TS_FlagMethod {
	
	static Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("CastleSiege");

	public static final void CaptureEnemyTeamFlag(Player p, String flag, int number, int nextnumber, int flagTeam, int playerTeam, Material FlagMaterialLayer1Block1, Block flagLayer1Block1, Material FlagMaterialLayer1Block2, Block flagLayer1Block2, Material FlagMaterialLayer1Block3, Block flagLayer1Block3, Material FlagMaterialLayer1Block4, Block flagLayer1Block4, Material FlagMaterialLayer2Block1, Block flagLayer2Block1, Material FlagMaterialLayer2Block2, Block flagLayer2Block2, Material FlagMaterialLayer2Block3, Block flagLayer2Block3, Material FlagMaterialLayer2Block4, Block flagLayer2Block4, Block flagLayer1Block1a, Block flagLayer1Block2a, Block flagLayer1Block3a, Block flagLayer1Block4a , Material Air,  Block flagb1, Block flagb2, Material woolmapA, Material woolmapB) {

		if(TS_FlagTeam.isFlagTeam(flag, flagTeam)) {

			new BukkitRunnable() {

				@Override
				public void run() {

					if(TS_FlagCounter.isFlagCounter(flag, number)) {
						//if(PlayerTeam.playerIsInTeam(p,  playerTeam)) {

							TS_FlagCounter.setFlagCounter(flag, nextnumber);

							if (TS_FlagRadius.isPlayerInRadius(p, flag)) {
					
								if (flag.equalsIgnoreCase("skyviewtower")) {  Messages_Skyviewtower.CapturesMessages(p, flag); }
								if (flag.equalsIgnoreCase("easttower")) {  Messages_Easttower.CapturesMessages(p, flag); }
								if (flag.equalsIgnoreCase("westtower")) {  Messages_Westtower.CapturesMessages(p, flag); }
								if (flag.equalsIgnoreCase("shiftedtower")) {  Messages_Shiftedtower.CapturesMessages(p, flag); }
								if (flag.equalsIgnoreCase("twinbridge")) {  Messages_Twinbridge.CapturesMessages(p, flag); }
								if (flag.equalsIgnoreCase("lonelytower")) {  Messages_Lonelytower.CapturesMessages(p, flag); }
								if (flag.equalsIgnoreCase("stairhall")) {  TS_FlagCaptures.CapturesMessages(p, flag); }
								
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
						//}
					}
				}	
			}.runTaskTimer(plugin, 60, 9999999);
		} 
	}




	public static final void CaptureOwnTeamFlag(Player p, String flag, int number, int nextnumber, int flagTeam, Material FlagMaterialLayer1Block1, Block flagLayer1Block1, Material FlagMaterialLayer1Block2, Block flagLayer1Block2, Material FlagMaterialLayer1Block3, Block flagLayer1Block3, Material FlagMaterialLayer1Block4, Block flagLayer1Block4, Material FlagMaterialLayer2Block1, Block flagLayer2Block1, Material FlagMaterialLayer2Block2, Block flagLayer2Block2, Material FlagMaterialLayer2Block3, Block flagLayer2Block3, Material FlagMaterialLayer2Block4, Block flagLayer2Block4, Block flagLayer1Block1a, Block flagLayer1Block2a, Block flagLayer1Block3a, Block flagLayer1Block4a , Material Air,  Block flagb1, Block flagb2, Material woolmapA, Material woolmapB) {

		if(TS_FlagTeam.isFlagTeam(flag, flagTeam)) {

			new BukkitRunnable() {

				@Override
				public void run() {
					if(TS_FlagCounter.isFlagCounter(flag, number)) {
						//if(PlayerTeam.playerIsInTeam(p,  flagTeam)) {

							TS_FlagCounter.setFlagCounter(flag, nextnumber);

							if (TS_FlagRadius.isPlayerInRadius(p, flag)) {
								
								if (flag.equalsIgnoreCase("skyviewtower")) {  Messages_Skyviewtower.CapturesMessages(p, flag); }
								if (flag.equalsIgnoreCase("easttower")) {  Messages_Easttower.CapturesMessages(p, flag); }
								if (flag.equalsIgnoreCase("westtower")) {  Messages_Westtower.CapturesMessages(p, flag); }
								if (flag.equalsIgnoreCase("shiftedtower")) {  Messages_Shiftedtower.CapturesMessages(p, flag); }
								if (flag.equalsIgnoreCase("twinbridge")) {  Messages_Twinbridge.CapturesMessages(p, flag); }
								if (flag.equalsIgnoreCase("lonelytower")) {  Messages_Lonelytower.CapturesMessages(p, flag); }
								if (flag.equalsIgnoreCase("stairhall")) {  TS_FlagCaptures.CapturesMessages(p, flag); }
								
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
						//}
					}
				}	
			}.runTaskTimer(plugin, 60, 9999999);
		} 
	}


	public static final void FullyCaptureOwnTeamFlag(Player p, String flag, int number, int nextnumber, int flagTeam, Material FlagMaterialLayer1Block1, Block flagLayer1Block1, Material FlagMaterialLayer1Block2, Block flagLayer1Block2, Material FlagMaterialLayer1Block3, Block flagLayer1Block3, Material FlagMaterialLayer1Block4, Block flagLayer1Block4, Material FlagMaterialLayer2Block1, Block flagLayer2Block1, Material FlagMaterialLayer2Block2, Block flagLayer2Block2, Material FlagMaterialLayer2Block3, Block flagLayer2Block3, Material FlagMaterialLayer2Block4, Block flagLayer2Block4, Block flagLayer1Block1a, Block flagLayer1Block2a, Block flagLayer1Block3a, Block flagLayer1Block4a , Material Air,  Block flagb1, Block flagb2, Material woolmapA, Material woolmapB) {

		if(TS_FlagTeam.isFlagTeam(flag, flagTeam)) {

			new BukkitRunnable() {

				@Override
				public void run() {
					if(TS_FlagCounter.isFlagCounter(flag, number)) {
						//if(PlayerTeam.playerIsInTeam(p,  flagTeam)) {

							TS_FlagCounter.setFlagCounter(flag, nextnumber);

							if (TS_FlagRadius.isPlayerInRadius(p, flag)) {
								
								if (flag.equalsIgnoreCase("skyviewtower")) {  Messages_Skyviewtower.CapturesMessagesFinal(p, flag); }
								if (flag.equalsIgnoreCase("easttower")) {  Messages_Easttower.CapturesMessagesFinal(p, flag); }
								if (flag.equalsIgnoreCase("westtower")) {  Messages_Westtower.CapturesMessagesFinal(p, flag); }
								if (flag.equalsIgnoreCase("shiftedtower")) {  Messages_Shiftedtower.CapturesMessagesFinal(p, flag); }
								if (flag.equalsIgnoreCase("twinbridge")) {  Messages_Twinbridge.CapturesMessagesFinal(p, flag); }
								if (flag.equalsIgnoreCase("lonelytower")) {  Messages_Lonelytower.CapturesMessagesFinal(p, flag); }
								if (flag.equalsIgnoreCase("stairhall")) {  TS_FlagCaptures.CapturesMessagesFinal(p, flag); }
								
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
						//}
					}
				}	
			}.runTaskTimer(plugin, 60, 9999999);
		} 
	}



	public static final void FlagNeutralisation(Player p, String flag, int number, int nextnumber, int flagTeam, int playerTeam, Material FlagMaterialLayer1Block1, Block flagLayer1Block1, Material FlagMaterialLayer1Block2, Block flagLayer1Block2, Material FlagMaterialLayer1Block3, Block flagLayer1Block3, Material FlagMaterialLayer1Block4, Block flagLayer1Block4, Material FlagMaterialLayer2Block1, Block flagLayer2Block1, Material FlagMaterialLayer2Block2, Block flagLayer2Block2, Material FlagMaterialLayer2Block3, Block flagLayer2Block3, Material FlagMaterialLayer2Block4, Block flagLayer2Block4, Block flagLayer1Block1a, Block flagLayer1Block2a, Block flagLayer1Block3a, Block flagLayer1Block4a , Material Air,  Block flagb1, Block flagb2, Material woolmapA, Material woolmapB) {

		if(TS_FlagTeam.isFlagTeam(flag, flagTeam)) {

			new BukkitRunnable() {

				@Override
				public void run() {
					if(TS_FlagCounter.isFlagCounter(flag, number)) {
						//if(PlayerTeam.playerIsInTeam(p,  playerTeam)) {

							TS_FlagCounter.setFlagCounter(flag, nextnumber);
							TS_FlagTeam.setFlagTeam(flag, 0);
							TS_FlagMessages.onNeutralisation(flag, 0);

							if (TS_FlagRadius.isPlayerInRadius(p, flag)) {
								if (flag.equalsIgnoreCase("skyviewtower")) {  Messages_Skyviewtower.CapturesMessages(p, flag); }
								if (flag.equalsIgnoreCase("easttower")) {  Messages_Easttower.CapturesMessages(p, flag); }
								if (flag.equalsIgnoreCase("westtower")) {  Messages_Westtower.CapturesMessages(p, flag); }
								if (flag.equalsIgnoreCase("shiftedtower")) {  Messages_Shiftedtower.CapturesMessages(p, flag); }
								if (flag.equalsIgnoreCase("twinbridge")) {  Messages_Twinbridge.CapturesMessages(p, flag); }
								if (flag.equalsIgnoreCase("lonelytower")) {  Messages_Lonelytower.CapturesMessages(p, flag); }
								if (flag.equalsIgnoreCase("stairhall")) {  TS_FlagCaptures.CapturesMessages(p, flag); }
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
						//}
					}
				}	
			}.runTaskTimer(plugin, 60, 9999999);
		} 
	}


	public static final void CaptureNeutralFlag(Player p, String flag, int number, int nextnumber, int playerTeam, Material FlagMaterialLayer1Block1, Block flagLayer1Block1, Material FlagMaterialLayer1Block2, Block flagLayer1Block2, Material FlagMaterialLayer1Block3, Block flagLayer1Block3, Material FlagMaterialLayer1Block4, Block flagLayer1Block4, Material FlagMaterialLayer2Block1, Block flagLayer2Block1, Material FlagMaterialLayer2Block2, Block flagLayer2Block2, Material FlagMaterialLayer2Block3, Block flagLayer2Block3, Material FlagMaterialLayer2Block4, Block flagLayer2Block4, Block flagLayer1Block1a, Block flagLayer1Block2a, Block flagLayer1Block3a, Block flagLayer1Block4a , Material Air,  Block flagb1, Block flagb2, Material woolmapA, Material woolmapB) {

		if(TS_FlagTeam.isFlagTeam(flag, 0)) {

			new BukkitRunnable() {

				@Override
				public void run() {
					if(TS_FlagCounter.isFlagCounter(flag, number)) {
						//if(PlayerTeam.playerIsInTeam(p,  playerTeam)) {

							TS_FlagCounter.setFlagCounter(flag, nextnumber);
							TS_FlagTeam.setFlagTeam(flag, playerTeam);
							TS_FlagMessages.onCaptured(flag, playerTeam);

							if (TS_FlagRadius.isPlayerInRadius(p, flag)) {
								if (flag.equalsIgnoreCase("skyviewtower")) {  Messages_Skyviewtower.CapturesMessages(p, flag); }
								if (flag.equalsIgnoreCase("easttower")) {  Messages_Easttower.CapturesMessages(p, flag); }
								if (flag.equalsIgnoreCase("westtower")) {  Messages_Westtower.CapturesMessages(p, flag); }
								if (flag.equalsIgnoreCase("shiftedtower")) {  Messages_Shiftedtower.CapturesMessages(p, flag); }
								if (flag.equalsIgnoreCase("twinbridge")) {  Messages_Twinbridge.CapturesMessages(p, flag); }
								if (flag.equalsIgnoreCase("lonelytower")) {  Messages_Lonelytower.CapturesMessages(p, flag); }
								if (flag.equalsIgnoreCase("stairhall")) {  TS_FlagCaptures.CapturesMessages(p, flag); }
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
						//}
					}
				}	
			}.runTaskTimer(plugin, 60, 9999999);
		} 
	}
}


