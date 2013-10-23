/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.xmas.item;

import monnef.jaffas.food.item.ItemJaffaFood;
import monnef.jaffas.xmas.JaffasXmas;
import monnef.jaffas.xmas.common.Reference;

public class ItemXmasFood extends ItemJaffaFood {
    public ItemXmasFood(int id) {
        super(id);
        this.setCreativeTab(JaffasXmas.instance.creativeTab);
    }

    @Override
    public String getDefaultModName() {
        return Reference.ModName;
    }

    @Override
    public int getDefaultSheetNumber() {
        return 4;
    }
}
