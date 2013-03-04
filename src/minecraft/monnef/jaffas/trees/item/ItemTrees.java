package monnef.jaffas.trees.item;

import monnef.jaffas.trees.mod_jaffas_trees;
import net.minecraft.item.Item;

public class ItemTrees extends Item {
    public ItemTrees(int v) {
        super(v);
        setCreativeTab(mod_jaffas_trees.CreativeTab);
    }

    public String getTextureFile() {
        return mod_jaffas_trees.textureFile;
    }
}
