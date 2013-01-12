package monnef.jaffas.xmas;

import monnef.jaffas.food.items.ItemJaffaFood;

public class ItemXmasFood extends ItemJaffaFood {
    public ItemXmasFood(int id) {
        super(id);
        this.setCreativeTab(mod_jaffas_xmas.CreativeTab);
    }

    @Override
    public String getTextureFile() {
        return "/jaffas_04.png";
    }
}
