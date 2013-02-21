package monnef.jaffas.food.item;

import monnef.jaffas.food.mod_jaffas_food;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemSword;

public class ItemCleaver extends ItemSword {
    public ItemCleaver(int ItemID, EnumToolMaterial material) {
        super(ItemID, material);
        this.setCreativeTab(mod_jaffas_food.CreativeTab);
    }

    public String getTextureFile() {
        return mod_jaffas_food.textureFile[0];
    }
}

