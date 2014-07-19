/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.trees.block;

import monnef.jaffas.food.block.BlockLeavesBaseJaffas;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class BlockFruitLeavesDummy extends BlockLeavesBaseJaffas {
    public BlockFruitLeavesDummy(int texture, Material material, boolean graphicsLevel) {
        super(texture, material, graphicsLevel);
    }

    public BlockFruitLeavesDummy() {
        this(0, Material.leaves, false);
    }

    @Override
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
        Block block = world.getBlock(x, y, z);
        if (block instanceof BlockFruitLeaves) {
            BlockFruitLeaves leaves = (BlockFruitLeaves) block;
            return leaves.getFruitIcon(world.getBlockMetadata(x, y, z));
        } else {
            return null;
        }
    }
}
