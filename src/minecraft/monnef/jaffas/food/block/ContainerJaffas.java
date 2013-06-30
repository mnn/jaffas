/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.block;

import monnef.core.MonnefCorePlugin;
import monnef.jaffas.food.JaffasFood;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public abstract class ContainerJaffas extends Container {
    protected TileEntity tile;

    protected ContainerJaffas(InventoryPlayer inventoryPlayer, TileEntity tile) {
        this.tile = tile;
        if (!(tile instanceof IInventory)) {
            throw new RuntimeException("Linked tile entity must implement IInventory.");
        }

        constructSlots((IInventory) tile);

        bindPlayerInventory(inventoryPlayer);
    }

    protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9,
                        8 + j * 18, 84 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 142));
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
        ItemStack stack = null;
        Slot slotObject = (Slot) inventorySlots.get(slot);

        int slots = getSlotsCount();

        if (MonnefCorePlugin.debugEnv) {
            JaffasFood.Log.printDebug(this.getClass().getSimpleName() + ": transferStackInSlot - slot#=" + slot);
        }

        //null checks and checks if the item can be stacked (maxStackSize > 1)
        if (slotObject != null && slotObject.getHasStack()) {
            ItemStack stackInSlot = slotObject.getStack();
            stack = stackInSlot.copy();

            //merges the item into player inventory since its in the tileEntity
            if (slot < slots) {
                if (!this.mergeItemStack(stackInSlot, slots, 36 + slots, true)) {
                    return null;
                }
            }
            //places it into the tileEntity is possible since its in the player inventory
            else {
                if (!this.mergeItemStack(stackInSlot, 0, getInputSlotsCount(), false)) {
                    return null;
                }
            }

            if (stackInSlot.stackSize == 0) {
                slotObject.putStack(null);
            } else {
                slotObject.onSlotChanged();
            }

            if (stackInSlot.stackSize == stack.stackSize) {
                return null;
            }
            slotObject.onPickupFromSlot(player, stackInSlot);
        }
        return stack;
    }

    protected abstract int getSlotsCount();

    protected abstract int getOutputSlotsCount();

    protected int getInputSlotsCount() {
        return getSlotsCount() - getOutputSlotsCount();
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return ((IInventory) tile).isUseableByPlayer(player);
    }

    /**
     * Creates slots via addSlotToContainer.
     * Output slots *must* be creates last!
     *
     * @param inv The inventory.
     */
    public abstract void constructSlots(IInventory inv);

    protected boolean mergeItemStack(ItemStack stack, int startingIndex, int endingIndex, boolean fromEnd) {
        return super.mergeItemStack(stack, startingIndex, endingIndex, fromEnd);
    }
}
