/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.trees.client;

import monnef.core.client.GuiContainerJaffas;
import monnef.jaffas.trees.block.ContainerFruitCollector;
import monnef.jaffas.trees.block.TileFruitCollector;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;

public class GuiFruitCollector extends GuiContainerJaffas {
    private TileFruitCollector tileEntity;

    public GuiFruitCollector(InventoryPlayer inventoryPlayer, TileFruitCollector tileEntity) {
        super(new ContainerFruitCollector(inventoryPlayer, tileEntity));
        //ySize = 198;

        this.tileEntity = tileEntity;
        setBackgroundTexture("guicollector.png");
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        this.fontRenderer.drawString("Fruit Collector", 8, 5, 4210752);
        fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        super.drawGuiContainerBackgroundLayer(par1, par2, par3);

        if (tileEntity.isBurning()) {
            int burn = tileEntity.getBurnTimeRemainingScaled(14);
            this.drawTexturedModalRect(x + 105, y + 24 + (13 - burn), 176, 14 - burn, 14, burn);
        }

        /*
        int temp = Math.round(tileEntity.temperature);
        int tY = (int) Math.round(56 - temp * 1.2D);
        this.drawTexturedModalRect(x + 154, y + tY, 190, 0, 1, 105 - tY);
        */
    }

}