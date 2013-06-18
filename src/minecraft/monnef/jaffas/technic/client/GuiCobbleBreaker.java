/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.client;

import monnef.jaffas.technic.block.ContainerCobbleBreaker;
import monnef.jaffas.technic.block.TileEntityCobbleBreaker;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

public class GuiCobbleBreaker extends GuiContainer {
    public static final String GUI_TEXTURE = "/guibreaker.png";
    public static final int TANK_METER_HEIGHT_MAX = 46;

    public TileEntityCobbleBreaker tile;

    public GuiCobbleBreaker(InventoryPlayer inventoryPlayer,
                            TileEntityCobbleBreaker tileEntity) {
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
    }
    // x, y, u, v, width, height
}
