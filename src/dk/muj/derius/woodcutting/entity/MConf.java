package dk.muj.derius.woodcutting.entity;

import java.util.Map;

import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;

public class MConf extends Entity<MConf>
{
	// -------------------------------------------- //
	// META
	// -------------------------------------------- //
	
	protected static transient MConf i;
	public static MConf get() { return i; }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	// -------------------------------------------- //
	// EXP GAIN
	// -------------------------------------------- //
	
	//The first int is the id of the block
	//The second is the amount of exp gained.
	public Map<Integer, Integer> expGain = MUtil.map(
			17,	10,
			162,	50
			);
}
