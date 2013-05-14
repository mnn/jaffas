/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.xmas.item;

import monnef.jaffas.food.item.ItemJaffaBase;
import monnef.jaffas.xmas.JaffasXmas;
import monnef.jaffas.xmas.common.Reference;

public class ItemXmas extends ItemJaffaBase {
    public ItemXmas(int id) {
        super(id);
        init();
    }

    public ItemXmas(int id, int textureIndex) {
        super(id);
        setCustomIconIndex(textureIndex);
        init();
    }

    private void init() {
        setCreativeTab(JaffasXmas.instance.creativeTab);
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
