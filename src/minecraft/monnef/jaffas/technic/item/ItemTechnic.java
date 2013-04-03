package monnef.jaffas.technic.item;

import monnef.core.base.ItemMonnefCore;
import monnef.jaffas.technic.JaffasTechnic;
import monnef.jaffas.technic.Reference;
import net.minecraft.creativetab.CreativeTabs;

public class ItemTechnic extends ItemMonnefCore {
    public ItemTechnic(int id, int textureIndex) {
        super(id);
        //setIconIndex(textureIndex);
        setCreativeTab(CreativeTabs.tabMaterials);
        setCreativeTab(JaffasTechnic.CreativeTab);
    }

    @Override
    public String getModName() {
        return Reference.ModName;
    }

    @Override
    public int getDefaultSheetNumber() {
        return 3;
    }

    /*
    @Override
    public String getTextureFile() {
        return JaffasTechnic.textureFile;
    }
    */
}
