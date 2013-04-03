package monnef.jaffas.power.item;

import monnef.jaffas.power.JaffasPower;
import net.minecraft.item.Item;

public class ItemPower extends Item {
    public ItemPower(int id, int textureIndex) {
        super(id);
        //setIconIndex(textureIndex);
        setCreativeTab(JaffasPower.CreativeTab);
    }

    public String getTextureFile() {
        return JaffasPower.textureFile;
    }
}
