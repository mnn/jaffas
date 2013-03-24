package monnef.jaffas.technic.item;

import monnef.core.base.ItemMonnefCore;
import monnef.jaffas.technic.mod_jaffas_technic;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemTechnic extends ItemMonnefCore {
    public ItemTechnic(int id, int textureIndex) {
        super(id);
        //setIconIndex(textureIndex);
        setCreativeTab(CreativeTabs.tabMaterials);
        setCreativeTab(mod_jaffas_technic.CreativeTab);
    }

    /*
    @Override
    public String getTextureFile() {
        return mod_jaffas_technic.textureFile;
    }
    */
}
