package monnef.jaffas.power.client;

import monnef.jaffas.power.block.common.ContainerMachine;
import monnef.jaffas.power.block.common.TileEntityMachineWithInventory;
import monnef.jaffas.power.client.common.GuiContainerMachine;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiContainerGenerator extends GuiContainerMachine {

    public GuiContainerGenerator(InventoryPlayer inventoryPlayer, TileEntityMachineWithInventory tileEntity, ContainerMachine container) {
        super(inventoryPlayer, tileEntity, container);
    }

    @Override
    protected String getTitle() {
        return "Generator";
    }

    @Override
    protected String getBackgroundTexture() {
        return "/guigenerator.png";
    }
}
