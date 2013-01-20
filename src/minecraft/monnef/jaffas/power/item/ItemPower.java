package monnef.jaffas.power.item;

import monnef.jaffas.power.mod_jaffas_power;
import net.minecraft.item.Item;

public class ItemPower extends Item {
    public ItemPower(int id, int textureIndex) {
        super(id);
        setIconIndex(textureIndex);
        setCreativeTab(mod_jaffas_power.CreativeTab);
    }

    public String getTextureFile() {
        return mod_jaffas_power.textureFile;
    }
}
