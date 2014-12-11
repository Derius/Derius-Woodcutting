package dk.muj.derius.woodcutting;

import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.skill.Skill;
import dk.muj.derius.woodcutting.entity.MConf;

public class WoodcuttingSkill extends Skill
{
	// -------------------------------------------- //
	// DESCRIPTION
	// -------------------------------------------- //
	
	public WoodcuttingSkill()
	{
		this.setName("Woodcutting");
		this.setDescription("Makes you better at woodcutting.");
		
		this.addEarnExpDesc("Cut wood!");
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public int getId()
	{
		return MConf.get().getSkillId;
	}

	@Override
	public boolean CanPlayerLearnSkill(MPlayer p)
	{
		return true;
	}
}
