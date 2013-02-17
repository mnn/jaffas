package monnef.jaffas.food.item;


import monnef.jaffas.food.mod_jaffas;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ItemJaffaBase extends Item implements IItemJaffa {
    protected int textureFileIndex;
    private String info = null;

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

    @Override
    public void setInfo(String text) {
        this.info = text;
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        if (info != null) par3List.add(info);
    }
}
