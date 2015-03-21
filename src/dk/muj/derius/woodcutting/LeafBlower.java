package dk.muj.derius.woodcutting;

import java.util.Optional;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.massivecraft.massivecore.Couple;
import com.massivecraft.massivecore.util.MUtil;

import dk.muj.derius.api.ability.AbilityAbstract;
import dk.muj.derius.api.player.DPlayer;
import dk.muj.derius.api.req.ReqIsAtleastLevel;
import dk.muj.derius.api.skill.Skill;
import dk.muj.derius.api.util.LevelUtil;
import dk.muj.derius.lib.ItemUtil;

public class LeafBlower extends AbilityAbstract<Block>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static LeafBlower i = new LeafBlower();
	public static LeafBlower get() { return i; }
	private LeafBlower()
	{
		this.setName("Leaf blower");
		this.setDesc("Destroy leaves instantly");
		
		this.setType(AbilityType.PASSIVE);
		this.setCooldownMillis(-1);
		
		this.addActivateRequirements(ReqIsAtleastLevel.get( () -> WoodcuttingSkill.getLeafBlowerMinLevel() ));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String getId()
	{
		return "derius:woodcutting:leafblower";
	}

	@Override
	public Skill getSkill()
	{
		return WoodcuttingSkill.get();
	}

	@Override
	public Optional<String> getLvlDescriptionMsg(int level)
	{
		if (level < WoodcuttingSkill.getLeafBlowerMinLevel()) return Optional.empty();
		double damage = LevelUtil.getLevelSettingFloat(WoodcuttingSkill.getLeafBlowerToolDamage(), level).orElse(1.0);
		return Optional.of(String.format("<i>Tool takes the damage for <h>%.1f <i>usages.", damage));
	}

	@Override
	public Object onActivate(DPlayer dplayer, Block block)
	{
		Player player = dplayer.getPlayer();
		ItemStack item = player.getItemInHand();
		
		double damage = LevelUtil.getLevelSettingFloat(WoodcuttingSkill.getLeafBlowerToolDamage(), dplayer.getLvl(this.getSkill())).orElse(1.0);
		
		// Apparently the item doesn't break. Solution is to disallow breaking the block so it is done normally.
		Couple<ItemStack, Boolean> result = ItemUtil.applyDamage(item, (short) MUtil.probabilityRound(damage));
		player.setItemInHand(result.getFirst());
		player.updateInventory();
		block.breakNaturally(item);
		
		return result; // Unused
	}

	@Override public void onDeactivate(DPlayer dplayer, Object other){ }

}
