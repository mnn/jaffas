package monnef.jaffas.power.block;

import monnef.jaffas.power.block.common.ContainerMachine;
import monnef.jaffas.power.block.common.TileEntityMachineWithInventory;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;

public class ContainerGenerator extends ContainerMachine {
    public ContainerGenerator(InventoryPlayer inventoryPlayer, TileEntityMachineWithInventory te) {
        super(inventoryPlayer, te);
    }

    @Override
    public void constructSlots() {
        addSlotToContainer(new Slot(tileEntity, 0, 80, 25)); // 0 is ID
    }
}
