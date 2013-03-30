package monnef.jaffas.trees.item;

import monnef.jaffas.trees.jaffasTrees;
import net.minecraft.item.Item;

public class ItemTrees extends Item {
    public ItemTrees(int v) {
        super(v);
        setCreativeTab(jaffasTrees.CreativeTab);
    }

    public String getTextureFile() {
        return jaffasTrees.textureFile;
    }
}
