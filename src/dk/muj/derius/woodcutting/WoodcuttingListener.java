package dk.muj.derius.woodcutting;

import java.util.Optional;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.massivecraft.massivecore.util.MUtil;

import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.util.Listener;
import dk.muj.derius.woodcutting.entity.MConf;

public class WoodcuttingListener implements Listener
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	private static WoodcuttingListener i = new WoodcuttingListener();
	public static WoodcuttingListener get() { return i; }
	
	@SuppressWarnings("deprecation")
	public WoodcuttingListener()
	{
		i = this;
		registerBlockBreakKeys(MConf.get().expGain.keySet().parallelStream().map(Material::getMaterial).collect(Collectors.toList()));
		Listener.registerTools(MUtil.AXE_MATERIALS);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void onBlockBreak(MPlayer mplayer, Block block)
	{
		if ( ! mplayer.isPlayer()) return;
		Player player = mplayer.getPlayer();
		ItemStack inHand = player.getItemInHand();
		if (!MUtil.isAxe(inHand))
			return;
		
		if ( ! mplayer.getPreparedTool().equals(Optional.empty()) && MUtil.AXE_MATERIALS.contains(mplayer.getPreparedTool().get()))
		{
			mplayer.activateAbility(TreeHarvest.get(), null);
		}
		
		@SuppressWarnings("deprecation")
		Integer exp = MConf.get().expGain.get(block.getTypeId());
		if ( exp != null)
		{
			mplayer.addExp(WoodcuttingSkill.get(), exp);
		}
		
		mplayer.activateAbility(DoubleDrop.get(), block);
	}
}
