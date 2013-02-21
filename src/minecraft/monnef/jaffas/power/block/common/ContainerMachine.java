package monnef.jaffas.power.block.common;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import monnef.jaffas.food.mod_jaffas_food;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

public abstract class ContainerMachine extends Container {
    protected TileEntityMachineWithInventory tileEntity;

    protected ArrayList<Integer> lastValue;

    public ContainerMachine(InventoryPlayer inventoryPlayer, TileEntityMachineWithInventory te) {
        tileEntity = te;

        constructSlots();

        //commonly used vanilla code that adds the player's inventory
        bindPlayerInventory(inventoryPlayer);

        lastValue = new ArrayList<Integer>();
        for (int i = 0; i < tileEntity.getIntegersToSyncCount(); i++) {
            lastValue.add(0);
        }
    }

    public abstract void constructSlots();

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return tileEntity.isUseableByPlayer(player);
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

        //null checks and checks if the item can be stacked (maxStackSize > 1)
        if (slotObject != null && slotObject.getHasStack()) {
            ItemStack stackInSlot = slotObject.getStack();
            stack = stackInSlot.copy();

            //merges the item into player inventory since its in the tileEntity
            int slots = tileEntity.getSizeInventory();
            if (slot < slots) {
                if (!this.mergeItemStack(stackInSlot, slots, 36 + slots, true)) {
                    return null;
                }
            }
            //places it into the tileEntity is possible since its in the player inventory
            else if (!this.mergeItemStack(stackInSlot, 0, slots, false)) {
                return null;
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

    public void addCraftingToCrafters(ICrafting par1ICrafting) {
        super.addCraftingToCrafters(par1ICrafting);
        for (int i = 0; i < lastValue.size(); i++) {
            par1ICrafting.sendProgressBarUpdate(this, i, this.tileEntity.getCurrentValueOfIntegerToSync(i));
        }
    }

    /**
     * Updates crafting matrix; called from onCraftMatrixChanged. Args: none
     */
    public void updateCraftingResults() {
        super.updateCraftingResults();

        for (int var1 = 0; var1 < this.crafters.size(); ++var1) {
            ICrafting var2 = (ICrafting) this.crafters.get(var1);

            /*
            if (this.lastChopTime != this.tileEntity.chopTime) {
                var2.sendProgressBarUpdate(this, 0, this.tileEntity.chopTime);
            }
            */
            for (int i = 0; i < lastValue.size(); i++) {
                if (lastValue.get(i) != tileEntity.getCurrentValueOfIntegerToSync(i)) {
                    var2.sendProgressBarUpdate(this, i, tileEntity.getCurrentValueOfIntegerToSync(i));
                }
            }
        }

        for (int i = 0; i < lastValue.size(); i++) {
            //this.lastChopTime = this.tileEntity.chopTime;
            lastValue.set(i, tileEntity.getCurrentValueOfIntegerToSync(i));
        }
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int index, int value) {
        /*if (index == 0) {
            this.tileEntity.chopTime = value;
        } */
        if (index < 0 || index > lastValue.size()) {
            if (mod_jaffas_food.debug) {
                System.err.println("skipping invalid index [" + index + "] of int to sync in container [" + this.getClass().getName() + "]");
            }
            return;
        }

        tileEntity.setCurrentValueOfIntegerToSync(index, value);
    }
}
