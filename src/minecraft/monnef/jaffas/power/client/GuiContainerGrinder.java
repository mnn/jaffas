/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.client;

import monnef.jaffas.power.block.common.ContainerMachine;
import monnef.jaffas.power.block.common.TileEntityBasicProcessingMachine;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiContainerGrinder extends GuiContainerBasicProcessingMachine {
    public GuiContainerGrinder(InventoryPlayer inventoryPlayer, TileEntityBasicProcessingMachine tileEntity, ContainerMachine container) {
        super(inventoryPlayer, tileEntity, container);
    }

    @Override
    protected String getTitle() {
        return "Grinder";
    }

    @Override
    protected String getBackgroundTexture() {
        return "/guipmachine.png";
    }
}
