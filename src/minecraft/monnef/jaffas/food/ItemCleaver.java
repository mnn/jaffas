package monnef.jaffas.food;

import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemSword;

public class ItemCleaver extends ItemSword {
    public ItemCleaver(int ItemID, EnumToolMaterial material) {
        super(ItemID, material);
        this.setCreativeTab(mod_jaffas.CreativeTab);
    }

    public String getTextureFile() {
        return "/jaffas_01.png";
    }
}
