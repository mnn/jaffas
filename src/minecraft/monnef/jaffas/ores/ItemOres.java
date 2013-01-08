package monnef.jaffas.ores;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemOres extends Item {
    public ItemOres(int id, int textureIndex) {
        super(id);
        setIconIndex(textureIndex);
        setCreativeTab(CreativeTabs.tabMaterials);
        setCreativeTab(mod_jaffas_ores.CreativeTab);
    }

    public String getTextureFile() {
        return mod_jaffas_ores.textureFile;
    }
}
