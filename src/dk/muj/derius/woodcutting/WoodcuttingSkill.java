package dk.muj.derius.woodcutting;

import java.util.Map;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.xlib.gson.reflect.TypeToken;

import dk.muj.derius.api.skill.SkillAbstract;

public class WoodcuttingSkill extends SkillAbstract
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
		this.writeConfig(Const.JSON_TIMBER_MIN_LEVEL, 0);
		this.writeConfig(Const.JSON_SHOULD_DROP_EXTRA, true);
		this.writeConfig(Const.JSON_SHOULD_INFORM_PLAYERS, true);
		
		this.writeConfig(Const.JSON_LEVELS_PER_PERCENT, 10);
		
		this.writeConfig(Const.JSON_PLANKS_PER_LOG_MIN, 0.05);
		this.writeConfig(Const.JSON_PLANKS_PER_LOG_MAX, 0.2);
		
		this.writeConfig(Const.JSON_STICKS_PER_LOG_MIN, 0.05);
		this.writeConfig(Const.JSON_STICKS_PER_LOG_MAX, 0.2);
		
		this.writeConfig(Const.JSON_APPLES_PER_LEAVE_MIN, 0.05);
		this.writeConfig(Const.JSON_APPLES_PER_LEAVE_MAX, 0.2);
		
		this.writeConfig(Const.JSON_TIMBER_TOOL_DAMAGE_MULTIPLIER, 2.0);
		this.writeConfig(Const.JSON_SPLINTER_MIN, 2);
		this.writeConfig(Const.JSON_SPLINTER_MAX, 5);
		this.writeConfig(Const.JSON_TIMBER_INFORM_DISTANCE, 25.0);
		
		this.writeConfig(Const.JSON_DOUBLE_DROP_BLOCKS, MUtil.set(
				Material.LOG,
				Material.LOG_2
				), new TypeToken<Set<Material>>(){});
		
		this.writeConfig(Const.JSON_EXP_GAIN, MUtil.map(
				Material.LOG, 20,
				Material.LOG_2, 20
				), new TypeToken<Map<Material, Integer>>(){});
		
		this.writeConfig(Const.JSON_RADIUS_OAK, 3);
		this.writeConfig(Const.JSON_RADIUS_SPRUCE, 3);
		this.writeConfig(Const.JSON_RADIUS_BIRCH, 3);
		this.writeConfig(Const.JSON_RADIUS_JUNGLE, 6);
		this.writeConfig(Const.JSON_RADIUS_ACACIA, 4);
		this.writeConfig(Const.JSON_RADIUS_DARK_OAK, 3);
		
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public Plugin getPlugin()
	{
		return DeriusWoodcutting.get();
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
	
	public static Set<Material> getDoubleDropBlocks()
	{
		return get().readConfig(Const.JSON_DOUBLE_DROP_BLOCKS, new TypeToken<Set<Material>>(){});
	}
	
	public static int getLevelsPerPercent()
	{
		return get().readConfig(Const.JSON_LEVELS_PER_PERCENT, int.class);
	}
	
	public static int getTimberMinLvl()
	{
		return get().readConfig(Const.JSON_TIMBER_MIN_LEVEL, int.class);
	}

	public static boolean shouldDropExtraItems()
	{
		return get().readConfig(Const.JSON_SHOULD_DROP_EXTRA, boolean.class);
	}
	
	public static boolean shouldInformSurroundingPlayers()
	{
		return get().readConfig(Const.JSON_SHOULD_INFORM_PLAYERS, boolean.class);
	}
	
	public static int getTimberDistance()
	{
		return get().readConfig(Const.JSON_TIMBER_INFORM_DISTANCE, int.class);
	}
	

	public static double getApplesPerLeaveMin()
	{
		return get().readConfig(Const.JSON_APPLES_PER_LEAVE_MIN, double.class);
	}
	
	public static double getApplesPerLeaveMax()
	{
		return get().readConfig(Const.JSON_APPLES_PER_LEAVE_MAX, double.class);
	}
	

	public static double getPlanksPerLogMin()
	{
		return get().readConfig(Const.JSON_PLANKS_PER_LOG_MIN, double.class);
	}
	
	public static double getPlanksPerLogMax()
	{
		return get().readConfig(Const.JSON_PLANKS_PER_LOG_MAX, double.class);
	}
	

	public static double getSticksPerLogMin()
	{
		return get().readConfig(Const.JSON_STICKS_PER_LOG_MIN, double.class);
	}
	
	public static double getSticksPerLogMax()
	{
		return get().readConfig(Const.JSON_STICKS_PER_LOG_MAX, double.class);
	}
	
	public static double getSplinterDamageMin()
	{
		return get().readConfig(Const.JSON_SPLINTER_MIN, double.class);
	}
	
	public static double getSplinterDamageMax()
	{
		return get().readConfig(Const.JSON_SPLINTER_MAX, double.class);
	}
	
	public static double getTimberToolDamageMultiplier()
	{
		return get().readConfig(Const.JSON_TIMBER_TOOL_DAMAGE_MULTIPLIER, double.class);
	}
	
	public static int getRadiusOak()
	{
		return get().readConfig(Const.JSON_RADIUS_OAK, int.class);
	}
	
	public static int getRadiusSpruce()
	{
		return get().readConfig(Const.JSON_RADIUS_SPRUCE, int.class);
	}
	
	public static int getRadiusBirch()
	{
		return get().readConfig(Const.JSON_RADIUS_BIRCH, int.class);
	}
	
	public static int getRadiusJungle()
	{
		return get().readConfig(Const.JSON_RADIUS_JUNGLE, int.class);
	}
	
	public static int getRadiusAcacia()
	{
		return get().readConfig(Const.JSON_RADIUS_ACACIA, int.class);
	}
	
	public static int getRadiusDarkOak()
	{
		return get().readConfig(Const.JSON_RADIUS_DARK_OAK, int.class);
	}

}
