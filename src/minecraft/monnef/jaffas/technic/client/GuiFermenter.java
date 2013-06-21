/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.client;

import monnef.jaffas.technic.block.ContainerFermenter;
import monnef.jaffas.technic.block.TileEntityFermenter;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

public class GuiFermenter extends GuiContainer {
    public static final String GUI_TEXTURE = "/guibreaker.png";

    public TileEntityFermenter tile;

    public GuiFermenter(InventoryPlayer inventoryPlayer,
                        TileEntityFermenter tileEntity) {
        super(new ContainerFermenter(inventoryPlayer, tileEntity));
        tile = tileEntity;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int param1, int param2) {
        fontRenderer.drawString("Fermenter", 8, 6, 4210752);
        fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2,
                                                   int par3) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(GUI_TEXTURE);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

        // arrow
        if (tile.isWorking()) {
            int m = (tile.getWorkMeter() * 24) / tile.getMaxWorkMeter();
            drawTexturedModalRect(x + 84, y + 34, 176, 14, m + 1, 16);
        }
    }
    // x, y, u, v, width, height
}
