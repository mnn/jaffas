package jaffas.common;

import net.minecraft.src.Item;

public class ItemJaffaTool extends Item {
    protected ItemJaffaTool(int id, int usageCount) {
        super(id);
        setMaxStackSize(1);
        setMaxDamage(usageCount);
    }

    public String getTextureFile(){
        return "/jaffas_01.png";
    }
}
