package dk.muj.derius.woodcutting;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
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
import dk.muj.derius.woodcutting.entity.MConf;

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
		
		this.addActivateRequirements(ReqIsAtleastLevel.get(MConf.get().getTimberMinLvl()));
		
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
		if( ! (other instanceof Block))
		{
			return Optional.empty();
		}
		
		Block sourceBlock = (Block) other;
		
		// Tree handling
		Set<Block> tree = TreeUtil.tree(sourceBlock, 3);
		if (tree == null) return Optional.empty();
		
		// Logs + leaves check
		int logs = logCounter(tree);
		int leaves = leaveCounter(tree);
		dplayer.msg(Txt.parse("You just wanted cut down %s logs and %s leaves.", logs, leaves));
		if (logs >= MConf.get().getLogSoftCap() + dplayer.getLvl(getSkill()) / 10 || logs >= MConf.get().getLogHardCap()) 
		{
			dplayer.sendMessage(Txt.parse("<b>You are not strong enough to cut down this tree."));
			return Optional.empty();
		}
		else if (leaves >= MConf.get().getLeaveSoftCap() + dplayer.getLvl(getSkill()) / 10 || leaves >= MConf.get().getLeaveHardCap()) 
		{
			dplayer.sendMessage(Txt.parse("<b>You are not strong enough to cut down this tree."));
			return Optional.empty();
		}
		dplayer.msg(Txt.parse("You just have cut down %s logs and %s leaves.", logs, leaves));
		
		// Cut down the tree
		tree.stream().forEach(Block::breakNaturally);
		
		// Take away some damage of the item in hand
		applyDamageToTool(dplayer, logs);
		
		// Inform surrounding players
		if (MConf.get().getInformSurroundingPlayers())
		{
			informSurroundingPlayers(dplayer);
		}
		
		// Drop extra items
		if (MConf.get().getDropExtraItems())
		{
			dropExtraItems(logs, sourceBlock.getLocation());
		}
		
		return Optional.empty();
	}

	@Override
	public void onDeactivate(DPlayer dplayer, Object other)
	{
		// Do nothing
	}
	
	// -------------------------------------------- //
	// PRIVATE
	// -------------------------------------------- //
	
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
	
	// I am lazy, create code two times
	private int  leaveCounter(Collection<Block> tree)
	{
		int leaveCounter = 0;
		for (Block bs : tree)
		{
			if (bs.getType() == Material.LEAVES || bs.getType() == Material.LEAVES_2)
			{
				leaveCounter++;
			}
		}
		return leaveCounter;
	}
	

	private void applyDamageToTool(DPlayer dplayer, int logs)
	{
		Player player = dplayer.getPlayer();
		if (ItemUtil.applyDamage(player.getItemInHand(), (short)logs))
		{
			double health = player.getHealth();
			if (health <= 1) return;
			
			player.damage(Math.random() * MConf.get().getSplinterDamageMultiplicator());
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
			if (distance >= MConf.get().getTimberDistance()) continue;
			
			TitleUtil.sendTitle(p, 20, 60, 20, Txt.parse("<i>TIMBER!"), "");
		}
		
	}

	private void dropExtraItems(int logs, Location loc)
	{
		int num = logs / 100;
		
		// Planks
		int numPlanks =  (int) (Math.random() * num * MConf.get().getChancePerPlanks());
		drop(loc, Material.WOOD, numPlanks = oneZero(numPlanks));
		
		// Sticks
		int numSticks = (int) (Math.random() * num * MConf.get().getChancePerSticks());
		drop(loc, Material.WOOD, numSticks = oneZero(numSticks));
		
		// Apples
		int numApples = (int) (Math.random() * num * MConf.get().getChancePerApples());
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
