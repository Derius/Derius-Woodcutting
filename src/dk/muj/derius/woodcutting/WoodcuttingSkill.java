package dk.muj.derius.woodcutting;

import java.util.Map;

import org.bukkit.Material;

import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.xlib.gson.reflect.TypeToken;

import dk.muj.derius.api.Skill;
import dk.muj.derius.entity.skill.DeriusSkill;

public class WoodcuttingSkill extends DeriusSkill implements Skill
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static WoodcuttingSkill i = new WoodcuttingSkill();
	public static WoodcuttingSkill get() { return i; }
	
	public WoodcuttingSkill()
	{
		// Skill properties 
		this.setName("Woodcutting");
		
		this.setDesc("Makes you better at woodcutting.");
		
		this.addEarnExpDescs("Cut wood!");
		
		this.setIcon(Material.LOG);
		
		// Config
		this.writeConfig("timberMinLvl", 0);
		this.writeConfig("logSoftCap", 10);
		this.writeConfig("logHardCap", 50);
		this.writeConfig("leaveSoftCap", 50);
		this.writeConfig("leaveHardCap", 100);
		this.writeConfig("dropExtraItems", true);
		this.writeConfig("informSurroundingPlayers", true);
		this.writeConfig("timberDistance", 25.0);
		this.writeConfig("chancePerPlank", 10);
		this.writeConfig("chancePerSticks", 10);
		this.writeConfig("chancePerApples", 25);
		this.writeConfig("splinterDamageMultiplicator", 3.5);
		
		this.writeConfig(Const.JSON_EXP_GAIN, MUtil.map(
				Material.LOG, 20,
				Material.LOG_2, 20
				), new TypeToken<Map<Material, Integer>>(){});
	}
	
	@Override
	public String getId()
	{
		return "derius:woodcutting";
	}
	
	// -------------------------------------------- //
	// CONFIG GETTERS
	// -------------------------------------------- //
	
	public static Map<Material, Integer> getExpGain()
	{
		return get().readConfig(Const.JSON_EXP_GAIN, new TypeToken<Map<Material, Integer>>(){});
	}
	
	public static int getTimberMinLvl()
	{
		return get().readConfig("timberMinLvl", int.class);
	}
	
	public static int getLogSoftCap()
	{
		return get().readConfig("logSoftCap", int.class);
	}

	public static int getLogHardCap()
	{
		return get().readConfig("logHardCap", int.class);
	}
	
	public static int getLeaveSoftCap()
	{
		return get().readConfig("leaveSoftCap", int.class);
	}
	
	public static int getLeaveHardCap()
	{
		return get().readConfig("leaveHardCap", int.class);
	}
	
	public static boolean getDropExtraItems()
	{
		return get().readConfig("dropExtraItems", boolean.class);
	}
	
	public static boolean getInformSurroundingPlayers()
	{
		return get().readConfig("informSurroundingPlayers", boolean.class);
	}
	
	public static double getTimberDistance()
	{
		return get().readConfig("informSurroundingPlayers", double.class);
	}
	
	public static double getChancePerPlank()
	{
		return get().readConfig("chancePerPlank", double.class);
	}
	
	public static double getChancePerSticks()
	{
		return get().readConfig("chancePerSticks", double.class);
	}
	
	public static double getChancePerApples()
	{
		return get().readConfig("chancePerApples", double.class);
	}
	
	
	public static double getSplinterDamageMultiplicator()
	{
		return get().readConfig("splinterDamageMultiplicator", double.class);
	}
	
}
