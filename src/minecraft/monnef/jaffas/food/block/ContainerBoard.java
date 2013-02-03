package monnef.jaffas.food.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import monnef.jaffas.food.item.JaffaItem;
import monnef.jaffas.food.mod_jaffas;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerBoard extends Container {
    protected TileEntityBoard board;
    private int lastChopTime;
    private final static int slots = 3;

    public ContainerBoard(InventoryPlayer inventoryPlayer, TileEntityBoard te) {
        board = te;

        //the Slot constructor takes the IInventory and the slot number in that it binds to
        //and the x-y coordinates it resides on-screen
//        for (int i = 0; i < te.getSizeInventory(); i++) {
//            int j=0;
//            for (int j = 0; j < 3; j++) {
//                addSlotToContainer(new Slot(tileEntity, j + i * 3, 62 + j * 18, 17 + i * 18));
        //          }
        addSlotToContainer(new Slot(board, 0, 56, 35)); //  input
        addSlotToContainer(new Slot(board, 1, 116, 35)); // output
        addSlotToContainer(new Slot(board, 2, 22, 35)); //  knife
//        }

        //commonly used vanilla code that adds the player's inventory
        bindPlayerInventory(inventoryPlayer);
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return board.isUseableByPlayer(player);
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
            if (slot < slots) {
                if (!this.mergeItemStack(stackInSlot, slots, 36 + slots, true)) {
                    return null;
                }
            }
            //places it into the tileEntity is possible since its in the player inventory
            else {
                if (stackInSlot.itemID == mod_jaffas.getItem(JaffaItem.knifeKitchen).shiftedIndex) {
                    if (!this.mergeItemStack(stackInSlot, 0, slots, true)) {
                        return null;
                    }
                } else if (!this.mergeItemStack(stackInSlot, 0, slots, false)) {
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

    /**
     * x* @param itemStack ItemStack to merge into inventory
     * x* @param start minimum slot to attempt fill
     * x* @param end maximum slot to attempt fill
     * x* @param backwards go backwards
     * x* @return true if stacks merged successfully
     */
    //public boolean mergeItemStack(itemStack, start, end, backwards)
    public void addCraftingToCrafters(ICrafting par1ICrafting) {
        super.addCraftingToCrafters(par1ICrafting);
        par1ICrafting.sendProgressBarUpdate(this, 0, this.board.chopTime);
    }

    /**
     * Updates crafting matrix; called from onCraftMatrixChanged. Args: none
     */
    public void updateCraftingResults() {
        super.updateCraftingResults();

        for (int var1 = 0; var1 < this.crafters.size(); ++var1) {
            ICrafting var2 = (ICrafting) this.crafters.get(var1);

            if (this.lastChopTime != this.board.chopTime) {
                var2.sendProgressBarUpdate(this, 0, this.board.chopTime);
            }
        }

        this.lastChopTime = this.board.chopTime;
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2) {
        if (par1 == 0) {
            this.board.chopTime = par2;
        }
    }

    @Override
    public void onCraftGuiClosed(EntityPlayer par1EntityPlayer) {
        board.checkKnife();
    }
}