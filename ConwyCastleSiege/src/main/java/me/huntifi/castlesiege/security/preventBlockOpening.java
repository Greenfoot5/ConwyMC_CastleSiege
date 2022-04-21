package me.huntifi.castlesiege.security;

import me.huntifi.castlesiege.events.combat.InCombat;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

public class preventBlockOpening implements Listener {

	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("CastleSiege");

	@EventHandler
	public void onInteractX(PlayerInteractEvent event){

		Player p = event.getPlayer();

		if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
			
			Block blk = event.getClickedBlock();

			if (blk.getType() == Material.FLOWER_POT) {

				if (p.getGameMode() != GameMode.CREATIVE) {

					event.setCancelled(true);

				}
			}
		}
	}


	@EventHandler
	public void onInteract(PlayerInteractEvent event){
		Player p = event.getPlayer();
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
			Block block = event.getClickedBlock();
			if(block.getType().equals(Material.CHEST)) {
				if (p.getGameMode() != GameMode.CREATIVE) {
					event.setCancelled(true);
				}
			}

		}
	}

	@EventHandler
	public void onInteract1(PlayerInteractEvent event){

		Player p = event.getPlayer();
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
			Block block = event.getClickedBlock();
			if(block.getType().equals(Material.CRAFTING_TABLE)) {
				if (p.getGameMode() != GameMode.CREATIVE) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onInteract2(PlayerInteractEvent event){
		Player p = event.getPlayer();
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
			Block block = event.getClickedBlock();
			if(block.getType().equals(Material.HOPPER)) {
				if (p.getGameMode() != GameMode.CREATIVE) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onInteract3(PlayerInteractEvent event){
		Player p = event.getPlayer();
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
			Block block = event.getClickedBlock();
			if(block.getType().equals(Material.DISPENSER)) {
				if (p.getGameMode() != GameMode.CREATIVE) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onInteract4(PlayerInteractEvent event){
		Player p = event.getPlayer();
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
			Block block = event.getClickedBlock();
			if(block.getType().equals(Material.DROPPER)) {
				if (p.getGameMode() != GameMode.CREATIVE) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onInteract5(PlayerInteractEvent event){
		Player p = event.getPlayer();
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
			Block block = event.getClickedBlock();
			if(block.getType().equals(Material.ENCHANTING_TABLE)) {
				if (p.getGameMode() != GameMode.CREATIVE) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onInteract6(PlayerInteractEvent event){
		Player p = event.getPlayer();
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
			Block block = event.getClickedBlock();
			if(block.getType().equals(Material.LOOM)) {
				if (p.getGameMode() != GameMode.CREATIVE) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onInteract7(PlayerInteractEvent event){
		Player p = event.getPlayer();
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
			Block block = event.getClickedBlock();
			if(block.getType().equals(Material.BARREL)) {
				if (p.getGameMode() != GameMode.CREATIVE) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onInteract8(PlayerInteractEvent event){
		Player p = event.getPlayer();
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
			Block block = event.getClickedBlock();
			if(block.getType().equals(Material.SMOKER)) {
				if (p.getGameMode() != GameMode.CREATIVE) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onInteract9(PlayerInteractEvent event){
		Player p = event.getPlayer();
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
			Block block = event.getClickedBlock();
			if(block.getType().equals(Material.CARTOGRAPHY_TABLE)) {
				if (p.getGameMode() != GameMode.CREATIVE) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onInteract10(PlayerInteractEvent event){
		Player p = event.getPlayer();
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
			Block block = event.getClickedBlock();
			if(block.getType().equals(Material.FURNACE)) {
				if (p.getGameMode() != GameMode.CREATIVE) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onInteract11(PlayerInteractEvent event){
		Player p = event.getPlayer();
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
			Block block = event.getClickedBlock();
			if(block.getType().equals(Material.BLAST_FURNACE)) {
				if (p.getGameMode() != GameMode.CREATIVE) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onInteract12(PlayerInteractEvent event){
		Player p = event.getPlayer();
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
			Block block = event.getClickedBlock();
			if(block.getType().equals(Material.ANVIL)) {
				if (p.getGameMode() != GameMode.CREATIVE) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onInteract13(PlayerInteractEvent event){
		Player p = event.getPlayer();
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
			Block block = event.getClickedBlock();
			if(block.getType().equals(Material.GRINDSTONE)) {
				if (p.getGameMode() != GameMode.CREATIVE) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onInteract14(PlayerInteractEvent event){
		Player p = event.getPlayer();
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
			Block block = event.getClickedBlock();
			if(block.getType().equals(Material.TRAPPED_CHEST)) {
				if (p.getGameMode() != GameMode.CREATIVE) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onInteract15(PlayerInteractEvent event){
		Player p = event.getPlayer();
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
			Block block = event.getClickedBlock();
			if(block.getType().equals(Material.SMITHING_TABLE)) {
				if (p.getGameMode() != GameMode.CREATIVE) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onInteract16(PlayerInteractEvent event){
		Player p = event.getPlayer();
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
			Block block = event.getClickedBlock();
			if(block.getType().equals(Material.FLETCHING_TABLE)) {
				if (p.getGameMode() != GameMode.CREATIVE) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onInteract17(PlayerInteractEvent event){
		Player p = event.getPlayer();
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
			Block block = event.getClickedBlock();

			if(block.getType().equals(Material.OAK_TRAPDOOR) || block.getType().equals(Material.SPRUCE_TRAPDOOR)) {
				if (!(p.hasPermission("Moderator.blockPlace"))) {

					event.setCancelled(true);

				} else if (p.hasPermission("Moderator.blockPlace") || p.getGameMode() == GameMode.CREATIVE) {

					event.setCancelled(false);

				}

				if (InCombat.isPlayerInLobby(p.getUniqueId())) {

					event.setCancelled(false);
				}
			} 
		}
	}

	@EventHandler
	public void onInteract18(PlayerInteractEvent event){
		Player p = event.getPlayer();
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
			Block block = event.getClickedBlock();
			if(block.getType().equals(Material.BLACK_SHULKER_BOX)) {
				if (p.getGameMode() != GameMode.CREATIVE) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onInteract19(PlayerInteractEvent event){
		Player p = event.getPlayer();
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
			Block block = event.getClickedBlock();
			if(block.getType().equals(Material.BLUE_SHULKER_BOX)) {
				if (p.getGameMode() != GameMode.CREATIVE) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onInteract20(PlayerInteractEvent event){
		Player p = event.getPlayer();
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
			Block block = event.getClickedBlock();
			if(block.getType().equals(Material.BROWN_SHULKER_BOX)) {
				if (p.getGameMode() != GameMode.CREATIVE) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onInteract21(PlayerInteractEvent event){
		Player p = event.getPlayer();
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
			Block block = event.getClickedBlock();
			if(block.getType().equals(Material.YELLOW_SHULKER_BOX)) {
				if (p.getGameMode() != GameMode.CREATIVE) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onInteract22(PlayerInteractEvent event){
		Player p = event.getPlayer();
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
			Block block = event.getClickedBlock();
			if(block.getType().equals(Material.ORANGE_SHULKER_BOX)) {
				if (p.getGameMode() != GameMode.CREATIVE) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onInteract23(PlayerInteractEvent event){
		Player p = event.getPlayer();
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
			Block block = event.getClickedBlock();
			if(block.getType().equals(Material.GREEN_SHULKER_BOX)) {
				if (p.getGameMode() != GameMode.CREATIVE) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onInteract24(PlayerInteractEvent event){
		Player p = event.getPlayer();
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
			Block block = event.getClickedBlock();
			if(block.getType().equals(Material.PINK_SHULKER_BOX)) {
				if (p.getGameMode() != GameMode.CREATIVE) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onInteract25(PlayerInteractEvent event){
		Player p = event.getPlayer();
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
			Block block = event.getClickedBlock();
			if(block.getType().equals(Material.RED_SHULKER_BOX)) {
				if (p.getGameMode() != GameMode.CREATIVE) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onInteract26(PlayerInteractEvent event){
		Player p = event.getPlayer();
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
			Block block = event.getClickedBlock();
			if(block.getType().equals(Material.GRAY_SHULKER_BOX)) {
				if (p.getGameMode() != GameMode.CREATIVE) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onInteract27(PlayerInteractEvent event){
		Player p = event.getPlayer();
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
			Block block = event.getClickedBlock();
			if(block.getType().equals(Material.LIGHT_BLUE_SHULKER_BOX)) {
				if (p.getGameMode() != GameMode.CREATIVE) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onInteract28(PlayerInteractEvent event){
		Player p = event.getPlayer();
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
			Block block = event.getClickedBlock();
			if(block.getType().equals(Material.CYAN_SHULKER_BOX)) {
				if (p.getGameMode() != GameMode.CREATIVE) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onInteract29(PlayerInteractEvent event){
		Player p = event.getPlayer();
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
			Block block = event.getClickedBlock();
			if(block.getType().equals(Material.PURPLE_SHULKER_BOX)) {
				if (p.getGameMode() != GameMode.CREATIVE) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onInteract30(PlayerInteractEvent event){
		Player p = event.getPlayer();
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
			Block block = event.getClickedBlock();
			if(block.getType().equals(Material.CHEST_MINECART)) {
				if (p.getGameMode() != GameMode.CREATIVE) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onInteract31(PlayerInteractEvent event){
		Player p = event.getPlayer();
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
			Block block = event.getClickedBlock();
			if(block.getType().equals(Material.FURNACE_MINECART)) {
				if (p.getGameMode() != GameMode.CREATIVE) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onInteract32(PlayerInteractEvent event){
		Player p = event.getPlayer();
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
			Block block = event.getClickedBlock();
			if(block.getType().equals(Material.BELL)) {
				if (p.getGameMode() != GameMode.CREATIVE) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onInteract33(PlayerInteractEvent event){
		Player p = event.getPlayer();
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
			Block block = event.getClickedBlock();
			if(block.getType().equals(Material.BREWING_STAND)) {
				if (p.getGameMode() != GameMode.CREATIVE) {
					event.setCancelled(true);
				}
			}
		}
	}


	@EventHandler
	public void onInteract34(PlayerInteractEvent event){
		Player p = event.getPlayer();
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
			Block block = event.getClickedBlock();
			if(block.getType().equals(Material.SHULKER_BOX)) {
				if (p.getGameMode() != GameMode.CREATIVE) {
					event.setCancelled(true);
				}
			}
		}
	}


	@EventHandler
	public void onInteract35(PlayerInteractEvent event){
		Player p = event.getPlayer();
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
			Block block = event.getClickedBlock();
			if(block.getType().equals(Material.WHITE_SHULKER_BOX)) {
				if (p.getGameMode() != GameMode.CREATIVE) {
					event.setCancelled(true);
				}
			}
		}
	}


	@EventHandler
	public void onInteract36(PlayerInteractEvent event){
		Player p = event.getPlayer();
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
			Block block = event.getClickedBlock();
			if(block.getType().equals(Material.MAGENTA_SHULKER_BOX)) {
				if (p.getGameMode() != GameMode.CREATIVE) {
					event.setCancelled(true);
				}
			}
		}
	}



	@EventHandler
	public void onInteract37(PlayerInteractEvent event){
		Player p = event.getPlayer();
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
			Block block = event.getClickedBlock();
			if(block.getType().equals(Material.LIME_SHULKER_BOX)) {
				if (p.getGameMode() != GameMode.CREATIVE) {
					event.setCancelled(true);
				}
			}
		}
	}


	@EventHandler
	public void onInteract38(PlayerInteractEvent event){
		Player p = event.getPlayer();
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
			Block block = event.getClickedBlock();
			if(block.getType().equals(Material.LIGHT_GRAY_SHULKER_BOX)) {
				if (p.getGameMode() != GameMode.CREATIVE) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onInteract39(PlayerInteractEvent event){
		Player p = event.getPlayer();
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
			Block block = event.getClickedBlock();
			if(block.getType().equals(Material.DAYLIGHT_DETECTOR)) {
				if (p.getGameMode() != GameMode.CREATIVE) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onInteract40(PlayerInteractEvent event){
		Player p = event.getPlayer();
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
			Block block = event.getClickedBlock();
			if(block.getType().equals(Material.CHIPPED_ANVIL)) {
				if (p.getGameMode() != GameMode.CREATIVE) {
					event.setCancelled(true);
				}
			}
		}
	}


	@EventHandler
	public void onInteract41(PlayerInteractEvent event){
		Player p = event.getPlayer();
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
			Block block = event.getClickedBlock();
			if(block.getType().equals(Material.DAMAGED_ANVIL)) {
				if (p.getGameMode() != GameMode.CREATIVE) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onInteract42(PlayerInteractEvent event){
		Player p = event.getPlayer();
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
			Block block = event.getClickedBlock();
			if(block.getType().equals(Material.STONECUTTER)) {
				if (p.getGameMode() != GameMode.CREATIVE) {
					event.setCancelled(true);
				}
			}
		}
	}

}
