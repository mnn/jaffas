/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.block;

import monnef.core.utils.MathHelper;
import net.minecraft.item.ItemStack;

public class TileRipeningBox extends TileWithInventory {
    public static final int SLOT_OUTPUT = 8;
    public static final int RIPENING_SLOTS = 8;
    public static final int MAX_RIPENING_STATUS = 100;
    private int[] ripeningStatus;

    public TileRipeningBox() {
        ripeningStatus = new int[RIPENING_SLOTS];
    }

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

    public int getRipeningStatus(int id) {
        if (id < 0 || id > ripeningStatus.length) {
            throw new IllegalArgumentException();
        }
        return id * MAX_RIPENING_STATUS / (RIPENING_SLOTS - 1);
        //return ripeningStatus[id]; // TODO: uncomment when riping is ipmlemented
    }

    public void setRipeningStatus(int id, int value) {
        if (!worldObj.isRemote) {
            throw new RuntimeException("Cannot set ripening status via gui method on a server!");
        }
        ripeningStatus[id] = value;
    }
}
