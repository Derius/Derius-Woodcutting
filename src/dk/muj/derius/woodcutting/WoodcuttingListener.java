package dk.muj.derius.woodcutting;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import com.massivecraft.massivecore.MassivePlugin;

import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.skill.Skill;
import dk.muj.derius.skill.SkillUtil;
import dk.muj.derius.woodcutting.entity.MConf;

public class WoodcuttingListener implements Listener
{
	MassivePlugin plugin;
	public WoodcuttingListener(MassivePlugin plugin)
	{
		this.plugin = plugin;
		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onCut(BlockBreakEvent e)
	{
		Skill skill = DeriusWoodcutting.getWoodcuttingSkill();
		
		Block b = e.getBlock();
		int logId = b.getTypeId();
		Location loc = b.getLocation();
		MPlayer mplayer = MPlayer.get(e.getPlayer().getUniqueId().toString());
		
		Bukkit.broadcastMessage("be used" + skill.CanSkillBeUsedInArea(loc));
		if (skill.CanSkillBeEarnedInArea(e.getBlock().getLocation()))
		{
			this.PlayerEarnExp(logId, mplayer);
		}
		
		Bukkit.broadcastMessage("be used" + skill.CanSkillBeUsedInArea(loc));
		if (skill.CanSkillBeUsedInArea(loc))
		{
			
			if (SkillUtil.PlayerGetDoubleDrop(mplayer,Const.ID))
			{
				for (ItemStack is: b.getDrops())
				{
					b.getWorld().dropItemNaturally(loc, is);
				}
			}
		}
	}
	
	// Adds the exp for said block
	private void PlayerEarnExp(int logId, MPlayer mplayer)
	{
		if (!MConf.get().expGain.containsKey(logId))
		{
			return;
		}
		
		int expGain = MConf.get().expGain.get(logId);
		mplayer.AddExp(Const.ID, expGain);
	}

}
