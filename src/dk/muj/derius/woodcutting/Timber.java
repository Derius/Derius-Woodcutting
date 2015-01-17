package dk.muj.derius.woodcutting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.Mutable;
import dk.muj.derius.ability.Ability;
import dk.muj.derius.ability.AbilityType;
import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.req.ReqIsAtleastLevel;
import dk.muj.derius.skill.Skill;
import dk.muj.derius.util.BlockUtil;
import dk.muj.derius.woodcutting.entity.MConf;

public class Timber extends Ability
{	
	private static Timber i = new Timber();
	public static Timber get() { return i; }
	
	// -------------------------------------------- //
	// DESCRIPTION
	// -------------------------------------------- //
	
	public Timber()
	{
		this.setName("Timber");
		
		this.setDescription("Harvests a full tree.");
		
		this.setType(AbilityType.ACTIVE);
		
		this.addActivateRequirements(ReqIsAtleastLevel.get(MConf.get().getTimberMinLvl()));
		
	}
	
	// -------------------------------------------- //
	// STATIC
	// -------------------------------------------- //
	
	public final static Set<Material> timberBlocks = MUtil.set(
			Material.LOG, Material.LOG_2, Material.LEAVES, Material.LEAVES_2
			);
	
	// -------------------------------------------- //
	// SKILL & ID
	// -------------------------------------------- //
	
	@Override
	public int getId()
	{
		return MConf.get().getTimberId();
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
	public Optional<Object> onActivate(MPlayer p, Object other)
	{
		if( ! p.isPlayer()) return Optional.empty();
		Block sourceBlock = (Block) other;
		if (sourceBlock == null) return Optional.empty();
		
		// Tree handling
		Set<Block> tree = tree(sourceBlock);
		if (tree == null) return Optional.empty();
		
		int logs = logCounter(tree);
		if (logs >= MConf.get().getLogSoftCap() + p.getLvl(getSkill()) / 10) return Optional.empty();
		
		tree.stream().forEach(Block::breakNaturally);
		
		
		// Inform surrounding players?
		if (MConf.get().getInformSurroundingPlayers())
		{
			informSurroundingPlayers();
		}
		
		// Drop extra items
		if (MConf.get().getDropExtraItems())
		{
			List<ItemStack> is = new ArrayList<ItemStack>();
			// Planks
			int numPlanks = logs / 10 + random();
			is.add(new ItemStack(Material.WOOD, numPlanks));
			
			// Sticks
			int numSticks = logs / 20 + random();
			is.add(new ItemStack(Material.STICK, numSticks));
			
			// Apples
			int numApples = logs / 50 + random();
			is.add(new ItemStack(Material.APPLE, numApples));
			
			// Saplings
			int numSaplings = logs / 50 + random();
			is.add(new ItemStack(Material.SAPLING, numSaplings));
			
			for (ItemStack itemStack : is)
			{
				sourceBlock.getWorld().dropItem(sourceBlock.getLocation(), itemStack);
			}
			
		}
		
		// Horribly unnecessary lore added, makes the player feel good
		Player player = p.getPlayer();
		ItemStack inHand = player.getItemInHand();
		if(inHand == null || inHand.getType() == Material.AIR) return Optional.empty();
		ItemMeta meta = inHand.getItemMeta();
		List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>(1);
		lore.add(Txt.parse("<lime>Derius Ability Tool"));
		
		meta.setLore(lore);
		
		return Optional.of(inHand);
	}

	@Override
	public void onDeactivate(MPlayer p, Optional<Object> other)
	{
		if( ! other.isPresent()) return;
		ItemStack hand = (ItemStack) other.get();

		ItemMeta meta = hand.getItemMeta();
		List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>(1);
		lore.remove(Txt.parse("<lime>Derius Ability Tool"));
		
		meta.setLore(lore);
	}
	
	// -------------------------------------------- //
	// PRIVATE
	// -------------------------------------------- //
	
	private Set<Block> tree(Block source)
	{
		Set<Block> ret = new HashSet<Block>();
		ret.add(source);
		
		Mutable<Boolean> someLeft = new Mutable<Boolean>(true);
		
		while (someLeft.get())
		{
			ret.stream().forEach(b -> someLeft.set(ret.addAll(
					BlockUtil.getSurroundingBlocksWith(b, 
							MUtil.list(BlockFace.UP, BlockFace.EAST, BlockFace.WEST, BlockFace.NORTH, BlockFace.SOUTH))
					.stream().filter(timberBlocks::contains).collect(Collectors.toList()))));
		}
		
		return ret;
	}
	
	private int logCounter(Collection<Block> tree)
	{
		int logCounter = 0;
		for (Block bs : tree)
		{
			if (bs.getType() == Material.LOG || bs.getType() == Material.LOG_2)
			{
				logCounter++;
			}
		}
		return logCounter;
	}
	
	private int random()
	{
		return (int)((Math.random()*10 + 1));
	}
	
	private void informSurroundingPlayers()
	{
		// TODO Auto-generated method stub
		
	}
	
	// -------------------------------------------- //
	// Level description
	// -------------------------------------------- //
	
	@Override
	public String getLvlDescription(int lvl)
	{
		return "Lasts "+this.getTicksLast(lvl)/20 + " seconds";
	}
}
