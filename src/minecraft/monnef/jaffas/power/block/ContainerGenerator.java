/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.block;

import monnef.jaffas.power.block.common.ContainerMachine;
import monnef.jaffas.power.block.common.TileEntityMachineWithInventory;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class ContainerGenerator extends ContainerMachine {
    public ContainerGenerator(InventoryPlayer inventoryPlayer, TileEntityMachineWithInventory te) {
        super(inventoryPlayer, te);
    }

    @Override
    public int getSlotsCount() {
        return 1;
    }

    @Override
    public void constructSlots(IInventory inv) {
        addSlotToContainer(new Slot(inv, TileEntityGenerator.SLOT_FUEL, 80, 25));
    }

    @Override
    public int getOutputSlotsCount() {
        return 0;
    }
}
