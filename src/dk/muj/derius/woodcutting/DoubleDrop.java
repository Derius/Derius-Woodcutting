package dk.muj.derius.woodcutting;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import com.massivecraft.massivecore.util.MUtil;

import dk.muj.derius.ability.Ability;
import dk.muj.derius.ability.AbilityType;
import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.skill.Skill;
import dk.muj.derius.skill.SkillUtil;
import dk.muj.derius.woodcutting.entity.MConf;

public class DoubleDrop extends Ability
{
	private static DoubleDrop i = new DoubleDrop();
	public static DoubleDrop get() { return i; }
	
	// -------------------------------------------- //
	// DESCRIPTION
	// -------------------------------------------- //

	public DoubleDrop()
	{
		this.setName("Doubledrop");
		this.setDescription("gives doubledrop");
		this.setType(AbilityType.PASSIVE);
		
		List<Material> blockBreakKeys = new ArrayList<Material>();
		for(int i : MConf.get().expGain.keySet())
			blockBreakKeys.add(Material.getMaterial(i));
		this.addBlockBreakKeys(blockBreakKeys);
	}
		
	// -------------------------------------------- //
	// SKILL & ID
	// -------------------------------------------- //
	
	@Override
	public int getId()
	{
		return MConf.get().getTreeHarvestId;
	}
	
	@Override
	public Skill getSkill()
	{
		return WoodcuttingSkill.get();
	}
	
	// -------------------------------------------- //
	// ABILITY ACTIVATION
	// -------------------------------------------- //
	
	@Override
	public boolean CanPlayerActivateAbility(MPlayer p)
	{
		return true;
	}
	
	@Override
	public void onActivate(MPlayer mplayer, Object block)
	{
		if(!(block instanceof Block))
			return;
		if(!mplayer.isPlayer())
			return;
		
		Skill skill = getSkill();
		
		Block b = (Block) block;
		
		int logId = b.getTypeId();
		ItemStack inHand = mplayer.getPlayer().getItemInHand();
		Location loc = b.getLocation();
		
		if(!MUtil.isAxe(inHand))
			return;
		
		if(skill.CanSkillBeEarnedInArea(b.getLocation()))
		{
			if(!MConf.get().expGain.containsKey(logId))
				return;
			int expGain = MConf.get().expGain.get(logId);
			mplayer.AddExp(WoodcuttingSkill.get(), expGain);
		}
		
		if(i.CanAbilityBeUsedInArea(loc) && MConf.get().expGain.containsKey(logId) && 
				SkillUtil.PlayerGetDoubleDrop(mplayer, skill, 10));
	}

	@Override
	public void onDeactivate(MPlayer p)
	{
		// There is nothing to deactivate, a passive ability gets only activated once on runtime
	}

	// -------------------------------------------- //
	// Level description
	// -------------------------------------------- //
	
	@Override
	public String getLvlDescription(int lvl)
	{
		return "chance to double drop" + lvl/10.0 + "%";
	}
}
