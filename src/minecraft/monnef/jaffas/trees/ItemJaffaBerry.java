package monnef.jaffas.trees;

import net.minecraft.item.Item;

public class ItemJaffaBerry extends Item {
    public ItemJaffaBerry(int id) {
        super(id);
        setCreativeTab(mod_jaffas_trees.CreativeTab);
    }

    public String getTextureFile() {
        return "/jaffas_02.png";
    }
}
