package monnef.jaffas.power.item;

import monnef.jaffas.power.jaffasPower;
import net.minecraft.item.Item;

public class ItemPower extends Item {
    public ItemPower(int id, int textureIndex) {
        super(id);
        //setIconIndex(textureIndex);
        setCreativeTab(jaffasPower.CreativeTab);
    }

    public String getTextureFile() {
        return jaffasPower.textureFile;
    }
}
