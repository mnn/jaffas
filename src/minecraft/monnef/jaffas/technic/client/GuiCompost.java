/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.client;

import monnef.core.utils.GuiHelper;
import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.technic.block.ContainerCompost;
import monnef.jaffas.technic.block.TileEntityCompostCore;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

public class GuiCompost extends GuiContainer {
    public static final String GUI_TEXTURE = "/guicompost.png";
    public static final int TANK_METER_HEIGHT_MAX = 46;

    TileEntityCompostCore core;

    public GuiCompost(InventoryPlayer inventoryPlayer,
                      TileEntityCompostCore tileEntity) {
        super(new ContainerCompost(inventoryPlayer, tileEntity));
        core = tileEntity;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int param1, int param2) {
        fontRenderer.drawString("Compost", 8, 6, 4210752);
        fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);

        if (JaffasFood.debug) {
            //String s = String.valueOf(core.getChopTimeScaled(core.chopTime));
            //fontRenderer.drawString(s, 100, 10, 4210752);
        }
    }

    static int n;

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2,
                                                   int par3) {
        //draw your Gui here, only thing you need to change is the path
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(GUI_TEXTURE);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

        if (JaffasFood.rand.nextFloat() < 0.01) n = JaffasFood.rand.nextInt(TANK_METER_HEIGHT_MAX + 1);
        GuiHelper.drawModalRectFromDown(this, x + 76, y + 20, 176, 31, 16, n, TANK_METER_HEIGHT_MAX);
        //drawTexturedModalRect(x + 76, y + 20 + (46 - n) /*+ n*/, 176, 31 + (46 - n), 16, n);

        // 176 77 - 191 122, scale
        drawTexturedModalRect(x + 76, y + 20, 176, 77, 16, TANK_METER_HEIGHT_MAX);

        //int var7 = this.core.getChopTimeScaled(24);
        //this.drawTexturedModalRect(x + 79, y + 34, 176, 14, var7 + 1, 16);
    }

    // x, y, u, v, width, height
}
