/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.trees.item;

import monnef.jaffas.food.block.ItemBlockJaffas;
import monnef.jaffas.trees.common.Reference;
import net.minecraft.block.Block;

public class ItemBlockTrees extends ItemBlockJaffas {
    public ItemBlockTrees(Block block) {
        super(block);
        setupFromOldDefaultProperties();
    }

    @Override
    public String getDefaultModName() {
        return Reference.ModName;
    }

    @Override
    public int getDefaultSheetNumber() {
        return 2;
    }
}
