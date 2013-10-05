/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.trees.client;

import monnef.core.client.GuiContainerMonnefCore;
import monnef.jaffas.trees.block.ContainerFruitCollector;
import monnef.jaffas.trees.block.TileFruitCollector;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;

public class GuiFruitCollector extends GuiContainerMonnefCore {
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
    }

}