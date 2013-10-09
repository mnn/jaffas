/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.block.common;

import monnef.core.common.ContainerRegistry;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public abstract class TileEntityMachineWithInventory extends TileEntityMachine implements IInventory {
    protected ItemStack[] inventory;
    public int powerMax;
    public int powerStored;
    private ContainerRegistry.ContainerDescriptor containerDescriptor;

    protected TileEntityMachineWithInventory() {
        super();
        setupContainerDescriptor();
        inventory = new ItemStack[getSizeInventory()];
    }

    private void setupContainerDescriptor() {
        containerDescriptor = ContainerRegistry.getContainerPrototype(this.getClass());
    }

    public ContainerRegistry.ContainerDescriptor getContainerDescriptor() {
        return containerDescriptor;
    }

    @Override
    public int getSizeInventory() {
        return containerDescriptor.getSlotsCount();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return inventory[slot];
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        inventory[slot] = stack;
        if (stack != null && stack.stackSize > getInventoryStackLimit()) {
            stack.stackSize = getInventoryStackLimit();
        }
        onInventoryChanged();
    }

    @Override
    public ItemStack decrStackSize(int slot, int amt) {
        ItemStack stack = getStackInSlot(slot);
        if (stack != null) {
            if (stack.stackSize <= amt) {
                setInventorySlotContents(slot, null);
            } else {
                stack = stack.splitStack(amt);
                if (stack.stackSize == 0) {
                    setInventorySlotContents(slot, null);
                }
            }
        }
        return stack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        ItemStack stack = getStackInSlot(slot);
        if (stack != null) {
            setInventorySlotContents(slot, null);
        }
        return stack;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) == this &&
                player.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) < 64;
    }

    @Override
    public void openChest() {
    }

    @Override
    public void closeChest() {
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);

        NBTTagList tagList = tagCompound.getTagList("Inventory");
        for (int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound tag = (NBTTagCompound) tagList.tagAt(i);
            byte slot = tag.getByte("Slot");
            if (slot >= 0 && slot < inventory.length) {
                inventory[slot] = ItemStack.loadItemStackFromNBT(tag);
            }
        }
        onInventoryChanged();
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);

        NBTTagList itemList = new NBTTagList();
        for (int i = 0; i < inventory.length; i++) {
            ItemStack stack = inventory[i];
            if (stack != null) {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setByte("Slot", (byte) i);
                stack.writeToNBT(tag);
                itemList.appendTag(tag);
            }
        }
        tagCompound.setTag("Inventory", itemList);
    }

    @Override
    public abstract String getInvName();

    public int getIntegersToSyncCount() {
        return 2;
    }

    public int getCurrentValueOfIntegerToSync(int index) {
        switch (index) {
            case 0:
                return (int) getPowerHandler().getEnergyStored();

            case 1:
                return (int) getPowerHandler().getMaxEnergyStored();
        }
        return -1;
    }

    public void setCurrentValueOfIntegerToSync(int index, int value) {
        switch (index) {
            case 0:
                powerStored = value;
                break;

            case 1:
                powerMax = value;
        }
    }

    @Override
    public boolean isInvNameLocalized() {
        return false;
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return true;
    }

    public boolean isPowerBarRenderingEnabled() {
        return true;
    }

    /**
     * Search whole inventory and tries to insert the item there.
     *
     * @param stack Input item.
     * @param doAdd Change inventory.
     * @return How many items we added.
     */
    protected int addItemToInventory(ItemStack stack, boolean doAdd) {
        int free = -1;
        boolean addToStack = false;
        int ret;
        int slotsCount = getSizeInventory();

        for (int i = 0; i < slotsCount; i++) {
            if (getStackInSlot(i) == null) {
                free = i;
                i = slotsCount;
            } else if (getStackInSlot(i).itemID == stack.itemID && getStackInSlot(i).stackSize < getStackInSlot(i).getMaxStackSize()) {
                addToStack = true;
                free = i;
                i = slotsCount;
            }
        }

        if (free != -1) {
            if (addToStack) {
                int newStackSize = stack.stackSize + getStackInSlot(free).stackSize;
                if (doAdd) getStackInSlot(free).stackSize += stack.stackSize;

                if (newStackSize > stack.getMaxStackSize()) {
                    int overflowItemsCount = newStackSize % stack.getMaxStackSize();
                    if (doAdd) getStackInSlot(free).stackSize = stack.getMaxStackSize();

                    ItemStack c = stack.copy();
                    c.stackSize = overflowItemsCount;
                    ret = stack.stackSize - overflowItemsCount;
                    ret += addItemToInventory(c, doAdd);
                } else {
                    ret = stack.stackSize;
                }
            } else {
                if (doAdd) setInventorySlotContents(free, stack);
                ret = stack.stackSize;
            }
        } else {
            ret = 0;
        }

        return ret;
    }

    public boolean canAddToInventory(EntityItem item) {
        return this.addItemToInventory(item.getEntityItem(), false) > 0;
    }
}
