/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.item;

import monnef.jaffas.food.item.ItemJaffaBase;
import monnef.jaffas.power.JaffasPower;
import monnef.jaffas.power.common.Reference;

public class ItemPower extends ItemJaffaBase {
    public ItemPower(int id, int textureIndex) {
        super(id, textureIndex);
        setCreativeTab(JaffasPower.CreativeTab);
    }

    @Override
    public String getModName() {
        return Reference.ModName;
    }

    @Override
    public int getDefaultSheetNumber() {
        return 5;
    }
}
