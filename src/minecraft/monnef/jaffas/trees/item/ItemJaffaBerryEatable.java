package monnef.jaffas.trees.item;

import monnef.jaffas.food.item.ItemJaffaFood;
import monnef.jaffas.trees.common.Reference;
import monnef.jaffas.trees.jaffasTrees;

public class ItemJaffaBerryEatable extends ItemJaffaFood {
    public ItemJaffaBerryEatable(int id) {
        super(id);
        // TODO
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
