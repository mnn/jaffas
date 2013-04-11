/*
 * Copyright (c) 2013 monnef.
 */

package monnef.jaffas.food.common;

import cpw.mods.fml.common.IFuelHandler;
import monnef.jaffas.food.JaffasFood;
import net.minecraft.item.ItemStack;

public class FuelHandler implements IFuelHandler {
    @Override
    public int getBurnTime(ItemStack fuel) {
        if (fuel.itemID == JaffasFood.blockSwitchgrass.blockID) {
            return 100;
        }

        return 0;
    }
}
