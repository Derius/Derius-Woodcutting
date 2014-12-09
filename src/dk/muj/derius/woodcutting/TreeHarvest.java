package dk.muj.derius.woodcutting;

import java.util.Collection;

import org.bukkit.Material;

import com.massivecraft.massivecore.util.MUtil;

import dk.muj.derius.ability.Ability;
import dk.muj.derius.ability.AbilityType;
import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.skill.Skill;
import dk.muj.derius.woodcutting.entity.MConf;

public class TreeHarvest extends Ability
{	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public int getId()
	{
		return MConf.get().getTreeHarvestId;
	}

	@Override
	public String getName()
	{
		return "Tree harvester";
	}

	@Override
	public boolean CanPlayerActivateAbility(MPlayer p)
	{
		return true;
	}
	
	@Override
	public void onActivate(MPlayer p)
	{
	}

	@Override
	public void onDeactivate(MPlayer p)
	{
	}
	
	@Override
	public Collection<Material> getInteractKeys()
	{
		return MUtil.AXE_MATERIALS;
	}
	
	@Override
	public Skill getSkill()
	{
		return DeriusWoodcutting.getWoodcuttingSkill();
	}

	@Override
	public AbilityType getType()
	{
		// ACTIVE
		return null;
	}

	@Override
	public Collection<Material> getBlockBreakKeys()
	{
		return null;
	}
}
