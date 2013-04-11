/*
 * Copyright (c) 2013 monnef.
 */

package monnef.jaffas.xmas.item;

import monnef.jaffas.food.item.ItemJaffaFood;
import monnef.jaffas.xmas.JaffasXmas;
import monnef.jaffas.xmas.common.Reference;

public class ItemXmasFood extends ItemJaffaFood {
    public ItemXmasFood(int id) {
        super(id);
        this.setCreativeTab(JaffasXmas.CreativeTab);
    }

    @Override
    public String getModName() {
        return Reference.ModName;
    }

    @Override
    public int getDefaultSheetNumber() {
        return 4;
    }
}
