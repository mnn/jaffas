/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.client;

import monnef.core.client.GuiContainerJaffas;
import monnef.jaffas.food.block.ContainerFridge;
import monnef.jaffas.food.block.TileFridge;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;

public class GuiFridge extends GuiContainerJaffas {
    private TileFridge tileEntity;

    public GuiFridge(InventoryPlayer inventoryPlayer,
                     TileFridge tileEntity) {
        //the container is instantiated and passed to the superclass for handling
        super(new ContainerFridge(inventoryPlayer, tileEntity));
        ySize = 198;

        this.tileEntity = tileEntity;
        setBackgroundTexture("guifridge.png");
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        fontRenderer.drawString("Fridge", 8, 3, 4210752);
        fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);

        if (TileFridge.tickDivider == 1) {
            String s = String.valueOf(tileEntity.getTemperature());
            fontRenderer.drawString(s, 75, 3, 4210752);

            if (tileEntity.powerProvider != null) {
                s = String.valueOf(tileEntity.powerProvider.getEnergyStored());
                fontRenderer.drawString(s, 90, 15, 4210752);
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        super.drawGuiContainerBackgroundLayer(par1, par2, par3);

        if (tileEntity.isBurning()) {
            int burn = tileEntity.getBurnTimeRemainingScaled(14);
            this.drawTexturedModalRect(x + 103, y + 54 + (13 - burn), 176, 14 - burn, 14, burn);
        }

        int temp = Math.round(tileEntity.temperature);
        int tY = (int) Math.round(56 - temp * 1.2D);
        this.drawTexturedModalRect(x + 154, y + tY, 190, 0, 1, 105 - tY);
    }

}