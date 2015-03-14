package dk.muj.derius.woodcutting;

import java.util.Collection;

import org.bukkit.Material;

import dk.muj.derius.api.ability.AbilityDoubleDrop;
import dk.muj.derius.api.skill.Skill;

public class DoubleDrop extends AbilityDoubleDrop
{
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //

	private static DoubleDrop i = new DoubleDrop();
	public static DoubleDrop get() { return i; }

	public DoubleDrop()
	{

	}
		
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String getId()
	{
		return "derius:woodcutting:doubledrop";
	}
	
	@Override
	public Skill getSkill()
	{
		return WoodcuttingSkill.get();
	}

	@Override
	public Collection<Material> getBlockTypes()
	{
		return WoodcuttingSkill.getDoubleDropBlocks();
	}

	@Override
	public int getLevelsPerPercent()
	{
		return WoodcuttingSkill.getLevelsPerPercent();
	}
	
}
