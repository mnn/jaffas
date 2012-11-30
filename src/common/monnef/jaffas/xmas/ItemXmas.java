package monnef.jaffas.xmas;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Item;

public class ItemXmas extends Item {
    public ItemXmas(int id, int textureIndex) {
        super(id);
        setIconIndex(textureIndex);
        setCreativeTab(CreativeTabs.tabMaterials);
        setCreativeTab(mod_jaffas_xmas.CreativeTab);
    }

    public String getTextureFile() {
        return mod_jaffas_xmas.textureFile;
    }
}
