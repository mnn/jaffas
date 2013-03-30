package monnef.jaffas.trees.item;

import monnef.jaffas.trees.jaffasTrees;
import net.minecraft.item.Item;

public class ItemJaffaBerry extends Item {
    public ItemJaffaBerry(int id) {
        super(id);
        setCreativeTab(jaffasTrees.CreativeTab);
    }

    public String getTextureFile() {
        return jaffasTrees.textureFile;
    }
}
