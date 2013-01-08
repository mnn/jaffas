package monnef.jaffas.trees;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemJaffaFruit extends Item {
    public ItemJaffaFruit(int v) {
        super(v);
        maxStackSize = 64;
        this.setCreativeTab(CreativeTabs.tabMaterials);
        setCreativeTab(mod_jaffas_trees.CreativeTab);
    }

    public String getTextureFile(){
        return "/jaffas_02.png";
    }
}
