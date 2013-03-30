package monnef.jaffas.trees.item;

import monnef.jaffas.food.item.ItemJaffaFood;
import monnef.jaffas.trees.jaffasTrees;

public class ItemJaffaBerryEatable extends ItemJaffaFood {
    public ItemJaffaBerryEatable(int id) {
        super(id);
        setCreativeTab(jaffasTrees.CreativeTab);
    }

    public String getTextureFile() {
        return jaffasTrees.textureFile;
    }
}
