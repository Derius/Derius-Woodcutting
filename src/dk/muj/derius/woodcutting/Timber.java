package dk.muj.derius.woodcutting;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang.mutable.MutableBoolean;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.massivecraft.massivecore.mixin.Mixin;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.util.IntervalUtil;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.TimeUnit;

import dk.muj.derius.api.ability.AbilityAbstract;
import dk.muj.derius.api.player.DPlayer;
import dk.muj.derius.api.req.ReqIsAtleastLevel;
import dk.muj.derius.api.req.ReqIsntExhausted;
import dk.muj.derius.api.skill.Skill;
import dk.muj.derius.api.util.AbilityUtil;
import dk.muj.derius.lib.BlockUtil;
import dk.muj.derius.lib.ItemUtil;

public class Timber extends AbilityAbstract
{	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static Timber i = new Timber();
	public static Timber get() { return i; }

	public Timber()
	{
		this.setName("Timber");
		
		this.setDesc("Harvests a full tree.");
		
		this.setCooldownMillis((int) (5*TimeUnit.MILLIS_PER_SECOND)); // normally at 2*60*20
		
		this.setType(AbilityType.PASSIVE);
		
		this.addActivateRequirements(ReqIsAtleastLevel.get( () -> WoodcuttingSkill.getTimberMinLvl() ));
		this.addActivateRequirements(ReqIsntExhausted.get());
	}
	
	// -------------------------------------------- //
	// STATIC
	// -------------------------------------------- //

	public final static Set<BlockFace> TIMBER_FACES = MUtil.set(
            BlockFace.UP,
            BlockFace.EAST,
            BlockFace.WEST,
            BlockFace.NORTH,
            BlockFace.SOUTH);
	
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
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public Object onActivate(DPlayer dplayer, Object other)
	{
		Block sourceBlock = (Block) other;
		BlockState originalState = sourceBlock.getState();
		
		// Tree handling
		Set<Block> tree = getTree(sourceBlock);
		if (tree == null || tree.isEmpty()) return AbilityUtil.CANCEL;
		
		// Logs + leaves check
		int logs = count(tree, Material.LOG, Material.LOG_2);
		int leaves = count(tree, Material.LEAVES, Material.LEAVES_2);
		
		// Debug message
		//dplayer.msg(Txt.parse("You just wanted cut down %s logs and %s leaves.", logs, leaves));

		// Debug message
		//dplayer.msg(Txt.parse("You just have cut down %s logs and %s leaves.", logs, leaves));
		
		// Cut down the tree
		tree.forEach(Block::breakNaturally);
		
		// Take away some damage of the item in hand
		applyDamageToTool(dplayer, logs);
		
		// Inform surrounding players
		informSurroundingPlayers(dplayer);
		
		// Drop extra items
		if (WoodcuttingSkill.shouldDropExtraItems())
		{
			dropExtraItems(logs, leaves, originalState);
		}
		
		return tree;
	}

	@Override public void onDeactivate(DPlayer dplayer, Object other) { }
	
	@Override
	public Optional<String> getLvlDescriptionMsg(int level)
	{
		return Optional.empty();
	}
	
	// -------------------------------------------- //
	// GET TREE
	// -------------------------------------------- //
   
	public static Set<Block> getTree(final Block source)
	{
		// We create a return...
		Set<Block> ret = new HashSet<>();
		// ... which initially contains the source block.
		ret.add(source);
		
		// We calculate the maximum radius dependent on the wood type.
		final int radius = getRadius(source.getState());
		
		// This boolean determines if we should look any further.
		// it is set to false if an operation doesn't add anything to the return value.
		MutableBoolean someLeft = new MutableBoolean(true);
		
		// The latest added blocks, we use this to prevent looking through
		// the same blocks multiple times.
		// Initially it is just the source block.
		Set<Block> latest = ret;
		  
		while (someLeft.booleanValue())
		{
			// The blocks we are going to add next time we modify return.
			Set<Block> add = new HashSet<Block>();
			
			// For all the latest added blocks, we look through
			// the nearest ones to add to the three.
			latest.forEach( (Block block) ->
				add.addAll(BlockUtil.getSurroundingBlocksWith(block, TIMBER_FACES)
							.stream()
							// Of course it must be a log or leave.
							.filter(BlockUtil::isLogOrLeave)
							// The wood type must match the source.
							.filter(b -> BlockUtil.isSameWoodType(source.getState(), b.getState()))
							// And it may not be too far away.
							.filter(b -> isLocationOk(source.getState(), b.getState(), radius))
							.collect(Collectors.toSet()))
			);
			// So if true is returned, we modified the return...
			// and there might still be wood to find.
			someLeft.setValue(ret.addAll(add));
			// The latest added blocks are the ones we just added.
			latest = add;	
		}
		  
		return ret;
	}
	
