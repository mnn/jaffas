package monnef.jaffas.technic.item;

import monnef.core.base.ItemMonnefCore;
import monnef.jaffas.technic.jaffasTechnic;
import net.minecraft.creativetab.CreativeTabs;

public class ItemTechnic extends ItemMonnefCore {
    public ItemTechnic(int id, int textureIndex) {
        super(id);
        //setIconIndex(textureIndex);
        setCreativeTab(CreativeTabs.tabMaterials);
        setCreativeTab(jaffasTechnic.CreativeTab);
    }

    /*
    @Override
    public String getTextureFile() {
        return jaffasTechnic.textureFile;
    }
    */
}
