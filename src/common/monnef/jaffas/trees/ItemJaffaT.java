package monnef.jaffas.trees;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Item;

public class ItemJaffaT extends Item {
    public ItemJaffaT(int v) {
        super(v);
        maxStackSize = 64;
        this.setCreativeTab(CreativeTabs.tabMaterials);
    }

    public String getTextureFile(){
        return "/jaffas_02.png";
    }
}
