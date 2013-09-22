/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import monnef.core.common.ContainerJaffas;
import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.food.item.JaffaItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import static monnef.jaffas.food.block.TileBoard.SLOT_INPUT;
import static monnef.jaffas.food.block.TileBoard.SLOT_KNIFE;
import static monnef.jaffas.food.block.TileBoard.SLOT_OUTPUT;

public class ContainerBoard extends ContainerJaffas {
    protected TileBoard board;
    private int lastChopTime;

    public ContainerBoard(InventoryPlayer inventoryPlayer, TileBoard te) {
        super(inventoryPlayer, te);
        board = te;
    }

    @Override
    public void addCraftingToCrafters(ICrafting par1ICrafting) {
        super.addCraftingToCrafters(par1ICrafting);
        par1ICrafting.sendProgressBarUpdate(this, 0, this.board.chopTime);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (int var1 = 0; var1 < this.crafters.size(); ++var1) {
            ICrafting var2 = (ICrafting) this.crafters.get(var1);

            if (this.lastChopTime != this.board.chopTime) {
                var2.sendProgressBarUpdate(this, 0, this.board.chopTime);
            }
        }

        this.lastChopTime = this.board.chopTime;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void updateProgressBar(int par1, int par2) {
        if (par1 == 0) {
            this.board.chopTime = par2;
        }
    }

    @Override
    public void onCraftGuiClosed(EntityPlayer par1EntityPlayer) {
        board.checkKnife();
    }

    @Override
    public int getSlotsCount() {
        return 3;
    }

    @Override
    public void constructSlots(IInventory inv) {
        addSlotToContainer(new Slot(inv, SLOT_INPUT, 56, 35)); //  input
        addSlotToContainer(new Slot(inv, SLOT_KNIFE, 22, 35)); //  knife
        addSlotToContainer(new Slot(inv, SLOT_OUTPUT, 116, 35)); // output
    }

    // knife...
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
        ItemStack stack = null;
        Slot slotObject = (Slot) inventorySlots.get(slot);

        int slots = getSlotsCount();

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
                if (stackInSlot.itemID == JaffasFood.getItem(JaffaItem.knifeKitchen).itemID) {
                    if (!this.mergeItemStack(stackInSlot, 0, getInputSlotsCount(), true)) {
                        return null;
                    }
                } else if (!this.mergeItemStack(stackInSlot, 0, getInputSlotsCount(), false)) {
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

    @Override
    public int getOutputSlotsCount() {
        return 1;
    }
}