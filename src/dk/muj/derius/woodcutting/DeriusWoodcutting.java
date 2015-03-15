package dk.muj.derius.woodcutting;

import com.massivecraft.massivecore.MassivePlugin;
import com.massivecraft.massivecore.util.MUtil;

import dk.muj.derius.api.DeriusAPI;

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
		if ( ! this.preEnable()) return;
			
		WoodcuttingSkill.get().register();
		Timber.get().register();
		DoubleDrop.get().register();
		LeafBlower.get().register();
		
		EngineWoodcutting.get().activate();
		
		DeriusAPI.registerPreparableTools(MUtil.AXE_MATERIALS);
		DeriusAPI.addBlockTypesToListenFor(WoodcuttingSkill.getExpGain().keySet());
		DeriusAPI.addBlockTypesToListenFor(WoodcuttingSkill.getDoubleDropBlocks());
		
		this.postEnable();
	}
	
}
