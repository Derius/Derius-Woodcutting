package dk.muj.derius.woodcutting;

import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import com.massivecraft.massivecore.util.MUtil;

import dk.muj.derius.ability.Ability;
import dk.muj.derius.ability.AbilityType;
import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.skill.Skill;
import dk.muj.derius.util.SkillUtil;
import dk.muj.derius.woodcutting.entity.MConf;

public class DoubleDrop extends Ability
{
	private static DoubleDrop i = new DoubleDrop();
	public static DoubleDrop get() { return i; }
	
	// -------------------------------------------- //
	// DESCRIPTION
	// -------------------------------------------- //

	public DoubleDrop()
	{
		this.setName("Doubledrop");
		
		this.setDescription("gives doubledrop");
		
		this.setType(AbilityType.PASSIVE);
	}
		
	// -------------------------------------------- //
	// SKILL & ID
	// -------------------------------------------- //
	
	@Override
	public int getId()
	{
		return MConf.get().getDoubleDropId();
	}
	
	@Override
	public Skill getSkill()
	{
		return WoodcuttingSkill.get();
	}
	
	// -------------------------------------------- //
	// ABILITY ACTIVATION
	// -------------------------------------------- //
	
	@Override
	public Optional<Object> onActivate(MPlayer mplayer, Object block)
	{
		if(!(block instanceof Block))
			return Optional.empty();
		if(!mplayer.isPlayer())
			return Optional.empty();
		
		Skill skill = getSkill();
		
		Block b = (Block) block;
		
		@SuppressWarnings("deprecation")
		int logId = b.getTypeId();
		ItemStack inHand = mplayer.getPlayer().getItemInHand();
		Location loc = b.getLocation();
		
		if(!MUtil.isAxe(inHand))
			return Optional.empty();
		
		if(MConf.get().expGain.containsKey(logId) && SkillUtil.shouldPlayerGetDoubleDrop(mplayer, skill, 10))
		{
			for(ItemStack is: b.getDrops(inHand))
				b.getWorld().dropItem(loc, is);
		}
		
		return Optional.empty();
	}

	@Override
	public void onDeactivate(MPlayer p, Optional<Object> other)
	{
		// There is nothing to deactivate, a passive ability gets only activated once on runtime
	}

	// -------------------------------------------- //
	// Level description
	// -------------------------------------------- //
	
	@Override
	public String getLvlDescription(int lvl)
	{
		return "chance to double drop " + lvl/10.0 + "%";
	}
}
