/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.block;

import monnef.jaffas.power.block.common.ContainerMachine;
import monnef.jaffas.power.block.common.TileEntityMachineWithInventory;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerWindGenerator extends ContainerMachine {
    public ContainerWindGenerator(InventoryPlayer inventoryPlayer, TileEntityMachineWithInventory te) {
        super(inventoryPlayer, te);
    }
}
