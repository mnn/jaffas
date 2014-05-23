/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileConstructionDummy extends TileEntity implements IInventory, ISidedInventory {
    private TileCompostCore core;
    int coreX;
    int coreY;
    int coreZ;

    public void setCore(TileCompostCore core) {
        this.core = core;
        coreX = core.xCoord;
        coreY = core.yCoord;
        coreZ = core.zCoord;
    }

    public TileCompostCore getCore() {
        if (core == null) {
            core = (TileCompostCore) worldObj.getBlockTileEntity(coreX, coreY, coreZ);
        }

        if (core == null) {
            worldObj.setBlock(xCoord, yCoord, zCoord, 0); // self destruction when no core
        }

        return core;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);

        coreX = tagCompound.getInteger("CoreX");
        coreY = tagCompound.getInteger("CoreY");
        coreZ = tagCompound.getInteger("CoreZ");
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);

        tagCompound.setInteger("CoreX", coreX);
        tagCompound.setInteger("CoreY", coreY);
        tagCompound.setInteger("CoreZ", coreZ);
    }

    @Override
    public int getSizeInventory() {
        if (getCore() == null) return 0;
        return getCore().getSizeInventory();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        if (getCore() == null) return null;
        return getCore().getStackInSlot(slot);
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        if (getCore() == null) return;
        getCore().setInventorySlotContents(slot, stack);
    }

    @Override
    public ItemStack decrStackSize(int slot, int amt) {
        if (getCore() == null) return null;
        return getCore().decrStackSize(slot, amt);
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        if (getCore() == null) return null;
        return getCore().getStackInSlotOnClosing(slot);
    }

    @Override
    public int getInventoryStackLimit() {
        if (getCore() == null) return 0;
        return getCore().getInventoryStackLimit();
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        if (getCore() == null) return false;
        return getCore().isUseableByPlayer(player);
    }

    @Override
    public void openChest() {
        if (getCore() == null) return;
        getCore().openChest();
    }

    @Override
    public void closeChest() {
        if (getCore() == null) return;
        getCore().closeChest();
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        if (getCore() == null) return false;
        return getCore().isItemValidForSlot(i, itemstack);
    }

    @Override
    public String getInvName() {
        if (getCore() == null) return "MISSING_CORE";
        return getCore().getInvName();
    }

    @Override
    public boolean isInvNameLocalized() {
        if (getCore() == null) return false;
        return getCore().isInvNameLocalized();
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int var1) {
        if (getCore() == null) return new int[]{};
        return getCore().getAccessibleSlotsFromSide(var1);
    }

    @Override
    public boolean canInsertItem(int i, ItemStack itemstack, int j) {
        if (getCore() == null) return false;
        return getCore().canInsertItem(i, itemstack, j);
    }

    @Override
    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
        if (getCore() == null) return false;
        return getCore().canExtractItem(i, itemstack, j);
    }
}
