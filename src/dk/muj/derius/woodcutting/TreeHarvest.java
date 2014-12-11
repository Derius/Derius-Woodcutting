package dk.muj.derius.woodcutting;

import com.massivecraft.massivecore.util.MUtil;

import dk.muj.derius.ability.Ability;
import dk.muj.derius.ability.AbilityType;
import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.skill.Skill;
import dk.muj.derius.woodcutting.entity.MConf;

public class TreeHarvest extends Ability
{	
	// -------------------------------------------- //
	// DESCRIPTION
	// -------------------------------------------- //
	
	public TreeHarvest()
	{
		this.setName("Tree Harvest");
		this.setDescription("Harvests a full tree.");
		this.setType(AbilityType.ACTIVE);
		
		this.addInteractKeys(MUtil.AXE_MATERIALS);
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
		return DeriusWoodcutting.getWoodcuttingSkill();
	}

	// -------------------------------------------- //
	// ABILITY ACTIVATION
	// -------------------------------------------- //

	@Override
	public void onActivate(MPlayer p, Object other)
	{
		// Nothing at the moment.
	}
	
	@Override
	public void onDeactivate(MPlayer p)
	{
		// Nothing at the moment.
	}

	// -------------------------------------------- //
	// ABILITY ACTIVATION
	// -------------------------------------------- //
	
	@Override
	public boolean CanPlayerActivateAbility(MPlayer p)
	{
		if(p.getLvl(DeriusWoodcutting.getWoodcuttingSkill()) >= MConf.get().getTreeHarvestMinLvl)
			return true;
		return false;
	}
	
	// -------------------------------------------- //
	// Level description
	// -------------------------------------------- //
	
	@Override
	public String getLvlDescription(int lvl)
	{
		return "";
	}
	

}
