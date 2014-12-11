package dk.muj.derius.woodcutting;

import dk.muj.derius.woodcutting.entity.MConf;

public class Const
{
	// -------------------------------------------- //
	// DATABASE
	// -------------------------------------------- //
	
	public static final String BASENAME = "derius_woodcutting";
	public static final String BASENAME_ = BASENAME+"_";
	
	public static final String COLLECTION_MCONF = BASENAME_+"mconf";
	
	// -------------------------------------------- //
	// SKILL DATA
	// -------------------------------------------- //
	
	public static final int ID = MConf.get().getSkillId;
}
