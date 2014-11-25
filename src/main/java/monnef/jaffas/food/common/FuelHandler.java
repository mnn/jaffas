/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.common;

import cpw.mods.fml.common.IFuelHandler;
import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.food.item.JaffaItem;
import net.minecraft.item.ItemStack;

import static monnef.core.utils.ItemHelper.isStackSameItemAsBlock;
import static monnef.jaffas.food.common.ContentHolder.blockSwitchgrass;
import static monnef.jaffas.food.common.ContentHolder.blockSwitchgrassSolid;

public class FuelHandler implements IFuelHandler {
    public static int switchgrassBurnValue;

    public static int getSwitchgrassSolidBurnValue() {
        return switchgrassBurnValue * (9 + 1);
    }

    @Override
    public int getBurnTime(ItemStack fuel) {
        if (fuel == null) {
            return 0;
        } else if (isStackSameItemAsBlock(fuel, blockSwitchgrass)) {
            return switchgrassBurnValue; // by default 100
        } else if (isStackSameItemAsBlock(fuel, blockSwitchgrassSolid)) {
            return getSwitchgrassSolidBurnValue(); // by default 1000
        } else if (fuel.getItem() == JaffasFood.getItem(JaffaItem.switchgrassCharcoal)) {
            return getSwitchgrassSolidBurnValue() * 2; // by default 2000 (by 800 = 80% better than solid switchgrass block after subtracting smelting)
        }

        return 0;
    }
}
