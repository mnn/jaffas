/*
 * Copyright (c) 2013 monnef.
 */

package monnef.jaffas.trees.item;

import monnef.jaffas.trees.JaffasTrees;
import monnef.jaffas.trees.block.BlockFruitSapling;
import net.minecraft.block.Block;

public class ItemBlockFruitSapling extends ItemBlockTrees {
    public ItemBlockFruitSapling(int par1) {
        super(par1);
        BlockFruitSapling myBlock = ((BlockFruitSapling) Block.blocksList[this.getBlockID()]);
        subNames = JaffasTrees.constructSubNames(JaffasTrees.saplingNames, myBlock.serialNumber, myBlock.getSubCount());
    }
}
