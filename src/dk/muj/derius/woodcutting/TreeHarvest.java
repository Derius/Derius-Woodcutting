package dk.muj.derius.woodcutting;

import dk.muj.derius.ability.Ability;
import dk.muj.derius.ability.AbilityType;
import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.skill.Skill;
import dk.muj.derius.woodcutting.entity.MConf;

public class TreeHarvest extends Ability
{	
	private static TreeHarvest i = new TreeHarvest();
	public static TreeHarvest get() { return i; }
	
	// -------------------------------------------- //
	// DESCRIPTION
	// -------------------------------------------- //
	
	public TreeHarvest()
	{
		this.setName("Tree Harvest");
		
		this.setDescription("Harvests a full tree.");
		
		this.setType(AbilityType.ACTIVE);
	}
	
	// -------------------------------------------- //
	// SKILL & ID
	// -------------------------------------------- //
	
	@Override
	public int getId()
	{
		return MConf.get().getTreeHarvestId();
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
	// Level description
	// -------------------------------------------- //
	
	@Override
	public String getLvlDescription(int lvl)
	{
		return "Lasts "+this.getTicksLast(lvl)/20 + " seconds";
	}
}
