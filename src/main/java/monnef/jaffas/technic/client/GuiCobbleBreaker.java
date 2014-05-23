/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.client;

import monnef.core.client.GuiContainerMonnefCore;
import monnef.core.utils.GuiHelper;
import monnef.jaffas.technic.block.ContainerCobbleBreaker;
import monnef.jaffas.technic.block.TileCobbleBreaker;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;

public class GuiCobbleBreaker extends GuiContainerMonnefCore {
    public TileCobbleBreaker tile;

    public GuiCobbleBreaker(InventoryPlayer inventoryPlayer, TileCobbleBreaker tileEntity, ContainerCobbleBreaker container) {
        super(container);
        tile = tileEntity;
        setBackgroundTexture("guibreaker.png");
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int param1, int param2) {
        fontRenderer.drawString("Cobble Breaker", 8, 6, 4210752);
        fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        super.drawGuiContainerBackgroundLayer(par1, par2, par3);

        // arrow
        int m = (tile.getWorkMeter() * 24) / tile.getMaxWorkMeter();
        drawTexturedModalRect(x + 84, y + 34, 176, 14, m + 1, 16);

        // flame
        int burn = (tile.getBurnTime() * 14) / (tile.getBurnItemTime() == 0 ? 1 : tile.getBurnItemTime());
        GuiHelper.drawModalRectFromDown(this, x + 23, y + 18, 176, 0, 16, burn, 14);
    }
    // x, y, u, v, width, height
}
