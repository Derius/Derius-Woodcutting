package dk.muj.derius.woodcutting;

import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.Material;

import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.xlib.gson.reflect.TypeToken;

import dk.muj.derius.api.Skill;
import dk.muj.derius.entity.skill.DeriusSkill;
import dk.muj.derius.mining.Const;
import dk.muj.derius.util.Listener;
import dk.muj.derius.woodcutting.entity.MConf;

public class WoodcuttingSkill extends DeriusSkill implements Skill
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static WoodcuttingSkill i = new WoodcuttingSkill();
	public static WoodcuttingSkill get() { return i; }
	
	@SuppressWarnings("deprecation")
	public WoodcuttingSkill()
	{
		// 
		this.setName("Woodcutting");
		
		this.setDesc("Makes you better at woodcutting.");
		
		this.addEarnExpDescs("Cut wood!");
		
		this.setIcon(Material.LOG);
		
		Listener.registerBlockBreakKeys(WoodcuttingListener.get(), MConf.get().expGain.keySet().stream().map(Material::getMaterial).collect(Collectors.toList()));
		Listener.registerTools(MUtil.AXE_MATERIALS);
		
		
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
	// CONFIG
	// -------------------------------------------- //
	
	public static Map<Material, Integer> getExpGain()
	{
		return get().readConfig(Const.JSON_EXP_GAIN, new TypeToken<Map<Material, Integer>>(){});
	}

}
