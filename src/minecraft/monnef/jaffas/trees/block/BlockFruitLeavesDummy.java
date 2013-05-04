/*
 * Copyright (c) 2013 monnef.
 */

package monnef.jaffas.trees.block;

import monnef.jaffas.food.block.BlockLeavesBaseJaffas;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;

public class BlockFruitLeavesDummy extends BlockLeavesBaseJaffas {
    public BlockFruitLeavesDummy(int id, int texture, Material material, boolean graphicsLevel) {
        super(id, texture, material, graphicsLevel);
    }

    public BlockFruitLeavesDummy(int id) {
        this(id, 0, Material.leaves, false);
    }

    @Override
    public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, int side) {
        int blockId = world.getBlockId(x, y, z);
        Block block = Block.blocksList[blockId];
        if (block instanceof BlockFruitLeaves) {
            BlockFruitLeaves leaves = (BlockFruitLeaves) block;
            return leaves.getFruitIcon(world.getBlockMetadata(x, y, z));
        } else {
            return null;
        }
    }
}
