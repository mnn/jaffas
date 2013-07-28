/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.client;

import monnef.core.MonnefCorePlugin;
import monnef.core.utils.GuiHelper;
import monnef.jaffas.food.block.ContainerRipeningBox;
import monnef.jaffas.food.block.TileRipeningBox;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

public class GuiRipeningBox extends GuiContainer {
    public static final String GUIBOARD_TEXTURE = "/guiripingbox.png";
    private static final int STATUS_WIDTH = 16;
    private static final int STATUS_HEIGHT = 4;
    private static final int STATUS_SPACE = 1;
    int x;
    int y;

    TileRipeningBox box;

    public GuiRipeningBox(InventoryPlayer inventoryPlayer,
                          TileRipeningBox tileEntity) {
        super(new ContainerRipeningBox(inventoryPlayer, tileEntity));
        box = tileEntity;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int param1, int param2) {
        fontRenderer.drawString("Ripening Box", 8, 6, 4210752);
        fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);

        /*
        if (JaffasFood.debug) {
            String s = String.valueOf(board.getChopTimeScaled(board.chopTime));
            fontRenderer.drawString(s, 100, 10, 4210752);
        }
        */

        for (int i = 0; i < this.inventorySlots.inventorySlots.size(); i++) {
            if (i < TileRipeningBox.RIPENING_SLOTS) {
                Slot slot = (Slot) this.inventorySlots.inventorySlots.get(i);
                int ripeningStatus = box.getRipeningStatus(i);
                if (MonnefCorePlugin.debugEnv) {
                    String status = String.format("%d", ripeningStatus);
                    fontRenderer.drawString(status, slot.xDisplayPosition, slot.yDisplayPosition, 4210752);
                }
                int stripWidth = (ripeningStatus * STATUS_WIDTH) / TileRipeningBox.MAX_RIPENING_STATUS;
                int yShift = i > 3 ? (16 + STATUS_SPACE * 2) : -STATUS_HEIGHT - STATUS_SPACE;
                GuiHelper.drawTextureModalRect(this, slot.xDisplayPosition, slot.yDisplayPosition + yShift, 10, 20, stripWidth, STATUS_HEIGHT);
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2,
                                                   int par3) {
        x = (width - xSize) / 2;
        y = (height - ySize) / 2;
        //draw your Gui here, only thing you need to change is the path
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(GUIBOARD_TEXTURE);

        this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

        /*
        int var7 = this.board.getChopTimeScaled(24);
        this.drawTexturedModalRect(x + 79, y + 34, 176, 14, var7 + 1, 16);
        */
    }
}
