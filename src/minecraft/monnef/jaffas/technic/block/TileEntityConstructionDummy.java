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

public class TileEntityConstructionDummy extends TileEntity implements IInventory, ISidedInventory {
    private TileEntityCompostCore core;
    int coreX;
    int coreY;
    int coreZ;

    public void setCore(TileEntityCompostCore core) {
        this.core = core;
        coreX = core.xCoord;
        coreY = core.yCoord;
        coreZ = core.zCoord;
    }

    public TileEntityCompostCore getCore() {
        if (core == null)
            core = (TileEntityCompostCore) worldObj.getBlockTileEntity(coreX, coreY, coreZ);

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
        return core.getSizeInventory();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return core.getStackInSlot(slot);
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        core.setInventorySlotContents(slot, stack);
    }

    @Override
    public ItemStack decrStackSize(int slot, int amt) {
        return core.decrStackSize(slot, amt);
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        return core.getStackInSlotOnClosing(slot);
    }

    @Override
    public int getInventoryStackLimit() {
        return core.getInventoryStackLimit();
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return core.isUseableByPlayer(player);
    }

    @Override
    public void openChest() {
        core.openChest();
    }

    @Override
    public void closeChest() {
        core.closeChest();
    }

    @Override
    public boolean isStackValidForSlot(int i, ItemStack itemstack) {
        return core.isStackValidForSlot(i, itemstack);
    }

    @Override
    public String getInvName() {
        return core.getInvName();
    }

    @Override
    public boolean isInvNameLocalized() {
        return core.isInvNameLocalized();
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int var1) {
        return core.getAccessibleSlotsFromSide(var1);
    }

    @Override
    public boolean canInsertItem(int i, ItemStack itemstack, int j) {
        return core.canInsertItem(i, itemstack, j);
    }

    @Override
    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
        return core.canExtractItem(i, itemstack, j);
    }
}
