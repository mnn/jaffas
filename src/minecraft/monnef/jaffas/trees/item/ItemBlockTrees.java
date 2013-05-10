/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.trees.item;

import monnef.jaffas.food.block.ItemBlockJaffas;
import monnef.jaffas.trees.common.Reference;
import net.minecraft.block.Block;

public class ItemBlockTrees extends ItemBlockJaffas {
    public ItemBlockTrees(int id, int blockId) {
        super(id, blockId);
    }

    public ItemBlockTrees(int par1) {
        super(par1);
    }

    @Override
    public String getModName() {
        return Reference.ModName;
    }

    @Override
    public int getDefaultSheetNumber() {
        return 2;
    }
}
