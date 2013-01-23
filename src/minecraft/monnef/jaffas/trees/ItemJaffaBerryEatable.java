package monnef.jaffas.trees;

import monnef.jaffas.food.item.ItemJaffaFood;

public class ItemJaffaBerryEatable extends ItemJaffaFood {
    public ItemJaffaBerryEatable(int id) {
        super(id);
        setCreativeTab(mod_jaffas_trees.CreativeTab);
    }

    public String getTextureFile() {
        return mod_jaffas_trees.textureFile;
    }
}
