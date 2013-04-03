package monnef.jaffas.trees.item;

import monnef.jaffas.food.item.ItemJaffaBase;
import monnef.jaffas.trees.common.Reference;
import monnef.jaffas.trees.jaffasTrees;

public class ItemTrees extends ItemJaffaBase {
    public ItemTrees(int v) {
        super(v);
        setCreativeTab(jaffasTrees.CreativeTab);
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
