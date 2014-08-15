/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.trees.item;

import monnef.jaffas.trees.JaffasTrees;
import monnef.jaffas.trees.block.BlockFruitSapling;
import net.minecraft.block.Block;

public class ItemBlockFruitSapling extends ItemBlockTrees {
    public ItemBlockFruitSapling(Block block) {
        super(block);
        BlockFruitSapling myBlock = (BlockFruitSapling) block;
        subNames = JaffasTrees.constructSubNames(JaffasTrees.saplingTitles, myBlock.serialNumber, myBlock.getSubCount());
    }
}
