package dk.muj.derius.woodcutting;

import java.util.ArrayList;
import java.util.List;

import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.skill.Skill;

public class WoodcuttingSkill extends Skill
{
	public WoodcuttingSkill()
	{
		this.addEarnExpDesc("cut wood");
		this.addPassiveAbilityDesc("Double drop", "Get twice as many logs");
		this.addActiveAbilityDesc("Tree harvester", "Harvests a whole tree");
	}
	
	@Override
	public String getId()
	{
		return Const.ID;
	}

	@Override
	public String getName()
	{
		return Const.NAME;
	}

	@Override
	public String getDesc()
	{
		return "Makes you better at woodcutting";
	}

	@Override
	public boolean CanPlayerLearnSkill(MPlayer p)
	{
		return true;
	}

	@Override
	public List<String> getAbilitiesDecriptionByLvl(int lvl)
	{
		List<String> list = new ArrayList<String>();
		double doubleDropChance = lvl/10.0;
		
		list.add("<i>Double drop: <yellow>"+doubleDropChance+"% chance to double drop");
		return null;
	}

}
