package monnef.jaffas.food.common;

import cpw.mods.fml.common.IFuelHandler;
import monnef.jaffas.food.mod_jaffas;
import net.minecraft.item.ItemStack;

public class FuelHandler implements IFuelHandler {
    @Override
    public int getBurnTime(ItemStack fuel) {
        if (fuel.itemID == mod_jaffas.blockSwitchgrass.blockID) {
            return 100;
        }

        return 0;
    }
}
