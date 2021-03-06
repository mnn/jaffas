/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.client.common;

import monnef.core.MonnefCorePlugin;
import monnef.core.client.GuiContainerMachine;
import monnef.core.block.ContainerMachine;
import monnef.jaffas.power.block.common.TileEntityBasicProcessingMachine;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiContainerBasicProcessingMachine extends GuiContainerMachine {
    protected TileEntityBasicProcessingMachine bpMachine;
    private TileEntityBasicProcessingMachine.MachineRecord machineRecord;

    public GuiContainerBasicProcessingMachine(InventoryPlayer inventoryPlayer, TileEntityBasicProcessingMachine tileEntity, ContainerMachine container) {
        super(inventoryPlayer, tileEntity, container);

        bpMachine = tileEntity;
        machineRecord = TileEntityBasicProcessingMachine.getMachineRecord(tileEntity.getClass());
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int param1, int param2) {
        super.drawGuiContainerForegroundLayer(param1, param2);

        if (MonnefCorePlugin.debugEnv) {
            fontRendererObj.drawString(bpMachine.processTime + " / " + bpMachine.processItemTime, 8, 16, 4210752);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        super.drawGuiContainerBackgroundLayer(par1, par2, par3);

        if (bpMachine.processItemTime != 0) {
            int m = (bpMachine.processTime * 24) / bpMachine.processItemTime;
            drawTexturedModalRect(x + 69, y + 34, 176, 14, m + 1, 16);
        }

    }

    @Override
    public String getBackgroundTexture() {
        return machineRecord.getGuiBackgroundTexture();
    }

    @Override
    protected String getTitle() {
        return bpMachine.getMachineTitle();
    }
}
