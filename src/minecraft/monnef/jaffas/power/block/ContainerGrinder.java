/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.block;

import monnef.jaffas.power.block.common.ContainerBasicProcessingMachine;
import monnef.jaffas.power.block.common.TileEntityMachineWithInventory;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerGrinder extends ContainerBasicProcessingMachine {
    public ContainerGrinder(InventoryPlayer inventoryPlayer, TileEntityMachineWithInventory te) {
        super(inventoryPlayer, te);
    }
}
