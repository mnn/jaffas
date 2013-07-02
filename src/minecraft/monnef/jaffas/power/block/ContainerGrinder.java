/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.block;

import monnef.jaffas.power.block.common.ContainerBasicProcessingMachine;
import monnef.jaffas.power.block.common.TileEntityBasicProcessingMachine;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerGrinder extends ContainerBasicProcessingMachine {
    public ContainerGrinder() {
    }

    public ContainerGrinder(InventoryPlayer inventoryPlayer, TileEntityBasicProcessingMachine te) {
        super(inventoryPlayer, te);
    }

    @Override
    public int getSlotsCount() {
        return 2;
    }

    @Override
    public int getOutputSlotsCount() {
        return 1;
    }
}
