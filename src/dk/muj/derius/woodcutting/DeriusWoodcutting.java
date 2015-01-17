package dk.muj.derius.woodcutting;

import com.massivecraft.massivecore.MassivePlugin;

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
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void onEnable()
	{
		super.preEnable();
	
		MConfColl.get().init();
			
		WoodcuttingSkill.get().register();
		Timber.get().register();
		DoubleDrop.get().register();
		new WoodcuttingListener();
	
		MConfColl.get().get("Woodcutting", true);
		
		super.postEnable();
	}
}
