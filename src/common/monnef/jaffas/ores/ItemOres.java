package monnef.jaffas.ores;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Item;

public class ItemOres extends Item {
    public ItemOres(int id, int textureIndex) {
        super(id);
        setIconIndex(textureIndex);
        setCreativeTab(CreativeTabs.tabMaterials);
    }

    public String getTextureFile() {
        return mod_jaffas_ores.textureFile;
    }
}
