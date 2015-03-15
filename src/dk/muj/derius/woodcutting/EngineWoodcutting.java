package dk.muj.derius.woodcutting;

import java.util.Optional;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

import com.massivecraft.massivecore.EngineAbstract;
import com.massivecraft.massivecore.util.MUtil;

import dk.muj.derius.api.DeriusAPI;
import dk.muj.derius.api.VerboseLevel;
import dk.muj.derius.api.player.DPlayer;
import dk.muj.derius.api.util.AbilityUtil;
import dk.muj.derius.lib.BlockUtil;

public class EngineWoodcutting extends EngineAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //

	private static EngineWoodcutting i = new EngineWoodcutting();
	public static EngineWoodcutting get() { return i; }
	public EngineWoodcutting() { }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public Plugin getPlugin()
	{
		return DeriusWoodcutting.get();
	}
	
	// -------------------------------------------- //
	// LISTENER
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event)
	{
		Player player = event.getPlayer();
		DPlayer dplayer = DeriusAPI.getDPlayer(player);
		Block block = event.getBlock();
		
		Optional<Material> optPrepared = dplayer.getPreparedTool();
		
		if (optPrepared.isPresent() && MUtil.AXE_MATERIALS.contains(optPrepared.get()) && BlockUtil.isLog(block))
		{
			AbilityUtil.activateAbility(dplayer, Timber.get(), block, VerboseLevel.NORMAL);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)//, ignoreCancelled = true)
	public void activateLeafBlower(PlayerInteractEvent event)
	{
		if (event.getAction() != Action.LEFT_CLICK_BLOCK) return;
		if ( ! event.hasItem()) return;
		if ( ! MUtil.isAxe(event.getItem())) return;
		
		Block block = event.getClickedBlock();
		if ( ! BlockUtil.isLeave(block)) return;
		
		DPlayer dplayer = DeriusAPI.getDPlayer(event.getPlayer());
		
		AbilityUtil.activateAbility(dplayer, LeafBlower.get(), block, VerboseLevel.HIGHEST);
	}

}
