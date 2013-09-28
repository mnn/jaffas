/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.client;

import monnef.core.client.GuiContainerJaffas;
import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.food.block.ContainerBoard;
import monnef.jaffas.food.block.TileBoard;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;

public class GuiBoard extends GuiContainerJaffas {
    public static final String GUIBOARD_TEXTURE = "guiboard.png";

    TileBoard board;

    public GuiBoard(InventoryPlayer inventoryPlayer,
                    TileBoard tileEntity) {
        super(new ContainerBoard(inventoryPlayer, tileEntity));
        board = tileEntity;
        setBackgroundTexture(GUIBOARD_TEXTURE);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int param1, int param2) {
        fontRenderer.drawString("Kitchen Board", 8, 6, 4210752);
        fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);

        if (JaffasFood.debug) {
            String s = String.valueOf(board.getChopTimeScaled(board.chopTime));
            fontRenderer.drawString(s, 100, 10, 4210752);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        super.drawGuiContainerBackgroundLayer(par1, par2, par3);

        int progress = this.board.getChopTimeScaled(24);
        this.drawTexturedModalRect(x + 79, y + 34, 176, 14, progress + 1, 16);
    }
}
