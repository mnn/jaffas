package monnef.jaffas.xmas.item;

import monnef.jaffas.food.item.ItemJaffaFood;
import monnef.jaffas.xmas.JaffasXmas;

public class ItemXmasFood extends ItemJaffaFood {
    public ItemXmasFood(int id) {
        super(id);
        this.setCreativeTab(JaffasXmas.CreativeTab);
    }

    /*
    @Override
    public String getTextureFile() {
        return "/jaffas_04.png";
    }
    */
}
