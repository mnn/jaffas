package monnef.jaffas.food;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Item;

public class ItemJaffaTool extends Item {
    protected ItemJaffaTool(int id, int usageCount) {
        super(id);
        setMaxStackSize(1);
        setMaxDamage(usageCount);
        this.setCreativeTab(CreativeTabs.tabTools);
        this.setCreativeTab(mod_jaffas.CreativeTab);
    }

    public String getTextureFile(){
        return "/jaffas_01.png";
    }
}
