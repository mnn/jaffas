/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.trees.item;

import monnef.jaffas.food.item.ItemJaffaFood;
import monnef.jaffas.trees.JaffasTrees;
import monnef.jaffas.trees.common.Reference;

public class ItemJaffaBerryEatable extends ItemJaffaFood {
    public ItemJaffaBerryEatable(int id) {
        super(id);
        setCreativeTab(JaffasTrees.instance.creativeTab);
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
