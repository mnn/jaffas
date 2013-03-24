package monnef.jaffas.xmas;

import monnef.core.base.ItemMonnefCore;
import net.minecraft.item.Item;

public class ItemXmas extends ItemMonnefCore {
    public ItemXmas(int id) {
        super(id);
        init();
    }

    public ItemXmas(int id, int textureIndex) {
        super(id);
        //setIconIndex(textureIndex);
        init();
    }

    private void init() {
        setCreativeTab(mod_jaffas_xmas.CreativeTab);
    }

    public String getTextureFile() {
        return mod_jaffas_xmas.textureFile;
    }

    @Override
    public Item setUnlocalizedName(String par1Str) {
        return super.setUnlocalizedName("jaffas.technic." + par1Str);
    }
}
