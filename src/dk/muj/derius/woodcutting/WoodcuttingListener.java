package dk.muj.derius.woodcutting;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import com.massivecraft.massivecore.MassivePlugin;

import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.skill.Skill;
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
		Player p = e.getPlayer();
		Location loc = b.getLocation();
		
		Bukkit.broadcastMessage("be used" + skill.CanSkillBeUsedInArea(loc));
		if (skill.CanSkillBeEarnedInArea(e.getBlock().getLocation()))
		{
			this.PlayerEarnExp(logId, p);
		}
		
		Bukkit.broadcastMessage("be used" + skill.CanSkillBeUsedInArea(loc));
		if (skill.CanSkillBeUsedInArea(loc))
		{
			if (this.PlayerGetDoubleDrop(p))
			{
				for (ItemStack is: b.getDrops())
				{
					b.getWorld().dropItemNaturally(loc, is);
				}
			}
		}
	}
	
	// Computes whether the Player gets a doubledrop or not.
	private boolean PlayerGetDoubleDrop (Player p)
	{
		MPlayer mplayer = MPlayer.get(p.getUniqueId().toString());
		int level = mplayer.getLvl(Const.ID);
		double chance = level/10.0;
		double random = (int) ((Math.random()*100) + 1);
		if (chance >= random)
		{
			return true;
		}
		return false;
	}
	
	// Adds the exp for said block
	private void PlayerEarnExp(int logId, Player p)
	{
		if (!MConf.get().expGain.containsKey(logId))
		{
			return;
		}
		
		int expGain = MConf.get().expGain.get(logId);
		MPlayer mplayer = MPlayer.get(p.getUniqueId().toString());
		mplayer.AddExp(Const.ID, expGain);
	}
}
