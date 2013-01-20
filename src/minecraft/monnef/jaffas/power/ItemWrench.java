package monnef.jaffas.power;

import monnef.jaffas.power.api.IPipeWrench;
import monnef.jaffas.power.item.ItemPower;

public class ItemWrench extends ItemPower implements IPipeWrench {
    public ItemWrench(int id, int textureIndex) {
        super(id, textureIndex);
        setItemName("pipeWrench");
    }
}
