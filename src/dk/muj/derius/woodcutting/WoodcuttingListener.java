package dk.muj.derius.woodcutting;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.massivecraft.massivecore.MassivePlugin;
import com.massivecraft.massivecore.util.MUtil;

import dk.muj.derius.ability.Ability;
import dk.muj.derius.ability.AbilityUtil;
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
		Player p = e.getPlayer();
		ItemStack inHand = p.getItemInHand();
		MPlayer mplayer = MPlayer.get(e.getPlayer().getUniqueId().toString());
		
		// Adding Exp
		
		// If the player can earn exp in this area...
		if (!skill.CanSkillBeEarnedInArea(e.getBlock().getLocation()))
			return;
		
		// and if the item in his hand is an axe...
		if(!MUtil.isAxe(inHand))
			return; 
		
		// ..add the exp!
		this.PlayerEarnExp(logId, mplayer);
		
		
		// Adding doubledrop
		
		// If this skill can be used in this area...
		if (!skill.CanSkillBeUsedInArea(loc))
			return;
			
		// and the player gets doubledrop (chance and random)..
		if (!SkillUtil.PlayerGetDoubleDrop(mplayer,skill))
			return;
		
		// ..add doubledrop
		for (ItemStack is: b.getDrops(inHand))
		{
			b.getWorld().dropItemNaturally(loc, is);
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onAbility(PlayerInteractEvent e)
	{	
		Material material = e.getItem().getType();
		MPlayer mplayer = MPlayer.get(e.getPlayer().getUniqueId().toString());
		Ability treeHarvester = DeriusWoodcutting.getTreeHarvest();
		Location loc = e.getPlayer().getLocation();
		
		// Checks whether the ability can be used in this area.
		if(!treeHarvester.CanAbilityBeUsedInArea(loc))
			return;
		
		// Checks whether the material is an interactKey
		if(!AbilityUtil.isInteractKey(treeHarvester, material))
			return;
		
		// Check whether the cooldown has expired and if so send the message and return.
		if (mplayer.hasCooldownExpired(true)) 
			return; 
		
		// Set Cooldown and activate the ability
		mplayer.setCooldownExpireBetween(120*20, 240*20);
		mplayer.ActivateActiveAbility(treeHarvester, 20*20);
		
	}
	
	// Adds the exp for said block
	private void PlayerEarnExp(int logId, MPlayer mplayer)
	{
		if (!MConf.get().expGain.containsKey(logId))
		{
			return;
		}
		
		int expGain = MConf.get().expGain.get(logId);
		mplayer.AddExp(DeriusWoodcutting.getWoodcuttingSkill(), expGain);
	}
}
