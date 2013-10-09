/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.block;

import monnef.core.block.ContainerMonnefCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

import java.util.Iterator;

public class ContainerFridge extends ContainerMonnefCore {

    public static final int inventorySize = 20;
    protected TileFridge tileEntity;
    protected float lastTemperature;

    public ContainerFridge(InventoryPlayer inventoryPlayer, TileFridge te) {
        super(inventoryPlayer, te);
        tileEntity = te;
    }

    @Override
    public int getYSize() {
        return 195;
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        Iterator var1 = this.crafters.iterator();

        while (var1.hasNext()) {
            ICrafting var2 = (ICrafting) var1.next();

            if (this.lastTemperature != this.tileEntity.temperature) {
                var2.sendProgressBarUpdate(this, 0, Math.round(this.tileEntity.temperature * 10));
            }
        }

        this.lastTemperature = this.tileEntity.temperature;
    }

    @Override
    public void updateProgressBar(int id, int value) {
        super.updateProgressBar(id, value);
        if (id == 0) {
            tileEntity.temperature = value / 10F;
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return tileEntity.isUseableByPlayer(player);
    }

    @Override
    public void constructSlots(IInventory inv) {
        int row, col;
        int colsPerRow = 4;
        for (int i = 0; i < inventorySize; i++) {
            col = i % colsPerRow;
            row = i / colsPerRow;
            addSlotToContainer(new Slot(inv, i, 8 + col * 18, 13 + row * 18));
        }
    }

    /*
    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int slot) {
        ItemStack stack = null;
        Slot slotObject = (Slot) inventorySlots.get(slot);

        //null checks and checks if the item can be stacked (maxStackSize > 1)
        if (slotObject != null && slotObject.getHasStack()) {
            ItemStack stackInSlot = slotObject.getStack();
            stack = stackInSlot.copy();

            //merges the item into player inventory since its in the tileEntity
            if (slot >= 0 && slot <= inventorySize + 1) {
                if (!mergeItemStack(stackInSlot, inventorySize + 1, inventorySlots.size(), true)) {
                    return null;
                }
                //places it into the tileEntity is possible since its in the player inventory
            } else if (!mergeItemStack(stackInSlot, 0, inventorySize + 1, false)) {
                return null;
            }

            if (stackInSlot.stackSize == 0) {
                slotObject.putStack(null);
            } else {
                slotObject.onSlotChanged();
            }
        }

        return stack;
    }
    */
}