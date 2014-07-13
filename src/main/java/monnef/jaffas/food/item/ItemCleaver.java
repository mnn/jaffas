/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.item;

import monnef.jaffas.food.JaffasFood;
import net.minecraft.item.Item;

public class ItemCleaver extends ItemJaffaSword {
    public ItemCleaver(int texture, Item.ToolMaterial material) {
        super(texture, material);
        this.setCreativeTab(JaffasFood.instance.creativeTab);
    }
}

