/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.common;

import cpw.mods.fml.common.IFuelHandler;
import net.minecraft.item.ItemStack;

import static monnef.core.utils.ItemHelper.isStackSameItemAsBlock;
import static monnef.jaffas.food.common.ContentHolder.blockSwitchgrass;
import static monnef.jaffas.food.common.ContentHolder.blockSwitchgrassSolid;

public class FuelHandler implements IFuelHandler {
    public static int switchgrassBurnValue;

    @Override
    public int getBurnTime(ItemStack fuel) {
        if (isStackSameItemAsBlock(fuel, blockSwitchgrass)) {
            return switchgrassBurnValue;
        } else if (isStackSameItemAsBlock(fuel, blockSwitchgrassSolid)) {
            return switchgrassBurnValue * (9 + 1);
        }

        return 0;
    }
}
