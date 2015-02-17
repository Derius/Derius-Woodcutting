package dk.muj.derius.woodcutting;
 
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;

import com.massivecraft.massivecore.util.MUtil;

import dk.muj.derius.lib.BlockUtil;
import dk.muj.derius.lib.Mutable;
 
public final class TreeUtil
{
        // -------------------------------------------- //
        // CONSTANTS
        // -------------------------------------------- //
       
        public final static Set<BlockFace> TIMBER_FACES = MUtil.set(
                        BlockFace.UP, BlockFace.EAST, BlockFace.WEST, BlockFace.NORTH, BlockFace.SOUTH);
       
        // -------------------------------------------- //
        // CONSTRUCTOR (FORBIDDEN)
        // -------------------------------------------- //
       
        private TreeUtil()
        {
               
        }
       
        // -------------------------------------------- //
        // GET TREE
        // -------------------------------------------- //
       
        public static Set<Block> tree(final Block source, int radius)
        {
                Set<Block> ret = new HashSet<>();
                ret.add(source);
               
                Mutable<Boolean> someLeft = new Mutable<>(true);
               
                Set<Block> latest = ret;
               
                while (someLeft.get())
                {
                        someLeft.set(false);
                        Set<Block> add = new HashSet<Block>();
                        latest.stream()
                        .forEach( (Block block) ->
                        add.addAll(BlockUtil.getSurroundingBlocksWith(block, TIMBER_FACES)
                                        .stream()
                                        .filter(b -> matches(source.getState(), b.getState()))
                                        .filter(b -> isLocationOk(source.getState(), b.getState(), radius))
                                        .collect(Collectors.toList()))
                        );
                        latest = add;
                        if (ret.addAll(add)) someLeft.set(true);
                }
               
                return ret;
        }
       
        // -------------------------------------------- //
        // MATCHES
        // -------------------------------------------- //
       
        @SuppressWarnings("deprecation")
        public static boolean matches(BlockState source, BlockState compared)
        {
                final Material srcType = source.getType();
                final Material compType = compared.getType();
               
                int compData = compared.getData().getData();
                final int srcData = source.getData().getData();
               
                if (compData >= 8) compData -= 8;
               
                /*Bukkit.broadcastMessage("srcId="+srcId + " srcDat=" + srcData + " compId="+compId + " compDat=" + compData);*/
               
                // Oak, Birch & Spruce
                if (srcType == Material.LOG)
                {
                        // If not a leave or log
                        if (compType != Material.LEAVES && compType != Material.LOG) return false;
                       
                        return srcData == compData;
                }
                // Dark oak & acacia
                else if (srcType == Material.LOG_2)
                {
                        //Acacia
                        if (srcData == 0) return compType == Material.LEAVES_2;
                        if (srcData == 1) return compType == Material.LEAVES && compData == 0;
                }
               
                return false;
        }
       
        // -------------------------------------------- //
        // IS LOCATION OK
        // -------------------------------------------- //
       
        @SuppressWarnings("deprecation")
		public static boolean isLocationOk(BlockState source, BlockState compared, int radius)
        {
                if (isLog(compared) && compared.getData().getData() <= 4) radius = 0;
                if (Math.abs(source.getX()-compared.getX()) <= radius && Math.abs(source.getZ()-compared.getZ()) <= radius) return true;
               
                return false;
        }
       
        // -------------------------------------------- //
        // TYPE
        // -------------------------------------------- //
 
        public static boolean isLog(BlockState compared)
        {
                return compared.getType() == Material.LOG || compared.getType() == Material.LOG_2;
        }
}
