/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.block.common;

import monnef.core.block.ContainerMachine;
import monnef.core.client.SlotOutput;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.tileentity.TileEntity;

public class ContainerBasicProcessingMachine extends ContainerMachine {
    private static final int SLOT_INPUT = 0;
    private static final int SLOT_OUTPUT = 1;

    public ContainerBasicProcessingMachine(InventoryPlayer inventoryPlayer, TileEntityBasicProcessingMachine te) {
        super(inventoryPlayer, te);
    }

    @Override
    public void constructSlotsFromTile(TileEntity tile) {
        IInventory inv = (IInventory) tile;
        addSlotToContainer(new Slot(inv, SLOT_INPUT, 42, 35));
        addSlotToContainer(new SlotOutput(inv, SLOT_OUTPUT, 111, 35));
    }

    public Slot getOutputSlot(int outputNumber) {
        return getSlot(getStartIndexOfOutput() + outputNumber);
    }

    public Slot getInputSlot(int number) {
        return getSlot(number);
    }
}
