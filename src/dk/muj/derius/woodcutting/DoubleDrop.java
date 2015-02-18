package dk.muj.derius.woodcutting;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import com.massivecraft.massivecore.util.MUtil;

import dk.muj.derius.api.Ability;
import dk.muj.derius.api.DPlayer;
import dk.muj.derius.api.Skill;
import dk.muj.derius.entity.ability.DeriusAbility;
import dk.muj.derius.util.SkillUtil;

public class DoubleDrop extends DeriusAbility implements Ability
{
	private static DoubleDrop i = new DoubleDrop();
	public static DoubleDrop get() { return i; }
	
	// -------------------------------------------- //
	// DESCRIPTION
	// -------------------------------------------- //

	public DoubleDrop()
	{
		this.setName("Doubledrop");
		
		this.setDesc("gives doubledrop");
		
		this.setType(AbilityType.PASSIVE);
	}
		
	// -------------------------------------------- //
	// SKILL & ID
	// -------------------------------------------- //
	
	@Override
	public String getId()
	{
		return "derius:woodcutting:doubledrop";
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
	public Object onActivate(DPlayer dplayer, Object other)
	{
		if ( ! (other instanceof Block)) return null;
		Block b = (Block) other;
		
		ItemStack inHand = dplayer.getPlayer().getItemInHand();
		Location loc = b.getLocation();
		
		if ( ! MUtil.isAxe(inHand)) return null;
		
		if (WoodcuttingSkill.getExpGain().containsKey(b.getType()) && SkillUtil.shouldDoubleDropOccur(dplayer.getLvl(getSkill()), 10))
		{
			for (ItemStack is : b.getDrops(inHand))
			{
				b.getWorld().dropItem(loc, is);
			}
		}
		
		return null;
	}

	@Override
	public void onDeactivate(DPlayer dplayer, Object other)
	{
		// There is nothing to deactivate, a passive ability gets only activated once on runtime
	}

	// -------------------------------------------- //
	// Level description
	// -------------------------------------------- //
	
	@Override
	public String getLvlDescriptionMsg(int lvl)
	{
		return "chance to double drop " + lvl/10.0 + "%";
	}
	
}
