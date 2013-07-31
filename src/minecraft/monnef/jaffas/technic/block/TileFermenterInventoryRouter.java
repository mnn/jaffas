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
            master = (TileFermenter) worldObj.getBlockTileEntity(xCoord, yCoord - 1, zCoord);
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
    public void openChest() {
        getMaster().openChest();
    }

    @Override
    public void closeChest() {
        getMaster().closeChest();
    }

    @Override
    public boolean isStackValidForSlot(int i, ItemStack itemstack) {
        return getMaster().isStackValidForSlot(i, itemstack);
    }

    @Override
    public String getInvName() {
        return getMaster().getInvName();
    }

    @Override
    public boolean isInvNameLocalized() {
        return getMaster().isInvNameLocalized();
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
