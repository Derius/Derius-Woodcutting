package dk.muj.derius.woodcutting;

import com.massivecraft.massivecore.MassivePlugin;

import dk.muj.derius.ability.Ability;
import dk.muj.derius.skill.Skill;
import dk.muj.derius.woodcutting.entity.MConfColl;

public class DeriusWoodcutting extends MassivePlugin
{
	// -------------------------------------------- //
		// INSTANCE & CONSTRUCT
		// -------------------------------------------- //
	    
	    private static DeriusWoodcutting i;
		public static DeriusWoodcutting get() { return i; }
		public DeriusWoodcutting() { i = this; }
		
		// -------------------------------------------- //
		// FIELDS
		// -------------------------------------------- //
		
		private static Skill WoodcuttingSkill = new WoodcuttingSkill();
		public static Skill getWoodcuttingSkill () {	return DeriusWoodcutting.WoodcuttingSkill;	}
		
		private static Ability TreeHarvest = new TreeHarvest();
		public static Ability getTreeHarvest() {	return DeriusWoodcutting.TreeHarvest;	}
		
		// -------------------------------------------- //
		// LISTENERS
		// -------------------------------------------- //
		
		@Override
		public void onEnable()
		{
			super.preEnable();
			
			MConfColl.get().init();
			
			WoodcuttingSkill.register();
			TreeHarvest.register();
			
			MConfColl.get().get("Woodcutting", true);
			
			super.postEnable();
		}
}
