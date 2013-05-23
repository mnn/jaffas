/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.client;

import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.food.block.ContainerBoard;
import monnef.jaffas.food.block.TileEntityBoard;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

public class GuiBoard extends GuiContainer {
    public static final String GUIBOARD_TEXTURE = "/guiboard.png";

    TileEntityBoard board;

    public GuiBoard(InventoryPlayer inventoryPlayer,
                    TileEntityBoard tileEntity) {
        //the container is instanciated and passed to the superclass for handling
        super(new ContainerBoard(inventoryPlayer, tileEntity));
        board = tileEntity;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int param1, int param2) {
        //draw text and stuff here
        //the parameters for drawString are: string, x, y, color
        fontRenderer.drawString("Kitchen Board", 8, 6, 4210752);
        //draws "Inventory" or your regional equivalent
        fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);

        if (JaffasFood.debug) {
            String s = String.valueOf(board.getChopTimeScaled(board.chopTime));
            fontRenderer.drawString(s, 100, 10, 4210752);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2,
                                                   int par3) {
        //draw your Gui here, only thing you need to change is the path
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(GUIBOARD_TEXTURE);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

        int var5 = (this.width - this.xSize) / 2;
        int var6 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);

        int var7 = this.board.getChopTimeScaled(24);
        this.drawTexturedModalRect(var5 + 79, var6 + 34, 176, 14, var7 + 1, 16);
    }
}
