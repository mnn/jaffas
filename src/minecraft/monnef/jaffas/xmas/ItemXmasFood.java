package monnef.jaffas.xmas;

import monnef.jaffas.food.item.ItemJaffaFood;

public class ItemXmasFood extends ItemJaffaFood {
    public ItemXmasFood(int id) {
        super(id);
        this.setCreativeTab(jaffasXmas.CreativeTab);
    }

    /*
    @Override
    public String getTextureFile() {
        return "/jaffas_04.png";
    }
    */
}
