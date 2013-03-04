package monnef.jaffas.trees.item;

import monnef.jaffas.food.item.ItemJaffaFood;
import monnef.jaffas.trees.mod_jaffas_trees;

public class ItemJaffaBerryEatable extends ItemJaffaFood {
    public ItemJaffaBerryEatable(int id) {
        super(id);
        setCreativeTab(mod_jaffas_trees.CreativeTab);
    }

    public String getTextureFile() {
        return mod_jaffas_trees.textureFile;
    }
}
