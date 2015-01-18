package dk.muj.derius.woodcutting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.Mutable;
import dk.muj.derius.ability.Ability;
import dk.muj.derius.ability.AbilityType;
import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.req.ReqIsAtleastLevel;
import dk.muj.derius.skill.Skill;
import dk.muj.derius.util.BlockUtil;
import dk.muj.derius.util.ChatUtil;
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
	
	public final static Set<Material> TIMBER_BLOCKS = MUtil.set(
			Material.LOG, Material.LOG_2, Material.LEAVES, Material.LEAVES_2
			);
	
	public final static Set<BlockFace> TIMBER_FACES = MUtil.set(
            BlockFace.UP, BlockFace.EAST, BlockFace.WEST, BlockFace.NORTH, BlockFace.SOUTH);
	
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
	public Optional<Object> onActivate(MPlayer p, Object block)
	{
		if( ! p.isPlayer()) return Optional.empty();
		
		if(!(block instanceof Block))
			return Optional.empty();
		
		Block sourceBlock = (Block) block;
		
		// Tree handling
		Set<Block> tree = tree(sourceBlock);
		if (tree == null) return Optional.empty();
		Bukkit.broadcastMessage("Yo! tree got!");
		
		int logs = logCounter(tree);
		if (logs >= MConf.get().getLogSoftCap() + p.getLvl(getSkill()) / 10 || logs >= MConf.get().getLogHardCap()) return Optional.empty();
		
		tree.stream().forEach(Block::breakNaturally);
		
		
		// Inform surrounding players?
		if (MConf.get().getInformSurroundingPlayers())
		{
			informSurroundingPlayers(p);
		}
		
		Bukkit.broadcastMessage("Yo! informed!");
		
		// Drop extra items
		if (MConf.get().getDropExtraItems())
		{
			dropExtraItems(logs, sourceBlock);
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
    public static Set<Block> tree(final Block source)
    {
        Set<Block> ret = new HashSet<>();
        ret.add(source);
        
        // int data = source.getData();
        
        Mutable<Boolean> someLeft = new Mutable<>(true);
        
        while(someLeft.get())
        {
            someLeft.set(false);
            Set<Block> add = new HashSet<Block>();
            ret.stream().forEach( (Block block) -> 
            add.addAll(BlockUtil.getSurroundingBlocksWith(block, TIMBER_FACES)
                    .stream()
                    .filter(b -> TIMBER_BLOCKS.contains(b.getType()))
                    .collect(Collectors.toList()))
            );
            
            if (ret.addAll(add)) someLeft.set(true);
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
	
	private void informSurroundingPlayers(MPlayer mplayer)
	{
		@SuppressWarnings("unchecked")
		List<Player> players = (List<Player>) MUtil.getOnlinePlayers();
		PS ps1 = PS.valueOf(mplayer.getPlayer().getLocation());
		
		for (Player p : players)
		{
			PS ps2 = PS.valueOf(p.getLocation());
			
			double distance = PS.locationDistance(ps1, ps2);
			if (distance >= MConf.get().getTimberDistance()) continue;
			
			ChatUtil.sendTitle(p, Txt.parse("<i>TIMBER!"), 20, 60, 20);
		}
		
	}

	private void dropExtraItems(int logs, Block dropBlock)
	{
		List<ItemStack> is = new ArrayList<ItemStack>();
		int num = logs * 100;
		
		// Planks
		int numPlanks =  random((double) (num / MConf.get().getChancePerPlanks()));
		is.add(new ItemStack(Material.WOOD, numPlanks));
		
		// Sticks
		int numSticks = random((double) (num / MConf.get().getChancePerSticks()));
		is.add(new ItemStack(Material.STICK, numSticks));
		
		// Apples
		int numApples = random((double) (num / MConf.get().getChancePerApples()));
		is.add(new ItemStack(Material.APPLE, numApples));
		
		// Saplings
		int numSaplings = random( (double) (num / MConf.get().getChancePerSaplings()));
		is.add(new ItemStack(Material.SAPLING, numSaplings));
		
		for (ItemStack itemStack : is)
		{
			dropBlock.getWorld().dropItem(dropBlock.getLocation(), itemStack);
		}
	}
	

	private int random(double chance)
	{
		return (int)((Math.random() * (chance + MConf.get().getRandomModifier())));
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
