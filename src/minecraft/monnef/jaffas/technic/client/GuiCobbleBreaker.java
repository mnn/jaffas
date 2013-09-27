/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.client;

import monnef.core.client.GuiContainerJaffas;
import monnef.core.utils.GuiHelper;
import monnef.jaffas.technic.block.ContainerCobbleBreaker;
import monnef.jaffas.technic.block.TileCobbleBreaker;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

public class GuiCobbleBreaker extends GuiContainerJaffas {
    public static final ResourceLocation GUI_TEXTURE = new ResourceLocation("/guibreaker.png");

    public TileCobbleBreaker tile;

    public GuiCobbleBreaker(InventoryPlayer inventoryPlayer,
                            TileCobbleBreaker tileEntity) {
        super(new ContainerCobbleBreaker(inventoryPlayer, tileEntity));
        tile = tileEntity;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int param1, int param2) {
        fontRenderer.drawString("Cobble Breaker", 8, 6, 4210752);
        fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2,
                                                   int par3) {
        //draw your Gui here, only thing you need to change is the path
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(GUI_TEXTURE);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

        // arrow
        int m = (tile.getWorkMeter() * 24) / tile.getMaxWorkMeter();
        drawTexturedModalRect(x + 84, y + 34, 176, 14, m + 1, 16);

        // flame
        int burn = (tile.getBurnTime() * 14) / (tile.getBurnItemTime() == 0 ? 1 : tile.getBurnItemTime());
        GuiHelper.drawModalRectFromDown(this, x + 23, y + 18, 176, 0, 16, burn, 14);
    }
    // x, y, u, v, width, height
}
