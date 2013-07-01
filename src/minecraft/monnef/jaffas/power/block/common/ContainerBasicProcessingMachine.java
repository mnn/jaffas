/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.block.common;

import monnef.jaffas.technic.client.SlotOutput;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class ContainerBasicProcessingMachine extends ContainerMachine {
    public static final int SLOT_INPUT = 0;
    public static final int SLOT_OUTPUT = 1;

    public ContainerBasicProcessingMachine(InventoryPlayer inventoryPlayer, TileEntityMachineWithInventory te) {
        super(inventoryPlayer, te);
    }

    @Override
    protected int getSlotsCount() {
        return 2;
    }

    @Override
    protected int getOutputSlotsCount() {
        return 1;
    }

    @Override
    public void constructSlots(IInventory inv) {
        addSlotToContainer(new Slot(inv, SLOT_INPUT, 42, 35));
        addSlotToContainer(new SlotOutput(inv, SLOT_OUTPUT, 111, 35));
    }
}
