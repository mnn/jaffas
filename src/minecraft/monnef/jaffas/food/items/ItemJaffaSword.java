package monnef.jaffas.food.items;

import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemSword;

public class ItemJaffaSword extends ItemSword {

    public ItemJaffaSword(int ItemID, EnumToolMaterial material) {
        super(ItemID, material);
        this.setCreativeTab(null);
    }

    public String getTextureFile() {
        return "/jaffas_01.png";
    }
}
