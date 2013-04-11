/*
 * Copyright (c) 2013 monnef.
 */

package monnef.jaffas.food.item;

import monnef.jaffas.food.JaffasFood;
import net.minecraft.item.EnumToolMaterial;

public class ItemCleaver extends ItemJaffaSword {
    public ItemCleaver(int ItemID, int texture, EnumToolMaterial material) {
        super(ItemID, texture, material);
        this.setCreativeTab(JaffasFood.CreativeTab);
    }
}

