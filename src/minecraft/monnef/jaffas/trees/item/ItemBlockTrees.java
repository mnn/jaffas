/*
 * Copyright (c) 2013 monnef.
 */

package monnef.jaffas.trees.item;

import monnef.jaffas.food.block.ItemBlockJaffas;
import monnef.jaffas.trees.common.Reference;

public class ItemBlockTrees extends ItemBlockJaffas {
    public ItemBlockTrees(int id, int blockId) {
        super(id, blockId);
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
