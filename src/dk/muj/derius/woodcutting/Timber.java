package dk.muj.derius.woodcutting;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.TitleUtil;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.api.Ability;
import dk.muj.derius.api.DPlayer;
import dk.muj.derius.api.Skill;
import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.entity.ability.DeriusAbility;
import dk.muj.derius.lib.ItemUtil;
import dk.muj.derius.req.ReqIsAtleastLevel;

public class Timber extends DeriusAbility implements Ability
{	
	private static Timber i = new Timber();
	public static Timber get() { return i; }
	
	// -------------------------------------------- //
	// DESCRIPTION
	// -------------------------------------------- //
	
	public Timber()
	{
		this.setName("Timber");
		
		this.setDesc("Harvests a full tree.");
		
		this.setTicksCooldown(5); // normally at 2*60*20
		
		this.setType(AbilityType.ACTIVE);
		
		this.addActivateRequirements(ReqIsAtleastLevel.get(WoodcuttingSkill.getTimberMinLvl()));
		
	}
	
	// -------------------------------------------- //
	// STATIC
	// -------------------------------------------- //
	
	public final static Set<Material> TIMBER_BLOCKS = MUtil.set(
			Material.LOG, Material.LOG_2, Material.LEAVES, Material.LEAVES_2
			);
	
	public final static Set<BlockFace> TIMBER_FACES = MUtil.set(
            BlockFace.UP, BlockFace.DOWN, BlockFace.EAST, BlockFace.WEST, BlockFace.NORTH, BlockFace.SOUTH);
	
	// -------------------------------------------- //
	// SKILL & ID
	// -------------------------------------------- //
	
	@Override
	public String getId()
	{
		return "derius:woodcutting:timber";
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
		if( ! (other instanceof Block)) return null;
		
		Block sourceBlock = (Block) other;
		
		// Tree handling
		Set<Block> tree = TreeUtil.tree(sourceBlock, 3);
		if (tree == null) return null;
		
		// Logs + leaves check
		int logs = counter(tree, Material.LOG, Material.LOG_2);
		int leaves = counter(tree, Material.LEAVES, Material.LEAVES_2);
		
		// Debug message
		dplayer.msg(Txt.parse("You just wanted cut down %s logs and %s leaves.", logs, leaves));
		
		if (logs >= WoodcuttingSkill.getLogSoftCap() + dplayer.getLvl(getSkill()) / 10 || logs >= WoodcuttingSkill.getLogHardCap()) 
		{
			dplayer.sendMessage(Txt.parse("<b>You are not strong enough to cut down this tree."));
			return null;
		}
		else if (leaves >= WoodcuttingSkill.getLeaveSoftCap() + dplayer.getLvl(getSkill()) / 10 || leaves >= WoodcuttingSkill.getLeaveHardCap()) 
		{
			dplayer.sendMessage(Txt.parse("<b>You are not strong enough to cut down this tree."));
			return null;
		}
		
		// Debug message
		dplayer.msg(Txt.parse("You just have cut down %s logs and %s leaves.", logs, leaves));
		
		// Cut down the tree
		tree.stream().forEach(Block::breakNaturally);
		
		// Take away some damage of the item in hand
		applyDamageToTool(dplayer, logs);
		
		// Inform surrounding players
		if (WoodcuttingSkill.getInformSurroundingPlayers())
		{
			informSurroundingPlayers(dplayer);
		}
		
		// Drop extra items
		if (WoodcuttingSkill.getDropExtraItems())
		{
			dropExtraItems(logs, sourceBlock.getLocation());
		}
		
		return null;
	}

	@Override
	public void onDeactivate(DPlayer dplayer, Object other)
	{
		// Do nothing
	}
	
	// -------------------------------------------- //
	// PRIVATE
	// -------------------------------------------- //
	
	private int counter(Collection<Block> tree, Material... materials)
	{
		int counter = 0;
		for (Block block : tree)
		{
			for (Material material : materials)
			{
				if (block.getType() == material) counter++;
			}
		}
		return counter;
	}
	

	private void applyDamageToTool(DPlayer dplayer, int logs)
	{
		Player player = dplayer.getPlayer();
		if (ItemUtil.applyDamage(player.getItemInHand(), (short)logs))
		{
			double health = player.getHealth();
			if (health <= 1) return;
			
			player.damage(Math.random() * WoodcuttingSkill.getSplinterDamageMultiplicator());
		}
	}
	
	private void informSurroundingPlayers(DPlayer dplayer)
	{
		@SuppressWarnings("unchecked")
		List<Player> players = (List<Player>) MUtil.getOnlinePlayers();
		PS ps1 = PS.valueOf(dplayer.getPlayer().getLocation());
		
		for (Player p : players)
		{
			if (MPlayer.get(p.getUniqueId()) == dplayer) return;
			
			PS ps2 = PS.valueOf(p.getLocation());
			
			double distance = PS.locationDistance(ps1, ps2);
			if (distance >= WoodcuttingSkill.getTimberDistance()) continue;
			
			TitleUtil.sendTitle(p, 20, 60, 20, Txt.parse("<i>TIMBER!"), "");
		}
		
	}

	private void dropExtraItems(int logs, Location loc)
	{
		int num = logs / 100;
		
		// Planks
		int numPlanks =  (int) (Math.random() * num * WoodcuttingSkill.getChancePerPlank());
		drop(loc, Material.WOOD, numPlanks = oneZero(numPlanks));
		
		// Sticks
		int numSticks = (int) (Math.random() * num * WoodcuttingSkill.getChancePerSticks());
		drop(loc, Material.WOOD, numSticks = oneZero(numSticks));
		
		// Apples
		int numApples = (int) (Math.random() * num * WoodcuttingSkill.getChancePerApples());
		drop(loc, Material.WOOD, numApples = oneZero(numApples));
		
	}
	
	private int oneZero(int num)
	{
		if (num == 1) return (int)Math.random();
		return num;
	}
	
	private void drop(Location loc, Material material, int amount)
	{
		if (amount == 0) return;

		loc.getBlock().getWorld().dropItem(loc, new ItemStack(material, amount));
	}
    
	// -------------------------------------------- //
	// Level description
	// -------------------------------------------- //
	
	@Override
	public String getLvlDescriptionMsg(int lvl)
	{
		return "Lasts "+this.getDuration(lvl)/20 + " seconds";
	}

}
