package monnef.jaffas.food.item;


import monnef.jaffas.food.mod_jaffas;
import net.minecraft.item.Item;

public class ItemJaffaBase extends Item {
    protected int textureFileIndex;

    public ItemJaffaBase(int v) {
        super(v);
        maxStackSize = 64;
        //this.setCreativeTab(CreativeTabs.tabMaterials);
        this.setCreativeTab(mod_jaffas.CreativeTab);
    }

    @Override
    public String getTextureFile() {
        return mod_jaffas.textureFile[getTextureFileIndex()];
    }

    public int getTextureFileIndex() {
        return textureFileIndex;
    }

    public void setTextureFileIndex(int value) {
        textureFileIndex = value;
    }
}
