/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.client;

import monnef.core.MonnefCorePlugin;
import monnef.core.client.GuiContainerMonnefCore;
import monnef.core.utils.GuiHelper;
import monnef.jaffas.food.block.ContainerRipeningBox;
import monnef.jaffas.food.block.TileRipeningBox;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

public class GuiRipeningBox extends GuiContainerMonnefCore {
    private static final int STATUS_WIDTH = 16;
    private static final int STATUS_HEIGHT = 2;
    private static final int STATUS_SPACE = 2;

    TileRipeningBox box;

    public GuiRipeningBox(InventoryPlayer inventoryPlayer,
                          TileRipeningBox tileEntity) {
        super(new ContainerRipeningBox(inventoryPlayer, tileEntity));
        box = tileEntity;
        setBackgroundTexture("guiripingbox.png");
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int param1, int param2) {
        fontRenderer.drawString("Ripening Box", 8, 6, 4210752);
        fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
    }

    private void renderStatusBars() {
        for (int i = 0; i < this.inventorySlots.inventorySlots.size(); i++) {
            if (i < TileRipeningBox.RIPENING_SLOTS) {
                Slot slot = (Slot) this.inventorySlots.inventorySlots.get(i);
                int ripeningStatus = box.getRipeningStatus(i);
                int xPos = slot.xDisplayPosition + x;
                int yPos = slot.yDisplayPosition + y;
                if (MonnefCorePlugin.debugEnv) {
                    String status = String.format("%d", ripeningStatus);
                    fontRenderer.drawString(status, xPos, yPos, 4210752);
                    bindBackgroundTexture();
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                }
                int stripWidth = (ripeningStatus * STATUS_WIDTH) / TileRipeningBox.MAX_RIPENING_STATUS;
                int yShift = i > 3 ? (16 + STATUS_SPACE * 2) : -STATUS_HEIGHT - STATUS_SPACE;
                GuiHelper.drawTextureModalRect(this, xPos, yPos + yShift - 1, 176, 31, stripWidth, STATUS_HEIGHT);
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2,
                                                   int par3) {
        super.drawGuiContainerBackgroundLayer(par1, par2, par3);
        renderStatusBars();
    }
}
