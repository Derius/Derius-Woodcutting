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
	// ID DEFINITION
	// -------------------------------------------- //
	
	/* 
	 * Note: skillId and abilityId are not related,
	 * but we use the tenners to define the skillId and all the values in between as the abilityId's.
	 */
	 
	/**
	 * The Id of the skill, there is only one of these in each skill.
	 */
	private int skillId = 20;
	public int getSkillId() { return skillId; }
	
	/**
	 * The Id of the Tree harvest Ability. Each ability has it's own Id
	 */
	private int doubleDropId = 21;
	public int getDoubleDropId() { return doubleDropId; }
	
	/**
	 * The Id of the Tree harvest Ability. Each ability has it's own Id
	 */
	private int timberId = 22;
	public int getTimberId() { return timberId; }
	
	// -------------------------------------------- //
	// ABILITY
	// -------------------------------------------- //
	
	/**
	 * The level the player has to have for executing the timber
	 */
	public int timberMinLvl = 500;
	public int getTimberMinLvl() { return timberMinLvl; }

	private int logSoftCap = 15;
	public int getLogSoftCap() { return logSoftCap; }
	
	private int logHardCap = 50;
	public int getLogHardCap() { return logHardCap; }
	
	private boolean dropExtraItems = true;
	public boolean getDropExtraItems() { return dropExtraItems; }
	
	private boolean informSurroundingPlayers = true;
	public boolean getInformSurroundingPlayers() { return informSurroundingPlayers; }
	
	private double timberDistance = 25.0;
	public double getTimberDistance() { return timberDistance; }
	
	/*
	 * Dividers, per how many blocks of log does a 
	 * item get thrown.
	 */
	
	private int chancePerPlank = 10;
	public int getChancePerPlanks() { return chancePerPlank; }
	
	private int chancePerSticks = 20;
	public int getChancePerSticks() { return chancePerSticks; }
	
	private int chancePerApples = 40;
	public int getChancePerApples() { return chancePerApples; }

	private int chancePerSaplings = 50;
	public int getChancePerSaplings() { return chancePerSaplings; }
	
	public int randomModifier = 5;
	public int getRandomModifier() { return randomModifier; }


	
	// -------------------------------------------- //
	// EXP GAIN
	// -------------------------------------------- //
	
	/**
	 * This Map stores, which blockId (first Integer) gives you how many exp (second Integer).
	 */
	public Map<Integer, Integer> expGain = MUtil.map(
			17,	20,
			162,	20
			);
	
}
