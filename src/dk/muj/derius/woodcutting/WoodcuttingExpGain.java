package dk.muj.derius.woodcutting;

import java.util.Collection;
import java.util.Map;

import org.bukkit.Material;

import com.massivecraft.massivecore.util.MUtil;

import dk.muj.derius.api.BlockBreakExpGain;
import dk.muj.derius.api.skill.Skill;

public class WoodcuttingExpGain implements BlockBreakExpGain
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
   
	private static WoodcuttingExpGain i = new WoodcuttingExpGain();
	public static WoodcuttingExpGain get() { return i; }
	private WoodcuttingExpGain() { }

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public Map<Material, Integer> getBlockTypes()
	{
		return WoodcuttingSkill.getExpGain();
	}
	
	@Override
	public Collection<Material> getToolTypes()
	{
		return MUtil.AXE_MATERIALS;
	}
	
	@Override
	public Skill getSkill()
	{
		return WoodcuttingSkill.get();
	}

}
