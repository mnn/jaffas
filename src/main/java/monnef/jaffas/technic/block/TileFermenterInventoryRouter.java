/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class TileFermenterInventoryRouter extends TileEntity implements IInventory, ISidedInventory {
    private TileFermenter master;

    public TileFermenter getMaster() {
        if (master == null) {
            master = (TileFermenter) worldObj.getTileEntity(xCoord, yCoord - 1, zCoord);
        }

        return master;
    }

    @Override
    public int getSizeInventory() {
        return getMaster().getSizeInventory();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return getMaster().getStackInSlot(slot);
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        getMaster().setInventorySlotContents(slot, stack);
    }

    @Override
    public ItemStack decrStackSize(int slot, int amt) {
        return getMaster().decrStackSize(slot, amt);
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        return getMaster().getStackInSlotOnClosing(slot);
    }

    @Override
    public int getInventoryStackLimit() {
        return getMaster().getInventoryStackLimit();
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return getMaster().isUseableByPlayer(player);
    }

    @Override
    public void openInventory() {
        getMaster().openInventory();
    }

    @Override
    public void closeInventory() {
        getMaster().closeInventory();
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return getMaster().isItemValidForSlot(i, itemstack);
    }

    @Override
    public String getInventoryName() {
        return getMaster().getInventoryName();
    }

    @Override
    public boolean hasCustomInventoryName() {
        return getMaster().hasCustomInventoryName();
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        return getMaster().getAccessibleSlotsFromSide(side);
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack stack, int side) {
        return getMaster().canInsertItem(slot, stack, side);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack stack, int side) {
        return getMaster().canExtractItem(slot, stack, side);
    }
}
