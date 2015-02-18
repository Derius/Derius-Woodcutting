package dk.muj.derius.woodcutting;

import org.bukkit.block.BlockState;

import com.massivecraft.massivecore.util.MUtil;

import dk.muj.derius.DeriusCore;
import dk.muj.derius.api.DPlayer;
import dk.muj.derius.util.AbilityUtil;
import dk.muj.derius.util.Listener;

public class WoodcuttingListener implements Listener
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	private static WoodcuttingListener i = new WoodcuttingListener();
	public static WoodcuttingListener get() { return i; }
	
	public WoodcuttingListener()
	{
		i = this;
		Listener.registerBlockBreakKeys(this, WoodcuttingSkill.getExpGain().keySet());
		Listener.registerTools(MUtil.AXE_MATERIALS);
		DeriusCore.getBlockMixin().addBlockTypesToListenFor(WoodcuttingSkill.getExpGain().keySet());
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void onBlockBreak(DPlayer dplayer, BlockState block)
	{
		if ( ! dplayer.isPlayer()) return;
		
		if (MUtil.AXE_MATERIALS.contains(dplayer.getPreparedTool().get()))
		{
			AbilityUtil.activateAbility(dplayer, Timber.get(), block.getBlock(), false);
		}
		
		Integer exp = WoodcuttingSkill.getExpGain().get(block.getType());
		if ( exp != null)
		{
			dplayer.addExp(WoodcuttingSkill.get(), exp);
		}
		
		AbilityUtil.activateAbility(dplayer, DoubleDrop.get(), block, false);
	}
}
