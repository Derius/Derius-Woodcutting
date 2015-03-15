package dk.muj.derius.woodcutting;

import java.util.Collection;
import java.util.Map;

import org.bukkit.Material;

import com.massivecraft.massivecore.util.MUtil;

import dk.muj.derius.api.ability.AbilityDurabilityMultiplier;
import dk.muj.derius.api.skill.Skill;

public class CarefulCutting extends AbilityDurabilityMultiplier
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //

	private static CarefulCutting i = new CarefulCutting();
	public static CarefulCutting get() { return i; }

	public CarefulCutting()
	{

	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String getId()
	{
		return "derius:woodcutting:careful";
	}

	@Override
	public Skill getSkill()
	{
		return WoodcuttingSkill.get();
	}

	@Override
	public Collection<Material> getToolTypes()
	{
		return MUtil.AXE_MATERIALS;
	}

	@Override
	public Map<Integer, Double> getDurabilityMultiplier()
	{
		return WoodcuttingSkill.getDurabilityMultiplier();
	}

	@Override
	public String getToolName()
	{
		return "Axe";
	}

}
