package dk.muj.derius.woodcutting;

import com.massivecraft.massivecore.MassivePlugin;

import dk.muj.derius.ability.Abilities;
import dk.muj.derius.ability.Ability;
import dk.muj.derius.skill.Skill;
import dk.muj.derius.skill.Skills;
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
		
		WoodcuttingListener listener;
		
		@Override
		public void onEnable()
		{
			super.preEnable();
			
			Skills.AddSkill(WoodcuttingSkill);
			Abilities.AddAbility(TreeHarvest);
			
			MConfColl.get().init();
			
			listener = new WoodcuttingListener(this);
			
			MConfColl.get().get("Woodcutting", true);
			
			super.postEnable();
		}
}