	private static int getRadius(BlockState state)
	{
		if (BlockUtil.isOak(state)) return WoodcuttingSkill.getRadiusOak();
		if (BlockUtil.isSpruce(state)) return WoodcuttingSkill.getRadiusSpruce();
		if (BlockUtil.isBirch(state)) return WoodcuttingSkill.getRadiusBirch();
		if (BlockUtil.isJungle(state)) return WoodcuttingSkill.getRadiusJungle();
		if (BlockUtil.isAcacia(state)) return WoodcuttingSkill.getRadiusAcacia();
		if (BlockUtil.isDarkOak(state)) return WoodcuttingSkill.getRadiusDarkOak();
		
		return 0;
	}
	
	@SuppressWarnings("deprecation")
	private static boolean isLocationOk(BlockState source, BlockState compared, int radius)
	{
		// If it is a log and it is facing up/down...
		if (BlockUtil.isLog(compared) && compared.getData().getData() < 4)
		{
			// ... it's radius must be 0...
			radius = 0;
			// .. unless it is jungle then it is 1
			if (BlockUtil.isJungle(source))
			{
				radius = 1;
			}
		}
		
		// If distance on x-axis is too much, location isn't ok.
		if (Math.abs(source.getX()-compared.getX()) > radius) return false;
		
		// If distance on z-axis is too much, location isn't ok.
		if (Math.abs(source.getZ()-compared.getZ()) > radius) return false;
		
		// It passed all the checks.
		return true;
	}
   

	// -------------------------------------------- //
	// PRIVATE
	// -------------------------------------------- //

	private int count(Collection<Block> tree, Material... materials)
	{
		int ret = 0;
		for (Block block : tree)
		{
			for (Material material : materials)
			{
				if (block.getType() == material) ret++;
			}
		}
		return ret;
	}
	
	private void applyDamageToTool(DPlayer dplayer, double logs)
	{
		Player player = dplayer.getPlayer();
		double damage = logs * WoodcuttingSkill.getTimberToolDamageMultiplier();
		damage = MUtil.probabilityRound(damage);
		if (ItemUtil.applyDamage(player.getItemInHand(), (short) damage).getSecond())
		{
			player.damage(IntervalUtil.random(WoodcuttingSkill.getSplinterDamageMin(), WoodcuttingSkill.getSplinterDamageMax()));
		}
	}
	
	private void informSurroundingPlayers(DPlayer dplayer)
	{
		if ( ! WoodcuttingSkill.shouldInformSurroundingPlayers()) return;
		PS ps1 = Mixin.getSenderPs(dplayer);
		
		for (Player player : MUtil.getOnlinePlayers())
		{
			PS ps2 = PS.valueOf(player.getLocation());
			
			// We get the distance squared, because that is a much cheaper operation,
			// than getting the distance. The latter calls Math.sgrt
			double distance = PS.locationDistanceSquared(ps1, ps2);
			// To make it match we must get the timberDistance squared
			if (distance >= (WoodcuttingSkill.getTimberDistance()^2)) continue;
			
			Mixin.sendTitleMsg(player, 10, 40, 10, "<lime>TIMBER!", null);
		}
		
	}

	private void dropExtraItems(int logs, int leaves, BlockState source)
	{
		// Planks
		double planksPerLog = IntervalUtil.random(WoodcuttingSkill.getSticksPerLogMin(), WoodcuttingSkill.getSticksPerLogMax());
		int numPlanks = (int) MUtil.probabilityRound(logs * planksPerLog);
		source.getWorld().dropItem(source.getLocation(), logToPlank(source, numPlanks));
		
		// Sticks
		double sticksPerLog = IntervalUtil.random(WoodcuttingSkill.getSticksPerLogMin(), WoodcuttingSkill.getSticksPerLogMax());
		int numSticks = (int) MUtil.probabilityRound(logs * sticksPerLog);
		source.getWorld().dropItem(source.getLocation(), new ItemStack(Material.STICK, numSticks));
		
		// Apples
		if (BlockUtil.isOak(source))
		{
			double applesPerLeave = IntervalUtil.random(WoodcuttingSkill.getApplesPerLeaveMin(), WoodcuttingSkill.getApplesPerLeaveMax());
			int numApples = (int) (int) MUtil.probabilityRound(leaves * applesPerLeave);
			source.getWorld().dropItem(source.getLocation(), new ItemStack(Material.APPLE, numApples));
		}
		
	}

	private ItemStack logToPlank(BlockState log, int num)
	{
		ItemStack ret = new ItemStack(Material.WOOD, num);
		
		if (BlockUtil.isOak(log)) ret.setDurability( (short) 0);
		else if (BlockUtil.isSpruce(log)) ret.setDurability( (short) 1);
		else if (BlockUtil.isBirch(log)) ret.setDurability( (short) 2);
		else if (BlockUtil.isJungle(log)) ret.setDurability( (short) 3);
		else if (BlockUtil.isAcacia(log)) ret.setDurability( (short) 4);
		else if (BlockUtil.isDarkOak(log)) ret.setDurability( (short) 5);

		return ret;
	}

}
