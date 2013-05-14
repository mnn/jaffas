/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.trees.item;

import monnef.jaffas.food.item.ItemJaffaBase;
import monnef.jaffas.trees.JaffasTrees;
import monnef.jaffas.trees.common.Reference;

public class ItemTrees extends ItemJaffaBase {
    public ItemTrees(int v) {
        super(v);
        setCreativeTab(JaffasTrees.instance.CreativeTab);
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
