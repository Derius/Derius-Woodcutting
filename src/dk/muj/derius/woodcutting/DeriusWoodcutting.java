package dk.muj.derius.woodcutting;

import com.massivecraft.massivecore.MassivePlugin;

import dk.muj.derius.woodcutting.DeriusWoodcutting;
import dk.muj.derius.woodcutting.WoodcuttingListener;
import dk.muj.derius.woodcutting.WoodcuttingSkill;
import dk.muj.derius.woodcutting.entity.MConfColl;
import dk.muj.derius.skill.Skill;
import dk.muj.derius.skill.Skills;

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
		
		// -------------------------------------------- //
		// LISTENERS
		// -------------------------------------------- //
		
		WoodcuttingListener listener;
		
		@Override
		public void onEnable()
		{
			super.preEnable();
			
			Skills.AddSkill(WoodcuttingSkill);
			
			MConfColl.get().init();
			
			listener = new WoodcuttingListener(this);
			
			MConfColl.get().get("Woodcutting", true);
			
			super.postEnable();
		}
}
