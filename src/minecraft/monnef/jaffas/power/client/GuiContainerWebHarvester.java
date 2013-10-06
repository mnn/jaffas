/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.client;

import monnef.jaffas.food.block.common.TileEntityMachineWithInventory;
import monnef.jaffas.power.block.common.ContainerMachine;
import monnef.jaffas.power.client.common.GuiContainerMachine;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiContainerWebHarvester extends GuiContainerMachine {
    public GuiContainerWebHarvester(InventoryPlayer inventoryPlayer, TileEntityMachineWithInventory tileEntity, ContainerMachine container) {
        super(inventoryPlayer, tileEntity, container);
        setBackgroundTexture("guiwebharvester.png");
    }
}
