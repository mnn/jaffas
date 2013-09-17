/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.common;

import cpw.mods.fml.common.IFuelHandler;
import net.minecraft.item.ItemStack;

public class FuelHandler implements IFuelHandler {
    public static int switchgrassBurnValue;

    @Override
    public int getBurnTime(ItemStack fuel) {
        if (fuel.itemID == ContentHolder.blockSwitchgrass.blockID) {
            return switchgrassBurnValue;
        } else if (fuel.itemID == ContentHolder.blockSwitchgrassSolid.blockID) {
            return switchgrassBurnValue * (9 + 1);
        }

        return 0;
    }
}
