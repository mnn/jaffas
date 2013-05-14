/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.item;

import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.jaffasMod;
import net.minecraft.item.EnumToolMaterial;

public class ItemCleaver extends ItemJaffaSword {
    public ItemCleaver(int ItemID, int texture, EnumToolMaterial material) {
        super(ItemID, texture, material);
        this.setCreativeTab(JaffasFood.instance.CreativeTab);
    }
}

