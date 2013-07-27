/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.block;

import monnef.core.utils.MathHelper;
import net.minecraft.item.ItemStack;

public class TileRipeningBox extends TileEntityWithInventory {
    @Override
    public int getSizeInventory() {
        return 9;
    }

    @Override
    public String getInvName() {
        return "container.ripeningBox";
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        return MathHelper.range(0, getSizeInventory());
    }

    @Override
    public boolean canInsertItem(int i, ItemStack itemstack, int j) {
        return true;
    }

    @Override
    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
        return true;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }
}
