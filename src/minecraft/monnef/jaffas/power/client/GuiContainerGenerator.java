/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.client;

import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.power.block.TileGenerator;
import monnef.jaffas.power.block.common.ContainerMachine;
import monnef.jaffas.power.block.common.TileEntityMachineWithInventory;
import monnef.jaffas.power.client.common.GuiContainerMachine;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;

import java.util.List;

public class GuiContainerGenerator extends GuiContainerMachine {
    private TileGenerator generator;

    public GuiContainerGenerator(InventoryPlayer inventoryPlayer, TileEntityMachineWithInventory tileEntity, ContainerMachine container) {
        super(inventoryPlayer, tileEntity, container);
        generator = (TileGenerator) tileEntity;
    }

    @Override
    protected String getTitle() {
        return "Generator";
    }

    @Override
    public String getBackgroundTexture() {
        return "guigenerator.png";
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int param1, int param2) {
        super.drawGuiContainerForegroundLayer(param1, param2);

        if (JaffasFood.debug) {
            String s = String.valueOf(generator.burnTime);
            fontRenderer.drawString(s, 100, 10, 4210752);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        super.drawGuiContainerBackgroundLayer(par1, par2, par3);

        if (generator.isBurning()) {
            int burn = generator.getBurnTimeScaled(14);
            this.drawTexturedModalRect(x + 80, y + 45 + (13 - burn), 176, 14 - burn, 14, burn);
        }
    }

    @Override
    public List<String> fillTooltips(GuiContainer gui, int mousex, int mousey, List<String> currenttip) {
        return currenttip;
    }
}
