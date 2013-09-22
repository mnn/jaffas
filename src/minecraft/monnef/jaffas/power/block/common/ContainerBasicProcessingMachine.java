/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.block.common;

import monnef.core.client.SlotOutput;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class ContainerBasicProcessingMachine extends ContainerMachine {
    public static final int SLOT_INPUT = 0;
    public static final int SLOT_OUTPUT = 1;

    // dummy constructor
    public ContainerBasicProcessingMachine() {
    }

    public ContainerBasicProcessingMachine(InventoryPlayer inventoryPlayer, TileEntityBasicProcessingMachine te) {
        super(inventoryPlayer, te);
    }

    @Override
    public void constructSlots(IInventory inv) {
        addSlotToContainer(new Slot(inv, SLOT_INPUT, 42, 35));
        addSlotToContainer(new SlotOutput(inv, SLOT_OUTPUT, 111, 35));
    }

    @Override
    public int getSlotsCount() {
        return 2;
    }

    @Override
    public int getOutputSlotsCount() {
        return 1;
    }

    public Slot getOutputSlot(int outputNumber) {
        return getSlot(getStartIndexOfOutput() + outputNumber);
    }

    public Slot getInputSlot(int number) {
        return getSlot(number);
    }
}
