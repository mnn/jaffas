/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.common;

import cpw.mods.fml.common.IFuelHandler;
import monnef.jaffas.food.JaffasFood;
import net.minecraft.item.ItemStack;

public class FuelHandler implements IFuelHandler {
    public static int SWITCHGRASS_BURN_VALUE;

    @Override
    public int getBurnTime(ItemStack fuel) {
        if (fuel.itemID == JaffasFood.blockSwitchgrass.blockID) {
            return SWITCHGRASS_BURN_VALUE;
        } else if (fuel.itemID == JaffasFood.blockSwitchgrassSolid.blockID) {
            return SWITCHGRASS_BURN_VALUE * (9 + 1);
        }

        return 0;
    }
}
