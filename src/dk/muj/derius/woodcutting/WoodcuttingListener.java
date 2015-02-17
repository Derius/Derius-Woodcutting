package dk.muj.derius.woodcutting;

import org.bukkit.block.BlockState;

import com.massivecraft.massivecore.util.MUtil;

import dk.muj.derius.api.DPlayer;
import dk.muj.derius.util.AbilityUtil;
import dk.muj.derius.util.Listener;
import dk.muj.derius.woodcutting.entity.MConf;

public class WoodcuttingListener implements Listener
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	private static WoodcuttingListener i = new WoodcuttingListener();
	public static WoodcuttingListener get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void onBlockBreak(DPlayer dplayer, BlockState block)
	{
		if (MUtil.AXE_MATERIALS.contains(dplayer.getPreparedTool().get()))
		{
			AbilityUtil.activateAbility(dplayer, Timber.get(), block.getBlock(), false);
		}
		
		@SuppressWarnings("deprecation")
		Integer exp = MConf.get().expGain.get(block.getTypeId());
		if ( exp != null)
		{
			dplayer.addExp(WoodcuttingSkill.get(), exp);
		}
		
		AbilityUtil.activateAbility(dplayer, DoubleDrop.get(), block, false);
	}
}
