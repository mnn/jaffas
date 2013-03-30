package monnef.jaffas.food.item;

import monnef.jaffas.food.jaffasFood;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemSword;

public class ItemCleaver extends ItemSword {
    public ItemCleaver(int ItemID, EnumToolMaterial material) {
        super(ItemID, material);
        this.setCreativeTab(jaffasFood.CreativeTab);
    }

    public String getTextureFile() {
        return jaffasFood.textureFile[0];
    }
}

