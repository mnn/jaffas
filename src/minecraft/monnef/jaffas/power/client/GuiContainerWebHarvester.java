/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.client;

import monnef.core.block.TileMachineWithInventory;
import monnef.core.block.ContainerMachine;
import monnef.core.client.GuiContainerMachine;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiContainerWebHarvester extends GuiContainerMachine {
    public GuiContainerWebHarvester(InventoryPlayer inventoryPlayer, TileMachineWithInventory tileEntity, ContainerMachine container) {
        super(inventoryPlayer, tileEntity, container);
        setBackgroundTexture("guiwebharvester.png");
    }
}
